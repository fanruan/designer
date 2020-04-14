/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.columnrow;

import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.frpane.UnitInputPane;
import com.fr.design.gui.frpane.UnitInputPane.ValueNotChangeException;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.DialogActionAdapter;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.unit.ReportLengthUNIT;
import com.fr.design.unit.UnitConvertUtil;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.ArrayUtils;
import com.fr.stable.unit.UNIT;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class ColumnRowSizingAction extends AbstractColumnRowIndexAction {
	protected ColumnRowSizingAction(ElementCasePane t, int indexOfColumnOrRow) {
		super(t, indexOfColumnOrRow);
	}

	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(
			CellSelection cs) {
        final ElementCasePane ePane = this.getEditingComponent();
        final ElementCase report = ePane.getEditingElementCase();

        final UnitInputPane uPane = new UnitInputPane(3,title4UnitInputPane()) {
        	@Override
        	protected String title4PopupWindow() {
        		return ColumnRowSizingAction.this.title4UnitInputPane();
        	}
        };
        UNIT len = getShowLen(report, cs);
        populateNumberDialog(uPane, len);

        final CellSelection finalCS = cs;
        uPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            @Override
            public void doOk() {
                try {
					float newHeight = (float) uPane.update();
					int unitType = DesignerEnvManager.getEnvManager().getReportLengthUnit();
                    ReportLengthUNIT lengthUNIT = UnitConvertUtil.parseLengthUNIT(unitType);
					UNIT len = lengthUNIT.float2UNIT(newHeight);
					updateAction(report, len, finalCS);
				} catch (ValueNotChangeException e) {
					// nothing
				}

                ePane.fireTargetModified();
            }
        }).setVisible(true);
        return false;
	}

    protected void populateNumberDialog(final UnitInputPane uPane, UNIT unit) {
        int unitType = DesignerEnvManager.getEnvManager().getReportLengthUnit();
        ReportLengthUNIT lengthUNIT = UnitConvertUtil.parseLengthUNIT(unitType);
        float va = lengthUNIT.unit2Value4Scale(unit);
        uPane.setUnitText(lengthUNIT.unitText());
        uPane.populate(va);
    }

    protected abstract String title4UnitInputPane();

    protected abstract void updateAction(ElementCase report, UNIT len, CellSelection cs);

    protected abstract UNIT getShowLen(ElementCase report, CellSelection cs);

    protected abstract UNIT getIndexLen(int index, ElementCase report);

    protected UNIT getSelectedCellsLen(int[] cells, ElementCase report){
        int length = ArrayUtils.getLength(cells);
        UNIT temp = null;
        for (int i = 0; i < length; i++) {
            UNIT current = getIndexLen(cells[i], report);
            if (temp == null) {
                temp = current;
                continue;
            }
            if (temp.subtract(current).not_equal_zero()){
                return UNIT.ZERO;
            }
        }

        return getIndexLen(this.getIndex(), report);
    }
}
