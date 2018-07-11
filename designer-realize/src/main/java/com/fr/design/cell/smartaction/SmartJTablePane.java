package com.fr.design.cell.smartaction;

import java.awt.BorderLayout;
import java.awt.Window;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionListener;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.selection.SelectionListener;

/*
 * SmartJTablePane用于在Grid上面选单元格时编辑JTable
 */
public abstract class SmartJTablePane extends BasicPane {
	
	public static final int OK = 0;
	public static final int CANCEL = 1;
	
	protected ElementCasePane actionReportPane;
	protected AbstractTableModel model;
	
	protected SelectionListener gridSelectionChangeL;
	protected SmartJTablePaneAction action;
	
	protected JTable table;
	protected JScrollPane scrollPane;

	protected boolean old_editable = true;

	protected int editingRowIndex = 0;

	public SmartJTablePane(AbstractTableModel model, 
			ElementCasePane actionReportPane) {
		this.model = model;
		this.actionReportPane = actionReportPane;
		old_editable = actionReportPane.isEditable();

		initComponents();
	}

	public void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		// BasicPane的north放描述
		this.add(new UILabel(Inter.getLocText("RWA-Click_Cell_To_Edit_Value")), BorderLayout.NORTH);

		// BasicPane的center放JTable
		table = new JTable(model);
		this.add(scrollPane = new JScrollPane(table), BorderLayout.CENTER);

		// 鼠标切换选中项时,editingRowIndex也要跟着变
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				int selected = table.getSelectedRow();
				// alex:当SmartJTablePane失去焦点时,好像也会触发这个,selected为-1
				if (selected >= 0) {
					setEditingRowIndex(selected);
					table.repaint();
				}
			}
		});

		setCellRenderer();
		actionReportPane.addSelectionChangeListener(gridSelectionChangeL);
	}
	
	public void changeGridSelectionChangeListener(SelectionListener g){
		actionReportPane.removeSelectionChangeListener(gridSelectionChangeL);
		gridSelectionChangeL = g;
		actionReportPane.addSelectionChangeListener(gridSelectionChangeL);
	}
	
	public void changeSmartJTablePaneAction(SmartJTablePaneAction a){
		this.action = a;
	}
	
	public abstract void setCellRenderer();
	
	
	
	/*
	 * 设置正在编辑的RowIndex,并Scroll
	 */
	protected void setEditingRowIndex(int idx) {
		editingRowIndex = idx;

		table.scrollRectToVisible(table.getCellRect(editingRowIndex, 2, true));
	}

	@Override
	public BasicDialog showWindow(Window window) {
		BasicDialog dlg = super.showSmallWindow(window,new DialogActionListener() {
			public void doOk() {
				action.doDialogExit(SmartJTablePane.OK);
			}

			public void doCancel() {
				action.doDialogExit(SmartJTablePane.CANCEL);
			}
		});
		

		return dlg;
	}
	
}