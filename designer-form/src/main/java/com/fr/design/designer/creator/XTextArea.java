/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.widget.editors.RegexEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.RegexCellRencerer;
import com.fr.form.ui.TextArea;
import com.fr.general.FRFont;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.IntrospectionException;

/**
 * @author richer
 * @since 6.5.3
 */
public class XTextArea extends XFieldEditor {

    public XTextArea(TextArea widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        CRPropertyDescriptor[] sup = (CRPropertyDescriptor[]) ArrayUtils.addAll(
                new CRPropertyDescriptor[]{
                        new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
                                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Estate_Widget_Value")).setEditorClass(
                                WidgetValueEditor.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Report_Advanced")}, super.supportedDescriptor());
        CRPropertyDescriptor regex = new CRPropertyDescriptor("regex", this.data.getClass()).setI18NName(
                com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Input_Rule")).setEditorClass(RegexEditor.RegexEditor4TextArea.class)
                .putKeyValue("renderer", RegexCellRencerer.class).putKeyValue(XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate");
        CRPropertyDescriptor regErrorMessage = new CRPropertyDescriptor("regErrorMessage", this.data.getClass()).setI18NName(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Verify_Message")).putKeyValue(XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate");
        CRPropertyDescriptor waterMark = new CRPropertyDescriptor("waterMark", this.data.getClass()).setI18NName(
                com.fr.design.i18n.Toolkit.i18nText("FR-Designer_WaterMark")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
                "Fine-Design_Report_Advanced");
        boolean displayRegField = true;
        displayRegField = isDisplayRegField(displayRegField);
        return displayRegField ? (CRPropertyDescriptor[]) ArrayUtils.addAll(sup, new CRPropertyDescriptor[]{regex, regErrorMessage, waterMark}) :
                (CRPropertyDescriptor[]) ArrayUtils.addAll(sup, new CRPropertyDescriptor[]{regex, waterMark});
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        TextArea area = (TextArea) data;
        if (area.getWidgetValue() != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            BaseUtils.drawStringStyleInRotation(g2d, this.getWidth(), this.getHeight(), area.getWidgetValue()
                    .toString(), Style.getInstance(FRFont.getInstance()).deriveHorizontalAlignment(Constants.LEFT)
                    .deriveVerticalAlignment(SwingConstants.TOP)
                    .deriveTextStyle(Style.TEXTSTYLE_WRAPTEXT), ScreenResolution.getScreenResolution());
        }
    }

    @Override
    protected JComponent initEditor() {
        setBorder(FIELDBORDER);
        return this;
    }

    @Override
    public Dimension initEditorSize() {
        return BIG_PREFERRED_SIZE;
    }

    @Override
    protected String getIconName() {
        return "text_area_16.png";
    }
}
