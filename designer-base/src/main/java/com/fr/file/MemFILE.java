package com.fr.file;

import javax.swing.Icon;
import java.io.InputStream;
import java.io.OutputStream;

public class MemFILE implements FILE {

    private String name;

    public MemFILE(String name) {
        this.name = name;
    }

    /**
     * 新建一个目录
     *
     * @param name 名字
     * @return 新建目录
     */
    @Override
    public boolean createFolder(String name) {
        return false;
    }

    /**
     * 是否存在
     *
     * @return 是否存在
     */
    @Override
    public boolean exists() {
        return false;
    }

    /**
     * 新建文件
     *
     * @return 是否新建成功
     * @throws Exception 异常
     */
    @Override
    public boolean mkfile() throws Exception {
        return false;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return name;
    }

    @Override
    public String getEnvFullName() {
        return name;
    }

    /**
     * 是否是内存文件
     *
     * @return 是则返回true
     */
    @Override
    public boolean isMemFile() {
        return true;
    }

    /**
     * 是否是环境文件
     *
     * @return 是则返回true
     */
    @Override
    public boolean isEnvFile() {
        return false;
    }

    @Override
    public FILE getParent() {
        return null;
    }

    /**
     * 是否是目录
     *
     * @return 是则返回true
     */
    @Override
    public boolean isDirectory() {
        return false;
    }

    /**
     * 列出当前目录下所有的文件及文件夹
     *
     * @return 文件
     */
    @Override
    public FILE[] listFiles() {
        return new FILE[0];
    }

    /**
     * 前缀
     *
     * @return 前缀
     */
    @Override
    public String prefix() {
        return FILEFactory.MEM_PREFIX;
    }

    /**
     * string方法
     *
     * @return 字符串
     */
    public String toString() {
        return prefix() + this.name;
    }

    @Override
    public void setPath(String path) {
        this.name = path;
    }

    /**
     * 关闭文件
     *
     * @throws Exception 异常
     */
    @Override
    public void closeTemplate() throws Exception {
    }

    /**
     * 作为输入流
     *
     * @return 输入流
     * @throws Exception 异常
     */
    @Override
    public InputStream asInputStream() throws Exception {
        return null;
    }

    /**
     * 作为输出流
     *
     * @return 输出流
     * @throws Exception 异常
     */
    @Override
    public OutputStream asOutputStream() throws Exception {
        return null;
    }
}