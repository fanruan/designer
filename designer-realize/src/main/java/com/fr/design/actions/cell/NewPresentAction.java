package com.fr.design.actions.cell;

import com.fr.base.present.Present;
import com.fr.design.actions.PresentCheckBoxAction;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;

import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.TemplateCellElement;
import com.fr.stable.StableUtils;

public class NewPresentAction extends PresentCheckBoxAction {

    private String itemName = null;

    public NewPresentAction(ElementCasePane t, String title, String name) {
        super(t);

        this.setName(title);
        this.itemName = name;
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        if (!ComparatorUtils.equals(this.itemName, "NOPRESENT")) {
            CellElementPropertyPane.getInstance().GoToPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Present"), this.itemName);
        } else {
            TemplateCellElement ce = getSelectedCellElement();
            // 只有原来ce设置了形态的情况下才有undo操作
            if (ce != null && ce.getPresent() != null) {
                ce.setPresent(null);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isSelected() {
        return hasCurrentPresentSet();
    }

    // TODO ALEX_SEP enable有没有可能也通过这个方法来解决呢?
    private TemplateCellElement getSelectedCellElement() {
        ElementCasePane ePane = this.getEditingComponent();
        Selection sel = ePane.getSelection();
        if (sel instanceof CellSelection) {
            return (TemplateCellElement) ePane.getEditingElementCase().getCellElement(((CellSelection) sel).getColumn(), ((CellSelection) sel).getRow());
        }
        return null;
    }

    @Override
    public void update() {
        ElementCasePane ePane = this.getEditingComponent();
        Selection sel = ePane.getSelection();
        if (sel instanceof CellSelection) {
            this.setEnabled(true);
        } else {
            this.setEnabled(false);
        }
    }

    public boolean hasCurrentPresentSet() {
        TemplateCellElement ce = getSelectedCellElement();
        if (ce != null) {
            Present currentPresent = ce.getPresent();
            try {
                Class clazz = GeneralUtils.classForName(itemName);
                if (itemName.equals(currentPresent.getClass().getName())) {
                    return StableUtils.objectInstanceOf(currentPresent, clazz);
                }
            } catch (Exception e) {
                return "NOPRESENT".equals(itemName) && currentPresent == null;
            }
        }
        return false;
    }
}
