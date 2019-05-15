package com.fr.design.mainframe.template.info;

import com.fr.invoke.Reflect;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by plough on 2019/4/21.
 */
public class DesignerOpenHistoryTest {
    private DesignerOpenHistory openHistory;
    private String[] mockHistory = new String[] {
            "2019-04-08", "2019-04-03", "2019-03-29"
    };

    @Before
    public void setUp() {
        openHistory = DesignerOpenHistory.getInstance();
        Reflect.on(openHistory).set("history", mockHistory);
    }

    @Test
    public void testReadAndWrite() throws XMLStreamException {
        // 写入 xml
        StringWriter sw = new StringWriter();
        XMLPrintWriter writer = XMLPrintWriter.create(new PrintWriter(sw));
        openHistory.writeXML(writer);
        writer.flush();
        writer.close();

        String xmlText = sw.getBuffer().toString();

        // 临时修改配置
        Reflect.on(openHistory).set("history", new String[] {"", "", ""});

        // 从 xml 中读取
        StringReader sr = new StringReader(xmlText);
        XMLableReader xmlReader = XMLableReader.createXMLableReader(sr);
        xmlReader.readXMLObject(openHistory);

        // 验证：与写入时的配置一致
        assertArrayEquals(mockHistory, (String[])Reflect.on(openHistory).field("history").get());
    }

    @Test
    public void testToString() {
        assertEquals("2019-04-08,2019-04-03,2019-03-29", openHistory.toString());
    }

    @Test
    public void testParseString() {
        String[] mockDates = {"2020-04-08", "2019-04-03", "2016-03-29"};
        Reflect.on(openHistory).call("parseString", StringUtils.join(",", mockDates));
        assertArrayEquals(mockDates, (String[])Reflect.on(openHistory).field("history").get());
    }

    @Test
    public void testGetHistorySpanDayCount() {
        assertEquals(11, openHistory.getHistorySpanDayCount());

        Reflect.on(openHistory).set("history", new String[] {"2019-05-03", "2019-05-02", ""});
        assertEquals(2, openHistory.getHistorySpanDayCount());

        Reflect.on(openHistory).set("history", new String[] {"2019-05-03", "", ""});
        assertEquals(1, openHistory.getHistorySpanDayCount());

        try {
            Reflect.on(openHistory).set("history", new String[] {"", "", ""});
            fail("should not be here");
        } catch (AssertionError ignore) {
        }
    }

    @Test
    public void testOpenEnoughTimesInPeriod() {
        assertTrue(openHistory.isOpenEnoughTimesInPeriod(15));
        assertFalse(openHistory.isOpenEnoughTimesInPeriod(3));

        Reflect.on(openHistory).set("history", new String[] {"2019-05-03", "2019-05-02", ""});
        assertFalse(openHistory.isOpenEnoughTimesInPeriod(15));

        Reflect.on(openHistory).set("history", new String[] {"2019-05-03", "", ""});
        assertFalse(openHistory.isOpenEnoughTimesInPeriod(15));
    }

    @Test
    public void testHasOpenedToday() {
        assertFalse(openHistory.hasOpenedToday());

        Reflect.on(openHistory).set("history", new String[] {getToday(), "2019-02-02", ""});
        assertTrue(openHistory.hasOpenedToday());
    }

    @Test
    public void testUpdate() {
        openHistory.update();
        String[] arr = { getToday(), "2019-04-08", "2019-04-03" };
        assertArrayEquals(arr, (String[])Reflect.on(openHistory).field("history").get());
    }

    private String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }
}
