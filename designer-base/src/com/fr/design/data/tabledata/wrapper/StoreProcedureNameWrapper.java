package com.fr.design.data.tabledata.wrapper;

import com.fr.base.BaseUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.design.data.DesignTableDataManager;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.env.RemoteEnv;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public final class StoreProcedureNameWrapper implements TableDataWrapper {
    private ProcedureDataModel procedureDataModel;
    private String name;
    private StoreProcedure storeProcedure;
    private List<String> columnNameList;

    /**
     * @param name           存储过程本身名字
     * @param storeProcedure 存储过程
     */
    public StoreProcedureNameWrapper(String name, StoreProcedure storeProcedure) {
        this.name = name;
        this.storeProcedure = storeProcedure;
    }

    /**
     * 生成子节点
     *
     * @return 子节点
     */
    public ExpandMutableTreeNode[] load() {
        List<String> namelist = calculateColumnNameList();
        ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[namelist.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = new ExpandMutableTreeNode(namelist.get(i));
        }

        return res;
    }

    @Override
    public String getTableDataName() {
        return name;
    }

    public TableData getTableData() {
        return storeProcedure;
    }

    @Override
    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/data/store_procedure.png");
    }

    private void createStore(boolean needLoadingBar) {
        try {
            procedureDataModel = DesignTableDataManager.createLazyDataModel(storeProcedure, needLoadingBar)[0];
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    /**
     * 数据集执行结果返回的所有字段
     * <p/>
     * TODO:要不要加上Exception呢？个人感觉很有必要
     *
     * @return 字段
     */
    public List<String> calculateColumnNameList() {
        if (columnNameList != null) {
            return columnNameList;
        }
        columnNameList = new ArrayList<String>();
        Env env = FRContext.getCurrentEnv();
        if (env instanceof RemoteEnv) {
            try {
                createStore(false);
                columnNameList = Arrays.asList(procedureDataModel.getColumnName());
            } catch (Exception e) {
                FRContext.getLogger().errorWithServerLevel(e.getMessage(), e);
            }

        } else {
            if (procedureDataModel == null) {
                createStore(false);
            }
            if (procedureDataModel != null) {
                columnNameList = Arrays.asList(procedureDataModel.getColumnName());
            }
        }
        return columnNameList;
    }

    /**
     * 预览数据集
     */
    public void previewData() {
        if (procedureDataModel == null) {
            createStore(true);
        }
        PreviewTablePane.previewStoreData(procedureDataModel);

    }

    /**
     * 预览数据集，带有显示值和实际值的标记结果
     *
     * @param keyIndex   显示值Index
     * @param valueIndex 实际值index
     */
    public void previewData(int keyIndex, int valueIndex) {
        if (procedureDataModel == null) {
            createStore(true);
        }
        PreviewTablePane.previewStoreData(procedureDataModel, keyIndex, valueIndex);
    }

    /**
     * 是否异常
     *
     * @return 异常返回true
     */
    public boolean isUnusual() {
        return false;
    }

    public StoreProcedure getStoreProcedure() {
        return storeProcedure;
    }

}