/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.data.datapane;

import com.fr.data.impl.*;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.data.tabledata.tabledatapane.*;
import com.fr.design.fun.TableDataCreatorProvider;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : richie
 * @since : 8.0
 */
public class TableDataCreatorProducer {

    private static TableDataCreatorProducer ourInstance = new TableDataCreatorProducer();

    public static TableDataCreatorProducer getInstance() {
        return ourInstance;
    }

    private TableDataCreatorProducer() {

    }

    public TableDataNameObjectCreator[] createReportTableDataCreator() {
        TableDataNameObjectCreator dataBase = new TableDataNameObjectCreator(Inter.getLocText("DS-Database_Query"),
                "ds",
                "/com/fr/design/images/data/database.png", DBTableData.class, DBTableDataPane.class);
        TableDataNameObjectCreator ds_Class = new TableDataNameObjectCreator(Inter.getLocText("DS-Class"),
                "/com/fr/design/images/data/source/classTableData.png", ClassTableData.class, ClassTableDataPane.class);
        TableDataNameObjectCreator table = new TableDataNameObjectCreator(Inter.getLocText("DS-Embedded_TableData"),
                "Embedded",
                "/com/fr/design/images/data/dataTable.png", EmbeddedTableData.class, EmbeddedTableDataPane.class);
        TableDataNameObjectCreator multiTable = new TableDataNameObjectCreator(Inter.getLocText("DS-Relation_TableData"),
                "Multi",
                "/com/fr/design/images/data/multi.png", ConditionTableData.class, MultiTDTableDataPane.class) {
            public boolean isNeedParameterWhenPopulateJControlPane() {
                return true;
            }
        };
        TableDataNameObjectCreator fileTable = new TableDataNameObjectCreator(Inter.getLocText(new String[]{"File", "DS-TableData"}),
                "File",
                "/com/fr/design/images/data/file.png", FileTableData.class, FileTableDataSmallHeightPane.class);
        TableDataNameObjectCreator treeTable = new TableDataNameObjectCreator(Inter.getLocText(new String[]{"Tree", "DS-TableData"}),
                "Tree",
                "/com/fr/design/images/data/tree.png", RecursionTableData.class, TreeTableDataPane.class) {
            public boolean isNeedParameterWhenPopulateJControlPane() {
                return true;
            }
        };
        TableDataNameObjectCreator storeProcedure = new TableDataNameObjectCreator(Inter.getLocText("Datasource-Stored_Procedure"),
                "Proc",
                "/com/fr/design/images/data/store_procedure.png",
                StoreProcedure.class, ProcedureDataPane.class) {
            public boolean shouldInsertSeparator() {
                return true;
            }
        };
        TableDataNameObjectCreator[] creators = new TableDataNameObjectCreator[]{dataBase, ds_Class, table, fileTable, storeProcedure, multiTable, treeTable};
        return merge(creators, ExtraDesignClassManager.getInstance().getReportTableDataCreators());
    }

    public TableDataNameObjectCreator[] createServerTableDataCreator() {
        TableDataNameObjectCreator dataBase = new TableDataNameObjectCreator(Inter.getLocText("DS-Database_Query"), "/com/fr/design/images/data/dock/serverdatabase.png", DBTableData.class,
                DBTableDataPane.class);
        TableDataNameObjectCreator ds_Class = new TableDataNameObjectCreator(Inter.getLocText("DS-Class"), "/com/fr/design/images/data/dock/serverclasstabledata.png", ClassTableData.class,
                ClassTableDataPane.class);
        TableDataNameObjectCreator table = new TableDataNameObjectCreator(Inter.getLocText("DS-Embedded_TableData"), "/com/fr/design/images/data/dock/serverdatatable.png", EmbeddedTableData.class,
                EmbeddedTableDataPane.class);
        TableDataNameObjectCreator fileTable = new TableDataNameObjectCreator(Inter.getLocText(new String[]{"File", "DS-TableData"}), "/com/fr/design/images/data/file.png", FileTableData.class,
                FileTableDataSmallPane.class);

        TableDataNameObjectCreator treeTable = new TableDataNameObjectCreator(Inter.getLocText(new String[]{"Tree", "DS-TableData"}), "/com/fr/design/images/data/tree.png",
                RecursionTableData.class, GlobalTreeTableDataPane.class) {
            public boolean isNeedParameterWhenPopulateJControlPane() {
                return true;
            }
        };
        TableDataNameObjectCreator multiTable = new TableDataNameObjectCreator(Inter.getLocText("DS-Relation_TableData"), "/com/fr/design/images/data/multi.png",
                ConditionTableData.class, GlobalMultiTDTableDataPane.class) {
            public boolean isNeedParameterWhenPopulateJControlPane() {
                return true;
            }
        };
        TableDataNameObjectCreator storeProcedure = new TableDataNameObjectCreator(Inter.getLocText("Datasource-Stored_Procedure"), "/com/fr/design/images/data/store_procedure.png",
                StoreProcedure.class, ProcedureDataPane.class) {
            public boolean shouldInsertSeparator() {
                return true;
            }
        };

        TableDataNameObjectCreator[] creators = new TableDataNameObjectCreator[]{dataBase, ds_Class, table, fileTable, storeProcedure, multiTable, treeTable};

        TableDataCreatorProvider creatorProvider = ExtraDesignClassManager.getInstance().getTableDataCreatorProvider();
        if (creatorProvider != null) {
            return creatorProvider.produceServerTableDataCreator(creators);
        }
        return merge(creators, ExtraDesignClassManager.getInstance().getServerTableDataCreators());
    }

    private TableDataNameObjectCreator[] merge(TableDataNameObjectCreator[] creators, TableDataNameObjectCreator[] extraCreators) {
        if (ArrayUtils.isEmpty(extraCreators)) {
            return creators;
        }
        List<TableDataNameObjectCreator> list = new ArrayList<>();
        List<Integer> coverIndexList = new ArrayList<>();
        List<TableDataNameObjectCreator> coverCreators = new ArrayList<>();
        for (TableDataNameObjectCreator ex : extraCreators) {
            int index = ArrayUtils.indexOf(creators, ex);
            // 说明已经存在了一个同类型的数据集，需要用插件的覆盖掉原先的
            if (index != ArrayUtils.INDEX_NOT_FOUND) {
                // 需要替换到列表里的
                coverIndexList.add(index);
                coverCreators.add(ex);
            } else {
                // 需要添加到列表中去的
                list.add(ex);
            }
        }
        for (int i = 0; i < coverIndexList.size(); i ++) {
            creators[coverIndexList.get(i)] = coverCreators.get(i);
        }
        return (TableDataNameObjectCreator[])ArrayUtils.addAll(creators, list.toArray(new TableDataNameObjectCreator[list.size()]));
    }
}