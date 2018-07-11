package com.fr.file;

import javax.swing.Icon;

public interface FILE {

    /**
     * 后缀
     * @return 后缀
     */
	public String prefix();

    /**
     * 是否是目录
     * @return 是则返回true
     */
	public boolean isDirectory();
	
	// Name
	public String getName();
	
	// Icon
	public Icon getIcon();
	
	// 当前目录的Path
	public String getPath();
	
	public void setPath(String path);
	
	// 取当前目录的上级目录
	public FILE getParent();

    /**
     * 列出当前目录下所有的文件及文件夹
     * @return 文件
     */
	public FILE[] listFiles();

    /**
     * 新建一个目录
     * @param name 名字
     * @return 新建目录
     */
	public boolean createFolder(String name);

    /**
     * 新建文件
     * @return 是否新建成功
     * @throws Exception 异常
     */
	public boolean mkfile() throws Exception;

    /**
     * 是否存在
     * @return 是否存在
     */
	public boolean exists();

    /**
     * 关闭文件
     * @throws Exception 异常
     */
	public void closeTemplate() throws Exception;

    /**
     * 作为输入流
     * @return 输入流
     * @throws Exception 异常
     */
	public java.io.InputStream asInputStream() throws Exception;

    /**
     * 作为输出流
     * @return 输出流
     * @throws Exception 异常
     */
	public java.io.OutputStream asOutputStream() throws Exception;
	
	public String getEnvFullName();


    /**
     * 是否是内存文件
     * @return 是则返回true
     */
	public boolean isMemFile();

    /**
     * 是否是环境文件
     * @return 是则返回true
     */
    public boolean isEnvFile();
}