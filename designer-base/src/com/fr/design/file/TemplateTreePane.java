/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.file;

import com.fr.base.FRContext;
import com.fr.base.io.FileAssistUtils;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.file.filetree.IOFileNodeFilter;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CoreConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

public class TemplateTreePane extends JPanel implements FileOperations {

    public static TemplateTreePane getInstance() {
        return HOLDER.singleton;
    }

    private static class HOLDER {
        private static TemplateTreePane singleton = new TemplateTreePane();
    }

    private TemplateFileTree reportletsTree;
    private FileToolbarStateChangeListener toobarStateChangeListener;

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

        this.reportletsTree.addMouseListener(mouseFileTreeListener);// lx: add
        // mouse
        // listener
        this.reportletsTree.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openSelectedReport();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openSelectedReport();
                }
                if (toobarStateChangeListener != null) {
                    toobarStateChangeListener.stateChange();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    openSelectedReport();
                }
            }
        });
    }

    public TemplateFileTree getTemplateFileTree() {
        return this.reportletsTree;
    }

    /**
     * 刷新
     */
    public void refreshDockingView() {
        reportletsTree.setFileNodeFilter(new IOFileNodeFilter(FRContext.getCurrentEnv().getSupportedTypes()));
        reportletsTree.refreshEnv();
    }

    /*
     * Tree.MouseAdapter
     */
    MouseListener mouseFileTreeListener = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent evt) {
            if (SwingUtilities.isRightMouseButton(evt)) {
                return;
            } else if (evt.getClickCount() == 2) {
                openSelectedReport();
            }


        }

        public void mouseReleased(MouseEvent e) {
            if (toobarStateChangeListener != null) {
                toobarStateChangeListener.stateChange();
            }
        }
    };


    /**
     * 打开选中的报表文件
     */
    public void openSelectedReport() {
        String reportPath = reportletsTree.getSelectedTemplatePath();
        final String selectedFilePath = StableUtils.pathJoin(new String[]{ProjectConstants.REPORTLETS_NAME, reportPath});
        DesignerContext.getDesignerFrame().openTemplate(new FileNodeFILE(new FileNode(selectedFilePath, false)));
    }

    /**
     * 打开文件夹
     */
    public void openContainerFolder() {
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
    public void refresh() {
        reportletsTree.refresh();
        FineLoggerFactory.getLogger().info(Inter.getLocText(new String[]{"File-tree", "Refresh_Successfully"}) + "!");
    }

    /**
     * 删除文件
     */
    public void deleteFile() {
        String[] reportPaths = reportletsTree.getSelectedTemplatePaths();
        if (reportPaths.length == 0) {
            return;
        }
        if (JOptionPane.showConfirmDialog(null, Inter.getLocText("Confirm-Delete-File")) != JOptionPane.OK_OPTION) {
            return;
        }
        for (String reportPath : reportPaths) {
            FileNodeFILE nodeFile = new FileNodeFILE(new FileNode(StableUtils.pathJoin(new String[]{ProjectConstants.REPORTLETS_NAME, reportPath}), false));

            if (nodeFile.isLocked()) {
                if (JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("fileLocked_undeleted"),
                        Inter.getLocText("Error"), JOptionPane.YES_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
                    refreshDockingView();
                }
                break;
            }
            if (nodeFile.exists()) {
                String path = StableUtils.pathJoin(nodeFile.getEnvPath(), nodeFile.getPath());
                FileAssistUtils.moveToTrash(nodeFile.getPath());
                deleteHistory(path.replaceAll("/", "\\\\"));
            } else {
                JOptionPane.showMessageDialog(this, Inter.getLocText("Warning-Template_Do_Not_Exsit"), ProductConstants.PRODUCT_NAME,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        reportletsTree.refresh();
    }

    @Override
    public void lockFile() {
        throw new UnsupportedOperationException("unsupport now");
    }

    @Override
    public void unLockFile() {
        throw new UnsupportedOperationException("unsupport now");
    }

    private void deleteHistory(String fileName) {
        int index = HistoryTemplateListPane.getInstance().contains(fileName);
        int size = HistoryTemplateListPane.getInstance().getHistoryCount();
        if (index == -1) {
            return;
        }
        //如果打开过，则删除，实时刷新多tab面板
        HistoryTemplateListPane.getInstance().getHistoryList().remove(index);
        int openfileCount = HistoryTemplateListPane.getInstance().getHistoryCount();
        if (openfileCount == 0) {
            DesignerContext.getDesignerFrame().addAndActivateJTemplate();
        }
        MutilTempalteTabPane.getInstance().repaint();
        if (size == index + 1 && index != 0) {
            //如果删除的是最后一个Tab，则定位到前一个
            MutilTempalteTabPane.getInstance().setSelectedIndex(index - 1);
        }
        JTemplate selectedfile = MutilTempalteTabPane.getInstance().getSelectedFile();
        if (!HistoryTemplateListPane.getInstance().isCurrentEditingFile(selectedfile.getFullPathName())) {
            //如果此时面板上的实时刷新的selectedIndex得到的和历史的不一样
            DesignerContext.getDesignerFrame().activateJTemplate(selectedfile);
        }
        MutilTempalteTabPane.getInstance().repaint();
    }


    public String getSelectedTemplatePath() {
        return reportletsTree.getSelectedTemplatePath();
    }

    public void setToobarStateChangeListener(FileToolbarStateChangeListener toobarStateChangeListener) {
        this.toobarStateChangeListener = toobarStateChangeListener;
    }

    /**
     * 文件名是否存在
     *
     * @param newName 原名
     * @param oldName 新的文件名
     * @param suffix  后缀名
     * @return 是否存在
     */
    public boolean isNameAlreadyExist(String newName, String oldName, String suffix) {
        boolean isNameAlreadyExist = false;

        TemplateFileTree tt = reportletsTree;
        DefaultMutableTreeNode gen = (DefaultMutableTreeNode) tt.getModel().getRoot();
        ArrayList<String> al = new ArrayList<String>();

        findFiles(gen, al);

        for (int i = 0; i < al.size(); i++) {
            if (ComparatorUtils.equals(al.get(i), newName + suffix)) {
                isNameAlreadyExist = true;
                break;
            }
        }

        if (ComparatorUtils.equals(newName, oldName)) {
            isNameAlreadyExist = false;
        }

        return isNameAlreadyExist;
    }


    private void findFiles(DefaultMutableTreeNode node, ArrayList<String> al) {
        String[] str = new String[node.getChildCount()];
        for (int j = 0; j < node.getChildCount(); j++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(j);
            if (childNode.getChildCount() > 0) {
                findFiles(childNode, al);
            } else {
                str[j] = node.getChildAt(j).toString();
                if (str[j].contains(".")) {
                    al.add(str[j]);
                }
            }
        }
    }
}