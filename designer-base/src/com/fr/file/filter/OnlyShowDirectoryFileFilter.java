package com.fr.file.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * 隐藏所有的文件的FileFilter.()
 */
public class OnlyShowDirectoryFileFilter implements FileFilter {
    public boolean accept(File file) {
        return file.isDirectory();
    }
}