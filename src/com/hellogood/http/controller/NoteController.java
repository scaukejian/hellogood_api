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
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/finish/{id}-{status}.do")
	public Map<String, Object> finish(@PathVariable Integer id, @PathVariable Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.finish(id, status);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 设置计划是否收藏/置顶
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/setTop/{id}-{status}.do")
	public Map<String, Object> setTop(@PathVariable Integer id, @PathVariable Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.setTop(id, status);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 放入回收站 / 移出回收站
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/recycle/{id}-{status}.do")
	public Map<String, Object> recycle(@PathVariable Integer id, @PathVariable Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.recycle(id, status);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete/{id}.do")
	public Map<String, Object> delete(@PathVariable Integer id, @PathVariable Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.deleteById(id);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}


	/**
	 * 批量删除/清空回收站计划
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete/{ids}.do")
	public Map<String, Object> delete(@PathVariable String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.delete(ids);
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
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get/{id}.do")
	public Map<String, Object> get(@PathVariable Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		NoteVO vo = noteService.get(id);
		map.put(DATA, vo);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}



}
