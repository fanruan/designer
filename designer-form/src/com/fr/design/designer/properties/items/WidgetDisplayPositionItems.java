package com.fr.design.designer.properties.items;

import com.fr.general.Inter;
import com.fr.report.stable.FormConstants;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-8-13
 * Time: 下午2:13
 */
public class WidgetDisplayPositionItems implements ItemProvider{
    //这里为了和web端一致，只好设置成012了
    private static Item[] VALUE_ITEMS = {
            new Item(Inter.getLocText("StyleAlignment-Left"), FormConstants.LEFTPOSITION),
            new Item(Inter.getLocText("Center"), FormConstants.CENTERPOSITION),
            new Item(Inter.getLocText("StyleAlignment-Right"), FormConstants.RIGHTPOSITION)
    };

    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }
}