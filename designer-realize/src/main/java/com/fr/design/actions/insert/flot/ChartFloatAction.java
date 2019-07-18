/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.flot;

import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.base.Style;
import com.fr.base.chart.BaseChartCollection;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.module.DesignModuleFactory;
import com.fr.grid.Grid;
import com.fr.grid.selection.FloatSelection;
import com.fr.log.FineLoggerFactory;
import com.fr.report.ReportHelper;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.Constants;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.OLDPIX;

import javax.swing.KeyStroke;
import java.awt.Color;

/**
 * 图表插入悬浮元素的操作.
 */
public class ChartFloatAction extends ElementCaseAction {

    private boolean isRecordNeeded;

    /**
     * 构造函数 图表插入悬浮元素
     */
    public ChartFloatAction(ElementCasePane t) {
        super(t);
        this.setMenuKeySet(FLOAT_INSERT_CHART);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/chart.png"));
    }

    public static final MenuKeySet FLOAT_INSERT_CHART = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'C';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Insert_Chart");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    /**
     * 执行插入悬浮元素操作, 并返回true, 需要记录撤销.
     *
     * @return 是则返回true
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        isRecordNeeded = false;
        final ElementCasePane reportPane = (ElementCasePane) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getCurrentElementCasePane();
        if (reportPane == null) {
            return isRecordNeeded;
        }

        reportPane.stopEditing();

        final BaseChartCollection cc = (BaseChartCollection) StableFactory.createXmlObject(BaseChartCollection.XML_TAG);
        final MiddleChartDialog chartDialog = DesignModuleFactory.getChartDialog(DesignerContext.getDesignerFrame());
        chartDialog.populate(cc);
        chartDialog.addDialogActionListener(new DialogActionAdapter() {
            @Override
            public void doOk() {
                isRecordNeeded = true;
                FloatElement newFloatElement;
                try {
                    newFloatElement = new FloatElement(chartDialog.getChartCollection().clone());
                    newFloatElement.setWidth(new OLDPIX(BaseChartCollection.CHART_DEFAULT_WIDTH));
                    newFloatElement.setHeight(new OLDPIX(BaseChartCollection.CHART_DEFAULT_HEIGHT));

                    Grid grid = reportPane.getGrid();
                    TemplateElementCase report = reportPane.getEditingElementCase();
                    DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
                    DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
                    int horizentalScrollValue = grid.getHorizontalValue();
                    int verticalScrollValue = grid.getVerticalValue();

                    int resolution = grid.getResolution();
                    int floatWdith = newFloatElement.getWidth().toPixI(resolution);
                    int floatHeight = newFloatElement.getWidth().toPixI(resolution);

                    int leftDifference = (grid.getWidth() - floatWdith) > 0 ? (grid.getWidth() - floatWdith) : 0;
                    int topDifference = (grid.getHeight() - floatHeight) > 0 ? (grid.getHeight() - floatHeight) : 0;
                    FU evtX_fu = FU.valueOfPix((leftDifference) / 2, resolution);
                    FU evtY_fu = FU.valueOfPix((topDifference) / 2, resolution);

                    FU leftDistance = FU.getInstance(evtX_fu.toFU() + columnWidthList.getRangeValue(0, horizentalScrollValue).toFU());
                    FU topDistance = FU.getInstance(evtY_fu.toFU() + rowHeightList.getRangeValue(0, verticalScrollValue).toFU());

                    newFloatElement.setLeftDistance(leftDistance);
                    newFloatElement.setTopDistance(topDistance);

                    Style style = newFloatElement.getStyle();
                    if (style != null) {
                        newFloatElement.setStyle(style.deriveBorder(Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black));
                    }
                    reportPane.getEditingElementCase().addFloatElement(newFloatElement);
                    reportPane.setSelection(new FloatSelection(newFloatElement.getName()));
                    reportPane.fireSelectionChangeListener();
                } catch (CloneNotSupportedException e) {
                    FineLoggerFactory.getLogger().error("Error in Float");
                }
            }
        });

        chartDialog.setVisible(true);
        return isRecordNeeded;
    }
}
