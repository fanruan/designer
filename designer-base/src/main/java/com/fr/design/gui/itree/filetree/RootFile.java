package com.fr.design.gui.itree.filetree;

import java.io.File;
/**
 * 用于判断是否为根目录
 * 例:  C:\
 * @author kunsnat
 *
 */
public class RootFile {
    private File file;

    public RootFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String toString() {
        return file.getAbsolutePath();
    }
}