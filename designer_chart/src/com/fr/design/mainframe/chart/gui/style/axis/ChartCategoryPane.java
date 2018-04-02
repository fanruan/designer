package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.CategoryAxis;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartAxisLabelPane;
import com.fr.design.mainframe.chart.gui.style.ChartAxisLineStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartAxisTitleNoFormulaPane;
import com.fr.design.mainframe.chart.gui.style.ChartAxisTitlePane;
import com.fr.design.mainframe.chart.gui.style.ChartAxisValueTypePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.van.chart.designer.component.format.FormatPaneWithOutFont;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.Component;

public class ChartCategoryPane extends ChartAxisUsePane<Axis>{
	
	private static final long serialVersionUID = 2095802666578050233L;

    protected ChartAxisTitlePane axisTitlePane;
    protected ChartAxisTitleNoFormulaPane axisTitleNoFormulaPane;

    private ChartAxisValueTypePane axisValuePane;
	private ChartAxisLineStylePane axisLineStylePane;
	private FormatPane formatPane;
	private ChartAxisLabelPane axisLabelPane;
	private UICheckBox axisReversed;
	
	private class ContentPane extends JPanel {
		private static final long serialVersionUID = 7058437226563300016L;

		public ContentPane(){
			initComponents();
		}
		
		private void initComponents() {
            axisTitlePane = new ChartAxisTitlePane();
            axisTitleNoFormulaPane = new ChartAxisTitleNoFormulaPane();

			axisValuePane = new ChartAxisValueTypePane();
			axisLineStylePane = new ChartAxisLineStylePane();
			formatPane = new FormatPaneWithOutFont();
			axisLabelPane = new ChartAxisLabelPane();
			axisReversed = new UICheckBox(Inter.getLocText("AxisReversed"));
            this.setLayout(new BorderLayout());
            this.add(isSupportLineStyle() ? getPaneWithLineStyle() : getPaneWithOutLineStyle(),BorderLayout.CENTER);
		}
	}

	private JPanel getPaneWithOutLineStyle(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		JPanel axisTypePane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Axis", "Type"}, new Component[][]{
				new Component[]{axisValuePane}}, new double[]{p}, new double[]{f});
		double[] columnSize = {f};
		double[] rowSize = { p, p, p, p, p, p, p, p, p, p};
		Component[][] components = new Component[][]{
  		new Component[]{axisTypePane},
  	    new Component[]{axisValuePane},
  	    new Component[]{new JSeparator()},
  	    new Component[]{getAxisTitlePane()},
  	    new Component[]{new JSeparator()},
  	    new Component[]{axisReversed},
  	    new Component[]{new JSeparator()},
  	    new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Data_Type"}, new Component[][]{
  	    										new Component[]{formatPane}}, new double[]{p}, new double[]{f})},
          new Component[]{new JSeparator()},
          new Component[]{axisLabelPane},
		};
		return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}

	private JPanel getPaneWithLineStyle(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		JPanel axisTypePane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Axis", "Type"}, new Component[][]{
				new Component[]{axisValuePane}}, new double[]{p}, new double[]{f});
		double[] columnSize = {f};
		double[] rowSize = { p, p, p, p, p, p, p, p, p, p, p, p};
		Component[][] components = new Component[][]{
  		new Component[]{axisTypePane},
  	    new Component[]{axisValuePane},
  	    new Component[]{new JSeparator()},
  	    new Component[]{getAxisTitlePane()},
  	    new Component[]{new JSeparator()},
  	    new Component[]{axisLineStylePane},
  	    new Component[]{new JSeparator()},
  	    new Component[]{axisReversed},
  	    new Component[]{new JSeparator()},
  	    new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Data_Type"}, new Component[][]{
  	    										new Component[]{formatPane}}, new double[]{p}, new double[]{f})},
          new Component[]{new JSeparator()},
          new Component[]{axisLabelPane},
		};
		return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}

    protected JPanel getAxisTitlePane(){
        return this.axisTitlePane;
    }

    protected void updateAxisTitle(Axis axis){
        this.axisTitlePane.update(axis);
    }

    protected void populateAxisTitle(Axis axis){
        this.axisTitlePane.populate(axis);
    }

	/**
	 * 弹出框的界面标题.
	 * @return 标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("ChartF-Category_Axis");
	}

	@Override
	protected JPanel createContentPane() {
		return new ContentPane();
	}
	
	@Override
	protected void layoutContentPane() {
		leftcontentPane = createContentPane();
		leftcontentPane.setBorder(BorderFactory.createMatteBorder(5, 10, 5, 0, original));
		this.add(leftcontentPane);
	}

	@Override
	public void updateBean(Axis axis, Plot plot) {
		updateBean(axis);
		
		if(plot.isSupportAxisReverse()){
			if(axis.hasAxisReversed()){
				if(plot instanceof Bar2DPlot && ((Bar2DPlot)plot).isHorizontal()){
					plot.getyAxis().setPosition(Constants.TOP);
				}else{
					plot.getyAxis().setPosition(Constants.RIGHT);
				}
			}else{
				if(plot instanceof Bar2DPlot && ((Bar2DPlot)plot).isHorizontal()){
					plot.getyAxis().setPosition(Constants.BOTTOM);
				}else{
					plot.getyAxis().setPosition(Constants.LEFT);
				}
			}
		}
	}
	
	public void updateBean(Axis axis) {
		if(axis == null) {
			axis = new CategoryAxis();
		}
		axisLineStylePane.update(axis);
		updateAxisTitle(axis);
		if(axis instanceof CategoryAxis) {
			CategoryAxis cateAxis = (CategoryAxis)axis;
			axisValuePane.updateBean(cateAxis);
			formatPane.setComboBoxModel(cateAxis.isDate());
		}
		
		axis.setAxisReversed(this.axisReversed.isSelected());
		axisLabelPane.update(axis);
		axis.setFormat(formatPane.update());
	}
	
	public void populateBean(Axis axis) {
		if(axis == null) {
			return;
		}
		
		axisLineStylePane.populate(axis);
		populateAxisTitle(axis);
		axisLabelPane.populate(axis);
		axisReversed.setSelected(axis.hasAxisReversed());
		
		if(axis instanceof CategoryAxis) {
			CategoryAxis cateAxis = (CategoryAxis)axis;
			axisValuePane.populateBean(cateAxis);
			formatPane.setComboBoxModel(cateAxis.isDate());
			formatPane.populateBean(cateAxis.getFormat());
		}
		
		GUICoreUtils.repaint(this);
	}
	
	public void populateBean(Axis axis, Plot plot){
		if(plot.isSupportAxisReverse()){
			populateBean(axis);
		}else{
			relayoutWithPlot(plot);
			populateBean(axis);
		}
	}
	
	//把轴逆序拿掉
	private void relayoutWithPlot(Plot plot){
		this.removeAll();
        JPanel pane = isSupportLineStyle() ? getPaneWithOutAxisRevert() : getPaneWithOutAxisRevertAndLineStyle();
		if(pane != null) {
			reloaPane(pane);
		}
	}

	private JPanel getPaneWithOutAxisRevertAndLineStyle(){
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		JPanel axisTypePane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Axis", "Type"}, new Component[][]{
				new Component[]{axisValuePane}}, new double[]{p}, new double[]{f});
		double[] columnSize = {f};
		double[] rowSize = { p, p, p, p, p, p, p, p};
        Component[][] components = new Component[][]{
        		new Component[]{axisTypePane},
        	    new Component[]{axisValuePane},
        	    new Component[]{new JSeparator()},
        	    new Component[]{getAxisTitlePane()},
        	    new Component[]{new JSeparator()},
        	    new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Data_Type"}, new Component[][]{
        	    										new Component[]{formatPane}}, new double[]{p}, new double[]{f})},
                new Component[]{new JSeparator()},
                new Component[]{axisLabelPane},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
	}

	private JPanel getPaneWithOutAxisRevert(){
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		JPanel axisTypePane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Axis", "Type"}, new Component[][]{
				new Component[]{axisValuePane}}, new double[]{p}, new double[]{f});
		double[] columnSize = {f};
		double[] rowSize = { p, p, p, p, p, p, p, p, p, p};
        Component[][] components = new Component[][]{
        		new Component[]{axisTypePane},
        	    new Component[]{axisValuePane},
        	    new Component[]{new JSeparator()},
        	    new Component[]{getAxisTitlePane()},
        	    new Component[]{new JSeparator()},
        	    new Component[]{axisLineStylePane},
        	    new Component[]{new JSeparator()},
        	    new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Data_Type"}, new Component[][]{
        	    										new Component[]{formatPane}}, new double[]{p}, new double[]{f})},
                new Component[]{new JSeparator()},
                new Component[]{axisLabelPane},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
	}
	
	
	public ChartAxisValueTypePane getAxisValueTypePane(){
		return this.axisValuePane;
	}

	protected boolean isSupportLineStyle(){
		return true;
	}

	
}