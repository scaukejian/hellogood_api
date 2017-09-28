package com.hellogood.utils.easemob;

import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hellogood.utils.easemob.vo.ClientSecretCredential;
import com.hellogood.utils.easemob.vo.Credential;
import com.hellogood.utils.easemob.vo.EndPoints;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * REST API Demo :用户体系集成 REST API HttpClient4.3实现
 * 
 * Doc URL: https://docs.easemob.com/doku.php?id=start:100serverintegration:20users
 * 
 * @author Lynch 2014-09-15
 * 
 */
public class IMUsers {
	

	private static final Logger LOGGER = LoggerFactory.getLogger(IMUsers.class);
	private static final JsonNodeFactory factory = new JsonNodeFactory(false);
	

    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(Constants.APP_CLIENT_ID,
            Constants.APP_CLIENT_SECRET, Constants.USER_ROLE_APPADMIN);

    public static void main(String[] args) {
        /**
         * 注册IM用户[单个]
         */
//        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
//        datanode.put("username", "120002048");
//        datanode.put("password", DigestUtils.md5Hex("120002048"));
//        datanode.put("nickname", "李小姐");
//        ObjectNode createNewIMUserSingleNode = createNewIMUserSingle(datanode);
//        if (null != createNewIMUserSingleNode) {
//            LOGGER.info("注册IM用户[单个]: " + createNewIMUserSingleNode.toString());
//        }

//    	createNewIMUserSingle("test1538", "麦先生");
//    	createNewIMUserSingle("test1808", "u先生");
//        /**
//         * IM用户登录
//         */
//        ObjectNode datanode2 = JsonNodeFactory.instance.objectNode();
//        datanode2.put("username", "test1538");
//        datanode2.put("password", DigestUtils.md5Hex("test1538"));
//        ObjectNode imUserLoginNode = imUserLogin(datanode2.get("username").asText(), datanode2.get("password").asText());
//        if (null != imUserLoginNode) {
//            LOGGER.info("IM用户登录: " + imUserLoginNode.toString());
//        }
//
//        /**
//         * 注册IM用户[批量生成用户然后注册]
//         */
//        String usernamePrefix = "kenshinnuser";
//        Long perNumber = 10l;
//        Long totalNumber = 100l;
//        ObjectNode createNewIMUserBatchGenNode = createNewIMUserBatchGen(usernamePrefix, perNumber, totalNumber);
//        if (null != createNewIMUserBatchGenNode) {
//            LOGGER.info("注册IM用户[批量]: " + createNewIMUserBatchGenNode.toString());
//        }
//
//        /**
//         * 获取IM用户[主键查询]
//         */
//        String userName = "test1538";
//        ObjectNode getIMUsersByUserNameNode = getIMUsersByUserName(userName);
//        if (null != getIMUsersByUserNameNode) {
//            LOGGER.info("获取IM用户[主键查询]: " + getIMUsersByUserNameNode.toString());
//        }
//
//        /**
//         * 重置IM用户密码 提供管理员token
//         */
//		String username = "kenshinnuser100";
//        ObjectNode json2 = JsonNodeFactory.instance.objectNode();
//        json2.put("newpassword", "123456");
//        ObjectNode modifyIMUserPasswordWithAdminTokenNode = modifyIMUserPasswordWithAdminToken(username, json2);
//        if (null != modifyIMUserPasswordWithAdminTokenNode) {
//            LOGGER.info("重置IM用户密码 提供管理员token: " + modifyIMUserPasswordWithAdminTokenNode.toString());
//        }
//        ObjectNode imUserLoginNode2 = imUserLogin(username, json2.get("newpassword").asText());
//        if (null != imUserLoginNode2) {
//            LOGGER.info("重置IM用户密码后,IM用户登录: " + imUserLoginNode2.toString());
//        }
//
//        /**
//         * 添加好友[单个]
//         */
//        String ownerUserName = "80cbe122ed32615b4b234fbe7e12710b";
//        String friendUserName = "c008ed6cfa731f07863aa457d3d882fa";
//        ObjectNode addFriendSingleNode = addFriendSingle(ownerUserName, friendUserName);
//        if (null != addFriendSingleNode) {
//            LOGGER.info("添加好友[单个]: " + addFriendSingleNode.toString());
//        }
//
//        /**
//         * 查看好友
//         */
//        ObjectNode getFriendsNode = getFriends("test1538");
//        if (null != getFriendsNode) {
//            LOGGER.info("查看好友: " + getFriendsNode.toString());
//        }
//
//        /**
//         * 解除好友关系
//         **/
//        ObjectNode deleteFriendSingleNode = deleteFriendSingle(ownerUserName, friendUserName);
//        if (null != deleteFriendSingleNode) {
//            LOGGER.info("解除好友关系: " + deleteFriendSingleNode.toString());
//        }
//
//        /**
//         * 删除IM用户[单个]
//         */
//        ObjectNode deleteIMUserByuserNameNode = deleteIMUserByuserName(userName);
//        if (null != deleteIMUserByuserNameNode) {
//            LOGGER.info("删除IM用户[单个]: " + deleteIMUserByuserNameNode.toString());
//        }
//
//        /**
//         * 删除IM用户[批量]
//         */
//        Long limit = 2l;
//        ObjectNode deleteIMUserByUsernameBatchNode = deleteIMUserByUsernameBatch(limit);
//        if (null != deleteIMUserByUsernameBatchNode) {
//            LOGGER.info("删除IM用户[批量]: " + deleteIMUserByUsernameBatchNode.toString());
//        }
    }

	/**
	 * 注册IM用户[批量]
	 * 
	 * 给指定Constants.APPKEY创建一批用户
	 * 
	 * @param dataArrayNode
	 * @return
	 */
	public static ObjectNode createNewIMUserBatch(ArrayNode dataArrayNode) {

		ObjectNode objectNode = factory.objectNode();

		// check properties that must be provided
		if (dataArrayNode.isArray()) {
			for (JsonNode jsonNode : dataArrayNode) {
				if (null != jsonNode && !jsonNode.has("username")) {
					LOGGER.error("Property that named username must be provided .");

					objectNode.put("message", "Property that named username must be provided .");

					return objectNode;
				}
				if (null != jsonNode && !jsonNode.has("password")) {
					LOGGER.error("Property that named password must be provided .");

					objectNode.put("message", "Property that named password must be provided .");

					return objectNode;
				}
			}
		}

		try {

			objectNode = HTTPClientUtils.sendHTTPRequest(EndPoints.USERS_URL, credential, dataArrayNode,
					HTTPMethod.METHOD_POST);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 注册IM用户[批量生成用户然后注册]
	 * 
	 * 给指定Constants.APPKEY创建一批用户
	 * 
	 * @param usernamePrefix
	 *            生成用户名的前缀
	 * @param perNumber
	 *            批量注册时一次注册的数量
	 * @param totalNumber
	 *            生成用户注册的用户总数
	 * @return
	 */
	public static ObjectNode createNewIMUserBatchGen(String usernamePrefix, Long perNumber, Long totalNumber) {
		ObjectNode objectNode = factory.objectNode();

		if (totalNumber == 0 || perNumber == 0) {
			return objectNode;
		}
		System.out.println("你即将注册" + totalNumber + "个用户，如果大于" + perNumber + "了,会分批注册,每次注册" + perNumber + "个");
		ArrayNode genericArrayNode = IMUsers.genericArrayNode(usernamePrefix, totalNumber);
		if (totalNumber <= perNumber) {
			objectNode = IMUsers.createNewIMUserBatch(genericArrayNode);
		} else {

			ArrayNode tmpArrayNode = factory.arrayNode();
			
			for (int i = 0; i < genericArrayNode.size(); i++) {
				tmpArrayNode.add(genericArrayNode.get(i));
				// 300 records on one migration
				if ((i + 1) % perNumber == 0) {
					objectNode = IMUsers.createNewIMUserBatch(tmpArrayNode);
					if(objectNode!=null){
						LOGGER.info("注册IM用户[批量]: " + objectNode.toString());
					}
					tmpArrayNode.removeAll();
					continue;
				}

				// the rest records that less than the times of 300
				if (i > (genericArrayNode.size() / perNumber * perNumber - 1)) {
					objectNode = IMUsers.createNewIMUserBatch(tmpArrayNode);
					if(objectNode!=null){
						LOGGER.info("注册IM用户[批量]: " + objectNode.toString());
					}
					tmpArrayNode.removeAll();
				}
			}
		}

		return objectNode;
	}

	/**
	 * 获取IM用户
	 * 
	 * @param userName
	 *            用户主键：username或者uuid
	 * @return
	 */
	public static ObjectNode getIMUsersByUserName(String userName) {
		ObjectNode objectNode = factory.objectNode();
		try {
			URL userPrimaryUrl = HTTPClientUtils
					.getURL(Constants.APPKEY.replace("#", "/") + "/users/" + userName);
			objectNode = HTTPClientUtils.sendHTTPRequest(userPrimaryUrl, credential, null, HTTPMethod.METHOD_GET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectNode;
	}

	/**
	 * 删除IM用户[单个]
	 * 
	 * 删除指定Constants.APPKEY下IM单个用户
	 *
	 * 
	 * @param userName
	 * @return
	 */
	public static ObjectNode deleteIMUserByuserName(String userName) {
		ObjectNode objectNode = factory.objectNode();
		try {
			URL deleteUserPrimaryUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/users/"
					+ userName);
			objectNode = HTTPClientUtils.sendHTTPRequest(deleteUserPrimaryUrl, credential, null,
					HTTPMethod.METHOD_DELETE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectNode;
	}

	/**
	 * 删除IM用户[批量]
	 * 
	 * 批量指定Constants.APPKEY下删除IM用户
	 * 
	 * @param limit
	 * @param queryStr
	 * @return
	 */
	public static ObjectNode deleteIMUserByUsernameBatch(Long limit) {

		ObjectNode objectNode = factory.objectNode();
		try {
			URL deleteIMUserByUsernameBatchUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/users"
					+ "?limit=" + limit);
			objectNode = HTTPClientUtils.sendHTTPRequest(deleteIMUserByUsernameBatchUrl, credential, null,
					HTTPMethod.METHOD_DELETE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectNode;
	}

	/**
	 * 重置IM用户密码 提供管理员token
	 * 
	 * @param userName
	 * @param dataObjectNode
	 * @return
	 */
	public static ObjectNode modifyIMUserPasswordWithAdminToken(String userName, String newpassword) {
		ObjectNode json2 = JsonNodeFactory.instance.objectNode();
		json2.put("newpassword", newpassword);
		ObjectNode objectNode = factory.objectNode();

		try {
			URL modifyIMUserPasswordWithAdminTokenUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/")
					+ "/users/" + userName + "/password");
			objectNode = HTTPClientUtils.sendHTTPRequest(modifyIMUserPasswordWithAdminTokenUrl, credential,
					json2, HTTPMethod.METHOD_PUT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectNode;
	}
	
	
	/*************************************************************************************************************************/
	/**
	 * 指定前缀和数量生成用户基本数据
	 * 
	 * @param usernamePrefix
	 * @param number
	 * @return
	 */
	public static ArrayNode genericArrayNode(String usernamePrefix, Long number) {
		ArrayNode arrayNode = factory.arrayNode();
		for (int i = 0; i < number; i++) {
			ObjectNode userNode = factory.objectNode();
			userNode.put("username", usernamePrefix + "_" + i);
			userNode.put("password", "123456");

			arrayNode.add(userNode);
		}

		return arrayNode;
	}

}
