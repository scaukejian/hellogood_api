package com.hellogood.http.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hellogood.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hellogood.domain.Province;

/**
 * 查询省份Controller
 * @author fukangwen
 *
 */
@Controller
@RequestMapping(value = "/province")
public class ProvinceController extends BaseController{
	@Autowired
	private ProvinceService provinceService;
	

	/**
	 * @Description: 查询所有的省份列表
	 * @param @param request
	 * @param @param response
	 * @return void
	 * @author fukangwen
	 * @throws IOException 
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	public Map<String, Object> getProvinceList() throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		List<Province> provinceList = provinceService.getAllProvince();
		if(provinceList == null || provinceList.size() < 1){
			map.put(MESSAGE, "没有该省的信息");
			map.put(STATUS, STATUS_FAILED);
			return map;
		}
		map.put(DATA_LIST, provinceList);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
}
