package com.example.student.login;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * 使用Base64来保存和获取密码数据
 */
public class Base64Utils {

    //压缩和解压的次数，防止被简单破解
    //private static int decodeTimes=5;

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) {
        int decodeTimes=5;
        byte[] bt;
        key = key.trim().replace(" ", "");//去掉空格
        try {
            while (decodeTimes > 0) {
                bt = (new BASE64Decoder()).decodeBuffer(key);
                key = new String(bt);
                decodeTimes--;
            }
            System.out.println("key after decryptBASE64:"+key);
            System.out.println("guess:"+key);
            System.out.println("key after decryptBASE64 and utf-8:"+new String(key.getBytes(), StandardCharsets.UTF_8));
            return new String(key.getBytes(), StandardCharsets.UTF_8);//如果出现乱码可以改成： String(bt, "utf-8")或 gbk
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key) {
        int decodeTimes=5;
        byte[] bt;
        key = key.trim().replace(" ", "");//去掉空格
        while (decodeTimes > 0) {
            bt = key.getBytes();
            key = (new BASE64Encoder()).encodeBuffer(bt);
            decodeTimes--;
        }
        System.out.println("key after encryptBASE64:"+key);
        return key;
    }
}
