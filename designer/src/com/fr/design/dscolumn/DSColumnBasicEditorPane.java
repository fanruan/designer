package com.fr.design.dscolumn;

import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.cell.CellEditorPane;
import com.fr.report.cell.TemplateCellElement;

import javax.swing.*;
import java.awt.*;

/**
 * 单元格元素 数据列 高级设置内容面板
 *
 * @author yaoh.wu
 * @version 2017年7月25日
 * @since 9.0
 */
public class DSColumnBasicEditorPane extends CellEditorPane {

    //数据集和数据列
    private SelectedDataColumnPane dataPane;
    //数据分组设置
    private ResultSetGroupDockingPane groupPane;
    //当前编辑的单元格
    private TemplateCellElement cellElement;

    public DSColumnBasicEditorPane(TemplateCellElement cellElement, SelectedDataColumnPane dataPane, ResultSetGroupDockingPane groupPane) {
        this.cellElement = cellElement;
        this.dataPane = dataPane;
        this.groupPane = groupPane;
        this.add(this.createContentPane(), BorderLayout.CENTER);
    }

    private JPanel createContentPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                //数据集列选择
                new Component[]{this.dataPane, null},
                //数据分组设置
                new Component[]{this.groupPane, null}
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    @Override
    public String getIconPath() {
        return "Basic";
    }

    @Override
    public String title4PopupWindow() {
        return "Basic";
    }


    @Override
    public void update() {
        dataPane.update(this.cellElement);
        groupPane.update();
    }

    @Override
    public void populate(TemplateCellElement cellElement) {
        this.cellElement = cellElement;
        dataPane.populate(null, cellElement);
        groupPane.populate(cellElement);
    }
}
