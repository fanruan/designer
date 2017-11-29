package com.fr.design.mainframe;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.*;

import com.fr.design.actions.UndoableAction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.designer.beans.actions.ChangeNameAction;
import com.fr.design.designer.beans.actions.FormUndoableAction;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itoolbar.UIToolBarUI;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.mainframe.widget.UITreeComboBox;

import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.parameter.HierarchyTreePane;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.treeview.ComponentTreeModel;

import com.fr.design.gui.ilable.UILabel;


import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.layout.FRGUIPaneFactory;


/**
 * 显示表单层次结构的树
 */
public class FormHierarchyTreePane extends FormDockView implements HierarchyTreePane {

	public static final int NODE_LENGTH = 2;
	public static final int PARA = 0;
	public static final int BODY = 1;

	private ShortCut4JControlPane[] shorts;
	private ComponentTree componentTree;
//	private UITreeComboBox treeComboBox;
    private ChangeNameAction changeVarNameAction;

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
		setLayout(new BorderLayout(0, 0));
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
	}

	@Override
	/**
	 * 刷新
	 */
	public void refreshDockingView() {
		FormDesigner formDesigner = this.getEditingFormDesigner();
		removeAll();
		changeVarNameAction = null;
		if(this.componentTree != null) {
			this.componentTree.removeAll();
		}
//		if(this.treeComboBox != null) {
//			this.treeComboBox.removeAll();
//		}
		if (formDesigner == null) {
			clearDockingView();
			return;
		}
		componentTree = new ComponentTree(formDesigner);
		formDesigner.addDesignerEditListener(new DesignerEditListener() {
			@Override
			public void fireCreatorModified(DesignerEvent evt) {
				componentTree.setAndScrollSelectionPath(componentTree.getSelectedTreePath());
			}
		});

		ComponentTreeModel treeModel = (ComponentTreeModel) componentTree.getModel();
		XCreator root = (XCreator)treeModel.getRoot();
		int childCount = treeModel.getChildCount(root);
		//按照节点添加para在下的，但这里需要para节点在上，调整一下位置
		if(childCount == NODE_LENGTH){
			adjustPosition(treeModel, formDesigner);
		}

		add(getWidgetPane(), BorderLayout.CENTER);
	}

	private JPanel getWidgetPane() {
		shorts = createShortcuts();

		JPanel widgetPane = new JPanel();
		widgetPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		widgetPane.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel headPane = new JPanel(new BorderLayout());
		headPane.add(new UILabel(Inter.getLocText("FR-Designer-Selected_Widget") + "  ",
				SwingConstants.HORIZONTAL), BorderLayout.WEST);
		headPane.add(getToolBarPane(), BorderLayout.EAST);

		widgetPane.add(headPane, BorderLayout.CENTER);
		UIScrollPane scrollPane = new UIScrollPane(componentTree);
		scrollPane.setPreferredSize(new Dimension(210, 170));
//		treeComboBox = new UITreeComboBox(componentTree);
		widgetPane.add(scrollPane, BorderLayout.SOUTH);
		return widgetPane;
	}

	private JPanel getToolBarPane() {
		ToolBarDef toolbarDef = new ToolBarDef();
		for (ShortCut4JControlPane sj : shorts) {
			toolbarDef.addShortCut(sj.getShortCut());
		}
		UIToolbar toolBar = ToolBarDef.createJToolBar();
        toolBar.setUI(new UIToolBarUI(){
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(245, 245, 247));
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });
		toolbarDef.updateToolBar(toolBar);
		JPanel toolBarPane = new JPanel(new BorderLayout());
		toolBarPane.add(toolBar, BorderLayout.CENTER);
		return toolBarPane;
	}

	protected ShortCut4JControlPane[] createShortcuts() {
        ArrayList<ShortCut4JControlPane> shortCutList = new ArrayList<>();
        FormDesigner designer = getEditingFormDesigner();
		if (changeVarNameAction == null) {
			changeVarNameAction = new ChangeNameAction(designer);
		}
		shortCutList.add(new WidgetEnableShortCut(changeVarNameAction));
        for (Action action : designer.getActions()) {
            shortCutList.add(new WidgetEnableShortCut((UndoableAction)action));
        }

        return shortCutList.toArray(new ShortCut4JControlPane[shortCutList.size()]);
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

	@Override
	/**
	 * 位置
	 *
	 * @return 位置
	 */
	public Location preferredLocation() {
		return Location.WEST_BELOW;
	}

	private class WidgetEnableShortCut extends ShortCut4JControlPane {
		public WidgetEnableShortCut(ShortCut shortCut) {
			this.shortCut = shortCut;
		}

		/**
		 * 检查是否可用
		 */
		@Override
		public void checkEnable() {
			this.shortCut.setEnabled(false);
		}
	}
}