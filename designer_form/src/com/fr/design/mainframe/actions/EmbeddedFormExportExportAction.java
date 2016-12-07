package com.fr.design.mainframe.actions;

import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.design.actions.JTemplateAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.iprogressbar.FRProgressBar;
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
import com.fr.form.main.FormEmbeddedTableDataExporter;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.project.ProjectConstants;

/**
 * Export Embedded.
 */
public class EmbeddedFormExportExportAction extends JTemplateAction<JForm>{
	
    private FRProgressBar progressbar;
    /**
     * Constructor
     */
    public EmbeddedFormExportExportAction(JForm jwb) {
    	super(jwb);
        this.setMenuKeySet(KeySetUtils.EMBEDDED_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/oem/logo.png"));
    }
    
    /**
	 * Action触发事件
	 * 
	 * @param e 触发事件
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
        JTemplate jwb = this.getEditingComponent();
        FILE editingFILE = jwb.getEditingFILE();
        DesignerFrame designerFrame = DesignerContext.getDesignerFrame();

        final Map<String, Object> parameterMap = new HashMap<String, Object>();
        final Form tpl = this.getEditingComponent().getTarget();
        inputParameter(parameterMap, tpl, designerFrame);
        
        FILEChooserPane fileChooserPane = FILEChooserPane.getInstance(false, true);
        fileChooserPane.setFILEFilter(this.getChooseFileFilter());

        String filenName = editingFILE.getName();
        fileChooserPane.setFileNameTextField(filenName, ProjectConstants.FRM_SUFFIX);
        int saveValue = fileChooserPane.showSaveDialog(designerFrame, ProjectConstants.FRM_SUFFIX);
        if (isCancel(saveValue)) {
            fileChooserPane = null;
            return;
        }
        
        if (isOk(saveValue)) {
        	startExport(parameterMap, tpl, designerFrame, fileChooserPane);
        }
	}
	
	private void startExport(Map<String, Object> parameterMap, Form tpl, DesignerFrame designerFrame, 
			FILEChooserPane fileChooserPane){
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
	
	private boolean isOk(int saveValue){
		return saveValue == FILEChooserPane.JOPTIONPANE_OK_OPTION || saveValue == FILEChooserPane.OK_OPTION;
	}
	
	private boolean isCancel(int saveValue){
		return saveValue == FILEChooserPane.CANCEL_OPTION || saveValue == FILEChooserPane.JOPTIONPANE_CANCEL_OPTION;
	}
	
	private void inputParameter(final Map<String, Object> parameterMap, final Form tpl, DesignerFrame designerFrame){
        Parameter[] parameters = tpl.getParameters();
        if (ArrayUtils.isNotEmpty(parameters)) {// 检查Parameter.
            final ParameterInputPane pPane = new ParameterInputPane(parameters);
            pPane.showSmallWindow(designerFrame, new DialogActionAdapter() {

                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        }
	}
	
    protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"frm"}, Inter.getLocText("FR-Designer_Form_EmbeddedTD"));
    }
	
    private SwingWorker createExportWork(FILE file, final Form tpl, final Map parameterMap) {
        final String filePath = file.getPath();
        final String fileGetName = file.getName();

        SwingWorker exportWorker = new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                Thread.sleep(100);
                try {
                    final FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                    this.setProgress(10);
                    FormEmbeddedTableDataExporter exporter = new FormEmbeddedTableDataExporter();
                    exporter.export(fileOutputStream, tpl, parameterMap);
                    this.setProgress(80);
                    fileOutputStream.close();
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

            public void done() {
                progressbar.close();
            }
        };
        return exportWorker;
    }

}