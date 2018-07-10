package com.fr.design.data.datapane;

import com.fr.base.BaseUtils;
import com.fr.general.NameObject;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.gui.itree.refreshabletree.UserObjectRefreshJTree;
import com.fr.design.icon.IconPathConstants;
import com.fr.general.ComparatorUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * TableData Tree
 */
public class TableDataTree extends UserObjectRefreshJTree<TableDataSourceOP> {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public TableDataTree() {
        super();
        this.setCellRenderer(tableDataTreeCellRenderer);
        this.setEditable(false);
    }
    // CellRenderer
    private DefaultTreeCellRenderer tableDataTreeCellRenderer = new DefaultTreeCellRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            ExpandMutableTreeNode treeNode = (ExpandMutableTreeNode) value;
            Object userObj = treeNode.getUserObject();
            if (userObj instanceof String) {
                // p:这个是column field.
                this.setIcon(BaseUtils.readIcon("com/fr/design/images/data/field.png"));
                this.setText((String) userObj);
            } else if (userObj instanceof NameObject) {
                NameObject nameObject = (NameObject) userObj;
                this.setText(nameObject.getName());
                if (nameObject.getObject() instanceof TableDataWrapper) {
                    TableDataWrapper tableDataWrappe = (TableDataWrapper) nameObject.getObject();
                    this.setIcon(tableDataWrappe.getIcon());
                } else if (nameObject.getObject() instanceof Integer) {
                    int num = (Integer) nameObject.getObject();
                    if (num == TableDataSourceOP.SERVER_TABLE_DATA) {
                        this.setIcon(BaseUtils.readIcon(IconPathConstants.STD_SHOW_ICON_PATH));
                    } else if (num == TableDataSourceOP.STORE_PRECEDURE_DATA) {
                        this.setIcon(BaseUtils.readIcon(IconPathConstants.SP_SHOW_ICON_PATH));
                    } else {
                        this.setIcon(BaseUtils.readIcon(IconPathConstants.DS_QUERY_ICON_PATH));
                    }
                } else {
                    this.setIcon(BaseUtils.readIcon("/com/fr/design/images/data/store_procedure.png"));
                }
            } else if (userObj == PENDING) {
                this.setIcon(null);
                this.setText(PENDING.toString());
            }
            // 这里新建一个Label作为render是因为JTree在动态刷新的时候，节点上render画布的的宽度不会变，会使得一部分比较长的数据显示为"..."
            UILabel label = new UILabel();
            label.setText(getText());
            label.setIcon(getIcon());
            Dimension dim = label.getPreferredSize();
            dim.height += 2;
            this.setSize(dim);
            this.setPreferredSize(dim);
            this.setBackgroundNonSelectionColor(UIConstants.TREE_BACKGROUND);
            this.setTextSelectionColor(Color.WHITE);
            this.setBackgroundSelectionColor(UIConstants.FLESH_BLUE);
            return this;
        }
    };

    public DefaultTreeCellRenderer getTableDataTreeCellRenderer() {
        return tableDataTreeCellRenderer;
    }

    public void setTableDataTreeCellRenderer(DefaultTreeCellRenderer tableDataTreeCellRenderer) {
        this.tableDataTreeCellRenderer = tableDataTreeCellRenderer;
    }

    protected void refreshTreeNode(ExpandMutableTreeNode eTreeNode, String childName) {
        if (interceptRefresh(eTreeNode)) {
            return;
        }
        boolean refreshall = childName.isEmpty();
        ExpandMutableTreeNode[] new_nodes = loadChildTreeNodes(eTreeNode);

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
            ExpandMutableTreeNode cTreeNode = null;
            for (int ni = 0, nlen = childTreeNodeList.size(); ni < nlen; ni++) {
                cTreeNode = (ExpandMutableTreeNode) childTreeNodeList.get(ni);
                if (ComparatorUtils.equals(cTreeNode.getUserObject(), cUserObject)) {
                    if (!refreshall && !ComparatorUtils.equals(childName, ((NameObject) cUserObject).getName())) {
                        new_nodes[ci] = cTreeNode;
                        break;
                    }
                    new_nodes[ci].setExpanded(cTreeNode.isExpanded());
                    if (cTreeNode.getFirstChild() instanceof ExpandMutableTreeNode && cTreeNode.isExpanded()) {
                        checkChildNodes(cTreeNode, new_nodes[ci]);
                    }
                    break;
                }
            }

            eTreeNode.add(new_nodes[ci]);
        }
    }


    protected void checkChildNodes(ExpandMutableTreeNode oldNode, ExpandMutableTreeNode newNode) {
        for (int i = 0; i < oldNode.getChildCount(); i++) {
            ExpandMutableTreeNode oldChild = (ExpandMutableTreeNode) oldNode.getChildAt(i);
            for (int j = 0; j < newNode.getChildCount(); j++) {
                ExpandMutableTreeNode newChild = (ExpandMutableTreeNode) newNode.getChildAt(j);
                newChild.removeAllChildren();
                ExpandMutableTreeNode[] nodes = TableDataTree.this.loadChildTreeNodes(newChild);
                for (int k = 0; k < nodes.length; k++) {
                    newChild.add(nodes[k]);
                }
                if (newChild.getChildCount() > 1 && ((ExpandMutableTreeNode) newChild.getFirstChild()).getUserObject() == PENDING) {
                    newChild.remove(0);
                }
                if (ComparatorUtils.equals(oldChild.getUserObject(), newChild.getUserObject())) {
                    newChild.setExpanded(oldChild.isExpanded());
                }
            }
        }
    }

    /*
     * p:获得选中的NameObject = name + tabledata.
	 */
    public NameObject getSelectedNameObject() {
        TreePath selectedTreePath = this.getSelectionPath();
        if (selectedTreePath == null) {
            return null;
        }
        ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
        Object selectedUserObject = selectedTreeNode.getUserObject();
        ExpandMutableTreeNode parentTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getParent();
        Object parentUserObject = parentTreeNode.getUserObject();
        if (parentUserObject instanceof NameObject && ((NameObject) parentUserObject).getObject() instanceof Integer) {
            if (selectedUserObject instanceof NameObject) {
                return (NameObject) selectedUserObject;
            }
        } else {
            parentTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getParent();
            parentUserObject = parentTreeNode.getUserObject();

            if (parentUserObject != null) {
                if (!(parentUserObject instanceof NameObject)) {
                    return (NameObject) selectedUserObject;
                } else {
                    return (NameObject) parentUserObject;
                }
            }
        }
        return null;

    }

    public TableDataWrapper[] getSelectedDatas() {
        TreePath[] selectedTreePaths = this.getSelectionPaths();
        if (selectedTreePaths == null || selectedTreePaths.length == 0) {
            return null;
        }
        TableDataWrapper[] nameobjs = new TableDataWrapper[selectedTreePaths.length];
        for (int i = 0; i < selectedTreePaths.length; i++) {
            TreePath selectedTreePath = selectedTreePaths[i];
            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            Object selectedUserObject = selectedTreeNode.getUserObject();
            ExpandMutableTreeNode parentTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getParent();
            Object parentUserObject = parentTreeNode.getUserObject();
            if (parentUserObject instanceof NameObject && ((NameObject) parentUserObject).getObject() instanceof Integer) {
                if (selectedUserObject instanceof NameObject) {
                    Object obj = ((NameObject) selectedUserObject).getObject();
                    if (obj instanceof TableDataWrapper) {
                        nameobjs[i] = (TableDataWrapper) obj;
                    }
                }
            } else {
                return new TableDataWrapper[0];
            }
        }

        return nameobjs;
    }

    public NameObject getRealSelectedNameObject() {
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

    /**
     * p:添加一个NameObject节点.
     */
    public void addNameObject(NameObject no) {
        if (no == null) {
            return;
        }
        DefaultTreeModel treeModel = (DefaultTreeModel) this.getModel();

        // 新建一个放着NameObject的newChildTreeNode,加到Root下面
        ExpandMutableTreeNode root = (ExpandMutableTreeNode) treeModel.getRoot();

        ExpandMutableTreeNode newChildTreeNode = new ExpandMutableTreeNode(no);
        root.add(newChildTreeNode);
        newChildTreeNode.add(new ExpandMutableTreeNode());

        treeModel.reload(root);
    }
}