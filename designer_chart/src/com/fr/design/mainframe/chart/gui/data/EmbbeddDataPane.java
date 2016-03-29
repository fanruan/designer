/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.design.constants.UIConstants;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.data.tabledata.tabledatapane.EmbeddedTableDataPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 图表设计器内置数据集面板
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-16
 * Time: 上午12:09
 */
public class EmbbeddDataPane extends ChartDesignDataLoadPane {
    private UIButton edit;
    private UIButton reviewButton;
    private EmbeddedTableData tableData;

    public EmbbeddDataPane(AbstractChartDataPane4Chart parentPane) {
        super(parentPane);
        tableData = new EmbeddedTableData();
        initEditButton();
        initReviewButton();
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 3));
        this.add(edit);
        this.add(reviewButton);
    }

    private void initEditButton() {
        edit = new UIButton(BaseUtils.readIcon("com/fr/design/images/control/edit.png"));
        edit.setBorder(new LineBorder(UIConstants.LINE_COLOR));
        edit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EmbeddedTableDataPane tableDataPane = new EmbeddedTableDataPane();
                tableDataPane.populateBean(tableData);
                dgEdit(tableDataPane, getNamePrefix());
            }
        });
    }

    private void initReviewButton() {
        reviewButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/data/search.png"));
        reviewButton.setBorder(new LineBorder(UIConstants.LINE_COLOR));
        reviewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //预览图表设计器内置数据集
                TableDataWrapper tableDataWrappe = getTableDataWrapper();
                if (tableDataWrappe != null) {
                    try {
                        tableDataWrappe.previewData();
                    } catch (Exception e1) {
                        FRContext.getLogger().error(e1.getMessage(), e1);
                    }
                }
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    @Override
    public TableData getTableData() {
        return tableData;
    }

    /**
     * 加载数据集
     *
     * @param tableData 数据集
     */
    public void populateChartTableData(TableData tableData) {
        if (tableData instanceof EmbeddedTableData) {
            this.tableData =(EmbeddedTableData) tableData;
        }
    }

    protected String getNamePrefix() {
        return "Embedded";
    }

    /**
     * 编辑面板
     *
     * @param uPanel       面板
     * @param originalName 原始名字
     */
    private void dgEdit(final EmbeddedTableDataPane uPanel, String originalName) {
        final BasicPane.NamePane nPanel = uPanel.asNamePane();
        nPanel.setObjectName(originalName);
        final BasicDialog dg;
        dg = nPanel.showLargeWindow(SwingUtilities.getWindowAncestor(EmbbeddDataPane.this), new DialogActionAdapter() {
            public void doOk() {
                tableData = uPanel.updateBean();
                fireChange();
            }
        });
        dg.setVisible(true);
    }
}