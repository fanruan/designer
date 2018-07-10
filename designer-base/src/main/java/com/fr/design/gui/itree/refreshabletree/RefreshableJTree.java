package com.fr.design.gui.itree.refreshabletree;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.itooltip.UIToolTip;
import com.fr.design.gui.itree.checkboxtree.CheckBoxTree;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class RefreshableJTree extends CheckBoxTree {
    private static final int WIDTH_BETWEEN_NODES = 20; //tree父子节点之间最左侧横向像素的差
    private Icon icon;

    public static final Object PENDING = new Object() {

        @Override
        public String toString() {
            return Inter.getLocText("Loading") + "...";
        }
    };

    public RefreshableJTree() {
        this(null);
    }
    public boolean isCheckBoxVisible(TreePath path) {
        return false;
    }
    public RefreshableJTree(Object rootObj) {
        super(new DefaultTreeModel(new ExpandMutableTreeNode(rootObj)));
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        ExpandMutableTreeNode root = (ExpandMutableTreeNode) model.getRoot();
        root.setExpanded(true);
        this.setRootVisible(false);
        this.setBackground(UIConstants.TREE_BACKGROUND);
        this.addTreeExpansionListener(expansion);
        this.addTreeWillExpandListener(willExpand);
    }

    private TreeExpansionListener expansion = new TreeExpansionListener() {

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            TreePath treePath = event.getPath();
            if (treePath == null) {
                return;
            }

            ExpandMutableTreeNode treeNode = ((ExpandMutableTreeNode) treePath.getLastPathComponent());
            treeNode.setExpanded(false);
        }

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            TreePath treePath = event.getPath();
            if (treePath == null) {
                return;
            }

            ExpandMutableTreeNode treeNode = ((ExpandMutableTreeNode) treePath.getLastPathComponent());
            treeNode.setExpanded(true);
        }
    };
    private TreeWillExpandListener willExpand = new TreeWillExpandListener() {

        @Override
        public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        }

        @Override
        public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
            TreePath treePath = event.getPath();
            if (treePath == null) {
                return;
            }

            final ExpandMutableTreeNode treeNode = ((ExpandMutableTreeNode) treePath.getLastPathComponent());

            if (treeNode.getChildCount() == 1 && ((ExpandMutableTreeNode) treeNode.getFirstChild()).getUserObject() == PENDING) {
                //如果空文件夹是只有一个默认增加的正在加载的子节点，则不显示正在加载，就是空就行了
                new SwingWorker<Long, Void>() {

                    @Override
                    protected Long doInBackground() throws Exception {
                        long startTime = System.currentTimeMillis();
                        ExpandMutableTreeNode[] nodes = RefreshableJTree.this.loadChildTreeNodes(treeNode);
                        for (int i = 0; i < nodes.length; i++) {
                            treeNode.add(nodes[i]);
                        }
                        DefaultTreeModel treeModel = (DefaultTreeModel) RefreshableJTree.this.getModel();
                        // 主要耗时是用在了treeUI的渲染上了，所以把这个放到工作线程里面
                        if (treeNode.getChildCount() >= 1 && ((ExpandMutableTreeNode) treeNode.getFirstChild()).getUserObject() == PENDING) {
                            treeNode.remove(0);
                        }
                        treeModel.nodeStructureChanged(treeNode);
                        long usedTime = System.currentTimeMillis() - startTime;
                        return usedTime;
                    }

                    @Override
                    protected void done() {
                        RefreshableJTree.this.updateUI();
                        // 恢复Tree的可用性
                        RefreshableJTree.this.setEnabled(true);
                    }

                }.execute();
            }
        }
    };

    /**
     * @return
     */
    public boolean isTemplateShowing() {
        return ((ExpandMutableTreeNode) this.getModel().getRoot()).getChildCount() == 0 ? false : true;
    }

    /*
      * 刷新
      */
    public void refresh() {
        refresh((ExpandMutableTreeNode) this.getModel().getRoot(), StringUtils.EMPTY);
    }

    public void refreshChildByName(String childName) {
        refresh((ExpandMutableTreeNode) this.getModel().getRoot(), childName);
    }

    /*
      * 刷新expandRoot节点下所有已打开的节点的UserObject,并打开isExpanded为true的TreeNode
      */
    private void refresh(ExpandMutableTreeNode expandRoot, String childName) {
        if (expandRoot == null) {
            return;
        }
        refreshTreeNode(expandRoot, childName);

        // model.reload, then do expand treenode that isExpanded is true
        ((DefaultTreeModel) this.getModel()).reload(expandRoot);

        // 展开所有isExpanded为true的TreeNode
        expandRoot.expandCurrentTreeNode(this);
    }

    /*
      * 刷新eTreeNode下面所有的已完成过取数的非叶子节点的子叶内容UserObject
      */
    protected void refreshTreeNode(ExpandMutableTreeNode eTreeNode, String childName) {
        // 如果eTreeNode是未取数状态,不用expand
        if (interceptRefresh(eTreeNode)) {
            return;
        }

        // 刷新当前eTreeNode下面的子节点的UserObject的数组
        ExpandMutableTreeNode[] new_nodes = loadChildTreeNodes(eTreeNode);

        /*
           * 保存下当前eTreeNode下的ChildTreeNode于childTreeNodeList 移除所有ChildTreeNode
           * 根据childUserObjects与childTreeNodeList的比对,重新构建eTreeNode
           */
        java.util.List<DefaultMutableTreeNode> childTreeNodeList = new java.util.ArrayList<DefaultMutableTreeNode>();
        for (int i = 0, len = eTreeNode.getChildCount(); i < len; i++) {
            if (eTreeNode.getChildAt(i) instanceof ExpandMutableTreeNode) {
                childTreeNodeList.add((ExpandMutableTreeNode) eTreeNode.getChildAt(i));
            } else {
                childTreeNodeList.add((DefaultMutableTreeNode) eTreeNode.getChildAt(i));
            }
        }

        eTreeNode.removeAllChildren();

        for (int ci = 0; ci < new_nodes.length; ci++) {
            Object cUserObject = new_nodes[ci].getUserObject();

            for (int ni = 0, nlen = childTreeNodeList.size(); ni < nlen; ni++) {
                ExpandMutableTreeNode cTreeNode = (ExpandMutableTreeNode) childTreeNodeList.get(ni);
                if (ComparatorUtils.equals(cTreeNode.getUserObject(), cUserObject)) {
                    new_nodes[ci].setExpanded(cTreeNode.isExpanded());
                    break;
                }
            }

            eTreeNode.add(new_nodes[ci]);
        }
    }

    /*
      * 判断eTreeNode是否需要Refresh,可提前中止,返回true则表示提前中止,不需要Refresh
      */
    protected abstract boolean interceptRefresh(ExpandMutableTreeNode eTreeNode);

    /*
      * 得到treeNode的子节点ExpandMutableTreeNode的数组
      */
    protected abstract ExpandMutableTreeNode[] loadChildTreeNodes(ExpandMutableTreeNode treeNode);

    public NameObject getSelectedNameObject() {
        TreePath selectedTreePath = this.getSelectionPath();
        if (selectedTreePath == null) {
            return null;
        }

        ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
        Object selectedUserObject = selectedTreeNode.getUserObject();
        if (selectedUserObject instanceof NameObject) {
            return (NameObject) selectedUserObject;
        }

        selectedTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getParent();
        selectedUserObject = selectedTreeNode.getUserObject();
        if (selectedUserObject instanceof NameObject) {
            return (NameObject) selectedUserObject;
        }
        return null;
    }

    public String getToolTipText(MouseEvent event) {
        String tip = null;
        icon = new ImageIcon();

        if (event != null) {
            Point p = event.getPoint();
            int selRow = getRowForLocation(p.x, p.y);
            TreeCellRenderer r = getCellRenderer();

            if (selRow != -1 && r != null) {
                int i = 0;              //tree节点的级数
                TreePath path = getPathForRow(selRow);
                Object lastPath = path.getLastPathComponent();
                if (lastPath instanceof TreeNode) {
                    TreeNode treeNode = (TreeNode) lastPath;
                    while (treeNode.getParent() instanceof TreeNode) {
                        i++;
                        treeNode = treeNode.getParent();
                    }
                }
                Component rComponent = r.getTreeCellRendererComponent
                        (this, lastPath, isRowSelected(selRow),
                                isExpanded(selRow), getModel().isLeaf(lastPath), selRow,
                                true);

                if (r instanceof DefaultTreeCellRenderer && rComponent instanceof JComponent && rComponent.getPreferredSize().getWidth() + i * WIDTH_BETWEEN_NODES > getVisibleRect().getWidth()) {
                    tip = ((DefaultTreeCellRenderer) r).getText();
                    icon = ((DefaultTreeCellRenderer) r).getIcon();
                }
            }
        }
        if (tip == null) {
            tip = getToolTipText();
        }
        return tip;
    }

    public Point getToolTipLocation(MouseEvent event) {
        if (event != null) {
            Point p = event.getPoint();
            int selRow = getRowForLocation(p.x, p.y);
            TreeCellRenderer r = getCellRenderer();
            if (selRow != -1 && r != null) {
                TreePath path = getPathForRow(selRow);
                Rectangle pathBounds = getPathBounds(path);
                return new Point(pathBounds.x - 2, pathBounds.y - 1);
            }
        }
        return null;
    }

    public JToolTip createToolTip() {
        UIToolTip tip = new UIToolTip(icon);
        tip.setComponent(this);
        tip.setOpaque(false);
        return tip;
    }
}