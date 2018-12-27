package com.fr.design.actions;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.design.actions.JTemplateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.JForm;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.parameter.ParameterArrayPane;
import com.fr.form.main.Form;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by 夏翔 on 2016/6/18.
 */
public class TemplateParameterAction extends JTemplateAction<JForm> {
    public TemplateParameterAction(JForm jwb) {
        super(jwb);
        this.setMenuKeySet(KeySetUtils.REPORT_PARAMETER_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/p.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JForm jwb = getEditingComponent();
        if (jwb == null) {
            return;
        }
        final Form wbTpl = jwb.getTarget();

        final ParameterArrayPane parameterArrayPane = new ParameterArrayPane();
        BasicDialog parameterArrayDialog = parameterArrayPane.showWindow(SwingUtilities.getWindowAncestor(jwb));
        parameterArrayDialog.setModal(true);

        final Parameter[] copyTemplateParameters = wbTpl.getTemplateParameters();
        parameterArrayPane.populate(copyTemplateParameters);
        parameterArrayDialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                wbTpl.clearParameters();
                Parameter[] parameters = parameterArrayPane.updateParameters();
                for (int i = 0; i < parameters.length; i++) {
                    wbTpl.addParameter(parameters[i]);
                }
                jwb.fireTargetModified();
                jwb.getFormDesign().setParameterArray(parameters);
                jwb.getFormDesign().refreshParameter();
            }
        });
        parameterArrayDialog.setVisible(true);

    }

}
