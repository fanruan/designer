package com.fr.design.designer.properties.items;

import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.general.Inter;

/**
 * Created by zhouping on 2016/8/1.
 */
public class FRAbsoluteConstraintsItems implements ItemProvider{

    public static final Item[] ITEMS = new Item[] {
            new Item(Inter.getLocText("FR-Designer_Widget_Scaling_Mode_Fit"), WAbsoluteLayout.STATE_FIT),
            new Item(Inter.getLocText("FR-Designer_Widget_Scaling_Mode_Fixed"), WAbsoluteLayout.STATE_FIXED)
    };

    public Item[] getItems() {
        return ITEMS;
    }
}