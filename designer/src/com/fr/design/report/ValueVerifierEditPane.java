package com.fr.design.report;

import com.fr.base.Formula;
import com.fr.data.Verifier;
import com.fr.design.gui.itableeditorpane.ActionStyle;
import com.fr.design.gui.itableeditorpane.UIArrayTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
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
	public void populate(ReportWriteAttrProvider reportWriteAttr) {
		if (reportWriteAttr == null) {
			return;
		}
		int rowCount = reportWriteAttr.getVerifierCount();
		Object[][] os = new Object[reportWriteAttr.getValueVerifierCount()][];
		int cnt = 0;
		for (int i = 0; i < rowCount; i++) {
			if (!(reportWriteAttr.getVerifier(i) instanceof ValueVerifierProvider)) {
				continue;
			}
			Formula formula = ((ValueVerifierProvider)reportWriteAttr.getVerifier(i)).getFormula();
			if (formula == null) {
				continue;
			}
			String formulaContent = formula.getContent().substring(1);
			String message = reportWriteAttr.getVerifier(i).getMessage();
			os[cnt++] = new Object[] { formulaContent, message };
		}
		this.tableEditorPane.populate(os);
	}

	/**
	 * update
	 */
	public void update(ReportWriteAttrProvider reportWriteAttr, String name) {
		java.util.List<Object[]> list = tableEditorPane.update();
		reportWriteAttr.clearVerifiers(true);
		for (int i = 0; i < list.size(); i++) {
			Object[] o = list.get(i);
			if (o == null || o[0] == null) {
				continue;
			}
			ValueVerifierProvider p = StableFactory.getMarkedInstanceObjectFromClass(ValueVerifierProvider.TAG, ValueVerifierProvider.class);
			p.setFormula(new Formula(readValueVerifyObject(o[0])));
			p.setMessage(readValueVerifyObject(o[1]));
			reportWriteAttr.addVerifier(name, (Verifier) p);
//			reportWriteAttr.addVerifier(name, new ValueVerifier(readValueVerifyObject(o[0]), readValueVerifyObject(o[1])));
		}
	}

	private String readValueVerifyObject(Object obj) {
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}
}


//public class ValueVerifierEditPane extends BasicBeanPane<ValueVerifier> {
//	private ValueVerifier valueVerifier;
//	private UITextArea formula = new UITextArea();
//	private UITextArea message = new UITextArea();
//	private int col = 60;
//	private int row = 5;
//
//	public ValueVerifierEditPane() {
//		valueVerifier = new ValueVerifier();
//
//		this.setLayout(new FlowLayout());
//
//		JPanel formulaPane = new JPanel();
//		UIScrollPane formulaScrollPane = new UIScrollPane(formula);
//		formula.setRows(row);
//		formula.setColumns(col);
//		formula.setLineWrap(true);
//		formula.setToolTipText(Inter.getLocText("Verify-ToolTips"));
//		UILabel fl = new UILabel(Inter.getLocText("Verify-Verify_Formula"));
//		fl.setPreferredSize(new Dimension(80, 20));
//		formulaPane.add(fl);
//		formulaPane.add(formulaScrollPane);
//
//		JPanel messagePane = new JPanel();
//		UIScrollPane messageScrollPane = new UIScrollPane(message);
//		message.setRows(row);
//		message.setColumns(col);
//		message.setLineWrap(true);
//		UILabel ml = new UILabel(Inter.getLocText("Verify-Error_Information"));
//		ml.setPreferredSize(new Dimension(80, 20));
//		messagePane.add(ml);
//		messagePane.add(messageScrollPane);
//
//		this.add(formulaPane);
//		this.add(messagePane);
//	}
//
//	@Override
//	public void populateBean(ValueVerifier ob) {
//		valueVerifier = ob;
//		if (ob != null) {
//			if (ob.getFormula() != null) {
//				String content = ob.getFormula().getContent();
//				formula.setText(content.startsWith("=") ? content.substring(1) : content);
//			} else {
//				formula.setText("");
//			}
//			message.setText(ob.getMessage());
//		}
//	}
//
//	@Override
//	public ValueVerifier updateBean() {
//		valueVerifier.setFormula(new Formula(formula.getText()));
//		valueVerifier.setMessage(message.getText());
//		return valueVerifier;
//	}
//
//	@Override
//	protected String title4PopupWindow() {
//		return "valueVerifierPane";
//	}
//}