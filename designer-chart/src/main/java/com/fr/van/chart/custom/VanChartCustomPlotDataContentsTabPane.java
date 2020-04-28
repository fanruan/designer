package com.fr.van.chart.custom;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.custom.CustomDefinition;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.van.chart.custom.component.VanChartCustomPlotTabPane;
import com.fr.van.chart.custom.component.VanChartDataPane;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fangjie on 2016/4/29.
 */
public class VanChartCustomPlotDataContentsTabPane extends VanChartCustomPlotTabPane<VanChartCustomPlot, ChartCollection> {
    public VanChartCustomPlotDataContentsTabPane(VanChartCustomPlot plot, VanChartCustomDataPane parent, AttributeChangeListener listener) {
        super(plot, parent, listener);
    }

    @Override
    protected void initTabTitle() {

        if (plot == null){
            return;
        }

        List<VanChartPlot> customPlotList = plot.getCustomPlotList();
        NameArray = new String[Math.min(customPlotList.size(), paneList.size())];
        for (int i = 0; i < customPlotList.size() && i < paneList.size(); i++) {
            JPanel pane = paneList.get(i);
            //获取点的tooltip作为标题
            VanChartPlot vanChartPlot = customPlotList.get(i);
            CustomPlotType plotType = CustomPlotFactory.getCustomType(vanChartPlot);

            NameArray[i] = CustomPlotFactory.getTitle(plotType);
            centerPane.add(pane, NameArray[i]);
        }
    }

    @Override
    protected List<JPanel> initPaneList() {

        if (plot == null){
            return null;
        }

        List<JPanel> paneList = new ArrayList<JPanel>();

        List<VanChartPlot> customPlotList = plot.getCustomPlotList();

        for (int i = 0; i < customPlotList.size(); i++){
            //根据不同的plot创建不同的数据配置界面
            ChartDataPane contentPane = new VanChartDataPane(listener);
            paneList.add(contentPane);
        }

        return paneList;
    }

    @Override
    public void populateBean(ChartCollection chartCollection){

        plot = (VanChartCustomPlot) chartCollection.getSelectedChart().getPlot();

        if (paneList == null){
            paneList = initPaneList();
        }

        if (paneList != null){

            try {

                List<VanChartPlot> customPlotList = plot.getCustomPlotList();


                for (int i = 0; i < paneList.size() && i < customPlotList.size(); i++) {
                    //將plot包裝起来，主要是为了获取dataDefinition
                    ChartCollection cloneCollection = (ChartCollection) chartCollection.clone();

                    //设置collection的plot
                    cloneCollection.getSelectedChart().setPlot(customPlotList.get(i));

                    //获取definitionMap中的dataDefinition
                    TopDefinitionProvider definition = chartCollection.getSelectedChart().getFilterDefinition();
                    TopDefinitionProvider dataDefinition = null;
                    if (definition != null && definition instanceof CustomDefinition) {
                        Map<CustomPlotType, TopDefinitionProvider> definitionProviderMap = ((CustomDefinition)definition).getDefinitionProviderMap();
                        dataDefinition = definitionProviderMap.get(CustomPlotFactory.getCustomType(customPlotList.get(i)));
                    }
                    cloneCollection.getSelectedChart().setFilterDefinition(dataDefinition);

                    ((ChartDataPane) paneList.get(i)).populate(cloneCollection);
                }
            }catch (Exception e){
                return;
            }
        }
    }

    @Override
    public ChartCollection updateBean() {
        return null;
    }

    @Override
    public void updateBean(ChartCollection collection){
        if (paneList == null || plot == null){
            return;
        }
        try {

            Map<CustomPlotType, TopDefinitionProvider> definitionMap = new LinkedHashMap<CustomPlotType, TopDefinitionProvider>();

            //已经有的数据配置不允许重置

            for (int i = 0; i < paneList.size() && i < plot.getCustomPlotList().size(); i++) {
                //将plot包裝起来，主要是为了获取dataDefinition
                ChartCollection cloneCollection = (ChartCollection) collection.clone();

                //设置Collection的plot
                cloneCollection.getSelectedChart().setPlot(plot.getCustomPlotList().get(i));

                //重置
                cloneCollection.getSelectedChart().setFilterDefinition(null);

                //更新
                ((ChartDataPane) paneList.get(i)).update(cloneCollection);

                CustomPlotFactory.setCustomCategoryAttr(plot);

                //将处理好的dataDefinition剥离出来并存储
                definitionMap.put(CustomPlotFactory.getCustomType(plot.getCustomPlotList().get(i)), cloneCollection.getSelectedChart().getFilterDefinition());

            }

            CustomDefinition customDefinition = new CustomDefinition();
            customDefinition.setDefinitionProviderMap(definitionMap);
            collection.getSelectedChart().setFilterDefinition(customDefinition);

        }catch (Exception e){
            return;
        }
    }

    @Override
    public boolean accept(Object ob) {
        return false;
    }

    @Override
    public String title4PopupWindow() {
        return null;
    }

    @Override
    public void reset() {

    }

    /**
     * 返回绑定的属性事件.
     * @param listener  增加监听
     */
    public void addAttributeChangeListener(AttributeChangeListener listener) {
        for (int i = 0; i < paneList.size(); i++){
            ((ChartDataPane) paneList.get(i)).addAttributeChangeListener(listener);
        }
    }
}
