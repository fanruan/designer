package com.fr.design.gui.itree.filetree;

import com.fr.base.FRContext;
import com.fr.base.extension.FileExtension;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.report.DesignAuthority;
import com.fr.stable.ArrayUtils;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.authority.AuthorityOperator;

import javax.swing.text.Position;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * 显示Env下的reportlets目录下面的所有cpt文件
 */
public class TemplateFileTree extends EnvFileTree {


    /**
     * 远程设计拥有全部权限的文件夹路径
     */
    private ArrayList<String> paths = new ArrayList<>();

    public TemplateFileTree() {
        super(ProjectConstants.REPORTLETS_NAME, null, null);
    }

    /*
     * 选中reportPath
     */
    public void setSelectedTemplatePath(String templatePath) {
        this.selectPath(templatePath);
    }

    /**
     * 返回选中的Template的路径
     */
    public String getSelectedTemplatePath() {
        FileNode fn = this.getSelectedFileNode();
        if (fn != null && !fn.isDirectory()) {
            String envPath = fn.getEnvPath();

            if (envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
                return envPath.substring(ProjectConstants.REPORTLETS_NAME.length());
            }
        }

        return null;
    }

    public String[] getSelectedTemplatePaths() {
        TreePath[] selectedTreePaths = this.getSelectionPaths();
        if (ArrayUtils.isEmpty(selectedTreePaths)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List<String> selectedPathList = new ArrayList<String>();
        for (TreePath treepath : selectedTreePaths) {
            ExpandMutableTreeNode currentTreeNode = (ExpandMutableTreeNode) treepath.getLastPathComponent();
            Object userObject = currentTreeNode.getUserObject();
            if (userObject instanceof FileNode) {
                FileNode fn = (FileNode) userObject;
                if (!fn.isDirectory()) {
                    String envPath = fn.getEnvPath();
                    if (envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
                        selectedPathList.add(envPath.substring(ProjectConstants.REPORTLETS_NAME.length()));
                    }
                }
            }
        }


        return selectedPathList.toArray(new String[0]);
    }

    @Override
    public TreePath getNextMatch(String prefix, int startingRow, Position.Bias bias) {

        int max = getRowCount();
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        if (startingRow < 0 || startingRow >= max) {
            throw new IllegalArgumentException();
        }
        prefix = prefix.toUpperCase();
        // start search from the next/previous element from the selected element
        int increment = (bias == Position.Bias.Forward) ? 1 : -1;
        int row = startingRow;
        do {
            TreePath path = getPathForRow(row);
            String text = convertValueToText(
                    path.getLastPathComponent(), isRowSelected(row),
                    isExpanded(row), true, row, false);

            if (text.toUpperCase().startsWith(prefix)) {
                return path;
            }
            row = (row + increment + max) % max;
        } while (row != startingRow);
        return null;
    }

    public FileNode[] listFile(String path) {
        return FRContext.getFileNodes().list(
                path,
                new FileExtension[]{FileExtension.CPT, FileExtension.FRM, FileExtension.CHT, FileExtension.XLS, FileExtension.XLSX});
    }

    /*
     * 改变Env后,根据构造函数时设置的RootPaths,重新加载
     */
    @Override
    public void refreshEnv() {
        paths.clear();

        if (!WorkContext.getCurrent().isLocal()) {
            try {
                String username = WorkContext.getConnector().currentUser();
                // 远程设计获取全部设计成员的权限列表
                DesignAuthority[] authorities = WorkContext.getCurrent().get(AuthorityOperator.class).getAuthorities();
                DesignAuthority authority = null;

                if (authorities != null) {
                    for (DesignAuthority designAuthority : authorities) {
                        if (ComparatorUtils.equals(designAuthority.getUsername(), username)) {
                            authority = designAuthority;
                        }
                    }
                }
                if (authority != null) {
                    for (DesignAuthority.Item item : authority.getItems()) {
                        if (item.getType()) {
                            paths.add(item.getPath());
                        }
                    }
                }
            } catch (Exception exception) {
                FineLoggerFactory.getLogger().error(exception.getMessage(), exception);
            }
        }


        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) this.getModel();
        ExpandMutableTreeNode rootTreeNode = (ExpandMutableTreeNode) defaultTreeModel.getRoot();
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

        ExpandMutableTreeNode[] subTreeNodes = fileNodeArray2TreeNodeArray(fns);

        for (ExpandMutableTreeNode node : subTreeNodes) {
            rootTreeNode.add(node);
        }

        defaultTreeModel.reload(rootTreeNode);
    }

    @Override
    protected ExpandMutableTreeNode[] loadChildTreeNodes(ExpandMutableTreeNode treeNode) {
        FileNode[] fnArray = listFileNodes(treeNode);

        return fileNodeArray2TreeNodeArray(fnArray);
    }

    /*
     * 把FileNode[]转成ExpandMutableTreeNode[]
     */
    private ExpandMutableTreeNode[] fileNodeArray2TreeNodeArray(FileNode[] fileNodes) {
        boolean isLocal = WorkContext.getCurrent().isLocal();
        ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[fileNodes.length];
        for (int i = 0; i < res.length; i++) {
            FileNode fn = fileNodes[i];
            res[i] = new ExpandMutableTreeNode(fn);
            if (fn.isDirectory()) {
                res[i].add(new ExpandMutableTreeNode());
                if (isLocal || WorkContext.getCurrent().isRoot()) {
                    res[i].setFullAuthority(true);
                } else {
                    boolean hasFullAuthority = isContained(fn);
                    res[i].setFullAuthority(hasFullAuthority);
                }
            }
        }

        return res;
    }

    private boolean isContained(FileNode fileNode) {

        for (String auPath : paths) {
            if (isContained(auPath, fileNode)) {
                return true;
            }
        }
        return false;
    }

    private boolean isContained(String auPath, FileNode fileNode) {
        auPath = ProjectConstants.REPORTLETS_NAME + CoreConstants.SEPARATOR + auPath;
        String fileName = fileNode.getEnvPath();
        String[] auPaths = auPath.split(CoreConstants.SEPARATOR);
        String[] nodePaths = fileName.split(CoreConstants.SEPARATOR);
        // 待判断目录是有权限目录或者有权限目录的子目录，全部权限
        if (auPaths.length <= nodePaths.length) {
            for (int i = 0; i < auPaths.length; i++) {
                if (!auPaths[i].equals(nodePaths[i])) {
                    return false;
                }
            }
            return fileNode.isDirectory();
        }
        // 其他情况半权限
        else {
            return false;
        }
    }


    private FileNode[] listFileNodes(String filePath) {
        FileNode[] fileNodes = null;
        try {
            fileNodes = listFile(filePath);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        if (fileNodes == null) {
            fileNodes = new FileNode[0];
        }
        // 用FileNodeFilter过滤一下
        if (filter != null) {
            List<FileNode> list = new ArrayList<FileNode>();
            for (FileNode fileNode : fileNodes) {
                if (filter.accept(fileNode)) {
                    list.add(fileNode);
                }
            }

            fileNodes = list.toArray(new FileNode[list.size()]);
        }

        Arrays.sort(fileNodes, new FileNodeComparator());

        return fileNodes;
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


}