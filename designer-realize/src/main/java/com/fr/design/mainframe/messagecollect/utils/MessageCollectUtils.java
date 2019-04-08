package com.fr.design.mainframe.messagecollect.utils;

import com.fr.general.DateUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;
import com.fr.third.javax.xml.stream.XMLStreamException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author alex sung
 * @date 2019/3/26
 */
public class MessageCollectUtils {

    public static void readXMLFile(XMLReadable xmlReadable, File xmlFile) {
        if (xmlFile == null || !xmlFile.exists()) {
            return;
        }
        String charset = EncodeConstants.ENCODING_UTF_8;
        try {
            InputStream is = new FileInputStream(xmlFile);
            String fileContent = IOUtils.inputStream2String(is);
            InputStream xmlInputStream = new ByteArrayInputStream(fileContent.getBytes(charset));
            InputStreamReader inputStreamReader = new InputStreamReader(xmlInputStream, charset);
            XMLableReader xmlReader = XMLableReader.createXMLableReader(inputStreamReader);
            if (xmlReader != null) {
                xmlReader.readXMLObject(xmlReadable);
            }
            xmlInputStream.close();
        } catch (IOException | XMLStreamException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public static long getLastTimeMillis(String lastTime) {
        if (StringUtils.isEmpty(lastTime)) {
            return 0;
        }
        try {
            return DateUtils.string2Date(lastTime, true).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
