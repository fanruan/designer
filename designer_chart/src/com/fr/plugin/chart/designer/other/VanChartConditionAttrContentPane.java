package com.fr.plugin.chart.designer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;

import java.awt.*;

/**
 * Created by Mitisky on 16/5/20.
 * 只是不包含外面一层scroll
 */
public class VanChartConditionAttrContentPane extends AbstractConditionAttrContentPane{
    private static final Dimension DIALOG_SIZE = new Dimension(500, 600);

    private UIListControlPane conditionPane;
    public VanChartConditionAttrContentPane() {
    }

    public void populateBean(Plot plot, Class<? extends ConditionAttributesPane> showPane) {
        ConditionCollection collection = plot.getConditionCollection();

        populateBean(plot, collection, showPane);
    }

    public void populateBean(Plot plot, ConditionCollection collection,  Class<? extends ConditionAttributesPane> showPane){

        if (conditionPane == null) {
            conditionPane = new VanChartListControlPane(plot);
        }

        this.setLayout(new BorderLayout());
        this.add(conditionPane, BorderLayout.CENTER);


        NameObject[] nameables = new NameObject[collection.getConditionAttrSize()];

        for(int i = 0; i < collection.getConditionAttrSize(); i++) {
            nameables[i]=(new NameObject(collection.getConditionAttr(i).getName(),collection.getConditionAttr(i)));
        }

        conditionPane.populate(nameables);
        conditionPane.doLayout();
    }

    public void updateBean(Plot plot) {
        ConditionCollection cc = plot.getConditionCollection();
        update(cc);
    }


    public void update(ConditionCollection cc) {
        Nameable[] nameables = conditionPane.update();
        cc.clearConditionAttr();
        for (int i = 0; i < nameables.length; i++) {
            if (nameables[i] instanceof UIMenuNameableCreator) {
                UIMenuNameableCreator uiMenuNameableCreator = (UIMenuNameableCreator) nameables[i];
                ConditionAttr ca = (ConditionAttr) uiMenuNameableCreator.getObj();
                ca.setName(uiMenuNameableCreator.getName());
                cc.addConditionAttr(ca);
            }
        }
    }
}
