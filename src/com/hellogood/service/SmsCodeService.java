package com.hellogood.service;

import com.hellogood.domain.SmsCode;
import com.hellogood.domain.SmsCodeExample;
import com.hellogood.enumeration.MessageType;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.vo.SmsCodeVO;
import com.hellogood.mapper.SmsCodeMapper;
import com.hellogood.service.redis.RedisCacheManger;
import com.google.gson.Gson;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 短信发送Service
 *
 * @author kejian
 */
@Service
public class SmsCodeService {

    @Autowired
    private SmsCodeMapper smsCodeMapper;
    @Autowired
    private RedisCacheManger redisCacheManger;
    /**
     * 发布渠道
     */
    public static final String PUSH_CHANNEL = "message";

    /**
     * json转换类
     */
    public static Gson gson = new Gson();


    public static org.slf4j.Logger logger = LoggerFactory.getLogger(SmsCodeService.class);

    /**
     * 保存
     * @param smsCode
     * @return
     */
    public int save(SmsCode smsCode) {
        return smsCodeMapper.insertSelective(smsCode);
    }

    /**
     * 保存
     * @param smsCode
     * @return
     */
    public void saveToRedis(SmsCode smsCode, String content) {
        this.save(smsCode);
        SmsCodeVO vo = new SmsCodeVO();
        vo.domain2VO(smsCode);
        vo.setType(MessageType.SMS_CODE.getCode());
        vo.setContent(content);
        //发布消息
        redisCacheManger.publish(PUSH_CHANNEL, gson.toJson(vo));
    }

    /**
     * 查询，返回一个实体
     * @param smsExample
     * @return
     */
    public SmsCode select(SmsCodeExample smsExample) {
        List<SmsCode> list = smsCodeMapper.selectByExample(smsExample);
        if (list.isEmpty()) throw new BusinessException("获取验证码失败");
        return list.get(list.size() - 1);
    }


    /**
     * 查询，返回一个实体
     * @param phone
     * @return
     */
    public SmsCode getListByPhone(String phone) {
        List<SmsCode> list = smsCodeMapper.getListByPhone(phone);
        if (list.isEmpty()) throw new BusinessException("获取验证码失败");
        return list.get(list.size() - 1);
    }

    /**
     * 判断一分钟一个号码是否超过5个，超过就抛出提示
     * @param phone
     * @return
     */
    public void validOneMinuteLimit(String phone) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, -1);
        System.out.println(cal.getTime().getTime());
        SmsCodeExample example = new SmsCodeExample();
        example.createCriteria().andPhoneEqualTo(phone).andTimestampGreaterThan(cal.getTime().getTime());
        example.setOrderByClause("id desc");
        List<SmsCode> list = smsCodeMapper.selectByExample(example);
        if (list.size() > 5) throw new BusinessException("短信发送过于频繁,请过段时间再使用");
    }

    /**
     * 判断验证码是否正确，10分钟内有效
     * @param mobile
     * @param code
     * @return
     */
    public Boolean isExist(String mobile, String code) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, -10);
        System.out.println(cal.getTime().getTime());
        SmsCodeExample example = new SmsCodeExample();
        example.createCriteria().andPhoneEqualTo(mobile)
                .andCodeEqualTo(code).andTimestampGreaterThan(cal.getTime().getTime());
        List<SmsCode> list = smsCodeMapper.selectByExample(example);
        if (list.isEmpty()) return false;
        return true;
    }

}
