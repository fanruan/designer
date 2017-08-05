package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.CheckBox;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class CheckBoxDefinePane extends AbstractDataModify<CheckBox> {
	private UITextField text;
	private UITextField fontSizePane;

	public CheckBoxDefinePane(XCreator xCreator) {
		super(xCreator);
		iniComoponents();
	}
	
	private void iniComoponents() {
		text = new UITextField();
		fontSizePane = new UITextField();
		FormWidgetValuePane formWidgetValuePane = new FormWidgetValuePane();
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_Text")),  text },
				new Component[]{new UILabel(Inter.getLocText("FR-Designer-Estate_Widget_Value")),  formWidgetValuePane },
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font-Size")), fontSizePane},
		};
		double[] rowSize = {p, p, p, p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 3},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, panel);

		this.add(uiExpandablePane);
	}
	
	@Override
	public String title4PopupWindow() {
		return "CheckBox";
	}
	
	@Override
	public void populateBean(CheckBox check) {
//		text.setText(check.getText());
	}

	@Override
	public CheckBox updateBean() {
		CheckBox box = new CheckBox();
		box.setText(text.getText());
		return box;
	}
}