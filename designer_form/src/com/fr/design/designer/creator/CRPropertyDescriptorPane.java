package com.fr.design.designer.creator;

import com.fr.base.FRContext;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xtable.TableUtils;
import com.fr.design.mainframe.widget.editors.ExtendedPropertyEditor;
import com.fr.design.mainframe.widget.editors.StringEditor;
import com.fr.form.ui.Widget;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;

/**
 * Created by kerry on 2017/9/6.
 */
public class CRPropertyDescriptorPane {
    private CRPropertyDescriptor crPropertyDescriptor;
    private XCreator xCreator;
    private PropertyEditor propertyEditor;

    public CRPropertyDescriptorPane(CRPropertyDescriptor crPropertyDescriptor, XCreator xCreator) {
        this.crPropertyDescriptor = crPropertyDescriptor;
        this.xCreator = xCreator;
    }

    public Component[] createTableLayoutComponent() {
        return new Component[]{new UILabel(crPropertyDescriptor.getDisplayName()), initEditorComponent(crPropertyDescriptor, xCreator)};
    }

    private Component initEditorComponent(CRPropertyDescriptor crPropertyDescriptor, final XCreator xCreator) {
        Component component = null;
        try {
            // 如果已有的编辑器就生成对应的component
            Class<?> editorClass = crPropertyDescriptor.getPropertyEditorClass();
            if (editorClass != null) {
                propertyEditor = initExtendEditor(crPropertyDescriptor, xCreator);
                component = propertyEditor.getCustomEditor();
            } else {
                Class propType = crPropertyDescriptor.getPropertyType();
                Class<? extends PropertyEditor> defaultEditorClass = TableUtils.getPropertyEditorClass(propType);
                if (defaultEditorClass == null) {
                    defaultEditorClass = StringEditor.class;
                }
                propertyEditor = defaultEditorClass.newInstance();
                component = propertyEditor.getCustomEditor();
            }
            propertyEditor.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    update(xCreator.toData());
                }
            });
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return component;
    }

    private PropertyEditor initExtendEditor(CRPropertyDescriptor crPropertyDescriptor, XCreator xCreator) throws Exception {
        ExtendedPropertyEditor editor = (ExtendedPropertyEditor) crPropertyDescriptor.createPropertyEditor(xCreator.toData());
        if (editor == null) {
            Class propType = crPropertyDescriptor.getPropertyType();
            editor = TableUtils.getPropertyEditorClass(propType).newInstance();
        }

        return editor;
    }

    public void populate(Widget widget) {
        try {
            Method m = crPropertyDescriptor.getReadMethod();
            Object value = m.invoke(widget);
            propertyEditor.setValue(value);
        } catch (Exception e) {

        }

    }

    public void update(Widget widget) {
        try {
            Method m = crPropertyDescriptor.getWriteMethod();
            m.invoke(widget, propertyEditor.getValue());
        } catch (Exception e) {

        }
    }

}
