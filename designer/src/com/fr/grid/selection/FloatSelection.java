package com.fr.grid.selection;

import java.awt.Toolkit;

import javax.swing.JPopupMenu;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.cell.CleanAuthorityAction;
import com.fr.design.actions.cell.FloatStyleAction;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.edit.CopyAction;
import com.fr.design.actions.edit.CutAction;
import com.fr.design.actions.edit.DeleteAction;
import com.fr.design.actions.edit.EditFloatElementNameAction;
import com.fr.design.actions.edit.HyperlinkAction;
import com.fr.design.actions.edit.PasteAction;
import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.cell.clipboard.FloatElementsClip;
import com.fr.design.designer.TargetComponent;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.ElementCasePane.Clear;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.design.selection.QuickEditor;
import com.fr.stable.ColumnRow;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.OLDPIX;
import com.fr.design.utils.DesignUtils;
/**
 * the float selection
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
        DesignUtils.errorMessage(Inter.getLocText(new String[]{"Only_selected_cell_can_paste_only", "M_Insert-Cell"}));

        return false;
    }

    @Override
    public boolean pasteString(String str, ElementCasePane ePane) {
        Toolkit.getDefaultToolkit().beep();
        DesignUtils.errorMessage(Inter.getLocText(new String[]{"Only_selected_cell_can_paste_only", "Character"}));
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
        JPopupMenu popup = new JPopupMenu();
        if (BaseUtils.isAuthorityEditing()) {
            popup.add(new CleanAuthorityAction(ePane).createMenuItem());
            return popup;
        }
        popup.add(DeprecatedActionManager.getCellMenu(ePane).createJMenu());
        popup.add(new FloatStyleAction(ePane).createMenuItem());
        popup.add(new HyperlinkAction(ePane).createMenuItem());

        // cut, copy and paste
        popup.addSeparator();
        popup.add(new CutAction(ePane).createMenuItem());
        popup.add(new CopyAction(ePane).createMenuItem());
        popup.add(new PasteAction(ePane).createMenuItem());
        popup.add(new DeleteAction(ePane).createMenuItem());

        popup.addSeparator();
        popup.add(DeprecatedActionManager.getOrderMenu(ePane));
        popup.add(new EditFloatElementNameAction(ePane).createMenuItem());

        return popup;
    }

    @Override
    public boolean clear(Clear type, ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();
        FloatElement fe = ec.getFloatElement(selectedFloatName);
        if (fe != null) {
            ec.removeFloatElement(fe);
            ePane.setSelection(new CellSelection(0, 0, 1, 1));

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
        Object value = selectedFloat.getValue();
        value = value == null ? "" : value;
        value = value instanceof Number ? value.toString() : value;
        QuickEditor editor = ActionUtils.getFloatEditor(value.getClass());
        editor.populate(tc);
        return editor;
    }
}