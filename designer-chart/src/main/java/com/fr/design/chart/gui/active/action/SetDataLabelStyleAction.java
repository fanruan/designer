package com.fr.design.chart.gui.active.action;

import java.awt.event.ActionEvent;

import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.module.DesignModuleFactory;


public class SetDataLabelStyleAction extends ChartComponentAction{

	public SetDataLabelStyleAction(ChartComponent chartComponent) {
		super(chartComponent);
		this.setName(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Set", "Data-Label"}));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		showDataLabelStylePane();
	}
	
	public void showDataLabelStylePane() {
		DesignModuleFactory.getChartPropertyPane().getChartEditPane().gotoPane(PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_LABEL_TITLE);
    }

}