package com.fr.design.report;

import com.fr.base.BaseFormula;
import com.fr.data.VerifyItem;
import com.fr.design.gui.itableeditorpane.ActionStyle;
import com.fr.design.gui.itableeditorpane.UIArrayFormulaTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.GeneralUtils;

import com.fr.report.write.ValueVerifier;
import com.fr.stable.FormulaProvider;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;

public class ValueVerifierEditPane extends JPanel {
	private UITableEditorPane<Object[]> tableEditorPane;
	private final String[] columnNames = new String[] {
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Formula_Verify"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Formula_Verify_Warn_Text") };

	public ValueVerifierEditPane() {
		// ben:UITableEditorPane；
		this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
		tableEditorPane = new UITableEditorPane(new UIArrayFormulaTableModel(columnNames, new int[] {
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
			String msg = item.getMessage();
			if (!StableUtils.canBeFormula(msg)) {
				msg = "\"" + msg + "\"";//如果报错信息是以前的写法(字符串)就拼上""
			}
			os[tableDataCount++] = new Object[]{formula, BaseFormula.createFormulaBuilder().build(msg)};
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
			VerifyItem item = new VerifyItem(BaseFormula.createFormulaBuilder().build(o[0]), GeneralUtils.objectToString(o[1]));
			valueVerifier.addVerifyItem(item);
		}
		return valueVerifier;
	}
}
