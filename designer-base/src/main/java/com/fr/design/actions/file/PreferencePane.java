package com.fr.design.actions.file;

import com.fr.cluster.engine.base.FineClusterConfig;
import com.fr.config.Configuration;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.editor.editor.IntegerEditor;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIDictionaryComboBox;
import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.iprogressbar.UIProgressBarUI;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.layout.VerticalFlowLayout;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.vcs.VcsConfigManager;
import com.fr.design.mainframe.vcs.common.VcsHelper;
import com.fr.design.update.push.DesignerPushUpdateManager;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.general.log.Log4jConfig;
import com.fr.locale.InterProviderFactory;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import com.fr.third.apache.log4j.Level;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.vcs.VcsOperator;
import com.fr.workspace.server.vcs.git.config.GcConfig;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JDialog;

import javax.swing.Timer;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import java.awt.event.WindowAdapter;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * 选项对话框
 *
 * @editor zhou
 * @since 2012-3-28下午3:39:48
 */
public class PreferencePane extends BasicPane {
    private static final int MAX_UNDO_LIMIT_5 = 5;
    private static final int MAX_UNDO_LIMIT_10 = 10;
    private static final int MAX_UNDO_LIMIT_15 = 15;
    private static final int MAX_UNDO_LIMIT_20 = 20;
    private static final int MAX_UNDO_LIMIT_50 = 50;
    private static final int SELECTED_INDEX_4 = 4;
    private static final int SELECTED_INDEX_5 = 5;
    private static final int CACHING_MAX = 10;
    private static final int CACHING_DEFAULT = 5;
    private static final int CACHING_GAP = 5;
    private static final int MEMORY_TIP_LABEL_MAX_WIDTH = 230;
    private static final int OFFSET_HEIGHT = 50;

    private static final String TYPE = "pressed";
    private static final String DISPLAY_TYPE = "+";
    private static final String BACK_SLASH = "BACK_SLASH";
    private static final String DISPLAY_BACK_SLASH = "\\";
    private static final String SLASH = "SLASH";
    private static final String DISPLAY_SLASH = "/";
    private static final String CONTROL = "CONTROL";
    private static final String DISPLAY_CONTROL = "ctrl";
    private static final String OPEN_BRACKET = "OPEN_BRACKET";
    private static final String DISPLAY_OPEN_BRACKET = "{";
    private static final String CLOSE_BRACKET = "CLOSE_BRACKET";
    private static final String DISPLAY_CLOSE_BRACKET = "}";
    private static final String COMMA = "COMMA";
    private static final String DISPLAY_COMMA = ",";
    private static final String PERIOD = "PERIOD";
    private static final String DISPLAY_PERIOD = ".";
    private static final String SEMICOLON = "SEMICOLON";
    private static final String DISPLAY_SEMICOLON = ";";
    private static final String QUOTE = "QUOTE";
    private static final String DISPLAY_QUOTE = "'";
    private static final String EQUALS = "EQUALS";
    private static final String DISPLAY_EQUALS = "+";
    private static final String MINUS = "MINUS";
    private static final String DISPLAY_MINUS = "-";

    private static final Level[] LOG = {Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG};

    private boolean languageChanged; // 是否修改了设计器语言设置
    //设置是否支持undo
    private UICheckBox supportUndoCheckBox;
    //设置最大撤销次数
    private UIComboBox maxUndoLimit;
    //是非支持自动计算父格
    private UICheckBox supportDefaultParentCalculateCheckBox;

    //是否自动转化为公式
    private UICheckBox supportStringToFormulaBox;
    private UICheckBox defaultStringToFormulaBox;

    private UILabel shortCutLabel;
    private KeyStroke shortCutKeyStore = null;
    private UIColorButton gridLineColorTBButton;

    private UIColorButton paginationLineColorTBButton;

    private UICheckBox supportCellEditorDefCheckBox;
    private UICheckBox isDragPermitedCheckBox;

    private UITextField logExportDirectoryField;

    private UIComboBox logLevelComboBox, pageLengthComboBox, reportLengthComboBox;
    private UIDictionaryComboBox<Locale> languageComboBox;
    private IntegerEditor portEditor;
    private UICheckBox oracleSpace;
    private UISpinner cachingTemplateSpinner;
    private UICheckBox openDebugComboBox;
    private UICheckBox useOptimizedUPMCheckbox;
    private UICheckBox useUniverseDBMCheckbox;
    private UICheckBox joinProductImproveCheckBox;
    private UICheckBox autoPushUpdateCheckBox;

    private UICheckBox vcsEnableCheckBox;
    private UICheckBox saveCommitCheckBox;
    private UICheckBox useIntervalCheckBox;
    private IntegerEditor saveIntervalEditor;
    private UICheckBox gcEnableCheckBox;
    private UIButton gcButton;
    private UILabel remindVcsLabel;

    private JDialog gcDialog;
    private UILabel gcMessage = new UILabel();
    private JPanel gcDialogDownPane = new JPanel();
    private JPanel gcProgressBarPanel = new JPanel();
    private JProgressBar gcProgressBar;
    private Timer gcProgressTimer;
    private UIButton gcOkButton = new UIButton(Toolkit.i18nText("Fine-Design_Report_OK"));

    public PreferencePane() {
        this.initComponents();
    }


    protected void initComponents() {
        JPanel contentPane = this;
        contentPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        UITabbedPane jtabPane = new UITabbedPane();
        JPanel generalPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        jtabPane.addTab(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_General"), generalPane);
        JPanel advancePane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        jtabPane.addTab(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Advanced"), advancePane);
        contentPane.add(jtabPane, BorderLayout.NORTH);

        createFunctionPane(generalPane);
        createEditPane(generalPane);
        createGuiOfGridPane(generalPane);
        createColorSettingPane(generalPane);
        createVcsSettingPane(generalPane);

        // ConfPane
        JPanel confLocationPane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
        advancePane.add(confLocationPane);

        createLogPane(advancePane);

        createLanPane(generalPane);

        createLengthPane(advancePane);

        createServerPane(advancePane);

        JPanel oraclePane = FRGUIPaneFactory.createTitledBorderPane("Oracle" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Oracle_All_Tables"));
        oracleSpace = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Show_All_Oracle_Tables"));
        oraclePane.add(oracleSpace);

        JPanel debuggerPane = FRGUIPaneFactory.createTitledBorderPane(Toolkit.i18nText("Fine-Design_Basic_Develop_Tools"));
        openDebugComboBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Basic_Open_Debug_Window"));
        debuggerPane.add(openDebugComboBox, BorderLayout.CENTER);
        advancePane.add(debuggerPane);

        JPanel upmSelectorPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Update_Plugin_Manager"));
        useOptimizedUPMCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Use_New_Update_Plugin_Manager"));
        upmSelectorPane.add(useOptimizedUPMCheckbox);
        advancePane.add(upmSelectorPane);

        JPanel dbmSelectorPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database_Manager"));
        useUniverseDBMCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Use_Universe_Database_Manager"));
        dbmSelectorPane.add(useUniverseDBMCheckbox);
        advancePane.add(dbmSelectorPane);

        JPanel improvePane = FRGUIPaneFactory.createVerticalTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Product_Improve"));
        joinProductImproveCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Join_Product_Improve"));
        improvePane.add(joinProductImproveCheckBox);

        if (DesignerPushUpdateManager.getInstance().isAutoPushUpdateSupported()) {
            autoPushUpdateCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Automatic_Push_Update"));
            improvePane.add(autoPushUpdateCheckBox);
        }

        JPanel spaceUpPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        spaceUpPane.add(oraclePane, BorderLayout.NORTH);
        spaceUpPane.add(createMemoryPane(), BorderLayout.CENTER);
        spaceUpPane.add(improvePane, BorderLayout.SOUTH);
        advancePane.add(spaceUpPane);
    }

    private void createVcsSettingPane(JPanel generalPane) {
        JPanel vcsPane = FRGUIPaneFactory.createVerticalTitledBorderPane(Toolkit.i18nText("Fine-Design_Vcs_Title"));
        generalPane.add(vcsPane);
        remindVcsLabel = new UILabel(Toolkit.i18nText("Fine-Design_Vcs_Remind"));
        remindVcsLabel.setVisible(!VcsHelper.getInstance().needInit());
        vcsEnableCheckBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Vcs_SaveAuto"));
        saveCommitCheckBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Vcs_No_Delete"));
        saveIntervalEditor = new IntegerEditor(60);
        useIntervalCheckBox = new UICheckBox();

        //gc面板
        JPanel gcControlPane = createGcControlPane();

        JPanel enableVcsPanel = new JPanel(FRGUIPaneFactory.createLeftZeroLayout());
        enableVcsPanel.add(vcsEnableCheckBox);
        enableVcsPanel.add(remindVcsLabel);
        JPanel intervalPanel = new JPanel(FRGUIPaneFactory.createLeftZeroLayout());
        final UILabel everyLabel = new UILabel(Toolkit.i18nText("Fine-Design_Vcs_Every"));
        final UILabel delayLabel = new UILabel(Toolkit.i18nText("Fine-Design_Vcs_Delay"));
        intervalPanel.add(useIntervalCheckBox);
        intervalPanel.add(everyLabel);
        intervalPanel.add(saveIntervalEditor);
        intervalPanel.add(delayLabel);
        vcsEnableCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                boolean selected = vcsEnableCheckBox.isSelected();
                if (selected) {
                    saveCommitCheckBox.setEnabled(true);
                    saveIntervalEditor.setEnabled(true);
                    useIntervalCheckBox.setEnabled(true);
                    everyLabel.setEnabled(true);
                    delayLabel.setEnabled(true);
                } else {
                    saveCommitCheckBox.setEnabled(false);
                    saveIntervalEditor.setEnabled(false);
                    useIntervalCheckBox.setEnabled(false);
                    everyLabel.setEnabled(false);
                    delayLabel.setEnabled(false);
                }
            }
        });
        vcsPane.add(enableVcsPanel);
        vcsPane.add(intervalPanel);
        vcsPane.add(saveCommitCheckBox);
        vcsPane.add(gcControlPane);
    }

    /**
     * 模创建板版本gc 配置操作面板
     *
     * @return 面板
     */
    private JPanel createGcControlPane() {
        //gc面板
        JPanel gcControlPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JPanel gcButtonPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        gcEnableCheckBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Vcs_Storage_Optimization"));
        gcButton = initGcButton();
        gcButtonPane.add(gcButton);
        gcControlPane.add(gcEnableCheckBox);
        gcControlPane.add(gcButtonPane);
        gcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                tryGc();
            }
        });
        gcEnableCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                gcButton.setEnabled(gcEnableCheckBox.isSelected());
            }
        });

        //集群下禁用
        if (FineClusterConfig.getInstance().isCluster()) {
            gcEnableCheckBox.setEnabled(false);
            gcButton.setEnabled(false);
        }
        return gcControlPane;
    }

    private void createFunctionPane(JPanel generalPane) {
        JPanel functionPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Function"));
        generalPane.add(functionPane);

        //添加supportUndo选择项
        supportUndoCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Support_Undo"));
        functionPane.add(supportUndoCheckBox);
        //添加maxUndoLimit
        //String[] undoTimes = {"最大撤销次数","5次","10次","15次","20次","50次"};
        String[] undoTimes = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Max_Undo_Limit"), MAX_UNDO_LIMIT_5 + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Time(s)"), MAX_UNDO_LIMIT_10 + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Time(s)")
                , MAX_UNDO_LIMIT_15 + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Time(s)"), MAX_UNDO_LIMIT_20 + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Time(s)"), MAX_UNDO_LIMIT_50 + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Time(s)")};
        maxUndoLimit = new UIComboBox(undoTimes);
        functionPane.add(maxUndoLimit);

        //不支持撤销则不能选择撤销可缓存，也不能设置最大撤销次数
        supportUndoCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                maxUndoLimit.setEnabled(supportUndoCheckBox.isSelected());
            }
        });


        //添加supportDefaultParentCalculate选择项
        supportDefaultParentCalculateCheckBox = new UICheckBox(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Support_Default_Parent_Calculate"));
        functionPane.add(supportDefaultParentCalculateCheckBox);
    }

    private void createEditPane(JPanel generalPane) {
        //samuel:编辑器设置
        JPanel editPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Editor_Preference"));
        generalPane.add(editPane);

        //设置是否支持将字符串编辑为公式
        supportStringToFormulaBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Support_String_To_Formula"));
        editPane.add(supportStringToFormulaBox);

        //是否默认转化
        defaultStringToFormulaBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Always"));

        editPane.add(defaultStringToFormulaBox);
        //不支持转化则不能默认执行
        supportStringToFormulaBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultStringToFormulaBox.setEnabled(supportStringToFormulaBox.isSelected());
            }
        });
        JPanel keyStrokePane = new JPanel(new BorderLayout());
        keyStrokePane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Support_Auto_Complete_Shortcut") + ":"), BorderLayout.WEST);
        shortCutLabel = new UILabel();
        keyStrokePane.add(shortCutLabel, BorderLayout.CENTER);
        editPane.add(keyStrokePane);
        shortCutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    final KeyStrokePane basicPane = new KeyStrokePane(shortCutLabel.getText());
                    BasicDialog dlg = basicPane.showSmallWindow(SwingUtilities.getWindowAncestor(PreferencePane.this), new DialogActionAdapter() {
                        @Override
                        public void doOk() {
                            shortCutLabel.setText(basicPane.getText());
                        }
                    });
                    dlg.setVisible(true);
                }
            }
        });
    }

    private class KeyStrokePane extends BasicPane {
        private UILabel label;

        public KeyStrokePane(String text) {
            setLayout(new BorderLayout());
            setFocusable(true);
            requestFocusInWindow();
            label = new UILabel(text);
            add(GUICoreUtils.createBorderLayoutPane(
                    new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Support_Current_Auto_Complete_Shortcut") + ":"),
                    BorderLayout.WEST,
                    label,
                    BorderLayout.CENTER),
                    BorderLayout.NORTH);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    int modifier = e.getModifiers();
                    if (modifier == 0) {
                        return;
                    }
                    int keyCode = e.getKeyCode();
                    shortCutKeyStore = KeyStroke.getKeyStroke(keyCode, modifier);
                    String str = shortCutKeyStore.toString();
                    label.setText(getDisplayShortCut(str));
                }
            });
        }

        public String getText() {
            return label.getText();
        }


        @Override
        protected String title4PopupWindow() {
            return "KeyStroke";
        }

    }

    private void createGuiOfGridPane(JPanel generalPane) {
        // GridPane
        JPanel guiOfGridPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Setting_Grid"));
        generalPane.add(guiOfGridPane);

        supportCellEditorDefCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Support_Cell_Editor_Definition"));
        guiOfGridPane.add(supportCellEditorDefCheckBox);

        isDragPermitedCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Is_Drag_Permited"));
        guiOfGridPane.add(isDragPermitedCheckBox);
    }

    private void createColorSettingPane(JPanel generalPane) {
        // Color Setting Pane
        JPanel colorSettingPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Setting_Colors"));
        generalPane.add(colorSettingPane);

        new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Grid_Line_Color"));

        new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Pagination_Line_Color"));

        gridLineColorTBButton = new UIColorButton(IOUtils.readIcon("/com/fr/design/images/gui/color/foreground.png"));
        gridLineColorTBButton.setEnabled(this.isEnabled());

        paginationLineColorTBButton = new UIColorButton(IOUtils.readIcon("/com/fr/design/images/gui/color/foreground.png"));
        paginationLineColorTBButton.setEnabled(this.isEnabled());

        JPanel leftPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        leftPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Grid_Line_Color") + ":"));
        leftPane.add(gridLineColorTBButton);
        JPanel rightPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        rightPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Pagination_Line_Color") + ":"));
        rightPane.add(paginationLineColorTBButton);
        colorSettingPane.add(leftPane);
        colorSettingPane.add(rightPane);
    }

    private void createLogPane(JPanel advancePane) {
        //richer:选择导出log文件的目录.
        JPanel logPane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
        advancePane.add(logPane);
        JPanel logExportPane = FRGUIPaneFactory.createTitledBorderPane("log" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Export_Setting"));
        logPane.add(logExportPane);
        UILabel logLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Select_Export_Log_Directory") + ":");
        logExportPane.add(logLabel, BorderLayout.WEST);
        logExportDirectoryField = new UITextField(24);
        logExportPane.add(logExportDirectoryField, BorderLayout.CENTER);
        UIButton chooseDirBtn = new UIButton("...");
        logExportPane.add(chooseDirBtn, BorderLayout.EAST);
        chooseDirBtn.setPreferredSize(new Dimension(25, 25));
        chooseDirBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int saveValue = fileChooser.showOpenDialog(DesignerContext.getDesignerFrame());
                if (saveValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    logExportDirectoryField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        JPanel logLevelPane = FRGUIPaneFactory.createTitledBorderPane("log" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Level_Setting"));
        logPane.add(logLevelPane);
        logLevelComboBox = new UIComboBox(LOG);
        logLevelPane.add(logLevelComboBox);
        logLevelComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        Log4jConfig.getInstance().setRootLevel((Level) logLevelComboBox.getSelectedItem());
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{Log4jConfig.class};
                    }
                });
            }
        });
    }

    private void createLanPane(JPanel generalPane) {
        // ben:选择版本语言;
        JPanel languageAndDashBoard_pane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
        JPanel LanguagePane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Choose_Language"));
        generalPane.add(languageAndDashBoard_pane);
        languageAndDashBoard_pane.add(LanguagePane);

        languageComboBox = createLanguageComboBox();

        ActionLabel languageLabel = new ActionLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Designer_Language"));
        languageLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final LocalePane localePane = new LocalePane();
                BasicDialog dlg = localePane.showLargeWindow(SwingUtilities.getWindowAncestor(PreferencePane.this), new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        localePane.save();
                    }
                });
                dlg.setVisible(true);
            }
        });
        UILabel noticeLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Work_After_Restart_Designer"));//sail:提示重启后生效
        double p = TableLayout.PREFERRED;
        double rowSize[] = {p};
        double columnSize[] = {p, p, p};
        Component[][] components = {
                {languageLabel, languageComboBox, noticeLabel},
        };
        JPanel choosePane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        LanguagePane.add(choosePane);
    }

    private UIDictionaryComboBox<Locale> createLanguageComboBox() {
        Map<Locale, String> map = InterProviderFactory.getProvider().getSupportLocaleMap();
        int size = map.size();
        Locale[] keys = new Locale[size];
        String[] values = new String[size];
        int i = 0;
        for (Map.Entry<Locale, String> entry : map.entrySet()) {
            keys[i] = entry.getKey();
            // 想要读取到，必需在这里 使用 Inter 才行。
            values[i] = Inter.getLocText(entry.getValue());
            i++;
        }
        UIDictionaryComboBox<Locale> languageComboBox = new UIDictionaryComboBox<>(keys, values);
        languageComboBox.setFont(FRFont.getInstance("Dialog", Font.PLAIN, 12));//为了在中文系统中显示韩文
        return languageComboBox;
    }

    private String getDisplayShortCut(String shotrCut) {
        return shotrCut.replace(TYPE, DISPLAY_TYPE).replace(BACK_SLASH, DISPLAY_BACK_SLASH).replace(SLASH, DISPLAY_SLASH)
                .replace(CONTROL, DISPLAY_CONTROL).replace(OPEN_BRACKET, DISPLAY_OPEN_BRACKET).replace(CLOSE_BRACKET, DISPLAY_CLOSE_BRACKET)
                .replace(COMMA, DISPLAY_COMMA).replace(PERIOD, DISPLAY_PERIOD).replace(SEMICOLON, DISPLAY_SEMICOLON).replace(QUOTE, DISPLAY_QUOTE)
                .replace(EQUALS, DISPLAY_EQUALS).replace(MINUS, DISPLAY_MINUS);
    }


    private KeyStroke convert2KeyStroke(String ks) {
        return KeyStroke.getKeyStroke(ks.replace(DISPLAY_TYPE, TYPE));
    }


    private void createLengthPane(JPanel advancePane) {
        double p = TableLayout.PREFERRED;
        double rowSize[] = {p};

        // 长度单位选择
        JPanel lengthPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Setting_Ruler_Units"));
        advancePane.add(lengthPane);
        pageLengthComboBox = new UIComboBox(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Page_Setup_MM"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_CM"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_INCH")});
        pageLengthComboBox.setPreferredSize(new Dimension(80, 20));
        pageLengthComboBox.setMinimumSize(new Dimension(80, 20));
        reportLengthComboBox = new UIComboBox(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Page_Setup_MM"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_CM"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_INCH"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_PT_Duplicate")});
        reportLengthComboBox.setPreferredSize(new Dimension(80, 20));
        reportLengthComboBox.setMinimumSize(new Dimension(80, 20));
        UILabel pagelengthLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Page_Setup_Scale_Units") + ":");
        UILabel reportLengthLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Report_Design_Ruler_Units") + ":");
        Component[][] lengthComponents = {
                {pagelengthLabel, pageLengthComboBox, reportLengthLabel, reportLengthComboBox},
        };
        JPanel chooseLengthPane = TableLayoutHelper.createTableLayoutPane(lengthComponents, rowSize, new double[]{p, p, p, p});
        lengthPane.add(chooseLengthPane);
    }

    private void createServerPane(JPanel advancePane) {
        double p = TableLayout.PREFERRED;
        double rowSize[] = {p};
        double columnSize[] = {p, p, p};

        JPanel serverPortPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Web_Preview_Port_Setting"));
        advancePane.add(serverPortPane);
        portEditor = new IntegerEditor();
        portEditor.setPreferredSize(new Dimension(80, 20));
        portEditor.setMinimumSize(new Dimension(80, 20));
        UILabel notiJlabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Work_After_Restart_Designer"));
        UILabel serverPortLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Web_Preview_Port") + ":");
        Component[][] portComponents = {
                {serverPortLabel, portEditor, notiJlabel},
        };
        JPanel choosePortPane = TableLayoutHelper.createTableLayoutPane(portComponents, rowSize, columnSize);
        serverPortPane.add(choosePortPane, BorderLayout.CENTER);
    }

    private JPanel createMemoryPane() {
        JPanel memoryPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Caching_Template"));
        UILabel memoryLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Max_Caching_Template"));
        UILabel memoryTipLabel = FRWidgetFactory.createLineWrapLabel(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preference_Caching_Template_Tip"), MEMORY_TIP_LABEL_MAX_WIDTH);
        memoryTipLabel.setBorder(BorderFactory.createEmptyBorder(0, CACHING_GAP, 0, 0));
        cachingTemplateSpinner = new UISpinner(0, CACHING_MAX, 1, CACHING_DEFAULT);
        JPanel memorySpace = new JPanel(FRGUIPaneFactory.createLeftZeroLayout());
        memorySpace.add(memoryLabel);
        memorySpace.add(cachingTemplateSpinner);
        memorySpace.add(memoryTipLabel);
        memoryPane.add(memorySpace);
        return memoryPane;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_M_Window_Preference");
    }

    /**
     * The method  of populate.
     *
     * @param designerEnvManager
     */
    public void populate(DesignerEnvManager designerEnvManager) {
        if (designerEnvManager == null) {
            return;
        }

        supportUndoCheckBox.setSelected(designerEnvManager.isSupportUndo());
        if (designerEnvManager.isSupportUndo()) {
            maxUndoLimit.setSelectedIndex(chooseCase(designerEnvManager.getUndoLimit()));
        } else {
            maxUndoLimit.setEnabled(false);
        }

        supportDefaultParentCalculateCheckBox.setSelected(designerEnvManager.isSupportDefaultParentCalculate());

        supportStringToFormulaBox.setSelected(designerEnvManager.isSupportStringToFormula());

        shortCutLabel.setText(getDisplayShortCut(designerEnvManager.getAutoCompleteShortcuts()));
        shortCutKeyStore = convert2KeyStroke(designerEnvManager.getAutoCompleteShortcuts());

        if (supportStringToFormulaBox.isSelected()) {
            defaultStringToFormulaBox.setEnabled(true);
            defaultStringToFormulaBox.setSelected(designerEnvManager.isDefaultStringToFormula());
        } else {
            defaultStringToFormulaBox.setEnabled(false);
            defaultStringToFormulaBox.setSelected(false);
        }
        VcsConfigManager vcsConfigManager = designerEnvManager.getVcsConfigManager();
        if (VcsHelper.getInstance().needInit()) {
            vcsEnableCheckBox.setSelected(vcsConfigManager.isVcsEnable());
        } else {
            vcsEnableCheckBox.setEnabled(false);
            vcsEnableCheckBox.setSelected(false);
        }
        if (!vcsEnableCheckBox.isSelected()) {
            saveCommitCheckBox.setEnabled(false);
            saveIntervalEditor.setEnabled(false);
            useIntervalCheckBox.setEnabled(false);
        }

        saveIntervalEditor.setValue(vcsConfigManager.getSaveInterval());
        saveCommitCheckBox.setSelected(vcsConfigManager.isSaveCommit());
        useIntervalCheckBox.setSelected(vcsConfigManager.isUseInterval());
        gcEnableCheckBox.setSelected(GcConfig.getInstance().isGcEnable());
        gcButton.setEnabled(gcEnableCheckBox.isSelected());
        supportCellEditorDefCheckBox.setSelected(designerEnvManager.isSupportCellEditorDef());

        isDragPermitedCheckBox.setSelected(designerEnvManager.isDragPermited());

        gridLineColorTBButton.setColor(designerEnvManager.getGridLineColor());
        paginationLineColorTBButton.setColor(designerEnvManager.getPaginationLineColor());

        this.logExportDirectoryField.setText(designerEnvManager.getLogLocation());

        this.logLevelComboBox.setSelectedItem(Log4jConfig.getInstance().getRootLevel());

        this.languageComboBox.setSelectedItem(designerEnvManager.getLanguage());

        this.pageLengthComboBox.setSelectedIndex(designerEnvManager.getPageLengthUnit());
        this.reportLengthComboBox.setSelectedIndex(designerEnvManager.getReportLengthUnit());

        this.portEditor.setValue(new Integer(designerEnvManager.getEmbedServerPort()));

        openDebugComboBox.setSelected(designerEnvManager.isOpenDebug());
        useOptimizedUPMCheckbox.setSelected(ServerPreferenceConfig.getInstance().isUseOptimizedUPM());

        useUniverseDBMCheckbox.setSelected(ServerPreferenceConfig.getInstance().isUseUniverseDBM());

        this.oracleSpace.setSelected(designerEnvManager.isOracleSystemSpace());
        this.cachingTemplateSpinner.setValue(designerEnvManager.getCachingTemplateLimit());
        this.joinProductImproveCheckBox.setSelected(designerEnvManager.isJoinProductImprove());

        if (this.autoPushUpdateCheckBox != null) {
            this.autoPushUpdateCheckBox.setSelected(designerEnvManager.isAutoPushUpdateEnabled());
        }
    }

    private int chooseCase(int sign) {
        switch (sign) {
            case 0:
                return 0;
            case MAX_UNDO_LIMIT_5:
                return 1;
            case MAX_UNDO_LIMIT_10:
                return 2;
            case MAX_UNDO_LIMIT_15:
                return 3;
            case MAX_UNDO_LIMIT_20:
                return SELECTED_INDEX_4;
            case MAX_UNDO_LIMIT_50:
                return SELECTED_INDEX_5;
            default:
                return 1;
        }
    }

    /**
     * The method of update.
     */
    public void update(DesignerEnvManager designerEnvManager) {
        if (designerEnvManager == null) {
            return;
        }

        designerEnvManager.setLogLocation(this.logExportDirectoryField.getText());

        designerEnvManager.setSupportUndo(supportUndoCheckBox.isSelected());

        designerEnvManager.setSupportDefaultParentCalculate(supportDefaultParentCalculateCheckBox.isSelected());

        designerEnvManager.setSupportStringToFormula(supportStringToFormulaBox.isSelected());

        designerEnvManager.setDefaultStringToFormula(defaultStringToFormulaBox.isSelected());

        designerEnvManager.setSupportCellEditorDef(supportCellEditorDefCheckBox.isSelected());

        designerEnvManager.setAutoCompleteShortcuts(shortCutKeyStore != null ? shortCutKeyStore.toString().replace(TYPE, DISPLAY_TYPE) : shortCutLabel.getText());

        designerEnvManager.setDragPermited(isDragPermitedCheckBox.isSelected());

        designerEnvManager.setGridLineColor(gridLineColorTBButton.getColor());

        designerEnvManager.setPaginationLineColor(paginationLineColorTBButton.getColor());

        designerEnvManager.setLanguage(languageComboBox.getSelectedItem());

        designerEnvManager.setPageLengthUnit((short) pageLengthComboBox.getSelectedIndex());
        designerEnvManager.setReportLengthUnit((short) reportLengthComboBox.getSelectedIndex());

        designerEnvManager.setJettyServerPort(portEditor.getValue().intValue());

        designerEnvManager.setOpenDebug(openDebugComboBox.isSelected());

        designerEnvManager.setOracleSystemSpace(this.oracleSpace.isSelected());
        designerEnvManager.setCachingTemplateLimit((int) this.cachingTemplateSpinner.getValue());
        designerEnvManager.setJoinProductImprove(this.joinProductImproveCheckBox.isSelected());
        VcsConfigManager vcsConfigManager = designerEnvManager.getVcsConfigManager();
        vcsConfigManager.setSaveInterval(this.saveIntervalEditor.getValue());
        vcsConfigManager.setVcsEnable(this.vcsEnableCheckBox.isSelected());
        vcsConfigManager.setSaveCommit(this.saveCommitCheckBox.isSelected());
        vcsConfigManager.setUseInterval(this.useIntervalCheckBox.isSelected());
        Configurations.update(new Worker() {
            @Override
            public void run() {
                GcConfig.getInstance().setGcEnable(gcEnableCheckBox.isSelected());
            }

            @Override
            public Class<? extends Configuration>[] targets() {
                return new Class[]{GcConfig.class};
            }
        });

        if (this.autoPushUpdateCheckBox != null) {
            designerEnvManager.setAutoPushUpdateEnabled(this.autoPushUpdateCheckBox.isSelected());
        }

        designerEnvManager.setUndoLimit(maxUndoLimit.getSelectedIndex() * SELECTED_INDEX_5);
        if (maxUndoLimit.getSelectedIndex() == SELECTED_INDEX_5) {
            designerEnvManager.setUndoLimit(MAX_UNDO_LIMIT_50);
        }

        Configurations.update(new Worker() {
            @Override
            public void run() {
                Log4jConfig.getInstance().setRootLevel(((Level) logLevelComboBox.getSelectedItem()));
            }

            @Override
            public Class<? extends Configuration>[] targets() {
                return new Class[]{Log4jConfig.class};
            }
        });

        Configurations.update(new Worker() {
            @Override
            public void run() {
                ServerPreferenceConfig.getInstance().setUseOptimizedUPM(useOptimizedUPMCheckbox.isSelected());
                ServerPreferenceConfig.getInstance().setUseUniverseDBM(useUniverseDBMCheckbox.isSelected());
            }

            @Override
            public Class<? extends Configuration>[] targets() {
                return new Class[] {ServerPreferenceConfig.class};
            }
        });

    }

    // 如果语言设置改变了，则显示重启对话框
    public void showRestartDialog() {
        if (!languageChanged) {
            return;
        }
        int rv = JOptionPane.showOptionDialog(
                null,
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Language_Change_Successful"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Restart_Designer"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Restart_Designer_Later")},
                null
        );
        if (rv == JOptionPane.OK_OPTION) {
            RestartHelper.restart();
        }
    }

    @Override
    public BasicDialog showWindow(Window window) {
        return showWindow(window, new DialogActionAdapter() {
            @Override
            public void doOk() {
                languageChanged = !ComparatorUtils.equals(languageComboBox.getSelectedItem(), DesignerEnvManager.getEnvManager(false).getLanguage());
            }
        });
    }

    @Override
    public BasicDialog showWindow(Window window, DialogActionListener l) {
        return showWindowWithCustomSize(window, l, new Dimension(BasicDialog.DEFAULT.width, this.getPreferredSize().height + OFFSET_HEIGHT));
    }

    private void tryGc() {
        final SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private  long size = 0;

            @Override
            protected Boolean doInBackground() {
                size = WorkContext.getCurrent().get(VcsOperator.class).immediatelyGc();
                return true;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (ExecutionException e) {
                    updateGcDialogPanelInfo(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Vcs_Need_Update_Remote_Server_Jar"));
                    return;
                } catch (InterruptedException e) {
                    FineLoggerFactory.getLogger().error(e, e.getMessage());
                }
                updateGcDialogPanelInfo(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Vcs_Reduce_File_Size") + fileSizeConvert(size));
                gcDialogDownPane.revalidate();
                gcDialogDownPane.repaint();
                gcDialogDownPane.add(gcOkButton);
            }
        };
        worker.execute();
        initGcDialog();
        gcOkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gcDialog.dispose();
            }
        });
        gcDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                stopGcProgressTimer();
                worker.cancel(true);
            }
        });
        gcDialog.setVisible(true);
        gcDialog.dispose();
    }

    /**
     * gc 后更新进度条面板信息
     *
     * @param message
     */
    private void updateGcDialogPanelInfo(String message) {
        stopGcProgressTimer();
        gcMessage.setText(message);
        if (null != gcProgressBar) {
            gcProgressBarPanel.remove(gcProgressBar);
        }
        if (null != gcDialog) {
            gcDialog.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Joption_News"));
        }
    }

    /**
     * 初始化 gc 对话框
     */
    private void initGcDialog() {
        gcDialog = new JDialog((Dialog) SwingUtilities.getWindowAncestor(PreferencePane.this), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Vcs_Clean_Progress") + "...", true);
        gcDialog.setSize(new Dimension(340, 140));

        JPanel jp = new JPanel();
        //中上
        JPanel gcUpPane = new JPanel();
        gcUpPane.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        gcUpPane.add(new UILabel(UIManager.getIcon("OptionPane.informationIcon")));
        gcProgressBarPanel = createProgressBarPane();
        gcUpPane.add(gcProgressBarPanel);

        //中下
        gcDialogDownPane = new JPanel();
        gcDialogDownPane.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 0));

        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        jp.add(gcUpPane);
        jp.add(gcDialogDownPane);
        gcDialog.add(jp);
        gcDialog.setResizable(false);
        gcDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(PreferencePane.this));
    }

    /**
     * gc 进度条面板
     *
     * @return
     */
    private JPanel createProgressBarPane() {
        JPanel jp = new JPanel();
        VerticalFlowLayout layout = new VerticalFlowLayout();
        layout.setAlignLeft(true);
        jp.setLayout(layout);

        //提示
        gcMessage = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Vcs_Cleaning"));
        // 创建一个进度条
        gcProgressBar = createGcProgressBar(0, 30, 240, 15, Color.GREEN);
        gcProgressTimer = createGcProgressTimer(500, gcProgressBar);
        gcProgressTimer.start();
        jp.add(gcMessage);
        jp.add(gcProgressBar);
        return jp;
    }

    /**
     * 创建 gc 进度条
     *
     * @param min    最小值
     * @param max    最大值
     * @param width  宽度
     * @param height 高度
     * @param color  填充的图片颜色
     * @return
     */
    private JProgressBar createGcProgressBar(int min, int max, int width, int height, Color color) {
        // 创建一个进度条
        JProgressBar progressBar = new JProgressBar(min, max);
        UIProgressBarUI progressBarUI = new UIProgressBarUI();
        progressBar.setUI(progressBarUI);

        //颜色（进度条里的小方块）
        progressBar.setForeground(color);

        progressBar.setOpaque(false);
        progressBar.setPreferredSize(new Dimension(width, height));
        return progressBar;
    }

    /**
     * @param delay       每隔 delay 毫秒更新进度
     * @param progressBar 要更新的进度条
     * @return
     */
    private Timer createGcProgressTimer(int delay, final JProgressBar progressBar) {
        if (null == progressBar) {
            return null;
        }
        // 模拟延时操作进度, 每隔 delay / 1000 秒更新进度
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentProgress = progressBar.getValue() + 1;
                if (currentProgress > progressBar.getMaximum()) {
                    currentProgress = progressBar.getMinimum();
                }
                progressBar.setValue(currentProgress);
            }
        });

        return timer;
    }

    /**
     * 停止进度条模拟计时器
     */
    private void stopGcProgressTimer() {
        if (null == gcProgressTimer) {
            return;
        }
        gcProgressTimer.stop();
    }

    /**
     * 将字节转换成 KB or MB or GB 保留两位小数
     *
     * @param size
     * @return
     */
    private String fileSizeConvert(long size) {
        DecimalFormat df = new DecimalFormat("0.00");
        double n = 1024d;
        if (size > Math.pow(n, 3)) {
            return df.format(size / Math.pow(n, 3)) + "GB";
        }
        if (size > Math.pow(n, 2)) {
            return df.format(size / Math.pow(n, 2)) + "MB";
        }
        return new StringBuilder().append(df.format(size / n)).append("KB").toString();
    }

    /**
     * 立即清理的Button
     *
     * @return
     */
    private UIButton initGcButton() {
        UIButton gcButton = new UIButton(Toolkit.i18nText("Fine-Design_Vcs_Clean"));
        gcButton.setPreferredSize(new Dimension(100, 15));
        gcButton.setRoundBorder(true, Constants.LEFT);
        return gcButton;
    }

}
