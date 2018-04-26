package com.fr.file.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import com.fr.file.FILE;
import com.fr.file.FileFILE;
import com.fr.general.ComparatorUtils;

public class ChooseFileFilter extends FileFilter implements FILEFilter , java.io.FileFilter {
    private List<String> filters = null;
    private String description = null;
    private String fullDescription = null;
    private boolean useExtensionsInDescription = true;

    private boolean isExtend = false;
    
    public ChooseFileFilter() {
    	if (filters==null) {
            this.filters = new ArrayList<String>();
        }
    }

    public ChooseFileFilter(String extension) {
        this(extension, null);
    }

    public ChooseFileFilter(String extension, String description) {
        this();
        if (extension != null) {
            addExtension(extension);
        }

        if (description != null) {
            setDescription(description);
        }
    }

    public ChooseFileFilter(String[] filters) {
        this(filters, null);
    }

    public ChooseFileFilter(String[] filters, boolean isExtend) {
        this(filters, null);

        this.isExtend = isExtend;
    }

    public ChooseFileFilter(String[] filters, String description) {
        this();
        for (int i = 0; i < filters.length; i++) {
            // add filters one by one
            addExtension(filters[i]);
        }

        if (description != null) {
            setDescription(description);
        }

    }

    /**
     * 是否支持改文档
     * @param f 文件
     * @return 支持返回true
     */
    public boolean accept(File f) {
        if (f != null) {
            if (this.filters == null) { //all file types.
                return true;
            }

            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);

            if (extension != null && filters.contains(extension)) {
                return !isExtend;
            }
        }

        return isExtend;
    }

    /**
     * 是否支持
     * @param f 文件
     * @return 支持返回 true
     */
    public boolean accept(FILE f) {
        if (f != null) {
            if (this.filters == null) { //all file types.
                return true;
            }

            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            

            if (extension != null && filters.contains(extension)) {
                return !isExtend;
            }
        }

        return isExtend;
    }
    
    public String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');

            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }

        return null;
    }
    
    public String getExtension(FILE f) {
        if (f != null) {
            String filename = f.getName();
            if (f instanceof FileFILE) {
            	filename = ((FileFILE)f).getTotalName();
            }
            int i = filename.lastIndexOf('.');

            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }

        return null;
    }

    /**
     * 加扩展
     * @param extension 扩展
     */
    public void addExtension(String extension) {
        if (filters == null) {
            filters = new ArrayList<String>();
        }
        
        if (!filters.contains(extension.toLowerCase())) {
            filters.add(extension.toLowerCase());
        }
        fullDescription = null;
    }


    /**
     * 是否包含该扩展
     * @param extension  扩展
     * @return 是则返回true
     */
    public boolean containsExtension(String extension){
         return filters.contains(extension.toLowerCase());
    }

    public String getDescription() {
        if (this.filters == null) { //all file types.
            return fullDescription;
        }

        if (fullDescription == null) {

            if (description == null || isExtensionListInDescription()) {

                fullDescription = description == null ? "(" : description + " (";
                // build the description from the extension list
//                Enumeration<String> extensions = filters.keys();
//
//                if (extensions != null) {
//                    fullDescription += "." + extensions.nextElement();
//                    while (extensions.hasMoreElements()) {
//                        fullDescription += ", " + extensions.nextElement();
//                    }
//                }

                if (!filters.isEmpty()){
                	fullDescription += "." + filters.get(0);
                	for (int i=1;i<filters.size();i++){
                		fullDescription += ", ." + filters.get(i);
                	}
                }
                
                fullDescription += ")";

            } else {
                fullDescription = description;
            }
        }

        return fullDescription;
    }

    public void setDescription(String description) {
        this.description = description;
        fullDescription = null;
    }

    public void setExtensionListInDescription(boolean b) {
        useExtensionsInDescription = b;
        fullDescription = null;
    }

    /**
     * 扩展是否列在描述之内了
     * @return 是则返回true
     */
    public boolean isExtensionListInDescription() {
        return useExtensionsInDescription;
    }

    /**
     * get extention.
     */
    public String getExtensionString() {
        if (filters == null) {
            return "";
        }

        StringBuffer extsb = new StringBuffer(".");

//        Enumeration keys = filters.keys();
//        while (keys.hasMoreElements()) {
//            extsb.append((String) keys.nextElement());
//        }

        for(int i=0;i<filters.size();i++){
        	extsb.append(filters.get(i));
        }
        
        return extsb.toString();
    }

    /**
     * toString方便重写
     * @return 重写
     */
    public String toString() {
    	return getDescription();
    }
    
    
    public boolean equals(Object o) {
    	return (o instanceof ChooseFileFilter)
    		&& ComparatorUtils.equals(((ChooseFileFilter)o).getDescription(),getDescription());
    }
}