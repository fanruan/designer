package com.fr.design.onlineupdate.push;

import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * 持久化与设计器自动推送更新相关的配置
 * Created by plough on 2019/4/8.
 */
public class DesignerPushUpdateConfigManager implements XMLReadable, XMLWriter {
    public static final String XML_TAG = "DesignerPushUpdateConfigManager";
    private static DesignerPushUpdateConfigManager singleton;

    private boolean autoPushUpdateEnabled = true;  // 是否开启自动推送更新
    private String lastIgnoredVersion = StringUtils.EMPTY;  // 最近一次跳过的更新版本

    private DesignerPushUpdateConfigManager() {
    }

    public static DesignerPushUpdateConfigManager getInstance() {
        if (singleton == null) {
            singleton = new DesignerPushUpdateConfigManager();
        }
        return singleton;
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isAttr()) {
            this.setAutoPushUpdateEnabled(reader.getAttrAsBoolean("autoPushUpdateEnabled", true));
            this.setLastIgnoredVersion(reader.getAttrAsString("lastIgnoredVersion", StringUtils.EMPTY));
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        writer.attr("autoPushUpdateEnabled", autoPushUpdateEnabled);
        writer.attr("lastIgnoredVersion", lastIgnoredVersion);
        writer.end();
    }

    public boolean isAutoPushUpdateEnabled() {
        return autoPushUpdateEnabled;
    }

    public void setAutoPushUpdateEnabled(boolean autoPushUpdateEnabled) {
        this.autoPushUpdateEnabled = autoPushUpdateEnabled;
    }

    public String getLastIgnoredVersion() {
        return lastIgnoredVersion;
    }

    public void setLastIgnoredVersion(String lastIgnoredVersion) {
        this.lastIgnoredVersion = lastIgnoredVersion;
    }
}
