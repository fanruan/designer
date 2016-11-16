package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.JTemplateAction;
import com.fr.design.mainframe.JForm;
import com.fr.design.menu.MenuKeySet;
import com.fr.form.main.Form;
import com.fr.report.mobile.ElementCaseMobileAttr;

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
    }

    private static final MenuKeySet REPORT_APP_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return "移动端属性";
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
