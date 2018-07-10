/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;

import com.fr.design.mainframe.widget.editors.DictionaryEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.ListEditor;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class XListEditor extends XFieldEditor {

    public XListEditor(ListEditor widget, Dimension initSize) {
        super(widget, initSize);
    }

     @Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(),
				new CRPropertyDescriptor[] {
						new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
								Inter.getLocText(new String[]{"Widget", "Value"})).setEditorClass(
								WidgetValueEditor.class),
						new CRPropertyDescriptor("dictionary", this.data.getClass()).setI18NName(
								Inter.getLocText("DS-Dictionary")).setEditorClass(DictionaryEditor.class)
								.setRendererClass(DictionaryRenderer.class),
						new CRPropertyDescriptor("needHead", this.data.getClass()).setI18NName(
								Inter.getLocText("List-Need_Head")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
								"Advanced") });
	}

    @Override
    protected void initXCreatorProperties() {
        super.initXCreatorProperties();
        JList jList = (JList)editor;
        ListEditor l = (ListEditor)data;
        jList.setSelectedIndex(0);
        jList.setSelectionBackground(l.getSelectionBackground());
        jList.setSelectionForeground(l.getSelectionForeground());
    }

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            DefaultListModel model = new DefaultListModel();
            model.addElement("Item 1");
            model.addElement("item 2");
            model.addElement("item 3");
            model.addElement("item 4");
            editor = new JList(model);
            editor.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        }
        return editor;
    }

    @Override
    public Dimension initEditorSize() {
        return new Dimension(120, 80);
    }

    /**
     * 该组件是否可以拖入参数面板
     * 这里控制 列表预定义控件在工具栏不显示
     * @return 是则返回true
     */
    public boolean canEnterIntoParaPane(){
        return false;
    }

    @Override
    protected String getIconName() {
        return "list_16.png";
    }
}