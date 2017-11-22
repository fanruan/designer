package com.fr.design.gui.icombobox;


import com.fr.design.gui.icombobox.filter.Filter;
import com.fr.design.gui.icombobox.filter.StartsWithFilter;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.stable.StringUtils;

import javax.swing.ComboBoxEditor;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MoMeak on 2017/9/5.
 */
public class TextFontComboBox<T> extends ExtendedComboBox {
    private Filter filter;

    public TextFontComboBox() {
        this(new ArrayList<T>());
        this.setEditable(true);
    }

    public TextFontComboBox(List<T> itemList) {
        this(new StartsWithFilter(), itemList);
    }

    public TextFontComboBox(Filter filter, List<T> itemList) {
        this.filter = filter;

        setModel(new FilterableComboBoxModel(itemList));
        setEditor(new TextFontComboBox.FilterComboBoxEditor());
        setEditable(true);
    }

    public void setItemArray(T[] objectArray) {
        List<T> itemList = new ArrayList<T>();
        if (objectArray != null) {
            for (int i = 0; i < objectArray.length; i++) {
                itemList.add(objectArray[i]);
            }
        }

        this.setItemList(itemList);
    }

    public void setItemList(List<T> itemList) {
        ((FilterableComboBoxModel) this.getModel()).setPrefix(StringUtils.EMPTY);
        ((FilterableComboBoxModel) this.getModel()).setItemList(itemList);
    }

    class FilterComboBoxEditor implements ComboBoxEditor, DocumentListener {
        private Object item;

        public UITextField textField;
        private volatile boolean filtering = false;
        private volatile boolean setting = false;

        public FilterComboBoxEditor() {
            textField = new UITextField(15){
                @Override
                public boolean shouldResponseChangeListener() {
                    return false;
                }
            };
            textField.getDocument().addDocumentListener(this);
        }

        public Component getEditorComponent() {
            return textField;
        }

        public void setItem(Object item) {
            if (filtering) {
                return;
            }
            this.item = item;

            this.setting = true;
            String newText = (item == null) ? StringUtils.EMPTY : item.toString();
            textField.setText(newText);
            this.setting = false;
        }

        public Object getItem() {
            return this.item;
        }

        public void selectAll() {
            textField.selectAll();
        }

        public void addActionListener(ActionListener l) {
            textField.addActionListener(l);
        }

        public void removeActionListener(ActionListener l) {
            textField.removeActionListener(l);
        }

        public void insertUpdate(DocumentEvent e) {
            handleChange();
        }

        public void removeUpdate(DocumentEvent e) {
            handleChange();
        }

        public void changedUpdate(DocumentEvent e) {
            handleChange();
        }

        protected void handleChange() {
            if (setting) {
                return;
            }

            filtering = true;

            ((FilterableComboBoxModel) getModel()).setSelectedItem(textField.getText());
            this.item = textField.getText();

            setPopupVisible(true);
            filtering = false;
        }
    }
}
