package com.fr.design.editor.editor;

import com.fr.design.gui.columnrow.ColumnRowPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.stable.ColumnRow;

import java.awt.*;

/**
 * the editor to edit ColumnRow
 *
 * @editor zhou
 * @since 2012-3-29下午6:01:37
 */
public class ColumnRowEditor extends Editor<ColumnRow> {

	private ColumnRowPane crPane;

	public ColumnRowEditor() {
		this("");
	}

	public ColumnRowEditor(String name) {
		this(null, name);
	}


	public ColumnRowEditor(ColumnRow value) {
		this(value, "");
	}

	public ColumnRowEditor(ColumnRow value, String name) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		crPane = new ColumnRowPane();
		this.add(crPane, BorderLayout.CENTER);
		this.setValue(value);
		this.setName(name);
	}

	@Override
	public ColumnRow getValue() {
		return this.crPane.update();
	}

	@Override
	public void setValue(ColumnRow value) {
		if (value == null) {
			value = ColumnRow.valueOf(0, 0);
		}

		this.crPane.populate(value);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		this.crPane.setEnabled(enabled);
	}

	@Override
	public void requestFocus() {
		this.crPane.requestFocus();
	}

	public String getIconName() {
		return "cell";
	}

	@Override
	public boolean accept(Object object) {
		return object instanceof ColumnRow;
	}
}