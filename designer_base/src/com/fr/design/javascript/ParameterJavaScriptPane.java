package com.fr.design.javascript;

import com.fr.base.Parameter;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.js.ParameterJavaScript;
import com.fr.stable.ParameterProvider;

import java.awt.*;
import java.util.List;

public class ParameterJavaScriptPane extends BasicBeanPane<ParameterJavaScript> {
    private UITextField itemNameTextField;
	private ReportletParameterViewPane parameterViewPane;
	
	public ParameterJavaScriptPane(){
		this.setLayout(new  BorderLayout());		
		parameterViewPane = new ReportletParameterViewPane(getChartParaType(), getValueEditorPane(), getValueEditorPane());
		this.add(parameterViewPane, BorderLayout.CENTER);
        if(needRenamePane()){
            itemNameTextField = new UITextField();
            this.add(GUICoreUtils.createNamedPane(itemNameTextField, Inter.getLocText("Name") + ":"), BorderLayout.NORTH);
        }
	}
	
	protected int getChartParaType() {
		return ParameterTableModel.NO_CHART_USE;
	}

    protected ValueEditorPane getValueEditorPane() {
        return ValueEditorPaneFactory.createVallueEditorPaneWithUseType(getChartParaType());
    }

    protected boolean needRenamePane(){
        return getChartParaType() != ParameterTableModel.NO_CHART_USE;
    }

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("JavaScript-Dynamic_Parameters");
	}

    @Override
	public void populateBean(ParameterJavaScript ob){
		ParameterProvider[] parameters = ob.getParameters();
		if (parameters.length == 0){
	    	// TODO ALEX_SEP
//			parameters = DesignUtils.getEditingTemplateReport().getTemplateWorkBook().getParameters();
		}
		parameterViewPane.populate(parameters);
        if(itemNameTextField != null){
            itemNameTextField.setText(ob.getItemName());
        }
	}

    @Override
	public ParameterJavaScript updateBean(){
		ParameterJavaScript js = new ParameterJavaScript();
		
		updateBean(js);
		if(this.itemNameTextField != null){
            js.setItemName(itemNameTextField.getText());
        }
		return js;
	}
    
    public void updateBean(ParameterJavaScript parameter) {
    	List<ParameterProvider> parameterList = parameterViewPane.update();
    	parameter.setParameters(parameterList.toArray(new Parameter[parameterList.size()]));
        if(this.itemNameTextField != null){
            parameter.setItemName(itemNameTextField.getText());
        }
    }

    public static class CHART_NO_RENAME extends ParameterJavaScriptPane{
        protected int getChartParaType() {
            return ParameterTableModel.CHART_NORMAL_USE;
        }
        protected boolean needRenamePane(){
            return false;
        }
    }

    public static class CHART extends ParameterJavaScriptPane {
    	@Override
    	protected int getChartParaType() {
    		return ParameterTableModel.CHART_NORMAL_USE;
    	}
    }
    
    public static class CHART_MAP extends ParameterJavaScriptPane {
    	protected int getChartParaType() {
    		return ParameterTableModel.CHART_MAP_USE;
    	}
    }
    
    public static class CHART_GIS extends ParameterJavaScriptPane{
		protected int getChartParaType() {
			return ParameterTableModel.CHART_GIS_USE;
		}
	}
    
    public static class CHART_PIE extends ParameterJavaScriptPane {
    	@Override
    	protected int getChartParaType() {
    		return ParameterTableModel.CHART_PIE_USE;
    	}
    };

    public static class CHART_XY extends ParameterJavaScriptPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART__XY_USE;
        }
    }

    public static class CHART_BUBBLE extends ParameterJavaScriptPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_BUBBLE_USE;
        }
    }

    public static class CHART_STOCK extends  ParameterJavaScriptPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_STOCK_USE;
        }
    }

    public static class CHART_GANTT extends  ParameterJavaScriptPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_GANTT_USE;
        }
    }

    public static class CHART_METER extends  ParameterJavaScriptPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_METER_USE;
        }
    }
	
}