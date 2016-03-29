package com.fr.design.fun.impl;

import com.fr.design.fun.IndentationUnitProcessor;

/**
 * Created by zhouping on 2015/9/20.
 */
public class AbstractIndentationUnitProcessor implements IndentationUnitProcessor {
    private int indentationUnit = 1;

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }


    public void setIndentationUnit(int value){
        this.indentationUnit = value;
    }

    public int getIndentationUnit(){
        return indentationUnit;
    }

    /**
     * 处理paddingunit的值
     * @param value 输入
     * @return 输出
     */
    public int paddingUnitProcessor(int value){
        return value;
    }

    /**
     * 处理从spinner处获得的值
     * @param value 输入值
     * @return 输出
     */
    public int paddingUnitGainFromSpinner(int value){
        return value;
    }
}