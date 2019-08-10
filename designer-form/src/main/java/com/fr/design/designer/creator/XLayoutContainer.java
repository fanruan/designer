/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.form.layout.FRLayoutManager;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.PaddingMarginEditor;
import com.fr.design.mainframe.widget.editors.WLayoutBorderStyleEditor;
import com.fr.design.parameter.ParameterBridge;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WLayout;
import com.fr.general.Background;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.IntrospectionException;
import java.util.List;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class XLayoutContainer extends XBorderStyleWidgetCreator implements ContainerListener, ParameterBridge {

    // 布局内部组件默认最小宽度36，最小高度21
    public static int MIN_WIDTH = 36;
    public static int MIN_HEIGHT = 21;

    protected static final Dimension LARGEPREFERREDSIZE = new Dimension(200, 200);
    protected boolean isRefreshing;
    protected int default_Length = 5; // 取指定点坐在的组件，默认为5保证取四侧相邻的组件时x、y在组件内非边框上

    /**
     * 布局是否可编辑，不可则显示编辑层
     */
    protected boolean editable = false;
    //鼠标移动到布局画出编辑层
    protected boolean isMouseEnter = false;

    public void setMouseEnter(boolean mouseEnter) {
        isMouseEnter = mouseEnter;
    }

    public XLayoutContainer(WLayout widget, Dimension initSize) {
        super(widget, initSize);
        this.addContainerListener(this);
    }

    /**
     * 得到属性名
     *
     * @return 属性名
     * @throws IntrospectionException
     */
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Toolkit.i18nText("Fine-Design_Form-Widget_Name")),
                new CRPropertyDescriptor("borderStyle", this.data.getClass()).setEditorClass(
                        WLayoutBorderStyleEditor.class).setI18NName(
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Style")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced")
                        .setPropertyChangeListener(new PropertyChangeAdapter() {

                    @Override
                    public void propertyChange() {
                        initStyle();
                    }
                }),
                new CRPropertyDescriptor("margin", this.data.getClass()).setEditorClass(PaddingMarginEditor.class)
                        .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Padding_Duplicate"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced"),
        };
    }

    /**
     * 控件名属性
     *
     * @return
     * @throws IntrospectionException
     */
    public CRPropertyDescriptor createWidgetNameDescriptor() throws IntrospectionException {
        return new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Toolkit.i18nText("Fine-Design_Form-Widget_Name"));
    }

    /**
     * 边距属性
     *
     * @return
     * @throws IntrospectionException
     */
    public CRPropertyDescriptor createMarginDescriptor() throws IntrospectionException {
        return new CRPropertyDescriptor("margin", this.data.getClass()).setEditorClass(PaddingMarginEditor.class)
                .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Padding_Duplicate"))
                .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced");
    }

    /**
     * 返回对应的wlayout
     *
     * @return wlayout控件
     */
    public WLayout toData() {
        return (WLayout) data;
    }

    @Override
    protected void initXCreatorProperties() {
        super.initXCreatorProperties();
        initBorderStyle();
        this.initLayoutManager();
        this.convert();
    }

    @Override
    protected JComponent initEditor() {
        return this;
    }

    public void setComponentZOrder(XCreator creator, int targetIndex) {
        super.setComponentZOrder(creator, targetIndex);  // 设计器界面上更改
        // 以下是数据层更改，会反映到 web 端
        WLayout layout = this.toData();
        String widgetName = creator.toData().getWidgetName();
        layout.setWidgetIndex(layout.getWidget(widgetName), targetIndex);
    }

    /**
     * 当前组件zorder位置替换新的控件
     *
     * @param widget     控件
     * @param oldcreator 旧组件
     * @return 组件
     */
    public XCreator replace(Widget widget, XCreator oldcreator) {
        int i = this.getComponentZOrder(oldcreator);
        if (i != -1) {
            this.toData().replace(widget, oldcreator.toData());
            this.convert();
            XCreator creator = (XCreator) this.getComponent(i);
            creator.setSize(oldcreator.getSize());
            return creator;
        }
        return null;
    }

    /**
     * 初始化时默认的组件大小
     *
     * @return 默认Dimension
     */
    public Dimension initEditorSize() {
        return LARGEPREFERREDSIZE;
    }

    protected abstract void initLayoutManager();

    /**
     * 将WLayout转换为XLayoutContainer
     */
    public void convert() {
        isRefreshing = true;
        WLayout layout = this.toData();
        this.removeAll();
        addWidgetToSwingComponent(layout);
        isRefreshing = false;
    }

    protected void addWidgetToSwingComponent(WLayout layout) {
        for (int i = 0; i < layout.getWidgetCount(); i++) {
            Widget wgt = layout.getWidget(i);
            if (wgt != null) {
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
                this.add(comp, i);
            }
        }
    }

    /**
     * 设计界面中有组件添加时，要通知WLayout容器重新paint
     *
     * @param e 待说明
     */
    @Override
    public void componentAdded(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        XWidgetCreator creator = (XWidgetCreator) e.getChild();
        WLayout wlayout = this.toData();
        Widget wgt = creator.toData();
        wlayout.addWidget(wgt);
        this.recalculateChildrenPreferredSize();
    }

    /**
     * 设计界面中有组件添加时，要通知WLayout容器重新paint
     *
     * @param e 待说明
     */
    @Override
    public void componentRemoved(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        WLayout wlayout = this.toData();
        Widget wgt = ((XWidgetCreator) e.getChild()).toData();
        wlayout.removeWidget(wgt);
        this.recalculateChildrenPreferredSize();
    }

    /**
     * 根据widget的属性值来获取
     *
     * @param wgt
     * @return
     */
    protected Dimension calculatePreferredSize(Widget wgt) {
        return new Dimension();
    }

    /**
     * 重新调整子组件的大小
     */
    public void recalculateChildrenPreferredSize() {
        for (int i = 0; i < this.getComponentCount(); i++) {
            XCreator creator = this.getXCreator(i);
            Widget wgt = creator.toData();
            Dimension dim = calculatePreferredSize(wgt);
            creator.setPreferredSize(dim);
            creator.setMaximumSize(dim);
        }
    }

    public int getShowXCreatorCount() {
        return getXCreatorCount();
    }

    public int getXCreatorCount() {
        return getComponentCount();
    }

    public XCreator getXCreator(int i) {
        return (XCreator) getComponent(i);
    }

    /**
     * 该组件是否可以拖入参数面板
     *
     * @return 是则返回true
     */
    public boolean canEnterIntoParaPane() {
        return false;
    }

    /**
     * 是否作为控件树的叶子节点
     *
     * @return 是则返回true
     */
    public boolean isComponentTreeLeaf() {
        return false;
    }

    public List<String> getAllXCreatorNameList(XCreator xCreator, List<String> namelist) {
        for (int i = 0; i < ((XLayoutContainer) xCreator).getXCreatorCount(); i++) {
            XCreator creatorSon = ((XLayoutContainer) xCreator).getXCreator(i);
            creatorSon.getAllXCreatorNameList(creatorSon, namelist);
        }
        return namelist;
    }

    /**
     * 是否有查询按钮
     *
     * @param xCreator 控件或容器
     * @return 有无查询按钮
     */
    public boolean SearchQueryCreators(XCreator xCreator) {
        for (int i = 0; i < ((XLayoutContainer) xCreator).getXCreatorCount(); i++) {
            XCreator creatorSon = ((XLayoutContainer) xCreator).getXCreator(i);
            if (creatorSon.SearchQueryCreators(creatorSon)) {
                return true;
            }
        }
        return false;
    }

    public FRLayoutManager getFRLayout() {
        LayoutManager layout = getLayout();
        if (layout instanceof FRLayoutManager) {
            return (FRLayoutManager) layout;
        }
        FineLoggerFactory.getLogger().error("FRLayoutManager isn't exsit!");
        return null;
    }

    public abstract LayoutAdapter getLayoutAdapter();

    public int getIndexOfChild(Object child) {
        int count = getComponentCount();
        for (int i = 0; i < count; i++) {
            Component comp = getComponent(i);
            if (comp == child) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 主要为自适应用
     * 返回指定point的上方组件
     *
     * @param x x位置
     * @param y y位置
     * @return 指定位置的组件
     */
    public Component getTopComp(int x, int y) {
        return this.getComponentAt(x, y - default_Length);
    }

    /**
     * 主要为自适应用
     * 返回指定point的左方组件
     *
     * @param x x位置
     * @param y y位置
     * @return 指定位置的组件
     */
    public Component getLeftComp(int x, int y) {
        return this.getComponentAt(x - default_Length, y);
    }

    /**
     * 返回指定point的右方组件
     *
     * @param x x位置
     * @param y y位置
     * @param w 宽度
     * @return 指定位置的组件
     */
    public Component getRightComp(int x, int y, int w) {
        return this.getComponentAt(x + w + default_Length, y);
    }

    /**
     * 返回指定point的下方组件
     *
     * @param x x位置
     * @param y y位置
     * @param h 高度
     * @return 指定位置的组件
     */
    public Component getBottomComp(int x, int y, int h) {
        return this.getComponentAt(x, y + h + default_Length);
    }

    /**
     * 返回指定point的上方且是右侧的组件
     *
     * @param x x位置
     * @param y y位置
     * @param w 宽度
     * @return 指定位置的组件
     */
    public Component getRightTopComp(int x, int y, int w) {
        return this.getComponentAt(x + w - default_Length, y - default_Length);
    }

    /**
     * 返回指定point的左方且是下侧的组件
     *
     * @param x x位置
     * @param y y位置
     * @param h 高度
     * @return 指定位置的组件
     */
    public Component getBottomLeftComp(int x, int y, int h) {
        return this.getComponentAt(x - default_Length, y + h - default_Length);
    }

    /**
     * 返回指定point的右方且是下侧的组件
     *
     * @param x x位置
     * @param y y位置
     * @param h 高度
     * @param w 宽度
     * @return 指定位置的组件
     */
    public Component getBottomRightComp(int x, int y, int h, int w) {
        return this.getComponentAt(x + w + default_Length, y + h - default_Length);
    }

    /**
     * 返回指定point的下方且是右侧的组件
     *
     * @param x x位置
     * @param y y位置
     * @param h 高度
     * @param w 宽度
     * @return 指定位置的组件
     */
    public Component getRightBottomComp(int x, int y, int h, int w) {
        return this.getComponentAt(x + w - default_Length, y + h + default_Length);
    }

    /**
     * 是否延迟展示报表内容，也就是说是否要等点击了查询之后才执行报表
     *
     * @return 如果是true，则表示点击之后才开始计算，false则表示会根据参数默认值直接计算报表并展现
     */
    public boolean isDelayDisplayContent() {
        return false;
    }

    /**
     * 是否启用参数模板
     * @return 显示参数模板界面则返回true，否则返回false
     */
    public boolean isUseParamsTemplate() {
        return false;
    }


    /**
     * 是否显示参数界面
     *
     * @return 显示参数界面则返回true，否则返回false
     */
    public boolean isDisplay() {
        return false;
    }

    public Background getDataBackground() {
        return toData().getBackground();
    }

    /**
     * 获取参数界面的宽度
     *
     * @return 宽度
     */
    public int getDesignWidth() {
        return 0;
    }

    /**
     * 获取参数面板的对齐方式
     *
     * @return 左中右三种对齐方式
     */
    public int getPosition() {
        return 0;
    }

    /**
     * 切换到非添加状态
     *
     * @param designer 表单设计器
     */
    public void stopAddingState(FormDesigner designer) {
    }

    /**
     * 寻找最近的为自适应布局的父容器
     *
     * @return 布局容器
     * @date 2014-12-30-下午3:15:28
     */
    public XLayoutContainer findNearestFit() {
        //一层一层网上找, 找到最近的fit那一层就return
        XLayoutContainer parent = this.getBackupParent();
        return parent == null ? null : parent.findNearestFit();
    }

    /**
     * 获取容器所有内部组件横坐标值
     *
     * @return 横坐标数组
     */
    public int[] getHors() {
        return ArrayUtils.EMPTY_INT_ARRAY;
    }

    /**
     * 获取容器所有内部组件纵坐标值
     *
     * @return 纵坐标数组
     */
    public int[] getVeris() {
        return ArrayUtils.EMPTY_INT_ARRAY;
    }

    public void setDelayDisplayContent(boolean delayPlaying) {

    }

    public void setUseParamsTemplate(boolean isUse) {

    }

    public void setPosition(int align) {

    }

    public void setDisplay(boolean showWindow) {

    }

    public void setBackground(Background background) {

    }

    /**
     * 布局是否可编辑，不可则显示编辑蒙层
     *
     * @return 可否编辑
     */
    public boolean isEditable() {
        return this.editable;
    }

    /**
     * 设置布局是否可编辑，不可则显示编辑蒙层
     *
     * @param isEditable 可否编辑
     */
    public void setEditable(boolean isEditable) {
        this.editable = isEditable;
    }

    /**
     * data属性改变触发其他操作
     *
     */
    public void firePropertyChange(){
        initStyle();
    }

    /**
     * 是否支持设置可见
     * return boolean
     */
    public boolean supportSetVisible(){
        return false;
    }

    /**
     * 是否支持设置可用
     * return boolean
     */
    public boolean supportSetEnable(){
        return false;
    }

    /**
     * 内部组件是否支持叠加顺序的调整
     * return boolean
     */
    public boolean supportInnerOrderChangeActions() {
        return false;
    }

    @Override
    public boolean supportMobileStyle() {
        return false;
    }

}
