package com.fr.design.gui.itree.filetree;

import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.fr.general.ComparatorUtils;

/**
 * 文件结构树.
 */
public abstract class AbstractFileTree extends JTree implements TreeExpansionListener {
    /**
     * 列出所有的FileNode.
     */
    public abstract FileDirectoryNode[] listFileNodes(DefaultMutableTreeNode currentTreeNode);

    /**
     * 展开TreeNode.
     */
    public boolean expandTreeNode(DefaultMutableTreeNode currentTreeNode) {
        if (currentTreeNode.isLeaf()) {
            return false;
        }

        //判断第一个孩子节点.
        DefaultMutableTreeNode flag = (DefaultMutableTreeNode) currentTreeNode.getFirstChild();
        if (flag == null) {     // No flag
            return false;
        }
        Object firstChildObj = flag.getUserObject();
        if (!(firstChildObj instanceof Boolean)) {
            return false;      // Already expanded
        }
        currentTreeNode.removeAllChildren();  //删除所有的节点.

        // 列出所有的子文件夹.
        FileDirectoryNode[] fileNodes = listFileNodes(currentTreeNode);
        // 如果所得为空,(此时 isDirectory是true 但是拿不到子文件 所以不应该展开文件夹)
        // 或者文件夹中没有文件.
        if ( fileNodes == null) {
        	return false;
        }
        for (int i = 0; i < fileNodes.length; i++) {
        	FileDirectoryNode tmpNameNode = fileNodes[i];

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(tmpNameNode);
            currentTreeNode.add(node);

            // 如果是目录,需要添加子节点.
            if (tmpNameNode.isDirectory()) {
            	// 如果有目录但是列不出子文件 就不添加子节点.
            	// 或者文件夹中没有子文件的 也不添加子节点.kt
            	FileDirectoryNode[] childFileNode = listFileNodes(node);
            	if (childFileNode != null ) {
            		node.add(new DefaultMutableTreeNode(Boolean.TRUE));
            	}
            }
        }

        // 重新加载parentTreeNode.
        ((DefaultTreeModel) this.getModel()).reload(currentTreeNode);
//        this.expandPath(GUICoreUtils.getTreePath(currentTreeNode));

        return true;
    }

    protected DefaultMutableTreeNode getMutableTreeNode(TreePath path) {
        return (DefaultMutableTreeNode) (path.getLastPathComponent());
    }

    public void treeExpanded(TreeExpansionEvent event) {
        DefaultMutableTreeNode currentTreeNode = getMutableTreeNode(event.getPath());
        // 判断当前文件节点所得子文件为null时  不展开节点.
        FileDirectoryNode[] fileNodes = listFileNodes(currentTreeNode);
        if(fileNodes != null) {
        	this.expandTreeNode(currentTreeNode);
        }
    }

    public void treeCollapsed(TreeExpansionEvent event) {
    }

    /**
     * 是否是父子关系的文件.
     */
    public static boolean isParentFile(File parentFile, File childFile) {
        while (true) {
            if (ComparatorUtils.equals(parentFile, childFile)) {
                return true;
            }

            childFile = childFile.getParentFile();
            if (childFile == null) {
                break;
            }
        }
        return false;
    }
}