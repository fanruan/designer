/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.DictionaryEditor;
import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.RadioGroup;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class XRadioGroup extends XFieldEditor {

	public XRadioGroup(RadioGroup widget, Dimension initSize) {
		super(widget, initSize);
	}

	@Override
	public RadioGroup toData() {
		return (RadioGroup) data;
	}

	@Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(),getCRPropertyDescriptor());
	}
	
	private CRPropertyDescriptor[] getCRPropertyDescriptor() throws IntrospectionException {
		CRPropertyDescriptor[] crp = new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
						Inter.getLocText(new String[]{"Widget", "Value"})).setEditorClass(WidgetValueEditor.class),
				new CRPropertyDescriptor("dictionary", this.data.getClass()).setI18NName(
						Inter.getLocText("DS-Dictionary")).setEditorClass(DictionaryEditor.class).setRendererClass(
						DictionaryRenderer.class),
				new CRPropertyDescriptor("adaptive", this.data.getClass()).setI18NName(Inter.getLocText("Adaptive"))
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced").setEditorClass(InChangeBooleanEditor.class)};
		if (!toData().isAdaptive()) {
			crp = (CRPropertyDescriptor[]) ArrayUtils.add(crp, new CRPropertyDescriptor("columnsInRow", this.data
					.getClass()).setI18NName(Inter.getLocText("Button-Group-Display-Columns")).putKeyValue(
					XCreatorConstants.PROPERTY_CATEGORY, "Advanced"));
		}
		return crp;
	}

	@Override
	protected JComponent initEditor() {
		if (editor == null) {
			editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
			editor.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			ButtonGroup bg = new ButtonGroup();
			JRadioButton radioLeft = new JRadioButton();
			radioLeft.setSelected(true);
			JRadioButton radioRight = new JRadioButton();
			bg.add(radioLeft);
			bg.add(radioRight);
			editor.add(radioLeft, BorderLayout.WEST);
			editor.add(radioRight, BorderLayout.EAST);
		}
		return editor;
	}

	@Override
	protected String getIconName() {
		return "button_group_16.png";
	}
}