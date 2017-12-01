package com.fr.design.mainframe;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.fr.design.constants.UIConstants;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.treeview.ComponentTreeCellRenderer;
import com.fr.design.designer.treeview.ComponentTreeModel;
import com.fr.design.gui.itree.UITreeUI;
import com.fr.stable.StringUtils;

public class ComponentTree extends JTree {

    private FormDesigner designer;
    private ComponentTreeModel model;
    private static final int PADDING_LEFT = 10;
    private static final int PADDING_TOP = 8;

    public ComponentTree(FormDesigner designer) {
        this.designer = designer;
        this.setBackground(UIConstants.TREE_BACKGROUND);
        setRootVisible(true);
        setCellRenderer(new ComponentTreeCellRenderer());
        getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        this.setDragEnabled(false);
        this.setDropMode(DropMode.ON_OR_INSERT);
        this.setTransferHandler(new TreeTransferHandler());
        this.refreshTreeRoot();
        addTreeSelectionListener(designer);
        setEditable(true);
        setUI(new UITreeUI());
        setBorder(BorderFactory.createEmptyBorder(PADDING_TOP, PADDING_LEFT, 0, 0));
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

    public void setSelectionPath(TreePath path) {
        // 不管点击哪一项，都要先退出编辑状态（图表、报表块、绝对布局、tab块）
//        getSelectionModel().setSelectionPath(path);
        designer.stopEditing(path);
        super.setSelectionPath(path);
    }


    /**
     * 是否可编辑
     * @param path 树路径
     * @return 是则返回true
     */
    @Override
    public boolean isPathEditable(TreePath path) {
        return false;
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

    public void setAndScrollSelectionPath(TreePath[] treepath) {
        setSelectionPaths(treepath);
        scrollPathToVisible(treepath[0]);
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
            setAndScrollSelectionPath(paths);
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
            //绝对布局作为body的时候不显示自适应布局父层
            if (((XCreator) parent).acceptType(XWAbsoluteBodyLayout.class)) {
                if ((parent.getParent() != null)
                        && ((XCreator)parent.getParent()).acceptType(XWFitLayout.class)){
                    parent = parent.getParent().getParent();
                    continue;
                }
            }

            parent = parent.getParent();
        }
        Object[] components = path.toArray();
        return new TreePath(components);
    }

}