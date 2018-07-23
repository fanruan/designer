package com.fr.design.gui.itree.filetree;

import com.fr.base.FRContext;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.gui.itree.refreshabletree.RefreshableJTree;
import com.fr.file.filetree.FileNode;
import com.fr.file.filetree.FileNodeFilter;
import com.fr.general.ComparatorUtils;
import com.fr.locale.InterProviderFactory;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;
import com.fr.workspace.WorkContext;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * 文件结构树.
 */
public class EnvFileTree extends RefreshableJTree {

    protected FileNodeFilter filter;
    protected String treeRootPath = "";
    protected String[] subPaths;

    public EnvFileTree() {
        this(null, null);
    }

    public EnvFileTree(String[] subPaths, FileNodeFilter filter) {
        this("", subPaths, filter);
    }

    public EnvFileTree(String treeRootPath, String[] subPaths, FileNodeFilter filter) {
        super(new FileNode(treeRootPath, true));

        this.setTreeRootPath(treeRootPath);
        this.setFileNodeFilter(filter);
        this.setSubPaths(subPaths);

        /*一些自己的 init 放在这里，防止直接错误重写了父类的 init 方法导致子类不能使用 CheckBoxTree 的一些特性。*/
        this.putClientProperty("JTree.lineStyle", "Angled");

        // CellRenderer
        // 这里新建一个Label作为render是因为JTree在动态刷新的时候，节点上render画布的的宽度不会变，会使得一部分比较长的数据显示为
        DefaultTreeCellRenderer fileTreeCellRenderer = new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
                                                          boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                ExpandMutableTreeNode treeNode = (ExpandMutableTreeNode) value;
                Object userObj = treeNode.getUserObject();
                if (userObj instanceof FileNode) {
                    FileNode node = (FileNode) userObj;
                    String lock = node.getLock();
                    String name = node.getName();
                    if (treeNode.hasFullAuthority()) {
                        if (lock != null && !node.getUserID().equals(lock)) {
                            name = name + InterProviderFactory.getProvider().getLocText("Locked");
                            this.setIcon(FileTreeIcon.getIcon(node));
                        } else {
                            this.setIcon(FileTreeIcon.getIcon(node, false));
                        }
                    } else {
                        this.setIcon(FileTreeIcon.getFolderHalfImageIcon());
                    }
                    this.setText(name);
                } else if (userObj == PENDING) {
                    this.setIcon(null);
                    this.setText(PENDING.toString());
                }
                // 这里新建一个Label作为render是因为JTree在动态刷新的时候，节点上render画布的的宽度不会变，会使得一部分比较长的数据显示为"..."
                UILabel label = new UILabel();
                label.setText(getText());
                label.setIcon(getIcon());
                this.setSize(label.getPreferredSize());
                Dimension dim = label.getPreferredSize();
                dim.height += 2;
                this.setPreferredSize(dim);
                this.setBackgroundNonSelectionColor(UIConstants.TREE_BACKGROUND);
                this.setTextSelectionColor(Color.WHITE);
                this.setBackgroundSelectionColor(UIConstants.FLESH_BLUE);
                return this;
            }
        };
        this.setCellRenderer(fileTreeCellRenderer);

        this.setRootVisible(false);
        this.setShowsRootHandles(true);
        this.setEditable(false);
    }

    private void setTreeRootPath(String path) {
        if (path == null) {
            path = "";
        }

        this.treeRootPath = path;
    }

    public void setFileNodeFilter(FileNodeFilter filter) {
        this.filter = filter;
    }

    /*
     * 在当前tree中选中currentPath
     */
    public void selectPath(String currentPath) {
        if (currentPath == null) {
            return;
        }

        DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
        ExpandMutableTreeNode treeNode = (ExpandMutableTreeNode) m_model.getRoot();
        for (int i = 0, len = treeNode.getChildCount(); i < len; i++) {
            // 取出当前的childTreeNode,并append到searchingPath后面
            ExpandMutableTreeNode childTreeNode = (ExpandMutableTreeNode) treeNode.getChildAt(i);

            if (selectFilePath(childTreeNode, "", currentPath, m_model)) {
                break;
            }
        }

        TreePath selectedTreePath = this.getSelectionPath();
        if (selectedTreePath != null) {
            this.scrollPathToVisible(selectedTreePath);
        }
    }

    /*
     * 在currentTreeNode下找寻filePath
     *
     * prefix + currentTreeNode.getName() = currentTreeNode所对应的Path
     *
     * 返回currentTreeNode下是否找到了filePath
     */
    private boolean selectFilePath(ExpandMutableTreeNode currentTreeNode, String prefix, String filePath, DefaultTreeModel m_model) {
        FileNode fileNode = (FileNode) currentTreeNode.getUserObject();
        String nodePath = fileNode.getName();
        String currentTreePath = prefix + nodePath;

        // 如果equals,说明找到了,不必再找下去了
        if (ComparatorUtils.equals(new File(currentTreePath), new File(filePath))) {
            this.setSelectionPath(new TreePath(m_model.getPathToRoot(currentTreeNode)));
            return true;
        }
        // 如果当前路径是currentFilePath的ParentFile,则expandTreeNode,并继续往下找
        else if (EnvFileTree.isParentFile(currentTreePath, filePath)) {
            this.loadPendingChildTreeNode(currentTreeNode);

            prefix = currentTreePath + CoreConstants.SEPARATOR;
            for (int i = 0, len = currentTreeNode.getChildCount(); i < len; i++) {
                ExpandMutableTreeNode childTreeNode = (ExpandMutableTreeNode) currentTreeNode.getChildAt(i);

                if (selectFilePath(childTreeNode, prefix, filePath, m_model)) {
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    /*
     * 求当前TreeNode下所有的FileNode.
     */
    private FileNode[] listFileNodes(ExpandMutableTreeNode currentTreeNode) {
        if (currentTreeNode == null) {
            return new FileNode[0];
        }

        Object object = currentTreeNode.getUserObject();

        if (object instanceof FileNode) {
            return this.listFileNodes(((FileNode) object).getEnvPath());
        }

        return new FileNode[0];
    }

    /*
     * 求filePath这个String,求其路径下面的所有的FileNode
     */
    private FileNode[] listFileNodes(String filePath) {
        FileNode[] resFns = null;

        try {
            resFns = WorkContext.getCurrent() == null ? new FileNode[0] : FRContext.getFileNodes().list(filePath);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }

        if (resFns == null) {
            resFns = new FileNode[0];
        }

        // 用FileNodeFilter过滤一下
        if (filter != null) {
            java.util.List<FileNode> tList = new ArrayList<FileNode>();
            for (int i = 0; i < resFns.length; i++) {
                if (filter.accept(resFns[i])) {
                    tList.add(resFns[i]);
                }
            }

            resFns = tList.toArray(new FileNode[tList.size()]);
        }

        Arrays.sort(resFns, new FileNodeComparator());

        return resFns;
    }

    /*
     * 获取当前选中的FilePath的String,这个FilePath是需要拼起来的
     */
    public FileNode getSelectedFileNode() {
        TreePath selectedTreePath = this.getSelectionPath();
        if (selectedTreePath == null) {
            return null;
        }

        ExpandMutableTreeNode currentTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
        Object userObject = currentTreeNode.getUserObject();

        if (userObject instanceof FileNode) {
            return (FileNode) userObject;
        }

        return null;
    }

    /*
     * 改变Env后,根据构造函数时设置的RootPaths,重新加载
     */
    public void refreshEnv() {

        DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
        ExpandMutableTreeNode rootTreeNode = (ExpandMutableTreeNode) m_model.getRoot();
        rootTreeNode.removeAllChildren();

        FileNode[] fns;

        // 如果rootPaths是null的话列出所有文件
        if (subPaths == null) {
            fns = listFileNodes(this.treeRootPath);
        } else {
            // 重新加载新的FileDirectoryNode
            fns = new FileNode[subPaths.length];
            for (int i = 0; i < subPaths.length; i++) {
                fns[i] = new FileNode(StableUtils.pathJoin(this.treeRootPath, subPaths[i]), true);
            }
        }


        ExpandMutableTreeNode[] subTreeNodeArray = fileNodeArray2TreeNodeArray(fns);

        for (int i = 0; i < subTreeNodeArray.length; i++) {
            ExpandMutableTreeNode node = subTreeNodeArray[i];
            rootTreeNode.add(node);
        }

        m_model.reload(rootTreeNode);
    }

    /*
     * 设置当前Tree的rootPaths
     */
    private void setSubPaths(String[] subPaths) {
        this.subPaths = subPaths;
    }

    /**
     * currentTreeNode下面如果是PENDING的节点,加载之...
     */
    protected void loadPendingChildTreeNode(ExpandMutableTreeNode currentTreeNode) {
        if (currentTreeNode.isLeaf()) {
            return;
        }

        // 判断第一个孩子节点.UserObject是不是PENDING,如果是PENDING的话,需要重新加载这个TreeNode
        ExpandMutableTreeNode flag = (ExpandMutableTreeNode) currentTreeNode.getFirstChild();
        if (flag == null || flag.getUserObject() != PENDING) {
            return;
        }
        // 删除所有的节点.
        currentTreeNode.removeAllChildren();

        ExpandMutableTreeNode[] children = loadChildTreeNodes(currentTreeNode);
        for (ExpandMutableTreeNode c : children) {
            currentTreeNode.add(c);
        }
    }

    /*
     * 判断eTreeNode是否需要Refresh,可提前中止,返回true则表示提前中止,不需要Refresh
     */
    @Override
    protected boolean interceptRefresh(ExpandMutableTreeNode eTreeNode) {
        Object userObject = eTreeNode.getUserObject();
        if (userObject instanceof FileNode && !((FileNode) userObject).isDirectory()) {
            return true;
        }

        return eTreeNode.getChildCount() == 1 && ((ExpandMutableTreeNode) eTreeNode.getFirstChild()).getUserObject() == PENDING;
    }

    /*
     * 得到treeNode的子节点ExpandMutableTreeNode的数组
     */
    @Override
    protected ExpandMutableTreeNode[] loadChildTreeNodes(ExpandMutableTreeNode treeNode) {
        FileNode[] fnArray = listFileNodes(treeNode);

        return fileNodeArray2TreeNodeArray(fnArray);
    }

    /*
     * 把FileNode[]转成ExpandMutableTreeNode[]
     */
    private ExpandMutableTreeNode[] fileNodeArray2TreeNodeArray(FileNode[] fn_array) {
        ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[fn_array.length];
        for (int i = 0; i < res.length; i++) {
            FileNode fn = fn_array[i];
            res[i] = new ExpandMutableTreeNode(fn);
            if (fn.isDirectory()) {
                res[i].add(new ExpandMutableTreeNode());
            }
        }

        return res;
    }

    /*
     * 是否是父子关系的文件.
     */
    protected static boolean isParentFile(String parentFilePath, String childFilePath) {
        File parentFile = new File(parentFilePath);
        File childFile = new File(childFilePath);

        do {
            if (ComparatorUtils.equals(parentFile, childFile)) {
                return true;
            }
            childFile = childFile.getParentFile();
        } while (childFile != null);

        return false;
    }
}