package com.hellogood.http.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hellogood.constant.Code;
import com.hellogood.domain.BaseData;
import com.hellogood.domain.BaseDataExample;
import com.hellogood.exception.BusinessException;
import com.hellogood.service.AreaService;
import com.hellogood.service.BaseDataService;
import com.hellogood.utils.DateUtil;
import com.github.pagehelper.PageInfo;

/**
 * @author yanyuan
 * @date 2015/4/29
 * @version 1.0
 */

@Controller
@RequestMapping("/baseData")
public class BaseDataController extends BaseController{
	
	@Autowired
	private BaseDataService baseDataService;
	@Autowired
	private AreaService areaService;

	
	@ResponseBody
	@RequestMapping("/all.do")
	public Map<String, Object> all(){
		Map<String, Object> map = new HashMap<String, Object>();
		BaseDataExample example = new BaseDataExample();
		example.createCriteria().andStatusEqualTo(Code.STATUS_VALID);
		example.setOrderByClause("id");
		map.put(DATA, baseDataService.getData(example));
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/get/{type}.do")
	public Map<String, Object> get(@PathVariable String type, HttpServletRequest request){
    	Integer pageSize = Integer.MAX_VALUE,page = 1;
		if(request.getParameter("page") != null && request.getParameter("pageSize") != null){
			page = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		PageInfo pageInfo = baseDataService.getData(type, page, pageSize);
		map.put(DATA_LIST, DateUtil.list2MapDateFormat((pageInfo.getList())));
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
	/**
	 * 获取红包提现的数据，手续费率，提现微信号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRedPacketConfig.do")
	public Map<String, Object> getRedPacketConfig(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<BaseData> weixinList = baseDataService.getData(Code.DATA_TYPE_WITHDRAW_NAME);
		if(weixinList == null || weixinList.isEmpty())
			throw new BusinessException("获取基础数据错误");
		map.put("weixinName", weixinList.get(0).getName());
		
		List<BaseData> costList = baseDataService.getData(Code.DATA_TYPE_WITHDRAW_COST);
		if(costList == null || costList.isEmpty())
			throw new BusinessException("获取基础数据错误");
		map.put("withdrawCost", costList.get(0).getName());
		
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
}
