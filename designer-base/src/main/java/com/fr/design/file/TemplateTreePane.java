/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.file;

import com.fr.base.FRContext;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.file.filetree.IOFileNodeFilter;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.lock.TplOperator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Objects;

import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class TemplateTreePane extends JPanel implements FileOperations {

    public static TemplateTreePane getInstance() {
        return HOLDER.singleton;
    }

    private static class HOLDER {
        private static TemplateTreePane singleton = new TemplateTreePane();
    }

    private TemplateFileTree reportletsTree;
    private FileToolbarStateChangeListener toolBarStateChangeListener;

    private TemplateTreePane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setPreferredSize(new Dimension(250, super.getPreferredSize().height));
        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(contentPane, BorderLayout.CENTER);

        reportletsTree = new TemplateFileTree();
        ToolTipManager.sharedInstance().registerComponent(reportletsTree);
        UIScrollPane scrollPane = new UIScrollPane(reportletsTree);
        scrollPane.setBorder(null);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        /*
         * Tree.MouseAdapter
         */
        MouseListener mouseFileTreeListener = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    openFile();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (toolBarStateChangeListener != null) {
                    toolBarStateChangeListener.stateChange();
                }
            }
        };
        // lx: add mouse listener
        this.reportletsTree.addMouseListener(mouseFileTreeListener);
        this.reportletsTree.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openFile();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openFile();
                }
                if (toolBarStateChangeListener != null) {
                    toolBarStateChangeListener.stateChange();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openFile();
                }
            }
        });
    }

    public TemplateFileTree getTemplateFileTree() {
        return this.reportletsTree;
    }


    /**
     * 选中的路径数
     *
     * @return 选中的路径数
     */
    public int countSelectedPath() {
        TreePath[] treePaths = reportletsTree.getSelectionPaths();
        if (treePaths == null) {
            return 0;
        }
        return treePaths.length;
    }

    /**
     * 选中的文件夹个数
     *
     * @return 选中的文件夹个数
     */

    public int countSelectedFolder() {

        if (reportletsTree.getSelectedFolderPaths() == null) {
            return 0;
        }

        return reportletsTree.getSelectedFolderPaths().length;
    }

    /**
     * 选中的文件个数
     *
     * @return 选中的文件
     */
    public int countSelectedFile() {

        if (reportletsTree.getSelectionPaths() == null) {
            return 0;
        }

        return reportletsTree.getSelectedTemplatePaths().length;
    }

    /**
     * 刷新
     */
    public void refreshDockingView() {
        reportletsTree.setFileNodeFilter(new IOFileNodeFilter(FRContext.getFileNodes().getSupportedTypes()));
        reportletsTree.refreshEnv();
    }


    @Override
    public boolean mkdir(String path) {
        return WorkContext.getWorkResource().createDirectory(path);
    }

    /**
     * 打开选中的报表文件
     */
    @Override
    public void openFile() {
        // 判断是否是远程设计的锁定文件
        if (!WorkContext.getCurrent().isLocal()) {
            FileNode node = reportletsTree.getSelectedFileNode();
            if (node == null) {
                return;
            }
            String lock = node.getLock();
            if (lock != null && !lock.equals(node.getUserID())) {
                return;
            }
        }
        String reportPath = reportletsTree.getSelectedTemplatePath();
        final String selectedFilePath = StableUtils.pathJoin(ProjectConstants.REPORTLETS_NAME, reportPath);
        DesignerContext.getDesignerFrame().openTemplate(new FileNodeFILE(new FileNode(selectedFilePath, false)));
    }

    /**
     * 打开文件夹
     */
    @Override
    public void showInExplorer() {
        FileNode fn = TemplateTreePane.this.reportletsTree.getSelectedFileNode();
        String filePath = StableUtils.pathJoin(WorkContext.getCurrent().getPath(), fn.getEnvPath());
        filePath = filePath.substring(0, filePath.lastIndexOf(CoreConstants.SEPARATOR));
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (Exception e) {
            IOUtils.openWindowsFolder(StableUtils.pathJoin(WorkContext.getCurrent().getPath(), fn.getEnvPath()));
        }
    }

    /**
     * 刷新
     */
    @Override
    public void refresh() {
        reportletsTree.refresh();
        FineLoggerFactory.getLogger().info(Toolkit.i18nText("Fine-Design_Basic_Template_File_Tree_Refresh_Successfully") + "!");
    }

    /**
     * 删除文件
     * 文件夹和文件均可删除
     * <p>
     * 当文件被锁时不能删除
     * 当文件夹中包含被锁文件时不能删除
     */
    @Override
    public void deleteFile() {


        ExpandMutableTreeNode[] treeNodes = reportletsTree.getSelectedTreeNodes();
        // 筛选可以删除的文件
        ArrayList<ExpandMutableTreeNode> deletableNodes = new ArrayList<>();
        ArrayList<ExpandMutableTreeNode> lockedNodes = new ArrayList<>();
        for (ExpandMutableTreeNode treeNode : treeNodes) {
            checkFreeOrLock(treeNode, deletableNodes, lockedNodes);
        }
        if (lockedNodes.isEmpty()) {

            String tipContent =
                    countSelectedFolder() > 0
                            ? Toolkit.i18nText("Fine-Design_Basic_Confirm_Delete_Folder")
                            : Toolkit.i18nText("Fine-Design_Basic_Confirm_Delete_File");

            if (FineJOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(),
                    tipContent,
                    Toolkit.i18nText("Fine-Design_Basic_Confirm"),
                    YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                // 删除所有选中的即可
                if (!deleteNodes(Arrays.asList(treeNodes))) {
                    FineJOptionPane.showConfirmDialog(null,
                            Toolkit.i18nText("Fine-Design_Basic_Delete_Failure"),
                            Toolkit.i18nText("Fine-Design_Basic_Error"),
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {

            String tipContent =
                    countSelectedFolder() > 0
                            ? Toolkit.i18nText("Fine-Design_Basic_Confirm_Delete_Unlock_File_And_Folder")
                            : Toolkit.i18nText("Fine-Design_Basic_Confirm_Delete_Unlock_File");

            if (deletableNodes.isEmpty()) {
                // 提醒被锁定模板无法删除
                FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                        Toolkit.i18nText("Fine-Design_Basic_Unable_Delete_Locked_File"),
                        Toolkit.i18nText("Fine-Design_Basic_Alert"),
                        WARNING_MESSAGE);
                return;
            }


            if (FineJOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(),
                    tipContent,
                    Toolkit.i18nText("Fine-Design_Basic_Confirm"),
                    YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                // 删除其他
                if (!deleteNodes(deletableNodes)) {
                    FineJOptionPane.showConfirmDialog(null,
                            Toolkit.i18nText("Fine-Design_Basic_Delete_Failure"),
                            Toolkit.i18nText("Fine-Design_Basic_Error"),
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        refreshAfterDelete();
    }

    private void refreshAfterDelete() {
        TreePath[] paths = reportletsTree.getSelectionPaths();
        if (paths == null) {
            reportletsTree.refresh();
        } else  {
            for (TreePath path : Objects.requireNonNull(reportletsTree.getSelectionPaths())) {
                reportletsTree.refreshParent(path);
            }
        }
    }

    private boolean deleteNodes(Collection<ExpandMutableTreeNode> nodes) {

        boolean success = true;
        for (ExpandMutableTreeNode treeNode : nodes) {
            Object node = treeNode.getUserObject();
            if (node instanceof FileNode) {
                FileNodeFILE nodeFILE = new FileNodeFILE((FileNode) node);
                if (nodeFILE.exists()) {
                    if (WorkContext.getCurrent().get(TplOperator.class).delete(nodeFILE.getPath())) {
                        HistoryTemplateListCache.getInstance().deleteFile(nodeFILE);
                    } else {
                        success = false;
                    }
                }
            }
        }
        return success;
    }

    private boolean checkFreeOrLock(ExpandMutableTreeNode node, ArrayList<ExpandMutableTreeNode> dNodes, ArrayList<ExpandMutableTreeNode> lNodes) {
        // 自己没锁
        boolean selfEmptyLock = false;
        Object userObj = node.getUserObject();
        if (userObj instanceof FileNode) {
            String lock = ((FileNode) userObj).getLock();
            selfEmptyLock = lock == null || ((FileNode) userObj).getUserID().equals(lock);
        }

        if (node.isLeaf()) {
            if (selfEmptyLock) {
                dNodes.add(node);
            } else {
                lNodes.add(node);
            }
            return selfEmptyLock;
        }

        ExpandMutableTreeNode[] children = reportletsTree.loadChildTreeNodes(node);

        boolean childrenEmptyLock = true;

        for (ExpandMutableTreeNode child : children) {
            childrenEmptyLock = checkFreeOrLock(child, dNodes, lNodes) && childrenEmptyLock;
        }

        boolean emptyLock = childrenEmptyLock && selfEmptyLock;
        if (emptyLock) {
            dNodes.add(node);
        } else {
            lNodes.add(node);
        }
        return emptyLock;
    }


    @Override
    public void lockFile() {
        throw new UnsupportedOperationException("unsupport now");
    }

    @Override
    public void unlockFile() {
        throw new UnsupportedOperationException("unsupport now");
    }


    @Override
    public String getFilePath() {
        return reportletsTree.getSelectedTemplatePath();
    }

    @Override
    public boolean access() {

        TreePath[] selectedTreePaths = reportletsTree.getSelectionPaths();

        if (ArrayUtils.isEmpty(selectedTreePaths)) {
            return false;
        }
        // 选中的是文件夹
        TreePath treePath = selectedTreePaths[0];
        ExpandMutableTreeNode currentTreeNode = (ExpandMutableTreeNode) treePath.getLastPathComponent();

        ExpandMutableTreeNode parentTreeNode = (ExpandMutableTreeNode) currentTreeNode.getParent();

        return parentTreeNode != null && parentTreeNode.hasFullAuthority();
    }

    @Override
    public boolean rename(FILE tplFile, String from, String to) {

        // 多人协作时判断是否有锁定的文件
        ExpandMutableTreeNode[] treeNodes = reportletsTree.getSelectedTreeNodes();
        // 筛选可以重命名的文件
        ArrayList<ExpandMutableTreeNode> unlockedNodes = new ArrayList<>();
        ArrayList<ExpandMutableTreeNode> lockedNodes = new ArrayList<>();
        for (ExpandMutableTreeNode treeNode : treeNodes) {
            checkFreeOrLock(treeNode, unlockedNodes, lockedNodes);
        }

        if (!lockedNodes.isEmpty()) {
            FineJOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(),
                    Toolkit.i18nText("Fine-Design_Basic_Warn_Rename_Lock_File"),
                    Toolkit.i18nText("Fine-Design_Basic_Alert"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            return true;
        }

        try {
            // 接收的是WEB-INF下的路径
            return WorkContext.getCurrent().get(TplOperator.class).rename(from, to);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public FileNode getFileNode() {
        return reportletsTree.getSelectedFileNode();
    }

    public void setToolbarStateChangeListener(FileToolbarStateChangeListener listener) {
        this.toolBarStateChangeListener = listener;
    }


    /**
     * 仅支持在拥有完整权限的文件夹下进行新建和重命名操作，那么是可以看到改文件夹下所有文件的。
     *
     * @param newName 原名
     * @param suffix  后缀名
     * @return 是否有重名的
     */
    @Override
    public boolean duplicated(String newName, String suffix) {

        // 选中的节点
        TreePath treePath = reportletsTree.getSelectionPath();
        if (treePath == null) {
            return false;
        }
        DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        TreeNode parentTreeNode = currentTreeNode.getParent();

        Enumeration children = parentTreeNode.children();

        while (children.hasMoreElements()) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
            Object object = childNode.getUserObject();
            if (object instanceof FileNode) {
                if (ComparatorUtils.equals(((FileNode) object).getName(), newName + suffix)) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }
}
