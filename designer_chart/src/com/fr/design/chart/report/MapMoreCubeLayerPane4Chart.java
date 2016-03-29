package com.fr.design.chart.report;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.MapPlot;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class MapMoreCubeLayerPane4Chart extends MultiTabPane<ChartCollection> {
    private static final long serialVersionUID = -174286187746442527L;

   	private MapCubeLayerPane layerPane;
   	private MapCubeDataPane4Chart dataPane;

   	@Override
   	protected List<BasicPane> initPaneList() {
   		List<BasicPane> paneList = new ArrayList<BasicPane>();

   		paneList.add(layerPane = new MapCubeLayerPane());
   		paneList.add(dataPane = new MapCubeDataPane4Chart());

   		return paneList;
   	}

   	public ChartCollection updateBean() {
   		return null;// do nothing
   	}

   	public void populateBean(ChartCollection collection) {
   		Chart selectChart = collection.getSelectedChart();
   		if(selectChart != null && selectChart.getPlot() instanceof MapPlot) {
   			MapPlot map = (MapPlot)selectChart.getPlot();
   			layerPane.populateBean(map.getMapName());
   		}

   		// 确认层级关系
   		dataPane.populateBean(collection.getSelectedChart().getFilterDefinition());
   	}

   	public void updateBean(ChartCollection collection) {

   		collection.getSelectedChart().setFilterDefinition(dataPane.update());

   		Chart selectChart = collection.getSelectedChart();
   		if(selectChart != null && selectChart.getPlot() instanceof MapPlot) {
   			MapPlot map = (MapPlot)selectChart.getPlot();
   			layerPane.updateBean(map.getMapName());// 确定更新地图名称所对应的层级关系
   		}
   	}

   	/**
   	 * 刷新层级树 和 数据中populate 数据的层数
        * @param collection  图表收集器.
   	 */
   	public void init4PopuMapTree(ChartCollection collection) {
   		Chart selectChart = collection.getSelectedChart();
   		if(selectChart != null && selectChart.getPlot() instanceof MapPlot) {
   			MapPlot map = (MapPlot)selectChart.getPlot();
   			if(layerPane != null) {
   				layerPane.initRootTree(map.getMapName());
   			}
   		}
   	}

       /**
        * 判断是否合格
        * @param ob  参数判断
        * @return 默认合格.
        */
   	public boolean accept(Object ob) {
   		return true;
   	}

       /**
        * 界面标题
        * @return 返回标题
        */
   	public String title4PopupWindow() {
   		return Inter.getLocText("FR-Chart-Map_Multilayer");
   	}

       /**
        * 重置
        */
   	public void reset() {

   	}

   	/**
   	 * 设置是否支持单元格数据.
   	 */
   	public void setSurpportCellData(boolean surpportCellData) {
   		dataPane.justSupportOneSelect(surpportCellData);
   	}

	/**
     * 出发数据集改变
     * @param tableDataWrapper 数据集
     */
	public void fireTableDataChanged(TableDataWrapper tableDataWrapper) {
		 dataPane.fireTableDataChanged(tableDataWrapper);
	}
}