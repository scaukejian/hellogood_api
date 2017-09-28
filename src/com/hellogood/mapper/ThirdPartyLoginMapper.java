package com.hellogood.mapper;

import com.hellogood.domain.ThirdPartyLogin;
import com.hellogood.domain.ThirdPartyLoginExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ThirdPartyLoginMapper {
    int deleteByExample(ThirdPartyLoginExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ThirdPartyLogin record);

    int insertSelective(ThirdPartyLogin record);

    List<ThirdPartyLogin> selectByExample(ThirdPartyLoginExample example);

    ThirdPartyLogin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ThirdPartyLogin record, @Param("example") ThirdPartyLoginExample example);

    int updateByExample(@Param("record") ThirdPartyLogin record, @Param("example") ThirdPartyLoginExample example);

    int updateByPrimaryKeySelective(ThirdPartyLogin record);

    int updateByPrimaryKey(ThirdPartyLogin record);
}