package com.fr.file;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.design.file.NodeAuthProcessor;
import com.fr.design.gui.itree.filetree.FileNodeComparator;
import com.fr.design.gui.itree.filetree.FileTreeIcon;
import com.fr.design.i18n.Toolkit;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.io.EncryptUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import com.fr.workspace.WorkspaceEvent;
import com.fr.workspace.resource.WorkResourceTempRenameStream;
import com.fr.workspace.server.lock.TplOperator;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class FileNodeFILE implements FILE {


    private static String webRootPath = FRContext.getCommonOperator().getWebRootPath();
    private static String[] supportTypes = FRContext.getFileNodes().getSupportedTypes();

    static {
        EventDispatcher.listen(WorkspaceEvent.AfterSwitch, new Listener<Workspace>() {
            @Override
            public void on(Event event, Workspace workspace) {
                webRootPath = FRContext.getCommonOperator().getWebRootPath();
                supportTypes = FRContext.getFileNodes().getSupportedTypes();
            }
        });
    }


    private FileNode node;

    //记录下FILE对应的运行环境,每次创建都设置下当前的运行环境
    private String envPath;

    /**
     * 是否有全部权限
     */
    private boolean hasFullAuth = true;

    public FileNodeFILE(FileNodeFILE parent, String name, boolean isDir) {

        FileNode fn = parent.node;
        String parentDir;
        if (fn.isDirectory()) {
            parentDir = fn.getEnvPath();
        } else {
            parentDir = fn.getParent();
        }

        this.node = new FileNode(StableUtils.pathJoin(parentDir, name), isDir);
        this.envPath = WorkContext.getCurrent().getPath();
        this.hasFullAuth = NodeAuthProcessor.getInstance().checkFileNodeAuth(node);
    }


    public FileNodeFILE(FileNode node) {
        this.node = node;
        this.envPath = WorkContext.getCurrent().getPath();
        this.hasFullAuth = NodeAuthProcessor.getInstance().checkFileNodeAuth(node);
    }

    public FileNodeFILE(FileNode node, boolean hasFullAuth) {
        this(node);
        this.hasFullAuth = hasFullAuth;
    }

    public FileNodeFILE(String envPath) {
        this.node = null;
        this.envPath = envPath;
    }

    public FileNodeFILE(FileNode node, String envPath) {
        this.node = node;
        this.envPath = envPath;
        this.hasFullAuth = NodeAuthProcessor.getInstance().checkFileNodeAuth(node);
    }

    public FileNodeFILE(FileNode node, String envPath, boolean hasFullAuth) {
        this(node, envPath);
        this.hasFullAuth = hasFullAuth;
    }


    /**
     * @return 是否有完整权限
     */
    public boolean hasFullAuth() {
        return hasFullAuth;
    }

    /**
     * 前缀
     *
     * @return 前缀
     */
    @Override
    public String prefix() {

        if (ComparatorUtils.equals(getEnvPath(), webRootPath)) {
            return FILEFactory.WEBREPORT_PREFIX;
        }
        return FILEFactory.ENV_PREFIX;
    }

    /**
     * @return
     */
    public String getEnvPath() {

        return this.envPath;
    }

    /**
     * 是否是目录
     *
     * @return 是则返回true
     */
    @Override
    public boolean isDirectory() {

        return ComparatorUtils.equals(node, null) || node.isDirectory();
    }

    /**
     * @return
     */
    @Override
    public String getName() {

        if (node == null) {
            return null;
        }

        if (ComparatorUtils.equals(node.getEnvPath(), ProjectConstants.REPORTLETS_NAME)) {
            return Toolkit.i18nText("Fine-Design_Basic_Utils_Report_Runtime_Env");
        } else {
            return node.getName();
        }
    }

    /**
     * @return
     */
    @Override
    public Icon getIcon() {

        if (node == null) {
            return null;
        }

        if (ComparatorUtils.equals(node.getEnvPath(), ProjectConstants.REPORTLETS_NAME)) {
            return BaseUtils.readIcon("/com/fr/base/images/oem/logo.png");
        } else {

            if (!hasFullAuth) {
                return FileTreeIcon.getFolderHalfImageIcon();
            }

            return FileTreeIcon.getIcon(node);
        }
    }

    /**
     * @return
     */
    @Override
    public String getPath() {

        if (node == null) {
            return "";
        }

        return node.getEnvPath();
    }

    /**
     * @param path
     */
    @Override
    public void setPath(String path) {

        node.setEnvPath(path);
    }

    /**
     * @return
     */
    @Override
    public FILE getParent() {

        if (node == null) {
            return null;
        }

        return new FileNodeFILE(new FileNode(node.getParent(), true));
    }

    /**
     * 文件
     *
     * @return 文件组
     */
    @Override
    public FILE[] listFiles() {

        if (ComparatorUtils.equals(node, null)) {
            node = new FileNode(CoreConstants.SEPARATOR, true);
        }
        if (!node.isDirectory()) {
            return new FILE[]{this};
        }

        try {
            FileNode[] nodeArray;
            nodeArray = listFile(node.getEnvPath());
            Arrays.sort(nodeArray, new FileNodeComparator(supportTypes));

            return fileNodeArray2FILEArray(nodeArray, envPath);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return new FILE[0];
        }
    }

    private FILE[] fileNodeArray2FILEArray(FileNode[] nodeArray, String envPath) {
        return NodeAuthProcessor.getInstance().parser2FILEArray(nodeArray, envPath);
    }


    /**
     * 根目录文件地址
     *
     * @param rootFilePath 根文件路径
     * @return 返回文件节点
     */
    private FileNode[] listFile(String rootFilePath) {

        try {
            if (ComparatorUtils.equals(envPath, webRootPath)) {
                return FRContext.getFileNodes().listWebRootFile(rootFilePath);
            } else {
                return FRContext.getFileNodes().list(rootFilePath);
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return new FileNode[0];
    }

    /**
     * 创建文件夹
     *
     * @param name 文件夹名字
     * @return 创建成功返回true
     */
    @Override
    public boolean createFolder(String name) {

        if (ComparatorUtils.equals(node, null) || !node.isDirectory()) {
            return false;
        }

        try {
            return WorkContext.getWorkResource().createDirectory(StableUtils.pathJoin(node.getEnvPath(), name));
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 是否锁住
     *
     * @return 文件被锁返回true
     */
    public boolean isLocked() {

        if (node == null) {
            return false;
        }

        try {
            return FRContext.getCommonOperator().fileLocked(node.getEnvPath());
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 是否存在
     *
     * @return 文件存在返回 true
     */
    @Override
    public boolean exists() {

        if (node == null) {
            return false;
        }
        // 当运行环境不一致，返回false
        if (!isCurrentEnv()) {
            return false;
        }

        try {
            return WorkContext.getWorkResource().exist(node.getEnvPath());
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 是否是当前环境
     *
     * @return 是报表当前环境返回true
     */
    public boolean isCurrentEnv() {

        return ComparatorUtils.equals(WorkContext.getCurrent().getPath(), envPath);
    }

    /**
     * 创建文件
     *
     * @return 成功返回true
     */
    @Override
    public boolean mkfile() {

        if (node == null) {
            return false;
        }

        try {
            return WorkContext.getWorkResource().createFile(node.getEnvPath());
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 作为输入流
     *
     * @return 输入流
     * @throws Exception
     */
    @Override
    public InputStream asInputStream() throws Exception {

        if (node == null) {
            return null;
        }

        String envPath = node.getEnvPath();
        // envPath必须以reportlets开头
        if (!envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            return null;
        }
        InputStream in = new ByteArrayInputStream(
                WorkContext.getCurrent().get(TplOperator.class).readAndLockFile(
                        StableUtils.pathJoin(
                                ProjectConstants.REPORTLETS_NAME,
                                envPath.substring(ProjectConstants.REPORTLETS_NAME.length() + 1)
                        )
                )
        );

        return envPath.endsWith(".cpt") || envPath.endsWith(".frm")
                ? XMLEncryptUtils.decodeInputStream(EncryptUtils.decodeInputStream(in)) : in;
    }

    /**
     * 作为输出流
     *
     * @return 返回输出流
     * @throws Exception
     */
    @Override
    public OutputStream asOutputStream() throws Exception {

        if (ComparatorUtils.equals(node, null)) {
            return null;
        }

        String envPath = node.getEnvPath();
        // envPath必须以reportlets开头
        if (!envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            return null;
        }
        return new WorkResourceTempRenameStream(envPath);
    }

    /**
     * 关闭模板
     *
     * @throws Exception
     */
    @Override
    public void closeTemplate() throws Exception {

        if (node == null) {
            return;
        }

        String envPath = node.getEnvPath();
        // envPath必须以reportlets开头
        if (!envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            return;
        }

        FRContext.getCommonOperator().unlockTemplate(
                envPath.substring(ProjectConstants.REPORTLETS_NAME.length() + 1));
    }

    /**
     * 得到环境的全名
     *
     * @return
     */
    @Override
    public String getEnvFullName() {

        return this.node.getEnvPath().substring(ProjectConstants.REPORTLETS_NAME.length() + 1);
    }

    /**
     * 是否是内存文件
     *
     * @return 是则返回true
     */
    @Override
    public boolean isMemFile() {

        return false;
    }

    /**
     * 是否是环境文件
     *
     * @return 是则返回true
     */
    @Override
    public boolean isEnvFile() {

        return true;
    }

    /**
     * 是佛相同
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {

        if (!(obj instanceof FileNodeFILE)) {
            return false;
        }

        return ComparatorUtils.equals(this.envPath, ((FileNodeFILE) obj).envPath) && ComparatorUtils.equals(this.node, ((FileNodeFILE) obj).node);
    }

    /**
     * 返回hash码
     *
     * @return 返回hash码
     */
    public int hashCode() {

        int hash = 5;
        hash = 61 * hash + (this.node != null ? this.node.hashCode() : 0);
        hash = 61 * hash + (this.envPath != null ? this.envPath.hashCode() : 0);
        return hash;
    }

    /**
     * 作为字符串返回
     *
     * @return String  字符串
     */
    public String toString() {

        return prefix() + (this.node != null ? this.node.getEnvPath() : "");
    }
}