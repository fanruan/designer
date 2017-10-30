package com.fr.design.mainframe.widget.accessibles;

import com.fr.data.Dictionary;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.widget.wrappers.DictionaryWrapper;
import com.fr.design.present.dict.DictionaryPane;

import javax.swing.SwingUtilities;

public class AccessibleDictionaryEditor extends UneditableAccessibleEditor {

    private DictionaryPane dictPane;

    public AccessibleDictionaryEditor() {
        super(new DictionaryWrapper());
    }


    @Override
    protected void showEditorPane() {
        if (dictPane == null) {
            dictPane = new DictionaryPane();
        }

        BasicDialog dlg = dictPane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                Dictionary dict = dictPane.updateBean();
                setValue(dict);
                fireStateChanged();
            }
        });
        dictPane.populateBean((Dictionary) getValue());
        dlg.setVisible(true);
    }


    /**
     * @param dictionary dictionary
     * @deprecated 这个方法只用于兼容8.0下拉框类型控件的插件，
     * 推荐使用{@link UneditableAccessibleEditor#setValue(java.lang.Object)}
     */
    @Deprecated
    public void populateBean(Dictionary dictionary) {
        this.setValue(dictionary);
    }


    /**
     * @return Dictionary dictionary
     * @deprecated 这个方法只用于兼容8.0下拉框类型控件的插件，
     * 推荐使用{@link UneditableAccessibleEditor#getValue()}
     */
    @Deprecated
    public Dictionary updateBean() {
        return (Dictionary) this.getValue();
    }
}