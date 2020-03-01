package com.fr.design.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import com.fr.general.ComparatorUtils;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * 一个层次连续节点.
 */
public class ContinuousTreeSelectionModel extends DefaultTreeSelectionModel {

    public void addSelectionPaths(TreePath[] paths) {
        if (paths == null || paths.length == 0) {
            return;
        }

        int firstIndex = 0;
        Map indexTreePathMap = new HashMap();

        TreePath[] oldSelectedPaths = this.getSelectionPaths();
        //peter:需要检查是否是同一个父亲的孩子
        if (oldSelectedPaths != null && oldSelectedPaths.length > 0) {
        	TreePath parentTreePath = oldSelectedPaths[0].getParentPath();
            DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode) parentTreePath.getLastPathComponent();

            for (int i = 0; i < oldSelectedPaths.length; i++) {
                if (ComparatorUtils.equals(parentTreePath, oldSelectedPaths[i].getParentPath())) {
                    DefaultMutableTreeNode tmpTreeNode = (DefaultMutableTreeNode) oldSelectedPaths[i].getLastPathComponent();
                    int treeNodeIndex = parentTreeNode.getIndex(tmpTreeNode);
                    if (i == 0) {
                        firstIndex = treeNodeIndex;
                    }
                    indexTreePathMap.put(new Integer(treeNodeIndex), GUICoreUtils.getTreePath(tmpTreeNode));
                }
            }

            for (int i = 0; i < paths.length; i++) {
                if (ComparatorUtils.equals(parentTreePath, paths[i].getParentPath())) {
                    DefaultMutableTreeNode tmpTreeNode = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                    int treeNodeIndex = parentTreeNode.getIndex(tmpTreeNode);
                    indexTreePathMap.put(new Integer(treeNodeIndex), GUICoreUtils.getTreePath(tmpTreeNode));
                }
            }
        } else {
            firstIndex = findFirstIndexIfNotEmpty(indexTreePathMap, paths);
        }

        List newTreePathList = resolveNewTreePathList(firstIndex, indexTreePathMap);

        if (newTreePathList.size() > 0) {
            super.addSelectionPaths((TreePath[])newTreePathList.toArray(new TreePath[newTreePathList.size()]));
        }
    }
    
    public int findFirstIndexIfNotEmpty(Map indexTreePathMap, TreePath[] paths) {
    	int firstIndex = 0;
    	TreePath parentTreePath = paths[0].getParentPath();
        DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode) parentTreePath.getLastPathComponent();

        for (int i = 0; i < paths.length; i++) {
            if (ComparatorUtils.equals(parentTreePath, paths[i].getParentPath())) {
                DefaultMutableTreeNode tmpTreeNode = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                int treeNodeIndex = parentTreeNode.getIndex(tmpTreeNode);
                if (i == 0) {
                    firstIndex = treeNodeIndex;
                }
                indexTreePathMap.put(new Integer(treeNodeIndex), GUICoreUtils.getTreePath(tmpTreeNode));
            }
        }
        
        return firstIndex;
    }
    
    public List resolveNewTreePathList(int firstIndex, Map indexTreePathMap) {
    	List newTreePathList = new ArrayList();
        //peter: 先向下找.
        for (int i = firstIndex - 1; i >= 0 && indexTreePathMap.size() > 0; i--) {
            Object obj = indexTreePathMap.get(new Integer(i));
            if (obj == null) {
                break;
            }

            newTreePathList.add(obj);
            indexTreePathMap.remove(new Integer(i));
        }

        //peter: 先向下找.
        for (int i = firstIndex; i < Integer.MAX_VALUE && indexTreePathMap.size() > 0; i++) {
            Object obj = indexTreePathMap.get(new Integer(i));
            if (obj == null) {
                break;
            }

            newTreePathList.add(obj);
            indexTreePathMap.remove(new Integer(i));
        }
        
        return newTreePathList;
    }

    //
    public void setSelectionPaths(TreePath[] paths) {
        if (paths == null || paths.length == 0) {
            return;
        }

        int firstIndex = 0;
        Map indexTreePathMap = new HashMap();

        firstIndex = findFirstIndexIfNotEmpty(indexTreePathMap, paths);

        List newTreePathList = resolveNewTreePathList(firstIndex, indexTreePathMap);

        if (newTreePathList.size() > 0) {
            super.setSelectionPaths((TreePath[])newTreePathList.toArray(new TreePath[newTreePathList.size()]));
        }
    }
}