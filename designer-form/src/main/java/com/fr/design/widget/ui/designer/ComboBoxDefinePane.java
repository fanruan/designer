package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.form.ui.ComboBox;


import javax.swing.*;
import java.awt.*;

public class ComboBoxDefinePane extends DictEditorDefinePane<ComboBox> {
	private UICheckBox removeRepeatCheckBox;
	private UITextField waterMarkField;

	public ComboBoxDefinePane(XCreator xCreator) {
		super(xCreator);
	}

	public UICheckBox createRepeatCheckBox(){
		removeRepeatCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_No_Repeat"));
		removeRepeatCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return removeRepeatCheckBox;
	}

	public Component[] createWaterMarkPane() {
		waterMarkField = new UITextField();
		return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WaterMark")), waterMarkField};
	}

	protected  void populateSubDictionaryEditorBean(ComboBox ob){
		removeRepeatCheckBox.setSelected(ob.isRemoveRepeat());
		waterMarkField.setText(ob.getWaterMark());
		formWidgetValuePane.populate(ob);
	}

	protected  ComboBox updateSubDictionaryEditorBean(){
		ComboBox combo = (ComboBox) creator.toData();
		combo.setWaterMark(waterMarkField.getText());
		combo.setRemoveRepeat(removeRepeatCheckBox.isSelected());
		formWidgetValuePane.update(combo);
		return combo;
	}


	@Override
	public String title4PopupWindow() {
		return "ComboBox";
	}

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }
}
