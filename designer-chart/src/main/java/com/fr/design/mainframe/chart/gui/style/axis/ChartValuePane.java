package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.chart.base.AxisUnitType;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.NumberAxis;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.ChartSwingUtils;
import com.fr.design.chart.axis.MinMaxValuePane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartAxisLabelPane;
import com.fr.design.mainframe.chart.gui.style.ChartAxisLineStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartAxisTitleNoFormulaPane;
import com.fr.design.mainframe.chart.gui.style.ChartAxisTitlePane;
import com.fr.general.ComparatorUtils;

import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import com.fr.van.chart.designer.component.format.FormatPaneWithOutFont;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 值轴警戒线界面, 属性表, 图表样式.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-5 上午01:13:25
 */
public class ChartValuePane extends ChartAxisUsePane<Axis>{
	
	private static final long serialVersionUID = 7384519245136736252L;

    protected ChartAxisTitlePane axisTitlePane;
    protected ChartAxisTitleNoFormulaPane axisTitleNoFormulaPane;

	private ChartAxisLineStylePane axisLineStylePane;
	private UIComboBox unitCombox;
	private ChartAxisLabelPane axisLabelPane;
	private FormatPane formatPane;
	private UICheckBox axisReversed;
	
	protected MinMaxValuePane minValuePane;
	
    private UICheckBox logBox;
    private UITextField logBaseField;

    private JPanel dataPane;
    private JPanel zeroPane;

    @Override
    protected void layoutContentPane() {
        leftcontentPane = createContentPane();
        leftcontentPane.setBorder(BorderFactory.createMatteBorder(5, 10, 5, 0, original));
        this.add(leftcontentPane);
    }

    protected JPanel createContentPane() {
        axisTitlePane = new ChartAxisTitlePane();
        axisTitleNoFormulaPane = new ChartAxisTitleNoFormulaPane();

        axisLineStylePane = new ChartAxisLineStylePane();
        zeroPane = aliagnZero4Second();
        axisReversed = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_AxisReversed"));
        unitCombox = new UIComboBox(AxisUnitType.getI18NValues());
        formatPane = new FormatPaneWithOutFont();
        axisLabelPane = new ChartAxisLabelPane();
        dataPane = createDataDefinePane();

        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.add(getComponentPane(), BorderLayout.CENTER);

        return pane;
    }
	
	private JPanel getComponentPane(){
		return isSupportLineStyle() ? getPaneWithLineStyle() : getPaneWithOutLineStyle();
	}


	private JPanel getPaneWithLineStyle(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { f };
		double[] rowSize = { p, p, p, p, p, p, p, p, p, p, p, p, p, p, p,p};

        Component[][] components = new Component[][]{
        		new Component[]{getAxisTitlePane()},
                new Component[]{new JSeparator()},
                new Component[]{axisLineStylePane},
                new Component[]{zeroPane},
                new Component[]{new JSeparator()},
                new Component[]{axisReversed},
                new Component[]{new JSeparator()},
                new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Fine-Design_Chart_Units"}, new Component[][]{
						new Component[]{unitCombox}}, new double[]{p}, new double[]{f})},
				new Component[]{new JSeparator()},
                new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Fine-Design_Chart_DataType"}, new Component[][]{
						new Component[]{formatPane}}, new double[]{p}, new double[]{f})},
				new Component[]{new JSeparator()},
				new Component[]{axisLabelPane},
				new Component[]{new JSeparator()},
				new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Value_Define"))},
                new Component[]{dataPane},
        } ;

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}

	private JPanel getPaneWithOutLineStyle(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { f };
		double[] rowSize = { p, p, p, p, p, p, p, p, p, p, p, p, p};

        Component[][] components = new Component[][]{
        		new Component[]{getAxisTitlePane()},
                new Component[]{new JSeparator()},
                new Component[]{axisReversed},
                new Component[]{new JSeparator()},
                new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Fine-Design_Chart_Units"}, new Component[][]{
						new Component[]{unitCombox}}, new double[]{p}, new double[]{f})},
				new Component[]{new JSeparator()},
                new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Fine-Design_Chart_DataType"}, new Component[][]{
						new Component[]{formatPane}}, new double[]{p}, new double[]{f})},
				new Component[]{new JSeparator()},
				new Component[]{axisLabelPane},
				new Component[]{new JSeparator()},
				new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Value_Define"))},
                new Component[]{dataPane},
        } ;

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}


	//构造数据定义界面
	private JPanel createDataDefinePane(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { LayoutConstants.CHART_ATTR_TOMARGIN,p,f};
		double[] rowSize = { p, p};
		Component[][] component = new Component[][]{
				new Component[]{null,initMinMaxValue(),null},
				new Component[]{null, addLogarithmicPane2ValuePane(), addLogText()},
		};
		return TableLayoutHelper.createTableLayoutPane(component, rowSize, columnSize);
	}

    // 返回最大最小值界面. 雷达轴 有切换按钮.
    protected JPanel initMinMaxValue() {
        if(minValuePane == null) {
            minValuePane = new MinMaxValuePane();
        }

        return minValuePane;
    }
	
	// 返回对数相关界面. 百分比 没有此界面.
	protected JPanel addLogarithmicPane2ValuePane() {
        JPanel labelLogPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        labelLogPane.add(logBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Logarithmic")+":"));
        labelLogPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Log_Base")));

        logBaseField = new UITextField(4);
        logBaseField.setText("10");
        logBaseField.setPreferredSize(new Dimension(55, 20));

        logBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	checkLogUse();
            }
        });

        ChartSwingUtils.addListener(logBox, logBaseField);

        return labelLogPane;
    }
	
    private JPanel addLogText() {
    	  JPanel labelLogPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
    	  
    	  if(logBaseField != null) {
    		  labelLogPane.add(logBaseField);
    	  }

          return labelLogPane;
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
    
    protected JPanel aliagnZero4Second() {// 添加 0值对齐 
    	return null;
    }
    
  
	/**
	 * 界面标题.
     * @return  返回标题.
	 */
	public String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Radar_Value_Axis");
	}
	
	@Override
	public void updateBean(Axis axis, Plot plot) {
		updateBean(axis);
		
		if(plot.isSupportAxisReverse()){
			if(axis.hasAxisReversed()){
				if(plot instanceof Bar2DPlot && ((Bar2DPlot)plot).isHorizontal()){
					plot.getxAxis().setPosition(Constants.RIGHT);
				}else{
					plot.getxAxis().setPosition(Constants.TOP);
				}
			}else{
				if(plot instanceof Bar2DPlot && ((Bar2DPlot)plot).isHorizontal()){
					plot.getxAxis().setPosition(Constants.LEFT);
				}else{
					plot.getxAxis().setPosition(Constants.BOTTOM);
				}
			}
		}
	}
    
	public void updateBean(Axis axis) {
		if(axis instanceof NumberAxis) {
			NumberAxis numberAxis = (NumberAxis)axis;
			
			axisLineStylePane.update(axis);
			axis.setAxisReversed(this.axisReversed.isSelected());
			String unitValue = Utils.objectToString(unitCombox.getSelectedItem());
			if(ComparatorUtils.equals(unitValue, AxisUnitType.UNIT_NONE.toLocaleString())) {
				unitValue = null;
			}
			numberAxis.setShowUnit(AxisUnitType.parse(unitValue));
            if(numberAxis.isSurpportAxisTitle()) {
                updateAxisTitle(numberAxis);
            }

            axisLabelPane.update(axis);
			axis.setFormat(formatPane.update());
			
			if(minValuePane != null) {
				minValuePane.update(axis);
			}
			
			updateLog(numberAxis);
		}
	}
	
	private void updateLog(NumberAxis numberAxis) {
		if (logBaseField != null && logBox.isSelected()) {
			String increment = logBaseField.getText();
			if (StringUtils.isEmpty(increment)) {
				numberAxis.setLog(false);
				numberAxis.setLogBase(null);
			} else {
				numberAxis.setLog(true);
				BaseFormula formula = BaseFormula.createFormulaBuilder().build(increment);
				Number number = ChartBaseUtils.formula2Number(formula);
				// 界面处理防止 遇到 对数增量为小于1的值.
				if (number != null && number.doubleValue() <= 1.0) {
					numberAxis.setLogBase(BaseFormula.createFormulaBuilder().build("2"));
				} else {
					numberAxis.setLogBase(formula);
				}
			}
		} else {
			numberAxis.setLog(false);
		}
	}
	
	public void populateBean(Axis axis, Plot plot){
		if(!plot.isSupportAxisReverse()){
            relayoutNoAxisRevser();
		}
        populateBean(axis);
	}

	// 把axisrevserd去掉
	private void relayoutNoAxisRevser(){
		this.removeAll();
		JPanel pane = isSupportLineStyle() ? getPaneWithOutAxisRevert() : getPaneWithOutAxisRevertAndLineStyle() ;
		if(pane != null) {
			reloaPane(pane);
		}
	}


	private JPanel getPaneWithOutAxisRevert(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { f };
		double[] rowSize = { p, p, p, p, p, p, p, p, p, p, p, p, p, p, p};

        Component[][] components = new Component[][]{
        		new Component[]{getAxisTitlePane()},
                new Component[]{new JSeparator()},
                new Component[]{axisLineStylePane},
                new Component[]{zeroPane},
                new Component[]{new JSeparator()},
                new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Fine-Design_Chart_Units"}, new Component[][]{
						new Component[]{unitCombox}}, new double[]{p}, new double[]{f})},
				new Component[]{new JSeparator()},
                new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Fine-Design_Chart_DataType"}, new Component[][]{
						new Component[]{formatPane}}, new double[]{p}, new double[]{f})},
				new Component[]{new JSeparator()},
				new Component[]{axisLabelPane},
				new Component[]{new JSeparator()},
				new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Value_Define"))},
                new Component[]{dataPane},

        } ;

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
	}

	private JPanel getPaneWithOutAxisRevertAndLineStyle(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { f };
		double[] rowSize = { p, p, p, p, p, p, p, p, p, p, p, p};

        Component[][] components = new Component[][]{
        		new Component[]{getAxisTitlePane()},
                new Component[]{new JSeparator()},
                new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Fine-Design_Chart_Units"}, new Component[][]{
						new Component[]{unitCombox}}, new double[]{p}, new double[]{f})},
				new Component[]{new JSeparator()},
                new Component[]{TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Fine-Design_Chart_DataType"}, new Component[][]{
						new Component[]{formatPane}}, new double[]{p}, new double[]{f})},
				new Component[]{new JSeparator()},
				new Component[]{axisLabelPane},
				new Component[]{new JSeparator()},
				new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Value_Define"))},
                new Component[]{dataPane},

        } ;

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
	}




    @Override
    public void populateBean(Axis axis) {
        if(axis instanceof NumberAxis) {
            NumberAxis numberAxis = (NumberAxis)axis;

            axisLineStylePane.populate(axis);
            axisReversed.setSelected(axis.hasAxisReversed());
			String unitKey;
            if(numberAxis.getShowUnit() != null) {
            	unitKey = numberAxis.getShowUnit().getStringType();
			}else{
                unitKey = AxisUnitType.UNIT_NONE.getStringType();
            }
            unitCombox.setSelectedItem(AxisUnitType.parse(unitKey).toLocaleString());

            if(numberAxis.isSurpportAxisTitle()) {
                populateAxisTitle(axis);
            }

            axisLabelPane.populate(axis);
            formatPane.populateBean(axis.getFormat());
            if(minValuePane != null) {
                minValuePane.populate(axis);
            }

            if(logBox != null && logBaseField != null) {
                logBox.setSelected(numberAxis.isLog());
                if (numberAxis.getLogBase() != null) {
                    logBaseField.setText(numberAxis.getLogBase().toString());
                }
            }

        }

        checkLogUse();
    }

    private void checkLogUse() {
        if(logBaseField != null && logBox != null) {
            logBaseField.setEnabled(logBox.isSelected());
        }
    }

	protected boolean isSupportLineStyle(){
	   return true;
	}
}
