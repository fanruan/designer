package com.fr.design.gui.itree.filetree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreePath;

import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.file.filetree.FileNode;
import com.fr.stable.ArrayUtils;
import com.fr.stable.project.ProjectConstants;

/*
 * 显示Env下的reportlets目录下面的所有cpt文件
 */
public class TemplateFileTree extends EnvFileTree {
	

    public TemplateFileTree() {
        super(ProjectConstants.REPORTLETS_NAME, null, null);
    }

    /*
     * 选中reportPath
     */
    public void setSelectedTemplatePath(String templatePath) {
        this.selectPath(templatePath);
    }

    /**
     * 返回选中的Template的路径
     */
    public String getSelectedTemplatePath() {
        FileNode fn = this.getSelectedFileNode();
        if (fn != null && !fn.isDirectory()) {
            String envPath = fn.getEnvPath();

            if (envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
                return envPath.substring(ProjectConstants.REPORTLETS_NAME.length());
            }
        }

        return null;
    }
    
    public String[] getSelectedTemplatePaths(){
    	TreePath[] selectedTreePaths = this.getSelectionPaths();
		if (ArrayUtils.isEmpty(selectedTreePaths)) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		List<String> selectedPathList = new ArrayList<String>();
		for(TreePath treepath : selectedTreePaths){
			ExpandMutableTreeNode currentTreeNode = (ExpandMutableTreeNode) treepath.getLastPathComponent();
			Object userObject = currentTreeNode.getUserObject();
			if (userObject instanceof FileNode) {
				 FileNode fn = (FileNode) userObject;
				 if (!fn.isDirectory()) {
			            String envPath = fn.getEnvPath();
			            if (envPath.startsWith(ProjectConstants.REPORTLETS_NAME)) {
			            	selectedPathList.add(envPath.substring(ProjectConstants.REPORTLETS_NAME.length()));
			            }
			        }
			}
		}


		return selectedPathList.toArray(new String[0]);
    }
}