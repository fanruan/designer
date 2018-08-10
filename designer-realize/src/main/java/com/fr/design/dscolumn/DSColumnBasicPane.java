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

import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DSColumnBasicPane extends BasicPane {

    private SelectedDataColumnPane selectDataColumnPane;
    private ConditionParentPane conditionParentPane;
    private ResultSetGroupPopUpPane resultSetGroupPane;
    private ExpandDirectionPane expandDirectionPane;
    private CellElement cellElement;

    private ActionListener summaryDirectionActionlistener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            if (expandDirectionPane != null) {
                expandDirectionPane.setNoneRadioButtonSelected(true);
            }
        }
    };
    private ActionListener othergroupDirectionActionlistener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            if (expandDirectionPane != null) {
                expandDirectionPane.setNoneRadioButtonSelected(false);
            }
        }
    };
    private ActionListener sdcupdateActionlistener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            selectDataColumnPane.update(cellElement);
        }
    };

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

        selectDataColumnPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Select_DataColumn"), null));

        if (setting > DSColumnPane.SETTING_DSRELATED) {
            conditionParentPane = new ConditionParentPane();
            conditionParentPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("ParentCell_Setting"), null));
        }

        resultSetGroupPane = new ResultSetGroupPopUpPane(setting > DSColumnPane.SETTING_DSRELATED);
        resultSetGroupPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Setting"), null));

        if (setting > DSColumnPane.SETTING_DSRELATED) {
            expandDirectionPane = new ExpandDirectionPane();
            expandDirectionPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Expand_Direction"), null));
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

        this.resultSetGroupPane.addListeners(summaryDirectionActionlistener, othergroupDirectionActionlistener, sdcupdateActionlistener);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Basic");
    }

    public void populate(TableDataSource source, TemplateCellElement cellElement) {
        if (cellElement == null) {
            return;
        }

        this.cellElement = cellElement;

        selectDataColumnPane.populate(source, cellElement, null);

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