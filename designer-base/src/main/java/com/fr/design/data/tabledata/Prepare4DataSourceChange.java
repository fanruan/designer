package com.fr.design.data.tabledata;

/**
 * Created with IntelliJ IDEA.
 * User: 小灰灰
 * Date: 13-8-5
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
public interface Prepare4DataSourceChange {
    /**
     *注册listener,相应数据集改变
     */
    public void registerDSChangeListener ();
}