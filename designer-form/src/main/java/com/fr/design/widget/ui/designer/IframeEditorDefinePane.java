package com.fr.design.widget.ui.designer;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.IframeEditor;

import com.fr.stable.ParameterProvider;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class IframeEditorDefinePane extends AbstractDataModify<IframeEditor> {
	private UITextField srcTextField;
	private ReportletParameterViewPane parameterViewPane;
	private UICheckBox horizontalCheck;
	private UICheckBox verticalCheck;

	public IframeEditorDefinePane(XCreator xCreator) {
		super(xCreator);
	}
	
	private void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
		JPanel attr = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		attr.add(horizontalCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Preference-Horizontal_Scroll_Bar_Visible")));
		attr.add(verticalCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Preference-Vertical_Scroll_Bar_Visible")));
		contentPane.add(attr);
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] rowSize = { p, p, p, p };
		double[] columnSize = { p, f };

		Component[][] coms = {
				{ horizontalCheck, null },
				{ verticalCheck, null },
				{ new UILabel(com.fr.design.i18n.Toolkit.i18nText("Form-Url") + ":"), srcTextField = new UITextField() },
				{ new UILabel(com.fr.design.i18n.Toolkit.i18nText("Parameter") + ":"), parameterViewPane = new ReportletParameterViewPane() } };
		int[][] rowCount = {{1, 1},{1, 1},{1, 1}, {1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(coms, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, 5);

		contentPane.add(panel);

		UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Advanced"), 280, 20, contentPane);
		this.add(uiExpandablePane, BorderLayout.NORTH);

	}


	@Override
	public String title4PopupWindow() {
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
		IframeEditor ob = (IframeEditor)creator.toData();
		ob.setSrc(srcTextField.getText());
		List<ParameterProvider> parameterList = parameterViewPane.update();
		ob.setParameters(parameterList.toArray(new ParameterProvider[parameterList.size()]));
		ob.setOverflowx(this.horizontalCheck.isSelected());
		ob.setOverflowy(this.verticalCheck.isSelected());
		return ob;
	}
}