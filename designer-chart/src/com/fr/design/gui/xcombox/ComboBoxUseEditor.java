package com.fr.design.gui.xcombox;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;

import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.icombobox.FRTreeComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.*;

/**
 * 下拉列表
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-22 下午03:14:25
 */
public class ComboBoxUseEditor extends Editor<String> {
	private FRTreeComboBox comBox;
	private String[] items = new String[0];

	private PopupMenuListener popupMenuListener = new PopupMenuListener() {

		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			new Thread() {

             public void run() {
				 calculateComboBoxNames(items);
			 }
			}.start();
		}

		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

		}

		public void popupMenuCanceled(PopupMenuEvent e) {

		}
	};

	TreeCellRenderer treeRenderer = new DefaultTreeCellRenderer() {
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				Object userObj = node.getUserObject();
				if (userObj instanceof String) {
					this.setIcon(null);
				}
			}
			return this;
		}
	};
	
	public ComboBoxUseEditor(String[] useNames) {
		this.setLayout(new BorderLayout(0, 0));
		comBox = new FRTreeComboBox(){
			protected void dealSamePath(TreePath parent,TreeNode node,UITextField textField){
				for (Enumeration e = node.children(); e.hasMoreElements(); ) {
					TreeNode n = (TreeNode) e.nextElement();
					TreePath path = parent.pathByAddingChild(n);
					if (pathToString(path).toUpperCase().startsWith(textField.getText().toUpperCase())) {
						tree.scrollPathToVisible(path);
						tree.setSelectionPath(path);
						break;
					}
				}
			}
		};
		comBox.getTree().setCellRenderer(treeRenderer);
		comBox.setEditable(true);
		comBox.setEnabled(true);
		items = useNames;

		this.comBox.addPopupMenuListener(popupMenuListener);
		comBox.setRenderer(new UIComboBoxRenderer());
		this.add(comBox, BorderLayout.CENTER);
		this.setName("");
		comBox.setBorder(null);
		
		comBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				fireStateChanged();
			}
		});
		
		comBox.getEditor().getEditorComponent().addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				fireStateChanged();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
	}

	/**
	 * 是否是String类型
	 * @param object 要判断的
	 * @return 是则返回true
	 */
	public boolean accept(Object object) {
		return object instanceof String;
	}

	@Override
	public String getValue() {
		if(comBox.getSelectedItem() != null) {
			return comBox.getSelectedItem().toString();
		}
		return "";
	}

	@Override
	public void setValue(String value) {
		comBox.setSelectedItem(value);
	}

	private void calculateComboBoxNames(String[] useNames) {
		JTree tree = comBox.getTree();
		if (tree == null) {
			return;
		}
		DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
		rootTreeNode.removeAllChildren();

		if (useNames.length == 0) {
			return;
		}

		for (int i = 0; i < useNames.length; i++) {
			ExpandMutableTreeNode tableChildTreeNode = new ExpandMutableTreeNode(useNames[i]);
			rootTreeNode.add(tableChildTreeNode);
		}

		((DefaultTreeModel) tree.getModel()).reload();

		TreeNode root = (TreeNode) tree.getModel().getRoot();
		TreePath parent = new TreePath(root);
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		for (Enumeration e = node.children(); e.hasMoreElements(); ) {
			TreeNode n = (TreeNode) e.nextElement();
			TreePath path = parent.pathByAddingChild(n);
			tree.expandPath(path);
		}
	}


}