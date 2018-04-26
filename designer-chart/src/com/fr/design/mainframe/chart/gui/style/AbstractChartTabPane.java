package com.fr.design.mainframe.chart.gui.style;

import com.fr.design.dialog.BasicScrollPane;

/**
 * 抽象的, 图表切换界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-4 下午03:55:42
 */
public abstract class AbstractChartTabPane<T> extends BasicScrollPane<T> {

	public abstract String title4PopupWindow();
	public AbstractChartTabPane(){
		super();
	}
	
	//上层pane已经有了scroll，需要把事件屏蔽掉
	public AbstractChartTabPane(boolean noScroll){
		super(noScroll);
	}
}