package com.hellogood;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Administrator on 2015/5/18.
 */
public class UtilTest {
    public static void main(String[] args){
        String password = "123456";
        String code = "13539939993";
        System.out.println(DigestUtils.md5Hex(password.concat(code)));

        String sb = "3,2,45,";
        System.out.println(sb.substring(0, sb.length() - 1));
    }
}
