package com.fr.file;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.design.gui.itree.filetree.FileNodeComparator;
import com.fr.design.gui.itree.filetree.FileTreeIcon;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;

import java.io.InputStream;
import java.io.OutputStream;

public class FileNodeFILE implements FILE {

    private FileNode node;
    // carl：记录下FILE对应的运行环境,每次创建都设置下当前的运行环境
    private String envPath;

    public FileNodeFILE(FileNodeFILE parent, String name, boolean isDir) {
        FileNode fn = parent.node;
        String parentDir;
        if (fn.isDirectory()) {
            parentDir = fn.getEnvPath();
        } else {
            parentDir = fn.getParent();
        }

        this.node = new FileNode(StableUtils.pathJoin(new String[]{
                parentDir, name
        }), isDir);
        this.envPath = FRContext.getCurrentEnv().getPath();
    }

    public FileNodeFILE(FileNode node) {
        this.node = node;
        this.envPath = FRContext.getCurrentEnv().getPath();
    }

    public FileNodeFILE(String envPath) {
        this.node = null;
        this.envPath = envPath;
    }

    public FileNodeFILE(FileNode node, String envPath) {
        this.node = node;
        this.envPath = envPath;
    }

    /**
     * prefix 后缀
     *
     * @return 返回后缀
     */
    public String prefix() {
        if (ComparatorUtils.equals(getEnvPath(), FRContext.getCurrentEnv().getWebReportPath())) {
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
    public boolean isDirectory() {
        return ComparatorUtils.equals(node, null) ? true : node.isDirectory();
    }

    /**
     * @return
     */
    public String getName() {
        if (node == null) {
            return null;
        }

        if (ComparatorUtils.equals(node.getEnvPath(), ProjectConstants.REPORTLETS_NAME)) {
            return Inter.getLocText("Utils-Report_Runtime_Env");
        } else {
            return node.getName();
        }
    }

    /**
     * @return
     */
    public Icon getIcon() {
        if (node == null) {
            return null;
        }

        if (ComparatorUtils.equals(node.getEnvPath(), ProjectConstants.REPORTLETS_NAME)) {
            return BaseUtils.readIcon("/com/fr/base/images/oem/logo.png");
        } else {
            return FileTreeIcon.getIcon(node);
        }
    }

    /**
     * @return
     */
    public String getPath() {
        if (node == null) {
            return "";
        }

        return node.getEnvPath();
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        node.setEnvPath(path);
    }

    /**
     * @return
     */
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
    public FILE[] listFiles() {
        if (ComparatorUtils.equals(node, null)) {
            node = new FileNode(CoreConstants.SEPARATOR, true);
            //return new FILE[0];
        }
        if (!node.isDirectory()) {
            return new FILE[]{this};
        }

        try {
            FileNode[] node_array;
            node_array = listFile(node.getEnvPath());
            java.util.Arrays.sort(node_array, new FileNodeComparator());

            FILE[] res_array = new FILE[node_array.length];
            for (int i = 0; i < node_array.length; i++) {
                res_array[i] = new FileNodeFILE(node_array[i], envPath);
            }

            return res_array;
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return new FILE[0];
        }
    }

    /**
     * 根目录文件地址
     *
     * @param rootFilePath 根文件路径
     * @return 返回文件节点
     */
    private FileNode[] listFile(String rootFilePath) {
        if (ComparatorUtils.equals(envPath, FRContext.getCurrentEnv().getWebReportPath())) {
            try {
                return FRContext.getCurrentEnv().listReportPathFile(rootFilePath);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        } else {
            try {
                return FRContext.getCurrentEnv().listFile(rootFilePath);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return new FileNode[0];
    }

    /**
     * 创建文件夹
     *
     * @param name 文件夹名字
     * @return 创建成功返回true
     */
    public boolean createFolder(String name) {
        if (ComparatorUtils.equals(node, null) || !node.isDirectory()) {
            return false;
        }

        try {
            return FRContext.getCurrentEnv().createFolder(StableUtils.pathJoin(new String[]{
                    node.getEnvPath(), name
            }));
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
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
            return FRContext.getCurrentEnv().fileLocked(node.getEnvPath());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 是否存在
     *
     * @return 文件存在返回 true
     */
    public boolean exists() {
        if (node == null) {
            return false;
        }
        // 当运行环境不一致，返回false
        if (!isCurrentEnv()) {
            return false;
        }

        try {
            return FRContext.getCurrentEnv().fileExists(node.getEnvPath());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 是否是当前环境
     *
     * @return 是报表当前环境返回true
     */
    public boolean isCurrentEnv() {
        return ComparatorUtils.equals(FRContext.getCurrentEnv().getPath(), envPath);
    }

    /**
     * 创建文件
     *
     * @return 成功返回true
     */
    public boolean mkfile() {
        if (node == null) {
            return false;
        }

        try {
            return FRContext.getCurrentEnv().createFile(node.getEnvPath());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 作为输入流
     *
     * @return 输入流
     * @throws Exception
     */
    public InputStream asInputStream() throws Exception {
        if (node == null) {
            return null;
        }

        String envPath = node.getEnvPath();
        // envPath必须以reportlets开头
        if (!envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            return null;
        }

        InputStream in = FRContext.getCurrentEnv().readBean(
                envPath.substring(ProjectConstants.REPORTLETS_NAME.length() + 1),
                ProjectConstants.REPORTLETS_NAME
        );
        
        return envPath.endsWith(".cpt") || envPath.endsWith(".frm")
                ? XMLEncryptUtils.decodeInputStream(in) : in;
    }
    
    /**
     * 作为输出流
     *
     * @return 返回输出流
     * @throws Exception
     */
    public OutputStream asOutputStream() throws Exception {
        if (ComparatorUtils.equals(node, null)) {
            return null;
        }

        String envPath = node.getEnvPath();
        // envPath必须以reportlets开头
        if (!envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            return null;
        }
        return FRContext.getCurrentEnv().writeBean(
                envPath.substring(ProjectConstants.REPORTLETS_NAME.length() + 1),
                ProjectConstants.REPORTLETS_NAME
        );
    }

    /**
     * 关闭模板
     *
     * @throws Exception
     */
    public void closeTemplate() throws Exception {
        if (node == null) {
            return;
        }

        String envPath = node.getEnvPath();
        // envPath必须以reportlets开头
        if (!envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            return;
        }

        FRContext.getCurrentEnv().unlockTemplate(
                envPath.substring(ProjectConstants.REPORTLETS_NAME.length() + 1));
    }

    /**
     * 得到环境的全名
     *
     * @return
     */
    public String getEnvFullName() {
        return this.node.getEnvPath().substring(ProjectConstants.REPORTLETS_NAME.length() + 1);
    }

    /**
     * 是否是内存文件
     *
     * @return 是则返回true
     */
    public boolean isMemFile() {
        return false;
    }

    /**
     * 是否是环境文件
     *
     * @return 是则返回true
     */
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