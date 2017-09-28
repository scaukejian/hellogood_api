package com.hellogood.utils;

import java.text.DecimalFormat;
import java.util.Random;

import com.hellogood.exception.BusinessException;

/**
 * 字符操作工具类
 * @author kejian
 * @date 2015-5-13
 *
 */
public class StringUtil {

    public static final Integer CONSUMER_CODE_LENGTH = 12;
    public static final Integer CONSUMER_CODE_RANDOM = 4;

    public static final Integer CODE_LENGTH = 10;
    public static final Integer CODE_RANDOM = 3;
    

    //改成8位
    public static final Integer NUM_CODE_LENGTH = 8;

    public static final String[] arrs = new String[] { "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J"};
	
	/**
	 * 根据ID生成制定长度随机字母
	 * @param id
	 * @return
	 */
	public static String generateCode(Integer length, Integer id) {
        if(length == null || length < 1){
            throw new BusinessException("编号生成失败：长度必须大于0");
        }
		if(id == null){
			throw new BusinessException("编号生成失败：id不能为空");
		}
		StringBuffer code = new StringBuffer(10);
		Random random = new Random();
		int randomLength = length - String.valueOf(id).length();
		//生成随机字母
		for (int i = 0, j = randomLength; i < j; i++) {
			code.append(arrs[random.nextInt(10)]);
		}
		//增加指定字母
		for (String str : String.valueOf(id).split("")) {
			code.append(arrs[Integer.valueOf(str)]);
		}
		return code.toString();
	}
	
	/**
     * 根据订单ID和随机位数生成消费码
     * @param id
     * @return
     */
	public static String generateConsumerCode(Integer id) {
		if(id == null){
			throw new BusinessException("编号生成失败：id不能为空");
		}
        StringBuffer code = new StringBuffer(12);
        Random random = new Random();
        //第一位为1-9随机数
        for(int i = 0; i < CONSUMER_CODE_RANDOM; i++){
	        String firstNumString =new Integer((random.nextInt(9) + 1)).toString();
	        code.append(firstNumString);
        }
        //中间加上n个0，零的个数为总长度减去id长度再减去1
        int zeroLength = CONSUMER_CODE_LENGTH - id.toString().length() - CONSUMER_CODE_RANDOM;
        for(int i = 0; i < zeroLength; i++)
        	code.append(0);
       
        //最后加上id
        code.append(id);
        
        return code.toString();
    }

	
	/**
     * 根据订单ID和随机位数生成订单号
     * @param id
     * @return
     */
	public static String generateCode(Integer id) {
		if(id == null){
			throw new BusinessException("编号生成失败：id不能为空");
		}
        StringBuffer code = new StringBuffer(10);
        Random random = new Random();
        //第一位为1-9随机数
        for(int i = 0; i < CODE_RANDOM; i++){
	        String firstNumString =new Integer((random.nextInt(9) + 1)).toString();
	        code.append(firstNumString);
        }
        //中间加上n个0，零的个数为总长度减去id长度再减去1
        int zeroLength = CODE_LENGTH - id.toString().length() - CODE_RANDOM;
        for(int i = 0; i < zeroLength; i++)
        	code.append(0);
       
        //最后加上id
        code.append(id);
        
        return code.toString();
    }

	
    /**
     * 根据ID生成8位数字
     * @param id
     * @return
     */
    public static String generateNumCode(Integer id) {
        if(id == null){
            throw new BusinessException("编号生成失败：id不能为空");
        }
        int[] extr = {100000, 200000, 300000, 400000, 500000};
        StringBuffer code = new StringBuffer(10);
        Random random = new Random();
        //第一位为1-9随机数
        String firstNumString =new Integer((random.nextInt(9) + 1)).toString();
        code.append(firstNumString);
        if(id >= 100000){//用户量到100000，规则需要改变
        	id += 600000;
        }else{
	        Integer extrNum = extr[new Integer((random.nextInt(extr.length)))];
	        id += extrNum;
        }
        //中间加上n个0，零的个数为总长度减去id长度再减去1
        int zeroLength = NUM_CODE_LENGTH - id.toString().length() - 1;
        for(int i = 0; i < zeroLength; i++)
        	code.append(0);
       
        //最后加上id
        code.append(id);
        
        return code.toString();
    }
    
    /**
     * 给数字隔三位加一个逗号
     * @param data
     * @return
     */
	public static String formatTosepara(Integer num) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(num);
	}
    
    public static void main(String[] args){
    	//System.out.println(generateConsumerCode(3));
    	System.out.println(generateNumCode(100000));
    }
    
}
