package com.fr.design.gui.ifilechooser;

import com.fr.design.gui.ifilechooser.AbstractFileChooser;
import com.fr.design.mainframe.DesignerContext;
import com.fr.stable.os.OperatingSystem;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;


/**
 * 系统原生风格的文件选择器
 *
 *  jdk问题：
 *  https://bugs.openjdk.java.net/browse/JDK-4811090 不支持文件扩展选择
 *  https://stackoverflow.com/questions/40713398/filter-not-working-in-filedialog windows下 setFilenameFilter不work
 *
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/31
 */
public class UINativeFileChooser extends AbstractFileChooser {

    private final FileDialog fileDialog;
    private FileFilter fileFilter;
    private int selectionMode;

    public UINativeFileChooser(File file) {
        fileDialog = new FileDialog(DesignerContext.getDesignerFrame());
        if (file != null) {
            fileDialog.setDirectory(file.getAbsolutePath());
            fileDialog.setFile(file.toString());
        }
    }

    public UINativeFileChooser() {
        this(null);
    }

    @Override
    public File getCurrentDirectory() {
        return new File(fileDialog.getDirectory());
    }

    @Override
    public FileFilter getFileFilter() {
        return fileFilter;
    }

    @Override
    public File getSelectedFile() {
        return new File(fileDialog.getDirectory() + fileDialog.getFile());
    }

    @Override
    public File[] getSelectedFiles() {
        return fileDialog.getFiles();
    }

    @Override
    public boolean isMultiSelectionEnabled() {
        return fileDialog.isMultipleMode();
    }

    @Override
    public void setCurrentDirectory(File f) {
        fileDialog.setDirectory(f.toString());
    }

    @Override
    public void setDialogTitle(String title) {
        fileDialog.setTitle(title);
    }

    @Override
    public void setFileFilter(final FileFilter cff) {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File Directory, String fileName) {
                return cff.accept(new File(Directory.getAbsolutePath() + fileName));
            }
        };
        fileDialog.setFilenameFilter(filter);
        fileFilter = cff;
    }

    @Override
    public void setFileSelectionMode(int selectionMode) {
        this.selectionMode = selectionMode;
    }

    @Override
    public void setMultiSelectionEnabled(boolean multiple) {
        fileDialog.setMultipleMode(multiple);
    }

    @Override
    public void setSelectedFile(File file) {
        fileDialog.setDirectory(file.getAbsolutePath());
        fileDialog.setFile(file.getName());
    }

    @Override
    public int showOpenDialog(Component parent) {
        boolean appleProperty = OperatingSystem.isMacos() && selectionMode == JFileChooser.DIRECTORIES_ONLY;
        if (appleProperty) {
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
        }
        try {
            fileDialog.setLocale(JComponent.getDefaultLocale());
            fileDialog.setMode(FileDialog.LOAD);
            fileDialog.setVisible(true);
            return fileDialog.getFile() == null ? JFileChooser.CANCEL_OPTION : JFileChooser.APPROVE_OPTION;
        } finally {
            if (appleProperty) {
                System.setProperty("apple.awt.fileDialogForDirectories", "false");
            }
        }
    }

    @Override
    public int showSaveDialog(Component parent) {
        fileDialog.setLocale(JComponent.getDefaultLocale());
        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setVisible(true);
        return fileDialog.getFile() == null ? JFileChooser.CANCEL_OPTION : JFileChooser.APPROVE_OPTION;
    }

    @Override
    public void setExtensionFilter(String file) {
        fileDialog.setFile(file);
    }

    /**
     * 确认本地文件选择器是否支持选择模式
     * @param selectionMode 选择模式
     * @return 是否支持选择模式
     */
    public static boolean supportsSelectionMode(int selectionMode) {
        switch (selectionMode) {
            case JFileChooser.FILES_AND_DIRECTORIES:
                return false;
            case JFileChooser.DIRECTORIES_ONLY:
                return OperatingSystem.isMacos();
            case JFileChooser.FILES_ONLY:
            default:
                return true;
        }
    }

}
