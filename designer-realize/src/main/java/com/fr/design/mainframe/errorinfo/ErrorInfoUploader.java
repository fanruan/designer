package com.fr.design.mainframe.errorinfo;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.mainframe.SiteCenterToken;
import com.fr.general.CloudCenter;
import com.fr.general.CommonIOUtils;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.general.http.HttpResponseType;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.license.function.VT4FR;
import com.fr.log.FineLoggerFactory;
import com.fr.log.LogHandler;
import com.fr.stable.ArrayUtils;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class ErrorInfoUploader {

    public static final String SUFFIX = ".json";
    public static final String FOLDER_NAME = "errorInfo";

    private static ErrorInfoUploader collector;
    // 在一台不能上网的电脑里发现了10w个errorinfo...
    private static final int MAX_ERROR_SIZE = 2000;

    //单次发送的错误信息最大条数
    private static final int MAX_ITEMS = 200;

    static {
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            @Override
            public void envChanged() {
                FineLoggerFactory.getLogger().addLogAppender(new LogHandler<ErrorInfoLogAppender>() {
                    private ErrorInfoLogAppender errorInfoLogAppender = new ErrorInfoLogAppender();

                    @Override
                    public ErrorInfoLogAppender getHandler() {
                        return errorInfoLogAppender;
                    }
                });
            }
        });

    }

    private ErrorInfoUploader() {
        FineLoggerFactory.getLogger().addLogAppender(new LogHandler<ErrorInfoLogAppender>() {
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
    private void checkUpdateSolution() {

        if (!VT4FR.AlphaFine.isSupport()) {
            return;
        }

        ExecutorService es = Executors.newSingleThreadExecutor(new NamedThreadFactory("ErrorInfoUploader"));
        es.submit(new Runnable() {
            @Override
            public void run() {
                String localCacheFilePath = StableUtils.pathJoin(ProductConstants.getEnvHome(), "solution", "solution.zip");
                File localCacheZip = new File(localCacheFilePath);
                if (needUpdate(localCacheZip)) {
                    downloadSolution(localCacheZip);
                }
            }
        });
        es.shutdown();
    }

    private void downloadSolution(File localCacheZip) {
        try {
            String downloadURL = CloudCenter.getInstance().acquireUrlByKind("solution.download");
            if (StringUtils.isBlank(downloadURL)) {
                return;
            }
            downloadURL = String.format("%s?token=%s", downloadURL, SiteCenterToken.generateToken());
            InputStream in = HttpToolbox.post(downloadURL, new HashMap<String, Object>(), HttpResponseType.STREAM);
            StableUtils.makesureFileExist(localCacheZip);
            FileOutputStream out = new FileOutputStream(localCacheZip);
            IOUtils.copyBinaryTo(in, out);
            out.close();
            in.close();

            IOUtils.unzip(localCacheZip, localCacheZip.getParent());
        } catch (Exception e) {
            FineLoggerFactory.getLogger().debug(e.getMessage());
        }

    }

    private boolean needUpdate(File localCacheZip) {
        if (localCacheZip.exists()) {
            // 判断本地文件大小.
            String checkURL = CloudCenter.getInstance().acquireUrlByKind("solution.check");
            if (StringUtils.isBlank(checkURL)) {
                return false;
            }
            checkURL = String.format("%s?token=%s", checkURL, SiteCenterToken.generateToken());
            try {
                JSONObject res = new JSONObject(HttpToolbox.get(checkURL));
                // 简单粗暴, 直接判断文件大小.
                return res.optLong("version") != localCacheZip.length();
            } catch (Exception ignore) {
            }
            return false;
        }
        return true;
    }

    public void sendErrorInfo() {
        // 判断更新解决方案缓存.
        checkUpdateSolution();

        //读取文件夹里的json, 加入上传队列中.
        File folder = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FOLDER_NAME));
        if (!folder.exists()) {
            return;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        if (ArrayUtils.getLength(files) > MAX_ERROR_SIZE) {
            CommonIOUtils.deleteFile(folder);
            return;
        }

        try {
            if (ArrayUtils.isNotEmpty(files)) {
                JSONArray jsonArray = new JSONArray();
                List<File> tempFiles = new ArrayList<>();
                int count = 0;
                for (File file : files) {
                    count++;
                    String filePath = file.getPath();
                    String suffix = filePath.substring(filePath.lastIndexOf("."));

                    if (suffix.endsWith(SUFFIX)) {
                        String content = IOUtils.inputStream2String(new FileInputStream(file));
                        if (content.length() > MAX_ERROR_SIZE) {
                            CommonIOUtils.deleteFile(file);
                            continue;
                        }
                        jsonArray.put(new JSONObject(content));
                        tempFiles.add(file);
                        if (jsonArray.length() == MAX_ITEMS || count == files.length) {
                            String url = CloudCenter.getInstance().acquireUrlByKind("design.error");
                            if (StringUtils.isBlank(url)) {
                                return;
                            }
                            if (sendErrorInfo(url, jsonArray)) {
                                deleteFiles(tempFiles);
                            }
                            jsonArray = new JSONArray();
                        }
                    }
                }
            }
        } catch (Exception ignore) {

        }
    }

    private void deleteFiles(List<File> files) {
        for (File file : files) {
            CommonIOUtils.deleteFile(file);
        }
    }

    private boolean sendErrorInfo(String url, JSONArray content) {
        HashMap<String, Object> para = new HashMap<>();
        para.put("token", SiteCenterToken.generateToken());
        para.put("content", content);

        try {
            String responseText = HttpToolbox.post(url, para);
            return "success".equals(new JSONObject(responseText).get("status"));
        } catch (Exception ignore) {

        }
        return false;
    }


}
