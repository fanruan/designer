package com.fr.design.mainframe.messagecollect.entity;

/**
 * @author alex sung
 * @date 2019/4/8
 */
public class FileEntity {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件的完整路径
     */
    private String pathName;
    /**
     * 文件夹路径
     */
    private String folderName;

    public FileEntity(String fileName, String pathName, String folderName) {
        this.fileName = fileName;
        this.pathName = pathName;
        this.folderName = folderName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
