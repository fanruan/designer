package com.fr.grid.selection;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.actions.cell.CleanAuthorityAction;
import com.fr.design.actions.cell.FloatStyleAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.edit.CopyAction;
import com.fr.design.actions.edit.CutAction;
import com.fr.design.actions.edit.DeleteAction;
import com.fr.design.actions.edit.EditFloatElementNameAction;
import com.fr.design.actions.edit.HyperlinkAction;
import com.fr.design.actions.edit.PasteAction;
import com.fr.design.actions.edit.order.BringFloatElementForwardAction;
import com.fr.design.actions.edit.order.BringFloatElementToFrontAction;
import com.fr.design.actions.edit.order.SendFloatElementBackwardAction;
import com.fr.design.actions.edit.order.SendFloatElementToBackAction;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.cell.clipboard.FloatElementsClip;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.ElementCasePane.Clear;
import com.fr.design.selection.QuickEditor;
import com.fr.design.utils.DesignUtils;
import com.fr.general.ComparatorUtils;

import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.OLDPIX;

import javax.swing.JPopupMenu;
import java.awt.Toolkit;

/**
 * the float selection
 *
 * @editor zhou
 * 2012-3-22下午2:09:20
 */
public class FloatSelection extends Selection {
    private String selectedFloatName;

    public FloatSelection(String selectedFloatName) {
        setFloatName(selectedFloatName);
    }

    public final void setFloatName(String selectedFloatName) {
        this.selectedFloatName = selectedFloatName;
    }

    public String getSelectedFloatName() {
        return selectedFloatName;
    }


    @Override
    public void asTransferable(ElementsTransferable transferable, ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();
        FloatElement fe = ec.getFloatElement(selectedFloatName);
        if (fe != null) {
            FloatElement transEl;
            try {
                transEl = (FloatElement) fe.clone();

                transferable.addObject(new FloatElementsClip(transEl));
            } catch (CloneNotSupportedException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean pasteCellElementsClip(CellElementsClip ceClip, ElementCasePane ePane) {
        Toolkit.getDefaultToolkit().beep();
//        DesignUtils.errorMessage(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Only_selected_cell_can_paste_only", "M_Insert-Cell"}));
        DesignUtils.errorMessage(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Paste_Cell_Tips"));

        return false;
    }

    @Override
    public boolean pasteString(String str, ElementCasePane ePane) {
        Toolkit.getDefaultToolkit().beep();
//        DesignUtils.errorMessage(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Only_selected_cell_can_paste_only", "Character"}));
        DesignUtils.errorMessage(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Paste_Character_Tips"));
//		return pasteOtherType(str, ePane);
        return false;
    }

    @Override
    public boolean pasteOtherType(Object ob, ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();
        FloatElement selectedEl = ec.getFloatElement(this.selectedFloatName);

        if (selectedEl != null) {
            selectedEl.setValue(ob);
        }

        return true;
    }

    @Override
    public boolean canMergeCells(ElementCasePane ePane) {
        return false;
    }

    @Override
    public boolean mergeCells(ElementCasePane ePane) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canUnMergeCells(ElementCasePane ePane) {
        return false;
    }

    @Override
    public boolean unMergeCells(ElementCasePane ePane) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JPopupMenu createPopupMenu(ElementCasePane ePane) {
        UIPopupMenu popup = new UIPopupMenu();
        if (DesignerMode.isAuthorityEditing()) {
            popup.add(new CleanAuthorityAction(ePane).createMenuItem());
            return popup;
        }
        popup.add(new FloatStyleAction(ePane).createMenuItem());
        popup.add(new HyperlinkAction().createMenuItem());
        // cut, copy and paste
        popup.addSeparator();

        popup.add(new CutAction(ePane).createMenuItem());
        popup.add(new CopyAction(ePane).createMenuItem());
        popup.add(new PasteAction(ePane).createMenuItem());
        popup.add(new DeleteAction(ePane).createMenuItem());
        popup.addSeparator();

        popup.add(new BringFloatElementToFrontAction(ePane).createMenuItem());
        popup.add(new SendFloatElementToBackAction(ePane).createMenuItem());
        popup.add(new BringFloatElementForwardAction(ePane).createMenuItem());
        popup.add(new SendFloatElementBackwardAction(ePane).createMenuItem());
        popup.addSeparator();

        popup.add(new EditFloatElementNameAction(ePane).createMenuItem());

        return popup;
    }

    @Override
    public boolean clear(Clear type, ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();
        FloatElement fe = ec.getFloatElement(selectedFloatName);
        if (fe != null) {
            //  REPORT-5955 [Report]删除悬浮元素后，设计器卡死；之前wu做了释放内存，删除悬浮元素会报npe；删除悬浮元素逻辑改为先setSelection 再 remove
            ePane.setSelection(new CellSelection(0, 0, 1, 1));
            ec.removeFloatElement(fe);

            return true;
        }

        return false;
    }

    @Override
    public int[] getSelectedColumns() {
        return new int[0];
    }

    @Override
    public int[] getSelectedRows() {
        return new int[0];
    }

    @Override
    public void moveLeft(ElementCasePane ePane) {
        FloatElement selectedFloatElement = ePane.getEditingElementCase().getFloatElement(selectedFloatName);
        if (selectedFloatElement.getLeftDistance().less_than_or_equal_zero()) {
            selectedFloatElement.setLeftDistance(FU.getInstance(0));
        } else {
            selectedFloatElement.setLeftDistance(FU.getInstance(selectedFloatElement.getLeftDistance().toFU() - new OLDPIX(1).toFU()));
        }
    }

    @Override
    public void moveRight(ElementCasePane ePane) {
        FloatElement selectedFloatElement = ePane.getEditingElementCase().getFloatElement(selectedFloatName);
        selectedFloatElement.setLeftDistance(FU.getInstance(selectedFloatElement.getLeftDistance().toFU() + new OLDPIX(1).toFU()));
    }

    @Override
    public void moveUp(ElementCasePane ePane) {
        FloatElement selectedFloatElement = ePane.getEditingElementCase().getFloatElement(selectedFloatName);
        if (selectedFloatElement.getTopDistance().less_than_or_equal_zero()) {
            selectedFloatElement.setTopDistance(FU.getInstance(0));
        } else {
            selectedFloatElement.setTopDistance(FU.getInstance(selectedFloatElement.getTopDistance().toFU() - new OLDPIX(1).toFU()));
        }

    }

    @Override
    public void moveDown(ElementCasePane ePane) {
        FloatElement selectedFloatElement = ePane.getEditingElementCase().getFloatElement(selectedFloatName);
        selectedFloatElement.setTopDistance(FU.getInstance(selectedFloatElement.getTopDistance().toFU() + new OLDPIX(1).toFU()));
    }


    @Override
    public boolean triggerDeleteAction(ElementCasePane ePane) {
        return ePane.clearAll();
    }

    @Override
    public boolean containsColumnRow(ColumnRow cr) {
        return false;
    }

    @Override
    public boolean isSelectedOneCell(ElementCasePane ePane) {
        return false;
    }

    //TODO:august 这儿不比较FloatElement会不会有问题啊
    @Override
    public boolean equals(Object obj) {
        return obj instanceof FloatSelection && ComparatorUtils.equals(this.getSelectedFloatName(), ((FloatSelection) obj).getSelectedFloatName());
    }

    @Override
    public QuickEditor getQuickEditor(TargetComponent tc) {
        ElementCasePane ePane = (ElementCasePane) tc;
        FloatElement selectedFloat = ePane.getEditingElementCase().getFloatElement(selectedFloatName);
        Object value = null;
        //处理撤销时npe
        if (selectedFloat != null) {
            value = selectedFloat.getValue();
        }
        value = value == null ? "" : value;
        value = value instanceof Number ? value.toString() : value;
        QuickEditor editor = ActionFactory.getFloatEditor(value.getClass());
        editor.populate(tc);
        return editor;
    }

    @Override
    public void populatePropertyPane(ElementCasePane ePane) {
        CellElementPropertyPane.getInstance().removeAll();
    }

    public void populateWidgetPropertyPane(ElementCasePane ePane) {
    }

}