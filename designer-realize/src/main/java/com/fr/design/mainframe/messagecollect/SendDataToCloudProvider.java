package com.fr.design.mainframe.messagecollect;

import com.fr.stable.query.data.DataList;

/**
 * @author alex sung
 * @date 2019/3/22
 */
public interface SendDataToCloudProvider {

    /**
     * 获取要回传的数据
     * @param currentTime 当前时间
     * @param lastTime 上次回传时间
     * @param tClass 埋点对象类型
     * @throws Exception 取数过程中可能的异常
     */
    <T> void getData(long currentTime, long lastTime, Class<T> tClass) throws Exception;

    /**
     * @param points 从swift获取的埋点数据
     * @throws Exception 解析或存储临时文件时可能的异常
     */
    <T> void dealWithData(DataList<T> points) throws Exception;

    /**
     * 回传Zip到云中心
     */
    void sendToCloudCenter();
}
