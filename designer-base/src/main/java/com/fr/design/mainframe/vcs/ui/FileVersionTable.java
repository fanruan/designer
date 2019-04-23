package com.fr.design.mainframe.vcs.ui;

import com.fr.report.entity.VcsEntity;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;


public class FileVersionTable extends JTable {
    private static volatile FileVersionTable instance;

    private FileVersionTable() {
        super(new CellModel(new ArrayList<VcsEntity>()));

        setDefaultRenderer(VcsEntity.class, new FileVersionCellRender());
        setDefaultEditor(VcsEntity.class, new FileVersionCellEditor());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setTableHeader(null);
        setRowHeight(30);
    }

    public static FileVersionTable getInstance() {
        if (instance == null) {
            synchronized (FileVersionTable.class) {
                if (instance == null) {
                    instance = new FileVersionTable();
                }
            }
        }
        return instance;
    }

    public void updateModel(int selectedRow, List<VcsEntity> entities) {
        if (selectedRow > entities.size()) {
            selectedRow = entities.size();
        }
        setModel(new CellModel(entities));
        editCellAt(selectedRow, 0);
        setRowSelectionInterval(selectedRow, selectedRow);
    }

    private static class CellModel extends AbstractTableModel {
        private static final long serialVersionUID = -6078568799037607690L;
        private List<VcsEntity> vcsEntities;


        CellModel(List<VcsEntity> vcsEntities) {
            this.vcsEntities = vcsEntities;
        }

        public Class getColumnClass(int columnIndex) {
            return VcsEntity.class;
        }

        public int getColumnCount() {
            return 1;
        }

        public int getRowCount() {
            return (vcsEntities == null) ? 0 : vcsEntities.size() + 1;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex == 0) {
                return null;
            }
            return (vcsEntities == null) ? null : vcsEntities.get(rowIndex - 1);
        }

        public boolean isCellEditable(int columnIndex, int rowIndex) {
            return true;
        }


    }
}
