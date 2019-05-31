package com.fr.design.i18n.impl;

import com.fr.design.i18n.ActionType;
import com.fr.general.CloudCenter;

import java.util.List;

public class TaiWanLocaleAction extends AbstractDefaultLocaleAction {

    @Override
    protected void init() {
        super.init();
        urls.put(ActionType.VIDEO, CloudCenter.getInstance().acquireUrlByKind("bbs.video.zh_TW"));
        urls.put(ActionType.ACTIVATION_CODE, CloudCenter.getInstance().acquireUrlByKind("frlogin.tw"));
    }

    @Override
    public void addAction(List list, Object action) {
        list.add(action);
    }
}
