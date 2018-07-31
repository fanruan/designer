/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.JComponent;

import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.ibutton.UIPasswordField;
import com.fr.design.mainframe.widget.editors.RegexEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.RegexCellRencerer;
import com.fr.form.ui.Password;

import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class XPassword extends XWrapperedFieldEditor {

    public XPassword(Password widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = new UIPasswordField();
        }
        return editor;
    }

    @Override
    protected String getIconName() {
        return "password_field_16.png";
    }

    /**
     * 控件的属性列表
     *
     * @return 此控件所用的属性列表
     * @throws IntrospectionException 异常
     */
    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        CRPropertyDescriptor[] sup = (CRPropertyDescriptor[]) ArrayUtils.addAll(
                new CRPropertyDescriptor[]{
                        new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
                                com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Estate_Widget_Value")).setEditorClass(
                                WidgetValueEditor.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "FR-Designer_Advanced")}
                , super.supportedDescriptor());
        CRPropertyDescriptor regErrorMessage = new CRPropertyDescriptor("regErrorMessage", this.data.getClass()).setI18NName(
                com.fr.design.i18n.Toolkit.i18nText("FR-Engine_Verify-Message")).putKeyValue(XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate");
        CRPropertyDescriptor regex = new CRPropertyDescriptor("regex", this.data.getClass())
                .setI18NName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Input_Rule"))
                .setEditorClass(RegexEditor.RegexEditor4TextArea.class)
                .putKeyValue("renderer", RegexCellRencerer.class)
                .putKeyValue(XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate");
        CRPropertyDescriptor waterMark = new CRPropertyDescriptor("waterMark", this.data.getClass())
                .setI18NName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_WaterMark"))
                .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "FR-Designer_Advanced");
        boolean displayRegField = true;
        displayRegField = isDisplayRegField(displayRegField);
        return displayRegField ? (CRPropertyDescriptor[]) ArrayUtils.addAll(sup, new CRPropertyDescriptor[]{regex, regErrorMessage, waterMark}) :
                (CRPropertyDescriptor[]) ArrayUtils.addAll(sup, new CRPropertyDescriptor[]{regex, waterMark});
    }

}