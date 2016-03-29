package com.fr.design.mainframe;

import java.awt.CardLayout;

import javax.swing.JComponent;

import com.fr.base.BaseUtils;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.report.TemplateReport;
import com.fr.report.worksheet.WorkSheet;

/**
 * 中间的整个报表编辑区域
 *
 * @editor zhou
 * @since 2012-3-27下午12:12:11
 */
public class ReportComponentCardPane extends JComponent implements TargetModifiedListener {
    protected ReportComponent editingComponet;

    private CardLayout cl;
    private WorkSheetDesigner sheetDezi;
    private PolyDesigner polyDezi;

    public ReportComponentCardPane() {
        this.setLayout(cl = new CardLayout());
    }

    public void requestGrifFocus() {
        if (sheetDezi != null) {
            sheetDezi.requestFocus();
        }
    }

    protected void stopEditing() {
        if (editingComponet != null) {
            editingComponet.stopEditing();
        }
    }

    protected boolean cut() {
        if (editingComponet != null) {
            return editingComponet.cut();
        }

        return false;
    }

    protected void copy() {
        if (editingComponet != null) {
            editingComponet.copy();
        }
    }

    protected boolean paste() {
        if (editingComponet != null) {
            return editingComponet.paste();
        }

        return false;
    }

    // TODO ALEX_SEP 期望与TargetComponent那边合并,或者减少与JWorkBook之间的层次
    private java.util.List<TargetModifiedListener> targetModifiedList = new java.util.ArrayList<TargetModifiedListener>();

    /**
     * 添加 TargetModifiedListener
     *
     * @param l TargetModifiedListener
     */
    public void addTargetModifiedListener(TargetModifiedListener l) {
        targetModifiedList.add(l);
    }

    /**
     * Invoked when the target of the listener has changed the rpt content.
     *
     * @param e
     */
    @Override
    public void targetModified(TargetModifiedEvent e) {
        for (TargetModifiedListener l : targetModifiedList) {
            l.targetModified(e);
        }
    }

    protected void showJWorkSheet(WorkSheet sheet) {
        if (sheetDezi == null) {
            sheetDezi = new WorkSheetDesigner(sheet);
            this.add(sheetDezi, "WS");
            sheetDezi.addTargetModifiedListener(this);
        } else {
            sheetDezi.setTarget(sheet);
        }
        cl.show(this, "WS");
        editingComponet = sheetDezi;
    }

    protected void showPoly(PolyWorkSheet sheet) {
        if (polyDezi == null) {
            polyDezi = new PolyDesigner(sheet);
            this.add(polyDezi, "PL");
            polyDezi.addTargetModifiedListener(this);
        } else {
            polyDezi.setTarget(sheet);
        }

        cl.show(this, "PL");
        editingComponet = polyDezi;
    }


    protected void populate(TemplateReport tpl) {
        if (tpl instanceof WorkSheet) {
            showJWorkSheet((WorkSheet) tpl);
        } else if (tpl instanceof PolyWorkSheet) {
            showPoly((PolyWorkSheet) tpl);
        }
        if (BaseUtils.isAuthorityEditing()) {
            JTemplate editingTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            editingTemplate.setSheetCovered(tpl.getWorkSheetPrivilegeControl().checkInvisible(selectedRoles));
        }
    }

    protected TemplateReport update() {
        if (polyDezi != null && polyDezi.isVisible()) {
            return polyDezi.getTarget();
        } else if (sheetDezi != null && sheetDezi.isVisible()) {
            return sheetDezi.getTarget();
        }

        return null;
    }
}