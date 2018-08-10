package com.fr.design.designer.properties.items;

import com.fr.form.ui.container.WAbsoluteLayout;


/**
 * Created by zhouping on 2016/8/1.
 */
public class FRAbsoluteConstraintsItems implements ItemProvider{

    public static final Item[] ITEMS = new Item[] {
            new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Scaling_Mode_Fit"), WAbsoluteLayout.STATE_FIT),
            new Item(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Scaling_Mode_Fixed"), WAbsoluteLayout.STATE_FIXED)
    };

    public Item[] getItems() {
        return ITEMS;
    }
}
