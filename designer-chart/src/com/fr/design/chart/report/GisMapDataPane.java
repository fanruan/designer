package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.GisMapReportDefinition;
import com.fr.chart.chartdata.GisMapTableDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GisMapDataPane extends DataContentsPane{
	private static final long serialVersionUID = -190573722921978406L;

	private UIComboBoxPane<Chart> dataFromPane;// 数据来源的box界面
	
	private GisMapReportDataContentPane reportPane;
	private GisMapTableDataContentPane tablePane;
	private AttributeChangeListener listener;
	
	public GisMapDataPane(AttributeChangeListener listener) {
		super();
		this.listener = listener;
	}
	

	/**
	 * 判断准许的情况
     * @param ob 传入的对象
     * @return 返回是否允许
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
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("ChartF-Gis");
	}

	public void setSupportCellData(boolean surpportCellData) {
		dataFromPane.justSupportOneSelect(surpportCellData);
	}


	@Override
	public void populate(ChartCollection collection) {
		TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
		
		if(definition instanceof GisMapTableDefinition) {
			dataFromPane.setSelectedIndex(0);
			tablePane.populateBean((GisMapTableDefinition)definition);
		}else if(definition instanceof GisMapReportDefinition) {
			dataFromPane.setSelectedIndex(1);
			reportPane.populateBean((GisMapReportDefinition)definition);
		} 
		
		this.initAllListeners();
		this.addAttributeChangeListener(listener);
	}

	@Override
	public void update(ChartCollection collection) {
			if(dataFromPane.getSelectedIndex() == 0){
				collection.getSelectedChart().setFilterDefinition(tablePane.updateBean());
			}else if(dataFromPane.getSelectedIndex() == 1){
				collection.getSelectedChart().setFilterDefinition(reportPane.updateBean());
			}
	}

	@Override
	protected JPanel createContentPane() {
		return new BasicScrollPane<Chart>() {

			@Override
			protected JPanel createContentPane() {
				JPanel contentPane = new JPanel();
				contentPane.setLayout(new BorderLayout());
				
				contentPane.add(dataFromPane = new UIComboBoxPane<Chart>() {
					protected void initLayout() {
                        this.setLayout(new BorderLayout(LayoutConstants.HGAP_LARGE,6));

                        double p = TableLayout.PREFERRED;
                        double f = TableLayout.FILL;
                        double[] columnSize = { p,f };
                        double[] rowSize = { p};
                        Component[][] components = new Component[][]{
                                new Component[]{new BoldFontTextLabel(Inter.getLocText("ChartF-Data-Resource") + ":"), jcb},
                        } ;

                        JPanel northPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 0);

						this.add(northPane, BorderLayout.NORTH);
						this.add(cardPane, BorderLayout.CENTER);
					}

					@Override
					protected List<FurtherBasicBeanPane<? extends Chart>> initPaneList() {
						List list = new ArrayList();
						
						list.add(tablePane = new GisMapTableDataContentPane());// 数据集界面
						list.add(reportPane = new GisMapReportDataContentPane());// 单元格界面
						
						return list;
					}

					@Override
					protected String title4PopupWindow() {
						return Inter.getLocText("Data_Setting");
					}
				}, BorderLayout.CENTER);

                dataFromPane.setBorder(BorderFactory.createEmptyBorder(10 ,0, 0, 0));
				return contentPane;
			}

			@Override
			public void populateBean(Chart ob) {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected String title4PopupWindow() {
				// TODO Auto-generated method stub
				return null;
			}};

	}

}