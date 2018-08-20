package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.ComparatorUtils;

import com.fr.report.cell.cellattr.core.SubReport;

import javax.swing.KeyStroke;

public class SubReportCellAction extends AbstractCellAction {

    public SubReportCellAction() {
        initAction();
    }

    public SubReportCellAction(ElementCasePane t) {
        super(t);
        initAction();
    }

    private void initAction() {
        this.setMenuKeySet(INSERT_SUB_REPORT);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon(
                "/com/fr/design/images/m_insert/subReport.png"));
    }

    public static final MenuKeySet INSERT_SUB_REPORT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'S';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Insert_Sub_Report");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    /**
     * equals 比较
     *
     * @param object
     * @return true false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SubReportCellAction)) {
            return false;
        }

        return ComparatorUtils.equals(this.getEditingComponent(), ((SubReportCellAction) object).getEditingComponent());
    }

    @Override
    public Class getCellValueClass() {
        return SubReport.class;
    }

}
