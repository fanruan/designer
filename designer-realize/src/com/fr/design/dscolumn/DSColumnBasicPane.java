package com.fr.design.dscolumn;

import com.fr.data.TableDataSource;
import com.fr.design.dialog.BasicPane;
import com.fr.design.expand.ConditionParentPane;
import com.fr.design.expand.ExpandDirectionPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSColumnBasicPane extends BasicPane {

    private SelectedDataColumnPane selectDataColumnPane;
    private ConditionParentPane conditionParentPane;
    private ResultSetGroupPopUpPane resultSetGroupPane;
    private ExpandDirectionPane expandDirectionPane;
    private CellElement cellElement;

    public DSColumnBasicPane() {
        this(DSColumnPane.SETTING_ALL);
    }

    public DSColumnBasicPane(int setting) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        if (setting > DSColumnPane.SETTING_DSRELATED) {
            selectDataColumnPane = new SelectedDataColumnPane();
        } else {
            selectDataColumnPane = new SelectedConfirmedDataColumnPane();
        }

        selectDataColumnPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Select_DataColumn"), null));

        if (setting > DSColumnPane.SETTING_DSRELATED) {
            conditionParentPane = new ConditionParentPane();
            conditionParentPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("ParentCell_Setting"), null));
        }

        resultSetGroupPane = new ResultSetGroupPopUpPane(setting > DSColumnPane.SETTING_DSRELATED);
        resultSetGroupPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Data_Setting"), null));

        if (setting > DSColumnPane.SETTING_DSRELATED) {
            expandDirectionPane = new ExpandDirectionPane();
            expandDirectionPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("ExpandD-Expand_Direction"), null));
        }

        double[] rowSize = {TableLayout.PREFERRED, TableLayout.PREFERRED,
                TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] columnSize = {TableLayout.FILL};

        Component[][] components = null;

        if (setting > DSColumnPane.SETTING_DSRELATED) {
            components = new Component[][]{
                    {selectDataColumnPane},
                    {conditionParentPane},
                    {resultSetGroupPane},
                    {expandDirectionPane}
            };
        } else {
            components = new Component[][]{
                    {selectDataColumnPane},
                    {resultSetGroupPane},
            };
        }

        this.add(TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize), BorderLayout.CENTER);

        this.resultSetGroupPane.addListeners(summary_direction_ActionListener, otherGroup_direction_ActionListener, sdcUpdate_ActionListener);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Basic");
    }

    public void populate(TableDataSource source, TemplateCellElement cellElement) {
        if (cellElement == null) {
            return;
        }

        this.cellElement = cellElement;

        selectDataColumnPane.populate(source, cellElement);

        CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
        if (conditionParentPane != null) {
            conditionParentPane.populate(cellExpandAttr);
        }
        if (expandDirectionPane != null) {
            expandDirectionPane.populate(cellExpandAttr);
        }

        resultSetGroupPane.populate(cellElement);

        if (expandDirectionPane != null && resultSetGroupPane.isSummaryRadioButtonSelected()) {
            expandDirectionPane.setNoneRadioButtonSelected(true);
        }
    }

    public void update(TemplateCellElement cellElement) {
        if (cellElement == null) {
            return;
        }

        selectDataColumnPane.update(cellElement);

        // 设置父格和扩展方向
        CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();

        if (cellExpandAttr == null) {
            cellExpandAttr = new CellExpandAttr();
            cellElement.setCellExpandAttr(cellExpandAttr);
        }

        if (conditionParentPane != null) {
            conditionParentPane.update(cellExpandAttr);
        }
        if (expandDirectionPane != null) {
            expandDirectionPane.update(cellExpandAttr);
        }
        resultSetGroupPane.update();
    }

    ActionListener summary_direction_ActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            if (expandDirectionPane != null) {
                expandDirectionPane.setNoneRadioButtonSelected(true);
            }
        }
    };
    ActionListener otherGroup_direction_ActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            if (expandDirectionPane != null) {
                expandDirectionPane.setNoneRadioButtonSelected(false);
            }
        }
    };
    ActionListener sdcUpdate_ActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            selectDataColumnPane.update(cellElement);
        }
    };

    public void putElementcase(ElementCasePane t) {
        if (conditionParentPane != null) {
            conditionParentPane.putElementcase(t);
        }
    }

    public void putCellElement(TemplateCellElement tplCE) {
        if (conditionParentPane != null) {
            conditionParentPane.putCellElement(tplCE);
        }
    }
}