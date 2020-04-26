package com.fr.env.utils;

/**
 * @author: Maksim
 * @Date: Created in 2020/3/16
 * @Description:
 */
public class DisplayUtils {

    /**
     * 获取字符串显示时的计算长度
     * @param text 被计算的字符串
     * @return 计算长度
     */
    public static int getDisplayLength(String text){
        if (text == null) {
            return 0;
        }
        char[] c = text.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if(!isLetter(c[i])){
                len++;
            };
        }
        return len;
    }

    /**
     * 判断字符是否为字母、数字、英文符号
     * @param c 传入的字符
     * @return 如果符合上述条件，返回true
     */
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0;
    }
}
