package com.fr.design.mainframe.burying.point;

import com.fr.base.FRContext;
import com.fr.base.io.XMLReadHelper;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.template.info.SendHelper;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;
import com.fr.third.org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2020-02-21
 */
public abstract class AbstractPointCollector<T extends AbstractPointInfo> implements BasePointCollector {

    protected Map<String, T> pointInfoMap;

    private static final int MAX_SIZE = 512 * 1024 * 1024;

    public AbstractPointCollector() {
        pointInfoMap = new ConcurrentHashMap<>();
        loadFromFile();
    }

    /**
     * 获取是否满足触发埋点的要求
     */
    protected boolean shouldCollectInfo() {
        return FileUtils.sizeOf(getInfoFile()) <= MAX_SIZE && DesignerEnvManager.getEnvManager().isJoinProductImprove() && FRContext.isChineseEnv();
    }

    @Override
    public void sendPointInfo() {

        addIdleDayCount();

        List<String> removeList = new ArrayList<>();
        List<String> sendList = new ArrayList<>();

        for (String key : pointInfoMap.keySet()) {
            AbstractPointInfo pointInfo = pointInfoMap.get(key);
            pointInfo.selectPoint(removeList, sendList);
        }

        // 发送记录
        for (String key : sendList) {
            if(SendHelper.sendPointInfo(pointInfoMap.get(key))){
                removeList.add(key);
            }
        }

        // 清空记录
        for (String key : removeList) {
            pointInfoMap.remove(key);
        }

        saveInfo();
    }

    /**
     * 从文件中读取埋点信息
     */
    protected void loadFromFile() {
        if (!getInfoFile().exists()) {
            return;
        }

        XMLableReader reader = null;
        try (InputStream in = new FileInputStream(getInfoFile())) {
            // XMLableReader 还是应该考虑实现 Closable 接口的，这样就能使用 try-with 语句了
            reader = XMLReadHelper.createXMLableReader(in, XMLPrintWriter.XML_ENCODER);
            if (reader == null) {
                return;
            }
            reader.readXMLObject(this);
        } catch (FileNotFoundException e) {
            // do nothing
        } catch (XMLStreamException | IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (XMLStreamException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }

    /**
     * 保存埋点信息到文件中
     */
    protected void saveInfo() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XMLTools.writeOutputStreamXML(this, out);
            out.flush();
            out.close();
            String fileContent = new String(out.toByteArray(), StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(getInfoFile(), fileContent, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            FineLoggerFactory.getLogger().error(ex.getMessage());
        }
    }

    /**
     * 获取缓存文件存放路径
     */
    protected abstract File getInfoFile();

    protected abstract void addIdleDayCount();
}
