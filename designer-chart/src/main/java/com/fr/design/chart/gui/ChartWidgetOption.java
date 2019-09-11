package com.fr.design.chart.gui;

import com.fr.base.chart.BaseChartGetter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.form.ui.ChartEditor;
import com.fr.form.ui.Widget;
import com.fr.log.FineLoggerFactory;

import javax.swing.Icon;

/**
 * 表单中 图表控件信息, 名称, class, form中通过XcreatorUtils反射类 实现XChartEditor的初始化.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-7-5 上午09:59:39
 */
public class ChartWidgetOption extends WidgetOption {
	private static final long serialVersionUID = -6576352405047132226L;
	private String optionName;
	private Icon optionIcon;
	private Class<? extends ChartEditor> widgetClass;
	private String chartID;

	public ChartWidgetOption(String optionName, Icon optionIcon, Class<? extends ChartEditor> widgetClass, String chartID) {
		this.optionName = optionName;
		this.optionIcon = optionIcon;
		this.widgetClass = widgetClass;
		this.chartID = chartID;
	}

	/**
	 * 通过类 信息等 创建对应的控件编辑器.
	 *
	 * @return 返回控件编辑器.
	 */
	public Widget createWidget() {
		Class<? extends ChartEditor> clz = widgetClass();
		try {
			ChartEditor widget = clz.newInstance();
			widget.resetChangeChartCollection(BaseChartGetter.createChartCollection(this.chartID));
			return widget;
		} catch (InstantiationException e) {
			FineLoggerFactory.getLogger().error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 信息名称, 一般是图表的类型名称.
	 *
	 * @return 返回名称.
	 */
	public String optionName() {
		return this.optionName;
	}

	/**
	 * 信息图片, 一般是图表按钮的缩略图.
	 *
	 * @return 信息图片.
	 */
	public Icon optionIcon() {
		return this.optionIcon;
	}

	/**
	 * 图表控件对应Editor的类.
	 *
	 * @return 返回类.
	 */
	public Class<? extends ChartEditor> widgetClass() {
		return this.widgetClass;
	}
}