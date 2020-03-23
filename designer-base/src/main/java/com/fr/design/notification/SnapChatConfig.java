package com.fr.design.notification;

import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

import java.util.HashMap;
import java.util.Map;

/**
 * created by Harrison on 2020/03/16
 **/
@SuppressWarnings("unchecked")
public class SnapChatConfig implements XMLable {
    
    public static final String XML_TAG = "SnapChatConfig";
    
    /**
     * 已经阅读过的属性
     */
    private Map<String, Boolean> markReadMap = new HashMap<>(8);
    
    private static final SnapChatConfig INSTANCE = new SnapChatConfig();
    
    public static SnapChatConfig getInstance() {
        return INSTANCE;
    }
    
    public Boolean hasRead(String key) {
        
        Map<String, Boolean> map = markReadMap;
        Boolean val = map.get(key);
        return val == null ? Boolean.FALSE : val;
    }
    
    public void markRead(String key) {
        
        markReadMap.put(key, Boolean.TRUE);
    }
    
    @Override
    public void readXML(XMLableReader reader) {
    
        String tagName = reader.getTagName();
        if ("MarkReadMap".equals(tagName)) {
            readMarkReadMapXML(reader);
        }
    }
    
    @Override
    public void writeXML(XMLPrintWriter writer) {
        
        writer.startTAG(XML_TAG);
        writeMarkReadMapXML(writer);
        writer.end();
    }
    
    private void readMarkReadMapXML(XMLableReader reader) {
        
        reader.readXMLObject(new XMLReadable() {
            @Override
            public void readXML(XMLableReader reader) {
                String tmpVal = reader.getElementValue();
                Boolean markRead = Boolean.valueOf(tmpVal);
                markReadMap.put(reader.getAttrAsString("key", ""), markRead);
            }
        });
    }
    
    /**
     * 写入map
     */
    private void writeMarkReadMapXML(XMLPrintWriter writer) {
        
        writer.startTAG("MarkReadMap");
        for (Map.Entry<String, Boolean> item : markReadMap.entrySet()) {
            writer.startTAG("item").attr("key", item.getKey()).textNode(item.getValue().toString()).end();
        }
        writer.end();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        
        return null;
    }
}
