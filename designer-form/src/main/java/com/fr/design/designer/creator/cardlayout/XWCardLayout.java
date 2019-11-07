/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator.cardlayout;

import com.fr.base.background.ColorBackground;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRCardLayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.form.layout.FRCardLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.editors.BooleanEditor;
import com.fr.design.mainframe.widget.editors.CardTagWLayoutBorderStyleEditor;
import com.fr.design.mainframe.widget.editors.DoubleEditor;
import com.fr.form.ui.CardAddButton;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.act.BorderPacker;
import com.fr.general.ComparatorUtils;
import com.fr.general.act.TitlePacker;
import com.fr.general.cardtag.DefaultTemplateStyle;
import com.fr.general.cardtag.TemplateStyle;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.beans.IntrospectionException;
import java.util.List;

/**
 * @author richer
 * @since 6.5.3
 */
public class XWCardLayout extends XLayoutContainer {

    private CardLayout cardLayout;
    private boolean initFlag = true;
    private static final int NORTH = 0;
    private FormDesigner designer;

    private static final int LAYOUT_INDEX = 0;

    public static final String DEFAULT_NAME = "cardlayout";


    //默认蓝色标题背景
    private static final Color TITLE_COLOR = new Color(51, 132, 240);

    public XWCardLayout(WCardLayout widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    protected String getIconName() {
        return "card_layout_16.png";
    }

    /**
     * 获取默认名字
     *
     * @return 默认名
     * @date 2014-11-25-下午6:22:40
     */
    @Override
    public String createDefaultName() {
        return DEFAULT_NAME;
    }

    /**
     * 获取当前封装的布局对象
     *
     * @return 布局
     * @date 2014-11-25-下午6:22:17
     */
    @Override
    public WCardLayout toData() {
        return (WCardLayout) data;
    }

    /**
     * 初始化时默认的组件大小
     *
     * @return 默认Dimension
     */
    @Override
    public Dimension initEditorSize() {
        return new Dimension(500, 300);
    }

    /**
     * 重置组件的名称
     * @param name 名称
     */
    public void resetCreatorName(String name) {
        super.resetCreatorName(name);
        changeRelateSwitchCardName(name);
    }


    @Override
    protected void initLayoutManager() {
        cardLayout = new FRCardLayout(toData().getHgap(), toData().getVgap());
        this.setLayout(cardLayout);
    }

    /**
     * 将WLayout转换为XLayoutContainer
     *
     * @date 2014-11-25-下午6:21:48
     */

    @Override
    protected void addWidgetToSwingComponent(WLayout layout) {
        for (int i = 0; i < layout.getWidgetCount(); i++) {
            Widget w = layout.getWidget(i);
            XWidgetCreator creator = (XWidgetCreator) XCreatorUtils.createXCreator(layout.getWidget(i));
            this.add(creator, w.getWidgetName(), i);
            creator.setBackupParent(this);
        }
    }

    /**
     * 展示当前选中的card
     *
     * @date 2014-11-25-下午6:21:23
     */
    public void showCard() {
        WCardLayout layout = this.toData();
        if (layout.getWidgetCount() > 0) {
            cardLayout.show(this, layout.getShowIndex2Name());
        }
    }

    /**
     * 获取当前XCreator的一个封装父容器
     *
     * @param widgetName 当前组件名
     * @return 封装的父容器
     * @date 2014-11-25-下午4:47:23
     */
    @Override
    protected XLayoutContainer getCreatorWrapper(String widgetName) {
        initStyle();
        Dimension dimension = new Dimension();
        //主结构是一个borderlayout, 标签部分是north, card部分为center
        WCardMainBorderLayout border = new WCardMainBorderLayout();
        XWCardMainBorderLayout xMainBorder = new XWCardMainBorderLayout(border, dimension);
        //将子WCardBorder的style设置到父容器上
        BorderPacker style = (this.toData()).getBorderStyle();
        border.setBorderStyle(style);
        this.setBackupParent(xMainBorder);
        XWCardTitleLayout titlePart = this.initTitlePart(widgetName, xMainBorder);
        xMainBorder.addTitlePart(titlePart, WBorderLayout.NORTH);

        return xMainBorder;
    }

    //标题栏
    private XWCardTitleLayout initTitlePart(String widgetName, XWCardMainBorderLayout xMainBorder) {
        Dimension dimension = new Dimension();
        //标题部分本身也是一个borderlayout布局, tag部分放center, 添加按钮放east
        WCardTitleLayout titleLayout = new WCardTitleLayout(this.toData().getWidgetName());
        XWCardTitleLayout xTitle = new XWCardTitleLayout(titleLayout, dimension);
        xTitle.setBackupParent(xMainBorder);

        //切换card的按钮部分
        XWCardTagLayout tagPart = initTagPart(widgetName, xTitle);
        //添加按钮
        XCardAddButton addBtn = initAddButton(widgetName, xTitle, tagPart);

        //添加顺序很重要，先addBtn再tagPart，后面用到
        xTitle.addNewButton(addBtn);
        xTitle.addTagPart(tagPart);

        return xTitle;
    }

    //添加tab按钮
    private XCardAddButton initAddButton(String widgetName, XWCardTitleLayout xTitle, XWCardTagLayout tagPart) {
        Dimension dimension = new Dimension();
        CardAddButton addButton = new CardAddButton(widgetName);
        XCardAddButton xAddBtn = new XCardAddButton(addButton, dimension, tagPart, this);
        xAddBtn.setBackupParent(xTitle);

        return xAddBtn;
    }

    //所有的tab所在的区域
    private XWCardTagLayout initTagPart(String widgetName, XWCardTitleLayout xTitle) {
        Dimension dimension = new Dimension();
        //放置标题的tab流式布局
        WCardTagLayout tagLayout = new WCardTagLayout("tabpane" + widgetName.replaceAll(createDefaultName(), ""));
        tagLayout.setNewTab(true);
        XWCardTagLayout xTag = new XWCardTagLayout(tagLayout, dimension, this);
        xTag.setBackupParent(xTitle);

        XCardSwitchButton xFirstBtn = initFirstButton(widgetName, xTag);
        xTag.add(xFirstBtn);

        return xTag;
    }

    //第一个tab
    private XCardSwitchButton initFirstButton(String widgetName, XWCardTagLayout xTag) {
        CardSwitchButton firstBtn = new CardSwitchButton(widgetName);
        firstBtn.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Title") + 0);
        xTag.setCurrentCard(firstBtn);
        XCardSwitchButton xFirstBtn = new XCardSwitchButton(firstBtn, new Dimension(CardSwitchButton.DEF_WIDTH, -1), this, xTag);
        xFirstBtn.setBackupParent(xTag);
        return xFirstBtn;
    }

    /**
     * 控件树不显示此组件
     *
     * @param path 控件树list
     */
    @Override
    public void notShowInComponentTree(List<Component> path) {
        path.remove(LAYOUT_INDEX);
    }


    /**
     * 设置父容器的名字
     *
     * @param parentPanel 当前父容器
     * @param widgetName  当前控件名
     * @date 2014-11-27-上午9:47:00
     */
    @Override
    protected void setWrapperName(XLayoutContainer parentPanel, String widgetName) {
        parentPanel.toData().setWidgetName("tablayout" + widgetName.replaceAll(createDefaultName(), ""));
    }

    /**
     * 将当前对象添加到父容器中
     *
     * @param parentPanel 父容器组件
     * @date 2014-11-25-下午4:57:55
     */
    @Override
    protected void addToWrapper(XLayoutContainer parentPanel, int width, int minHeight) {
        parentPanel.add(this, WBorderLayout.CENTER);
    }

    /**
     * 添加组件的监听事件
     *
     * @param e 事件
     * @date 2014-11-25-下午6:20:10
     */
    @Override
    public void componentAdded(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        XWidgetCreator creator = (XWidgetCreator) e.getChild();
        creator.setDirections(null);
        WCardLayout layout = this.toData();
        Widget w = creator.toData();

        for (int i = 0, count = this.getComponentCount(); i < count; i++) {
            if (creator == this.getComponent(i)) {
                layout.addWidget(w, i);
            }
        }
    }

    @Override
    public LayoutAdapter getLayoutAdapter() {
        return new FRCardLayoutAdapter(this);
    }

    /**
     * 是否支持标题样式
     *
     * @return 默认false
     */
    @Override
    public boolean hasTitleStyle() {
        return true;
    }

    /**
     * 得到属性名
     *
     * @return 属性名
     * @throws IntrospectionException
     */
    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        //嵌套的tab组件，内层的不支持轮播属性，屏蔽属性表
        if (!isNested()) {
            CRPropertyDescriptor[] crp = ((WCardLayout) data).isCarousel() ? getisCarousel() : getisnotCarousel();
            return ArrayUtils.addAll(getDefaultDescriptor(), crp);
        } else {
            return getDefaultDescriptor();
        }
    }

    /**
     * 判断当前tab组件是不是嵌套的
     *
     * @return 嵌套与否
     */
    private boolean isNested() {
        XCreator xCreator = (XCreator) this.getBackupParent().getParent();
        while (xCreator != null) {
            if (xCreator.acceptType(XWCardMainBorderLayout.class)) {
                return true;
            }
            xCreator = (XCreator) xCreator.getParent();
        }
        return false;
    }

    public CRPropertyDescriptor[] getisCarousel() throws IntrospectionException {
        return new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("carousel", this.data.getClass())
                        .setEditorClass(BooleanEditor.class)
                        .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Carousel"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Report_Tab_Carousel")
                        .setPropertyChangeListener(new PropertyChangeAdapter() {
                    @Override
                    public void propertyChange() {
                        designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
                        designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
                    }
                }),
                new CRPropertyDescriptor("carouselInterval", this.data.getClass())
                        .setEditorClass(DoubleEditor.class)
                        .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Carousel_Interval"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Report_Tab_Carousel")
        };
    }

    public CRPropertyDescriptor[] getisnotCarousel() throws IntrospectionException {
        return new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("carousel", this.data.getClass())
                        .setEditorClass(BooleanEditor.class)
                        .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Carousel"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Report_Tab_Carousel")
                        .setPropertyChangeListener(new PropertyChangeAdapter() {
                    @Override
                    public void propertyChange() {
                        designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
                        designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
                    }
                })
        };
    }

    public CRPropertyDescriptor[] getDefaultDescriptor() throws IntrospectionException {
        return new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(
                        Toolkit.i18nText("Fine-Design_Form-Widget_Name")).setPropertyChangeListener(new PropertyChangeAdapter() {

                    @Override
                    public void propertyChange() {
                        WCardLayout cardLayout = toData();
                        changeRelateSwitchCardName(cardLayout.getWidgetName());
                    }
                }),
                new CRPropertyDescriptor("borderStyle", this.data.getClass()).setEditorClass(
                        CardTagWLayoutBorderStyleEditor.class).setI18NName(
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Style")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced")
                        .setPropertyChangeListener(new PropertyChangeAdapter() {

                    @Override
                    public void propertyChange() {
                        initStyle();
                    }
                })
        };
    }

    //初始化样式
    @Override
    protected void initStyle() {
        BorderPacker style = toData().getBorderStyle();
        initBorderTitleStyle(style);
        initBorderStyle();
        clearOrShowTitleLayout(ComparatorUtils.equals(style.getType(), LayoutBorderStyle.TITLE));
    }

    private void initBorderTitleStyle(BorderPacker style) {
        //初始化默认标题样式
        if (!initFlag) {
            return;
        }

        style.setType(LayoutBorderStyle.TITLE);
        style.setBorder(Constants.LINE_THIN);
        TitlePacker widgetTitle = style.getTitle();
        widgetTitle.setBackground(ColorBackground.getInstance(TITLE_COLOR));
        initFlag = false;
    }

    //隐藏或显示标题部分
    protected void clearOrShowTitleLayout(boolean isTitleStyle) {
        XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) this.getBackupParent();
        if (mainLayout != null) {
            XWCardTitleLayout titleLayout = (XWCardTitleLayout) mainLayout.getComponent(NORTH);
            if (titleLayout != null) {
                WCardTitleLayout layout = (WCardTitleLayout) titleLayout.toData();
                titleLayout.setVisible(isTitleStyle);
                layout.setVisible(isTitleStyle);
            }
        }
    }

    //修改相关SwitchButton所绑定的cardLayout控件名
    private void changeRelateSwitchCardName(String cardLayoutName) {
        XWCardMainBorderLayout borderLayout = (XWCardMainBorderLayout) this.getBackupParent();
        WCardMainBorderLayout border = borderLayout.toData();
        WCardTitleLayout titleLayout = border.getTitlePart();
        titleLayout.setCardName(cardLayoutName);
        WCardTagLayout tagLayout = titleLayout.getTagPart();
        for (int i = 0, len = tagLayout.getWidgetCount(); i < len; i++) {
            CardSwitchButton button = tagLayout.getSwitchButton(i);
            button.setCardLayoutName(cardLayoutName);
        }
    }

    /**
     * 删除相关组件
     *
     * @param creator  当前组件
     * @param designer 表单设计器
     */
    @Override
    public void deleteRelatedComponent(XCreator creator, FormDesigner designer) {
        XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) creator.getBackupParent();
        SelectionModel selectionModel = designer.getSelectionModel();
        selectionModel.setSelectedCreator(mainLayout);
        selectionModel.deleteSelection();
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
        XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) this.getBackupParent();
        if (mainLayout != null) {
            mainLayout.setBorder(border);
        }
    }

    @Override
    public XLayoutContainer getTopLayout() {
        return this.getBackupParent().getTopLayout();
    }

    @Override
    public boolean supportRenameInWidgetTree() {
        return false;
    }

    /**
     * data属性改变触发其他操作
     */
    @Override
    public void firePropertyChange() {
        initStyle();
    }

    public void resetTabBackground(TemplateStyle templateStyle) {
        for (int i = 0; i < this.getXCreatorCount(); i++) {
            XWTabFitLayout xCreator = (XWTabFitLayout) this.getXCreator(i);
            WTabFitLayout wTabFitLayout = (WTabFitLayout) xCreator.toData();
            boolean defaultStyle = ComparatorUtils.equals(templateStyle.getStyle(), DefaultTemplateStyle.DEFAULT_TEMPLATE_STYLE);
            wTabFitLayout.setInitialBackground(defaultStyle ? null : templateStyle.getTabDefaultBackground());
            wTabFitLayout.setOverBackground(null);
            wTabFitLayout.setClickBackground(null);
            wTabFitLayout.setCustomStyle(!defaultStyle);
            xCreator.checkButonType();
        }
    }

}
