package com.hellogood.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hellogood.constant.Code;
import com.hellogood.domain.Note;
import com.hellogood.domain.NoteExample;
import com.hellogood.domain.User;
import com.hellogood.domain.UserExample;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.vo.NoteVO;
import com.hellogood.mapper.NoteMapper;
import com.hellogood.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    private void checkCommon(NoteVO vo){
        if (StringUtils.isBlank(vo.getPhoneUniqueCode()))
            throw new BusinessException("请先授权APP获取系统权限");
        if (StringUtils.isBlank(vo.getType()))
            throw new BusinessException("操作失败: 请选择便签类型");
        if (StringUtils.isBlank(vo.getContent()))
            throw new BusinessException("操作失败: 便签内容不能为空");
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
        domain.setValidStatus(Code.STATUS_VALID);
        domain.setDisplay(Code.STATUS_VALID);
        domain.setTop(Code.STATUS_INVALID);
        noteMapper.insert(domain);
    }

    /**
     * 设置状态
     * @param id
     * @param status
     */
    public void setStatusById(Integer id, Integer status) {
        if (id == null) throw new BusinessException("请选择要操作的记录");
        if (status == null) throw new BusinessException("参数有误");
        Note note = noteMapper.selectByPrimaryKey(id);
        if (note == null) throw new BusinessException("参数有误");
        note.setValidStatus(status);
        noteMapper.updateByPrimaryKeySelective(note);
    }

    /**
     * 批量设置状态
     * @param ids
     */
    public void setStatus(String ids, Integer status) {
        if (StringUtils.isBlank(ids)) throw new BusinessException("请选择要操作的记录");
        if (status == null) throw new BusinessException("参数有误");
        String[] idStrArr = ids.split(",");
        for (String idStr : idStrArr) {
            Integer noteId = Integer.parseInt(idStr);
            Note note = noteMapper.selectByPrimaryKey(noteId);
            if (note == null) continue;
            if (status == note.getValidStatus()) continue;
            note.setValidStatus(status);
            noteMapper.updateByPrimaryKeySelective(note);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(Integer id) {
        if (id == null) throw new BusinessException("请选择要删除的记录");
        Note note = noteMapper.selectByPrimaryKey(id);
        if (note == null) throw new BusinessException("参数有误");
        note.setDisplay(Code.STATUS_INVALID);
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
            note.setDisplay(Code.STATUS_INVALID);
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
        NoteExample example = new NoteExample();
        NoteExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(queryVo.getUserCode()) || StringUtils.isNotBlank(queryVo.getPhone()))
            criteria.andUserIdIn(getUserIds(queryVo));
        if (StringUtils.isNotBlank(queryVo.getPhoneUniqueCode()))
            criteria.andPhoneUniqueCodeLike(MessageFormat.format("%{0}%", queryVo.getPhoneUniqueCode()));
        if (StringUtils.isNotBlank(queryVo.getType()))
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


    /**
     * 查找便签id集合
     * @param queryVo
     * @return
     */
    public List<Integer> getUserIds(NoteVO queryVo) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(queryVo.getUserCode()))
            criteria.andUserCodeLike(MessageFormat.format("%{0}%", queryVo.getUserCode()));
        if (StringUtils.isNotBlank(queryVo.getPhone()))
            criteria.andPhoneLike(MessageFormat.format("%{0}%", queryVo.getPhone()));
        List<User> userList = userMapper.selectByExample(example);
        List<Integer> userIdList = new ArrayList<>();
        if (!userList.isEmpty()) userIdList = userList.stream().map(user -> user.getId()).collect(Collectors.toList());
        if (userIdList.isEmpty()) userIdList.add(-1);
        return userIdList;
    }

}