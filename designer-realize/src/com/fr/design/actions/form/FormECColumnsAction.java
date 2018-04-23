package com.fr.design.actions.form;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.form.FormElementCasePaneDelegate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.ReportColumnsPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.report.stable.WorkSheetAttr;
import com.fr.report.worksheet.FormElementCase;

public class FormECColumnsAction extends AbastractFormECAction<FormElementCasePaneDelegate>{

    public FormECColumnsAction(FormElementCasePaneDelegate t) {
        super(t);
        this.setMenuKeySet(KeySetUtils.EC_COLUMNS);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/form/toolbar/ec_columns.png"));
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        final FormElementCasePaneDelegate jws = this.getEditingComponent();
        if (jws == null) {
            return false;
        }
        final FormElementCase elementCase = jws.getTarget();

        final ReportColumnsPane cPane = new ReportColumnsPane();
        WorkSheetAttr attr = elementCase.getWorkSheetAttr();
        int rowCount = elementCase.getRowCount();
        int colCount = elementCase.getColumnCount();
        cPane.populate(attr, rowCount, colCount);
        cPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            @Override
            public void doOk() {
                // bug:64173 重新生成一个workSheetAttr,否则会受到之前属性的干扰
                WorkSheetAttr attr = new WorkSheetAttr();
                elementCase.setWorkSheetAttr(attr);
                cPane.update(attr);
                jws.fireTargetModified();
            }
        }).setVisible(true);
        return false;
    }
}