/**
 * 
 */
package com.fr.design.designer.beans.painters;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import com.fr.design.designer.beans.adapters.layout.FRFitLayoutAdapter;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.general.ComparatorUtils;

/**
 * @author jim
 * @date 2014-7-14
 */
public class FRFitLayoutPainter extends AbstractPainter{

	private static final int BORDER_PROPORTION = 10; 
	private static final int X = 0;
	private static final int Y = 1;
	private static final int WIDTH = 2;
	private static final int HEIGHT = 3;
	
	private static final Color DEPEND_LINE_COLOR = new Color(200,200,200);
	private static final int DEPEND_LINE_SOCOPE = 3;
	
	/**
	 * 构造函数
	 * @param container
	 */
	public FRFitLayoutPainter(XLayoutContainer container) {
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
        FRFitLayoutAdapter adapter = (FRFitLayoutAdapter) container.getLayoutAdapter();
        Component currentComp = container.getComponentAt(x, y);
        if (currentComp == null ) {
        	return;
        }
        int[] hot_rec = new int[]{hotspot_bounds.x, hotspot_bounds.y, hotspot_bounds.width, hotspot_bounds.height};
        boolean accept = adapter.accept(creator, x, y);
        Color bColor = XCreatorConstants.FIT_LAYOUT_HOTSPOT_COLOR;
        if (accept) {
        	y = y==container.getHeight() ? y-1 : y;
        	x = x==container.getWidth() ? x-1 : x;
        	hot_rec = adapter.getChildPosition(currentComp, creator, x, y);
        } else {
        	bColor = XCreatorConstants.LAYOUT_FORBIDDEN_COLOR;
        	Rectangle rec = currentComp.getBounds();
        	hot_rec = currentComp == container ? new int[]{x, y, 0, 0} : new int[]{rec.x, rec.y, rec.width, rec.height};
        }
        // tab布局的边界提示区域
        if(!ComparatorUtils.equals(container.getBackupParent(),container.getOuterLayout()) && adapter.intersectsEdge(x, y, container)){
        	dealHotspotOfTab(hot_rec,container,x,y,bColor,g,accept);
        	return;
        }
        hot_rec[X] += hotspot_bounds.x;
        hot_rec[Y] += hotspot_bounds.y;
        drawRegionBackground(g, hot_rec[X], hot_rec[Y], hot_rec[WIDTH], hot_rec[HEIGHT], bColor, accept);
        if (accept) {
        	//画交叉区域和中间点区域
        	// 拖入的区域也改为整个渲染，点区域的后画下，不然被遮住了
        	paintCrossPoint(currentComp, g, x, y);
        }
    }
	
	private void dealHotspotOfTab(int[] hot_rec,XLayoutContainer container,int x,int y,Color bColor,Graphics g,boolean accept){
		int containerX = container.getX();
		int containerY = container.getY();
		int containerWidth = container.getWidth();
		int containerHeight = container.getHeight();
		// 当前坐标点
		Rectangle currentXY = new Rectangle(x, y, 1, 1);
		hot_rec = new int[]{0, 0, 0, 0};

		// 上边缘
		Rectangle upEdge = new Rectangle(containerX, containerY, containerWidth, BORDER_PROPORTION);
		if(upEdge.intersects(currentXY)){
			hotspot_bounds.y -= WCardMainBorderLayout.TAB_HEIGHT;
    		hot_rec[WIDTH] = container.getWidth();
    		hot_rec[HEIGHT] = (container.getHeight() + WCardMainBorderLayout.TAB_HEIGHT)/2;
		}
		
		int bottomY = containerY + containerHeight - BORDER_PROPORTION;
		// 下边缘
		Rectangle bottomEdge = new Rectangle(containerX, bottomY, containerWidth, BORDER_PROPORTION);
		if(bottomEdge.intersects(currentXY)){
			hotspot_bounds.y += (container.getHeight() - WCardMainBorderLayout.TAB_HEIGHT)/2;
    		hot_rec[WIDTH] = container.getWidth();	
    		hot_rec[HEIGHT] = (container.getHeight() + WCardMainBorderLayout.TAB_HEIGHT)/2;
		}
		
		//左右边缘的高度 -10*2 是为了不和上下边缘重合
		int verticalHeight = containerHeight - BORDER_PROPORTION * 2;
		int leftY = containerY + BORDER_PROPORTION;
		// 左边缘 
		Rectangle leftEdge = new Rectangle(containerX, leftY, BORDER_PROPORTION, verticalHeight);
		if(leftEdge.intersects(currentXY)){
			hotspot_bounds.y -= WCardMainBorderLayout.TAB_HEIGHT;
    		hot_rec[WIDTH] = container.getWidth()/2;
    		hot_rec[HEIGHT] = (container.getHeight() + WCardMainBorderLayout.TAB_HEIGHT);
		}
		
		int rightY = containerY + BORDER_PROPORTION;
		int rightX = containerX + containerWidth - BORDER_PROPORTION;
		// 右边缘
		Rectangle rightEdge = new Rectangle(rightX, rightY, BORDER_PROPORTION, verticalHeight);
		if(rightEdge.intersects(currentXY)){
			hotspot_bounds.y -= WCardMainBorderLayout.TAB_HEIGHT;
			hotspot_bounds.x += container.getWidth()/2;
    		hot_rec[WIDTH] = container.getWidth()/2;
    		hot_rec[HEIGHT] = (container.getHeight() + WCardMainBorderLayout.TAB_HEIGHT);
		}
        hot_rec[X] += hotspot_bounds.x;
        hot_rec[Y] += hotspot_bounds.y;
        drawRegionBackground(g, hot_rec[X], hot_rec[Y], hot_rec[WIDTH], hot_rec[HEIGHT], bColor,accept);
	}
	
	private void paintCrossPoint(Component currentComp, Graphics g, int x, int y) {
		if (currentComp == container) {
			return;
		}
		Color bColor = XCreatorConstants.FIT_LAYOUT_POINT_COLOR;
		int cX = currentComp.getX(), cY = currentComp.getY(), cH = currentComp.getHeight(), cW = currentComp.getWidth();
    	int defaultWidth = cW/BORDER_PROPORTION, defaultHeight = cH/BORDER_PROPORTION;
    	// 交叉点提示区域最大值为10px
    	int defaultLength = Math.min(BORDER_PROPORTION, Math.min(defaultWidth, defaultHeight));
    	Component topComp = container.getTopComp(cX, cY);
    	Component bottomComp = container.getBottomComp(cX, cY, cH);
		Component rightComp = container.getRightComp(cX, cY, cW); //组件的左右组件要区分上侧和下侧
		Component leftComp = container.getLeftComp(cX, cY);
		boolean top = topComp!=null && topComp!=container, left = leftComp!=null && leftComp!=container,bottom = bottomComp!=null && bottomComp!=container,right = rightComp!=null && rightComp!=container;
		if (top || left) {
			drawRegionBackground(g, cX+hotspot_bounds.x, cY+hotspot_bounds.y, defaultLength, defaultLength, bColor, true);
		}
		if (bottom || left) {
			drawRegionBackground(g, cX+hotspot_bounds.x, cY+cH-defaultLength+hotspot_bounds.y, defaultLength, defaultLength, bColor,true);
		}
		if (top || right) {
			drawRegionBackground(g, cX+cW-defaultLength+hotspot_bounds.x, cY+hotspot_bounds.y, defaultLength, defaultLength, bColor,true);
		}
		if (bottom || right) {
			drawRegionBackground(g, cX+cW-defaultLength+hotspot_bounds.x, cY+cH-defaultLength+hotspot_bounds.y, defaultLength, defaultLength, bColor,true);
		}
		if (left && right) {
			if (leftComp.getY()==cY && rightComp.getY()==cY) {
				drawRegionBackground(g, cX+cW/2-defaultWidth+hotspot_bounds.x, cY+hotspot_bounds.y, defaultWidth*2, defaultLength, bColor,true);
			}
			//底边线位置，左右组件都不为null且低端对齐，取左、右靠下侧组件判断
			leftComp = container.getBottomLeftComp(cX, cY, cH);
			rightComp = container.getBottomRightComp(cX, cY, cH, cW);
			if (leftComp.getY()+leftComp.getHeight()==cY+cH && rightComp.getY()+rightComp.getHeight()==cY+cH) {
				drawRegionBackground(g, cX+cW/2-defaultWidth+hotspot_bounds.x, cY+cH-defaultLength+hotspot_bounds.y, defaultWidth*2, defaultLength, bColor,true);
			}
		}
		if (top && bottom) {
			if (topComp.getX()==cX && bottomComp.getX()==cX) {
				drawRegionBackground(g, cX+hotspot_bounds.x, cY+cH/2-defaultHeight+hotspot_bounds.y, defaultLength, defaultHeight*2, bColor,true);
			}
			// 右边线位置，上下组件不为null且右端对齐，取上、下靠右侧组件判断
			topComp = container.getRightTopComp(cX, cY, cW);
			bottomComp = container.getRightBottomComp(cX, cY, cH, cW);
			if (topComp.getX()+topComp.getWidth()==cX+cW && bottomComp.getX()+bottomComp.getWidth()==cX+cW) {
				drawRegionBackground(g, cX+cW-defaultLength+hotspot_bounds.x, cY+cH/2-defaultHeight+hotspot_bounds.y, defaultLength, defaultHeight*2, bColor,true);
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
        for(int i=0; i<referCoordinates.length; i++){
        	if(referCoordinates[i] == oriCoordinate){
        		continue;
        	}
        	if(currentCoordinate > referCoordinates[i]-DEPEND_LINE_SOCOPE && currentCoordinate < referCoordinates[i] + DEPEND_LINE_SOCOPE){
        		lineCoordinate = referCoordinates[i];
        		break;
        	}
        }
        return lineCoordinate;
	}
}