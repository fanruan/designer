/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Color;
import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.form.ui.FieldEditor;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class XFieldEditor extends XWidgetCreator {

	protected static final Border FIELDBORDER = BorderFactory.createLineBorder(new Color(128, 152, 186), 1);
	
    public XFieldEditor(FieldEditor widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return (CRPropertyDescriptor[]) ArrayUtils.addAll(
                super.supportedDescriptor(),getCRPropertyDescriptor()
               );
    }

	private CRPropertyDescriptor[] getCRPropertyDescriptor() throws IntrospectionException {
		CRPropertyDescriptor allowBlank = new CRPropertyDescriptor("allowBlank", this.data.getClass()).setI18NName(
								Inter.getLocText("Allow_Blank")).setEditorClass(InChangeBooleanEditor.class).putKeyValue(
								XCreatorConstants.PROPERTY_CATEGORY, "Advanced");
		CRPropertyDescriptor blankErrorMsg = new CRPropertyDescriptor("errorMessage", this.data.getClass()).setI18NName(
								Inter.getLocText("Verify-Message"))
								.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced");
		CRPropertyDescriptor fontSize = new CRPropertyDescriptor("fontSize", this.data.getClass(), "getFontSize", "setFontSize")
								.setI18NName(Inter.getLocText(new String[]{"FRFont", "FRFont-Size"}))
								.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced");
		return !((FieldEditor) toData()).isAllowBlank() ?
				new CRPropertyDescriptor[] {allowBlank, blankErrorMsg, fontSize}
				: new CRPropertyDescriptor[] {allowBlank, fontSize};
	}
}