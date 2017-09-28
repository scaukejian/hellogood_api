package com.hellogood.utils.easemob;

import com.hellogood.utils.StaticFileUtil;

/**
 * Constants
 * 
 * @author Lynch 2014-09-15
 *
 */
public interface Constants {

	// API_HTTP_SCHEMA
	public static String API_HTTP_SCHEMA = "http";
	// API_SERVER_HOST
	public static String API_SERVER_HOST = StaticFileUtil.getProperty("easemob", "API_SERVER_HOST");
	// APPKEY
	public static String APPKEY = StaticFileUtil.getProperty("easemob", "APPKEY");
	// APP_CLIENT_ID
	public static String APP_CLIENT_ID = StaticFileUtil.getProperty("easemob", "APP_CLIENT_ID");
	// APP_CLIENT_SECRET
	public static String APP_CLIENT_SECRET = StaticFileUtil.getProperty("easemob", "APP_CLIENT_SECRET");
	//
	public static String USER_ROLE_APPADMIN = "appAdmin";
	// DEFAULT_PASSWORD
	public static String DEFAULT_PASSWORD = "123456";
}
