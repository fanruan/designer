package com.fr.design.mainframe;

import com.fr.base.ScreenResolution;
import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.designer.EditingState;
import com.fr.design.event.RemoveListener;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icontainer.UIModeControlContainer;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.grid.Grid;
import com.fr.log.FineLoggerFactory;
import com.fr.main.impl.WorkBook;
import com.fr.report.report.TemplateReport;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

/**
 * 整个报表编辑区域 包括滚动条、中间的grid或者聚合块、下面的sheetTab
 *
 * @editor zhou
 * @since 2012-3-27下午12:12:05
 */
public class ReportComponentComposite extends JComponent implements RemoveListener {

    private static final int MAX = 400;
    private static final int HUND = 100;
    private static final int MIN = 10;
    private static final int DIR = 15;
    private JWorkBook parent;
    private UIModeControlContainer parentContainer = null;

    protected ReportComponentCardPane centerCardPane;
    private JPanel CellElementRegion;

    private java.util.List<EditingState> templateStateList = new ArrayList<EditingState>();

    private SheetNameTabPane sheetNameTab;

    private JPanel hbarContainer;

    private JSliderPane jSliderContainer;

    /**
     * Constructor with workbook..
     */
    public ReportComponentComposite(JWorkBook jwb) {
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.parent = jwb;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(centerCardPane = new ReportComponentCardPane(), BorderLayout.CENTER);
        sheetNameTab = jwb.createSheetNameTabPane(this);
        sheetNameTab.setSelectedIndex(0);
        CellElementRegion = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(CellElementRegion, BorderLayout.NORTH);
        this.add(createSouthControlPane(), BorderLayout.SOUTH);
        jSliderContainer.getShowVal().addChangeListener(showValSpinnerChangeListener);
        jSliderContainer.getSelfAdaptButton().addItemListener(selfAdaptButtonItemListener);
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
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setScale(resolution);
        }
    };

    ItemListener selfAdaptButtonItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (jSliderContainer.getSelfAdaptButton().isSelected()) {
                int resolution = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().selfAdaptUpdate();
                jSliderContainer.getShowVal().setValue(resolution * HUND / ScreenResolution.getScreenResolution());
            }
        }
    };

    protected void doBeforeChange(int oldIndex) {
        if (oldIndex >= 0) {
            templateStateList.set(oldIndex, centerCardPane.editingComponet.createEditingState());
        }
    }

    protected void doAfterChange(int newIndex) {
        WorkBook workbook = getEditingWorkBook();
        if (workbook == null) {
            FineLoggerFactory.getLogger().error(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Read_Failure") + "!");
            //AUGUST:加个报错,不然测试总是SB的认为打不开一个坏的excel文件就是BUG，也不知道去检查下源文件。
            return;
        }
        centerCardPane.populate(workbook.getTemplateReport(newIndex));
        if (parentContainer != null) {
            parentContainer.setDownPane(ReportComponentComposite.this);
        }

        if (templateStateList.size() > newIndex) {
            EditingState reportPaneEditState = templateStateList.get(newIndex);
            if (reportPaneEditState != null) {
                reportPaneEditState.revert();
                updateJSlider();
            }
        } else {
            while (templateStateList.size() <= newIndex) {
                templateStateList.add(null);
            }
            centerCardPane.editingComponet.setSelection(centerCardPane.editingComponet.getDefaultSelectElement());
            jSliderContainer=JSliderPane.getInstance();
            jSliderContainer.reset();
        }

        if (centerCardPane.editingComponet.elementCasePane == null) {
            centerCardPane.getPolyDezi().polyArea.addMouseWheelListener(showValSpinnerMouseWheelListener);
            return;
        }
        Grid grid = centerCardPane.editingComponet.elementCasePane.getGrid();

        this.centerCardPane.editingComponet.elementCasePane.getGrid().addMouseWheelListener(showValSpinnerMouseWheelListener);

        if (!grid.hasFocus() && grid.isRequestFocusEnabled()) {
            grid.requestFocus();
        }
    }

    private void updateJSlider(){
        centerCardPane.editingComponet.updateJSliderValue();
    }

    /**
     * 移除选中状态
     *
     * @date 2015-2-5-上午11:41:44
     */
    public void removeSelection() {
        if (centerCardPane.editingComponet instanceof WorkSheetDesigner) {
            ((WorkSheetDesigner) centerCardPane.editingComponet).removeSelection();
        } else {
            centerCardPane.populate(centerCardPane.editingComponet.getTemplateReport());
        }

    }

    public TemplateReport getEditingTemplateReport() {
        return centerCardPane.editingComponet.getTemplateReport();
    }

    public int getEditingIndex() {
        return sheetNameTab.getSelectedIndex();
    }

    public JSliderPane getjSliderContainer() {
        return this.jSliderContainer;
    }


    public void setParentContainer(UIModeControlContainer parentContainer) {
        this.parentContainer = parentContainer;
    }


    public void setComponents() {
        CellElementRegion.removeAll();
        hbarContainer.removeAll();
        hbarContainer.add(centerCardPane.editingComponet.getHorizontalScrollBar());
        centerCardPane.editingComponet.getHorizontalScrollBar().setValue(centerCardPane.editingComponet.getHorizontalScrollBar().getValue());
        centerCardPane.editingComponet.getVerticalScrollBar().setValue(centerCardPane.editingComponet.getVerticalScrollBar().getValue());
        this.doLayout();
    }

    public int getSelectedIndex() {
        return sheetNameTab.getSelectedIndex();
    }

    protected ReportComponent getEditingReportComponent() {
        return this.centerCardPane.editingComponet;
    }

    protected WorkBook getEditingWorkBook() {
        return this.parent.getTarget();
    }

    /**
     * 添加目标改变的监听
     *
     * @param targetModifiedListener 对象修改监听器
     */
    public void addTargetModifiedListener(TargetModifiedListener targetModifiedListener) {
        this.centerCardPane.addTargetModifiedListener(targetModifiedListener);
    }

    private JComponent createSouthControlPane() {
        hbarContainer = FRGUIPaneFactory.createBorderLayout_S_Pane();
        hbarContainer.add(centerCardPane.editingComponet.getHorizontalScrollBar());
        JPanel southPane = new JPanel(new BorderLayout());
        jSliderContainer = JSliderPane.getInstance();

        southPane.add(hbarContainer, BorderLayout.NORTH);
        southPane.add(sheetNameTab, BorderLayout.CENTER);
        southPane.add(jSliderContainer, BorderLayout.EAST);
        return southPane;
    }


    public void setSelectedIndex(int selectedIndex) {
        sheetNameTab.setSelectedIndex(selectedIndex);
        centerCardPane.populate(getEditingWorkBook().getTemplateReport(selectedIndex));
    }

    /**
     * 停止编辑
     */
    public void stopEditing() {
        centerCardPane.stopEditing();
    }

    public void setComposite() {
        DesignerContext.getDesignerFrame().resetToolkitByPlus(parent);
        parent.setComposite();
        this.validate();
        this.repaint(40);
    }

    /**
     * 模板更新
     */
    public void fireTargetModified() {
        parent.fireTargetModified();
    }

    @Override
    public void doRemoveAction() {
        sheetNameTab.doRemoveAction();
    }
}
