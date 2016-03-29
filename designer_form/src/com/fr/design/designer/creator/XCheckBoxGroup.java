/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.DictionaryEditor;
import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;


/**
 * @author richer
 * @since 6.5.3
 */
public class XCheckBoxGroup extends XFieldEditor {

    public XCheckBoxGroup(CheckBoxGroup widget, Dimension initSize) {
        super(widget, initSize);
    }

	@Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(), getCRPropertyDescriptor());
	}
	
	private CRPropertyDescriptor[] getCRPropertyDescriptor() throws IntrospectionException {
		CRPropertyDescriptor[] crp = new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
						Inter.getLocText(new String[]{"Widget", "Value"})).setEditorClass(WidgetValueEditor.class),
				new CRPropertyDescriptor("dictionary", this.data.getClass()).setI18NName(
						Inter.getLocText("DS-Dictionary")).setEditorClass(DictionaryEditor.class).setRendererClass(
						DictionaryRenderer.class),
				new CRPropertyDescriptor("adaptive", this.data.getClass()).setI18NName(Inter.getLocText("Adaptive"))
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced").setEditorClass(InChangeBooleanEditor.class),
				new CRPropertyDescriptor("chooseAll", this.data.getClass()).setI18NName(
						Inter.getLocText(new String[]{"Provide", "Choose_All"})).putKeyValue(
						XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
				new CRPropertyDescriptor("returnString", this.data.getClass()).setI18NName(
						Inter.getLocText("Return-String")).setEditorClass(InChangeBooleanEditor.class).putKeyValue(
						XCreatorConstants.PROPERTY_CATEGORY, "Return-Value") };
		if (((CheckBoxGroup) this.toData()).isReturnString()) {
			crp = (CRPropertyDescriptor[]) ArrayUtils.addAll(crp, new CRPropertyDescriptor[] {
					new CRPropertyDescriptor("delimiter", this.data.getClass()).setI18NName(
							Inter.getLocText("Form-Delimiter")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
							"Return-Value"),
					new CRPropertyDescriptor("startSymbol", this.data.getClass()).setI18NName(
							Inter.getLocText("ComboCheckBox-Start_Symbol")).putKeyValue(
							XCreatorConstants.PROPERTY_CATEGORY, "Return-Value"),
					new CRPropertyDescriptor("endSymbol", this.data.getClass()).setI18NName(
							Inter.getLocText("ComboCheckBox-End_Symbol")).putKeyValue(
							XCreatorConstants.PROPERTY_CATEGORY, "Return-Value") });
		}
		if (!((CheckBoxGroup) this.toData()).isAdaptive()) {
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
            UICheckBox cb1 = new UICheckBox();
            editor.add(cb1, BorderLayout.WEST);
            UICheckBox cb2 = new UICheckBox();
            editor.add(cb2, BorderLayout.EAST);
        }
        return editor;
    }

    @Override
    protected String getIconName() {
        return "checkbox_group_16.png";
    }
}