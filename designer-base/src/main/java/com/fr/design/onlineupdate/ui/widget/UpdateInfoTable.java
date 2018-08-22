package com.fr.design.onlineupdate.ui.widget;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class UpdateInfoTable extends JTable {
    private UpdateInfoTableModel dataModel;

    public UpdateInfoTable(TableModel dm) {
        super(dm);
        dataModel = (UpdateInfoTableModel) dm;
    }

    public UpdateInfoTable() {
    }

    public UpdateInfoTable(String[] columnNames) {
        this(new UpdateInfoTableModel(columnNames, new ArrayList<Object[]>()));
    }

    public UpdateInfoTable(List<Object[]> data, String[] columnNames) {
        this(new UpdateInfoTableModel(columnNames, data));
    }

    public UpdateInfoTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
    }

    public UpdateInfoTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public UpdateInfoTableModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(UpdateInfoTableModel dataModel) {
        this.dataModel = dataModel;
    }
}
