package com.fr.design.mainframe.loghandler;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogLevel;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.general.LogRecordTime;
import com.fr.general.log.Log4jConfig;
import com.fr.log.FineLoggerFactory;
import com.fr.log.LogHandler;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.xml.LogRecordTimeProvider;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

public class DesignerLogHandler {
    protected static final int INFO_INT = Level.INFO.intValue();
    protected static final int ERROR_INT = Level.SEVERE.intValue();
    protected static final int WARN_INT = Level.WARNING.intValue();
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
        caption = new LogHandlerBar(Inter.getLocText("FR-Designer_Log"));

        caption.addClearListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logHandlerArea.jTextArea.setText("");
                caption.clearMessage();
                DesignerLogImpl.getInstance().clear();
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
                LogRecordTimeProvider[] recorders = DesignerLogImpl.getInstance().getRecorders();
                for (LogRecordTimeProvider logRecordTime : recorders) {
                    logHandlerArea.printStackTrace(logRecordTime);
                }
            }
        };
        showInfo = new JCheckBoxMenuItem(Inter.getLocText(new String[]{"Display", "Normal", "Info"}), true);
        showInfo.addItemListener(itemlistener);
        showError = new JCheckBoxMenuItem(Inter.getLocText(new String[]{"Display", "Alert", "Info"}), true);
        showError.addItemListener(itemlistener);
        showServer = new JCheckBoxMenuItem(Inter.getLocText(new String[]{"Display", "Seriously", "Info"}), true);
        showServer.addItemListener(itemlistener);
        caption.addSetListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu jPopupMenu = new JPopupMenu();
                //这里把log4j的日志级别也转成java级别,可以进行比对
                int logLevelInt = FRLogLevel.convertLog4jToJava(Log4jConfig.getInstance().getRootLevel()).intValue();
                if (logLevelInt <= INFO_INT) {
                    jPopupMenu.add(showInfo);
                    jPopupMenu.add(showError);
                    jPopupMenu.add(showServer);
                    jPopupMenu.show(caption, caption.getWidth() + GAP_X, INFO_GAP_Y);
                } else if (logLevelInt == ERROR_INT) {
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
            selectAll = new UIMenuItem(Inter.getLocText("FR-Designer_Select_All"));
            selectAll.addActionListener(popupListener);
            selectAll.setIcon(BaseUtils.readIcon("/com/fr/design/images/log/selectedall.png"));
            popup.add(selectAll);

            copy = new UIMenuItem(Inter.getLocText("FR-Designer_Copy"));
            copy.addActionListener(popupListener);
            copy.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
            popup.add(copy);

            clear = new UIMenuItem(Inter.getLocText("FR-Designer_Clear_All"));
            clear.addActionListener(popupListener);
            clear.setIcon(BaseUtils.readIcon("/com/fr/design/images/log/clear.png"));
            popup.add(clear);

            selectAll.setAccelerator(KeyStroke.getKeyStroke('A', DEFAULT_MODIFIER));
            copy.setAccelerator(KeyStroke.getKeyStroke('C', DEFAULT_MODIFIER));
            clear.setAccelerator(KeyStroke.getKeyStroke('L', DEFAULT_MODIFIER));

            jTextArea.addMouseListener(new MouseAdapter() {
                // check for right click
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
                public void actionPerformed(ActionEvent evt) {
                    resultPane.setText("");
                    caption.clearMessage();
                    DesignerLogImpl.getInstance().clear();
                }
            });
            return resultPane;
        }

        public void printStackTrace(LogRecordTimeProvider logRecordTime) {
            LogRecord logRecord = logRecordTime.getLogRecord();
            Date date = logRecordTime.getDate();
            int logLevelvalue = logRecord.getLevel().intValue();
            if (logLevelvalue == INFO_INT && showInfo.isSelected()) {
                printMessage(logRecord.getMessage(), logLevelvalue, date, logRecord.getThrown());
            } else if (logLevelvalue == ERROR_INT && showError.isSelected()) {
                printMessage(logRecord.getMessage(), logLevelvalue, date, logRecord.getThrown());
            } else if (logLevelvalue == WARN_INT && showServer.isSelected()) {
                printMessage(logRecord.getMessage(), logLevelvalue, date, logRecord.getThrown());
            }

        }

        public void printStackTrace(String message, Level level, Date date) {
            int logLevelvalue = level.intValue();
            if (logLevelvalue == INFO_INT && showInfo.isSelected()) {
                printMessage(message, logLevelvalue, date);
            } else if (logLevelvalue == ERROR_INT && showError.isSelected()) {
                printMessage(message, logLevelvalue, date);
            } else if (logLevelvalue == WARN_INT && showServer.isSelected()) {
                printMessage(message, logLevelvalue, date);
            }

        }

        private void printMessage(String message, int logLevelvalue, Date date) {
            printMessage(message, logLevelvalue, date, null);
        }

        private void printMessage(String messange, int logLevelvalue, Date date, Throwable e) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.log(simpleDateFormat.format(date) + "\n", 0);
            String message = swithInter(messange, logLevelvalue);
            this.log(message, logLevelvalue);
            setMessage(message, logLevelvalue);
            if (e == null) {
                return;
            }

            StackTraceElement[] stacktraceelement = e.getStackTrace();
            for (int i = 0; i < stacktraceelement.length; i++) {
                this.log("\t" + "at " + stacktraceelement[i].toString() + "\n", 0);
            }
        }

        private void log(String str, int style) {
            SimpleAttributeSet attrSet = new SimpleAttributeSet();
            if (style == ERROR_INT) {
                StyleConstants.setForeground(attrSet, new Color(247, 148, 29));
                StyleConstants.setBold(attrSet, true);
            } else if (style == WARN_INT) {
                StyleConstants.setForeground(attrSet, Color.red);
                StyleConstants.setBold(attrSet, true);
            } else if (style == INFO_INT) {
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
                FRContext.getLogger().error(e.getMessage());
            }
        }

        private String swithInter(String str, int style) {
            if (style == ERROR_INT) {
                str = Inter.getLocText("FR-Designer_Alert") + ":" + str + "\n";
            } else if (style == WARN_INT) {
                str = Inter.getLocText("FR-Designer_Seriously") + ":" + str + "\n";
            } else {
                str = Inter.getLocText("FR-Designer_Normal") + ":" + str + "\n";
            }
            return str;
        }

        private void setMessage(String message, int level) {
            LogMessageBar.getInstance().setMessage(message);
            if (level == DesignerLogHandler.INFO_INT && showInfo.isSelected()) {
                caption.infoAdd();
            } else if (level == DesignerLogHandler.ERROR_INT && showError.isSelected()) {
                caption.errorAdd();
            } else if (level == DesignerLogHandler.WARN_INT && showServer.isSelected()) {
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
            public void actionPerformed(ActionEvent evt) {
                if (ComparatorUtils.equals(evt.getActionCommand(), LogHandlerArea.this.selectAll.getText())) {
                    LogHandlerArea.this.jTextArea.selectAll();
                } else if (ComparatorUtils.equals(evt.getActionCommand(), LogHandlerArea.this.copy.getText())) {
                    LogHandlerArea.this.jTextArea.copy();
                } else if (ComparatorUtils.equals(evt.getActionCommand(), LogHandlerArea.this.clear.getText())) {
                    LogHandlerArea.this.jTextArea.setText("");
                    caption.clearMessage();
                    DesignerLogImpl.getInstance().clear();
                }
            }
        };

    }

    public void printRemoteLog(LogRecordTime logRecordTime) {
        logHandlerArea.printStackTrace(logRecordTime);
    }
}