package com.fr.design.mainframe.widget.propertypane;

import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;
import com.fr.design.mainframe.widget.editors.ComboEditor;

import java.util.Vector;

public class BrowserFitEditor extends ComboEditor {
    public BrowserFitEditor() {
        this(new BrowserFitAlignmentItems());
    }

    public BrowserFitEditor(ItemProvider provider) {
        this(provider.getItems());
    }

    public BrowserFitEditor(Item[] items) {
        super(items);
    }

    public BrowserFitEditor(Vector<Item> items) {
        super(items);
    }

    @Override
    public Object getValue() {
        Item item = (Item) comboBox.getSelectedItem();
        return item.getValue();
    }

    @Override
    public void setValue(Object value) {
        Item item = new Item("", value);
        comboBox.setSelectedItem(item);
    }

    /**
     * 是否立即刷新
     *
     * @return 是或者否
     */
    @Override
    public boolean refreshInTime() {
        return false;
    }
}
