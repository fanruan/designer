package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWCardTagLayout;
import com.fr.design.designer.properties.EventPropertyTable;
import com.fr.design.designer.properties.mobile.MobileBookMarkPropertyUI;
import com.fr.design.designer.properties.mobile.MobileStylePropertyUI;
import com.fr.design.form.util.FormDesignerUtils;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.ui.FormWidgetCardPane;
import com.fr.design.widget.ui.designer.mobile.MobileWidgetDefinePane;

import com.fr.stable.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 控件属性表绘制
 * Modified by fanglei
 */
public class WidgetPropertyPane extends FormDockView implements BaseWidgetPropertyPane {

    private static final int PADDING = 10;
    private static final int PADDING_M = 12;
    private FormWidgetCardPane formWidgetCardPane; // 控件的属性表
    private EventPropertyTable eventTable; // 控件的事件表
    private List<AbstractPropertyTable> widgetPropertyTables; // 这个变量应该是保存控件拓展的属性tab
    private List<MobileWidgetDefinePane> mobileExtraPropertyPanes; // 保存9.0设计器下移动端拓展的属性tab，舍弃JTable
    private FormDesigner designer; // 当前designer
    private UIScrollPane psp; // 用来装载属性表table的容器
    private JPanel wsp; // 装载移动端tab的容器，包括移动端属性表和控件拓展的移动端属性表
    private UIHeadGroup tabsHeaderIconPane;
    private XComponent lastAffectedCreator;


    public static WidgetPropertyPane getInstance() {
        if (HOLDER.singleton == null) {
            HOLDER.singleton = new WidgetPropertyPane();
        }
        return HOLDER.singleton;
    }

    public static WidgetPropertyPane getInstance(FormDesigner formEditor) {
        HOLDER.singleton.setEditingFormDesigner(formEditor);
        HOLDER.singleton.refreshDockingView();
        return HOLDER.singleton;
    }

    private static class HOLDER {
        private static WidgetPropertyPane singleton = new WidgetPropertyPane();
    }

    private WidgetPropertyPane() {
        setLayout(FRGUIPaneFactory.createBorderLayout());
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Control_Setting");
    }


    @Override
    public String getViewTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Property_Table");
    }

    @Override
    public Icon getViewIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/attributes.png");
    }

    @Override
    /**
     *  绘制属性表tab
     */
    public void refreshDockingView() {
        designer = this.getEditingFormDesigner();
        removeAll();
        if (designer == null) {
            clearDockingView();
            return;
        }
        widgetPropertyTables = new ArrayList<AbstractPropertyTable>();
        mobileExtraPropertyPanes = new ArrayList<>();

        //依次创建属性表、事件表、移动端表，再将它们整合到TabPane中去
        this.createPropertyTable();
        this.createEventTable();
        this.createMobileWidgetTable();
        this.createTabPane();

        this.initTables();
    }

    /**
     * 初始化属性表，事件表，移动端拓展的属性表
     */
    private void initTables() {
        formWidgetCardPane.populate();
        eventTable.refresh();

        if (mobileExtraPropertyPanes != null) {
            for (MobileWidgetDefinePane extraPane : mobileExtraPropertyPanes) {
                extraPane.initPropertyGroups(designer);
            }
        }
        if (widgetPropertyTables != null) {
            for (AbstractPropertyTable propertyTable : widgetPropertyTables) {
                propertyTable.initPropertyGroups(designer);
            }
        }
    }

    /**
     * 创建属性表table
     */
    private void createPropertyTable() {
        formWidgetCardPane = new FormWidgetCardPane(designer);
        designer.addDesignerEditListener(new WidgetPropertyDesignerAdapter(formWidgetCardPane));
        psp = new UIScrollPane(formWidgetCardPane); // 用来装载属性表table
        psp.setBorder(null);
    }

    /**
     * 创建事件表（事件选项卡不是JTable）
     */
    private void createEventTable() {
        eventTable = new EventPropertyTable(designer);
        designer.addDesignerEditListener(new EventPropertyDesignerAdapter(eventTable));
    }

    /**
     * 创建移动端控件列表
     */
    private void createMobileWidgetTable() {
        //加上表头后，这里不再使用borderLayout布局，而采用BoxLayout布局
        wsp = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        wsp.setBorder(null);
        designer.addDesignerEditListener(new MobileWidgetDesignerAdapter());

        //获取拓展移动端属性tab
        WidgetPropertyUIProvider[] widgetAttrProviders = getExtraPropertyUIProviders();
        addWidgetAttr(widgetAttrProviders);
    }

    /**
     * 将属性表，事件表，移动端控件列表整合到TabPane里面去
     */
    private void createTabPane() {
        initTabPane();
    }

    /**
     * 获取当前控件扩展的属性tab
     * 来源有两个:
     * 1, 各个控件从各自的Xcreator里扩展（例如手机重布局的tab就是从Xcreator中扩展的）;
     * 2, 所有的控件从插件里扩展.
     *
     * @return 扩展的tab
     */
    private WidgetPropertyUIProvider[] getExtraPropertyUIProviders() {
        FormSelection selection = designer.getSelectionModel().getSelection();
        WidgetPropertyUIProvider[] embeddedPropertyUIProviders = null;
        XCreator xCreator = selection.getSelectedCreator();
        if (selection != null && xCreator != null) {
            embeddedPropertyUIProviders = selection.getSelectedCreator().getWidgetPropertyUIProviders();
            if(!designer.getDesignerMode().isFormParameterEditor()) {
                if (!xCreator.acceptType(XWAbsoluteLayout.class, XWFitLayout.class, XWCardTagLayout.class, XWCardMainBorderLayout.class) && FormDesignerUtils.isAppRelayout(designer)) {
                    embeddedPropertyUIProviders = ArrayUtils.insert(0, embeddedPropertyUIProviders, new MobileBookMarkPropertyUI(xCreator));
                }
                if (xCreator.supportMobileStyle()) {
                    embeddedPropertyUIProviders = ArrayUtils.insert(0, embeddedPropertyUIProviders, new MobileStylePropertyUI(xCreator));
                }
            }
        }
        Set<WidgetPropertyUIProvider> set = ExtraDesignClassManager.getInstance().getArray(WidgetPropertyUIProvider.XML_TAG);
        return ArrayUtils.addAll(embeddedPropertyUIProviders, set.toArray(new WidgetPropertyUIProvider[set.size()]));
    }

    /**
     * 判断是将拓展的tab放入属性表还是将原来的tab放入属性表
     *
     * @param widgetAttrProviders 拓展的tab
     */
    private void addWidgetAttr(WidgetPropertyUIProvider[] widgetAttrProviders) {
        if (widgetAttrProviders.length == 0) { // 判断有没有拓展的tab，提示"无可用配置项"
            wsp.add(getUnavailablePane());
        } else {
            for (WidgetPropertyUIProvider widgetAttrProvider : widgetAttrProviders) {
                MobileWidgetDefinePane extraPane = (MobileWidgetDefinePane) widgetAttrProvider.createWidgetAttrPane();
                if (extraPane != null) {
                    extraPane.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING_M));
                    mobileExtraPropertyPanes.add(extraPane);
                    wsp.add(extraPane);
                }
                AbstractPropertyTable propertyTable = widgetAttrProvider.createWidgetAttrTable();
                if (propertyTable != null) {
                    widgetPropertyTables.add(propertyTable);
                    designer.addDesignerEditListener(new WidgetPropertyDesignerAdapter(formWidgetCardPane));

                    UIScrollPane uiScrollPane = new UIScrollPane(propertyTable);
                    wsp.add(uiScrollPane);
                }
            }
        }
    }

    // "无可用配置项"面板
    private JPanel getUnavailablePane() {
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No_Settings_Available"));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
        return panel;
    }

    private void initTabPane() {
        final String[] tabTitles = new String[]{
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Properties"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Event"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget_Mobile_Terminal")
        };
        final CardLayout tabbedPane = new CardLayout();
        final JPanel center = new JPanel(tabbedPane);
        center.add(formWidgetCardPane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Properties"));
        center.add(eventTable, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Event"));
        center.add(wsp, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget_Mobile_Terminal"));
        this.add(center, BorderLayout.CENTER);

        tabsHeaderIconPane = new UIHeadGroup(tabTitles) {
            @Override
            public void tabChanged(int index) {
                //切换的时候再populate
                if (index == 1) {
                    eventTable.populateNameObjects();
                } else if (index == 2) {
                    if (mobileExtraPropertyPanes != null) {
                        for (MobileWidgetDefinePane extraPane : mobileExtraPropertyPanes) {
                            extraPane.populate(designer);
                        }
                    }
                }
                tabbedPane.show(center, tabTitles[index]);
            }
        };
        tabsHeaderIconPane.setNeedLeftRightOutLine(true);
        tabsHeaderIconPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.SHADOW_GREY));
        this.add(tabsHeaderIconPane, BorderLayout.NORTH);
    }


    /**
     * 选中的组件是否在参数面板里
     *
     * @param designer 设计器
     * @return 是则返回true
     */
    public boolean hasSelectParaPane(FormDesigner designer) {
        XCreator xCreator = designer.getSelectionModel().getSelection().getSelectedCreator();
        if (xCreator == null) {
            xCreator = designer.getRootComponent();
        }
        XLayoutContainer container = XCreatorUtils.getHotspotContainer(xCreator);

        boolean xCreatorAccept = xCreator.acceptType(XWParameterLayout.class);
        boolean containerAccept = container != null && container.acceptType(XWParameterLayout.class);

        return xCreatorAccept || containerAccept;
    }

    public void setEditingFormDesigner(BaseFormDesigner editor) {
        FormDesigner fd = (FormDesigner) editor;
        super.setEditingFormDesigner(fd);
    }

    private void clearDockingView() {
        formWidgetCardPane = null;
        eventTable = null;
        if (widgetPropertyTables != null) {
            widgetPropertyTables.clear();
        }
        JScrollPane psp = new JScrollPane();
        psp.setBorder(null);
        this.add(psp, BorderLayout.CENTER);
    }

    /**
     * 属性表监听界面事件(编辑，删除，选中，改变大小)
     */
    private class WidgetPropertyDesignerAdapter implements DesignerEditListener {
        FormWidgetCardPane formWidgetCardPane;


        WidgetPropertyDesignerAdapter(FormWidgetCardPane formWidgetCardPane) {
            this.formWidgetCardPane = formWidgetCardPane;
        }

        @Override
        public void fireCreatorModified(DesignerEvent evt) {
            if (evt.getCreatorEventID() == DesignerEvent.CREATOR_DELETED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_RESIZED) {
                formWidgetCardPane.populate();
            } else if (evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED) {
                // 防止多次触发
                if (lastAffectedCreator != null && lastAffectedCreator == evt.getAffectedCreator()) {
                    return;
                }
                lastAffectedCreator = evt.getAffectedCreator();
                refreshDockingView();
                formWidgetCardPane.populate();
            }
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof WidgetPropertyDesignerAdapter;
        }
    }

    /**
     * 事件表监听界面事件（编辑，选中）
     */
    private class EventPropertyDesignerAdapter implements DesignerEditListener {
        EventPropertyTable propertyTable;

        EventPropertyDesignerAdapter(EventPropertyTable eventTable) {
            this.propertyTable = eventTable;
        }

        @Override
        public void fireCreatorModified(DesignerEvent evt) {
            if (evt.getCreatorEventID() == DesignerEvent.CREATOR_EDITED) {
                propertyTable.refresh();
            } else if (evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED) {
                // 防止多次触发
                if (lastAffectedCreator != null && lastAffectedCreator == evt.getAffectedCreator()) {
                    return;
                }
                lastAffectedCreator = evt.getAffectedCreator();
                propertyTable.refresh();
            }
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof EventPropertyDesignerAdapter;
        }
    }

    /**
     * 移动端属性表监听界面事件（改变大小，编辑，选中，增加控件）
     */
    private class MobileWidgetDesignerAdapter implements DesignerEditListener {

        MobileWidgetDesignerAdapter() {
        }

        /**
         * 响应界面改变事件
         *
         * @param evt 事件
         */
        public void fireCreatorModified(DesignerEvent evt) {
            int[] validEventIds = {DesignerEvent.CREATOR_RESIZED, DesignerEvent.CREATOR_EDITED,
                    DesignerEvent.CREATOR_SELECTED, DesignerEvent.CREATOR_ADDED, DesignerEvent.CREATOR_ORDER_CHANGED};
            boolean isValid = false;
            for (int validEventId : validEventIds) {
                if (evt.getCreatorEventID() == validEventId) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                return;
            }
            //fanglei：下面的注释不要删，只是暂时屏蔽
//                int value = downPanel.getVerticalScrollBar().getValue();
//                if (hasSelectParaPane(getEditingFormDesigner())) {
//                    cardLayout.show(centerPane, PARA);
//                    mobileParaWidgetTable.refreshData();
//                } else {
//                    cardLayout.show(centerPane, BODY);
//                    mobileWidgetTable.refreshData();
//                }
//                //出现滚动条
//                downPanel.doLayout();
//                //控件列表选中某组件，触发表单中选中控件，选中事件又触发列表刷新，滚动条回到0
//                //此处设置滚动条值为刷新前
//                downPanel.getVerticalScrollBar().setValue(value);
            if (mobileExtraPropertyPanes != null) {
                for (MobileWidgetDefinePane extraPane : mobileExtraPropertyPanes) {
                    extraPane.populate(designer);
                }
            }

        }

        @Override
        public boolean equals(Object o) {
            return o instanceof MobileWidgetDesignerAdapter;
        }
    }

    @Override
    public Location preferredLocation() {
        return Location.WEST_BELOW;
    }
}
