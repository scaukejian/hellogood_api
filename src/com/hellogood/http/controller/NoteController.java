package com.hellogood.http.controller;

import com.github.pagehelper.PageInfo;
import com.hellogood.http.vo.NoteVO;
import com.hellogood.service.NoteService;
import com.hellogood.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 计划Controller
 * Created by kejian
 */
@RequestMapping("note")
@Controller
public class NoteController extends BaseController {

	@Autowired
	private NoteService noteService;

	/**
	 * 查找计划列表
	 * @param noteVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getNoteList.do")
	public Map<String, Object> getNoteList(@RequestBody NoteVO noteVO) {
		logger.info(noteVO.toString());
		Map<String, Object> map = new HashMap<>();
		PageInfo pageInfo = noteService.pageQuery(noteVO);
		map.put(DATA_LIST, DateUtil.list2MapDateFormat(pageInfo.getList()));
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 新增计划
	 * @param noteVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add.do")
	public Map<String, Object> add(@RequestBody NoteVO noteVO) {
		logger.info(noteVO.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.add(noteVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 设置计划是否完成
	 * @param noteVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/setFinish.do")
	public Map<String, Object> setFinish(@RequestBody NoteVO noteVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.setFinish(noteVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 设置计划是否收藏/置顶
	 * @param noteVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/setTop.do")
	public Map<String, Object> setTop(@RequestBody NoteVO noteVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.setTop(noteVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 放入回收站 / 移出回收站
	 * @param noteVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/setRecycle.do")
	public Map<String, Object> setRecycle(@RequestBody NoteVO noteVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.setRecycle(noteVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 删除
	 * @param noteVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete.do")
	public Map<String, Object> delete(@RequestBody NoteVO noteVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.delete(noteVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}


	/**
	 * 批量删除/清空回收站计划
	 * @param noteVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteBatch.do")
	public Map<String, Object> deleteBatch(@RequestBody NoteVO noteVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.deleteBatch(noteVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 修改计划信息
	 * @param noteVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update.do")
	public Map<String, Object> update(@RequestBody NoteVO noteVO) {
		logger.info(noteVO.toString());
		Map<String, Object> map = new HashMap<>();
		noteService.update(noteVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 获取计划详细信息
	 * @param noteVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getNote.do")
	public Map<String, Object> getNote(@RequestBody NoteVO noteVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		NoteVO vo = noteService.get(noteVO);
		map.put(DATA, vo);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}



}
