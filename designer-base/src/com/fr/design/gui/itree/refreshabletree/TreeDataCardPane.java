package com.fr.design.gui.itree.refreshabletree;

import java.awt.BorderLayout;

import com.fr.general.NameObject;
import com.fr.data.impl.TreeAttr;
import com.fr.data.impl.TreeNodeAttr;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;


public class TreeDataCardPane extends BasicBeanPane<Object> {
	private NameObject no;
	private TreeNodeAttr treeNodeAttr;
	private TreeNodePane treeNodePane;

	public TreeDataCardPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		treeNodePane = new TreeNodePane();

	}

	@Override
	protected String title4PopupWindow() {
		return "treedata";
	}

	@Override
	public void populateBean(Object object) {
		this.remove(treeNodePane);
		if (object instanceof NameObject){
			this.no = (NameObject)object;
			Object ob = this.no.getObject();
			if (ob instanceof TreeNodeAttr) {
				this.treeNodeAttr = (TreeNodeAttr)ob;
				this.add(treeNodePane, BorderLayout.CENTER);
				treeNodePane.populateBean(treeNodeAttr);
			}
		} 
		validate();
		repaint();
		revalidate();
	}

	@Override
	public Object updateBean() {
		if (treeNodeAttr != null) {
			if (this.isAncestorOf(treeNodePane) && treeNodePane.isVisible() && !(this.no.getObject() instanceof TreeAttr)) {
				treeNodeAttr = treeNodePane.updateBean();
				this.no.setObject(treeNodeAttr);
			}
		}
		return null;
	}
}