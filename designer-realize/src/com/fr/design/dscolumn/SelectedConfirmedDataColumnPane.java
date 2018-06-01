package com.fr.design.dscolumn;

import com.fr.data.TableDataSource;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.main.impl.WorkBook;
import com.fr.report.cell.TemplateCellElement;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;

public class SelectedConfirmedDataColumnPane extends SelectedDataColumnPane {

    public SelectedConfirmedDataColumnPane() {
        super(false);
    }

    @Override
    protected void initTableNameComboBox() {
        tableNameComboBox = new TableDataComboBox(new WorkBook());
        tableNameComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                columnNameComboBox.setLoaded(false);
            }
        });
        tableNameComboBox.setPreferredSize(new Dimension(100, 20));
    }

    public void populate(TableDataSource source, TemplateCellElement cell) {
        tableNameComboBox.refresh(source);
        tableNameComboBox.setEditable(false);
        tableNameComboBox.setEnabled(false);
        super.populate(source, cell, null);
        try {
            Iterator it = source.getTableDataNameIterator();
            String name = (String) it.next();
            TemplateTableDataWrapper wrapper = new TemplateTableDataWrapper(source.getTableData(name), name);
            tableNameComboBox.setSelectedItem(wrapper);
            tableNameComboBox.getModel().setSelectedItem(wrapper);
        } catch (Exception ignored) {
        }
    }
}