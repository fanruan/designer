package com.fr.design.editor.editor;

import com.fr.base.Utils;
import com.fr.general.Inter;

/**
 * 整数编辑器
 *
 * @author zhou
 * @since 2012-3-29下午4:02:06
 */
public class IntegerEditor extends NumberEditor<Integer> {

	private static final long serialVersionUID = 1L;

	public IntegerEditor() {
		this(new Integer(0));
	}

	public IntegerEditor(Integer value) {
		super(value, Inter.getLocText("FR-Designer_Parameter_Integer"));
	}

	@Override
	public Integer getValue() {
		return new Integer((int) this.numberField.getValue());
	}

	@Override
	public boolean accept(Object object) {
		return object != null && object instanceof Integer;
	}

	public String getIconName() {
		return "type_int";
	}

	@Override
	public void setValue(Integer value) {
		if (value == null) {
			value = new Integer(0);
		}
		this.numberField.setInteger(true);
		this.numberField.setValue(value.intValue());
		oldValue = Utils.objectToString(value);
	}


}