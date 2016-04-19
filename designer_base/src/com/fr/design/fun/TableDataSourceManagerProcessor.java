package com.fr.design.fun;

import com.fr.design.DesignModelAdapter;
import com.fr.design.data.datapane.TableDataSourceOP;
import com.fr.design.data.datapane.TableDataTree;
import com.fr.design.menu.ShortCut;
import com.fr.stable.fun.Level;

/**
 * 自定义的模板（服务器）数据集的树样式接口  el:分组样式
 * Coder: zack
 * Date: 2016/4/18
 * Time: 9:04
 */
public interface TableDataSourceManagerProcessor extends Level {
    String XML_TAG = "TableDataSourceManagerProcessor";

    int CURRENT_LEVEL = 1;
    /**
     * 创建数据集的树操作
     * @param tc 设计模式
     * @return 数据集树操作
     */
    TableDataSourceOP createTableDataSourceOP(DesignModelAdapter<?, ?> tc);

    /**
     * 创建相应的tabledatatree
     * @return tabledatatree
     */
    TableDataTree createUserObjectJtree();

    /**
     * 返回面板新增的action(el:GroupAction)
     * @return (shortcut)action数组
     */
    ShortCut[] getShortCuts(TableDataTree dataTree);

}
