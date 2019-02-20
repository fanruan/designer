package com.fr.design.designer.creator;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xtable.TableUtils;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.editors.ExtendedPropertyEditor;
import com.fr.design.mainframe.widget.editors.StringEditor;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Component;
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
    private boolean isPopulate = true;
    private FormDesigner designer;

    public CRPropertyDescriptorPane(CRPropertyDescriptor crPropertyDescriptor, XCreator xCreator, FormDesigner designer) {
        this.crPropertyDescriptor = crPropertyDescriptor;
        this.xCreator = xCreator;
        this.designer = designer;
    }

    public Component[] createTableLayoutComponent() {
        Component component = initEditorComponent(crPropertyDescriptor, xCreator);
        if (component instanceof UICheckBox) {
            ((UICheckBox) component).setText(crPropertyDescriptor.getDisplayName());
            return new Component[]{component, null};
        }
        if (crPropertyDescriptor.isSubLevel()) {
            JPanel subPanel = TableLayoutHelper.createGapTableLayoutPane(
                    new Component[][]{new Component[]{new UILabel(crPropertyDescriptor.getDisplayName()), component}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
            subPanel.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L5, 0, 0));
            return new Component[]{subPanel, null};
        }
        return new Component[]{new UILabel(crPropertyDescriptor.getDisplayName()), component};

    }

    private Component initEditorComponent(final CRPropertyDescriptor crPropertyDescriptor, final XCreator xCreator) {
        Component component = null;
        try {
            // 如果已有的编辑器就生成对应的component
            Class<?> editorClass = crPropertyDescriptor.getPropertyEditorClass();
            ExtendedPropertyEditor editor = null;
            if (editorClass != null) {
                editor = (ExtendedPropertyEditor) crPropertyDescriptor.createPropertyEditor(xCreator.toData());
                if (editor == null) {
                    Class propType = crPropertyDescriptor.getPropertyType();
                    editor = TableUtils.getPropertyEditorClass(propType).newInstance();
                }

            } else {
                Class propType = crPropertyDescriptor.getPropertyType();
                Class<? extends ExtendedPropertyEditor> defaultEditorClass = TableUtils.getPropertyEditorClass(propType);
                if (defaultEditorClass == null) {
                    defaultEditorClass = StringEditor.class;
                }
                editor = defaultEditorClass.newInstance();
            }
            propertyEditor = editor;
            component = propertyEditor.getCustomEditor();
            final ExtendedPropertyEditor extendEditor = editor;

            extendEditor.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (isPopulate) {
                        return;
                    }
                    if (ComparatorUtils.equals(extendEditor.getValue(), getValue(xCreator.toData()))) {
                        return;
                    }
                    update(xCreator.toData());
                    if (extendEditor.refreshInTime()) {
                        WidgetPropertyPane.getInstance().refreshDockingView();
                    }
                    if (designer == null) {
                        return;
                    }
                    if ("widgetName".equals(crPropertyDescriptor.getName())) {
                        designer.getEditListenerTable().fireCreatorModified(xCreator, DesignerEvent.CREATOR_RENAMED);
                    } else {
                        designer.fireTargetModified();
                    }

                }
            });
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return component;
    }
    
    public void populate(Widget widget) {
        try {
            isPopulate = true;
            Object value = getValue(widget);
            propertyEditor.setValue(value);
            isPopulate = false;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }

    }

    public Object getValue(Widget widget) {
        try {
            Method m = crPropertyDescriptor.getReadMethod();
            Object value = m.invoke(widget);
            return value;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return null;
        }
    }

    public void update(Widget widget) {
        try {
            Object value = propertyEditor.getValue();
            Method m = crPropertyDescriptor.getWriteMethod();
            m.invoke(widget, value);

            crPropertyDescriptor.firePropertyChanged();
        } catch (Exception e) {

        }
    }

}
