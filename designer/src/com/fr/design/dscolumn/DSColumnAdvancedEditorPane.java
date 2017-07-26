package com.fr.design.dscolumn;

import com.fr.design.mainframe.cell.CellEditorPane;
import com.fr.report.cell.TemplateCellElement;


/**
 * 单元格元素 数据列 基本设置内容面板
 *
 * @author yaoh.wu
 * @version 2017年7月25日
 * @since 9.0
 */
public class DSColumnAdvancedEditorPane extends CellEditorPane {


    @Override
    public String getIconPath() {
        return "Advanced";
    }

    @Override
    public String title4PopupWindow() {
        return "Advanced";
    }


    @Override
    public void update() {

    }

    @Override
    public void populate(TemplateCellElement cellElement) {

    }

}
