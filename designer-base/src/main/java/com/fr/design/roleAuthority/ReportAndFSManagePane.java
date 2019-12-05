package com.fr.design.roleAuthority;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.actions.UpdateAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.Prepare4DataSourceChange;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignAuthorityEventType;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.DockingView;
import com.fr.design.menu.ToolBarDef;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * 设计器左下角面板，用于在权限编辑时存放角色
 * Author : daisy
 * Date: 13-8-30
 * Time: 下午2:22
 */
public class ReportAndFSManagePane extends DockingView implements Prepare4DataSourceChange {

    private static final int LEFT_GAP = -125;
    private static ReportAndFSManagePane singleton = new ReportAndFSManagePane();

    private static RoleTree roleTree;
    static {
        EventDispatcher.listen(DesignAuthorityEventType.StopEdit, new Listener<DesignerFrame>() {

            public void on(Event event, DesignerFrame param) {
                DefaultTreeSelectionModel model = roleTree.getCheckBoxTreeSelectionModel();
                model.removeSelectionPaths(model.getSelectionPaths());
            }
        });
    }
    private RefreshAction refreshAction = new RefreshAction();
    private UIHeadGroup buttonGroup;
    private RoleSourceOP op;
    private String roleNames;

    public synchronized static ReportAndFSManagePane getInstance() {
        singleton.op = new RoleSourceOP();
        singleton.setDefaultSelectedRole();
        return singleton;

    }

    public ReportAndFSManagePane() {
        initRoleTree();
        this.setLayout(new BorderLayout(4, 0));
        this.setBorder(null);
        this.add(iniToolBarPane(), BorderLayout.NORTH);
        refreshAction.setEnabled(true);
        UIScrollPane scrollPane = new UIScrollPane(roleTree);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        initbuttonGroup();
        JPanel jPanel = new JPanel(new BorderLayout(4, 4));
        JPanel buttonPane = new JPanel(new GridLayout());
        buttonPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.LINE_COLOR));
        buttonPane.add(buttonGroup, BorderLayout.CENTER);
        jPanel.add(buttonPane, BorderLayout.NORTH);
        jPanel.add(scrollPane, BorderLayout.CENTER);
        this.add(jPanel, BorderLayout.CENTER);
        registerDSChangeListener();
    }

    private void initRoleTree() {
        roleTree = new RoleTree() {
            public void refreshRoleTree(String selectedRole) {
                super.refreshRoleTree(selectedRole);
                changeAlreadyEditedPaneRole(selectedRole);
            }


            protected void doWithValueChanged(TreeSelectionEvent e) {
                super.doWithValueChanged(e);
                TreeNode root = (TreeNode) roleTree.getModel().getRoot();
                TreePath parent = new TreePath(root);
                setSelectedRole(roleTree.getSelectedRoleName(), parent);

            }

            protected void setTabRoleName(String roleName) {
                roleNames = roleTree.getSelectedRoleName();
            }
        };
        roleTree.setEnabled(true);
        roleTree.setEditable(false);
        roleTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                roleTree.setEditable(false);
            }
        });
    }

    private void changeAlreadyEditedPaneRole(String selectedRole) {
        RolesAlreadyEditedPane.getInstance().refreshDockingView();
        RoleTree roleTree = RolesAlreadyEditedPane.getInstance().getRoleTree();
        TreeNode root = (TreeNode) roleTree.getModel().getRoot();
        TreePath parent = new TreePath(root);
        roleTree.setSelectedRole(selectedRole, parent);
    }

    public void setDefaultSelectedRole() {
        //设置选中的节点
        TreeNode root = (TreeNode) roleTree.getModel().getRoot();
        TreePath parent = new TreePath(root);
        ExpandMutableTreeNode node = (ExpandMutableTreeNode) parent.getLastPathComponent();
        String selectedRole = null;
        if (singleton != null) {
            selectedRole = roleNames;
        }
        if (selectedRole == null) {
            if (node.getChildCount() <= 0 || node.getFirstChild().getChildCount() <= 0) {
                return;
            }
            selectedRole = node.getFirstChild().getChildAt(0).toString();
        }
        roleTree.setSelectedRole(selectedRole, parent);
    }


    public RoleTree getRoleTree() {
        return roleTree;
    }

    /**
     * 检查f)	每增删改一个角色信息（及时保存），先对比下privilege下之前的角色信息有没有发生变化（即在此期间有没有在其他途径中修改过）
     */
    private void checkChanges() {
        //如若有变化，则弹出下面的对话框
        int returnVal = FineJOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Role_changed_Is_Refresh") + "?",
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Refresh"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (returnVal == JOptionPane.OK_OPTION) {
            roleTree.refreshTreeNode();
            expandTree(roleTree, true);
            roleTree.updateUI();
        }

    }

    private JPanel iniToolBarPane() {
        ToolBarDef toolbarDef = new ToolBarDef();
        toolbarDef.addShortCut(refreshAction);
        UIToolbar toolBar = ToolBarDef.createJToolBar();
        toolbarDef.updateToolBar(toolBar);
        JPanel toolbarPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        toolbarPane.add(toolBar, BorderLayout.CENTER);
        return toolbarPane;
    }

    private void initbuttonGroup() {
    
        Icon[] iconArray = new Icon[]{BaseUtils.readIcon("/com/fr/web/images/platform/demo.png")};
        String[] textArray = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FS_Name")};
        buttonGroup = new UIHeadGroup(iconArray, textArray) {
            public void tabChanged(int index) {
                roleTree.setEditable(false);
                if (op != null) {
                    //判断是否可编辑
                    refreshDockingView();
                }
                setDefaultSelectedRole();
                if (singleton != null) {
                    changeAlreadyEditedPaneRole(roleNames);
                }
            }
        };
        buttonGroup.setBorder(BorderFactory.createMatteBorder(1, LEFT_GAP, 0, 0, UIConstants.LINE_COLOR));
        buttonGroup.setNeedLeftRightOutLine(false);
    }

    /**
     * 刷新界面
     */
    public void refreshDockingView() {
        populate(new RoleSourceOP());
    }

    private void populate(RoleSourceOP op) {
        this.op = op;
        roleTree.populate(op);
        expandTree(roleTree, true);
    }

    public String getViewTitle() {
        return null;
    }

    public Icon getViewIcon() {
        return null;
    }

    /**
     * 最佳定位
     * @return 定位
     */
    public Location preferredLocation() {
        return null;
    }

    /**
     * 注册数据库改变的响应的Listener
     */
    public void registerDSChangeListener() {
        DesignTableDataManager.addDsChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (DesignerMode.isAuthorityEditing()) {
                    roleTree.refreshTreeNode();
                    expandTree(roleTree, true);
                    roleTree.updateUI();
                }
            }
        });

    }

    /*
 * 刷新ReportletsTree
 */
    private class RefreshAction extends UpdateAction {

        public RefreshAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Refresh"));
            this.setSmallIcon(UIConstants.REFRESH_ICON);
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            roleTree.refreshTreeNode();
            expandTree(roleTree, true);
            roleTree.updateUI();
        }
    }

    /**
     * 展开树
     * @param tree 树
     * @param isExpand 是否展开
     */
    public void expandTree(JTree tree, boolean isExpand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(tree, new TreePath(root), isExpand);
    }


    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
}
