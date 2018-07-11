package com.fr.van.chart.column;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.chart.comp.BorderAttriPane;
import com.fr.design.chart.series.SeriesCondition.LabelBorderPane;
import com.fr.design.condition.ConditionAttributesPane;

/**
 * Created by hufan on 2016/8/11.
 */
public class VanChartColumnLabelBorderPane extends LabelBorderPane {
    public VanChartColumnLabelBorderPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
    }

    @Override
    protected BorderAttriPane initBorderAttrPane(){
        return new ColumnBorderAttriPane();
    }

    public void populate(DataSeriesCondition condition) {
        super.populate(condition);
        if (condition instanceof AttrBorder) {
            ((ColumnBorderAttriPane)linePane).setRadius(attrBorder.getRoundRadius());
        }
    }

    public DataSeriesCondition update() {
        super.update();
        attrBorder.setRoundRadius((int) ((ColumnBorderAttriPane)linePane).getRadius());
        attrBorder.setRoundBorder(true);
        return attrBorder;
    }
}
