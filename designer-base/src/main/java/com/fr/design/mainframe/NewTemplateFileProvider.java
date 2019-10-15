package com.fr.design.mainframe;

import com.fr.file.FILEChooserPane;
import com.fr.file.filetree.FileNode;
import com.fr.stable.fun.mark.Mutable;

import javax.swing.Icon;

/**
 * Created by kerry on 2019-10-11
 */
public interface NewTemplateFileProvider extends Mutable {

    int CURRENT_LEVEL = 1;

    String XML_TAG = "NewFileTypeProvider";

    /**
     * 向文件选择器中添加指定文件类型过滤器
     * @param fileChooser 文件选择器
     * @param suffix 文件后缀
     */
    void addChooseFileFilter(FILEChooserPane fileChooser, String suffix);

    /**
     * 获取本地目录文件关联的icon
     * @param path 文件路径
     * @return 对应的图标
     */
    Icon getLocalFileIcon(String path);

    /**
     * 获取远程目录文件关联的icon
     * @param node 文件节点
     * @param isShowLock 是否显示被锁住
     * @return 对应的图标
     */
    Icon getRemoteFileIcon(FileNode node, boolean isShowLock);

    /**
     * 保存为新类型文件
     * @param targetPath 目标路径
     * @param jTemplate 模板对象
     */
    boolean saveToNewFile(String targetPath, JTemplate jTemplate);
}
