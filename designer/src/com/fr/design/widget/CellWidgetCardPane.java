package com.fr.design.widget;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.frpane.UITabbedPane;
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

/*
 * carl :单独弄出来
 */
public class CellWidgetCardPane extends BasicPane {
    //当前的编辑器属性定义面板
    private DataModify<? extends Widget> currentEditorDefinePane;
    //属性配置切换面板
    private JTabbedPane tabbedPane;
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

    public CellWidgetCardPane(ElementCasePane pane) {
        this.initComponents(pane);
    }

    private void initComponents(ElementCasePane pane) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        tabbedPane = new UITabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);
        attriTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        eventTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        eventPane = new WidgetEventPane(pane);
        eventTabPane.add(eventPane, BorderLayout.CENTER);
        tabbedPane.add(Inter.getLocText("FR-Designer_Attribute"), attriTabPane);
        tabbedPane.add(Inter.getLocText("FR-Designer_Form_Editing_Listeners"), eventTabPane);

        dictTabPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        dictCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        dictTabPane.add(dictCardPane, BorderLayout.CENTER);
        dictCardLayout = new CardLayout();
        dictCardPane.setLayout(dictCardLayout);

        treeTabPane = FRGUIPaneFactory.createBorderLayout_L_Pane();

        widgetPropertyPane = new BasicWidgetPropertySettingPane();
        attriTabPane.add(widgetPropertyPane, BorderLayout.NORTH);
        attriCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        attriTabPane.add(attriCardPane, BorderLayout.CENTER);
        attriCardLayout = (CardLayout) attriCardPane.getLayout();
        this.setPreferredSize(new Dimension(600, 450));
    }

    @Override
    protected String title4PopupWindow() {
        return "Widget";
    }

    public void populate(Widget cellWidget) {
        currentEditorDefinePane = null;

        if (cellWidget instanceof NoneWidget) {
            this.tabbedPane.setEnabled(false);
        } else {
            this.tabbedPane.setEnabled(true);
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
        tabbedPane.setSelectedIndex(0);
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
        tabbedPane.add(this.dictTabPane, 1);
        tabbedPane.setTitleAt(1, Inter.getLocText("FR-Designer_DS_Dictionary"));
    }

    private void addTreeAttriPane() {
        tabbedPane.add(this.treeTabPane, 1);
        tabbedPane.setTitleAt(1, Inter.getLocText("FR-Designer_Create_Tree"));
    }

    private void removeDictAttriPane() {
        tabbedPane.remove(this.dictTabPane);
    }

    private void removeTreeAttriPane() {
        tabbedPane.remove(this.treeTabPane);
    }

}
