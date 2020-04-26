package com.fr.design.editor.editor;

import com.fr.design.DesignModelAdapter;
import com.fr.design.gui.icombobox.FilterComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.stable.js.WidgetName;

import javax.swing.JList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * the editor to edit WidgetName
 *
 * @editor zhou
 * @since 2012-3-29下午6:04:37
 */
public class WidgetNameEditor extends Editor<WidgetName> {
	private FilterComboBox<WidgetName> comb;


	public WidgetNameEditor(String name) {
		this(null, name);
	}

	public WidgetNameEditor(WidgetName value) {
		this(value, "");
	}

	private void generateWidgets() {
		DesignModelAdapter<?, ?> model = DesignModelAdapter.getCurrentModelAdapter();
		this.comb.setItemList(model == null ? null : model.getWidgetsName());
		comb.setSelectedItem(comb.getEditor().getItem());
	}

	public WidgetNameEditor(WidgetName value, String name) {
		comb = new FilterComboBox<WidgetName>();
		comb.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				//do nothing
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				//do nothing
			}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				WidgetNameEditor.this.generateWidgets();
			}

		});
		this.generateWidgets();
		comb.setRenderer(new UIComboBoxRenderer() {
			public Component getListCellRendererComponent(JList list,
														  Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (value instanceof WidgetName) {
					this.setText(((WidgetName) value).getName());
				}

				return this;
			}
		});
		comb.setEditable(true);
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.add(comb, BorderLayout.CENTER);

		this.setValue(value);
		this.setName(name);
	}

	@Override
	public WidgetName getValue() {
		return (WidgetName) comb.getSelectedItem();
	}

	@Override
	public void setValue(WidgetName value) {
		comb.setSelectedItem(value);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		comb.setEnabled(enabled);
	}

	@Override
	public void requestFocus() {
		comb.requestFocus();
	}

	public String getIconName() {
		return "widgets";
	}

	@Override
	public boolean accept(Object object) {
		return object instanceof WidgetName;
	}
}