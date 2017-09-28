package com.hellogood.service;

import com.hellogood.domain.Province;
import com.hellogood.domain.ProvinceExample;
import com.hellogood.mapper.ProvinceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Service
 * @author kejian
 *
 */
@Service
public class ProvinceService {
	@Autowired
	private ProvinceMapper provinceMapper;

	public static Map<Integer, String> provinceMap = new HashMap<>();

	@PostConstruct
    public void init(){
        //基础数据
        List<Province> list = provinceMapper.selectByExample(new ProvinceExample());
        for(Province province : list){
        	provinceMap.put(province.getId(), province.getName());
        }
    }

	/**
	 * 获取全部的省
	 * @param ticket
	 */
	public List<Province> getAllProvince(){
		return provinceMapper.selectByExample(new ProvinceExample());
	}
	
	/**
	 * 根据省的id获取该省城市
	 * @param ticket
	 */
	public Province getProvince(Integer id){
		return provinceMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 根据省的code
	 * @param ticket
	 */
	public Province getProvinceByCode(Integer provinceCode){
		ProvinceExample example = new ProvinceExample();
		example.createCriteria().andCodeEqualTo(provinceCode);
		List<Province> provinceList = provinceMapper.selectByExample(example);
		if(provinceList == null || provinceList.size() < 1)
			return null;
		return provinceList.get(0);
	}
	
	/**
	 * 根据省的code
	 * @param ticket
	 */
	public Province getProvinceByName(String provinceName){
		ProvinceExample example = new ProvinceExample();
		example.createCriteria().andNameEqualTo(provinceName);
		List<Province> provinceList = provinceMapper.selectByExample(example);
		if(provinceList == null || provinceList.size() < 1)
			return null;
		return provinceList.get(0);
	}
	
}
