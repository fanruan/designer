package com.fr.design.data.tabledata.tabledatapane;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;

import com.fr.base.StoreProcedureParameter;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableModelAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;

public class StoreProcedureTableModel extends UITableModelAdapter<StoreProcedureParameter> {

	public StoreProcedureTableModel() {
		super(new String[] { Inter.getLocText("Parameter"), Inter.getLocText("Type"), Inter.getLocText("Model"), Inter.getLocText("Value") });
		if (shouldResponseDoubleClickAction()) {
            table.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() < 2) {
                        return;
                    }
                    final int selectedRow = table.getSelectedRow();
                    final List<StoreProcedureParameter> sppList = getList();
                    if (selectedRow < 0 || selectedRow >= sppList.size()) {
                        return;
                    }
                    StoreProcedureParameter spp = sppList.get(selectedRow);

                    final StoreProcedureParameterPane pane = new StoreProcedureParameterPane();
                    pane.populate(spp);
                    pane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Parameter")));
                    BasicDialog stpEditDialog = pane.showWindow(DesignerContext.getDesignerFrame());
                    stpEditDialog.addDialogActionListener(new DialogActionAdapter() {

                        @Override
                        public void doOk() {
                            StoreProcedureParameter spp = pane.update();
                            setSelectedValue(spp);
                            fireTableDataChanged();
                        }
                    });
                    stpEditDialog.setVisible(true);
                }
            });
        }

	}

	public Object getValueAt(int row, int col) {
		StoreProcedureParameter stp = getList().get(row);

		if (col == 0) {
			return stp.getName();
		} else if (col == 1) {
			return StoreProcedureParameterPane.getInfo4Name(stp.getType());
		} else if (col == 2) {
			return StoreProcedureParameterPane.getInfo4Name(stp.getSchema());
		} else if (col == 3) {
			return stp.getValue();
		}

		return null;
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

    public boolean shouldResponseDoubleClickAction () {
        return true;
    }
	@Override
	public UITableEditAction[] createAction() {
		return new UITableEditAction[] { new AddStoreProcedureAction(), new EditStoreProcedureAction(), new DeleteAction(), new MoveUpAction(),
				new MoveDownAction() };
	}

	private class AddStoreProcedureAction extends AddTableRowAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			StoreProcedureParameter para = new StoreProcedureParameter();
			final StoreProcedureParameterPane pane = new StoreProcedureParameterPane();
			pane.populate(para);
			pane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Parameter")));
			BasicDialog stpEditDialog = pane.showWindow(DesignerContext.getDesignerFrame());
			stpEditDialog.addDialogActionListener(new DialogActionAdapter() {

				@Override
				public void doOk() {
					StoreProcedureParameter spp = pane.update();
					addRow(spp);
					fireTableDataChanged();
				}
			});
			stpEditDialog.setVisible(true);
		}
	}

	private class EditStoreProcedureAction extends EditAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			StoreProcedureParameter para = getSelectedValue();
			final StoreProcedureParameterPane pane = new StoreProcedureParameterPane();
			pane.populate(para);
			pane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Parameter")));
			BasicDialog stpEditDialog = pane.showWindow(DesignerContext.getDesignerFrame());
			stpEditDialog.addDialogActionListener(new DialogActionAdapter() {

				@Override
				public void doOk() {
					StoreProcedureParameter spp = pane.update();
					setSelectedValue(spp);
					fireTableDataChanged();
				}
			});
			stpEditDialog.setVisible(true);
		}

		@Override
		public void checkEnabled() {
			if (table.getSelectedRow() < 0) {
				this.setEnabled(false);
			} else {
				this.setEnabled(true);
			}
		}

	}
}