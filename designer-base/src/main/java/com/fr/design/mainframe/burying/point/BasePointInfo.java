package com.fr.design.mainframe.burying.point;

import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLWriter;

import java.util.List;
import java.util.Map;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2020-02-21
 */
public interface BasePointInfo extends XMLReadable, XMLWriter {

    /**
     * 重置埋点的未编辑天数
     */
    void resetIdleDayCount();

    /**
     * 增加一天埋点的未编辑天数
     */
    void addIdleDayCountByOne();

    /**
     * 上传前判断该埋点，是否需要被上传，或者删除，或者什么都不做。
     */
    void selectPoint(List<String> removeList, List<String> sendList);

    /**
     * 获取要上传的内容，key→上传路径，value→上传内容
     */
    Map<String, String> getSendInfo();
}
