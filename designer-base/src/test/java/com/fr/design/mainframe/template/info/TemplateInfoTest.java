package com.fr.design.mainframe.template.info;

import com.fr.config.MarketConfig;
import com.fr.general.GeneralUtils;
import com.fr.invoke.Reflect;
import com.fr.json.JSONObject;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.StringReader;
import java.util.Map;

import static com.fr.design.mainframe.template.info.TemplateInfoTestHelper.assertJsonStringEquals;
import static com.fr.design.mainframe.template.info.TemplateInfoTestHelper.setUpMockForNewInstance;
import static org.junit.Assert.assertEquals;

/**
 * Created by plough on 2019/4/19.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MarketConfig.class, ProductConstants.class, GeneralUtils.class})
public class TemplateInfoTest {

    private static final String NORMAL_INFO = "<TemplateInfo templateID=\"16a988ce-8529-42f5-b17c-2ee849355071\" day_count=\"9\">\n" +
            "<processMap process=\"\" float_count=\"0\" widget_count=\"0\" cell_count=\"1\" block_count=\"0\" report_type=\"0\"/>\n" +
            "<consumingMap activitykey=\"2e0ea413-fa9c241e0-9723-4354fce51e81\" jar_time=\"不是安装版本\" create_time=\"2019-03-26 16:13\" uuid=\"476ca2cc-f789-4c5d-8e89-ef146580775c\" time_consume=\"129\" version=\"10.0\" username=\"plough\"/>\n" +
            "</TemplateInfo>";

    private static final String SAVE_AS_INFO = "<TemplateInfo templateID=\"49avd2c4-1104-92j2-wx24-3dd0k2136080\" originID=\"16a988ce-8529-42f5-b17c-2ee849355071\" day_count=\"9\">\n" +
            "<processMap process=\"\" float_count=\"0\" widget_count=\"0\" cell_count=\"1\" block_count=\"0\" report_type=\"0\"/>\n" +
            "<consumingMap activitykey=\"2e0ea413-fa9c241e0-9723-4354fce51e81\" jar_time=\"不是安装版本\" create_time=\"2019-03-26 16:13\" uuid=\"476ca2cc-f789-4c5d-8e89-ef146580775c\" time_consume=\"429\" originTime=\"129\" version=\"10.0\" username=\"plough\"/>\n" +
            "</TemplateInfo>";

    private TemplateInfo templateInfo;
    private TemplateInfo templateInfoSaveAs;  // 另存为的模版记录

    @Before
    public void setUp() throws XMLStreamException {
        templateInfo = createTemplateInfo(NORMAL_INFO);
        templateInfoSaveAs = createTemplateInfo(SAVE_AS_INFO);
    }

    @Test
    public void testNewInstance() throws Exception {
        setUpMockForNewInstance();

        String templateID = "24avc8n2-1iq8-iuj2-wx24-8yy0i8132302";
        TemplateInfo templateInfo = TemplateInfo.newInstance(templateID);
        assertEquals(templateID, templateInfo.getTemplateID());
        assertEquals(StringUtils.EMPTY, Reflect.on(templateInfo).field("originID").get());
        assertEquals(0, (int) Reflect.on(templateInfo).field("idleDayCount").get());

        Map<String, Object> consumingMap = Reflect.on(templateInfo).field("consumingMap").get();
        assertEquals(templateID, consumingMap.get("templateID"));
        assertEquals(0, consumingMap.get("originTime"));
        assertEquals(StringUtils.EMPTY, consumingMap.get("originID"));
        assertEquals(0, consumingMap.get("time_consume"));
        assertEquals("不是安装版本", consumingMap.get("jar_time"));
        assertEquals("plough", consumingMap.get("username"));
        assertEquals("10.0", consumingMap.get("version"));
    }

    @Test
    public void testNewInstanceWithMoreArgs() throws Exception {
        setUpMockForNewInstance();

        String templateID = "24121212-u2c8-ncd2-82nx-8ud0i8138888";
        String originID = "24avc8n2-1iq8-iuj2-wx24-8yy0i8132302";
        int originTime = 100;
        TemplateInfo templateInfo = TemplateInfo.newInstance(templateID, originID, originTime);
        assertEquals(templateID, templateInfo.getTemplateID());
        assertEquals(originID, Reflect.on(templateInfo).field("originID").get());
        assertEquals(0, (int) Reflect.on(templateInfo).field("idleDayCount").get());

        Map<String, Object> consumingMap = Reflect.on(templateInfo).field("consumingMap").get();
        assertEquals(templateID, consumingMap.get("templateID"));
        assertEquals(originTime, consumingMap.get("originTime"));
        assertEquals(originID, consumingMap.get("originID"));
        assertEquals(originTime, consumingMap.get("time_consume"));
        assertEquals("不是安装版本", consumingMap.get("jar_time"));
        assertEquals("plough", consumingMap.get("username"));
        assertEquals("10.0", consumingMap.get("version"));
    }

    @Test
    public void testGetTemplateID() {
        assertEquals("16a988ce-8529-42f5-b17c-2ee849355071", templateInfo.getTemplateID());
        assertEquals("49avd2c4-1104-92j2-wx24-3dd0k2136080", templateInfoSaveAs.getTemplateID());
    }

    @Test
    public void testGetSendInfo() {

        Map consumingMap = Reflect.on(templateInfo).field("consumingMap").get();
        Map processMap = Reflect.on(templateInfo).field("processMap").get();
        Map consumingMap1 = Reflect.on(templateInfoSaveAs).field("consumingMap").get();
        Map processMap1 = Reflect.on(templateInfoSaveAs).field("processMap").get();
        assertJsonStringEquals("{\"activitykey\":\"2e0ea413-fa9c241e0-9723-4354fce51e81\",\"jar_time\":\"不是安装版本\"," +
                "\"create_time\":\"2019-03-26 16:13\",\"templateID\":\"16a988ce-8529-42f5-b17c-2ee849355071\",\"originID\":\"\"," +
                "\"uuid\":\"476ca2cc-f789-4c5d-8e89-ef146580775c\",\"time_consume\":129,\"originTime\":0,\"version\":\"10.0\"," +
                "\"username\":\"plough\"}", new JSONObject(consumingMap).toString());

        assertJsonStringEquals("{\"activitykey\":\"2e0ea413-fa9c241e0-9723-4354fce51e81\",\"jar_time\":\"不是安装版本\"," +
                "\"create_time\":\"2019-03-26 16:13\",\"templateID\":\"49avd2c4-1104-92j2-wx24-3dd0k2136080\",\"originID\":\"16a988ce-8529-42f5-b17c-2ee849355071\"," +
                "\"uuid\":\"476ca2cc-f789-4c5d-8e89-ef146580775c\",\"time_consume\":429,\"originTime\":129,\"version\":\"10.0\"," +
                "\"username\":\"plough\"}", new JSONObject(consumingMap1).toString());
        assertJsonStringEquals("{\"process\":\"\",\"float_count\":0,\"widget_count\":0,\"cell_count\":1," +
                "\"block_count\":0,\"report_type\":0,\"templateID\":\"16a988ce-8529-42f5-b17c-2ee849355071\"}", new JSONObject(processMap).toString());
        assertJsonStringEquals("{\"process\":\"\",\"float_count\":0,\"widget_count\":0,\"cell_count\":1," +
                "\"block_count\":0,\"report_type\":0,\"templateID\":\"49avd2c4-1104-92j2-wx24-3dd0k2136080\"}", new JSONObject(processMap1).toString());
    }

    private TemplateInfo createTemplateInfo(String xmlContent) throws XMLStreamException {
        StringReader sr = new StringReader(xmlContent);
        XMLableReader xmlReader = XMLableReader.createXMLableReader(sr);
        return TemplateInfo.newInstanceByRead(xmlReader);
    }
}
