package com.nianlun.objectboxdb.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class NameUtils {

    public static String createRandomZHName(int len) {
        StringBuilder randomName = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String str = null;
            int highPos, lowPos; // 定义高低位
            Random random = new Random();
            highPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
            byte[] b = new byte[2];
            b[0] = (Integer.valueOf(highPos).byteValue());
            b[1] = (Integer.valueOf(lowPos).byteValue());
            try {
                str = new String(b, "GBK"); // 转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            randomName.append(str);
        }
        return randomName.toString();
    }

}
