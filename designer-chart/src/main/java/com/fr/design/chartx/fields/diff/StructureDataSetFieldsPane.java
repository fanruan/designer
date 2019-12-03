package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.StructureColumnFieldCollection;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;

import java.awt.Component;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-09-02
 */
public class StructureDataSetFieldsPane extends AbstractDataSetFieldsPane<StructureColumnFieldCollection> {

    private UIComboBox nodeName;
    private UIComboBox nodeId;
    private UIComboBox parentId;
    private UITextField seriesName;
    private UIComboBox nodeValue;
    private CalculateComboBox calculateCombox;

    @Override
    protected void initComponents() {
        nodeName = new UIComboBox();
        nodeId = new UIComboBox();
        parentId = new UIComboBox();
        seriesName = new UITextField();
        nodeValue = new UIComboBox();
        calculateCombox = new CalculateComboBox();

        initValueAndCalComboBox(nodeValue, calculateCombox);
        super.initComponents();
    }

    @Override
    protected String[] fieldLabels() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Node_Name"),
                "id",
                Toolkit.i18nText("Fine-Design_Chart_Parent_ID"),
                Toolkit.i18nText("Fine-Design_Chart_MultiPie_Series_Name"),
                Toolkit.i18nText("Fine-Design_Chart_Series_Value"),
                Toolkit.i18nText("Fine-Design_Chart_Summary_Method")
        };
    }

    @Override
    protected UIComboBox[] filedComboBoxes() {
        return new UIComboBox[]{
                nodeName,
                nodeId,
                parentId,
                nodeValue
        };
    }

    @Override
    protected Component[] fieldComponents() {
        return new Component[]{
                nodeName,
                nodeId,
                parentId,
                seriesName,
                nodeValue,
                calculateCombox
        };
    }

    @Override
    public void populateBean(StructureColumnFieldCollection ob) {
        seriesName.setText(ob.getSeriesName());
        populateField(nodeName, ob.getNodeName());
        populateField(nodeId, ob.getNodeId());
        populateField(parentId, ob.getParentId());
        populateFunctionField(nodeValue, calculateCombox, ob.getNodeValue());
    }

    @Override
    public StructureColumnFieldCollection updateBean() {
        StructureColumnFieldCollection result = new StructureColumnFieldCollection();
        result.setSeriesName(seriesName.getText());
        updateField(nodeName, result.getNodeName());
        updateField(nodeId, result.getNodeId());
        updateField(parentId, result.getParentId());
        updateFunctionField(nodeValue, calculateCombox, result.getNodeValue());
        return result;
    }
}
