package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.TableData;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;

/**
 * 图表设计器导入 界面
 * Created by kunsnat on 14-10-21.
 * kunsnat@gmail.com
 */
public abstract class ChartDesignDataLoadPane extends BasicPane {

    private AbstractChartDataPane4Chart parentPane;

    public ChartDesignDataLoadPane(AbstractChartDataPane4Chart parentPane){
        this.parentPane = parentPane;
    }

    /**
     * 加载数据集
     *
     * @param tableData 数据集
     */
    public abstract void populateChartTableData(TableData tableData);

    /**
     * 根据界面 获取数据集相关.
     *
     * @return 返回数据集
     */
    public abstract TableData getTableData();


    protected abstract String getNamePrefix();

    //响应属性事件
    protected void fireChange() {
        parentPane.fireTableDataChange();
    }


    public TableDataWrapper getTableDataWrapper(){
        return new TemplateTableDataWrapper(getTableData());
    }
}