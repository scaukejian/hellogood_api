package com.hellogood.mapper;

import com.hellogood.domain.BootUp;
import com.hellogood.domain.BootUpExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BootUpMapper {
    int deleteByExample(BootUpExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BootUp record);

    int insertSelective(BootUp record);

    List<BootUp> selectByExample(BootUpExample example);

    BootUp selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BootUp record, @Param("example") BootUpExample example);

    int updateByExample(@Param("record") BootUp record, @Param("example") BootUpExample example);

    int updateByPrimaryKeySelective(BootUp record);

    int updateByPrimaryKey(BootUp record);
}