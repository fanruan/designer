package com.fr.design.editor.editor;

import com.fr.base.Utils;

/**
 * 长整形编辑器
 * 
 * @author zhou
 * @since 2012-3-29下午4:50:31
 */
public class LongEditor extends NumberEditor<Long> {

	public LongEditor() {
		super();
	}

	public LongEditor(Long value, String name) {
		super(value, name);
	}

	@Override
	public Long getValue() {
		return new Long((int)this.numberField.getValue());
	}

	@Override
	public void setValue(Long value) {
		if (value == null) {
			value = new Long(0);
		}
		this.numberField.setInteger(true);
		this.numberField.setValue(value.intValue());
		oldValue = Utils.objectToString(value);
	}

	@Override
	public boolean accept(Object object) {
		return object != null && object instanceof Long;
	}

}