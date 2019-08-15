package com.fr.design.gui.itree.filetree;

import com.fr.base.FRContext;
import com.fr.base.extension.FileExtension;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.file.NodeAuthProcessor;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.mainframe.App;
import com.fr.file.filetree.FileNode;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.text.Position;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * 显示Env下的reportlets目录下面的所有cpt文件
 */
public class TemplateFileTree extends EnvFileTree {

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

    public String[] getSelectedFolderPaths() {
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
                if (fn.isDirectory()) {
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
        // 支持插件扩展, 先从env的filter拿, 再从插件拿
        Set<FileExtension> supportTypes = createFileExtensionFilter();
        return FRContext.getFileNodes().list(
                path,
                supportTypes.toArray(new FileExtension[supportTypes.size()])
                );
    }

    private Set<FileExtension> createFileExtensionFilter() {
        Set<FileExtension> supportTypes = new HashSet<FileExtension>();
        if (filter != null) {
            for (String temp : filter.getSupportedTypes()) {
                supportTypes.add(FileExtension.parse(temp));
            }
        }
        Set<App> apps = ExtraDesignClassManager.getInstance().getArray(App.MARK_STRING);
        for (App temp : apps) {
            for (String extendsion : temp.defaultExtensions()) {
                supportTypes.add(FileExtension.parse(extendsion));
            }
        }
        return supportTypes;
    }

    /*
     * 改变Env后,根据构造函数时设置的RootPaths,重新加载
     */
    @Override
    public void refreshEnv() {
        NodeAuthProcessor.getInstance().refresh();

        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) this.getModel();
        ExpandMutableTreeNode rootTreeNode = (ExpandMutableTreeNode) defaultTreeModel.getRoot();
        NodeAuthProcessor.getInstance().fixTreeNodeAuth(rootTreeNode);
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
    public ExpandMutableTreeNode[] loadChildTreeNodes(ExpandMutableTreeNode treeNode) {

        FileNode[] fnArray = listFileNodes(treeNode);

        return fileNodeArray2TreeNodeArray(fnArray);
    }


    public ExpandMutableTreeNode[] getSelectedTreeNodes() {
        TreePath[] paths = this.getSelectionPaths();

        if (paths == null) {
            return new ExpandMutableTreeNode[0];
        }

        ArrayList<ExpandMutableTreeNode> res = new ArrayList<>();

        for (TreePath path : paths) {
            res.add((ExpandMutableTreeNode) path.getLastPathComponent());
        }
        return res.toArray(new ExpandMutableTreeNode[res.size()]);
    }

    /*
     * 把FileNode[]转成ExpandMutableTreeNode[]
     */
    private ExpandMutableTreeNode[] fileNodeArray2TreeNodeArray(FileNode[] fileNodes) {
        return NodeAuthProcessor.getInstance().parser2TreeNodeArray(fileNodes);
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
        Arrays.sort(fileNodes, new FileNodeComparator(FileNodeConstants.getSupportFileTypes()));

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