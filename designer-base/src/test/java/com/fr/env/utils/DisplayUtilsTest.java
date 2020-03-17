package com.fr.env.utils;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author: Maksim
 * @Date: Created in 2020/3/16
 * @Description:
 */
public class DisplayUtilsTest {

    @Test
    public void getDisplayLengthTest() {
        String s1 = "本地设计器";
        String s2 = "リモートサーバ";
        String s3 = "Local Designer:";
        String s4 =  "本地,设，计器： persist-2020.01.15";
        String s5 = "ローカルデザイナrelease-2020.03.03  ... Remote Server:persist-2020.01.15";
        String s6 = "本地 00:00ロ ーカDE / abc ";

        int length1 = DisplayUtils.getDisplayLength(s1);
        int length2 = DisplayUtils.getDisplayLength(s2);
        int length3 = DisplayUtils.getDisplayLength(s3);
        int length4 = DisplayUtils.getDisplayLength(s4);
        int length5 = DisplayUtils.getDisplayLength(s5);
        int length6 = DisplayUtils.getDisplayLength(s6);
        Assert.assertEquals(10,length1);
        Assert.assertEquals(14,length2);
        Assert.assertEquals(15,length3);
        Assert.assertEquals(34,length4);
        Assert.assertEquals(72,length5);
        Assert.assertEquals(26,length6);
    }

    @Test
    public void isLetterTest(){
        char c1 = 'A';
        char c2 = '中';
        char c3 = ',';
        char c4 = '，';
        char c5 = 'デ';
        char c6 = ' ';
        Assert.assertTrue(DisplayUtils.isLetter(c1));
        Assert.assertFalse(DisplayUtils.isLetter(c2));
        Assert.assertTrue(DisplayUtils.isLetter(c3));
        Assert.assertFalse(DisplayUtils.isLetter(c4));
        Assert.assertFalse(DisplayUtils.isLetter(c5));
        Assert.assertTrue(DisplayUtils.isLetter(c6));
    }
}