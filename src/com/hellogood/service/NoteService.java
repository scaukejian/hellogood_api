package com.hellogood.service;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hellogood.constant.Code;
import com.hellogood.domain.*;
import com.hellogood.exception.BusinessException;
import com.hellogood.http.task.NoticeExecutor;
import com.hellogood.http.vo.NoteVO;
import com.hellogood.mapper.NoteMapper;
import com.hellogood.mapper.UserMapper;
import com.hellogood.utils.AppPush;
import com.hellogood.utils.StaticFileUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private UserService userService;
    @Autowired
    private BootUpService bootUpService;
    @Autowired
    private FolderService folderService;

    private Logger logger = LoggerFactory.getLogger(NoteService.class);

    private void checkCommon(NoteVO vo){
        if (StringUtils.isBlank(vo.getPhoneUniqueCode()))
            throw new BusinessException("请先授权APP获取系统权限");
        if (vo.getFolderId() == null || vo.getFolderId() == 0)
            throw new BusinessException("操作失败: 请选择所属文件夹");
        Folder folder = folderService.getFolder(vo.getFolderId());
        if (folder == null) throw new BusinessException("操作失败: 文件夹id有误");
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
    public Note checkAndReturnNote(Integer id, Integer status, Integer userId, String phoneUniqueCode) {
        if (id == null) throw new BusinessException("请选择要操作的记录");
        if (status == null) throw new BusinessException("状态参数有误");
        if (StringUtils.isBlank(phoneUniqueCode)) throw new BusinessException("获取设备唯一标识码失败");
        Note note = noteMapper.selectByPrimaryKey(id);
        if (note == null) throw new BusinessException("参数有误");
        return checkAuth(note, userId, phoneUniqueCode);
    }

    /**
     * 检查用户权限
     * @param note
     * @param userId
     * @param phoneUniqueCode
     * @return
     */
    public Note checkAuth(Note note, Integer userId, String phoneUniqueCode) {
        boolean isSameUser = false;//是否同一个用户在操作计划
        if (StringUtils.equals(note.getPhoneUniqueCode(), phoneUniqueCode)) isSameUser = true;
        if (userId != null && note.getUserId() != null && note.getUserId().equals(userId)) isSameUser = true;
        if (!isSameUser) throw new BusinessException("你没有权限进行此操作");
        return note;
    }

    /**
     * 放入回收站 / 移出回收站
     * @param noteVO
     */
    public void setRecycle(NoteVO noteVO) {
        Integer id = noteVO.getId();
        Integer display = noteVO.getDisplay();
        Integer userId = noteVO.getUserId();
        String phoneUniqueCode = noteVO.getPhoneUniqueCode();
        Note note = checkAndReturnNote(id, display, userId, phoneUniqueCode);
        note.setDisplay(display);
        noteMapper.updateByPrimaryKeySelective(note);
    }

    /**
     * 设置是否完成
     * @param noteVO
     */
    public void setFinish(NoteVO noteVO) {
        Integer id = noteVO.getId();
        Integer finish = noteVO.getFinish();
        Integer userId = noteVO.getUserId();
        String phoneUniqueCode = noteVO.getPhoneUniqueCode();
        Note note = checkAndReturnNote(id, finish, userId, phoneUniqueCode);
        note.setFinish(finish);
        noteMapper.updateByPrimaryKeySelective(note);
    }

    public List<Note> getNoteByFolderId(Integer folderId) {
        NoteExample example = new NoteExample();
        NoteExample.Criteria criteria = example.createCriteria();
        criteria.andFolderIdEqualTo(folderId);
        return noteMapper.selectByExample(example);
    }

    /**
     * 初始化
     */
    public void initFinish(Integer folderId) {
        Folder folder = folderService.getFolder(folderId);
        if (folder == null) return;
        List<Note> noteList = getNoteByFolderId(folderId);
        if (noteList.isEmpty()) return;
        for (Note note : noteList) {
            note.setFinish(Code.STATUS_INVALID);
            noteMapper.updateByPrimaryKeySelective(note);
        }
    }

    /**
     * 提醒用户某文件夹id未完成的计划数
     */
    public void noticeUserFinishPlan(Integer folderId) {
        Folder folder = folderService.getFolder(folderId);
        if (folder == null) return;
        String folderName = folder.getName();
        List<NoteVO> noteList = noteMapper.getUserIdAndCountMap(folderId);
        if (!noteList.isEmpty()) {
            for (NoteVO vo : noteList) {
                Integer userId = vo.getUserId();
                Integer userIdCount = vo.getUserIdCount();
                BootUp bootUp = bootUpService.getBootUpByUserId(userId);
                if (bootUp == null) continue;
                String clientId = bootUp.getClientId();
                if (StringUtils.isBlank(clientId)) continue;
                User user = userService.getUser(userId);
                if (user.getUserName() == null) user.setUserName("");
                NoticeExecutor.getExecutor().execute(() -> pushMessage(user.getUserName(), userIdCount, clientId, folderName));
            }
        }
        List<NoteVO> noteList_uniqueCode = noteMapper.getPhoneUniqueCodeAndCountMap(folderId);
        if (!noteList_uniqueCode.isEmpty()) {
            for (NoteVO vo : noteList_uniqueCode) {
                String phoneUniqueCode = vo.getPhoneUniqueCode();
                Integer phoneUniqueCodeCount = vo.getPhoneUniqueCodeCount();
                BootUp bootUp = bootUpService.getBootUpByPhoneUniqueCode(phoneUniqueCode);
                if (bootUp == null) continue;
                String clientId = bootUp.getClientId();
                if (StringUtils.isBlank(clientId)) continue;
                NoticeExecutor.getExecutor().execute(() -> pushMessage("", phoneUniqueCodeCount, clientId, folderName));
            }
        }

    }

    /**
     * 发送个推消息给用户
     * @param name 用户姓名，如果没登录为空字符串
     * @param count 未完成计划数
     * @param clientId 个推CID终端标识
     */
    public void pushMessage(String name, Integer count, String clientId, String folderName) {
        IGtPush push = new IGtPush(Code.APPKEY, Code.MASTER_SECRET);
        String transmissionContent = "【渣渣计划】亲爱的" + name;
        String text = transmissionContent;
        String title = "您还有" + count + "条"+folderName+"未完成哟~";
        // 点击通知打开应用模板
        NotificationTemplate template = AppPush.notificationTemplate(Code.APPID, Code.APPKEY, transmissionContent,
                text, title, Code.LOGO, Code.LOGO_URL);
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(5 * 3600 * 1000);
        // 配置推送目标
        List targets = new ArrayList();
        //发送目标
        Target target = new Target();
        target.setAppId(Code.APPID);
        target.setClientId(clientId);
        targets.add(target);//放到目标列表
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targets);
        logger.info(ret.getResponse().toString());
    }

    /**
     * 设置是否置顶/收藏
     * @param noteVO
     */
    public void setTop(NoteVO noteVO) {
        Integer id = noteVO.getId();
        Integer top = noteVO.getTop();
        Integer userId = noteVO.getUserId();
        String phoneUniqueCode = noteVO.getPhoneUniqueCode();
        Note note = checkAndReturnNote(id, top, userId, phoneUniqueCode);
        note.setTop(top);
        noteMapper.updateByPrimaryKeySelective(note);
    }

    /**
     * 删除
     * @param noteVO
     */
    public void delete(NoteVO noteVO) {
        if (noteVO.getId() == null) throw new BusinessException("请选择要删除的记录");
        Note note = noteMapper.selectByPrimaryKey(noteVO.getId());
        if (note == null) throw new BusinessException("参数有误");
        checkAuth(note, noteVO.getUserId(), noteVO.getPhoneUniqueCode());
        note.setValidStatus(Code.STATUS_INVALID);
        noteMapper.updateByPrimaryKeySelective(note);
    }

    /**
     * 批量删除
     * @param noteVO
     */
    public void deleteBatch(NoteVO noteVO) {
        if (StringUtils.isBlank(noteVO.getIds())) throw new BusinessException("请选择要删除的记录");
        String[] idStrArr = noteVO.getIds().split(",");
        if (idStrArr.length == 0) throw new BusinessException("请选择要删除的记录");
        for (String idStr : idStrArr) {
            Integer noteId = Integer.parseInt(idStr);
            Note note = noteMapper.selectByPrimaryKey(noteId);
            if (note == null) throw new BusinessException("参数有误");
            checkAuth(note, noteVO.getUserId(), noteVO.getPhoneUniqueCode());
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
        Note note = getNote(vo.getId());
        checkAuth(note, vo.getUserId(), vo.getPhoneUniqueCode());
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
     * @param vo
     * @return
     */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public NoteVO get(NoteVO vo) {
	    if (vo.getId() == null || vo.getId() == 0) throw new BusinessException("参数id不能为空");
        Note note = getNote(vo.getId());
        if (note == null) throw new BusinessException("参数id有误");
        checkAuth(note, vo.getUserId(), vo.getPhoneUniqueCode());
        vo.domain2Vo(note);
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
        if (queryVo.getDisplay() == null)
            throw new BusinessException("展示状态不能为空");
        boolean needFolder = true;//是否需要传递文件夹参数，回收站，收藏夹，已完成的都不需要传递文件夹id
        if (queryVo.getDisplay() == Code.STATUS_INVALID) needFolder = false;
        if (queryVo.getTop() == Code.STATUS_VALID) needFolder = false;
        if (queryVo.getFinish() == Code.STATUS_VALID) needFolder = false;
        if (queryVo.getFolderId() == null && needFolder)
            throw new BusinessException("文件夹id不能为空");
        Folder folder = folderService.getFolder(queryVo.getFolderId());
        if (folder == null && needFolder)
            throw new BusinessException("操作失败: 文件夹不能为空");
        NoteExample example = new NoteExample();
        NoteExample.Criteria criteria = example.createCriteria();
        if(queryVo.getUserId() != null && queryVo.getUserId() != 0) {
            criteria.andUserIdEqualTo(queryVo.getUserId());
        } else {
            criteria.andPhoneUniqueCodeLike(MessageFormat.format("%{0}%", queryVo.getPhoneUniqueCode()));
        }
        if (queryVo.getFolderId() != null) criteria.andFolderIdEqualTo(queryVo.getFolderId());
        if (queryVo.getTop() != null) criteria.andTopEqualTo(queryVo.getTop());
        if (queryVo.getFinish() != null) criteria.andFinishEqualTo(queryVo.getFinish());
        criteria.andDisplayEqualTo(queryVo.getDisplay());
        if (StringUtils.isNotBlank(queryVo.getContent())) criteria.andContentLike(MessageFormat.format("%{0}%", queryVo.getContent()));
        criteria.andValidStatusEqualTo(Code.STATUS_VALID);
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
            User user = userService.getUser(vo.getUserId());
            if (user != null) {
                vo.setUserCode(user.getUserCode());
                vo.setUserName(user.getUserName());
                vo.setPhone(user.getPhone());
            }
        }
        if (vo.getFolderId() != null) {
            Folder folder = folderService.getFolder(vo.getFolderId());
            vo.setFolderName(folder.getName());
        }
    }

}
