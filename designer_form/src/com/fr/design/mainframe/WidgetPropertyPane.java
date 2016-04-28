package com.fr.design.mainframe;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.WidgetAttrProvider;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.general.Inter;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.properties.EventPropertyTable;
import com.fr.design.designer.properties.WidgetPropertyTable;

/**
 * 控件属性表Docking
 */
public class WidgetPropertyPane extends FormDockView implements BaseWidgetPropertyPane {

    private WidgetPropertyTable propertyTable;
    private EventPropertyTable eventTable;
    private List<AbstractPropertyTable> widgetPropertyTables;
    private FormDesigner designer;

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
//        JTabbedPane tabbedPane = new JTabbedPane();
        UITabbedPane tabbedPane = new UITabbedPane();
        tabbedPane.setOpaque(true);
        tabbedPane.setBorder(null);
        tabbedPane.setTabPlacement(SwingConstants.BOTTOM);
        tabbedPane.addTab(Inter.getLocText("Form-Properties"), psp);
        tabbedPane.addTab(Inter.getLocText("Form-Events"), esp);

        WidgetAttrProvider[] widgetAttrProviders = ExtraDesignClassManager.getInstance().getWidgetAttrProviders();
        for (WidgetAttrProvider widgetAttrProvider : widgetAttrProviders) {
            AbstractPropertyTable propertyTable = widgetAttrProvider.createWidgetAttrTable();
            widgetPropertyTables.add(propertyTable);
            designer.addDesignerEditListener(new WidgetPropertyDesignerAdapter(propertyTable));
            UIScrollPane uiScrollPane = new UIScrollPane(propertyTable);
            uiScrollPane.setBorder(null);
            tabbedPane.addTab(widgetAttrProvider.setTableTitle(), uiScrollPane);
        }
        add(tabbedPane, BorderLayout.CENTER);

        propertyTable.initPropertyGroups(null);
        eventTable.refresh();
        if (widgetPropertyTables.size() > 0) {
            for (AbstractPropertyTable propertyTable : widgetPropertyTables) {
                propertyTable.initPropertyGroups(designer);
            }
        }
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