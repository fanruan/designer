package com.fr.design.mainframe.messagecollect.impl;

import com.fr.design.mainframe.messagecollect.entity.FileEntityBuilder;
import com.fr.design.mainframe.messagecollect.utils.MessageCollectUtils;
import com.fr.intelli.record.FocusPoint;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.query.data.DataList;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.Date;
import java.util.UUID;

/**
 * @author alex sung
 * @date 2019/3/22
 */
public class FocusPointMessageUploader extends AbstractSendDataToCloud {

    private static final String TAG = "FocusPointMessageTag";
    private static final long DELTA = 24 * 3600 * 1000L;
    private static volatile FocusPointMessageUploader instance;

    public static FocusPointMessageUploader getInstance() {
        if (instance == null) {
            synchronized (FocusPointMessageUploader.class) {
                if (instance == null) {
                    instance = new FocusPointMessageUploader();
                }
            }
        }
        return instance;
    }

    @Override
    public <T> JSONArray dealWithSendFunctionContent(DataList<T> focusPoints) {
        if (focusPoints == null) {
            return null;
        }
        JSONArray ja = new JSONArray();
        for(T t:focusPoints.getList()){
            FocusPoint focusPoint = (FocusPoint)t;
            JSONObject jo = new JSONObject();
            jo.put("id",focusPoint.getId());
            jo.put("text",focusPoint.getText());
            jo.put("source",focusPoint.getSource());
            jo.put("time",focusPoint.getTime());
            jo.put("username",focusPoint.getUsername());
            jo.put("ip",focusPoint.getIp());
            jo.put("title",focusPoint.getTitle());
            jo.put("body",focusPoint.getBody());
            ja.put(jo);
        }
        return ja;
    }

    public void sendToCloudCenter() {
        MessageCollectUtils.readXMLFile(instance, getLastTimeFile());
        long currentTime = new Date().getTime();
        long lastTime = MessageCollectUtils.getLastTimeMillis(this.lastTime);
        if (currentTime - lastTime <= DELTA) {
            return;
        }
        try {
            generatePath();
            queryData(currentTime, lastTime, FocusPoint.class);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
        sendZipFile(getFileEntityBuilder().getFolderName());
        saveLastTime();
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isAttr()) {
            this.setLastTime(reader.getAttrAsString("focusPointLastTime", null));
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(TAG);
        writer.attr("focusPointLastTime", lastTime);
        writer.end();
    }

    private void generatePath() {
        //文件夹名称是uuid.zip，版本信息已经在edition中体现了
        String folderName = StableUtils.pathJoin(ProductConstants.getEnvHome(), String.valueOf(UUID.randomUUID()));
        setFileEntityBuilder(new FileEntityBuilder(folderName));
    }
}
