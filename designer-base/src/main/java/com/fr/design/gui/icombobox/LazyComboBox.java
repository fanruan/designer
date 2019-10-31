/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.icombobox;

import com.fr.log.FineLoggerFactory;


import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.Dimension;
import java.util.concurrent.ExecutionException;

/**
 * @author richer
 * @version 2018年2月6日14点43分 by @yaoh.wu
 * @since 6.5.5 创建于2011-6-15 延迟加载的下拉框
 */
public abstract class LazyComboBox extends UIComboBox implements PopupMenuListener {

    private static final int NUM = 80;
    private static final String[] PENDING_CONTENT = new String[]{"", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Loading") + "..."};

    /**
     * 是否加载完成
     */
    protected boolean loaded = false;

    /**
     * 初始化选项
     */
    private Object initialSelected = null;


    protected LazyComboBox() {
        super();
        this.setEditor(new FilterComboBoxEditor());
        addPopupMenuListener(this);
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * 加载下拉框中的选项
     *
     * @return 下拉框中的选项
     */
    public abstract Object[] load();

    @Override
    public void setSelectedItem(Object anObject) {
        initialSelected = anObject;
        if (loaded) {
            super.setSelectedItem(anObject);
        } else {
            this.setModel(new DefaultComboBoxModel<>(new Object[]{anObject}));
            super.setSelectedItem(anObject);
        }
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        if (loaded) {
            return;
        }
        DefaultComboBoxModel<String> loadingModel = new DefaultComboBoxModel<>(PENDING_CONTENT);
        this.setModel(loadingModel);
        new SwingWorker<Object[], Void>() {

            @Override
            protected Object[] doInBackground() {
                return load();
            }

            @Override
            public void done() {
                try {
                    LazyComboBox.this.loadList(get());
                } catch (InterruptedException | ExecutionException exception) {
                    FineLoggerFactory.getLogger().debug(exception.getMessage());
                    Thread.currentThread().interrupt();
                }
                LazyComboBox.this.showPopup();
            }
        }.execute();
    }


    /**
     * 加载下拉列表
     */
    public void loadList() {
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>(load());
        model.setSelectedItem(initialSelected);
        this.setModel(model);
        this.selectedItemReminder = initialSelected;
        loaded = true;
    }

    /**
     * 加载下拉列表
     *
     * @param contents 下拉列表内容
     */
    private void loadList(Object[] contents) {
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>(contents);
        model.setSelectedItem(initialSelected);
        this.setModel(model);
        this.selectedItemReminder = initialSelected;
        loaded = true;
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {

    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.width = NUM;
        return dim;
    }

    class FilterComboBoxEditor extends UIComboBoxEditor implements DocumentListener {
        private Object item;
        private volatile boolean filtering = false;
        private volatile boolean setting = false;

        public FilterComboBoxEditor() {
            super();
            textField.getDocument().addDocumentListener(this);
        }

        @Override
        public void setItem(Object item) {
            if (filtering) {
                return;
            }
            this.item = item;
            this.setting = true;
            textField.setSetting(true);
            String newText = (item == null) ? "" : item.toString();
            textField.setText(newText);
            textField.setSetting(false);
            this.setting = false;
        }

        @Override
        public Object getItem() {
            return this.item;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            handleChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            handleChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            handleChange();
        }

        void handleChange() {
            if (setting) {
                return;
            }
            filtering = true;
            String xx = textField.getText();
            LazyComboBox.this.setSelectedItem(xx);
            this.item = textField.getText();

            setPopupVisible(true);
            filtering = false;
        }
    }
}
