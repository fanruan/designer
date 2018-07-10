package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.StockPlot;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.design.style.color.ColorSelectBox;

import javax.swing.*;
import java.awt.*;

/**
 * 股价图, 属性表 图表样式-系列.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-6 上午09:20:19
 */
public class StockSeriesPane extends AbstractPlotSeriesPane {
	
	private LineComboBox upLineBox;
	private ColorSelectBox upBorderColor;
	private ColorSelectBox upBackColor;
	
	private LineComboBox downLineBox;
	private ColorSelectBox downBorderColor;
	private ColorSelectBox downBackColor;

	public StockSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}

	@Override
	protected JPanel getContentInPlotType() {
		upLineBox = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
		upBorderColor = new ColorSelectBox(100);
		upBackColor = new ColorSelectBox(100);
		
		downLineBox = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
		downBorderColor = new ColorSelectBox(100);
		downBackColor = new ColorSelectBox(100);
		
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p,f };
		double[] rowSize = { p,p,p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
        		new Component[]{new BoldFontTextLabel(Inter.getLocText("UpBarBorderStyleAndColor")), null},
        		new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Border", "Line-Style"})), upLineBox},
        		new Component[]{new BoldFontTextLabel(Inter.getLocText("Border-Color")), upBorderColor},
        		new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Background", "Color"})), upBackColor},
        		
                new Component[]{new JSeparator(),null},
                
            	new Component[]{new BoldFontTextLabel(Inter.getLocText("DownBarBorderStyleAndColor")), null},
        		new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Border", "Line-Style"})), downLineBox},
        		new Component[]{new BoldFontTextLabel(Inter.getLocText("Border-Color")), downBorderColor},
        		new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Background", "Color"})), downBackColor},
                
        } ;
        JPanel pane = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
		
		return pane;
	}
	
	/**
	 * 返回 填充界面.
	 */
	protected ChartFillStylePane getFillStylePane() {
		return null;
	}
	
	public void populateBean(Plot plot) {
		super.populateBean(plot);
		if(plot instanceof StockPlot) {
			StockPlot stock = (StockPlot)plot;
			
			upLineBox.setSelectedLineStyle(stock.getUpBarBorderLineStyle());
			upBorderColor.setSelectObject(stock.getUpBarBorderLineBackground());
			upBackColor.setSelectObject(stock.getUpBarBackground());
			downLineBox.setSelectedLineStyle(stock.getDownBarBorderLineStyle());
			downBorderColor.setSelectObject(stock.getDownBarBorderLineBackground());
			downBackColor.setSelectObject(stock.getDownBarBackground());
		}
	}
	
	public void updateBean(Plot plot) {
		super.updateBean(plot);
		if(plot instanceof StockPlot) {
			StockPlot stock = (StockPlot)plot;
			
			stock.setUpBarBorderLineStyle(upLineBox.getSelectedLineStyle());
			stock.setUpBarBorderLineBackground(upBorderColor.getSelectObject());
			stock.setUpBarBackground(upBackColor.getSelectObject());
			stock.setDownBarBorderLineStyle(downLineBox.getSelectedLineStyle());
			stock.setDownBarBorderLineBackground(downBorderColor.getSelectObject());
			stock.setDownBarBackground(downBackColor.getSelectObject());
		}
	}

}