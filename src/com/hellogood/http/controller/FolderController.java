package com.hellogood.http.controller;

import com.github.pagehelper.PageInfo;
import com.hellogood.http.vo.FolderVO;
import com.hellogood.service.FolderService;
import com.hellogood.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件夹Controller
 * Created by kejian
 */
@RequestMapping("folder")
@Controller
public class FolderController extends BaseController {

	@Autowired
	private FolderService folderService;

	/**
	 * 查找文件夹列表
	 * @param folderVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getFolderList.do")
	public Map<String, Object> getFolderList(@RequestBody FolderVO folderVO) {
		logger.info(folderVO.toString());
		Map<String, Object> map = new HashMap<>();
		PageInfo pageInfo = folderService.pageQuery(folderVO);
		map.put(DATA_LIST, DateUtil.list2MapDateFormat(pageInfo.getList()));
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 新增文件夹
	 * @param folderVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add.do")
	public Map<String, Object> add(@RequestBody FolderVO folderVO) {
		logger.info(folderVO.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		folderService.add(folderVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 删除
	 * @param folderVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete.do")
	public Map<String, Object> delete(@RequestBody FolderVO folderVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		folderService.delete(folderVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 批量删除/清空回收站文件夹
	 * @param folderVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteBatch.do")
	public Map<String, Object> deleteBatch(@RequestBody FolderVO folderVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		folderService.deleteBatch(folderVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 修改文件夹信息
	 * @param folderVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update.do")
	public Map<String, Object> update(@RequestBody FolderVO folderVO) {
		logger.info(folderVO.toString());
		Map<String, Object> map = new HashMap<>();
		folderService.update(folderVO);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}

	/**
	 * 获取文件夹详细信息
	 * @param folderVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getFolder.do")
	public Map<String, Object> getFolder(@RequestBody FolderVO folderVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		FolderVO vo = folderService.get(folderVO);
		map.put(DATA, vo);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
}
