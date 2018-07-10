package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.FormDesigner;

public class Add extends Outer {
	
	private Cursor addCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			BaseUtils.readImage("/com/fr/design/images/form/designer/cursor/add.png"), new Point(0, 0),
			"add");

	public void updateCursor(FormDesigner formEditor) {
		formEditor.setCursor(addCursor);
	}
}