package com.fr.design.mainframe;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.event.UIObserverListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.toolbar.AuthorityEditToolBarComponent;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;

import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.js.NameJavaScriptGroup;
import com.fr.report.cell.AbstractCellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ColumnRow;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Author : daisy
 * Date: 13-9-4
 * Time: 下午4:01
 */
public class ElementCasePaneAuthorityEditPane extends AuthorityEditPane {
    private static final int WIDGET_VISIBLE = 0;
    private static final int WIDGET_USABLE = 1;
    private static final int CELL = 2;
    private static final int HYPER_LINK = 3;
    private static final int FLOAT_SELECTION = 3;
    private static final int NEW_VALUE = 4;
    //新值下面的编辑器的宽度
    private static final int NEW_PANE_WIDTH = 120;
    private static final Dimension VALUEPANE_NEW_DIMENSION = new Dimension(154,20);
    private static final Dimension VALUEPANE_OLD_DIMENSION = new Dimension(0,0);



    private UICheckBox floatElementVisibleCheckBoxes = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Float_Visible"));
    private UICheckBox cellElementVisibleCheckBoxes = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cell_Visible"));
    private UICheckBox widgetVisible = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Visible"));
    private UICheckBox widgetAvailable = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Enabled"));
    private UICheckBox gridColumnRowVisible = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Hide"));
    private UICheckBox newValue = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_New_Value"));
    private UIButtonGroup oldNewValueButton = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellWrite_InsertRow_COPY"),com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_New_Value")});
    private JPanel newValuePane;
    private ValueEditorPane valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane(NEW_PANE_WIDTH);
    private UICheckBox[] hyperlinkCheckBoxes = null;
    private ElementCasePane elementCasePane = null;
    private int selectionType = CellSelection.NORMAL;
    private CellSelection cellSelection;
    private FloatSelection floatSelection;
    private CardLayout newValueCard;
    private boolean isAllHasWidget;
    private boolean isAllHasHyperlink;
    private String[] selectedPathArray;
    private UIObserverListener observerListener = new UIObserverListener() {
        @Override
        public void doChange() {
            if (elementCasePane == null || cellSelection == null) {
                return;
            }
            if (setAuthorityStyle(NEW_VALUE)) {
                elementCasePane.fireTargetModified();
            }

        }
    };
    private ItemListener newValuelistener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (elementCasePane == null || cellSelection == null) {
                return;
            }
            if (setAuthorityStyle(NEW_VALUE)) {
                valueEditor.setEnabled(newValue.isSelected());
                doAfterAuthority();
            }
        }
    };
    private ItemListener columnRowAuthorityListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            if (elementCasePane == null) {
                return;
            }
            boolean isDone = false;
            if (selectionType == CellSelection.CHOOSE_COLUMN) {
                isDone = setAuthorityColumn();
            } else {
                isDone = setAuthorityRow();
            }
            if (isDone) {
                doAfterAuthority();
            }
        }
    };
    private ItemListener floatElementAuthorityListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            if (elementCasePane == null || floatSelection == null) {
                return;
            }
            if (setLFloatAuthorityStyle()) {
                doAfterAuthority();
            }
        }
    };
    private ItemListener cellRolesAuthorityListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            if (elementCasePane == null || cellSelection == null) {
                return;
            }
            if (setAuthorityStyle(CELL)) {
                doAfterAuthority();
            }
        }
    };
    private ItemListener widgetVisibleRoleAuthorityListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            if (elementCasePane == null || cellSelection == null) {
                return;
            }
            if (setAuthorityStyle(WIDGET_VISIBLE)) {
                doAfterAuthority();
            }
        }
    };
    private ItemListener widgetUsableAuthorityListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            if (elementCasePane == null || cellSelection == null) {
                return;
            }

            if (setAuthorityStyle(WIDGET_USABLE)) {
                doAfterAuthority();
            }
        }
    };
    private ChangeListener buttonChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (oldNewValueButton.getSelectedIndex() == 1){
                newValuePane.setPreferredSize(VALUEPANE_NEW_DIMENSION);
                newValueCard.show(newValuePane,"new");
                newValue.setSelected(true);
                valueEditor.setEnabled(true);
            }else {
                newValue.setSelected(false);
//                valueEditor.setEnabled(false);
                newValuePane.setPreferredSize(VALUEPANE_OLD_DIMENSION);
                newValueCard.show(newValuePane,"old");
            }
        }
    };

    public ElementCasePaneAuthorityEditPane(ElementCasePane elementCasePane) {
        super(elementCasePane);
        this.elementCasePane = elementCasePane;
        initCheckBoxesState();
        initListener();
    }

    private void doAfterAuthority() {
        elementCasePane.repaint();
        elementCasePane.fireTargetModified();
        RolesAlreadyEditedPane.getInstance().refreshDockingView();
        RolesAlreadyEditedPane.getInstance().setReportAndFSSelectedRoles();
        RolesAlreadyEditedPane.getInstance().repaint();
        checkCheckBoxes();
    }

    private boolean setAuthorityColumn() {
        initSelectedPathArray();
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if (ComparatorUtils.equals(selectedRoles, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Role"))) {
            return false;
        }
        if (selectedRoles == null) {
            return false;
        }
        if (selectedPathArray == null) {
            return false;
        }
        final TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        boolean isVisible = !gridColumnRowVisible.isSelected();
        for (int t = 0; t < selectedPathArray.length; t++) {
            if (!isVisible) {
                for (int col = cellSelection.getColumn(); col < cellSelection.getColumn() + cellSelection.getColumnSpan(); col++) {
                    elementCase.addColumnPrivilegeControl(col, selectedPathArray[t]);
                }
            } else {
                for (int col = cellSelection.getColumn(); col < cellSelection.getColumn() + cellSelection.getColumnSpan(); col++) {
                    elementCase.removeColumnPrivilegeControl(col, selectedPathArray[t]);
                }
            }
        }

        return true;
    }

    private boolean setAuthorityRow() {
        initSelectedPathArray();
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if (ComparatorUtils.equals(selectedRoles, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Role"))) {
            return false;
        }
        if (selectedRoles == null) {
            return false;
        }
        if (selectedPathArray == null) {
            return false;
        }
        final TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        boolean isVisible = !gridColumnRowVisible.isSelected();
        for (int t = 0; t < selectedPathArray.length; t++) {
            if (!isVisible) {
                for (int row = cellSelection.getRow(); row < cellSelection.getRow() + cellSelection.getRowSpan(); row++) {
                    elementCase.addRowPrivilegeControl(row, selectedPathArray[t]);
                }
            } else {
                for (int row = cellSelection.getRow(); row < cellSelection.getRow() + cellSelection.getRowSpan(); row++) {
                    elementCase.removeRowPrivilegeControl(row, selectedPathArray[t]);
                }
            }
        }


        return true;
    }

    private boolean setLFloatAuthorityStyle() {
        initSelectedPathArray();
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if (ComparatorUtils.equals(selectedRoles, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Role"))) {
            return false;
        }
        if (selectedRoles == null) {
            return false;
        }
        if (selectedPathArray == null) {
            return false;
        }
        String name = floatSelection.getSelectedFloatName();
        TemplateElementCase ec = elementCasePane.getEditingElementCase();
        FloatElement fe = ec.getFloatElement(name);
        for (int t = 0; t < selectedPathArray.length; t++) {
            fe.changeAuthorityState(selectedPathArray[t], floatElementVisibleCheckBoxes.isSelected());
        }

        return true;
    }

    private boolean setAuthorityStyle(int type) {
        initSelectedPathArray();
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if (ComparatorUtils.equals(selectedRoles, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Role")) ||
                selectedRoles == null || selectedPathArray == null) {
            return false;
        }
        final TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        int cellRectangleCount = cellSelection.getCellRectangleCount();
        for (int t = 0; t < selectedPathArray.length; t++) {

            for (int rec = 0; rec < cellRectangleCount; rec++) {
                Rectangle cellRectangle = cellSelection.getCellRectangle(rec);
                // 从最后循环起以保证最后一个修改标准单元格（originalStyle）。
                for (int j = cellRectangle.height - 1; j >= 0; j--) {
                    for (int i = cellRectangle.width - 1; i >= 0; i--) {
                        int column = i + cellRectangle.x;
                        int row = j + cellRectangle.y;
                        TemplateCellElement editCellElement = elementCase.getTemplateCellElement(column, row);
                        if (editCellElement == null) {
                            editCellElement = new DefaultTemplateCellElement(column, row);
                            elementCase.addCellElement(editCellElement);
                        } else {
                            // 对于合并的格子,我们不多次计算的权限.
                            if (editCellElement.getColumn() != column
                                    || editCellElement.getRow() != row) {
                                continue;
                            }
                        }
                        if (type == CELL) {
                            editCellElement.changeAuthorityState(selectedPathArray[t], cellElementVisibleCheckBoxes.isSelected());
                        } else if (type == NEW_VALUE) {
                            editCellElement.changeNewValueAuthorityState(selectedPathArray[t], newValue.isSelected(), valueEditor.update());
                        } else if (type == WIDGET_VISIBLE) {
                            Widget widget = editCellElement.getWidget();
                            widget.changeVisibleAuthorityState(selectedPathArray[t], widgetVisible.isSelected());
                        } else {
                            Widget widget = editCellElement.getWidget();
                            widget.changeUsableAuthorityState(selectedPathArray[t], widgetAvailable.isSelected());
                        }

                    }
                }
            }
        }
        return true;
    }

    /**
     * @see AuthorityEditToolBarPane initSelectedPathArray
     */
    private void initSelectedPathArray() {
        TreePath[] selectionPaths = ReportAndFSManagePane.getInstance().getRoleTree().getCheckBoxTreeSelectionModel().getSelectionPaths();
        if (selectionPaths.length == 1) {
            if (((ExpandMutableTreeNode) (selectionPaths[0].getLastPathComponent())).getChildCount() > 0) {
                ExpandMutableTreeNode node = (ExpandMutableTreeNode) ((ExpandMutableTreeNode) (selectionPaths[0].getLastPathComponent())).getLastChild();
                selectedPathArray = new String[node.getChildCount()];
                for (int i = 0; i < node.getChildCount(); i++) {
                    ExpandMutableTreeNode n = (ExpandMutableTreeNode) node.getChildAt(i);
                    String nodeName = n.getUserObject().toString();
                    selectedPathArray[i] = nodeName;
                }
            } else {
                selectedPathArray = pathToString(selectionPaths);
            }
        } else {
            selectedPathArray = pathToString(selectionPaths);
        }

    }

    public static String[] pathToString(TreePath[] path) {
        java.util.List<String> roles = new ArrayList<String>();
        if (path != null && path.length > 0) {
            for (TreePath tempPath : path) {
                String temp = tempPath.toString();
                boolean isTrue = temp.length() > 0 && temp.charAt(0) == '[' && temp.endsWith("]");
                if (isTrue) {
                    temp = temp.substring(1, temp.length() - 1);
                    String[] selectedRoles = temp.split("," + StringUtils.BLANK);
                    String role = selectedRoles[2].trim();
                    roles.add(role);
                }

            }
        }
        return roles.toArray(new String[0]);
    }

    /**
     * 选中的单元格的乐见状态以第一个单元格为齐
     */
    public void initCheckBoxesState() {
        final TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        if (cellSelection == null) {
            cellElementVisibleCheckBoxes.setSelected(true);
        } else {
            Rectangle cellRectangle = cellSelection.getCellRectangle(0);
            DefaultTemplateCellElement cellElement = (DefaultTemplateCellElement) elementCase.getCellElement(cellRectangle.x, cellRectangle.y);
            if (cellElement == null) {
                cellElement = new DefaultTemplateCellElement(cellRectangle.x, cellRectangle.y);
            }
            boolean firstCellDoneaAuthority = cellElement.isDoneAuthority(
                    ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName());
            cellElementVisibleCheckBoxes.setSelected(!firstCellDoneaAuthority);
        }
        widgetAvailable.setEnabled(cellElementVisibleCheckBoxes.isSelected());
        widgetVisible.setEnabled(cellElementVisibleCheckBoxes.isSelected());
        oldNewValueButton.setSelectedIndex(0);
    }

    private void initListener() {
        cellElementVisibleCheckBoxes.addItemListener(cellRolesAuthorityListener);
        widgetVisible.addItemListener(widgetVisibleRoleAuthorityListener);
        widgetAvailable.addItemListener(widgetUsableAuthorityListener);
        floatElementVisibleCheckBoxes.addItemListener(floatElementAuthorityListener);
        gridColumnRowVisible.addItemListener(columnRowAuthorityListener);
        newValue.addItemListener(newValuelistener);
        valueEditor.registerChangeListener(observerListener);
        oldNewValueButton.addChangeListener(buttonChangeListener);
    }

    private void removeListener() {
        cellElementVisibleCheckBoxes.removeItemListener(cellRolesAuthorityListener);
        widgetVisible.removeItemListener(widgetVisibleRoleAuthorityListener);
        widgetAvailable.removeItemListener(widgetUsableAuthorityListener);
        floatElementVisibleCheckBoxes.removeItemListener(floatElementAuthorityListener);
        gridColumnRowVisible.removeItemListener(columnRowAuthorityListener);
        newValue.removeItemListener(newValuelistener);
        valueEditor.registerChangeListener(null);
        oldNewValueButton.removeChangeListener(buttonChangeListener);
    }

    private void addHyperlinkListener() {
        cellElementVisibleCheckBoxes.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (hyperlinkCheckBoxes != null) {
                    for (int i = 0; i < hyperlinkCheckBoxes.length; i++) {
                        hyperlinkCheckBoxes[i].setEnabled(cellElementVisibleCheckBoxes.isSelected());
                        if (!cellElementVisibleCheckBoxes.isSelected()) {
                            hyperlinkCheckBoxes[i].setSelected(false);
                        }
                    }
                }
            }
        });
        for (int i = 0; i < hyperlinkCheckBoxes.length; i++) {
            hyperlinkCheckBoxes[i].addItemListener(cellRolesAuthorityListener);
        }
    }

    /**
     * 更新适合的类型
     */
    public void populateType() {
        if (selectionType == CellSelection.NORMAL) {
            type.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cell"));
        } else if (selectionType == CellSelection.CHOOSE_ROW) {
            type.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Row"));
        } else if (selectionType == CellSelection.CHOOSE_COLUMN) {
            type.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column"));
        } else {
            type.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Insert-Float"));
        }
    }

    /**
     * 名字
     */
    public void populateName() {
        if (selectionType == CellSelection.NORMAL) {
            name.setText(getCellSelectionName());
        } else if (selectionType == CellSelection.CHOOSE_ROW || selectionType == CellSelection.CHOOSE_COLUMN) {
            name.setText(getCellColumnRowName());
        } else {
            name.setText(getFloatSelectionName());
        }
    }

    private String getCellSelectionName() {
        String nameText = "";
        int count = cellSelection.getCellRectangleCount();
        for (int rect = 0; rect < count; rect++) {
            nameText += ",";
            Rectangle cellRectangle = cellSelection.getCellRectangle(rect);
            ColumnRow beginCR = ColumnRow.valueOf(cellRectangle.x, cellRectangle.y);
            nameText += beginCR.toString();
            if (cellRectangle.width * cellRectangle.height != 1) {
                ColumnRow endCR = ColumnRow.valueOf(cellRectangle.width + cellRectangle.x - 1, cellRectangle.height + cellRectangle.y - 1);
                nameText += ":" + endCR.toString();
            }
        }
        return nameText.substring(1);
    }

    private String getCellColumnRowName() {
        int count = cellSelection.getCellRectangleCount();
        String nameText = "";
        ColumnRow cr = ColumnRow.valueOf(cellSelection.getColumn(), cellSelection.getRow());
        if (cellSelection.getSelectedType() == CellSelection.CHOOSE_COLUMN && count == 1) {
            if (cellSelection.getColumnSpan() == 1) {
                nameText = cr.toString().substring(0, 1);
            } else {
                ColumnRow endCr = ColumnRow.valueOf(cellSelection.getColumn() + cellSelection.getColumnSpan() - 1,
                        cellSelection.getRow() + cellSelection.getRowSpan() - 1);
                nameText = cr.toString().substring(0, 1) + "-" + endCr.toString().substring(0, 1);
            }
        } else if (cellSelection.getSelectedType() == CellSelection.CHOOSE_ROW && count == 1) {
            if (cellSelection.getRowSpan() == 1) {
                nameText = cr.toString().substring(1);
            } else {
                ColumnRow endCr = ColumnRow.valueOf(cellSelection.getColumn() + cellSelection.getColumnSpan() - 1,
                        cellSelection.getRow() + cellSelection.getRowSpan() - 1);
                nameText = cr.toString().substring(1) + "-" + endCr.toString().substring(1);
            }
        }
        return nameText;
    }

    private String getFloatSelectionName() {
        return floatSelection.getSelectedFloatName();
    }

    private void mutilRect(CellSelection cellSelection) {
        isAllHasWidget = true;
        isAllHasHyperlink = true;
        int count = cellSelection.getCellRectangleCount();
        final TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        for (int rect = 0; rect < count; rect++) {
            Rectangle cellRectangle = cellSelection.getCellRectangle(rect);
            for (int j = 0; j < cellRectangle.height; j++) {
                for (int i = 0; i < cellRectangle.width; i++) {
                    int column = i + cellRectangle.x;
                    int row = j + cellRectangle.y;
                    DefaultTemplateCellElement cellElement = (DefaultTemplateCellElement) elementCase.getCellElement(column, row);
                    if (cellElement == null) {
                        cellElement = new DefaultTemplateCellElement(cellSelection.getColumn(), cellSelection.getRow());
                    }
                    if (cellElement.getCellWidgetAttr() == null) {
                        isAllHasWidget = false;
                    }
                    if (cellElement.getNameHyperlinkGroup() == null) {
                        isAllHasHyperlink = false;
                    }
                }
            }
        }
    }

    /**
     * 更新适合的pane
     *
     * @return 返回Pane
     */
    public JPanel populateCheckPane() {
        checkPane.removeAll();
        if (selectionType == CellSelection.NORMAL) {
            populateCellSelectionCheckPane(checkPane);
        } else if (selectionType == CellSelection.CHOOSE_COLUMN || selectionType == CellSelection.CHOOSE_ROW) {
            populateColumnRowCheckPane(checkPane);
        } else if (selectionType == FLOAT_SELECTION) {
            populateFloatSelectionCheckPane(checkPane);
        }
        checkPane.setBorder(BorderFactory.createEmptyBorder(0, LEFT_CHECKPANE, 0, 0));
        return checkPane;
    }

    private void populateColumnRowCheckPane(JPanel checkPane) {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{gridColumnRowVisible}
        };
        double[] rowSize = {p};
        double[] columnSize = {f};
        int[][] rowCount = {{1}};
        checkPane.add(
                TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM)
                , BorderLayout.WEST);
    }

    private void populateFloatSelectionCheckPane(JPanel checkPane) {
        checkPane.add(populateFloatElementCheckPane(), BorderLayout.WEST);
    }

    private void populateCellSelectionCheckPane(JPanel checkPane) {
        if (elementCasePane.isSelectedOneCell()) {
            //只选中了一个单元格
            final TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
            DefaultTemplateCellElement cellElement = (DefaultTemplateCellElement) elementCase.getCellElement(cellSelection.getColumn(), cellSelection.getRow());
            if (cellElement == null) {
                cellElement = new DefaultTemplateCellElement(cellSelection.getColumn(), cellSelection.getRow());
            }
            //单元格带控件
            if (cellElement.getCellWidgetAttr() != null) {
                checkPane.add(populateWidgetCheckPane(), BorderLayout.CENTER);
            } else {
                checkPane.add(populatCellCheckPane(), BorderLayout.CENTER);
            }
        } else {
            //批量选中单元格
            mutilRect(cellSelection);
            if (!isAllHasWidget && !isAllHasHyperlink) {
                checkPane.add(populateMutilCellCheckPane(), BorderLayout.CENTER);
            } else if (isAllHasWidget) {
                checkPane.add(populateMutilWidgetCheckPane(), BorderLayout.CENTER);
            }
        }
    }

    /**
     * 对单元格区域进行操作时的权限编辑页面 ,对应的角色的populate
     */
    public void populateDetials() {
        //做模式标记，此时鼠标焦点是在报表主体，为下一次退出权限编辑时做准备
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(false);
        TemplateElementCase templateElementCase = elementCasePane.getEditingElementCase();
        if (templateElementCase instanceof WorkSheet) {
            ((WorkSheet) templateElementCase).setPaintSelection(true);
        }
        signelSelection();
        Selection selection = elementCasePane.getSelection();
        if (selection instanceof CellSelection) {
            selectionType = CellSelection.NORMAL;
            this.cellSelection = ((CellSelection) selection).clone();
            this.floatSelection = null;
            judgeChooseCR();
        } else if (selection instanceof FloatSelection) {
            selectionType = FLOAT_SELECTION;
            cellSelection = null;
            floatSelection = new FloatSelection(((FloatSelection) selection).getSelectedFloatName());
        }
        populateType();
        populateName();
        populateCheckPane();
        checkCheckBoxes();
    }

    //判断是否选择的行列
    private void judgeChooseCR() {
        if (cellSelection.getSelectedType() == CellSelection.CHOOSE_COLUMN && cellSelection.getCellRectangleCount() == 1) {
            selectionType = CellSelection.CHOOSE_COLUMN;
        }
        if (cellSelection.getSelectedType() == CellSelection.CHOOSE_ROW && cellSelection.getCellRectangleCount() == 1) {
            selectionType = CellSelection.CHOOSE_ROW;
        }
    }

    //实现单选
    private void signelSelection() {
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jTemplate.isJWorkBook()) {
            //清工具栏
            JComponent component = DesignerContext.getDesignerFrame().getToolbarComponent();
            if (component instanceof AuthorityEditToolBarComponent) {
                ((AuthorityEditToolBarComponent) component).removeSelection();
            }
            //清参数面板
            jTemplate.removeParameterPaneSelection();
        }
    }

    private JPanel populateFloatElementCheckPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{floatElementVisibleCheckBoxes}
        };
        double[] rowSize = {p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, LayoutConstants.VGAP_SMALL);
    }

    private JPanel populateWidgetCheckPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        UILabel cv = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cell_Value") + "   ");
        cv.setBorder(BorderFactory.createEmptyBorder(0, LEFT_CHECKPANE, 0, 0));
        Component[][] components = new Component[][]{
                new Component[]{cellElementVisibleCheckBoxes, null},
                new Component[]{cv,valueGroup()},
                new Component[]{null, newValuePane},
                new Component[]{widgetVisible,null},
                new Component[]{widgetAvailable,null}
        };
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, LayoutConstants.VGAP_SMALL);
    }

    private JPanel populateMutilWidgetCheckPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cell"), SwingConstants.LEFT), cellElementVisibleCheckBoxes},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget"), SwingConstants.LEFT), widgetVisible},
                new Component[]{null, widgetAvailable}
        };
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }

    private JPanel populateMutilCellCheckPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{cellElementVisibleCheckBoxes},
        };
        double[] rowSize = {p};
        double[] columnSize = {f};
        int[][] rowCount = {{1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }

    private JPanel populatCellCheckPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        UILabel cv = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cell_Value") + "   ");
        cv.setBorder(BorderFactory.createEmptyBorder(0, LEFT_CHECKPANE, 0, 0));
        Component[][] components = new Component[][]{
                new Component[]{cellElementVisibleCheckBoxes,null},
                new Component[]{cv,valueGroup()},
                new Component[]{null,newValuePane}
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, LayoutConstants.VGAP_SMALL);
    }

    public TemplateCellElement getFirstCell() {
        final TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        Rectangle cellRectangle = cellSelection.getCellRectangle(0);
        // 从最后循环起以保证最后一个修改标准单元格（originalStyle）。
        TemplateCellElement firstCell = null;
        for (int j = cellRectangle.height - 1; j >= 0; j--) {
            for (int i = cellRectangle.width - 1; i >= 0; i--) {
                int column = i + cellRectangle.x;
                int row = j + cellRectangle.y;
                TemplateCellElement editCellElement = elementCase.getTemplateCellElement(column, row);
                if (editCellElement != null) {
                    // 对于合并的格子,我们不多次计算的权限.
                    if (editCellElement.getColumn() != column || editCellElement.getRow() != row) {
                        continue;
                    }
                    firstCell = editCellElement;
                }
            }
        }
        return firstCell;
    }

    private void checkCheckBoxes() {
        String selected = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        removeListener();
        if (selectionType == CellSelection.NORMAL) {
            checkCellSelectionCkeckboxes(selected);
        } else if (selectionType == CellSelection.CHOOSE_COLUMN || selectionType == CellSelection.CHOOSE_ROW) {
            checkColumnRowCheckBoxes(selected);
        } else if (selectionType == FLOAT_SELECTION) {
            checkFloatSelectionCkeckboxes(selected);
        }
        initListener();
    }

    private void checkCellSelectionCkeckboxes(String selected) {
        TemplateCellElement firstCell = getFirstCell();
        if (firstCell == null) {
            resetCellElementCheckBoxes();
            return;
        }
        cellElementVisibleCheckBoxes.setSelected(!firstCell.isDoneAuthority(selected));
        newValue.setEnabled(!firstCell.isDoneAuthority(selected));
        if (!firstCell.isDoneAuthority(selected)) {
            newValue.setSelected(firstCell.isDoneNewValueAuthority(selected));
            if (newValue.isSelected()) {
                oldNewValueButton.setSelectedIndex(1);
                if (newValuePane != null){
                    newValuePane.setPreferredSize(VALUEPANE_NEW_DIMENSION);
                    newValueCard.show(newValuePane,"new");
                    newValuePane.setVisible(true);
                }
                valueEditor.setEnabled(true);
                valueEditor.populate(firstCell.getCellPrivilegeControl().getNewValueMap().get(selected));
            } else {
                if (newValuePane != null){
                    oldNewValueButton.setSelectedIndex(0);
                    newValuePane.setPreferredSize(VALUEPANE_OLD_DIMENSION);
                    newValueCard.show(newValuePane,"old");
                    newValuePane.setVisible(false);
                }
                valueEditor.setEnabled(false);
            }
        } else {
            oldNewValueButton.setSelectedIndex(0);
            if (newValuePane != null){
                newValuePane.setPreferredSize(new Dimension(0,0));
                newValueCard.show(newValuePane,"old");
                newValuePane.setVisible(false);
            }
            newValue.setSelected(false);
            valueEditor.setEnabled(false);
        }
        populateWidgetButton(firstCell.getWidget(), selected, firstCell);
    }

    private void populateWidgetButton(Widget widget, String selected, TemplateCellElement firstCell) {
        if (widget != null) {
            if (widget.isVisible()) {
                widgetVisible.setSelected(!widget.isDoneVisibleAuthority(selected));
                widgetVisible.setEnabled(!firstCell.isDoneAuthority(selected));
            } else {
                widgetVisible.setSelected(widget.isVisibleAuthority(selected));
            }
            if (widget.isEnabled()) {
                widgetAvailable.setSelected(!widget.isDoneUsableAuthority(selected));
                widgetAvailable.setEnabled(!widget.isDoneVisibleAuthority(selected));
            } else {
                widgetAvailable.setSelected(widget.isUsableAuthority(selected));
            }
        }
    }

    private void resetCellElementCheckBoxes() {
        cellElementVisibleCheckBoxes.setSelected(true);
        widgetVisible.setSelected(true);
        widgetVisible.setEnabled(true);
        widgetAvailable.setSelected(true);
        widgetAvailable.setEnabled(true);
        newValue.setSelected(false);
        valueEditor.setEnabled(false);
        oldNewValueButton.setSelectedIndex(0);
    }

    private void checkColumnRowCheckBoxes(String selected) {
        if (cellSelection == null) {
            gridColumnRowVisible.setSelected(false);
            return;
        }
        TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        boolean isInside = selectionType == CellSelection.CHOOSE_COLUMN ?
                elementCase.getColumnPrivilegeControl(cellSelection.getColumn()).checkInvisible(selected) :
                elementCase.getRowPrivilegeControl(cellSelection.getRow()).checkInvisible(selected);

        gridColumnRowVisible.setSelected(isInside);
    }

    private void checkFloatSelectionCkeckboxes(String selected) {
        String name = floatSelection.getSelectedFloatName();
        TemplateElementCase ec = elementCasePane.getEditingElementCase();
        FloatElement fe = ec.getFloatElement(name);
        floatElementVisibleCheckBoxes.setSelected(!fe.isDoneAuthority(selected));
    }

    private JPanel populateHyperlinkCheckPane(AbstractCellElement cellElement) {
        NameJavaScriptGroup linkGroup = cellElement.getNameHyperlinkGroup();
        //超链接的个数+单元格可见的操作
        hyperlinkCheckBoxes = new UICheckBox[linkGroup.size()];
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[hyperlinkCheckBoxes.length + 1][];
        if (linkGroup.size() == 1) {
            components[0] = new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cell"), SwingConstants.LEFT), cellElementVisibleCheckBoxes};
            components[1] = new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Hyperlink"), SwingConstants.LEFT), hyperlinkCheckBoxes[0] = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Visible"))};

        } else {
            components[0] = new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cell"), SwingConstants.LEFT), cellElementVisibleCheckBoxes = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Visible"))};
            components[1] = new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Hyperlink"), SwingConstants.LEFT), hyperlinkCheckBoxes[0] = new UICheckBox(linkGroup.getNameHyperlink(0).getName() + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Visible"))};
            for (int i = 1; i < hyperlinkCheckBoxes.length; i++) {
                components[i + 1] = new Component[]{null, hyperlinkCheckBoxes[i] = new UICheckBox(linkGroup.getNameHyperlink(i).getName() + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Visible"))};
            }
        }
        for (int i = 0; i < hyperlinkCheckBoxes.length; i++) {
            hyperlinkCheckBoxes[i].setEnabled(cellElementVisibleCheckBoxes.isSelected());
        }
        addHyperlinkListener();
        double[] rowSize = new double[hyperlinkCheckBoxes.length + 1];
        int[][] rowCount = new int[hyperlinkCheckBoxes.length + 1][];
        for (int i = 0; i < hyperlinkCheckBoxes.length + 1; i++) {
            rowSize[i] = p;
            rowCount[i] = new int[]{1, 1};
        }
        double[] columnSize = {p, f};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);

    }

    private JPanel populateMutilHyperlinkCheckPane() {
        //超链接的个数+单元格可见的操作
        hyperlinkCheckBoxes = new UICheckBox[1];
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[2][];
        components[0] = new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cell"), SwingConstants.LEFT), cellElementVisibleCheckBoxes};
        components[1] = new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Hyperlink"), SwingConstants.LEFT), hyperlinkCheckBoxes[0] = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Visible"))};
        hyperlinkCheckBoxes[0].setEnabled(cellElementVisibleCheckBoxes.isSelected());
        addHyperlinkListener();
        double[] rowSize = {p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }

    private JPanel valueGroup(){
        newValueCard = new CardLayout();
        newValuePane = new JPanel(newValueCard);
        newValuePane.add(valueEditor, "new");
        newValuePane.add(new JPanel(), "old");
        newValuePane.setPreferredSize(VALUEPANE_OLD_DIMENSION);
        oldNewValueButton.setPreferredSize(VALUEPANE_NEW_DIMENSION);
        return oldNewValueButton;
    }
}
