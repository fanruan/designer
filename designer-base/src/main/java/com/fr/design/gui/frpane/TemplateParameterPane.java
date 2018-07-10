package com.fr.design.gui.frpane;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.base.Parameter;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.general.Inter;

/**
 * 模板的参数添加面板
 * @since 6.5.4
 */
public class TemplateParameterPane extends BasicPane {
    private UITextField nameTextField;
    private ValueEditorPane valuePane;
    
    public TemplateParameterPane() {
        this.initComponents();
    }

    public void initComponents() {
        this.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Parameter")));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(northPane, BorderLayout.NORTH);
//        northPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel defaultPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        northPane.add(defaultPane, BorderLayout.CENTER);
        UILabel nameLabel = new UILabel(Inter.getLocText("Name") + ":");
        UILabel valueLabel = new UILabel(Inter.getLocText("Value") + ":");

        nameTextField = new UITextField(12);       
        valuePane = ValueEditorPaneFactory.createBasicValueEditorPane();
        double i = TableLayout.PREFERRED;
        double j = TableLayout.FILL;
        double[] column = {i, j};
        double[] row = {i, i};
        Component[][] coms = {{nameLabel, nameTextField}, {valueLabel, valuePane}};
        JPanel centerPane = TableLayoutHelper.createTableLayoutPane(coms, row, column);
        defaultPane.add(centerPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("Parameter");
    }

    public void populate(Parameter parameter) {
        if (parameter == null) {
            return;
        }

        nameTextField.setText(parameter.getName());
        valuePane.populate(parameter.getValue());
    }

    public Parameter update() {
        Parameter parameter = new Parameter();

        parameter.setName(this.nameTextField.getText());
        parameter.setValue(valuePane.update());
        return parameter;
    }
    
    /**
     * Check valid.
     */
    @Override
    public void checkValid() throws Exception {
        Parameter parameter = this.update();
        if (parameter.getName() == null || parameter.getName().trim().length() <= 0) {
            throw new Exception(Inter.getLocText("ParameterD-Parameter_name_cannot_be_null") + ".");
        }
        this.valuePane.checkValid();
    }
}