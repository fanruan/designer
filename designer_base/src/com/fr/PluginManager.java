package com.fr.plugin;

import com.fr.design.actions.UpdateAction;
import com.fr.file.XMLFileManager;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-20
 * Time: 下午5:05
 */
public class PluginManager extends XMLFileManager {

    private static PluginManager pluginManager = null;

    private String extensionPoint = StringUtils.EMPTY;

    private ArrayList<UpdateAction> resultList = new ArrayList<UpdateAction>();


    public synchronized static PluginManager getInstance() {
        if (pluginManager == null) {
            pluginManager = new PluginManager();
        }
        return pluginManager;
    }


    public PluginManager() {

    }

    /**
     * 文件名
     * @return  文件名
     */
    public String fileName() {
        return "plugin.xml";
    }


    public void setExtensionPoint(String point) {
        extensionPoint = point;
        resultList.clear();
        pluginManager.readXMLFile();
    }


    @Override
    public void readXML(XMLableReader reader) {
        if (extensionPoint == StringUtils.EMPTY) {
            return;
        }
        if (reader.getTagName().equals("PluginManager")) {
            reader.readXMLObject(new XMLReadable() {
                @Override
                public void readXML(XMLableReader reader) {
                    readExtension(reader);
                }
            });
        }


    }

    private void readExtension(XMLableReader reader) {
        if (reader.isChildNode()) {
            if (reader.getTagName().equals("Extension")) {
                String name = null, tmpVal = null;
                if ((tmpVal = reader.getAttrAsString("position", null)) != null) {
                    name = tmpVal;
                }
                if (!ComparatorUtils.equals(name, extensionPoint)) {
                    return;
                }
                reader.readXMLObject(new XMLReadable() {
                    @Override
                    public void readXML(XMLableReader reader) {
                        readActions(reader);
                    }
                });

            }
        }
    }

    private void readActions(XMLableReader reader) {
        if (reader.isChildNode()) {
            if (reader.getTagName().equals("Action")) {
                String name = null, tmpVal = null;
                if ((tmpVal = reader.getAttrAsString("class", null)) != null) {
                    name = tmpVal;
                }
                //读取模板数据集菜单
                if (name.isEmpty()) {
                    return;
                }
                try {
                    UpdateAction action = (UpdateAction) GeneralUtils.classForName(name).newInstance();
                    PluginManager.this.resultList.add(action);
                } catch (Exception exp) {
                    FRLogger.getLogger().error(exp.getMessage(), exp);
                }
            }
        }

    }


    public ArrayList<UpdateAction> getResultList() {
        return resultList;
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
    }

}