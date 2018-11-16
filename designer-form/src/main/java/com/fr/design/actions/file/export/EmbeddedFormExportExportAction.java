package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.iprogressbar.FRProgressBar;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.JForm;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.form.main.Form;
import com.fr.io.exporter.DesignExportType;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.exporter.TemplateExportOperator;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Export Embedded.
 */
public class EmbeddedFormExportExportAction extends AbstractExportAction<JForm> {

    private FRProgressBar progressbar;

    public EmbeddedFormExportExportAction(JForm jwb) {
        super(jwb);
        this.setMenuKeySet(KeySetUtils.EMBEDDED_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/oem/logo.png"));
    }

    @Override
    public String exportScopeName() {
        return "FINE_FORM";
    }

    @Override
    public DesignExportType exportType() {
        return DesignExportType.EMBEDDED_FORM;
    }

    /**
     * Action触发事件
     *
     * @param e 触发事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // todo 提示保存
        JTemplate jwb = this.getEditingComponent();
        FILE source = jwb.getEditingFILE();

        // 输入参数
        final Map<String, Object> parameterMap = new HashMap<String, Object>();
        final Form tpl = this.getEditingComponent().getTarget();
        inputParameter(parameterMap, tpl, DesignerContext.getDesignerFrame());

        FILEChooserPane fileChooserPane = FILEChooserPane.getInstance(false, true);
        fileChooserPane.setFILEFilter(this.getChooseFileFilter());

        String fileName = source.getName();
        fileChooserPane.setFileNameTextField(fileName, ProjectConstants.FRM_SUFFIX);
        int saveValue = fileChooserPane.showSaveDialog(DesignerContext.getDesignerFrame(), ProjectConstants.FRM_SUFFIX);

        if (isOk(saveValue)) {
            startExport(source, parameterMap, fileChooserPane);
        }
    }

    private void startExport(FILE source, Map<String, Object> parameterMap,
                             FILEChooserPane fileChooserPane) {
        FILE target = fileChooserPane.getSelectedFILE();
        try {
            target.mkfile();
        } catch (Exception e1) {
            FineLoggerFactory.getLogger().error("Error In Make New File");
        }
        FineLoggerFactory.getLogger().info("\"" + target.getName() + "\"" + Toolkit.i18nText("Fine-Design_Report_Prepare_Export") + "!");

        (progressbar =
                new FRProgressBar(
                        createExportWork(source, target, parameterMap),
                        DesignerContext.getDesignerFrame(),
                        Toolkit.i18nText("Fine-Design_Report_Exporting"),
                        StringUtils.EMPTY,
                        0,
                        100)
        ).start();
    }

    private boolean isOk(int saveValue) {
        return saveValue == FILEChooserPane.JOPTIONPANE_OK_OPTION || saveValue == FILEChooserPane.OK_OPTION;
    }


    private void inputParameter(final Map<String, Object> parameterMap, final Form tpl, DesignerFrame designerFrame) {
        Parameter[] parameters = tpl.getParameters();
        // 检查Parameter.
        if (ArrayUtils.isNotEmpty(parameters)) {
            final ParameterInputPane pPane = new ParameterInputPane(parameters);
            pPane.showSmallWindow(designerFrame, new DialogActionAdapter() {

                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        }
    }

    @Override
    protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"frm"}, Toolkit.i18nText("Fine-Design_Form_EmbeddedTD"));
    }

    private SwingWorker createExportWork(FILE source, final FILE target, final Map<String, Object> parameterMap) {
        final String path = source.getPath();
        final String name = target.getName();

        return new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(100);
                try (OutputStream fileOutputStream = target.asOutputStream()) {
                    this.setProgress(10);
                    dealExporter(fileOutputStream, path, parameterMap);
                    this.setProgress(80);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    this.setProgress(100);

                    FineLoggerFactory.getLogger().info("\"" + name + "\"" + Toolkit.i18nText("Fine-Design_Report_Finish_Export") + "!");
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                            Toolkit.i18nText("Fine-Design_Report_Exported_Successfully") + "\n" + name);
                } catch (Exception exp) {
                    this.setProgress(100);
                    FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Toolkit.i18nText("Fine-Design_Report_Export_Failed") + "\n" + path);
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

}
