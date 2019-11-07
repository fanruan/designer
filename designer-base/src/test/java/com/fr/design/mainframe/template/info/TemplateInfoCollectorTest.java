package com.fr.design.mainframe.template.info;

import com.fr.config.MarketConfig;
import com.fr.general.GeneralUtils;
import com.fr.invoke.Reflect;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import com.fr.third.org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.fr.design.mainframe.template.info.TemplateInfoTestHelper.assertJsonStringEquals;
import static com.fr.design.mainframe.template.info.TemplateInfoTestHelper.setUpMockForNewInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by plough on 2019/4/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ProductConstants.class, MarketConfig.class, ProductConstants.class, GeneralUtils.class})
public class TemplateInfoCollectorTest {
    private String filePath;
    private String initialFileContent;
    private TemplateProcessInfo mockProcessInfo;

    @Before
    public void setUp() throws IOException {
        PowerMock.mockStatic(ProductConstants.class);

        filePath = getClass().getResource("tpl.info").getPath();
        String dirPath = filePath.substring(0, filePath.indexOf("tpl.info"));
        EasyMock.expect(ProductConstants.getEnvHome()).andReturn(dirPath).anyTimes();
        EasyMock.replay();
        PowerMock.replayAll();

        mockProcessInfo = EasyMock.mock(TemplateProcessInfo.class);
        EasyMock.expect(mockProcessInfo.getBlockCount()).andReturn(3).anyTimes();
        EasyMock.expect(mockProcessInfo.getCellCount()).andReturn(13).anyTimes();
        EasyMock.expect(mockProcessInfo.getFloatCount()).andReturn(1).anyTimes();
        EasyMock.expect(mockProcessInfo.getReportType()).andReturn(0).anyTimes();
        EasyMock.expect(mockProcessInfo.getWidgetCount()).andReturn(0).anyTimes();
        EasyMock.replay(mockProcessInfo);

        // 缓存 tpl.info
        initialFileContent = FileUtils.readFileToString(new File(filePath), "utf-8");

        Reflect.on(TemplateInfoCollector.class).set("instance", null);
        // 后执行 testReadXML 用例时，之前保留的单例会造成影响
        Reflect.on(DesignerOpenHistory.class).set("singleton", null);
    }

    @After
    public void tearDown() throws IOException {
        // 恢复 tpl.info
        FileUtils.writeStringToFile(new File(filePath), initialFileContent, "utf-8");
    }

    @Test
    public void testReadXML() {
        assertEquals(",,", DesignerOpenHistory.getInstance().toString());

        TemplateInfoCollector collector = TemplateInfoCollector.getInstance();
        assertEquals(7, ((Map) Reflect.on(collector).field("templateInfoMap").get()).size());

        assertEquals("2019-04-08,2019-04-03,2019-03-29", DesignerOpenHistory.getInstance().toString());
    }

    @Test
    public void testCollectInfo() {
        TemplateInfoCollector collector = TemplateInfoCollector.getInstance();

        String templateID = "16a988ce-8529-42f5-b17c-2ee849355071";
        int timeConsume = 200;

        collector.collectInfo(templateID, StringUtils.EMPTY, mockProcessInfo, timeConsume);

        // 检查是否写入成功
        collector.loadFromFile();
        TemplateInfo templateInfo = collector.getOrCreateTemplateInfoByID(templateID);

        assertJsonStringEquals("{\"process\":\"\",\"float_count\":1,\"widget_count\":0," +
                "\"cell_count\":13,\"block_count\":3,\"report_type\":0," +
                "\"templateID\":\"16a988ce-8529-42f5-b17c-2ee849355071\"}", templateInfo.getProcessMapJsonString());

        assertJsonStringEquals("{\"activitykey\":\"2e0ea413-fa9c241e0-9723-4354fce51e81\"," +
                "\"jar_time\":\"不是安装版本\",\"create_time\":\"2019-03-26 16:13\"," +
                "\"templateID\":\"16a988ce-8529-42f5-b17c-2ee849355071\",\"originID\":\"\"," +
                "\"uuid\":\"476ca2cc-f789-4c5d-8e89-ef146580775c\",\"time_consume\":329,\"originTime\":0," +
                "\"version\":\"10.0\",\"username\":\"plough\"}", templateInfo.getConsumingMapJsonString());
    }

    @Test
    public void testCollectInfoForNewTemplate() throws Exception {
        setUpMockForNewInstance();

        TemplateInfoCollector collector = TemplateInfoCollector.getInstance();

        String templateID = "73a97777-8jnk-47cd-b57c-2ee89991279796";
        int timeConsume = 200;

        collector.collectInfo(templateID, StringUtils.EMPTY, mockProcessInfo, timeConsume);

        // 检查是否写入成功
        collector.loadFromFile();
        assertTrue(collector.contains(templateID));

        TemplateInfo templateInfo = collector.getOrCreateTemplateInfoByID(templateID);
        assertEquals(templateID, templateInfo.getTemplateID());

        assertJsonStringEquals("{\"process\":\"\",\"float_count\":1,\"widget_count\":0," +
                "\"cell_count\":13,\"block_count\":3,\"report_type\":0," +
                "\"templateID\":\"73a97777-8jnk-47cd-b57c-2ee89991279796\"}", templateInfo.getProcessMapJsonString());

        Map<String, Object> consumingMap = Reflect.on(templateInfo).field("consumingMap").get();
        assertEquals(templateID, consumingMap.get("templateID"));
        assertEquals(StringUtils.EMPTY, consumingMap.get("originID"));
        assertEquals(200, consumingMap.get("time_consume"));
        assertEquals(0, consumingMap.get("originTime"));
    }

    @Test
    public void testCollectInfoWhenSaveAs() throws Exception {
        setUpMockForNewInstance();

        TemplateInfoCollector collector = TemplateInfoCollector.getInstance();

        String templateID = "423238d4-5223-22vj-vlsj-42jc49245iw3";
        String originID = "16a988ce-8529-42f5-b17c-2ee849355071";
        int timeConsume = 200;

        collector.collectInfo(templateID, originID, mockProcessInfo, timeConsume);

        // 检查是否写入成功
        collector.loadFromFile();
        TemplateInfo templateInfo = collector.getOrCreateTemplateInfoByID(templateID);

        assertJsonStringEquals("{\"process\":\"\",\"float_count\":1,\"widget_count\":0," +
                "\"cell_count\":13,\"block_count\":3,\"report_type\":0," +
                "\"templateID\":\"423238d4-5223-22vj-vlsj-42jc49245iw3\"}", templateInfo.getProcessMapJsonString());

        Map<String, Object> consumingMap = Reflect.on(templateInfo).field("consumingMap").get();
        assertEquals(templateID, consumingMap.get("templateID"));
        assertEquals(originID, consumingMap.get("originID"));
        assertEquals(329, consumingMap.get("time_consume"));
        assertEquals(129, consumingMap.get("originTime"));
    }

    @Test
    public void testCollectInfoWhenSaveAsWithNoTrackOriginID() throws Exception {
        setUpMockForNewInstance();

        TemplateInfoCollector collector = TemplateInfoCollector.getInstance();

        String templateID = "423238d4-5223-22vj-vlsj-42jc49245iw3";
        String originID = "3kha8jcs-31xw-42f5-h2ww-2ee84935312z";
        int timeConsume = 200;

        collector.collectInfo(templateID, originID, mockProcessInfo, timeConsume);

        TemplateInfo templateInfo = collector.getOrCreateTemplateInfoByID(templateID);
        assertEquals(templateID, templateInfo.getTemplateID());
        assertEquals(originID, templateInfo.getOriginID());

        Map<String, Object> consumingMap = Reflect.on(templateInfo).field("consumingMap").get();
        assertEquals(templateID, consumingMap.get("templateID"));
        assertEquals(originID, consumingMap.get("originID"));
        assertEquals(200, consumingMap.get("time_consume"));
        assertEquals(0, consumingMap.get("originTime"));
    }

    @Test
    public void testAddIdleDateCount() {
        String templateID = "16a988ce-8529-42f5-b17c-2ee849355071";
        TemplateInfoCollector collecter = TemplateInfoCollector.getInstance();
        TemplateInfo templateInfo = collecter.getOrCreateTemplateInfoByID(templateID);

        assertEquals(9, templateInfo.getIdleDayCount());

        Reflect.on(collecter).call("addIdleDayCount");
        assertEquals(10, templateInfo.getIdleDayCount());

        // 同一天内多次调用无效
        Reflect.on(collecter).call("addIdleDayCount");
        assertEquals(10, templateInfo.getIdleDayCount());
    }
}
