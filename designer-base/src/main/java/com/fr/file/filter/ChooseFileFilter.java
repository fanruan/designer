package com.fr.file.filter;

import com.fr.base.extension.FileExtension;
import com.fr.file.FILE;
import com.fr.file.FileFILE;
import com.fr.general.ComparatorUtils;
import com.fr.stable.CoreConstants;
import com.fr.stable.StringUtils;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ChooseFileFilter extends FileFilter implements FILEFilter, java.io.FileFilter {
    private List<String> filters = null;
    private String description = null;
    private String fullDescription = null;
    private boolean useExtensionsInDescription = true;

    private boolean isExtend = false;

    public ChooseFileFilter() {
        if (filters == null) {
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
        for (String filter : filters) {
            // add filters one by one
            addExtension(filter);
        }

        if (description != null) {
            setDescription(description);
        }

    }

    public ChooseFileFilter(FileExtension extension) {
        this(extension, null);
    }

    public ChooseFileFilter(FileExtension extension, String description) {
        this();
        if (extension != null) {
            addExtension(extension.getExtension());
        }

        if (description != null) {
            setDescription(description);
        }
    }

    /**
     * 使用指定枚举类集合构建文件过滤器
     *
     * @param filters 文件扩展名枚举类集合
     */
    public ChooseFileFilter(EnumSet<FileExtension> filters) {
        this(filters, null);
    }

    /**
     * 使用指定枚举类集合构建文件过滤器
     *
     * @param filters     文件扩展名枚举类集合
     * @param description 描述
     */
    public ChooseFileFilter(EnumSet<FileExtension> filters, String description) {
        this();
        for (FileExtension filter : filters) {
            addExtension(filter.getExtension());
        }

        if (description != null) {
            setDescription(description);
        }

    }

    /**
     * 是否支持改文档
     *
     * @param f 文件
     * @return 支持返回true
     */
    @Override
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
     *
     * @param f 文件
     * @return 支持返回 true
     */
    @Override
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
                filename = ((FileFILE) f).getTotalName();
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
     *
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
     *
     * @param extension 扩展
     * @return 是则返回true
     */
    @Override
    public boolean containsExtension(String extension) {
        return filters.contains(extension.toLowerCase());
    }

    @Override
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

                if (!filters.isEmpty()) {
                    fullDescription += "." + filters.get(0);
                    for (int i = 1; i < filters.size(); i++) {
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
     *
     * @return 是则返回true
     */
    public boolean isExtensionListInDescription() {
        return useExtensionsInDescription;
    }

    /**
     * 默认情况下 返回首位的后缀名
     * get extention.
     */
    public String getExtensionString() {
        return getExtensionString(0);
    }

    public String getExtensionString(int index) {
        if (filters == null || filters.isEmpty() || index >= filters.size()) {
            return StringUtils.EMPTY;
        }
        return CoreConstants.DOT + filters.get(index);
    }

    public int getExtensionCount() {
        if (filters == null || filters.isEmpty()) {
            return 0;
        }
        return filters.size();
    }

    /**
     * toString方便重写
     *
     * @return 重写
     */
    @Override
    public String toString() {
        return getDescription();
    }


    @Override
    public boolean equals(Object o) {
        return (o instanceof ChooseFileFilter)
                && ComparatorUtils.equals(((ChooseFileFilter) o).getDescription(), getDescription());
    }
}