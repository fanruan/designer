package com.fr.design.chart.axis;

import com.fr.general.Inter;


/**
 * 警戒线 居上 居下界面. 
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-5-22 上午09:53:28
 */
public class ChartAlertValueInTopBottomPane extends ChartAlertValuePane {
	
	protected String getLeftName() {
		return Inter.getLocText("Chart_Alert_Bottom");
	}
	
	protected String getRightName() {
		return Inter.getLocText("Chart_Alert_Top");
	}
}