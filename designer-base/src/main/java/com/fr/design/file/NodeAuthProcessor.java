package com.fr.design.file;

import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.report.DesignAuthority;
import com.fr.stable.CoreConstants;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.authority.AuthorityOperator;
import com.fr.workspace.server.authority.decision.DecisionOperator;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;

public class NodeAuthProcessor {

    public static NodeAuthProcessor getInstance() {
        return NodeAuthProcessor.HOLDER.singleton;
    }

    private static class HOLDER {
        private static NodeAuthProcessor singleton = new NodeAuthProcessor();
    }

    /**
     * 远程设计拥有全部权限的文件夹路径
     */
    private ArrayList<String> authPaths = new ArrayList<>();


    private NodeAuthProcessor() {

    }

    public void refresh() {

        authPaths.clear();
        if (!WorkContext.getCurrent().isLocal()) {
            try {
                String userName = WorkContext.getCurrent().getConnection().getUserName();
                DesignAuthority[] authorities = null;
                try {
                    String userId = WorkContext.getCurrent().get(DecisionOperator.class).getUserIdByName(userName);
                    authorities = WorkContext.getCurrent().get(AuthorityOperator.class).getAuthorities(userId);
                } catch(UndeclaredThrowableException e) {
                    // 兼容旧版本的服务器
                    authorities = WorkContext.getCurrent().get(AuthorityOperator.class).getAuthorities();
                }
                // 远程设计获取设计成员的权限列表
                DesignAuthority authority = null;

                if (authorities != null) {
                    for (DesignAuthority designAuthority : authorities) {
                        if (ComparatorUtils.equals(designAuthority.getUsername(), userName)) {
                            authority = designAuthority;
                        }
                    }
                }
                if (authority != null) {
                    for (DesignAuthority.Item item : authority.getItems()) {
                        if (item.getType()) {
                            authPaths.add(item.getPath());
                        }
                    }
                }
            } catch (Exception exception) {
                FineLoggerFactory.getLogger().error(exception.getMessage(), exception);
            }
        }
    }

    public void clear() {
        authPaths.clear();
    }


    /**
     * 生成带权限信息的文件节点，供另存对话框使用
     *
     * @param fileNodes file nodes
     * @return 带权限信息的文件节点
     */
    public FILE[] parser2FILEArray(FileNode[] fileNodes, String envPath) {

        boolean isLocal = WorkContext.getCurrent().isLocal();
        boolean isRoot = WorkContext.getCurrent().isRoot();
        FILE[] res = new FILE[fileNodes.length];
        for (int i = 0; i < res.length; i++) {
            FileNode fn = fileNodes[i];

            if (fn.isDirectory()) {
                if (isLocal || isRoot) {
                    res[i] = new FileNodeFILE(fileNodes[i], envPath);
                } else {
                    boolean hasFullAuthority = isContained(fn);
                    res[i] = new FileNodeFILE(fileNodes[i], envPath, hasFullAuthority);
                }
            } else {
                res[i] = new FileNodeFILE(fileNodes[i], envPath);
            }
        }
        return res;
    }

    /**
     * 生成带权限信息的文件节点，供另存对话框使用
     *
     * @param fileNode file nodes
     * @return 带权限信息的文件节点
     */
    public FILE fixFILENodeAuth(FileNode fileNode) {

        boolean isLocal = WorkContext.getCurrent().isLocal();
        boolean isRoot = WorkContext.getCurrent().isRoot();

        if (fileNode.isDirectory()) {
            if (isLocal || isRoot) {
                return new FileNodeFILE(fileNode);
            } else {
                boolean hasFullAuthority = isContained(fileNode);
                return new FileNodeFILE(fileNode, hasFullAuthority);
            }
        } else {
            return new FileNodeFILE(fileNode);
        }
    }

    /**
     * 生成带权限信息的文件节点，供另存对话框使用
     *
     * @param fileNode file nodes
     * @return 带权限信息的文件节点
     */
    public boolean fixFileNodeAuth(FileNode fileNode) {

        boolean isLocal = WorkContext.getCurrent().isLocal();
        boolean isRoot = WorkContext.getCurrent().isRoot();

        if (fileNode.isDirectory()) {
            if (isLocal || isRoot) {
                return true;
            } else {
                return isContained(fileNode);
            }
        } else {
            return true;
        }
    }

    /**
     * 生成带权限信息的目录树节点，
     * 提供给目录树使用
     *
     * @param fileNodes file nodes
     * @return 带权限信息的目录树节点
     */
    public ExpandMutableTreeNode[] parser2TreeNodeArray(FileNode[] fileNodes) {
        boolean isLocal = WorkContext.getCurrent().isLocal();
        boolean isRoot = WorkContext.getCurrent().isRoot();
        ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[fileNodes.length];
        for (int i = 0; i < res.length; i++) {
            FileNode fn = fileNodes[i];
            res[i] = new ExpandMutableTreeNode(fn);
            if (fn.isDirectory()) {
                res[i].add(new ExpandMutableTreeNode());
                if (isLocal || isRoot) {
                    res[i].setFullAuthority(true);
                } else {
                    boolean hasFullAuthority = isContained(fn);
                    res[i].setFullAuthority(hasFullAuthority);
                }
            }
        }
        return res;
    }

    public void fixTreeNodeAuth(ExpandMutableTreeNode treeNode) {
        if (treeNode == null) {
            return;
        }

        Object object = treeNode.getUserObject();
        if (object instanceof FileNode) {
            boolean isLocal = WorkContext.getCurrent().isLocal();
            boolean isRoot = WorkContext.getCurrent().isRoot();
            if (((FileNode) object).isDirectory()) {
                if (isLocal || isRoot) {
                    treeNode.setFullAuthority(true);
                } else {
                    boolean hasFullAuthority = isContained((FileNode) object);
                    treeNode.setFullAuthority(hasFullAuthority);
                }
            }
        }


    }

    private boolean isContained(FileNode fileNode) {

        for (String auPath : authPaths) {
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


}
