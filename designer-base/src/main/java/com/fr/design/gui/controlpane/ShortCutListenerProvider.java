package com.fr.design.gui.controlpane;

/**
 * Created by plough on 2018/8/12.
 */
public interface ShortCutListenerProvider {
    void onAddItem(NameableCreator creator);
    void onRemoveItem();
    void onCopyItem();
    void onMoveUpItem();
    void onMoveDownItem();
    void onSortItem(boolean isAtoZ);
    boolean isItemSelected();
    NameableCreator[] creators();
}