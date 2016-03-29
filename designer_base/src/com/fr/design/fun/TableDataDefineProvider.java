package com.fr.design.fun;

import com.fr.base.TableData;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.stable.fun.Level;

/**
 * @author : richie
 * @since : 7.1.1
 * 自定义报表数据集界面接口，单独存在的一个个数据集
 */
public interface TableDataDefineProvider extends Level {

    public static final String XML_TAG = "TableDataDefineProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 自定义的数据集设置界面所对应的数据集类型
     * @return 数据集的类型
     */
    public Class<? extends TableData> classForTableData();

    /**
     * 自定义数据集设置界面所对应的初始化数据集类型，在一种数据集有多个实现的时候有效
     * @return 数据集类型
     */
    public Class<? extends TableData> classForInitTableData();

    /**
     * 自定义的数据集设置界面所对应的界面类型
     * @return 数据集界面类型
     */
    public Class<? extends AbstractTableDataPane> appearanceForTableData();

    /**
     * 自定义数据集设置界面在菜单上的现实名字
     * @return 名字
     */
    public String nameForTableData();

    /**
     * 自定义数据集在新建的时候名字前缀
     * @return 名字前缀
     */
    public String prefixForTableData();

    /**
     * 自定义数据集在菜单上现实的图标
     * @return 图标
     */
    public String iconPathForTableData();
}