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
    //	当前的编辑器属性定义面板
    private DataModify<? extends Widget> currentEditorDefinePane;

    private JTabbedPane tabbedPane;

    private BasicWidgetPropertySettingPane widgetPropertyPane;
    private JPanel attriPane;
    private JPanel cardPane;
    private CardLayout card;

    private JPanel presPane;
    private JPanel cardPaneForPresent;
    private CardLayout cardForPresent;

    private JPanel cardPaneForTreeSetting;

    private JPanel formPane;
    private WidgetEventPane eventTabPane;

    public CellWidgetCardPane(ElementCasePane pane) {
        this.initComponents(pane);
    }

    private void initComponents(ElementCasePane pane) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        tabbedPane = new UITabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);
        attriPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        formPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        eventTabPane = new WidgetEventPane(pane);
        formPane.add(eventTabPane, BorderLayout.CENTER);
        tabbedPane.add(Inter.getLocText("Attribute"), attriPane);
        tabbedPane.add(Inter.getLocText("Form-Editing_Listeners"), formPane);

        presPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        cardPaneForPresent = FRGUIPaneFactory.createCardLayout_S_Pane();
        presPane.add(cardPaneForPresent, BorderLayout.CENTER);
        cardForPresent = new CardLayout();
        cardPaneForPresent.setLayout(cardForPresent);

        cardPaneForTreeSetting = FRGUIPaneFactory.createBorderLayout_L_Pane();

        widgetPropertyPane = new BasicWidgetPropertySettingPane();
        attriPane.add(widgetPropertyPane, BorderLayout.NORTH);
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        attriPane.add(cardPane, BorderLayout.CENTER);
        card = (CardLayout) cardPane.getLayout();
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

        attriPane.remove(widgetPropertyPane);
        widgetPropertyPane = new BasicWidgetPropertySettingPane();
        JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        northPane.setBorder(BorderFactory.createEmptyBorder(5, 8, 0, 8));
        JPanel basic = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Form-Basic_Properties"));
        northPane.add(basic);
        basic.add(widgetPropertyPane);
        attriPane.add(northPane, BorderLayout.NORTH);

        WidgetDefinePaneFactory.RN rn = WidgetDefinePaneFactory.createWidgetDefinePane(cellWidget, new Operator() {
            @Override
            public void did(DataCreatorUI ui, String cardName) {
                if (ui == null) {
                    addPresPane(false);
                    addTreeSettingPane(false);
                }
                if (ui instanceof DictionaryPane) {
                    showDictPane(ui, cardName);
                } else if (ui instanceof TreeSettingPane) {
                    showTreePane(ui);
                }
            }
        });
        DataModify<? extends Widget> definePane = rn.getDefinePane();
        cardPane.add(definePane.toSwingComponent(), rn.getCardName());
        card.show(cardPane, rn.getCardName());
        currentEditorDefinePane = definePane;
        eventTabPane.populate(cellWidget);
        widgetPropertyPane.populate(cellWidget);
        tabbedPane.setSelectedIndex(0);
    }

    private void showDictPane(DataCreatorUI ui, String cardName) {
        cardPaneForPresent.removeAll();
        cardPaneForPresent.add(ui.toSwingComponent(), cardName);
        cardForPresent.show(cardPaneForPresent, cardName);
        addPresPane(true);
    }

    private void showTreePane(DataCreatorUI ui) {
        cardPaneForTreeSetting.removeAll();
        cardPaneForTreeSetting.add(ui.toSwingComponent());
        addTreeSettingPane(true);
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

        Listener[] listener = eventTabPane == null ? new Listener[0] : eventTabPane.updateListeners();
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
    }

    //:jackie  如果选择的项有形态，则将形态面板加入tab面板
    private void addPresPane(boolean add) {
        if (add) {
            tabbedPane.add(this.presPane, 1);
            tabbedPane.setTitleAt(1, Inter.getLocText("DS-Dictionary"));
        } else {
            tabbedPane.remove(presPane);
        }
    }

    private void addTreeSettingPane(boolean add) {
        if (add) {
            tabbedPane.add(this.cardPaneForTreeSetting, 1);
            tabbedPane.setTitleAt(1, Inter.getLocText("Create_Tree"));
        } else {
            tabbedPane.remove(this.cardPaneForTreeSetting);
        }
    }
}