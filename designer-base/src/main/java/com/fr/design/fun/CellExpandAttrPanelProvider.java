package com.fr.design.fun;

import com.fr.design.beans.BasicBeanPane;
import com.fr.report.cell.TemplateCellElement;
import com.fr.stable.fun.mark.Mutable;

/**
 * @author yaohwu
 * created by yaohwu at 2020/4/26 15:50
 */
public interface CellExpandAttrPanelProvider extends Mutable {

    String MARK_STRING = "CellExpandAttrPanelProvider";

    int CURRENT_LEVEL = 1;

    /**
     * @return 创建单元格属性-扩展设置中的额外面板
     */
    BasicBeanPane<TemplateCellElement> createPanel();
}
