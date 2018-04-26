package com.fr.design.gui.icombobox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

import com.fr.design.gui.icombobox.filter.Filter;
import com.fr.design.gui.icombobox.filter.StartsWithFilter;

public class FilterableComboBoxModel extends AbstractListModel implements MutableComboBoxModel {
    private List itemList;
    private Filter filter = new StartsWithFilter();
    private String prefix = "";
    private List filteredItems;
    private Object selectedItem;

    public FilterableComboBoxModel(List items) {
        this.itemList = new ArrayList(items);
        filteredItems = new ArrayList(items.size());
        updateFilteredItems();
    }

    public void addElement(Object obj) {
        itemList.add(obj);
        updateFilteredItems();
    }

    public void removeElement(Object obj) {
        itemList.remove(obj);
        updateFilteredItems();
    }

    public void removeElementAt(int index) {
        itemList.remove(index);
        updateFilteredItems();
    }

    public void setItemList(List itemList) {
        this.itemList = itemList;
        updateFilteredItems();
    }

    public void insertElementAt(Object obj, int index) {
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        updateFilteredItems();
    }

    protected void updateFilteredItems() {
        fireIntervalRemoved(this, 0, filteredItems.size());
        filteredItems.clear();

        if (filter == null) {
            filteredItems.addAll(itemList);
        } else {
        	// alex:先加一个NPE判断
        	if (itemList != null) {
        		for (Iterator iterator = itemList.iterator(); iterator.hasNext();) {
        			Object item = iterator.next();
        			if (filter.accept(prefix, item)) {
        				filteredItems.add(item);
        			}
        		}
        	}
        }
        
        fireIntervalAdded(this, 0, filteredItems.size());
    }

    public int getSize() {
        return filteredItems.size();
    }

    public Object getElementAt(int index) {
        return filteredItems.get(index);
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object val) {
         if ((selectedItem == null) && (val == null)) {
            return;
         }

        if ((selectedItem != null) && selectedItem.equals(val)) {
            return;
        }

        if ((val != null) && val.equals(selectedItem)) {
            return;
        }

        selectedItem = val;
        fireContentsChanged(this, -1, -1);
    }
}