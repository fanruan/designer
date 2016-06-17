package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;
import com.fr.design.mainframe.widget.editors.ComboEditor;

import java.util.Vector;

public class MobileFitEditor extends ComboEditor {
    public MobileFitEditor() {
        this(new MobileFitAlignmentItems());
    }

    public MobileFitEditor(ItemProvider provider) {
        this(provider.getItems());
    }

    public MobileFitEditor(Item[] items) {
        super(items);
    }

    public MobileFitEditor(Vector<Item> items) {
        super(items);
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            return;
        }

        Item item = new Item(value.toString(), value);
        comboBox.setSelectedItem(item);
    }

    @Override
    public Object getValue() {
        Item item = (Item) comboBox.getSelectedItem();
        return item.getValue();
    }

    /**
     * 是否立即刷新
     * @return 是或者否
     */
    @Override
    public boolean refreshInTime() {
        return true;
    }
}
