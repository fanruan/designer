package com.fr.design.data.datapane;

import com.fr.stable.StringUtils;

/**
 * 
 * @author zhou
 * @since 2012-7-11下午5:11:31
 */
public class DataBaseItems {
	private String databaseName;
	private String schemaName;
	private String tableName;

	public DataBaseItems() {
		this(null, null, null);
	}

	public DataBaseItems(String databaseName, String schemaName, String tableName) {
		this.setDatabaseName(databaseName);
		this.setSchemaName(schemaName);
		this.setTableName(tableName);
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName == null ? StringUtils.EMPTY : databaseName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName == null ? StringUtils.EMPTY : schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName == null ? StringUtils.EMPTY : tableName;
	}

}