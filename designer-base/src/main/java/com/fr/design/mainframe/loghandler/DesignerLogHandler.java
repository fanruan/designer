package com.fr.design.mainframe.loghandler;

import com.fr.base.BaseUtils;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.ui.util.UIUtil;
import com.fr.general.ComparatorUtils;
import com.fr.general.log.Log4jConfig;
import com.fr.log.FineLoggerFactory;
import com.fr.third.apache.log4j.Level;
import com.fr.third.apache.log4j.spi.LoggingEvent;
import com.fr.third.apache.log4j.spi.ThrowableInformation;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

public class DesignerLogHandler {

    private final SimpleDateFormat LOG_SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final int GAP_X = -150;

    private static final int INFO_GAP_Y = -60;

    private static final int ERRO_GAP_Y = -40;

    private static final int SERVER_GAP_Y = -20;

    public static DesignerLogHandler getInstance() {

        return HOLDER.singleton;
    }

    private static class HOLDER {

        private static DesignerLogHandler singleton = new DesignerLogHandler();
    }

    // 所有的面板
    private LogHandlerBar caption;

    private JCheckBoxMenuItem showInfo;

    private JCheckBoxMenuItem showError;

    private JCheckBoxMenuItem showServer;

    private LogHandlerArea logHandlerArea;

    public DesignerLogHandler() {

        logHandlerArea = new LogHandlerArea();
        caption = new LogHandlerBar(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Log"));

        caption.addClearListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                logHandlerArea.jTextArea.setText("");
                caption.clearMessage();
            }
        });
        caption.addSelectedListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                logHandlerArea.jTextArea.requestFocus();
                logHandlerArea.jTextArea.selectAll();
            }
        });
        ItemListener itemlistener = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                logHandlerArea.jTextArea.setText("");
                caption.clearMessage();
            }
        };
        showInfo = new JCheckBoxMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Log_Level_Info"), true);
        showInfo.addItemListener(itemlistener);
        showError = new JCheckBoxMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Log_Level_Warn"), true);
        showError.addItemListener(itemlistener);
        showServer = new JCheckBoxMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Log_Level_Error"), true);
        showServer.addItemListener(itemlistener);
        caption.addSetListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JPopupMenu jPopupMenu = new JPopupMenu();

                int logLevelInt = Log4jConfig.getInstance().getRootLevel().toInt();
                if (logLevelInt <= DesignerLogger.INFO_INT) {
                    jPopupMenu.add(showInfo);
                    jPopupMenu.add(showError);
                    jPopupMenu.add(showServer);
                    jPopupMenu.show(caption, caption.getWidth() + GAP_X, INFO_GAP_Y);
                } else if (logLevelInt == DesignerLogger.ERROR_INT) {
                    jPopupMenu.add(showError);
                    jPopupMenu.add(showServer);
                    jPopupMenu.show(caption, caption.getWidth() + GAP_X, ERRO_GAP_Y);
                } else {
                    jPopupMenu.add(showServer);
                    jPopupMenu.show(caption, caption.getWidth() + GAP_X, SERVER_GAP_Y);
                }
            }
        });
    }

    public JComponent getLogHandlerArea() {

        return logHandlerArea;
    }

    public JComponent getCaption() {

        return caption;
    }

    public void printRemoteLog(String message, Level level, Date date) {

        logHandlerArea.printStackTrace(message, level, date);
    }

    private class LogHandlerArea extends JPanel {

        private static final long serialVersionUID = 8215630927304621660L;

        private JTextPane jTextArea;

        private JPopupMenu popup;

        private UIMenuItem selectAll;

        private UIMenuItem copy;

        private UIMenuItem clear;

        private LogHandlerArea() {

            jTextArea = initLogJTextArea();
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            UIScrollPane js = new UIScrollPane(jTextArea);
            this.add(js, BorderLayout.CENTER);
            this.setPreferredSize(new Dimension(super.getPreferredSize().width, 150));

            jTextArea.setEditable(false);
            jTextArea.setBackground(Color.WHITE);

            popup = new JPopupMenu();
            selectAll = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Select_All"));
            selectAll.addActionListener(popupListener);
            selectAll.setIcon(BaseUtils.readIcon("/com/fr/design/images/log/selectedall.png"));
            popup.add(selectAll);

            copy = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Copy"));
            copy.addActionListener(popupListener);
            copy.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
            popup.add(copy);

            clear = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Clear_All"));
            clear.addActionListener(popupListener);
            clear.setIcon(BaseUtils.readIcon("/com/fr/design/images/log/clear.png"));
            popup.add(clear);

            selectAll.setAccelerator(KeyStroke.getKeyStroke('A', DEFAULT_MODIFIER));
            copy.setAccelerator(KeyStroke.getKeyStroke('C', DEFAULT_MODIFIER));
            clear.setAccelerator(KeyStroke.getKeyStroke('L', DEFAULT_MODIFIER));

            jTextArea.addMouseListener(new MouseAdapter() {

                // check for right click
                @Override
                public void mousePressed(MouseEvent event) {

                    if (event.getButton() == MouseEvent.BUTTON3) {
                        popup.show(jTextArea, event.getX(), event.getY());
                        checkEnabled();
                    }
                }
            });
        }

        private JTextPane initLogJTextArea() {

            final JTextPane resultPane = new JTextPane();
            InputMap inputMap = resultPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, DEFAULT_MODIFIER), DefaultEditorKit.copyAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, DEFAULT_MODIFIER), DefaultEditorKit.selectAllAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, DEFAULT_MODIFIER), "clear");
            ActionMap actionMap = resultPane.getActionMap();
            actionMap.put("clear", new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent evt) {

                    resultPane.setText("");
                    caption.clearMessage();
                }
            });
            return resultPane;
        }

        public void printStackTrace(LoggingEvent event) {

            int intLevel = event.getLevel().toInt();
            Date date = new Date(event.getTimeStamp());
            ThrowableInformation information = event.getThrowableInformation();
            if (intLevel == DesignerLogger.INFO_INT && showInfo.isSelected()) {
                printMessage(event.getRenderedMessage(), intLevel, date, information == null ? null : information.getThrowable());
            } else if (intLevel == DesignerLogger.ERROR_INT && showError.isSelected()) {
                printMessage(event.getRenderedMessage(), intLevel, date, information == null ? null : information.getThrowable());
            } else if (intLevel == DesignerLogger.WARN_INT && showServer.isSelected()) {
                printMessage(event.getRenderedMessage(), intLevel, date, information == null ? null : information.getThrowable());
            }
        }

        public void printStackTrace(String message, Level level, Date date) {

            int intLevel = level.toInt();
            if (intLevel == DesignerLogger.INFO_INT && showInfo.isSelected()) {
                printMessage(message, intLevel, date);
            } else if (intLevel == DesignerLogger.ERROR_INT && showError.isSelected()) {
                printMessage(message, intLevel, date);
            } else if (intLevel == DesignerLogger.WARN_INT && showServer.isSelected()) {
                printMessage(message, intLevel, date);
            }

        }

        private void printMessage(String message, int intLevel, Date date) {

            printMessage(message, intLevel, date, null);
        }

        private void printMessage(final String msg, final int intLevel, final Date date, final Throwable e) {
            UIUtil.invokeLaterIfNeeded(new Runnable() {
                @Override
                public void run() {
                    LogHandlerArea.this.log(LOG_SIMPLE_DATE_FORMAT.format(date) + "\n", 0);
                    String message = appendLocaleMark(msg, intLevel);
                    LogHandlerArea.this.log(message, intLevel);
                    setMessage(message, intLevel);
                    if (e == null) {
                        return;
                    }

                    StackTraceElement[] traceElements = e.getStackTrace();
                    for (StackTraceElement traceElement : traceElements) {
                        LogHandlerArea.this.log("\t" + "at " + traceElement.toString() + "\n", 0);
                    }
                }
            });
        }

        private void log(String str, int style) {

            SimpleAttributeSet attrSet = new SimpleAttributeSet();
            if (style == DesignerLogger.ERROR_INT) {
                StyleConstants.setForeground(attrSet, new Color(247, 148, 29));
                StyleConstants.setBold(attrSet, true);
            } else if (style == DesignerLogger.WARN_INT) {
                StyleConstants.setForeground(attrSet, Color.red);
                StyleConstants.setBold(attrSet, true);
            } else if (style == DesignerLogger.INFO_INT) {
                StyleConstants.setForeground(attrSet, Color.black);
                StyleConstants.setBold(attrSet, false);
            } else {
                StyleConstants.setForeground(attrSet, Color.black);
                StyleConstants.setBold(attrSet, false);
            }
            Document doc = jTextArea.getDocument();
            try {
                doc.insertString(doc.getLength(), str, attrSet);
            } catch (BadLocationException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }

        private String appendLocaleMark(String str, int style) {

            if (style == DesignerLogger.ERROR_INT) {
                str = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Alert") + ":" + str + "\n";
            } else if (style == DesignerLogger.WARN_INT) {
                str = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Seriously") + ":" + str + "\n";
            } else {
                str = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Normal") + ":" + str + "\n";
            }
            return str;
        }

        private void setMessage(String message, int level) {

            LogMessageBar.getInstance().setMessage(message);
            if (level == DesignerLogger.INFO_INT && showInfo.isSelected()) {
                caption.infoAdd();
            } else if (level == DesignerLogger.ERROR_INT && showError.isSelected()) {
                caption.errorAdd();
            } else if (level == DesignerLogger.WARN_INT && showServer.isSelected()) {
                caption.serverAdd();
            }
        }

        private void checkEnabled() {

            this.selectAll.setEnabled(true);
            this.copy.setEnabled(true);
            this.clear.setEnabled(true);

            if (ComparatorUtils.equals(this.jTextArea.getText(), "")) {
                this.selectAll.setEnabled(false);
                this.clear.setEnabled(false);
            }

            if (ComparatorUtils.equals(this.jTextArea.getSelectionStart(), this.jTextArea.getSelectionEnd())) {
                this.copy.setEnabled(false);
            }

            if (this.jTextArea.getSelectionStart() == 0 && ComparatorUtils.equals(this.jTextArea.getSelectionEnd(), this.jTextArea.getText().length())) {
                this.selectAll.setEnabled(false);
            }
        }

        ActionListener popupListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {

                if (ComparatorUtils.equals(evt.getActionCommand(), LogHandlerArea.this.selectAll.getText())) {
                    LogHandlerArea.this.jTextArea.selectAll();
                } else if (ComparatorUtils.equals(evt.getActionCommand(), LogHandlerArea.this.copy.getText())) {
                    LogHandlerArea.this.jTextArea.copy();
                } else if (ComparatorUtils.equals(evt.getActionCommand(), LogHandlerArea.this.clear.getText())) {
                    LogHandlerArea.this.jTextArea.setText("");
                    caption.clearMessage();
                }
            }
        };

    }
}
