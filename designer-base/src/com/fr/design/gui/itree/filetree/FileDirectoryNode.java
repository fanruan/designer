package com.fr.design.gui.itree.filetree;

import java.io.File;

/**
 * 文件节点,用于存放文件的 name 和 isDirectory属性
 */
public class FileDirectoryNode implements java.io.Serializable, Cloneable {
    private String name;
    private boolean isDirectory;
    private boolean canRead;

    public FileDirectoryNode() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}

	public boolean isCanRead() {
		return canRead;
	}

	public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String toString() {
        return name;
    }

    /**
     * 从File产生 FileDirectoryNode.
     */
    public static FileDirectoryNode createFileDirectoryNode(File file) {
    	FileDirectoryNode fileDirectoryNode = new FileDirectoryNode();

    	fileDirectoryNode.setName(file.getName());
    	fileDirectoryNode.setDirectory(file.isDirectory());
    	fileDirectoryNode.setCanRead(file.canRead());

        return fileDirectoryNode;
    }
}