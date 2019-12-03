package com.fr.design.data.tabledata.tabledatapane;

import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;

import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;


public class EmbeddedTableDataDefinedPane extends BasicPane{
	
	private EmbeddedTableData tableData;
	private JTable dataJTable;
	private UIButton add;
	private UIButton del;
    
    private static String[] TYPE = {
    	 com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_String"),
    	 com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Integer"),
    	 com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Double"),
    	 com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date")
    };
	
	public EmbeddedTableDataDefinedPane() {
		initComponents();
	}
	
	protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        dataJTable = new JTable(new EmbeddedTableDataDefinedPane.EmbeddedTableDefinedModel(new EmbeddedTableData()));
        JScrollPane scrollPane = new JScrollPane(dataJTable);
        this.add(scrollPane, BorderLayout.CENTER);
        dataJTable.setRowSelectionAllowed(true);
        dataJTable.setColumnSelectionAllowed(true);
        
        // 类型选择
        UIComboBox typeBox = new UIComboBox(TYPE);
        dataJTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(typeBox));
        
        // 单击编辑
        TableCellEditor tableCellEditor = dataJTable.getDefaultEditor(String.class);
    	if (tableCellEditor != null) {
    		(( DefaultCellEditor) tableCellEditor).setClickCountToStart(1);
    	}
    	
    	// 行号显示
        TableColumn tableColumn = dataJTable.getColumnModel().getColumn(0);
        tableColumn.setCellRenderer(new CellRenderer());
        tableColumn.setMaxWidth(dataJTable.getColumnCount());
        
        // 控制按钮
        add = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add"));
        del = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Delete"));
        JPanel buttonPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
//        buttonPane.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
        buttonPane.add(add);
        buttonPane.add(del);
        this.add(buttonPane, BorderLayout.NORTH);
        
        add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				insertRow();
				checkEnabled();
			}
        });
        del.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				removeRow();
				checkEnabled();
			}
        });
        checkEnabled();
	}
	
	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tabledata_Embedded_Column_Setting");
	}
	 
	public void checkValid() throws Exception {
        try {
            dataJTable.getCellEditor().stopCellEditing();
        }
        catch (Exception e) {
        }
    }
	
	public void insertRow(){
		EmbeddedTableDataDefinedPane.EmbeddedTableDefinedModel localDefaultModel = 
			(EmbeddedTableDataDefinedPane.EmbeddedTableDefinedModel) dataJTable.getModel();
        
        try {
        	dataJTable.getCellEditor().stopCellEditing();
        }
        catch (Exception e) {
        }
        
        int indexedrow = dataJTable.getSelectedRow();
        if (indexedrow == - 1) {
        	indexedrow = dataJTable.getRowCount() - 1;
        }
		
        localDefaultModel.addNewRowData(indexedrow);
        localDefaultModel.fireTableDataChanged();
        dataJTable.requestFocusInWindow();
        dataJTable.setRowSelectionInterval(indexedrow + 1, indexedrow + 1);
        dataJTable.editCellAt(indexedrow + 1, 1);
	}
	
	protected void removeRow() {		
		EmbeddedTableDataDefinedPane.EmbeddedTableDefinedModel localDefaultModel = 
			(EmbeddedTableDataDefinedPane.EmbeddedTableDefinedModel) dataJTable.getModel();
		
        int selectedRow = dataJTable.getSelectedRow();
        if (selectedRow == -1) {
            selectedRow = dataJTable.getRowCount() - 1;
        }

        try {
            dataJTable.getCellEditor().stopCellEditing();
        }
        catch (Exception e) {
        }

        for (int i = 0; i < dataJTable.getSelectedRowCount(); i++){
        	localDefaultModel.removeRow(selectedRow);
        }
        localDefaultModel.fireTableDataChanged();

        int rowCount = localDefaultModel.getRowCount();
        if (rowCount > 0) {
            if (selectedRow < rowCount) {
                dataJTable.setRowSelectionInterval(selectedRow, selectedRow);
            }
            else {
                dataJTable.setRowSelectionInterval(rowCount - 1, rowCount - 1);
            }
        }
    }
	
	private void checkEnabled() {        
        this.dataJTable.setEnabled(true);
        this.del.setEnabled(true);
        this.add.setEnabled(true);
        
        if (dataJTable.getRowCount() <=0 ) {
        	this.del.setEnabled(false);
        }
    }
	
	public void populate(Object obj) {
		if(obj == null || !(obj instanceof EmbeddedTableData)) {
			return;
		}
		try {
			this.tableData = (EmbeddedTableData)obj;
			
			EmbeddedTableDataDefinedPane.EmbeddedTableDefinedModel localDefaultModel = 
				(EmbeddedTableDataDefinedPane.EmbeddedTableDefinedModel) dataJTable.getModel();
			localDefaultModel.setEditableTableData((EmbeddedTableData)tableData.clone());
			localDefaultModel.fireTableDataChanged();
			checkEnabled();
		} catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
		}
	}
	
	public EmbeddedTableData update() {
		if(dataJTable.getCellEditor() != null){
        	dataJTable.getCellEditor().stopCellEditing();	
        }
		EmbeddedTableDataDefinedPane.EmbeddedTableDefinedModel localDefaultModel = 
			(EmbeddedTableDataDefinedPane.EmbeddedTableDefinedModel) dataJTable.getModel();
		try {
			tableData = (EmbeddedTableData)(localDefaultModel.getEditableTableData().clone());
		} catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
		}
		
		return tableData;
	}
	
	class CellRenderer extends DefaultTableCellRenderer {
    	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    		if (column == 0) {
    			setBackground(new Color(229, 229, 229));
    			setHorizontalAlignment(CENTER);
    		}
    		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    	}
    }
	
	class EmbeddedTableDefinedModel extends AbstractTableModel {
        private EmbeddedTableData embeddedTableData;
        private String[] COLUMN_NAME = {
              	 "", 
              	 com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Column_Name"),
              	 com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Type")
              };
        private int sum = 0;

        public EmbeddedTableDefinedModel(EmbeddedTableData editableTableData) {
            this.embeddedTableData = editableTableData;
        }
        
        public EmbeddedTableData getEditableTableData() {
            return embeddedTableData;
        }

        public void setEditableTableData(EmbeddedTableData editableTableData) {
            this.embeddedTableData = editableTableData;
        }

        public String getColumnName(int column) {
        	if (column < COLUMN_NAME.length) {
        		return COLUMN_NAME[column];
        	} else {
        		return "";
        	}
        }

        public Class getColumnClass(int column) {
        	return String.class;
        }
        
        public int getRowCount() {
            return embeddedTableData.getColumnCount();
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int row, int column) {
        	switch (column) {
        	case 0:
        		return Integer.toString(row + 1);
        	case 1:
        		return embeddedTableData.getColumnName(row);
        	case 2:
        		if (embeddedTableData.getColumnClass(row).equals(String.class)) {
        			return TYPE[0];
        		} else if (embeddedTableData.getColumnClass(row).equals(Integer.class)) {
        			return TYPE[1];
        		} else if (embeddedTableData.getColumnClass(row).equals(Double.class)) {
        			return TYPE[2];
        		} else if (embeddedTableData.getColumnClass(row).equals(Date.class)) {
        			return TYPE[3];
        		}
        	}
            return null;
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
        	return columnIndex !=0;
        }
        
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        	switch (columnIndex) {
        	case 0:
        		break;
        	case 1:
        		embeddedTableData.setColumn(rowIndex, (String)aValue, embeddedTableData.getColumnClass(rowIndex));
        		break;
        	case 2:
        		Class cls = null;
        		if (((String)aValue).equals(TYPE[0])){
        			cls = String.class;
        		} else if (((String)aValue).equals(TYPE[1])){
        			cls = Integer.class;
        		} else if (((String)aValue).equals(TYPE[2])){
        			cls = Double.class;
        		} else if (((String)aValue).equals(TYPE[3])){
        			cls = Date.class;
        		}
        		embeddedTableData.setColumn(rowIndex, embeddedTableData.getColumnName(rowIndex), cls);
        		break;
        	}
        }

        public void addNewRowData(int index) {
        	int n= embeddedTableData.getColumnCount();
        	sum++;
        	for(int i=0;i<n;i++){
        		String columnName = embeddedTableData.getColumnName(i);
        		if(ComparatorUtils.equals("ColName" + sum, columnName)){
        			sum++;
        		}
        		
        	}
            embeddedTableData.insertColumn("ColName" + sum, String.class, index);
        }

        public void removeRow(int rowIndex) {
            embeddedTableData.removeColumn(rowIndex);
        }
        
        public void clear() {
        	embeddedTableData.clear();
        }
    }
}
