package com.fr.design.widget.ui;

import java.awt.*;
import java.util.List;

import javax.swing.BorderFactory;

import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.IframeEditor;
import com.fr.general.Inter;
import com.fr.stable.ParameterProvider;
import com.fr.design.widget.DataModify;

public class IframeEditorDefinePane extends AbstractDataModify<IframeEditor> {
	private UITextField srcTextField;
	private ReportletParameterViewPane parameterViewPane;
	private UICheckBox horizontalCheck;
	private UICheckBox verticalCheck;

	public IframeEditorDefinePane() {
		this.initComponents();
	}
	
	private void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
		JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
		JPanel attr = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		attr.add(horizontalCheck = new UICheckBox(Inter.getLocText("Preference-Horizontal_Scroll_Bar_Visible")));
		attr.add(verticalCheck = new UICheckBox(Inter.getLocText("Preference-Vertical_Scroll_Bar_Visible")));
		contentPane.add(attr);
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] rowSize = { p, TableLayout.PREFERRED };
		double[] columnSize = { p, f };

		java.awt.Component[][] coms = {
				{ new UILabel(Inter.getLocText("Form-Url") + ":"), srcTextField = new UITextField() },
				{ new UILabel(Inter.getLocText("Parameter") + ":"), parameterViewPane = new ReportletParameterViewPane() } };
		parameterViewPane.setPreferredSize(new Dimension(400, 256));
		JPanel centerPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
		centerPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		centerPane.add(GUICoreUtils.createFlowPane(new UILabel(Inter.getLocText("Form-Url") + ":"), srcTextField = new UITextField(16), FlowLayout.LEFT));
		centerPane.add(GUICoreUtils.createFlowPane( new UILabel(Inter.getLocText("Parameter") + ":"), parameterViewPane = new ReportletParameterViewPane(), FlowLayout.LEFT));
		parameterViewPane.setPreferredSize(new Dimension(540, 235));
		JPanel advancedPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Advanced"));
		advancedPane.add(contentPane);
		contentPane.add(centerPane);
		this.add(advancedPane, BorderLayout.CENTER);
	}


	@Override
	protected String title4PopupWindow() {
		return "iframe";
	}

	@Override
	public void populateBean(IframeEditor e) {
		srcTextField.setText(e.getSrc());
		parameterViewPane.populate(e.getParameters());
		this.horizontalCheck.setSelected(e.isOverflowx());
		this.verticalCheck.setSelected(e.isOverflowy());
	}

	@Override
	public IframeEditor updateBean() {
		IframeEditor ob = new IframeEditor();
		ob.setSrc(srcTextField.getText());
		List<ParameterProvider> parameterList = parameterViewPane.update();
		ob.setParameters(parameterList.toArray(new ParameterProvider[parameterList.size()]));
		ob.setOverflowx(this.horizontalCheck.isSelected());
		ob.setOverflowy(this.verticalCheck.isSelected());
		return ob;
	}
}