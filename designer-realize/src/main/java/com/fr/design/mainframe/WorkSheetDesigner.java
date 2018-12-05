package com.fr.design.mainframe;

import com.fr.base.ScreenResolution;
import com.fr.design.DesignState;
import com.fr.design.actions.report.ReportColumnsAction;
import com.fr.design.actions.report.ReportEngineAttrAction;
import com.fr.design.actions.report.ReportPageAttrAction;
import com.fr.design.actions.report.ReportWriteAttrAction;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.designer.DesignerProxy;
import com.fr.design.designer.EditingState;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.DottedSeparator;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.selection.SelectionListener;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ArrayUtils;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import java.awt.BorderLayout;

public class WorkSheetDesigner extends ReportComponent<WorkSheet, ElementCasePaneDelegate, Selection> implements DesignerProxy {

    private static final int HUND = 100;

    public WorkSheetDesigner(WorkSheet sheet) {
        super(sheet);

        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        elementCasePane = new ElementCasePaneDelegate(sheet);
        this.add(elementCasePane, BorderLayout.CENTER);
        elementCasePane.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
                WorkSheetDesigner.this.fireTargetModified();
            }
        });

    }

    //august：以下方法貌似都可以放到父类中去 不过问题就是PolyDesigner可能用不到，它还得覆盖
    @Override
    public void setTarget(WorkSheet t) {
        super.setTarget(t);

        this.elementCasePane.setTarget(t);
    }

    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }


    public AuthorityEditPane createAuthorityEditPane() {
        ElementCasePaneAuthorityEditPane elementCasePaneAuthorityEditPane = new ElementCasePaneAuthorityEditPane(elementCasePane);
        elementCasePaneAuthorityEditPane.populateDetials();
        return elementCasePaneAuthorityEditPane;
    }


    @Override
    public EditingState createEditingState() {
        return this.elementCasePane.createEditingState();
    }

//////////////////////////////////////////////////////////////////////
//////////////////for toolbarMenuAdapter//////////////////////////////
//////////////////////////////////////////////////////////////////////

    @Override
    public void copy() {
        DesignModeContext.doCopy(this.elementCasePane);
    }

    @Override
    public boolean paste() {
        return DesignModeContext.doPaste(this.elementCasePane);
    }

    @Override
    public boolean cut() {
        return DesignModeContext.doCut(this.elementCasePane);
    }

    @Override
    public void stopEditing() {
        this.elementCasePane.stopEditing();
    }

    @Override
    public ToolBarDef[] toolbars4Target() {
        return this.elementCasePane.toolbars4Target();
    }

    public JComponent[] toolBarButton4Form() {
        return this.elementCasePane.toolBarButton4Form();
    }


    @Override
    public ShortCut[] shortcut4TemplateMenu() {
        return ArrayUtils.addAll(super.shortcut4TemplateMenu(), new ShortCut[]{
                new DottedSeparator(),
                new ReportWriteAttrAction(this),
                new ReportColumnsAction(this),
                new ReportPageAttrAction(this),
                new ReportEngineAttrAction(this)
        });
    }


    @Override
    public MenuDef[] menus4Target() {
        return this.elementCasePane.menus4Target();
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        elementCasePane.requestFocus();
    }

    @Override
    public JScrollBar getHorizontalScrollBar() {
        return elementCasePane.getHorizontalScrollBar();
    }

    public JScrollBar getVerticalScrollBar() {
        return elementCasePane.getVerticalScrollBar();
    }

    public JPanel getEastUpPane() {
        return elementCasePane.getEastUpPane();
    }

    public JPanel getEastDownPane() {
        return elementCasePane.getEastDownPane();
    }


    @Override
    public Selection getSelection() {
        return elementCasePane.getSelection();
    }

    @Override
    public void setSelection(Selection selectElement) {
        if (selectElement == null) {
            selectElement = new CellSelection();
        }
        this.elementCasePane.setSelection(selectElement);
    }

    public void removeSelection() {
        TemplateElementCase templateElementCase = this.elementCasePane.getEditingElementCase();
        if (templateElementCase instanceof WorkSheet) {
            ((WorkSheet) templateElementCase).setPaintSelection(false);
        }
        elementCasePane.repaint();
    }

    @Override
    public Selection getDefaultSelectElement() {
        TemplateElementCase tpc = this.elementCasePane.getEditingElementCase();
        CellElement cellElement = tpc.getCellElement(0, 0);
        return cellElement == null ? new CellSelection() : new CellSelection(0, 0, cellElement.getColumnSpan(), cellElement.getRowSpan());
    }

    @Override
    public void updateJSliderValue() {
        ReportComponentComposite reportComposite = (ReportComponentComposite) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getCurrentReportComponentPane();
        JSliderPane jSliderContainer = reportComposite.getjSliderContainer();
        jSliderContainer.getShowVal().setValue((int) Math.ceil((double) this.elementCasePane.getResolution() * HUND / ScreenResolution.getScreenResolution()));
    }

    @Override
    public void addSelectionChangeListener(SelectionListener selectionListener) {
        elementCasePane.addSelectionChangeListener(selectionListener);
    }

    @Override
    public void removeSelectionChangeListener(SelectionListener selectionListener) {
        elementCasePane.removeSelectionChangeListener(selectionListener);
    }

}