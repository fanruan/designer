package com.fr.design.fun;

import com.fr.design.beans.BasicBeanPane;
import com.fr.form.ui.mobile.MobileBookMarkStyle;
import com.fr.stable.fun.mark.Mutable;

/**
 * 移动端书签样式扩展接口
 *
 * @author hades
 * @version 10.0
 * Created by hades on 2019/12/23
 */
public interface MobileBookMarkStyleProvider extends Mutable {

    String XML_TAG = "MobileBookMarkStyleProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 书签样式
     * @return
     */
    Class<? extends MobileBookMarkStyle> classForMobileBookMarkStyle();

    /**
     * 书签样式面板
     * @return
     */
    Class<? extends BasicBeanPane<MobileBookMarkStyle>> classForMobileBookMarkStyleAppearance();

    String displayName();

}
