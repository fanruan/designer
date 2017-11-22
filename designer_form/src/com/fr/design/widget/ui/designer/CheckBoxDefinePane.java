package com.fr.design.widget.ui.designer;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.component.FontSizeComboPane;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.CheckBox;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Component;

public class CheckBoxDefinePane extends AbstractDataModify<CheckBox> {
	private UITextField text;
	private FontSizeComboPane fontSizePane;
	private FormWidgetValuePane formWidgetValuePane;
	protected UITextField labelNameTextField;

	public CheckBoxDefinePane(XCreator xCreator) {
		super(xCreator);
		iniComoponents();
	}
	
	private void iniComoponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		text = new UITextField();
		fontSizePane = new FontSizeComboPane();
		labelNameTextField = new UITextField();
		UILabel widgetValueLabel = new UILabel(Inter.getLocText("FR-Designer-Estate_Widget_Value"));
		widgetValueLabel.setVerticalAlignment(SwingConstants.TOP);
		formWidgetValuePane = new FormWidgetValuePane(creator.toData(), false);
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_Label_Name")), labelNameTextField},
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_Text")),  text },
				new Component[]{widgetValueLabel,  formWidgetValuePane },
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font-Size")), fontSizePane},
		};
		double[] rowSize = {p, p, p, p, p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 1}, {1, 3},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
		JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		boundsPane.add(panel);
		UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, boundsPane);

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
		labelNameTextField.setText(check.getLabelName());
	}

	@Override
	public CheckBox updateBean() {
		CheckBox box = (CheckBox)creator.toData();
		box.setText(text.getText());
		box.setFontSize(fontSizePane.getValue());
		formWidgetValuePane.update(box);
		box.setLabelName(labelNameTextField.getText());
		return box;
	}
}