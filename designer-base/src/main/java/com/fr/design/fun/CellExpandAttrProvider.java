package com.fr.design.fun;

import com.fr.design.beans.BasicBeanPane;
import com.fr.report.cell.TemplateCellElement;
import com.fr.stable.fun.mark.Mutable;

/**
 * @author yaohwu
 * created by yaohwu at 2020/4/26 15:50
 */
public interface CellExpandAttrProvider extends Mutable {

    String MARK_STRING = "CellExpandAttrProvider";

    int CURRENT_LEVEL = 1;

    BasicBeanPane<TemplateCellElement> createPanel();
}
