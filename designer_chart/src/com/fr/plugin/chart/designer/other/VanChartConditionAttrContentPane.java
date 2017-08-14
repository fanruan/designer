package com.fr.plugin.chart.designer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.component.ConditionUIMenuNameableCreator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 16/5/20.
 * 只是不包含外面一层scroll
 */
public class VanChartConditionAttrContentPane extends AbstractConditionAttrContentPane{
    private static final Dimension DIALOG_SIZE = new Dimension(500, 600);

    private UICorrelationComboBoxPane conditionPane;
    public VanChartConditionAttrContentPane() {
        if (conditionPane == null) {
            conditionPane = new UICorrelationComboBoxPane(){
                protected Dimension getDialogSize() {
                    return DIALOG_SIZE;
                }
            };
        }

        this.setLayout(new BorderLayout());
        this.add(conditionPane, BorderLayout.CENTER);
    }

    public void populateBean(Plot plot, Class<? extends ConditionAttributesPane> showPane) {
        ConditionCollection collection = plot.getConditionCollection();

        populateBean(plot, collection, showPane);
    }

    public void populateBean(Plot plot, ConditionCollection collection,  Class<? extends ConditionAttributesPane> showPane){
        List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        list.add(new ConditionUIMenuNameableCreator(plot, Inter.getLocText("Chart-Condition_Attributes"), new ConditionAttr(), showPane));

        conditionPane.refreshMenuAndAddMenuAction(list);

        List<UIMenuNameableCreator> valueList = new ArrayList<UIMenuNameableCreator>();

        for(int i = 0; i < collection.getConditionAttrSize(); i++) {
            valueList.add(new ConditionUIMenuNameableCreator(plot, collection.getConditionAttr(i).getName(), collection.getConditionAttr(i), showPane));
        }

        conditionPane.populateBean(valueList);
        conditionPane.doLayout();
    }

    public void updateBean(Plot plot) {
        ConditionCollection cc = plot.getConditionCollection();
        update(cc);
    }

    public void update(ConditionCollection cc){
        List<UIMenuNameableCreator> list = conditionPane.updateBean();

        cc.clearConditionAttr();
        for(UIMenuNameableCreator creator : list){
            ConditionAttr ca = (ConditionAttr)creator.getObj();
            ca.setName(creator.getName());
            cc.addConditionAttr(ca);
        }
    }
}
