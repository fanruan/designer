package com.fr.van.chart.structure.desinger.data;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.data.util.function.AbstractDataFunction;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.structure.data.StructureTableDefinition;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

/**
 * Created by shine on 2017/2/15.
 */
public class StructurePlotTableDataContentPane extends AbstractTableDataContentPane{
    private UIComboBox nodeName;
    private UIComboBox nodeId;
    private UIComboBox parenrId;
    private UITextField seriesName;
    private UIComboBox nodeValue;
    private CalculateComboBox calculateCombox;

    public StructurePlotTableDataContentPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, COMPONENT_WIDTH};
        double[] rowSize = {p, p, p, p, p, p};

        nodeName = new UIComboBox();
        nodeId = new UIComboBox();
        parenrId = new UIComboBox();
        seriesName = new UITextField();
        nodeValue = new UIComboBox();
        calculateCombox = new CalculateComboBox();

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Node_Name")), nodeName},
                new Component[]{new UILabel("id"), nodeId},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Parent_ID")), parenrId},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_MultiPie_Series_Name")), seriesName},
                new Component[]{new UILabel(Inter.getLocText("Chart-Series_Value")), nodeValue},
                new Component[]{new UILabel(Inter.getLocText("Chart-Summary_Method")), calculateCombox}
        };

        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components,rowSize,columnSize,24,6);
        panel.setBorder(BorderFactory.createEmptyBorder(0,24,0,15));
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(246,(int)this.getPreferredSize().getHeight()));
    }


    @Override
    public void populateBean(ChartCollection collection) {
        TopDefinitionProvider top = collection.getSelectedChart().getFilterDefinition();
        if (top instanceof StructureTableDefinition) {
            StructureTableDefinition definition = (StructureTableDefinition) top;

            seriesName.setText(definition.getSeriesName());

            combineCustomEditValue(nodeId, definition.getNodeID());
            combineCustomEditValue(parenrId, definition.getParentID());
            combineCustomEditValue(nodeName, definition.getNodeName());
            combineCustomEditValue(nodeValue, definition.getNodeValue());

            calculateCombox.populateBean((AbstractDataFunction) definition.getDataFunction());
        }
    }

    @Override
    public void updateBean(ChartCollection ob) {
        StructureTableDefinition definition = new StructureTableDefinition();
        ob.getSelectedChart().setFilterDefinition(definition);

        definition.setSeriesName(seriesName.getText());

        Object _id = nodeId.getSelectedItem();
        Object _parentid = parenrId.getSelectedItem();
        Object _nodename = nodeName.getSelectedItem();
        Object _nodevalue = nodeValue.getSelectedItem();

        if (_id != null) {
            definition.setNodeID(_id.toString());
        }
        if (_parentid != null) {
            definition.setParentID(_parentid.toString());
        }
        if(_nodename != null) {
            definition.setNodeName(_nodename.toString());
        }
        if(_nodevalue != null) {
            definition.setNodeValue(_nodevalue.toString());
        }

        definition.setDataFunction(calculateCombox.updateBean());
    }

    /**
     * 检查 某些Box是否可用
     *
     * @param hasUse 是否使用.
     */
    public void checkBoxUse(boolean hasUse) {
        nodeId.setEnabled(hasUse);
        parenrId.setEnabled(hasUse);
        nodeName.setEnabled(hasUse);
        nodeValue.setEnabled(hasUse);
    }

    /**
     * 清空所有的box设置
     */
    @Override
    public void clearAllBoxList() {
        clearBoxItems(nodeId);
        clearBoxItems(parenrId);
        clearBoxItems(nodeName);
        clearBoxItems(nodeValue);
    }

    @Override
    protected void refreshBoxListWithSelectTableData(List columnNameList) {
        refreshBoxItems(nodeId, columnNameList);
        refreshBoxItems(parenrId, columnNameList);
        refreshBoxItems(nodeName, columnNameList);
        refreshBoxItems(nodeValue, columnNameList);
    }
}
