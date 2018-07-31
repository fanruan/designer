package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UndoableAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.treeview.ComponentTreeModel;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itoolbar.UIToolBarUI;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.parameter.HierarchyTreePane;


import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;


/**
 * 显示表单层次结构的树
 */
public class FormHierarchyTreePane extends FormDockView implements HierarchyTreePane {

	private static final int NODE_LENGTH = 2;
	private static final int PARA = 0;
	private static final int BODY = 1;
    private static final int SHORTS_SEPARATOR_POS = 4;
    private static final int TOOLBAR_PADDING_RIGHT = 10;

	private ShortCut4JControlPane[] shorts;
	private ComponentTree componentTree;

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
		return com.fr.design.i18n.Toolkit.i18nText("Form-Hierarchy_Tree");
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
		if(this.componentTree != null) {
			this.componentTree.removeAll();
		}
		if (formDesigner == null) {
			clearDockingView();
			return;
		}
		componentTree = new ComponentTree(formDesigner);
		formDesigner.addDesignerEditListener(new DesignerEditListener() {
			@Override
			public void fireCreatorModified(DesignerEvent evt) {
				refreshComponentTree();
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

		// 这里要刷新一下，否则控件树中没有任何一个控件处于选中状态
		refreshComponentTree();
	}

	private void refreshComponentTree() {
		componentTree.setAndScrollSelectionPath(componentTree.getSelectedTreePath());
		componentTree.refreshUI();
	}

	private JPanel getWidgetPane() {
		shorts = createShortcuts();

		JPanel widgetPane = new JPanel();
		widgetPane.setLayout(FRGUIPaneFactory.createBorderLayout());

		widgetPane.add(getToolBarPane(), BorderLayout.CENTER);
		UIScrollPane scrollPane = new UIScrollPane(componentTree);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setPreferredSize(new Dimension(210, 170));
		widgetPane.add(scrollPane, BorderLayout.SOUTH);
		return widgetPane;
	}

	private JPanel getToolBarPane() {
		UIToolbar toolBar = ToolBarDef.createJToolBar();
        toolBar.setUI(new UIToolBarUI(){
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(245, 245, 247));
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });
        for (int i = 0; i < shorts.length; i++) {
            if (i == SHORTS_SEPARATOR_POS) {
                toolBar.addSeparator(new Dimension(2, 16));
            }
            shorts[i].getShortCut().intoJToolBar(toolBar);
        }

		JPanel toolBarPane = new JPanel(new BorderLayout());
		toolBarPane.add(toolBar, BorderLayout.CENTER);
		toolBarPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BARNOMAL));
		JPanel toolBarPaneWrapper = new JPanel(new BorderLayout());
		toolBarPaneWrapper.add(toolBarPane, BorderLayout.CENTER);
		toolBarPaneWrapper.setBorder(BorderFactory.createEmptyBorder(1, 0, 2, TOOLBAR_PADDING_RIGHT));
		return toolBarPaneWrapper;
	}

	protected ShortCut4JControlPane[] createShortcuts() {
        ArrayList<ShortCut4JControlPane> shortCutList = new ArrayList<>();
        FormDesigner designer = getEditingFormDesigner();

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