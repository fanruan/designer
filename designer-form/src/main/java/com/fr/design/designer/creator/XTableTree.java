package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;

import com.fr.design.mainframe.widget.editors.DictionaryEditor;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.TableTree;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

public class XTableTree extends XWidgetCreator {

    public XTableTree(TableTree widget, Dimension initSize) {
        super(widget, initSize);
    }
    
    @Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return (CRPropertyDescriptor[]) ArrayUtils.addAll(
            super.supportedDescriptor(),
            new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("dictionary", this.data.getClass())
                    .setI18NName(Inter.getLocText("DS-Dictionary"))
                    .setEditorClass(DictionaryEditor.class)
                    .setRendererClass(DictionaryRenderer.class),
                new CRPropertyDescriptor("dataUrl", this.data.getClass()),
            });
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
        }
        return editor;
    }

    @Override
    public Dimension initEditorSize() {
        return new Dimension(120, 80);
    }

    @Override
    protected String getIconName() {
        return "list_16.png";
    }

}