package com.fr.design.javascript;

import com.fr.base.Parameter;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.hyperlink.AbstractHyperLinkPane;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.js.ParameterJavaScript;
import com.fr.stable.ParameterProvider;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class ParameterJavaScriptPane extends AbstractHyperLinkPane<ParameterJavaScript> {
    private UITextField itemNameTextField;


    public ParameterJavaScriptPane() {
        this(null, false);
    }

    public ParameterJavaScriptPane(HashMap hyperLinkEditorMap, boolean needRenamePane) {
        super(hyperLinkEditorMap, needRenamePane);
        this.setLayout(new BorderLayout());
        parameterViewPane = new ReportletParameterViewPane(getChartParaType(), getValueEditorPane(), getValueEditorPane());
        this.add(parameterViewPane, BorderLayout.CENTER);
        if (needRenamePane()) {
            itemNameTextField = new UITextField();
            this.add(GUICoreUtils.createNamedPane(itemNameTextField, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Name") + ":"), BorderLayout.NORTH);
        }
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_JavaScript_Dynamic_Parameters");
    }

    @Override
    public void populateBean(ParameterJavaScript ob) {
        ParameterProvider[] parameters = ob.getParameters();
        if (parameters.length == 0) {
            // TODO ALEX_SEP
//			parameters = DesignUtils.getEditingTemplateReport().getTemplateWorkBook().getParameters();
        }
        parameterViewPane.populate(parameters);
        if (itemNameTextField != null) {
            itemNameTextField.setText(ob.getItemName());
        }
    }

    @Override
    public ParameterJavaScript updateBean() {
        ParameterJavaScript js = new ParameterJavaScript();

        updateBean(js);
        if (this.itemNameTextField != null) {
            js.setItemName(itemNameTextField.getText());
        }
        return js;
    }

    public void updateBean(ParameterJavaScript parameter) {
        List<ParameterProvider> parameterList = parameterViewPane.update();
        parameter.setParameters(parameterList.toArray(new Parameter[parameterList.size()]));
        if (this.itemNameTextField != null) {
            parameter.setItemName(itemNameTextField.getText());
        }
    }

    public static class ChartNoRename extends ParameterJavaScriptPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_NORMAL_USE;
        }

        protected boolean needRenamePane() {
            return false;
        }
    }
}
