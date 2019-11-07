/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly;

import com.fr.base.GraphHelper;
import com.fr.base.Margin;
import com.fr.base.PaperSize;
import com.fr.base.ScreenResolution;
import com.fr.base.iofile.attr.WatermarkAttr;
import com.fr.design.utils.ComponentUtils;
import com.fr.general.Background;

import com.fr.main.FineBook;
import com.fr.page.PaperSettingProvider;
import com.fr.page.ReportSettingsProvider;
import com.fr.page.WatermarkPainter;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.model.AddedData;
import com.fr.poly.model.AddingData;
import com.fr.report.core.ReportUtils;
import com.fr.report.report.Report;
import com.fr.report.report.TemplateReport;
import com.fr.report.stable.ReportConstants;
import com.fr.stable.Constants;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.unit.UNIT;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComponentUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author richer
 * @since 6.5.3 聚合报表设计块的ui类
 */
public class PolyDesignUI extends ComponentUI {
    private static final Color PAGINATE_LINE_COLOR = Color.GRAY;

    private static final double SCROLL_POINT = 50;
    private static final int SCROLL_DISTANCE = 15;
    private static final int TEN = 10;
    private static final int HHUNDRED = 100;
    private PolyDesigner designer;
    private int resolution;
	private float time;

    public PolyDesignUI(int resolution) {
        if (resolution == 0){
            resolution = ScreenResolution.getScreenResolution();
        }
        this.resolution = resolution;
    }

    /**
	 * 从组件中获取设计器, 并赋值
	 *
	 * @param c 组件对象
	 *
	 * @date 2015-2-12-下午2:38:05
	 *
	 */
    public void installUI(JComponent c) {
        designer = ((PolyArea) c).getPolyDesigner();
    }

    private void paintBackground(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = c.getSize();
        TemplateReport report = designer.getTemplateReport();
        g2d.setPaint(Color.WHITE);
        GraphHelper.fillRect(g, 0, 0, d.width, d.height);
        ReportSettingsProvider rs = report.getReportSettings();
        if (rs != null) {
            Background bg = rs.getBackground();
            if (bg != null) {
                Rectangle2D.Double paintShape = new Rectangle2D.Double(0, 0, d.width, d.height);
                bg.paint(g2d, paintShape);
            }
        }
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        this.resolution = ((PolyArea)c).getResolution();
        this.time = (float)resolution/ScreenResolution.getScreenResolution();
        paintBackground(g, c);
        Graphics2D g2d = (Graphics2D) g;
        paintAddedData(g2d);
        AddingData addData = designer.getAddingData();
        // 画准备添加到聚合报表上的设计块
        if (addData != null && addData.getCreator() != null) {
            paintAddingData(g2d, addData);
        }
        paintPaginateLine(g2d);
        paintWatermark(g2d, designer.getTarget().getBook(), c.getWidth(), c.getHeight());
    }

    // 绘制水印
    private void paintWatermark(Graphics2D g2d, FineBook book, int width, int height) {
        WatermarkAttr watermark = ReportUtils.getWatermarkAttrFromTemplateAndGlobal(book);
        WatermarkPainter painter = WatermarkPainter.createPainter(watermark, resolution);
        painter.paint(g2d, width, height);
    }

    private void paintAddedData(Graphics g) {
        AddedData addedData = designer.getAddedData();
        for (int i = 0, count = addedData.getAddedCount(); i < count; i++) {
            BlockCreator creator = addedData.getAddedAt(i);
            // richer:如果当前这个组件正在编辑，那么他是完全被他的编辑器所遮挡的，不需要画出来
			if (creator == designer.getSelection()) {
                paintPositionLine(g, Math.round(creator.getX(time)), Math.round(creator.getY(time)),
                        Math.round(designer.getHorizontalValue()*time), Math.round(designer.getVerticalValue()*time));
                if (creator.getEditor().isDragging()) {
                    creator.getEditor().paintAbsorptionline(g);
                    //如果与其他块重合了, 需要画出提示禁止重叠
                    changeForbiddenWindowVisibility(creator);
                    //到边缘地带自动滚动
                    scrollWhenCreatorAtCorner(creator);
                }else{
                    creator.getEditor().hideForbiddenWindow();
                }
			} else {
                paintCreator(g, creator, creator.getX() - designer.getHorizontalValue(), creator.getY() - designer.getVerticalValue(),
                            creator.getWidth(), creator.getHeight());
            }
        }
    }

    private void changeForbiddenWindowVisibility(BlockCreator creator){
		Rectangle pixRec = creator.getBounds();
		boolean intersected = designer.intersectsAllBlock(creator);
		if(!intersected){
			creator.getEditor().hideForbiddenWindow();
			return;
		}

        int x = (int) (designer.getAreaLocationX() + pixRec.getCenterX()*time - designer.getHorizontalValue());
        int y = (int) (designer.getAreaLocationY() + pixRec.getCenterY()*time - designer.getVerticalValue());
		creator.getEditor().showForbiddenWindow(x, y);
    }

	private Rectangle getCreatorPixRectangle(BlockCreator creator, Point location){
		int width = creator.getWidth();
		int height = creator.getHeight();
		int resx = location.x - width / 2 + designer.getHorizontalValue();
		int resy = location.y - height / 2 + designer.getVerticalValue();
		return new Rectangle(resx, resy, width, height);
	}

	@SuppressWarnings("squid:S2142")
    private void scrollWhenCreatorAtCorner(final BlockCreator creator){
    	Thread scrollThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(100);

					int rightCornerX = creator.getX() + creator.getWidth();
					int rightCornerY = creator.getY() + creator.getHeight();

					JScrollBar horizonBar = designer.getHorizontalScrollBar();
					JScrollBar verticalBar = designer.getVerticalScrollBar();

					int scrollX = designer.getWidth() + horizonBar.getValue();
					int scrollY = designer.getHeight() + verticalBar.getValue();

					//当block的右下角小于当前面板的-SCROLL_POINT时, 开始滚动
					if(rightCornerX > scrollX - SCROLL_POINT){
						horizonBar.setValue(horizonBar.getValue() + SCROLL_DISTANCE);
					}

					if(rightCornerY > scrollY - SCROLL_POINT){
						verticalBar.setValue(verticalBar.getValue() + SCROLL_DISTANCE);
					}
				} catch (InterruptedException e) {
				}
			}
		});
    	scrollThread.start();
    }

    private void paintPositionLine(Graphics g, int x, int y, int deltax, int deltay) {
        int resx = x - deltax;
        int resy = y - deltay;
        g.setColor(Color.gray);
		GraphHelper.drawLine(g, 0, resy, resx, resy, Constants.LINE_DOT);
		GraphHelper.drawLine(g, resx, 0, resx, resy, Constants.LINE_DOT);
		GraphHelper.drawString(g, convertUnit(y), 0, resy);
		GraphHelper.drawString(g, convertUnit(x), resx, 10);
    }

	private String convertUnit(int i) {
		short unit = designer.getRulerLengthUnit();
//		int resolution = ScreenResolution.getScreenResolution();
		if (unit == Constants.UNIT_MM) {
			Double j = (i + 2) * 1.0 * Constants.HUNDRED_FU_PER_INCH / Constants.HUNDRED_FU_PER_MM / resolution;
			return j.intValue() + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_MM");
		} else if (unit == Constants.UNIT_CM) {
			Double j = (i + 2) * 1.0 * Constants.HUNDRED_FU_PER_INCH / Constants.HUNDRED_FU_PER_MM / resolution;
			return new DecimalFormat("0.0").format(j.intValue() * 1.0 / TEN) + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_CM");
		} else if (unit == Constants.UNIT_INCH) {
			Double j = i == 0 ? 0 : (i + 2) * 1.0 / resolution * HHUNDRED;
			return new DecimalFormat("0.00").format(j.intValue() * 1.0 / HHUNDRED) + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_INCH");
		} else if (unit == Constants.UNIT_PT) {
			int j = i == 0 ? 0 : (i + 2) * UNIT.PT_PER_INCH / resolution;
			return j + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_PT");
		} else {
			return "" + i;
		}
	}

    private void paintAddingData(Graphics g, AddingData addingData) {
        BlockCreator comp = addingData.getCreator();
        int x = (int) (addingData.getCurrentX() / time);
        int y = (int) (addingData.getCurrentY() / time);
        int width = comp.getWidth();
        int height = comp.getHeight();
        paintCreator(g, comp, x, y, width, height);
    }

    private void paintCreator(Graphics g, JComponent comp, int x, int y, int width, int height) {
        ArrayList<JComponent> dbcomponents = new ArrayList<JComponent>();
        // richer:禁止双缓冲行为,否则会出现两个图像
        ComponentUtils.disableBuffer(comp, dbcomponents);
//        if (comp instanceof ECBlockCreator) {
//            Graphics clipg = g.create((int) (x * time), (int) (y * time), (int) (width * time), (int) (height * time));
//            comp.paint(clipg);
//            clipg.dispose();
//        }else {
        BufferedImage img = CoreGraphHelper.createBufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        comp.printAll(g2d);
        g.drawImage(img, (int) (x * time), (int) (y * time), (int) (width * time), (int) (height * time),null);

        g2d.dispose();

//        }

        ComponentUtils.resetBuffer(dbcomponents);
    }

    // 画分页线
    private void paintPaginateLine(Graphics2D g2d) {
        Graphics2D newG2D = (Graphics2D) g2d.create();
        GraphHelper.setStroke(newG2D, GraphHelper.getStroke(Constants.LINE_DASH_DOT));
        newG2D.setPaint(PAGINATE_LINE_COLOR);
        Report report = designer.getTarget();
        ReportSettingsProvider reportSettings = report.getReportSettings();
        PaperSettingProvider paperSetting = reportSettings.getPaperSetting();
        PaperSize paperSize = paperSetting.getPaperSize();
        Margin margin = paperSetting.getMargin();
        double paperWidth = paperSize.getWidth().toPixD(resolution);
        double paperHeight = paperSize.getHeight().toPixD(resolution);
        if (paperSetting.getOrientation() == ReportConstants.LANDSCAPE) {
            paperWidth = paperSize.getHeight().toPixD(resolution);
            paperHeight = paperSize.getWidth().toPixD(resolution);
        }
        paperWidth = paperWidth - margin.getLeft().toPixD(resolution) - margin.getRight().toPixI(resolution);
        paperHeight = paperHeight - margin.getTop().toPixI(resolution) - margin.getBottom().toPixI(resolution);
        int horizontalValue = designer.getHorizontalValue();
        int verticalValue = designer.getVerticalValue();
        int displayWidth = designer.getWidth();
        int displayHeight = designer.getHeight();
        for (double i = paperWidth; i < displayWidth + horizontalValue; i += paperWidth) {
            GraphHelper.drawLine(newG2D, i - horizontalValue, 0, i - horizontalValue, displayHeight, Constants.LINE_DASH_DOT);
        }
        for (double j = paperHeight; j < displayHeight + verticalValue; j += paperHeight) {
            GraphHelper.drawLine(newG2D, 0, j - verticalValue, displayWidth, j - verticalValue, Constants.LINE_DASH_DOT);
        }
        newG2D.dispose();
    }
}
