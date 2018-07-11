package com.fr.design.editor.editor;

import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.icombobox.IntComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

public class OldColumnIndexEditor extends Editor {
	private UIComboBox valueColumnIndexComboBox;

	protected UIRadioButton indexCheckBox;
	protected UIRadioButton nameCheckBox;
	protected String[] columnNames;

	public OldColumnIndexEditor(String name) {
		this(0, name);
	}

	public OldColumnIndexEditor(Object value, String name) {
		this.setLayout(new BorderLayout(0, 0));
		valueColumnIndexComboBox = new IntComboBox();
		for (int i = 1; i <= ((Integer)value).intValue(); i++) {
			valueColumnIndexComboBox.addItem(new Integer(i));
		}

		valueColumnIndexComboBox.setRenderer(columnNameListCellRenderer);

		if (((Integer)value).intValue() > 0) {
			((IntComboBox) valueColumnIndexComboBox).setSelectedInt(1);
		}
		this.add(valueColumnIndexComboBox, BorderLayout.CENTER);
		this.setName(name);
		valueColumnIndexComboBox.setBorder(null);
	}


	public OldColumnIndexEditor(String[] columnNames, String name) {
		this(columnNames.length, name);
		this.columnNames = columnNames;

		this.initComponents();
	}

	protected void initComponents() {
		indexCheckBox = new UIRadioButton(Inter.getLocText("Datasource-Column_Index"));
		nameCheckBox = new UIRadioButton(Inter.getLocText("ColumnName"));
		indexCheckBox.addActionListener(actionListener);
		nameCheckBox.addActionListener(actionListener);
		javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		buttonGroup.add(indexCheckBox);
		buttonGroup.add(nameCheckBox);

		JPanel checkPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		checkPane.add(indexCheckBox);
		checkPane.add(nameCheckBox);
		indexCheckBox.setSelected(true);
		// 用名字啊 序号谁知道是对应的什么.
//		nameCheckBox.setSelected(true);

		this.add(checkPane, BorderLayout.EAST);
	}
	
	public boolean isColumnIndexSelect(){
		return this.indexCheckBox.isSelected();
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	@Override
	public Object getValue() {
//		return this.isColumnIndexSelect() ? valueColumnIndexComboBox.getSelectedItem()
//				: getColumnName();
		return valueColumnIndexComboBox.getSelectedItem();
	}
	
	public String getColumnName(){
		int index = ((Integer) valueColumnIndexComboBox.getSelectedItem()).intValue() - 1;
		return index >= 0 && columnNames.length > index ? columnNames[index] : StringUtils.EMPTY;
	}

	@Override
	public void setValue(Object value) {
		if(value instanceof Integer){
			valueColumnIndexComboBox.setSelectedItem(value);
		}else{
//			value
		}
	}

	public String getIconName() {
		return "ds_column";
	}

	@Override
	public boolean accept(Object object) {
		return object instanceof Integer || object instanceof String;
	}

	protected ListCellRenderer columnNameListCellRenderer = new UIComboBoxRenderer() {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if ((indexCheckBox != null && indexCheckBox.isSelected()) || columnNames == null) {
				if (value == null) {
					this.setText("");
				} else {
					this.setText("" + value);
				}
			} else {
				if (value == null) {
					this.setText("");
				} else
					this.setText(columnNames[((Integer) value).intValue() - 1]);
			}

			return this;
		}
	};

	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (indexCheckBox != null) {
				indexCheckBox.isSelected();
			}
			OldColumnIndexEditor.this.invalidate();
			OldColumnIndexEditor.this.repaint();
		}
	};

	public void setEnabled(boolean enabled) {
		if (valueColumnIndexComboBox != null) {
			valueColumnIndexComboBox.setEnabled(enabled);
		}
		if (indexCheckBox != null) {
			indexCheckBox.setEnabled(enabled);
		}
		if (nameCheckBox != null) {
			nameCheckBox.setEnabled(enabled);
		}
	}

	public void addItemListener(ItemListener l) {
		valueColumnIndexComboBox.addItemListener(l);
	}

	public void removeItemListener(ItemListener l) {
		valueColumnIndexComboBox.removeItemListener(l);
	}
}