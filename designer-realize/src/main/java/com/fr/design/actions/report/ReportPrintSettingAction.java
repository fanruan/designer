package com.fr.design.actions.report;

import com.fr.base.IconManager;
import com.fr.base.print.PrintSettingsAttrMark;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.webattr.printsettings.ReportPrintSettingPane;
import com.fr.general.IOUtils;

import com.fr.main.impl.WorkBook;
import com.fr.report.core.ReportUtils;

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
        this.setSearchText(ReportPrintSettingPane.class.getName());
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
        final WorkBook wbTpl = jwb.getTarget();
        PrintSettingsAttrMark printSettings = ReportUtils.getPrintSettingsFromWorkbook(wbTpl);

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
            return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Print_Setting");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
