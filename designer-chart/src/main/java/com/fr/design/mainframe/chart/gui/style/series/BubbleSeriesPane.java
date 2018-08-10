package com.fr.design.mainframe.chart.gui.style.series;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.fr.base.Utils;
import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;


public class BubbleSeriesPane extends AbstractPlotSeriesPane{
	protected UIButtonGroup<Integer> bubbleMean;
	protected UICheckBox isMinus;
	protected UITextField zoomTime;
	
	public BubbleSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}

	/**
	 * 加载内容到展现的界面
	 */
	public void populateBean(Plot plot) {
		super.populateBean(plot);
		BubblePlot bubblePlot = (BubblePlot)plot;
		bubbleMean.setSelectedItem(bubblePlot.getSeriesEqualsBubbleInWidthOrArea());
		zoomTime.setText("" + bubblePlot.getMaxBubblePixel());
		isMinus.setSelected(bubblePlot.isShowNegativeBubble());
	}

	/**
	 * 保存展示的界面内容到属性
	 */
	public void updateBean(Plot plot) {
		super.updateBean(plot);
		BubblePlot bubblePlot = (BubblePlot) plot;
		bubblePlot.setSeriesEqualsBubbleInWidthOrArea(bubbleMean.getSelectedItem());
        Number result = Utils.string2Number(zoomTime.getText());
        bubblePlot.setMaxBubblePixel(result == null ? 0 : result.doubleValue());
		bubblePlot.setShowNegativeBubble(isMinus.isSelected());
	}

	protected void initCom(){
		String[] nameArray = {com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Chart_Bubble", "Chart_Area"}),
				com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Bubble_Width")};
		Integer[] valueArray = {BubblePlot.BUBBLE_AREA, BubblePlot.BUBBLE_WIDTH};
		bubbleMean = new UIButtonGroup<Integer>(nameArray, valueArray);
		zoomTime = new UITextField();
		isMinus = new UICheckBox(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Display", "Chart_Negative_Bubble"}));
	}

	@Override
	protected JPanel getContentInPlotType() {
		initCom();

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f };
		double[] rowSize = { p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{bubbleMean,null},
                new Component[]{new JSeparator(),null},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Chart_Change_Bubble_Size")),zoomTime},
                new Component[]{isMinus,null}
        } ;
        JPanel pane = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(pane,BorderLayout.CENTER);

		return pane;
	}

}