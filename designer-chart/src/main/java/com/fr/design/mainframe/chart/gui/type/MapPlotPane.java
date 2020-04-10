package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.ChartEnumDefinitions;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Legend;
import com.fr.chart.chartattr.MapPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.MapIndependentChart;
import com.fr.design.chart.series.PlotSeries.MapGroupExtensionPane;
import com.fr.design.chart.series.PlotStyle.ChartSelectDemoPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.log.FineLoggerFactory;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class MapPlotPane extends AbstractDeprecatedChartTypePane {


	private boolean isSvgMap = true; // 默认是svg地图
	private MapGroupExtensionPane groupExtensionPane;

	public MapPlotPane() {// 完全和其他图表类型不同的界面.
		typeDemo = createTypeDemoList();
		groupExtensionPane = new MapGroupExtensionPane();

		JPanel typePane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(4);
		for(int i = 0; i < typeDemo.size(); i++) {
			ChartImagePane tmp = typeDemo.get(i);
			typePane.add(tmp);
			tmp.setDemoGroup(typeDemo.toArray(new ChartSelectDemoPane[typeDemo.size()]));
		}

		this.setLayout(new BorderLayout());
		this.add(typePane,BorderLayout.NORTH);
		this.add(groupExtensionPane, BorderLayout.CENTER);
	}

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/MapPlot/type/0.png",
                "/com/fr/design/images/chart/MapPlot/type/1.png",
                "/com/fr/design/images/chart/MapPlot/type/2.png",
                "/com/fr/design/images/chart/MapPlot/type/3.png",
        };
    }

	@Override
	protected String[] getTypeTipName() {
		return new String[]{
				com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Map_Normal"),
				com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Map_Bubble"),
				com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Map_Pie"),
				com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Map_Column")
		};
	}

	@Override
    protected String[] getTypeLayoutPath() {
        return new String[0];
    }

	@Override
	protected String[] getTypeLayoutTipName() {
		return new String[0];
	}

	public void reLayout(String chartID) {

	}

	@Override
	public String getPlotID() {
		return ChartConstants.MAP_CHART;
	}

	/**
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Toolkit.i18nText("Fine-Design_Chart_SVG_MAP");
	}

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }

        MapPlot plot = getNewMapPlot();
        if(chart.getPlot().accept(MapPlot.class)){
            try{
                plot = (MapPlot)chart.getPlot().clone();
            } catch (CloneNotSupportedException e){
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
		plot.setMapName(groupExtensionPane.updateBean(plot));// 名字问题

		if(typeDemo.get(ChartEnumDefinitions.MapType.Map_Normal.ordinal()).isPressing){
			plot.setMapType(ChartEnumDefinitions.MapType.Map_Normal);
		}else if(typeDemo.get(ChartEnumDefinitions.MapType.Map_Bubble.ordinal()).isPressing){
			plot.setMapType(ChartEnumDefinitions.MapType.Map_Bubble);
		}else if(typeDemo.get(ChartEnumDefinitions.MapType.Map_Pie.ordinal()).isPressing){
			plot.setMapType(ChartEnumDefinitions.MapType.Map_Pie);
		}else if(typeDemo.get(ChartEnumDefinitions.MapType.Map_Column.ordinal()).isPressing){
			plot.setMapType(ChartEnumDefinitions.MapType.Map_Column);
		}

        if(plot.getMapType() != ChartEnumDefinitions.MapType.Map_Normal){
            plot.setHeatMap(false);
        }

		chart.setPlot(plot);

		checkDemosBackground();
	}

	@Override
	protected String getPlotTypeID() {
		return ChartConstants.MAP_CHART;
	}

	private MapPlot getNewMapPlot() {
        MapPlot plot = new MapPlot();
        plot.setLegend(new Legend());
        plot.setSvgMap(this.isSvgMap);
        return plot;
    }

	/**
	 * 更新地图名称 界面
	 */
	public void populateBean(Chart chart) {
		Plot plot = chart.getPlot();
		if (plot instanceof MapPlot) {
			MapPlot mapPlot = (MapPlot)plot;
			this.isSvgMap = mapPlot.isSvgMap();
			groupExtensionPane.populateBean(mapPlot);

			for(ChartImagePane imagePane : typeDemo) {
				imagePane.isPressing = false;
			}
			typeDemo.get(mapPlot.getDetailType()).isPressing = true;
		}

		checkDemosBackground();

        checkState();
	}

    private void checkState(){
        for(int i = 0; i < typeDemo.size(); i++){
            typeDemo.get(i).setEnabled(this.isSvgMap);
        }
        groupExtensionPane.setEnabled(isSvgMap);
    }

    public Chart getDefaultChart() {
        return MapIndependentChart.mapChartTypes[0];
    }
}