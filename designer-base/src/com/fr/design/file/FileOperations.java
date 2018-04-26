package com.fr.design.file;


public interface FileOperations {
    /**
     *打开选中的报表文件
     */
	public void openSelectedReport();

    /**
     *打开文件夹
     */
	public void openContainerFolder();

    /**
     *刷新
     */
	public void refresh();

    /**
     *删除文件
     */
	public void deleteFile();

    /**
     *加上文件锁
     */
	public void lockFile();

    /**
     *文件解锁
     */
	public void unLockFile();

	public String getSelectedTemplatePath();

    /**
     *文件名是否存在
     * @param newName 原名
     * @param oldName 新的文件名
     * @param suffix 后缀名
     * @return 是否存在
     */
	public boolean isNameAlreadyExist(String newName, String oldName, String suffix);
}