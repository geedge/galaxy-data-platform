package com.mesalab.hos.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class PublicUtil {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PublicUtil.class);

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static String stringTonextAscii(String value) {
        StringBuilder sbu = new StringBuilder();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i] + 1);
            }
        }
        StringBuilder ats = new StringBuilder();
        String[] atsChars = sbu.toString().split(",");
        for (int i = 0; i < chars.length; i++) {
            ats.append((char) Integer.parseInt(atsChars[i]));
        }
        return ats.toString();
    }

    public static String getRowKey(String filename) {
        String newFilename;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(filename.getBytes());
            newFilename = new BigInteger(1, md.digest()).toString(16).substring(8, 24);
        } catch (Exception e) {
            newFilename = filename;
            logger.error(e.toString());
        }
        return newFilename;
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static String getUTCTime(long timeStamp){
        Date date = new Date(timeStamp);
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static byte[][] stringToByteArray(String regions){
        String[] regionStartKey = regions.split(",");
        byte[][] regionParts = new byte[regionStartKey.length][1];
        for (int i = 0; i < regionStartKey.length; i++) {
            regionParts[i][0] = regionStartKey[i].getBytes()[0];
        }
        return regionParts;
    }

    public static String getRandomHead(List<String> list){
        int max=list.size(),min=1;
        int ran = (int) (Math.random()*(max-min)+min);
        return list.get(ran);
    }


}
