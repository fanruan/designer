package com.fr.design.widget;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.dialog.BasicPane;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.ui.BasicWidgetPropertySettingPane;
import com.fr.form.event.Listener;
import com.fr.form.ui.NoneWidget;
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

    //数字字典属性容器
    private JPanel dictTabPane;
    private JPanel dictCardPane;
    private CardLayout dictCardLayout;

    //构建树属性容器
    private JPanel treeTabPane;

    //事件属性容器
    private JPanel eventTabPane;
    private WidgetEventPane eventPane;

    private ElementCasePane pane;

    public CellWidgetCardPane(ElementCasePane pane) {
        this.pane = pane;
//        this.initComponents(pane);
    }

    public  BasicWidgetPropertySettingPane initBasicWidgetPropertyPane(){
        return new BasicWidgetPropertySettingPane();
    }


    private void initComponents(ElementCasePane pane) {

        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        //k
        tabbedPane =  new CardLayout();
        center = new JPanel(tabbedPane);
        this.add(center, BorderLayout.CENTER);

        attriTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        eventTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        initPaneList();
        eventPane = new WidgetEventPane(pane);
        eventTabPane.add(eventPane, BorderLayout.CENTER);
        //k
        center.add(attriTabPane, Inter.getLocText("FR-Designer_Attribute"));
        center.add(eventTabPane, Inter.getLocText("FR-Designer_Form_Editing_Listeners"));
        final String [] tabTitles = new String[]{Inter.getLocText("FR-Designer_Attribute"), Inter.getLocText("FR-Designer_Form_Editing_Listeners")};

        tabsHeaderIconPane = new UIHeadGroup(tabTitles) {
            @Override
            public void tabChanged(int index) {
                tabbedPane.show(center, tabTitles[index]);
            }
        };
        tabsHeaderIconPane.setNeedLeftRightOutLine(false);
        this.add(tabsHeaderIconPane, BorderLayout.NORTH);

        //数据字典
        dictTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        dictCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        dictTabPane.add(dictCardPane, BorderLayout.CENTER);
        dictCardLayout = new CardLayout();
        dictCardPane.setLayout(dictCardLayout);

        //构建树
        treeTabPane = FRGUIPaneFactory.createBorderLayout_L_Pane();

        widgetPropertyPane = new BasicWidgetPropertySettingPane();

        UIExpandablePane uiExpandablePane = new UIExpandablePane("基本", 280, 20, widgetPropertyPane);

        attriTabPane.add(uiExpandablePane, BorderLayout.NORTH);

        attriCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        attriTabPane.add(attriCardPane, BorderLayout.CENTER);
        attriCardLayout = (CardLayout) attriCardPane.getLayout();
        this.setPreferredSize(new Dimension(600, 450));
    }

    private void initPaneList(){
        paneList = new ArrayList<JPanel>();
        paneList.add(attriTabPane);
        paneList.add(eventPane);
    }

    @Override
    protected String title4PopupWindow() {
        return "Widget";
    }

    public void populate(Widget cellWidget) {
        initComponents(pane);
//        super.populate(cellWidget);
        currentEditorDefinePane = null;

        if (cellWidget instanceof NoneWidget) {
//            this.tabbedPane.setEnabled(false);
        } else {
//            this.tabbedPane.setEnabled(true);
        }

        WidgetDefinePaneFactory.RN rn = WidgetDefinePaneFactory.createWidgetDefinePane(cellWidget, new Operator() {
            @Override
            public void did(DataCreatorUI ui, String cardName) {
                if (ui == null) {
                    removeDictAttriPane();
                    removeTreeAttriPane();
                }
                if (ui instanceof DictionaryPane) {
                    removeDictAttriPane();
                    removeTreeAttriPane();
                    showDictPane(ui, cardName);
                } else if (ui instanceof TreeSettingPane) {
                    removeDictAttriPane();
                    removeTreeAttriPane();
                    showTreePane(ui);
                }
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

    private void showDictPane(DataCreatorUI ui, String cardName) {
        dictCardPane.removeAll();
        dictCardPane.add(ui.toSwingComponent(), cardName);
        dictCardLayout.show(dictCardPane, cardName);
        addDictAttriPane();
    }

    private void showTreePane(DataCreatorUI ui) {
        treeTabPane.removeAll();
        treeTabPane.add(ui.toSwingComponent());
        addTreeAttriPane();
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
//        super.update(widget);

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


    private void addDictAttriPane() {
        center.add(this.dictTabPane, Inter.getLocText("FR-Designer_DS_Dictionary"));
        reInitHeaderPane(this.dictTabPane);
    }

    private void addTreeAttriPane() {
        center.add(this.dictTabPane, Inter.getLocText("FR-Designer_Create_Tree"));
        reInitHeaderPane(this.treeTabPane);
    }

    private void removeDictAttriPane() {
        center.remove(this.dictTabPane);
    }

    private void removeTreeAttriPane() {
        center.remove(this.treeTabPane);
    }

    private void reInitHeaderPane(JPanel jPanel){
        paneList.add(jPanel);
//        tabsHeaderIconPane = new
    }

}
