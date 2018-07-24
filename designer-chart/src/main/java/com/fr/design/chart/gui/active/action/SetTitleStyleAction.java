package com.fr.design.chart.gui.active.action;

import java.awt.event.ActionEvent;

import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.module.DesignModuleFactory;


/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午4:52
 */
public class SetTitleStyleAction extends ChartComponentAction {
	private static final long serialVersionUID = -4763886493273213850L;

	public SetTitleStyleAction(ChartComponent chartComponent) {
		super(chartComponent);
		this.setName(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Set", "Title", "Style"}));
	}

	public void actionPerformed(ActionEvent e) {
		showTitlePane();
	}

	public void showTitlePane() {
		DesignModuleFactory.getChartPropertyPane().getChartEditPane().gotoPane(PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_TITLE_TITLE);
	}
}