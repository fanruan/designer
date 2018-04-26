package com.fr.design.mainframe.chart.gui.style.series;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpinnerNumberModel;

import com.fr.base.Utils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.PiePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

public class Pie2DSeriesPane extends AbstractPlotSeriesPane{

	private static final long serialVersionUID = 2800670681079289596L;
    private static final int NUM = 100;
    protected ChartBeautyPane stylePane;
	protected UICheckBox isSecondPlot;
	protected UIButtonGroup<Integer> secondPlotType;
	protected UIBasicSpinner smallPercent;

	public Pie2DSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}
	
	
	protected void initCom() {
		stylePane = new ChartBeautyPane();
		isSecondPlot = new UICheckBox(Inter.getLocText("Chart_Second_Plot"));
		String[] nameArray = {Inter.getLocText("PieStyle"),Inter.getLocText("BarStyle")};
		Integer[] valueArray = {0, 1};
		secondPlotType = new UIButtonGroup<Integer>(nameArray, valueArray);
		smallPercent = new UIBasicSpinner(new SpinnerNumberModel(5, 0, 100, 1));
		
		secondPlotType.setSelectedIndex(0);
		
		isSecondPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkBoxUse();
			}
		});
	}

	@Override
	protected JPanel getContentInPlotType() {
		initCom();

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f };
		double[] rowSize = { p, p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{stylePane,null},
                new Component[]{new JSeparator(),null},
                new Component[]{isSecondPlot,null},
                new Component[]{new UILabel("    "+Inter.getLocText(new String[]{"Chart_Second_Plot", "Content"})),secondPlotType},
                new Component[]{new UILabel("    "+Inter.getLocText(new String[]{"ConditionB-is_less_than", "StyleFormat-Percent"})),smallPercent}
        }   ;
        
		return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}
	
	private void checkBoxUse() {
		boolean use = isSecondPlot.isSelected();
		GUICoreUtils.setEnabled(secondPlotType, use);
		GUICoreUtils.setEnabled(smallPercent, use);
	}
	
	public void populateBean(Plot plot) {
		super.populateBean(plot);
		if(plot instanceof PiePlot) {
			PiePlot piePlot = (PiePlot)plot;
			stylePane.populateBean(piePlot.getPlotStyle());
			
			isSecondPlot.setSelected(false);
			if(piePlot.getSubType() == ChartConstants.PIE_PIE) {
				isSecondPlot.setSelected(true);
				secondPlotType.setSelectedIndex(0);
			} else if(piePlot.getSubType() == ChartConstants.PIE_BAR) {
				isSecondPlot.setSelected(true);
				secondPlotType.setSelectedIndex(1);
			}
			
			if(isSecondPlot.isSelected()) {
				smallPercent.setValue(piePlot.getSmallPercent() * NUM);
			}
		}
		
		checkBoxUse();
	}
	
	public void updateBean(Plot plot) {
		super.updateBean(plot);
		if(plot instanceof PiePlot) {
			PiePlot piePlot = (PiePlot)plot;
			
			piePlot.setPlotStyle(stylePane.updateBean());
			
			if(isSecondPlot.isSelected()) {
				if(secondPlotType.getSelectedIndex() == 0) {
					piePlot.setSubType(ChartConstants.PIE_PIE);
				} else if(secondPlotType.getSelectedIndex() == 1) {
					piePlot.setSubType(ChartConstants.PIE_BAR);
				}
				
				Number number = Utils.objectToNumber(smallPercent.getValue(), true);
				if (number != null) {
					piePlot.setSmallPercent(number.doubleValue() / NUM);
				}
			} else {
				piePlot.setSubType(ChartConstants.PIE_NORMAL);
			}
		}
	}
}