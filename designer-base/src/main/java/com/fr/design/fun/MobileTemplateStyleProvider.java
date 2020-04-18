package com.fr.design.fun;

import com.fr.design.beans.BasicBeanPane;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.stable.fun.mark.Mutable;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/31
 */
public interface MobileTemplateStyleProvider extends Mutable {

    String XML_TAG = "MobileTemplateStyleProvider";

    int CURRENT_LEVEL = 1;

    Class<? extends MobileTemplateStyle> classFroMobileTemplateStyle();


    Class<? extends BasicBeanPane<MobileTemplateStyle>>  classFroMobileTemplateStyleAppearance();

    String displayName();

}
