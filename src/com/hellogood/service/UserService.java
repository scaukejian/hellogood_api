package com.hellogood.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.hellogood.constant.Code;
import com.hellogood.domain.*;
import com.hellogood.exception.BusinessException;
import com.hellogood.exception.UserRegisterOperateException;
import com.hellogood.http.vo.MinaUserVO;
import com.hellogood.http.vo.NoteVO;
import com.hellogood.http.vo.UserVO;
import com.hellogood.utils.*;
import com.hellogood.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

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
    private TokenService tokenService;
    @Autowired
    AreaService areaService;
    @Autowired
    LoginService loginService;
    @Autowired
    LoginRecordsService loginRecordsService;
    @Autowired
    NoteService noteService;

    public static String STOREPATH = StaticFileUtil.getProperty("fileSystem", "storagePath");
    /**
     * 保存
     * @param user
     */
    public void update(User user) {
        user.setUpdateTime(new Date());
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

    /**
     * 保存小程序用户
     * @param userVO
     */
    public MinaUserVO saveMinaUser(UserVO userVO) {
        checkUserVO(userVO);
        User user = new User();
        userVO.vo2Domain(user);
        this.update(user);
        user = userMapper.selectByPrimaryKey(user.getId());
        MinaUserVO minaUserVO = new MinaUserVO();
        minaUserVO.domain2Vo(user);
        minaUserVO.setUserId(user.getId());
        return minaUserVO;
    }

    private void checkUserVO(UserVO userVO) {
        if (userVO.getId() == null)
            throw new BusinessException("用户id不能为空");
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
        if (StringUtils.isNotBlank(userVO.getWeixinName()) && userVO.getWeixinName().length() > 50)
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
        User user = userMapper.selectByPrimaryKey(userVO.getId());
        if (!StringUtils.equals(user.getPhone(), userVO.getPhone())) {
            User userTemp = getUserByPhone(userVO.getPhone());
            if (userTemp != null)  throw new BusinessException("操作失败: 该号码已经被绑定其他的小程序，请更换其他号码");
        }
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
     * 查询用户
     * @param phone
     * @return
     */
    public User getUserByPhone(String phone) {
        User user = null;
        UserExample example = new UserExample();
        example.createCriteria().andPhoneEqualTo(phone);
        List<User> list = userMapper.selectByExample(example);
        if (!list.isEmpty()) user = list.get(0);
        return user;
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
     * 返回用户自己的数据
     * @param userId
     * @return
     */
    public Map<String, Object> getMyData(Integer userId) {
        Map<String, Object> userDetail = new HashMap<String, Object>();
        User user = this.getUser(userId);
        user.setAge(DateUtil.getAge(user.getBirthday()));
        //去掉头像，使用形象照
        UserVO userVO = new UserVO();
        userVO.domain2Vo(user);
        userVO.setHeadPhotoName(userPhotoService.getUserPhotoName(user.getId(), false));
        userDetail.put("user", DateUtil.object2MapDateFormat(userVO));
        return userDetail;
    }

    /**
     * 根据openId行级锁获取用户资料 SELECT * FROM User FOR UPDATE 的作用是在查询用户的时候防止修改或删除
     * @param openId
     * @return
     */
    public User getUserByOpenIdForUpdate(String openId){
        List<User> list = userMapper.getUserByOpenIdForUpdate(openId);
        if(list.isEmpty())
            return null;
        return list.get(0);
    }

    /**
     * 小程序新增用户
     * @param openId
     * @param name
     * @param imgUrl
     * @return
     */
    public User insert(String openId, String name, String imgUrl) {
        logger.info("openId="+openId + ",nickName=" + name + ", imgurl=" + imgUrl);
        User user = new User();
        user.setCreateTime(new Date());
        user.setImgUrl(imgUrl);
        user.setUserName(EmojiUtil.filterEmoji(name));
        user.setOpenId(openId);
        user.setValidStatus(Code.STATUS_VALID);
        add(user);

        //生成登录信息
        Login login = new Login();
        login.setUserId(user.getId());
        login.setCreateTime(new Date());
        login.setLastLogintime(new Date());
        loginService.save(login);// 保存登录表

        loginRecordsService.addMiniRecord(login.getId());

        //为每个用户新增一条提示引导计划
        NoteVO vo = new NoteVO();
        vo.setMini(Code.STATUS_VALID);
        vo.setFolderId(Code.FOLDER_DAY_ID);
        vo.setUserId(user.getId());
        vo.setContent("嗨，欢迎来到橙子计划，请点击右下角的加号开始计划之旅吧，右滑可删了我哟~");
        noteService.add(vo);

        return user;
    }

    /**
     * 获取返回的userVO
     * @param user
     * @return
     */
    private UserVO getReturnUserVO(User user){
        UserVO vo = new UserVO();
        vo.domain2Vo(user);
        return vo;
    }
    /**
     * 获取我的资料
     * @param openId
     * @return
     */
    public UserVO getMyUserInfo(String openId) {
        if(StringUtils.isBlank(openId))
            throw new BusinessException("获取资料失败");
        User user = this.getUserByOpenId(openId);
        if(user == null)
            throw new BusinessException("获取资料失败");
        return this.getReturnUserVO(user);
    }
    /**
     * 根据openId 获取用户
     * @param openId
     * @return
     */
    public User getUserByOpenId(String openId){
        UserExample example = new UserExample();
        example.createCriteria().andOpenIdEqualTo(openId).andValidStatusEqualTo(Code.STATUS_VALID);
        List<User> list = userMapper.selectByExample(example);
        if(list.isEmpty())
            return null;
        return list.get(0);
    }
    /**
     * 获取我的二维码
     * @param openId
     * @return
     */
    public String getQRCodeUrl(String openId) {
        if(StringUtils.isBlank(openId))
            throw new BusinessException("获取二维码失败");
        User user = this.getUserByOpenId(openId);
        if(user == null)
            throw new BusinessException("获取二维码失败");
        return getQRCodeUrl(user);
    }

    /**
     * 生成二维码
     * @author kejian
     */
    public String getQRCodeUrl(User user){
        if (user == null || user.getId() == null || StringUtils.isBlank(user.getPhone())) return null;
        String fileName = "qrcode_" + user.getId() + ".jpg";
        File localFile = new File(STOREPATH +"qrcode/"+ fileName);
        if (!localFile.exists()) {
            String name = user.getUserName(); // 姓名
            String telephone = user.getPhone(); // 电话
            String job = StringUtils.isNotBlank(user.getJob()) ? user.getJob() : ""; // 职位
            String company = StringUtils.isNotBlank(user.getCompany()) ? user.getCompany() : ""; // 公司
            String email = StringUtils.isNotBlank(user.getEmail()) ? user.getEmail() : ""; // 邮箱
            String characteristicSignature = StringUtils.isNotBlank(user.getCharacteristicSignature()) ? user.getCharacteristicSignature() : ""; // 简介

            StringBuffer sb = new StringBuffer();
            sb.append("BEGIN:VCARD\n");
            sb.append("N:").append(name).append("\n");
            sb.append("TITLE:").append(job).append("\n");
            sb.append("TEL:").append(telephone).append("\n");
            sb.append("ORG:").append(company).append("\n");
            sb.append("EMAIL:").append(email).append("\n");
            sb.append("NOTE:").append(characteristicSignature).append("\n");
            sb.append("END:VCARD");
            String content = sb.toString();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            try {
                // 文件命名 生成文件夹名+UUID+后缀名
                BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);
                MatrixToImageWriter.writeToFile(bitMatrix, "jpg", localFile);
                logger.info("生成名片二维码完成！");
            } catch (Exception e) {
                throw new BusinessException("生成名片二维码失败！");
            }
        }
        return fileName;
    }
}
