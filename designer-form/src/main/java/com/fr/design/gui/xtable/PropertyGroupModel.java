package com.fr.design.gui.xtable;

import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.ExtendedPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.report.stable.FormConstants;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PropertyGroupModel extends AbstractPropertyGroupModel {

    private FormDesigner designer;

    public PropertyGroupModel(String name, XCreator creator, CRPropertyDescriptor[] propArray,
                              FormDesigner designer) {
        super(name, creator, propArray);
        this.designer = designer;
    }

    @Override
    public Object getValue(int row, int column) {
        if (column == 0) {
            return properties[row].getDisplayName();
        }
        try {
            Method m = properties[row].getReadMethod();
            return m.invoke(dealCreatorData());
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean setValue(Object value, int row, int column) {
        if (column == 0) {
            return false;
        }

        try {
            Method m = properties[row].getWriteMethod();
            m.invoke(dealCreatorData(), value);
            //属性名称为控件名时，单独处理下
            if(ComparatorUtils.equals(FormConstants.NAME, properties[row].getName())){
                creator.resetCreatorName(value.toString());
            }
            if(ComparatorUtils.equals("visible", properties[row].getName())){
                creator.resetVisible((boolean) value);
            }
            properties[row].firePropertyChanged();
            return true;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 改行是否可编辑
     * @param row 行
     * @return 第row行可编辑返回true，否则返回false
     */
    @Override
    public boolean isEditable(int row) {
        return properties[row].getWriteMethod() != null;
    }

    /**
     * 该属性所属的分类,普通属性分为基本属性和其它,事件属性根据事件名称不同进行分类
     * @return 返回属性名称
     */
    @Override
    public String getGroupName() {
        return com.fr.design.i18n.Toolkit.i18nText(groupName);
    }

    /**
     * 比较
     * @param gm 属性类
     * @return 返回比较结果
     */
    @Override
    public int compareTo(AbstractPropertyGroupModel gm) {
        int firstIndex = PROPERTIES.indexOf(groupName);
        int lastIndex = PROPERTIES.indexOf(gm.getGroupName());
        if (firstIndex < lastIndex) {
            return -1;
        } else if (firstIndex == lastIndex) {
            return 0;
        } else {
            return 1;
        }
    }
    private static ArrayList<String> PROPERTIES = new ArrayList<String>();

    static {
        PROPERTIES.add("Properties");
        PROPERTIES.add("Others");
    }

    /**
     * 控件属性赋值和取值时，针对scale和title做下处理
     * @return
     */
    private Object dealCreatorData() {
        return creator.getPropertyDescriptorCreator().toData();
    }

    @Override
    protected void initEditor(final int row) throws Exception {
        ExtendedPropertyEditor editor = (ExtendedPropertyEditor) properties[row].createPropertyEditor(dealCreatorData());
        if (editor == null) {
            Class propType = properties[row].getPropertyType();
            editor = TableUtils.getPropertyEditorClass(propType).newInstance();
        }
        if (editor != null) {
            final ExtendedPropertyEditor extendEditor = editor;
            editors[row] = new PropertyCellEditor(editor);
            extendEditor.addPropertyChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if(ComparatorUtils.equals(extendEditor.getValue(),getValue(row,1))) {
                        return;
                    }
                    if (extendEditor.refreshInTime()) {
                        editors[row].stopCellEditing();
                    } else {
                        setValue(extendEditor.getValue(), row, 1);

                        if (designer == null) {
                            return;
                        }
                        if ("widgetName".equals(properties[row].getName())) {
                            designer.getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_RENAMED);
                        } else {
                            designer.fireTargetModified();
                        }
                        designer.refreshDesignerUI();
                    }
                }
            });
        }
    }
}