package com.fr.design.mainframe.cell.settingpane;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;

import com.fr.base.FRContext;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.cell.cellattr.CellInsertPolicyAttr;
import com.fr.report.cell.cellattr.CellPageAttr;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * @author zhou
 * @since 2012-5-11下午5:24:31
 */
public class CellOtherSetPane extends AbstractCellAttrPane {
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

    // 插入行策略
    private UIButtonGroup insertRowPolicy;
    private ValueEditorPane valueEditor;

    private JPanel southContentPane;
    private JPanel defaultValuePane;

    /**
     * 初始化
     * @return   面板
     */
    public JPanel createContentPane() {
        final JPanel fileNamePane = createNormal();
        createOthers();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize1 = {p, p, p, p, p, p, p};
        double[] columnSize1 = {p, f};
        UILabel autoAdjustLabel = new UILabel(Inter.getLocText("FR-Designer_Auto_Adjust_Size") + ":", SwingConstants.RIGHT);
        autoAdjustLabel.setVerticalAlignment(UILabel.TOP);
        Component[][] components1 = new Component[][]{
                new Component[]{autoAdjustLabel, autoshrik},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Preview") + ":", SwingConstants.RIGHT), previewCellContent},
                new Component[]{new UILabel(Inter.getLocText("CellWrite-Print_Export") + ":", SwingConstants.RIGHT), printAndExportContent},
                new Component[]{null, printAndExportBackground},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Show_Content") + ":", SwingConstants.RIGHT), showContent},
                new Component[]{null, fileNamePane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_CellWrite_ToolTip") + ":", SwingConstants.RIGHT), tooltipTextField},
        };
        JPanel northContentPane = TableLayoutHelper.createTableLayoutPane(components1, rowSize1, columnSize1);
        double[] rowSize2 = {p, p, p, p, p, p};
        double[] columnSize2 = {p, f};
        Component[][] components2 = new Component[][]{
                new Component[]{new JSeparator(JSeparator.HORIZONTAL), null},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Pagination")), null},
                new Component[]{pageBeforeRowCheckBox, pageAfterRowCheckBox},
                new Component[]{pageBeforeColumnCheckBox, pageAfterColumnCheckBox},
                new Component[]{canBreakOnPaginateCheckBox, null},
                new Component[]{repeatCheckBox, null}
        };
        JPanel centerContentPane = TableLayoutHelper.createTableLayoutPane(components2, rowSize2, columnSize2);
        double[] rowSize3 = {p, p, p, p};
        double[] columnSize3 = {f};
        Component[][] components3 = new Component[][]{
                new Component[]{new JSeparator(JSeparator.HORIZONTAL)},
                new Component[]{new UILabel(Inter.getLocText("CellWrite-InsertRow_Policy"), SwingConstants.LEFT)},
                new Component[]{insertRowPolicy},
                new Component[]{defaultValuePane}
        };
        southContentPane = TableLayoutHelper.createTableLayoutPane(components3, rowSize3, columnSize3);
        JPanel contentsmallPane = new JPanel(new BorderLayout(0, 10));
        contentsmallPane.add(northContentPane, BorderLayout.NORTH);
        contentsmallPane.add(centerContentPane, BorderLayout.CENTER);
        JPanel contentPane = new JPanel(new BorderLayout(0, 10));
        contentPane.add(contentsmallPane, BorderLayout.NORTH);
        contentPane.add(southContentPane, BorderLayout.CENTER);
        initAllNames();
        return contentPane;
    }


    private JPanel createNormal() {
        String[] AjustRowTypes = new String[]{
                Inter.getLocText("FR-Designer_No"), Inter.getLocText("Utils-Row_Height"), Inter.getLocText("Utils-Column_Width"), Inter.getLocText("FR-Designer_DEFAULT")};
        autoshrik = new UIButtonGroup(AjustRowTypes);
        if (FRContext.getLocale().equals(Locale.US)) {
            // 英文显示不全，故每行一个按钮
            autoshrik.setFourLine();
            autoshrik.setLayout(new GridLayout(4, 1, 1, 1));
        } else {
            autoshrik.setTwoLine();
            autoshrik.setLayout(new GridLayout(2, 2, 1, 1));
        }

        previewCellContent = new UICheckBox(Inter.getLocText("CellWrite-Preview_Cell_Content"));
        printAndExportContent = new UICheckBox(Inter.getLocText("CellWrite-Print_Content"));
        printAndExportBackground = new UICheckBox(Inter.getLocText("CellWrite-Print_Background"));

        showContent = new UIComboBox(new String[]{Inter.getLocText("FR-Designer_DEFAULT"), Inter.getLocText("CellWrite-Show_As_Image"), Inter.getLocText("CellWrite-Show_As_HTML"),
                Inter.getLocText("FR-Designer_Show_As_Download")});
        final CardLayout fileNameLayout = new CardLayout();
        final JPanel fileNamePane = new JPanel(fileNameLayout);
        JPanel fileNameCCPane = new JPanel(new BorderLayout(4, 0));

        fileNameCCPane.add(new UILabel(Inter.getLocText("FR-Designer_File_Name_For_Download")), BorderLayout.WEST);
        fileNameTextField = new UITextField();

        tooltipTextField = new UITextField();
        tooltipTextField.getUI();
        fileNamePane.add(new JPanel(), "none");
        fileNamePane.add(fileNameCCPane, "content");
        fileNameCCPane.add(fileNameTextField, BorderLayout.CENTER);

        showContent.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                fileNameLayout.show(fileNamePane, showContent.getSelectedIndex() == 3 ? "content" : "none");
            }
        });

        tooltipTextField = new UITextField();
        tooltipTextField.getUI();
        return fileNamePane;
    }

    private void createOthers() {
        // 分页
        pageBeforeRowCheckBox = new UICheckBox(Inter.getLocText("CellWrite-Page_Before_Row"));
        pageAfterRowCheckBox = new UICheckBox(Inter.getLocText("CellWrite-Page_After_Row"));
        pageBeforeColumnCheckBox = new UICheckBox(Inter.getLocText("CellWrite-Page_Before_Column"));
        pageAfterColumnCheckBox = new UICheckBox(Inter.getLocText("CellWrite-Page_After_Column"));

        canBreakOnPaginateCheckBox = new UICheckBox(Inter.getLocText("CellPage-Can_Break_On_Paginate"));
        repeatCheckBox = new UICheckBox(Inter.getLocText("CellWrite-Repeat_Content_When_Paging"));

        // 插入行策略
        insertRowPolicy = new UIButtonGroup(new String[]{Inter.getLocText("CellWrite-InsertRow_NULL"), Inter.getLocText("CellWrite-InsertRow_DEFAULT"),
                Inter.getLocText("CellWrite-InsertRow_COPY")});
        defaultValuePane = new JPanel(new BorderLayout(4, 0));
        valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane();
        defaultValuePane.add(valueEditor, BorderLayout.CENTER);
        defaultValuePane.setVisible(false);

        insertRowPolicy.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                defaultValuePane.setVisible(insertRowPolicy.getSelectedIndex() == 1);
            }
        });

    }

    private void initAllNames() {
        autoshrik.setGlobalName(Inter.getLocText("FR-Designer_Auto_Adjust_Size"));
        previewCellContent.setGlobalName(Inter.getLocText("FR-Designer_Preview"));
        printAndExportContent.setGlobalName(Inter.getLocText("CellWrite-Preview_Cell_Content"));
        printAndExportBackground.setGlobalName(Inter.getLocText("CellWrite-Print_Background"));
        showContent.setGlobalName(Inter.getLocText("FR-Designer_Show_Content"));
        fileNameTextField.setGlobalName(Inter.getLocText("FR-Designer_Show_Content"));
        tooltipTextField.setGlobalName(Inter.getLocText("FR-Designer_CellWrite_ToolTip"));
        pageBeforeRowCheckBox.setGlobalName(Inter.getLocText("CellWrite-Page_Before_Row"));
        pageAfterRowCheckBox.setGlobalName(Inter.getLocText("CellWrite-Page_After_Row"));
        pageBeforeColumnCheckBox.setGlobalName(Inter.getLocText("CellWrite-Page_Before_Column"));
        pageAfterColumnCheckBox.setGlobalName(Inter.getLocText("CellWrite-Page_After_Column"));
        canBreakOnPaginateCheckBox.setGlobalName(Inter.getLocText("CellPage-Can_Break_On_Paginate"));
        repeatCheckBox.setGlobalName(Inter.getLocText("CellWrite-Repeat_Content_When_Paging"));
        insertRowPolicy.setGlobalName(Inter.getLocText("CellWrite-InsertRow_Policy"));
        valueEditor.setGlobalName(Inter.getLocText("CellWrite-InsertRow_Policy"));
    }


    @Override
    public String getIconPath() {
//        return "com/fr/design/images/m_format/cellstyle/otherset.png";
        return Inter.getLocText("FR-Designer_Other");
    }

    @Override
    protected void populateBean() {
        CellGUIAttr cellGUIAttr = cellElement.getCellGUIAttr();
        if (cellGUIAttr == null) {
            cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
        }
        autoshrik.setSelectedIndex(cellGUIAttr.getAdjustMode());
        previewCellContent.setSelected(cellGUIAttr.isPreviewContent());
        printAndExportContent.setSelected(cellGUIAttr.isPrintContent());
        printAndExportBackground.setSelected(cellGUIAttr.isPrintBackground());
        if (cellGUIAttr.isShowAsImage()) {
            showContent.setSelectedItem(Inter.getLocText("CellWrite-Show_As_Image"));
        } else if (cellGUIAttr.isShowAsHTML()) {
            showContent.setSelectedItem(Inter.getLocText("CellWrite-Show_As_HTML"));
        } else if (cellGUIAttr.isShowAsDownload()) {
            showContent.setSelectedItem(Inter.getLocText("FR-Designer_Show_As_Download"));
            fileNameTextField.setText(cellGUIAttr.getFileName());
        } else {
            showContent.setSelectedItem(Inter.getLocText("FR-Designer_DEFAULT"));
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
            insertRowPolicy.setSelectedIndex(2);
        } else if (ComparatorUtils.equals(CellInsertPolicyAttr.INSERT_POLICY_DEFAULT, cellInsertPolicyAttr.getInsertPolicy())) {
            insertRowPolicy.setSelectedIndex(1);
            Object defaultValue = cellInsertPolicyAttr.getDefaultInsertValue();
            this.valueEditor.populate(defaultValue);
        } else {
            insertRowPolicy.setSelectedIndex(0);
        }
        defaultValuePane.setVisible(insertRowPolicy.getSelectedIndex() == 1);
        southContentPane.setVisible(true);
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (!jTemplate.isJWorkBook()){ //表单中报表块编辑屏蔽掉  插入行策略
            southContentPane.setVisible(false);
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

        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_Auto_Adjust_Size"))) {
            cellGUIAttr.setAdjustMode(autoshrik.getSelectedIndex());
        }

        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_Preview"))) {
            cellGUIAttr.setPreviewContent(previewCellContent.isSelected());
        }

        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Preview_Cell_Content"))) {
            cellGUIAttr.setPrintContent(printAndExportContent.isSelected());
        }

        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Print_Background"))) {
            cellGUIAttr.setPrintBackground(printAndExportBackground.isSelected());
        }

        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_Show_Content"))) {
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

        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_CellWrite_ToolTip"))) {
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

        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Page_Before_Row"))) {
            cellPageAttr.setPageBeforeRow(this.pageBeforeRowCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Page_After_Row"))) {
            cellPageAttr.setPageAfterRow(this.pageAfterRowCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Page_Before_Column"))) {
            cellPageAttr.setPageBeforeColumn(this.pageBeforeColumnCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Page_After_Column"))) {
            cellPageAttr.setPageAfterColumn(this.pageAfterColumnCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellPage-Can_Break_On_Paginate"))) {
            cellPageAttr.setCanBreakOnPaginate(canBreakOnPaginateCheckBox.isSelected());
        }
        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Repeat_Content_When_Paging"))) {
            cellPageAttr.setRepeat(this.repeatCheckBox.isSelected());
        }

        cellElement.setCellPageAttr(cellPageAttr);

        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-InsertRow_Policy"))) {
            // 插入
            CellInsertPolicyAttr cellInsertPolicyAttr = new CellInsertPolicyAttr();

            if (insertRowPolicy.getSelectedIndex() == 2) {
                cellInsertPolicyAttr.setInsertPolicy(CellInsertPolicyAttr.INSERT_POLICY_COPY);
            } else if (insertRowPolicy.getSelectedIndex() == 1) {
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
     *存储
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
     * @return    标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Datasource-Other_Attributes");
    }

}