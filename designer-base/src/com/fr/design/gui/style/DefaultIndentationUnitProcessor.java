package com.fr.design.gui.style;

import com.fr.design.fun.impl.AbstractIndentationUnitProcessor;

/**
 * Created by zhouping on 2015/9/21.
 */
public class DefaultIndentationUnitProcessor extends AbstractIndentationUnitProcessor {
    private static final int INDENTATION_PADDING_ARG = 1;

    @Override
    public int getIndentationUnit() {
        return INDENTATION_PADDING_ARG;
    }

    /**
     * 默认处理系数是1
     * @param value 输入
     * @return 输出
     */
    @Override
    public int paddingUnitProcessor(int value){
        return (value / INDENTATION_PADDING_ARG);
    }

    /**
     * 默认处理系数是1，输入spinner的就是pt值
     * @param value 输入值
     * @return 输出
     */
    @Override
    public int paddingUnitGainFromSpinner(int value) {
        return (value * INDENTATION_PADDING_ARG);
    }

}