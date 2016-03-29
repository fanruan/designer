package com.fr.poly;

import java.util.ArrayList;

import javax.swing.table.TableModel;

import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.gui.itable.PropertyGroup;
import com.fr.poly.group.PolyBoundsGroup;
import com.fr.poly.group.PolyNameGroup;
import com.fr.report.poly.TemplateBlock;

public class PolyBlockProperTable extends AbstractPropertyTable {
	private PolyDesigner designer;

	/**
	 * 初始化属性表
	 * 
	 * @param source 指定的属性来源
	 * 
	 */
	public void initPropertyGroups(Object source) {
		groups = new ArrayList<PropertyGroup>();
		if (source instanceof TemplateBlock) {
			TemplateBlock block = (TemplateBlock) source;
			PolyNameGroup namegroup = new PolyNameGroup(block);
			groups.add(new PropertyGroup(namegroup));
			PolyBoundsGroup boundsgroup = new PolyBoundsGroup(block, designer.getTarget());
			groups.add(new PropertyGroup(boundsgroup));
		}
		TableModel model = new BeanTableModel();
		setModel(model);
		this.repaint();
	}

	/**
	 * 触发组件属性编辑事件
	 * 
	 */
	public void firePropertyEdit() {
		designer.fireTargetModified();
	}

	public void populate(PolyDesigner designer) {
		this.designer = designer;
		initPropertyGroups(this.designer.getEditingTarget());
	}

}