package com.fr.design.designer.properties.mobile;

import com.fr.base.mobile.MobileFitAttrState;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;

public class MobileFitAlignmentItems implements ItemProvider {

    private static Item[] VALUE_ITEMS;

    static {
//        此处不能循环根据MobileFitAttrState的枚举顺序创建items，否则不自适应选项就会跑到双向自适应选项前面。
        VALUE_ITEMS = new Item[]{
                new Item(MobileFitAttrState.DEFAULT.description(), MobileFitAttrState.DEFAULT),
                new Item(MobileFitAttrState.HORIZONTAL.description(), MobileFitAttrState.HORIZONTAL),
                new Item(MobileFitAttrState.VERTICAL.description(), MobileFitAttrState.VERTICAL),
                new Item(MobileFitAttrState.BIDIRECTIONAL.description(), MobileFitAttrState.BIDIRECTIONAL),
                new Item(MobileFitAttrState.NONE.description(), MobileFitAttrState.NONE)
        };
    }

    @Override
    public Item[] getItems() {
        return VALUE_ITEMS;
    }

}
