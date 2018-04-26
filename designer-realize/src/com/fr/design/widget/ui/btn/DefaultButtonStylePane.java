package com.fr.design.widget.ui.btn;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.IconDefinePane;
import com.fr.form.ui.Button;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class DefaultButtonStylePane extends BasicPane {
	
	private UITextField buttonNameTextField;
	private IconDefinePane iconPane;
	private UIComboBox buttonStyleComboBox;
	
	public DefaultButtonStylePane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(new BorderLayout());
		
		JPanel labelPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		iconPane = new IconDefinePane();
	    labelPane.add(iconPane);
		Component[][] n_components = {
				{ new UILabel(Inter.getLocText("Text") + ":"), buttonNameTextField = new UITextField(20) },
				{ new UILabel(Inter.getLocText("Icon") + ":"), labelPane } };
		JPanel panel = TableLayoutHelper.createTableLayoutPane(n_components, new double[]{-2, -2}, new double[]{-2, -2});
		
		this.add(panel,BorderLayout.CENTER);
	}
	
	public void populate(Button button) {
		buttonNameTextField.setText(button.getText());
		iconPane.populate(button.getIconName());
	}

	public Button update(Button button) {
		button.setText(buttonNameTextField.getText());
		button.setIconName(iconPane.update());
		return button;
	}
	
	@Override
	protected String title4PopupWindow() {
		return "default";
	}

}