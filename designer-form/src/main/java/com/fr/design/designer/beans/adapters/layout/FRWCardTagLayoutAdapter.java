package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;

/**
 * 针对tabpane的布局适配器
 * Created by kerry on 2019-12-10
 */
public class FRWCardTagLayoutAdapter extends AbstractLayoutAdapter {
    public FRWCardTagLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    /**
     * 对于这种布局方式，不允许其他组件添加
     *
     * @param creator
     * @param x
     * @param y
     */
    @Override
    protected void addComp(XCreator creator, int x, int y) {

    }

    /**
     * 对于这种布局方式，不允许其他组件添加
     *
     * @param creator 组件
     * @param x       添加的位置x，该位置是相对于container的
     * @param y       添加的位置y，该位置是相对于container的
     * @return
     */
    @Override
    public boolean accept(XCreator creator, int x, int y) {
        return false;
    }
}
