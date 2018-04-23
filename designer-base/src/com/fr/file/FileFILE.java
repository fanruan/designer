package com.fr.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import com.fr.base.io.XMLEncryptUtils;
import com.fr.design.gui.itree.filetree.FileComparator;
import com.fr.design.gui.itree.filetree.FileTreeIcon;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogManager;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;

public class FileFILE implements FILE {

    private File file;

    public FileFILE(FileFILE parent, String name) {
        this(new File(parent.file, name));
    }

    public FileFILE(File file) {
        this.file = file;
    }

    /**
     * 后缀
     *
     * @return 后缀
     */
    public String prefix() {
        return FILEFactory.FILE_PREFIX;
    }

    /**
     * 是否是目录
     *
     * @returns 是则返回true
     */
    public boolean isDirectory() {
        return file == null ? false : file.isDirectory();
    }

    @Override
    public String getName() {
        if (file == null) {
            return "";
        }
        FileSystemView view = FileSystemView.getFileSystemView();
        return view.getSystemDisplayName(file);
    }

    public String getTotalName() {
        if (file == null) {
            return "";
        }

        return file.getName();
    }

    @Override
    public Icon getIcon() {
        if (file == null) {
            return FileTreeIcon.BLANK_IMAGE_ICON;
        }
        FileSystemView view = FileSystemView.getFileSystemView();
        try {
            return view.getSystemIcon(file);
        } catch (Exception e) {
            /*
             * alex:在显示Mac里面某个目录的时候,居然会抛
             * java.io.FileNotFoundException: File F:\.HFS+ Private Directory Data
             */
            return FileTreeIcon.BLANK_IMAGE_ICON;
        }
    }

    @Override
    public String getPath() {
        if (file == null) {
            return "";
        }

        return file.getAbsolutePath();
    }

    @Override
    public void setPath(String path) {
        file = new File(path);
    }

    @Override
    public FILE getParent() {
        if (file == null) {
            return null;
        }

        return new FileFILE(file.getParentFile());
    }

    /**
     * 列出当前目录下所有的文件及文件夹
     *
     * @return 文件
     */
    public FILE[] listFiles() {
        if (file == null) {
            return new FILE[0];
        }

        if (!file.isDirectory()) {
            return new FILE[]{this};
        }

        File[] file_array = file.listFiles();
        if (file_array == null) {
            return new FILE[0];
        }
        java.util.Arrays.sort(file_array, new FileComparator());

        java.util.List<FILE> res_list = new ArrayList<FILE>(file_array.length);

        for (int i = 0; i < file_array.length; i++) {
            // 因为有一些系统文件,比如虚拟内存等,会在listFiles的时候出现,但却not exists
            if (file_array[i].exists()) {
                res_list.add(new FileFILE(file_array[i]));
            }
        }

        return res_list.toArray(new FILE[res_list.size()]);
    }

    /**
     * 新建一个目录
     *
     * @param name 名字
     * @return 新建目录
     */
    public boolean createFolder(String name) {
        if (file == null || !file.isDirectory()) {
            return false;
        }

        File new_file = new File(StableUtils.pathJoin(new String[]{
                file.getAbsolutePath(), name
        }));

        if (new_file.exists()) {
            return false;
        }

        return new_file.mkdir();
    }

    /**
     * 是否存在
     *
     * @return 是否存在
     */
    public boolean exists() {
        return file == null ? false : file.exists();
    }

    /**
     * 是否存在
     *
     * @return 是否存在
     */
    public boolean mkfile() throws IOException {
        return StableUtils.makesureFileExist(file);
    }

    /**
     * 作为输入流
     *
     * @return 输入流
     * @throws Exception 异常
     */
    public InputStream asInputStream() throws Exception {
        InputStream in = new java.io.FileInputStream(file);
        return file.getName().endsWith(".cpt") || file.getName().endsWith(".frm")
                ? XMLEncryptUtils.decodeInputStream(in) : in;
    }

    /**
     * 作为输出流
     *
     * @return 输出流
     * @throws Exception 异常
     */
    public OutputStream asOutputStream() throws Exception {
        if (file == null || !file.exists()) {
            return null;
        }
        FRLogManager.declareResourceWriteStart(file.getAbsolutePath());
        java.io.OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (Exception e) {
            throw FRLogManager.createLogPackedException(e);
        }
        return out;
    }

    /**
     * 关闭文件
     *
     * @throws Exception 异常
     */
    public void closeTemplate() throws Exception {
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FileFILE)) {
            return false;
        }

        return ComparatorUtils.equals(this.file, ((FileFILE) obj).file);
    }

    /**
         * 返回hash码
         *
         * @return 返回hash码
         */
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.file != null ? this.file.hashCode() : 0);
        return hash;
    }

    /**
     * string方法
     *
     * @return 字符串
     */
    public String toString() {
        return this.prefix() + (this.file == null ? "" : this.file.getAbsolutePath());
    }

    @Override
    public String getEnvFullName() {
        String[] nodes = file.getAbsolutePath().split(ProjectConstants.REPORTLETS_NAME);
        return nodes[nodes.length - 1].substring(1);
    }

    /**
         * 是否是内存文件
         * @return 是则返回true
         */
    public boolean isMemFile() {
        return false;
    }

    /**
         * 是否是环境文件
         * @return 是则返回true
         */
    public boolean isEnvFile() {
        return false;
    }
}