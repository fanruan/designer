package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.properties.EventPropertyTable;
import com.fr.design.designer.properties.WidgetPropertyTable;
import com.fr.design.designer.treeview.ComponentTreeModel;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 控件属性表Docking
 */
public class WidgetPropertyPane extends FormDockView implements BaseWidgetPropertyPane {

    private WidgetPropertyTable propertyTable;
    private EventPropertyTable eventTable;
    private List<AbstractPropertyTable> widgetPropertyTables;
    private FormDesigner designer;
    private ComponentTree componentTree;
    private JPanel wsp;
    private MobileWidgetTable mobileWidgetTable;
    private MobileBodyWidgetTable mobileBodyWidgetTable;
    private UIScrollPane downPanel;
    private JPanel centerPane;
    private CardLayout cardLayout;
    public static final String PARA = "para";
    public static final String BODY = "body";
    public static final int NODE_LENGTH = 2;
    public boolean isrefresh = true;


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

    public class mobileWidgetDesignerAdapter implements DesignerEditListener {

        public mobileWidgetDesignerAdapter() {
        }

        /**
         *  响应界面改变事件
         * @param evt  事件
         */
        public void fireCreatorModified(DesignerEvent evt) {
            if (evt.getCreatorEventID() == DesignerEvent.CREATOR_RESIZED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_EDITED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_ADDED) {
                int value = downPanel.getVerticalScrollBar().getValue();
                if(hasSelectParaPane(getEditingFormDesigner())){
                    cardLayout.show(centerPane,PARA);
                    mobileWidgetTable.refresh();
                } else {
                    cardLayout.show(centerPane,BODY);
                    mobileBodyWidgetTable.refresh();
                }
                //出现滚动条
                downPanel.doLayout();
                //控件列表选中某组件，触发表单中选中控件，选中事件又触发列表刷新，滚动条回到0
                //此处设置滚动条值为刷新前
                downPanel.getVerticalScrollBar().setValue(value);
            }
        }
    }

    @Override
    public String getViewTitle() {
        return Inter.getLocText("Form-Widget_Property_Table");
    }

    @Override
    public Icon getViewIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/attributes.png");
    }

    @Override
    public void refreshDockingView() {

        designer = this.getEditingFormDesigner();
        removeAll();
        if (designer == null) {
            clearDockingView();
            return;
        }

        componentTree = new ComponentTree(designer);
        widgetPropertyTables = new ArrayList<AbstractPropertyTable>();
        propertyTable = new WidgetPropertyTable(designer);
        designer.addDesignerEditListener(new WidgetPropertyDesignerAdapter(propertyTable));
        propertyTable.setBorder(null);
        UIScrollPane psp = new UIScrollPane(propertyTable);
        psp.setBorder(null);
        eventTable = new EventPropertyTable(designer);
        designer.addDesignerEditListener(new EventPropertyDesignerAdapter(eventTable));
        eventTable.setBorder(null);
        UIScrollPane esp = new UIScrollPane(eventTable);
        esp.setBorder(null);

        wsp = FRGUIPaneFactory.createBorderLayout_S_Pane();
        wsp.setBorder(null);
        mobileWidgetTable = new MobileWidgetTable(designer);
        mobileBodyWidgetTable = new MobileBodyWidgetTable(designer);
        designer.addDesignerEditListener(new mobileWidgetDesignerAdapter());
        centerPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardLayout = (CardLayout) centerPane.getLayout();
        centerPane.add(mobileWidgetTable,PARA);
        centerPane.add(mobileBodyWidgetTable,BODY);
        if(hasSelectParaPane(designer)){
            cardLayout.show(centerPane,PARA);

        } else {
            cardLayout.show(centerPane,BODY);
        }
        downPanel = new UIScrollPane(centerPane);
        downPanel.setBorder(new LineBorder(Color.gray));
        wsp.add(downPanel,BorderLayout.CENTER);

        UITabbedPane tabbedPane = new UITabbedPane();

        tabbedPane.setOpaque(true);
        tabbedPane.setBorder(null);
        tabbedPane.setTabPlacement(SwingConstants.BOTTOM);
        tabbedPane.addTab(Inter.getLocText("Form-Properties"), psp);
        tabbedPane.addTab(Inter.getLocText("Form-Events"), esp);
        tabbedPane.addTab(Inter.getLocText("FR-Widget_Mobile_Terminal"), wsp);

        WidgetPropertyUIProvider[] widgetAttrProviders = getExtraPropertyUIProviders();
        if (widgetAttrProviders.length == 0) {
            UILabel upLabel = new UILabel(Inter.getLocText("FR-Widget_Mobile_Table"),SwingConstants.CENTER);
            upLabel.setBorder(BorderFactory.createEmptyBorder(6,0,6,0));
            wsp.add(upLabel,BorderLayout.NORTH);
        } else {
            for (WidgetPropertyUIProvider widgetAttrProvider : widgetAttrProviders) {
                AbstractPropertyTable propertyTable = widgetAttrProvider.createWidgetAttrTable();
                widgetPropertyTables.add(propertyTable);
                designer.addDesignerEditListener(new WidgetPropertyDesignerAdapter(propertyTable));
                UIScrollPane uiScrollPane = new UIScrollPane(propertyTable);
                uiScrollPane.setBorder(null);
                wsp.add(uiScrollPane);

            }
        }
        add(tabbedPane, BorderLayout.CENTER);
        propertyTable.initPropertyGroups(null);
        eventTable.refresh();
        for (AbstractPropertyTable propertyTable : widgetPropertyTables) {
            propertyTable.initPropertyGroups(designer);
        }
        isrefresh = false;
    }

    //
    /**
     * 选中的组件是否在参数面板里
     * @param designer   设计器
     * @return     是则返回true
     */
    public boolean hasSelectParaPane(FormDesigner designer){
        XCreator xCreator = designer.getSelectionModel().getSelection().getSelectedCreator();
        if(xCreator == null){
            xCreator = designer.getRootComponent();
        }
        XLayoutContainer container = XCreatorUtils.getHotspotContainer(xCreator);
        return xCreator.acceptType(XWParameterLayout.class) || container.acceptType(XWParameterLayout.class);
    }

    /**
     * 获取当前控件扩展的属性tab
     * 来源有两个:
     * 1, 各个控件从各自的Xcreator里扩展;
     * 2, 所有的控件从插件里扩展.
     *
     * @return 扩展的tab
     */
    private WidgetPropertyUIProvider[] getExtraPropertyUIProviders() {
        FormSelection selection = designer.getSelectionModel().getSelection();
        WidgetPropertyUIProvider[] embeddedPropertyUIProviders = null;
        if (selection != null && selection.getSelectedCreator() != null) {
            embeddedPropertyUIProviders = selection.getSelectedCreator().getWidgetPropertyUIProviders();
        }
        Set<WidgetPropertyUIProvider> set = ExtraDesignClassManager.getInstance().getArray(WidgetPropertyUIProvider.XML_TAG);
        WidgetPropertyUIProvider[] widgetAttrProviders = ArrayUtils.addAll(embeddedPropertyUIProviders, set.toArray(new WidgetPropertyUIProvider[set.size()]));
        return widgetAttrProviders;
    }

    public void setEditingFormDesigner(BaseFormDesigner editor) {
        FormDesigner fd = (FormDesigner) editor;
        super.setEditingFormDesigner(fd);
    }

    public void clearDockingView() {
        propertyTable = null;
        eventTable = null;
        if (widgetPropertyTables != null) {
            widgetPropertyTables.clear();
        }
        JScrollPane psp = new JScrollPane();
        psp.setBorder(null);
        this.add(psp, BorderLayout.CENTER);
    }

    public class WidgetPropertyDesignerAdapter implements DesignerEditListener {
        AbstractPropertyTable propertyTable;

        public WidgetPropertyDesignerAdapter(AbstractPropertyTable propertyTable) {
            this.propertyTable = propertyTable;
        }

        @Override
        public void fireCreatorModified(DesignerEvent evt) {
            if (evt.getCreatorEventID() == DesignerEvent.CREATOR_EDITED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_DELETED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED) {
                propertyTable.initPropertyGroups(designer);
            } else if (evt.getCreatorEventID() == DesignerEvent.CREATOR_RESIZED) {
                repaint();
            }
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof WidgetPropertyDesignerAdapter && ((WidgetPropertyDesignerAdapter) o).propertyTable == this.propertyTable;
        }
    }

    public class EventPropertyDesignerAdapter implements DesignerEditListener {
        EventPropertyTable propertyTable;

        public EventPropertyDesignerAdapter(EventPropertyTable eventTable) {
            this.propertyTable = eventTable;
        }

        @Override
        public void fireCreatorModified(DesignerEvent evt) {
            if (evt.getCreatorEventID() == DesignerEvent.CREATOR_EDITED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_EDITED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED) {
                propertyTable.refresh();
            }
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof EventPropertyDesignerAdapter;
        }
    }

    @Override
    public Location preferredLocation() {
        return Location.WEST_BELOW;
    }
}