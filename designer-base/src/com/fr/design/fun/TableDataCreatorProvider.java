/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.fun;

import com.fr.design.data.datapane.TableDataNameObjectCreator;

import java.util.Map;

/**
 * @author : richie
 * @since : 8.0
 * @deprecated
 * @see com.fr.design.fun.TableDataDefineProvider
 * @see com.fr.design.fun.ServerTableDataDefineProvider
 */
public interface TableDataCreatorProvider {
    public static final String XML_TAG = "TableDataCreatorProvider";

    /**
     * 获取报表数据集设计界面类型数组
     * @param creators 内置的数据集设计界面类型数组
     * @return 完整的报表数据集设计界面类型数组
     */
    public TableDataNameObjectCreator[] produceReportTableDataCreator(TableDataNameObjectCreator[] creators);

    /**
     * 获取服务器数据集设计界面类型数组
     * @param creators 内置的数据集设计界面类型数组
     * @return 完整的服务器数据集设计界面类型数组
     */
    public TableDataNameObjectCreator[] produceServerTableDataCreator(TableDataNameObjectCreator[] creators);

    /**
     * 需要将额外使用的数据集以及其设计界面做一个记录
     * @return 额外记录对应
     */
    public Map<String, TableDataNameObjectCreator> registerMap();
}