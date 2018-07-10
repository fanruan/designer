package com.fr.design.cell.smartaction;

import java.awt.Container;
import java.awt.Dialog;

public abstract class AbstractSmartJTablePaneAction implements SmartJTablePaneAction {

	protected Container dialog;
	
	private SmartJTablePane smartJTablePane;
	
	public AbstractSmartJTablePaneAction(SmartJTablePane s, Container d){
		this.smartJTablePane = s;
		this.dialog = d;
	}
	
	public void setSmartJTablePane(SmartJTablePane s){
		this.smartJTablePane = s;
	}
	
	public void doDialogExit(int choice) {
		smartJTablePane.actionReportPane.getGrid().setNotShowingTableSelectPane(true);
		smartJTablePane.actionReportPane.setEditable(smartJTablePane.old_editable);
		smartJTablePane.actionReportPane.removeSelectionChangeListener(smartJTablePane.gridSelectionChangeL);
		smartJTablePane.actionReportPane.repaint();
		
		if(choice == SmartJTablePane.OK){
			doOk();
		}
		showDialog();
	}
	
	public void showDialog() {
		while (dialog != null) {
			if (dialog instanceof Dialog) {
				break;
			}
			dialog = dialog.getParent();
		}

		if (dialog != null) {
			dialog.repaint();
			dialog.setVisible(true);
		}
	}
	
	public abstract void doOk();

}