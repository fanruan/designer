package com.fr.design.mainframe.messagecollect.impl;

import com.fr.design.mainframe.messagecollect.SendDataToCloudProvider;
import com.fr.design.mainframe.messagecollect.entity.FileEntity;
import com.fr.design.mainframe.messagecollect.utils.MessageCollectUtils;
import com.fr.general.CloudCenter;
import com.fr.general.IOUtils;
import com.fr.general.http.HttpRequestType;
import com.fr.general.http.HttpToolbox;
import com.fr.intelli.record.MetricRegistry;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CommonUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.query.QueryFactory;
import com.fr.stable.query.condition.QueryCondition;
import com.fr.stable.query.data.DataList;
import com.fr.stable.query.restriction.RestrictionFactory;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLable;
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
 * @date 2019/3/22
 */
public abstract class AbstractSendDataToCloud implements SendDataToCloudProvider, XMLable {

    private static final String INTELLI_OPERATION_URL = "intelli.operation.url";
    private static final String OPERATION_URL = "https://cloud.fanruan.com/config/protect/operation";
    private static final String ATTR_SIGNATURE = "signature";
    private static final String ATTR_KEY = "key";
    private static final String FILE_NAME = "messagecollect.info";
    private static final String COLUMN_TIME = "time";

    protected String lastTime;
    private static final int PAGE_SIZE = 200;
    private long totalCount = -1;
    private FileEntity fileEntity;

    public FileEntity getFileEntity() {
        return fileEntity;
    }

    public void setFileEntity(FileEntity fileEntity) {
        this.fileEntity = fileEntity;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public void saveLastTime() {
        try {
            FileOutputStream out = new FileOutputStream(getLastTimeFile());
            XMLTools.writeOutputStreamXML(this, out);
        } catch (Exception ex) {
            FineLoggerFactory.getLogger().error(ex.getMessage());
        }
    }

    public static File getLastTimeFile() {
        return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FILE_NAME));
    }

    @Override
    public <T> void queryData(long currentTime, long lastTime, Class<T> tClass) {
        queryAndSendOnePageFunctionContent(currentTime, lastTime, 0, tClass);
        long page = (totalCount / PAGE_SIZE) + 1;
        for (int i = 1; i < page; i++) {
            queryAndSendOnePageFunctionContent(currentTime, lastTime, i, tClass);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private <T> void queryAndSendOnePageFunctionContent(long current, long last, int page, Class<T> tClass) {
        QueryCondition condition = QueryFactory.create()
                .skip(page * PAGE_SIZE)
                .count(PAGE_SIZE)
                .addSort(COLUMN_TIME, true)
                .addRestriction(RestrictionFactory.lte(COLUMN_TIME, current))
                .addRestriction(RestrictionFactory.gte(COLUMN_TIME, last));
        try {
            DataList<T> points = MetricRegistry.getMetric().find(tClass, condition);
            //第一次查询获取总记录数
            if (page == 0) {
                totalCount = points.getTotalCount();
            }
            dealWithData(points);

        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    @Override
    public <T> void dealWithData(DataList<T> tDataList) throws Exception {
        generateThisPageFile(tDataList);
    }

    private <T> void generateThisPageFile(DataList<T> points) {
        File file = null;
        try {
            JSONObject jsonObject = dealWithSendFunctionContent(points);
            //生成json文件
            generateFile(jsonObject);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public abstract <T> JSONObject dealWithSendFunctionContent(DataList<T> focusPoints);

    /**
     * 生成zip并发送zip文件
     * @param pathName zip文件路径
     */
    protected void sendZipFile(String pathName) {

        File file = null;
        try {
            file = generateZipFile(pathName);
            if (file != null) {
                uploadFile(file, file.getName());
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return;
        }
        deleteFileAndZipFile(file, pathName);
    }

    private File generateZipFile(String pathName) {
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

    private void generateFile(JSONObject jsonObject) {
        try {
            String content = jsonObject.toString();
            File file = new File(getFileEntity().getPathName() + ".json");
            StableUtils.makesureFileExist(file);
            FileOutputStream out = new FileOutputStream(file);
            InputStream in = new ByteArrayInputStream(content.getBytes(EncodeConstants.ENCODING_UTF_8));
            IOUtils.copyBinaryTo(in, out);
            out.close();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private static void uploadFile(File file, String keyFileName) throws IOException {
        String url = generateSignedUploadUrl(keyFileName);
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

    private void deleteFileAndZipFile(File zipFile, String pathName) {
        File file = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), pathName));
        MessageCollectUtils.deleteDir(file);
        CommonUtils.deleteFile(zipFile);
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
