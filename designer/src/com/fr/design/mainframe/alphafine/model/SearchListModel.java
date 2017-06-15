package com.fr.design.mainframe.alphafine.model;

import javax.swing.*;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class SearchListModel extends DefaultListModel {
    SearchResult myDelegate;

    public SearchListModel(SearchResult searchResult) {
        this.myDelegate = searchResult;
    }

    @Override
    public void addElement(Object element) {
        int index = myDelegate.size();
        myDelegate.add(element);
        fireContentsChanged(this, index, index);
    }

    @Override
    public Object getElementAt(int index) {
        return myDelegate.get(index);
    }

    @Override
    public void add(int index, Object element) {
        myDelegate.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    @Override
    public Object remove(int index) {
        Object object = myDelegate.get(index);
        myDelegate.remove(object);
        fireIntervalRemoved(this, index, index);
        return object;
    }

    @Override
    public int getSize() {
        return this.myDelegate.size();
    }

    @Override
    public void removeAllElements() {
        this.myDelegate.clear();
    }
}
