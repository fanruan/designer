package com.fr.design.widget.ui.designer;

import com.fr.base.GraphHelper;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.CustomWriteAbleRepeatEditor;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Author : Shockway
 * Date: 13-9-18
 * Time: 下午2:17
 */
public abstract class CustomWritableRepeatEditorPane<T extends CustomWriteAbleRepeatEditor> extends WritableRepeatEditorPane<T> {

	private UICheckBox customDataCheckBox;
	private static final int CUSTOM_DATA_CHECK_BOX_WIDTH = GraphHelper.getLocTextWidth("Form-Allow_CustomData") + 30;
	private static final int CUSTOM_DATA_CHECK_BOX_HEIGHT = 30;

	public CustomWritableRepeatEditorPane(XCreator xCreator) {
		super(xCreator);
	}


	public  JPanel setValidatePane(){
		this.customDataCheckBox = new UICheckBox(Inter.getLocText("Form-Allow_CustomData"), false);
		this.customDataCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.customDataCheckBox.setPreferredSize(
				new Dimension(CUSTOM_DATA_CHECK_BOX_WIDTH, CUSTOM_DATA_CHECK_BOX_HEIGHT));
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