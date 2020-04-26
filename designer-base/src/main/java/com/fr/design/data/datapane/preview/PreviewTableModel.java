package com.fr.design.data.datapane.preview;

import com.fr.cache.list.IntList;
import com.fr.data.AbstractDataModel;
import com.fr.data.impl.EmbeddedTableData.EmbeddedTDDataModel;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.design.utils.DesignUtils;
import com.fr.general.data.DataModel;
import com.fr.general.data.TableDataException;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;

import javax.swing.table.AbstractTableModel;

/**
 * 这个TableModel主要是预览数据的. 字段TableData必须转化为内置的
 */
public class PreviewTableModel extends AbstractTableModel {
    private DataModel dataModel;
    private String erroMessage = null;

    public IntList dateIndexs = new IntList(4);

    public PreviewTableModel(int maxRowCount) {
        // peter:默认必须显示错误的数据源.
        this(new ErrorResultSet(), maxRowCount);
    }

    public PreviewTableModel(DataModel sourceResultSet, int maxRowCount) {
        if (sourceResultSet instanceof ProcedureDataModel) {
            ProcedureDataModel rs = (ProcedureDataModel) sourceResultSet;
            try {
                this.dataModel = createRowDataModel(rs, maxRowCount);
            } catch (TableDataException e) {
                // TODO Auto-generated catch block
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        } else {
            this.dataModel = sourceResultSet;
        }
    }

    public static DataModel createRowDataModel(final ProcedureDataModel rs, int maxRowCount) throws TableDataException {
        int rowCount = rs.getRowCount();
        if (maxRowCount == 0) {
            maxRowCount = rowCount;
        } else if (maxRowCount > rowCount) {
            maxRowCount = rowCount;
        }
        final int finalRowCount = maxRowCount;
        DataModel dm = new AbstractDataModel() {

            @Override
            public void release() throws Exception {
                rs.release();
            }

            @Override
            public boolean hasRow(int rowIndex) throws TableDataException {
                return rowIndex <= finalRowCount - 1 && rowIndex >= 0;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex)
                    throws TableDataException {
                return rs.getValueAt(rowIndex, columnIndex);
            }

            @Override
            public int getRowCount() throws TableDataException {
                return finalRowCount;
            }

            @Override
            public String getColumnName(int columnIndex) throws TableDataException {
                return rs.getColumnName(columnIndex);
            }

            @Override
            public int getColumnCount() throws TableDataException {
                return rs.getColumnCount();
            }
        };
        return dm;
    }

    public String getErrMessage() {
        return this.erroMessage;
    }

    public void setErrMessage(String err) {
        this.erroMessage = err;
    }

    public String getColumnName(int column) {
        try {
            return Integer.toString(column + 1) + ". " + dataModel.getColumnName(column) + checkType(column);
        } catch (TableDataException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            DesignUtils.errorMessage(e.getMessage());
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Error");
        }
    }

    public int getRowCount() {
        try {
            return this.dataModel.getRowCount();
        } catch (TableDataException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return 0;
        }
    }

    public int getColumnCount() {
        try {
            if (dataModel == null) {
                return 0;
            }
            return dataModel.getColumnCount();
        } catch (TableDataException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            DesignUtils.errorMessage(e.getMessage());
            return 0;
        }
    }

    public Object getValueAt(int row, int column) {
        try {
            return dataModel.getValueAt(row, column);
        } catch (TableDataException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            DesignUtils.errorMessage(e.getMessage());
            return "";
        }
    }

    /*
     * peter:由于如果预览的TableData发生错误,界面有点象死机了一样,晕啊.
     * 就是由于预览的JTable在不停的getRowCount来显示数据.
     */
    private static class ErrorResultSet extends AbstractDataModel {

        public int getRowCount() {
            return 0;
        }

        public String getColumnName(int column) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Error");
        }

        public int getColumnCount() {
            return 1;
        }

        public Object getValueAt(int row, int column) {
            return "";
        }

        public void release() throws Exception {
            // Do nothing
        }
    }

    private String checkType(int column) {
        if (dateIndexs.contain(column)) {
            String s = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date");
            return ("(" + s + ")");
        }

        String s = StringUtils.EMPTY;
        Object o = null;
        try {
            for (int i = 0; i < dataModel.getRowCount(); i++) {
                o = dataModel.getRowCount() <= 0 ? null : dataModel.getValueAt(i, column);

                if (o != null && StringUtils.isNotEmpty(o.toString())) {
                    break;
                }
            }
        } catch (TableDataException e) {
            return ("(?)");
        }

        if (o == null) {
            s = "?";
        } else if (o instanceof String) {
            s = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_String");
            if (!WorkContext.getCurrent().isLocal() && dataModel instanceof EmbeddedTDDataModel) {
                Class clzz = ((EmbeddedTDDataModel) dataModel).getColumnClass(column);
                if (clzz != null) {
                    if (Number.class.isAssignableFrom(clzz)) {
                    s = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Number");//bigdecimal
                    } else if (java.sql.Date.class.isAssignableFrom(clzz)) {
                        s = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date");
                    }
                }
            }
        } else if (o instanceof Integer) {
            s = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Integer");
        } else if (o instanceof Double || o instanceof Float) {
            s = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Double");
        } else if (o instanceof java.sql.Date || o instanceof java.util.Date) {
            s = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date");
        } else if (o instanceof Number) {
            s = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Number");//bigdecimal
        } else {
            s = "?";
        }
        return ("(" + s + ")");
    }
}
