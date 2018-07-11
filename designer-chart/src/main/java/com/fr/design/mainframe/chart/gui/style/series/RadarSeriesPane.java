package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.AttrMarkerType;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.RadarPlot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.MarkerFactory;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

/**
 * 属性表, 样式 - 雷达图系列属性.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-5-3 上午10:59:27
 */
public class RadarSeriesPane extends AbstractPlotSeriesPane{

	protected UIButtonGroup<Boolean> isNullValueBreak;
	protected UICheckBox isCurve;
	private LineComboBox lineStyle;
	private MarkerComboBox markerPane;
	
	public RadarSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}

	@Override
	protected JPanel getContentInPlotType() {
		isCurve = new UICheckBox(Inter.getLocText("ChartF-Fill"));
		lineStyle = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
		markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
		String[] nameArray = {Inter.getLocText("Chart_Null_Value_Break"), Inter.getLocText("Chart_Null_Value_Continue")};
		Boolean[] valueArray = {true, false};
		isNullValueBreak = new UIButtonGroup<Boolean>(nameArray, valueArray);
		
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f };
		double[] rowSize = { p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Chart_Line_Style")),isCurve},
                new Component[]{new UILabel(Inter.getLocText(new String[]{"Chart_Line", "Line-Style"})),lineStyle },
                new Component[]{new UILabel(Inter.getLocText(new String[]{"ChartF-Marker", "FS_Report_Type"})), markerPane},
                new Component[]{new UILabel(Inter.getLocText("Null_Value_Show")),isNullValueBreak}
        } ;
        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
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

        attr = attrList.getExisted(AttrColor.class);// kunsnat: 兼容.
        if(attr != null) {
            attrList.remove(attr);
        }
    }
    @Override
    public void populateBean(Plot plot) {
        super.populateBean(plot);
        isCurve.setSelected(((RadarPlot)plot).isFilled());
        ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
        populateAttrCondition(attrList.getConditionIterator());
        isNullValueBreak.setSelectedItem(plot.isNullValueBreak());
    }

    @Override
    public void updateBean(Plot plot) {
        super.updateBean(plot);
        ((RadarPlot)plot).setIsFilled(isCurve.isSelected());
        plot.setNullValueBreak(isNullValueBreak.getSelectedItem());
        ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
        updateAttrCondition(attrList);
    }
}