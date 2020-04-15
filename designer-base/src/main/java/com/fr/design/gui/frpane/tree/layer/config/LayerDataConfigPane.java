package com.fr.design.gui.frpane.tree.layer.config;

import com.fr.base.TableData;
import com.fr.data.impl.TableDataDictionary;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.present.dict.TableDataDictPane;
import com.fr.form.ui.tree.LayerConfig;
import com.fr.form.ui.tree.LayerDependence;
import com.fr.stable.StringUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by juhaoyu on 16/9/13.
 */
public class LayerDataConfigPane extends BasicBeanPane<LayerConfig> {

    /**
     * 数据集数据字典选择panel
     */
    private TableDataDictPane tableDataDictPane;

    /**
     * 与父级关联的字段选择
     */
    private LayerDependenceSettingPane dependenceSettingPane;


    /**
     * 当前用户正在修改的LayerData
     */
    private LayerConfig layerConfig;

    public LayerDataConfigPane() {
        //初始化组件及布局
        this.tableDataDictPane = new TableDataDictPane();
        this.dependenceSettingPane = new LayerDependenceSettingPane(tableDataDictPane);
        this.setLayout(new BorderLayout(2, 2));
        this.add(tableDataDictPane, BorderLayout.NORTH);
        this.add(dependenceSettingPane, BorderLayout.CENTER);
    }

    /**
     * 1.切换层级时
     *
     * @param layerConfig
     */
    @Override
    public void populateBean(LayerConfig layerConfig) {

        if (layerConfig != null) {
            this.layerConfig = layerConfig;
            TableDataDictionary ta = layerConfig.getDictionary();
            this.tableDataDictPane.populateBean(ta);
            this.dependenceSettingPane.populate(layerConfig.getIndex(), layerConfig.getDependenceList());
        }

    }

    @Override
    public LayerConfig updateBean() {

        if (layerConfig == null) {
            return null;

        }
        //从下层panel中读取数据
        TableData tableData = tableDataDictPane.updateBean().getTableData();
        TableDataWrapper wrapper = tableDataDictPane.tableDataNameComboBox.getSelectedItem();
        List<String> columnNames;
        if (wrapper != null) {
            columnNames = wrapper.calculateColumnNameList();
        } else {
            columnNames = new ArrayList<>();
        }
        TableDataDictionary dataDictionary = tableDataDictPane.updateBean();
        String viewColStr = dataDictionary.getValueColumnName();
        String modelColStr = dataDictionary.getKeyColumnName();
        int viewCol = columnNames.indexOf(viewColStr);
        int modelCol = columnNames.indexOf(modelColStr);
        if (StringUtils.EMPTY.equals(viewColStr)) {
            viewCol = dataDictionary.getValueColumnIndex();
        }
        if (StringUtils.EMPTY.equals(modelColStr)) {
            modelCol = dataDictionary.getKeyColumnIndex();
        }
        TableDataDictionary dictionary = tableDataDictPane.updateBean();
        //将数据设置到当前正在修改的layerData中
        this.layerConfig.setDictionary(dictionary);
        this.layerConfig.setModelColumn(modelCol);
        this.layerConfig.setViewColumn(viewCol);
        this.layerConfig.setTableData(tableData);
        //添加依赖
        java.util.List<LayerDependence> dependenceList = dependenceSettingPane.updateLayerDependence();
        layerConfig.getDependenceList().clear();
        layerConfig.addAll(dependenceList);
        return layerConfig;
    }

    @Override
    protected String title4PopupWindow() {

        return "Layer Data Config Panel";
    }


}
