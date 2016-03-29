package com.fr.quickeditor.floatquick;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.base.Formula;
import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.quickeditor.FloatQuickEditor;
import com.fr.report.ReportHelper;
import com.fr.stable.StringUtils;

public class FloatStringQuickEditor extends FloatQuickEditor {
	private UITextField stringTextField;

	// august：如果是原来编辑的是公式,要保留公式里的这些属性,不然在公式和字符串转化时,就会丢失这些属性设置
	private boolean reserveInResult = false;
	private boolean reserveOnWriteOrAnaly = true;

	public FloatStringQuickEditor() {
		super();
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] cloumnSize = {f};
        double[] rowSize = {p};
		stringTextField = new UITextField();
        Component[][] components = new Component[][]{
                new Component[]{stringTextField}
        };
        JPanel pane = TableLayoutHelper.createTableLayoutPane(components,rowSize,cloumnSize) ;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(pane,BorderLayout.CENTER);
	}

	@Override
	protected void refreshDetails() {
		String str = null;
		Object value = floatElement.getValue();
		if (value == null) {
			str = StringUtils.EMPTY;
		} else if (value instanceof Formula) {
			Formula formula = (Formula)value;
			str = formula.getContent();
			reserveInResult = formula.isReserveInResult();
			reserveOnWriteOrAnaly = formula.isReserveOnWriteOrAnaly();
		} else {
			str = value.toString();
		}
		showText(str);
	}

	public void showText(String str) {
		stringTextField.getDocument().removeDocumentListener(documentListener);
		stringTextField.setText(str);
		stringTextField.getDocument().addDocumentListener(documentListener);
	}

	DocumentListener documentListener = new DocumentListener() {

		@Override
		public void insertUpdate(DocumentEvent e) {
			changeReportPaneCell(stringTextField.getText().trim());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			changeReportPaneCell(stringTextField.getText().trim());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			changeReportPaneCell(stringTextField.getText().trim());
		}

	};

	protected void changeReportPaneCell(String tmpText) {
		if (tmpText != null && (tmpText.length() > 0 && tmpText.charAt(0) == '=')) {
			Formula textFormula = new Formula(tmpText);
			textFormula.setReserveInResult(reserveInResult);
			textFormula.setReserveOnWriteOrAnaly(reserveOnWriteOrAnaly);
			floatElement.setValue(textFormula);
		} else {
			Style style = floatElement.getStyle();
			if (floatElement != null && style != null && style.getFormat() != null && style.getFormat() == TextFormat.getInstance()) {
				floatElement.setValue(tmpText);
			} else {
				floatElement.setValue(ReportHelper.convertGeneralStringAccordingToExcel(tmpText));
			}
		}
		fireTargetModified();
		stringTextField.requestFocus();
	}

}