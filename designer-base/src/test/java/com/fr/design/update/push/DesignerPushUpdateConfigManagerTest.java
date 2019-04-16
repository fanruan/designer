package com.fr.design.update.push;

import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


/**
 * Created by plough on 2019/4/8.
 */
public class DesignerPushUpdateConfigManagerTest {

    @Test
    public void testSingleton() {
        DesignerPushUpdateConfigManager m1 = DesignerPushUpdateConfigManager.getInstance();
        DesignerPushUpdateConfigManager m2 = DesignerPushUpdateConfigManager.getInstance();
        assertSame(m1, m2);
    }

    @Test
    public void testDefaultValue() throws XMLStreamException {
        DesignerPushUpdateConfigManager configManager = DesignerPushUpdateConfigManager.getInstance();
        XMLableReader xmlReader = XMLableReader.createXMLableReader(new StringReader("<xml></xml>"));
        xmlReader.readXMLObject(configManager);

        assertEquals(StringUtils.EMPTY, configManager.getLastIgnoredVersion());
        assertTrue(configManager.isAutoPushUpdateEnabled());
    }

    @Test
    public void testReadAndWrite() throws XMLStreamException {
        final String initLastIngnoredVersion = "1.1.2";
        final boolean initAutoPushEnabled = false;

        DesignerPushUpdateConfigManager configManager = DesignerPushUpdateConfigManager.getInstance();

        configManager.setLastIgnoredVersion(initLastIngnoredVersion);
        configManager.setAutoPushUpdateEnabled(initAutoPushEnabled);

        // 写入 xml
        StringWriter sw = new StringWriter();
        XMLPrintWriter writer = XMLPrintWriter.create(new PrintWriter(sw));
        configManager.writeXML(writer);
        writer.flush();
        writer.close();

        String xml_str = sw.getBuffer().toString();

        // 临时修改配置
        configManager.setAutoPushUpdateEnabled(true);
        configManager.setLastIgnoredVersion("0.20.1");

        // 从 xml 中读取
        StringReader sr = new StringReader(xml_str);
        XMLableReader xmlReader = XMLableReader.createXMLableReader(sr);
        xmlReader.readXMLObject(configManager);

        // 验证：与写入时的配置一致
        assertEquals(initLastIngnoredVersion, configManager.getLastIgnoredVersion());
        assertEquals(initAutoPushEnabled, configManager.isAutoPushUpdateEnabled());
    }
}
