package com.fr.grid;

import com.fr.base.BaseFormula;
import com.fr.base.DynamicUnitList;
import com.fr.base.GraphHelper;
import com.fr.base.Margin;
import com.fr.base.PaperSize;
import com.fr.base.Utils;
import com.fr.base.background.ColorBackground;
import com.fr.base.background.ImageFileBackground;
import com.fr.base.iofile.attr.WatermarkAttr;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.log.FineLoggerFactory;
import com.fr.main.FineBook;
import com.fr.page.PaperSettingProvider;
import com.fr.page.ReportSettingsProvider;
import com.fr.page.WatermarkPainter;
import com.fr.page.stable.PaperSetting;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.cell.cellattr.CellPageAttr;
import com.fr.report.core.PaintUtils;
import com.fr.report.core.ReportUtils;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.report.Report;
import com.fr.report.stable.ReportConstants;
import com.fr.report.stable.ReportSettings;
import com.fr.report.worksheet.FormElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ColumnRow;
import com.fr.stable.Constants;
import com.fr.stable.script.CalculatorUtils;
import com.fr.stable.unit.FU;
import com.fr.stable.AssistUtils;
import com.fr.third.antlr.ANTLRException;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GridUI extends ComponentUI {

    public static int INVALID_INTEGER = Integer.MIN_VALUE;// 作为不合法的数值.
    protected Dimension gridSize;
    protected int verticalValue;
    protected int horizontalValue;
    protected double paperPaintWidth;
    protected double paperPaintHeight;
    protected DynamicUnitList rowHeightList;
    protected DynamicUnitList columnWidthList;
    protected int verticalEndValue;
    protected int horizontalEndValue;
    protected DrawFlowRect drawFlowRect;
    // paint的辅助类
    protected List paintCellElementList = new ArrayList();
    protected List paintCellElementRectangleList = new ArrayList();
    protected List paginateLineList = new ArrayList(); // 分页线
    // 为了画白色的背景.
    protected static Background WHITE_Backgorund = ColorBackground.getInstance(Color.WHITE);
    // CellElementPainter
    protected CellElementPainter painter = new CellElementPainter();
    // Left
    protected Rectangle2D.Double left_col_row_rect = new Rectangle2D.Double(0, 0, 0, 0);
    // Top
    protected Rectangle2D.Double top_col_row_rect = new Rectangle2D.Double(0, 0, 0, 0);
    protected Rectangle2D.Double back_or_selection_rect = new Rectangle2D.Double(0, 0, 0, 0);
    // alex:用来画拖拽中的格子的边框
    protected Rectangle2D.Double drag_cell_rect = new Rectangle2D.Double(0, 0, 0, 0);
    // alex:用来画单元格的白色背景,以清空原来画的内容
    protected Rectangle2D.Double cell_back_rect = new Rectangle2D.Double(0, 0, 0, 0);
    // 由于报表冻结的时候,当有冻结线穿过merge的格子的时候,最多需要由8个rectangle来决定.
    protected Rectangle2D.Double tmpRectangle = new Rectangle2D.Double(INVALID_INTEGER,
            INVALID_INTEGER, INVALID_INTEGER, INVALID_INTEGER);

    protected int resolution;

    private boolean isAuthority = false;

    public GridUI(int resolution) {
        super();
        this.resolution = resolution;
    }

    protected ReportSettingsProvider getReportSettings(ElementCase elementCase) {
        if (elementCase instanceof Report) {
            return ReportUtils.getReportSettings((Report) elementCase);
        } else if (elementCase instanceof FormElementCase) {
            return ((FormElementCase) elementCase).getReportSettings();
        } else {
            return new ReportSettings();
        }
    }

    protected void paintBackground(Graphics g, Grid grid, ElementCase elementCase, int resolution) {
        Graphics2D g2d = (Graphics2D) g;

        // 当前的Grid面板的大小
        this.back_or_selection_rect.setRect(0, 0, gridSize.getWidth(), gridSize.getHeight());

        // 需要用白色背景来清空背景.
        clearBackground(g2d, grid);

        // paint print dash line.
        this.paperPaintWidth = 0;
        this.paperPaintHeight = 0;

        // richer;聚合报表设计中，最初的ElementCase还没有加到Report中,所以elementCase.getReport()可能为空
        ReportSettingsProvider reportSettings = getReportSettings(elementCase);
        PaperSettingProvider psetting = reportSettings.getPaperSetting();
        if (psetting == null) {
            psetting = new PaperSetting();
        }
        if (grid.getPaginateLineShowType() != Grid.NO_PAGINATE_LINE) {// paint paper margin line.
            PaperSize paperSize = psetting.getPaperSize();
            Margin margin = psetting.getMargin();

            double paperWidth = paperSize.getWidth().toPixD(resolution);
            double paperHeight = paperSize.getHeight().toPixD(resolution);
            // carl:横向就反过来
            if (psetting.getOrientation() == ReportConstants.LANDSCAPE) {
                paperWidth = paperSize.getHeight().toPixD(resolution);
                paperHeight = paperSize.getWidth().toPixD(resolution);
            }

            paperPaintWidth = paperWidth - margin.getLeft().toPixD(resolution)
                    - margin.getRight().toPixD(resolution);
            paperPaintHeight = paperHeight - margin.getTop().toPixD(resolution)
                    - margin.getBottom().toPixD(resolution)
                    - reportSettings.getHeaderHeight().toPixD(resolution)
                    - reportSettings.getFooterHeight().toPixD(resolution);
        }

        // denny:画背景.Background
        Background background = reportSettings.getBackground();

        if (background != null) {
            // denny: except the ColorBackground and ImageBackground
            if (grid.isEnabled() && !(background instanceof ImageFileBackground)) {
                background.paint(g2d, this.back_or_selection_rect);
            }

            // denny: make that the background can move with scroll
            paintScrollBackground(g2d, grid, background, psetting, reportSettings);
        }
    }

    private void clearBackground(Graphics2D g2d, Grid grid) {
        if (grid.isEnabled()) {
            g2d.setPaint(Color.WHITE);
        } else {
            g2d.setPaint(UIManager.getColor("control"));
        }
        GraphHelper.fill(g2d, this.back_or_selection_rect);
    }

    private void paintScrollBackground(Graphics2D g2d, Grid grid, Background background, PaperSettingProvider psetting, ReportSettingsProvider reportSettings) {
        boolean isCanDrawImage = grid.isEditable() || isAuthority;
        if (isCanDrawImage && (background instanceof ImageFileBackground)) {
            if (grid.getPaginateLineShowType() == Grid.NO_PAGINATE_LINE) {
                calculatePaper(psetting, reportSettings);
            }

            ImageFileBackground imageBackground = (ImageFileBackground) background;

            int hideWidth = columnWidthList.getRangeValue(0, horizontalValue)
                    .toPixI(resolution);
            int hideHeight = rowHeightList.getRangeValue(0, verticalValue).toPixI(resolution);

            for (int i = 0; i * paperPaintWidth < gridSize.getWidth(); i++) {
                for (int j = 0; j * paperPaintHeight < gridSize.getHeight(); j++) {
                    this.back_or_selection_rect.setRect(i * paperPaintWidth, j
                            * paperPaintHeight, paperPaintWidth, paperPaintHeight);
                    imageBackground.paint4Scroll(g2d, this.back_or_selection_rect, hideWidth,
                            hideHeight);
                }
            }

            this.back_or_selection_rect
                    .setRect(0, 0, gridSize.getWidth(), gridSize.getHeight());
        }
    }


    private void calculatePaper(PaperSettingProvider psetting, ReportSettingsProvider reportSettings) {
        PaperSize paperSize = psetting.getPaperSize();
        Margin margin = psetting.getMargin();

        double paperWidth = paperSize.getWidth().toPixD(resolution);
        double paperHeight = paperSize.getHeight().toPixD(resolution);
        if (psetting.getOrientation() == ReportConstants.LANDSCAPE
                && paperSize.getWidth().toPixD(resolution) < paperSize.getHeight()
                .toPixD(resolution)) {
            paperWidth = Math.max(paperSize.getWidth().toPixD(resolution), paperSize
                    .getHeight().toPixD(resolution));
            paperHeight = Math.min(paperSize.getWidth().toPixD(resolution), paperSize
                    .getHeight().toPixD(resolution));
        } else if (psetting.getOrientation() == ReportConstants.PORTRAIT
                && paperSize.getWidth().toPixD(resolution) > paperSize.getHeight()
                .toPixD(resolution)) {
            paperWidth = Math.min(paperSize.getWidth().toPixD(resolution), paperSize
                    .getHeight().toPixD(resolution));
            paperHeight = Math.max(paperSize.getWidth().toPixD(resolution), paperSize
                    .getHeight().toPixD(resolution));
        }

        paperPaintWidth = paperWidth - margin.getLeft().toPixD(resolution)
                - margin.getRight().toPixD(resolution);
        paperPaintHeight = paperHeight - margin.getTop().toPixD(resolution)
                - margin.getBottom().toPixD(resolution)
                - reportSettings.getHeaderHeight().toPixD(resolution)
                - reportSettings.getFooterHeight().toPixD(resolution);
    }

    private void paintGridLine(Graphics g, Grid grid, double realWidth, double realHeight,
                               int resolution) {
        Graphics2D g2d = (Graphics2D) g;

        // --开始画水平，垂直线.
        g2d.setPaint(grid.getGridLineColor()); // line color.
        GraphHelper.setStroke(g2d, GraphHelper.getStroke(Constants.LINE_THIN));

        // 分页线
        paginateLineList.clear();

        boolean isShowVerticalPaginateLine = grid.getPaginateLineShowType() == Grid.MULTIPLE_PAGINATE_LINE;
        boolean isShowHorizontalPaginateLine = grid.getPaginateLineShowType() != Grid.NO_PAGINATE_LINE;

        new DrawVerticalLineHelper(grid.getVerticalBeginValue(), verticalEndValue,
                grid.isShowGridLine(), isShowVerticalPaginateLine, rowHeightList, paperPaintHeight,
                paginateLineList, realWidth, resolution).iterateStart2End(g2d);

        new DrawHorizontalLineHelper(grid.getHorizontalBeginValue(), horizontalEndValue,
                grid.isShowGridLine(), isShowHorizontalPaginateLine, columnWidthList, paperPaintWidth,
                paginateLineList, realHeight, resolution).iterateStart2End(g2d);
    }

    /**
     * 最后处理
     */
    public void finalize() {
        try {
            super.finalize();
            if (drawFlowRect != null) {
                this.drawFlowRect.exit();
            }
        } catch (Throwable e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private static abstract class DrawLineHelper {
        private int startIndex;
        private int endIndex;

        private boolean showGridLine;
        private boolean showPaginateLine;

        private DynamicUnitList sizeList;
        private double paperPaintSize;

        private List paginateLineList;

        Line2D tmpLine2D = new Line2D.Double(0, 0, 0, 0);

        private int resolution;

        DrawLineHelper(int startIndex, int endIndex, boolean showGridLine,
                       boolean showPaginateLine, DynamicUnitList sizeList, double paperPaintSize,
                       List paginateLineList, int resolution) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.showGridLine = showGridLine;
            this.showPaginateLine = showPaginateLine;
            this.sizeList = sizeList;
            this.paperPaintSize = paperPaintSize;

            this.paginateLineList = paginateLineList;
            this.resolution = resolution;
        }

        protected void iterateStart2End(Graphics2D g2d) {
            float tmpSize = 0, paperSumSize = 0, sumSize = 0;
            for (int i = 0; i <= endIndex; i++) {
                // denny: 开始
                if (i == 0) {
                    i = startIndex;

                    // denny: 增加从0到Grid左边被hide的列宽
                    for (int k = 0; k < startIndex; k++) {
                        tmpSize = sizeList.get(k).toPixF(resolution);

                        paperSumSize += tmpSize;
                        if (paperSumSize >= paperPaintSize) {
                            paperSumSize = tmpSize;
                        }
                    }
                }

                // adjust height.
                tmpSize = sizeList.get(i).toPixF(resolution);
                paperSumSize += tmpSize;

                if (showGridLine) {// paint line.
                    setLine2D((int) sumSize);
                    g2d.draw(tmpLine2D);
                }

                // paint paper margin line.
                if (showPaginateLine && paperSumSize >= paperPaintSize) {
                    paginateLineList.add(getPaginateLine2D((int) sumSize));
                    paperSumSize = tmpSize;
                }

                sumSize += tmpSize;
            }

            // paint 最后一个横线..
            if (showGridLine) {
                drawLastLine(g2d, (int) sumSize);
            }
        }

        protected abstract void setLine2D(int sumSize);

        protected abstract Line2D.Double getPaginateLine2D(int sumSize);

        protected abstract void drawLastLine(Graphics2D g2d, int sumSize);
    }

    private class DrawVerticalLineHelper extends DrawLineHelper {
        private double realWidth;

        DrawVerticalLineHelper(int startIndex, int endIndex, boolean showGridLine,
                               boolean showPaginateLine, DynamicUnitList unitSizeList, double paperPaintSize,
                               List paginateLineList, double realWidth, int resolution) {
            super(startIndex, endIndex, showGridLine, showPaginateLine, unitSizeList,
                    paperPaintSize, paginateLineList, resolution);
            this.realWidth = realWidth;
        }

        @Override
        protected Double getPaginateLine2D(int sumHeight) {
            return new Line2D.Double(0, sumHeight, gridSize.width, sumHeight);
        }

        @Override
        protected void setLine2D(int sumHeight) {
            tmpLine2D.setLine(0, sumHeight, realWidth, sumHeight);
        }

        @Override
        protected void drawLastLine(Graphics2D g2d, int sumHeight) {
            GraphHelper.drawLine(g2d, 0, sumHeight, realWidth, sumHeight);
        }
    }

    private class DrawHorizontalLineHelper extends DrawLineHelper {
        private double realHeight;

        DrawHorizontalLineHelper(int startIndex, int endIndex, boolean showGridLine,
                                 boolean showPaginateLine, DynamicUnitList unitSizeList, double paperPaintSize,
                                 List paginateLineList, double realHeight, int resolution) {
            super(startIndex, endIndex, showGridLine, showPaginateLine, unitSizeList,
                    paperPaintSize, paginateLineList, resolution);
            this.realHeight = realHeight;
        }

        @Override
        protected Double getPaginateLine2D(int sumWidth) {
            return new Line2D.Double(sumWidth, 0, sumWidth, gridSize.height);
        }

        @Override
        protected void setLine2D(int sumWidth) {
            tmpLine2D.setLine(sumWidth, 0, sumWidth, realHeight);
        }

        @Override
        protected void drawLastLine(Graphics2D g2d, int sumWidth) {
            GraphHelper.drawLine(g2d, sumWidth, 0, sumWidth, realHeight);
        }
    }

    private void paintCellElements(Graphics g, Grid grid, TemplateElementCase report, int resolution) {
        Graphics2D g2d = (Graphics2D) g;

        CellElement selectedCellElement = null;
        ElementCasePane reportPane = grid.getElementCasePane();
        Selection sel = reportPane.getSelection();

        if (sel instanceof CellSelection) {
            selectedCellElement = report.getCellElement(((CellSelection) sel).getColumn(), ((CellSelection) sel).getRow());
        }

        int horizontalBeginValue = grid.getHorizontalBeginValue();
        int verticalBeginValue = grid.getVerticalBeginValue();

        // 元素数目.
        Shape oldClip = null;
        TemplateCellElement tmpCellElement = null;

        Iterator cells = report.intersect(horizontalBeginValue, verticalBeginValue,
                horizontalEndValue - horizontalBeginValue, verticalEndValue - verticalBeginValue);

        // 计算隐藏掉的width
        double hideWidth = columnWidthList.getRangeValue(0, horizontalValue).toPixD(resolution);
        double hideHeight = rowHeightList.getRangeValue(0, verticalValue).toPixD(resolution);

        // 清空left和top的绘画区域.
        this.left_col_row_rect.setRect(0, 0, 0, 0);
        this.top_col_row_rect.setRect(0, 0, 0, 0);

        paintDetailedCellElements(g2d, cells, tmpCellElement, reportPane, selectedCellElement, hideWidth, hideHeight, oldClip, report);
        paintBorder(g2d, tmpCellElement, report);
        paintFatherLeft(g2d, selectedCellElement, report);
    }

    private void paintDetailedCellElements(Graphics2D g2d, Iterator cells, TemplateCellElement tmpCellElement, ElementCasePane reportPane,
                                           CellElement selectedCellElement, double hideWidth, double hideHeight, Shape oldClip, TemplateElementCase report) {
        while (cells.hasNext()) {
            tmpCellElement = (TemplateCellElement) cells.next();
            if (tmpCellElement == null) {
                continue;
            }
            // 强制分页线
            this.calculateForcedPagingOfCellElement(reportPane, tmpCellElement, hideWidth, hideHeight);
            storeFatherLocation(selectedCellElement, tmpCellElement);
            // element bounds
            // TODO: 2017/7/13  tmpRectangle : 72*19
            this.caculateScrollVisibleBounds(this.tmpRectangle, tmpCellElement.getColumn(),
                    tmpCellElement.getRow(), tmpCellElement.getColumnSpan(),
                    tmpCellElement.getRowSpan());
            // peter:clip的区域.
            // peter:保留clip.
            oldClip = g2d.getClip();            /*
             * alex:此处的tmpRectangle_1被GridUtils.validate判断必定为true,
             * 因为这些tmpCellElement是intersect的结果 所以,不必判断了
             */
            g2d.clip(this.tmpRectangle);

            // 这边为什么要加1? 因为单元格的左边和上面有线，宽度为一，属于此单元格，画单元格的内容不应该把那条线给遮住了
            g2d.translate(this.tmpRectangle.getX() + 1, this.tmpRectangle.getY() + 1);

            // peter:tmpRectangle2D_3只是一个临时的Rectangle2D,由于后面不少地方需要用到这个矩形
            this.cell_back_rect.setRect(0, 0, this.tmpRectangle.getWidth() - 1,
                    this.tmpRectangle.getHeight() - 1);
            // peter:对于合并的单元格,需要先白色的背景来清除背景.
            if (tmpCellElement.getColumnSpan() > 1 || tmpCellElement.getRowSpan() > 1) {
                // REPORT-23492 要看下是否设置了纸张背景 如果设置了按照背景来画
                ReportSettingsProvider reportSettings = getReportSettings(report);
                Background currentBackground = reportSettings.getBackground();
                if (currentBackground != null) {
                    currentBackground.paint(g2d, this.cell_back_rect);
                } else {
                    WHITE_Backgorund.paint(g2d, this.cell_back_rect);
                }
                //daniel:上面这里就有问题了啊....报表的背景在这个之前画的 会覆盖报表背景....不过只是设计器中看到预览浏览没问题
            }
            // peter:将这个元素添加到需要paint的元素列表当中去,留着画边框线..
            paintCellElementList.add(tmpCellElement);
            paintCellElementRectangleList.add(this.tmpRectangle.clone());


            int cellWidth = (int) this.tmpRectangle.getWidth();
            int cellHeight = (int) this.tmpRectangle.getHeight();
            // denny_Grid: 画Grid中单元格的内容(包括单元格的背景Content + Background), 不包括边框

            painter.paintBackground(g2d, report, tmpCellElement, cellWidth, cellHeight);
            painter.paintContent(g2d, report, tmpCellElement, cellWidth, cellHeight, resolution);
            // denny_Grid: 注意下面还要减一, 因为上面translate时加一
            g2d.translate(-this.tmpRectangle.getX() - 1, -this.tmpRectangle.getY() - 1);
            paintAuthorityCell(g2d, tmpCellElement);
            g2d.setClip(oldClip);
        }
    }

    private void paintAuthorityCell(Graphics2D g2d, TemplateCellElement tmpCellElement) {
        String selectedRole = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        boolean isWidgetAuthority = false;
        if (tmpCellElement.getWidget() != null) {
            isWidgetAuthority = tmpCellElement.getWidget().isDirtyWidget(selectedRole);
        }
        boolean isCellDoneAuthority = tmpCellElement.isDoneAuthority(selectedRole) || tmpCellElement.isDoneNewValueAuthority(selectedRole);
        boolean isDoneAuthority = isWidgetAuthority || isCellDoneAuthority;
        if (isAuthority && isDoneAuthority) {
            Composite oldComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            Paint oldPaint = g2d.getPaint();
            g2d.setPaint(UIConstants.AUTHORITY_COLOR);
            GraphHelper.fill(g2d, tmpRectangle);
            g2d.setComposite(oldComposite);
            g2d.setPaint(oldPaint);
        }
    }

    private void storeFatherLocation(CellElement selectedCellElement, TemplateCellElement tmpCellElement) {
//		// 需要检查是否在Design状态隐藏.
//		CellGUIAttr cellGUIAttr = tmpCellElement.getCellGUIAttr();
//		if (cellGUIAttr == null) {
//			// 不set给curCellElement,赋值只是为了方便下面的操作
//			cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
//		}

        /*
         * 记录当前选中的单元格的左父与上父的位置于leftColumnRowRectangle2D &
         * topColumnRowRectangle2D
         */
        if (selectedCellElement == tmpCellElement) {
            CellExpandAttr cellExpandAttr = tmpCellElement.getCellExpandAttr();
            if (cellExpandAttr != null) {
                ColumnRow leftColumnRow = cellExpandAttr.getLeftParentColumnRow();
                // leftColumnRow必须在可视范围内
                if (ColumnRow.validate(leftColumnRow)) {
                    this.caculateScrollVisibleBounds(this.left_col_row_rect,
                            leftColumnRow.getColumn(), leftColumnRow.getRow(), 1, 1);
                }

                ColumnRow topColumnRow = cellExpandAttr.getUpParentColumnRow();
                // topColumnRow必须在可视范围内
                if (ColumnRow.validate(topColumnRow)) {
                    this.caculateScrollVisibleBounds(this.top_col_row_rect,
                            topColumnRow.getColumn(), topColumnRow.getRow(), 1, 1);
                }
            }
        }
    }

    private void paintBorder(Graphics2D g2d, TemplateCellElement tmpCellElement, TemplateElementCase report) {
        // 画边框
        Rectangle2D.Double tmpCellElementRectangle;
        for (int i = 0; i < paintCellElementList.size(); i++) {
            tmpCellElement = (TemplateCellElement) paintCellElementList.get(i);
            tmpCellElementRectangle = (Rectangle2D.Double) paintCellElementRectangleList.get(i);

            // 需要检查是否在Design状态隐藏.
            CellGUIAttr cellGUIAttr = tmpCellElement.getCellGUIAttr();
            if (cellGUIAttr == null) {
                // 不set给curCellElement,赋值只是为了方便下面的操作
                cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
            }

            g2d.translate(tmpCellElementRectangle.getX(), tmpCellElementRectangle.getY());

            painter.paintBorder(g2d, report, tmpCellElement, tmpCellElementRectangle.getWidth(),
                    tmpCellElementRectangle.getHeight());

            g2d.translate(-tmpCellElementRectangle.getX(), -tmpCellElementRectangle.getY());
        }
    }

    protected void paintFatherLeft(Graphics2D g2d, CellElement selectedCellElement, TemplateElementCase report) {
        // 画左父格子.
        if (validate(this.left_col_row_rect) && this.left_col_row_rect.getHeight() > 5) {
            g2d.setPaint(Color.BLUE);
            double centerX = this.left_col_row_rect.getX() + 4;
            double centerY = this.left_col_row_rect.getY() + this.left_col_row_rect.getHeight() / 2;
            GraphHelper.drawLine(g2d, centerX, centerY - 5, centerX, centerY + 5);
            GeneralPath polyline = new GeneralPath(Path2D.WIND_EVEN_ODD, 3);
            polyline.moveTo((float) centerX, (float) centerY + 5);
            polyline.lineTo((float) centerX + 3, (float) centerY + 5 - 4);
            polyline.lineTo((float) centerX - 2, (float) centerY + 5 - 4);
            GraphHelper.fill(g2d, polyline);
        }
        if (validate(this.top_col_row_rect) && this.top_col_row_rect.getWidth() > 5) {
            g2d.setPaint(Color.BLUE);
            double centerX = this.top_col_row_rect.getX() + this.top_col_row_rect.getWidth() / 2;
            double centerY = this.top_col_row_rect.getY() + 4;
            GraphHelper.drawLine(g2d, centerX - 5, centerY, centerX + 5, centerY);
            GeneralPath polyline = new GeneralPath(Path2D.WIND_EVEN_ODD, 3);
            polyline.moveTo((float) centerX + 5, (float) centerY);
            polyline.lineTo((float) centerX + 5 - 4, (float) centerY + 3);
            polyline.lineTo((float) centerX + 5 - 4, (float) centerY - 3);

            GraphHelper.fill(g2d, polyline);
        }
    }

    private void paintPaginateLines(Graphics g, Grid grid) {
        Graphics2D g2d = (Graphics2D) g;

        // james 画分页线
        if (this.paginateLineList.size() > 0) {
            Line2D tmpLine2D = new Line2D.Double(0, 0, 0, 0);

            // james
            // 梳理forcedPaperMarginLineList，将其中相同的线去除，否则在Grid的paintConponet时会因setXORMode而将相同的线给覆盖掉
            Line2D tmpLine2D2;
            for (int j = 0; j < paginateLineList.size(); j++) {
                tmpLine2D = (Line2D) paginateLineList.get(j);// 直接强制转换，因为List中肯定都是Line2D型的
                for (int k = j + 1; k < paginateLineList.size(); k++) {
                    tmpLine2D2 = (Line2D) paginateLineList.get(k);
                    if (AssistUtils.equals(tmpLine2D2.getX1() ,tmpLine2D.getX1())
                            && AssistUtils.equals(tmpLine2D2.getX2() , tmpLine2D.getX2())
                            && AssistUtils.equals(tmpLine2D2.getY1() , tmpLine2D.getY1())
                            && AssistUtils.equals(tmpLine2D2.getY2() , tmpLine2D.getY2())) {
                        paginateLineList.remove(k);
                    }
                }
            }

            g2d.setPaint(grid.getPaginationLineColor());

            //g2d.setXORMode(Utils.getXORColor(grid.getPaginationLineColor()));
            GraphHelper.setStroke(g2d, GraphHelper.getStroke(Constants.LINE_DASH_DOT));
            if (grid.getPaginateLineShowType() == Grid.SINGLE_HORIZONTAL_PAGINATE_LINE) {
                g2d.draw((Shape) paginateLineList.get(0));
            } else {
                for (int i = 0, len = paginateLineList.size(); i < len; i++) {
                    g2d.draw((Shape) paginateLineList.get(i));
                }
            }

            g2d.setPaintMode();
        }
    }

    private void paintGridSelection(Graphics g, Grid grid, ElementCase report) {
        Graphics2D g2d = (Graphics2D) g;
        Selection sel = grid.getElementCasePane().getSelection();
        // 画GridSelection:CELL
        if (sel instanceof CellSelection) {// james:cell不可以select的时候就不画了
            CellSelection gridSelection = (CellSelection) sel;
            // peter:开始选择框CellRectangle.
            Rectangle editRectangle = gridSelection.getEditRectangle();
            int cellRectangleCount = gridSelection.getCellRectangleCount();
            Area selectedCellRectArea = new Area();
            Area editCellRectArea = new Area();
            // denny: editCellRectArea
            ElementCasePane reportPane = grid.getElementCasePane();
            if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_NULL) {
                this.caculateScrollVisibleBounds(this.tmpRectangle, new Rectangle(
                        gridSelection.getColumn(), gridSelection.getRow(), gridSelection.getColumnSpan(), gridSelection.getRowSpan()
                ));
                if (validate(this.tmpRectangle)) {
                    editCellRectArea.add(new Area(this.tmpRectangle));
                }
                if (cellRectangleCount == 1) {
                    paintOne(selectedCellRectArea, gridSelection, grid, g2d);
                } else {
                    paintMore(selectedCellRectArea, gridSelection, grid, g2d, cellRectangleCount, editCellRectArea);
                }
            } else {
                Rectangle referenced = null;
                referenced = paintReferenceCell(reportPane, grid, g2d, editCellRectArea, selectedCellRectArea, referenced);
                paintFormatArea(selectedCellRectArea, gridSelection, grid, g2d, referenced);
            }
            // denny: 标记公式用到的单元格
            paintGridSelectionForFormula(g2d, report, gridSelection);
        }
    }

    private Rectangle paintReferenceCell(ElementCasePane reportPane, Grid grid, Graphics2D g2d, Area editCellRectArea, Area selectedCellRectArea, Rectangle referenced) {
        CellSelection referencedCell = reportPane.getFormatReferencedCell();
        if (referencedCell == null) {
            return null;
        }

        if (DesignerContext.getReferencedIndex() !=
                ((JTemplate) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()).getEditingReportIndex()) {
            return null;
        }
        referenced = new Rectangle(
                referencedCell.getColumn(), referencedCell.getRow(), referencedCell.getColumnSpan(), referencedCell.getRowSpan()
        );
        this.caculateScrollVisibleBounds(this.tmpRectangle, referenced);
        if (validate(this.tmpRectangle)) {
            editCellRectArea.add(new Area(this.tmpRectangle));
        }
        paintOne(selectedCellRectArea, referencedCell, grid, g2d);
        return referenced;
    }


    private void paintFormatArea(Area selectedCellRectArea, CellSelection gridSelection, Grid grid, Graphics2D g2d, Rectangle referenced) {
        Rectangle format = new Rectangle(
                gridSelection.getColumn(), gridSelection.getRow(), gridSelection.getColumnSpan(), gridSelection.getRowSpan()
        );
        if (ComparatorUtils.equals(DesignerContext.getReferencedElementCasePane(), grid.getElementCasePane()) && ComparatorUtils.equals(format, referenced)) {
            return;
        }

        this.caculateScrollVisibleBounds(this.tmpRectangle, format);
        if (validate(this.tmpRectangle)) {
            selectedCellRectArea = new Area(this.tmpRectangle);
            double selectedCellX = this.tmpRectangle.getX();
            double selectedCellY = this.tmpRectangle.getY();
            double selectedCellWidth = this.tmpRectangle.getWidth();
            double selectedCellHeight = this.tmpRectangle.getHeight();

            // TODO ALEX_SEP 这个半透明的,画不画呢?
            // peter:画半透明的背景,必须SelectCellRectangle和EditCellRectangle不重复才需要半透明背景.
            if (gridSelection.getRowSpan() > 1
                    || gridSelection.getColumnSpan() > 1) {
                Composite oldComposite = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setPaint(grid.getSelectedBackground());
                GraphHelper.fill(g2d, selectedCellRectArea);
                g2d.setComposite(oldComposite);
            }


            paintNormal(g2d, selectedCellX, selectedCellY, selectedCellWidth, selectedCellHeight, grid);
        }

    }

    private void paintOne(Area selectedCellRectArea, CellSelection gridSelection, Grid grid, Graphics2D g2d) {
        if (validate(this.tmpRectangle)) {
            selectedCellRectArea = new Area(this.tmpRectangle);
            double selectedCellX = this.tmpRectangle.getX();
            double selectedCellY = this.tmpRectangle.getY();
            double selectedCellWidth = this.tmpRectangle.getWidth();
            double selectedCellHeight = this.tmpRectangle.getHeight();

            // TODO ALEX_SEP 这个半透明的,画不画呢?
            // peter:画半透明的背景,必须SelectCellRectangle和EditCellRectangle不重复才需要半透明背景.
            if (gridSelection.getRowSpan() > 1
                    || gridSelection.getColumnSpan() > 1) {
                Composite oldComposite = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setPaint(grid.getSelectedBackground());
                GraphHelper.fill(g2d, selectedCellRectArea);
                g2d.setComposite(oldComposite);
            }


            // 判断是否使用智能添加单元格若使用智能添加单元格，则画动态虚线
            paintGridSelectionDependsOnTableSelectionPane(g2d, selectedCellX, selectedCellY, selectedCellWidth, selectedCellHeight, grid);
        }
    }


    private void paintMore(Area selectedCellRectArea, CellSelection gridSelection, Grid grid, Graphics2D g2d, int cellRectangleCount, Area editCellRectArea) {

        selectedCellRectArea = new Area();
        // cellRectangleCount > 1

        // p:下面处理多个选择的情况，麻烦一些.
        for (int i = 0; i < cellRectangleCount; i++) {
            Rectangle cellRectangle = gridSelection.getCellRectangle(i);

            // p:计算CellSelection.
            this.caculateScrollVisibleBounds(this.tmpRectangle, cellRectangle);

            if (validate(this.tmpRectangle)) {
                selectedCellRectArea.add(new Area(this.tmpRectangle));
                paintGridSelectionDependsOnTableSelectionPane(g2d, tmpRectangle.x, tmpRectangle.y, tmpRectangle.width, tmpRectangle.height, grid);
            }
        }

        selectedCellRectArea.add(editCellRectArea);
        selectedCellRectArea.exclusiveOr(editCellRectArea);

        // denny: 画半透明背景
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setPaint(grid.getSelectedBackground());
        GraphHelper.fill(g2d, selectedCellRectArea);
        g2d.setComposite(oldComposite);

        // p:画edit空白区域的边框.
        g2d.setPaint(Color.blue);
        // marks:如果editCellRectArea为空，就不需要画，说明都不在可见区域内
        if (editCellRectArea != null) {
            GraphHelper.draw(g2d, editCellRectArea);
        }


    }


    private void paintGridSelectionForFormula(Graphics2D g2d, ElementCase report, CellSelection cs) {
        // denny: 标记公式用到的单元格
        if (report.getCellValue(cs.getColumn(), cs.getRow()) instanceof BaseFormula) {
            BaseFormula tmpFormula = (BaseFormula) report
                    .getCellValue(cs.getColumn(), cs.getRow());
            String statement = tmpFormula.getContent();
            // denny: 获得公式中包含的所有单元格
            ColumnRow[] columnRowArray = new ColumnRow[0];
            try {
                columnRowArray = CalculatorUtils.relatedColumnRowArray(statement.substring(1));
            } catch (ANTLRException ae) {
                // do nothing.
            }
            Area formulaCellArea = null;
            for (int i = 0; i < columnRowArray.length; i++) {
                ColumnRow columnRow = columnRowArray[i];
                int columnSpan = 1;
                int rowSpan = 1;
                CellElement columnRowCE = report.getCellElement(columnRow.getColumn(),
                        columnRow.getRow());
                if (columnRowCE != null) {
                    columnSpan = columnRowCE.getColumnSpan();
                    rowSpan = columnRowCE.getRowSpan();
                    // _denny: 如果取得是合并单元格的后面的，那么需要重置columnRow
                    if (columnSpan > 1 || rowSpan > 1) {
                        columnRow = ColumnRow.valueOf(columnRowCE.getColumn(),
                                columnRowCE.getRow());
                    }
                }

                this.caculateScrollVisibleBounds(this.tmpRectangle, columnRow.getColumn(),
                        columnRow.getRow(), columnSpan, rowSpan);

                if (validate(this.tmpRectangle)) {
                    paintFormulaCellArea(g2d, formulaCellArea, i);
                }
            }
        }
    }

    protected void paintFormulaCellArea(Graphics2D g2d, Area formulaCellArea, int i) {
        // denny: 标记格子的边框
        formulaCellArea = new Area(new Rectangle2D.Double(this.tmpRectangle.getX(),
                this.tmpRectangle.getY(), this.tmpRectangle.getWidth(),
                this.tmpRectangle.getHeight()));

        // peter:去掉edit的格子区域
        formulaCellArea.exclusiveOr(new Area(
                new Rectangle2D.Double(this.tmpRectangle.getX() + 1,
                        this.tmpRectangle.getY() + 1,
                        this.tmpRectangle.getWidth() - 2, this.tmpRectangle
                        .getHeight() - 2)));

        formulaCellArea.add(new Area(new Rectangle2D.Double(this.tmpRectangle
                .getX(), this.tmpRectangle.getY(), 3, 3)));
        formulaCellArea.add(new Area(new Rectangle2D.Double(this.tmpRectangle
                .getX() + this.tmpRectangle.getWidth() - 3, this.tmpRectangle
                .getY(), 3, 3)));
        formulaCellArea.add(new Area(new Rectangle2D.Double(this.tmpRectangle
                .getX(), this.tmpRectangle.getY() + this.tmpRectangle.getHeight()
                - 3, 3, 3)));
        formulaCellArea.add(new Area(new Rectangle2D.Double(this.tmpRectangle
                .getX() + this.tmpRectangle.getWidth() - 3, this.tmpRectangle
                .getY() + this.tmpRectangle.getHeight() - 3, 3, 3)));
        g2d.setPaint(new Color(((i + 2) * 50) % 256, ((i + 1) * 50) % 256,
                (i * 50) % 256));
        if (formulaCellArea != null) {
            GraphHelper.fill(g2d, formulaCellArea);
        }
    }

    private void paintGridSelectionDependsOnTableSelectionPane(Graphics2D g2d, double selectedCellX, double selectedCellY,
                                                               double selectedCellWidth, double selectedCellHeight, Grid grid) {
        if (grid.IsNotShowingTableSelectPane()) {
            // peter:构建并开始画边框,只需要对第一个Cell
            paintNormal(g2d, selectedCellX, selectedCellY, selectedCellWidth, selectedCellHeight, grid);
        } else {
            // 动态虚线
            Stroke stroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(1));
            if (drawFlowRect == null) {
                drawFlowRect = new DrawFlowRect();
            }
            drawFlowRect.setGrid(grid);
            drawFlowRect.drawFlowRect(g2d, (int) selectedCellX, (int) selectedCellY,
                    (int) selectedCellWidth + (int) selectedCellX,
                    (int) selectedCellHeight + (int) selectedCellY);
            g2d.setStroke(stroke);
        }
    }


    private void paintNormal(Graphics2D g2d, double selectedCellX, double selectedCellY,
                             double selectedCellWidth, double selectedCellHeight, Grid grid) {
        this.back_or_selection_rect.setRect(selectedCellX - 1, selectedCellY - 1,
                selectedCellWidth + 3, selectedCellHeight + 3);
        Area borderLineArea = new Area(this.back_or_selection_rect);
        this.back_or_selection_rect.setRect(selectedCellX + 2, selectedCellY + 2,
                selectedCellWidth - 3, selectedCellHeight - 3);
        borderLineArea.exclusiveOr(new Area(this.back_or_selection_rect));
        this.back_or_selection_rect.setRect(selectedCellX + selectedCellWidth - 1,
                selectedCellY + selectedCellHeight - 3, 3, 1);
        borderLineArea.exclusiveOr(new Area(this.back_or_selection_rect));
        this.back_or_selection_rect.setRect(selectedCellX + selectedCellWidth - 3,
                selectedCellY + selectedCellHeight - 1, 1, 3);
        borderLineArea.exclusiveOr(new Area(this.back_or_selection_rect));
        // peter:右下角落的那个小黑方块.
        this.back_or_selection_rect.setRect(selectedCellX + selectedCellWidth - 2,
                selectedCellY + selectedCellHeight - 2, 5, 5);
        borderLineArea.add(new Area(this.back_or_selection_rect));

        //g2d.setXORMode(Utils.getXORColor(grid.getSelectedBorderLineColor()));
        g2d.setPaint(grid.getSelectedBorderLineColor());
        GraphHelper.fill(g2d, borderLineArea);
        g2d.setPaintMode();
    }

    private void paintFloatElements(Graphics g, Grid grid, ElementCase report, int resolution) {
        Graphics2D g2d = (Graphics2D) g;

        Selection sel = grid.getElementCasePane().getSelection();

        Iterator flotIt = report.floatIterator();
        while (flotIt.hasNext()) {
            FloatElement tmpFloatElement = (FloatElement) flotIt.next();

            int lastRow = rowHeightList.getValueIndex(FU.getInstance(
                    tmpFloatElement.getTopDistance().toFU() + tmpFloatElement.getHeight().toFU()
            ));

            // 限制相对位置.
            if (isSuitablePosition(lastRow)) {
                float floatX = columnWidthList.getRangeValue(horizontalValue, 0).toPixF(
                        resolution)
                        + tmpFloatElement.getLeftDistance().toPixF(resolution);
                float floatY = rowHeightList.getRangeValue(verticalValue, 0).toPixF(resolution)
                        + tmpFloatElement.getTopDistance().toPixF(resolution);

                g2d.translate(floatX, floatY);

                PaintUtils.paintFloatElement(g2d, tmpFloatElement,
                        tmpFloatElement.getWidth().toPixI(resolution),
                        tmpFloatElement.getHeight().toPixI(resolution), resolution);

                g2d.translate(-floatX, -floatY);
            }
        }

        // p:画选中的悬浮元素的边框,这里必须再循环一次所有的悬浮元素，
        // 因为这些方框选择点，及时这个悬浮元素被覆盖的时候，还能够选中点击的，来改变元素的尺寸的.
        paintFloatElementsBorder(g2d, grid, sel, flotIt, report);

        paintAuthorityFloatElement(g2d, report, ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName(), grid);
    }

    private boolean isSuitablePosition(int lastRow) {
        // confusing logic...
        return 0 >= verticalValue || 0 <= verticalEndValue || lastRow >= verticalValue
                || lastRow <= verticalValue;
    }

    private void paintFloatElementsBorder(Graphics2D g2d, Grid grid, Selection sel, Iterator flotIt, ElementCase report) {
        // p:画选中的悬浮元素的边框,这里必须再循环一次所有的悬浮元素，
        // 因为这些方框选择点，及时这个悬浮元素被覆盖的时候，还能够选中点击的，来改变元素的尺寸的.
        if (sel instanceof FloatSelection) {
            flotIt = report.floatIterator();
            while (flotIt.hasNext()) {
                FloatElement tmpFloatElement = (FloatElement) flotIt.next();
                // p:如果不是选中的悬浮元素,直接continue.
                if (!ComparatorUtils.equals(tmpFloatElement.getName(), ((FloatSelection) sel).getSelectedFloatName())) {
                    continue;
                }

                Rectangle2D[] rectArray = calculateFloatElementPoints(tmpFloatElement);

                GraphHelper.setStroke(g2d, GraphHelper.getStroke(Constants.LINE_THIN));
                for (int j = 0; j < rectArray.length; j++) {
                    g2d.setPaint(Utils.getXORColor(grid.getSelectedBorderLineColor()));
                    GraphHelper.fill(g2d, rectArray[j]);
                    g2d.setPaint(grid.getSelectedBorderLineColor());
                    GraphHelper.draw(g2d, rectArray[j]);
                }
            }
        }
    }


    private Rectangle2D[] calculateFloatElementPoints(FloatElement tmpFloatElement) {
        // width and height
        float floatX1 = columnWidthList.getRangeValue(horizontalValue, 0).toPixF(
                resolution) + tmpFloatElement.getLeftDistance().toPixF(resolution);
        float floatY1 = rowHeightList.getRangeValue(verticalValue, 0).toPixF(resolution)
                + tmpFloatElement.getTopDistance().toPixF(resolution);
        int floatX2 = (int) (floatX1 + tmpFloatElement.getWidth().toPixF(resolution));
        int floatY2 = (int) (floatY1 + tmpFloatElement.getHeight().toPixF(resolution));

        return new Rectangle2D[]{
                new Rectangle2D.Double(floatX1 - 3, floatY1 - 3, 6, 6),
                new Rectangle2D.Double((floatX1 + floatX2) / 2 - 3, floatY1 - 3, 6, 6),
                new Rectangle2D.Double(floatX2 - 3, floatY1 - 3, 6, 6),
                new Rectangle2D.Double(floatX2 - 3, (floatY1 + floatY2) / 2 - 3, 6, 6),
                new Rectangle2D.Double(floatX2 - 3, floatY2 - 3, 6, 6),
                new Rectangle2D.Double((floatX1 + floatX2) / 2 - 3, floatY2 - 3, 6, 6),
                new Rectangle2D.Double(floatX1 - 3, floatY2 - 3, 6, 6),
                new Rectangle2D.Double(floatX1 - 3, (floatY1 + floatY2) / 2 - 3, 6, 6)};
    }


    private void paintAuthorityFloatElement(Graphics2D g2d, ElementCase report, String selectedRoles, Grid grid) {
        Iterator flotIt = report.floatIterator();
        while (flotIt.hasNext()) {
            FloatElement tmpFloatElement = (FloatElement) flotIt.next();
            // p:如果不是选中的悬浮元素,直接continue.
            if (!tmpFloatElement.getFloatPrivilegeControl().checkInvisible(selectedRoles)) {
                continue;
            }
            float floatX1 = columnWidthList.getRangeValue(horizontalValue, 0).toPixF(
                    resolution) + tmpFloatElement.getLeftDistance().toPixF(resolution);
            float floatY1 = rowHeightList.getRangeValue(verticalValue, 0).toPixF(resolution)
                    + tmpFloatElement.getTopDistance().toPixF(resolution);
            int floatX2 = (int) (floatX1 + tmpFloatElement.getWidth().toPixF(resolution));
            int floatY2 = (int) (floatY1 + tmpFloatElement.getHeight().toPixF(resolution));
            double x = floatX1;
            double y = floatY1;
            double width = floatX2 - x;
            double height = floatY2 - y;

            //画做过权限编辑过的悬浮元素的边框
            if (isAuthority) {
                paintAuthorityFloatElementBorder(g2d, x, y, width, height, grid);
            }
        }
    }

    private void paintAuthorityFloatElementBorder(Graphics2D g2d, double selectedCellX, double selectedCellY,
                                                  double selectedCellWidth, double selectedCellHeight, Grid grid) {
        this.back_or_selection_rect.setRect(selectedCellX - 1, selectedCellY - 1,
                selectedCellWidth + 3, selectedCellHeight + 3);
        Area borderLineArea = new Area(this.back_or_selection_rect);
        g2d.setXORMode(Utils.getXORColor(grid.getSelectedBorderLineColor()));
        g2d.setPaint(UIConstants.AUTHORITY_COLOR);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        GraphHelper.fill(g2d, borderLineArea);
        g2d.setPaintMode();
    }

    private void paintDragCellBorder(Graphics g, Grid grid) {
        Graphics2D g2d = (Graphics2D) g;

        if ((grid.getDragType() == GridUtils.DRAG_CELLSELECTION || grid.getDragType() == GridUtils.DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER)
                && (grid.getDragRectangle() != null)) {
            this.caculateScrollVisibleBounds(this.drag_cell_rect, grid.getDragRectangle());

            g2d.setPaint(Color.GRAY);
            GraphHelper.draw(g2d, this.drag_cell_rect, Constants.LINE_THICK);
        }
    }

    @Override
    /**
     * paint
     */
    public void paint(Graphics g, JComponent c) {
        if (!(c instanceof Grid)) {
            throw new IllegalArgumentException("The component c to paint must be a Grid!");
        }

        isAuthority = DesignerMode.isAuthorityEditing();

        Graphics2D g2d = (Graphics2D) g;

        Grid grid = (Grid) c;

        // 取得ElementCasePane.ElementCase
        ElementCasePane elementCasePane = grid.getElementCasePane();
        TemplateElementCase elementCase = elementCasePane.getEditingElementCase();// 取出ElementCase对象

        dealWithSizeBeforePaint(grid, elementCase);

        double realWidth = gridSize.getWidth();// 宽度
        double realHeight = gridSize.getHeight();// 高度

        // 画背景
        this.paintBackground(g2d, grid, elementCase, resolution);

        // 画Grid Line
        this.paintGridLine(g2d, grid, realWidth, realHeight, resolution);

        // peter:添上线程的支持,有时候,paint元素的时候,可能会有元素被删除了.
        // 需要先清除画Border需要的元素.
        paintCellElementList.clear();
        paintCellElementRectangleList.clear();

        // 画单元格元素
        this.paintCellElements(g2d, grid, elementCase, resolution);

        // 画分页线
        this.paintPaginateLines(g2d, grid);

        // 画 GridSelection
        if (!(isAuthority && elementCase instanceof WorkSheet && !((WorkSheet) elementCase).isPaintSelection())) {
            this.paintGridSelection(g2d, grid, elementCase);
        }

        // 画悬浮元素
        this.paintFloatElements(g2d, grid, elementCase, resolution);

        // 画Drag格子的边框.
        this.paintDragCellBorder(g2d, grid);

        // 画水印
        if (elementCase instanceof WorkSheet) {
            paintWatermark(g2d, ((WorkSheet) elementCase).getBook());
        }

        grid.ajustEditorComponentBounds(); // refresh size
    }

    // 绘制水印
    private void paintWatermark(Graphics2D g2d, FineBook book) {
        WatermarkAttr watermark = ReportUtils.getWatermarkAttrFromTemplateAndGlobal(book);
        WatermarkPainter painter = WatermarkPainter.createPainter(watermark, resolution);
        painter.paint(g2d, gridSize.width, gridSize.height);
    }


    private void dealWithSizeBeforePaint(Grid grid, TemplateElementCase elementCase) {
        // 取出所有的行高和列宽的List
        this.rowHeightList = ReportHelper.getRowHeightList(elementCase);
        this.columnWidthList = ReportHelper.getColumnWidthList(elementCase);

        this.verticalValue = grid.getVerticalValue();
        this.horizontalValue = grid.getHorizontalValue();

        int verticalExtent = grid.getVerticalExtent();
        int horizontalExtent = grid.getHorizontalExtent();

        // denny: set the verticalBeginValue and horizontalBeginValue
        grid.setVerticalBeinValue(verticalValue);
        grid.setHorizontalBeginValue(horizontalValue);
        // denny: end

        // 获得控件的实际尺寸
        this.gridSize = grid.getSize();

        this.verticalEndValue = verticalValue + verticalExtent + 1;
        this.horizontalEndValue = horizontalValue + horizontalExtent + 1;
    }

    /**
     * 计算单元格可见的边框范围
     *
     * @param rect        用于计算的rect
     * @param cellElement 单元格
     */
    public void caculateScrollVisibleBounds(Rectangle2D.Double rect, CellElement cellElement) {
        caculateScrollVisibleBounds(rect, cellElement.getColumn(), cellElement.getRow(),
                cellElement.getColumnSpan(), cellElement.getRowSpan());
    }

    /**
     * 计算矩形可见的边框范围
     *
     * @param rect   用于计算的rect
     * @param target 目标矩形
     */
    public void caculateScrollVisibleBounds(Rectangle2D.Double rect, Rectangle target) {
        caculateScrollVisibleBounds(rect, target.x, target.y, target.width, target.height);
    }

    /**
     * 计算(int column,int row,int columnSpan,int rowSpan,),在Grid控件上面的位置.
     * 注意:返回的paintRectangle和clipRectangle不能为null,来区别是否不需要paint或者没有clip.
     * 因为这个方法用在Grid里面,为了加快Grid的paint速度,不能不停的new Rectangle()来增加内存的消耗.
     * <p/>
     * 处理过的tmpRectangle2Ds必须经过validate的处理才可以使用!!
     *
     * @param rect       rect是一个长度为8的Rectangle2D数组, 绘画的区域不可见 paintRectangle.x =INVALID_INTEGER
     * @param column     列
     * @param row        行
     * @param columnSpan 列数
     * @param rowSpan    行数
     */
    public void caculateScrollVisibleBounds(Rectangle2D.Double rect, int column, int row,
                                            int columnSpan, int rowSpan) {
        // 判断是否在不可见的区域.
        if (outOfScreen(column, row, columnSpan, rowSpan)) {
            // 如果在屏幕之外,变成INVALID_INTEGER
            rect.x = INVALID_INTEGER;
        } else {
            rect.x = (columnWidthList.getRangeValue(horizontalValue, column))
                    .toPixD(resolution);
            rect.y = (rowHeightList.getRangeValue(verticalValue, row)).toPixD(resolution);
            rect.width = (columnWidthList.getRangeValue(column, column + columnSpan))
                    .toPixD(resolution);
            rect.height = (rowHeightList.getRangeValue(row, row + rowSpan)).toPixD(resolution);
        }
    }

    private boolean outOfScreen(int column, int row, int columnSpan, int rowSpan) {
        return column > horizontalEndValue || row > verticalEndValue
                || column + columnSpan <= horizontalValue || row + rowSpan <= verticalValue;
    }

    /**
     * 是否有效的Rectangle2D
     *
     * @param rect 目标rect
     * @return 有效返回true
     */
    public static boolean validate(Rectangle2D rect) {
        return rect != null && !ComparatorUtils.equals(rect.getX(), INVALID_INTEGER);
    }


    /**
     * double frozenHeight从顶部到冻结窗口的格子高度
     *
     * @param reportPane     用于计算的单元格面板
     * @param tmpCellElement 用于计算的element
     * @param hideWidth      由于冻结窗口或滚动条滚动隐藏了的总宽度 double
     * @param hideHeight     由于冻结窗口或滚动条滚动隐藏了的总高度 double frozenWidth;//从左侧到冻结窗口的格子宽度
     */
    public void calculateForcedPagingOfCellElement(ElementCasePane reportPane,
                                                   CellElement tmpCellElement, double hideWidth, double hideHeight) {
        if (tmpCellElement == null) {
            return;
        }

        // 找出所有需要paint的强制分页线
        CellPageAttr cellPageAttr = tmpCellElement.getCellPageAttr();
        if (cellPageAttr == null) {
            return;
        }

        ElementCase report = reportPane.getEditingElementCase();

        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);

        int width = reportPane.getSize().width;
        int height = reportPane.getSize().height;

        double sumWidth;// 分页线的起始x位置
        double sumHeight;// 分页线的起始y位置
        if (cellPageAttr.isPageAfterColumn()) {
            sumWidth = columnWidthList.getRangeValueFromZero(
                    tmpCellElement.getColumn() + tmpCellElement.getColumnSpan()).toPixD(resolution)
                    - hideWidth;
            paginateLineList.add(new Line2D.Double(sumWidth, 0, sumWidth, height));
        }
        if (cellPageAttr.isPageBeforeColumn()) {
            sumWidth = columnWidthList.getRangeValueFromZero(tmpCellElement.getColumn()).toPixD(
                    resolution)
                    - hideWidth;
            paginateLineList.add(new Line2D.Double(sumWidth, 0, sumWidth, height));
        }
        if (cellPageAttr.isPageAfterRow()) {
            sumHeight = rowHeightList.getRangeValueFromZero(
                    tmpCellElement.getRow() + tmpCellElement.getRowSpan()).toPixD(resolution)
                    - hideHeight;
            paginateLineList.add(new Line2D.Double(0, sumHeight, width, sumHeight));
        }
        if (cellPageAttr.isPageBeforeRow()) {
            sumHeight = rowHeightList.getRangeValueFromZero(tmpCellElement.getRow()).toPixD(
                    resolution)
                    - hideHeight;
            paginateLineList.add(new Line2D.Double(0, sumHeight, width, sumHeight));
        }
    }
}
