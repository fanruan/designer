package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.base.Utils;
import com.fr.chart.base.ChartEnumDefinitions;
import com.fr.chart.chartattr.MapPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.chart.chartglyph.MapHotAreaColor;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.general.ComparatorUtils;


import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * 图表属性表, 图表样式- 地图系列.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-14 上午11:00:32
 */
public class MapSeriesPane extends AbstractPlotSeriesPane{
	private UICheckBox isHeatMap;
	private UIComboBox areaTitles;

	private UIColorPickerPane4Map colorPickPane;
	private AbstractPlotSeriesPane combinePane;

	public MapSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}

	protected UIColorPickerPane4Map createColorPickerPane(){
		return new MapColorPickerPaneWithFormula(parentPane);
	}
	
	@Override
	protected ChartFillStylePane getFillStylePane() {
		return null;
	}

	@Override
	protected JPanel getContentInPlotType() {
		isHeatMap = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Heat_Map"));
		isHeatMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkHeatMapAreaTitles();
			}
		});

		areaTitles = new UIComboBox();
        areaTitles.setEnabled(false);
		colorPickPane = createColorPickerPane();

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f };
		double[] rowSize = { p, p, p, p, p};
        Component[][] components = new Component[][]{
				new Component[]{isHeatMap, null},
				new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Chart-Data_Configuration")), areaTitles},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("ChartF_ValueRange_MatchColor")),null},
                new Component[]{colorPickPane,null},
        } ;

		JPanel northPane = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);

		combinePane = this.getCombinePane();
		if(combinePane != null){
			double[] col = {f};
			double[] row = {p, p};
			components = new Component[][]{
					new Component[]{new JSeparator()},
					new Component[]{combinePane}
			};
			JPanel centerPane = TableLayoutHelper.createTableLayoutPane(components, row, col);

			JPanel pane = new JPanel(new BorderLayout());
			pane.add(northPane, BorderLayout.NORTH);
			pane.add(centerPane, BorderLayout.CENTER);
			return pane;
		}else{
			return northPane;
		}
	}

	private AbstractPlotSeriesPane getCombinePane(){
		AbstractPlotSeriesPane pane = null;
		if(this.chart != null && this.chart.getPlot() != null){
			MapPlot plot = (MapPlot)this.chart.getPlot();
			if(plot.getMapType() != ChartEnumDefinitions.MapType.Map_Normal){
				switch (plot.getMapType()){
					case Map_Bubble:
						pane = new CombinedBubbleSeriesPane(this.parentPane, plot.getCurrentCombinedPlot());
						break;
					case Map_Pie:
						pane = new CombinedPie2DSeriesPane(this.parentPane, plot.getCurrentCombinedPlot());
						break;
					case Map_Column:
						pane = new CombinedBar2DSeriesPane(this.parentPane, plot.getCurrentCombinedPlot());
						break;
					default:
						pane = null;
				}
			}
		}
		return pane;
	}

	//每次populate和update都要check一下
	private void checkHeatMapAreaTitles(){
		if(this.chart != null && this.chart.getFilterDefinition() != null){
			MapPlot mapPlot = (MapPlot)this.chart.getPlot();
			String currentTitle = Utils.objectToString(areaTitles.getSelectedItem());
			areaTitles.removeAllItems();

			TopDefinition definition = (TopDefinition)this.chart.getFilterDefinition();
			ArrayList seriesDefinitions = definition.getSeriesDefinitionList();
			int heatIndex = 0;
			for(int i = 0, count = seriesDefinitions.size(); i < count; i++){
				SeriesDefinition sd = (SeriesDefinition)seriesDefinitions.get(i);
				String title = Utils.objectToString(sd.getSeriesName());
				areaTitles.addItem(title);
				if(ComparatorUtils.equals(title, currentTitle)){
					heatIndex = i;
				}
			}
			mapPlot.setHeatIndex(heatIndex);
			if(heatIndex < seriesDefinitions.size()){
                areaTitles.setSelectedIndex(heatIndex);
            }
		}
		areaTitles.setEnabled(this.isHeatMap.isSelected());
	}

	public void populateBean(Plot plot) {
		super.populateBean(plot);
		if(plot instanceof MapPlot) {
			MapPlot map = (MapPlot)plot;

			reLayoutWithMapPlot();

			if(this.combinePane != null){
				combinePane.populateBean(map.getCurrentCombinedPlot());
			}
			colorPickPane.populateBean(map.getMapAreaColor());
			isHeatMap.setSelected(map.isHeatMap());

            populateAreaTitles();
		}
	}

    private void populateAreaTitles(){
        if(this.chart != null && this.chart.getFilterDefinition() != null){
            MapPlot mapPlot = (MapPlot)this.chart.getPlot();
            areaTitles.removeAllItems();
            TopDefinition definition = (TopDefinition)this.chart.getFilterDefinition();
            ArrayList seriesDefinitions = definition.getSeriesDefinitionList();
            for(int i = 0, count = seriesDefinitions.size(); i < count; i++){
                SeriesDefinition sd = (SeriesDefinition)seriesDefinitions.get(i);
                String title = Utils.objectToString(sd.getSeriesName());
                areaTitles.addItem(title);
            }
            if(mapPlot.getHeatIndex() < seriesDefinitions.size()){
                areaTitles.setSelectedIndex(mapPlot.getHeatIndex());
            }
        }
        areaTitles.setEnabled(this.isHeatMap.isSelected());
    }

	public void updateBean(Plot plot) {
		super.updateBean(plot);
		if(plot instanceof MapPlot) {
			MapPlot map = (MapPlot)plot;
			if(this.combinePane != null){
				this.combinePane.updateBean(map.getCurrentCombinedPlot());
			}
			MapHotAreaColor mapHotAreaColor = map.getMapAreaColor();
			colorPickPane.updateBean(mapHotAreaColor);
			map.setHeatMap(isHeatMap.isSelected());
            map.setHeatIndex(map.isHeatMap() ? areaTitles.getSelectedIndex() : 0);
		}
	}

	//根据组合地图的不同类型重构面板
	private void reLayoutWithMapPlot(){
		this.removeAll();
		fillStylePane = getFillStylePane();
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { f };
		double[] rowSize = { p,p,p};
		Component[][] components = new Component[3][1];
		if(fillStylePane != null) {
			components[0] = new Component[]{fillStylePane};
			components[1] = new Component[]{new JSeparator()};
		}

		JPanel contentPane = getContentInPlotType();
		if(contentPane != null) {
			components[2] = new Component[]{contentPane};
		}

		JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
		this.setLayout(new BorderLayout());
		this.add(panel,BorderLayout.CENTER);
	}
}