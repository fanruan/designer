package com.fr.van.chart.designer.style.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.axis.component.VanChartAxisButtonPane;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.CardLayout;

/**
 * 样式-坐标轴界面
 */
public class VanChartAxisPane extends BasicBeanPane<VanChart> {

    private static final long serialVersionUID = 3208082344409802817L;
    protected VanChartAxisButtonPane axisButtonPane;
    protected JPanel centerPane;
    private CardLayout cardLayout;
    protected Map<String, VanChartXYAxisPaneInterface> xAxisPaneMap = new HashMap<String, VanChartXYAxisPaneInterface>();
    protected Map<String, VanChartXYAxisPaneInterface> yAxisPaneMap = new HashMap<String, VanChartXYAxisPaneInterface>();

    protected VanChartAxisPlot editingPlot;

    protected VanChartStylePane parent;

    public VanChartAxisPane(VanChartAxisPlot plot, VanChartStylePane parent){
        this.editingPlot = plot;
        this.parent = parent;
        initComponents();
    }

    protected void initComponents() {
        this.setLayout(new BorderLayout());
        axisButtonPane = new VanChartAxisButtonPane(VanChartAxisPane.this);

        cardLayout = new CardLayout();
        centerPane = new JPanel(cardLayout);

        this.add(axisButtonPane, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);

        initXYPaneList();
    }

    private void initXYPaneList() {

        for(VanChartAxis axis : editingPlot.getXAxisList()){
            initXAxisPane(axis);
        }
        for(VanChartAxis axis : editingPlot.getYAxisList()){
            initYAxisPane(axis);
        }

        axisButtonPane.populateBean(editingPlot);
    }

    private VanChartXYAxisPaneInterface initXAxisPane(VanChartAxis axis){
        VanChartXYAxisPaneInterface axisPane = getDefaultXAxisScrollPane();
        axisPane.setParentPane(parent);
        centerPane.add((JPanel)axisPane, editingPlot.getXAxisName(axis));//centerPane需要和axisButton的text一样,因为切换
        xAxisPaneMap.put(axis.getAxisName(), axisPane);//paneMap里面的key用axisName,因为获取axis populate 对应pane
        return axisPane;
    }

    private VanChartXYAxisPaneInterface initYAxisPane(VanChartAxis axis){
        VanChartXYAxisPaneInterface axisPane = getDefaultYAxisScrollPane();
        axisPane.setParentPane(parent);
        centerPane.add((JPanel)axisPane, editingPlot.getYAxisName(axis));
        yAxisPaneMap.put(axis.getAxisName(), axisPane);
        return axisPane;
    }

    /**
     * 切换坐标轴
     * @param name 坐标轴名称
     */
    public void changeAxisSelected(String name) {
        cardLayout.show(centerPane, name);
    }

    /**
     * 添加X轴
     * @param name 坐标轴名称
     */
    public void addXAxis(String name) {
        List<VanChartAxis> xAxisList = editingPlot.getXAxisList();
        VanChartAxis axis = editingPlot.createXAxis(name, VanChartConstants.AXIS_BOTTOM);
        xAxisList.add(axis);

        VanChartXYAxisPaneInterface axisPane = initXAxisPane(axis);
        axisPane.populate(axis);
        axisButtonPane.populateBean(editingPlot);
        parent.initAllListeners();//这句话不能挪出去，自定义的时候每次add后populate都得initListeners
    }

    /**
     * 添加Y轴
     * @param name 坐标轴名称
     */
    public void addYAxis(String name) {
        List<VanChartAxis> yAxisList = editingPlot.getYAxisList();
        VanChartAxis axis = editingPlot.createYAxis(name, VanChartConstants.AXIS_LEFT);
        yAxisList.add(axis);

        VanChartXYAxisPaneInterface axisPane = initYAxisPane(axis);
        axisPane.populate(axis);
        axisButtonPane.populateBean(editingPlot);
        parent.initAllListeners();//这句话不能挪出去，自定义的时候每次add后populate都得initListeners
    }

    /**
     * 删除
     * @param name 坐标轴名称
     */
    public void removeAxis(String name) {
        List<VanChartAxis> xAxisList = editingPlot.getXAxisList();
        List<VanChartAxis> yAxisList = editingPlot.getYAxisList();
        for(int i = 0, len = xAxisList.size(); i < len; i++){
            VanChartAxis axis = xAxisList.get(i);
            if(ComparatorUtils.equals(name, axis.getAxisName())){
                xAxisList.remove(axis);
                removeOthers(i, true);
                centerPane.remove((JPanel)xAxisPaneMap.get(name));
                xAxisPaneMap.remove(name);
                axisButtonPane.populateBean(editingPlot);
                parent.initAllListeners();
                return;
            }
        }
        for(int i = 0, len = yAxisList.size(); i < len; i++){
            VanChartAxis axis = yAxisList.get(i);
            if(ComparatorUtils.equals(name, axis.getAxisName())){
                yAxisList.remove(axis);
                removeOthers(i, false);
                centerPane.remove((JPanel)yAxisPaneMap.get(name));
                yAxisPaneMap.remove(name);
                axisButtonPane.populateBean(editingPlot);
                parent.initAllListeners();
                return;
            }
        }
    }

    //删除此坐标轴相关堆积属性的设置
    protected void removeOthers(int axisIndex, boolean isXAxis){
        //堆积和坐标轴
        ConditionCollection stackAndAxisCondition = editingPlot.getStackAndAxisCondition();
        if(stackAndAxisCondition == null){
            return;
        }
        for(int i = 0, len = stackAndAxisCondition.getConditionAttrSize(); i < len; i++){
            ConditionAttr conditionAttr = stackAndAxisCondition.getConditionAttr(i);
            AttrSeriesStackAndAxis stackAndAxis = (AttrSeriesStackAndAxis)conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
            int index = isXAxis ? stackAndAxis.getXAxisIndex() : stackAndAxis.getYAxisIndex();
            if(index == axisIndex){
                stackAndAxisCondition.removeConditionAttr(conditionAttr);
            }
        }
    }

    @Override
    protected String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_AXIS_TITLE;
    }
    @Override
    public void populateBean(VanChart chart) {
        if(chart == null){
            return;
        }
        Plot plot = chart.getPlot();

        populateBean(plot);
    }

    public void populateBean(Plot plot) {
        if(plot == null){
            return;
        }

        editingPlot = (VanChartAxisPlot)plot;
        populate();
    }

    protected void populate(){
        if(editingPlot == null){
            return;
        }

        for(VanChartAxis axis : editingPlot.getXAxisList()){
            VanChartXYAxisPaneInterface axisPane = xAxisPaneMap.get(axis.getAxisName());
            axisPane.populate(axis);
        }
        for(VanChartAxis axis : editingPlot.getYAxisList()){
            VanChartXYAxisPaneInterface axisPane = yAxisPaneMap.get(axis.getAxisName());
            axisPane.populate(axis);
        }
    }

    protected VanChartXYAxisPaneInterface getDefaultXAxisScrollPane() {
        VanChartXYAxisPaneInterface axisPane = AxisPaneFactory.getXAxisScrollPane(editingPlot);
        if(axisPane == null){
            axisPane = new VanChartAxisScrollPaneWithTypeSelect();
        }
        return axisPane;
    }

    protected VanChartXYAxisPaneInterface getDefaultYAxisScrollPane() {
        VanChartXYAxisPaneInterface axisPane = AxisPaneFactory.getYAxisScrollPane(editingPlot);
        if(axisPane == null){
            axisPane = new VanChartAxisScrollPaneWithOutTypeSelect();
        }
        return axisPane;
    }

    public void updateBean(VanChart chart){
        if(chart == null){
            return;
        }

        Plot plot = chart.getPlot();

        updateBean(plot);

        updateBuryingPoint(chart);
    }

    protected void updateBuryingPoint(VanChart chart) {
        //坐标轴埋点
        ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.AXIS, chart.getPlot().getBuryingPointAxisConfig());
    }

    public void updateBean(Plot plot){
        if(plot == null){
            return;
        }

        update((VanChartRectanglePlot)plot);
    }
    protected void update(VanChartRectanglePlot rectanglePlot) {
        if(rectanglePlot == null){
            return;
        }
        List<VanChartAxis> xAxisNewList = new ArrayList<VanChartAxis>();
        List<VanChartAxis> yAxisNewList = new ArrayList<VanChartAxis>();

        for(VanChartAxis axis : rectanglePlot.getXAxisList()){
            VanChartXYAxisPaneInterface axisPane = xAxisPaneMap.get(axis.getAxisName());
            xAxisNewList.add(axisPane.update(axis));
        }

        for(VanChartAxis axis : rectanglePlot.getYAxisList()){
            VanChartXYAxisPaneInterface axisPane = yAxisPaneMap.get(axis.getAxisName());
            yAxisNewList.add(axisPane.update(axis));
        }

        rectanglePlot.setXAxisList(xAxisNewList);
        rectanglePlot.setYAxisList(yAxisNewList);
    }

    public VanChart updateBean() {
        return null;
    }
}