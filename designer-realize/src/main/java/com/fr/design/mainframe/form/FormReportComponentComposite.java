package com.fr.design.mainframe.form;

import com.fr.base.DynamicUnitList;
import com.fr.base.ScreenResolution;
import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.cell.bar.DynamicScrollBar;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JSliderPane;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.grid.Grid;
import com.fr.grid.GridUtils;
import com.fr.report.ReportHelper;
import com.fr.report.worksheet.FormElementCase;
import com.fr.stable.AssistUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/**
 * 整个FormElementCase编辑区域 包括滚动条、中间的grid或者聚合块、下面的sheetTab
 */
public class FormReportComponentComposite extends JComponent implements TargetModifiedListener, FormECCompositeProvider {

    private static final int MAX = 400;
    private static final int HUND = 100;
    private static final int MIN = 10;
    private static final int DIR = 15;
    private static final double MIN_TIME = 0.4;
    public FormElementCaseDesigner elementCaseDesigner;
    private BaseJForm jForm;

    private FormTabPane sheetNameTab;
    private JPanel hbarContainer;
    private JSliderPane jSliderContainer;

    public FormReportComponentComposite(BaseJForm jform, FormElementCaseDesigner elementCaseDesign, FormElementCaseContainerProvider ecContainer) {
        this.jForm = jform;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.elementCaseDesigner = elementCaseDesign;
        this.add(elementCaseDesigner, BorderLayout.CENTER);
        sheetNameTab = new FormTabPane(ecContainer, jform);
        this.add(createSouthControlPane(), BorderLayout.SOUTH);
        jSliderContainer.getShowVal().addChangeListener(showValSpinnerChangeListener);
        jSliderContainer.getSelfAdaptButton().addItemListener(selfAdaptButtonItemListener);
        this.elementCaseDesigner.elementCasePane.getGrid().addMouseWheelListener(showValSpinnerMouseWheelListener);
        elementCaseDesigner.addTargetModifiedListener(this);
    }

    MouseWheelListener showValSpinnerMouseWheelListener = new MouseWheelListener() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (InputEventBaseOnOS.isControlDown(e)) {
                int dir = e.getWheelRotation();
                int old_resolution = (int) jSliderContainer.getShowVal().getValue();
                jSliderContainer.getShowVal().setValue(old_resolution - (dir * DIR));
            }
        }
    };


    ChangeListener showValSpinnerChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            double value = (int) ((UIBasicSpinner) e.getSource()).getValue();
            value = value > MAX ? MAX : value;
            value = value < MIN ? MIN : value;
            int resolution = (int) (ScreenResolution.getScreenResolution() * value / HUND);
            setScale(resolution);
//            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setScale(resolution);
        }
    };

    ItemListener selfAdaptButtonItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (jSliderContainer.getSelfAdaptButton().isSelected()) {
                int resolution = selfAdaptUpdate();
                jSliderContainer.getShowVal().setValue(resolution * HUND / ScreenResolution.getScreenResolution());
            }
        }
    };

    private java.util.List<TargetModifiedListener> targetModifiedList = new java.util.ArrayList<TargetModifiedListener>();

    private void setScale(int resolution) {
        ElementCasePane elementCasePane = elementCaseDesigner.getEditingElementCasePane();
        //网格线
        if (resolution < ScreenResolution.getScreenResolution() * MIN_TIME) {
            elementCasePane.getGrid().setShowGridLine(false);
        } else {
            elementCasePane.getGrid().setShowGridLine(true);
        }
        elementCasePane.setResolution(resolution);
        elementCasePane.getGrid().getGridMouseAdapter().setResolution(resolution);
        elementCasePane.getGrid().setResolution(resolution);
        //更新Grid
        Grid grid = elementCasePane.getGrid();
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(elementCasePane.getEditingElementCase());
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(elementCasePane.getEditingElementCase());
        grid.setVerticalExtent(GridUtils.getExtentValue(0, rowHeightList, grid.getHeight(), resolution));
        grid.setHorizontalExtent(GridUtils.getExtentValue(0, columnWidthList, grid.getWidth(), resolution));
        elementCasePane.getGrid().updateUI();
        //更新Column和Row
        ((DynamicScrollBar) elementCasePane.getVerticalScrollBar()).setDpi(resolution);
        ((DynamicScrollBar) elementCasePane.getHorizontalScrollBar()).setDpi(resolution);
        elementCasePane.getGridColumn().setResolution(resolution);
        elementCasePane.getGridColumn().updateUI();
        elementCasePane.getGridRow().setResolution(resolution);
        elementCasePane.getGridRow().updateUI();
    }

    private int selfAdaptUpdate() {
        ElementCasePane elementCasePane = elementCaseDesigner.getEditingElementCasePane();
        ElementCasePane reportPane = elementCasePane.getGrid().getElementCasePane();
        int column = reportPane.getSelection().getSelectedColumns()[0];
        double columnLength = reportPane.getSelection().getSelectedColumns().length;
        double columnExtent = reportPane.getGrid().getHorizontalExtent();
        int row = reportPane.getSelection().getSelectedRows()[0];
        double rowLength = reportPane.getSelection().getSelectedRows().length;
        double rowExtent = reportPane.getGrid().getVerticalExtent();
        if (AssistUtils.equals(columnLength, 0d) || AssistUtils.equals(rowLength, 0d)) {
            return ScreenResolution.getScreenResolution();
        }
        double time = (columnExtent / columnLength) < (rowExtent / rowLength) ? (columnExtent / columnLength) : (rowExtent / rowLength);
        if (reportPane.isHorizontalScrollBarVisible()) {
            reportPane.getVerticalScrollBar().setValue(row);
            reportPane.getHorizontalScrollBar().setValue(column);
        }
        return (int) (time * elementCasePane.getGrid().getResolution());
    }

    /**
     * 添加目标改变的监听
     *
     * @param targetModifiedListener 目标改变事件
     */
    public void addTargetModifiedListener(TargetModifiedListener targetModifiedListener) {
        targetModifiedList.add(targetModifiedListener);
    }

    /**
     * 目标改变
     *
     * @param e 事件
     */
    public void targetModified(TargetModifiedEvent e) {
        for (TargetModifiedListener l : targetModifiedList) {
            l.targetModified(e);
        }
    }

    public void setEditingElementCase(FormElementCase formElementCase) {
        elementCaseDesigner.setTarget(formElementCase);
        fireTargetModified();
    }

    private JComponent createSouthControlPane() {
        JPanel southPane = new JPanel(new BorderLayout());
        hbarContainer = FRGUIPaneFactory.createBorderLayout_S_Pane();
        hbarContainer.add(elementCaseDesigner.getHorizontalScrollBar());
        jSliderContainer = JSliderPane.getInstance();

        southPane.add(hbarContainer, BorderLayout.NORTH);
        southPane.add(sheetNameTab, BorderLayout.CENTER);
        southPane.add(jSliderContainer, BorderLayout.EAST);
        return southPane;
    }

    /**
     * 停止编辑
     */
    public void stopEditing() {
        elementCaseDesigner.stopEditing();
    }

    public void setComposite() {
        DesignerContext.getDesignerFrame().resetToolkitByPlus((ToolBarMenuDockPlus) jForm);
        this.validate();
        this.repaint(40);
    }

    public void setSelectedWidget(FormElementCaseProvider fc) {
        if (fc != null) {
            elementCaseDesigner.setTarget(fc);
        }
    }

    /**
     * 模板更新
     */
    public void fireTargetModified() {
        jForm.fireTargetModified();
    }


}