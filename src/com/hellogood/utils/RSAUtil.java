package com.hellogood.utils;

import javax.crypto.Cipher;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;


public class RSAUtil {

    /**
     * 生成公钥和私钥
     * @throws NoSuchAlgorithmException
     *
     */
    public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException, IOException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);

        /** 用对象流将生成的密钥写入文件 */
        ObjectOutputStream oosPublicKey = null;
        ObjectOutputStream oosPrivateKey = null;
        try{
            oosPublicKey = new ObjectOutputStream(new FileOutputStream("publicKey"));
            oosPrivateKey = new ObjectOutputStream(new FileOutputStream("privateKey"));
        }finally {
            oosPublicKey.writeObject(publicKey);
            oosPrivateKey.writeObject(privateKey);
        }
        return map;
    }
    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus
     *            模
     * @param exponent
     *            指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus
     *            模
     * @param exponent
     *            指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA"); //android要用 "RSA/None/PKCS1Padding"
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        System.err.println(bcd.length);
        //如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for(byte[] arr : arrays){
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }
    /**
     * ASCII码转BCD码
     *
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }
    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }
    /**
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }
    /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i=0; i<x+z; i++) {
            if (i==x+z-1 && y!=0) {
                str = string.substring(i*len, i*len+y);
            }else{
                str = string.substring(i*len, i*len+len);
            }
            strings[i] = str;
        }
        return strings;
    }
    /**
     *拆分数组
     */
    public static byte[][] splitArray(byte[] data,int len){
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if(y!=0){
            z = 1;
        }
        byte[][] arrays = new byte[x+z][];
        byte[] arr;
        for(int i=0; i<x+z; i++){
            arr = new byte[len];
            if(i==x+z-1 && y!=0){
                System.arraycopy(data, i*len, arr, 0, y);
            }else{
                System.arraycopy(data, i*len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
/*

         HashMap<String, Object> map = RSAUtil.getKeys();
         //生成公钥和私钥
         RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
         RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");

         //模
         String modulus = publicKey.getModulus().toString();
         //公钥指数
         String public_exponent = publicKey.getPublicExponent().toString();
         //私钥指数
         String private_exponent = privateKey.getPrivateExponent().toString();

         //明文
         String ming = "123456789";
         //使用模和指数生成公钥和私钥
         RSAPublicKey pubKey = RSAUtil.getPublicKey(modulus, public_exponent);
         RSAPrivateKey priKey = RSAUtil.getPrivateKey(modulus, private_exponent);
         //加密后的密文
         String mi = RSAUtil.encryptByPublicKey(ming, pubKey);
         System.out.println(mi);
         //解密后的明文
         ming = RSAUtil.decryptByPrivateKey(mi, priKey);
         System.out.println(ming);
         System.out.println("公钥指数: " +  publicKey.getPublicExponent().toString());
         System.out.println("私钥指数: " + privateKey.getPrivateExponent().toString());
         System.out.println("模: " + privateKey.getModulus().toString());

        //十六进制
        System.out.println("公钥指数: " +  publicKey.getPublicExponent().toString(16));
        System.out.println("私钥指数: " + privateKey.getPrivateExponent().toString(16));
        System.out.println("模: " + privateKey.getModulus().toString(16));
*/

        String password = "123456";
        String passwordMi = "3D3973641EDFDB2DB5C53ED340468004C2C9AB38A61260C81DF03131140B02C43261323C25D4F510A63C56CC0465C0FF662652FA8AEC6568694B71FF00432B39379BF857DF38F50F42E3926A2A897250C21CB8634C53ED37D63BBDB1BA4EC021FC84DAD5573EFDFD5BBFF8ED08EF52804D3ED5A462E4C0BE757AFDF7BB93525D";
        testDecrypt(passwordMi);
    }

    public static String testEncrypt(String password) throws Exception{
        String privateExponent = StaticFileUtil.getProperty("RSAKey", "publicExponent");
        String modulus = StaticFileUtil.getProperty("RSAKey", "modulus");
        RSAPublicKey publicKey = RSAUtil.getPublicKey(modulus, privateExponent);
        password = Base64Util.encode(password);//base64加密
        System.out.println(RSAUtil.encryptByPublicKey(password, publicKey));
        return RSAUtil.encryptByPublicKey(password, publicKey);
    }

    public static void testDecrypt(String password) throws Exception{
        String privateExponent = StaticFileUtil.getProperty("RSAKey", "privateExponent");
        String modulus = StaticFileUtil.getProperty("RSAKey", "modulus");
        RSAPrivateKey priKey = RSAUtil.getPrivateKey(modulus, privateExponent);
        String mima = RSAUtil.decryptByPrivateKey(password, priKey);
        System.out.println(Base64Util.decode(mima));//base64解密
    }
}
