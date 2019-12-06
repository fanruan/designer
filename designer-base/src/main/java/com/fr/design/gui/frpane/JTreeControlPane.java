package com.fr.design.gui.frpane;

import com.fr.base.BaseUtils;
import com.fr.data.impl.TreeNodeAttr;
import com.fr.data.impl.TreeNodeWrapper;
import com.fr.design.actions.UpdateAction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.controlpane.ControlPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.ToolBarDef;
import com.fr.form.ui.TreeEditor;
import com.fr.general.ComparatorUtils;

import com.fr.general.NameObject;
import com.fr.stable.Nameable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class JTreeControlPane extends ControlPane {
    // 添加一个treeNode
    private AddTreeNodeAction addTreeNode;
    // 移除一个treeNode
    private RemoveTreeNodeAction removeTreeNode;

    private BasicBeanPane updatePane;

    private JTree tree;
    private DefaultTreeModel defaultTreeModel;


    boolean isEditor = false;

    private UICheckBox isPerformanceFirst;

    public JTreeControlPane(NameableCreator[] creators, BasicBeanPane updatePane, boolean isEditor) {
        this.initComponents(creators, updatePane);
        this.isEditor = isEditor;
    }

    private void initComponents(NameableCreator[] creators, BasicBeanPane updatePane) {
        this.setLayout(new BorderLayout(2, 2));
        this.updatePane = updatePane;
        // LeftPane
        JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel northPane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Root"));
        defaultTreeModel = new DefaultTreeModel(rootNode);
        DefaultMutableTreeNode firstLayer = new DefaultMutableTreeNode(new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Gradation") + 1, new TreeNodeAttr()));
        tree = new JTree(defaultTreeModel);
        tree.setRootVisible(false);
        ((DefaultMutableTreeNode) defaultTreeModel.getRoot()).getLastLeaf().add(firstLayer);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        leftPane.add(new UIScrollPane(tree), BorderLayout.CENTER);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                JTreeControlPane.this.updatePane.updateBean();
                refreshCurrentUpdatePane();
                checkButtonEnabled();
            }
        });
        tree.setPreferredSize(new Dimension(170, 350));
        tree.setCellRenderer(renderer);

        // JTreeControlPane控制栏
        ToolBarDef toolbarDef = new ToolBarDef();
        toolbarDef.addShortCut(addTreeNode = new AddTreeNodeAction(creators));
        toolbarDef.addShortCut(removeTreeNode = new RemoveTreeNodeAction());
        UIToolbar toolBar = ToolBarDef.createJToolBar();
        toolbarDef.updateToolBar(toolBar);
        leftPane.add(toolBar, BorderLayout.NORTH);


        isPerformanceFirst = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Performance_First"));
        northPane.add(new UILabel("                 "));
        northPane.add(isPerformanceFirst);
        this.add(northPane, BorderLayout.NORTH);
        this.add(leftPane, BorderLayout.WEST);
        this.add(this.updatePane, BorderLayout.CENTER);

        this.checkButtonEnabled();
    }

    @Override
    protected String title4PopupWindow() {
        return "tree";
    }

    private void checkButtonEnabled() {
        this.addTreeNode.setEnabled(true);
        this.removeTreeNode.setEnabled(true);

        // richer:当选择了树根节点时,不能被删除、上移和下移
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
        TreePath rootPath = new TreePath(defaultTreeModel.getPathToRoot(root));
        if (ComparatorUtils.equals(rootPath, tree.getSelectionPath())) {
            this.removeTreeNode.setEnabled(false);
        }
    }

    public void populate(NameObject nameObject) {
        // 重新添加tree节点的时候需要remove掉原来的所有子节点
        ((DefaultMutableTreeNode) defaultTreeModel.getRoot()).removeAllChildren();
        Object obj = nameObject.getObject();
        TreeNodeAttr[] treeNodeAttr = null;
        if (obj instanceof TreeNodeAttr[]) {
            treeNodeAttr = ((TreeNodeAttr[]) obj);
            isPerformanceFirst.setSelected(false);
        } else if (obj instanceof TreeEditor) {
            TreeEditor treeEditor = (TreeEditor) obj;
            treeNodeAttr = treeEditor.getTreeNodeAttr();
            isPerformanceFirst.setSelected(treeEditor.isPerformanceFirst());
        } else if (obj instanceof TreeNodeWrapper) {
            treeNodeAttr = ((TreeNodeWrapper) obj).getTreeNodeAttrs();
            isPerformanceFirst.setSelected(((TreeNodeWrapper) obj).isPerformanceFirst());
        }

        int count = treeNodeAttr == null ? 0 : treeNodeAttr.length;
        //将树的层次一层一层的加上去
        DefaultMutableTreeNode node4root = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
        for (int i = 0; i < count; i++) {

            DefaultMutableTreeNode node4add = new DefaultMutableTreeNode(
                    new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Gradation") + (i + 1), treeNodeAttr[i]));
            node4root.add(node4add);
            node4root = node4add;
        }

        defaultTreeModel.reload();
        expandAll(tree, true);
        tree.setSelectionRow(0);
    }

    public NameObject update() {
        if (isEditor) {
            TreeEditor treeEditor = new TreeEditor();

            treeEditor.setTreeNodeAttr(updateTreeNodeAttr());

            treeEditor.setPerformanceFirst(isPerformanceFirst.isSelected());

            return new NameObject("tree", treeEditor);
        } else {
            TreeNodeWrapper treeNodeWrapper = new TreeNodeWrapper(isPerformanceFirst.isSelected(), updateTreeNodeAttr());
            return new NameObject("tree", treeNodeWrapper);
        }
    }

    public TreeNodeAttr[] updateTreeNodeAttr() {
        updatePane.updateBean();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
        List<TreeNodeAttr> nodeList = new ArrayList<TreeNodeAttr>();

        for (; root != null; root = root.getNextNode()) {
            if (!(root.getUserObject() instanceof NameObject)) {
                continue;
            }
            NameObject no = (NameObject) root.getUserObject();
            if (no.getObject() instanceof TreeNodeAttr) {
                nodeList.add((TreeNodeAttr) no.getObject());
            }
        }

        return nodeList.toArray(new TreeNodeAttr[nodeList.size()]);
    }

    public void refreshCurrentUpdatePane() {
        TreePath selectTreePath = this.tree.getSelectionPath();
        if (selectTreePath != null) {
            Object object = ((DefaultMutableTreeNode) selectTreePath.getLastPathComponent()).getUserObject();
            if (object != null) {
                JTreeControlPane.this.updatePane.populateBean(object);
            }
        }
    }


    private class AddTreeNodeAction extends UpdateAction {
        private NameableCreator creator;

        public AddTreeNodeAction(NameableCreator[] creators) {
            this.creator = creators[0];
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add"));
            this.setMnemonic('A');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
        }

        public void actionPerformed(ActionEvent e) {
            // TODO add tree node
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
            Nameable nameable = creator.createNameable(new UnrepeatedNameHelper() {

                @Override
                public String createUnrepeatedName(String prefix) {
                    DefaultMutableTreeNode innerNode = node;
                    int nodeCount = 0;
                    do {
                        nodeCount++;
                    } while ((innerNode = innerNode.getNextNode()) != null);
                    return prefix + nodeCount;
                }
            });


            node.getLastLeaf().add(new DefaultMutableTreeNode(nameable));
            defaultTreeModel.reload();
            TreePath path = new TreePath(defaultTreeModel.getPathToRoot(node.getLastLeaf()));
            tree.setSelectionPath(path);
        }
    }

    private class RemoveTreeNodeAction extends UpdateAction {
        public RemoveTreeNodeAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"));
            this.setMnemonic('R');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
        }

        public void actionPerformed(ActionEvent e) {
            // TODO remove tree node
            int val = FineJOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Are_You_Sure_To_Remove_The_Selected_Item") + "?",
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (val != JOptionPane.OK_OPTION) {
                return;
            }
            TreePath selectionPath = tree.getSelectionPath();
            DefaultMutableTreeNode tmpNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
            tmpNode.removeFromParent();
            defaultTreeModel.reload();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
            TreePath path = new TreePath(defaultTreeModel.getPathToRoot(node.getLastLeaf()));
            tree.setSelectionPath(path);
        }
    }

    TreeCellRenderer renderer = new DefaultTreeCellRenderer() {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            if (leaf) {
                this.setIcon(BaseUtils.readIcon("com/fr/design/images/data/default_widget.png"));
            } else {
                this.setIcon(BaseUtils.readIcon("com/fr/design/images/data/arrow_branch.png"));
            }

            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObj = node.getUserObject();
                if (userObj instanceof NameObject) {
                    this.setText(((NameObject) userObj).getName());
                }
            }
            this.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
            return this;
        }
    };
}
