package com.fr.file;

import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.third.org.apache.commons.io.FileUtils;
import com.fr.web.session.SessionLocalManager;

import javax.swing.Icon;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RenameExportFILE extends AbstractFILE {

    private static final String EXPORT_SUFFIX = ".FRExportTmp";

    private FILE file;

    private RenameExportFILE(FILE file) {
        this.file = new FileFILE(new File(file.getPath() + EXPORT_SUFFIX));
    }

    public static RenameExportFILE create(FILE file) {
        return new RenameExportFILE(file);
    }

    public static String recoverFileName(String fileName) {
        if (StringUtils.isEmpty(fileName) || !fileName.endsWith(EXPORT_SUFFIX)) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf(EXPORT_SUFFIX));
    }

    @Override
    public String prefix() {
        return file.prefix();
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getPath() {
        return file.getPath();
    }


    @Override
    public FILE getParent() {
        return file.getParent();
    }


    @Override
    public boolean mkfile() throws Exception {
        return file.mkfile();
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public OutputStream asOutputStream() throws Exception {

        final File localeFile = new File(file.getPath());
        OutputStream out;
        try {
            StableUtils.makesureFileExist(localeFile);
            out = new FileOutputStream(localeFile, false) {
                @Override
                public void close() throws IOException {
                    super.close();
                    String path = file.getPath();
                    if (localeFile.exists()) {
                        FileUtils.copyFile(localeFile, new File(recoverFileName(path)));
                        if (localeFile.getPath().endsWith(EXPORT_SUFFIX)) {
                            FileUtils.forceDelete(localeFile);
                        }
                    }
                }
            };
        } catch (Exception e) {
            throw SessionLocalManager.createLogPackedException(e);
        }
        return out;
    }

    @Override
    public void closeTemplate() throws Exception {
        //do nothing
    }
}