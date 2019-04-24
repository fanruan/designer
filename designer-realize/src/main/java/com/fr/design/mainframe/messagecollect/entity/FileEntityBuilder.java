package com.fr.design.mainframe.messagecollect.entity;

import com.fr.general.CloudCenter;
import com.fr.general.IOUtils;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CommonUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.third.jodd.datetime.JDateTime;
import com.fr.third.org.apache.http.HttpEntity;
import com.fr.third.org.apache.http.HttpResponse;
import com.fr.third.org.apache.http.client.HttpClient;
import com.fr.third.org.apache.http.client.methods.HttpPut;
import com.fr.third.org.apache.http.entity.FileEntity;
import com.fr.third.org.apache.http.impl.client.DefaultHttpClient;
import com.fr.third.org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.fr.third.org.apache.http.HttpStatus.SC_OK;

/**
 * @author alex sung
 * @date 2019/4/8
 */
public class FileEntityBuilder {

    private static final String INTELLI_OPERATION_URL = "intelli.operation.url";
    private static final String OPERATION_URL = "https://cloud.fanruan.com/config/protect/operation";
    private static final String ATTR_SIGNATURE = "signature";
    private static final String ATTR_KEY = "key";
    private static final String FOCUS_POINT_FILE_ROOT_PATH = "FocusPoint";

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
     * @param file 待上传文件
     * @param keyFileName 目标文件
     * @throws IOException
     */
    public static void uploadFile(File file, String keyFileName) throws IOException {
        String today = new JDateTime().toString("YYYY-MM-DD");
        HttpClient httpclient = new DefaultHttpClient();
        try {
            String signedUrl = generateSignedUploadUrl(FOCUS_POINT_FILE_ROOT_PATH + File.separator + today + File.separator +keyFileName);
            if(StringUtils.isEmpty(signedUrl)){
                FineLoggerFactory.getLogger().error("signedUrl is null.");
                return;
            }
            HttpPut httpPost = new HttpPut(signedUrl);
            httpPost.addHeader("Content-Type","application/octet-stream");
            FileEntity fileEntity = new FileEntity(file);
            httpPost.setEntity(fileEntity);
            HttpResponse response = httpclient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == SC_OK) {
                HttpEntity resEntity = response.getEntity();
                EntityUtils.consume(resEntity);
            } else {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "utf-8");
                FineLoggerFactory.getLogger().info("upload file result：" + result);
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private static String generateSignedUploadUrl(String fileKeyName) throws IOException {
        String url = CloudCenter.getInstance().acquireUrlByKind(INTELLI_OPERATION_URL, OPERATION_URL);
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(ATTR_KEY, fileKeyName);
        parameters.put(ATTR_SIGNATURE, String.valueOf(CommonUtils.signature()));
        String responseText = HttpToolbox.get(url, parameters);
        try {
            JSONObject data = new JSONObject(responseText);
            if ("success".equals(data.optString("status"))) {
                return data.optString("url");
            }
        } catch (JSONException e) {
            FineLoggerFactory.getLogger().error("Illegal response text."+e, e.getMessage());
        }
        return null;
    }
}
