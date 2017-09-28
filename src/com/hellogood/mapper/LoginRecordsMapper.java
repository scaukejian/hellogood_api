package com.hellogood.mapper;

import com.hellogood.domain.LoginRecords;
import com.hellogood.domain.LoginRecordsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LoginRecordsMapper {
    int deleteByExample(LoginRecordsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LoginRecords record);

    int insertSelective(LoginRecords record);

    List<LoginRecords> selectByExample(LoginRecordsExample example);

    LoginRecords selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LoginRecords record, @Param("example") LoginRecordsExample example);

    int updateByExample(@Param("record") LoginRecords record, @Param("example") LoginRecordsExample example);

    int updateByPrimaryKeySelective(LoginRecords record);

    int updateByPrimaryKey(LoginRecords record);
}