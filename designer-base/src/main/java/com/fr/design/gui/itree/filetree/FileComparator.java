package com.fr.design.gui.itree.filetree;

import java.util.Comparator;

public class FileComparator implements Comparator<java.io.File> {

	public int compare(java.io.File file1, java.io.File file2) {
		if (file1.isDirectory()) {
            if (file2.isDirectory()) {
                return file1.getName().toLowerCase().compareTo(file2.getName().toLowerCase());
            } else {
                return -1;
            }
        } else {
            if (file2.isDirectory()) {
                return 1;
            } else {
                return file1.getName().toLowerCase().compareTo(file2.getName().toLowerCase());
            }
        }
	}

}