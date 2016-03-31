package com.fr.design.actions.file;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.editor.IntegerEditor;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.FRLevel;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;

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
    
    private static final FRLevel[] LOG = {FRLevel.SEVERE, FRLevel.WARNING, FRLevel.INFO, FRLevel.DEBUG};
    private static final String[] LANGUAGE = {Inter.getLocText("FR-Designer_Language_Default"),
    	getLocaledLanguage("Simplified_Chinese_Language", Locale.SIMPLIFIED_CHINESE), 
    	getLocaledLanguage("English_Language", Locale.ENGLISH),
    	getLocaledLanguage("Japanese_Language", Locale.JAPAN),
    	getLocaledLanguage("Traditional_Chinese_Language", Locale.TRADITIONAL_CHINESE),
        getLocaledLanguage("Korea_Language",Locale.KOREA),
    };

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

    private UIComboBox logLevelComboBox, languageComboBox, pageLengthComboBox, reportLengthComboBox;
    private IntegerEditor portEditor;
    private UITextField jdkHomeTextField;
    private UICheckBox oracleSpace;
    private UICheckBox joinProductImprove;

    public PreferencePane() {
        this.initComponents();
    }

    protected void initComponents() {
        JPanel contentPane = this;
        contentPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        UITabbedPane jtabPane = new UITabbedPane();
        JPanel generalPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        jtabPane.addTab(Inter.getLocText("FR-Designer_General"), generalPane);
        JPanel advancePane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        jtabPane.addTab(Inter.getLocText("FR-Designer_Advanced"), advancePane);
        contentPane.add(jtabPane, BorderLayout.NORTH);


        createFunctionPane(generalPane);
        createEditPane(generalPane);
        createGuiOfGridPane(generalPane);
        createColorSettingPane(generalPane);

        // ConfPane
        JPanel confLocationPane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
        advancePane.add(confLocationPane);

        createLogPane(advancePane);

        createLanPane(generalPane);

        createLengthPane(advancePane);

        createServerPane(advancePane);

        createJdkHomePane(advancePane);

        String[] message = new String[]{"Display", "Oracle_All_Tables"};
        String[] sign = new String[]{"Oracle"};
        JPanel oraclePane = FRGUIPaneFactory.createTitledBorderPane("Oracle" + Inter.getLocText("FR-Designer_Oracle_All_Tables"));
        oracleSpace = new UICheckBox(Inter.getLocText(message, sign));
        oraclePane.add(oracleSpace);
        
        JPanel improvePane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Product_improve"));
        joinProductImprove = new UICheckBox(Inter.getLocText("FR-Designer_Join_Product_improve"));
        improvePane.add(joinProductImprove);

        JPanel spaceUpPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        spaceUpPane.add(oraclePane, BorderLayout.NORTH);
        spaceUpPane.add(improvePane, BorderLayout.SOUTH);
        advancePane.add(spaceUpPane);
    }
    
    private static String getLocaledLanguage(String key, Locale locale){
    	StringBuilder sb = new StringBuilder();
    	sb.append(Inter.getLocText(key)).append("(");
    	sb.append(Inter.getLocText(key, locale)).append(")");
    	return sb.toString();
    }

    private void createFunctionPane(JPanel generalPane) {
        JPanel functionPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Preference-Function"));
        generalPane.add(functionPane);

        //添加supportUndo选择项
        supportUndoCheckBox = new UICheckBox(Inter.getLocText("Preference-Support_Undo"));
        functionPane.add(supportUndoCheckBox);
        //添加maxUndoLimit
        //String[] undoTimes = {"最大撤销次数","5次","10次","15次","20次","50次"};
        String[] undoTimes = {Inter.getLocText("FR-Designer_max_undo_limit"), MAX_UNDO_LIMIT_5 + Inter.getLocText("FR-Designer_time(s)"), MAX_UNDO_LIMIT_10 + Inter.getLocText("FR-Designer_time(s)")
                , MAX_UNDO_LIMIT_15 + Inter.getLocText("FR-Designer_time(s)"), MAX_UNDO_LIMIT_20 + Inter.getLocText("FR-Designer_time(s)"), MAX_UNDO_LIMIT_50 + Inter.getLocText("FR-Designer_time(s)")};
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
                Inter.getLocText("Preference-Support_Default_Parent_Calculate"));
        functionPane.add(supportDefaultParentCalculateCheckBox);
    }

    private void createEditPane(JPanel generalPane) {
        //samuel:编辑器设置
        JPanel editPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText(new String[]{"Editor", "Set"}));
        generalPane.add(editPane);

        //设置是否支持将字符串编辑为公式
        supportStringToFormulaBox = new UICheckBox(Inter.getLocText("FR-Designer_Surport_String_To_Formula"));
        editPane.add(supportStringToFormulaBox);

        //是否默认转化
        defaultStringToFormulaBox = new UICheckBox(Inter.getLocText("FR-Designer_Always"));

        editPane.add(defaultStringToFormulaBox);
        //不支持转化则不能默认执行
        supportStringToFormulaBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultStringToFormulaBox.setEnabled(supportStringToFormulaBox.isSelected());
            }
        });
        JPanel keyStrokePane = new JPanel(new BorderLayout());
        keyStrokePane.add(new UILabel(Inter.getLocText("Support-Auto_Complete_Shortcut") + ":"), BorderLayout.WEST);
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
                    new UILabel(Inter.getLocText("Support-Current_Auto_Complete_Shortcut") + ":"),
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
        JPanel guiOfGridPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Preference-Setting_Grid"));
        generalPane.add(guiOfGridPane);

        supportCellEditorDefCheckBox = new UICheckBox(Inter.getLocText("Preference-Support_Cell_Editor_Definition"));
        guiOfGridPane.add(supportCellEditorDefCheckBox);

        isDragPermitedCheckBox = new UICheckBox(Inter.getLocText("Preference-Is_Drag_Permited"));
        guiOfGridPane.add(isDragPermitedCheckBox);
    }

    private void createColorSettingPane(JPanel generalPane) {
        // Color Setting Pane
        JPanel colorSettingPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Preference-Setting_Colors"));
        generalPane.add(colorSettingPane);

        new UILabel(Inter.getLocText("Preference-Grid_Line_Color"));

        new UILabel(Inter.getLocText("Preference-Pagination_Line_Color"));

        gridLineColorTBButton = new UIColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/foreground.png"));
        gridLineColorTBButton.setEnabled(this.isEnabled());

        paginationLineColorTBButton = new UIColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/foreground.png"));
        paginationLineColorTBButton.setEnabled(this.isEnabled());

        JPanel leftPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        leftPane.add(new UILabel(Inter.getLocText("Preference-Grid_Line_Color") + ":"));
        leftPane.add(gridLineColorTBButton);
        JPanel rightPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        rightPane.add(new UILabel(Inter.getLocText("Preference-Pagination_Line_Color") + ":"));
        rightPane.add(paginationLineColorTBButton);
        colorSettingPane.add(leftPane);
        colorSettingPane.add(rightPane);
    }

    private void createLogPane(JPanel advancePane) {
        //richer:选择导出log文件的目录.
        JPanel logPane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
        advancePane.add(logPane);
        JPanel logExportPane = FRGUIPaneFactory.createTitledBorderPane("log" + Inter.getLocText("FR-Designer_Export_Setting"));
        logPane.add(logExportPane);
        UILabel logLabel = new UILabel(Inter.getLocText("FR-Designer_Select_Export_Log_Directory") + ":");
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

        JPanel logLevelPane = FRGUIPaneFactory.createTitledBorderPane("log" + Inter.getLocText("FR-Designer_Level_Setting"));
        logPane.add(logLevelPane);
        logLevelComboBox = new UIComboBox(LOG);
        logLevelPane.add(logLevelComboBox);
        logLevelComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DesignerEnvManager.getEnvManager().setLogLevel(((FRLevel) logLevelComboBox.getSelectedItem()).getLevel());
            }
        });
    }

    private void createLanPane(JPanel generalPane) {
        // ben:选择版本语言;
        JPanel languageAndDashBoard_pane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
        JPanel LanguagePane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Choose_Language"));
        generalPane.add(languageAndDashBoard_pane);
        languageAndDashBoard_pane.add(LanguagePane);
        languageComboBox = new UIComboBox(LANGUAGE);
        languageComboBox.setFont(FRFont.getInstance("Dialog", Font.PLAIN, 12));//为了在中文系统中显示韩文
        ActionLabel languageLabel = new ActionLabel(Inter.getLocText("FR-Designer_Designer_Language"));
        languageLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final  LocalePane localePane = new LocalePane();
                BasicDialog dlg = localePane.showLargeWindow(SwingUtilities.getWindowAncestor(PreferencePane.this), new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        localePane.save();
                    }
                });
                dlg.setVisible(true);
            }
        });
        UILabel noticeLabel = new UILabel(Inter.getLocText("FR-Designer_Work_After_Restart_Designer"));//sail:提示重启后生效
        double p = TableLayout.PREFERRED;
        double rowSize[] = {p};
        double columnSize[] = {p, p, p};
        Component[][] components = {
                {languageLabel, languageComboBox, noticeLabel},
        };
        languageComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //Inter.fr = ResourceBundle.getBundle("com/fr/general/locale/fr", Locale.US);
            }
        });
        JPanel choosePane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        LanguagePane.add(choosePane);
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
        JPanel lengthPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Setting-Ruler-Units"));
        advancePane.add(lengthPane);
        pageLengthComboBox = new UIComboBox(new String[]{Inter.getLocText("FR-Designer_PageSetup-mm"), Inter.getLocText("FR-Designer_Unit_CM"), Inter.getLocText("FR-Designer_Unit_INCH")});
        pageLengthComboBox.setPreferredSize(new Dimension(80, 20));
        pageLengthComboBox.setMinimumSize(new Dimension(80, 20));
        reportLengthComboBox = new UIComboBox(new String[]{Inter.getLocText("FR-Designer_PageSetup-mm"), Inter.getLocText("FR-Designer_Unit_CM"), Inter.getLocText("FR-Designer_Unit_INCH"), Inter.getLocText("FR-Designer_Unit_PT")});
        reportLengthComboBox.setPreferredSize(new Dimension(80, 20));
        reportLengthComboBox.setMinimumSize(new Dimension(80, 20));
        UILabel pagelengthLabel = new UILabel(Inter.getLocText("FR-Designer_Page-Setup-Scale-Units") + ":");
        UILabel reportLengthLabel = new UILabel(Inter.getLocText("FR-Designer_Report-Design-Ruler-Units") + ":");
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

        JPanel serverPortPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Web_Preview_Port_Setting"));
        advancePane.add(serverPortPane);
        portEditor = new IntegerEditor();
        portEditor.setPreferredSize(new Dimension(80, 20));
        portEditor.setMinimumSize(new Dimension(80, 20));
        UILabel notiJlabel = new UILabel(Inter.getLocText("FR-Designer_Work_After_Restart_Designer"));
        UILabel serverPortLabel = new UILabel(Inter.getLocText("FR-Designer_Web_Preview_Port") + ":");
        Component[][] portComponents = {
                {serverPortLabel, portEditor, notiJlabel},
        };
        JPanel choosePortPane = TableLayoutHelper.createTableLayoutPane(portComponents, rowSize, columnSize);
        serverPortPane.add(choosePortPane, BorderLayout.CENTER);
    }

    private void createJdkHomePane(JPanel advancePane) {
        double p = TableLayout.PREFERRED;
        double rowSize[] = {p};
        double columnSize[] = {p, p, p};

        JPanel serverPortPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Preference-JDK_Home"));
        advancePane.add(serverPortPane);
        jdkHomeTextField = new UITextField();
        UIButton chooseBtn = new UIButton("...");
        chooseBtn.setPreferredSize(new Dimension(20, 20));
        JPanel panel = GUICoreUtils.createBorderLayoutPane(
                jdkHomeTextField, BorderLayout.CENTER,
                chooseBtn, BorderLayout.EAST
        );
        chooseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(PreferencePane.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    jdkHomeTextField.setText(file.getAbsolutePath());
                }
            }
        });
        panel.setPreferredSize(new Dimension(300, 20));
        UILabel notiJlabel = new UILabel(Inter.getLocText("FR-Designer_Work_After_Restart_Designer"));
        UILabel serverPortLabel = new UILabel(Inter.getLocText("Preference-JDK_Home") + ":");
        Component[][] portComponents = {
                {serverPortLabel, panel, notiJlabel},
        };
        JPanel choosePortPane = TableLayoutHelper.createTableLayoutPane(portComponents, rowSize, columnSize);
        serverPortPane.add(choosePortPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("M_Window-Preference");
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

        supportCellEditorDefCheckBox.setSelected(designerEnvManager.isSupportCellEditorDef());

        isDragPermitedCheckBox.setSelected(designerEnvManager.isDragPermited());

        gridLineColorTBButton.setColor(designerEnvManager.getGridLineColor());
        paginationLineColorTBButton.setColor(designerEnvManager.getPaginationLineColor());

        this.logExportDirectoryField.setText(designerEnvManager.getLogLocation());

		this.logLevelComboBox.setSelectedItem(FRLevel.getByLevel(designerEnvManager.getLogLevel()));

        this.languageComboBox.setSelectedItem(LANGUAGE[designerEnvManager.getLanguage()]);

        this.pageLengthComboBox.setSelectedIndex(designerEnvManager.getPageLengthUnit());
        this.reportLengthComboBox.setSelectedIndex(designerEnvManager.getReportLengthUnit());

        this.portEditor.setValue(new Integer(designerEnvManager.getJettyServerPort()));

        this.jdkHomeTextField.setText(designerEnvManager.getJdkHome());

        this.oracleSpace.setSelected(designerEnvManager.isOracleSystemSpace());
        this.joinProductImprove.setSelected(designerEnvManager.isJoinProductImprove());
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

        designerEnvManager.setLogLevel(((FRLevel) logLevelComboBox.getSelectedItem()).getLevel());

        designerEnvManager.setSupportUndo(supportUndoCheckBox.isSelected());

        designerEnvManager.setSupportDefaultParentCalculate(supportDefaultParentCalculateCheckBox.isSelected());

        designerEnvManager.setSupportStringToFormula(supportStringToFormulaBox.isSelected());

        designerEnvManager.setDefaultStringToFormula(defaultStringToFormulaBox.isSelected());

        designerEnvManager.setSupportCellEditorDef(supportCellEditorDefCheckBox.isSelected());

        designerEnvManager.setAutoCompleteShortcuts(shortCutKeyStore != null ? shortCutKeyStore.toString().replace(TYPE, DISPLAY_TYPE) : shortCutLabel.getText());

        designerEnvManager.setDragPermited(isDragPermitedCheckBox.isSelected());

        designerEnvManager.setGridLineColor(gridLineColorTBButton.getColor());

        designerEnvManager.setPaginationLineColor(paginationLineColorTBButton.getColor());

        designerEnvManager.setLanguage(getLanguageInt());

        designerEnvManager.setPageLengthUnit((short) pageLengthComboBox.getSelectedIndex());
        designerEnvManager.setReportLengthUnit((short) reportLengthComboBox.getSelectedIndex());

        designerEnvManager.setJettyServerPort(portEditor.getValue().intValue());

        designerEnvManager.setJdkHome(jdkHomeTextField.getText());

        designerEnvManager.setOracleSystemSpace(this.oracleSpace.isSelected());
        designerEnvManager.setJoinProductImprove(this.joinProductImprove.isSelected());
//		designerEnvManager.setAutoBackUp(this.autoBackUp.isSelected());

        designerEnvManager.setUndoLimit(maxUndoLimit.getSelectedIndex() * SELECTED_INDEX_5);
        if (maxUndoLimit.getSelectedIndex() == SELECTED_INDEX_5) {
            designerEnvManager.setUndoLimit(MAX_UNDO_LIMIT_50);
        }
    }

    /*
     * 得到所选语言的int值
     */
    private int getLanguageInt() {
        int l = 0;
        String lang = (String) this.languageComboBox.getSelectedItem();
        for (int i = 0; i < LANGUAGE.length; i++) {
            if (ComparatorUtils.equals(lang, LANGUAGE[i])) {
                l = i;
                break;
            }
        }
        return l;
    }
}