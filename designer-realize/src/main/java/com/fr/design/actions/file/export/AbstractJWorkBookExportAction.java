/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.Parameter;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.iprogressbar.FRProgressBar;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.log.FineLoggerFactory;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.report.report.Report;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.exporter.TemplateExportOperator;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract export action.
 */
public abstract class AbstractJWorkBookExportAction extends AbstractExportAction<JWorkBook> {

    private FRProgressBar progressbar;

    protected AbstractJWorkBookExportAction(JWorkBook jwb) {
        super(jwb);
    }

    protected WorkBook getTemplateWorkBook() {
        return this.getEditingComponent().getTarget();
    }

    /**
     * 执行方法
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // todo 弹出提醒保存，如果没保存


        JWorkBook jwb = this.getEditingComponent();
        FILE source = jwb.getEditingFILE();

        // 弹出参数
        final Map<String, Object> parameterMap = new HashMap<>();
        final TemplateWorkBook tpl = getTemplateWorkBook();
        Parameter[] parameters = tpl.getParameters();
        if (parameters != null && parameters.length > 0) {// 检查Parameter.
            final ParameterInputPane pPane = new ParameterInputPane(
                    parameters);
            pPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        }

        // file choose
        FILEChooserPane fileChooserPane = FILEChooserPane.getInstance(true, true);
        fileChooserPane.addChooseFILEFilter(this.getChooseFileFilter());

        // 打开文件后输出文件名修改，eg：w.cpt.doc / w.svg.doc，去掉中间的后缀名~~ w.doc
        String fileName = source.getName();
        if (fileName.indexOf('.') != -1) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        fileChooserPane.setFileNameTextField(fileName, "." + this.getDefaultExtension());
        int saveValue = fileChooserPane.showSaveDialog(DesignerContext.getDesignerFrame(), "." + this.getDefaultExtension());
        if (saveValue == FILEChooserPane.JOPTIONPANE_OK_OPTION || saveValue == FILEChooserPane.OK_OPTION) {
            FILE target = fileChooserPane.getSelectedFILE();
            try {
                target.mkfile();
            } catch (Exception exp) {
                FineLoggerFactory.getLogger().error("Error In Make New File", exp);
            }
            FineLoggerFactory.getLogger().info("\"" + target.getName() + "\"" + Toolkit.i18nText("Fine-Design_Report_Prepare_Export") + "!");

            (progressbar = new FRProgressBar(
                    createExportWork(source, target, parameterMap),
                    DesignerContext.getDesignerFrame(),
                    Toolkit.i18nText("Fine-Design_Report_Exporting"),
                    StringUtils.EMPTY,
                    0,
                    100)
            ).start();
        }
    }

    private SwingWorker createExportWork(final FILE source, final FILE target, final Map<String, Object> parameterMap) {
        final String path = source.getPath();
        final String name = target.getName();

        return new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                //bug 10516
                Thread.sleep(100);
                OutputStream outputStream = null;
                try {
                    outputStream = target.asOutputStream();
                    this.setProgress(10);
                    dealExporter(outputStream, path, parameterMap);
                    this.setProgress(80);
                    outputStream.flush();
                    outputStream.close();
                    this.setProgress(100);

                    FineLoggerFactory.getLogger().info("\"" + name + "\"" + Toolkit.i18nText("Fine-Design_Report_Finish_Export") + "!");
                    JOptionPane.showMessageDialog(
                            DesignerContext.getDesignerFrame(),
                            Toolkit.i18nText("Fine-Design_Report_Exported_Successfully") + "\n" + name);
                } catch (Exception exp) {
                    this.setProgress(100);
                    target.closeTemplate();
                    FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                    JOptionPane.showMessageDialog(
                            DesignerContext.getDesignerFrame(),
                            Toolkit.i18nText("Fine-Design_Report_Export_Failed") + "\n" + path,
                            null,
                            JOptionPane.ERROR_MESSAGE,
                            UIManager.getIcon("OptionPane.errorIcon")
                    );
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
                return null;
            }

            @Override
            public void done() {
                progressbar.close();
            }
        };
    }

    private void dealExporter(OutputStream outputStream, String path, final Map<String, Object> para) throws Exception {

        // 没有办法处理这个 isLocal 判断，因为一个是修改参数传递结果，一个是返回值做结果
        // todo 后续想想办法
        if (WorkContext.getCurrent().isLocal()) {
            WorkContext.getCurrent().get(TemplateExportOperator.class)
                    .export(exportScopeName(), exportType(), outputStream, path, para);
        } else {
            byte[] contents =
                    WorkContext.getCurrent().get(TemplateExportOperator.class)
                            .export(exportScopeName(), exportType(), null, path, para);

            outputStream.write(contents);
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


    protected abstract String getDefaultExtension();

    public String exportScopeName() {
        return "FINE_BOOK";
    }
}
