package com.fr.design.mainframe.messagecollect.impl;

import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
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
    private static final String SEPARATOR = "_";
    private static final String FOCUS_POINT = "FocusPoint";
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
        JSONArray ja = new JSONArray();
        for(T t:focusPoints.getList()){
            FocusPoint focusPoint = (FocusPoint)t;
            JSONObject jo = new JSONObject();
            jo.put("id",focusPoint.getId());
            jo.put("text",focusPoint.getId());
            jo.put("source",focusPoint.getId());
            jo.put("time",focusPoint.getId());
            jo.put("username",focusPoint.getId());
            jo.put("ip",focusPoint.getId());
            jo.put("title",focusPoint.getId());
            jo.put("body",focusPoint.getId());
            ja.put(jo);
        }
        return ja;
    }

    public void sendToCloudCenter() {
        MessageCollectUtils.readXMLFile(instance, getLastTimeFile());
        long currentTime = new Date().getTime();
        long lastTIme = MessageCollectUtils.getLastTimeMillis(lastTime);
        try {
            generatePath();
            queryData(currentTime, lastTIme, FocusPoint.class);
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
        DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
        String bbsUserName = MarketConfig.getInstance().getBbsUsername();
        String uuid = envManager.getUUID();
        //文件夹名称的格式是: "FocusPoint" + 大版本号 + 小版本号 + uuid + bbsUserName + randomUuid，均以下划线分隔
        StringBuilder sb = new StringBuilder();
        sb.append(FOCUS_POINT).append(SEPARATOR).
                append(ProductConstants.MAIN_VERSION).append(SEPARATOR).
                append(ProductConstants.MINOR_VERSION).append(SEPARATOR).
                append(uuid).append(SEPARATOR).append(bbsUserName).append(SEPARATOR).
                append(UUID.randomUUID());
        String fileName = String.valueOf(UUID.randomUUID());
        String pathName = StableUtils.pathJoin(ProductConstants.getEnvHome(), sb.toString(), fileName);
        String folderName = StableUtils.pathJoin(ProductConstants.getEnvHome(), sb.toString());
        setFileEntityBuilder(new FileEntityBuilder(fileName, pathName, folderName));
    }
}
