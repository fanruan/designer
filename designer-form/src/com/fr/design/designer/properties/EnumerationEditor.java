package com.fr.design.designer.properties;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.EventObject;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;

public class EnumerationEditor extends DelegateEditor {

    public EnumerationEditor(ItemProvider provider) {
        this(provider.getItems());
    }

    public EnumerationEditor(Item[] items) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Item item : items) {
            model.addElement(item);
        }
        init(new UIComboBox(model));
    }

    public EnumerationEditor(Iterable<Item> items) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Item item : items) {
            model.addElement(item);
        }
        init(new UIComboBox(model));
    }

    public EnumerationEditor(Enumeration<Item> items) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        while (items.hasMoreElements()) {
            model.addElement(items.nextElement());
        }
        init(new UIComboBox(model));
    }

    public EnumerationEditor(UIComboBox comboBox) {
        init(comboBox);
    }

    private void init(final UIComboBox comboBox) {
        editorComponent = comboBox;
        comboBox.putClientProperty("UIComboBox.isTableCellEditor", Boolean.TRUE);
        delegate = new EditorDelegate() {

            @Override
            public void setValue(Object value) {
                Item item = new Item(null, value);
                comboBox.setSelectedItem(item);
            }

            @Override
            public Object getCellEditorValue() {
                Item item = (Item) comboBox.getSelectedItem();
                return item.getValue();
            }

            @Override
            public boolean shouldSelectCell(EventObject anEvent) {
                if (anEvent instanceof MouseEvent) {
                    MouseEvent e = (MouseEvent) anEvent;
                    return e.getID() != MouseEvent.MOUSE_DRAGGED;
                }
                return true;
            }

            @Override
            public boolean stopCellEditing() {
                if (comboBox.isEditable()) {
                    comboBox.actionPerformed(new ActionEvent(
                        EnumerationEditor.this, 0, ""));
                }
                return super.stopCellEditing();
            }
        };
        ((JComponent) comboBox.getEditor().getEditorComponent()).setBorder(null);
        editorComponent.setBorder(null);
        comboBox.addActionListener(delegate);
    }
}