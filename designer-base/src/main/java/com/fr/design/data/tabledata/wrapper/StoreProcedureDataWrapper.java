package com.fr.design.data.tabledata.wrapper;

import com.fr.base.BaseUtils;
import com.fr.base.TableData;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.data.operator.DataOperator;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.iprogressbar.AutoProgressBar;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * <code>StoreProcedureDataWrappe</code> ：存储过程的一个返回数据集,不是它本身。<br>
 * Oracle数据库肯定有它。SQL SERVER就不一定有了,大部分情况下都没有.
 * <p/>
 * <code>StoreProcedureNameWrappe</code> ：存储过程本身的返回数据集，是它本身。<br>
 * Oracle数据库有它，但其实没必要，它就是Oracle所有返回数据集的第一个。SQL SERVER肯定有这个东西。
 * <code>StoreProcedureNameWrappe</code>
 * 实际就是：以前我们在新建数据集时，从左边的列表中拖动一个存储过程到右边的SQL面板，
 * 得到的SQL语句执行后返回的数据集。又因为结果可能得到多个数据集，所以默认用第一个
 *
 * @author zhou
 * @since 2012-4-12上午10:29:15
 */
public final class StoreProcedureDataWrapper implements TableDataWrapper {
    public static final int PREVIEW_ALL = 0;
    public static final int PREVIEW_ONE = 1;
    public static AutoProgressBar loadingBar;

    private ProcedureDataModel procedureDataModel;
    private String dsName;
    private String storeprocedureName;
    private StoreProcedure storeProcedure;
    private List<String> columnNameList;
    private AutoProgressBar connectionBar;
    private ProcedureDataModel[] dataModels;
    private SwingWorker worker;
    private BasicDialog dialog;
    private int previewModel;

    public StoreProcedureDataWrapper(StoreProcedure storeProcedure, String storeprocedureName, String dsName) {
        this(storeProcedure, storeprocedureName, dsName, true);
    }


    /**
     * @param dsName             存储过程一个返回数据集的名字
     * @param storeProcedure     存储过程
     * @param storeprocedureName 存储过程的名字(某些情况下可以为空)
     */
    public StoreProcedureDataWrapper(StoreProcedure storeProcedure, String storeprocedureName, String dsName, boolean needLoad) {
        this.dsName = dsName;
        this.storeProcedure = storeProcedure;
        this.storeProcedure.setCalculating(false);
        this.storeprocedureName = storeprocedureName;
        if (needLoad) {
            setWorker();
        }
        dialog = PreviewTablePane.getInstance().getDialog();
        dialog.addDialogActionListener(new DialogActionAdapter() {
            public void doOk() {
                getWorker().cancel(true);
            }

            public void doCancel() {
                getWorker().cancel(true);
            }
        });
        loadingBar = new AutoProgressBar(dialog, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Loading_Data"), "", 0, 100) {
            public void doMonitorCanceled() {
                getDialog().setVisible(false);
                getWorker().cancel(true);
            }
        };
    }

    /**
     * 数据集执行结果返回的所有字段
     *
     * @return 数据集执行结果返回的所有字段
	 * 
	 *
	 * @date 2014-12-3-下午7:43:17
	 * 
	 */
    public List<String> calculateColumnNameList() {
        if (columnNameList != null) {
            return columnNameList;
        }
        if (!createStore(false)) {
            FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Engine_No_TableData"));
            return new ArrayList<String>();
        }
        columnNameList = Arrays.asList(procedureDataModel.getColumnName());
        return columnNameList;
    }

    /**
     * 生成子节点
     *
     * @return 节点数组
	 * 
	 *
	 * @date 2014-12-3-下午7:06:47
	 * 
	 */
    public ExpandMutableTreeNode[] load() {
        List<String> namelist;
        if (storeProcedure.isCalculating()) {
            namelist = Arrays.asList(new String[0]);
        } else {
            namelist = calculateColumnNameList();
        }
        ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[namelist.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = new ExpandMutableTreeNode(namelist.get(i));
        }

        return res;
    }

    private boolean createStore(boolean needLoadingBar) {
        try {
            dataModels = DesignTableDataManager.createLazyDataModel(storeProcedure, needLoadingBar);
            if (dataModels == null || dataModels.length == 0) {
                return false;
            }
            for (int i = 0; i < dataModels.length; i++) {
                if (ComparatorUtils.equals(this.dsName, storeprocedureName + "_" + dataModels[i].getName())) {
                    procedureDataModel = dataModels[i];
                    break;
                }
            }
            return true;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/data/store_procedure.png");
    }

    /**
	 * 预览数据
	 * 
	 * @param previewModel 预览模式, 全部还是一个
	 * 
	 *
	 * @date 2014-12-3-下午7:05:50
	 * 
	 */
    public void previewData(final int previewModel) {
        this.previewModel = previewModel;
        new SwingWorker() {

            protected Object doInBackground() throws Exception {
                loadingBar.close();
                PreviewTablePane.resetPreviewTable();
                dialog.setVisible(true);
                return null;
            }
        }.execute();
        connectionBar = new AutoProgressBar(dialog, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Now_Create_Connection"), "", 0, 100) {
            public void doMonitorCanceled() {
                connectionBar.close();
                worker.cancel(true);
            }
        };
        worker.execute();
    }

    private void setWorker() {
        worker = new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                connectionBar.start();
                boolean status = DataOperator.getInstance().testConnection(((StoreProcedure) getTableData()).getDatabaseConnection());
                if (!status) {
                    connectionBar.close();
                    // bug 61345 预览失败时，关闭窗口
                    dialog.setVisible(false);
                    throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database_Connection_Failed"));
                }
                connectionBar.close();
                storeProcedure.resetDataModelList();
                createStore(true);
                return null;
            }

            public void done() {
                try {
                    get();
                    loadingBar.close();
                    dialog.setVisible(false);
                    switch (previewModel) {
                        case StoreProcedureDataWrapper.PREVIEW_ALL:
                            PreviewTablePane.previewStoreDataWithAllDs(dataModels);
                            break;
                        case StoreProcedureDataWrapper.PREVIEW_ONE:
                            previewData();
                            break;
                    }
                } catch (Exception e) {
                    if (!(e instanceof CancellationException)) {
                        FineLoggerFactory.getLogger().error(e.getMessage(), e);
                        FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), e.getMessage());
                    }
                    loadingBar.close();
                }
            }
        };
    }

    private BasicDialog getDialog() {
        return this.dialog;
    }

    private SwingWorker getWorker() {
        return this.worker;
    }

    // august:这个只是预览返回的一个数据集

    /**
     * 预览返回的一个数据集
     * 
	 *
	 * @date 2014-12-3-下午7:42:53
	 * 
	 */
    public void previewData() {
        previewData(-1, -1);
    }

    // august:这个只是预览返回的一个数据集

    /**
     * 预览返回的一个数据集，带有显示值和实际值的标记结果
     * 
	 * @param keyIndex 实际值
	 * @param valueIndex 显示值
	 * 
	 *
	 * @date 2014-12-3-下午7:42:27
	 * 
	 */
    public void previewData(final int keyIndex, final int valueIndex) {
        PreviewTablePane.previewStoreData(procedureDataModel, keyIndex, valueIndex);
    }


    /**
     * 预览返回的所有数据集，只有在编辑存储过程时才用到
     */
    public void previewAllTable() {
        if (procedureDataModel == null) {
            if (!createStore(true)) {
                return;
            }
        }
        PreviewTablePane.previewStoreDataWithAllDs(dataModels);
    }

    @Override
    public String getTableDataName() {
        return dsName;
    }

    public TableData getTableData() {
        return storeProcedure;
    }

    public String getStoreprocedureName() {
        return storeprocedureName;
    }

    /**
     * 是否异常
     *
     * @return 是否异常
     */
    public boolean isUnusual() {
        return false;
    }

    public boolean equals(Object obj) {
        return obj instanceof StoreProcedureDataWrapper
                && ComparatorUtils.equals(this.dsName, ((StoreProcedureDataWrapper) obj).getTableDataName())
                && ComparatorUtils.equals(this.storeProcedure, ((StoreProcedureDataWrapper) obj).getTableData())
                && ComparatorUtils.equals(this.storeprocedureName, ((StoreProcedureDataWrapper) obj).getStoreprocedureName());

    }

}
