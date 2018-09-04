/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.DictionaryEditor;
import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.stable.ArrayUtils;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.IntrospectionException;


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
		CRPropertyDescriptor [] sup = (CRPropertyDescriptor[]) ArrayUtils.addAll(new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Value")).setEditorClass(WidgetValueEditor.class)
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced"),
				new CRPropertyDescriptor("dictionary", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Dictionary")).setEditorClass(DictionaryEditor.class).setRendererClass(
						DictionaryRenderer.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced")},super.supportedDescriptor());
		CRPropertyDescriptor [] properties = (CRPropertyDescriptor[]) ArrayUtils.addAll(sup,getCRPropertyDescriptor());
		return	properties;
	}

	private CRPropertyDescriptor[] getCRPropertyDescriptor() throws IntrospectionException {
		CRPropertyDescriptor[] crp = new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("adaptive", this.data.getClass()).setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Adaptive"))
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced").setEditorClass(InChangeBooleanEditor.class),
				new CRPropertyDescriptor("chooseAll", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Choose_Type_All")).putKeyValue(
						XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced"),
				new CRPropertyDescriptor("returnString", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Return_String")).setEditorClass(InChangeBooleanEditor.class)
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced") };
		if (((CheckBoxGroup) this.toData()).isReturnString()) {
			crp = (CRPropertyDescriptor[]) ArrayUtils.addAll(crp, new CRPropertyDescriptor[] {
					new CRPropertyDescriptor("delimiter", this.data.getClass()).setI18NName(
							com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Delimiter")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced"),
					new CRPropertyDescriptor("startSymbol", this.data.getClass()).setI18NName(
							com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Combo_CheckBox_Start_Symbol")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced"),
					new CRPropertyDescriptor("endSymbol", this.data.getClass()).setI18NName(
							com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Combo_CheckBox_End_Symbol")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced") });
		}
		if (!((CheckBoxGroup) this.toData()).isAdaptive()) {
			crp = (CRPropertyDescriptor[]) ArrayUtils.add(crp, new CRPropertyDescriptor("columnsInRow", this.data
					.getClass()).setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Button_Group_Display_Columns")).putKeyValue(
					XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced"));
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
