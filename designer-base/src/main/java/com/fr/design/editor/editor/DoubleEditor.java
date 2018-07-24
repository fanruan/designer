package com.fr.design.editor.editor;

import com.fr.base.Utils;


/**
 * 双精度编辑器
 *
 * @editor zhou
 * @since 2012-3-29下午4:51:03
 */
public class DoubleEditor extends NumberEditor<Double> {

	public DoubleEditor() {
		this(new Double(0));
	}

	public DoubleEditor(Double value) {
		super(value, com.fr.design.i18n.Toolkit.i18nText("Parameter-Double"));
	}

	@Override
	public Double getValue() {
		return new Double(this.numberField.getValue());
	}

	@Override
	public void setValue(Double value) {
		if (value == null) {
			value = new Double(0);
		}
		this.numberField.setInteger(false);
		this.numberField.setValue(value.doubleValue());
		oldValue = Utils.objectToString(value);
	}

	public String getIconName() {
		return "type_double";
	}

	@Override
	public boolean accept(Object object) {
		return object != null && object instanceof Double;
	}

}