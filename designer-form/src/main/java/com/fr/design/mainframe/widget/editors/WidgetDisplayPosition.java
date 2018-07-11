package com.fr.design.mainframe.widget.editors;

import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;
import com.fr.design.designer.properties.items.WidgetDisplayPositionItems;
import com.fr.stable.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-22
 * Time: 下午6:56
 */
public class WidgetDisplayPosition extends ComboEditor{
    public WidgetDisplayPosition() {
        this(new WidgetDisplayPositionItems());
    }

    public WidgetDisplayPosition(ItemProvider provider) {
        this(provider.getItems());
    }

    public WidgetDisplayPosition(Item[] items) {
        super(items);
    }

    @Override
    public void setValue(Object value) {
        Item item = new Item(StringUtils.EMPTY, value);
        comboBox.setSelectedItem(item);
    }

    /**
     * 待说明
     * @return   是
     */
    public boolean refreshInTime(){
        return true;
    }

    @Override
    public Object getValue() {
        Item item = (Item) comboBox.getSelectedItem();
        return item.getValue();
    }
}