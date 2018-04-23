package com.fr.design.chart.report;

import com.fr.chart.chartdata.MapMoreLayerReportDefinition;
import com.fr.chart.chartdata.MapSingleLayerReportDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.general.Inter;

import java.awt.*;

/**
 * 多层钻取  单元格的数据设置界面
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-23 上午10:07:41
 */
public class MapReportCubeDataPane extends FurtherBasicBeanPane<MapMoreLayerReportDefinition> {

    private MapMoreReportIndexPane reportPane;
	
	public MapReportCubeDataPane() {
		this.setLayout(new BorderLayout(0, 0));

        reportPane = new MapMoreReportIndexPane();
		this.add(reportPane, BorderLayout.CENTER);
	}

	/**
	 * 能够展示界面的判断.
     * @param ob  界面类型
     *            @return  返回是否符合.
	 */
	public boolean accept(Object ob) {
		return ob instanceof MapMoreLayerReportDefinition;
	}

	/**
	 * 界面重置
	 */
	public void reset() {

	}

	/**
	 * 界面弹出标题
     * @return  返回标题.
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("Cell");
	}

	@Override
	public void populateBean(MapMoreLayerReportDefinition ob) {// 根据Populate 决定层级的个数,  表现为row的个数

		if (ob != null) {

            MapSingleLayerReportDefinition[] values = ob.getNameValues();

            if(values != null && values.length > 0) {
                reportPane.populateBean(values[0]);
            }
		}
	}

	@Override
	public MapMoreLayerReportDefinition updateBean() {
		MapMoreLayerReportDefinition reportDefinition = new MapMoreLayerReportDefinition();

        reportDefinition.clearNameValues();
        reportDefinition.addNameValue(reportPane.updateBean());

		return reportDefinition;
	}

}