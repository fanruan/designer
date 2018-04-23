package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.Parameter;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 图表弹出超链, 悬浮窗属性设置界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-8-6 上午09:39:22
 */
public class ChartHyperPopAttrPane extends AbstractChartAttrPane {
	
	private UITextField titleField;
	private UINumberField widthField;
	private UINumberField heightField;
	private ReportletParameterViewPane parameterViewPane;

    public static final int DEFAULT_H_VALUE = 270;
    public static final int DEFAULT_V_VALUE = 500;
	
	private int paraType;
	private ValueEditorPane valueEditorPane;
	private ValueEditorPane valueRenderPane;
	
	public ChartHyperPopAttrPane(int paraType, ValueEditorPane valueEditorPane, ValueEditorPane valueRenderPane) {
		this.paraType = paraType;
		this.valueEditorPane = valueEditorPane;
		this.valueRenderPane = valueRenderPane;
		
		initAll();
	}
	
	@Override
	protected JPanel createContentPane() {
		JPanel pane = new JPanel();
		pane.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		titleField = new UITextField(15);
		titleField.setPreferredSize(new Dimension(200, 20));
		
		widthField = new UINumberField(4);
		widthField.setColumns(10);
		widthField.setPreferredSize(new Dimension(200, 20));
		
		heightField = new UINumberField(4);
		heightField.setColumns(10);
		heightField.setPreferredSize(new Dimension(200, 20));
		
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		
		double[] columnSize = { p,f};
		double[] rowSize = { p,p,p,p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Designer-Widget-Style_Title") + ":", SwingConstants.RIGHT), titleField},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Designer_Width") + ":", SwingConstants.RIGHT), widthField},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Designer_Height") + ":", SwingConstants.RIGHT), heightField},
        };

        widthField.setText(String.valueOf(DEFAULT_V_VALUE));
        heightField.setText(String.valueOf(DEFAULT_H_VALUE));
        
		JPanel northPane = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
		pane.add(northPane, BorderLayout.NORTH);
		
		parameterViewPane = new ReportletParameterViewPane(paraType, valueEditorPane, valueRenderPane);
		parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Parameter")));
		parameterViewPane.setPreferredSize(new Dimension(200, 200));
		pane.add(parameterViewPane, BorderLayout.CENTER);

		return pane;
	}
	
	public void populateBean(ChartHyperPoplink chartHyperlink) {
		
		titleField.setText(chartHyperlink.getChartDigTitle());
		widthField.setText(String.valueOf(chartHyperlink.getWidth()));
		heightField.setText(String.valueOf(chartHyperlink.getHeight()));
		
		List parameterList = this.parameterViewPane.update();
		parameterList.clear();

		ParameterProvider[] parameters = chartHyperlink.getParameters();
		parameterViewPane.populate(parameters);
	}

	/**
	 * 属性表 对应update
	 */
	public void updateBean(ChartHyperPoplink chartHyperlink) {
		String title = titleField.getText();
		if (StringUtils.isBlank(title)) {
			title = "Chart";
		}
		chartHyperlink.setChartDigTitle(title);
		chartHyperlink.setWidth((int)widthField.getValue());
		chartHyperlink.setHeight((int)heightField.getValue());
		
		List<ParameterProvider> parameterList = this.parameterViewPane.update();
		if (!parameterList.isEmpty()) {
			Parameter[] parameters = new Parameter[parameterList.size()];
			parameterList.toArray(parameters);

			chartHyperlink.setParameters(parameters);
		} else {
			chartHyperlink.setParameters(null);
		}
	}

	@Override
	public void populate(ChartCollection collection) {
		// do nothing
	}

	@Override
	public void update(ChartCollection collection) {
		// do nothing
	}

	@Override
	public String getIconPath() {
		return "com/fr/design/images/chart/link.png";
	}


    /**
     * 界面标题
     * @return 标题
     */
	public String title4PopupWindow() {
		return Inter.getLocText("Plugin-ChartF_Hyperlink");
	}

}