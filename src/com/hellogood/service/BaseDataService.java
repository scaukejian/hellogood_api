package com.hellogood.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hellogood.constant.Code;
import com.hellogood.domain.AreaExample;
import com.hellogood.domain.BaseData;
import com.hellogood.domain.BaseDataExample;
import com.hellogood.domain.ProvinceExample;
import com.hellogood.enumeration.BaseDataType;
import com.hellogood.mapper.AreaMapper;
import com.hellogood.mapper.BaseDataMapper;
import com.hellogood.mapper.ProvinceMapper;
import com.hellogood.service.redis.RedisCacheManger;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 基础数据Service
 * @author kejian
 *
 */
@Service
public class BaseDataService {

	@Autowired
	private AreaMapper areaMapper;
	@Autowired
	private BaseDataMapper baseDataMapper;
	@Autowired
	private ProvinceMapper provinceMapper;
	@Autowired
	private RedisCacheManger redisCacheManger;

	Logger logger = LoggerFactory.getLogger(BaseDataService.class);

	private Gson gson = new Gson();

	/**
	 * 获取基础数据列表
	 * @param example
	 * @return
	 */
	public Map<String, Object> getData(BaseDataExample example){
		List<BaseData> degreeList = new ArrayList<BaseData>(),
				assetList = new ArrayList<BaseData>(),
				familyList = new ArrayList<BaseData>(),
				industryList = new ArrayList<BaseData>(),
				marryList = new ArrayList<BaseData>(),
				ownnessList = new ArrayList<BaseData>(),
				mouthSalaryList = new ArrayList<BaseData>(),
				datingTopicList = new ArrayList<>(),
				interestTabsList = new ArrayList<>(),
				momentTypeList = new ArrayList<>();
					
		//基础数据
		List<BaseData> list = baseDataMapper.selectByExample(example);
		for (BaseData data : list) {
			BaseDataType baseDataType = BaseDataType.get(data.getType());
			if(baseDataType == null){
				continue;
			}
			switch (baseDataType) {
				case INDUSTRY:
					industryList.add(data);
					break;
				case DEGREE:
					degreeList.add(data);
					break;
				case FAMILY:
					familyList.add(data);
					break;
				case ASSET:
					assetList.add(data);
					break;
				case MARRY:
					marryList.add(data);
					break;
				case OWNNESS:
					ownnessList.add(data);
					break;
				case MOUTH_SALARY:
					mouthSalaryList.add(data);
					break;
				case DATING_TOPIC:
					datingTopicList.add(data);
					break;
				case INTEREST_TABS:
					interestTabsList.add(data);
					break;
				case MOMENT_TYPE:
					momentTypeList.add(data);
					break;
				default:
					break;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(BaseDataType.INDUSTRY.getCode(), industryList);//所属行业
		map.put(BaseDataType.DEGREE.getCode(), degreeList);//学历
		map.put(BaseDataType.FAMILY.getCode(), familyList);//家庭背景
		map.put(BaseDataType.ASSET.getCode(), assetList);//个人资产
		map.put(BaseDataType.MARRY.getCode(), marryList);//婚姻
		map.put(BaseDataType.OWNNESS.getCode(), ownnessList);//个人状态
		map.put(BaseDataType.MOUTH_SALARY.getCode(), mouthSalaryList);//个人状态
		map.put(BaseDataType.DATING_TOPIC.getCode(), datingTopicList);//约会主题
		map.put(BaseDataType.INTEREST_TABS.getCode(), interestTabsList);//个人状态
		map.put(BaseDataType.MOMENT_TYPE.getCode(), momentTypeList);//随感类型
		map.put(Code.DATA_TYPE_PROVINCE, provinceMapper.selectByExample(new ProvinceExample()));//省份
		map.put(Code.DATA_TYPE_AREA, areaMapper.selectByExample(new AreaExample()));//地区
		return map;
	}
	
	/**
	 * 根据类型查询基础数据
	 * @param type
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageInfo getData(String type, Integer page, Integer pageSize){
		PageHelper.startPage(page, pageSize, false);
        List list = null;
		switch(type){
			case Code.DATA_TYPE_PROVINCE :
                list = provinceMapper.selectByExample(new ProvinceExample());
				break;
			case Code.DATA_TYPE_AREA :
                list = areaMapper.selectByExample(new AreaExample());
				break;
			default :
				BaseDataExample example = new BaseDataExample();
				example.createCriteria().andTypeEqualTo(type).andStatusEqualTo(Code.STATUS_VALID);
				example.setOrderByClause("id");
                list = baseDataMapper.selectByExample(example);
				break;
				
		}
		PageInfo pageInfo = new PageInfo(list);
		return pageInfo;
	}
	
	/**
	 * 根据id查询基础数据
	 * @param id
	 * @return
	 */
	public BaseData getData(Integer id){
		return baseDataMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 根据id, type查询基础数据
	 * @param type
	 * @return
	 */
	public BaseData getData(String type, Integer id){
		BaseData baseData = null;
		BaseDataExample example = new BaseDataExample();
		example.createCriteria().andIdEqualTo(id).andTypeEqualTo(type).andStatusEqualTo(Code.STATUS_VALID);
		List<BaseData> list = baseDataMapper.selectByExample(example);
		if(!baseDataMapper.selectByExample(example).isEmpty()){
			baseData = list.get(0);
		}
		return baseData;
	}
	
	/**
	 * 根据CODE, type查询基础数据
	 * @param type
	 * @return
	 */
	public BaseData getData(String type, String code){
		BaseData baseData = null;
		BaseDataExample example = new BaseDataExample();
		example.createCriteria().andCodeEqualTo(code).andTypeEqualTo(type).andStatusEqualTo(Code.STATUS_VALID);
		List<BaseData> list = baseDataMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			baseData = list.get(0);
		}
		return baseData;
	}
	
	/**
	 * 根据CODE, type查询基础数据
	 * @param type
	 * @return
	 */
	public List<BaseData> getData(String type){
		BaseDataExample example = new BaseDataExample();
		example.createCriteria().andTypeEqualTo(type).andStatusEqualTo(Code.STATUS_VALID);
		List<BaseData> list = baseDataMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list;
		}
		return null;
	}
	
	/**
	 * 获取基础客服账号用户id List
	 * @return
	 */
	public List<Integer> getCustomerUidList(){
		BaseDataExample example = new BaseDataExample();
		example.createCriteria().andTypeEqualTo(Code.DATA_TYPE_CUSTOMER_SERVICE).andStatusEqualTo(Code.STATUS_VALID);
		List<BaseData> list = baseDataMapper.selectByExample(example);
		List<Integer> uidList = new ArrayList<Integer>();
		if(list != null && list.size() > 0){
			for(BaseData baseData : list)
				uidList.add(Integer.parseInt(baseData.getCode()));
		}
		return uidList;
	}
	
	/**
	 * 随机获取一条模板签名
	 * @return
	 */
	public String getSign(){
		List<BaseData> list = getData(Code.DATA_TYPE_SIGN);
		if(list == null)
			return null;
		int index = RandomUtils.nextInt(10000)%list.size();
		return list.get(index).getName();
	}

	/**
	 * 按类型缓存
	 * @param type
	 * @refresh 是否刷新缓存
	 * @return
	 */
	public List<BaseData> getValidDataByType(BaseDataType type, boolean refresh) {
		return getValidDataByType(type.getCode(), refresh);
	}

	/**
	 * 按类型缓存
	 * @param type
	 * @refresh 是否刷新缓存
	 * @return
	 */
	public List<BaseData> getValidDataByType(String type, boolean refresh) {
		return getValidDataByType(type, RedisCacheManger.REDIS_CACHE_EXPIRE_DEFAULT, refresh);
	}
	/**
	 * 按类型缓存
	 * @param type
	 * @refresh 是否刷新缓存
	 * @return
	 */
	public List<BaseData> getValidDataByType(String type, int expire, boolean refresh) {
		List<BaseData> list = new ArrayList<>();
		String baseDataCacheKey = BaseData.class.getSimpleName() + "_" + type;
		String jsonStr = null;
		//是否刷新缓存
		if(false == refresh){
			jsonStr = redisCacheManger.getRedisCacheInfo(baseDataCacheKey);
		}
		if(StringUtils.isNotBlank(jsonStr)){
			list = gson.fromJson(jsonStr, new TypeToken<List<BaseData>>() {}.getType());
		}else {
			BaseDataExample example = new BaseDataExample();
			example.createCriteria().andTypeEqualTo(type).andStatusEqualTo(Code.STATUS_VALID);
			list = baseDataMapper.selectByExample(example);
			if(list != null && !list.isEmpty()){
				redisCacheManger.setRedisCacheInfo(baseDataCacheKey, expire, gson.toJson(list));
			}
		}
		return list;
	}
	
}
