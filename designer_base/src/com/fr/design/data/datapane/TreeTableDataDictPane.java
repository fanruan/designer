package com.fr.design.data.datapane;

import com.fr.data.impl.RecursionTableData;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.preview.PreviewLabel;
import com.fr.design.data.datapane.preview.PreviewLabel.Previewable;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.ColumnIndexEditor;
import com.fr.design.editor.editor.ColumnNameEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.script.Calculator;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class TreeTableDataDictPane extends BasicPane implements Previewable {

    private UILabel selectTableDataLabel;
    protected TableDataComboBox tableDataNameComboBox;
    private UIRadioButton parentMarkRadio;
    private UIRadioButton lengthMarkRadio;
    private ButtonGroup markButtonGroup;

    UILabel originalMarkedFieldLabel1;
    UILabel parentMarkedFieldLabel1;
    UILabel treeDataFieldLabel1;
    UILabel originalMarkedFieldLabel2;
    UILabel treeDataFieldLabel2;

    private ValueEditorPane originalMarkedFieldPane1;
    private ValueEditorPane parentMarkedFieldPane1;
    private ValueEditorPane originalMarkedFieldPane2;

    public TreeTableDataDictPane() {
        this(StringUtils.EMPTY);
    }

    public TreeTableDataDictPane(String treeName) {
        this.setLayout(new BorderLayout(5, 30));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
        selectTableDataLabel = new UILabel(Inter.getLocText(new String[]{"Please_Select", "Single", "DS-TableData"}) + " :");
        setTableDataNameComboBox(treeName);
        tableDataNameComboBox.setPreferredSize(new Dimension(180, 20));
        JPanel tableFlowPane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        tableFlowPane.add(selectTableDataLabel);
        tableFlowPane.add(tableDataNameComboBox);
        tableDataNameComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                tdChange();
            }
        });
        tableFlowPane.add(new PreviewLabel(this));
        this.add(tableFlowPane, BorderLayout.NORTH);
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(centerPane, BorderLayout.CENTER);
        parentMarkRadio = new UIRadioButton(Inter.getLocText("FR-Designer_Build_Tree_Accord_Parent_Marked_Filed"), true);
        lengthMarkRadio = new UIRadioButton(Inter.getLocText("FR-Designer_Build_Tree_Accord_Marked_Filed_Length"));
        parentMarkRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isBuildByParentFiled()) {
                    makeParentEnable();
                    tdChange();
                }
            }
        });
        lengthMarkRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isBuildByParentFiled()) {
                    makeLengthEnable();
                    tdChange();
                }
            }
        });
        markButtonGroup = new ButtonGroup();
        markButtonGroup.add(parentMarkRadio);
        markButtonGroup.add(lengthMarkRadio);

        originalMarkedFieldLabel1 = new UILabel(Inter.getLocText("FR-Designer_Original_Marked_Filed") + " :", SwingConstants.RIGHT);
        parentMarkedFieldLabel1 = new UILabel("  " + Inter.getLocText("FR-Designer_Parent_Marked_Field") + " :", SwingConstants.RIGHT);
        treeDataFieldLabel1 = new UILabel("  " + Inter.getLocText("FR-Designer_Tree_Data_Field") + " :", SwingConstants.RIGHT);
        originalMarkedFieldLabel2 = new UILabel(Inter.getLocText("FR-Designer_Original_Marked_Filed") + " :", SwingConstants.RIGHT);
        treeDataFieldLabel2 = new UILabel("  " + Inter.getLocText("FR-Designer_Tree_Data_Field") + " :", SwingConstants.RIGHT);

//		originalMarkedFieldPane1 = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {new OldColumnIndexEditor(Inter.getLocText("Columns"))});
//		parentMarkedFieldPane1 = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {new OldColumnIndexEditor(Inter.getLocText("Columns"))});
//		originalMarkedFieldPane2 = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {new OldColumnIndexEditor(Inter.getLocText("Columns"))});
        originalMarkedFieldPane1 = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor()});
        parentMarkedFieldPane1 = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor()});
        originalMarkedFieldPane2 = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor()});

        makeParentEnable();

        JPanel p1 = createCenterFlowZeroGapBorderPane(originalMarkedFieldLabel1, originalMarkedFieldPane1);
        JPanel p2 = createCenterFlowZeroGapBorderPane(parentMarkedFieldLabel1, parentMarkedFieldPane1);
        JPanel border1 = new JPanel();
        border1.setLayout(new BorderLayout(0, 10));
        border1.add(p1, BorderLayout.NORTH);
        border1.add(p2, BorderLayout.CENTER);
        JPanel p4 = createCenterFlowZeroGapBorderPane(originalMarkedFieldLabel2, originalMarkedFieldPane2);
        JPanel border2 = new JPanel();
        border2.setLayout(new BorderLayout(0, 20));
        border2.add(p4, BorderLayout.NORTH);

        JPanel xx = FRGUIPaneFactory.createBorderLayout_S_Pane();
        xx.add(parentMarkRadio, BorderLayout.NORTH);
        xx.add(border1, BorderLayout.CENTER);
        JPanel xxx = FRGUIPaneFactory.createBorderLayout_S_Pane();
        xxx.add(lengthMarkRadio, BorderLayout.NORTH);
        xxx.add(border2, BorderLayout.CENTER);
        JPanel buildTreePanel = new JPanel(new BorderLayout(5, 30));
        buildTreePanel.add(xx, BorderLayout.NORTH);
        buildTreePanel.add(xxx, BorderLayout.CENTER);
        centerPane.add(buildTreePanel, BorderLayout.NORTH);
        JPanel previewPanel = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        UIButton treeDataPreviewButton = new UIButton(Inter.getLocText("FR-Designer_Preview"));
        previewPanel.add(treeDataPreviewButton);
        treeDataPreviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableDataWrapper tableDataWrappe = tableDataNameComboBox.getSelectedItem();
                if (tableDataWrappe == null) {
                    return;
                }
                RecursionTableData rtd = new RecursionTableData();
                rtd.setOriginalTableDataName(tableDataWrappe.getTableDataName());
                if (isBuildByParentFiled()) {
                    Object o = parentMarkedFieldPane1.update();
                    rtd.setParentmarkFields((Integer) o - 1 + "");
                    rtd.setParentmarkFieldName("" + o);
                    Object o2 = originalMarkedFieldPane1.update();
                    rtd.setMarkFields((Integer) o2 - 1 + "");
                    rtd.setMarkFieldName("" + o2);
                } else {
                    Object o = originalMarkedFieldPane2.update();
                    if (o == null) {
                        rtd.setMarkFields("-1");
                    } else {
                        rtd.setMarkFields((Integer) o - 1 + "");
                        rtd.setMarkFieldName("" + o);
                    }
                }
                rtd.setTableDataSource(DesignTableDataManager.getEditingTableDataSource());
                rtd.createDataModel(Calculator.createCalculator());
                PreviewTablePane.previewTableData(rtd);
            }
        });
        centerPane.add(previewPanel, BorderLayout.CENTER);
    }

    protected void setTableDataNameComboBox(String treeName) {
        tableDataNameComboBox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource(), treeName);

    }

    private void tdChange() {
        TableDataWrapper tableDataWrappe = this.tableDataNameComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            return;
        }
        ValueEditorPane[] valueEditorPanes;
        if (isBuildByParentFiled()) {
            valueEditorPanes = new ValueEditorPane[]{originalMarkedFieldPane1, parentMarkedFieldPane1};
        } else {
            valueEditorPanes = new ValueEditorPane[]{originalMarkedFieldPane2};
        }
        try {
            List<String> namelist = tableDataWrappe.calculateColumnNameList();
            int len = namelist.size();
            String[] columnNames = new String[len];
            namelist.toArray(columnNames);
            for (int i = 0; i < valueEditorPanes.length; i++) {
                valueEditorPanes[i].setEditors(new Editor[]{new ColumnNameEditor(columnNames), new ColumnIndexEditor(len)}, columnNames[0]);
            }
        } catch (Exception e) {
            for (int i = 0; i < valueEditorPanes.length; i++) {
                valueEditorPanes[i].setEditors(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor()}, 1);
            }
        } finally {
            valueEditorPanes = null;
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "TreeTableDataDictionay";
    }

    public RecursionTableData update() {
        RecursionTableData td = new RecursionTableData();
        if (tableDataNameComboBox.getSelectedItem() == null) {
            td.setOriginalTableDataName(null);
        } else {
            td.setOriginalTableDataName(tableDataNameComboBox.getSelectedItem().getTableDataName());
        }
        if (isBuildByParentFiled()) {
            Object o = parentMarkedFieldPane1.update(StringUtils.EMPTY);
            if (o instanceof Object[]) {
                Object[] temp = (Object[]) o;
                td.setParentmarkFields(((Integer) temp[0]).intValue() - 1 + "");
                td.setParentmarkFieldName((String) temp[1]);
            }
            Object o2 = originalMarkedFieldPane1.update(StringUtils.EMPTY);
            if (o2 instanceof Object[]) {
                Object[] temp = (Object[]) o2;
                td.setMarkFields(((Integer) temp[0]).intValue() - 1 + "");
                td.setMarkFieldName((String) temp[1]);
            }
        } else {
            Object o = originalMarkedFieldPane2.update(StringUtils.EMPTY);
            if (o == null || !(o instanceof Object[])) {
                td.setMarkFields("-1");
            } else {
                Object[] temp = (Object[]) o;
                td.setMarkFields(((Integer) temp[0]).intValue() - 1 + "");
                td.setMarkFieldName((String) temp[1]);
            }
        }
        td.setTableDataSource(DesignTableDataManager.getEditingTableDataSource());
        return td;
    }

    public void populate(RecursionTableData rtb) {
        if (StringUtils.isNotEmpty(rtb.getParentmarkFields())) {
            makeParentEnable();
            parentMarkRadio.setSelected(true);
            lengthMarkRadio.setSelected(false);
            tableDataNameComboBox.setSelectedTableDataByName(rtb.getOriginalTableDataName());
            if (StringUtils.isNotEmpty(rtb.getMarkFieldName())) {
                originalMarkedFieldPane1.populate(rtb.getMarkFieldName());
            } else {
                originalMarkedFieldPane1.populate(rtb.getMarkFieldIndex() + 1);
            }
            if (StringUtils.isNotEmpty(rtb.getParentmarkFieldName())) {
                parentMarkedFieldPane1.populate(rtb.getParentmarkFieldName());
            } else {
                parentMarkedFieldPane1.populate(rtb.getParentmarkFieldIndex() + 1);
            }
        } else {
            makeLengthEnable();
            lengthMarkRadio.setSelected(true);
            parentMarkRadio.setSelected(false);
            tableDataNameComboBox.setSelectedTableDataByName(rtb.getOriginalTableDataName());
            if (StringUtils.isNotEmpty(rtb.getMarkFieldName())) {
                originalMarkedFieldPane2.populate(rtb.getMarkFieldName());
            } else {
                originalMarkedFieldPane2.populate(rtb.getMarkFieldIndex() + 1);
            }
        }
    }

    /**
     *
     */
    public void preview() {
        TableDataWrapper tableDataWrappe = tableDataNameComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            return;
        }
        tableDataWrappe.previewData();
    }

    private JPanel createCenterFlowZeroGapBorderPane(Component p1, Component p2) {
        JPanel p = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        p.add(p1);
        p.add(p2);
        return p;
    }

    private boolean isBuildByParentFiled() {
        return parentMarkRadio.isSelected();
    }

    private void makeParentEnable() {
        originalMarkedFieldPane1.setEnabled(true);
        parentMarkedFieldPane1.setEnabled(true);
        originalMarkedFieldLabel1.setEnabled(true);
        parentMarkedFieldLabel1.setEnabled(true);
        treeDataFieldLabel1.setEnabled(true);
        originalMarkedFieldLabel2.setEnabled(false);
        treeDataFieldLabel2.setEnabled(false);
        originalMarkedFieldPane2.setEnabled(false);
    }

    private void makeLengthEnable() {
        originalMarkedFieldPane1.setEnabled(false);
        parentMarkedFieldPane1.setEnabled(false);
        originalMarkedFieldLabel1.setEnabled(false);
        parentMarkedFieldLabel1.setEnabled(false);
        treeDataFieldLabel1.setEnabled(false);
        originalMarkedFieldLabel2.setEnabled(true);
        treeDataFieldLabel2.setEnabled(true);
        originalMarkedFieldPane2.setEnabled(true);
    }
}