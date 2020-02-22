package com.fr.design.mainframe.template.info;

import com.fr.invoke.Reflect;
import com.fr.json.JSONObject;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;

import java.io.StringReader;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by plough on 2019/4/25.
 */
public class SendHelperTest {
    private static final String CONSUMING_URL = "http://cloud.fanruan.com/api/monitor/record_of_reports_consuming/single";
    private static final String PROCESS_URL = "http://cloud.fanruan.com/api/monitor/record_of_reports_process/single";

    private static final String NORMAL_INFO = "<TemplateInfo templateID=\"16a988ce-8529-42f5-b17c-2ee849355071\" day_count=\"9\">\n" +
            "<processMap process=\"\" float_count=\"0\" widget_count=\"0\" cell_count=\"1\" block_count=\"0\" report_type=\"0\"/>\n" +
            "<consumingMap activitykey=\"2e0ea413-fa9c241e0-9723-4354fce51e81\" jar_time=\"不是安装版本\" create_time=\"2019-03-26 16:13\" uuid=\"476ca2cc-f789-4c5d-8e89-ef146580775c\" time_consume=\"129\" version=\"10.0\" username=\"plough\"/>\n" +
            "</TemplateInfo>";

    // 只在调试的时候运行，不需要每次都自动运行
    public static void main(String[] args) throws XMLStreamException {
        StringReader sr = new StringReader(NORMAL_INFO);
        XMLableReader xmlReader = XMLableReader.createXMLableReader(sr);
        TemplateInfo templateInfo = TemplateInfo.newInstanceByRead(xmlReader);
        Map consumingMap = Reflect.on(templateInfo).field("consumingMap").get();
        Map processMap = Reflect.on(templateInfo).field("processMap").get();
        boolean res = Reflect.on(SendHelper.class).call("sendSinglePointInfo", CONSUMING_URL, new JSONObject(consumingMap).toString()).get();
        assertTrue(res);

        boolean res2 = Reflect.on(SendHelper.class).call("sendSinglePointInfo", PROCESS_URL, new JSONObject(processMap).toString()).get();
        assertTrue(res2);
    }
}
