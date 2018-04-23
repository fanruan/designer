/**
 * 
 */
package com.fr.design.report.share;

import com.fr.data.impl.EmbeddedTableData;
import com.fr.stable.StringUtils;

/**
 * 数据集混淆相关的信息
 * 
 * @author neil
 * 
 * @date: 2015-3-9-上午10:56:40
 */
public class ConfusionInfo {

	//数据集名
	private String tabledataName;
	//混淆的每一列的key
	private String[] confusionKeys;
	//列名
	private String[] columnNames;
	//列类型
	private Class[] colType;
	
	public ConfusionInfo(EmbeddedTableData tabledata, String tabledataName){
		this.tabledataName = tabledataName;
		
		int columnCount = tabledata.getColumnCount();
		this.confusionKeys = new String[columnCount];
		this.columnNames = new String[columnCount];
		this.colType = new Class[columnCount];
		for (int i = 0; i < columnCount; i++) {
			columnNames[i] = tabledata.getColumnName(i);
			confusionKeys[i] = StringUtils.EMPTY;
			colType[i] = tabledata.getColumnClass(i);
		}
		
	}
	
	public ConfusionInfo(String tabledataName, String[] confusionKeys, String[] columnNames, Class[] colType) {
		this.tabledataName = tabledataName;
		this.confusionKeys = confusionKeys;
		this.columnNames = columnNames;
		this.colType = colType;
	}
	
	public String getTabledataName() {
		return tabledataName;
	}

	public void setTabledataName(String tabledataName) {
		this.tabledataName = tabledataName;
	}

	public String[] getConfusionKeys() {
		return confusionKeys;
	}

	public void setConfusionKeys(String[] confusionKeys) {
		this.confusionKeys = confusionKeys;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public Class[] getColType() {
		return colType;
	}

	public void setColType(Class[] colType) {
		this.colType = colType;
	}
	
	/**
	 * 指定的列是否是数字类型的
	 * 
	 * @param col 指定的列
	 * 
	 * @return 指定的列是否是数字类型的
	 * 
	 */
	public boolean isNumberColumn(int col){
		return colType[col] == Integer.class ||
				colType[col] == Double.class ||
				colType[col] == Float.class;
	}
	
}