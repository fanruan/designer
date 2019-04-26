package com.fr.design.mainframe.vcs;

import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * Created by XiaXiang on 2019/4/26.
 */
public class VcsConfigManager implements XMLReadable, XMLWriter {
    public static final String XML_TAG = "VcsConfigManager";
    private static volatile VcsConfigManager instance = new VcsConfigManager();
    private boolean vcsEnable;
    private boolean saveCommit;
    private boolean useInterval;
    private int saveInterval;

    public static VcsConfigManager getInstance() {
        return instance;
    }

    public boolean isVcsEnable() {
        return vcsEnable;
    }

    public void setVcsEnable(boolean vcsEnable) {
        this.vcsEnable = vcsEnable;
    }

    public boolean isSaveCommit() {
        return saveCommit;
    }

    public void setSaveCommit(boolean saveCommit) {
        this.saveCommit = saveCommit;
    }

    public boolean isUseInterval() {
        return useInterval;
    }

    public void setUseInterval(boolean useInterval) {
        this.useInterval = useInterval;
    }

    public int getSaveInterval() {
        return saveInterval;
    }

    public void setSaveInterval(int saveInterval) {
        this.saveInterval = saveInterval;
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isAttr()) {
            this.setSaveCommit(reader.getAttrAsBoolean("saveCommit", true));
            this.setSaveInterval(reader.getAttrAsInt("saveInterval", 60));
            this.setUseInterval(reader.getAttrAsBoolean("useInterval", true));
            this.setVcsEnable(reader.getAttrAsBoolean("vcsEnable", true));
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        writer.attr("saveCommit", this.isSaveCommit());
        writer.attr("saveInterval", this.getSaveInterval());
        writer.attr("useInterval", this.isUseInterval());
        writer.attr("vcsEnable", this.isVcsEnable());
        writer.end();
    }
}
