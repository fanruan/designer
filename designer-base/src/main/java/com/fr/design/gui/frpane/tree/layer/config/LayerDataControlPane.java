package com.fr.design.gui.frpane.tree.layer.config;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.controlpane.ControlPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.ToolBarDef;
import com.fr.form.ui.tree.LayerConfig;
import com.fr.general.ComparatorUtils;

import com.fr.general.NameObject;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by juhaoyu on 16/9/21.
 */
public class LayerDataControlPane extends ControlPane {

    public static final String BEAN_NAME = "Tree Layer Data";

    // 添加一个treeNode
    private AddTreeNodeAction addTreeNode;

    // 移除一个treeNode
    private RemoveTreeNodeAction removeTreeNode;

    private LayerDataConfigPane configPane;

    private JTree tree;

    private DefaultTreeModel defaultTreeModel;

    public LayerDataControlPane() {

        this.setLayout(new BorderLayout(2, 2));
        //创建层编辑panel
        configPane = new LayerDataConfigPane();
        //创建树结构及树控件
        JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Root"));
        defaultTreeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(defaultTreeModel);
        DefaultMutableTreeNode firstLayer = new DefaultMutableTreeNode(new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Gradation") + 1, new LayerConfig(1)));
        tree.setRootVisible(false);
        ((DefaultMutableTreeNode) defaultTreeModel.getRoot()).getLastLeaf().add(firstLayer);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        leftPane.add(new UIScrollPane(tree), BorderLayout.CENTER);

        tree.setPreferredSize(new Dimension(170, 350));
        tree.setCellRenderer(renderer);

        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {

                configPane.updateBean();
                refreshCurrentUpdatePane();
                checkButtonEnabled();
            }
        });


        // JTreeControlPane控制栏
        ToolBarDef toolbarDef = new ToolBarDef();
        toolbarDef.addShortCut(addTreeNode = new AddTreeNodeAction());
        toolbarDef.addShortCut(removeTreeNode = new RemoveTreeNodeAction());
        UIToolbar toolBar = ToolBarDef.createJToolBar();
        toolbarDef.updateToolBar(toolBar);
        leftPane.add(toolBar, BorderLayout.NORTH);


        this.add(leftPane, BorderLayout.WEST);
        this.add(this.configPane, BorderLayout.CENTER);

        defaultTreeModel.reload();
        TreePath path = new TreePath(defaultTreeModel.getPathToRoot(rootNode.getLastLeaf()));
        tree.setSelectionPath(path);

        this.checkButtonEnabled();

    }

    public void refreshCurrentUpdatePane() {

        TreePath selectTreePath = this.tree.getSelectionPath();
        if (selectTreePath != null) {
            NameObject object = (NameObject) ((DefaultMutableTreeNode) selectTreePath.getLastPathComponent()).getUserObject();
            if (object != null && object.getObject() != null) {
                configPane.populateBean((LayerConfig) object.getObject());
            }
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

    private class AddTreeNodeAction extends UpdateAction {


        public AddTreeNodeAction() {

            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add"));
            this.setMnemonic('A');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
        }

        public void actionPerformed(ActionEvent e) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) defaultTreeModel.getRoot();

            DefaultMutableTreeNode innerNode = node;
            int nodeCount = 0;
            do {
                nodeCount++;
            } while ((innerNode = innerNode.getNextNode()) != null);
            NameObject nameable = new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Gradation") + nodeCount, new LayerConfig(nodeCount));

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

    public void populate(NameObject nameObject) {
        // 重新添加tree节点的时候需要remove掉原来的所有子节点
        ((DefaultMutableTreeNode) defaultTreeModel.getRoot()).removeAllChildren();
        if (BEAN_NAME.equals(nameObject.getName())) {
            Object obj = nameObject.getObject();
            LayerConfig[] layerConfigs = null;
            if (obj instanceof LayerConfig[]) {
                layerConfigs = ((LayerConfig[]) obj);
            }

            int count = layerConfigs == null ? 0 : layerConfigs.length;
            //将树的层次一层一层的加上去
            DefaultMutableTreeNode node4root = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
            for (int i = 0; i < count; i++) {

                DefaultMutableTreeNode node4add = new DefaultMutableTreeNode(
                    new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Gradation") + (i + 1), layerConfigs[i].clone()));
                node4root.add(node4add);
                node4root = node4add;
            }

            defaultTreeModel.reload();
            expandAll(tree, true);
            tree.setSelectionRow(0);
        }
    }

    public NameObject update() {

        return new NameObject(BEAN_NAME, updateLayerDatas());
    }

    private LayerConfig[] updateLayerDatas() {

        //保存最后一个设置的层级
        configPane.updateBean();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
        java.util.List<LayerConfig> nodeList = new ArrayList<LayerConfig>();

        for (; root != null; root = root.getNextNode()) {
            if (!(root.getUserObject() instanceof NameObject)) {
                continue;
            }
            NameObject no = (NameObject) root.getUserObject();
            if (no.getObject() instanceof LayerConfig) {
                nodeList.add((LayerConfig) no.getObject());
            }
        }

        return nodeList.toArray(new LayerConfig[nodeList.size()]);
    }


    @Override
    protected String title4PopupWindow() {

        return "Layer Data Control Pane";
    }

}
