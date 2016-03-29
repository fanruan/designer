package com.fr.design.widget.ui;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.WriteAbleRepeatEditor;

import javax.swing.*;
import java.awt.*;

public abstract class WritableRepeatEditorPane<E extends WriteAbleRepeatEditor> extends DirectWriteEditorDefinePane<E> {

	public WritableRepeatEditorPane() {
		this.initComponents();
	}


	@Override
	protected JPanel setSecondContentPane() {
		JPanel contentPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel contenter = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		contentPane.add(contenter,BorderLayout.NORTH);
		JPanel otherContentPane = this.setThirdContentPane();
		if (otherContentPane != null) {
			contentPane.add(otherContentPane,BorderLayout.CENTER);
		}
		return contentPane;
	}

	protected abstract JPanel setThirdContentPane();

	@Override
	protected void populateSubDirectWriteEditorBean(E e) {
		populateSubWritableRepeatEditorBean(e);
	}

	protected abstract void populateSubWritableRepeatEditorBean(E e);

	@Override
	protected E updateSubDirectWriteEditorBean() {
		return updateSubWritableRepeatEditorBean();
	}

	protected abstract E updateSubWritableRepeatEditorBean();
}