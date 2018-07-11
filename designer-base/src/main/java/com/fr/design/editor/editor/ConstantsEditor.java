package com.fr.design.editor.editor;

import com.fr.base.BaseFormula;


public class ConstantsEditor extends FormulaEditor {

	public ConstantsEditor(String name, BaseFormula formula) {
		super(name, formula);
	}
	
	protected void showFormulaPane() {
		// do nothing 防止修改...
	}
	
	public void setValue(BaseFormula value) {
		// do nothing 防止修改...
    }
	
	public boolean accept(Object object) {
        return object instanceof BaseFormula && object.equals(this.getValue());
    }
}