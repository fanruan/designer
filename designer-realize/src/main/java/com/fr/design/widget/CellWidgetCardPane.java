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
import com.fr.design.widget.mobile.WidgetMobilePane;
import com.fr.design.widget.ui.BasicWidgetPropertySettingPane;
import com.fr.form.event.Listener;
import com.fr.form.ui.Widget;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;

/*
 * carl :单独弄出来
 */
public class CellWidgetCardPane extends BasicPane {
    //当前的编辑器属性定义面板
    private DataModify<? extends Widget> currentEditorDefinePane;
    //当前的编辑器移动端面板
    private WidgetMobilePane currentWidgetMobilePane;
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

    //移动端属性容器
    private JPanel mobileTabPane;
    private JPanel mobileCardPane;
    private CardLayout mobileCardLayout;

    private ElementCasePane pane;

    public CellWidgetCardPane(ElementCasePane pane) {
        this.pane = pane;
    }


    private void initComponents(ElementCasePane pane) {

        this.removeAll();
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        tabbedPane = new CardLayout();
        center = new JPanel(tabbedPane);
        this.add(center, BorderLayout.CENTER);

        // 属性
        attriTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        BasicScrollPane basicScrollPane = new AttrScrollPane() {
            @Override
            protected JPanel createContentPane() {
                return attriTabPane;
            }
        };
        widgetPropertyPane = new BasicWidgetPropertySettingPane();
        UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Basic"), 280, 24, widgetPropertyPane);
        attriTabPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        attriTabPane.add(uiExpandablePane, BorderLayout.NORTH);
        attriCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        attriTabPane.add(attriCardPane, BorderLayout.CENTER);
        attriCardLayout = (CardLayout) attriCardPane.getLayout();

        // 事件
        eventTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        eventTabPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        eventPane = initWidgetEventPane(pane);
        eventTabPane.add(eventPane, BorderLayout.CENTER);

        // 移动端
        mobileTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        mobileCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        mobileTabPane.add(mobileCardPane, BorderLayout.CENTER);
        mobileCardLayout = (CardLayout) mobileCardPane.getLayout();

        center.add(basicScrollPane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Attribute"));
        center.add(eventTabPane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Event"));
        center.add(mobileTabPane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget_Mobile_Terminal"));
        initPaneList();


        final String[] tabTitles = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Attribute"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Event"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget_Mobile_Terminal")};
        tabsHeaderIconPane = new UIHeadGroup(tabTitles) {
            @Override
            public void tabChanged(int index) {
                tabbedPane.show(center, tabTitles[index]);
            }
        };
        tabsHeaderIconPane.setNeedLeftRightOutLine(true);
        tabsHeaderIconPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.SHADOW_GREY));
        this.add(tabsHeaderIconPane, BorderLayout.NORTH);
    }

    private void initPaneList() {
        paneList = new ArrayList<JPanel>();
        paneList.add(attriTabPane);
        paneList.add(eventPane);
        paneList.add(mobileTabPane);
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

        WidgetDefinePaneFactory.RN rn = WidgetDefinePaneFactory.createWidgetDefinePane(cellWidget, new Operator() {
            @Override
            public void did(DataCreatorUI ui, String cardName) {
                //todo
            }
        });
        DataModify<? extends Widget> definePane = rn.getDefinePane();
        attriCardPane.add(definePane.toSwingComponent(), rn.getCardName());
        attriCardLayout.show(attriCardPane, rn.getCardName());
        widgetPropertyPane.populate(cellWidget);
        currentEditorDefinePane = definePane;

        eventPane.populate(cellWidget);

        WidgetMobilePane mobilePane = WidgetMobilePaneFactory.createWidgetMobilePane(cellWidget);
        mobileCardPane.add(mobilePane, mobilePane.getClass().toString());
        mobileCardLayout.show(mobileCardPane, mobilePane.getClass().toString());
        currentWidgetMobilePane = mobilePane;


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

        currentWidgetMobilePane.update(widget);

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
