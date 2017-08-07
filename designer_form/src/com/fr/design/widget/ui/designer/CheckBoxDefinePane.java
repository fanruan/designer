package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.CheckBox;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class CheckBoxDefinePane extends AbstractDataModify<CheckBox> {
	private UITextField text;
	private UISpinner fontSizePane;
	private FormWidgetValuePane formWidgetValuePane;

	public CheckBoxDefinePane(XCreator xCreator) {
		super(xCreator);
		iniComoponents();
	}
	
	private void iniComoponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		text = new UITextField();
		fontSizePane = new UISpinner(0, 20, 1, 0);
		formWidgetValuePane = new FormWidgetValuePane(creator.toData(), false);
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
		text.setText(check.getText());
		fontSizePane.setValue(check.getFontSize());
		formWidgetValuePane.populate(check);
	}

	@Override
	public CheckBox updateBean() {
		CheckBox box = (CheckBox)creator.toData();
		box.setText(text.getText());
		box.setFontSize((int)fontSizePane.getValue());
		formWidgetValuePane.update(box);
		return box;
	}
}