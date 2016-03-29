package com.fr.design.designer.properties.items;

public class Item {

    private Object value;
    private String name;

    public Item(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
	public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Item) {
            Item a = (Item) o;
            Object av = a.getValue();
            if (value == null) {
            	return av == null;
            } else {
                if (av == null) {
                    return false;
                } else {
                    return value.equals(av);
                }
            }
        } else {
            return false;
        }
    }
}