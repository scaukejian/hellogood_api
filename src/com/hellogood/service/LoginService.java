package com.hellogood.service;

import com.hellogood.constant.Code;
import com.hellogood.constant.TokenConstants;
import com.hellogood.domain.*;
import com.hellogood.exception.BusinessException;
import com.hellogood.exception.UserRegisterOperateException;
import com.hellogood.http.vo.LoginVO;
import com.hellogood.http.vo.RegisterVO;
import com.hellogood.mapper.LoginMapper;
import com.hellogood.utils.HttpClientUtil;
import com.hellogood.utils.StaticFileUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 登录Service
 *
 * @author kejian
 */
@Service
public class LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private LoginRecordsService loginRecordsService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    UserCacheManager userCacheManager;
    @Autowired
    ThirdPartyLoginService thirdPartyLoginService;

    public static Logger logger = LoggerFactory.getLogger(LoginService.class);

    /**
     * 保存
     * @param login
     */
    public void save(Login login) {
        try {
            loginMapper.insert(login);
        } catch (DuplicateKeyException e) {
            logger.info("用户注册时重复提交");
            throw new UserRegisterOperateException("您已注册");
        }
    }

    /**
     * 保存
     * @param login
     */
    public User saveAndUpdate(Login login, RegisterVO register) {
        LoginVO loginVO = new LoginVO();
        loginVO.domain2Vo(login);
        loginVO.setMobile(login.getPhone());
        loginVO.setClientType(register.getClientType());
        loginVO.setLoginIp(register.getLoginIp());
        loginVO.setLoginAddr(register.getLoginAddr());
        User user = this.createUser(loginVO);

        //注册时绑定第三方账号
        if (register.getType() != null && register.getOpenId() != null) {
            logger.info("注册时绑定第三方账号类型：" + register.getType() + ",手机号：" + user.getPhone() + ",openId:" + register.getOpenId());
            ThirdPartyLogin thirdPartyLogin = new ThirdPartyLogin(user.getPhone(), register.getOpenId(), register.getType(), null);
            thirdPartyLoginService.insert(thirdPartyLogin);
        }
        return user;

    }

    /**
     * 查询用户
     * @param example
     * @return
     */
    public Login getUser(LoginExample example) {
        List<Login> login = loginMapper.selectByExample(example);
        if (login != null && login.size() > 0)
            return login.get(0);
        return null;
    }

    public Login getLoginByPhone(String phone) {
        Login login = null;
        LoginExample example = new LoginExample();
        example.createCriteria().andPhoneEqualTo(phone);
        List<Login> list = loginMapper.selectByExample(example);
        if (!list.isEmpty()) {
            login = list.get(0);
        }
        return login;
    }

    public Login getLoginByUserId(Integer userId) {
        LoginExample example = new LoginExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<Login> list = loginMapper.selectByExample(example);
        if (list.isEmpty()) {
            throw new BusinessException("获取用户登录信息失败");
        }
        return list.get(0);

    }
    /**
     * 更新
     * @param login
     */
    public void updateLogin(Login login) {
        loginMapper.updateByPrimaryKey(login);
    }

    public void updateBootUpTime(LoginVO loginVO) {
        if (loginVO.getUserId() != null && loginVO.getUserId() > 0) {
            Login login = this.getLoginByUserId(loginVO.getUserId());
            login.setLastBootUpTime(new Date());
            loginMapper.updateByPrimaryKey(login);
        }

    }

    /**
     * 更新
     * @param loginVO
     */
    public void updateLoginByUserId(LoginVO loginVO) {
        Login login = this.getLoginByUserId(loginVO.getUserId());
        if (login != null) {
            login.setApkVersion(loginVO.getApkVersion());
            login.setClientInfo(loginVO.getClientInfo());
            login.setLastBootUpTime(new Date());
            loginMapper.updateByPrimaryKey(login);
            //更新缓存
            userCacheManager.updateApkVersion(loginVO.getUserId(), loginVO.getApkVersion());
        }
    }

    /**
     * 更新并增加登陆记录表并返回新生成的token
     * @param login
     */
    public String updateLoginAndRecord(Login login, LoginVO loginVO) {
        login.setLastLogintime(new Date());
        this.updateLogin(login);
        loginRecordsService.add(login.getId(), loginVO);
        return tokenService.insertOrUpdate(login.getUserId(), TokenConstants.TOKEN_INVALID_LOGIN_OTHER_DEVICE);
    }

    /**
     * 是否存在用户
     * @param phone
     * @return
     */
    public boolean isExist(String phone) {
        LoginExample example = new LoginExample();
        example.createCriteria().andPhoneEqualTo(phone);
        List<Login> loginList = loginMapper.selectByExample(example);
        if (!loginList.isEmpty()) return true;
        return false;
    }

    /**
     * 网页授权，根据code获取unionid
     * @param code
     * @return
     */
    public String getUnionIdByCode(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        StringBuffer sb = new StringBuffer();
        sb.append("appid=" + StaticFileUtil.getProperty("webLicense", "appid"));
        sb.append("&secret=" + StaticFileUtil.getProperty("webLicense", "secret"));
        sb.append("&code=" + code);
        sb.append("&grant_type=authorization_code");
        String respone = HttpClientUtil.sendGet(url, sb.toString());
        JSONObject data = new JSONObject(respone);
        if (data.isNull("unionid") || data.get("unionid") == null) {
            throw new BusinessException("网页授权返回参数错误");
        }
        return (String) data.get("unionid");
    }

    /**
     * 绑定第三方没有账号，生成一个
     * @param loginVO
     */
    public User createUser(LoginVO loginVO) {
        //生成用户信息
        User user = new User();
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setPhone(loginVO.getMobile());
        user.setValidStatus(Code.STATUS_VALID);
        userService.add(user);

        //生成登录信息
        Login login = new Login();
        loginVO.vo2Domain(login);
        login.setPhone(loginVO.getMobile());
        login.setPhoneClient(loginVO.getClientType());
        login.setUserId(user.getId());
        login.setCreateTime(new Date());
        if (loginVO.getPassword() == null)
            loginVO.setPassword(DigestUtils.md5Hex(loginVO.getMobile().substring(5).concat(loginVO.getMobile())));
        login.setPassword(loginVO.getPassword());
        login.setLastLogintime(new Date());
        this.save(login);// 保存登录表

        loginRecordsService.add(login.getId(), loginVO);

        return user;
    }
}
