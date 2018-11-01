package com.fr.design.reportfit.menupane;

import com.fr.base.BaseUtils;
import com.fr.design.actions.JTemplateAction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.UIDialog;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuKeySet;
import com.fr.report.reportfit.FitProvider;
import com.fr.report.reportfit.ReportFitAttr;

import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

/**
 * Created by Administrator on 2015/7/6 0006.
 */
public class ReportFitAttrAction extends JTemplateAction {
    private static final Dimension MEDIUM = new Dimension(430, 400);
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
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/reportfit/fit.png"));
    }

    /**
     * Action触发事件
     *
     * @param e 事件
     */
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
            final ReportFitAttrPane attrPane = new ReportFitAttrPane();
            showReportFitDialog(fitAttr, jwb, wbTpl, attrPane);
        }
    }

    private void showReportFitDialog(ReportFitAttr fitAttr, final JTemplate jwb, final FitProvider wbTpl, final BasicBeanPane<ReportFitAttr> attrPane) {
        attrPane.populateBean(fitAttr);
        UIDialog dialog = attrPane.showUnsizedWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                wbTpl.setReportFitAttr(attrPane.updateBean());
                jwb.fireTargetModified();
            }
        });
        dialog.setSize(MEDIUM);
        dialog.setVisible(true);
    }

}