package com.fr.van.chart.designer.other;

import com.fr.base.chart.BasePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;

import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.van.chart.designer.component.VanChartUIListControlPane;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by mengao on 2017/8/18.
 * 条件属性ListControlPane，弹出条件属性设置面板
 */
public class VanChartConditionListControlPane extends VanChartUIListControlPane {

    public VanChartConditionListControlPane(BasePlot plot) {
        super(plot);
    }

    public void populate(Nameable[] nameableArray, Class<? extends ConditionAttributesPane> showPane) {
        initComponentPane();
        this.setBorder(null);
        NameObjectCreator[] creators = new NameObjectCreator[]{new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Condition_Attributes"), ConditionAttr.class, showPane)};
        refreshNameableCreator(creators);
        super.populate(nameableArray);
    }

    @Override
    public NameableCreator[] createNameableCreators() {
        return new NameObjectCreator[]{new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Condition_Attributes"), ConditionAttr.class, ChartTypeInterfaceManager.getInstance().getPlotConditionPane((Plot) plot).getClass())};
    }


    protected BasicBeanPane createPaneByCreators(NameableCreator creator) {
        Constructor<? extends BasicBeanPane> constructor = null;
        try {
            constructor = creator.getUpdatePane().getConstructor(Plot.class);
            return constructor.newInstance(plot);

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String getAddItemText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Add_Condition");
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Condition_Attributes");
    }


    /**
     * Update.
     */
    protected void update(Plot plot) {
        ConditionCollection cc = plot.getConditionCollection();
        Nameable[] nameables = this.update();

        cc.clearConditionAttr();

        for (int i = 0; i < nameables.length; i++) {
            ConditionAttr ca = (ConditionAttr) ((NameObject) nameables[i]).getObject();
            ca.setName(nameables[i].getName());
            cc.addConditionAttr(ca);

        }
    }

}
