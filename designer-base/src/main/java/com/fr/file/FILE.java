package com.fr.file;

import javax.swing.Icon;
import java.io.InputStream;
import java.io.OutputStream;

public interface FILE {

    /**
     * 前缀
     *
     * @return 前缀
     */
    String prefix();

    /**
     * 是否是目录
     *
     * @return 是则返回true
     */
    boolean isDirectory();

    // Name
    String getName();

    // Icon
    Icon getIcon();

    // 当前目录的Path
    String getPath();

    void setPath(String path);

    // 取当前目录的上级目录
    FILE getParent();

    /**
     * 列出当前目录下所有的文件及文件夹
     *
     * @return 文件
     */
    FILE[] listFiles();

    /**
     * 新建一个目录
     *
     * @param name 名字
     * @return 新建目录
     */
    boolean createFolder(String name);

    /**
     * 新建文件
     *
     * @return 是否新建成功
     * @throws Exception 异常
     */
    @SuppressWarnings({"UnusedReturnValue"})
    boolean mkfile() throws Exception;

    /**
     * 是否存在
     *
     * @return 是否存在
     */
    boolean exists();

    /**
     * 关闭文件
     *
     * @throws Exception 异常
     */
    void closeTemplate() throws Exception;

    /**
     * 作为输入流
     *
     * @return 输入流
     * @throws Exception 异常
     */
    InputStream asInputStream() throws Exception;

    /**
     * 作为输出流
     *
     * @return 输出流
     * @throws Exception 异常
     */
    OutputStream asOutputStream() throws Exception;

    String getEnvFullName();


    /**
     * 是否是内存文件
     *
     * @return 是则返回true
     */
    boolean isMemFile();

    /**
     * 是否是环境文件
     *
     * @return 是则返回true
     */
    boolean isEnvFile();
}