package com.fr.design.actions.cell;

import com.fr.base.present.Present;
import com.fr.design.actions.PresentCheckBoxAction;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;

import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.StableUtils;

import java.awt.*;

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
            CellElementPropertyPane.getInstance().GoToPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Present"), this.itemName);
        } else {
            ElementCasePane ePane = this.getEditingComponent();
            TemplateElementCase elementCase = ePane.getEditingElementCase();
            Selection sel = ePane.getSelection();
            if (sel instanceof CellSelection) {
                CellSelection cs = (CellSelection) sel;
                int cellRectangleCount = cs.getCellRectangleCount();
                for (int rect = 0; rect < cellRectangleCount; rect++) {
                    Rectangle cellRectangle = cs.getCellRectangle(rect);
                    for (int j = 0; j < cellRectangle.height; j++) {
                        for (int i = 0; i < cellRectangle.width; i++) {
                            int column = i + cellRectangle.x;
                            int row = j + cellRectangle.y;
                            TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                            if (cellElement == null) {
                                cellElement = new DefaultTemplateCellElement(column, row);
                                elementCase.addCellElement(cellElement);
                            } else if (cellElement.getPresent() != null) {
                                cellElement.setPresent(null);
                            }
                        }
                    }
                }
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
