package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.MapMoreLayerReportDefinition;
import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.chart.chartdata.MapSingleLayerReportDefinition;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择地图类型后 使用的地图数据界面
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-21 下午10:49:13
 */
public class MapDataPane extends DataContentsPane {
	
	private UIComboBoxPane<Chart> mainPane;
	private MapMoreCubeLayerPane morePane;
	private MapSinglePane singlePane;
	
	private AttributeChangeListener listener;
	
	public MapDataPane(AttributeChangeListener listener) {
		super();
		this.listener = listener;
	}
	
	/**
	 * 加载界面, 更新地图数据
	 */
	public void populate(ChartCollection collection) {
		TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
		
		morePane.init4PopuMapTree(collection);
		
		if(definition instanceof MapSingleLayerTableDefinition) {
			mainPane.setSelectedIndex(0);
			singlePane.populateBean(definition);
		}
		else if(definition instanceof MapMoreLayerTableDefinition) {
			mainPane.setSelectedIndex(1);
			morePane.populateBean(collection);
		} else if(definition instanceof MapMoreLayerReportDefinition) {
			mainPane.setSelectedIndex(1);
			morePane.populateBean(collection);
		} else if(definition instanceof MapSingleLayerReportDefinition) {
			mainPane.setSelectedIndex(0);
			singlePane.populateBean(definition);
		}
		
		this.initAllListeners();
		this.addAttributeChangeListener(listener);
	}
	
	/**
	 * 下载保存数据 
	 */
	public void update(ChartCollection collection) {
		if(mainPane.getSelectedIndex() == 0) {
			collection.getSelectedChart().setFilterDefinition(singlePane.updateBean());
		} else {
			morePane.updateBean(collection);
		}
	}

	@Override
	protected JPanel createContentPane() {
		BasicScrollPane<Chart> scroll = new BasicScrollPane<Chart>() {

			protected JPanel createContentPane() {
				JPanel pane = new JPanel();
				pane.setLayout(new BorderLayout());
				mainPane = new UIComboBoxPane<Chart>() {
					protected void initLayout() {
						this.setLayout(new BorderLayout(0, 6));
						JPanel northPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
						northPane.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Map_ShowWay") + ":"));
						northPane.add(jcb);
						this.add(northPane, BorderLayout.NORTH);
						this.add(cardPane, BorderLayout.CENTER);
					}
					protected List<FurtherBasicBeanPane<? extends Chart>> initPaneList() {
						List list = new ArrayList();
						list.add(singlePane = new MapSinglePane());
						list.add(morePane = new MapMoreCubeLayerPane());
						return list;
					}
					protected String title4PopupWindow() {
						return Inter.getLocText(new String[]{"Chart-Map", "Data"});
					}
				};
				pane.add(mainPane, BorderLayout.CENTER);
				return pane;
			}
			public void populateBean(Chart ob) {
			}
			protected String title4PopupWindow() {
				return null;
			}
		};
		return scroll;
	}


	/**
	 * 返回界面使用图标路径
	 */
	public String getIconPath() {
		return "com/fr/design/images/chart/ChartData.png";
	}

    /**
     * 返回界面标题
     * @return 标题
     */
	public String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Chart-Map", "Data"});
	}

	@Override
	public void setSupportCellData(boolean surpportCellData) {
		morePane.setSurpportCellData(surpportCellData);
		singlePane.setSurpportCellData(surpportCellData);
	}
}