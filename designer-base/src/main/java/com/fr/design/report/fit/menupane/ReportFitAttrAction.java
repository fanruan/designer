package com.fr.design.report.fit.menupane;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.JTemplateAction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.UIDialog;
import com.fr.design.fun.FormAdaptiveConfigUIProcessor;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.IOUtils;
import com.fr.report.fit.FitProvider;
import com.fr.report.fit.ReportFitAttr;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;

/**
 * Created by Administrator on 2015/7/6 0006.
 */
public class ReportFitAttrAction extends JTemplateAction {
    private static final MenuKeySet REPORT_FIT_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Template");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
    private static final MenuKeySet REPORT_FIT_ATTR_ELEMENTCASE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            FormAdaptiveConfigUIProcessor configPane = ExtraDesignClassManager.getInstance().getSingle(FormAdaptiveConfigUIProcessor.MARK_STRING);
            if (configPane != null) {
                return configPane.getConfigPane().getTitle();
            }
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Elementcase");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public ReportFitAttrAction(JTemplate jTemplate) {
        super(jTemplate);
        initMenuStyle();
    }

    private void initMenuStyle() {
        JTemplate jTemplate = getEditingComponent();
        if (jTemplate.isJWorkBook()) {
            this.setMenuKeySet(REPORT_FIT_ATTR);
        } else {
            this.setMenuKeySet(REPORT_FIT_ATTR_ELEMENTCASE);
        }
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/reportfit/fit.png"));
    }

    /**
     * Action触发事件
     *
     * @param e 事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        final JTemplate jwb = getEditingComponent();
        if (jwb == null) {
            return;
        }
        final FitProvider wbTpl = (FitProvider) jwb.getTarget();
        ReportFitAttr fitAttr = wbTpl.getReportFitAttr();
        if (jwb.isJWorkBook()) {
            final TemplateFitAttrPane attrPane = new TemplateFitAttrPane();
            showReportFitDialog(fitAttr, jwb, wbTpl, attrPane);
        } else {
            BasicBeanPane attrPane = null;
            FormAdaptiveConfigUIProcessor configPane = ExtraDesignClassManager.getInstance().getSingle(FormAdaptiveConfigUIProcessor.MARK_STRING);
            if (configPane != null) {
                attrPane =  configPane.getConfigPane();
            }else{
                attrPane = new ReportFitAttrPane();
            }
            showReportFitDialog(fitAttr, jwb, wbTpl, attrPane);
        }
    }

    private void showReportFitDialog(ReportFitAttr fitAttr, final JTemplate jwb, final FitProvider wbTpl, final BasicBeanPane<ReportFitAttr> attrPane) {
        attrPane.populateBean(fitAttr);
        UIDialog dialog = attrPane.showMediumWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                wbTpl.setReportFitAttr(attrPane.updateBean());
                jwb.fireTargetModified();
            }
        });
        dialog.setVisible(true);
    }

}
