package com.hellogood.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hellogood.constant.Code;
import com.hellogood.domain.Folder;
import com.hellogood.domain.FolderExample;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.vo.FolderVO;
import com.hellogood.mapper.FolderMapper;
import com.hellogood.service.redis.RedisCacheManger;
import org.apache.commons.lang.StringUtils;
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
    @Autowired
    private RedisCacheManger redisCacheManger;

    private Gson gson = new Gson();

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
        List<Folder> currentUserFolderList = getUserFolderListByRedis(vo.getUserId(), false);
        if (currentUserFolderList.size() >= 10) throw new BusinessException("操作失败：一个用户只能自定义10个文件夹");
        Folder domain = new Folder();
        vo.vo2Domain(domain);
        domain.setCreateTime(new Date());
        domain.setUpdateTime(new Date());
        domain.setValidStatus(Code.STATUS_VALID);//有效
        domain.setSystemFolder(Code.STATUS_INVALID);
        folderMapper.insert(domain);
        getUserFolderListByRedis(vo.getUserId(), true); //更新缓存
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
        getUserFolderListByRedis(folderVO.getUserId(), true); //更新缓存
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
        getUserFolderListByRedis(folderVO.getUserId(), true); //更新缓存
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
        getUserFolderListByRedis(vo.getUserId(), true); //更新缓存
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
    public List<FolderVO> getFolderList(FolderVO queryVo) {
        List<Folder> systemFolderList = getSystemFolderListByRedis(false); //系统文件夹
        List<Folder> userFolderList = getUserFolderListByRedis(queryVo.getUserId(), false); //用户文件夹
        systemFolderList.addAll(userFolderList);
        return domainList2VoList(systemFolderList);
    }

    /**
     * 从缓存获取用户文件夹
     * @param userId
     * @param refresh
     * @return
     */
    public List<Folder> getUserFolderListByRedis(Integer userId, boolean refresh) {
        List<Folder> folderList = new ArrayList<>();
        if (userId == null || userId == 0) return folderList;
        String folderCacheKey = FolderService.class.getSimpleName() + "_" + userId;
        String jsonStr = null;
        if (!refresh) {
            jsonStr = redisCacheManger.getRedisCacheInfo(folderCacheKey);
        }
        if (StringUtils.isNotBlank(jsonStr)) {
            folderList = gson.fromJson(jsonStr, new TypeToken<List<Folder>>() {}.getType());
        } else {
            FolderExample example = new FolderExample();
            FolderExample.Criteria criteria = example.createCriteria();
            criteria.andValidStatusEqualTo(Code.STATUS_VALID);
            criteria.andUserIdEqualTo(userId);
            example.setOrderByClause("create_time");
            folderList = folderMapper.selectByExample(example);
            redisCacheManger.setRedisCacheInfo(folderCacheKey, RedisCacheManger.REDIS_CACHE_EXPIRE_WEEK, gson.toJson(folderList));
        }
        return folderList;
    }

    /**
     * 从缓存获取系统文件夹
     * @param refresh
     * @return
     */
    public List<Folder> getSystemFolderListByRedis(boolean refresh) {
        List<Folder> folderList = new ArrayList<>();
        String folderCacheKey = FolderService.class.getSimpleName() + "_systemFolder";
        String jsonStr = null;
        if (!refresh) {
            jsonStr = redisCacheManger.getRedisCacheInfo(folderCacheKey);
        }
        if (StringUtils.isNotBlank(jsonStr)) {
            folderList = gson.fromJson(jsonStr, new TypeToken<List<Folder>>() {}.getType());
        } else {
            FolderExample example = new FolderExample();
            FolderExample.Criteria criteria = example.createCriteria();
            criteria.andValidStatusEqualTo(Code.STATUS_VALID);
            criteria.andSystemFolderEqualTo(Code.STATUS_VALID);
            criteria.andUserIdIsNull();
            example.setOrderByClause("create_time");
            folderList = folderMapper.selectByExample(example);
            redisCacheManger.setRedisCacheInfo(folderCacheKey, RedisCacheManger.REDIS_CACHE_EXPIRE_WEEK, gson.toJson(folderList));
        }
        return folderList;
    }

    private List<FolderVO> domainList2VoList(List<Folder> domainList) {
        List<FolderVO> voList = new ArrayList<>(domainList.size());
        for (Folder domain : domainList) {
            FolderVO vo = new FolderVO();
            vo.domain2Vo(domain);
            voList.add(vo);
        }
        return voList;
    }

}
