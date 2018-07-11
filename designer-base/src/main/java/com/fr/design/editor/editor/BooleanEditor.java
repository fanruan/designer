/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.editor.editor;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * CellEditor used to edit Boolean object.
 *
 * @editor zhou
 * @since 2012-3-29下午6:01:09
 */
public class BooleanEditor extends Editor<Boolean> {

	private UICheckBox booleanCheckBox; // boolean checkbox

	/**
	 * Constructor.
	 */
	public BooleanEditor() {
		this(new Boolean(true));
	}

	public BooleanEditor(boolean b) {
		this(new Boolean(b));
	}

	/**
	 * Constructor.
	 */

	public BooleanEditor(Boolean value) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		booleanCheckBox = new UICheckBox("true");
		this.add(booleanCheckBox, BorderLayout.CENTER);
		this.setValue(value);
		this.setName(Inter.getLocText("Parameter-Boolean"));
		booleanCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				fireStateChanged();
			}
		});
	}

	public JComponent getEditComp() {
		return booleanCheckBox;
	}

	/**
	 * Return the value of the CellEditor.
	 */
	@Override
	public Boolean getValue() {
		return new Boolean(this.booleanCheckBox.isSelected());
	}

	/**
	 * Set the value to the CellEditor.
	 */
	@Override
	public void setValue(Boolean value) {
		// populate data to UI
		if (value == null) {
			value = true;
		}
		this.booleanCheckBox.setSelected(value.booleanValue());
	}

	/**
	 * Sets whether or not this component is enabled.
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.booleanCheckBox.setEnabled(enabled);
	}

	/**
	 * Request focus
	 */
	@Override
	public void requestFocus() {
		this.booleanCheckBox.requestFocus();
	}

	/**
	 * Fire editing stopped listeners.
	 */
	@Override
	protected void fireEditingStopped() {
		// populate UI to data.
		this.setValue(new Boolean(this.booleanCheckBox.isSelected()));

		super.fireEditingStopped();
	}

	public String getIconName() {
		return "type_bool";
	}

	@Override
	public boolean accept(Object object) {
		return object instanceof Boolean;
	}
}