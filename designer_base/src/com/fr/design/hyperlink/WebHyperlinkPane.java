package com.fr.design.hyperlink;

import com.fr.base.Parameter;
import com.fr.base.chart.BasePlot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.js.WebHyperlink;
import com.fr.stable.ParameterProvider;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WebHyperlinkPane extends BasicBeanPane<WebHyperlink> {
	private BasePlot plot;
	private WebHyperNorthPane northPane;
	
	private ReportletParameterViewPane parameterViewPane;
    
    private UICheckBox useCJKCheckBox;
    
    private UICheckBox extendParametersCheckBox;

	protected BasePlot getPlot() {
		return plot;
	}

	public WebHyperlinkPane() {
		super();
		this.initComponents();
	}
    
	public WebHyperlinkPane(BasePlot plot) {
		super();
		this.plot = plot;
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		northPane = new WebHyperNorthPane(needRenamePane());
		this.add(northPane, BorderLayout.NORTH);
		
		parameterViewPane = new ReportletParameterViewPane(getChartParaType(), getValueEditorPane(), getValueEditorPane());
		this.add(parameterViewPane, BorderLayout.CENTER);
		parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Parameters"), null));
		
		useCJKCheckBox = new UICheckBox(Inter.getLocText("Hyperlink-Use_CJK_to_encode_parameter"));
        extendParametersCheckBox = new UICheckBox(Inter.getLocText("Hyperlink-Extends_Report_Parameters"));
        this.add(GUICoreUtils.createFlowPane(new Component[] {useCJKCheckBox, extendParametersCheckBox}, FlowLayout.LEFT), BorderLayout.SOUTH);
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Hyperlink-Web_link");
	}
	
	protected int getChartParaType() {
		return plot != null ? ParameterTableModel.CHART_NORMAL_USE : ParameterTableModel.NO_CHART_USE;
	}

	protected ValueEditorPane getValueEditorPane() {
		return ValueEditorPaneFactory.createVallueEditorPaneWithUseType(getChartParaType(), plot);
	}

    protected boolean needRenamePane(){
        return plot != null && plot.isNeedRenameHyperLinkPane();
    }

    @Override
	public void populateBean(WebHyperlink ob) {
		northPane.populateBean(ob);
		//parameter
		List<ParameterProvider> parameterList = this.parameterViewPane.update();
		parameterList.clear();

		ParameterProvider[] parameters = ob.getParameters();
		parameterViewPane.populate(parameters);
		useCJKCheckBox.setSelected(ob.isUseCJK());
		extendParametersCheckBox.setSelected(ob.isExtendParameters());
	}

    @Override
	public WebHyperlink updateBean() {
		WebHyperlink webHyperlink = new WebHyperlink();

		updateBean(webHyperlink);
		
		return webHyperlink;
	}
    
    public void updateBean(WebHyperlink webHyperlink) {
    	northPane.updateBean(webHyperlink);
    	//Parameter.
		List<ParameterProvider> parameterList = this.parameterViewPane.update();
		if (!parameterList.isEmpty()) {
			Parameter[] parameters = new Parameter[parameterList.size()];
			parameterList.toArray(parameters);

			webHyperlink.setParameters(parameters);
		} else {
			webHyperlink.setParameters(null);
		}
		webHyperlink.setUseCJK(this.useCJKCheckBox.isSelected());
		webHyperlink.setExtendParameters(this.extendParametersCheckBox.isSelected());
    }

    public static class CHART_NO_RENAME extends WebHyperlinkPane{
        protected boolean needRenamePane(){
            return false;
        }
        protected int getChartParaType() {
            return ParameterTableModel.CHART_NORMAL_USE;
        }
    }

	public WebHyperNorthPane getNorthPane() {
		return northPane;
	}

	public void setNorthPane(WebHyperNorthPane northPane) {
		this.northPane = northPane;
	}

	public ReportletParameterViewPane getParameterViewPane() {
		return parameterViewPane;
	}

	public void setParameterViewPane(ReportletParameterViewPane parameterViewPane) {
		this.parameterViewPane = parameterViewPane;
	}

	public UICheckBox getUseCJKCheckBox() {
		return useCJKCheckBox;
	}

	public void setUseCJKCheckBox(UICheckBox useCJKCheckBox) {
		this.useCJKCheckBox = useCJKCheckBox;
	}

	public UICheckBox getExtendParametersCheckBox() {
		return extendParametersCheckBox;
	}

	public void setExtendParametersCheckBox(UICheckBox extendParametersCheckBox) {
		this.extendParametersCheckBox = extendParametersCheckBox;
	}
}