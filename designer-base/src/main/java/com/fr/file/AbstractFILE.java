package com.fr.file;

import javax.swing.Icon;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractFILE implements FILE {

    @Override
    public String prefix() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDirectory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Icon getIcon() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPath(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FILE getParent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FILE[] listFiles() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean createFolder(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean mkfile() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exists() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeTemplate() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream asInputStream() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public OutputStream asOutputStream() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getEnvFullName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMemFile() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnvFile() {
        throw new UnsupportedOperationException();
    }
}
