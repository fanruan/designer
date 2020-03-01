package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.StructureColumnFieldCollection;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;

import java.awt.Component;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-09-02
 */
public class StructureCellDataFieldsPane extends AbstractCellDataFieldsPane<StructureColumnFieldCollection> {

    private TinyFormulaPane nodeName;
    private TinyFormulaPane nodeId;
    private TinyFormulaPane parentId;
    private UITextField seriesName;
    private TinyFormulaPane nodeValue;

    @Override
    protected void initComponents() {
        seriesName = new UITextField();
        nodeName = new TinyFormulaPane();
        nodeId = new TinyFormulaPane();
        parentId = new TinyFormulaPane();
        nodeValue = new TinyFormulaPane();

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
        };
    }

    @Override
    protected TinyFormulaPane[] formulaPanes() {
        return new TinyFormulaPane[]{
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
        };
    }

    @Override
    public void populateBean(StructureColumnFieldCollection ob) {
        seriesName.setText(ob.getSeriesName());
        populateField(nodeName, ob.getNodeName());
        populateField(nodeId, ob.getNodeId());
        populateField(parentId, ob.getParentId());
        populateField(nodeValue, ob.getNodeValue());
    }

    @Override
    public StructureColumnFieldCollection updateBean() {
        StructureColumnFieldCollection result = new StructureColumnFieldCollection();
        result.setSeriesName(seriesName.getText());
        updateField(nodeName, result.getNodeName());
        updateField(nodeId, result.getNodeId());
        updateField(parentId, result.getParentId());
        updateField(nodeValue, result.getNodeValue());
        return result;
    }
}