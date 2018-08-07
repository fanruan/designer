package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.AttrMarkerType;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.LinePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.MarkerFactory;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

/**
 * 折线图 属性表 系列界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-23 上午10:29:03
 */
public class LineSeriesPane extends AbstractPlotSeriesPane{

	protected UICheckBox isCurve;
	protected UIButtonGroup<Boolean> isNullValueBreak;
	protected LineComboBox lineStyle;
	protected MarkerComboBox markerPane;

	public LineSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}
	
	public LineSeriesPane(ChartStylePane parent, Plot plot, boolean custom) {
		super(parent, plot, true);
	}
	
	@Override
	protected JPanel getContentInPlotType() {
		isCurve = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Chart_Curve"));
		lineStyle = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
		markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
		String[] nameArray = {com.fr.design.i18n.Toolkit.i18nText("Chart_Null_Value_Break"), com.fr.design.i18n.Toolkit.i18nText("Chart_Null_Value_Continue")};
		Boolean[] valueArray = {true, false};
		isNullValueBreak = new UIButtonGroup<Boolean>(nameArray, valueArray);
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f };
		double[] rowSize = { p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                    new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Chart_Line_Style")),isCurve},
                    new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line_Style")),lineStyle},
                    new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Marker_Type")), markerPane},
                    new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Null_Value_Show")), isNullValueBreak}
            };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}

	/**
	 * 更新系列界面.
	 */
	@Override
	public void populateBean(Plot plot) {
		super.populateBean(plot);
		if(plot instanceof LinePlot) {
			isCurve.setSelected(((LinePlot)plot).isCurve());
		}

		if(plot.isNullValueBreak()) {
			isNullValueBreak.setSelectedIndex(0);
		} else {
			isNullValueBreak.setSelectedIndex(1);
		}
        ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
        populateAttrCondition(attrList.getConditionIterator());
	}

    protected void populateAttrCondition(Iterator<DataSeriesCondition> iterator) {
        while(iterator.hasNext()) {
            DataSeriesCondition condition = iterator.next();
            if(condition instanceof AttrLineStyle) {
                int line = ((AttrLineStyle)condition).getLineStyle();
                if(line != Constants.LINE_THICK && line != Constants.LINE_THIN
                        && line != Constants.LINE_MEDIUM && line != Constants.LINE_NONE) {
                    line = Constants.LINE_THIN;
                }
                lineStyle.setSelectedLineStyle(line);
            } else if (condition instanceof AttrMarkerType) {
                markerPane.setSelectedMarker(MarkerFactory.createMarker(((AttrMarkerType) condition).getMarkerType()));
            }
        }
    }

	/**
	 * 保存系列界面的属性
	 */
	@Override
	public void updateBean(Plot plot) {
		super.updateBean(plot);
		if(plot instanceof LinePlot) {
			LinePlot linePlot = (LinePlot)plot;
			linePlot.setCurve(isCurve.isSelected());
		}
		plot.setNullValueBreak(isNullValueBreak.getSelectedIndex() == 0);
		ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
		updateAttrCondition(attrList);
	}

    protected void updateAttrCondition(ConditionAttr attrList) {
        DataSeriesCondition attr = attrList.getExisted(AttrLineStyle.class);
        if(attr != null) {
            attrList.remove(attr);
        }
        attrList.addDataSeriesCondition(new AttrLineStyle(lineStyle.getSelectedLineStyle()));

        attr = attrList.getExisted(AttrMarkerType.class);
        if(attr != null) {
            attrList.remove(attr);
        }
        attrList.addDataSeriesCondition(new AttrMarkerType(markerPane.getSelectedMarkder().getMarkerType()));

        attr = attrList.getExisted(AttrColor.class);//kunsnat: 兼容 颜色去掉设置.
        if(attr != null) {
            attrList.remove(attr);
        }
    }
}