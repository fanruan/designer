package com.fr.design.mainframe;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.general.ComparatorUtils;

import com.fr.main.impl.WorkBook;
import com.fr.privilege.finegrain.WorkSheetPrivilegeControl;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-1-17
 * Time: 上午10:56
 */
public class SheetAuthorityEditPane extends AuthorityEditPane {
    private static final int TOP_GAP = 11;
    private static final int LEFT_GAP = 4;
    private static final int ALIGNMENT_GAP = -3;

    private UICheckBox sheetVisible = new UICheckBox("sheet" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget_Visible"));
    private WorkBook workBook = null;
    private int selectedIndex = -1;
    private JPanel typePane;
    private JPanel namePane;

    private ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            String selectedRole = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if (ComparatorUtils.equals(selectedRole, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Role"))) {
                return;
            }
            if (selectedRole == null) {
                return;
            }

            WorkSheetPrivilegeControl wpc = workBook.getTemplateReport(selectedIndex)
                    .getWorkSheetPrivilegeControl();
            if (sheetVisible.isSelected()) {
                wpc.removeInvisibleRole(selectedRole);
            } else {
                wpc.addInvisibleRole(selectedRole);
            }
            JTemplate editingTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            editingTemplate.setSheetCovered(!sheetVisible.isSelected());
            editingTemplate.fireTargetModified();
            editingTemplate.refreshContainer();
            RolesAlreadyEditedPane.getInstance().refreshDockingView();
        }
    };


    public SheetAuthorityEditPane(WorkBook editingWorkBook, int selectedIndex) {
        super(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
        setLayout(new BorderLayout());
        type = new UILabel();
        typePane = new JPanel(new BorderLayout());
        typePane.add(type, BorderLayout.CENTER);
        type.setBorder(BorderFactory.createEmptyBorder(0,LEFT_GAP,0,0));
        typePane.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        name = new UILabel();
        namePane = new JPanel(new BorderLayout());
        namePane.add(name, BorderLayout.CENTER);
        name.setBorder(BorderFactory.createEmptyBorder(0,LEFT_GAP,0,0));
        namePane.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        checkPane = new JPanel();
        checkPane.setLayout(new BorderLayout());
//        this.add(layoutText(), BorderLayout.WEST);
//        this.add(layoutPane(), BorderLayout.CENTER);
        this.add(centerPane(), BorderLayout.CENTER);
        this.setBorder(BorderFactory.createEmptyBorder(TOP_GAP, LEFT_GAP, 0, RIGHT_GAP));
        this.workBook = editingWorkBook;
        sheetVisible.addItemListener(itemListener);
        this.selectedIndex = selectedIndex;
    }

    private JPanel centerPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Type") + "        ", SwingConstants.LEFT), typePane},
                new Component[]{new UILabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_WF_Name") + "        ", SwingConstants.LEFT), namePane},
                new Component[]{checkPane, null},
        };

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, LayoutConstants.VGAP_MEDIUM);
    }



    /**
     * 更新权限编辑面板的具体内容：类型、名称、权限面板
     */
    public void populateDetials() {
        if (workBook.getTemplateReport(selectedIndex) instanceof WorkSheet) {
            ((WorkSheet) workBook.getTemplateReport(selectedIndex)).setPaintSelection(false);
        }
        populateName();
        populateType();
        checkPane.removeAll();
        if (StringUtils.isEmpty(name.getText())){
            return;
        }
        checkPane.add(populateCheckPane(), BorderLayout.CENTER);
        checkPane.setBorder(BorderFactory.createEmptyBorder(0, LEFT_CHECKPANE, 0, 0));
        checkVisibleCheckBoxes();
    }

    /**
     * 刷新类型
     */
    public void populateType() {
        type.setText("sheet");
    }

    /**
     * 更新名字
     */
    public void populateName() {
        name.setText(workBook.getReportName(selectedIndex));
    }

    /**
     * 更新复选框面板
     *
     * @return 返回面板
     */
    public JPanel populateCheckPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{sheetVisible},
        };
        double[] rowSize = {p};
        double[] columnSize = {f};
        int[][] rowCount = {{1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }

    private void checkVisibleCheckBoxes() {
        sheetVisible.removeItemListener(itemListener);
        String selected = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if (selected == null) {
            sheetVisible.setSelected(true);
        }
        sheetVisible.setSelected(!workBook.getTemplateReport(selectedIndex).getWorkSheetPrivilegeControl().checkInvisible(selected));
        sheetVisible.addItemListener(itemListener);
    }

}
