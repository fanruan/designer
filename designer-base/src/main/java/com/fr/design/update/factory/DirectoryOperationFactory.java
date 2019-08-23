package com.fr.design.update.factory;

import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StableUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class DirectoryOperationFactory {
    /**
     * 新建一个目录
     *
     * @param dirPath 目录路径
     */
    public static void createNewDirectory(String dirPath) {
        try {
            File newDirPath = new File(dirPath);
            if (!newDirPath.exists()) {
                StableUtils.mkdirs(newDirPath);
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }

    /**
     * 删除目录
     *
     * @param dirPath 目录路径
     */
    public static void deleteDirectory(String dirPath) {
        try {
            File dir = new File(dirPath);
            if (dir.isDirectory()) {
                File[] file = dir.listFiles();
                for (File fileTemp : file) {
                    deleteDirectory(fileTemp.toString());
                    fileTemp.delete();
                }
            } else {
                dir.delete();
            }
            dir.delete();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }

    /**
     * 复制目录
     *
     * @param oldDirPath 被复制目录
     * @param newDirPath 新目录
     */
    public static void copyDirectory(String oldDirPath, String newDirPath) {
        File oldDir = new File(oldDirPath);
        if (oldDir.isDirectory()) {
            StableUtils.mkdirs(new File(newDirPath));
            File[] files = oldDir.listFiles();
            for (File fileTemp : files) {
                copyDirectory(fileTemp.toString(), newDirPath + "/" + fileTemp.getName());
            }
        } else {
            try {
                copy(oldDirPath, newDirPath);
            } catch (IOException e) {
                FineLoggerFactory.getLogger().error(e.getMessage());
            }
        }
    }

    private static void copy(String path1, String path2) throws IOException {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(path1)));
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path2)))) {
            byte[] date = new byte[in.available()];
            in.read(date);
            out.write(date);
        }
    }

    /**
     * 移动目录
     *
     * @param oldDirPath 被移动目录
     * @param newDirPath 新目录
     */
    public static void moveDirectory(String oldDirPath, String newDirPath) {
        copyDirectory(oldDirPath, newDirPath);
        deleteDirectory(oldDirPath);
    }

    /**
     * 列出过滤后的文件
     *
     * @param installHome 安装目录
     * @param backupdir   备份目录
     * @return String数组
     */
    public static String[] listFilteredFiles(String installHome, String backupdir) {
        File backupDir = new File(StableUtils.pathJoin(installHome, backupdir));
        StableUtils.mkdirs(backupDir);
        File[] fileNames = backupDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        String[] jarFileName = new String[fileNames.length];
        int j = 0;
        for (File fileName : fileNames) {
            if ((fileName.isDirectory()) && (ArrayUtils.getLength(fileName.listFiles()) > 0)) {//判断备份文件夹中是否为空，为空不显示
                jarFileName[j++] = fileName.getName();
            }
        }
        return Arrays.copyOf(jarFileName, j);
    }
}
