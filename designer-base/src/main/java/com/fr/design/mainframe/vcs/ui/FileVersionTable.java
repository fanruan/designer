package com.fr.design.mainframe.vcs.ui;

import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.log.FineLoggerFactory;
import com.fr.report.entity.VcsEntity;
import com.fr.workspace.server.vcs.VcsOperator;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.List;


public class FileVersionTablePanel extends JTable {

    private final VcsOperator vcs;

    public FileVersionTablePanel(VcsOperator vcs, TableCellEditor tableCellEditor, TableCellRenderer tableCellRenderer) {
        super(new Model(new ArrayList<VcsEntity>()));
        this.vcs = vcs;
        setDefaultRenderer(VcsEntity.class, tableCellRenderer);
        setDefaultEditor(VcsEntity.class, tableCellEditor);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setTableHeader(null);
        setRowHeight(30);
    }

    public void updateModel(int selectedRow) {
        String path = DesignerFrameFileDealerPane.getInstance().getSelectedOperation().getFilePath();
        List<VcsEntity> vcsEntities = null;
        try {
            vcsEntities = vcs.getVersions(path.replaceFirst("/", ""));
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
        if (selectedRow > vcsEntities.size()) {
            selectedRow = vcsEntities.size();
        }
        setModel(new Model(vcsEntities));
        editCellAt(selectedRow, 0);
        setRowSelectionInterval(selectedRow, selectedRow);
    }

    private static class Model extends AbstractTableModel {
        private List<VcsEntity> vcsEntities;


        Model(List<VcsEntity> vcsEntities) {
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


        public List<VcsEntity> getVcsEntities() {
            return vcsEntities;
        }


    }
}
