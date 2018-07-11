package com.fr.design.gui.itree.refreshabletree;

import com.fr.data.Dictionary;
import com.fr.data.impl.TreeNodeAttr;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;

import java.awt.*;

public class TreeNodePane extends BasicBeanPane<TreeNodeAttr> {
	
	private DictionaryPane dataRadioPane;
	
	/*
	 * richer:树支节点数据设置面板
	 */
	public TreeNodePane(){
		this.initComponents();
	}
	
	private void initComponents(){
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		dataRadioPane = new DictionaryPane();
		this.add(dataRadioPane, BorderLayout.CENTER);

	}
	
	@Override
	protected String title4PopupWindow() {
		return "tree";
	}
	
	public void populateBean(TreeNodeAttr treeNodeAttr){
		Dictionary dict = treeNodeAttr.getDictionary();
		dataRadioPane.populateBean(dict);
	}
	
	public TreeNodeAttr updateBean(){
		TreeNodeAttr treeNodeAttr = new TreeNodeAttr();
		Dictionary mvList = this.dataRadioPane.updateBean();	
		treeNodeAttr.setDictionary(mvList);

		return treeNodeAttr;
	}
}