package com.hellogood.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import com.hellogood.domain.BaseData;
import com.hellogood.service.BaseDataService;
import net.sf.ehcache.Element;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.web.context.ContextLoader;

/**
 * 发短信工具类
 * @author fzh
 *
 */
public class SmsUtil {
	private static final Logger logger = Logger.getLogger(SmsUtil.class);
	private static String cu_fa_jie_kou_Url = StaticFileUtil.getProperty("sms", "cu_fa_jie_kou_Url");
	private static String chuang_lan_Url = StaticFileUtil.getProperty("sms", "chuang_lan_Url");

	
	/**
	 * 发送方法  其他方法同理      返回0 或者 1 都是  提交成功
	 * @param Mobile
	 * @param Content
	 * @param SendTime
	 * @return
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	public static int sendSMS(String Mobile,String Content,String SendTime) throws UnsupportedEncodingException, MalformedURLException, DocumentException {
		String channelType = null;
		BaseDataService baseDataService = (BaseDataService) ContextLoader.getCurrentWebApplicationContext().getBean("baseDataService");
		List<BaseData> list = baseDataService.getValidDataByType("smsChannel", false);
		if(list != null && !list.isEmpty()){
			channelType = list.get(0).getCode();
		}
		//选择渠道发送短信
		if("1".equals(channelType) )
			return channelOne(Mobile, Content, SendTime);
		if("2".equals(channelType) )
			return channelTwo(Mobile, Content, SendTime);
		if("3".equals(channelType) )
			return channelThird(Mobile, Content, SendTime);
		
		return channelThird(Mobile, Content, SendTime);
	}
	/**
	 * 发送方法  其他方法同理      返回0 或者 1 都是  提交成功
	 * @param Mobile
	 * @param Content
	 * @param SendTime
	 * @return
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	public static int channelOne(String Mobile,String Content,String SendTime) throws UnsupportedEncodingException, MalformedURLException {
		logger.info("凌凯短信平台..phone: " + Mobile);
		/*String CorpID = StaticFileUtil.getProperty("sms", "CorpID");//账户名
		String Pwd = StaticFileUtil.getProperty("sms", "Pwd");//密码
		Content = URLEncoder.encode(Content.replaceAll("<br/>", " "), "GBK");//发送内容
		String _url = "%s?CorpID=%s&Pwd=%s&Mobile=%s&Content=%s&Cell=&SendTime=%s";
		_url = String.format(_url, StaticFileUtil.getProperty("sms", "BatchSendUrl"),
				CorpID, Pwd, Mobile, Content, SendTime==null?"":SendTime);
		URL url = new URL(_url);
		BufferedReader in = null;
		int inputLine = 0;
		try {
			logger.info("开始发送短信手机号码为 ："+Mobile);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			inputLine = new Integer(in.readLine()).intValue();
		} catch (Exception e) {
			inputLine=-2;
			logger.info("网络异常,发送短信失败！");
			e.printStackTrace();
			throw new BusinessException("网络异常,发送短信失败！");
		}
		logger.info("结束发送短信返回值：  "+inputLine);
		return inputLine;*/
		return 1; //取消屏蔽请删除
	}
	
	public static int channelTwo(String phone, String content, String SendTime) throws DocumentException{
		logger.info("互亿短信平台..phone: " + phone);
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(cu_fa_jie_kou_Url); 
		int status = 0;	
//		client.getParams().setContentCharset("UTF-8");
//		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
//
//		NameValuePair[] data = {
//			    new NameValuePair("account", StaticFileUtil.getProperty("sms", "cu_fa_jie_kou_account")), 
//			    new NameValuePair("password", StaticFileUtil.getProperty("sms", "cu_fa_jie_kou_password")), //
//			    //new NameValuePair("password", util.StringUtil.MD5Encode("")),
//			    new NameValuePair("mobile", phone), 
//			    new NameValuePair("content", content)
//		};
//		
//		method.setRequestBody(data);		
//		
//		try {
//			logger.info("开始发送短信手机号码为 ："+phone);
//			client.executeMethod(method);	
//			
//			String SubmitResult =method.getResponseBodyAsString();
//
//			Document doc = DocumentHelper.parseText(SubmitResult); 
//			Element root = doc.getRootElement();
//			String code = root.elementText("code");	
//						
//			if("2".equals(code)){
//				logger.info("短信提交成功");
//				status = 1;
//			}
//			
//		} catch (HttpException e) {
//			logger.info("网络异常,发送短信失败！");
//			status = -2;
//			e.printStackTrace();
//			throw new BusinessException("网络异常,发送短信失败！");
//		} catch (IOException e) {
//			status = -2;
//			e.printStackTrace();
//			throw new BusinessException("网络异常,发送短信失败！");
//		} catch (DocumentException e) {
//			status = -2;
//			e.printStackTrace();
//			throw new BusinessException("网络异常,发送短信失败！");
//		}
		return status;
	}
	
	public static int channelThird(String phone, String content, String SendTime){
		logger.info("创蓝短信平台..phone: " + phone);
		String account = StaticFileUtil.getProperty("sms", "chuang_lan_account");// 账号
		String pswd = StaticFileUtil.getProperty("sms", "chuang_lan_password");// 密码
		boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
		String product = null;// 产品ID
		String extno = null;// 扩展码

		try {
			String returnString = HttpSender.batchSend(chuang_lan_Url, account, pswd, phone, content, needstatus, product, extno);
			int status = Integer.parseInt((returnString.split(",")[1]).split("\n")[0]);
			System.out.println("短信状态码："+ returnString);
			if(status == 0)
				return 1;
			return 0;
			// TODO 处理返回值,参见HTTP协议文档
		} catch (Exception e) {
			// TODO 处理异常
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void main(String[] args){
		try {
			sendSMS("15915735052", "您本次购买的【变量】，订单号为：3325325。如若有疑问请拨打4000-520-029客服热线", null);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//channelThird("15915735052", "您本次购买的【变量】，订单号为：3325325。如若有疑问请拨打4000-520-029客服热线", null);
	}

}
