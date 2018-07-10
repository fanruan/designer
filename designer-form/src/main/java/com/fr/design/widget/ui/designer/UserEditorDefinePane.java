package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.NameWidget;

public class UserEditorDefinePane extends AbstractDataModify<NameWidget> {
	private NameWidget nWidget;
	public UserEditorDefinePane(XCreator xCreator) {
		super(xCreator);
		this.initComponents();
	}
	
	private void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
	}
	
	@Override
	public String title4PopupWindow() {
		return "name";
	}
	
	@Override
	public void populateBean(NameWidget cellWidget) {
		nWidget = cellWidget;
	}
	
	@Override
	public NameWidget updateBean() {
		return nWidget;
	}
}