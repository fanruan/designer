package com.fr.design.extra.ucenter;

import com.fr.base.Base64;
import com.fr.base.FRContext;
import com.fr.stable.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author lp
 * @date 2016/9/9
 */
public abstract class AbstractClient {

    static final String UC_API_MYSQL = "ucApiMysql";
    static final String UC_API_POST = "ucApiPost";

    protected String urlEncode(String value) {
        return URLEncoder.encode(value);
    }

    protected String md5(String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            FRContext.getLogger().info(e.getMessage());
            return "";
        }
        return byte2hex(md.digest(input.getBytes()));
    }

    protected String md5(long input) {
        return md5(String.valueOf(input));
    }

    protected String base64Decode(String input) {
        try {
            return new String(Base64.decode(input));
        } catch (Exception e) {
            FRContext.getLogger().info(e.getMessage());
            return "";
        }
    }

    protected String base64Encode(String input) {
        try {
            return Base64.encode(input.getBytes("iso-8859-1"));
        } catch (Exception e) {
            FRContext.getLogger().info(e.getMessage());
            return "";
        }
    }

    protected String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (byte aB : b) {
            stmp = (Integer.toHexString(aB & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString();
    }

    protected String subStr(String input, int begin, int length) {
        return input.substring(begin, begin + length);
    }

    protected String subStr(String input, int begin) {
        if (begin > 0) {
            return input.substring(begin);
        } else {
            return input.substring(input.length() + begin);
        }
    }

    protected long microTime() {
        return System.currentTimeMillis();
    }

    protected long time() {
        return System.currentTimeMillis() / 1000;
    }

    protected String sprintf(String format, long input) {
        String temp = "0000000000" + input;
        return temp.substring(temp.length() - 10);
    }

    protected String callUserFunc(String function, String model, String action, Map<String, Object> args) {
        if (UC_API_MYSQL.equals(function)) {
            return this.ucApiMysql(model, action, args);
        }
        if (UC_API_POST.equals(function)) {
            return this.ucApiPost(model, action, args);
        }
        return StringUtils.EMPTY;
    }

    public abstract String ucApiPost(String module, String action, Map<String, Object> arg);

    public abstract String ucApiMysql(String model, String action, Map args);

    protected String urlEncode(String value, String code) {
        try {
            return URLEncoder.encode(value, code);
        } catch (UnsupportedEncodingException e) {
            FRContext.getLogger().info(e.getMessage());
        }
        return "";
    }
}