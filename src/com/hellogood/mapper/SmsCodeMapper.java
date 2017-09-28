package com.hellogood.mapper;

import com.hellogood.domain.SmsCode;
import com.hellogood.domain.SmsCodeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsCodeMapper {
    int deleteByExample(SmsCodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsCode record);

    int insertSelective(SmsCode record);

    List<SmsCode> selectByExample(SmsCodeExample example);

    SmsCode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmsCode record, @Param("example") SmsCodeExample example);

    int updateByExample(@Param("record") SmsCode record, @Param("example") SmsCodeExample example);

    int updateByPrimaryKeySelective(SmsCode record);

    int updateByPrimaryKey(SmsCode record);
    
    List<SmsCode> getListByPhone(String phone);
}