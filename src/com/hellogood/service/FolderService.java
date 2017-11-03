package com.hellogood.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hellogood.constant.Code;
import com.hellogood.domain.Folder;
import com.hellogood.domain.FolderExample;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.vo.FolderVO;
import com.hellogood.mapper.FolderMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * FolderService
 * Create by kejian
 */
@Service
@Transactional
public class FolderService {

    @Autowired
    private FolderMapper folderMapper;

    private void checkCommon(FolderVO vo){
        if (vo.getUserId() == null)
            throw new BusinessException("请先登录APP");
        if (StringUtils.isBlank(vo.getName()))
            throw new BusinessException("操作失败: 文件夹名称不能为空");
        if (vo.getName().length() > 20)
            throw new BusinessException("操作失败: 文件夹名称不能大于20个字符");
    }

    /**
     * 新增
     * @param vo
     */
    public void add(FolderVO vo) {
        checkCommon(vo);
        Folder domain = new Folder();
        vo.vo2Domain(domain);
        domain.setCreateTime(new Date());
        domain.setUpdateTime(new Date());
        domain.setValidStatus(Code.STATUS_VALID);//有效
        domain.setSystemFolder(Code.STATUS_INVALID);
        folderMapper.insert(domain);
    }

    /**
     * 检查参数并返回计划
     * @param id
     * @param status
     * @return
     */
    public Folder checkAndReturnFolder(Integer id, Integer status, Integer userId) {
        if (id == null) throw new BusinessException("请选择要操作的记录");
        if (status == null) throw new BusinessException("状态参数有误");
        Folder folder = folderMapper.selectByPrimaryKey(id);
        if (folder == null) throw new BusinessException("参数有误");
        return checkAuth(folder, userId);
    }

    /**
     * 检查用户权限
     * @param folder
     * @param userId
     * @return
     */
    public Folder checkAuth(Folder folder, Integer userId) {
        boolean isSameUser = false;//是否同一个用户在操作计划
        if (userId != null && folder.getUserId() != null && folder.getUserId().equals(userId)) isSameUser = true;
        if (!isSameUser) throw new BusinessException("你没有权限进行此操作");
        return folder;
    }

    /**
     * 删除
     * @param folderVO
     */
    public void delete(FolderVO folderVO) {
        if (folderVO.getId() == null) throw new BusinessException("请选择要删除的记录");
        Folder folder = folderMapper.selectByPrimaryKey(folderVO.getId());
        if (folder == null) throw new BusinessException("参数有误");
        checkAuth(folder, folderVO.getUserId());
        folder.setValidStatus(Code.STATUS_INVALID);
        folderMapper.updateByPrimaryKeySelective(folder);
    }

    /**
     * 批量删除
     * @param folderVO
     */
    public void deleteBatch(FolderVO folderVO) {
        if (StringUtils.isBlank(folderVO.getIds())) throw new BusinessException("请选择要删除的记录");
        String[] idStrArr = folderVO.getIds().split(",");
        if (idStrArr.length == 0) throw new BusinessException("请选择要删除的记录");
        for (String idStr : idStrArr) {
            Integer folderId = Integer.parseInt(idStr);
            Folder folder = folderMapper.selectByPrimaryKey(folderId);
            if (folder == null) throw new BusinessException("参数有误");
            checkAuth(folder, folderVO.getUserId());
            folder.setValidStatus(Code.STATUS_INVALID);
            folderMapper.updateByPrimaryKeySelective(folder);
        }
    }

    /**
     * 修改
     * @param vo
     */
    public void update(FolderVO vo) {
        checkCommon(vo);
        Folder folder = getFolder(vo.getId());
        checkAuth(folder, vo.getUserId());
        Folder domain = new Folder();
        vo.vo2Domain(domain);
        domain.setUpdateTime(new Date());
        folderMapper.updateByPrimaryKeySelective(domain);
    }

    /**
     * 获取数据
     * @param id
     * @return
     */
    public Folder getFolder(Integer id) {
        return folderMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取详情
     * @param vo
     * @return
     */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public FolderVO get(FolderVO vo) {
	    if (vo.getId() == null || vo.getId() == 0) throw new BusinessException("参数id不能为空");
        Folder folder = getFolder(vo.getId());
        if (folder == null) throw new BusinessException("参数id有误");
        checkAuth(folder, vo.getUserId());
        vo.domain2Vo(folder);
        return vo;
    }

    /**
     * 分页查询
     * @param queryVo
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public PageInfo pageQuery(FolderVO queryVo) {
        FolderExample example = new FolderExample();
        FolderExample.Criteria criteria = example.createCriteria();
        if(queryVo.getUserId() != null) {
            criteria.andUserIdEqualTo(queryVo.getUserId());
        } else {
            criteria.andUserIdEqualTo(Code.STATUS_INVALID);
        }
        criteria.andValidStatusEqualTo(Code.STATUS_VALID);
        example.setOrderByClause("create_time desc");
        PageHelper.startPage(queryVo.getPage(), queryVo.getPageSize());
        List<Folder> list = folderMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        List<FolderVO> voList = domainList2VoList(list);
        pageInfo.getList().clear();
        pageInfo.getList().addAll(voList);
        return pageInfo;
    }

    /**
     * 系统默认文件夹
     * @return
     */
    public List<Folder> getSystemFolderList() {
        FolderExample example = new FolderExample();
        FolderExample.Criteria criteria = example.createCriteria();
        criteria.andValidStatusEqualTo(Code.STATUS_VALID);
        criteria.andSystemFolderEqualTo(Code.STATUS_VALID);
        example.setOrderByClause("create_time");
        return folderMapper.selectByExample(example);
    }

    private List<FolderVO> domainList2VoList(List<Folder> domainList) {
        List<FolderVO> voList = new ArrayList<>(domainList.size());
        List<Folder> systemFolderList = getSystemFolderList(); //先加载系统默认文件夹
        systemFolderList.addAll(domainList);
        for (Folder domain : systemFolderList) {
            FolderVO vo = new FolderVO();
            vo.domain2Vo(domain);
            voList.add(vo);
        }
        return voList;
    }

}
