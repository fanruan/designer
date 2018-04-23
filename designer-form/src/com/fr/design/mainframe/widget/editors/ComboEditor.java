package com.fr.design.mainframe.widget.editors;

import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.icombobox.UIComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * @author richer
 * @since 6.5.3
 * 通过下拉框选择特定值的编辑器
 */
public class ComboEditor extends AbstractPropertyEditor {

    protected UIComboBox comboBox;

    public ComboEditor() {
        comboBox = new UIComboBox();
        initComboBoxLookAndFeel();
        ComboBoxModel model = model();
        if (model != null) {
            comboBox.setModel(model);
        }
        ListCellRenderer cellRenderer = renderer();
        if (cellRenderer != null) {
            comboBox.setRenderer(cellRenderer);
        }
    }

    public ComboEditor(Object[] items) {
        this(new UIComboBox(items));
    }

    public ComboEditor(Vector items) {
        this(new UIComboBox(items));
    }

    public ComboEditor(ComboBoxModel model) {
        this(new UIComboBox(model));
    }

    public ComboEditor(UIComboBox combo) {
        comboBox = combo;
        initComboBoxLookAndFeel();
    }

    private void initComboBoxLookAndFeel() {
        comboBox.setEditable(false);
        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                firePropertyChanged();
            }
        });
        ((JComponent) comboBox.getEditor().getEditorComponent()).setBorder(null);
        comboBox.setBorder(null);
    }

    public ComboBoxModel model() {
        return null;
    }

    public ListCellRenderer renderer() {
        return null;
    }


    @Override
    public void setValue(Object value) {
        comboBox.setSelectedItem(value);
    }

    @Override
    public Object getValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    public Component getCustomEditor() {
        return comboBox;
    }

    @Override
    public void validateValue() throws ValidationException {
    }
}