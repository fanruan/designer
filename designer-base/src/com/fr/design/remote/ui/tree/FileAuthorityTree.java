package com.fr.design.remote.ui.tree;

import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.stable.CoreConstants;
import com.fr.stable.StringUtils;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAuthorityTree extends TemplateFileTree {

    @Override
    public boolean isCheckBoxVisible(TreePath path) {
        return true;
    }


    public void selectCheckBoxPaths(String[] paths) {
        if (paths == null || paths.length == 0) {
            return;
        }

        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        ExpandMutableTreeNode treeNode = (ExpandMutableTreeNode) model.getRoot();
        List<TreePath> res = new ArrayList<>();
        for (int i = 0, len = treeNode.getChildCount(); i < len; i++) {
            ExpandMutableTreeNode childTreeNode = (ExpandMutableTreeNode) treeNode.getChildAt(i);
            for (String path : paths) {
                TreePath tPath = getSelectingPath(childTreeNode, StringUtils.EMPTY, path, model);
                if (tPath != null) {
                    res.add(tPath);
                }
            }
        }
        // 勾选中这些结点
        this.getCheckBoxTreeSelectionModel().setSelectionPaths(res.toArray(new TreePath[0]));
    }


    private TreePath getSelectingPath(ExpandMutableTreeNode currentTreeNode, String prefix, String filePath, DefaultTreeModel model) {
        FileNode fileNode = (FileNode) currentTreeNode.getUserObject();
        String nodePath = fileNode.getName();
        String currentTreePath = prefix + nodePath;
        TreePath res;

        // 判断是否是希望选中的
        if (ComparatorUtils.equals(new File(currentTreePath), new File(filePath))) {
            return new TreePath(model.getPathToRoot(currentTreeNode));
        }
        // 如果当前路径是currentFilePath的ParentFile,则expandTreeNode,并继续往下找
        else if (isParentFile(currentTreePath, filePath)) {
            loadPendingChildTreeNode(currentTreeNode);
            prefix = currentTreePath + CoreConstants.SEPARATOR;
            for (int i = 0, len = currentTreeNode.getChildCount(); i < len; i++) {
                ExpandMutableTreeNode childTreeNode = (ExpandMutableTreeNode) currentTreeNode.getChildAt(i);
                // 继续在子结点里面找
                res = getSelectingPath(childTreeNode, prefix, filePath, model);
                if (res != null) {
                    return res;
                }
            }
            return null;
        }
        return null;
    }
}
