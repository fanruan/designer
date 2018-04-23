package com.fr.design.mainframe.widget.accessibles;
//package com.fr.design.mainframe.cell.editors.accessibles;
//
//import com.fr.form.gui.ColumnWidget4GridPane;
//import com.fr.form.ui.IndexWidget;
//import com.fr.design.mainframe.cell.wrappers.GridWidgetWrapper;
//import com.fr.design.dialog.BasicDialog;
//
//import com.fr.design.dialog.DialogActionAdapter;
//
//import javax.swing.*;
//
//
//public class AccessibleGridWidgetEditor extends UneditableAccessibleEditor {
//	private ColumnWidget4GridPane columnWidget;
//	public AccessibleGridWidgetEditor() {
//		super(new GridWidgetWrapper());
//	}
//
//	protected void showEditorPane() {
//		if (columnWidget == null) {
//			columnWidget = new ColumnWidget4GridPane();
//		}
//
//		BasicDialog dlg = columnWidget.showWindow(SwingUtilities.getWindowAncestor(this));
//		if (this.getValue() != null) {
//			columnWidget.populate((IndexWidget[])getValue());
//		}
//		dlg.addDialogActionListener(new DialogActionAdapter() {
//			public void doOk() {
//				IndexWidget[] iws = columnWidget.updateWidgets();
//				setValue(iws);
//				fireStateChanged();
//			}
//        });
//	    dlg.setVisible(true);
//	}
//
//}