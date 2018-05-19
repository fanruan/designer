package com.fr.design.remote.ui.tree;

import com.fr.design.gui.itree.filetree.TemplateFileTree;

import javax.swing.tree.TreePath;

public class FileAuthorityTree extends TemplateFileTree {

    @Override
    public boolean isCheckBoxVisible(TreePath path) {
//        return super.isCheckBoxVisible(path);
        return true;
    }


}
