package com.fr.design.mainframe;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.parameter.HierarchyTreePane;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.treeview.ComponentTreeModel;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;

import javax.swing.JComponent;

import com.fr.design.gui.ilable.UILabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fr.design.gui.itextfield.UITextField;

import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreePath;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.layout.FRGUIPaneFactory;


/**
 * 显示表单层次结构的树
 */
public class FormHierarchyTreePane extends FormDockView implements HierarchyTreePane {

	public static final int NODE_LENGTH = 2;
	public static final int PARA = 0;
	public static final int BODY = 1;
	
	private ComponentTree componentTree;
	// richer:搜寻树节点的的文本框
	private UITextField searchTextField;
	private SearchResultPane searchResult;

	public static FormHierarchyTreePane getInstance() {
		return HOLDER.singleton;
	}

	public static FormHierarchyTreePane getInstance(FormDesigner editor) {
		HOLDER.singleton.setEditingFormDesigner(editor);
		HOLDER.singleton.refreshDockingView();
		return HOLDER.singleton;
	}

	private static class HOLDER {
		private static FormHierarchyTreePane singleton = new FormHierarchyTreePane();
	}

	private FormHierarchyTreePane() {
		setLayout(new BorderLayout(0, 6));
	}

	@Override
	public String getViewTitle() {
		return Inter.getLocText("Form-Hierarchy_Tree");
	}

	@Override
	public Icon getViewIcon() {
		return BaseUtils.readIcon("/com/fr/design/images/m_report/tree.png");
	}

	public ComponentTree getComponentTree() {
		return componentTree;
	}

	/**
	 * 清除
	 */
	public void clearDockingView() {
		this.componentTree = null;
		this.searchTextField = null;
		this.searchResult = null;
		add(new JScrollPane(), BorderLayout.CENTER);
	}

	@Override
	/**
	 * 刷新
	 */
	public void refreshDockingView() {
		FormDesigner formDesigner = this.getEditingFormDesigner();
		removeAll();
		if (formDesigner == null) {
			clearDockingView();
			return;
		}
		componentTree = new ComponentTree(formDesigner);
		
		ComponentTreeModel treeModel = (ComponentTreeModel) componentTree.getModel();
		XCreator root = (XCreator)treeModel.getRoot();
		int childCount = treeModel.getChildCount(root);
		//按照节点添加para在下的，但这里需要para节点在上，调整一下位置
		if(childCount == NODE_LENGTH){
			adjustPosition(treeModel,formDesigner);
		}
		UIScrollPane scrollPane = new UIScrollPane(componentTree);
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);
		JPanel searchPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		add(searchPane, BorderLayout.NORTH);
		searchPane.add(new UILabel(Inter.getLocText("FR-Designer_Search") + ":",
				SwingConstants.HORIZONTAL), BorderLayout.WEST);
		searchTextField = new UITextField();
		searchPane.add(searchTextField, BorderLayout.CENTER);
		searchTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				search();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				search();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				search();
			}

			private void search() {
				String text = searchTextField.getText();
				if (StringUtils.isEmpty(text)) {
					removeSearchResult();
				} else {
					populate(componentTree.search(text));
				}
			}
		});
	}
	
	/**
	 * 调整结构树para和body的位置
	 * 
	 * @param treeModel
	 * @param formDesigner
	 */
	private void adjustPosition(ComponentTreeModel treeModel,FormDesigner formDesigner){
		XCreator root = (XCreator)treeModel.getRoot();
		XCreator firstChild = (XCreator)treeModel.getChild(root,PARA);
		if(firstChild.acceptType(XWParameterLayout.class)){
			return;
		}
		// 绝对布局作为body的时候
		// 获取第一个子节点的方法中屏蔽了fit
		// 这边另外处理一下
		else if (firstChild.acceptType(XWAbsoluteBodyLayout.class) && firstChild.getBackupParent() != null) {
			firstChild = firstChild.getBackupParent();
		}
		root.add(firstChild,BODY);
		treeModel.setRoot(root);
		componentTree = new ComponentTree(formDesigner,treeModel);
	}

	/**
	 * 刷新树
	 */
	public void refreshRoot() {
		if (componentTree == null) {
			return;
		}
		componentTree.refreshTreeRoot();
	}

	/**
	 * 删除搜索结果
	 */
	public void removeSearchResult() {
		componentTree.setSelectionPath(null);
		if (searchResult != null) {
			this.remove(searchResult);
		}
	}

	public void populate(TreePath[] treepath) {
		if (this.searchResult == null) {
			searchResult = new SearchResultPane();
		}
		if (((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.SOUTH) == null) {
			add(searchResult, BorderLayout.SOUTH);
		}
		searchResult.populate(treepath);
	}

	private class SearchResultPane extends JPanel {
		private UILabel resultLabel = new UILabel();
		private BackAction backAction = new BackAction();
		private ForWardAction forwardAction = new ForWardAction();
		private TreePath[] tree;
		private int number = 0;

		SearchResultPane() {
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			JPanel actionJPanel = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
			addButtonToJPanel(actionJPanel, backAction.createToolBarComponent());
			addButtonToJPanel(actionJPanel, forwardAction.createToolBarComponent());

			this.add(actionJPanel, BorderLayout.EAST);
			this.add(resultLabel, BorderLayout.WEST);
		}

		private void addButtonToJPanel(JPanel actionLabel,
									   JComponent toolBarComponent) {
			actionLabel.add(toolBarComponent);
			if (toolBarComponent instanceof UIButton) {
				toolBarComponent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			}
		}

		public void populate(TreePath[] search) {
			tree = search;
			resultLabel.setText(Inter.getLocText("FR-Designer_Total") + ":" + tree.length);
			number = 0;
			check();
		}

		public void next() {
			if (number < tree.length - 1) {
				componentTree.setAndScrollSelectionPath(tree[++number]);
			}
			check();
		}

		public void last() {
			if (number > 0) {
				componentTree.setAndScrollSelectionPath(tree[--number]);
			}
			check();
		}

		public void check() {
			if (tree.length < 1) {
				backAction.setEnabled(false);
				forwardAction.setEnabled(false);
			} else {
				backAction.setEnabled(number > 0);
				forwardAction.setEnabled(number < tree.length - 1);
			}

		}

	}

	private class BackAction extends UpdateAction {

		public BackAction() {
			this.setName(Inter.getLocText("Form-Hierarchy_Tree_Last"));
			this.setSmallIcon(BaseUtils
					.readIcon("com/fr/design/images/m_help/back.png"));
			this.setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			searchResult.last();
		}
	}

	private class ForWardAction extends UpdateAction {

		public ForWardAction() {
			this.setName(Inter.getLocText("Form-Hierarchy_Tree_Next"));
			this.setSmallIcon(BaseUtils
					.readIcon("com/fr/design/images/m_help/forward.png"));
			this.setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			searchResult.next();
		}
	}

	@Override
	/**
	 * 位置
	 * 
	 * @return 位置
	 */
	public Location preferredLocation() {
		return Location.WEST_BELOW;
	}
}