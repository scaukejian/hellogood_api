package com.hellogood.mapper;

import com.hellogood.domain.Note;
import com.hellogood.domain.NoteExample;
import java.util.List;

import com.hellogood.http.vo.NoteVO;
import org.apache.ibatis.annotations.Param;

public interface NoteMapper {
    int deleteByExample(NoteExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Note record);

    int insertSelective(Note record);

    List<Note> selectByExample(NoteExample example);

    Note selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Note record, @Param("example") NoteExample example);

    int updateByExample(@Param("record") Note record, @Param("example") NoteExample example);

    int updateByPrimaryKeySelective(Note record);

    int updateByPrimaryKey(Note record);

    /**
     * 查找用户id未完成的type类型计划数
     * @param type
     * @return
     */
    List<NoteVO> getUserIdAndCountMap(@Param("type") String type);

    /**
     * 查找设备未完成的type类型计划数
     * @param type
     * @return
     */
    List<NoteVO> getPhoneUniqueCodeAndCountMap(@Param("type") String type);
}