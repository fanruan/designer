package com.fr.design.fun;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.stable.fun.mark.Immutable;

/**
 * Created by zhouping on 2015/11/11.
 */
public interface CellAttributeProvider extends Immutable{
    String MARK_STRING = "CellAttributeProvider";

    int CURRENT_LEVEL = 1;


    /**
     * 构造单元格属性面板
     * @return 面板类
     */
    AbstractAttrNoScrollPane createCellAttributePane();
}