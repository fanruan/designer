package com.fr.design.editor.editor;

import java.awt.Dimension;

import com.fr.design.gui.ilable.UILabel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
//TODO:august what's this?
public class CursorEditor extends Editor<CursorEditor> {
	private UILabel label;
	
	public CursorEditor(){
		this.setLayout(FRGUIPaneFactory.createCenterFlowLayout());
		label = new UILabel(Inter.getLocText("Cursor"));
		this.add(label);
		this.setPreferredSize(new Dimension(10,20));
		this.setName(Inter.getLocText("Cursor"));
		this.setEnabled(false);
	}

	@Override
	public CursorEditor getValue() {
		return this;
	}
	
	@Override
	public String getIconName() {
		return "type_cursor";
	}

	@Override
	public boolean accept(Object object) {
		return object instanceof CursorEditor ;
	}

	@Override
	public void setValue(CursorEditor value) {
		label.setText(Inter.getLocText("Cursor"));
	}

}