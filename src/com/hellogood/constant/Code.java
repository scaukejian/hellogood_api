package com.hellogood.constant;

import com.hellogood.utils.StaticFileUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 公共代码类
 * @author kejian
 * @date 2017-10-8
 */
public class Code {
	/**
	 * 有效状态
	 */
	public static final Integer STATUS_VALID = 1;
	/**
	 * 无效状态
	 */
	public static final Integer STATUS_INVALID = 0;
	/**
	 * 基础数据类型-省份
	 */
	public static final String DATA_TYPE_PROVINCE = "province";
	/**
	 * 基础数据类型-地区
	 */
	public static final String DATA_TYPE_AREA = "area";
	/**
	 * 计划分类
	 */
	public static final List<String> typeList = Arrays.asList("日","周","月","季","年");
	/**
	 * 个推定义常量, appId、appKey、masterSecret
	 */
	public static final String APPID = StaticFileUtil.getProperty("getui", "appId");
	public static final String APPKEY = StaticFileUtil.getProperty("getui", "appKey");
	public static final String MASTER_SECRET = StaticFileUtil.getProperty("getui", "masterSecret");
	public static final String LOGO = StaticFileUtil.getProperty("getui", "logo");
	public static final String LOGO_URL = StaticFileUtil.getProperty("getui", "logoUrl");
}
