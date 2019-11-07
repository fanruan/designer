package com.fr.design.condition;


import com.fr.common.annotations.Open;
import com.fr.design.actions.UpdateAction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.log.FineLoggerFactory;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;

@Open
public abstract class ConditionAttributesPane<T> extends BasicBeanPane<T> {
    private static final int MIN_HEIGHT = 60;
    protected CellHighlightAddMenuDef menuDef;

    protected JPanel selectedItemPane;

    protected LiteConditionPane liteConditionPane;

    protected java.util.Map<Class, ConditionAttrSingleConditionPane> classPaneMap = new HashMap<Class, ConditionAttrSingleConditionPane>();

    //可用的Actions.
    protected java.util.List<UpdateAction> useAbleActionList = new java.util.ArrayList<UpdateAction>();

    protected void initComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 属性 界面
        JPanel propertyChangePane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        this.add(propertyChangePane);

        propertyChangePane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Property") + ":", null));

        // 选择要改变的属性.
        JPanel addItemPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        ToolBarDef toolbarDef = new ToolBarDef();

        menuDef = new CellHighlightAddMenuDef();
        menuDef.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Highlight_Click_to_Choose_Property_To_Modify"));
        menuDef.setIconPath("com/fr/design/images/control/addPopup.png");

        toolbarDef.addShortCut(menuDef);

        updateMenuDef();

        UIToolbar toolBar = ToolBarDef.createJToolBar();
        toolbarDef.updateToolBar(toolBar);

        addItemPane.add(toolBar);

        propertyChangePane.add(addItemPane);

        selectedItemPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
//		selectedItemPane.setLayout(new BoxLayout(selectedItemPane,BoxLayout.Y_AXIS));

        // 选中的添加Itempane
        JScrollPane selectedItemScrollPane = new JScrollPane();
        selectedItemScrollPane.setViewportView(selectedItemPane);
        selectedItemScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        selectedItemScrollPane.setMinimumSize(new Dimension((int) selectedItemScrollPane.getPreferredSize().getWidth(), MIN_HEIGHT));
        propertyChangePane.add(selectedItemScrollPane);
    }
    
	public void updateBean(T ob) {
		updateMenuDef();
	}

    protected void updateMenuDef() {
        menuDef.clearShortCuts();
        for (UpdateAction ac : useAbleActionList) {
            menuDef.addShortCut(ac);
        }
        menuDef.updateMenu();
        menuDef.setEnabled(true);
    }

    public void checkConditionPane() {
        GUICoreUtils.setEnabled(this.liteConditionPane, this.selectedItemPane.getComponentCount() >= 1);
    }

    public ConditionAttrSingleConditionPane createConditionAttrSingleConditionPane(Class<? extends ConditionAttrSingleConditionPane> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    protected void addAction2UseAbleActionList() {
        useAbleActionList.clear();
        Iterator<ConditionAttrSingleConditionPane> iterator = classPaneMap.values().iterator();

        while (iterator.hasNext()) {
            useAbleActionList.add(iterator.next().getHighLightConditionAction());
        }
    }

    public void resetUseAbleActionList() {
        Iterator<ConditionAttrSingleConditionPane> iterator = classPaneMap.values().iterator();

        while (iterator.hasNext()) {
            ConditionAttrSingleConditionPane pane = iterator.next();
            if (pane.getParent() != this.selectedItemPane && !this.useAbleActionList.contains(pane.getHighLightConditionAction())) {
                this.useAbleActionList.add(pane.getHighLightConditionAction());
            }
        }
    }

    public void removeConditionAttrSingleConditionPane(JComponent component) {
        selectedItemPane.remove(component);
    }

    public void addConditionAttrSingleConditionPane(JComponent component) {
        selectedItemPane.add(component);
    }


    public void removeUpdateActionFromUsableList(UpdateAction action) {
        useAbleActionList.remove(action);
    }

    public void redraw() {
        validate();
        repaint();
    }

}