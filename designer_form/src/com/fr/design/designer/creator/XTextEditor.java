/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.IntrospectionException;

import javax.swing.JComponent;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.widget.editors.RegexEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.RegexCellRencerer;
import com.fr.form.ui.TextEditor;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;

/**
 * @author richer
 * @since 6.5.3
 */
public class XTextEditor extends XWrapperedFieldEditor {

    public XTextEditor(TextEditor widget, Dimension initSize) {
        super(widget, initSize);
    }

    /**
     * 控件的属性列表
     * @return 此控件所用的属性列表
     * @throws IntrospectionException 异常
     */
    @Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(),
				new CRPropertyDescriptor[] {
						new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
								Inter.getLocText(new String[]{"FR-Designer_Widget", "Value"})).setEditorClass(
								WidgetValueEditor.class),
						new CRPropertyDescriptor("regex", this.data.getClass()).setI18NName(
								Inter.getLocText("FR-Designer_Input_Rule")).setEditorClass(RegexEditor.class).putKeyValue(
								"renderer", RegexCellRencerer.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
								"Advanced"),
						new CRPropertyDescriptor("waterMark", this.data.getClass()).setI18NName(
								Inter.getLocText("FR-Designer_WaterMark")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
								"Advanced"), });
	}
    
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		TextEditor area = (TextEditor) data;
		if (area.getWidgetValue() != null) {
			Graphics2D g2d = (Graphics2D) g.create();
			BaseUtils.drawStringStyleInRotation(g2d, this.getWidth(), this.getHeight(), area.getWidgetValue()
					.toString(), Style.getInstance(FRFont.getInstance()).deriveHorizontalAlignment(Constants.LEFT)
					.deriveTextStyle(Style.TEXTSTYLE_SINGLELINE), ScreenResolution.getScreenResolution());
		}
	}

    @Override
	protected JComponent initEditor() {
		setBorder(FIELDBORDER);
		return this;
	}

    @Override
    protected String getIconName() {
        return "text_field_16.png";
    }

}