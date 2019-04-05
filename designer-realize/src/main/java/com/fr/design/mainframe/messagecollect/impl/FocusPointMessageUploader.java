package com.fr.design.mainframe.messagecollect.impl;

import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.messagecollect.utils.MessageCollectUtils;
import com.fr.intelli.record.FocusPoint;
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
    private static final String FOCUS_POINT_VERSION = "FocusPoint1003_";
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
    public <T> JSONObject dealWithSendFunctionContent(DataList<T> focusPoints) {
        return new JSONObject();
    }

    @Override
    public void sendToCloudCenter() {
        MessageCollectUtils.readXMLFile(instance, getLastTimeFile());
        long currentTime = new Date().getTime();
        long lastTIme = MessageCollectUtils.getLastTimeMillis(lastTime);
        try {
            generatePath();
            getData(currentTime, lastTIme, FocusPoint.class);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
        sendZipFile(getFolderName());
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

    private void generatePath(){
        DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
        String bbsUserName = MarketConfig.getInstance().getBbsUsername();
        String uuid = envManager.getUUID();
        //文件夹名称的格式是: "FocusPoint1003_" + uuid_bbsUserName_randomUuid，均以下划线分隔
        StringBuilder sb = new StringBuilder();
        sb.append(FOCUS_POINT_VERSION).append(uuid).append(SEPARATOR).append(bbsUserName).append(SEPARATOR).append(UUID.randomUUID());

        setFileName(String.valueOf(UUID.randomUUID()));
        setPathName(StableUtils.pathJoin(ProductConstants.getEnvHome(), sb.toString(), getFileName()));
        setFolderName(StableUtils.pathJoin(ProductConstants.getEnvHome(), sb.toString()));
    }
}
