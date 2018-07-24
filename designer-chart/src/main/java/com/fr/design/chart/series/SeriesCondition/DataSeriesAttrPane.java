package com.fr.design.chart.series.SeriesCondition;


import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.chart.series.SeriesCondition.impl.DataSeriesConditionPaneFactory;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * 系列属性的设置界面.
 * 主要是对系列的DataSeriesCondition进行设置.
 *
 * @see DataSeriesConditionPane
 */
public class DataSeriesAttrPane extends JListControlPane {
    private static final long serialVersionUID = -7265389532959632525L;

    private Plot plot;

    public DataSeriesAttrPane() {
        super();
    }

    public DataSeriesAttrPane(Plot plot) {
        this.plot = plot;
        this.initComponentPane();
    }

    @Override
    public NameableCreator[] createNameableCreators() {
        return new NameableCreator[]{
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Condition_Attributes"), ConditionAttr.class, DataSeriesConditionPaneFactory.findConfitionPane4DataSeries(plot))
        };
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Condition", "Display"});
    }

    public void populate(Plot plot) {
        List<NameObject> nameObjectList = new ArrayList<NameObject>();

        ConditionCollection cc = plot.getConditionCollection();
        for (int i = 0, len = cc.getConditionAttrSize(); i < len; i++) {
            ConditionAttr attr = cc.getConditionAttr(i);
            nameObjectList.add(new NameObject(attr.getName(), attr));
        }
        if (nameObjectList.size() > 0) {
            populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
        }
    }

    public void update(Plot plot) {
        Nameable[] res = update();
        NameObject[] res_array = new NameObject[res.length];
        java.util.Arrays.asList(res).toArray(res_array);
        ConditionCollection cc = plot.getConditionCollection();
        cc.clearConditionAttr();

        if (res_array.length < 1) {
            return;
        }

        for (int i = 0; i < res_array.length; i++) {
            NameObject nameObject = res_array[i];
            ConditionAttr attr = (ConditionAttr) nameObject.getObject();
            attr.setName(nameObject.getName());
            cc.addConditionAttr(attr);
        }
    }
}