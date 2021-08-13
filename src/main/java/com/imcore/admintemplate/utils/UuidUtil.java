package com.imcore.admintemplate.utils;

import java.util.UUID;

/***
 * @Author: skyyan
 * @Date: 2019/1/23 14:27
 * @Description:
 */
public class UuidUtil {
    /***
     * 生成去除"-"的uuid主键
     * @return
     */
    public static  String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }


    public static String[] chars = new String[] {"1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    /**
     * 生成8位UUID
     * @return 8位UUID
     */
    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = getUUID();
        for (int i = 0; i < 6; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x22]);
        }
        return shortBuffer.toString();
    }
}
