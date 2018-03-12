package com.fr.grid;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

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
 * @since 2012-3-22下午5:51:10
 */
public class GridColumnUI extends ComponentUI {
    protected Color withoutDetailsBackground =  UIConstants.GRID_COLUMN_DETAILS_BACKGROUND;
    private int resolution ;

    public GridColumnUI(int resolution){
        if (resolution == 0){
            resolution =  ScreenResolution.getScreenResolution();
        }
        this.resolution = resolution;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        if (!(c instanceof GridColumn)) {
            throw new IllegalArgumentException("The component c to paint must be a GridColumn!");
        }
        Graphics2D g2d = (Graphics2D) g;
        GridColumn gridColumn = (GridColumn) c;
        ElementCasePane reportPane = gridColumn.getElementCasePane();
        Grid gird = reportPane.getGrid();
        // size
        Dimension size = gridColumn.getSize();
        float time = (float)resolution/ScreenResolution.getScreenResolution();
        g2d.setFont(gridColumn.getFont().deriveFont(gridColumn.getFont().getSize2D() * time));

        ElementCase elementCase = reportPane.getEditingElementCase();
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(elementCase);
        int horizontalValue = gird.getHorizontalValue();
        // denny:
        int horizontalBeginValue = gird.getHorizontalBeginValue();
        horizontalBeginValue = horizontalValue;
        gird.setHorizontalBeginValue(horizontalBeginValue);
        int horizontalExtent = gird.getHorizontalExtent();
        // paint more rows(double extent), for dragging.
        int horizontalEndValue = horizontalValue + horizontalExtent + 1;
        double verticalLineWidth = size.getWidth();
        // Paint background.
        // denny: 用来标识已有数据
        int columnCount = elementCase.getColumnCount();
        double columnLeftWidth = 0;
        if (columnCount >= horizontalBeginValue) {
            columnLeftWidth = columnWidthList.getRangeValue(horizontalBeginValue, columnCount).toPixD(resolution);
        }
        columnLeftWidth = Math.min(verticalLineWidth, columnLeftWidth);
        if (gridColumn.getBackground() != null) {
            g2d.setPaint(this.withoutDetailsBackground);
            GraphHelper.fill(g2d, new Rectangle2D.Double(0, 0, columnLeftWidth, size.getHeight()));
            g2d.setPaint(Color.WHITE);
            GraphHelper.fill(g2d, new Rectangle2D.Double(columnLeftWidth, 0, size.getWidth() - columnLeftWidth, size.getHeight()));
        }
        // draw left border line.
        g2d.setPaint(gridColumn.getSeparatorLineColor());
        GraphHelper.drawLine(g2d, 0, 0, 0, size.getHeight());
        double tmpWidth2 = 0;
        drawColumn(horizontalBeginValue, horizontalEndValue, columnWidthList, tmpWidth2, reportPane, g2d, gridColumn, size);
        // 画上边的边框线.
        g2d.setColor(gridColumn.getSeparatorLineColor());
        GraphHelper.drawLine(g2d, 0, 0, tmpWidth2, 0);
    }


    private void drawColumn(int horizontalBeginValue, int horizontalEndValue, DynamicUnitList columnWidthList, double tmpWidth2,
                            ElementCasePane reportPane, Graphics2D g2d, GridColumn gridColumn, Dimension size) {

        // draw column.
        boolean isSelectedBounds;
        double tmpWidth1 = 0;
        double tmpIncreaseWidth = 0;
        ElementCase elementCase = reportPane.getEditingElementCase();
        // FontMetrics
        FontRenderContext fontRenderContext = g2d.getFontRenderContext();
        LineMetrics fm = gridColumn.getFont().getLineMetrics("", fontRenderContext);
        float fmAscent = fm.getAscent();
        // Increase horizontal scroll value.
        for (int i = 0; i <= horizontalEndValue; i++) {
            if (i == 0) {
                i = horizontalBeginValue;
            }
            // ajust width.
            tmpWidth1 += tmpIncreaseWidth;
            // marks:每列的宽度
            tmpIncreaseWidth = columnWidthList.get(i).toPixD(resolution);

            // check these column wich width is zero.
            tmpWidth2 = tmpIncreaseWidth <= 0 ? tmpWidth1 + 1 : tmpWidth1 + tmpIncreaseWidth;
            // marks:画出来多个选中的区域
            Selection sel = reportPane.getSelection();
            int[] selectedColumn = sel.getSelectedColumns();
            if (IntList.asList(selectedColumn).contain(i)) {
                g2d.setColor(gridColumn.getSelectedBackground());
                GraphHelper.fill(g2d, new Rectangle2D.Double(tmpWidth1 + 1, 0, tmpIncreaseWidth - 1, size.height));
                isSelectedBounds = true;
            } else {
                isSelectedBounds = false;
            }

            drawAuthority(elementCase, g2d, tmpWidth1, tmpIncreaseWidth, size, i);
            // draw seperate line
            g2d.setColor(gridColumn.getSeparatorLineColor());
            GraphHelper.drawLine(g2d, tmpWidth2, 0, tmpWidth2, size.height);
            // draw content
            paintContent(i, g2d, tmpWidth1, size, tmpIncreaseWidth, isSelectedBounds, gridColumn, elementCase, fmAscent, fontRenderContext);
        }
    }

    private void drawAuthority(ElementCase elementCase, Graphics2D g2d, double tmpWidth1, double tmpIncreaseWidth, Dimension size, int i) {
        boolean isAuthorityEdited = DesignerMode.isAuthorityEditing();
        if (isAuthorityEdited) {
        	ColumnRowPrivilegeControl cpc = elementCase.getColumnPrivilegeControl(i);
        	String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        	if(cpc.checkInvisible(selectedRoles)){
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2d.setColor(UIConstants.AUTHORITY_COLOR);
                GraphHelper.fill(g2d, new Rectangle2D.Double(tmpWidth1 + 1, 0, tmpIncreaseWidth - 1, size.height));
        	}
        }
    }

    // draw content
    private void paintContent(int i, Graphics2D g2d, double tmpWidth1, Dimension size, double tmpIncreaseWidth, boolean isSelectedBounds, GridColumn gridColumn,
                              ElementCase elementCase, float fmAscent, FontRenderContext fontRenderContext) {
        String columnContent = gridColumn.getDisplay(i);
        if (columnContent == null) {
            return;
        }

        if (elementCase.getReportPageAttr() != null) {
            if (i >= elementCase.getReportPageAttr().getRepeatHeaderColumnFrom() && i <= elementCase.getReportPageAttr().getRepeatHeaderColumnTo()) {
                columnContent += "(HR)";
            }
            if (i >= elementCase.getReportPageAttr().getRepeatFooterColumnFrom() && i <= elementCase.getReportPageAttr().getRepeatFooterColumnTo()) {
                columnContent += "(FR)";
            }
        }
        float time = (float)resolution/ScreenResolution.getScreenResolution();

        double stringWidth = gridColumn.getFont().getStringBounds(columnContent, fontRenderContext).getWidth() * time;
        if (stringWidth > tmpIncreaseWidth) {
            paintMoreContent(i, g2d, tmpWidth1, size, tmpIncreaseWidth, isSelectedBounds, gridColumn, elementCase, columnContent, stringWidth, fmAscent);
        } else {
            paintNormalContent(i, g2d, tmpWidth1, tmpIncreaseWidth, isSelectedBounds, gridColumn, elementCase, columnContent, stringWidth, fmAscent);
        }
    }


    private void paintMoreContent(int i, Graphics2D g2d, double tmpWidth1, Dimension size, double tmpIncreaseWidth, boolean isSelectedBounds, GridColumn gridColumn,
                                  ElementCase elementCase, String columnContent, double stringWidth, float fmAscent) {
        Graphics2D tmpTextG2d = (Graphics2D) g2d.create();

        // set clip
        tmpTextG2d.setClip(new Rectangle2D.Double(0, tmpWidth1, size.width, tmpIncreaseWidth));
        if (isSelectedBounds) {
            tmpTextG2d.setPaint(gridColumn.getSelectedForeground());
        } else {
            // p: 需要判断Enabled.
            if (gridColumn.isEnabled()) {
                tmpTextG2d.setPaint(gridColumn.getForeground());
            } else {
                g2d.setPaint(UIManager.getColor("controlShadow"));
            }
        }

        GraphHelper.drawString(tmpTextG2d, columnContent, tmpWidth1 + (tmpIncreaseWidth - stringWidth) / 2, fmAscent + GridHeader.SIZE_ADJUST / 2 + 1);

        tmpTextG2d.dispose();
    }


    private void paintNormalContent(int i, Graphics2D g2d, double tmpWidth1, double tmpIncreaseWidth, boolean isSelectedBounds, GridColumn gridColumn,
                                    ElementCase elementCase, String columnContent, double stringWidth, float fmAscent) {
        if (isSelectedBounds) {
            g2d.setPaint(gridColumn.getSelectedForeground());
        } else {
            // p: 需要判断Enabled.
            if (gridColumn.isEnabled()) {
                g2d.setPaint(gridColumn.getForeground());
            } else {
                g2d.setPaint(UIManager.getColor("controlShadow"));
            }
        }
        GraphHelper.drawString(g2d, columnContent, tmpWidth1 + (tmpIncreaseWidth - stringWidth) / 2,
                (gridColumn.getSize().height/2 + g2d.getFont().getSize2D()/2));
    }


}