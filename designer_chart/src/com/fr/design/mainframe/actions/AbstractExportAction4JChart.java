/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.actions;

import com.fr.base.FRContext;
import com.fr.design.gui.iprogressbar.FRProgressBar;
import com.fr.form.ui.ChartBook;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.JChart;
import com.fr.design.mainframe.exporter.Exporter4Chart;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: 上午9:48
 */
public abstract class AbstractExportAction4JChart extends JChartAction {
    protected AbstractExportAction4JChart(JChart chart){
        super(chart);
    }

    private FRProgressBar progressbar;

    protected ChartBook getChartBook(){
        return this.getEditingComponent().getTarget();
    }

    /**
     * 执行方法
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        JChart chart = this.getEditingComponent();
        FILE editingFILE = chart.getEditingFILE();
        DesignerFrame designerFrame = DesignerContext.getDesignerFrame();

        // 弹出参数
        final java.util.Map parameterMap = new java.util.HashMap();
        final ChartBook chartBook = getChartBook();

        // Choose a file name....
        FILEChooserPane fileChooserPane = FILEChooserPane.getInstance(false, true);
        fileChooserPane.setFILEFilter(this.getChooseFileFilter());

        // 打开文件后输出文件名修改，eg：w.cpt.doc / w.svg.doc，去掉中间的后缀名~~ w.doc
        String filenName = editingFILE.getName();
        if (filenName.indexOf('.') != -1) {
            filenName = filenName.substring(0, editingFILE.getName().lastIndexOf('.'));
        }
        fileChooserPane.setFileNameTextField(filenName, "." + this.getDefaultExtension());
        int saveValue = fileChooserPane.showSaveDialog(designerFrame, "." + this.getDefaultExtension());
        if (saveValue == FILEChooserPane.CANCEL_OPTION || saveValue == FILEChooserPane.JOPTIONPANE_CANCEL_OPTION) {
            fileChooserPane = null;
            return;
        } else if (saveValue == FILEChooserPane.JOPTIONPANE_OK_OPTION || saveValue == FILEChooserPane.OK_OPTION) {
            FILE file = fileChooserPane.getSelectedFILE();
            try {
                file.mkfile();
            } catch (Exception e1) {
                FRLogger.getLogger().error("Error In Make New File");
            }
            fileChooserPane = null;
            FRContext.getLogger().info("\"" + file.getName() + "\"" + Inter.getLocText("Prepare_Export") + "!");

            (progressbar = new FRProgressBar(createExportWork(file, chartBook), designerFrame,
                    Inter.getLocText("Exporting"), "", 0, 100)).start();
        }

    }

    private SwingWorker createExportWork(FILE file, final ChartBook chartBook) {
        final String filePath = file.getPath();
        final String fileGetName = file.getName();

        SwingWorker exportWorker = new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                Thread.sleep(100); //bug 10516
                try {
                    final FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                    this.setProgress(10);
                    dealExporter(fileOutputStream,chartBook);
                    this.setProgress(80);
                    fileOutputStream.close();
                    this.setProgress(100);

                    FRContext.getLogger().info("\"" + fileGetName + "\"" + Inter.getLocText("Finish_Export") + "!");
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                            Inter.getLocText("Exported_successfully") + "\n" + filePath);
                } catch (Exception exp) {
                    this.setProgress(100);
                    FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Export_failed") + "\n" + filePath);
                }
                return null;
            }

            public void done() {
                progressbar.close();
            }
        };
        return exportWorker;
    }

    private void dealExporter(FileOutputStream fileOutputStream, final ChartBook chartBook) throws Exception {
        final Exporter4Chart exporter = AbstractExportAction4JChart.this.getExporter();
        exporter.export(fileOutputStream,this.getEditingComponent());
    }



    protected abstract ChooseFileFilter getChooseFileFilter();

    protected abstract String getDefaultExtension();

    protected abstract Exporter4Chart getExporter();

}