package com.fr.design.mainframe.messagecollect.entity;

import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.general.CloudCenter;
import com.fr.general.CloudClient;
import com.fr.general.IOUtils;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CommonUtils;
import com.fr.stable.CoreConstants;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.third.jodd.datetime.JDateTime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author alex sung
 * @date 2019/4/8
 */
public class FileEntityBuilder {

    private static final String FOCUS_POINT_FILE_ROOT_PATH = "FocusPoint";
    private static final String FOCUS_POINT_FILE_UPLOAD_TOPIC = "__fine_intelli_file_upload__";
    private static final String FILE_FROM = "design";
    private static final String FOCUS_POINT_FILE_UPLOAD_TYPE = "FocusPoint";
    private static final String FOCUS_POINT_FILE_UPLOAD_URL = CloudCenter.getInstance().acquireUrlByKind("design.feedback");
    private static final String FOCUS_POINT_URL_KEY = "focuspoint";

    /**
     * 文件夹路径
     */
    private String folderName;

    public FileEntityBuilder(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public File generateZipFile(String pathName) {
        if (pathName == null) {
            return null;
        }
        File zipFile = null;
        try {
            zipFile = new File(pathName + ".zip");
            java.util.zip.ZipOutputStream zipOut = new java.util.zip.ZipOutputStream(new FileOutputStream(zipFile));
            IOUtils.zip(zipOut, new File(pathName));
            zipOut.close();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return zipFile;
    }

    public void generateFile(JSONArray jsonArray, String folderName) {
        if (jsonArray.size() == 0) {
            return;
        }
        try {
            String content = jsonArray.toString();
            String fileName = String.valueOf(UUID.randomUUID());
            File file = new File(folderName + File.separator + fileName + ".json");
            StableUtils.makesureFileExist(file);
            FileOutputStream out = new FileOutputStream(file);
            InputStream in = new ByteArrayInputStream(content.getBytes(EncodeConstants.ENCODING_UTF_8));
            IOUtils.copyBinaryTo(in, out);
            in.close();
            out.close();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public void deleteFileAndZipFile(File zipFile, String pathName) {
        File file = new File(pathName);
        CommonUtils.deleteFile(file);
        CommonUtils.deleteFile(zipFile);
    }

    /**
     * 上传文件到云中心
     *
     * @param file        待上传文件
     * @param keyFileName 目标文件
     * @throws IOException
     */
    public static void uploadFile(File file, String keyFileName) throws IOException {
        CloudClient client = CloudClient.getInstance();
        String today = new JDateTime().toString("YYYY-MM-DD");
        String filePath = FOCUS_POINT_FILE_ROOT_PATH + CoreConstants.SEPARATOR + today + CoreConstants.SEPARATOR + keyFileName;
        String bbsUserName = MarketConfig.getInstance().getBbsUsername();
        String uuid = DesignerEnvManager.getEnvManager().getUUID();
        String name = StringUtils.isEmpty(bbsUserName) ? uuid : bbsUserName;

        client.uploadFile(file, filePath, name, FILE_FROM);
        addMessageQueue(filePath, bbsUserName, uuid);
    }

    private static void addMessageQueue(String filePath, String userName, String uuid) {
        JSONObject uploadInfo = new JSONObject(FOCUS_POINT_FILE_UPLOAD_URL);
        String focusPointUrl = uploadInfo.optString(FOCUS_POINT_URL_KEY);
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("topic", FOCUS_POINT_FILE_UPLOAD_TOPIC);
            params.put("username", URLEncoder.encode(userName, EncodeConstants.ENCODING_UTF_8));
            params.put("uuid", uuid);
            params.put("filepath", filePath);
            params.put("timestamp", String.valueOf(System.currentTimeMillis()));
            params.put("signature", String.valueOf(CommonUtils.signature()));
            params.put("type", FOCUS_POINT_FILE_UPLOAD_TYPE);
            if(StringUtils.isNotEmpty(focusPointUrl)){
                HttpToolbox.post(focusPointUrl, params);
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
}
