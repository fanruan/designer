package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.base.Parameter;
import com.fr.base.ScreenResolution;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.DesignModelAdapter;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.AllowAuthorityEditAction;
import com.fr.design.actions.ExitAuthorityEditAction;
import com.fr.design.actions.file.export.CSVExportAction;
import com.fr.design.actions.file.export.EmbeddedExportExportAction;
import com.fr.design.actions.file.export.ExcelExportAction;
import com.fr.design.actions.file.export.PDFExportAction;
import com.fr.design.actions.file.export.PageExcelExportAction;
import com.fr.design.actions.file.export.PageToSheetExcelExportAction;
import com.fr.design.actions.file.export.SVGExportAction;
import com.fr.design.actions.file.export.TextExportAction;
import com.fr.design.actions.file.export.WordExportAction;
import com.fr.design.actions.report.ReportExportAttrAction;
import com.fr.design.actions.report.ReportMobileAttrAction;
import com.fr.design.actions.report.ReportParameterAction;
import com.fr.design.actions.report.ReportPrintSettingAction;
import com.fr.design.actions.report.ReportWatermarkAction;
import com.fr.design.actions.report.ReportWebAttrAction;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.cell.bar.DynamicScrollBar;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.designer.TargetComponent;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.fun.PreviewProvider;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.gui.frpane.HyperlinkGroupPaneActionProvider;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIModeControlContainer;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.mainframe.template.info.JWorkBookProcessInfo;
import com.fr.design.mainframe.template.info.TemplateProcessInfo;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.parameter.ParameterDefinitePane;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.design.preview.MobilePreview;
import com.fr.design.preview.PagePreview;
import com.fr.design.preview.ViewPreview;
import com.fr.design.preview.WriteEnhancePreview;
import com.fr.design.preview.WritePreview;
import com.fr.design.report.fit.menupane.ReportFitAttrAction;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.selection.QuickEditor;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.write.submit.SmartInsertDBManipulationInWidgetEventPane;
import com.fr.design.write.submit.SmartInsertDBManipulationPane;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.general.ModuleContext;
import com.fr.grid.Grid;
import com.fr.grid.GridUtils;
import com.fr.io.exporter.EmbeddedTableDataExporter;
import com.fr.log.FineLoggerFactory;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.main.impl.WorkBookAdapter;
import com.fr.main.impl.WorkBookX;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.poly.PolyDesigner;
import com.fr.poly.creator.BlockCreator;
import com.fr.privilege.finegrain.WorkSheetPrivilegeControl;
import com.fr.report.ReportHelper;
import com.fr.report.cell.Elem;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.report.cell.painter.CellImagePainter;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ArrayUtils;
import com.fr.stable.AssistUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.module.Module;
import com.fr.stable.project.ProjectConstants;
import com.fr.web.controller.ViewRequestConstants;
import com.fr.workspace.WorkContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * JWorkBook used to edit WorkBook.
 */
public class JWorkBook extends JTemplate<WorkBook, WorkBookUndoState> {

    private static final String SHARE_SUFFIX = "_share";
    private static final String SHARE_FOLDER = "share";
    private static final String DEFAULT_WBX_FILE_PREFIX = "WorkBookX";
    private static final String DEFAULT_WB_FILE_PREFIX = "WorkBook";

    private static final int TOOLBARPANEDIMHEIGHT = 26;
    private static final double MIN_TIME = 0.4;

    private UIModeControlContainer centerPane;
    public ReportComponentComposite reportComposite;
    private ParameterDefinitePane parameterPane;
    private int resolution = ScreenResolution.getScreenResolution();

    public JWorkBook() {
        super(new WorkBook(new WorkSheet()), DEFAULT_WB_FILE_PREFIX);
        populateReportParameterAttr();
    }

    public JWorkBook(WorkBookX workBookX) {
        super(new WorkBookAdapter(workBookX), DEFAULT_WBX_FILE_PREFIX);
        populateReportParameterAttr();
    }

    public JWorkBook(WorkBook workBook, String fileName) {
        super(workBook, fileName);
        populateReportParameterAttr();
    }

    public JWorkBook(WorkBook workBook, FILE file) {
        super(workBook, file);
        populateReportParameterAttr();
    }

    @Override
    public void refreshEastPropertiesPane() {
        if (isEditingPolySheet()) {
            EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.POLY);
        } else {
            if (isUpMode()) {
                EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.REPORT_PARA);
            } else {
                EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.REPORT);
            }
        }
        refreshToolArea();
    }

    private boolean isEditingPolySheet() {
        return template.getReport(getEditingReportIndex()) instanceof PolyWorkSheet;
    }

    @Override
    public TargetComponent getCurrentElementCasePane() {
        return getEditingElementCasePane();
    }

    @Override
    public JComponent getCurrentReportComponentPane() {
        return reportComposite;
    }

    @Override
    protected UIModeControlContainer createCenterPane() {
        parameterPane = ModuleContext.isModuleStarted(Module.FORM_MODULE) ? new ParameterDefinitePane() : null;
        centerPane = new UIModeControlContainer(parameterPane, reportComposite = new ReportComponentComposite(this)) {
            @Override
            protected void onModeChanged() {
                refreshToolArea();
            }

            @Override
            protected void onResize(int distance) {
                if (hasParameterPane()) {
                    parameterPane.setDesignHeight(distance);
                    fireTargetModified();
                }
            }
        };

        reportComposite.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
                JWorkBook.this.fireTargetModified();
            }
        });

        reportComposite.setParentContainer(centerPane);
        return centerPane;
    }

    @Override
    public TemplateProcessInfo<WorkBook> getProcessInfo() {
        if (processInfo == null) {
            processInfo = new JWorkBookProcessInfo(template);
        }
        return processInfo;
    }

    @Override
    public void setJTemplateResolution(int resolution) {
        this.resolution = resolution;
    }

    @Override
    public int getJTemplateResolution() {
        return this.resolution;
    }

    /**
     * 判断sheet权限
     *
     * @param rolsName 角色
     */
    @Override
    public void judgeSheetAuthority(String rolsName) {
        boolean isCovered = reportComposite.getEditingTemplateReport().getWorkSheetPrivilegeControl().checkInvisible(rolsName);
        centerPane.setSheeetCovered(isCovered);
        centerPane.refreshContainer();
    }

    /**
     * 在编辑的面板是被参考的面板时，取消格式刷
     */
    @Override
    public void doConditionCancelFormat() {
        if (ComparatorUtils.equals(reportComposite.centerCardPane.editingComponet.elementCasePane, DesignerContext.getReferencedElementCasePane())) {
            cancelFormat();
        }
    }


    /**
     * 无条件取消格式刷
     */
    @Override
    public void cancelFormat() {
        DesignerContext.setFormatState(DesignerContext.FORMAT_STATE_NULL);
        reportComposite.centerCardPane.editingComponet.elementCasePane.getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(true);
        DesignerContext.setReferencedElementCasePane(null);
        DesignerContext.setReferencedIndex(0);
        this.repaint();
    }

    @Override
    public int getEditingReportIndex() {
        return reportComposite.getEditingIndex();
    }

    /**
     * 创建权限细粒度面板
     *
     * @return 返回权限细粒度面板
     */
    @Override
    public AuthorityEditPane createAuthorityEditPane() {
        if (centerPane.isUpEditMode()) {
            return parameterPane.getParaDesigner().getAuthorityEditPane();
        } else {
            WorkSheetPrivilegeControl workSheetPrivilegeControl = reportComposite.getEditingTemplateReport().getWorkSheetPrivilegeControl();
            if (workSheetPrivilegeControl.checkInvisible(ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName())) {
                SheetAuthorityEditPane sheetEditedPane = new SheetAuthorityEditPane(reportComposite.getEditingWorkBook(), this.getEditingReportIndex());
                sheetEditedPane.populateDetials();
                return sheetEditedPane;
            }
            return reportComposite.getEditingReportComponent().createAuthorityEditPane();
        }

    }

    @Override
    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
        if (this.getEditingElementCasePane() == null) {
            return JWorkBook.this;
        }
        this.getEditingElementCasePane().getGrid().setEditable(!DesignerMode.isAuthorityEditing());
        centerPane.needToShowCoverAndHidPane();
        if (centerPane.isUpEditMode()) {
            return parameterPane;
        } else {
            return JWorkBook.this;
        }
    }

    private boolean hasParameterPane() {
        return parameterPane != null;
    }

    /**
     *
     */
    public void setAutoHeightForCenterPane() {
        centerPane.setUpPaneHeight(hasParameterPane() ? parameterPane.getPreferredSize().height : 0);
    }

    /**
     *
     */
    @Override
    public void setComposite() {
        super.setComposite();
        reportComposite.setComponents();
    }

    @Override
    public JPanel getEastUpPane() {
        if (DesignerMode.isAuthorityEditing()) {
            return allowAuthorityUpPane();
        } else {
            return exitEastUpPane();
        }
    }

    private JPanel allowAuthorityUpPane() {
        //初始时显示不支持权限编辑的情况
        //1.编辑参数面板，参数面板什么也没有选中
        //2.在报表主体选中的聚合块不是报表聚合块，是图标聚合块
        boolean isParameterNotSuppportAuthority = centerPane.isUpEditMode() && !parameterPane.getParaDesigner().isSupportAuthority();
        boolean isReportNotSupportAuthority = reportComposite.getEditingReportComponent() instanceof PolyDesigner
                && !((PolyDesigner) reportComposite.getEditingReportComponent()).isSelectedECBolck();
        WorkSheetPrivilegeControl workSheetPrivilegeControl = reportComposite.getEditingTemplateReport().getWorkSheetPrivilegeControl();
        if (!centerPane.isUpEditMode() && workSheetPrivilegeControl.checkInvisible(ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName())) {
            AuthoritySheetEditedPane sheetEditedPane = new AuthoritySheetEditedPane(reportComposite.getEditingWorkBook(), this.getEditingReportIndex());
            sheetEditedPane.populate();
            return sheetEditedPane;
        }
        boolean isSelectedDownSupport = !centerPane.isUpEditMode() && isReportNotSupportAuthority;
        if (isParameterNotSuppportAuthority || isSelectedDownSupport) {
            return new NoSupportAuthorityEdit();
        }

        return new AuthorityPropertyPane(this);
    }

    private JPanel exitEastUpPane() {
        if (centerPane.isUpEditMode()) {
            return parameterPane.getParaDesigner().getEastUpPane();
        }
        if (delegate4ToolbarMenuAdapter() instanceof PolyDesigner) {
            return delegate4ToolbarMenuAdapter().getEastUpPane();
        } else {
            ElementCasePane casePane = ((ReportComponent) delegate4ToolbarMenuAdapter()).elementCasePane;
            if (casePane != null) {
                return casePane.getEastUpPane();
            }
        }
        return new JPanel();
    }

    @Override
    public JPanel getEastDownPane() {
        if (centerPane.isUpEditMode()) {
            return parameterPane.getParaDesigner().getEastDownPane();
        }
        if (delegate4ToolbarMenuAdapter() instanceof PolyDesigner) {
            if (((PolyDesigner) delegate4ToolbarMenuAdapter()).getSelectionType() == PolyDesigner.SelectionType.NONE) {
                return new JPanel();
            } else {
                return delegate4ToolbarMenuAdapter().getEastDownPane();
            }
        } else {
            ElementCasePane casePane = ((ReportComponent) delegate4ToolbarMenuAdapter()).elementCasePane;
            if (casePane != null) {
                return casePane.getEastDownPane();
            }
        }
        return new JPanel();
    }

    /**
     * 移除选择
     */
    @Override
    public void removeTemplateSelection() {
        this.reportComposite.removeSelection();
    }

    @Override
    public void setSheetCovered(boolean isCovered) {
        centerPane.setSheeetCovered(isCovered);
    }

    /**
     * 刷新容器
     */
    @Override
    public void refreshContainer() {
        centerPane.refreshContainer();
    }

    /**
     * 移除参数面板选择
     */
    @Override
    public void removeParameterPaneSelection() {
        parameterPane.getParaDesigner().removeSelection();
    }

    /**
     * 缩放条
     */
    @Override
    public void setScale(int resolution) {
        //更新resolution
        this.resolution = resolution;
        ElementCasePane elementCasePane = reportComposite.centerCardPane.editingComponet.elementCasePane;
        PolyDesigner polyDezi = reportComposite.centerCardPane.getPolyDezi();
        if (elementCasePane != null) {
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
        if (polyDezi != null) {
            polyDezi.setResolution(resolution);
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setJTemplateResolution(resolution);
            polyDezi.updateUI();
        }
    }

    @Override
    public int selfAdaptUpdate() {
        PolyDesigner polyDezi = reportComposite.centerCardPane.getPolyDezi();
        ElementCasePane elementCasePane = reportComposite.centerCardPane.editingComponet.elementCasePane;
        if (resolution == 0) {
            resolution = ScreenResolution.getScreenResolution();
        }
        if (polyDezi != null && polyDezi.getSelection() != null) {
            BlockCreator blockCreator = polyDezi.getSelection();
            double x = blockCreator.getEditorBounds().getX();
            double y = blockCreator.getEditorBounds().getY();
            polyDezi.setHorizontalValue((int) x);
            polyDezi.setVerticalValue((int) y);
            double creatorHeight = blockCreator.getEditorBounds().height;
            double creatorWidth = blockCreator.getEditorBounds().width;
            double areaHeight = polyDezi.polyArea.getHeight();
            double areaWidth = polyDezi.polyArea.getWidth();
            if(AssistUtils.equals(creatorWidth,0) || AssistUtils.equals(creatorHeight,0)){
                return resolution;
            }
            double time = (areaHeight / creatorHeight) < (areaWidth / creatorWidth) ? (areaHeight / creatorHeight) : (areaWidth / creatorWidth);
            return (int) (time * ScreenResolution.getScreenResolution());

        } else if (elementCasePane != null) {
            ElementCasePane reportPane = elementCasePane.getGrid().getElementCasePane();
            if (reportPane.getSelection().getSelectedColumns().length == 0) {
                return resolution;
            }
            int column = reportPane.getSelection().getSelectedColumns()[0];
            double columnLength = reportPane.getSelection().getSelectedColumns().length;
            double columnExtent = reportPane.getGrid().getHorizontalExtent();
            int row = reportPane.getSelection().getSelectedRows()[0];
            double rowLength = reportPane.getSelection().getSelectedRows().length;
            double rowExtent = reportPane.getGrid().getVerticalExtent();
            if(AssistUtils.equals(columnLength,0) || AssistUtils.equals(rowLength,0)){
                return resolution;
            }
            double time = (columnExtent / columnLength) < (rowExtent / rowLength) ? (columnExtent / columnLength) : (rowExtent / rowLength);
            if (reportPane.isHorizontalScrollBarVisible()) {
                reportPane.getVerticalScrollBar().setValue(row);
                reportPane.getHorizontalScrollBar().setValue(column);
            }
            return (int) (time * elementCasePane.getGrid().getResolution());
        } else {
            return resolution;
        }
    }

    @Override
    public int getScale() {
        return this.resolution;
    }

    @Override
    public int getToolBarHeight() {
        return TOOLBARPANEDIMHEIGHT;
    }

    /**
     * 更新报表参数属性
     */
    public void populateReportParameterAttr() {
        if (hasParameterPane()) {
            parameterPane.populate(this);
            setAutoHeightForCenterPane();
        }
    }

    /**
     * 更新ReportParameterAttr
     */
    public void updateReportParameterAttr() {
        if (hasParameterPane()) {
            ReportParameterAttr rpt = this.parameterPane.update(this.getTarget().getReportParameterAttr());
            this.getTarget().setReportParameterAttr(rpt);
        }
    }

    /**
     * 检查提交按钮
     */
    public void checkHasSubmitButton() {
        if (parameterPane != null) {
            parameterPane.checkSubmitButton();
        }
    }

    // ////////////////////////////////////////////////////////
    // //////////////////////OLD BELOW/////////////////////////
    // ////////////////////////////////////////////////////////


    /**
     * set target
     */
    @Override
    public void setTarget(WorkBook book) {
        if (book == null) {
            return;
        }

        if (book.getReportCount() == 0) {
            book.addReport(new WorkSheet());
        }

        super.setTarget(book);
    }

    private TargetComponent delegate4ToolbarMenuAdapter() {
        return this.reportComposite.getEditingReportComponent();
    }

    /**
     * 复制
     */
    @Override
    public void copy() {
        DesignModeContext.doCopy(this.delegate4ToolbarMenuAdapter());
    }

    /**
     * 剪切
     *
     * @return 剪切成功返回true
     */
    @Override
    public boolean cut() {
        return DesignModeContext.doCut(this.delegate4ToolbarMenuAdapter());
    }

    /**
     * 黏贴
     *
     * @return 黏贴成功返回true
     */
    @Override
    public boolean paste() {
        return DesignModeContext.doPaste(this.delegate4ToolbarMenuAdapter());
    }

    /**
     * 停止编辑
     */
    @Override
    public void stopEditing() {
        reportComposite.stopEditing();
        if (!this.isSaved()) {
            this.updateReportParameterAttr();
            this.delegate4ToolbarMenuAdapter().stopEditing();
        }
    }

    /**
     * 保存文件的后缀名
     *
     * @return 后缀的字符串
     */
    @Override
    public String suffix() {
        return template.suffix();
    }

    @Override
    public void setPictureElem(Elem elem, CellImage cellImage) {
        WorkBook workBook = this.getTarget();
        if (workBook instanceof WorkBookAdapter) {
            elem.setValue(new CellImagePainter(cellImage));
        } else {
            elem.setValue(cellImage.getImage());
        }
    }


    // ////////////////////////////////////////////////////////////////////
    // ////////////////for toolbarMenuAdapter//////////////////////////////
    // ////////////////////////////////////////////////////////////////////

    /**
     * 文件菜单的子菜单
     *
     * @return 子菜单
     */
    @Override
    public ShortCut[] shortcut4FileMenu() {
        boolean hideWorkBookExportMenu = DesignerMode.isVcsMode()
                || DesignerMode.isAuthorityEditing();
        return ArrayUtils.addAll(super.shortcut4FileMenu(),
                hideWorkBookExportMenu ? new ShortCut[0] : new ShortCut[]{this.createWorkBookExportMenu()}
        );
    }

    /**
     * 目标的菜单
     *
     * @return 菜单
     */
    @Override
    public MenuDef[] menus4Target() {
        return ArrayUtils.addAll(
                super.menus4Target(), this.delegate4ToolbarMenuAdapter().menus4Target()
        );
    }

    @Override
    public int getMenuState() {
        return this.delegate4ToolbarMenuAdapter().getMenuState();
    }

    private MenuDef createWorkBookExportMenu() {
        MenuDef excelExportMenuDef = new MenuDef(KeySetUtils.EXCEL_EXPORT.getMenuKeySetName(), KeySetUtils.EXCEL_EXPORT.getMnemonic());
        excelExportMenuDef.setIconPath("/com/fr/design/images/m_file/excel.png");
        excelExportMenuDef
                .addShortCut(new PageExcelExportAction(this), new ExcelExportAction(this), new PageToSheetExcelExportAction(this));
        // Export - MenuDef
        MenuDef exportMenuDef = new MenuDef(KeySetUtils.EXPORT.getMenuName());
        exportMenuDef.setIconPath("/com/fr/design/images/m_file/export.png");

        exportMenuDef.addShortCut(excelExportMenuDef, new PDFExportAction(this), new WordExportAction(this), new SVGExportAction(this),
                new CSVExportAction(this), new TextExportAction(this), new EmbeddedExportExportAction(this));

        return exportMenuDef;
    }

    /**
     * 权限细粒度情况下的子菜单
     *
     * @return 子菜单
     */
    @Override
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[]{
                new NameSeparator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Permissions_Edition")),
                DesignerMode.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this),
        };

    }

    /**
     * 模板的子菜单
     *
     * @return 子菜单
     */
    @Override
    public ShortCut[] shortcut4TemplateMenu() {
        return ArrayUtils.addAll(new ShortCut[]{
                new ReportWebAttrAction(this),
                new ReportExportAttrAction(this),
                new ReportParameterAction(this),
                new ReportFitAttrAction(this),
                new ReportMobileAttrAction(this),
                new ReportPrintSettingAction(this),
                new ReportWatermarkAction(this),
                new NameSeparator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Current_Sheet")),
        }, this.reportComposite.getEditingReportComponent().shortcut4TemplateMenu());
    }

    /**
     * 模板的工具
     *
     * @return 工具
     */
    @Override
    public ToolBarDef[] toolbars4Target() {
        return this.delegate4ToolbarMenuAdapter().toolbars4Target();
    }

    @Override
    protected WorkBookUndoState createUndoState() {
        return new WorkBookUndoState(
                this,
                this.reportComposite.getSelectedIndex(),
                this.reportComposite.getEditingReportComponent().createEditingState()
        );
    }

    @Override
    protected void applyUndoState(WorkBookUndoState u) {
        try {
            this.setTarget((WorkBook) u.getWorkBook().clone());
            if (!DesignerMode.isAuthorityEditing()) {
                if (u.getAuthorityType() != BaseUndoState.NORMAL_STATE) {
                    applyAll(u);
                    this.undoState = u;
                    return;
                }
                if (centerPane.isUpEditMode()) {
                    if (hasParameterPane()) {
                        parameterPane.populate(u.getApplyTarget());
                        DesignModuleFactory.getFormHierarchyPane().refreshRoot();
                    }
                } else {
                    reportComposite.setSelectedIndex(u.getSelectedReportIndex());
                    u.getSelectedEditingState().revert();
                    TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()).refreshDockingView();
                }
                this.undoState = u;
            } else {
                //参数面板
                applyAll(u);
                this.authorityUndoState = u;
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyAll(WorkBookUndoState u) {
        if (hasParameterPane()) {
            parameterPane.populate(u.getApplyTarget());
            DesignModuleFactory.getFormHierarchyPane().refreshRoot();
        }
        //报表主体
        reportComposite.setSelectedIndex(u.getSelectedReportIndex());
        u.getSelectedEditingState().revert();
        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()).refreshDockingView();
        //如果是在权限编辑状态下，则有工具栏
        DesignerContext.getDesignerFrame().resetToolkitByPlus(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
    }

    /**
     * 请求焦点
     */
    @Override
    public void requestFocus() {
        super.requestFocus();
        ReportComponent reportComponent = reportComposite.getEditingReportComponent();
        reportComponent.requestFocus();
    }

    /**
     * @return
     */
    public final TemplateElementCase getEditingElementCase() {
        return this.reportComposite.getEditingReportComponent().getEditingElementCasePane().getEditingElementCase();
    }

    /**
     * 获取当前workBook中的正在编辑的component对应的elementCasePane
     *
     * @return
     */
    public final ElementCasePane getEditingElementCasePane() {
        return this.reportComposite.getEditingReportComponent().getEditingElementCasePane();
    }

    /**
     * 刷新所有的控件
     */
    public void refreshAllNameWidgets() {
        if (parameterPane != null) {
            parameterPane.refreshAllNameWidgets();
        }
    }

    /**
     * 为数据集刷新参数面板
     *
     * @param oldName 旧名字
     * @param newName 新名字
     */
    public void refreshParameterPane4TableData(String oldName, String newName) {
        if (parameterPane != null) {
            parameterPane.refresh4TableData(oldName, newName);
        }
    }

    /**
     * 恢复
     */
    @Override
    public void revert() {
        ElementCasePane epane = reportComposite.getEditingReportComponent().elementCasePane;
        if (epane == null) {
            return;
        }
        if (delegate4ToolbarMenuAdapter() instanceof PolyDesigner) {
            PolyDesigner polyDesigner = (PolyDesigner) delegate4ToolbarMenuAdapter();
            if (polyDesigner.getSelectionType() == PolyDesigner.SelectionType.NONE || polyDesigner.getSelection() == null) {
                QuickEditorRegion.getInstance().populate(QuickEditor.DEFAULT_EDITOR);
            } else {
                QuickEditorRegion.getInstance().populate(epane.getCurrentEditor());
            }
        } else {
            QuickEditorRegion.getInstance().populate(epane.getCurrentEditor());
        }
        CellElementPropertyPane.getInstance().populate(epane);
    }

    @Override
    protected WorkBookModelAdapter createDesignModel() {
        return new WorkBookModelAdapter(this);
    }

    /**
     * 表单的工具栏
     *
     * @return 表单工具栏
     */
    @Override
    public JPanel[] toolbarPanes4Form() {
        if (centerPane.isUpEditMode() && hasParameterPane()) {
            return parameterPane.toolbarPanes4Form();
        }
        return new JPanel[0];
    }

    /**
     * 表单的工具按钮
     *
     * @return 工具按钮
     */
    @Override
    public JComponent[] toolBarButton4Form() {
        centerPane.needToShowCoverAndHidPane();
        if (centerPane.isUpEditMode() && hasParameterPane()) {
            return parameterPane.toolBarButton4Form();
        } else {
            return this.delegate4ToolbarMenuAdapter().toolBarButton4Form();
        }
    }

    /**
     * 权限细粒度状态下的工具面板
     *
     * @return 工具面板
     */
    @Override
    public JComponent toolBar4Authority() {
        return new AuthorityToolBarPane();
    }

    /**
     * 是否支持预览
     *
     * @return 预览接口
     */
    @Override
    public PreviewProvider[] supportPreview() {
        Set<PreviewProvider> set = ExtraDesignClassManager.getInstance().getArray(PreviewProvider.MARK_STRING);
        return ArrayUtils.addAll(new PreviewProvider[]{
                new PagePreview(), new WritePreview(), new ViewPreview(), new WriteEnhancePreview(), new MobilePreview()
        }, set.toArray(new PreviewProvider[set.size()]));
    }

    /**
     * 预览菜单项
     *
     * @return 预览菜单项
     */
    @Override
    public UIMenuItem[] createMenuItem4Preview() {
        List<UIMenuItem> menuItems = new ArrayList<UIMenuItem>();
        PreviewProvider[] previewProviders = supportPreview();
        for (final PreviewProvider provider : previewProviders) {
            UIMenuItem item = new UIMenuItem(provider.nameForPopupItem(), BaseUtils.readIcon(provider.iconPathForPopupItem()));
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    provider.onClick(JWorkBook.this);
                }
            });
            menuItems.add(item);
        }
        return menuItems.toArray(new UIMenuItem[menuItems.size()]);
    }

    /**
     * 预览按钮点击事件
     *
     * @param provider 预览接口
     */
    @Override
    public void previewMenuActionPerformed(PreviewProvider provider) {
        super.previewMenuActionPerformed(provider);
    }

    /**
     * 是不是模板
     *
     * @return 是则返回true
     */
    @Override
    public boolean isJWorkBook() {
        return true;
    }

    @Override
    public HyperlinkGroupPane getHyperLinkPane(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        return ReportHyperlinkGroupPane.getInstance(hyperlinkGroupPaneActionProvider);
    }

    @Override
    public HyperlinkGroupPane getHyperLinkPaneNoPop(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        return ReportHyperlinkGroupPaneNoPop.getInstance(hyperlinkGroupPaneActionProvider);
    }

    @Override
    public void setAuthorityMode(boolean isUpMode) {
        centerPane.setAuthorityMode(isUpMode);
    }

    /**
     * 是不是正在编辑参数面板
     *
     * @return 是则返回true
     */
    @Override
    public boolean isUpMode() {
        return centerPane.isUpEditMode();
    }

    /**
     * 刷新参数和工具区域
     */
    @Override
    public void refreshToolArea() {
        populateReportParameterAttr();
        if (centerPane.isUpEditMode()) {
            if (hasParameterPane()) {
                DesignerContext.getDesignerFrame().resetToolkitByPlus(parameterPane);
                parameterPane.initBeforeUpEdit();
            }
        } else {
            DesignerContext.getDesignerFrame().resetToolkitByPlus(JWorkBook.this);
            EastRegionContainerPane.getInstance().removeParameterPane();
            if (delegate4ToolbarMenuAdapter() instanceof PolyDesigner) {
                PolyDesigner polyDesigner = (PolyDesigner) delegate4ToolbarMenuAdapter();
                if (polyDesigner.getSelectionType() == PolyDesigner.SelectionType.NONE || polyDesigner.getSelection() == null) {
                    EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.POLY);
//                    EastRegionContainerPane.getInstance().replaceDownPane(new JPanel());
                    QuickEditorRegion.getInstance().populate(QuickEditor.DEFAULT_EDITOR);
                } else {
                    EastRegionContainerPane.getInstance().replaceDownPane(CellElementPropertyPane.getInstance());
                }
                EastRegionContainerPane.getInstance().replaceUpPane(QuickEditorRegion.getInstance());
            } else {
                ElementCasePane casePane = ((ReportComponent) delegate4ToolbarMenuAdapter()).elementCasePane;
                if (casePane != null) {
                    casePane.fireSelectionChangeListener();
                }
            }
        }
        if (DesignerMode.isAuthorityEditing()) {
            EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION);
            EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(allowAuthorityUpPane());
            EastRegionContainerPane.getInstance().replaceConfiguredRolesPane(RolesAlreadyEditedPane.getInstance());
        }

        centerPane.needToShowCoverAndHidPane();
    }

    @Override
    /**
     *
     */
    public Icon getPreviewLargeIcon() {
        PreviewProvider provider = getPreviewType();
        String iconPath = provider.iconPathForLarge();
        return BaseUtils.readIcon(iconPath);
    }

    /**
     * 获取当前workBook的参数及默认值
     * 同名参数的话模板参数覆盖全局参数
     *
     * @return
     */
    @Override
    public Parameter[] getParameters() {
        Parameter[] ps = this.parameterPane.getParameterArray();
        Parameter[] curPs = this.parameterPane.getAllParameters();
        for (int i = 0; i < ps.length; i++) {
            for (int j = 0; j < curPs.length; j++) {
                if (ComparatorUtils.equals(ps[i].getName(), curPs[j].getName())) {
                    ps[i].setValue(curPs[j].getValue());
                }
            }
        }
        return ps;
    }

    @Override
    public Parameter[] getJTemplateParameters() {
        return this.parameterPane.getAllParameters();
    }

    /**
     * 请求单元格区域的焦点
     */
    @Override
    public void requestGridFocus() {
        reportComposite.centerCardPane.requestGrifFocus();
    }


    /**
     * 创建内置sql提交的pane
     *
     * @return 内置sql提交的pane
     * @date 2014-10-14-下午7:39:27
     */
    @Override
    public DBManipulationPane createDBManipulationPane() {
        ElementCasePane<TemplateElementCase> epane = this.getEditingElementCasePane();
        return new SmartInsertDBManipulationPane(epane);
    }

    /**
     * 创建控件事件里内置sql提交的pane
     *
     * @return 内置sql提交的pane
     * @date 2014-10-14-下午7:39:27
     */
    @Override
    public DBManipulationPane createDBManipulationPaneInWidget() {
        ElementCasePane<TemplateElementCase> epane = this.getEditingElementCasePane();
        return new SmartInsertDBManipulationInWidgetEventPane(epane);
    }

    @Override
    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/buttonicon/newcpts.png");
    }

    /**
     * 创建sheet名称tab面板
     *
     * @param reportCompositeX 当前组件对象
     * @return sheet名称tab面板
     * @date 2015-2-5-上午11:42:12
     */
    public SheetNameTabPane createSheetNameTabPane(ReportComponentComposite reportCompositeX) {
        return new SheetNameTabPane(reportCompositeX);
    }

    /**
     * 将模板另存为可以分享出去的混淆后内置数据集模板
     *
     * @return 是否另存成功
     */
    @Override
    public boolean saveShareFile() {
        FILE newFile = createNewEmptyFile();
        //如果文件已经打开, 那么就覆盖关闭掉他
        MutilTempalteTabPane.getInstance().closeFileTemplate(newFile);
        final WorkBook tpl = this.getTarget();
        // 弹出输入参数
        java.util.Map<String, Object> parameterMap = inputParameters(tpl);

        String fullPath = StableUtils.pathJoin(WorkContext.getCurrent().getPath(), newFile.getPath());
        try (FileOutputStream fileOutputStream = new FileOutputStream(fullPath)) {
            EmbeddedTableDataExporter exporter = new EmbeddedTableDataExporter();
            exporter.export(fileOutputStream, tpl, parameterMap);
        } catch (Exception e1) {
            FineLoggerFactory.getLogger().error(e1.getMessage());
        }

        //打开导出的内置模板
        DesignerContext.getDesignerFrame().openTemplate(newFile);
        return true;
    }

    //创建新的空白模板
    private FILE createNewEmptyFile() {
        String oldName = this.getEditingFILE().getName();
        oldName = oldName.replaceAll(ProjectConstants.CPT_SUFFIX, StringUtils.EMPTY);
        String shareFileName = oldName + SHARE_SUFFIX;
        String newFilePath = StableUtils.pathJoin(ProjectConstants.REPORTLETS_NAME, SHARE_FOLDER, shareFileName, shareFileName + ProjectConstants.CPT_SUFFIX);
        FileNode node = new FileNode(newFilePath, false);

        FileNodeFILE newFile = new FileNodeFILE(node);
        mkNewFile(newFile);

        return newFile;
    }

    //输入导出内置数据集需要的参数
    private Map<String, Object> inputParameters(final TemplateWorkBook tpl) {
        final java.util.Map<String, Object> parameterMap = new java.util.HashMap<String, Object>();
        DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        Parameter[] parameters = tpl.getParameters();
        if (!ArrayUtils.isEmpty(parameters)) {// 检查Parameter.
            final ParameterInputPane pPane = new ParameterInputPane(
                    parameters);
            pPane.showSmallWindow(designerFrame, new DialogActionAdapter() {

                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        }

        return parameterMap;
    }

    /**
     * 创建分享模板的按钮, 目前只有jworkbook实现了
     *
     * @return 分享模板按钮
     */
    @Override
    public UIButton[] createShareButton() {
        return new UIButton[0];
        //产品想要重新设计下, 1现在的分享多列数据集很麻烦, 2想做成自动上传附件.
//        return new UIButton[]{new ShareButton()};
    }

    @Override
    public String route() {
        return ViewRequestConstants.REPORT_VIEW_PATH;
    }

    protected void addChooseFILEFilter(FILEChooserPane fileChooser){
        super.addChooseFILEFilter(fileChooser);
        Set<NewTemplateFileProvider> providers = ExtraDesignClassManager.getInstance().getArray(NewTemplateFileProvider.XML_TAG);
        for (NewTemplateFileProvider provider : providers) {
            provider.addChooseFileFilter(fileChooser, this.suffix());
        }
    }
}
