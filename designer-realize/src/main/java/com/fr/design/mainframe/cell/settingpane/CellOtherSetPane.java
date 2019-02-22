package com.fr.design.mainframe.cell.settingpane;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.layout.VerticalFlowLayout;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.general.ComparatorUtils;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.cell.cellattr.CellInsertPolicyAttr;
import com.fr.report.cell.cellattr.CellPageAttr;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author zhou
 * @since 2012-5-11下午5:24:31
 */
public class CellOtherSetPane extends AbstractCellAttrPane {

    private static final int HEAD_WDITH = 290;
    private static final int HEAD_HEIGTH = 24;
    private static final int COMBO_WIDTH = 154;
    private static final int BUTTON_GROUP_WIDTH = 140;
    // normal
    private UIButtonGroup autoshrik;

    private UICheckBox previewCellContent;
    private UICheckBox printAndExportContent;
    private UICheckBox printAndExportBackground;

    private UIComboBox showContent;

    private UITextField tooltipTextField;

    private UITextField fileNameTextField;

    // 分页
    private UICheckBox pageBeforeRowCheckBox;
    private UICheckBox pageAfterRowCheckBox;
    private UICheckBox pageBeforeColumnCheckBox;
    private UICheckBox pageAfterColumnCheckBox;

    private UICheckBox canBreakOnPaginateCheckBox;
    private UICheckBox repeatCheckBox;

    // 自动调整
    private UIRadioButton autoHeightRadioButton;  // 自动调整行高
    private UIRadioButton autoWidthRadioButton;  // 自动调整列宽
    private UIRadioButton noAutoRadioButton;  // 不自动调整
    private UIRadioButton defaultAutoRadioButton;  // 跟随页面设置（默认）
    private UIRadioButton[] adjustRadioButtons;

    // 插入行策略
    private UIButtonGroup insertRowPolicyButtonGroup;
    private ValueEditorPane valueEditor;
    private CardLayout insertRowLayout;
    private JPanel insertRowPane;
    private JPanel insertRowPolicyPane;
    private JPanel defaultValuePane;

    /**
     * 初始化
     *
     * @return 面板
     */
    public JPanel createContentPane() {
        JPanel downPane = new JPanel(new BorderLayout());
        downPane.add(new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advaced"), HEAD_WDITH, HEAD_HEIGTH, seniorPane()), BorderLayout.NORTH);
        downPane.add(new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Pagination"), HEAD_WDITH, HEAD_HEIGTH, pagePane()), BorderLayout.CENTER);
        JPanel contentPane = new JPanel(new BorderLayout(0, 0));
        contentPane.add(new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Basic"), HEAD_WDITH, HEAD_HEIGTH, basicPane()), BorderLayout.NORTH);
        contentPane.add(downPane, BorderLayout.CENTER);
        initAllNames();
        return contentPane;
    }

    private JPanel basicPane() {
        defaultAutoRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Follow_Paper_Settings"));
        noAutoRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_No_Auto_Adjust"));
        autoHeightRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Auto_Adjust_Height"));
        autoWidthRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Auto_Adjust_Width"));
        adjustRadioButtons = new UIRadioButton[]{
                defaultAutoRadioButton, noAutoRadioButton, autoHeightRadioButton, autoWidthRadioButton
        };

        // 指定分组
        ButtonGroup autoBG = new ButtonGroup();
        for (UIRadioButton radioButton : adjustRadioButtons) {
            autoBG.add(radioButton);
        }

        JPanel basicPane = new JPanel() {
            @Override
            public Insets getInsets() {
                return new Insets(LayoutConstants.VGAP_MEDIUM, 0, LayoutConstants.VGAP_MEDIUM, 0);
            }
        };
        VerticalFlowLayout verticalFlowLayout = new VerticalFlowLayout(VerticalFlowLayout.CENTER, 0, 0);
        verticalFlowLayout.setAlignLeft(true);
        basicPane.setLayout(verticalFlowLayout);
        basicPane.add(defaultAutoRadioButton);
        basicPane.add(noAutoRadioButton);
        basicPane.add(autoHeightRadioButton);
        basicPane.add(autoWidthRadioButton);

        return basicPane;
    }

    private JPanel seniorPane() {
        initInsertRowPolicyPane();
        JPanel seniorPane = new JPanel(new BorderLayout());
        seniorPane.add(seniorUpPane(), BorderLayout.NORTH);
        seniorPane.add(insertRowPolicyPane, BorderLayout.CENTER);
        insertRowPolicyPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return seniorPane;
    }

    private void initInsertRowPolicyPane() {
        // 插入行策略
        insertRowPolicyButtonGroup = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_InsertRow_NULL"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Estate_Default_Text"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_InsertRow_COPY")});
        defaultValuePane = new JPanel(new BorderLayout(4, 0));
        valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane();
        defaultValuePane.add(valueEditor, BorderLayout.CENTER);
        insertRowLayout = new CardLayout();
        insertRowPane = new JPanel(insertRowLayout);
        insertRowPane.add(new JPanel(), "none");
        insertRowPane.add(defaultValuePane, "content");
        insertRowPane.setPreferredSize(new Dimension(0, 0));
        insertRowPolicyButtonGroup.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (insertRowPolicyButtonGroup.getSelectedIndex() == 1) {
                    insertRowPane.setPreferredSize(new Dimension(100, 20));
                    insertRowLayout.show(insertRowPane, "content");
                } else {
                    insertRowLayout.show(insertRowPane, "none");
                    insertRowPane.setPreferredSize(new Dimension(0, 0));
                }
            }
        });


        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        UILabel insertRowPolicyLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_InsertRow_Policy", SwingConstants.LEFT));
        UIComponentUtils.setLineWrap(insertRowPolicyLabel);

        // 如果右侧需要很宽的空间，就用3行1列的布局
        if (insertRowPolicyButtonGroup.getPreferredSize().getWidth() > BUTTON_GROUP_WIDTH) {
            double[] rowSize = {p, p, p};
            double[] columnSize = {f};

            Component[][] components = new Component[][] {
                    new Component[]{insertRowPolicyLabel},
                    new Component[]{insertRowPolicyButtonGroup},
                    new Component[]{insertRowPane},
            };
            insertRowPolicyPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_MEDIUM);
        } else {
            double[] rowSize = {p, p};
            double[] columnSize = {f, BUTTON_GROUP_WIDTH};
            Component[][] components = new Component[][] {
                    new Component[]{insertRowPolicyLabel, insertRowPolicyButtonGroup},
                    new Component[]{null, insertRowPane},
            };
            insertRowPolicyPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_MEDIUM);
        }
    }

    private JPanel seniorUpPane() {
        JPanel pane = new JPanel(new BorderLayout());
        // TODO: 方法之间的耦合还比较严重。现在必须先执行 createShowContentPane，再执行 createSeniorCheckPane。否则出现 npe。
        pane.add(createShowContentPane(), BorderLayout.CENTER);
        pane.add(createSeniorCheckPane(), BorderLayout.NORTH);
        return pane;
    }

    private JPanel createShowContentPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] colSize = {f, COMBO_WIDTH};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};

        JPanel fileNamePane = createNormal();
        fileNamePane.setBorder(BorderFactory.createEmptyBorder(0,12,0,0));

        UILabel showContentLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_Content"), SwingConstants.LEFT);
        UIComponentUtils.setLineWrap(showContentLabel);
        UILabel toolTipLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_ToolTip"));

        JPanel toolTipTextFieldWrapper = new JPanel(new BorderLayout());
        toolTipTextFieldWrapper.add(tooltipTextField, BorderLayout.NORTH);

        Component[][] components = new Component[][]{
                new Component[]{showContentLabel, UIComponentUtils.wrapWithBorderLayoutPane(showContent)},
                new Component[]{fileNamePane, null},  // 选择"用下载连接显示二进制内容"时，会显示这一行的面板
                new Component[]{toolTipLabel, toolTipTextFieldWrapper}
        };

        JPanel showContentPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, colSize, rowCount, LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_MEDIUM);
        showContentPane.setBorder(BorderFactory.createEmptyBorder(6, 0, 12, 0));

        return showContentPane;
    }

    private JPanel createSeniorCheckPane() {
        previewCellContent.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        printAndExportContent.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        printAndExportBackground.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{previewCellContent, null},
                new Component[]{printAndExportContent, null},
                new Component[]{printAndExportBackground, null},
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_LARGE);
    }

    private JPanel pagePane() {
        // 分页
        pageBeforeRowCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_Before_Row"));

        pageAfterRowCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_After_Row"));
        pageBeforeColumnCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_Before_Column"));
        pageAfterColumnCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_After_Column"));

        canBreakOnPaginateCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellPage_Can_Break_On_Paginate"));
        repeatCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellPage_Repeat_Content_When_Paging"));

        pageBeforeRowCheckBox.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        pageAfterRowCheckBox.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        pageBeforeColumnCheckBox.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        pageAfterColumnCheckBox.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        canBreakOnPaginateCheckBox.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        repeatCheckBox.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);

        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p, p, p, p, p, p, p};
        double[] columnSize = {p};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{null},
                new Component[]{pageBeforeRowCheckBox},
                new Component[]{pageAfterRowCheckBox},
                new Component[]{null},
                new Component[]{pageBeforeColumnCheckBox},
                new Component[]{pageAfterColumnCheckBox},
                new Component[]{null},
                new Component[]{canBreakOnPaginateCheckBox},
                new Component[]{repeatCheckBox},
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_LARGE);

    }

    private JPanel createNormal() {
        previewCellContent = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Preview_Cell_Content"));
        printAndExportContent = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Print_Content"));
        printAndExportBackground = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Print_Background"));
        showContent = new UIComboBox(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Show_As_Image"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Show_As_HTML"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_As_Download")});
        final CardLayout fileNameLayout = new CardLayout();
        final JPanel fileNamePane = new JPanel(fileNameLayout);
        JPanel fileNameCCPane = new JPanel(new BorderLayout(4, 0));
        fileNameCCPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_File_Name_For_Download")), BorderLayout.WEST);
        fileNameTextField = new UITextField();
        tooltipTextField = new UITextField();
        tooltipTextField.getUI();
        fileNamePane.add(new JPanel(), "none");
        fileNamePane.add(fileNameCCPane, "content");
        fileNamePane.setPreferredSize(new Dimension(0, 0));
        fileNameCCPane.add(fileNameTextField, BorderLayout.CENTER);
        showContent.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (showContent.getSelectedIndex() == 3) {
                    fileNamePane.setPreferredSize(new Dimension(100, 20));
                    fileNameLayout.show(fileNamePane, "content");
                } else {
                    fileNameLayout.show(fileNamePane, "none");
                    fileNamePane.setPreferredSize(new Dimension(0, 0));
                }
            }
        });
        return fileNamePane;
    }


    private void initAllNames() {
        defaultAutoRadioButton.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Follow_Paper_Settings"));
        noAutoRadioButton.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_No_Auto_Adjust"));
        autoHeightRadioButton.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Auto_Adjust_Height"));
        autoWidthRadioButton.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Auto_Adjust_Width"));
        previewCellContent.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview"));
        printAndExportContent.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Print_Content"));
        printAndExportBackground.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Print_Background"));
        showContent.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_Content"));
        fileNameTextField.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_Content"));
        tooltipTextField.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_ToolTip"));
        pageBeforeRowCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_Before_Row"));
        pageAfterRowCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_After_Row"));
        pageBeforeColumnCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_Before_Column"));
        pageAfterColumnCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_After_Column"));
        canBreakOnPaginateCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellPage_Can_Break_On_Paginate"));
        repeatCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellPage_Repeat_Content_When_Paging"));
        insertRowPolicyButtonGroup.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_InsertRow_Policy"));
        valueEditor.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_InsertRow_Policy"));
    }


    @Override
    public String getIconPath() {
//        return "com/fr/design/images/m_format/cellstyle/otherset.png";
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Other");
    }

    @Override
    protected void populateBean() {
        CellGUIAttr cellGUIAttr = cellElement.getCellGUIAttr();
        if (cellGUIAttr == null) {
            cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
        }

        // 是否在编辑表单中的报表块
        boolean isInForm = EastRegionContainerPane.getInstance().getCurrentMode().equals(EastRegionContainerPane.PropertyMode.FORM_REPORT);

        defaultAutoRadioButton.setVisible(!isInForm);
        switch (cellGUIAttr.getAdjustMode()) {
            case CellGUIAttr.ADJUST_MODE_NO_AUTO:
                noAutoRadioButton.setSelected(true);
                break;
            case CellGUIAttr.ADJUST_MODE_AUTO_HEIGHT:
                autoHeightRadioButton.setSelected(true);
                break;
            case CellGUIAttr.ADJUST_MODE_AUTO_WIDTH:
                autoWidthRadioButton.setSelected(true);
                break;
            case CellGUIAttr.ADJUST_MODE_DEFAULT:
                if (isInForm) {
                    autoHeightRadioButton.setSelected(true);
                } else {
                    defaultAutoRadioButton.setSelected(true);
                }
                break;
            default:
                break;
        }

        previewCellContent.setSelected(cellGUIAttr.isPreviewContent());
        printAndExportContent.setSelected(cellGUIAttr.isPrintContent());
        printAndExportBackground.setSelected(cellGUIAttr.isPrintBackground());
        if (cellGUIAttr.isShowAsImage()) {
            showContent.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Show_As_Image"));
        } else if (cellGUIAttr.isShowAsHTML()) {
            showContent.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Show_As_HTML"));
        } else if (cellGUIAttr.isShowAsDownload()) {
            showContent.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_As_Download"));
            fileNameTextField.setText(cellGUIAttr.getFileName());
        } else {
            showContent.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default"));
        }
        tooltipTextField.setText(cellGUIAttr.getTooltipText());
        CellPageAttr cellPageAttr = cellElement.getCellPageAttr(); // 分页
        if (cellPageAttr == null) {
            cellPageAttr = new CellPageAttr();
        }
        this.pageBeforeRowCheckBox.setSelected(cellPageAttr.isPageBeforeRow());
        this.pageBeforeColumnCheckBox.setSelected(cellPageAttr.isPageBeforeColumn());
        this.pageAfterRowCheckBox.setSelected(cellPageAttr.isPageAfterRow());
        this.pageAfterColumnCheckBox.setSelected(cellPageAttr.isPageAfterColumn());
        this.canBreakOnPaginateCheckBox.setSelected(cellPageAttr.isCanBreakOnPaginate());
        this.repeatCheckBox.setSelected(cellPageAttr.isRepeat());
        CellInsertPolicyAttr cellInsertPolicyAttr = cellElement.getCellInsertPolicyAttr();// 插入
        if (cellInsertPolicyAttr == null) {
            cellInsertPolicyAttr = new CellInsertPolicyAttr();
        }
        if (ComparatorUtils.equals(CellInsertPolicyAttr.INSERT_POLICY_COPY, cellInsertPolicyAttr.getInsertPolicy())) {
            insertRowPolicyButtonGroup.setSelectedIndex(2);
        } else if (ComparatorUtils.equals(CellInsertPolicyAttr.INSERT_POLICY_DEFAULT, cellInsertPolicyAttr.getInsertPolicy())) {
            insertRowPolicyButtonGroup.setSelectedIndex(1);
            Object defaultValue = cellInsertPolicyAttr.getDefaultInsertValue();
            this.valueEditor.populate(defaultValue);
        } else {
            insertRowPolicyButtonGroup.setSelectedIndex(0);
            this.valueEditor.populate(StringUtils.EMPTY);
        }
        if (insertRowPolicyButtonGroup.getSelectedIndex() == 1) {
            insertRowPane.setPreferredSize(new Dimension(100, 20));
            insertRowLayout.show(insertRowPane, "content");
        } else {
            insertRowLayout.show(insertRowPane, "none");
            insertRowPane.setPreferredSize(new Dimension(0, 0));
        }
        insertRowPolicyPane.setVisible(true);
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (!jTemplate.isJWorkBook()) { //表单中报表块编辑屏蔽掉  插入行策略
            insertRowPolicyPane.setVisible(false);
        }
    }

    /**
     * @param cellElement
     */
    public void updateBean(TemplateCellElement cellElement) {
        String fieldName = null;
        CellGUIAttr cellNullGUIAttr = null;
        CellGUIAttr cellGUIAttr = cellElement.getCellGUIAttr();
        if (cellGUIAttr == null) {
            cellGUIAttr = new CellGUIAttr();
        }

        for (UIRadioButton radioButton : adjustRadioButtons) {
            if (ComparatorUtils.equals(getGlobalName(), radioButton.getGlobalName())) {
                // 自动调整
                int flag;
                if (defaultAutoRadioButton.isSelected()) {
                    flag = CellGUIAttr.ADJUST_MODE_DEFAULT;
                } else if (autoWidthRadioButton.isSelected()) {
                    flag = CellGUIAttr.ADJUST_MODE_AUTO_WIDTH;
                } else if (autoHeightRadioButton.isSelected()) {
                    flag = CellGUIAttr.ADJUST_MODE_AUTO_HEIGHT;
                } else {
                    flag = CellGUIAttr.ADJUST_MODE_NO_AUTO;
                }
                cellGUIAttr.setAdjustMode(flag);
                break;
            }
        }

        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview"))) {
            cellGUIAttr.setPreviewContent(previewCellContent.isSelected());
        }

        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Print_Content"))) {
            cellGUIAttr.setPrintContent(printAndExportContent.isSelected());
        }

        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Print_Background"))) {
            cellGUIAttr.setPrintBackground(printAndExportBackground.isSelected());
        }

        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_Content"))) {
            cellGUIAttr.setShowAsDefault(showContent.getSelectedIndex() == 0);
            cellGUIAttr.setShowAsImage(showContent.getSelectedIndex() == 1);
            cellGUIAttr.setShowAsHTML(showContent.getSelectedIndex() == 2);
            cellGUIAttr.setShowAsDownload(showContent.getSelectedIndex() == 3);
            if (fileNameTextField.getText() == null || fileNameTextField.getText().trim().length() <= 0) {
                cellGUIAttr.setFileName(fieldName);
            } else if (showContent.getSelectedIndex() == 3) {
                cellGUIAttr.setFileName(fileNameTextField.getText());
            }
        }

        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_ToolTip"))) {
            if (tooltipTextField.getText() == null || tooltipTextField.getText().trim().length() <= 0) {
                cellGUIAttr.setTooltipText(fieldName);
            } else {
                cellGUIAttr.setTooltipText(tooltipTextField.getText());
            }
        }
        // 如果与默认的CellGUIAttr相同,就不用保存这个属性了
        if (ComparatorUtils.equals(cellGUIAttr, CellGUIAttr.DEFAULT_CELLGUIATTR)) {
            cellElement.setCellGUIAttr(cellNullGUIAttr);
        } else {
            cellElement.setCellGUIAttr(cellGUIAttr);
        }
        updatePageAttr(cellElement);
    }


    private void updatePageAttr(TemplateCellElement cellElement) {

        // 分页属性
        CellPageAttr cellPageAttr = cellElement.getCellPageAttr();
        if (cellPageAttr == null) {
            cellPageAttr = new CellPageAttr();
        }

        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_Before_Row"))) {
            cellPageAttr.setPageBeforeRow(this.pageBeforeRowCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_After_Row"))) {
            cellPageAttr.setPageAfterRow(this.pageAfterRowCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_Before_Column"))) {
            cellPageAttr.setPageBeforeColumn(this.pageBeforeColumnCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_Page_After_Column"))) {
            cellPageAttr.setPageAfterColumn(this.pageAfterColumnCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellPage_Can_Break_On_Paginate"))) {
            cellPageAttr.setCanBreakOnPaginate(canBreakOnPaginateCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellPage_Repeat_Content_When_Paging"))) {
            cellPageAttr.setRepeat(this.repeatCheckBox.isSelected());
        }

        cellElement.setCellPageAttr(cellPageAttr);

        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_InsertRow_Policy"))) {
            // 插入
            CellInsertPolicyAttr cellInsertPolicyAttr = new CellInsertPolicyAttr();

            if (insertRowPolicyButtonGroup.getSelectedIndex() == 2) {
                cellInsertPolicyAttr.setInsertPolicy(CellInsertPolicyAttr.INSERT_POLICY_COPY);
            } else if (insertRowPolicyButtonGroup.getSelectedIndex() == 1) {
                cellInsertPolicyAttr.setInsertPolicy(CellInsertPolicyAttr.INSERT_POLICY_DEFAULT);
                Object value = valueEditor.update();
                cellInsertPolicyAttr.setDefaultInsertValue(value);
            } else {
                cellInsertPolicyAttr.setInsertPolicy(CellInsertPolicyAttr.INSERT_POLICY_NULL);
            }
            cellElement.setCellInsertPolicyAttr(cellInsertPolicyAttr);
        }
    }

    /**
     * 存储
     */
    public void updateBeans() {
        TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        int cellRectangleCount = cs.getCellRectangleCount();
        for (int rect = 0; rect < cellRectangleCount; rect++) {
            Rectangle cellRectangle = cs.getCellRectangle(rect);
            // 需要先行后列地增加新元素。
            for (int j = 0; j < cellRectangle.height; j++) {
                for (int i = 0; i < cellRectangle.width; i++) {
                    int column = i + cellRectangle.x;
                    int row = j + cellRectangle.y;
                    TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                    if (cellElement == null) {
                        cellElement = new DefaultTemplateCellElement(column, row);
                        elementCase.addCellElement(cellElement);
                    }
                    updateBean(cellElement);
                }
            }
        }
    }

    /**
     * 返回界面的标题
     *
     * @return 标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Datasource_Other_Attributes");
    }

}
