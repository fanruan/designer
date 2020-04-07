package com.fr.design.gui.ifilechooser.swing;

import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/31
 */
public abstract class AbstractFileChooser {

    /**
     * 返回当前目录
     *
     */
    public abstract File getCurrentDirectory();

    /**
     * 返回当前的文件选择过滤器
     *
     */
    public abstract FileFilter getFileFilter();

    /**
     * 返回选择的文件
     *
     */
    public abstract File getSelectedFile();

    /**
     * 多文件选择模式下 返回选择的多个文件
     *
     */
    public abstract File[] getSelectedFiles();

    /**
     * 是否可以选择多个文件
     *
     */
    public abstract boolean isMultiSelectionEnabled();

    /**
     * 设置当前选择的目录
     *
     */
    public abstract void setCurrentDirectory(File dir);

    /**
     * 设置左上角标题
     *
     */
    public abstract void setDialogTitle(String title);

    /**
     * 设置当前的文件过滤器
     *
     */
    public abstract void setFileFilter(final FileFilter filter);

    /**
     * 设置文件选择器模式
     *
     * JFileChooser.FILES_ONLY
     * JFileChooser.DIRECTORIES_ONLY
     * JFileChooser.FILES_AND_DIRECTORIES
     */
    public abstract void setFileSelectionMode(int selectionMode);

    /**
     * 设置是否允许选择多个文件
     *
     */
    public abstract void setMultiSelectionEnabled(boolean multiple);

    /**
     * 设置选择的文件 用于showSaveDialog
     *
     */
    public abstract void setSelectedFile(File file);

    /**
     * 弹出文件选择器 打开文件
     *
     */
    public abstract int showOpenDialog(Component parent);

    /**
     * 弹出文件选择器 保存文件
     *
     */
    public abstract int showSaveDialog(Component parent);
}
