package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartdata.MapSingleLayerReportDefinition;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据定义: 地图单层设置
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-22 上午10:41:03
 */
public class MapSinglePane extends FurtherBasicBeanPane<TopDefinitionProvider> {

	private UIComboBoxPane<Chart> dataFromPane;// 数据来源的box界面
	
	private MapReportDataSinglePane reportSinglePane;
	private MapTableDataSinglePane tableSinglePane;
	
	public MapSinglePane() {
		initCom();
	}
	
	private void initCom() {
		this.setLayout(new BorderLayout());
		
		this.add(dataFromPane = new UIComboBoxPane<Chart>() {
			protected void initLayout() {
				this.setLayout(new BorderLayout(0, 6));
				JPanel northPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
				northPane.add(new BoldFontTextLabel(Inter.getLocText("ChartF-Data-Resource") + ":"));
				northPane.add(jcb);
				
				this.add(northPane, BorderLayout.NORTH);
				this.add(cardPane, BorderLayout.CENTER);
			}

			@Override
			protected List<FurtherBasicBeanPane<? extends Chart>> initPaneList() {
				List list = new ArrayList();
				
				list.add(tableSinglePane = new MapTableDataSinglePane());// 数据集界面
				list.add(reportSinglePane = new MapReportDataSinglePane());// 单元格界面
				
				return list;
			}

			@Override
			protected String title4PopupWindow() {
				return Inter.getLocText("Data_Setting");
			}
		}, BorderLayout.CENTER);
	}

	/**
	 * 判断准许的情况 
	 */
	public boolean accept(Object ob) {
		return ob instanceof TopDefinition;
	}

	/**
	 * 重置
	 */
	public void reset() {
		
	}

	/**
	 * 界面标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText(new String[]{"SingleLayer", "Chart-Map"});
	}

	/**
	 * 加载单层地图时的 数据来源界面
	 */
	public void populateBean(TopDefinitionProvider ob) {
		if(ob instanceof MapSingleLayerReportDefinition) {
			dataFromPane.setSelectedIndex(1);
			reportSinglePane.populateBean((MapSingleLayerReportDefinition)ob);
		} else if(ob instanceof MapSingleLayerTableDefinition) {
			dataFromPane.setSelectedIndex(0);
			tableSinglePane.populateBean((MapSingleLayerTableDefinition)ob);
		}
	}

	/**
	 * 保存下载 单层数据界面
	 */
	public TopDefinitionProvider updateBean() {
		if(dataFromPane.getSelectedIndex() == 0) {
			return tableSinglePane.updateBean();
		} else {
			return reportSinglePane.updateBean();
		}
	}
	
	public void setSurpportCellData(boolean surpportCellData) {
		dataFromPane.justSupportOneSelect(surpportCellData);
	}
}