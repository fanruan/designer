package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.JTemplateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.form.mobile.FormMobileAttrPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormArea;
import com.fr.design.mainframe.JForm;
import com.fr.design.menu.MenuKeySet;
import com.fr.form.main.Form;
import com.fr.form.main.mobile.FormMobileAttr;
import com.fr.general.Inter;
import com.fr.plugin.ExtraClassManager;
import com.fr.stable.ReportFunctionProcessor;
import com.fr.stable.fun.FunctionProcessor;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by fanglei on 2016/11/14.
 */
public class FormMobileAttrAction extends JTemplateAction<JForm> {

    public FormMobileAttrAction(JForm jf) {
        super(jf);
        this.setMenuKeySet(REPORT_APP_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/mobile.png"));
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        final JForm jf = getEditingComponent();
        if (jf == null) {
            return;
        }
        final Form formTpl = jf.getTarget();
        FormMobileAttr mobileAttr = formTpl.getFormMobileAttr();

        final FormMobileAttrPane mobileAttrPane = new FormMobileAttrPane();
        mobileAttrPane.populateBean(mobileAttr);
        BasicDialog dialog = mobileAttrPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                FormMobileAttr formMobileAttr = mobileAttrPane.updateBean();
                formTpl.setFormMobileAttr(formMobileAttr);
                ((FormArea)jf.getFormDesign().getParent()).onMobileAttrModified();
                jf.fireTargetModified();
                if (formMobileAttr.isMobileOnly()) {
                    FunctionProcessor processor = ExtraClassManager.getInstance().getFunctionProcessor();
                    if (processor != null) {
                        processor.recordFunction(ReportFunctionProcessor.MOBILE_TEMPLATE_FRM);
                    }
                }
            }
        });
        dialog.setVisible(true);
    }

    private static final MenuKeySet REPORT_APP_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Mobile-Attr");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
