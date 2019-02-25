package com.fr.file;

import javax.swing.Icon;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 切换环境用于暂存的文件类型
 */
public class StashedFILE extends AbstractFILE {

    private FILE file;
    private byte[] content;

    public StashedFILE(FILE file, byte[] content) {
        this.file = file;
        this.content = content;
    }

    @Override
    public String prefix() {
        return file.prefix();
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public Icon getIcon() {
        return file.getIcon();
    }

    @Override
    public String getPath() {
        return file.getPath();
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public void closeTemplate() throws Exception {
        // do nothing
    }

    @Override
    public InputStream asInputStream() throws Exception {
        return new ByteArrayInputStream(content);
    }

    @Override
    public String getEnvFullName() {
        return file.getEnvFullName();
    }

    @Override
    public boolean isMemFile() {
        return true;
    }

    @Override
    public boolean isEnvFile() {
        return false;
    }
}
