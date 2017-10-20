package com.fr.design.mainframe.errorinfo;

import com.fr.base.FRContext;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.license.function.VT4FR;
import com.fr.log.LogHandler;
import com.fr.regist.FRCoreContext;
import com.fr.regist.LicenseListener;
import com.fr.stable.CodeUtils;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
    private static boolean licSupport = true;
    // 在一台不能上网的电脑里发现了10w个errorinfo...
    private static final int MAX_ERROR_SIZE = 2000;

    static {
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            @Override
            public void envChanged() {
                FRLogger.getLogger().addLogAppender(new LogHandler<ErrorInfoLogAppender>() {
                    @Override
                    public ErrorInfoLogAppender getHandler() {
                        return new ErrorInfoLogAppender();
                    }
                });
            }
        });

        // 这个控制没啥意义, 主要在于宣传功能.
        licSupport = VT4FR.AlphaFine.support();
        FRCoreContext.listenerLicense(new LicenseListener() {
        
            @Override
            public void onChange() {
            
                licSupport = VT4FR.AlphaFine.support();
            }
        });
    }

    private ErrorInfoUploader() {
        FRLogger.getLogger().addLogAppender(new LogHandler<ErrorInfoLogAppender>() {
            @Override
            public ErrorInfoLogAppender getHandler() {
                return new ErrorInfoLogAppender();
            }
        });
    }

    public static ErrorInfoUploader getInstance() {
        if (collector == null) {
            collector = new ErrorInfoUploader();
        }

        return collector;
    }

    // 从云中心更新最新的解决方案文件
    private void checkUpdateSolution(){
        if (!licSupport) {
            return;
        }


        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String localCacheFilePath = StableUtils.pathJoin(ProductConstants.getEnvHome(), "solution", "solution.zip");
                File localCacheZip = new File(localCacheFilePath);
                if (needUpdate(localCacheZip)) {
                    downloadSolution(localCacheZip);
                }
            }
        });
        updateThread.start();
    }

    private void downloadSolution(File localCacheZip) {
        try {
            String downloadURL = SiteCenter.getInstance().acquireUrlByKind("solution.download", "http://cloud.fanruan.com/api/solution");
            HttpClient hc = new HttpClient(downloadURL);
            hc.asGet();
            InputStream in = hc.getResponseStream();
            StableUtils.makesureFileExist(localCacheZip);
            FileOutputStream out = new FileOutputStream(localCacheZip);
            IOUtils.copyBinaryTo(in, out);
            out.close();
            in.close();

            IOUtils.unzip(localCacheZip, localCacheZip.getParent());
        } catch (Exception e) {
            FRContext.getLogger().debug(e.getMessage());
        }

    }

    private boolean needUpdate(File localCacheZip){
        if (localCacheZip.exists()) {
            // 判断本地文件大小.
            String checkURL = SiteCenter.getInstance().acquireUrlByKind("solution.check", "http://cloud.fanruan.com/api/checkUpdate");
            HttpClient client = new HttpClient(checkURL);
            client.asGet();
            if (client.isServerAlive()){
                try {
                    JSONObject res = new JSONObject(client.getResponseText());
                    // 简单粗暴, 直接判断文件大小.
                    return res.optLong("version") != localCacheZip.length();
                } catch (JSONException ignore) {
                }
            }
            return false;
        }
        return true;
    }

    public void sendErrorInfo(){
        // 判断更新解决方案缓存.
        checkUpdateSolution();

        //读取文件夹里的json, 加入上传队列中.
        File folder = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FOLDER_NAME));
        if (!folder.exists()) {
            return;
        }

        File[] files = folder.listFiles();
        if (files.length > MAX_ERROR_SIZE) {
            StableUtils.deleteFile(folder);
            return;
        }

        try {
            for (File file : files) {
                String filePath = file.getPath();
                String suffix = filePath.substring(filePath.lastIndexOf("."));

                if (suffix.endsWith(SUFFIX)) {
                    Thread.sleep(1000L);
                    String content = IOUtils.inputStream2String(new FileInputStream(file));
                    if (content.length() > MAX_ERROR_SIZE) {
                        file.delete();
                        continue;
                    }

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
            success = true;
        }
        return success;
    }


}
