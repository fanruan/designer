package com.fr.design.fun.impl;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.CellExpandAttrProvider;
import com.fr.report.cell.TemplateCellElement;
import com.fr.stable.fun.mark.API;

/**
 * @author yaohwu
 * created by yaohwu at 2020/4/26 16:08
 */
@API(level = CellExpandAttrProvider.CURRENT_LEVEL)
public class AbstractCellExpandAttrProvider implements CellExpandAttrProvider {

    /**
     * 当前接口的API等级,用于判断是否需要升级插件
     *
     * @return API等级
     */
    @Override
    public int currentAPILevel() {
        return CellExpandAttrProvider.CURRENT_LEVEL;
    }

    /**
     * 获取当前provider的标记（可以使用类路径保证唯一）以避免provider的重复加载
     *
     * @return 当前provider的标记
     */
    @Override
    public String mark4Provider() {
        return null;
    }

    @Override
    public BasicBeanPane<TemplateCellElement> createPanel() {
        return null;
    }
}
