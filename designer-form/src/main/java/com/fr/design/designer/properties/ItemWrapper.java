package com.fr.design.designer.properties;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;

public class ItemWrapper implements Encoder, Decoder {

    private Item[] items;

    public ItemWrapper(ItemProvider provider) {
        this(provider.getItems());
    }

    public ItemWrapper(Item[] items) {
        this.items = items;
    }

    @Override
    public Object decode(String txt) {
        for (Item item : items) {
            if (txt.equals(item.getName())) {
                return item.getValue();
            }
        }
        return null;
    }

    @Override
    public String encode(Object v) {
        for (Item item : items) {
            if (item.getValue().equals(v)) {
                return item.getName();
            }
        }
        return null;
    }

    @Override
    public void validate(String txt) throws ValidationException {
        for (Item item : items) {
            if (txt.equals(item.getName())) {
                return;
            }
        }
        throw new ValidationException("No such element:" + txt);
    }
}