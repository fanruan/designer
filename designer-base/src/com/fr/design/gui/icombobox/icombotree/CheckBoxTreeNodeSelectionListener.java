package com.fr.design.gui.icombobox.icombotree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CheckBoxTreeNodeSelectionListener extends MouseAdapter
{
    private ArrayList treePathList = new ArrayList<TreePath>();

    /**
     * 鼠标点击事件
     * @param event 事件
     */
    @Override
    public void mouseClicked(MouseEvent event)
    {
        JTree tree = (JTree)event.getSource();
        int x = event.getX();
        int y = event.getY();
        int row = tree.getRowForLocation(x, y);
        TreePath treePath = tree.getPathForRow(row);
        if(treePath != null)
        {
            tree.setExpandsSelectedPaths(true);
            CheckBoxTreeNode node = (CheckBoxTreeNode)treePath.getLastPathComponent();
            if(node != null)
            {
                boolean isSelected = !node.isSelected();
                node.setSelected(isSelected);
                ((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);
            }
        }else{
            tree.setExpandsSelectedPaths(false);
        }
        CheckBoxTreeNode root = (CheckBoxTreeNode) tree.getModel().getRoot();
        treePathList.clear();
        addPathList(tree,root);
        selectTreeItem(tree,treePathList);
    }

    private void addPathList(JTree tree,CheckBoxTreeNode root){
        for(int i = 0;i< root.getChildCount();i++){
            CheckBoxTreeNode treeNode = (CheckBoxTreeNode) root.getChildAt(i);
            if(treeNode.isLeaf() && treeNode.isSelected()){
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                treePathList.add(new TreePath(model.getPathToRoot(treeNode)));
            }else{
                addPathList(tree,treeNode);
            }
        }
    }

    private void selectTreeItem(JTree tree,ArrayList<TreePath> treePath) {
        TreePath[] treePaths = new TreePath[treePath.size()];
        for(int i = 0;i < treePath.size();i++){
            treePaths[i] = treePath.get(i);
        }
        tree.setSelectionPaths(treePaths);
    }

}