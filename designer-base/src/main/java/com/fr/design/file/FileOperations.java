package com.fr.design.file;


import com.fr.file.FILE;
import com.fr.file.filetree.FileNode;

public interface FileOperations {

    /**
     * 新建文件夹
     *
     * @param name 文件夹名称
     * @return 是否成功
     */
    boolean mkdir(String name);

    /**
     * 打开报表文件
     */
    void openFile();

    /**
     * 打开文件夹
     */
    void showInExplorer();

    /**
     * 刷新
     */
    void refresh();

    /**
     * 删除文件
     */
    void deleteFile();

    /**
     * 加上文件锁
     */
    void lockFile();

    /**
     * 文件解锁
     */
    void unlockFile();

    /**
     * 选中的模板路径
     *
     * @return 选中的模板路径
     */
    String getFilePath();


    /**
     * 选中的文件节点
     *
     * @return 文件节点
     */
    FileNode getFileNode();

    /**
     * 是否有完整权限
     *
     * @return 是否有完整权限
     */
    boolean access();

    /**
     * 重命名
     *
     * @param tplFile 旧文件
     * @param to      新文件名
     * @param from    旧文件名
     * @return 是否成功
     */
    boolean rename(FILE tplFile, String from, String to);


    /**
     * 文件名是否存在
     *
     * @param newName 原名
     * @param suffix  后缀名
     * @return 是否存在
     */
    boolean duplicated(String newName, String suffix);
}