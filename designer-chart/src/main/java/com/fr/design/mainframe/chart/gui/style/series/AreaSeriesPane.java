package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.AttrMarkerType;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.AreaPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.MarkerFactory;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;


import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class AreaSeriesPane extends AbstractPlotSeriesPane{
	private UICheckBox isCurve;
    protected MarkerComboBox markerPane;
    private ChartBeautyPane stylePane;
	
	public AreaSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}

	public AreaSeriesPane(ChartStylePane parent, Plot plot, boolean custom) {
		super(parent, plot, true);
	}

	@Override
	protected JPanel getContentInPlotType() {
        stylePane = new ChartBeautyPane();
		isCurve = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Curve_Line"));
        markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p,f };
		double[] rowSize = { p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{stylePane, null},
                new Component[]{new JSeparator(), null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Polyline_Style")),isCurve},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Marker_Type")), markerPane}
        }  ;
        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}

    @Override
    public void populateBean(Plot plot) {
        super.populateBean(plot);
        if(stylePane != null) {
            stylePane.populateBean(plot.getPlotStyle());
        }
        isCurve.setSelected(((AreaPlot)plot).isCurve());

        ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
        populateAttrCondition(attrList.getConditionIterator());
    }

    protected void populateAttrCondition(Iterator<DataSeriesCondition> iterator) {
        while(iterator.hasNext()) {
            DataSeriesCondition condition = iterator.next();
            if (condition instanceof AttrMarkerType) {
                markerPane.setSelectedMarker(MarkerFactory.createMarker(((AttrMarkerType) condition).getMarkerType()));
            }
        }
    }

    @Override
    public void updateBean(Plot plot) {
        super.updateBean(plot);
        if(stylePane != null) {
            plot.setPlotStyle(stylePane.updateBean());
        }
        ((AreaPlot)plot).setCurve(isCurve.isSelected());
        ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
        updateAttrCondition(attrList);
    }

    protected void updateAttrCondition(ConditionAttr attrList) {
        DataSeriesCondition attr = attrList.getExisted(AttrMarkerType.class);
        if(attr != null) {
            attrList.remove(attr);
        }
        attrList.addDataSeriesCondition(new AttrMarkerType(markerPane.getSelectedMarkder().getMarkerType()));

    }

}