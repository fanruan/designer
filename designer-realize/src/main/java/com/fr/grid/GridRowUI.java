package com.fr.grid;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import com.fr.stable.AssistUtils;
import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.base.vcs.DesignerMode;
import com.fr.cache.list.IntList;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.grid.selection.Selection;
import com.fr.privilege.finegrain.ColumnRowPrivilegeControl;
import com.fr.report.ReportHelper;
import com.fr.report.elementcase.ElementCase;

/**
 * @editor zhou
 * @since 2012-3-22下午5:54:21
 */
public class GridRowUI extends ComponentUI {
    private Color detailsBackground = UIConstants.GRID_ROW_DETAILS_BACKGROUND;
    private int resolution ;

    GridRowUI(int resolution){
        if (resolution == 0){
            resolution =  ScreenResolution.getScreenResolution();
        }
        this.resolution = resolution;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        if (!(c instanceof GridRow)) {
            throw new IllegalArgumentException("The component c to paint must be a GridColumn!");
        }
        Graphics2D g2d = (Graphics2D) g;
        GridRow gridRow = (GridRow) c;
        ElementCasePane reportPane = gridRow.getElementCasePane();
        // size
        Dimension size = gridRow.getSize();
        float time = (float)resolution/ScreenResolution.getScreenResolution();
        g2d.setFont(gridRow.getFont().deriveFont(gridRow.getFont().getSize2D() * time));

        ElementCase elementCase = reportPane.getEditingElementCase();
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(elementCase);
        int verticalValue = reportPane.getGrid().getVerticalValue();
        // denny:
        int verticalBeginValue = verticalValue;
        reportPane.getGrid().setVerticalBeinValue(verticalBeginValue);
        int verticalExtent = reportPane.getGrid().getVerticalExtent();

        // paint more rows(double extent), for dragging.
        int verticalEndValue = verticalValue + verticalExtent + 1;
        double horizontalLineHeight = size.getHeight();

        // use background to paint first.
        // denny: 用来标识已有数据
        int rowCount = elementCase.getRowCount();
        double rowTopHeight = 0;
        if (rowCount > verticalBeginValue) {
            rowTopHeight = rowHeightList.getRangeValue(verticalBeginValue, rowCount).toPixD(resolution);
        }
        rowTopHeight = Math.min(horizontalLineHeight, rowTopHeight);
        if (gridRow.getBackground() != null) {
            g2d.setPaint(this.detailsBackground);
                GraphHelper.fill(g2d, new Rectangle2D.Double(0, 0, size.getWidth(), rowTopHeight));
            g2d.setPaint(Color.WHITE);
            GraphHelper.fill(g2d, new Rectangle2D.Double(0, rowTopHeight, size.getHeight(), size.getHeight() - rowTopHeight));
        }
        // draw top border line.
        g2d.setPaint(gridRow.getSeparatorLineColor());
        GraphHelper.drawLine(g2d, 0, 0, size.getWidth(), 0);
        // draw row
        double tmpHeight2 = 0;
        drawRow(verticalBeginValue, verticalEndValue, rowHeightList, resolution, tmpHeight2, gridRow, g2d);
        // 画左边的边框线.
        g2d.setColor(gridRow.getSeparatorLineColor());
        GraphHelper.drawLine(g2d, 0, 0, 0, tmpHeight2);
    }

    private void drawRow(int verticalBeginValue, int verticalEndValue, DynamicUnitList rowHeightList, int resolution,
                         double tmpHeight2, GridRow gridRow, Graphics2D g2d) {
        boolean isSelectedBounds;
        double tmpHeight1 = 0;
        double tmpIncreaseHeight = 0;
        ElementCasePane reportPane = gridRow.getElementCasePane();
        // size
        Dimension size = gridRow.getSize();
        ElementCase elementCase = reportPane.getEditingElementCase();
        // Increase vertical scroll value.
        for (int i = 0; i <= verticalEndValue; i++) {
            // denny:
            if (i == 0) {
                i = verticalBeginValue;
            }
            // ajust height.
            tmpHeight1 += tmpIncreaseHeight;
            tmpIncreaseHeight = rowHeightList.get(i).toPixD(resolution);
            // check these row wich height is zero.
            tmpHeight2 = AssistUtils.equals(tmpIncreaseHeight,0d) ? tmpHeight1 + 1 : tmpHeight1 + tmpIncreaseHeight;
            // check selection bound.
            Selection sel = reportPane.getSelection();
            int[] selectedRows = sel.getSelectedRows();
            if (IntList.asList(selectedRows).contain(i)) {
                g2d.setPaint(gridRow.getSelectedBackground());
                GraphHelper.fill(g2d, new Rectangle2D.Double(0, tmpHeight1 + 1, size.width, tmpIncreaseHeight - 1));
                isSelectedBounds = true;
            } else {
                isSelectedBounds = false;
            }
            drawAuthority(elementCase, g2d, tmpHeight1, tmpIncreaseHeight, size, i);
            // draw seperate line.
            g2d.setColor(gridRow.getSeparatorLineColor());
            GraphHelper.drawLine(g2d, 0, tmpHeight2, size.getWidth(), tmpHeight2);
            // draw content
            // marks: 目前只支持处理文字和图像。
            Integer rowContent = gridRow.getDisplay(i);
            String paintText = rowContent.toString();
            if (elementCase.getReportPageAttr() != null) {
                if (i >= elementCase.getReportPageAttr().getRepeatHeaderRowFrom() && i <= elementCase.getReportPageAttr().getRepeatHeaderRowTo()) {
                    paintText += "(H)";
                }
                if (i >= elementCase.getReportPageAttr().getRepeatFooterRowFrom() && i <= elementCase.getReportPageAttr().getRepeatFooterRowTo()) {
                    paintText += "(F)";
                }
            }
            drawNormalContent(i, g2d, gridRow, paintText, tmpIncreaseHeight, isSelectedBounds, elementCase, size, tmpHeight1);
        }
    }

    private void drawAuthority(ElementCase elementCase, Graphics2D g2d, double tmpHeight1, double tmpIncreaseHeight, Dimension size, int i) {
        boolean isAuthorityEdited = DesignerMode.isAuthorityEditing();
        if (isAuthorityEdited) {
            ColumnRowPrivilegeControl cpc = elementCase.getRowPrivilegeControl(i);
            String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if (cpc.checkInvisible(selectedRoles)) {
                g2d.setColor(UIConstants.AUTHORITY_COLOR);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                GraphHelper.fill(g2d, new Rectangle2D.Double(0, tmpHeight1 + 1, size.width, tmpIncreaseHeight - 1));

            }
        }
    }


    private void drawNormalContent(int i, Graphics2D g2d, GridRow gridRow, String paintText, double tmpIncreaseHeight, boolean isSelectedBounds
            , ElementCase elementCase, Dimension size, double tmpHeight1) {
        // FontMetrics
        FontRenderContext fontRenderContext = g2d.getFontRenderContext();
        float time = (float)resolution/ScreenResolution.getScreenResolution();
        float fmAscent = GraphHelper.getFontMetrics(gridRow.getFont()).getAscent() * time;
        double stringWidth = gridRow.getFont().getStringBounds(paintText, fontRenderContext).getWidth() * time;
        double stringHeight = gridRow.getFont().getStringBounds(paintText, fontRenderContext).getHeight() * time;
        // 如果高度太小了就不画了
        if (stringHeight <= tmpIncreaseHeight + 2) {
            if (isSelectedBounds) {
                g2d.setColor(gridRow.getSelectedForeground());
            } else {
                // p:检查eanbled
                if (gridRow.isEnabled()) {
                    g2d.setColor(gridRow.getForeground());
                } else {
                    g2d.setPaint(UIManager.getColor("controlShadow"));
                }
            }

            GraphHelper.drawString(g2d, paintText, (size.width - stringWidth) / 2, tmpHeight1 + (tmpIncreaseHeight - stringHeight) / 2 + GridHeader.SIZE_ADJUST / 2 + fmAscent - 2);
        }
    }

}