package com.fr.design.data.datapane.connect;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ItemEditableComboBoxPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final Object EMPTY = new Object() {
		public String toString() {
			return "";
		}
	};
	
	protected UIComboBox itemComboBox;
	protected UIButton editButton;
    protected UIButton refreshButton;
	
	public ItemEditableComboBoxPanel() {
		super();
		
		initComponents();
	}
	
	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
		
		itemComboBox = new UIComboBox();
		itemComboBox.setEnabled(true);
		this.add(itemComboBox, BorderLayout.CENTER);
		
		editButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/control-center2.png"));
        refreshButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
        JPanel jPanel = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(2, 4 ,4);
        jPanel.add(editButton);
        jPanel.add(refreshButton);
		this.add(jPanel, BorderLayout.EAST);
        Dimension buttonSize = new Dimension(26, 20);
		editButton.setPreferredSize(buttonSize);
		editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                editItems();
            }
        });
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshItems();
            }
        });
	}
	
	/**
	 * 给itemComboBox添加ActionListener
	 */
	public void addComboBoxActionListener(ActionListener l) {
		itemComboBox.addActionListener(l);
	}
	
	/*
	 * 刷新itemComboBox的内容
	 */
	protected void refreshItems() {
		// 记录原来选中的Item,重新加载后需要再次选中
		Object lastSelectedItem = itemComboBox.getSelectedItem();
		
		DefaultComboBoxModel model = ((DefaultComboBoxModel) itemComboBox.getModel());
		model.removeAllElements();
		
		// 先加EMPTY,再加items
		model.addElement(EMPTY);
		
		java.util.Iterator<String> itemIt = items();
		while(itemIt.hasNext()) {
			model.addElement(itemIt.next());
		}
		
		// 再次选中之前选中的Item
		int idx = model.getIndexOf(lastSelectedItem);
		if(idx < 0) {
			idx = 0;
		}
		itemComboBox.setSelectedIndex(idx);
	}
	
	/*
	 * 得到其中的itemComboBox所选中的Item
	 */
	public String getSelectedItem() {
		Object selected = itemComboBox.getSelectedItem();
		
		return selected instanceof String ? (String)selected : null;
	}
	
	/*
	 * 选中name项
	 */
	public void setSelectedItem(String name) {
		DefaultComboBoxModel model = ((DefaultComboBoxModel) itemComboBox.getModel());
		model.setSelectedItem(name);
	}
	
	/*
	 * 刷新ComboBox.items
	 */
	protected abstract java.util.Iterator<String> items();
	
	/*
	 * 弹出对话框编辑Items
	 */
	protected abstract void editItems();
}