package com.fr.design.fun;

import com.fr.data.impl.Connection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.stable.fun.mark.Mutable;

/**
 * @author : richie
 * @since : 8.0
 */
public interface ConnectionProvider extends Mutable {

    String XML_TAG = "ConnectionProvider";

    // 2016-12-14 1 -> 2 , 增加connection.feature方法导致不兼容.
    int CURRENT_LEVEL = 2;

    /**
     * 数据连接弹出菜单的名字
     *
     * @return 名字
     */
    String nameForConnection();

    /**
     * 数据连接弹出菜单的图标
     *
     * @return 图标路径
     */
    String iconPathForConnection();

    /**
     * 数据连接的类型
     *
     * @return 连接类型
     */
    Class<? extends com.fr.data.impl.Connection> classForConnection();

    /**
     * 数据连接的设计界面
     *
     * @return 设计界面
     */
    Class<? extends BasicBeanPane<? extends Connection>> appearanceForConnection();
}