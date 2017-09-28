package com.hellogood.http.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hellogood.domain.Area;
import com.hellogood.service.AreaService;

/**
 * 城市Controller
 * @author fukangwen
 *
 */
@Controller
@RequestMapping(value = "/area")
public class AreaController extends BaseController{
	@Autowired
	private AreaService areaService;
	

	/**
	 * @Description: 根据省份id返回城市的列表
	 * @param @param request
	 * @param @param response
	 * @return void
	 * @author fukangwen
	 * @throws IOException 
	 */
	@RequestMapping(value = "/list/{provinceCode}.do")
	@ResponseBody
	public Map<String, Object> getProvinceList(@PathVariable Integer provinceCode) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Area> areaList = areaService.getCityByProvinceCode(provinceCode);
		if(areaList == null || areaList.size() < 1){
			map.put(MESSAGE, "该省份没有城市");
			map.put(DATA_LIST, new ArrayList<Area>());
			map.put(STATUS, STATUS_SUCCESS);
			return map;
		}
		if(areaList.size() == 1){
			map.put(MESSAGE, "该省份只有一个市");
			map.put(DATA_LIST, new ArrayList<Area>());
			map.put(STATUS, STATUS_SUCCESS);
			return map;
		}
		map.put(DATA_LIST, areaList);
		map.put(STATUS, STATUS_SUCCESS);
		return map;
	}
	
}
