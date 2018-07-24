package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.form.ui.TreeEditor;


import java.awt.*;

public class TreeComboBoxEditorDefinePane extends TreeEditorDefinePane {

	protected UITextField waterMarkDictPane;

	public TreeComboBoxEditorDefinePane(XCreator xCreator) {
		super(xCreator);
	}

	public Component[] createWaterMarkPane(){
		waterMarkDictPane = new UITextField();
		return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_WaterMark")), waterMarkDictPane};
	}

	@Override
	public String title4PopupWindow() {
		return "treecombobox";
	}

	protected  void populateSubCustomWritableRepeatEditorBean(TreeEditor ob){
		super.populateSubCustomWritableRepeatEditorBean(ob);
		waterMarkDictPane.setText(ob.getWaterMark());
	}

	protected  TreeEditor updateSubCustomWritableRepeatEditorBean(){
		TreeEditor editor = super.updateSubCustomWritableRepeatEditorBean();
		editor.setWaterMark(waterMarkDictPane.getText());
		return editor;
	}



	@Override
    public DataCreatorUI dataUI() {
        return null;
    }
}