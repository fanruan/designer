package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UIPropertyTextField;
import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;

import java.awt.*;

public class TreeComboBoxEditorDefinePane extends TreeEditorDefinePane {

	protected UIPropertyTextField waterMarkDictPane;

	public TreeComboBoxEditorDefinePane(XCreator xCreator) {
		super(xCreator);
	}

	public Component[] createWaterMarkPane(){
		waterMarkDictPane = new UIPropertyTextField();
		return new Component[]{new UILabel(Inter.getLocText("FR-Designer_WaterMark")), waterMarkDictPane};
	}

	@Override
	public String title4PopupWindow() {
		return "treecombobox";
	}

	protected  void populateSubDictionaryEditorBean(TreeEditor ob){
		super.populateSubDictionaryEditorBean(ob);
		formWidgetValuePane.populate(ob);
		waterMarkDictPane.setText(ob.getWaterMark());
	}

	protected  TreeEditor updateSubDictionaryEditorBean(){
		TreeEditor editor = super.updateSubDictionaryEditorBean();
		formWidgetValuePane.update(editor);
		editor.setWaterMark(waterMarkDictPane.getText());
		return editor;
	}



	@Override
    public DataCreatorUI dataUI() {
        return treeSettingPane;
    }
}