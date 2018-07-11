package com.fr.design.data.tabledata;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 小灰灰
 * Date: 13-8-5
 * Time: 上午11:24
 * To change this template use File | Settings | File Templates.
 */
public interface ResponseDataSourceChange {

    /**
     * 响应数据集改变
     */
    public void fireDSChanged();

    /**
     * 响应数据集改变
     */
    public void fireDSChanged(Map<String, String> map);


}