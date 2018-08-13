/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.design.mainframe.widget.editors.SpinnerMaxNumberEditor;
import com.fr.design.mainframe.widget.editors.SpinnerMinNumberEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.form.ui.NumberEditor;
import com.fr.general.FRFont;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.IntrospectionException;

/**
 * @author richer
 * @since 6.5.3
 */
public class XNumberEditor extends XWrapperedFieldEditor {

    public XNumberEditor(NumberEditor widget, Dimension initSize) {
        super(widget, initSize);
    }

    /**
     * 控件的属性列表
     * @return 此控件所用的属性列表
     * @throws IntrospectionException 异常
     */
    @Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		CRPropertyDescriptor[] sup =(CRPropertyDescriptor[]) ArrayUtils.addAll(
				new CRPropertyDescriptor[] {
						new CRPropertyDescriptor("widgetValue", this.data.getClass())
						.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Value"))
						.setEditorClass(WidgetValueEditor.class)
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")}
				,super.supportedDescriptor());
		CRPropertyDescriptor allowDecimal = new CRPropertyDescriptor("allowDecimals", this.data.getClass())
				.setEditorClass(InChangeBooleanEditor.class)
				.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Allow_Decimals"))
				.putKeyValue(XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate");
		CRPropertyDescriptor decimalLength = new CRPropertyDescriptor("maxDecimalLength", this.data.getClass())
				.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Decimal_Digits"))
				.putKeyValue(XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate");
		sup = (CRPropertyDescriptor[]) ArrayUtils.addAll(sup, ((NumberEditor)this.data).isAllowDecimals() ?
				new CRPropertyDescriptor[] {allowDecimal, decimalLength} : new CRPropertyDescriptor[] {allowDecimal});
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(sup,
				new CRPropertyDescriptor[] {
						new CRPropertyDescriptor("allowNegative", this.data.getClass())
								.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Allow_Negative"))
								.setEditorClass(InChangeBooleanEditor.class)
								.putKeyValue(XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate"),
						new CRPropertyDescriptor("minValue", this.data.getClass())
								.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Min_Value"))
								.setEditorClass(SpinnerMinNumberEditor.class)
								.putKeyValue(XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate"),
						new CRPropertyDescriptor("maxValue", this.data.getClass())
								.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Max_Value"))
								.setEditorClass(SpinnerMaxNumberEditor.class)
								.putKeyValue(XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate"),
						new CRPropertyDescriptor("waterMark", this.data.getClass())
								.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WaterMark"))
								.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
						new CRPropertyDescriptor("regErrorMessage", this.data.getClass())
								.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Verify_Message"))
								.putKeyValue(XCreatorConstants.PROPERTY_VALIDATE, "FR-Designer_Validate")
				});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		NumberEditor editor = (NumberEditor) data;
		if (editor.getWidgetValue() != null) {
			Graphics2D g2d = (Graphics2D) g.create();
			BaseUtils.drawStringStyleInRotation(g2d, this.getWidth(), this.getHeight(), editor.getWidgetValue()
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
        return "number_field_16.png";
    }
	
}
