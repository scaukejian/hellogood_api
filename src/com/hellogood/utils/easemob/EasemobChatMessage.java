package com.hellogood.utils.easemob;

import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hellogood.utils.easemob.vo.ClientSecretCredential;
import com.hellogood.utils.easemob.vo.Credential;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

/**
 * REST API Demo : 聊天消息导出REST API HttpClient4.3实现
 * 
 * Doc URL: https://docs.easemob.com/doku.php?id=start:100serverintegration:30chatlog
 * 
 * @author Lynch 2014-09-15
 * 
 */
public class EasemobChatMessage {

	private static final Logger LOGGER = LoggerFactory.getLogger(EasemobChatMessage.class);
	private static final JsonNodeFactory factory = new JsonNodeFactory(false);
	private static final String APPKEY = Constants.APPKEY;

    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(Constants.APP_CLIENT_ID,
            Constants.APP_CLIENT_SECRET, Constants.USER_ROLE_APPADMIN);
  
    /**
     * Main Test
     *
     * @param args
     */
    public static void main(String[] args) {
    	  Gson gson = new Gson();

        // 聊天消息 获取最新的20条记录
        /*
        ObjectNode queryStrNode = factory.objectNode();
        queryStrNode.put("ql", "select+*+where+from='mm1'+and+to='mm2'");
        queryStrNode.put("limit", "20");
        ObjectNode messages = getChatMessages(queryStrNode);*/

        // 聊天消息 获取7天以内的消息
//        String currentTimestamp = String.valueOf(System.currentTimeMillis());
//        String senvenDayAgo = String.valueOf(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
//        ObjectNode queryStrNode1 = factory.objectNode();
//        queryStrNode1.put("ql", "select * where timestamp>" + senvenDayAgo + " and timestamp<" + currentTimestamp);
//        ObjectNode messages1 = getChatMessages(queryStrNode1);
//
//        // 聊天消息 分页获取
//        ObjectNode queryStrNode2 = factory.objectNode();
//        queryStrNode2.put("limit", "1000");
//        // 第一页
//        ObjectNode messages2 = getChatMessages(queryStrNode2);
//        // 第二页
//        String cursor = messages2.get("cursor").asText();
//        queryStrNode2.put("cursor", cursor);
//        ObjectNode messages3 = getChatMessages(queryStrNode2);
//        // 第三页
//        String cursor3 = messages3.get("cursor").asText();
//        queryStrNode2.put("cursor", cursor3);
//        ObjectNode messages4 = getChatMessages(queryStrNode2);
//        
//        System.out.println(messages2);
//        System.out.println(messages3);
//        System.out.println(messages4);
//        String cursor = null;
//        for(int i = 0; i < 1; i++){
//        	
//        	ObjectNode queryStrNode = factory.objectNode();
//        	queryStrNode.put("limit", "20");
//        	if(cursor != null)
//        		queryStrNode.put("cursor", cursor);
//        	ObjectNode messages = getChatMessages(queryStrNode);
//        	JsonNode temp = messages.get("cursor");
//        	if(temp == null){
//        		System.out.println("已经没有内容了.....");
//        		return;
//        	}
//        	else 
//        		cursor = messages.get("cursor").asText();
//        	System.out.println(messages.get("entities").toString());
//        	
//        	 /* 
//             * 获取环信数据库中对话实体 entities 的内容，保存在entities的JSONArray中 
//             */  
//            List<HXMessEntity> messEntities = gson.fromJson(messages.get("entities").toString(), new TypeToken<List<HXMessEntity>>() {}.getType());
//            System.out.println(messEntities);
//        }
    }

    /**
	 * 获取聊天消息
	 * 
	 * @param queryStrNode
	 *
	 */
	public static ObjectNode getChatMessages(ObjectNode queryStrNode) {

		ObjectNode objectNode = factory.objectNode();

		try {

			String rest = "";
			if (null != queryStrNode && queryStrNode.get("ql") != null && !StringUtils.isEmpty(queryStrNode.get("ql").asText())) {
				rest = "ql="+ java.net.URLEncoder.encode(queryStrNode.get("ql").asText(), "utf-8");
			}
			if (null != queryStrNode && queryStrNode.get("limit") != null && !StringUtils.isEmpty(queryStrNode.get("limit").asText())) {
				rest = rest + "&limit=" + queryStrNode.get("limit").asText();
			}
			if (null != queryStrNode && queryStrNode.get("cursor") != null && !StringUtils.isEmpty(queryStrNode.get("cursor").asText())) {
				rest = rest + "&cursor=" + queryStrNode.get("cursor").asText();
			}
		
			URL chatMessagesUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/chatmessages?" + rest);
			
			objectNode = HTTPClientUtils.sendHTTPRequest(chatMessagesUrl, credential, null, HTTPMethod.METHOD_GET);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

}
