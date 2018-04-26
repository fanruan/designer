package com.fr.design.widget.ui;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.DirectWriteEditor;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

//richer:需要提供能否直接编辑的控件设置面板——下拉框、复选框、时间、日期、下拉树
public abstract class DirectWriteEditorDefinePane<T extends DirectWriteEditor> extends FieldEditorDefinePane<T> {
	public UICheckBox directWriteCheckBox;
	protected WaterMarkDictPane waterMarkDictPane;

	public DirectWriteEditorDefinePane() {
		super();
	}


	@Override
	protected JPanel setFirstContentPane() {
		JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
		directWriteCheckBox = new UICheckBox(Inter.getLocText("Form-Allow_Edit"), false);
		directWriteCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		waterMarkDictPane = new WaterMarkDictPane();
		contentPane.add(waterMarkDictPane, BorderLayout.NORTH);
		JPanel otherContentPane = this.setSecondContentPane();
		if (otherContentPane != null) {
			contentPane.add(otherContentPane, BorderLayout.CENTER);
		}
		return contentPane;
	}



	public  JPanel setValidatePane(){
		JPanel otherContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		JPanel jPanel = GUICoreUtils.createFlowPane(new JComponent[]{directWriteCheckBox}, FlowLayout.LEFT, 0);
		otherContentPane.add(jPanel, BorderLayout.NORTH);
		return otherContentPane;
	}

	protected abstract JPanel setSecondContentPane();

	@Override
	protected void populateSubFieldEditorBean(T e) {
		this.directWriteCheckBox.setSelected(e.isDirectEdit());
		this.waterMarkDictPane.populate(e);

		populateSubDirectWriteEditorBean(e);
	}

	protected abstract void populateSubDirectWriteEditorBean(T e);

	@Override
	protected T updateSubFieldEditorBean() {
		T e = updateSubDirectWriteEditorBean();

		e.setDirectEdit(directWriteCheckBox.isSelected());
		this.waterMarkDictPane.update(e);

		return e;
	}

	protected abstract T updateSubDirectWriteEditorBean();
}