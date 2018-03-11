/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRParameterLayoutAdapter;
import com.fr.design.designer.properties.mobile.ParaMobilePropertyUI;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.design.mainframe.widget.editors.BackgroundEditor;
import com.fr.design.mainframe.widget.editors.BooleanEditor;
import com.fr.design.mainframe.widget.editors.WidgetDisplayPosition;
import com.fr.design.mainframe.widget.renderer.BackgroundRenderer;
import com.fr.design.mainframe.widget.renderer.WidgetDisplayPositionRender;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.design.fun.ParameterWindowEditorProcessor;
import com.fr.stable.ArrayUtils;

import java.awt.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

/**
 * 表单参数界面container
 */
public class XWParameterLayout extends XWAbsoluteLayout {
	
	public XWParameterLayout() {
		this(new WParameterLayout(), new Dimension());
	}
	
	public XWParameterLayout(WParameterLayout widget) {
		this(widget,new Dimension());
	}

	public XWParameterLayout(WParameterLayout widget, Dimension initSize) {
		super(widget, initSize);
	}

    /**
     * 初始化尺寸
     * @return    尺寸
     */
    public Dimension initEditorSize() {
        return new Dimension(WFitLayout.DEFAULT_WIDTH, WBorderLayout.DEFAULT_SIZE);
    }

    /**
     * 参数面板属性表
     * @return 属性
     * @throws java.beans.IntrospectionException
     */
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        CRPropertyDescriptor[] propertyTableEditor = new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
                        .getLocText("FR-Designer_Form-Widget_Name")),
                new CRPropertyDescriptor("background", this.data.getClass()).setEditorClass(BackgroundEditor.class)
                        .setRendererClass(BackgroundRenderer.class).setI18NName(Inter.getLocText("Background"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                new CRPropertyDescriptor("delayDisplayContent", this.data.getClass()).setEditorClass(BooleanEditor.class)
                        .setI18NName(Inter.getLocText("FR-Designer_DisplayNothingBeforeQuery"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                new CRPropertyDescriptor("position", this.data.getClass()).setEditorClass(WidgetDisplayPosition.class)
                        .setRendererClass(WidgetDisplayPositionRender.class).setI18NName(Inter.getLocText("FR-Designer_WidgetDisplyPosition"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                new CRPropertyDescriptor("useParamsTemplate", this.data.getClass()).setEditorClass(BooleanEditor.class)
                        .setI18NName(Inter.getLocText("FR-Designer_Use_Params_Template"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
        };

        return ArrayUtils.addAll(propertyTableEditor, getExtraTableEditor());
    }
    
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRParameterLayoutAdapter(this);
	}

    /**
     * 获取插件给该控件提供的额外属性表
     * TODO 需要抽成额外的接口，因为{@link XCreator#supportedDescriptor()} 提供的属性里面的默认属性面板都是额外自己画的，因此插件提供额外属性应该有一个方法去获取；
     * TODO 此外，需要将基本高级等分开，在控件的对应属性（基本，高级还有一些其他分类）中进行过滤生成对应的面板。
     * @return 插件给该控件提供的额外属性表
     */
    public CRPropertyDescriptor[] getExtraTableEditor(){
        ParameterWindowEditorProcessor processor = ExtraDesignClassManager.getInstance().getSingle(ParameterWindowEditorProcessor.MARK_STRING);
        if (processor == null) {
            return  new CRPropertyDescriptor[0];
        }
        return processor.createPropertyDescriptor(this.data.getClass());
    }

    /**
     * 该组件是否可以拖入参数面板
     * @return 是则返回true
     */
    public boolean canEnterIntoParaPane(){
        return false;
    }

    /**
     * 该组件是否可以拖拽(表单中参数面板和自适应布局不可以拖拽)
     * @return 是则返回true
     */
    public boolean isSupportDrag(){
        return false;
    }


    /**
     *  返回对应的widget容器
     * @return   对应容器
     */
    public WParameterLayout toData() {
        return (WParameterLayout) data;
    }

    /**
     * 控件默认名称
     * @return   名称
     */
    public String createDefaultName() {
        return "para";
    }

    /**
     * 是否延迟展示报表内容，也就是说是否要等点击了查询之后才执行报表
     * @return 如果是true，则表示点击之后才开始计算，false则表示会根据参数默认值直接计算报表并展现
     */
    public boolean isDelayDisplayContent() {
        return toData().isDelayDisplayContent();
    }

    /**
     * 是否启用参数模板
     * @return 显示参数模板界面则返回true，否则返回false
     */
    public boolean isUseParamsTemplate() {
        return toData().isUseParamsTemplate();
    }

    /**
     * 是否显示参数界面
     * @return 显示参数界面则返回true，否则返回false
     */
    public boolean isDisplay() {
        return toData().isDisplay();
    }

    /**
     * 获取参数界面的宽度
     * @return 宽度
     */
    public int getDesignWidth() {
        return toData().getDesignWidth();
    }

    /**
     * 获取参数面板的对齐方式
     * @return 左中右三种对齐方式
     */
    public int getPosition() {
        return toData().getPosition();
    }

    public void setDelayDisplayContent(boolean delayPlaying){
        this.toData().setDelayDisplayContent(delayPlaying);
    }

    public void setUseParamsTemplate(boolean isUse) {
        this.toData().setUseParamsTemplate(isUse);
    }

    public void setPosition(int align){
        this.toData().setPosition(align);
    }

    public void setDisplay(boolean showWindow){
        this.toData().setDisplay(showWindow);
    }

    public void setBackground(Background background){
        this.toData().setBackground(background);
    }

    @Override
    public void paint(Graphics g) {
        //参数面板特殊处理，不出现编辑层
        setEditable(true);
        super.paint(g);
    }
    @Override
    public XLayoutContainer getTopLayout() {
        return this;
    }

    /**
     * 新增删除拉伸后更新每个组件的BoundsWidget
     *
     * @param xCreator
     */
    @Override
    public void updateBoundsWidget(XCreator xCreator) {
    }

    /**
     * 新增删除拉伸后每个组件的BoundsWidget
     */
    @Override
    public void updateBoundsWidget() {
    }

    @Override
    public WidgetPropertyUIProvider[] getWidgetPropertyUIProviders() {
        return new WidgetPropertyUIProvider[]{ new ParaMobilePropertyUI(this)};
    }

    @Override
    protected String getIconName() {
        return "layout_absolute.png";
    }

    /**
     * data属性改变触发其他操作
     *
     */
    public void firePropertyChange(){

    }

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public boolean supportInnerOrderChangeActions() {
        return false;
    }

}