package com.fr.design.data.tabledata.tabledatapane;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.filetree.ClassFileTree;
import com.fr.design.layout.FRGUIPaneFactory;


import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class ClassNameSelectPane extends BasicPane {
    private ClassFileTree classFileTree;

    public ClassNameSelectPane() {
        this.setLayout(new BorderLayout(0, 4));
        
        JPanel webDirectoryPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(webDirectoryPane, BorderLayout.NORTH);
        webDirectoryPane.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        webDirectoryPane.add(new UILabel(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Function_Choose_Function_Class") + ":"));

        classFileTree = new ClassFileTree();
        classFileTree.refreshEnv();
        classFileTree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.add(new JScrollPane(classFileTree), BorderLayout.CENTER);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Function_Function_Class_Name");
    }

    /*
     * 选中某ClassPath
     */
    public void setClassPath(String classPath) {

        classFileTree.setSelectedClassPath(classPath + ".class");
    }

    /*
     * 返回选中的ClassPath
     */
    public String getClassPath() {
        return classFileTree.getSelectedClassPath();
    }

    @Override
    public void checkValid() throws Exception {
        String classPath = classFileTree.getSelectedClassPath();
        if (classPath == null) {
            throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Function_The_Selected_File_Cannot_Be_Null"));
        }
    }
}