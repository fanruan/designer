package com.fr.design.fun;

import com.fr.data.impl.Connection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.stable.fun.Level;

/**
 * @author : richie
 * @since : 8.0
 */
public interface ConnectionProvider extends Level {

    public static final String XML_TAG = "ConnectionProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 数据连接弹出菜单的名字
     * @return 名字
     */
    public String nameForConnection();

    /**
     * 数据连接弹出菜单的图标
     * @return 图标路径
     */
    public String iconPathForConnection();

    /**
     * 数据连接的类型
     * @return 连接类型
     */
    public Class<? extends com.fr.data.impl.Connection> classForConnection();

    /**
     * 数据连接的设计界面
     * @return 设计界面
     */
    public Class<? extends BasicBeanPane<? extends Connection>> appearanceForConnection();
}