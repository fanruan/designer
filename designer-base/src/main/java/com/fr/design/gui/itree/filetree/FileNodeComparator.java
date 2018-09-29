package com.fr.design.gui.itree.filetree;

import com.fr.base.FRContext;
import com.fr.file.filetree.FileNode;

import java.util.Comparator;

/**
 * FileTreeNode compare...
 * Directory is in the first. and  normal file  the  in the  last.
 */
public class FileNodeComparator implements Comparator<FileNode> {
	// 正序还是倒序
	private boolean isReverse = false;
	
	public FileNodeComparator() {
		this(false);
	}
	
	public FileNodeComparator(boolean reverse) {
		this.isReverse = reverse;
	}
	
    /**
     * This method should return > 0 if v1 is greater than v2, 0 if
     * v1 is equal to v2, or < 0 if v1 is less than v2.
     * It must handle null values for the comparison values.
     *
     * @return < 0, 0, or > 0 for v1<v2, v1==v2, or v1>v2.
     */
    public int compare(FileNode nameNode1, FileNode nameNode2) {
    	int returnVal;
        if (nameNode1.isDirectory()) {
            if (nameNode2.isDirectory()) {
            	returnVal = nameNode1.getName().toLowerCase().compareTo(nameNode2.getName().toLowerCase());
            } else {
            	returnVal = -1;
            }
        } else {
            if (nameNode2.isDirectory()) {
            	returnVal = 1;
            } else {
            	returnVal=groupByFileType(nameNode1, nameNode2, 0);
            }
        }
        if (isReverse) {
        	returnVal = 0 - returnVal;
        }
        return returnVal;
    }

    /**
     * 一个简单的递归判断算法
     * @param nameNode1
     * @param nameNode2
     * @param i
     * @return
     */
	private int groupByFileType(FileNode nameNode1, FileNode nameNode2,
			int i) {
		
		String[] supportTypes = FRContext.getFileNodes().getSupportedTypes();
		if (i < supportTypes.length) {
			if (nameNode1.isFileType(supportTypes[i]))
				if (nameNode2.isFileType(supportTypes[i]))
					return nameNode1.getName().toLowerCase().compareTo(nameNode2.getName().toLowerCase());
				else
					return-1;
			else if (nameNode2.isFileType(supportTypes[i]))
					return 1;
				else{
					return groupByFileType(nameNode1, nameNode2, i+1);
					}
			}else
				return -1;
		}
	}