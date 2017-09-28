package com.hellogood.service;

import com.hellogood.domain.*;
import com.hellogood.exception.BusinessException;
import com.hellogood.exception.UserRegisterOperateException;
import com.hellogood.http.vo.UserVO;
import com.hellogood.service.redis.RedisCacheManger;
import com.hellogood.utils.DateUtil;
import com.hellogood.utils.RegexUtils;
import com.hellogood.utils.StringUtil;
import com.hellogood.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户资料Service
 */
@Service
public class UserService {
    public static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPhotoService userPhotoService;
    @Autowired
    private BaseDataService baseDataService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisCacheManger redisCacheManger;
    @Autowired
    AreaService areaService;

    /**
     * 保存
     * @param user
     */
    public void update(User user) {
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 保存
     * @param user
     */
    public void updateByDomain(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 保存
     * @param user
     */
    public void updateSign(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void save(UserVO userVO) {
        checkUserVO(userVO);
        User user = new User();
        User oldUser = getUser(userVO.getId());
        userVO.vo2Domain(user);
        if (StringUtils.isBlank(userVO.getSex())) {
            user.setSex(oldUser.getSex());
        }
        //省市
        if (StringUtils.isBlank(userVO.getLiveCity())) {
            user.setLiveCity(oldUser.getLiveCity());
            userVO.setLiveCity(oldUser.getLiveCity());
        }
        if (StringUtils.isBlank(userVO.getLiveProvince())) {
            user.setLiveProvince(areaService.getProviceByCityName(userVO.getLiveCity()));
        } else {
            user.setLiveProvince(userVO.getLiveProvince());
        }
        this.update(user);
    }

    private void checkUserVO(UserVO userVO) {
        tokenService.checkUserToken(userVO.getToken(), userVO.getId());
        if (userVO.getId() == null)
            throw new BusinessException("用户id不能为空");
        if (!RegexUtils.isUsername(userVO.getUserCode()))
            throw new BusinessException(RegexUtils.USERNAME_MSG);
        if (StringUtils.isBlank(userVO.getUserName()))
            throw new BusinessException("姓名不能为空");
        if (userVO.getUserName().length() > 32)
            throw new BusinessException("姓名长度不能大于32个字符");
        if (!RegexUtils.isMobileExact(userVO.getPhone()))
            throw new BusinessException(RegexUtils.PHONE_MSG);
        if (StringUtils.isBlank(userVO.getSex()))
            throw new BusinessException("请选择性别");
        if (userVO.getBirthday() == null)
            throw new BusinessException("请输入生日日期");
        if (userVO.getAge() != null && !RegexUtils.isPositiveInteger(String.valueOf(userVO.getAge())))
            throw new BusinessException("年龄"+RegexUtils.POSITIVE_INTEGER_MSG);
        if (StringUtils.isBlank(userVO.getWeixinName()))
            throw new BusinessException("微信不能为空");
        if (userVO.getWeixinName().length() > 50)
            throw new BusinessException("微信长度不能大于50个字符");
        if (userVO.getHeight() != null && !RegexUtils.isPositiveInteger(String.valueOf(userVO.getHeight())))
            throw new BusinessException("身高"+RegexUtils.POSITIVE_INTEGER_MSG);
        if (StringUtils.isNotBlank(userVO.getRemark()) && userVO.getRemark().length() > 255)
            throw new BusinessException("操作失败: 备注长度不能大于255个字符");
        if (StringUtils.isNotBlank(userVO.getDegree()) && userVO.getDegree().length() > 20)
            throw new BusinessException("操作失败: 学历长度不能大于20个字符");
        if (StringUtils.isNotBlank(userVO.getConstellation()) && userVO.getConstellation().length() > 10)
            throw new BusinessException("操作失败: 星座长度不能大于10个字符");
        if (StringUtils.isNotBlank(userVO.getSchool()) && userVO.getSchool().length() > 64)
            throw new BusinessException("操作失败: 学校长度不能大于64个字符");
        if (StringUtils.isNotBlank(userVO.getCompany()) && userVO.getCompany().length() > 200)
            throw new BusinessException("操作失败: 公司长度不能大于200个字符");
        if (StringUtils.isNotBlank(userVO.getJob()) && userVO.getJob().length() > 32)
            throw new BusinessException("操作失败: 职位长度不能大于32个字符");
        if (StringUtils.isNotBlank(userVO.getCharacteristicSignature()) && userVO.getCharacteristicSignature().length() > 255)
            throw new BusinessException("操作失败: 个性签名长度不能大于255个字符");
        if (StringUtils.isNotBlank(userVO.getQq()) && userVO.getQq().length() > 20)
            throw new BusinessException("操作失败: QQ长度不能大于20个字符");
        if (StringUtils.isNotBlank(userVO.getEmail()) && userVO.getEmail().length() > 50)
            throw new BusinessException("操作失败: Email长度不能大于50个字符");
    }

    /**
     * 增加
     * @param user
     */
    public void add(User user) {
        user.setUpdateTime(new Date());
        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            logger.info("用户注册时重复提交");
            throw new UserRegisterOperateException("您已注册");
        }
        user.setUserCode(StringUtil.generateNumCode(user.getId()));
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 判断易悦号是否存在
     * @param userCode
     * @return
     */
    public Boolean isExistUser(String userCode) {
        User user = null;
        UserExample example = new UserExample();
        example.createCriteria().andUserCodeEqualTo(userCode);
        List<User> list = userMapper.selectByExample(example);
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 查询用户
     * @param phone
     * @return
     */
    public User getUserByPhone(String phone) {
        User user = null;
        UserExample example = new UserExample();
        example.createCriteria().andPhoneEqualTo(phone);
        List<User> list = userMapper.selectByExample(example);
        if (!list.isEmpty()) {
            user = list.get(0);
        }
        return user;
    }

    /**
     * 判断手机号是否存在
     * @param phone
     * @return
     */
    public Boolean isExistByPhone(String phone) {
        UserExample example = new UserExample();
        example.createCriteria().andPhoneEqualTo(phone);
        List<User> list = userMapper.selectByExample(example);
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 查询用户详情
     * @param userId
     * @return
     */
    public User getUser(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 获取用户称呼
     * @param userId
     * @return
     */
    private String getUserName(Integer userId) {
        User user = getUser(userId);
        return user.getUserName();
    }

    /**
     * 查询用户集合
     * @param example
     * @return
     */
    public List<User> getUserList(UserExample example) {
        return userMapper.selectByExample(example);
    }


    /**
     * 返回用户自己的数据
     * @param userId
     * @return
     */
    public Map<String, Object> getMyData(Integer userId) {
        Map<String, Object> userDetail = new HashMap<String, Object>();
        User user = this.getUser(userId);
        user.setAge(DateUtil.getAge(user.getBirthday()));
        if (!StringUtils.isEmpty(user.getCharacteristicSignature()))
            user.setCharacteristicSignature(new String(org.apache.commons.codec.binary.Base64.decodeBase64(user.getCharacteristicSignature())));
        //去掉头像，使用形象照
        UserVO userVO = new UserVO();
        userVO.domain2Vo(user);
        userVO.setHeadPhotoName(userPhotoService.getUserPhotoName(user.getId(), false));
        userDetail.put("user", DateUtil.object2MapDateFormat(userVO));
        return userDetail;
    }

    public List<Integer> getAllUserIds() {
        List<User> list = userMapper.selectByExample(new UserExample());
        return list.stream().map(user -> user.getId()).collect(Collectors.toList());
    }

    /**
     * 通过userCode查找对应用户
     * @param userCode
     * @return
     */
    public User getByUserCode(String userCode) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUserCodeEqualTo(userCode);
        List<User> users = userMapper.selectByExample(example);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

}
