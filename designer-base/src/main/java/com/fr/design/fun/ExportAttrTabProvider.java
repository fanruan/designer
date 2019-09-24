package com.fr.design.fun;

import com.fr.design.beans.BasicStorePane;
import com.fr.stable.fun.mark.Mutable;

/**
 * 导出属性Tab页的接口
 */
public interface ExportAttrTabProvider extends Mutable {
    String XML_TAG = "ExportAttrTabProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 转换成业务视图界面
     *
     * @return 业务视图界面
     */
    BasicStorePane<?> toServiceComponent();
}
