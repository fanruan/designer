package com.fr.design.mainframe;

import com.fr.stable.CodeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hzzz on 2017/12/4.
 */
public class SiteCenterToken {

    public static String generateToken() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        return CodeUtils.md5Encode(date, "", "MD5");
    }
}
