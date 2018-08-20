package com.fr.design.editor.editor;

import com.fr.base.Utils;


/**
 * 单精度型编辑器
 *
 * @author zhou
 * @since 2012-3-29下午4:50:01
 */
public class FloatEditor extends NumberEditor<Float> {

	public FloatEditor() {
		this(new Float(0));
	}

	public FloatEditor(Float value) {
		super(value, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_Float"));
	}

	@Override
	public Float getValue() {
		return new Float(this.numberField.getValue());
	}

	@Override
	public void setValue(Float value) {
		if (value == null) {
			value = new Float(0);
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
		return object != null && object instanceof Float;
	}

}