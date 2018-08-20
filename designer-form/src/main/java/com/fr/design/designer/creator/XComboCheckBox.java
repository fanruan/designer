/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.form.ui.ComboCheckBox;
import com.fr.design.form.util.XCreatorConstants;

import com.fr.stable.ArrayUtils;

import java.awt.*;
import java.beans.IntrospectionException;
/**
 * @author richer
 * @since 6.5.3
 */
public class XComboCheckBox extends XComboBox {

    public XComboCheckBox(ComboCheckBox widget, Dimension initSize) {
        super(widget, initSize);
    }
    
    @Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(), !((ComboCheckBox) this.toData())
				.isReturnString() ? new CRPropertyDescriptor[] {
                new CRPropertyDescriptor("supportTag", this.data.getClass()).setI18NName(
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Support_Tag")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
                        "Advanced"),
				new CRPropertyDescriptor("delimiter", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Delimiter")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
						"Advanced"),
				new CRPropertyDescriptor("returnString", this.data.getClass()).setEditorClass(
						InChangeBooleanEditor.class).setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Return_String"))
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced") } : new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("delimiter", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Delimiter")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
						"Advanced"),
				new CRPropertyDescriptor("returnString", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Return_String")).setEditorClass(InChangeBooleanEditor.class)
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
				new CRPropertyDescriptor("startSymbol", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Combo_CheckBox_Start_Symbol"))
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
				new CRPropertyDescriptor("endSymbol", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Combo_CheckBox_End_Symbol"))
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced") });
	}

    @Override
    protected String getIconName() {
        return "combo_check_16.png";
    }
}
