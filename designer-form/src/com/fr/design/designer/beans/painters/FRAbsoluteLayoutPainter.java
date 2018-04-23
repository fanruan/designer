package com.fr.design.designer.beans.painters;

import com.fr.design.designer.beans.adapters.layout.FRAbsoluteLayoutAdapter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.FormDesigner;
import com.fr.general.FRLogger;

import java.awt.*;

/**
 * Created by zhouping on 2016/7/11.
 */
public class FRAbsoluteLayoutPainter extends AbstractPainter {

    private static final int BORDER_PROPORTION = 10;
    private static final int X = 0;
    private static final int Y = 1;
    private static final int WIDTH = 2;
    private static final int HEIGHT = 3;

    private static final Color DEPEND_LINE_COLOR = new Color(200, 200, 200);
    private static final int DEPEND_LINE_SCOPE = 3;

    /**
     * 构造函数
     * @param container
     */
    public FRAbsoluteLayoutPainter(XLayoutContainer container) {
        super(container);
    }

    /**
     * 组件渲染
     * @param g 画图类
     * @param startX 开始位置x
     * @param startY 开始位置y
     */
    @Override
    public void paint(Graphics g, int startX, int startY) {
        if(hotspot_bounds == null && creator != null && container != null){
            drawDependingLine(g);
            return;
        }
        super.paint(g, startX, startY);

        int x = hotspot.x - hotspot_bounds.x;
        int y = hotspot.y - hotspot_bounds.y;
        FRAbsoluteLayoutAdapter adapter = (FRAbsoluteLayoutAdapter) container.getLayoutAdapter();

        int[] hot_rec;

        boolean accept = adapter.accept(creator, x, y);
        //如果absolute不可编辑那么就将之当普通控件处理，在周围添加控件，
        //否则，只往内部添加，不需要出现蓝色悬浮提示框
        if(container.isEditable() && accept){
            return;
        }
        Component currentComp = container.getComponentAt(x, y);
        //不可编辑的时候要获取顶层的绝对布局容器
        if (XCreatorUtils.getHotspotContainer((XCreator) currentComp) != null) {
            currentComp = XCreatorUtils.getHotspotContainer((XCreator) currentComp).getTopLayout();
            if (currentComp == null) {
                FRLogger.getLogger().info("FRAbsoluteLayoutPainter get currentComp null!");
                return;
            }
        }
        else{
            FRLogger.getLogger().info("FRAbsoluteLayoutPainter getHotspotContainer currentComp null!");
            return;
        }
        Color bColor = XCreatorConstants.FIT_LAYOUT_HOTSPOT_COLOR;
        if (accept) {
            y = (y == container.getHeight()) ? y - 1 : y;
            x = (x == container.getWidth()) ? x - 1 : x;
            hot_rec = adapter.getChildPosition(currentComp, creator, x + hotspot_bounds.x, y + hotspot_bounds.y);
        } else {
            bColor = XCreatorConstants.LAYOUT_FORBIDDEN_COLOR;
            Rectangle rec = currentComp.getBounds();
            hot_rec = currentComp == container ? new int[]{x, y, 0, 0} : new int[]{rec.x, rec.y, rec.width, rec.height};
        }
        drawRegionBackground(g, hot_rec[X], hot_rec[Y] + startY, hot_rec[WIDTH], hot_rec[HEIGHT], bColor, accept);
        if (accept) {
            //画交叉区域和中间点区域
            //拖入的区域也改为整个渲染，点区域的后画下，不然被遮住了
            paintCrossPoint(currentComp, g, x, y);
        }
    }

    private void paintCrossPoint(Component currentComp, Graphics g, int x, int y) {
        if (currentComp == container) {
            return;
        }
        Color bColor = XCreatorConstants.FIT_LAYOUT_POINT_COLOR;
        int cX = currentComp.getX(), cY = currentComp.getY(), cH = currentComp.getHeight(), cW = currentComp.getWidth();
        int defaultWidth = cW / BORDER_PROPORTION, defaultHeight = cH / BORDER_PROPORTION;
        // 交叉点提示区域最大值为10px
        int defaultLength = Math.min(BORDER_PROPORTION, Math.min(defaultWidth, defaultHeight));
        Component topComp = container.getTopComp(cX, cY);
        Component bottomComp = container.getBottomComp(cX, cY, cH);
        Component rightComp = container.getRightComp(cX, cY, cW); //组件的左右组件要区分上侧和下侧
        Component leftComp = container.getLeftComp(cX, cY);
        boolean top = topComp != null && topComp != container, left = leftComp != null && leftComp != container,
                bottom = bottomComp != null && bottomComp != container, right = rightComp != null && rightComp != container;
        if (top || left) {
            drawRegionBackground(g, cX + hotspot_bounds.x, cY + hotspot_bounds.y, defaultLength, defaultLength, bColor, true);
        }
        if (bottom || left) {
            drawRegionBackground(g, cX + hotspot_bounds.x, cY + cH - defaultLength + hotspot_bounds.y, defaultLength, defaultLength, bColor,true);
        }
        if (top || right) {
            drawRegionBackground(g, cX + cW - defaultLength + hotspot_bounds.x, cY + hotspot_bounds.y, defaultLength, defaultLength, bColor,true);
        }
        if (bottom || right) {
            drawRegionBackground(g, cX + cW - defaultLength + hotspot_bounds.x, cY + cH - defaultLength + hotspot_bounds.y, defaultLength, defaultLength, bColor,true);
        }
        if (left && right) {
            if (leftComp.getY() == cY && rightComp.getY() == cY) {
                drawRegionBackground(g, cX + cW / 2 - defaultWidth + hotspot_bounds.x, cY + hotspot_bounds.y, defaultWidth * 2, defaultLength, bColor,true);
            }
            //底边线位置，左右组件都不为null且低端对齐，取左、右靠下侧组件判断
            leftComp = container.getBottomLeftComp(cX, cY, cH);
            rightComp = container.getBottomRightComp(cX, cY, cH, cW);
            if ((leftComp.getY() + leftComp.getHeight() == cY + cH) && (rightComp.getY() + rightComp.getHeight()== cY + cH)) {
                drawRegionBackground(g, cX + cW / 2 - defaultWidth + hotspot_bounds.x, cY + cH - defaultLength + hotspot_bounds.y, defaultWidth * 2, defaultLength, bColor,true);
            }
        }
        if (top && bottom) {
            if (topComp.getX() == cX && bottomComp.getX() == cX) {
                drawRegionBackground(g, cX + hotspot_bounds.x, cY + cH / 2 - defaultHeight + hotspot_bounds.y, defaultLength, defaultHeight * 2, bColor,true);
            }
            // 右边线位置，上下组件不为null且右端对齐，取上、下靠右侧组件判断
            topComp = container.getRightTopComp(cX, cY, cW);
            bottomComp = container.getRightBottomComp(cX, cY, cH, cW);
            if ((topComp.getX() + topComp.getWidth() == cX + cW) && (bottomComp.getX() + bottomComp.getWidth() == cX + cW)) {
                drawRegionBackground(g, cX + cW - defaultLength + hotspot_bounds.x, cY + cH / 2 - defaultHeight + hotspot_bounds.y, defaultLength, defaultHeight * 2, bColor,true);
            }
        }
    }

    // 画依附线
    private void drawDependingLine(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Stroke backup = g2d.getStroke();

        // 当前拖拽组件的坐标
        int oriX = creator.getX();
        int oriY = creator.getY();

        // 拖拽位置的即时坐标
        double x = hotspot.getX();
        double y = hotspot.getY();

        // 容器所有的内部组件的横纵坐标值
        int[] posXs = container.getHors();
        int[] posYs = container.getVeris();

        // 依附线的坐标
        int lineX = 0;
        int lineY = 0;

        // 根据拖拽位置调整依附线的坐标
        lineX = getDependLinePos(lineX, posXs, oriX, x);
        lineY = getDependLinePos(lineY, posYs, oriY, y);


        g2d.setStroke(backup);
        g2d.setColor(DEPEND_LINE_COLOR);
        if(lineX != 0){
            g2d.drawRect(lineX, 0, 0, container.getHeight());
        }
        if(lineY != 0){
            g2d.drawRect(0, lineY, container.getWidth(), 0);
        }

    }

    /**
     * 根据容器内部组件的横纵坐标值画依附线
     *
     * @param lineCoordinate 依附线坐标值
     * @param referCoordinates 容器内部所有组件坐标值
     * @param oriCoordinate 当前拖拽组件坐标
     * @param currentCoordinate 拖拽位置的即时坐标
     * @return 依附线的坐标
     *
     */
    private int getDependLinePos(int lineCoordinate,int referCoordinates[],int oriCoordinate,double currentCoordinate){
        for(int i = 0; i < referCoordinates.length; i++){
            if(referCoordinates[i] == oriCoordinate){
                continue;
            }
            if(currentCoordinate > referCoordinates[i] - DEPEND_LINE_SCOPE && currentCoordinate < referCoordinates[i] + DEPEND_LINE_SCOPE){
                lineCoordinate = referCoordinates[i];
                break;
            }
        }
        return lineCoordinate;
    }
}
