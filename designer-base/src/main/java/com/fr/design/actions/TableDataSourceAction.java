package com.fr.design.actions;

import javax.swing.SwingUtilities;

import com.fr.base.BaseUtils;
import com.fr.design.data.DesignTableDataManager;
import com.fr.data.TableDataSource;
import com.fr.design.data.datapane.ReportTableDataPane;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.DesignModelAdapter;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;

import java.util.HashMap;
import java.util.Map;

/*
 * richer:包括报表和表单的数据集  && 图表的数据集
 */
public class TableDataSourceAction extends TemplateComponentAction<JTemplate<?, ?>> implements ResponseDataSourceChange {
    public TableDataSourceAction(JTemplate<?, ?> t) {
        super(t);
        this.setMenuKeySet(KeySetUtils.TEMPLATE_TABLE_DATA_SOURCE);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_web/datasource.png"));
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        final TableDataSource tds = this.getEditingComponent().getTarget();

        final ReportTableDataPane tableDataPane = new ReportTableDataPane() {
            public void complete() {
                populate(tds);
            }
        };
        final BasicDialog reportTableDataDialog = tableDataPane.showLargeWindow(SwingUtilities.getWindowAncestor(this.getEditingComponent()), null);
        reportTableDataDialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                if (!tableDataPane.isNamePermitted()) {
                    reportTableDataDialog.setDoOKSucceed(false);
                    return;
                }
                DesignModelAdapter.getCurrentModelAdapter().renameTableData(tableDataPane.getDsNameChangedMap());
                tableDataPane.update(tds);
                TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
                TableDataSourceAction.this.getEditingComponent().fireTargetModified();
                fireDSChanged(tableDataPane.getDsNameChangedMap());
            }
        });
        reportTableDataDialog.setVisible(true);
        return false;
    }

    // TODO ALEX_SEP 同JWorkBookAction,也不知道这个undo该如何处理

    /**
     * 撤销
     */
    public void prepare4Undo() {
        //do nothing
    }

    /**
     * 响应数据集改变
     */
    public void fireDSChanged() {
        fireDSChanged(new HashMap<String, String>());
    }

    /**
     * 响应数据集改变
     *
     * @param map 地图
     */
    public void fireDSChanged(Map<String, String> map) {
        DesignTableDataManager.fireDSChanged(map);
    }
}