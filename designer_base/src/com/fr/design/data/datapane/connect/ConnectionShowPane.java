package com.fr.design.data.datapane.connect;

import com.fr.file.DatasourceManagerProvider;

/**
 * Created by yaoh.wu on 2017/4/22.
 * 数据链接显示面板
 */
public interface ConnectionShowPane {
    void update(DatasourceManagerProvider datasourceManager);

    void populate(DatasourceManagerProvider datasourceManager);

    void setSelectedIndex(int index);
}
