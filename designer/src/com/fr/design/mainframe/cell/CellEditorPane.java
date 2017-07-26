package com.fr.design.mainframe.cell;

import com.fr.design.dialog.BasicPane;
import com.fr.report.cell.TemplateCellElement;

/**
 * 右侧单元格元素面板抽象类
 *
 * @author yaoh.wu
 * @version 2017年7月25日
 * @since 9.0
 */
public abstract class CellEditorPane extends BasicPane {

    public abstract String getIconPath();

    public abstract String title4PopupWindow();

    /**
     * 从面板拿数据保存
     */
    public abstract void update();

    /**
     * 更新面板数据
     *
     * @param cellElement 单元格
     */
    public abstract void populate(TemplateCellElement cellElement);
}
