package com.fr.plugin.chart.designer.style.axis;

import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.designer.AbstractVanChartScrollPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

/**
 * 无类型选择。y轴数值轴。
 */
public class VanChartAxisScrollPaneWithOutTypeSelect extends AbstractVanChartScrollPane<VanChartAxis> implements VanChartXYAxisPaneInterface{
    private static final long serialVersionUID = 7700110757493668325L;
    protected VanChartBaseAxisPane axisPane;

    protected JPanel createContentPane(){
        initAxisPane();
        return axisPane;
    }

    public void setParentPane(VanChartStylePane parent) {
        axisPane.setParentPane(parent);
    }

    protected boolean isXAxis() {
        return false;
    }

    protected void initAxisPane() {
        axisPane = new VanChartValueAxisPane(isXAxis());
    }

    @Override
    protected String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_AXIS_TITLE;
    }
    @Override
    public void populateBean(VanChartAxis axis) {
        populate(axis);
    }

    public VanChartAxis update(VanChartAxis axis){
        if(axis != null){
            axisPane.updateBean(axis);
        }
        return axis;
    }

    public void populate(VanChartAxis axis){
        if(axis == null){
            return;
        }
        axisPane.populateBean(axis);
    }

    public VanChartAxis updateBean(String axisName, int position){
        return axisPane.updateBean(axisName, position);
    }
}