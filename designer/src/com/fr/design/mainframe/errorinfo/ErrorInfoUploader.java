package com.fr.design.mainframe.errorinfo;

import com.fr.general.*;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONObject;
import com.fr.stable.CodeUtils;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class ErrorInfoUploader {

    public static final String SUFFIX = ".json";
    public static final String FOLDER_NAME = "errorInfo";

    private static ErrorInfoUploader collector;

    static {
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            @Override
            public void envChanged() {
                FRLogger.getLogger().addLogAppender(new ErrorInfoLogAppender());
            }
        });
    }

    private ErrorInfoUploader() {
        FRLogger.getLogger().addLogAppender(new ErrorInfoLogAppender());
    }

    public static ErrorInfoUploader getInstance() {
        if (collector == null) {
            collector = new ErrorInfoUploader();
        }

        return collector;
    }

    public void sendErrorInfo(){
        //读取文件夹里的json, 加入上传队列中.
        File folder = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FOLDER_NAME));
        if (!folder.exists()) {
            return;
        }

        File[] files = folder.listFiles();
        try {
            for (File file : files) {
                String filePath = file.getPath();
                String suffix = filePath.substring(filePath.lastIndexOf("."));

                Thread.sleep(1000L);
                if (suffix.endsWith(SUFFIX)) {
                    String content = IOUtils.inputStream2String(new FileInputStream(file));
                    String url = SiteCenter.getInstance().acquireUrlByKind("design.error");
                    if (sendErroInfo(url, content)) {
                        file.delete();
                    }
                }
            }
        } catch (Exception ignore) {

        }
    }

    private boolean sendErroInfo(String url, String content) {
        HashMap<String, String> para = new HashMap<>();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        para.put("token", CodeUtils.md5Encode(date, "", "MD5"));
        para.put("content", content);
        HttpClient httpClient = new HttpClient(url, para, true);
        httpClient.asGet();

        if (!httpClient.isServerAlive()) {
            return false;
        }

        String res =  httpClient.getResponseText();
        boolean success;
        try {
            success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
        } catch (Exception ex) {
            success = false;
        }
        return success;
    }


}
