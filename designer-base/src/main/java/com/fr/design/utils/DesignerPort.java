package com.fr.design.utils;

import com.fr.design.i18n.Toolkit;
import com.fr.general.ComparatorUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLWriter;
import com.fr.stable.xml.XMLableReader;

import javax.swing.JOptionPane;

/**
 * 为的就是能替换 DesignPort.class 实现多开,因此避免编译器常量编译展开优化
 */
public class DesignerPort implements XMLReadable, XMLWriter {

    public static final String XML_TAG = "DesignerPort";
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65536;

    public static final  DesignerPort INSTANCE = new DesignerPort();

    public static DesignerPort getInstance() {
        return INSTANCE;
    }

    private DesignerPort() {
    }

    /**
     * 设计器端口
     */
    private int messagePort = 51462;

    /**
     * 设计器端口，debug模式下
     */
    private int debugMessagePort = 51463;

    public int getMessagePort() {
        return messagePort;
    }

    public int getDebugMessagePort() {
        return debugMessagePort;
    }

    public void setMessagePort(int messagePort) {
        this.messagePort = messagePort;
    }

    public void setDebugMessagePort(int debugMessagePort) {
        this.debugMessagePort = debugMessagePort;
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isAttr()) {
            this.setMessagePort(reader.getAttrAsInt("messagePort", 51462));
            this.setDebugMessagePort(reader.getAttrAsInt("debugMessagePort", 51463));
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        writer.attr("messagePort", this.messagePort);
        writer.attr("debugMessagePort", this.debugMessagePort);
        writer.end();
    }

    public int resetPort() {
        String port = JOptionPane.showInputDialog(null,
                Toolkit.i18nText("Fine-Design_Modify_Designer_Port_Tip"),
                Toolkit.i18nText("Fine-Design_Modify_Designer_Port"), JOptionPane.INFORMATION_MESSAGE);
        int value;
        try {
             value = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, Toolkit.i18nText("Fine-Design_Modify_Designer_Port_Not_Number_Tip"));
            value = resetPort();
        }
        if (value < MIN_PORT || value > MAX_PORT) {
            JOptionPane.showMessageDialog(null, Toolkit.i18nText("Fine-Design_Modify_Designer_Port_Out_Of_Range_Tip"));
            value = resetPort();
        }
        if (ComparatorUtils.equals("true", System.getProperty("debug"))) {
            setDebugMessagePort(value);
        } else {
            setMessagePort(value);
        }
        return value;
    }

}
