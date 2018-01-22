package com.fr.design.fun;

import com.fr.design.beans.BasicBeanPane;
import com.fr.stable.fun.mark.Mutable;

/**
 * 自定义提交接口
 */
public interface SubmitProvider extends Mutable{

    String MARK_STRING = "SubmitProvider";

    int CURRENT_LEVEL = 2;


    /**
     * 设置界面
     * @return 界面
     */
    BasicBeanPane appearanceForSubmit();

    /**
     * 下拉选项
     * @return 下拉框中的文本
     */
    String dataForSubmit();

    /**
     * 键
     * @return 提交的键
     */
    String keyForSubmit();
}