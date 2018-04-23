package com.fr.design.present;

import java.awt.BorderLayout;

import com.fr.base.present.FormulaPresent;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

public class FormulaPresentPane extends FurtherBasicBeanPane<FormulaPresent> {
	private TinyFormulaPane tinyFormulaPane;

	public FormulaPresentPane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		tinyFormulaPane = new TinyFormulaPane();
		this.add(tinyFormulaPane, BorderLayout.CENTER);
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Present-Formula_Present");
	}

	@Override
	public boolean accept(Object ob) {
		return ob instanceof FormulaPresent;
	}
	
	public void reset() {
		this.tinyFormulaPane.populateBean(null);
	}

	@Override
	public void populateBean(FormulaPresent ob) {
		this.tinyFormulaPane.populateBean(ob.getFormulaContent());
	}

	@Override
	public FormulaPresent updateBean() {
		return new FormulaPresent(this.tinyFormulaPane.updateBean());
	}
}