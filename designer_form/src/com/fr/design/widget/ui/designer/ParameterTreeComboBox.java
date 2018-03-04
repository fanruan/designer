package com.fr.design.widget.ui.designer;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.base.TableData;
import com.fr.config.ServerConfig;
import com.fr.design.DesignModelAdapter;
import com.fr.design.gui.icombobox.FRTreeComboBox;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.parameter.ParameterGroup;
import com.fr.file.TableDataConfig;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 控件设置面板的“控件名”下拉框，里面是所有参数的名字
 * 
 * @author zhou
 * 
 */
public class ParameterTreeComboBox extends FRTreeComboBox {

	public ParameterTreeComboBox() {
		super(new JTree(), new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				if (value instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
					Object userObj = node.getUserObject();
					if (userObj instanceof String) {
						this.setIcon(BaseUtils.readIcon("com/fr/design/images/m_insert/expandCell.gif"));
					} else if (userObj instanceof Parameter) {
						Parameter parameter = (Parameter)userObj;
						this.setText(parameter.getName());
					}
				}
				return this;
			}
		});
		this.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem() instanceof TreePath) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode)((TreePath)e.getItem()).getLastPathComponent();
						if (node.getUserObject() instanceof Parameter) {
							ParameterTreeComboBox.this.getEditor().setItem(((Parameter)node.getUserObject()).getName());
						} else {
							ParameterTreeComboBox.this.getEditor().setItem(null);
						}
					}
				}
			}
		});
		this.setEditable(true);
	}

	public String getSelectedParameterName() {
		Object obj = this.getSelectedItem();
		if (obj instanceof TreePath) {
			return ((Parameter)((ExpandMutableTreeNode)((TreePath)obj).getLastPathComponent()).getUserObject()).getName();
		}
		return (String)obj;
	}

	@Override
	public void setSelectedItem(Object o) {
		if (o instanceof String) {
			this.setSelectedItemString((String)o);
			return;
		}
		this.tree.setSelectionPath((TreePath)o);
		this.getModel().setSelectedItem(o);
	}

	public void setSelectedParameterName(String name) {
		DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
		DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
		DefaultMutableTreeNode leaf = rootTreeNode.getFirstLeaf();
		do {
			if (leaf.getUserObject() instanceof Parameter) {
				if (ComparatorUtils.equals(((Parameter) leaf.getUserObject()).getName(), name)) {
					TreePath visiblePath = new TreePath(treeModel.getPathToRoot(leaf));
					tree.setSelectionPath(visiblePath);
					this.setSelectedItem(visiblePath);
					break;
				}
			}
		} while ((leaf = leaf.getNextLeaf()) != null);
		if (this.getSelectedItem() == null) {
			((ComboBoxModel)treeModel).setSelectedItem(name);
		}
	}

    /**
     * 刷新目录树
     */
	public void refreshTree() {
		DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
		rootTreeNode.removeAllChildren();
		addParameterTreeNode(rootTreeNode);
		DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
		if (treeModel != null) {
			treeModel.reload();
		}
	}

	private void addParameterTreeNode(DefaultMutableTreeNode rootTreeNode) {
		ParameterGroup[] groups = getParameterGroup();
		for (ParameterGroup group : groups) {
			ExpandMutableTreeNode paraTreeNode = new ExpandMutableTreeNode(group.getGroupName());
			rootTreeNode.add(paraTreeNode);
			for (Parameter parameter : group.getParameter()) {
				if (parameter != null) {
					ExpandMutableTreeNode childTreeNode = new ExpandMutableTreeNode(parameter);
					paraTreeNode.add(childTreeNode);
				}
			}
		}
	}

	private ParameterGroup[] getParameterGroup() {
		List<ParameterGroup> groupList = new ArrayList<ParameterGroup>();
		Parameter[] parameters;
		DesignModelAdapter<?,?> model = DesignModelAdapter.getCurrentModelAdapter();
		if (model != null) {
			// 报表参数
			parameters = model.getReportParameters();
			if (!ArrayUtils.isEmpty(parameters)) {
				groupList.add(new ParameterGroup(Inter.getLocText("ParameterD-Report_Parameter"), parameters));
			}
			// 数据源参数
			parameters = model.getTableDataParameters();
			if (!ArrayUtils.isEmpty(parameters)) {
				groupList.add(new ParameterGroup(Inter.getLocText("FR-Designer_Datasource-Parameter"), parameters));
			}
		}
		
		// 全局参数
		parameters = ServerConfig.getInstance().getGlobeParameters();
		if (!ArrayUtils.isEmpty(parameters)) {
			groupList.add(new ParameterGroup(Inter.getLocText("M_Server-Global_Parameters"), parameters));
		}
		// 全局数据源参数
		parameters = new Parameter[0];
		Calculator c = Calculator.createCalculator();
		TableDataConfig tableDataConfig = TableDataConfig.getInstance();
		Iterator<String> nameIt = tableDataConfig.getTableDatas().keySet().iterator();
		while (nameIt.hasNext()) {
			TableData tableData = tableDataConfig.getTableData(nameIt.next());
			ParameterProvider[] ps = tableData.getParameters(c);
			if (!ArrayUtils.isEmpty(ps)) {
				parameters = (Parameter[])ArrayUtils.addAll(parameters, ps);
			}
		}
		if (!ArrayUtils.isEmpty(parameters)) {
			groupList.add(new ParameterGroup(Inter.getLocText(new String[]{"Server", "Datasource-Datasource", "Parameter"}), parameters));
		}
		return groupList.toArray(new ParameterGroup[0]);

	}

}