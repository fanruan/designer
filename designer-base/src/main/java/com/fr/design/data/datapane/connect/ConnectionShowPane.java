package com.fr.design.data.datapane.connect;

import com.fr.file.ConnectionConfig;

/**
 * Created by yaoh.wu on 2017/4/22.
 * 数据链接显示面板
 */
public interface ConnectionShowPane {
    void update(ConnectionConfig connectionConfig);

    void populate(ConnectionConfig connectionConfig);

    void setSelectedIndex(int index);
}
