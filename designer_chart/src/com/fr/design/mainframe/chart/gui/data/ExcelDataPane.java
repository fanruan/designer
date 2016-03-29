/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.data.impl.ExcelTableData;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: 下午11:58
 */
public class ExcelDataPane extends ChartDesignDataLoadPane {

    private UITextField path = new UITextField();
    private UIButton reviewButton;
    private ExcelTableData tableData;
    private MouseListener listener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            FILEChooserPane fileChooserPane = new FILEChooserPane(true, true);
            if (fileChooserPane.showOpenDialog(DesignerContext.getDesignerFrame(), ".xlsx")
                    == FILEChooserPane.OK_OPTION) {
                FILE chooseFILE = fileChooserPane.getSelectedFILE();
                if (chooseFILE != null && chooseFILE.exists()) {
                    path.setText(chooseFILE.getPath());
                } else {
                    JOptionPane.showConfirmDialog(ExcelDataPane.this, Inter.getLocText("FR-Template-Path_chooseRightPath"),
                            Inter.getLocText("FR-App-All_Warning"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    path.setText("");
                }
                tableData.setFilePath(path.getText().toString());
                tableData.setFromEnv(chooseFILE.isEnvFile());
                tableData.setNeedColumnName(true);
                fireChange();
            }
        }
    };

    public ExcelDataPane(AbstractChartDataPane4Chart parentPane, JComponent pathChooseButton) {
        super(parentPane);
        initReviewButton();
        tableData = new ExcelTableData();
        tableData.setFilePath(path.getText().toString());
        tableData.setNeedColumnName(true);
        path.setEditable(false);
        pathChooseButton.addMouseListener(listener);
        this.setLayout(new BorderLayout(0, 0));
        JPanel pane = new JPanel(new BorderLayout(LayoutConstants.HGAP_LARGE, 0));
        pane.add(path, BorderLayout.CENTER);

        pane.add(reviewButton, BorderLayout.EAST);
        this.add(pane, BorderLayout.CENTER);
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
            public void mouseClicked(MouseEvent e) {
                //预览本地excel
                try {
                    PreviewTablePane.previewTableData(getTableData());
                } catch (Exception e1) {
                    FRContext.getLogger().error(e1.getMessage(), e1);
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
     *
     * @param tableData 数据集
     */
    public void populateChartTableData(TableData tableData) {
        if (tableData instanceof ExcelTableData) {
            path.setText(((ExcelTableData) tableData).getFilePath());
            this.tableData = (ExcelTableData)tableData;
            this.tableData.setNeedColumnName(true);
        }
        fireChange();
    }
}