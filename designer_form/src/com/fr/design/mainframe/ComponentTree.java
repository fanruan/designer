package com.fr.design.mainframe;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DropMode;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.treeview.ComponentTreeCellRenderer;
import com.fr.design.designer.treeview.ComponentTreeModel;
import com.fr.stable.StringUtils;

public class ComponentTree extends JTree {

    private FormDesigner designer;
    private ComponentTreeModel model;

    public ComponentTree(FormDesigner designer) {
        this.designer = designer;
        this.setBackground(UIConstants.NORMAL_BACKGROUND);
        setRootVisible(true);
        setCellRenderer(new ComponentTreeCellRenderer());
        getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        this.setDragEnabled(false);
        this.setDropMode(DropMode.ON_OR_INSERT);
        this.setTransferHandler(new TreeTransferHandler());
        this.refreshTreeRoot();
        TreePath[] paths = getSelectedTreePath();
        addTreeSelectionListener(designer);
        setSelectionPaths(paths);

        designer.addDesignerEditListener(new TreeDesignerEditAdapter());
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu(e);
                }
            }
        });
        setEditable(true);
    }

    public FormDesigner getDesigner() {
        return designer;
    }

    /**
     * 构造函数
     *
     * @param designer 设计界面组件
     * @param model 构造JTree的model
     */
    public ComponentTree(FormDesigner designer,ComponentTreeModel model) {
        this(designer);
        this.setModel(model);
    }




    /**
     * 是否可编辑
     * @param path 树路径
     * @return 是则返回true
     */
    @Override
    public boolean isPathEditable(TreePath path) {
        Object object = path.getLastPathComponent();
        if (object == designer.getRootComponent()) {
            return false;
        }
        return super.isPathEditable(path);
    }

    /**
     * 将值转换为文本
     * @param value 值
     * @param selected 是否选中
     * @param expanded 扩展
     * @param leaf 是否叶子
     * @param row 行
     * @param hasFocus 是否焦点
     *
     * @return 返回文本
     */
    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value != null && value instanceof XCreator) {
            return ((XCreator) value).toData().getWidgetName();
        } else {
            return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        }
    }

    public void setAndScrollSelectionPath(TreePath treepath) {
        setSelectionPath(treepath);
        scrollPathToVisible(treepath);
    }

    private void popupMenu(MouseEvent e) {
        TreePath path = this.getSelectionPath();
        if (path == null) {
            return;
        }
        Component component = (Component) path.getLastPathComponent();
        if (!(component instanceof XCreator)) {
            return;
        }
        ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, (XCreator) component);
        JPopupMenu menu = adapter.getContextPopupMenu(e);
        menu.show(this, e.getX(), e.getY());
    }

    /**
     * 刷新
     */
    public void refreshUI() {
        updateUI();
    }





    public TreePath[] getSelectedTreePath() {
        XCreator[] creators = designer.getSelectionModel().getSelection().getSelectedCreators();
        TreePath[] paths = new TreePath[creators.length];

        for (int i = 0; i < paths.length; i++) {
            paths[i] = buildTreePath(creators[i]);
        }
        return paths;
    }

    private class TreeDesignerEditAdapter implements DesignerEditListener {

        @Override
        public void fireCreatorModified(DesignerEvent evt) {
            if (evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED) {
                TreePath[] paths = getSelectedTreePath();

                if (paths.length == 1) {
                    setAndScrollSelectionPath(paths[0]);
                } else {
                    setSelectionPaths(paths);
                }
            }  else if(evt.getCreatorEventID() == DesignerEvent.CREATOR_PASTED) {
                ComponentTree.this.refreshUI();
                TreePath[] paths = getSelectedTreePath();

                if (paths.length == 1) {
                    setAndScrollSelectionPath(paths[0]);
                } else {
                    setSelectionPaths(paths);
                }
                ComponentTree.this.repaint();

            }  else {
                ComponentTree.this.refreshUI();
                ComponentTree.this.repaint();
            }
        }

        @Override
        public boolean equals(Object o) {
            return o.getClass() == this.getClass();
        }
    }


    /**
     *搜索指定名称的路径
     *
     * @param text 名称
     * @return 树路径
     */
    public TreePath[] search(String text) {
        if (StringUtils.isNotEmpty(text)) {
            text = text.toLowerCase();
        }
        ArrayList<XCreator> searchList = new ArrayList<XCreator>();
        XLayoutContainer root = designer.getRootComponent();
        searchFormRoot(root, searchList, text);
        TreePath[] paths = new TreePath[searchList.size()];

        for (int i = 0; i < paths.length; i++) {
            paths[i] = buildTreePath(searchList.get(i));
        }
        if(paths.length > 0) {
            setAndScrollSelectionPath(paths[0]);
        } else {
            setSelectionPath();
        }
        return paths;
    }


    private void  setSelectionPath(){

        /**
         * 不让传null参数，所以只有自己定义
         */
        super.setSelectionPath(null);
        clearSelection();
    }

    private void searchFormRoot(XLayoutContainer container, ArrayList<XCreator> searchList, String text) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        for (int i = 0, size = container.getXCreatorCount(); i < size; i++) {
            XCreator creator = container.getXCreator(i);
            String xName = creator.toData().getWidgetName();
            if (xName.toLowerCase().contains(text)) {
                searchList.add(creator);
            }
            if (creator instanceof XLayoutContainer) {
                searchFormRoot((XLayoutContainer) creator, searchList, text);
            }
        }
    }

    /**
     * 触发
     */
    public void fireTreeChanged() {
        designer.refreshDesignerUI();
    }

    /**
     * 刷新
     */
    public void refreshTreeRoot() {
        model = new ComponentTreeModel(designer, designer.getTopContainer());
        setModel(model);
        setDragEnabled(false);
        setDropMode(DropMode.ON_OR_INSERT);
        setTransferHandler(new TreeTransferHandler());
        repaint();
    }

    private TreePath buildTreePath(Component comp) {
        ArrayList<Component> path = new ArrayList<Component>();
        Component parent = comp;

        while (parent != null) {
            XCreator creator = (XCreator) parent;
            path.add(0, parent);
            if (creator != comp ) {
                creator.notShowInComponentTree(path);
            }
            parent = parent.getParent();
        }
        Object[] components = path.toArray();
        return new TreePath(components);
    }
}