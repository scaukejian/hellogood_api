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
	 * 设置计划状态
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/setStatusById/{id}-{status}.do")
	public Map<String, Object> setStatusById(@PathVariable Integer id, @PathVariable Integer status) {
		logger.info("setStatusById=======id:"+id+"======status:"+status);
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.setStatusById(id, status);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 删除计划
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteById/{id}.do")
	public Map<String, Object> deleteById(@PathVariable Integer id) {
		logger.info("deleteById=======id:"+id);
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.deleteById(id);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 批量设置计划状态
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/setStatus/{ids}-{status}.do")
	public Map<String, Object> delete(@PathVariable String ids, @PathVariable Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		noteService.setStatus(ids, status);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 批量删除计划
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
		logger.info("note---get---id:"+id);
		Map<String, Object> map = new HashMap<String, Object>();
		NoteVO vo = noteService.get(id);
		map.put(DATA, vo);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

}
