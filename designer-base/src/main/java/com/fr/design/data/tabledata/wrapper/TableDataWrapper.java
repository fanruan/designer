package com.fr.design.data.tabledata.wrapper;

import com.fr.base.TableData;
import com.fr.design.gui.itree.refreshabletree.loader.ChildrenNodesLoader;

import javax.swing.*;
import java.util.List;

/**
 * 将TableData包装下 用以区分模板数据集、服务器数据集、存储过程数据集
 * 定义了操作TableData的一些要用的方法：tabledata的名字、图标、产生的列名集合、预览数据集等
 * 注意现在存储过程StoreProcedure本质上不是TableData
 * !!Notice:不支持对数据集的修改、重命名等改变TableData的操作。一个既定的数据集
 * （确定的SQL查询语句），对应相应的TableDataWrappe。 如果数据集变化了，那么TableDataWrappe也会重新生成。
 * 用以保证>>>>>>>>calculateColumnNameList()只计算一次，而且是正确的<<<<<<
 * 
 * @author zhou
 * @since 2012-3-28下午9:51:49
 */
public interface TableDataWrapper extends ChildrenNodesLoader {

	/**
	 * 数据集名字
	 * 
	 * @return
	 */
	public String getTableDataName();

    /**
     * TableData
     *
     * @return
     */
    public TableData getTableData();

    /**
	 * 数据集图标
	 * 
	 * @return
	 */
	public Icon getIcon();

	/**
	 * 数据集执行结果返回的所有字段
	 * 
	 * TODO:要不要加上Exception呢？个人感觉很有必要
	 * @return
	 */
	public List<String> calculateColumnNameList();

	/**
	 * 预览数据集
	 */
	public void previewData();

	/**
	 * 预览数据集，带有显示值和实际值的标记结果
	 */
	public void previewData(final int keyIndex, final int valueIndex);

	/**
	 * 是否异常 TODO:这个应该和calculateColumnNameList方法结合在一起
	 * 
	 * @return
	 */
	public boolean isUnusual();

}