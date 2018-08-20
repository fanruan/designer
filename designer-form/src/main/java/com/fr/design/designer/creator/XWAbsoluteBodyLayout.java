package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRAbsoluteBodyLayoutAdapter;
import com.fr.design.designer.properties.mobile.BodyMobilePropertyUI;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.widget.editors.WLayoutBorderStyleEditor;
import com.fr.form.ui.container.WAbsoluteBodyLayout;

import com.fr.stable.core.PropertyChangeAdapter;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.IntrospectionException;

/**
 * Created by zhouping on 2016/10/14.
 * 用作body的绝对布局
 */
public class XWAbsoluteBodyLayout extends XWAbsoluteLayout {
    public XWAbsoluteBodyLayout(WAbsoluteBodyLayout widget, Dimension initSize) {
        super(widget, initSize);
        this.editable = true;
    }

    /**
     * 返回对应的widget容器
     *
     * @return 返回WAbsoluteLayout
     */
    @Override
    public WAbsoluteBodyLayout toData() {
        return (WAbsoluteBodyLayout)data;
    }

    @Override
    public LayoutAdapter getLayoutAdapter() {
        return new FRAbsoluteBodyLayoutAdapter(this);
    }



    /**
     * 假如是body的话，始终要能编辑，不会出现蒙层
     *
     * @param isEditable 可否编辑
     */
    @Override
    public void setEditable(boolean isEditable) {
        super.setEditable(true);
    }

    /**
     * 该组件是否可以拖拽(表单中绝对布局不可以拖拽)
     *
     * @return 是则返回true
     */
    @Override
    public boolean isSupportDrag() {
        return false;
    }

    /**
     * 得到属性名
     *
     * @return 属性名
     * @throws java.beans.IntrospectionException
     */
    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return  new CRPropertyDescriptor[] {
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Toolkit.i18nText("Fine-Design_Form-Widget_Name")),
                new CRPropertyDescriptor("borderStyle", this.data.getClass()).setEditorClass(
                        WLayoutBorderStyleEditor.class).setI18NName(
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Style")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
                        .setPropertyChangeListener(new PropertyChangeAdapter() {

                            @Override
                            public void propertyChange() {
                                initStyle();
                            }
                        })
        };
    }

    /**
     * 获取其在控件树上可见父层
     * @return 组件
     */
    @Override
    public Component getParentShow(){
        //绝对布局作为body的时候不显示自适应布局父层
        if ((this.getParent() != null)) {
            return ((XCreator) this.getParent()).getParentShow();
        }
        return super.getParentShow();
    }



    @Override
    public WidgetPropertyUIProvider[] getWidgetPropertyUIProviders() {
        return new WidgetPropertyUIProvider[]{ new BodyMobilePropertyUI(this)};
    }

    @Override
    protected void initStyle() {
        initBorderStyle();
    }

    /**
     * data属性改变触发其他操作
     *
     */
    public void firePropertyChange(){
        initStyle();
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    /**
     * 是否支持共享-body不支持共享
     * @return
     */
    public boolean isSupportShared() {
        return false;
    }
}
