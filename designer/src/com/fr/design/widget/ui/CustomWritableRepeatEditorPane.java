package com.fr.design.widget.ui;

import com.fr.base.GraphHelper;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
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

	public CustomWritableRepeatEditorPane() {
		this.initComponents();
	}

	@Override
	protected JPanel setThirdContentPane() {
		JPanel contentPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.customDataCheckBox = new UICheckBox(Inter.getLocText("Form-Allow_CustomData"), false);
		this.customDataCheckBox.setPreferredSize(
				new Dimension(CUSTOM_DATA_CHECK_BOX_WIDTH, CUSTOM_DATA_CHECK_BOX_HEIGHT));
		this.customDataCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel otherContentPane = this.setForthContentPane();
		return otherContentPane;
	}


	public  JPanel setValidatePane(){
		JPanel otherContentPane = super.setValidatePane();
		JPanel jPanel = GUICoreUtils.createFlowPane(new JComponent[]{customDataCheckBox}, FlowLayout.LEFT, 0);
		otherContentPane.add(jPanel, BorderLayout.CENTER);
		return otherContentPane;
	}


	protected abstract JPanel setForthContentPane();

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