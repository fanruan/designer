package com.fr.design.editor.editor;

import com.fr.base.Parameter;
import com.fr.design.gui.icombobox.ParameterComboBox;
import com.fr.design.layout.FRGUIPaneFactory;


import java.awt.*;

/**
 * 参数Editor
 *
 * @editor zhou
 * @since 2012-3-29下午5:24:41
 */
public class ParameterEditor extends Editor<Parameter> {

	private ParameterComboBox parameterCombobox;

	public ParameterEditor() {
		this(null);
	}

	public ParameterEditor(Parameter parameter) {
		parameterCombobox = new ParameterComboBox();
		parameterCombobox.setEditable(true);
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.add(parameterCombobox, BorderLayout.CENTER);

		this.setValue(parameter);
		this.setName(com.fr.design.i18n.Toolkit.i18nText("Parameter"));
	}

	@Override
	public Parameter getValue() {
		return parameterCombobox.getSelectedItem();
	}

	@Override
	public void setValue(Parameter value) {
		parameterCombobox.setSelectedParameter(value);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		parameterCombobox.setEnabled(enabled);
	}

	@Override
	public void requestFocus() {
		parameterCombobox.requestFocus();
	}

	public String getIconName() {
		return "parameter";
	}

	@Override
	public boolean accept(Object object) {
		return object instanceof Parameter;
	}
}