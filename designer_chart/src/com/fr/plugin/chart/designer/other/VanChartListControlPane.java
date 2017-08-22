package com.fr.plugin.chart.designer.other;

import com.fr.base.chart.BasePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.designer.component.ConditionUIMenuNameableCreator;
import com.fr.stable.Nameable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by mengao on 2017/8/18.
 */
public class VanChartListControlPane extends UIListControlPane {

    public VanChartListControlPane(BasePlot plot) {
        super(plot);
    }

    @Override
    public void populate(Nameable[] nameableArray) {
        initComponentPane();
        super.populate(nameableArray);
    }

    @Override
    public NameableCreator[] createNameableCreators() {
        return new ChartConditionNameObjectCreator[]{new ChartConditionNameObjectCreator(this.plot, Inter.getLocText("Condition_Attributes"), ConditionUIMenuNameableCreator.class, ChartTypeInterfaceManager.getInstance().getPlotConditionPane((Plot) plot).getClass())};
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
    public void saveSettings() {
        if (isPopulating) {
            return;
        }
        updateConditionCollection(((VanChartPlot) plot).getConditionCollection());
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified();
    }

    @Override
    public String getAddItemText() {
        return Inter.getLocText("FR-Designer_Add_Condition");
    }

    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Condition_Attributes");
    }

    protected Object getob2Populate(Object ob2Populate) {
        return ((ConditionUIMenuNameableCreator) ob2Populate).getObj();

    }

    /**
     * Update.
     */
    public void updateConditionCollection(ConditionCollection cc) {
        Nameable[] nameables = this.update();

        cc.clearConditionAttr();

        for (int i = 0; i < nameables.length; i++) {
            UIMenuNameableCreator uiMenuNameableCreator = (UIMenuNameableCreator) ((NameObject) nameables[i]).getObject();
            ConditionAttr ca = (ConditionAttr) uiMenuNameableCreator.getObj();
            ca.setName(nameables[i].getName());
            cc.addConditionAttr(ca);

        }
    }

}
