package com.hellogood.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by KJ on 2017/10/17.
 */
public class Base64Util {

    /**
     * 加密
     * @param data
     * @return
     */
    public static String encode(String data){
        return new String(Base64.encodeBase64(data.getBytes()));
    }

    /**
     * 解密
     * @param data
     */
    public static String decode(String data){
        return new String(Base64.decodeBase64(data));
    }

    public static void main(String[] args) {
        String testString = "123456";
        String encodeString = encode(testString);
        System.out.println(encodeString);
        System.out.println(decode(encodeString));
    }
}
