package com.fr.design.mainframe;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.base.FRContext;
import com.fr.base.ScreenResolution;
import com.fr.design.designer.EditingState;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icontainer.UIModeControlContainer;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import com.fr.main.impl.WorkBook;
import com.fr.report.report.TemplateReport;

/**
 * 整个报表编辑区域 包括滚动条、中间的grid或者聚合块、下面的sheetTab
 *
 * @editor zhou
 * @since 2012-3-27下午12:12:05
 */
public class ReportComponentComposite extends JComponent {

    private static final int MAX = 400;
    private static final int HUND = 100;
    private static final int MIN = 10;
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
     *
     */
    public ReportComponentComposite(JWorkBook jwb) {
        this.parent = jwb;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(centerCardPane = new ReportComponentCardPane(), BorderLayout.CENTER);
        sheetNameTab = jwb.createSheetNameTabPane(this);
        sheetNameTab.setSelectedIndex(0);
        CellElementRegion = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(CellElementRegion, BorderLayout.NORTH);
        this.add(createSouthControlPane(), BorderLayout.SOUTH);
        jSliderContainer.getShowVal().getDocument().addDocumentListener(jSliderContainerListener);
    }

    DocumentListener jSliderContainerListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            double value = Integer.parseInt(jSliderContainer.getShowVal().getText().substring(0, jSliderContainer.getShowVal().getText().indexOf("%")));
            value = value>MAX ? MAX : value;
            value = value<MIN ? MIN : value;
            int resolution =  (int) (ScreenResolution.getScreenResolution()*value/HUND);
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setScale(resolution);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
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
            FRContext.getLogger().error(Inter.getLocText("FR-Designer_Read_failure") + "!");
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
            }
        } else {
            while (templateStateList.size() <= newIndex) {
                templateStateList.add(null);
            }
            centerCardPane.editingComponet.setSelection(centerCardPane.editingComponet.getDefaultSelectElement());
        }

        if (centerCardPane.editingComponet.elementCasePane == null) {
            return;
        }
        Grid grid = centerCardPane.editingComponet.elementCasePane.getGrid();

        if (!grid.hasFocus() && grid.isRequestFocusEnabled()) {
            grid.requestFocus();
        }
    }

    /**
	 * 移除选中状态
	 * 
	 * @date 2015-2-5-上午11:41:44
	 * 
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
//        hbarContainer = FRGUIPaneFactory.createBorderLayout_S_Pane();
//        hbarContainer.add(createSouthControlPaneWithJSliderPane());
        hbarContainer = FRGUIPaneFactory.createBorderLayout_S_Pane();
        hbarContainer.add(centerCardPane.editingComponet.getHorizontalScrollBar());
//        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sheetNameTab, hbarContainer);
        JPanel southPane = new JPanel(new BorderLayout());
        jSliderContainer = JSliderPane.getInstance();
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sheetNameTab, jSliderContainer);
        splitpane.setBorder(null);
        splitpane.setDividerSize(3);
        splitpane.setResizeWeight(1);
        southPane.add(hbarContainer,BorderLayout.NORTH);
        southPane.add(splitpane,BorderLayout.CENTER);
        return southPane;
    }

    private JComponent createSouthControlPaneWithJSliderPane() {
        hbarContainer = FRGUIPaneFactory.createBorderLayout_S_Pane();
        hbarContainer.add(centerCardPane.editingComponet.getHorizontalScrollBar());
        JSplitPane splitWithJSliderPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, hbarContainer, JSliderPane.getInstance());
        splitWithJSliderPane.setBorder(null);
        splitWithJSliderPane.setDividerLocation(0.9);
        splitWithJSliderPane.setDividerSize(3);
        splitWithJSliderPane.setResizeWeight(1);
        return splitWithJSliderPane;
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
}