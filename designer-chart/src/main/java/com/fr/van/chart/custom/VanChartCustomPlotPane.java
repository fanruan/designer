package com.fr.van.chart.custom;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.custom.CustomDefinition;
import com.fr.plugin.chart.custom.CustomIndependentVanChart;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.plugin.chart.custom.type.CustomStyle;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.custom.component.VanChartCustomPlotSelectPane;
import com.fr.van.chart.designer.type.AbstractVanChartTypePane;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mitisky on 16/2/16.
 */
public class VanChartCustomPlotPane extends AbstractVanChartTypePane {

    //是否选择自定义
    private boolean isCustom = false;

    private static final long serialVersionUID = -3481633368542654247L;

    //切换到自定义组合图时，显示的版面
    private JPanel customPane;
    private VanChartCustomPlotSelectPane customSelectPane;

    private JPanel autoPane;

    //自定义和自动版面的容器，cardLayOut布局
    private JPanel contentPane;

    protected Component[][] getPaneComponents(JPanel typePane) {

        initContent();

        return new Component[][]{
                new Component[]{typePane},
                new Component[]{stylePane},
                new Component[]{contentPane}
        };
    }


    private void initContent() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        autoPane = new JPanel();

        customSelectPane = new VanChartCustomPlotSelectPane();

        Component[][] components = new Component[][]{
                new Component[]{new JSeparator()},
                new Component[]{customSelectPane}
        };

        double[] columnSize = {p, f};
        double[] rowSize = {p, p};
        customPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        contentPane = new JPanel(new CardLayout()) {
            @Override
            public Dimension getPreferredSize() {
                if (isCustom) {
                    return customPane.getPreferredSize();
                } else {
                    return new Dimension(autoPane.getWidth(), 0);
                }
            }
        };
        contentPane.add(autoPane, "auto");
        contentPane.add(customPane, "custom");
    }

    private void checkCardPane() {
        CardLayout cardLayout = (CardLayout) contentPane.getLayout();
        if (isCustom) {
            cardLayout.show(contentPane, "custom");
        } else {
            cardLayout.show(contentPane, "auto");
        }
    }


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/van/chart/custom/images/column_line.png",
                "/com/fr/van/chart/custom/images/column_area.png",
                "/com/fr/van/chart/custom/images/stack_column_line.png",
                "/com/fr/van/chart/custom/images/custom.png",

        };
    }

    @Override
    public void updateBean(Chart chart) {

        //保存上次选中的值，其会在super中更新
        int lastState = chart.getPlot().getDetailType();

        super.updateBean(chart);

        //如果上次的状态和这次的装填不在同一个页面，说明同一个图表內切换了，需要情況数据配置
        if (lastState != chart.getPlot().getDetailType()) {
            chart.setFilterDefinition(null);
        }

        Chart[] customChart = CustomIndependentVanChart.CustomVanChartTypes;
        for (int i = 0, len = customChart.length; i < len; i++) {
            if (typeDemo.get(i).isPressing) {
                if (i == customChart.length - 1) {
                    isCustom = true;

                    //先重置自定义组合面板，如果不重置，无法获取选择顺序
                    if (lastState == customChart.length - 1 && isSamePlot()) {
                        //更新数据配置，刪除已经不在的图表数据
                        dealCustomDefinition(chart);

                        customSelectPane.updateBean(chart);
                    } else if (isSamePlot()) {//如果是同一个图表切换过来，则重置面板
                        customSelectPane.populateBean(chart);
                    }
                }
            } else {
                isCustom = false;
            }
        }

        checkCardPane();

    }

    private void dealCustomDefinition(Chart chart) {
        CustomDefinition definition = (CustomDefinition) chart.getFilterDefinition();

        if (definition == null) {
            return;
        }

        Map<CustomPlotType, TopDefinitionProvider> definitionMap = definition.getDefinitionProviderMap();

        if (definitionMap == null) {
            return;
        }

        Map<CustomPlotType, TopDefinitionProvider> newDefinitionMap = new HashMap<CustomPlotType, TopDefinitionProvider>();

        VanChartCustomPlot customPlot = (VanChartCustomPlot) chart.getPlot();
        for (int i = 0; i < customPlot.getCustomPlotList().size(); i++) {
            CustomPlotType plotType = CustomPlotFactory.getCustomType(customPlot.getCustomPlotList().get(i));
            TopDefinitionProvider definitionProvider = definitionMap.get(plotType);

            newDefinitionMap.put(plotType, definitionProvider);
        }

        definition.setDefinitionProviderMap(newDefinitionMap);
    }

    /**
     * 不同图表切換，重置chart屬性
     *
     * @param chart
     * @param newPlot
     */
    @Override
    protected void resetChartAttr(Chart chart, Plot newPlot) {
        super.resetChartAttr(chart, newPlot);
        //切换图表清空数据配置
        chart.setFilterDefinition(null);
        //设置默认不排序
        VanChartTools tools = ((VanChart) chart).getVanChartTools();
        if (tools != null) {
            tools.setSort(false);
        }
    }

    /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        for (ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }

        //获取上次选中的图标
        VanChartCustomPlot customPlot = (VanChartCustomPlot) chart.getPlot();
        typeDemo.get(customPlot.getDetailType()).isPressing = true;

        isCustom = customPlot.getCustomStyle() == CustomStyle.CUSTOM;

        //自定义选择时，更新自定义面板
        if (isCustom) {
            customSelectPane.populateBean(chart);
        }

        checkCardPane();

        checkDemosBackground();

    }

    protected Plot getSelectedClonedPlot() {
        VanChartCustomPlot newPlot = null;
        Chart[] customChart = CustomIndependentVanChart.CustomVanChartTypes;
        for (int i = 0, len = customChart.length; i < len; i++) {
            if (typeDemo.get(i).isPressing) {
                newPlot = (VanChartCustomPlot) customChart[i].getPlot();
            }
        }
        Plot cloned = null;
        try {
              if(newPlot != null) {
                 cloned = (Plot) newPlot.clone();
              }
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error In ScatterChart");
        }
        return cloned;
    }

    public Chart getDefaultChart() {
        return CustomIndependentVanChart.CustomVanChartTypes[0];
    }

    @Override
    /**
     *删除配置的条件属性
     */
    protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException {
        cloneOldDefaultAttrConditionCollection(oldPlot, newPlot);
    }

    @Override
    /**
     * 删除线型配置
     */
    protected void cloneOldDefaultAttrConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException {
        if (oldPlot.getConditionCollection() != null) {
            ConditionCollection newCondition = new ConditionCollection();
            newCondition.setDefaultAttr((ConditionAttr) oldPlot.getConditionCollection().getDefaultAttr().clone());
            newPlot.setConditionCollection(newCondition);

            //删除线型设置
            ConditionAttr attrList = newCondition.getDefaultAttr();
            DataSeriesCondition attr = attrList.getExisted(VanChartAttrLine.class);
            if (attr != null) {
                attrList.remove(VanChartAttrLine.class);
            }
        }
    }
}