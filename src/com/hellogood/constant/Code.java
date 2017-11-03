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
	 * 文件夹id(从数据库获得)
	 */
	public static final Integer FOLDER_DAY_ID = 1;
	public static final Integer FOLDER_WEEK_ID = 2;
	public static final Integer FOLDER_MONTH_ID = 3;
	public static final Integer FOLDER_SEASON_ID = 4;
	public static final Integer FOLDER_YEAR_ID_ID = 5;

	/**
	 * 个推定义常量, appId、appKey、masterSecret
	 */
	public static final String APPID = StaticFileUtil.getProperty("getui", "appId");
	public static final String APPKEY = StaticFileUtil.getProperty("getui", "appKey");
	public static final String MASTER_SECRET = StaticFileUtil.getProperty("getui", "masterSecret");
	public static final String LOGO = StaticFileUtil.getProperty("getui", "logo");
	public static final String LOGO_URL = StaticFileUtil.getProperty("getui", "logoUrl");
}
