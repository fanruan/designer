/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.beans.IntrospectionException;

import com.fr.form.ui.WriteAbleRepeatEditor;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class XWriteAbleRepeatEditor extends XDirectWriteEditor {

    public XWriteAbleRepeatEditor(WriteAbleRepeatEditor widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return (CRPropertyDescriptor[]) ArrayUtils.addAll(
            super.supportedDescriptor(),
            new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("removeRepeat", this.data.getClass()).setI18NName(Inter.getLocText("Form-Remove_Repeat")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                new CRPropertyDescriptor("waterMark", this.data.getClass()).setI18NName(Inter.getLocText("WaterMark")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
            });
    }
}