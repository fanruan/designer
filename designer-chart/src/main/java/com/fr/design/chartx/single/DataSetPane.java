package com.fr.design.chartx.single;

import com.fr.chartx.data.DataSetDefinition;
import com.fr.data.impl.NameTableData;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.stable.AssistUtils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by shine on 2019/5/21.
 */
public class DataSetPane extends FurtherBasicBeanPane<DataSetDefinition> {
    private static final int TABLE_DATA_LABEL_LINE_WRAP_WIDTH = 65;
    private static final int TABLE_DATA_PANE_WIDTH = 246;

    private DatabaseTableDataPane tableDataPane;

    private AbstractDataSetFieldsPane dataSetFieldsPane;

    public DataSetPane(AbstractDataSetFieldsPane dataSetFieldsPane) {
        initComps(dataSetFieldsPane);
    }

    private void initComps(AbstractDataSetFieldsPane dataSetFieldsPane) {
        UILabel label = new BoldFontTextLabel(Toolkit.i18nText("Fine-Design_Chart_Table_Data"));
        UIComponentUtils.setLineWrap(label, TABLE_DATA_LABEL_LINE_WRAP_WIDTH);
        UIComponentUtils.setPreferedWidth(label, ChartDataPane.LABEL_WIDTH);

        tableDataPane = new DatabaseTableDataPane(label) {
            @Override
            protected void userEvent() {
                refreshBoxList();
                checkBoxUse();
            }
        };
        tableDataPane.setPreferredSize(new Dimension(TABLE_DATA_PANE_WIDTH, tableDataPane.getPreferredSize().height));

        this.dataSetFieldsPane = dataSetFieldsPane;

        this.setLayout(new BorderLayout());
        this.add(tableDataPane, BorderLayout.NORTH);
        this.add(dataSetFieldsPane, BorderLayout.CENTER);
    }

    /**
     * 检查box是否可用.
     */
    public void checkBoxUse() {
        TableDataWrapper dataWrap = tableDataPane.getTableDataWrapper();

        if (dataSetFieldsPane != null) {
            dataSetFieldsPane.checkBoxUse(dataWrap != null);
        }
    }

    /**
     * 刷新字段下拉列表
     */
    private void refreshBoxList() {
        TableDataWrapper dataWrap = tableDataPane.getTableDataWrapper();

        if (dataWrap == null) {
            return;
        }

        List<String> columnNameList = dataWrap.calculateColumnNameList();

        if (dataSetFieldsPane != null) {
            dataSetFieldsPane.refreshBoxListWithSelectTableData(columnNameList);
        }
    }

    @Override
    public boolean accept(Object ob) {
        return ob instanceof DataSetDefinition;
    }

    @Override
    public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_TableData");
    }

    @Override
    public void reset() {
        this.removeAll();
    }

    @Override
    public void populateBean(DataSetDefinition ob) {
        if (ob == null || ob.getColumnFieldCollection() == null) {
            return;
        }

        refreshBoxList();
        checkBoxUse();

        tableDataPane.populateBean(ob.getNameTableData());
        Type dataType = ((ParameterizedType) dataSetFieldsPane.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (AssistUtils.equals(ob.getColumnFieldCollection().getClass(), dataType)) {
            dataSetFieldsPane.populateBean(ob.getColumnFieldCollection());
        }

    }

    @Override
    public DataSetDefinition updateBean() {
        DataSetDefinition dataSetDefinition = new DataSetDefinition();

        TableDataWrapper tableDataWrapper = tableDataPane.getTableDataWrapper();
        if (tableDataWrapper != null) {
            dataSetDefinition.setNameTableData(new NameTableData(tableDataWrapper.getTableDataName()));
        }

        dataSetDefinition.setColumnFieldCollection(dataSetFieldsPane.updateBean());

        return dataSetDefinition;
    }
}
