package com.fr.design.mainframe.loghandler;

import com.fr.general.LogRecordTime;
import com.fr.stable.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by Administrator on 2017/6/2/0002.
 */
public class DesignerLogAppendThread extends Thread{

    PipedReader reader;

    public DesignerLogAppendThread() {
        Logger root = Logger.getRootLogger();
        // 获取子记录器的输出源
        Appender appender = root.getAppender("design");
        // 定义一个未连接的输入流管道
        reader = new PipedReader();
        // 定义一个已连接的输出流管理，并连接到reader
        Writer writer = null;
        try {
            writer = new PipedWriter(reader);
            // 设置 appender 输出流
            ((WriterAppender) appender).setWriter(writer);
        } catch (Throwable e) {
        }
    }

    public void run() {
        // 不间断地扫描输入流
        Scanner scanner = new Scanner(reader);

        // 将扫描到的字符流打印在屏目
        while (scanner.hasNext()) {
            try {
                Thread.sleep(100);
                String log = scanner.nextLine();
                if (StringUtils.isEmpty(log)) {
                    return;
                }

                LogRecordTime logRecordTime = new LogRecordTime(new Date(),new LogRecord(Level.INFO, log));
                DesignerLogHandler.getInstance().printRemoteLog(logRecordTime);
            } catch (Throwable e) {

            }
        }
    }
}
