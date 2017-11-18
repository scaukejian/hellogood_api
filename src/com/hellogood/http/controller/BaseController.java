package com.hellogood.http.controller;

import java.io.File;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

import com.hellogood.exception.CheckingException;
import com.hellogood.exception.RequestParamException;
import com.hellogood.exception.UserOperateException;
import com.hellogood.utils.Base64Util;
import com.hellogood.utils.RSAUtil;
import com.hellogood.utils.StaticFileUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hellogood.exception.BusinessException;

/**
 * 
 * @author kejian
 * @date 2017/04/30
 * @version 1.0
 *
 */
public class BaseController {
	
	public static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	/**
	 * controller公共返回参数变量名
	 */
	public static final String STATUS = "status";
	public static final String USER_STATUS = "userStatus";
	public static final String DATA = "data";
	public static final String DATA_LIST = "dataList";
	public static final String MESSAGE = "message";
	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_FAILED = "failed";
	public static final String STATUS_ERROR = "error";
	public static final String MESSAGE_CODE = "code";
	public static final String MESSAGE_LABEL = "messageLabel";
	public static final String TOTAL = "total";

	@ResponseBody
	@ExceptionHandler(RuntimeException.class)
	public Map<String, Object> exceptionHandler(RuntimeException rex){
		//rex.printStackTrace();//控制台输出异常信息
		logger.error(rex.getMessage());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(MESSAGE_LABEL, rex.getMessage());
        if(rex instanceof CheckingException){
            CheckingException checkingException = (CheckingException) rex;
            map.put(STATUS, STATUS_FAILED);
            map.put(MESSAGE_CODE, checkingException.getErrorCode().getCode());
            map.put(MESSAGE, checkingException.getErrorCode().getMessage());
        }else if(rex instanceof BusinessException){
			map.put(STATUS, STATUS_FAILED);
			map.put(MESSAGE, rex.getMessage());
		}else if(rex instanceof RequestParamException){
            map.put(STATUS, STATUS_FAILED);
            map.put(MESSAGE, "操作失败，请求参数有误");
        }else if(rex instanceof UserOperateException){
			map.put(STATUS, STATUS_SUCCESS);
		}else{
			rex.printStackTrace();
			map.put(STATUS, STATUS_ERROR);
			map.put(MESSAGE, "服务器繁忙，请稍后再试");
		}
		return map;
	}
	
	/**
	 * md5加密
	 * @param password
	 * @param code
	 * @return
	 */
	public String md5Encrypt(String password, String code){
		return DigestUtils.md5Hex(password.concat(code));
	}

    /**
     * 根据userId生成文件夹
     * @param userId
     * @return
     */
    public String generateFolderPath(Integer userId){
        // 文件夹路径
        StringBuffer folderPath = new StringBuffer(StaticFileUtil.getProperty("fileSystem", "storagePath"));
        String folderName = generateFolderName(userId);
        folderPath.append(folderName).append("/");
        //文件夹不存在时创建
        if(!new File(folderPath.toString()).exists()){
            new File(folderPath.toString()).mkdir();
        }
        return folderPath.toString();
    }
    
    /**
     * 根据userId生成文件夹名称
     * @param userId
     * @return
     */
    public String generateFolderName(Integer userId){
        //最大文件夹数
        Integer fileMaxCount = Integer.valueOf(StaticFileUtil.getProperty("fileSystem", "fileMaxCount"));
        //生成文件夹名
        return String.valueOf(userId%fileMaxCount);
    }
    
    
    /**
     * 根据userId生成有前缀的文件夹
     * @param userId
     * @return
     */
    public String generateFolderPathByPrefix(Integer userId, String prefix){
        // 文件夹路径
        StringBuffer folderPath = new StringBuffer(StaticFileUtil.getProperty("fileSystem", "storagePath"));
        String folderName = generateFolderNameByPrefix(userId, prefix);
        folderPath.append(folderName).append("/");
        //文件夹不存在时创建
        if(!new File(folderPath.toString()).exists()){
            new File(folderPath.toString()).mkdir();
        }
        return folderPath.toString();
    }
    
    /**
     * 根据userId生成有前缀文件夹名称
     * @param userId
     * @return
     */
    public String generateFolderNameByPrefix(Integer userId, String prefix){
        //最大文件夹数
        Integer headFileMaxCount = Integer.valueOf(StaticFileUtil.getProperty("fileSystem", "headFileMaxCount"));
        //生成文件夹名
        return prefix + (userId%headFileMaxCount);
    }


    /**
     * 根据文件名生成文件路径
     * @param fileName
     * @return
     */
    public String generateFilePath(String fileName){
        //存储路径
        StringBuffer filePath = new StringBuffer(StaticFileUtil.getProperty("fileSystem", "storagePath"));
        //文件名是否包含文件夹名称 前缀为文件夹名称
        if(fileName.indexOf("_") > 0){
            String folderName = fileName.substring(0, fileName.indexOf("_"));
            filePath.append(folderName).append("/").append(fileName);
        }else {
            filePath.append(fileName);
        }
        return filePath.toString();
    }

    /**
     * 解密
     * @param password
     * @return
     */
    public String decrypt(String password){
        String reuslt = "";
        String privateExponent = StaticFileUtil.getProperty("RSAKey", "privateExponent");
        String modulus = StaticFileUtil.getProperty("RSAKey", "modulus");
        RSAPrivateKey priKey = RSAUtil.getPrivateKey(modulus, privateExponent);
        try{
            reuslt = RSAUtil.decryptByPrivateKey(password, priKey); //RSA解密
            reuslt = Base64Util.decode(reuslt); //base64解密
        }catch (Exception e){
            logger.info("解密失败");
            throw new RuntimeException(e);
        }
        return reuslt;
    }

    /**
     * 验证图片格式
     * @param imgName
     */
    protected void validImgFormat(String imgName){
        logger.info("验证图片格式, 图片名称: " + imgName);
        if(StringUtils.isBlank(imgName)){
            throw new BusinessException("图片名不能为空");
        }
        //判断上传格式
        String prefix = imgName.substring(imgName.lastIndexOf(".") + 1);
        if (!("jpeg".equals(prefix.toLowerCase())
                || "jpg".equals(prefix.toLowerCase())
                || "gif".equals(prefix.toLowerCase())
                || "png".equals(prefix.toLowerCase()))) {
            logger.error("文件格式有误");
            throw new BusinessException("图片格式有误, 系统目前支持jpeg, jpg, gif, png");
        }
    }
    
    /**
     * 验证图片格式
     * @param imgName
     */
    public void validBase64ImgFormat(String imgFile){
    	if(imgFile == null || imgFile.isEmpty() || !imgFile.contains(","))
			throw new BusinessException("头像上传数据有误");
		
		//data:image/png;base64,base64编码的png图片数据  
		String prefix = imgFile.split(",")[0].split("image/")[1].split(";")[0];
		
        logger.info("验证图片格式, 图片: " + imgFile);
       
        //判断上传格式
        if (!("jpeg".equals(prefix.toLowerCase())
        		|| "jpg".equals(prefix.toLowerCase())
                || "gif".equals(prefix.toLowerCase())
            	|| "png".equals(prefix.toLowerCase()))) {
            logger.error("文件格式有误");
            throw new BusinessException("图片格式有误, 系统目前支持jpeg, jpg, gif, png");
        }
    }

    public static void main(String[] args){
        String phone = "19010000669";
        System.out.println(DigestUtils.md5Hex("123456".concat(phone)));
    }
}
