/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.beans.IntrospectionException;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.widget.editors.FontEditor;
import com.fr.design.mainframe.widget.editors.ItemCellEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.FontCellRenderer;
import com.fr.design.mainframe.widget.renderer.LabelHorizontalAlignmentRenderer;
import com.fr.form.ui.Label;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;

/**
 * @author richer
 * @since 6.5.3
 */
public class XLabel extends XWidgetCreator {
	
	private int cornerSize = 15;

	public XLabel(Label widget, Dimension initSize) {
		super(widget, initSize);
	}

	/**
	 * 生成creator对应的控件widget
	 * @return 控件widget
	 */
	public Label toData() {
		return (Label) data;
	}
	
	/**
	 * 返回组件属性值
	 * @return 返回组件属性值
	 * @throws IntrospectionException 异常
	 */
	@Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(),
				new CRPropertyDescriptor[] {
						new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
								Inter.getLocText(new String[]{"FR-Designer_Widget", "Value"})).setEditorClass(
								WidgetValueEditor.class),
						new CRPropertyDescriptor("wrap", this.data.getClass()).setI18NName(
								Inter.getLocText("FR-Designer_StyleAlignment-Wrap_Text")).putKeyValue(
								XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
						new CRPropertyDescriptor("verticalCenter", this.data.getClass()).setI18NName(
								Inter.getLocText("FR-Designer_PageSetup-Vertically")).putKeyValue(
								XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
						new CRPropertyDescriptor("textalign", this.data.getClass()).setI18NName(
								Inter.getLocText("FR-Designer_Alignment-Style")).setEditorClass(ItemCellEditor.class)
								.setRendererClass(LabelHorizontalAlignmentRenderer.class).putKeyValue(
										XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
						new CRPropertyDescriptor("font", this.data.getClass()).setI18NName(Inter.getLocText("FR-Designer_Font"))
								.setEditorClass(FontEditor.class).setRendererClass(FontCellRenderer.class).putKeyValue(
										XCreatorConstants.PROPERTY_CATEGORY, "Advanced") });
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Label label = (Label) data;
		Dimension size = this.getSize();
		//先画背景，再画标题
		if (toData().getBackground() != null) {
			toData().getBackground().paint(g,new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight()));
		}
		if (label.getWidgetValue() != null) {
			Graphics2D g2d = (Graphics2D) g.create();
			BaseUtils.drawStringStyleInRotation(g2d, this.getWidth(), this.getHeight(), label.getWidgetValue()
					.toString(), Style.getInstance(label.getFont()).deriveHorizontalAlignment(label.getTextalign())
					.deriveVerticalAlignment(label.isVerticalCenter() ? SwingConstants.CENTER : SwingConstants.TOP)
					.deriveTextStyle(label.isWrap() ? Style.TEXTSTYLE_WRAPTEXT : Style.TEXTSTYLE_SINGLELINE),
					ScreenResolution.getScreenResolution());
		}
	}

	@Override
	protected JComponent initEditor() {
		if (editor == null) {
			editor = new UILabel();
		}
		return editor;
	}
	
	@Override
	protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		 if (toData().getBorder() != Constants.LINE_NONE) {
	            this.setBorder(new UIRoundedBorder(toData().getBorder(), toData().getColor(), toData().isCorner() ? cornerSize : 0));
	        } else {
	            this.setBorder(DEFALUTBORDER);
	        }
	}
	
	@Override
	protected String getIconName() {
		return "label_16.png";
	}
	
}