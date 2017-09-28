package com.hellogood.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hellogood.domain.Area;
import com.hellogood.domain.AreaExample;
import com.hellogood.domain.Province;
import com.hellogood.domain.ProvinceExample;
import com.hellogood.mapper.AreaMapper;
import com.hellogood.mapper.ProvinceMapper;

/**
 * @Service
 * @author fukangwen
 *
 */
@Service
public class AreaService {
	@Autowired
	private AreaMapper areaMapper;
	@Autowired
	private ProvinceMapper provinceMapper;
	@Autowired
	private ProvinceService provinceService;


	/**
	 * 获取全部的市
	 * @param ticket
	 */
	public List<Area> getAllCity(){
		return areaMapper.selectByExample(new AreaExample());
	}
	
	/**
	 * 根据id获取城市
	 * @param ticket
	 */
	public Area getCity(Integer id){
		return areaMapper.selectByPrimaryKey(id);
	}
	
	public List<Area> getCityByProvinceCode(Integer provinceCode){
		AreaExample example = new AreaExample();
		example.createCriteria().andParentIdEqualTo(provinceCode);
		return areaMapper.selectByExample(example);
	}
	
	public void save(Area area){
		areaMapper.updateByPrimaryKey(area);
	}
	
	public Area getCityByCityCode(Integer cityCode){
		AreaExample example = new AreaExample();
		example.createCriteria().andCodeEqualTo(cityCode);
		List<Area> areaList = areaMapper.selectByExample(example);
		if(areaList == null || areaList.size() < 1)
			return null;
		return areaList.get(0);
		
	}
	
	/**
	 * 根据省的id找到它唯一的一个市的id
	 * @param provinceId
	 * @return
	 */
	public Integer getCityIdByProvinceId(Integer provinceId){
		Province province = provinceMapper.selectByPrimaryKey(provinceId);
		if(province != null){
			Area area = this.getCityByCityCode(province.getCode());
			if(area != null)
				return area.getId();
		}
		return null;
	}

	/**
	 * 根据 省份/市 查询城市名称
	 * @param cityName
	 * @return
     */
	public List<String> findCityNames(String cityName) {
		List<String> cityList = new ArrayList<>();
		cityList.add(cityName);
		ProvinceExample example = new ProvinceExample();
		example.createCriteria().andNameEqualTo(cityName);
		List<Province> provinceList = provinceMapper.selectByExample(example);
		if (!provinceList.isEmpty()) {
			List<Area> areaList = getCityByProvinceCode(provinceList.get(0).getCode());
			for (Area area : areaList) {
				cityList.add(area.getName());
			}
		}
		return cityList;
	}

	/**
	 * 根据城市名称，获取省名称
	 * @param liveCity
	 * @return
	 */
	public String getProviceByCityName(String liveCity) {
		AreaExample example = new AreaExample();
		example.createCriteria().andNameEqualTo(liveCity);
		List<Area> list = areaMapper.selectByExample(example);
		if(list.isEmpty())//没找到返回城市名称
			return liveCity;
		
		Province province = provinceService.getProvinceByCode(list.get(0).getParentId());
		if(province == null)
			return liveCity;
		return province.getName();
	}
}
