package com.fr.design.roleAuthority;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itree.checkboxtree.CheckBoxTree;
import com.fr.design.gui.itree.checkboxtree.CheckBoxTreeSelectionModel;
import com.fr.general.NameObject;
import com.fr.design.constants.UIConstants;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.gui.itree.refreshabletree.UserObjectRefreshJTree;
import com.fr.design.mainframe.AuthorityPropertyPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.general.ComparatorUtils;


import javax.swing.SwingUtilities;
import javax.swing.JTree;
import javax.swing.JComponent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Author : daisy
 * Time: 下午3:32
 * Date: 13-8-30
 */
public class RoleTree extends UserObjectRefreshJTree<RoleSourceOP> {
    private static final long serialVersionUID = 2L;

    private String roleName = null;

    public RoleTree() {
        super();
        this.setCellRenderer(roleTreeRenderer);
        this.setEnabled(true);
        this.setEditable(true);
        this.setRowHeight(20);
        this.setDigIn(true);
        Handler handler = createHandlerForRoleTree();
        this.replaceMouseListener(this, handler, 0);
        this.replaceKeyListener(this, handler, 0);
        this.addTreeSelectionListener(handler);
//        this.removeMouseListener(treeMouseListener);
//        this.addTreeSelectionListener(new TreeSelectionListener() {
//            public void valueChanged(TreeSelectionEvent e) {
//                doWithValueChanged(e);
//            }
//        });

    }

    public boolean isCheckBoxVisible(TreePath path) {
        return true;
    }


    /**
     * Creates the mouse listener and key listener used by RoleTree.
     *
     * @return the Handler.
     */
    protected Handler createHandlerForRoleTree() {
        return new Handler(this);
    }

    protected static class Handler implements MouseListener, KeyListener, TreeSelectionListener {
        protected RoleTree _tree;
        int _hotspot = new UICheckBox().getPreferredSize().width;
        private int _toggleCount = -1;

        public Handler(RoleTree tree) {
            _tree = tree;
        }

        protected TreePath getTreePathForMouseEvent(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return null;
            }

            if (!_tree.isCheckBoxEnabled()) {
                return null;
            }

            TreePath path = _tree.getPathForLocation(e.getX(), e.getY());
            if (path == null) {
                return null;
            }

//            if (clicksInCheckBox(e, path)) {

            return path;
//            } else {
//                return null;
//            }
        }

//        protected boolean clicksInCheckBox(MouseEvent e, TreePath path) {
//            if (!_tree.isCheckBoxVisible(path)) {
//                return false;
//            } else {
//                Rectangle bounds = _tree.getPathBounds(path);
//                if (_tree.getComponentOrientation().isLeftToRight()) {
//                    return e.getX() < bounds.x + _hotspot;
//                } else {
//                    return e.getX() > bounds.x + bounds.width - _hotspot;
//                }
//            }
//        }

        private TreePath preventToggleEvent(MouseEvent e) {
            TreePath pathForMouseEvent = getTreePathForMouseEvent(e);
            if (pathForMouseEvent != null) {
                int toggleCount = _tree.getToggleClickCount();
                if (toggleCount != -1) {
                    _toggleCount = toggleCount;
                    _tree.setToggleClickCount(-1);
                }
            }
            return pathForMouseEvent;
        }

        public void mouseClicked(MouseEvent e) {
            preventToggleEvent(e);
        }

        public void mousePressed(MouseEvent e) {
            TreePath path = preventToggleEvent(e);
            if (path != null) {
                toggleSelection(path);
                e.consume();
            }
        }

        public void mouseReleased(MouseEvent e) {
            TreePath path = preventToggleEvent(e);
            if (path != null) {
                e.consume();
            }
            if (_toggleCount != -1) {
                _tree.setToggleClickCount(_toggleCount);
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            if (e.isConsumed()) {
                return;
            }

            if (!_tree.isCheckBoxEnabled()) {
                return;
            }

            if (e.getModifiers() == 0 && e.getKeyChar() == KeyEvent.VK_SPACE) {
                toggleSelections();
            }
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }

        public void valueChanged(TreeSelectionEvent e) {
            _tree.treeDidChange();
            _tree.doWithValueChanged(e);
        }

        private void toggleSelection(TreePath path) {
            if (!_tree.isEnabled() || !_tree.isCheckBoxEnabled(path)) {
                return;
            }
            CheckBoxTreeSelectionModel selectionModel = _tree.getCheckBoxTreeSelectionModel();
            boolean selected = selectionModel.isPathSelected(path, selectionModel.isDigIn());
            selectionModel.removeTreeSelectionListener(this);
            try {
                if (!selectionModel.isSingleEventMode()) {
                    selectionModel.setBatchMode(true);
                }
                if (selected)
                    selectionModel.removeSelectionPath(path);
                else
                    selectionModel.addSelectionPath(path);
            } finally {
                if (!selectionModel.isSingleEventMode()) {
                    selectionModel.setBatchMode(false);
                }
                selectionModel.addTreeSelectionListener(this);
                _tree.treeDidChange();
                _tree.doWithValueChanged(path);
            }
        }

        protected void toggleSelections() {
            TreePath[] treePaths = _tree.getSelectionPaths();
            if (treePaths == null) {
                return;
            }
            for (int i = 0, length = treePaths.length; i < length; i++) {
                TreePath tmpTreePath = treePaths[i];
                toggleSelection(tmpTreePath);
            }
//            for (TreePath treePath : treePaths) {
//                toggleSelection(treePath);
//            }
        }
    }

    /**
     * 更新UI
     */
    public void updateUI() {
        super.updateUI();
        setUI(new UIRoleTreeUI());
    }

    /**
     * @param e 选中事件
     */
    protected void doWithValueChanged(TreeSelectionEvent e) {
        if (e.getNewLeadSelectionPath() != null) {
            if (!e.getNewLeadSelectionPath().getLastPathComponent().toString().equals(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Role"))) {
                roleName = e.getNewLeadSelectionPath().getLastPathComponent().toString();
                setTabRoleName(roleName);
                refreshRoleTree(roleName);
                refreshElementAndAuthorityPane();
                HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().judgeSheetAuthority(roleName);
            }
        }
    }

    /**
     * @param treepath 所选的节点路径
     */
    protected void doWithValueChanged(TreePath treepath) {
        if (treepath != null && !treepath.getLastPathComponent().toString().equals(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Role"))) {
            roleName = treepath.getLastPathComponent().toString();
            setTabRoleName(roleName);
            refreshRoleTree(roleName);
            refreshElementAndAuthorityPane();
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().judgeSheetAuthority(roleName);
        }

    }

    protected void setTabRoleName(String roleName) {

    }


    /**
     * 刷新角色树
     *
     * @param selectedRole 角色
     */
    public void refreshRoleTree(String selectedRole) {
        if (EastRegionContainerPane.getInstance().getAuthorityEditionPane() instanceof AuthorityPropertyPane) {
            AuthorityPropertyPane authorityPropertyPane = (AuthorityPropertyPane) EastRegionContainerPane.getInstance().getAuthorityEditionPane();
            authorityPropertyPane.populate();
            EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(authorityPropertyPane);
        }
    }

    public void setSelectedRole(String selectedRole, TreePath parent) {
        ExpandMutableTreeNode node = (ExpandMutableTreeNode) parent.getLastPathComponent();
        if (node.children() != null && node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                ExpandMutableTreeNode n = (ExpandMutableTreeNode) e.nextElement();
                Object userObj = n.getUserObject();
                String chilld = null;
                if (userObj instanceof String) {
                    chilld = (String) userObj;
                } else if (userObj instanceof NameObject) {
                    NameObject nameObject = (NameObject) userObj;
                    chilld = nameObject.getName();
                }
                if (ComparatorUtils.equals(chilld, selectedRole)) {
                    this.setSelectionPath(parent.pathByAddingChild(n));
                    return;
                } else {
                    setSelectedRole(selectedRole, parent.pathByAddingChild(n));
                }
            }
        }
    }


    private void refreshElementAndAuthorityPane() {
        JComponent authorityToolBar = DesignerContext.getDesignerFrame().getToolbarComponent();
        if (authorityToolBar instanceof BasicBeanPane) {
            //说明是工具栏的
            ((BasicBeanPane) authorityToolBar).populateAuthority();
        }
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().repaint();
    }

    public String getSelectedRoleName() {
        return roleName;
    }

    public void setSelectedRoleName(String name) {
        roleName = name;
    }


    private DefaultTreeCellRenderer roleTreeRenderer = new DefaultTreeCellRenderer() {
        private static final long serialVersionUID = 2L;


        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            ExpandMutableTreeNode treeNode = (ExpandMutableTreeNode) value;
            Object userObj = treeNode.getUserObject();
            if (userObj instanceof String) {
                // p:这个是column field.
                this.setIcon(null);
                this.setText((String) userObj);
            } else if (userObj instanceof NameObject) {
                NameObject nameObject = (NameObject) userObj;
                this.setText(nameObject.getName());
                this.setIcon(null);
            }


            // 这里新建一个Label作为render是因为JTree在动态刷新的时候，节点上render画布的的宽度不会变，会使得一部分比较长的数据显示为"..."
            this.setBackgroundNonSelectionColor(UIConstants.TREE_BACKGROUND);
            this.setForeground(UIConstants.FONT_COLOR);
            this.setBackgroundSelectionColor(UIConstants.FLESH_BLUE);
            return this;
        }
    };


    /**
     * 去除不需要的鼠标监听器
     *
     * @param component 组件
     * @param l 所需的鼠标监听器
     * @param index 插入的索引
     */
    private void replaceMouseListener(Component component, MouseListener l, int index) {

        component.removeMouseListener(treeMouseListener);

        MouseListener[] listeners = component.getMouseListeners();
        for (int i = 0, length = listeners.length; i < length; i++) {
            component.removeMouseListener(listeners[i]);
        }
//        for (MouseListener listener : listeners) {
//            component.removeMouseListener(listener);
//        }
        for (int i = 0; i < listeners.length; i++) {
            MouseListener listener = listeners[i];
            if (index == i) {
                component.addMouseListener(l);
            }
            if (listener instanceof CheckBoxTree.Handler) {
                continue;
            }
            component.addMouseListener(listener);
        }
        // index is too large, add to the end.
        if (index > listeners.length - 1) {
            component.addMouseListener(l);
        }
    }


    /**
     * 去除一些不需要的键盘监听器
     *
     * @param component 组件
     * @param l 所需的键盘监听器
     * @param index 插入的索引
     */
    private void replaceKeyListener(Component component, KeyListener l, int index) {

        KeyListener[] listeners = component.getKeyListeners();
        for (int i = 0, length = listeners.length; i < length; i++) {
            component.removeKeyListener(listeners[i]);
        }
//        for (MouseListener listener : listeners) {
//            component.removeMouseListener(listener);
//        }
        for (int i = 0; i < listeners.length; i++) {
            KeyListener listener = listeners[i];
            if (index == i) {
                component.addKeyListener(l);
            }
            if (listener instanceof CheckBoxTree.Handler) {
                continue;
            }

            component.addKeyListener(listener);
        }
        // index is too large, add to the end.
        if (index > listeners.length - 1) {
            component.addKeyListener(l);
        }
    }

    /*
 * p:获得选中的NameObject = name + role.
 */
    public NameObject getSelectedNameObject() {
        TreePath selectedTreePath = this.getSelectionPath();
        if (selectedTreePath == null) {
            return null;
        }
        ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
        Object selectedUserObject = selectedTreeNode.getUserObject();
        return new NameObject(selectedUserObject.toString(), "");
    }


    /**
     * p:添加一个NameObject节点
     *
     * @param no 需要添加的节点
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


    /**
     * 刷新树节点
     */
    public void refreshTreeNode() {
        DefaultTreeModel treeModel = (DefaultTreeModel) this.getModel();
        ExpandMutableTreeNode root = (ExpandMutableTreeNode) treeModel.getRoot();
        if (interceptRefresh(root)) {
            return;
        }

        ExpandMutableTreeNode[] new_nodes = loadChildTreeNodes(root);

        List<DefaultMutableTreeNode> childTreeNodeList = new ArrayList<DefaultMutableTreeNode>();
        for (int i = 0, len = root.getChildCount(); i < len; i++) {
            if (root.getChildAt(i) instanceof ExpandMutableTreeNode) {
                childTreeNodeList.add((ExpandMutableTreeNode) root.getChildAt(i));
            } else {
                childTreeNodeList.add((DefaultMutableTreeNode) root.getChildAt(i));
            }
        }

        root.removeAllChildren();

        for (int ci = 0; ci < new_nodes.length; ci++) {
            Object cUserObject = new_nodes[ci].getUserObject();

            for (int ni = 0, nlen = childTreeNodeList.size(); ni < nlen; ni++) {
                ExpandMutableTreeNode cTreeNode = (ExpandMutableTreeNode) childTreeNodeList.get(ni);
                if (ComparatorUtils.equals(cTreeNode.getUserObject(), cUserObject)) {
                    new_nodes[ci].setExpanded(cTreeNode.isExpanded());
                    if (cTreeNode.getFirstChild() instanceof ExpandMutableTreeNode && cTreeNode.isExpanded()) {
                        checkChildNodes(cTreeNode, new_nodes[ci]);
                    }
                    break;
                }
            }

            root.add(new_nodes[ci]);
        }
    }


    protected void checkChildNodes(ExpandMutableTreeNode oldNode, ExpandMutableTreeNode newNode) {
        for (int i = 0; i < oldNode.getChildCount(); i++) {
            ExpandMutableTreeNode oldChild = (ExpandMutableTreeNode) oldNode.getChildAt(i);
            for (int j = 0; j < newNode.getChildCount(); j++) {
                ExpandMutableTreeNode newChild = (ExpandMutableTreeNode) newNode.getChildAt(j);
                ExpandMutableTreeNode[] nodes = RoleTree.this.loadChildTreeNodes(newChild);
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


}
