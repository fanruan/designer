package com.fr.van.chart.drillmap.designer.data.comp;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.map.designer.type.GEOJSONTreeHelper;
import com.fr.plugin.chart.map.server.ChartGEOJSONHelper;
import com.fr.plugin.chart.map.server.CompatibleGEOJSONHelper;
import com.fr.stable.StringUtils;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;

/**
 * Created by Mitisky on 16/5/3.
 */
public class MapDataTree extends JTree {

    public MapDataTree(TreeNode root){
        super();
        this.setModel(model);
        model.setRoot(root);
        this.setEditable(true);
        this.setRootVisible(false);
        this.setShowsRootHandles(true);
        this.setInvokesStopCellEditing(true);
        this.setBackground(UIConstants.NORMAL_BACKGROUND);
        this.setCellRenderer(treeCellRenderer);
    }

    private DefaultTreeModel model = new DefaultTreeModel(null);

    /**
     * 根据节点的第一个子节点
     * 如果该子节点为可选择的节点，则使用该节点
     * 如果该子节点是不可选择的节点，则选择该节点的子节点
     */
    public void selectDefaultTreeNode() {
        DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) m_model.getRoot();
        Enumeration<DefaultMutableTreeNode> children = root.children();
        if (children.hasMoreElements()) {
            DefaultMutableTreeNode child = children.nextElement();

            if(GEOJSONTreeHelper.getInstance().isSelectableTreeNode(child)){
                selectTreeNode(child, m_model);
            }else{
                setFirstChildTreeNode(child, m_model);
            }

        }
    }

    private void setFirstChildTreeNode(DefaultMutableTreeNode parent, DefaultTreeModel m_model){
        Enumeration<DefaultMutableTreeNode> children = parent.children();
        if (children.hasMoreElements()){
            DefaultMutableTreeNode node = children.nextElement();
            selectTreeNode(node, m_model);
        }
    }

    public void changeRootNode(TreeNode node){
        DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
        m_model.setRoot(node);
    }

    public String getSelectNodeJSONPath() {
        if(this.getLastSelectedPathComponent() == null){
            return StringUtils.EMPTY;
        }

        DefaultMutableTreeNode currentSel = (DefaultMutableTreeNode)this.getLastSelectedPathComponent();

        return CompatibleGEOJSONHelper.getJsonUrlByPathIncludeParam(currentSel.getUserObject().toString());
    }

    //根据路径精确查找
    public DefaultMutableTreeNode setSelectNodePath(String jsonUrl) {
        if(StringUtils.isEmpty(jsonUrl) || CompatibleGEOJSONHelper.isDeprecated(jsonUrl)){
            return null;
        }
        DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) m_model.getRoot();

        Enumeration<DefaultMutableTreeNode> els = root.postorderEnumeration();
        while(els.hasMoreElements()){
            DefaultMutableTreeNode el = els.nextElement();
            if(el == null || el.getUserObject() == null){
                return null;
            }
            String dirPath = el.getUserObject().toString();
            String url =  CompatibleGEOJSONHelper.getJsonUrlByPathIncludeParam(dirPath);
            //先equals再valid原因：valid 远程下实时去服务器看有没有json文件
            if (ComparatorUtils.equals(jsonUrl, url) && GEOJSONTreeHelper.isValidDirPath(dirPath)) {
                selectTreeNode(el, m_model);
                return el;
            }
        }
        return null;
    }

    private DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer() {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
            String name = getPresentName(treeNode);
            this.setText(name);

            UILabel label = new UILabel();
            label.setText(getText());
            label.setIcon(getIcon());
            this.setSize(label.getPreferredSize());
            Dimension dim = label.getPreferredSize();
            dim.height += 2;
            this.setPreferredSize(dim);
            this.setBackgroundNonSelectionColor(UIConstants.NORMAL_BACKGROUND);
            this.setForeground(UIConstants.FONT_COLOR);
            this.setBackgroundSelectionColor(UIConstants.FLESH_BLUE);
            return this;
        }
    };

    protected String getPresentName(DefaultMutableTreeNode treeNode) {
        if(treeNode == null || treeNode.getUserObject() == null){
            return StringUtils.EMPTY;
        }
        return ChartGEOJSONHelper.getPresentNameWithPath(treeNode.getUserObject().toString());
    }

    //模糊搜索 深度优先.
    public void search(String text){
        if(StringUtils.isEmpty(text)){
            return;
        }
        DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) m_model.getRoot();

        Enumeration<DefaultMutableTreeNode> els = root.postorderEnumeration();
        while(els.hasMoreElements()){

            DefaultMutableTreeNode el = els.nextElement();
            String path =  el.getUserObject().toString();
            String fileName = ChartGEOJSONHelper.getPresentNameWithPath(path);
            if (StringUtils.contains(fileName, text) && GEOJSONTreeHelper.isValidDirPath(path)) {
                selectTreeNode(el, m_model);
                return;
            }
        }
    }

    public void selectTreeNode(DefaultMutableTreeNode node, DefaultTreeModel m_model){
        TreeNode[] nodes = m_model.getPathToRoot(node);
        TreePath treePath = new TreePath(nodes);
        setSelectionPath(treePath);
        scrollPathToVisible(treePath);
    }

}
