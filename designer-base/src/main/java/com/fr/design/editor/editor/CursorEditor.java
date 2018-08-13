package com.fr.design.editor.editor;

import java.awt.Dimension;

import com.fr.design.gui.ilable.UILabel;

import com.fr.design.layout.FRGUIPaneFactory;

//TODO:august what's this?
public class CursorEditor extends Editor<CursorEditor> {
	private UILabel label;
	
	public CursorEditor(){
		this.setLayout(FRGUIPaneFactory.createCenterFlowLayout());
		label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cursor"));
		this.add(label);
		this.setPreferredSize(new Dimension(10,20));
		this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cursor"));
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
		label.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cursor"));
	}

}