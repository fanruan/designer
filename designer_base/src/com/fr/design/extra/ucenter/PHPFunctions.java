package com.fr.design.extra.ucenter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import com.fr.base.Base64;

/**
 * Created by lp on 2016/9/9.
 */
public abstract class PHPFunctions {
    protected String urlencode(String value){
        return URLEncoder.encode(value);
    }

    protected String md5(String input){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return byte2hex(md.digest(input.getBytes()));
    }

    protected String md5(long input){
        return md5(String.valueOf(input));
    }

    protected String base64_decode(String input){
        try {
            return new String(Base64.decode(input));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    protected String base64_encode(String input){
        try {
            return new String(Base64.encode(input.getBytes("iso-8859-1")));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    protected String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString();
    }

    protected String substr(String input,int begin, int length){
        return input.substring(begin, begin+length);
    }

    protected String substr(String input,int begin){
        if(begin>0){
            return input.substring(begin);
        }else{
            return input.substring(input.length()+ begin);
        }
    }

    protected long microtime(){
        return System.currentTimeMillis();
    }

    protected long time(){
        return System.currentTimeMillis()/1000;
    }

    protected String sprintf(String format, long input){
        String temp = "0000000000"+input;
        return temp.substring(temp.length()-10);
    }

    protected String call_user_func(String function, String model, String action, Map<String,Object> args){
        if("uc_api_mysql".equals(function)){
            return this.uc_api_mysql(model, action, args);
        }
        if("uc_api_post".equals(function)){
            return this.uc_api_post(model, action, args);
        }
        return "";
    }

    public abstract String uc_api_post(String $module, String $action, Map<String,Object> $arg );

    public abstract String uc_api_mysql(String $model, String $action, Map $args);

    protected String urlencode(String value,String code){
        try {
            return URLEncoder.encode(value,code);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}