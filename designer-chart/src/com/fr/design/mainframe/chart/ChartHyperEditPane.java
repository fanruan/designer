package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPopAttrPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartOtherPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.ChartTypePane;

import java.util.ArrayList;


/**
 * 图表 超级链接  tab 切换
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-8-6 下午02:18:23
 */
public class ChartHyperEditPane  extends ChartEditPane {
	
	private ChartComponent useChartComponent;
	private ChartHyperPopAttrPane attrPane;
	
	public ChartHyperEditPane(int paraType, ValueEditorPane valueEditorPane, ValueEditorPane valueRenderPane) {
		paneList = new ArrayList<AbstractChartAttrPane>();
		
		paneList.add(attrPane = new ChartHyperPopAttrPane(paraType, valueEditorPane, valueRenderPane));
		paneList.add(new ChartTypePane());

        dataPane4SupportCell =  new ChartDataPane(listener);
        dataPane4SupportCell.setSupportCellData(false);
		paneList.add(dataPane4SupportCell);
		paneList.add(new ChartStylePane(listener));
		paneList.add(new ChartOtherPane());

		createTabsPane();
	}

	@Override
	protected ChartDataPane createChartDataPane(String plotID) {
		ChartDataPane dataPane = ChartTypeInterfaceManager.getInstance().getChartDataPane(plotID, listener);
		dataPane.setSupportCellData(false);
		return dataPane;
	}

    protected void addTypePane() {
        paneList.add(attrPane);
        paneList.add(typePane);
    }


    protected void setSelectedTab() {
        tabsHeaderIconPane.setSelectedIndex(1);
        card.show(center, getSelectedTabName());
        for (int i = 0; i < paneList.size(); i++) {
            paneList.get(i).registerChartEditPane(getCurrentChartEditPane());
        }
    }

	protected ChartEditPane getCurrentChartEditPane() {
		return this;
	}
	
	/**
	 * 关联  对应的ChartComponent
	 * @param chartComponent 对应的ChartComponent
	 */
	public void useChartComponent(ChartComponent chartComponent) {
		this.useChartComponent = chartComponent;
	}
	
	/**
	 * 响应超级链接中的demo变化.
	 */
	public void fire() {
		if(useChartComponent != null) {
			useChartComponent.populate(this.collection);
			useChartComponent.reset();
		}
	}

    /**
     * 取
     * @param hyperlink 超链
     */
	public void populateHyperLink(ChartHyperPoplink  hyperlink) {
		attrPane.populateBean(hyperlink);
		populate((ChartCollection)hyperlink.getChartCollection());
	}

    /**
     * 存
     * @param hyperlink 超链
     */
	public void updateHyperLink(ChartHyperPoplink hyperlink) {
		attrPane.updateBean(hyperlink);
	}
	
}