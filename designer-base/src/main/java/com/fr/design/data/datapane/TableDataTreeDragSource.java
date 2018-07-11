package com.fr.design.data.datapane;

import java.awt.Component;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.fr.general.NameObject;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.mainframe.dnd.ArrayTransferable;

/**
 * 从数据源树开始drag.
 */
public class TableDataTreeDragSource extends DragSourceAdapter implements DragGestureListener {
    private DragSource source;
    private String[][] doubleArray;
    public TableDataTreeDragSource(JTree tree, int actions) {
        source = new DragSource();
        source.createDefaultDragGestureRecognizer(tree, actions, this);
    }

    /*
      * Drag Gesture Handler
      */
    public void dragGestureRecognized(DragGestureEvent dge) {
        Component comp = dge.getComponent();
        if (comp instanceof TableDataTree) {

            List<String[]> stringList = new ArrayList<String[]>();
            TableDataTree tree = (TableDataTree) comp;

            TreePath[] paths = tree.getSelectionPaths();
            if (paths == null) {
                return;
            }
            doubleArray = null;
            setnameArray(paths, stringList);

            source.startDrag(dge, DragSource.DefaultLinkDrop, new ArrayTransferable(doubleArray), this);
        }
    }

    private void setnameArray(TreePath[] paths, List<String[]> stringList) {
        for (int i = 0; i < paths.length; i++) {
            if ((paths[i] == null) || (paths[i].getPathCount() <= 1)) {
                return;// We can't move the root node or an empty selection
            }
            ExpandMutableTreeNode segmentMutableTreeNode = (ExpandMutableTreeNode) paths[i].getLastPathComponent();
            Object userObj = segmentMutableTreeNode.getUserObject();
            List<String> tableAddress = new ArrayList<String>();
            String displayName;
            StringBuffer dsNameBuf = new StringBuffer();
            if (userObj instanceof NameObject) {
                continue;
            } else {
                if (!(userObj instanceof String)) {
                    continue;
                }
                displayName = (String) userObj;
                // james 读取表名
                while (segmentMutableTreeNode.getParent().getParent() != null) {
                    segmentMutableTreeNode = (ExpandMutableTreeNode) segmentMutableTreeNode.getParent();
                    ExpandMutableTreeNode datasheetExpandMutableTreeNode = segmentMutableTreeNode;
                    Object parentUserObj = datasheetExpandMutableTreeNode.getUserObject();
                    if (parentUserObj instanceof NameObject && !(((NameObject) parentUserObj).getObject() instanceof Integer)) {
                        NameObject nameObject = (NameObject) parentUserObj;
                        tableAddress.add(nameObject.getName());
                    }
                }
            }
            for (int j = tableAddress.size() - 1; j > -1; j--) {
                dsNameBuf.append(tableAddress.get(j));
                if (j != 0) {
                    dsNameBuf.append('_');
                }
            }
            String dsName;
            if (dsNameBuf.toString() != null) {
                dsName = dsNameBuf.toString();
            } else {
                return;
            }
            String[] attributes = {dsName, displayName};
            stringList.add(attributes);
            Object[] bo = stringList.toArray();
            doubleArray = new String[bo.length][];
            for (int io = 0; io < bo.length; io++) {
                if (bo[io] instanceof String[]) {
                    doubleArray[io] = (String[]) bo[io];
                }
            }
        }
    }

}