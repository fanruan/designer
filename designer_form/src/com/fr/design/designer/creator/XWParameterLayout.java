/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRParameterLayoutAdapter;
import com.fr.design.form.util.XCreatorConstants;
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

import java.awt.*;
import java.beans.IntrospectionException;

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
        return  new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
                        .getLocText("FR-Designer_Form-Widget_Name")),
                new CRPropertyDescriptor("background", this.data.getClass()).setEditorClass(BackgroundEditor.class)
                        .setRendererClass(BackgroundRenderer.class).setI18NName(Inter.getLocText("Background"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),        
                new CRPropertyDescriptor("display", this.data.getClass()).setEditorClass(BooleanEditor.class)
                        .setI18NName(Inter.getLocText("ParameterD-Show_Parameter_Window"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                new CRPropertyDescriptor("delayDisplayContent", this.data.getClass()).setEditorClass(BooleanEditor.class)
                        .setI18NName(Inter.getLocText("FR-Designer_DisplayNothingBeforeQuery"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                new CRPropertyDescriptor("position", this.data.getClass()).setEditorClass(WidgetDisplayPosition.class)
                     .setRendererClass(WidgetDisplayPositionRender.class).setI18NName(Inter.getLocText("FR-Designer_WidgetDisplyPosition"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
        };
    }
    
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRParameterLayoutAdapter(this);
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

    public void setPosition(int align){
        this.toData().setPosition(align);
    }

    public void setDisplay(boolean showWindow){
        this.toData().setDisplay(showWindow);
    }

    public void setBackground(Background background){
        this.toData().setBackground(background);
    }
}