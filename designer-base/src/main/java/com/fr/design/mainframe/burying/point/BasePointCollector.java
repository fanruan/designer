package com.fr.design.mainframe.burying.point;

import com.fr.design.mainframe.template.info.TemplateProcessInfo;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLWriter;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2020-02-21
 */
public interface BasePointCollector extends XMLReadable, XMLWriter {

    /**
     * 发送埋点信息到服务器
     */
    public void sendPointInfo();

    /**
     * 保存埋点的信息到本地
     */
    public void collectInfo(String templateID, String originID, TemplateProcessInfo processInfo, int timeConsume);
}
