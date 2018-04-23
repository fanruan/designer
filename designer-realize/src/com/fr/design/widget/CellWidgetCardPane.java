package com.fr.design.widget;

import com.fr.design.constants.UIConstants;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.dialog.AttrScrollPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.widget.ui.BasicWidgetPropertySettingPane;
import com.fr.form.event.Listener;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/*
 * carl :单独弄出来
 */
public class CellWidgetCardPane extends BasicPane {
    //当前的编辑器属性定义面板
    private DataModify<? extends Widget> currentEditorDefinePane;
    //属性配置切换面板
    private ArrayList<JPanel> paneList;
    private JPanel center;
    private UIHeadGroup tabsHeaderIconPane;
    private CardLayout tabbedPane;
    private BasicWidgetPropertySettingPane widgetPropertyPane;

    //通用属性容器
    private JPanel attriTabPane;
    private JPanel attriCardPane;
    private CardLayout attriCardLayout;

    //事件属性容器
    private JPanel eventTabPane;
    private WidgetEventPane eventPane;

    private ElementCasePane pane;

    public CellWidgetCardPane(ElementCasePane pane) {
        this.pane = pane;
    }


    private void initComponents(ElementCasePane pane) {

        this.removeAll();
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        //k
        tabbedPane = new CardLayout();
        center = new JPanel(tabbedPane);
        this.add(center, BorderLayout.CENTER);
        attriTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        eventTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        eventTabPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        initPaneList();
        eventPane = initWidgetEventPane(pane);
        eventTabPane.add(eventPane, BorderLayout.CENTER);
        //k
        BasicScrollPane basicScrollPane = new AttrScrollPane() {
            @Override
            protected JPanel createContentPane() {
                return attriTabPane;
            }
        };
        center.add(basicScrollPane, Inter.getLocText("FR-Designer_Attribute"));
        center.add(eventTabPane, Inter.getLocText("FR-Designer_Event"));
        final String[] tabTitles = new String[]{Inter.getLocText("FR-Designer_Attribute"), Inter.getLocText("FR-Designer_Event")};

        tabsHeaderIconPane = new UIHeadGroup(tabTitles) {
            @Override
            public void tabChanged(int index) {
                tabbedPane.show(center, tabTitles[index]);
            }
        };
        tabsHeaderIconPane.setNeedLeftRightOutLine(true);
        tabsHeaderIconPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.SHADOW_GREY));
        this.add(tabsHeaderIconPane, BorderLayout.NORTH);

        widgetPropertyPane = new BasicWidgetPropertySettingPane();

        UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Basic"), 280, 24, widgetPropertyPane);


        attriTabPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        attriTabPane.add(uiExpandablePane, BorderLayout.NORTH);

        attriCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        attriTabPane.add(attriCardPane, BorderLayout.CENTER);
        attriCardLayout = (CardLayout) attriCardPane.getLayout();
    }

    private void initPaneList() {
        paneList = new ArrayList<JPanel>();
        paneList.add(attriTabPane);
        paneList.add(eventPane);
    }

    protected WidgetEventPane initWidgetEventPane(ElementCasePane pane){
        return new WidgetEventPane(pane);
    }

    @Override
    protected String title4PopupWindow() {
        return "Widget";
    }

    public void populate(Widget cellWidget) {
        initComponents(pane);
        currentEditorDefinePane = null;

        WidgetDefinePaneFactory.RN rn = WidgetDefinePaneFactory.createWidgetDefinePane(cellWidget, new Operator() {
            @Override
            public void did(DataCreatorUI ui, String cardName) {
                //todo
            }
        });
        DataModify<? extends Widget> definePane = rn.getDefinePane();
        attriCardPane.add(definePane.toSwingComponent(), rn.getCardName());
        attriCardLayout.show(attriCardPane, rn.getCardName());
        currentEditorDefinePane = definePane;
        eventPane.populate(cellWidget);
        widgetPropertyPane.populate(cellWidget);
        tabsHeaderIconPane.setSelectedIndex(0);
    }

    public Widget update() {
        if (currentEditorDefinePane == null) {
            return null;
        }
        Widget widget = currentEditorDefinePane.updateBean();
        if (widget == null) {
            return null;
        }
        widgetPropertyPane.update(widget);

        Listener[] listener = eventPane == null ? new Listener[0] : eventPane.updateListeners();
        widget.clearListeners();
        for (Listener l : listener) {
            widget.addListener(l);
        }

        return widget;
    }


    @Override
    /**
     *检查
     */
    public void checkValid() throws Exception {
        currentEditorDefinePane.checkValid();
        eventPane.checkValid();
    }

}
