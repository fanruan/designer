package com.fr.design.data.tabledata.wrapper;

import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.DesignModelAdapter;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.utils.DesignUtils;
import com.fr.general.ComparatorUtils;
import com.fr.stable.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractTableDataWrapper implements TableDataWrapper {
	protected TableData tabledata;
	private String name;
	private List<String> columnNameList;
	public AbstractTableDataWrapper(TableData tabledata) {
		this(tabledata,null);
	}

	public AbstractTableDataWrapper(TableData tabledata,String name) {
		this.tabledata = tabledata;
		this.name = name;
	}

	public TableData getTableData() {
		return tabledata;
	}

    /**
	 * 获取数据集的所有列名list
	 * 
	 * @return 数据集的所有列名list
	 * 
	 *
	 * @date 2014-11-24-下午3:51:41
	 * 
	 */
	public List<String> calculateColumnNameList() {
		if(columnNameList != null){
			return columnNameList;
		}
		
		DesignModelAdapter adapter = DesignModelAdapter.getCurrentModelAdapter();
		TableDataSource tds = adapter == null ? null : adapter.getBook();
		String[] colNames = tabledata.getColumnNames(tds);
		if(ArrayUtils.isNotEmpty(colNames)){
            columnNameList = new ArrayList<String>();
			columnNameList.addAll(Arrays.asList(colNames));
			return columnNameList;
		}
		
		EmbeddedTableData embeddedTableData = null;
		try {
			embeddedTableData = DesignTableDataManager.previewTableDataNotNeedInputParameters(tds, tabledata, TableData.RESULT_NOT_NEED, false);
		} catch (Exception e) {
            if (e.getMessage()!=null) {
                DesignUtils.errorMessage(e.getMessage());
            }
		}
		columnNameList = DesignTableDataManager.getColumnNamesByTableData(embeddedTableData);
        DesignTableDataManager.addDsColumnNames(name, columnNameList.toArray(new String[0]));
		return columnNameList;
	}

    /**
	 * 生成子节点
	 * 
	 * @return 生成子节点
	 * 
	 *
	 * @date 2014-11-24-下午3:51:17
	 * 
	 */
	public ExpandMutableTreeNode[] load() {
		List<String> namelist = calculateColumnNameList();
		ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[namelist.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = new ExpandMutableTreeNode(namelist.get(i));
		}

		return res;
	}

	/**
	 * 预览数据集
	 * 
	 *
	 * @date 2014-11-24-下午3:50:20
	 * 
	 */
	public void previewData() {
		PreviewTablePane.previewTableData(tabledata);
	}

    /**
	 * 预览数据集,带有显示值和实际值的标记结果
	 * 
	 * @param keyIndex 实际值
	 * @param valueIndex 显示值
	 * 
	 *
	 * @date 2014-11-24-下午3:50:20
	 * 
	 */
	public void previewData(final int keyIndex,final int valueIndex){
		PreviewTablePane.previewTableData(tabledata, keyIndex, valueIndex);
	}
	@Override
	public String getTableDataName(){
		return name; 
	}

	/**
	 * 获取数据集的面板
	 * 
	 * @return 数据集面板
	 * 
	 *
	 * @date 2014-11-24-下午3:50:00
	 * 
	 */
	public AbstractTableDataPane<?> creatTableDataPane() {
		return TableDataFactory.creatTableDataPane(tabledata, name);
	}

    public boolean equals (Object obj) {
        return obj instanceof AbstractTableDataWrapper
                && ComparatorUtils.equals(this.name, ((AbstractTableDataWrapper) obj).getTableDataName())
                && ComparatorUtils.equals(this.tabledata, ((AbstractTableDataWrapper) obj).getTableData());
    }

}