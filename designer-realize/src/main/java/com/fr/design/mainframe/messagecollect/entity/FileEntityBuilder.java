package com.fr.design.mainframe.messagecollect.entity;

import com.fr.general.CloudCenter;
import com.fr.general.IOUtils;
import com.fr.general.http.HttpRequestType;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CommonUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.third.org.apache.http.entity.mime.MultipartEntityBuilder;
import com.fr.third.org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;

/**
 * @author alex sung
 * @date 2019/4/8
 */
public class FileEntityBuilder {

    private static final String INTELLI_OPERATION_URL = "intelli.operation.url";
    private static final String OPERATION_URL = "https://cloud.fanruan.com/config/protect/operation";
    private static final String ATTR_SIGNATURE = "signature";
    private static final String ATTR_KEY = "key";
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件的完整路径
     */
    private String pathName;
    /**
     * 文件夹路径
     */
    private String folderName;

    public FileEntityBuilder(String fileName, String pathName, String folderName) {
        this.fileName = fileName;
        this.pathName = pathName;
        this.folderName = folderName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
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
            File file = new File(pathName);
            zipFile = new File(pathName + ".zip");
            InputStream input = null;
            java.util.zip.ZipOutputStream zipOut = null;
            zipOut = new java.util.zip.ZipOutputStream(new FileOutputStream(zipFile));
            int temp = 0;
            if (file.isDirectory()) {
                File lists[] = file.listFiles();
                for (int i = 0; i < lists.length; i++) {
                    input = new FileInputStream(lists[i]);
                    zipOut.putNextEntry(new ZipEntry(file.getName()
                            + File.separator + lists[i].getName()));
                    while ((temp = input.read()) != -1) {
                        zipOut.write(temp);
                    }
                    input.close();
                }
            }
            zipOut.close();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return zipFile;
    }

    public void generateFile(JSONArray jsonArray, String pathName) {
        try {
            String content = jsonArray.toString();
            File file = new File(pathName + ".json");
            StableUtils.makesureFileExist(file);
            FileOutputStream out = new FileOutputStream(file);
            InputStream in = new ByteArrayInputStream(content.getBytes(EncodeConstants.ENCODING_UTF_8));
            IOUtils.copyBinaryTo(in, out);
            out.close();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public void deleteFileAndZipFile(File zipFile, String pathName) {
        File file = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), pathName));
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
        String url = generateSignedUploadUrl("FocusPoint/"+keyFileName);
        if(StringUtils.isEmpty(url)){
            FineLoggerFactory.getLogger().error("url is null.");
        }else {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .addPart("file", new FileBody(file));
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/zip");
            HttpToolbox.upload(url, builder, Charset.forName("utf-8"), headers, HttpRequestType.PUT);
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
            FineLoggerFactory.getLogger().error("Illegal response text.");
        }
        return null;
    }
}
