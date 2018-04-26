/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.hanlder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.fr.design.mainframe.DesignerContext;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyUtils;
import com.fr.poly.creator.BlockCreator;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-2 用于聚合报表块的编辑、移动、删除等操作的监听器
 */
public class DataEditingListener extends MouseAdapter {

	private PolyDesigner designer;

	public DataEditingListener(PolyDesigner designer) {
		this.designer = designer;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!designer.isFocusOwner()) {
			// 获取焦点，以便获取热键
			designer.requestFocus();
		}

		designer.stopEditing();
		int x = e.getX() + designer.getHorizontalValue();
		int y = e.getY() + designer.getVerticalValue();
		BlockCreator creator = PolyUtils.searchAt(designer, x, y);
		designer.setSelection(creator);
		DesignerContext.getDesignerFrame().resetToolkitByPlus(DesignerContext.getDesignerFrame().getSelectedJTemplate());
	}


	public void mouseMoved(MouseEvent e) {
		if (designer.getSelection() != null) {
			designer.getSelection().checkButtonEnable();
		}
	}

}