package com.fr.design.mainframe.burying.point;

import java.util.List;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2020-02-21
 */
public abstract class AbstractPointInfo implements BasePointInfo {

    protected int idleDayCount;  // 到现在为止，埋点闲置的天数

    @Override
    public void resetIdleDayCount() {
        this.idleDayCount = 0;
    }

    @Override
    public void addIdleDayCountByOne() {
        this.idleDayCount += 1;
    }

    /**
     * 上传前判断该埋点，是否需要被上传，或者删除，或者什么都不做。
     */
    @Override
    public void selectPoint(List<String> removeList, List<String> sendList) {
        //埋点还未完成，直接返回
        if (!isComplete()) {
            return;
        }
        //属于测试模板，直接删除，否则发送信息
        if (isTestTemplate()) {
            removeList.add(key());
        } else {
            sendList.add(key());
        }
    }

    /**
     * 是否为测试模板
     */
    protected abstract boolean isTestTemplate();

    /**
     * 是否已经制作完成
     */
    protected abstract boolean isComplete();

    /**
     * 埋点记录的主键
     */
    protected abstract String key();
}
