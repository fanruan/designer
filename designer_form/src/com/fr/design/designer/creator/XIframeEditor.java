/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.ParameterEditor;
import com.fr.design.mainframe.widget.renderer.ParameterRenderer;
import com.fr.form.ui.IframeEditor;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class XIframeEditor extends XWidgetCreator {

    public XIframeEditor(IframeEditor widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("src", this.data.getClass()).setI18NName(Inter.getLocText("Form-Url"))
						.setPropertyChangeListener(new PropertyChangeAdapter() {

							@Override
							public void propertyChange() {
								initFieldText();
							}
						}),
				new CRPropertyDescriptor("overflowx", this.data.getClass()).setI18NName(
						Inter.getLocText("Preference-Horizontal_Scroll_Bar_Visible")).putKeyValue(
						XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
				new CRPropertyDescriptor("overflowy", this.data.getClass()).setI18NName(
						Inter.getLocText("Preference-Vertical_Scroll_Bar_Visible")).putKeyValue(
						XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
				new CRPropertyDescriptor("parameters", this.data.getClass())
						.setI18NName(Inter.getLocText("Parameters")).setEditorClass(ParameterEditor.class)
						.setRendererClass(ParameterRenderer.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
								"Advanced") });
	}

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            UITextField address = new UITextField();
            editor.add(address, BorderLayout.NORTH);
            JPanel contentPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            contentPane.setBackground(Color.white);
            editor.add(contentPane, BorderLayout.CENTER);
        }
        return editor;
    }
    
	private void initFieldText() {
		IframeEditor iframe = (IframeEditor) data;
		String src = iframe.getSrc();
		((UITextField) editor.getComponent(0)).setText(StringUtils.isNotEmpty(src) ? src : "http://ip:port/address?");
	}

	@Override
	protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		initFieldText();
	}

    @Override
    public Dimension initEditorSize() {
        return new Dimension(160, 80);
    }

    /**
     * 该组件是否可以拖入参数面板
     * 这里控制 网页预定义控件在工具栏不显示
     * @return 是则返回true
     */
    public boolean canEnterIntoParaPane(){
        return false;
    }

    @Override
    protected String getIconName() {
        return "iframe_16.png";
    }
}