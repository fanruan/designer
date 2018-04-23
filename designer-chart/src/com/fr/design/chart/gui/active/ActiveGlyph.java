package com.fr.design.chart.gui.active;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChartGlyph;
import com.fr.base.chart.Glyph;
import com.fr.design.chart.gui.ActiveGlyphFactory;
import com.fr.design.chart.gui.ChartComponent;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午3:51
 * 选中的Glyph
 */
public abstract class ActiveGlyph {
    protected Glyph parentGlyph;
    protected ChartComponent chartComponent;

    public ActiveGlyph(ChartComponent chartComponent, Glyph parentGlyph) {
        this.chartComponent = chartComponent;
        this.parentGlyph = parentGlyph;
    }

    public abstract Glyph getGlyph();
    
    public void drawAllGlyph(Graphics2D g2d, int resolution){
		Point2D offset4Paint = offset4Paint();
        g2d.translate(offset4Paint.getX(), offset4Paint.getY());
		this.getGlyph().draw(g2d, resolution);
		g2d.translate(-offset4Paint.getX(), -offset4Paint.getY());
	};
    
    /**
     * 属性表中, 通过点击 展开到对应的界面.
     */
    public abstract void goRightPane();

    /**
     * 画的偏移的
     * @return 偏移的
     */
    public Point2D offset4Paint() {
        return new Point2D.Double(
                this.parentGlyph.getShape().getBounds().getX(),
                this.parentGlyph.getShape().getBounds().getY()
        );
    }

    public void paint4ActiveGlyph(Graphics2D g2d, BaseChartGlyph chartGlyph) {
    	if(this.parentGlyph == null) {
    		return;
    	}
    	
        Paint oldPaint = g2d.getPaint();
        Composite oldComposite = g2d.getComposite();
    	g2d.setPaint(Color.WHITE);
    	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

        g2d.fill(chartGlyph.getShape());
        drawAllGlyph(g2d, ScreenResolution.getScreenResolution());

        g2d.setPaint(oldPaint);
        g2d.setComposite(oldComposite);
    }
    
    protected void drawSelectedBounds4Active(Graphics2D g2d) {
    	if (this.getGlyph() != null) {
    		Shape shape = this.getGlyph().getShape();
    		if (shape != null) {
    			g2d.draw(shape);
    		}
    	}
    }


    /**
     *当前的ActiveGlyph是否包含坐标mouseX, mouseY
     * @param mouseX 坐标X
     * @param mouseY 坐标Y
     * @return 包含则返回true
     */
    public boolean contains(int mouseX, int mouseY) {
        if (getGlyph() == null || getGlyph().getShape() == null){
        	return false;
        }

        Point2D offset = this.offset4Paint();

        /*
        * alex:因为Line2D.contains(x, y)必然返回false
        * 所以用intersect一个区域,这个区域大小用4 * 4的,区域大一些,就灵敏一些
        */
        return getGlyph().getShape().intersects(mouseX - offset.getX() - 2, mouseY - offset.getY() - 2, 4, 4);
    }

    /**
     * 在当前选中的ActiveGlyph中,仅仅在其Children中找与mouseX, mouseY匹配的ActiveGlyph
     * @param mouseX 坐标X
     * @param mouseY 坐标Y
     * @return 当前ativeGlyph
     */
    public ActiveGlyph findActionGlyphFromChildren(int mouseX, int mouseY) {
        Glyph currentGlyph = getGlyph();
        // 报错应对.
        if (currentGlyph == null) {
            return null;
        }
        java.util.Iterator selectableChildren = currentGlyph.selectableChildren();

        ActiveGlyph resAG = null;
        while (selectableChildren.hasNext() && resAG == null) {
            ActiveGlyph childActiveGlyph = ActiveGlyphFactory.createActiveGlyph(chartComponent, selectableChildren.next(), currentGlyph);

            // 如果childActiveGlyph不为null,找一下其子辈有没有符合条件
            if (childActiveGlyph != null) {
                resAG = childActiveGlyph.findActionGlyphFromChildren(mouseX, mouseY);
            }

            // 如果childActiveGlyph的子辈没有符合条件的,就看一下这个childGlyph是否符合条件
            if (resAG == null && childActiveGlyph != null && childActiveGlyph.contains(mouseX, mouseY)) {
                resAG = childActiveGlyph;
            }
        }

        // 如果当前ActiveGlyph的所有子辈都没有与mouseX, mouseY相匹配的,看一下它自己是否匹配
        if (resAG == null) {
            if (this.contains(mouseX, mouseY)) {
                resAG = this;
            }
        }

        return resAG;
    }

    /**
     * 拖拽
     * @param e 事件
     */
    public void onMouseDragged(MouseEvent e) {

    }

    /**
     * 移动事件
     * @param e 事件
     */
    public void onMouseMove(MouseEvent e) {

    }
}