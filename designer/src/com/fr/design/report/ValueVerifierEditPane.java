package com.fr.design.report;

import com.fr.base.Formula;
import com.fr.data.Verifier;
import com.fr.data.VerifyItem;
import com.fr.design.gui.itableeditorpane.ActionStyle;
import com.fr.design.gui.itableeditorpane.UIArrayTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.GeneralUtils;
import com.fr.general.Inter;
import com.fr.report.write.ValueVerifier;
import com.fr.stable.FormulaProvider;
import com.fr.stable.bridge.StableFactory;
import com.fr.write.ReportWriteAttrProvider;
import com.fr.write.ValueVerifierProvider;

import javax.swing.*;
import java.awt.*;

public class ValueVerifierEditPane extends JPanel {
	private UITableEditorPane<Object[]> tableEditorPane;
	private final String[] columnNames = new String[] {
			Inter.getLocText(new String[] {"Verify-Verify_Formula", "Verify-ToolTips"}, new String[] {"(", ")"}),
			Inter.getLocText("Verify-Error_Information") };

	public ValueVerifierEditPane() {
		// ben:UITableEditorPane；
		this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
		tableEditorPane = new UITableEditorPane<Object[]>(new UIArrayTableModel(columnNames, new int[] {
				ActionStyle.ADDSTYLE, ActionStyle.DELETESTYLE,
				ActionStyle.MOVEUPSTYLE, ActionStyle.MOVEDOWNSTYLE}));
		this.add(tableEditorPane, BorderLayout.CENTER);
	}

	/**
	 * populate
	 */
	public void populate(ValueVerifier valueVerifier) {
		if (valueVerifier == null) {
			return;
		}
		int rowCount = valueVerifier.getVerifyItemsCount();
		Object[][] os = new Object[rowCount][];
		int tableDataCount = 0;
		for (int i = 0; i < rowCount; i ++) {
			VerifyItem item = valueVerifier.getVerifyItem(i);
			FormulaProvider formula = item.getFormula();
			if (formula == null) {
				continue;
			}
			os[tableDataCount++] = new Object[]{formula.getPureContent(), item.getMessage()};
		}
		this.tableEditorPane.populate(os);
	}

	public ValueVerifier update() {
		ValueVerifier valueVerifier = new ValueVerifier();
		java.util.List<Object[]> list = tableEditorPane.update();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = list.get(i);
			if (o == null || o[0] == null) {
				continue;
			}
			VerifyItem item = new VerifyItem(new Formula(GeneralUtils.objectToString(o[0])), GeneralUtils.objectToString(o[1]));
			valueVerifier.addVerifyItem(item);
		}
		return valueVerifier;
	}
}
