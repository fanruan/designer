package com.fr.design.mainframe.alphafine.model;

import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;

import javax.swing.*;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class SearchListModel extends DefaultListModel<AlphaCellModel> {
    SearchResult myDelegate;

    public SearchListModel(SearchResult searchResult) {
        this.myDelegate = searchResult;
    }

    @Override
    public void addElement(AlphaCellModel element) {
        int index = myDelegate.size();
        myDelegate.add(element);
        fireContentsChanged(this, index, index);
    }

    @Override
    public AlphaCellModel getElementAt(int index) {
        return myDelegate.get(index);
    }

    @Override
    public void add(int index, AlphaCellModel element) {
        myDelegate.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    @Override
    public AlphaCellModel remove(int index) {
        AlphaCellModel object = myDelegate.get(index);
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
