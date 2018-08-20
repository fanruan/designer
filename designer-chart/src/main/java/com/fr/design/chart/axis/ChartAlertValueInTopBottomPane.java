package com.fr.design.chart.axis;




/**
 * 警戒线 居上 居下界面. 
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-5-22 上午09:53:28
 */
public class ChartAlertValueInTopBottomPane extends ChartAlertValuePane {
	
	protected String getLeftName() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alert_Bottom");
	}
	
	protected String getRightName() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alert_Top");
	}
}