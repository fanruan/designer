package com.fr.design.mainframe.widget.editors;

import java.awt.Component;

import javax.swing.*;

import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.icombobox.ComboCheckBox;
import com.fr.design.designer.properties.items.FRBorderConstraintsItems;
import com.fr.design.designer.properties.items.Item;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

public class BorderLayoutDirectionEditor extends AbstractPropertyEditor {

	public static final Item[] ITEMS = new Item[] {
			new Item(Inter.getLocText("BorderLayout-North"), WBorderLayout.NORTH),
			new Item(Inter.getLocText("BorderLayout-South"), WBorderLayout.SOUTH),
			new Item(Inter.getLocText("BorderLayout-West"), WBorderLayout.WEST),
			new Item(Inter.getLocText("BorderLayout-East"), WBorderLayout.EAST) };

	private ComboCheckBox comboBox;

	public BorderLayoutDirectionEditor() {
		comboBox = new ComboCheckBox(ITEMS);
		comboBox.setRenderer(new UIComboBoxRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if (value != null) {
					if (value instanceof Object[]) {
						Object[] obj = (Object[]) value;
						String[] res = new String[obj.length];
						for (int i = 0, len = obj.length; i < len; i++) {
							res[i] = ((Item) obj[i]).getName();
						}
						setText(StringUtils.join("、", res));
					}
				} else {
					setText(Inter.getLocText("None"));
				}
				return this;
			}
		});
//		comboBox.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				BorderLayoutDirectionEditor.this.firePropertyChanged();
//			}
//		});
	}

	@Override
	public void validateValue() throws ValidationException {

	}

	@Override
	public Component getCustomEditor() {
		return comboBox;
	}

	@Override
	public Object getValue() {
		return comboBox.getSelectedItem();
	}

	@Override
	public void setValue(Object value) {
		Item[] items;
		if (value instanceof String[]) {
			items = FRBorderConstraintsItems.createItems((String[]) value);
		} else {
			items = new Item[0];
		}
		comboBox.getModel().setSelectedItem(items);
	}
}