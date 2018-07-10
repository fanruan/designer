package com.fr.design.fun;

import com.fr.design.DesignModelAdapter;
import com.fr.design.data.BasicTableDataTreePane;
import com.fr.design.data.datapane.TableDataPaneController;
import com.fr.design.menu.ShortCut;
import com.fr.stable.fun.mark.Immutable;

/**
 * 自定义的模板（服务器）数据集的树样式接口  el:分组样式
 * Coder: zack
 * Date: 2016/4/18
 * Time: 9:04
 */
public interface TableDataPaneProcessor extends Immutable {
    String XML_TAG = "TableDataPaneProcessor";

    int CURRENT_LEVEL = 1;
    /**
     * 创建数据集面板
     * @return 数据集面板
     */
    BasicTableDataTreePane createTableDataTreePane(DesignModelAdapter<?, ?> tc);

    /**
     * 服务器数据集面板(模板数据集面板)
     * @return 服务器数据集面板
     */
    TableDataPaneController createServerTableDataPane(DesignModelAdapter<?, ?> tc);

    /**
     * 自定义服务器数据集菜单项
     * @return
     */
    ShortCut createServerTDAction();
}
