package com.fr.file.filter;

import com.fr.file.FILE;

public interface FILEFilter {

    /**
     *文件是否支持
     * @param f 文件
     * @return 支持则返回true
     */
    public boolean accept(FILE f);

    /**
     * 取描述
     * @return 描述
     */
    public String getDescription();

    /**
     * 是否包含也定得扩展
     * @param extension  扩展
     * @return 包含则返回true
     */
    public boolean containsExtension(String extension);
    
}