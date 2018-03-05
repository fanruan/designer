package com.fr.design.data.datapane;

import com.fr.data.TableDataSource;
import com.fr.file.TableDataConfig;

import javax.swing.*;
import java.util.Map;

/**
 * Coder: zack
 * Date: 2016/5/18
 * Time: 10:13
 */
public interface TableDataPaneController {
    /**
     * 获取数据集名字变更集合
     *
     * @return
     */
    Map<String, String> getDsNameChangedMap();

    /**
     * 数据集重命名
     *
     * @param oldName
     * @param newName
     */
    void rename(String oldName, String newName);

    /**
     * 是否合法
     * @throws Exception
     */
    void checkValid() throws Exception;

    /**
     * 名字是否允许
     * @return
     */
    boolean isNamePermitted();

    void populate(TableDataConfig tableDataConfig);

    void update(TableDataConfig tableDataConfig);

    void populate(TableDataSource datasourceManagerProvider);

    void update(TableDataSource datasourceManagerProvider);

    /**
     * 设置选中项
     * @param index
     */
    void setSelectedIndex(int index);

    /**
     * 返回当前数据集面板
     * @return
     */
    JPanel getPanel();

}
