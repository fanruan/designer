package com.fr.file;

import com.fr.base.FRContext;
import com.fr.file.filetree.FileNode;

public class FILEFactory {
    public static final String MEM_PREFIX = "mem://";
    public static final String FILE_PREFIX = "file://";
    public static final String ENV_PREFIX = "env://";
    public static final String WEBREPORT_PREFIX = "webreport://";

    private FILEFactory() {
    }

    /*
     * 根据path生成FILE
     * path might start with env://, file:// or mem://
     * 也有可能就是一个普通的路径d:/foo/bar
     */
    public static FILE createFILE(String path) {
        String envPath = FRContext.getCurrentEnv().getPath().replaceAll("/", "\\\\");
        if (path == null) {
            return null;
        } else if (path.startsWith(MEM_PREFIX)) {
            return new MemFILE(path.substring(MEM_PREFIX.length()));
        } else if (path.startsWith(ENV_PREFIX)) {
            return new FileNodeFILE(new FileNode(path.substring(ENV_PREFIX.length()), false));
        } else if (path.startsWith(envPath)) {
            return new FileNodeFILE(new FileNode(path.substring(envPath.length() + 1), false));
        } else if (path.startsWith(WEBREPORT_PREFIX)) {
            return new FileNodeFILE(new FileNode(path.substring(WEBREPORT_PREFIX.length()), false),
                    FRContext.getCurrentEnv().getWebReportPath());
        } else if (path.startsWith(FILE_PREFIX)) {
            return new FileFILE(new java.io.File(path.substring(FILE_PREFIX.length())));
        } else {
            return new FileFILE(new java.io.File(path));
        }
    }

    public static FILE createFolder(String path) {
        if (path == null) {
            return null;
        } else if (path.startsWith(MEM_PREFIX)) {
            return new MemFILE(path.substring(MEM_PREFIX.length()));
        } else if (path.startsWith(ENV_PREFIX)) {
            return new FileNodeFILE(new FileNode(path.substring(ENV_PREFIX.length()), true));
        } else if (path.startsWith(WEBREPORT_PREFIX)) {
            return new FileNodeFILE(new FileNode(path.substring(WEBREPORT_PREFIX.length()), true),
                    FRContext.getCurrentEnv().getWebReportPath());
        } else if (path.startsWith(FILE_PREFIX)) {
            return new FileFILE(new java.io.File(path.substring(FILE_PREFIX.length())));
        } else {
            return new FileFILE(new java.io.File(path));
        }
    }
}