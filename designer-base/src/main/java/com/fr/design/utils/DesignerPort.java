package com.fr.design.utils;

import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.dialog.TipDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.exit.DesignerExiter;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLWriter;
import com.fr.stable.xml.XMLableReader;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 为的就是能替换 DesignPort.class 实现多开,因此避免编译器常量编译展开优化
 */
public class DesignerPort implements XMLReadable, XMLWriter {

    public static final String XML_TAG = "DesignerPort";
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65535;

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

    public void resetPort() {

        TipDialog dialog = new TipDialog(null,
                                         StringUtils.EMPTY,
                                         Toolkit.i18nText("Fine-Design_Port_Found_Port_Conflict"),
                                         Toolkit.i18nText("Fine-Design_End_Occupied_Process"),
                                         Toolkit.i18nText("Fine-Design_Modify_Designer_Port")) {
            @Override
            protected void endEvent() {
                dispose();
            }

            @Override
            protected void cancelEvent() {
                new ResetPortDialog();
            }
        };
        dialog.setVisible(true);
        DesignerExiter.getInstance().execute();
    }

    private class ResetPortDialog extends JDialog {
        private UITextField portFiled;
        private UILabel warnLabel;
        private UIButton okButton;

        private  ResetPortDialog() {
            this.setLayout(new BorderLayout());
            this.setModal(true);
            this.portFiled = new UITextField();
            this.portFiled.setPreferredSize(new Dimension(180, 20));
            this.portFiled.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    checkValid();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    checkValid();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    checkValid();
                }
            });
            JPanel iconPanel = new JPanel();
            UILabel iconLabel = new UILabel();
            iconLabel.setIcon(IOUtils.readIcon("com/fr/design/images/edit/edit_typing.png"));
            iconPanel.add(iconLabel);
            iconPanel.add(iconLabel);
            JPanel textPane = FRGUIPaneFactory.createVerticalFlowLayout_Pane(true, FlowLayout.LEADING, 0, 10);
            textPane.add(new UILabel(Toolkit.i18nText("Fine-Design_Modify_Designer_Port_Tip")));
            textPane.add(portFiled);
            warnLabel = new UILabel();
            warnLabel.setVisible(false);
            warnLabel.setForeground(Color.RED);
            okButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Button_OK"));
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int value = Integer.parseInt(portFiled.getText().trim());
                    if (ComparatorUtils.equals("true", System.getProperty("debug"))) {
                        setDebugMessagePort(value);
                    } else {
                        setMessagePort(value);
                    }
                    dispose();
                    DesignerEnvManager.getEnvManager().saveXMLFile();
                    RestartHelper.restart();
                }
            });
            UIButton cancelButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Button_Cancel"));
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            buttonPane.add(okButton);
            buttonPane.add(cancelButton);
            JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            controlPane.add(buttonPane, BorderLayout.EAST);
            JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            northPane.add(iconPanel, BorderLayout.WEST);
            northPane.add(textPane, BorderLayout.CENTER);
            JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            centerPane.add(warnLabel);
            JPanel southPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            southPane.add(controlPane);
            this.add(northPane, BorderLayout.NORTH);
            this.add(centerPane, BorderLayout.CENTER);
            this.add(southPane, BorderLayout.SOUTH);
            this.setSize(300, 150);
            this.setTitle(Toolkit.i18nText("Fine-Design_Modify_Designer_Port"));
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.setResizable(false);
            this.setAlwaysOnTop(true);
            GUICoreUtils.centerWindow(this);
            this.setVisible(true);
        }

        private void checkValid() {
            String port = this.portFiled.getText().trim();
            if (StringUtils.isEmpty(port)) {
                okButton.setEnabled(false);
                return;
            }

            int value;
            try {
                value = Integer.parseInt(port);
            } catch (NumberFormatException ignore) {
                warnLabel.setText(Toolkit.i18nText("Fine-Design_Modify_Designer_Port_Not_Number_Tip"));
                warnLabel.setVisible(true);
                okButton.setEnabled(false);
                return;
            }

            if (value < MIN_PORT || value > MAX_PORT) {
                warnLabel.setText(Toolkit.i18nText("Fine-Design_Modify_Designer_Port_Out_Of_Range_Tip"));
                warnLabel.setVisible(true);
                okButton.setEnabled(false);
                return;
            }

            warnLabel.setVisible(false);
            okButton.setEnabled(true);
        }
    }

}
