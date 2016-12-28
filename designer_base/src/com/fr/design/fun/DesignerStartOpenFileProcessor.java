package com.fr.design.fun;

import com.fr.file.FILE;
import com.fr.stable.fun.mark.Immutable;

/**
 * 指定设计器启动时默认打开的文件
 * Created by rinoux on 2016/12/16.
 */
public interface DesignerStartOpenFileProcessor extends Immutable {

    int CURRENT_LEVEL = 1;

    String XML_TAG = "DesignerStartOpenFileProcessor";

    /**
     * 显示需要打开的报表文件
     */
    FILE fileToShow();
}
