package com.fr.design.actions.report;

import com.fr.base.IconManager;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.webattr.printsettings.ReportPrintSettingPane;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.main.TemplateWorkBook;
import com.fr.print.PrintSettingsAttrMark;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;

/**
 * Created by plough on 2018/3/5.
 */
public class ReportPrintSettingAction extends JWorkBookAction {

    public ReportPrintSettingAction(JWorkBook jwb) {
        super(jwb);
        this.setMenuKeySet(REPORT_APP_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon(IconManager.PRINT.getPath()));
        this.setSearchText(new ReportPrintSettingPane());
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    public void actionPerformed(ActionEvent evt) {
        final JWorkBook jwb = getEditingComponent();
        if (jwb == null) {
            return;
        }
        final TemplateWorkBook wbTpl = jwb.getTarget();
        PrintSettingsAttrMark printSettings = PrintSettingsAttrMark.loadFromFinebook(wbTpl, false);

        final ReportPrintSettingPane reportPrintSettingPane = new ReportPrintSettingPane();
        reportPrintSettingPane.populate(printSettings);
        BasicDialog dialog = reportPrintSettingPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                PrintSettingsAttrMark newPrintSettings = reportPrintSettingPane.updateBean();
                wbTpl.addAttrMark(newPrintSettings);
                jwb.fireTargetModified();
            }
        });
        dialog.setVisible(true);
    }

    private static final MenuKeySet REPORT_APP_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() { return 'P'; }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Print_Setting");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
