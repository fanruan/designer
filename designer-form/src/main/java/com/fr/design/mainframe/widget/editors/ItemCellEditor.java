package com.fr.design.mainframe.widget.editors;

import java.util.Vector;

import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;
import com.fr.design.designer.properties.items.LabelHorizontalAlignmentItems;
/**
 * 
 * barry: 用作左中右对齐设置的下拉框Editor
 *
 */
public class ItemCellEditor extends ComboEditor {
	public ItemCellEditor() {
		this(new LabelHorizontalAlignmentItems());
	}

    public ItemCellEditor(ItemProvider provider) {
        this(provider.getItems());
    }

    public ItemCellEditor(Item[] items) {
        super(items);
    }

    public ItemCellEditor(Vector<Item> items) {
        super(items);
    }

    @Override
    public void setValue(Object value) {
        Item item = new Item("", value);
        comboBox.setSelectedItem(item);
    }

    @Override
    public Object getValue() {
        Item item = (Item) comboBox.getSelectedItem();
        return item.getValue();
    }
}