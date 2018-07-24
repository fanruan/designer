package com.fr.van.chart.designer.style.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.general.ComparatorUtils;

import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartTimeAxis;
import com.fr.plugin.chart.attr.axis.VanChartValueAxis;
import com.fr.plugin.chart.type.AxisType;
import com.fr.van.chart.designer.AbstractVanChartScrollPane;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * 带有坐标轴类型选择控件。可选类型：分类、时间、数值
 */
public class VanChartAxisScrollPaneWithTypeSelect extends AbstractVanChartScrollPane<VanChartAxis> implements VanChartXYAxisPaneInterface{
    private static final long serialVersionUID = -8462279689749700666L;
    private UIComboBoxPane axisTypePane;
    private VanChartTimeAxisPane timeAxisPane;
    private VanChartBaseAxisPane textAxisPane;
    private VanChartValueAxisPane valueAxisPane;

    protected boolean isXAxis() {
        return true;
    }

    public void setParentPane(VanChartStylePane parent) {
        timeAxisPane.setParentPane(parent);
        textAxisPane.setParentPane(parent);
        valueAxisPane.setParentPane(parent);
    }

    protected JPanel createContentPane(){
        timeAxisPane = new VanChartTimeAxisPane(isXAxis());
        textAxisPane = new VanChartBaseAxisPane(isXAxis());
        valueAxisPane = new VanChartValueAxisPane(isXAxis());

        axisTypePane = new UIComboBoxPane<Axis>() {

            protected List<FurtherBasicBeanPane<? extends Axis>> initPaneList() {
                List list = new ArrayList<FurtherBasicBeanPane>();
                list.add(textAxisPane);
                list.add(timeAxisPane);
                list.add(valueAxisPane);
                return list;
            }

            protected void initLayout() {
                this.setLayout(new BorderLayout(0,6));
                JPanel northPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_type"),jcb);
                northPane.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
                this.add(northPane, BorderLayout.NORTH);
                this.add(cardPane, BorderLayout.CENTER);

            }

            protected String title4PopupWindow() {
                return "";
            }
        };

        return axisTypePane;
    }

    @Override
    protected String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_AXIS_TITLE;
    }
    @Override
    public void populateBean(VanChartAxis axis) {
        populate(axis);
    }

    public void populate(VanChartAxis axis){
        AxisType axisType = axis.getAxisType();
        textAxisPane.populateBean(new VanChartAxis(axis.getAxisName(), axis.getPosition()));
        timeAxisPane.populateBean(new VanChartTimeAxis(axis.getAxisName(), axis.getPosition()));
        valueAxisPane.populateBean(new VanChartValueAxis(axis.getAxisName(), axis.getPosition()));
        if(ComparatorUtils.equals(axisType, AxisType.AXIS_CATEGORY)){
            textAxisPane.populateBean(axis);
        } else if(ComparatorUtils.equals(axisType, AxisType.AXIS_TIME)){
            timeAxisPane.populateBean(axis);
        } else if(ComparatorUtils.equals(axisType, AxisType.AXIS_VALUE)){
            valueAxisPane.populateBean(axis);
        }
        axisTypePane.setSelectedIndex(axisType.ordinal());
    }

    public VanChartAxis update(VanChartAxis axis) {
        int index = axisTypePane.getSelectedIndex();
        if(ComparatorUtils.equals(index, AxisType.AXIS_CATEGORY.ordinal())){
            if(ComparatorUtils.equals(axis.getAxisType(), AxisType.AXIS_CATEGORY)){
                textAxisPane.updateBean(axis);
            } else {
                axis = new VanChartAxis(axis.getAxisName(), axis.getPosition());
                textAxisPane.updateBean(axis);
            }
        } else if(ComparatorUtils.equals(index, AxisType.AXIS_TIME.ordinal())){
            if(ComparatorUtils.equals(axis.getAxisType(), AxisType.AXIS_TIME)){
                timeAxisPane.updateBean(axis);
            } else {
                axis = new VanChartTimeAxis(axis.getAxisName(), axis.getPosition());
                timeAxisPane.updateBean(axis);
            }
        } else if(ComparatorUtils.equals(index, AxisType.AXIS_VALUE.ordinal())){
            if(ComparatorUtils.equals(axis.getAxisType(), AxisType.AXIS_VALUE)){
                valueAxisPane.updateBean(axis);
            } else {
                axis = new VanChartValueAxis(axis.getAxisName(), axis.getPosition());
                valueAxisPane.updateBean(axis);
            }
        }
        return axis;
    }

}