package com.fr.design.actions.file.export;

import com.fr.design.actions.JTemplateAction;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.gui.iprogressbar.FRProgressBar;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.exception.RemoteDesignPermissionDeniedException;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.RenameExportFILE;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.io.exporter.DesignExportType;
import com.fr.io.exporter.ExporterKey;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import com.fr.third.jodd.io.FileNameUtil;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.exporter.TemplateExportOperator;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.util.Map;

public abstract class AbstractExportAction<E extends JTemplate<?, ?>> extends JTemplateAction<E> {

    private FRProgressBar progressbar;

    public AbstractExportAction(E t) {
        super(t);
    }

    /**
     * 导出接口名
     *
     * @return String scopeName
     */
    public abstract ExporterKey exportKey();

    /**
     * 导出类型
     *
     * @return DesignExportType tyoe
     */
    public abstract DesignExportType exportType();

    /**
     * 目标文件过滤器
     *
     * @return ChooseFileFilter filter
     */
    protected abstract ChooseFileFilter getChooseFileFilter();

    /**
     * 目标文件扩展名
     *
     * @return String extensionName
     */
    protected abstract String getDefaultExtension();

    /**
     * 处理参数
     *
     * @return Map para
     */
    protected abstract Map<String, Object> processParameter();


    /**
     * 执行方法
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (!processNotSaved()) {
            return;
        }

        Map<String, Object> para = processParameter();

        // 选择输入的文件
        FILEChooserPane fileChooserPane = FILEChooserPane.getMultiEnvInstance(true, false);
        fileChooserPane.addChooseFILEFilter(this.getChooseFileFilter());


        String fileName = getTargetFileName();
        fileChooserPane.setFileNameTextField(fileName, "." + this.getDefaultExtension());
        int saveValue = fileChooserPane.showSaveDialog(DesignerContext.getDesignerFrame(), "." + this.getDefaultExtension());
        if (saveValue == FILEChooserPane.JOPTIONPANE_OK_OPTION || saveValue == FILEChooserPane.OK_OPTION) {
            FILE target = fileChooserPane.getSelectedFILE();
            //rename 方式导出
            target = RenameExportFILE.create(target);
            try {
                target.mkfile();
            } catch (Exception exp) {
                FineLoggerFactory.getLogger().error("Error In Make New File", exp);
            }
            FineLoggerFactory.getLogger().info(
                    "\"" + RenameExportFILE.recoverFileName(target.getName()) + "\"" +
                            Toolkit.i18nText("Fine-Design_Report_Prepare_Export") + "!"
            );

            progressbar = new FRProgressBar(
                    createExportWork(getSource(), target, para),
                    DesignerContext.getDesignerFrame(),
                    Toolkit.i18nText("Fine-Design_Report_Exporting"),
                    StringUtils.EMPTY,
                    0,
                    100);

            progressbar.start();
        }
    }

    private FILE getSource() {
        return this.getEditingComponent().getEditingFILE();
    }

    private String getTargetFileName() {
        FILE source = getSource();
        String fileName = source.getName();
        return FileNameUtil.removeExtension(fileName);
    }

    private boolean processNotSaved() {
        //当前编辑的模板
        E e = getEditingComponent();
        if (!e.isALLSaved() && !DesignModeContext.isVcsMode()) {
            e.stopEditing();
            int returnVal = JOptionPane.showConfirmDialog(
                    DesignerContext.getDesignerFrame(),
                    Toolkit.i18nText("Fine-Design_Basic_Utils_Would_You_Like_To_Save") + " \"" + e.getEditingFILE() + "\" ?",
                    ProductConstants.PRODUCT_NAME,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (returnVal == JOptionPane.YES_OPTION) {
                e.saveTemplate();
                FineLoggerFactory.getLogger().info(
                        Toolkit.i18nText("Fine-Design_Basic_Template_Already_Saved", e.getEditingFILE().getName())
                );
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private SwingWorker createExportWork(final FILE source, final FILE target, final Map<String, Object> parameterMap) {
        final String path = source.getPath();
        final String name = RenameExportFILE.recoverFileName(target.getName());

        return new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                //bug 10516
                Thread.sleep(100);
                try (OutputStream outputStream = target.asOutputStream()) {
                    this.setProgress(10);
                    dealExporter(outputStream, path, parameterMap);
                    this.setProgress(80);
                    outputStream.flush();
                    this.setProgress(100);

                    FineLoggerFactory.getLogger().info("\"" + name + "\"" + Toolkit.i18nText("Fine-Design_Report_Finish_Export") + "!");
                    JOptionPane.showMessageDialog(
                            DesignerContext.getDesignerFrame(),
                            Toolkit.i18nText("Fine-Design_Report_Exported_Successfully") + "\n" + name);


                } catch (RemoteDesignPermissionDeniedException exp) {
                    FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                    this.setProgress(100);
                    target.closeTemplate();
                    JOptionPane.showMessageDialog(
                            DesignerContext.getDesignerFrame(),
                            Toolkit.i18nText("Fine-Engine_Remote_Design_Permission_Denied"),
                            UIManager.getString("OptionPane.messageDialogTitle"),
                            JOptionPane.ERROR_MESSAGE,
                            UIManager.getIcon("OptionPane.errorIcon")
                    );
                } catch (Exception exp) {
                    FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                    this.setProgress(100);
                    target.closeTemplate();
                    JOptionPane.showMessageDialog(
                            DesignerContext.getDesignerFrame(),
                            Toolkit.i18nText("Fine-Design_Report_Export_Failed") + "\n" + path,
                            UIManager.getString("OptionPane.messageDialogTitle"),
                            JOptionPane.ERROR_MESSAGE,
                            UIManager.getIcon("OptionPane.errorIcon")
                    );
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
                    .export(exportKey(), exportType(), outputStream, path, para);
        } else {
            byte[] contents =
                    WorkContext.getCurrent().get(TemplateExportOperator.class)
                            .export(exportKey(), exportType(), null, path, para);

            outputStream.write(contents);
        }
    }

}
