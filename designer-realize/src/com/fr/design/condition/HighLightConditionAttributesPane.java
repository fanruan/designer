package com.fr.design.condition;

import com.fr.data.condition.ListCondition;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.HighlightProvider;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.report.cell.cellattr.highlight.*;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Set;

public class HighLightConditionAttributesPane extends ConditionAttributesPane<DefaultHighlight> {

    public HighLightConditionAttributesPane() {
        this.initComponents();
    }

    /**
     * 初始化组件
     */
    public void initComponents() {
        super.initComponents();
        this.initActionList();
        this.initLiteConditionPane();
    }

    protected void initLiteConditionPane() {
        // 条件Panel
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(centerPane);

        liteConditionPane = new ObjectLiteConditionPane();
        centerPane.add(liteConditionPane, BorderLayout.CENTER);
        liteConditionPane.setPreferredSize(new Dimension(300, 300));
    }

    protected void initActionList() {
        classPaneMap.put(ForegroundHighlightAction.class, new ForeGroundPane(this));
        classPaneMap.put(BackgroundHighlightAction.class, new BackPane(this));
        classPaneMap.put(FRFontHighlightAction.class, new FontPane(this));
        classPaneMap.put(PresentHighlightAction.class, new PresentHighlightPane(this));
        classPaneMap.put(PaddingHighlightAction.class, new PaddingPane(this));
        classPaneMap.put(RowHeightHighlightAction.class, new RowHeightPane(this));
        classPaneMap.put(ColWidthHighlightAction.class, new ColumnWidthPane(this));
        classPaneMap.put(PageHighlightAction.class, new PagePane(this));
        classPaneMap.put(HyperlinkHighlightAction.class, new HyperlinkPane(this));
        classPaneMap.put(BorderHighlightAction.class, new BorderHighlightPane(this));
        classPaneMap.put(WidgetHighlightAction.class, new WidgetHighlightPane(this));
        classPaneMap.put(ValueHighlightAction.class, new NewRealValuePane(this));
        Set<HighlightProvider> providers = ExtraDesignClassManager.getInstance().getArray(HighlightProvider.MARK_STRING);
        for (HighlightProvider provider : providers) {
            classPaneMap.put(provider.classForHighlightAction(), provider.appearanceForCondition(this));
        }

        useAbleActionList.clear();

        Iterator<ConditionAttrSingleConditionPane> iterator = classPaneMap.values().iterator();

        while (iterator.hasNext()) {
            useAbleActionList.add(iterator.next().getHighLightConditionAction());
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "Condition";
    }

    @Override
    public void populateBean(DefaultHighlight ob) {
        this.selectedItemPane.removeAll();
        this.initActionList();

        for (int i = 0, size = ob.actionCount(); i < size; i++) {
            HighlightAction hightlightAction = ob.getHighlightAction(i);

            ConditionAttrSingleConditionPane pane = classPaneMap.get(hightlightAction.getClass());

            pane.populate(hightlightAction);
            this.selectedItemPane.add(pane);
            useAbleActionList.remove(pane.getHighLightConditionAction());
        }
        this.liteConditionPane.populateBean(ob.getCondition() == null ? new ListCondition() : ob.getCondition());

        checkConditionPane();
        updateMenuDef();

        validate();
        repaint(10);
    }

    @Override
    public DefaultHighlight updateBean() {
        DefaultHighlight hl = new DefaultHighlight();

        for (ConditionAttrSingleConditionPane<HighlightAction> pane : classPaneMap.values()) {
            if (pane.getParent() == this.selectedItemPane) {
                hl.addHighlightAction(pane.update());
            }
        }

        hl.setCondition(this.liteConditionPane.updateBean());

        return hl;
    }


}