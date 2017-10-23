package com.hellogood.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hellogood.constant.Code;
import com.hellogood.domain.Note;
import com.hellogood.domain.NoteExample;
import com.hellogood.domain.User;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.vo.NoteVO;
import com.hellogood.mapper.NoteMapper;
import com.hellogood.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

/**
 * NoteService
 * Create by kejian
 */
@Service
@Transactional
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;
    @Autowired
    private UserMapper userMapper;

    public static List<String> typeList = Arrays.asList("日","周","月","季","年");

    private void checkCommon(NoteVO vo){
        if (StringUtils.isBlank(vo.getPhoneUniqueCode()))
            throw new BusinessException("请先授权APP获取系统权限");
        if (StringUtils.isBlank(vo.getType()))
            throw new BusinessException("操作失败: 请选择计划类型");

        if (!typeList.contains(vo.getType()))
            throw new BusinessException("操作失败: 计划类型只能为日、周、月、季、年");

        if (StringUtils.isBlank(vo.getContent()))
            throw new BusinessException("操作失败: 计划内容不能为空");
        if (vo.getContent().length() > 5000)
            throw new BusinessException("操作失败: 内容长度不能大于5000个字符");
    }

    /**
     * 新增
     * @param vo
     */
    public void add(NoteVO vo) {
        checkCommon(vo);
        if (StringUtils.isBlank(vo.getColor())) vo.setColor("#FFF");
        Note domain = new Note();
        vo.vo2Domain(domain);
        domain.setCreateTime(new Date());
        domain.setUpdateTime(new Date());
        domain.setFinish(Code.STATUS_INVALID);//未完成
        domain.setValidStatus(Code.STATUS_VALID);//有效
        domain.setDisplay(Code.STATUS_VALID);//显示
        domain.setTop(Code.STATUS_INVALID);//非置顶
        noteMapper.insert(domain);
    }

    /**
     * 检查参数并返回计划
     * @param id
     * @param status
     * @return
     */
    public Note checkAndReturnNote(Integer id, Integer status) {
        if (id == null) throw new BusinessException("请选择要操作的记录");
        if (status == null) throw new BusinessException("状态参数有误");
        Note note = noteMapper.selectByPrimaryKey(id);
        if (note == null) throw new BusinessException("参数有误");
        return note;
    }

    /**
     * 放入回收站 / 移出回收站
     * @param id
     * @param status
     */
    public void recycle(Integer id, Integer status) {
        Note note = checkAndReturnNote(id, status);
        note.setDisplay(status);
        noteMapper.updateByPrimaryKeySelective(note);
    }

    /**
     * 设置是否完成
     * @param id
     * @param status
     */
    public void finish(Integer id, Integer status) {
        Note note = checkAndReturnNote(id, status);
        note.setFinish(status);
        noteMapper.updateByPrimaryKeySelective(note);
    }

    /**
     * 初始化
     */
    public void initFinish(String type) {
        NoteExample example = new NoteExample();
        NoteExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(type);
        List<Note> noteList = noteMapper.selectByExample(example);
        if (noteList.isEmpty()) return;
        for (Note note : noteList) {
            note.setFinish(Code.STATUS_INVALID);
            noteMapper.updateByPrimaryKeySelective(note);
        }
    }

    /**
     * 设置是否置顶/收藏
     * @param id
     * @param status
     */
    public void setTop(Integer id, Integer status) {
        Note note = checkAndReturnNote(id, status);
        note.setTop(status);
        noteMapper.updateByPrimaryKeySelective(note);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(Integer id) {
        if (id == null) throw new BusinessException("请选择要删除的记录");
        Note note = noteMapper.selectByPrimaryKey(id);
        if (note == null) throw new BusinessException("参数有误");
        note.setValidStatus(Code.STATUS_INVALID);
        noteMapper.updateByPrimaryKeySelective(note);
    }

    /**
     * 批量删除
     * @param ids
     */
    public void delete(String ids) {
        if (StringUtils.isBlank(ids)) throw new BusinessException("请选择要删除的记录");
        String[] idStrArr = ids.split(",");
        for (String idStr : idStrArr) {
            Integer noteId = Integer.parseInt(idStr);
            Note note = noteMapper.selectByPrimaryKey(noteId);
            if (note == null) continue;
            note.setValidStatus(Code.STATUS_INVALID);
            noteMapper.updateByPrimaryKeySelective(note);
        }
    }

    /**
     * 修改
     * @param vo
     */
    public void update(NoteVO vo) {
        checkCommon(vo);
        Note domain = new Note();
        vo.vo2Domain(domain);
        domain.setUpdateTime(new Date());
        noteMapper.updateByPrimaryKeySelective(domain);
    }
    /**
     * 获取数据
     * @param id
     * @return
     */
    public Note getNote(Integer id) {
        return noteMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取详情
     * @param id
     * @return
     */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public NoteVO get(Integer id) {
	    if (id == null || id == 0) throw new BusinessException("参数id不能为空");
        NoteVO vo = new NoteVO();
        if (id == null)  return vo;
        Note domain = getNote(id);
        vo.domain2Vo(domain);
        supplement(vo);
        return vo;
    }

    /**
     * 分页查询
     * @param queryVo
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public PageInfo pageQuery(NoteVO queryVo) {
        if(StringUtils.isBlank(queryVo.getPhoneUniqueCode()))
            throw new BusinessException("请先授权APP获取系统权限");
        if (StringUtils.isBlank(queryVo.getType()))
            throw new BusinessException("计划类型不能为空");
        if (!typeList.contains(queryVo.getType()))
            throw new BusinessException("操作失败: 计划类型只能为日、周、月、季、年");
        NoteExample example = new NoteExample();
        NoteExample.Criteria criteria = example.createCriteria();
        if(queryVo.getUserId() != null && queryVo.getUserId() != 0) {
            criteria.andUserIdEqualTo(queryVo.getUserId());
        } else {
            criteria.andPhoneUniqueCodeLike(MessageFormat.format("%{0}%", queryVo.getPhoneUniqueCode()));
        }
        criteria.andTypeEqualTo(queryVo.getType());
        criteria.andDisplayEqualTo(Code.STATUS_VALID);
        example.setOrderByClause(" top desc, update_time desc");
        PageHelper.startPage(queryVo.getPage(), queryVo.getPageSize());
        List<Note> list = noteMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        List<NoteVO> voList = domainList2VoList(list);
        pageInfo.getList().clear();
        pageInfo.getList().addAll(voList);
        return pageInfo;
    }

    private List<NoteVO> domainList2VoList(List<Note> domainList) {
        List<NoteVO> voList = new ArrayList<>(domainList.size());
        for (Note domain : domainList) {
            NoteVO vo = new NoteVO();
            vo.domain2Vo(domain);
            supplement(vo);
            voList.add(vo);
        }
        return voList;
    }

    public void supplement(NoteVO vo) {
        if (vo.getUserId() != null) {
            User user = userMapper.selectByPrimaryKey(vo.getUserId());
            if (user != null) {
                vo.setUserCode(user.getUserCode());
                vo.setUserName(user.getUserName());
                vo.setPhone(user.getPhone());
            }
        }
    }

}
