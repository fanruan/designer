package com.fr.design.gui.frpane;

import com.fr.base.BaseUtils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.ListMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;


public class ObjectProperiesPane extends BasicBeanPane<ListMap> {
	private UIButton addButton;
	private JPanel selectedItemPane;
	private java.util.List propertyPaneList = new ArrayList();
	
	public ObjectProperiesPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel buttonPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		this.addButton = new UIButton(Inter.getLocText("Add") + " " +Inter.getLocText("Property"));
		buttonPane.add(this.addButton, BorderLayout.WEST);
		this.add(buttonPane, BorderLayout.NORTH);
		
		this.addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPropertyPane(new PropertyPane());
				ObjectProperiesPane.this.validate();
				ObjectProperiesPane.this.repaint();
			}
		});
		
		selectedItemPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
//		selectedItemPane.setLayout(new BoxLayout(selectedItemPane,BoxLayout.Y_AXIS));
		
		JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		northPane.add(selectedItemPane, BorderLayout.NORTH);

		JScrollPane selectedItemScrollPane = new JScrollPane();
		selectedItemScrollPane.setViewportView(northPane);
		selectedItemScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(selectedItemScrollPane, BorderLayout.CENTER);
	}

	@Override
	protected String title4PopupWindow() {
		return "property";
	}
	
	@Override
	public void populateBean(ListMap propertyMap) {
		this.selectedItemPane.removeAll();
		this.propertyPaneList.removeAll(propertyPaneList);
		
		Iterator iterator = propertyMap.entrySet().iterator();
		while (iterator.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
			String key = (String)entry.getKey();
			Object obj = entry.getValue();
			PropertyPane propertyPane = new PropertyPane();
			propertyPane.populate(new NameObject(key, obj));
			addPropertyPane(propertyPane);
		}
		
		ObjectProperiesPane.this.validate();
		ObjectProperiesPane.this.repaint();
	}

	/**
	 * set addButton enable state
	 * @param enable
	 */
	public void enableAddButton(boolean enable) {
		this.addButton.setEnabled(enable);
	}

	/**
	 * add propertyPane
	 * @param propertyPane
	 */
	public void addPropertyPane(PropertyPane propertyPane) {
		this.selectedItemPane.add(propertyPane);
		this.propertyPaneList.add(propertyPane);
	}

	/**
	 * remove propertyPane
	 * @param propertyPane
	 */
	public void removePropertyPane(PropertyPane propertyPane) {
		this.selectedItemPane.remove(propertyPane);
		this.propertyPaneList.remove(propertyPane);
	}

	@Override
	public ListMap updateBean() {
		ListMap propertyMap = new ListMap();
		
		for (int i = 0; i < propertyPaneList.size(); i ++) {
			NameObject nameO = ((PropertyPane)propertyPaneList.get(i)).update();
			propertyMap.put(nameO.getName(), nameO.getObject());
		}
		
		return propertyMap;
	}

	public class PropertyPane extends JPanel {
		private UITextField nameField;
		private ValueEditorPane valueEditorPane;
		
		public PropertyPane () {
			this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
			
			UIButton cancel  = new UIButton(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
			cancel.setToolTipText(Inter.getLocText("Remove"));
			cancel.addActionListener(cancleListener);
			cancel.setMargin(new Insets(0, 0, 0, 0));
			this.add(cancel);
			
			this.add(new UILabel(Inter.getLocText("Name")+":"));
			this.nameField = new UITextField(6);
			this.add(this.nameField);
			
			this.add(new UILabel(Inter.getLocText("Value")+ ":"));
			this.valueEditorPane = ValueEditorPaneFactory.createExtendedValueEditorPane();
			this.add(this.valueEditorPane);
		}
		
		public void populate(NameObject nameObject) {
			if (nameObject == null) {
				return;
			}
			this.nameField.setText(nameObject.getName());
			this.valueEditorPane.populate(nameObject.getObject());
		}
		
		public NameObject update() {
			NameObject nameO = new NameObject();
			nameO.setName(this.nameField.getText());
			nameO.setObject(this.valueEditorPane.update());
			return nameO;
		}
		
		ActionListener cancleListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removePropertyPane(PropertyPane.this);
				ObjectProperiesPane.this.validate();
				ObjectProperiesPane.this.repaint();
			}
		};
	}
}