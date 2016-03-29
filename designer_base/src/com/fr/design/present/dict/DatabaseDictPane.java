package com.fr.design.present.dict;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.Formula;
import com.fr.base.TableData;
import com.fr.design.data.DesignTableDataManager;
import com.fr.data.core.db.DBUtils;
import com.fr.design.data.datapane.ChoosePane;
import com.fr.design.data.datapane.DataBaseItems;
import com.fr.design.data.datapane.VerticalChoosePane;
import com.fr.design.data.datapane.preview.PreviewLabel.Previewable;
import com.fr.data.impl.DatabaseDictionary;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.ColumnIndexEditor;
import com.fr.design.editor.editor.ColumnNameEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

public class DatabaseDictPane extends FurtherBasicBeanPane<DatabaseDictionary> implements Previewable, UIObserver {
    /**
     * richer:数据字典和数据链面板
     */
    protected com.fr.data.impl.Connection database;
    protected ValueEditorPane keyColumnPane;
    protected ValueEditorPane valueDictPane;

    protected ChoosePane chooseTable;
    private UIObserverListener uiObserverListener;
    private ItemListener itemListener;

    public DatabaseDictPane() {
        initBasicComponet();
        initComponet();
        iniListener();
        chooseTable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                dbChange();
            }
        });
    }


    private void initBasicComponet() {
        keyColumnPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor()});
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        formulaEditor.setEnabled(true);
        valueDictPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor(), formulaEditor});
    }

    private void initComponet() {
        chooseTable = new VerticalChoosePane(this);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel("  " + Inter.getLocText("Actual_Value") + ":"), keyColumnPane},
                new Component[]{new UILabel("  " + Inter.getLocText("Display_Value") + ":"), valueDictPane}
        };
        JPanel dbDictPanel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout(0, 4));
        this.add(chooseTable, BorderLayout.NORTH);
        this.add(dbDictPanel, BorderLayout.CENTER);
    }


    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        uiObserverListener.doChange();
                    }
                }
            });
        }
    }

    /**
     * @param aListener
     */
    public void addItemListener(ItemListener aListener) {
        this.chooseTable.addItemListener(aListener);
        this.itemListener = aListener;
    }

    @Override
    /**
     *
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Datasource-From_Database");
    }

    public void populateBean(DatabaseDictionary dbDict) {
        if (dbDict == null) {
            this.chooseTable.populateBean(new DataBaseItems());
            this.keyColumnPane.populate(StringUtils.EMPTY);
            this.valueDictPane.populate(StringUtils.EMPTY);
            return;
        }
        this.database = dbDict.getDatabaseConnection();
        String dbName;
        if (database instanceof NameDatabaseConnection) {
            dbName = ((NameDatabaseConnection) database).getName();
            dbName = dbName == null ? StringUtils.EMPTY : dbName;
        } else {
            dbName = StringUtils.EMPTY;
        }

        this.chooseTable.populateBean(new DataBaseItems(dbName, dbDict.getSchema(), dbDict.getTableName()));

        if(this.database == null){
        	return;
        }
        
        // richer:这个也要+1才行
    	if(StringUtils.isNotEmpty(dbDict.getKeyColumnName())){
    		this.keyColumnPane.populate(dbDict.getKeyColumnName());
    	}else{
    		this.keyColumnPane.populate(dbDict.getKeyColumnIndex() + 1);
    	}


    	if(StringUtils.isNotEmpty(dbDict.getValueColumnName())){
    		this.valueDictPane.populate(dbDict.getValueColumnName());
    	}else{
    		Object value = null;
	        if (dbDict.getFormula() != null) {
	            value = dbDict.getFormula();
	        } else {
	            // alex:因为显示到界面上的index是以1为始的
	            value = dbDict.getValueColumnIndex() + 1;
	        }
	        this.valueDictPane.populate(value);
    	}
    }

    public DatabaseDictionary updateBean() {
        DatabaseDictionary dbDict = new DatabaseDictionary();

        DataBaseItems para = chooseTable.updateBean();
        if (StringUtils.isBlank(para.getDatabaseName())) {
            dbDict.setDatabaseConnection(null);
        } else {
            database = DBUtils.checkDBConnection(para.getDatabaseName());
            if (database != null) {
                dbDict.setDatabaseConnection(database);
            }
        }

        dbDict.setSchema(para.getSchemaName());
        dbDict.setTableName(para.getTableName());
        // alex:因为显示到界面上的index是以1为始的,所以要减1
		if (this.keyColumnPane.update() != null && (Integer) this.keyColumnPane.update() - 1 >= 0) {
			int keyColumnIndex = (Integer) this.keyColumnPane.update() - 1;
			String keyColumnName = StringUtils.EMPTY;
			if (this.keyColumnPane.getCurrentEditor() instanceof ColumnNameEditor) {
				keyColumnName = ((ColumnNameEditor) this.keyColumnPane.getCurrentEditor()).getColumnName();
				keyColumnIndex = -1;
			}
			dbDict.setKeyColumnIndex(keyColumnIndex);
			dbDict.setKeyColumnName(keyColumnName);
		}
        Object value = this.valueDictPane.update();
		if (value instanceof Integer) {
			int valueColumnIndex = (Integer) this.valueDictPane.update() - 1;
			String valueColumnName = StringUtils.EMPTY;
			if (this.valueDictPane.getCurrentEditor() instanceof ColumnNameEditor) {
				valueColumnName = ((ColumnNameEditor) this.valueDictPane.getCurrentEditor()).getColumnName();
				valueColumnIndex = -1;
			}
			dbDict.setValueColumnIndex(valueColumnIndex);
			dbDict.setValueColumnName(valueColumnName);
		} else {
            dbDict.setFormula(((Formula) value));
        }

        return dbDict;
    }

    /**
     *
     */
    public void dbChange() {
        TableData tableData = this.chooseTable.createSelectTableData();
        String[] columnNames = DesignTableDataManager.getColumnNamesByTableData(tableData).toArray(new String[0]);
        ColumnNameEditor columnNameEditor1 = new ColumnNameEditor(columnNames);
        columnNameEditor1.addItemListener(itemListener);
        ColumnIndexEditor columnIndexEditor1 = new ColumnIndexEditor(columnNames.length);
        columnIndexEditor1.addItemListener(itemListener);
        String columnNameValue = columnNames.length > 0 ? columnNames[0] : StringUtils.EMPTY;
        keyColumnPane.setEditors(new Editor[]{columnNameEditor1, columnIndexEditor1}, columnNameValue);

        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        formulaEditor.setEnabled(true);
        formulaEditor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (itemListener != null) {
                    itemListener.itemStateChanged(new MyItemEvent(new UIComboBox(), 0, ItemEvent.SELECTED));
                }
            }
        });
        ColumnNameEditor columnNameEditor2 = new ColumnNameEditor(columnNames);
        columnNameEditor2.addItemListener(itemListener);
        ColumnIndexEditor columnIndexEditor2 = new ColumnIndexEditor(columnNames.length);
        columnIndexEditor2.addItemListener(itemListener);
        valueDictPane.setEditors(new Editor[]{columnNameEditor2, columnIndexEditor2, formulaEditor}, columnNameValue);
        if (uiObserverListener != null) {
            uiObserverListener.doChange();
        }
    }

    /**
     *
     */
    public void preview() {
        if (this.valueDictPane.update() instanceof Integer) {
            this.chooseTable.preview((Integer) keyColumnPane.update() - 1, (Integer) valueDictPane.update() - 1);
        } else {
            int key = keyColumnPane.update() == null ? -1 : (Integer) keyColumnPane.update() - 1;
            this.chooseTable.preview(key, -1);
        }
    }

    @Override
    /**
     *
     */
    public boolean accept(Object ob) {
        return ob instanceof DatabaseDictionary;
    }

    @Override
    /**
     *
     */
    public void reset() {
        keyColumnPane.clearComponentsData();
        valueDictPane.clearComponentsData();
        chooseTable.resetComponets();
    }

    @Override
    /**
     *
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    @Override
    /**
     *
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }


    public class MyItemEvent extends ItemEvent {
        public MyItemEvent(ItemSelectable source, int id, int stateChange) {
            super(source, id, null, stateChange);
        }
    }
}