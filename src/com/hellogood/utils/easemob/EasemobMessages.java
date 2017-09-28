package com.hellogood.utils.easemob;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hellogood.utils.easemob.vo.ClientSecretCredential;
import com.hellogood.utils.easemob.vo.Credential;
import com.hellogood.utils.easemob.vo.EndPoints;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * REST API Demo: 发送消息 REST API HttpClient4.3实现
 * 
 * Doc URL: https://docs.easemob.com/doku.php?id=start:100serverintegration:50messages
 * 
 * @author Lynch 2014-09-15
 *
 */
public class EasemobMessages {

	private static final Logger LOGGER = LoggerFactory.getLogger(EasemobMessages.class);
    private static final String APPKEY = Constants.APPKEY;
    private static final JsonNodeFactory factory = new JsonNodeFactory(false);

    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(Constants.APP_CLIENT_ID,
            Constants.APP_CLIENT_SECRET, Constants.USER_ROLE_APPADMIN);

    public static void main(String[] args) {
        //  检测用户是否在线
//        String targetUserName = "30000942";
//        ObjectNode usernode = getUserStatus(targetUserName);
//        if (null != usernode) {
//            LOGGER.info("检测用户是否在线: " + usernode.toString());
//        }

        // 给用户发一条文本消息
        String from = "40001913";
        String targetTypeus = "users";
        ObjectNode ext = factory.objectNode();
        String arg1 = "4";
        Integer arg2 = 4;
        Boolean arg3 = true;
        Double arg4 = 4d;
        Float arg5 = 4f;
        Long arg6 = 4l;
        ext.put("arg1", arg1);
        ext.put("arg2", arg2);
        ext.put("arg3", arg3);
        ext.put("arg4", arg4);
        ext.put("arg5", arg5);
        ext.put("arg6", arg6);
        ArrayNode targetusers = factory.arrayNode();
        targetusers.add("30000942");
      //  targetusers.add("kenshinnuser002");
        ObjectNode txtmsg = factory.objectNode();
        txtmsg.put("msg", "Hello Easemob!");
        txtmsg.put("type","txt");
        
        ObjectNode sendTxtMessageusernode = sendMessages(targetTypeus, targetusers, txtmsg, from, ext);
        if (null != sendTxtMessageusernode) {
            LOGGER.info("给用户发一条文本消息: " + sendTxtMessageusernode.toString());
        }

//        // 给用户发一条图片消息
//        File uploadImgFile = new File("/home/lynch/Pictures/24849.jpg");
//        ObjectNode imgDataNode = EasemobFiles.mediaUpload(uploadImgFile);
//        if (null != imgDataNode) {
//            String imgFileUUID = imgDataNode.path("entities").get(0).path("uuid").asText();
//            String shareSecret = imgDataNode.path("entities").get(0).path("share-secret").asText();
//
//            LOGGER.info("上传图片文件: " + imgDataNode.toString());
//
//            ObjectNode imgmsg = factory.objectNode();
//            imgmsg.put("type","img");
//            imgmsg.put("url", HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/chatfiles/" + imgFileUUID).toString());
//            imgmsg.put("filename", "24849.jpg");
//            imgmsg.put("length", 10);
//            imgmsg.put("secret", shareSecret);
//            ObjectNode sendimgMessageusernode = sendMessages(targetTypeus, targetusers, imgmsg, from, ext);
//
//        }
//        // 给用户发一条语音消息
//        File uploadAudioFile = new File("/home/lynch/Music/music.MP3");
//        ObjectNode audioDataNode = EasemobFiles.mediaUpload(uploadAudioFile);
//        if (null != audioDataNode) {
//            String audioFileUUID = audioDataNode.path("entities").get(0).path("uuid").asText();
//            String audioFileShareSecret = audioDataNode.path("entities").get(0).path("share-secret").asText();
//
//            LOGGER.info("上传语音文件: " + audioDataNode.toString());
//
//            ObjectNode audiomsg = factory.objectNode();
//            audiomsg.put("type","audio");
//            audiomsg.put("url", HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/chatfiles/" + audioFileUUID).toString());
//            audiomsg.put("filename", "music.MP3");
//            audiomsg.put("length", 10);
//            audiomsg.put("secret", audioFileShareSecret);
//            ObjectNode sendaudioMessageusernode = sendMessages(targetTypeus, targetusers, audiomsg, from, ext);
//            if (null != sendaudioMessageusernode) {
//                LOGGER.info("给用户发一条语音消息: " + sendaudioMessageusernode.toString());
//            }
//
//        }
//        // 给用户发一条透传消息
//        ObjectNode cmdmsg = factory.objectNode();
//        cmdmsg.put("action", "gogogo");
//        cmdmsg.put("type","cmd");
//        ObjectNode sendcmdMessageusernode = sendMessages(targetTypeus, targetusers, cmdmsg, from, ext);
//        if (null != sendcmdMessageusernode) {
//            LOGGER.info("给用户发一条透传消息: " + sendcmdMessageusernode.toString());
//        }
    }

	/**
	 * 检测用户是否在线
	 * 
	 * @param username
     *
	 * @return
	 */
	public static ObjectNode getUserStatus(String username) {
		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);
			objectNode.put("message", "Bad format of Appkey");
			return objectNode;
		}

		// check properties that must be provided
		if (StringUtils.isEmpty(username)) {
			LOGGER.error("You must provided a targetUserName .");
			objectNode.put("message", "You must provided a targetUserName .");
			return objectNode;
		}

		try {
			URL userStatusUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/users/"
					+ username + "/status");
			objectNode = HTTPClientUtils.sendHTTPRequest(userStatusUrl, credential, null, HTTPMethod.METHOD_GET);
			String userStatus = objectNode.get("data").path(username).asText();
			if ("online".equals(userStatus)) {
				LOGGER.error(String.format("The status of user[%s] is : [%s] .", username, userStatus));
			} else if ("offline".equals(userStatus)) {
				LOGGER.error(String.format("The status of user[%s] is : [%s] .", username, userStatus));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 发送消息
	 * 
	 * @param targetType
	 *            消息投递者类型：users 用户, chatgroups 群组
	 * @param target
	 *            接收者ID 必须是数组,数组元素为用户ID或者群组ID
	 * @param msg
	 *            消息内容
	 * @param from
	 *            发送者
	 * @param ext
	 *            扩展字段
	 * 
	 * @return 请求响应
	 */
	public static ObjectNode sendMessages(String targetType, ArrayNode target, ObjectNode msg, String from,
			ObjectNode ext) {

		ObjectNode objectNode = factory.objectNode();

		ObjectNode dataNode = factory.objectNode();

		// check appKey format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		// check properties that must be provided
		if (!("users".equals(targetType) || "chatgroups".equals(targetType))) {
			LOGGER.error("TargetType must be users or chatgroups .");

			objectNode.put("message", "TargetType must be users or chatgroups .");

			return objectNode;
		}

		try {
			// 构造消息体
			dataNode.put("target_type", targetType);
			dataNode.put("target", target);
			dataNode.put("msg", msg);
			dataNode.put("from", from);
			dataNode.put("ext", ext);

			objectNode = HTTPClientUtils.sendHTTPRequest(EndPoints.MESSAGES_URL, credential, dataNode,
					HTTPMethod.METHOD_POST);

			objectNode = (ObjectNode) objectNode.get("data");
			for (int i = 0; i < target.size(); i++) {
				String resultStr = objectNode.path(target.path(i).asText()).asText();
				if ("success".equals(resultStr)) {
					LOGGER.error(String.format("Message has been send to user[%s] successfully .", target.path(i)
							.asText()));
				} else if (!"success".equals(resultStr)) {
					LOGGER.error(String.format("Message has been send to user[%s] failed .", target.path(i).asText()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

}
