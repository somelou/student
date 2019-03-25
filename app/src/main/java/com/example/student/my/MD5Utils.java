package com.example.student.my;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author somelou
 * @description
 * @date 2019/3/22
 */
public class MD5Utils {

    private static char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * MD5加密
     * @param pwsd
     * @return
     */
    public static String CHARtoMD5(char[] pwsd) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            // char[] to bytes[]
            Charset cs = Charset.forName("UTF-8");
            CharBuffer cb = CharBuffer.allocate(pwsd.length);
            cb.put(pwsd);
            cb.flip();
            ByteBuffer bb = cs.encode(cb);
            md5.update(bb.array());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md5 != null;
        return byte2str(md5.digest());
    }

    /*
     * public static String StrtoMD5(String key) { MessageDigest md5 = null; try {
     * md5 = MessageDigest.getInstance("MD5"); md5.update(key.getBytes()); } catch
     * (NoSuchAlgorithmException e) { e.printStackTrace(); } // ����ʹ��MD5�㷨 return
     * byte2str(md5.digest()); }
     */

    /**
     *
     *
     * @param bytes
     * @return
     */
    private static String byte2str(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byte0 : bytes) {
            result.append(hex[byte0 >>> 4 & 0xf]);
            result.append(hex[byte0 & 0xf]);
        }
        return result.toString();
    }

    public static void main(String[] args){
        String key="123456";
        System.out.println(CHARtoMD5(key.toCharArray()));
    }
}