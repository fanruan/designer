/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.IntrospectionException;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.design.mainframe.widget.editors.RegexEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.RegexCellRencerer;
import com.fr.form.ui.TextArea;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;

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
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(),
				new CRPropertyDescriptor[] {
						new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
								Inter.getLocText(new String[]{"Widget", "Value"})).setEditorClass(
								WidgetValueEditor.class),
						new CRPropertyDescriptor("regex", this.data.getClass()).setI18NName(
								Inter.getLocText("Input_Rule")).setEditorClass(RegexEditor.RegexEditor4TextArea.class)
								.putKeyValue("renderer", RegexCellRencerer.class).putKeyValue(
										XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
						new CRPropertyDescriptor("waterMark", this.data.getClass()).setI18NName(
								Inter.getLocText("WaterMark")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
								"Advanced"), });
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