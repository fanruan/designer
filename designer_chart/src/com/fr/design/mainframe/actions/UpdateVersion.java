package com.fr.design.mainframe.actions;

import com.fr.general.FRLogger;
import com.fr.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class UpdateVersion extends SwingWorker<JSONObject,Void> {

    private static final String VERSION_URL ="http://chart.finedevelop.com/update/update.json";
    private static final int TIME_OUT = 300;//5s
    public static final String VERSION = "version";

    public UpdateVersion(){

    }


    @Override
    protected JSONObject doInBackground() throws Exception {
        return getJsonContent();
    }

    public static JSONObject getJsonContent() throws Exception{
        String res = null;
        try {
            res = readVersionFromServer(TIME_OUT);
        } catch (IOException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return new JSONObject(res);
    }

    /**
     * 从服务器读取版本
     */
    private static String readVersionFromServer(int timeOut) throws IOException {
        URL getUrl = new URL(VERSION_URL);
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();
        connection.setReadTimeout(timeOut);
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        // 服务器
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf8"));//设置编码,否则中文乱码
        String lines;
        StringBuffer sb = new StringBuffer();
        while ((lines = reader.readLine()) != null) {
            sb.append(lines);
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        return sb.toString();
    }
}