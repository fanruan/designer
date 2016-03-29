/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.chart.chartdata.JSONTableData;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: 下午6:45
 */
public class JSONDataPane extends ChartDesignDataLoadPane {
    private UITextField url = new UITextField();
    private UIButton reviewButton;
    private JSONTableData tableData;

    public JSONDataPane(AbstractChartDataPane4Chart parentPane) {
        super(parentPane);
        initReviewButton();
        url.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER){
                    tableData.setFilePath(url.getText());
                    fireChange();
                }
            }
        });
        this.setLayout(new BorderLayout(0, 0));
        JPanel pane = new JPanel(new BorderLayout(LayoutConstants.HGAP_LARGE, 0));
        pane.add(url, BorderLayout.CENTER);
        pane.add(reviewButton, BorderLayout.EAST);
        this.add(pane, BorderLayout.CENTER);
        tableData = new JSONTableData(url.getText());
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    private void initReviewButton() {
        reviewButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/data/search.png"));
        reviewButton.setBorder(new LineBorder(UIConstants.LINE_COLOR));
        reviewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                tableData.setFilePath(url.getText());
                fireChange();
                //预览JSON数据
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
    public TableData getTableData() {
        return tableData;
    }

    @Override
    protected String getNamePrefix() {
        return null;
    }

    /**
     * 加载数据集
     * @param tableData 数据集
     */
    public void populateChartTableData(TableData tableData) {
        if(tableData instanceof JSONTableData) {
            url.setText(((JSONTableData) tableData).getFilePath());
            this.tableData = (JSONTableData)tableData;
        }
    }
}