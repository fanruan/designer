package com.fr.design.mainframe.alphafine.model;

import javax.swing.*;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class SearchListModel extends DefaultListModel {
    SearchResult myDelegate;

    public SearchListModel( SearchResult searchResult) {
        this.myDelegate = searchResult;
    }

    @Override
    public void addElement(Object element) {
        myDelegate.add(element);
    }

    @Override
    public Object getElementAt(int index) {
        return myDelegate.get(index);
    }

    @Override
    public void insertElementAt(Object element, int index) {
        this.myDelegate.add(index, element);
    }

    @Override
    public void removeElementAt(int index) {
        this.myDelegate.remove(index);
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
