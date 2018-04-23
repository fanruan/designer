package com.fr.design.dscolumn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.fr.design.data.DesignTableDataManager;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.group.CustomGrouper;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.cellattr.core.group.FunctionGrouper;
import com.fr.report.cell.cellattr.core.group.RecordGrouper;

//august:1:31 我又改回以前的样子了 neil那个方法还是有bug 9922
public abstract class ResultSetGroupPane extends JPanel {
	
	protected static final int COMMON = 0;
	protected static final int CONTINUUM = 1;
	protected static final int ADVANCED = 2;
	
	protected TemplateCellElement cellElement;
	protected RecordGrouper recordGrouper;
	protected UIComboBox groupComboBox;
	
	protected ResultSetGroupPane(){
		groupComboBox = new UIComboBox(new String[] {Inter.getLocText("Common"), Inter.getLocText("Continuum"), Inter.getLocText("Advanced")});
	}

	abstract void populate(TemplateCellElement cellElement);

	abstract void update();

	abstract void setRecordGrouper(RecordGrouper recordGrouper);

	void fireTargetChanged() {
	};

	ActionListener groupAdvancedListener = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			if (cellElement == null) {
				return;
			}

			final SpecifiedGroupAttrPane specifiedGroupAttrPane;
			Object value = cellElement.getValue();
			if (value == null || !(value instanceof DSColumn)) {
				return;
			}

			final DSColumn dSColumn = (DSColumn) value;
			String[] columnNames = DesignTableDataManager.getSelectedColumnNames(DesignTableDataManager.getEditingTableDataSource(), dSColumn.getDSName());
			specifiedGroupAttrPane = new SpecifiedGroupAttrPane(columnNames);

			specifiedGroupAttrPane.populate(recordGrouper);
			specifiedGroupAttrPane.showWindow(SwingUtilities.getWindowAncestor(ResultSetGroupPane.this), new DialogActionAdapter() {
				@Override
				public void doOk() {
					RecordGrouper rg = specifiedGroupAttrPane.update(cellElement, recordGrouper);
					if (!isNPE(cellElement)) {
						dSColumn.setGrouper(rg);
					}
					setRecordGrouper(rg);
					fireTargetChanged();
                    JTemplate targetComponent = DesignerContext.getDesignerFrame().getSelectedJTemplate();
                    if (targetComponent != null) {
                        targetComponent.fireTargetModified();
                        targetComponent.requestGridFocus();
                    }
				}
			}).setVisible(true);
		}
	};

	protected RecordGrouper updateGroupCombox() {
		if (groupComboBox.getSelectedIndex() == 0) {
			FunctionGrouper valueGrouper = new FunctionGrouper();
			valueGrouper.setDivideMode(FunctionGrouper.GROUPING_MODE);
			valueGrouper.setCustom(false);
			recordGrouper = valueGrouper;
		} else if (groupComboBox.getSelectedIndex() == 1) {
			FunctionGrouper valueGrouper = new FunctionGrouper();
			valueGrouper.setDivideMode(FunctionGrouper.CONTINUUM_MODE);
			valueGrouper.setCustom(false);
			recordGrouper = valueGrouper;
		} else if (groupComboBox.getSelectedIndex() == 2) {
			if (recordGrouper == null) {
				recordGrouper = new CustomGrouper();
			}
			if (!(recordGrouper instanceof CustomGrouper) && !(recordGrouper instanceof FunctionGrouper && ((FunctionGrouper) recordGrouper).isCustom())) {
				recordGrouper = new CustomGrouper();
			}
		}

		return recordGrouper;
	}

	protected boolean isNPE(CellElement cellElement) {
		if (cellElement == null) {
			return true;
		}
		if (cellElement.getValue() == null) {
			return true;
		}
		if (!(cellElement.getValue() instanceof DSColumn)) {
			return true;
		}
		return false;
	}

}