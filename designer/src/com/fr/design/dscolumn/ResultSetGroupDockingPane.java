package com.fr.design.dscolumn;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;


import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.FunctionComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.core.group.CustomGrouper;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.cellattr.core.group.FunctionGrouper;
import com.fr.report.cell.cellattr.core.group.RecordGrouper;
import com.fr.report.cell.cellattr.core.group.SummaryGrouper;
import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * 这个pane是选中数据列后，在上方QuickRegion处显示的pane
 *
 * @author zhou
 */
public class ResultSetGroupDockingPane extends ResultSetGroupPane {
    private static final int BIND_GROUP = 0;
    private static final int BIND_SELECTED = 1;
    private static final int BIND_SUMMARY = 2;

    private UIButton advancedButton;
    private FunctionComboBox functionComboBox;
    private JPanel cardPane;
    private CardLayout cardLayout;
    private UIComboBox goBox;

    ItemListener l;

    public ResultSetGroupDockingPane(ElementCasePane ePane) {
        super();
        this.initComponents(ePane);
    }

    public void initComponents(ElementCasePane ePane) {
        goBox = new UIComboBox(new String[]{Inter.getLocText("BindColumn-Group"), Inter.getLocText("BindColumn-Select"), Inter.getLocText("BindColumn-Summary")});
        initCardPane();
        JPanel pane = layoutPane();
        this.setLayout(new BorderLayout());
        this.add(pane, BorderLayout.CENTER);
    }

    private JPanel layoutPane() {
        double vs = 4;
        double vg = 6;
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        Component[][] components = new Component[][]
                {
                        new Component[]{new UILabel(Inter.getLocText("Data_Setting")), goBox},
                        new Component[]{cardPane, null}
                };
        goBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ee) {
                checkButtonEnabled();
                int i = goBox.getSelectedIndex();
                if (i == BIND_GROUP) {
                    cardLayout.show(cardPane, "groupPane");
                } else if (i == BIND_SELECTED) {
                    cardLayout.show(cardPane, "listPane");
                } else if (i == BIND_SUMMARY) {
                    cardLayout.show(cardPane, "summaryPane");
                    CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
                    cellExpandAttr.setDirection(Constants.NONE);
                }
            }
        });

        double[] columnSize = {p, f};
        double[] rowSize = {p, p};
        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    private void initCardPane() {
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardLayout = new CardLayout();
        cardPane.setLayout(cardLayout);

        JPanel pane = new JPanel(new BorderLayout(3, 0));
        groupComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                checkButtonEnabled();
            }
        });
        advancedButton = new UIButton(Inter.getLocText("Custom"));
        advancedButton.addActionListener(groupAdvancedListener);
        pane.add(groupComboBox, BorderLayout.WEST);
        pane.add(advancedButton, BorderLayout.CENTER);
        cardPane.add(pane, "groupPane");

        cardPane.add(new JPanel(), "listPane");

        cardPane.add(functionComboBox = new FunctionComboBox(GUICoreUtils.getFunctionArray()), "summaryPane");
    }

    @Override
    public void populate(TemplateCellElement cellElement) {
        this.cellElement = cellElement;

        if (isNPE(cellElement)) return;
        DSColumn dSColumn = (DSColumn) cellElement.getValue();

        // populate groupPane
        // RecordGrouper
        recordGrouper = dSColumn.getGrouper();
        if (recordGrouper instanceof FunctionGrouper && !((FunctionGrouper) recordGrouper).isCustom()) {
            int mode = ((FunctionGrouper) recordGrouper).getDivideMode();
            if (mode == FunctionGrouper.GROUPING_MODE) {
                cardLayout.show(cardPane, "groupPane");
                this.goBox.setSelectedIndex(BIND_GROUP);
                this.groupComboBox.setSelectedIndex(COMMON);
            } else if (mode == FunctionGrouper.CONTINUUM_MODE) {
                cardLayout.show(cardPane, "groupPane");
                this.goBox.setSelectedIndex(BIND_GROUP);
                this.groupComboBox.setSelectedIndex(CONTINUUM);
            } else if (mode == FunctionGrouper.LIST_MODE) {
                cardLayout.show(cardPane, "listPane");
                this.goBox.setSelectedIndex(BIND_SELECTED);
            }
        } else if (recordGrouper instanceof FunctionGrouper && ((FunctionGrouper) recordGrouper).isCustom()) {
            // 这种情况也放到自定义分组里面
            cardLayout.show(cardPane, "groupPane");
            this.goBox.setSelectedIndex(BIND_GROUP);
            this.groupComboBox.setSelectedIndex(ADVANCED);
        } else if (recordGrouper instanceof SummaryGrouper) {
            cardLayout.show(cardPane, "summaryPane");
            this.goBox.setSelectedIndex(BIND_SUMMARY);
            this.functionComboBox.setFunction(((SummaryGrouper) recordGrouper).getFunction());
        } else if (recordGrouper instanceof CustomGrouper) {
            // 自定义分组 or 高级分组
            cardLayout.show(cardPane, "groupPane");
            this.goBox.setSelectedIndex(BIND_GROUP);
            this.groupComboBox.setSelectedIndex(ADVANCED);
        }

        checkButtonEnabled();
    }

    @Override
    public void update() {
        if (isNPE(cellElement)) return;
        DSColumn dSColumn = (DSColumn) cellElement.getValue();

        if (this.goBox.getSelectedIndex() == BIND_GROUP) {
            recordGrouper = updateGroupCombox();
        } else if (this.goBox.getSelectedIndex() == BIND_SELECTED) {
            FunctionGrouper valueGrouper = new FunctionGrouper();
            valueGrouper.setDivideMode(FunctionGrouper.LIST_MODE);
            valueGrouper.setCustom(false);
            recordGrouper = valueGrouper;
        } else if (this.goBox.getSelectedIndex() == BIND_SUMMARY) {
            SummaryGrouper summaryGrouper = new SummaryGrouper();
            summaryGrouper.setFunction(functionComboBox.getFunction());
            recordGrouper = summaryGrouper;
        }

        dSColumn.setGrouper(recordGrouper);
    }

    private void checkButtonEnabled() {
        advancedButton.setEnabled(false);
        functionComboBox.setEnabled(false);
        groupComboBox.setEnabled(false);
        if (this.goBox.getSelectedIndex() == BIND_SUMMARY) {
            functionComboBox.setEnabled(true);
        }
        if (this.goBox.getSelectedIndex() == BIND_GROUP) {
            groupComboBox.setEnabled(true);
            if (groupComboBox.getSelectedIndex() == ADVANCED) {
                advancedButton.setEnabled(true);
            }
        }
    }

    public void addListener(ItemListener l) {
        goBox.addItemListener(l);
        groupComboBox.addItemListener(l);
        functionComboBox.addItemListener(l);
        this.l = l;
    }

    void fireTargetChanged() {
        l.itemStateChanged(null);
    }

    @Override
    public void setRecordGrouper(RecordGrouper recordGrouper) {
        this.recordGrouper = recordGrouper;
    }
}