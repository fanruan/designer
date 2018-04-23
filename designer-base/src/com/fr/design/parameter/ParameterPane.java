package com.fr.design.parameter;

import java.awt.BorderLayout;
import java.awt.Component;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.base.Parameter;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.general.Inter;

public class ParameterPane extends BasicBeanPane<Parameter> {
	/*
	 * richer:参数定义面板
	 * ——————6.5中，参数面板做出较大改动，取消了在参数定义的地方设置编辑器的做法
	 * ——————默认参数界面也自动生成，并且能给用户看
	 * ——————参数面板的大小尽量使用(600, 350),可以统一
	 */

	// 参数名字
	private UITextField nameTextField;

	// 参数默认值
	private ValueEditorPane valueEditor;

	public ParameterPane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		nameTextField = new UITextField(10);
		nameTextField.setEditable(false);
		
		JPanel textFieldPanel=FRGUIPaneFactory.createBorderLayout_S_Pane();
		textFieldPanel.add(nameTextField,BorderLayout.CENTER);
		
		valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane();

		// richer:要排列显示的控件
		Component[][] components = {{null},
				{ null, new UILabel(Inter.getLocText("Name") + ":"),textFieldPanel },
				{ null, new UILabel(Inter.getLocText("Utils-Default_Value") + ":"),valueEditor }
				};
		double p =TableLayout.PREFERRED;
		double f =TableLayout.FILL;
		double[] rowSize = {p, p, p, p};
		double[] columnSize = {p, p, f, p, p};

		JPanel centerPane = TableLayoutHelper.createGapTableLayoutPane(
				components, rowSize, columnSize, 20, 10);
		this.add(centerPane, BorderLayout.CENTER);
	}
	
	public void setNameEditable(boolean bool) {
		this.nameTextField.setEditable(true);
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Parameter");
	}

	@Override
	public void populateBean(Parameter parameter) {
		if (parameter == null) {
			return;
		}
		
		this.nameTextField.setText(parameter.getName());
		this.valueEditor.populate(parameter.getValue());
	}

	@Override
	public Parameter updateBean() {
		Parameter parameter = new Parameter();
		parameter.setName(this.nameTextField.getText());
    	parameter.setValue(valueEditor.update());
		return parameter;
	}
	
	public void checkValid() throws Exception{
		this.valueEditor.checkValid();
	}
}