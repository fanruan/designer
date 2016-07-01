package com.fr.design.fun;

import com.fr.data.Verifier;
import com.fr.design.beans.BasicBeanPane;
import com.fr.stable.fun.mark.Mutable;

/**
 * Created by richie on 16/6/8.
 */
public interface VerifyDefineProvider extends Mutable {

    String MARK_STRING = "VerifyDefineProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 对应的校验类
     * @return 校验类
     */
    Class<? extends Verifier> classForVerifier();

    /**
     * 校验设置的界面
     * @return 界面
     */
    Class<? extends BasicBeanPane> appearanceForVerifier();

    /**
     * 此种类型的校验的名字
     * @return 名字
     */
    String nameForVerifier();

    /**
     * 菜单图标
     * @return 图标路径
     */
    String iconPath();
}
