package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.CustomWriteAbleRepeatEditor;


import javax.swing.*;
import java.awt.*;

/**
 * Author : Shockway
 * Date: 13-9-18
 * Time: 下午2:17
 */
public abstract class CustomWritableRepeatEditorPane<T extends CustomWriteAbleRepeatEditor> extends WritableRepeatEditorPane<T> {

	private UICheckBox customDataCheckBox;

	public CustomWritableRepeatEditorPane(XCreator xCreator) {
		super(xCreator);
	}


	public  JPanel setValidatePane(){
		this.customDataCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_ Allow_Custom_Data"), false);
		this.customDataCheckBox.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		JPanel otherContentPane = super.setValidatePane();
		otherContentPane.add(GUICoreUtils.createFlowPane(new JComponent[]{customDataCheckBox}, FlowLayout.LEFT, 0));
		return otherContentPane;
	}



	protected void populateSubWritableRepeatEditorBean(T e) {
		this.customDataCheckBox.setSelected(e.isCustomData());
		populateSubCustomWritableRepeatEditorBean(e);
	}

	protected abstract void populateSubCustomWritableRepeatEditorBean(T e);

	protected T updateSubWritableRepeatEditorBean() {
		T e = updateSubCustomWritableRepeatEditorBean();
		e.setCustomData(this.customDataCheckBox.isSelected());
		return e;
	}

	protected abstract T updateSubCustomWritableRepeatEditorBean();

}