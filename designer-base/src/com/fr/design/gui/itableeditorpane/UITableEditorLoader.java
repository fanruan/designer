package com.fr.design.gui.itableeditorpane;



public interface UITableEditorLoader {

	// august:生成工具栏上的一系列动作按钮
	UITableEditAction[] createAction();

	void stopCellEditing();

}