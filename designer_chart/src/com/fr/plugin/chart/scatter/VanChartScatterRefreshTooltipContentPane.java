package com.fr.plugin.chart.scatter;

import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.designer.component.format.ChangedPercentFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.component.format.ChangedValueFormatPaneWithCheckBox;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.scatter.attr.ScatterAttrTooltipContent;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/6/9.
 */
public class VanChartScatterRefreshTooltipContentPane extends VanChartScatterTooltipContentPane {

    private ChangedValueFormatPaneWithCheckBox changedSizeFormatPane;


    public VanChartScatterRefreshTooltipContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(null, showOnPane);
    }

    @Override
    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane) {
        super.initFormatPane(parent, showOnPane);
        changedSizeFormatPane = new ChangedValueFormatPaneWithCheckBox(parent, showOnPane);
        changedPercentFormatPane = new ChangedPercentFormatPaneWithCheckBox(parent, showOnPane);
    }


    protected double[] getRowSize(double p) {
        return new double[]{p, p, p, p, p, p};
    }

    protected Component[][] getPaneComponents() {
        return new Component[][]{
                new Component[]{seriesNameFormatPane, null},
                new Component[]{getxFormatPane(), null},
                new Component[]{getyFormatPane(), null},
                new Component[]{getSizeFormatPane(), null},
                new Component[]{changedSizeFormatPane, null},
                new Component[]{changedPercentFormatPane, null},
        };
    }


    @Override
    protected void populateFormatPane(AttrTooltipContent attrTooltipContent) {
        super.populateFormatPane(attrTooltipContent);
        if (attrTooltipContent instanceof ScatterAttrTooltipContent) {
            ScatterAttrTooltipContent scatterAttrTooltipContent = (ScatterAttrTooltipContent) attrTooltipContent;
            changedSizeFormatPane.populate(scatterAttrTooltipContent.getChangeSizeFormat());
            changedPercentFormatPane.populate(scatterAttrTooltipContent.getChangedSizePercentFormat());

        }
    }

    @Override
    protected void updateFormatPane(AttrTooltipContent attrTooltipContent) {
        super.updateFormatPane(attrTooltipContent);
        if (attrTooltipContent instanceof ScatterAttrTooltipContent) {
            ScatterAttrTooltipContent scatterAttrTooltipContent = (ScatterAttrTooltipContent) attrTooltipContent;
            changedSizeFormatPane.update(scatterAttrTooltipContent.getChangeSizeFormat());
            changedPercentFormatPane.update(scatterAttrTooltipContent.getChangedSizePercentFormat());
        }
    }

    @Override
    public void setDirty(boolean isDirty) {
        super.setDirty(isDirty);
        changedSizeFormatPane.setDirty(isDirty);
        changedPercentFormatPane.setDirty(isDirty);

    }

    @Override
    public boolean isDirty() {
        return super.isDirty() || changedSizeFormatPane.isDirty() || changedPercentFormatPane.isDirty();
    }

}
