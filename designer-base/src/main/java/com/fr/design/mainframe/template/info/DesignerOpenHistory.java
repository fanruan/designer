package com.fr.design.mainframe.template.info;

import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLWriter;
import com.fr.stable.xml.XMLableReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 管理打开设计器的日期记录
 * Created by plough on 2019/4/19.
 */
class DesignerOpenHistory implements XMLReadable, XMLWriter {
    static final String XML_TAG = "DesignerOpenHistory";
    private static DesignerOpenHistory singleton;
    private static final String SPLITTER = ",";
    private static final int LENGTH = 3;  // 保留最近 3 次的记录
    // 最近的日期是 history[0]，最早的日期是 history[LENGTH-1]
    private String[] history = new String[LENGTH];

    private DesignerOpenHistory() {
        for (int i = 0; i < LENGTH; i++) {
            history[i] = StringUtils.EMPTY;
        }
    }

    static DesignerOpenHistory getInstance() {
        if (singleton == null) {
            singleton = new DesignerOpenHistory();
        }
        return singleton;
    }

    void update() {
        String today = getToday();
        if (ComparatorUtils.equals(history[0], today)) {
            return;
        }
        shiftByOne();
        history[0] = today;
    }

    /**
     * 设计器在 dayCount 时间内启动超过 X 次（目前定的 X = 3）
     */
    boolean isOpenEnoughTimesInPeriod(int dayCount) {
        boolean enoughTimes = StringUtils.isNotEmpty(history[LENGTH - 1]);
        if (!enoughTimes) {
            return false;
        }
        return getHistorySpanDayCount() < dayCount;
    }


    /**
     * 获取历史记录中囊括的日子数。即最早的历史记录 history[LENGTH - 1]，到最晚的记录 history[0] 之间的时间跨度
     */
    int getHistorySpanDayCount() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date earliestDate = sdf.parse(getEarliestDate());
            Date latestDate = sdf.parse(history[0]);
            long diffInMillies = latestDate.getTime() - earliestDate.getTime();
            return (int)TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS) + 1;
        } catch (ParseException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return 1;
    }

    /**
     * 今天是否已经打开过设计器
     */
    boolean hasOpenedToday() {
        String today = getToday();
        return ComparatorUtils.equals(today, history[0]);
    }

    private String getEarliestDate() {
        for (int i = LENGTH - 1; i >= 0; i--) {
            if (StringUtils.isNotEmpty(history[i])) {
                return history[i];
            }
        }
        throw new AssertionError("Designer open history is empty!");
    }

    private void shiftByOne() {
        for (int i = LENGTH - 1; i > 0; i--) {
            history[i] = history[i-1];
        }
    }

    private String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    @Override
    public String toString() {
        return StringUtils.join(SPLITTER, history);
    }

    private void parseString(String s) {
        String[] arr = s.split(SPLITTER);
        System.arraycopy(arr, 0, history, 0, arr.length);
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (XML_TAG.equals(reader.getTagName())) {
            parseString(reader.getElementValue());
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        writer.textNode(toString());
        writer.end();
    }
}
