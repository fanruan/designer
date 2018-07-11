package com.fr.design.fun;

import com.fr.design.report.AbstractExportPane;
import com.fr.stable.fun.mark.Mutable;

import javax.swing.*;

/**
 * Created by vito on 16/5/5.
 */

/**
 * 导出属性Tab页的接口
 */
public interface ExportAttrTabProvider extends Mutable {
    String XML_TAG = "ExportAttrTabProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 转换成SwingComponent
     *
     * @return JComponent
     */
    JComponent toSwingComponent();

    /**
     * 转换成AbstractExportPane
     *
     * @return AbstractExportPane
     */
    AbstractExportPane toExportPane();

    /**
     * tab的标题
     *
     * @return
     */
    String title();

    /**
     * tag标签
     *
     * @return tag名称
     * @deprecated 将来不使用tag作为区分
     */
    String tag();
}
