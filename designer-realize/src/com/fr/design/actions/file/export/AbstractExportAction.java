/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.iprogressbar.FRProgressBar;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.io.exporter.AppExporter;
import com.fr.io.exporter.CSVExporter;
import com.fr.io.exporter.EmbeddedTableDataExporter;
import com.fr.io.exporter.ExcelExporter;
import com.fr.io.exporter.Exporter;
import com.fr.io.exporter.PDFExporterProcessor;
import com.fr.io.exporter.WordExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.page.PageSetProvider;
import com.fr.report.ReportHelper;
import com.fr.report.core.ReportUtils;
import com.fr.report.report.Report;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ActorConstants;
import com.fr.stable.ActorFactory;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.util.Map;

/**
 * Abstract export action.
 */
public abstract class AbstractExportAction extends JWorkBookAction {
    protected AbstractExportAction(JWorkBook jwb) {
        super(jwb);
    }

    private FRProgressBar progressbar;

    protected WorkBook getTemplateWorkBook() {
        return this.getEditingComponent().getTarget();
    }

    /**
     * 执行方法
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JWorkBook jwb = this.getEditingComponent();
        FILE editingFILE = jwb.getEditingFILE();
        DesignerFrame designerFrame = DesignerContext.getDesignerFrame();

        // 弹出参数
        final java.util.Map parameterMap = new java.util.HashMap();
        final TemplateWorkBook tpl = getTemplateWorkBook();
        Parameter[] parameters = tpl.getParameters();
        if (parameters != null && parameters.length > 0) {// 检查Parameter.
            final ParameterInputPane pPane = new ParameterInputPane(
                    parameters);
            pPane.showSmallWindow(designerFrame, new DialogActionAdapter() {

                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        }

        // Choose a file name....
        FILEChooserPane fileChooserPane = FILEChooserPane.getInstance(true, true);
        fileChooserPane.addChooseFILEFilter(this.getChooseFileFilter());

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
            FRContext.getLogger().info("\"" + file.getName() + "\"" + Inter.getLocText("FR-Designer_Prepare_Export") + "!");

            (progressbar = new FRProgressBar(createExportWork(file, tpl, parameterMap), designerFrame,
                    Inter.getLocText("FR-Designer_Exporting"), "", 0, 100)).start();
        }
    }

    private SwingWorker createExportWork(final FILE file, final TemplateWorkBook tpl, final Map parameterMap) {
        final String filePath = file.getPath();
        final String fileGetName = file.getName();

        SwingWorker exportWorker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(100); //bug 10516
                try {
                    OutputStream outputStream = file.asOutputStream();

                    this.setProgress(10);
                    dealExporter(outputStream, tpl, parameterMap);
                    this.setProgress(80);
                    outputStream.flush();
                    outputStream.close();
                    this.setProgress(100);

                    FRContext.getLogger().info("\"" + fileGetName + "\"" + Inter.getLocText("FR-Designer_Finish_Export") + "!");
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                            Inter.getLocText("FR-Designer_Exported_successfully") + "\n" + filePath);
                } catch (Exception exp) {
                    this.setProgress(100);
                    FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer_Export_failed") + "\n" + filePath);
                }
                return null;
            }

            @Override
            public void done() {
                progressbar.close();
            }
        };
        return exportWorker;
    }

    private void dealExporter(OutputStream outputStream, final TemplateWorkBook tpl, final Map parameterMap) throws Exception {
        final Exporter exporter = AbstractExportAction.this.getExporter();
        if (exporter instanceof AppExporter) {
            AppExporter appExporter = (AppExporter) exporter;
            if (exporter instanceof ExcelExporter || exporter instanceof CSVExporter
                    || exporter instanceof PDFExporterProcessor || exporter instanceof WordExporter) {
                ReportHelper.clearFormulaResult(tpl);// 清空rpt中的公式计算结果

                appExporter.export(outputStream, tpl.execute(parameterMap, ActorFactory.getActor(ActorConstants.TYPE_PAGE)
                ));
            } else {
                ReportHelper.clearFormulaResult(tpl);// 清空currentReport中的公式计算结果

                PageSetProvider pageSet = tpl.execute(parameterMap, ActorFactory.getActor(ActorConstants.TYPE_PAGE)).generateReportPageSet(
                        ReportUtils.getPaperSettingListFromWorkBook(tpl)).traverse4Export();
                appExporter.export(outputStream, pageSet);
                pageSet.release();
            }
        } else if (exporter instanceof EmbeddedTableDataExporter) {
            ((EmbeddedTableDataExporter) exporter).export(outputStream, (WorkBook) tpl, parameterMap);
        }
    }

    /*
     * 这边判断是否有层式报表，有层式需要使用大数据量导出
     */
    protected boolean hasLayerReport(TemplateWorkBook tpl) {
        if (tpl == null) {
            return false;
        }
        for (int i = 0; i < tpl.getReportCount(); i++) {
            Report r = tpl.getReport(i);
            if (r instanceof WorkSheet) {
                if (((WorkSheet) r).getLayerReportAttr() != null) {
                    return true;
                }
            }
        }

        return false;
    }

    protected abstract ChooseFileFilter getChooseFileFilter();

    protected abstract String getDefaultExtension();

    protected abstract Exporter getExporter();
}