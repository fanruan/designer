/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.present.ConditionAttributesGroupPane;
import com.fr.grid.GridUtils;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.poly.PolyConstants;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyDesigner.SelectionType;
import com.fr.poly.hanlder.ColumnOperationMouseHandler;
import com.fr.poly.hanlder.RowOperationMouseHandler;
import com.fr.report.poly.PolyECBlock;
import com.fr.stable.unit.UNITConstants;
import com.fr.stable.unit.UnitRectangle;

import javax.swing.*;
import java.awt.*;

/**
 * @author richer
 * @since 6.5.4 创建于2011-5-5 单元格类型的聚合块编辑器
 */
public class ECBlockEditor extends BlockEditor<ECBlockPane, PolyECBlock> {
    private static final int HEIGHT_MORE = 5;
    private int resolution = ScreenResolution.getScreenResolution();

    public ECBlockEditor(PolyDesigner designer, ECBlockCreator creator) {
        super(designer, creator);
    }

    @Override
    protected void initDataChangeListener() {
        editComponent.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
                designer.fireTargetModified();
                if (DesignerContext.isRefreshOnTargetModifiedEnabled()) {
                    resetSelectionAndChooseState();
                }
            }
        });
    }

    /**
     * 获取当前编辑的组件
     *
     * @return 聚合报表组件
     * @date 2014-11-24-下午3:49:12
     */
    public ECBlockPane createEffective() {
        PolyECBlock pcb = creator.getValue();
        if (editComponent == null) {
            editComponent = new ECBlockPane(designer, pcb, this);
        }
        if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_NULL) {
            editComponent.getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        }
        return editComponent;
    }

    @Override
    protected void initSize() {
        resolution = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getJTemplateResolution();
        if (resolution == 0) {
            resolution = ScreenResolution.getScreenResolution();
        }
        Dimension cornerSize = getCornerSize();
        PolyECBlock block = getValue();
        UnitRectangle ub = block.getBounds();
        int x = ub.x.toPixI(resolution) - cornerSize.width - designer.getHorizontalValue();
        int y = ub.y.toPixI(resolution) - cornerSize.height - designer.getVerticalValue();
        int w = ub.width.toPixI(resolution) + cornerSize.width + PolyConstants.OPERATION_SIZE
                + UNITConstants.DELTA.toPixI(resolution);
        int h = ub.height.toPixI(resolution) + cornerSize.height + PolyConstants.OPERATION_SIZE
                + UNITConstants.DELTA.toPixI(resolution);
        setBounds(x, y, w, h);
        editComponent.getGrid().setVerticalExtent(
                GridUtils.getExtentValue(0, block.getRowHeightList_DEC(), editComponent.getGrid().getHeight(),
                        resolution));
        editComponent.getGrid().setHorizontalExtent(
                GridUtils.getExtentValue(0, block.getColumnWidthList_DEC(), editComponent.getGrid().getWidth(),
                        resolution));
    }

    public void setBounds(int x, int y, int width, int height) {
        int selfheight = height + HEIGHT_MORE;
        super.setBounds(x, y, width, selfheight);
    }

    @Override
    protected Dimension getAddHeigthPreferredSize() {
        Dimension cornerSize = getCornerSize();
        cornerSize.height = PolyConstants.OPERATION_SIZE;
        return cornerSize;
    }

    @Override
    protected Dimension getAddWidthPreferredSize() {
        Dimension cornerSize = getCornerSize();
        cornerSize.width = PolyConstants.OPERATION_SIZE;
        return cornerSize;
    }

    @Override
    protected RowOperationMouseHandler createRowOperationMouseHandler() {
        return new RowOperationMouseHandler.ECBlockRowOperationMouseHandler(designer, this);
    }

    @Override
    protected ColumnOperationMouseHandler createColumnOperationMouseHandler() {
        return new ColumnOperationMouseHandler.ECBlockColumnOperationMouseHandler(designer, this);
    }

    @Override
    public Dimension getCornerSize() {
        return editComponent.getCornerSize();
    }

    /**
     * 重置当前的选择状态, 用于更新右侧属性表
     *
     * @date 2014-11-24-下午3:48:19
     */
    public void resetSelectionAndChooseState() {
        boolean chooseBlock = designer.isChooseBlock();
        designer.setChooseType(SelectionType.INNER);
        if (DesignerMode.isAuthorityEditing()) {
            JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            if (jTemplate.isJWorkBook()) {
                //清参数面板
                jTemplate.removeParameterPaneSelection();
            }
            designer.noAuthorityEdit();
            return;
        }
        QuickEditorRegion.getInstance().populate(editComponent.getCurrentEditor());
        CellElementPropertyPane.getInstance().populate(editComponent);
        CellWidgetPropertyPane.getInstance().populate(editComponent);
        Selection Selection = ((JWorkBook) (HistoryTemplateListPane.getInstance().getCurrentEditingTemplate())).getEditingElementCasePane().getSelection();
        if (Selection instanceof FloatSelection) {
            EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.REPORT_FLOAT);
            JPanel floatPane = new JPanel(new BorderLayout());
            floatPane.add(ReportFloatPane.getInstance(), BorderLayout.NORTH);
            floatPane.add(QuickEditorRegion.getInstance(), BorderLayout.CENTER);
            EastRegionContainerPane.getInstance().replaceFloatElementPane(floatPane);
        } else {
            EastRegionContainerPane.getInstance().replaceFloatElementPane(ReportFloatPane.getInstance());
            EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.REPORT);
            EastRegionContainerPane.getInstance().replaceCellAttrPane(CellElementPropertyPane.getInstance());
            EastRegionContainerPane.getInstance().replaceCellElementPane(QuickEditorRegion.getInstance());
            EastRegionContainerPane.getInstance().replaceWidgetSettingsPane(CellWidgetPropertyPane.getInstance());
            // 条件属性
            ConditionAttributesGroupPane conditionAttributesGroupPane = ConditionAttributesGroupPane.getInstance();
            conditionAttributesGroupPane.populate(editComponent);

            EastRegionContainerPane.getInstance().updateCellElementState(isSelectedOneCell());
            if (chooseBlock) {
                EastRegionContainerPane.getInstance().switchTabTo(EastRegionContainerPane.KEY_CELL_ATTR);
            }
        }

        // 超级链接
        HyperlinkGroupPane hyperlinkGroupPane = DesignerContext.getDesignerFrame().getSelectedJTemplate()
                .getHyperLinkPane(HyperlinkGroupPaneActionImpl.getInstance());
        hyperlinkGroupPane.populate(editComponent);
    }

    private boolean isSelectedOneCell() {
        JTemplate jTemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
        if (jTemplate == null) {
            return false;
        }
        ElementCasePane ePane = (ElementCasePane)jTemplate.getCurrentElementCasePane();
        return ePane != null && ePane.isSelectedOneCell();
    }
}