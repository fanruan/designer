package com.fr.design.gui.itree.filetree;

import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.file.filetree.FileNode;
import com.fr.stable.ArrayUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.text.Position;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

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

    public TreePath getNextMatch(String prefix, int startingRow,
                                 Position.Bias bias) {

        int max = getRowCount();
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        if (startingRow < 0 || startingRow >= max) {
            throw new IllegalArgumentException();
        }
        prefix = prefix.toUpperCase();

        // start search from the next/previous element froom the
        // selected element
        int increment = (bias == Position.Bias.Forward) ? 1 : -1;
        int row = startingRow;
        do {
            TreePath path = getPathForRow(row);
            String text = convertValueToText(
                    path.getLastPathComponent(), isRowSelected(row),
                    isExpanded(row), true, row, false);

            if (text.toUpperCase().startsWith(prefix)) {
                return path;
            }
            row = (row + increment + max) % max;
        } while (row != startingRow);
        return null;
    }
}