package com.fr.design.present.dict;

import java.awt.*;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;

import com.fr.base.Formula;
import com.fr.data.impl.FormulaDictionary;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

public class FormulaDictPane extends FurtherBasicBeanPane<FormulaDictionary> {
	private FormulaEditor keyFormulaEditor;
	private FormulaEditor valueFormulaEditor;

	public FormulaDictPane() {
		initComponents();
	}

	private void initComponents() {
		keyFormulaEditor = new FormulaEditor();
		keyFormulaEditor.setColumns(15);
		valueFormulaEditor = new FormulaEditor();
		valueFormulaEditor.setColumns(15);

		double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
		double[] columnSize = { f };
		double[] rowSize = { p,p,p,p,p };
//		ActionLabel tips = new ActionLabel("(对应实际值范围内的每个值)") {
//			@Override
//			public JToolTip createToolTip() {
//				MultiLineToolTip tip = new MultiLineToolTip();
//				tip.setComponent(this);
//				tip.setOpaque(false);
//				return tip;
//			}
//		};
//		tips.setToolTipText(Inter.getLocText("Formula_Dictionary_Display_Examples"));
//		JPanel cc = new JPanel(new BorderLayout());
//		cc.add(tips, BorderLayout.WEST);

		UILabel tag = new UILabel(Inter.getLocText("Formula_Dictionary_Display_Examples_Html"));
		JPanel t = new JPanel(new BorderLayout());
		t.add(tag, BorderLayout.WEST);

		Formula vf = new Formula("$$$");
		valueFormulaEditor = new FormulaEditor("",vf);
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Actual_Value") + ":")},
                new Component[]{keyFormulaEditor},
                new Component[]{new UILabel(Inter.getLocText("Display_Value") + ":")},
                new Component[]{valueFormulaEditor},
				new Component[]{t}
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);

	}

	public void addChangeListener(DocumentListener l) {
		keyFormulaEditor.addDocumentListener(l);
		valueFormulaEditor.addDocumentListener(l);
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Formula");
	}

	@Override
	public void populateBean(FormulaDictionary dict) {
		keyFormulaEditor.setValue(new Formula(dict.getProduceFormula() == null ? StringUtils.EMPTY : dict.getProduceFormula()));
		valueFormulaEditor.setValue(new Formula(dict.getExcuteFormula() == null ? StringUtils.EMPTY : dict.getExcuteFormula()));
	}

	@Override
	public FormulaDictionary updateBean() {
		FormulaDictionary dict = new FormulaDictionary();
		if (keyFormulaEditor.getValue() != null) {
			dict.setProduceFormula(keyFormulaEditor.getValue().getContent());
		}
		if (valueFormulaEditor.getValue() != null) {
			dict.setExcuteFormula(valueFormulaEditor.getValue().getContent());
		}

		return dict;
	}

	@Override
	public boolean accept(Object ob) {
		return ob instanceof FormulaDictionary;
	}

	@Override
	public void reset() {
		keyFormulaEditor.reset();
		valueFormulaEditor.reset();
	}
}