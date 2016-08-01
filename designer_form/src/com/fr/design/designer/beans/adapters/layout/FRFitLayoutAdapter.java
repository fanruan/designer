/**
 * 
 */
package com.fr.design.designer.beans.adapters.layout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRFitLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.designer.properties.FRFitLayoutConstraints;
import com.fr.design.designer.properties.FRFitLayoutPropertiesGroupModel;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.JForm;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.general.ComparatorUtils;

/**
 * 自适应布局的容器适配器
 * 
 * @author jim
 * @date 2014-6-24
 */
public class FRFitLayoutAdapter extends AbstractLayoutAdapter {
	
	public static final String WIDGETPANEICONPATH="/com/fr/web/images/form/resources/layout_absolute.png";
	
	private static final double TOP_HALF = 0.25;
	private static final double BOTTOM_HALF = 0.75;
	private static final int DEFAULT_AREA_LENGTH = 5; //判断交叉区域范围的默认长度
	private static final int BORDER_PROPORTION = 10; //边界三等分或交叉区域大小取组件1/10和默认大小
	private static final int COMP_TOP = 1;
	private static final int COMP_BOTTOM = 2;
	private static final int COMP_LEFT = 3;
	private static final int COMP_RIGHT = 4;
	private static final int COMP_LEFT_TOP = 5;
	private static final int COMP_LEFT_BOTTOM = 6;
	private static final int COMP_RIGHT_TOP = 7;
	private static final int COMP_RIGHT_BOTTOM = 8;
	private static final int INDEX_ZERO = 0;
	
	private static final int DEPENDING_SCOPE = 3;
	
	private int trisectAreaDirect = 0;
	private int crossPointAreaDirect = 0;
	// 增加删除拉伸控件用的临时list
	private List<Component> rightComps;
	private List<Component> leftComps;
	private List<Component> downComps;
	private List<Component> upComps;
	// 三等分时计算对应侧的组件
	private boolean isFindRelatedComps = false;
	// 渲染时只计算对应的bounds而不调整
	private boolean isCalculateChildPos = false;
	private int[] childPosition = null; //painter用的位置
	private HoverPainter painter;
	private int minWidth = 0; // 最小尺寸，由于屏幕百分比里不同，显示的最小大小也不同
	private int minHeight = 0;
	private int actualVal = 0;  // 存在间隔时，add move drag 判断对齐等都要考虑
	private PaddingMargin margin; // 布局容器边距
	
	/**
	 * 构造函数
	 * @param container XWFitLayout容器
	 */
	public FRFitLayoutAdapter(XLayoutContainer container) {
		super(container);
		painter = new FRFitLayoutPainter(container);
		initMinSize();
	}
	
	private void initMinSize() {
		XWFitLayout layout = (XWFitLayout) container;
		minWidth = layout.getActualMinWidth();
		minHeight = layout.getActualMinHeight();
		actualVal = layout.getAcualInterval();
		margin = layout.toData().getMargin();
	}
	
	@Override
	public HoverPainter getPainter() {
		return painter;
	}
	
	/**
	 * 返回布局自身属性，方便一些特有设置在layout刷新时处理
	 */
	@Override
    public GroupModel getLayoutProperties() {
		XWFitLayout xfl = (XWFitLayout) container;
        return new FRFitLayoutPropertiesGroupModel(xfl);
    }
	
	/**
	 * 添加组件
	 * 
	 * @param child 待添加的组件
	 *@param x 坐标x
	 *@param y 坐标y
	 */
	@Override
	public void addComp(XCreator child, int x, int y) {
		fix(child, x, y);
		if (child.shouldScaleCreator() || child.hasTitleStyle()) {
			addParentCreator(child);
		} else {
			container.add(child, child.toData().getWidgetName());
		}
		XWFitLayout layout = (XWFitLayout) container;
		// 更新对应的BoundsWidget
		layout.updateBoundsWidget();
		updateCreatorBackBound();
    }
	
	 private void updateCreatorBackBound() {
	    	for (int i=0,size=container.getComponentCount(); i<size; i++) {
				XCreator creator = (XCreator) container.getComponent(i);
				creator.updateChildBound(minHeight);
				creator.setBackupBound(creator.getBounds());
			}
	    }
	
	 
	 
	private void addParentCreator(XCreator child) {
		XLayoutContainer parentPanel = child.initCreatorWrapper(minHeight);
		container.add(parentPanel, child.toData().getWidgetName());
	}
    
	/**
	 * 能否对应位置放置当前组件
	 *@param creator 组件
     *@param x 添加的位置x，该位置是相对于container的
     *@param y 添加的位置y，该位置是相对于container的
     *@return 是否可以放置
	 */
    @Override
    public boolean accept(XCreator creator, int x, int y) {
    	// 计算是否能拖入鼠标区域时，会用到fix 的方法
    	isFindRelatedComps = false;
    	//拖入组件判断时，先判断是否为交叉点区域，其次三等分区域，再次平分区域
    	Component comp = container.getComponentAt(x, y);
    	if (checkInterval(comp)) {
    		return false;
    	}
    	//如果当前处于边缘地带, 那么就把他贴到父容器上
    	boolean isMatchEdge = matchEdge(x, y);
    	
		int componentHeight = comp.getHeight();
		int componentWidth = comp.getWidth();
		//上半部分高度
		int upHeight = (int) (componentHeight * TOP_HALF) + comp.getY();
		//下半部分高度
		int downHeight = (int) (componentHeight * BOTTOM_HALF) + comp.getY();

		//布局控件要先判断是不是可编辑
		XLayoutContainer topLayout = XCreatorUtils.getHotspotContainer((XCreator)comp).getTopLayout();
		if(topLayout != null && !isMatchEdge && !topLayout.isEditable()){
			return false;
		}

    	if (isCrossPointArea(comp, x, y)) {
    		return canAcceptWhileCrossPoint(comp, x, y);
    	} 
    	
    	if (isTrisectionArea(comp, x, y)) {
    		return canAcceptWhileTrisection(comp, x, y);
    	}

    	boolean horizonValid = componentWidth >= minWidth * 2 + actualVal;
    	boolean verticalValid = componentHeight >= minHeight * 2 + actualVal;
		return y > upHeight && y < downHeight ? horizonValid : verticalValid;
    }
    
    // 间隔区域
    private boolean checkInterval(Component comp){
    	return container.getComponentCount()>0 && comp == container;
    }
    
    /**
     * 是否在组件边缘
     * @param x 横坐标
     * @param y 纵坐标
     * @return 是否在组件边缘
     */
    public boolean matchEdge(int x, int y){
    	if(intersectsEdge(x, y,container)){
    		//寻找最近的fit, 在边缘地段添加的控件, 将其送给该fit
    		XLayoutContainer parent = container.findNearestFit();
    		container = parent != null ? parent : container;
    		return true;
    	}
    	return false;
    }
    
	/**
	 * 是否在组件边缘
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @param container 参照组件
	 * @return 是否在组件边缘
	 */
    //是否包含于边缘地段, 按顺序上, 下, 左, 右检测
	public boolean intersectsEdge(int x, int y,XLayoutContainer container) {
		int containerX = container.getX();
		int containerY = container.getY();
		int containerWidth = container.getWidth();
		int containerHeight = container.getHeight();
		
		// 当前坐标点
		Rectangle currentXY = new Rectangle(x, y, 1, 1);
		// 上边缘
		Rectangle upEdge = new Rectangle(containerX, containerY, containerWidth, BORDER_PROPORTION);
		if(upEdge.intersects(currentXY)){
			return true;
		}
		
		int bottomY = containerY + containerHeight - BORDER_PROPORTION;
		// 下边缘
		Rectangle bottomEdge = new Rectangle(containerX, bottomY, containerWidth, BORDER_PROPORTION);
		if(bottomEdge.intersects(currentXY)){
			return true;
		}
		
		//左右边缘的高度 -10*2 是为了不和上下边缘重合
		int verticalHeight = containerHeight - BORDER_PROPORTION * 2;
		int leftY = containerY + BORDER_PROPORTION;
		// 左边缘 
		Rectangle leftEdge = new Rectangle(containerX, leftY, BORDER_PROPORTION, verticalHeight);
		if(leftEdge.intersects(currentXY)){
			return true;
		}
		
		int rightY = containerY + BORDER_PROPORTION;
		int rightX = containerX + containerWidth - BORDER_PROPORTION;
		// 右边缘
		Rectangle rightEdge = new Rectangle(rightX, rightY, BORDER_PROPORTION, verticalHeight);
		return  rightEdge.intersects(currentXY);
	}
    
    /**
     * 交叉点区域时，能否对应位置放入组件
     */
    private boolean canAcceptWhileCrossPoint(Component comp, int x, int y) {
    	int cX = comp.getX(), cY = comp.getY(), cH = comp.getHeight(), cW = comp.getWidth();
    	Component topComp = container.getTopComp(cX, cY);
    	Component bottomComp = container.getBottomComp(cX, cY, cH);
		Component rightComp = container.getRightComp(cX, cY, cW);
		Component leftComp = container.getLeftComp(cX, cY);
		int minLength = 0, min = minHeight*2;
		boolean isNotDefaultArea = false;
		if (ComparatorUtils.equals(crossPointAreaDirect, COMP_LEFT_TOP)) {
			isNotDefaultArea = topComp==null || topComp.getX() != cX;
			minLength = isNotDefaultArea ? Math.min(cH, leftComp.getHeight()) : Math.min(cW, topComp.getWidth());
			min = isNotDefaultArea ? min : minWidth*2;
		} else if (ComparatorUtils.equals(crossPointAreaDirect, COMP_RIGHT_BOTTOM)) {
			bottomComp = container.getRightBottomComp(cX, cY, cH, cW);
			isNotDefaultArea = bottomComp==null || (bottomComp.getX()+bottomComp.getWidth() != cX+cW) ;
			rightComp = container.getBottomRightComp(cX, cY, cH, cW);
			minLength = isNotDefaultArea ? Math.min(cH, rightComp.getHeight()) : Math.min(cW, bottomComp.getWidth());
			min = isNotDefaultArea ? min : minWidth*2;
		} else if (ComparatorUtils.equals(crossPointAreaDirect, COMP_LEFT_BOTTOM)) {
			leftComp = container.getBottomLeftComp(cX, cY, cH);
			isNotDefaultArea = leftComp==null || (leftComp.getY()+leftComp.getHeight() != cY+cH);
			minLength = isNotDefaultArea ? Math.min(cW, bottomComp.getWidth()) : Math.min(cH, leftComp.getHeight());
			min = isNotDefaultArea ? minWidth*2 : min ;
		} else if (ComparatorUtils.equals(crossPointAreaDirect, COMP_RIGHT_TOP)) {
			isNotDefaultArea = rightComp==null || (rightComp.getY() != cY) ;
			topComp = container.getRightTopComp(cX, cY, cW);
			minLength = isNotDefaultArea ? Math.min(cW, topComp.getWidth()) : Math.min(cH, rightComp.getWidth());
			min = isNotDefaultArea ? minWidth*2 : min ;
		} else if (ComparatorUtils.equals(crossPointAreaDirect, COMP_TOP)) {
			minLength= Math.min(rightComp.getHeight(), Math.min(cH, leftComp.getHeight()));
		} else if (ComparatorUtils.equals(crossPointAreaDirect, COMP_BOTTOM)) {
			leftComp = container.getBottomLeftComp(cX, cY, cH);
			rightComp = container.getBottomRightComp(cX, cY, cH, cW);
			minLength= Math.min(rightComp.getHeight(), Math.min(cH, leftComp.getHeight()));
		} else {
			if (ComparatorUtils.equals(crossPointAreaDirect, COMP_RIGHT)) {
				topComp = container.getRightTopComp(cX, cY, cW);
				bottomComp = container.getRightBottomComp(cX, cY, cH, cW);
			}
			minLength = Math.min(topComp.getWidth(), Math.min(cW, bottomComp.getWidth()));
			min = minWidth*2;
		}
		// 有间隔的话，要考虑容纳间隔
    	return minLength >= min+actualVal;
    }
    
    private boolean canAcceptWhileTrisection(Component comp, int x, int y) {
    	//符合三等分，实际区域不满足三等分的大小
		int cX = comp.getX(), cY = comp.getY(), cH = comp.getHeight(), cW = comp.getWidth();
		int upMinHeight = 0, downMinHeight = 0, leftMinWidth = 0, rightMinWidth = 0;
    	if (ComparatorUtils.equals(trisectAreaDirect, COMP_TOP)) {
    		upMinHeight = getUpMinHeightComp(cY, x);
    		downMinHeight = getDownMinHeightComp(comp, y);
    		return upMinHeight==0 ? downMinHeight>= minHeight*2+actualVal : (upMinHeight+downMinHeight)>= minHeight*3+actualVal;
    	} else if(ComparatorUtils.equals(trisectAreaDirect, COMP_BOTTOM)) {
    		upMinHeight = getUpMinHeightComp(cY+cH+actualVal, x);
    		if (cY+cH+DEFAULT_AREA_LENGTH>container.getHeight() - margin.getBottom()){
    			downMinHeight = 0;
    		} else {
    			Component targetComp = container.getBottomComp(x, cY, cH);
    			downMinHeight = getDownMinHeightComp(targetComp, cY+cH+DEFAULT_AREA_LENGTH+actualVal);
    		}
    		return  downMinHeight == 0 ? upMinHeight>= minHeight*2+actualVal : (upMinHeight+downMinHeight)>= minHeight*3+actualVal;
    	} else if(ComparatorUtils.equals(trisectAreaDirect, COMP_LEFT)) {
    		rightMinWidth = getMinRightWidth(cX, 0,  y);
    		if(cX-DEFAULT_AREA_LENGTH < margin.getLeft()) {
    			leftMinWidth = 0;
    		} else {
    			Component targetRightComp  = container.getLeftComp(cX, y);
    			leftMinWidth = getMinLeftWidth(targetRightComp, cX-DEFAULT_AREA_LENGTH - actualVal);
    		}
    		return leftMinWidth==0 ? rightMinWidth>=minWidth*2+actualVal : (leftMinWidth+rightMinWidth)>= minWidth*3+actualVal;
    	} else if(ComparatorUtils.equals(trisectAreaDirect, COMP_RIGHT)) {
    		leftMinWidth = getMinLeftWidth(comp, x);
    		rightMinWidth = getMinRightWidth(cX, cW, y);
    		return rightMinWidth==0 ? leftMinWidth>=minWidth*2+actualVal : (leftMinWidth+rightMinWidth)>= minWidth*3+actualVal;
    	}
    	return false;
    }
    
    /**
     * 返回当前组件所在y值上方的所有组件中最小高度，且保证这些控件是相邻不隔断的
     * 判断对齐时考虑间隔
     */
    private int getUpMinHeightComp(int cY, int x) {
    	if (cY == margin.getTop()) {
    		return 0;
    	}
    	int max=container.getWidth() - margin.getRight();
    	int mouseX = x;
    	int minHeight = cY;
    	int bott = 0;
    	if (isFindRelatedComps) {
    		upComps = new ArrayList<Component>();
    	}
    	for(; mouseX<max; ){
    		Component comp = container.getTopComp(mouseX, cY);
    		if(comp == null){
    			break;
    		}
    		bott = comp.getHeight()+comp.getY()+actualVal;
    		if (bott == cY) {
    			if (comp.getHeight() < minHeight) {
    				minHeight = comp.getHeight();
    			}
    			mouseX = comp.getX()+comp.getWidth()+DEFAULT_AREA_LENGTH+actualVal;
    			if (isFindRelatedComps) {
    				upComps.add(comp);
    			}
    		} else{
    			break;
    		}
    	}
    	if(container.getTopComp(x, cY) == null){
    		return 0;
    	}
    	mouseX = container.getTopComp(x, cY).getX()-DEFAULT_AREA_LENGTH - actualVal;
    	while(mouseX > margin.getLeft()) {
    		Component comp = container.getTopComp(mouseX, cY);
    		bott = comp.getHeight()+comp.getY()+actualVal;
    		if (bott == cY) {
    			if (comp.getHeight() < minHeight) {
    				minHeight = comp.getHeight();
    			}
    			mouseX = comp.getX()-DEFAULT_AREA_LENGTH-actualVal;
    			if (isFindRelatedComps) {
    				upComps.add(comp);
    			}
    		} else{
    			break;
    		}
    	}
      return minHeight;  
    }
    
    /**
     * 返回和当前组件相同y坐标的所有组件中最小高度，且保证这些控件是相邻不隔断的
     * 判断对齐时考虑间隔
     */
    private int getDownMinHeightComp(Component currentcomp, int y) {
    	int cX = currentcomp.getX();
    	int cY = currentcomp.getY();
    	int minHeight = currentcomp.getHeight();
    	int max=container.getWidth() - margin.getRight();
    	if (isFindRelatedComps) {
    		downComps = new ArrayList<Component>();
    	}
    	int mouseX = cX + DEFAULT_AREA_LENGTH;
    	while (mouseX < max) {
    		Component comp =  container.getComponentAt(mouseX, y);
    		if (comp.getY()==cY) {
    			if (comp.getHeight() < minHeight) {
    				minHeight = comp.getHeight();
    			}
    			mouseX = comp.getX()+comp.getWidth()+DEFAULT_AREA_LENGTH + actualVal;
    			if (isFindRelatedComps) {
    				downComps.add(comp);
    			}
    		}else{
    			break;
    		}
    	}
    	mouseX = cX - DEFAULT_AREA_LENGTH-actualVal;
    	while(mouseX > margin.getLeft()) {
    		Component comp = container.getComponentAt(mouseX, y);
    		if (comp.getY()==cY) {
    			if (comp.getHeight() < minHeight) {
    				minHeight = comp.getHeight();
    			}
    			mouseX = comp.getX() - DEFAULT_AREA_LENGTH - actualVal;
    			if (isFindRelatedComps) {
    				downComps.add(comp);
    			}
    		}else{
    			break;
    		}
    	}
    	return minHeight;
    }
    
    /**
     * 返回当前组件右侧相同x的所有组件中最小宽度，且保证这些控件是相邻不隔断的
     * 判断对齐时考虑间隔
     */
    private int getMinRightWidth(int cX, int cW, int y) {
    	int xL = cX+DEFAULT_AREA_LENGTH ;
    	xL = cW==0 ? xL : xL+cW+actualVal;
    	if (xL>container.getWidth() - margin.getRight()){
    		return 0;
    	}
    	// 以当前组件紧挨着右侧的组件为基准，在y轴方向查找符合条件的组件
    	Component targetComp = container.getComponentAt(xL, y);
    	int minWidth = targetComp.getWidth();
    	int max=container.getHeight() - margin.getBottom();
    	if (isFindRelatedComps) {
    		rightComps = new ArrayList<Component>();
    	}
    	int mouseY = targetComp.getY() + DEFAULT_AREA_LENGTH;
    	while (mouseY<max) {
    		Component comp = container.getComponentAt(xL, mouseY);
    		if (comp.getX()== targetComp.getX()) {
    			if (comp.getWidth() < minWidth) {
    				minWidth = comp.getWidth();
    			}
    			mouseY = comp.getY()+comp.getHeight()+DEFAULT_AREA_LENGTH + actualVal;
    			if (isFindRelatedComps) {
    				rightComps.add(comp);
    			}
    		}else{
    			break;
    		}
    	}
    	mouseY = targetComp.getY() - DEFAULT_AREA_LENGTH - actualVal;
    	while(mouseY>margin.getTop()) {
    		Component comp = container.getComponentAt(xL, mouseY);
    		if (comp.getX()==targetComp.getX()) {
    			if (comp.getWidth() < minWidth) {
    				minWidth = comp.getWidth();
    			}
    			mouseY = comp.getY() - DEFAULT_AREA_LENGTH - actualVal;
    			if (isFindRelatedComps) {
    				rightComps.add(comp);
    			}
    		}else{
    			break;
    		}
    	}
    	return minWidth;
    }
    
    /**
     * 返回当前组件垂直方向同侧的组件(组件右边界相连)中最小宽度
     * 判断对齐时考虑间隔
     */
    private int getMinLeftWidth(Component currentComp, int x) {
    	int minWidth = currentComp.getWidth();
    	int compRightLength = currentComp.getX()+currentComp.getWidth();
    	int max=container.getHeight() - margin.getBottom();
    	if (isFindRelatedComps) {
    		leftComps = new ArrayList<Component>();
    	}
    	int rightx  = 0;
    	int mouseY = currentComp.getY()+DEFAULT_AREA_LENGTH;
    	while(mouseY<max) {
    		Component comp = container.getComponentAt(x, mouseY);
    		rightx = comp.getX()+comp.getWidth();
    		if (rightx == compRightLength) {
    			if (comp.getWidth() < minWidth) {
    				minWidth = comp.getWidth();
    			}
    			mouseY = comp.getY()+comp.getHeight()+DEFAULT_AREA_LENGTH + actualVal;
    			if (isFindRelatedComps) {
    				leftComps.add(comp);
    			}
    		}else{
    			break;
    		}
    	}
    	mouseY = currentComp.getY() - DEFAULT_AREA_LENGTH - actualVal;
    	while(mouseY>margin.getTop()) {
    		Component comp = container.getComponentAt(x, mouseY);
    		rightx = comp.getX()+comp.getWidth();
    		if (rightx == compRightLength) {
    			if (comp.getWidth() < minWidth) {
    				minWidth = comp.getWidth();
    			}
    			mouseY = comp.getY() - DEFAULT_AREA_LENGTH - actualVal;
    			if (isFindRelatedComps) {
    				leftComps.add(comp);
    			}
    		}else{
    			break;
    		}
    	}
    	return minWidth;
    }
    
    /**
     * 判断是否鼠标在组件的三等分区域，如果组件在布局管理器中间，上下左右都可能会三等分
     * @param parentComp 鼠标所在区域的组件
     * @param x 坐标x
     * @param y 坐标y
     * @return 是则返回true
     */
    public boolean isTrisectionArea(Component parentComp, int x, int y) {
    	XCreator creator = (XCreator)parentComp;
    	if (container.getComponentCount()<=1) {
    		return false;
    	}
    	int maxWidth = parentComp.getWidth();
    	int maxHeight = parentComp.getHeight();
    	int xL = parentComp.getX();
    	int yL = parentComp.getY();
    	// 组件宽高的十分之一和默认值取大
    	int minRangeWidth = Math.max(maxWidth/BORDER_PROPORTION, DEFAULT_AREA_LENGTH);
    	int minRangeHeight = Math.max(maxHeight/BORDER_PROPORTION, DEFAULT_AREA_LENGTH);
    	if(y<yL+minRangeHeight ) {
    		// 在组件上侧三等分
    		trisectAreaDirect = COMP_TOP;
    	} else if(y>yL+maxHeight-minRangeHeight) {
    		// 在组件下侧三等分
    		trisectAreaDirect = COMP_BOTTOM;
    	} else if (x<xL+minRangeWidth) {
    		// 在组件左侧三等分
    		trisectAreaDirect = COMP_LEFT;
    	} else if(x>xL+maxWidth-minRangeWidth) {
    		// 在组件右侧三等分
    		trisectAreaDirect = COMP_RIGHT;
    	}
    	// tab布局的边界特殊处理，不进行三等分
    	if(!creator.getTargetChildrenList().isEmpty()){
    		return false;
    	}
    	
    	return !ComparatorUtils.equals(trisectAreaDirect, 0);
    }
    
    /**
     * 是否为组件交叉点区域 或者是相邻三组建中间点
     * @param currentComp 当前组件
     * @param x 坐标x
     * @param y 坐标y
     * @return 是则返回true
     */
    public boolean isCrossPointArea(Component currentComp, int x, int y) {
    	// 3个及以上都会出现交叉点区域（包括边界处的）
    	if(currentComp == null || container.getComponentCount() <= 2){
    		return false;
    	}
    	int cX = currentComp.getX();
    	int cY = currentComp.getY();
    	int cW = currentComp.getWidth();
    	int cH = currentComp.getHeight();
		int areaWidth = Math.max(cW/BORDER_PROPORTION ,DEFAULT_AREA_LENGTH);
    	int areaHeight = Math.max(cH/BORDER_PROPORTION, DEFAULT_AREA_LENGTH);
    	int rx = cX + cW;
    	int by = cY + cH;
    	int objX = cX + areaWidth;
    	int objY = cY + areaHeight;
    	int containerW = container.getWidth() - margin.getRight();
    	int containerH = container.getHeight() - margin.getBottom();
		if (x<objX && y<objY) {
			//左上角区域
			crossPointAreaDirect = cY > margin.getTop() || cX > margin.getLeft() ? COMP_LEFT_TOP : 0;
		} else if (y<objY && x>rx-areaWidth){
			//右上角
			crossPointAreaDirect = cY>margin.getTop() || rx < containerW ? COMP_RIGHT_TOP : 0;
		} else if (x<objX && y>by-areaHeight) {
			//左下角
			crossPointAreaDirect = cX>margin.getLeft() || by<containerH ? COMP_LEFT_BOTTOM : 0;
		} else if (x>rx-areaWidth && y>by-areaHeight) {
			//右下角
			crossPointAreaDirect = by<containerH || rx < containerW ? COMP_RIGHT_BOTTOM : 0;
		} else {
			isMiddlePosition(currentComp, x, y, areaWidth, areaHeight);
		} 
    	// tab布局的边界特殊处理
		XCreator creator = (XCreator)currentComp;
    	if(!creator.getTargetChildrenList().isEmpty()){
    		return false;
    	}
		return crossPointAreaDirect != 0;
    }
    
    private void isMiddlePosition(Component comp, int x, int y, int areaWidth , int areaHeight) {
    	int cX = comp.getX();
    	int cY = comp.getY();
    	int cW = comp.getWidth();
    	int cH = comp.getHeight();
    	boolean isCrosspoint = false;
    	if (x>cX+cW/2-areaWidth && x<cX+cW/2+areaWidth) {
    		// 上下边框线中间位置
    		Component leftComp = container.getLeftComp(cX, cY);
    		Component rightComp = container.getRightComp(cX, cY, cW);
    		if (y < cY + areaHeight) {
				isCrosspoint = leftComp!=null && rightComp!=null && leftComp.getY()==cY && rightComp.getY() == cY;
				crossPointAreaDirect = isCrosspoint ? COMP_TOP : 0;
			} else if (y > cY + cH - areaHeight) {
				leftComp = container.getBottomLeftComp(cX, cY, cH);
				rightComp = container.getBottomRightComp(cX, cY, cH, cW);
				if (leftComp!=null && rightComp!=null) {
					isCrosspoint = leftComp.getY()+leftComp.getHeight() == cY + cH &&  rightComp.getY()+rightComp.getHeight()== cY + cH;
				}
				crossPointAreaDirect = isCrosspoint ? COMP_BOTTOM : 0;
			}
    	} else if (y>cY+cH/2-areaHeight && y<cY+cH/2+areaHeight) {
    		// 左右边框线中间位置
    		Component topComp = container.getTopComp(cX, cY);
    		Component bottomComp = container.getBottomComp(cX, cY, cH);
			if (x < cX+areaWidth) {
				isCrosspoint = topComp!=null && bottomComp!=null && topComp.getX()==cX && bottomComp.getX() == cX;
				crossPointAreaDirect =  isCrosspoint ? COMP_LEFT : 0;
			} else if (x > cX+cW-areaWidth) {
				topComp = container.getRightTopComp(cX, cY, cW);
				bottomComp = container.getRightBottomComp(cX, cY, cH, cW);
				if (topComp!=null && bottomComp!=null) {
					isCrosspoint = topComp.getX()+topComp.getWidth()==cX+cW &&  bottomComp.getX()+bottomComp.getWidth()== cX+cW;
				}
				crossPointAreaDirect =  isCrosspoint ? COMP_RIGHT : 0;
			}
		}
    }
    
    private void initCompsList() {
    	rightComps = new ArrayList<Component>();
    	leftComps = new ArrayList<Component>();
    	upComps = new ArrayList<Component>();
    	downComps = new ArrayList<Component>();
    }
    
    private void clearCompsList() {
    	rightComps = null;
    	leftComps = null;
    	upComps = null;
    	downComps = null;
    }
    private Rectangle adjustBackupBound(Rectangle backupBound,XWCardMainBorderLayout mainLayout){
    	// 参数界面高度对纵坐标产生的影响
    	JForm jform = (JForm)(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
    	if(jform.getFormDesign().getParaComponent()!= null){
    		backupBound.y -= jform.getFormDesign().getParaHeight();
    	}
    	
		Rectangle rec = mainLayout.getBounds();
		// XWTabLayout里面的横纵坐标收到外层XWCardMainBorderLayout的横纵坐标影响
		// 减掉之后可以按照它原来的逻辑执行
		backupBound.x -= rec.x;
		backupBound.y -= rec.y;
		XWCardLayout cardLayout = mainLayout.getCardPart();
    	LayoutBorderStyle style = cardLayout.toData().getBorderStyle();
    	// 当tab布局为标题样式时，才需要处理标题栏高度产生的影响
    	if(ComparatorUtils.equals(style.getType(), LayoutBorderStyle.TITLE)){
    		backupBound.y -= WCardMainBorderLayout.TAB_HEIGHT;
    	}
    	return backupBound;
    }
    
    /**
     * 拖拽控件边框后，根据控件的大小尺寸，进行相关组件的调整
     * @param creator 组件
     */
    @Override
    public void fix(XCreator creator) {
    	//拖拽组件原大小、位置
    	Rectangle backupBound = creator.getBackupBound();
    	backupBound.x -= container.getX();
    	backupBound.y -= container.getY();
    	//当前拖拽组件的位置
    	int x = creator.getX();
    	int y = creator.getY();
    	
    	// 获取容器所有内部组件横坐标
    	int[] posXs = container.getHors();
    	// 获取容器所有内部组件纵坐标                                                                 
    	int[] posYs = container.getVeris();
    	
    	XLayoutContainer outerLayout = container.getOuterLayout();
    	if(!ComparatorUtils.equals(outerLayout, container.getBackupParent())){
    		XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout)outerLayout;
    		backupBound = adjustBackupBound(backupBound, mainLayout);
    	}
    	
    	//拖拽组件拖拽以后的大小
    	int w = creator.getWidth();
    	int h = creator.getHeight();
    	initCompsList();
    	creator.setBounds(backupBound);
    	int difference = 0;
		if (x!=backupBound.x) {
			dealLeft(backupBound, x, posXs, difference, creator);
		}  else if(w!=backupBound.width) {
			dealRight(backupBound, x, w, posXs, difference, creator);
    	} else if (y!=backupBound.y) {
    		dealTop(backupBound, y, posYs, difference, creator);
    	} else if (h!=backupBound.height) {
    		dealButtom(backupBound, y, h, posYs, difference, creator);
    	}
		clearCompsList();
    	XWFitLayout layout = (XWFitLayout) container;
    	layout.updateBoundsWidget(); // 更新对应的BoundsWidget
    	updateCreatorBackBound();
    }
    
    private void dealLeft(Rectangle backupBound,int x,int[] posXs,int difference,XCreator creator){
		if (backupBound.x == margin.getLeft()) {
			return;
		}
		x = adjustCoordinateByDependingLine(x,posXs);
		difference = x-backupBound.x;
		dealDirectionAtLeft(backupBound, difference, creator);
    }
    
    private void dealRight(Rectangle backupBound,int x,int w,int[] posXs,int difference,XCreator creator){
		if (backupBound.width+backupBound.x== container.getWidth() - margin.getRight()) {
			return;
		}
		w = adjustDiffByDependingLine(x, posXs, w);
		difference = w-backupBound.width; //拖拽长度
		dealDirectionAtRight(backupBound, difference, creator);
    }
    
    private void dealTop(Rectangle backupBound,int y,int[] posYs,int difference,XCreator creator){
		if (backupBound.y== margin.getTop()) {
			return;
		}
		y = adjustCoordinateByDependingLine(y, posYs);
		difference = y-backupBound.y;
		dealDirectionAtTop(backupBound, difference, creator);
    }
    
    private void dealButtom(Rectangle backupBound,int y,int h,int[] posYs,int difference,XCreator creator){
		if (backupBound.y+backupBound.height==container.getHeight() - margin.getBottom()) {
			return;
		}
		h = adjustDiffByDependingLine(y, posYs, h);
		difference = h-backupBound.height;
		dealDirectionABottom(backupBound, difference, creator);
    }
    
    // 根据需要依附的位置调整拖拽的坐标值
    private int adjustCoordinateByDependingLine(int coordinate,int[] coordinates){
		for(int i=0; i<coordinates.length; i++){
			if(coordinate == coordinates[i]){
				continue;
			}
			if(coordinate > coordinates[i]-DEPENDING_SCOPE && coordinate < coordinates[i] + DEPENDING_SCOPE){
				coordinate = coordinates[i];
				break;
			}
		}
		return coordinate;
    }
    
    // 根据需要依附的位置调整拖拽的距离
    private int adjustDiffByDependingLine(int coordinate,int[] coordinates,int diff){
		for(int i=0; i<coordinates.length; i++){
			if(coordinate+diff > coordinates[i]-DEPENDING_SCOPE && coordinate+diff < coordinates[i] + DEPENDING_SCOPE){
				diff = coordinates[i] - coordinate;
				break;
			}
		}
		return diff;
    }
    
    // 左侧边框拉伸，循环找出对齐的两侧控件
    private void dealDirectionAtLeft(Rectangle backupBound, int difference, Component creator) {
    	rightComps.add(creator);
    	Component rightComp = null;
    	int leftx = backupBound.x-DEFAULT_AREA_LENGTH - actualVal;
    	// 取左侧边框右面的组件x值
    	int rightx = backupBound.x+DEFAULT_AREA_LENGTH;
    	Component leftComp = container.getLeftComp(backupBound.x, backupBound.y);
    	leftComps.add(leftComp);
    	//先找上侧对齐时(y相等)的左右两边组件
    	int ry = backupBound.y;
    	int ly = leftComp.getY();	
    	int min = margin.getTop();
    	int max = container.getHeight() - margin.getBottom();
    	while (ry>= min && ly>= min) {
    		if  (ry == ly) {
    			break;
    		} else {
    			if (ry>ly) {
    				rightComp = container.getTopComp(rightx, ry);
    				ry = rightComp.getY();
    				rightComps.add(rightComp);
    			} else {
    				leftComp = container.getTopComp(leftx, ly);
    				ly = leftComp.getY();
    				leftComps.add(leftComp);
    			}
    		}
    	}
    	// 下侧对齐时（y+h相等）两边组件
    	ry = backupBound.y + backupBound.height ;
    	ly = leftComps.get(0).getY() + leftComps.get(0).getHeight();
    	while(ry<= max && ly<= max) {
    		if (ry==ly) {
    			break;
    		} else {
    			if (ry>ly) {
    				leftComp = container.getComponentAt(leftx, ly+DEFAULT_AREA_LENGTH+actualVal);
    				ly = leftComp.getY() + leftComp.getHeight();
    				leftComps.add(leftComp);
    			} else {
    				rightComp = container.getComponentAt(rightx, ry+DEFAULT_AREA_LENGTH+actualVal);
    				ry = rightComp.getY() + rightComp.getHeight();
    				rightComps.add(rightComp);
    			}
    		}
    	}
    	dealHorDirection(backupBound.x, difference);
    }
    
    // 右侧边框拉伸，循环找出对齐的两侧控件
    private void dealDirectionAtRight(Rectangle backupBound, int difference, Component creator) {
    	leftComps.add(creator);
    	Component leftComp = null;
    	int leftx = backupBound.x+backupBound.width-DEFAULT_AREA_LENGTH;
    	// 取右侧边框右面的组件x值
    	int rightx = backupBound.x+backupBound.width+DEFAULT_AREA_LENGTH+actualVal;
    	Component rightComp = container.getRightComp(backupBound.x, backupBound.y, backupBound.width);
    	rightComps.add(rightComp);
    	int ly = backupBound.y, ry = rightComp.getY();
    	int min = margin.getTop();
    	int max = container.getHeight() - margin.getBottom();
    	while (ry>= min && ly>= min) {
    		if  (ry == ly) {
    			break;
    		} else {
    			if (ry>ly) {
    				rightComp = container.getTopComp(rightx, ry);
    				ry = rightComp.getY();
    				rightComps.add(rightComp);
    			} else {
    				leftComp = container.getTopComp(leftx, ly);
    				ly = leftComp.getY();
    				leftComps.add(leftComp);
    			}
    		}
    	}
    	ly = backupBound.y + backupBound.height;
    	ry = rightComps.get(0).getY() + rightComps.get(0).getHeight();
    	while(ry<= max && ly<= max) {
    		if (ry==ly) {
    			break;
    		} else {
    			if (ry>ly) {
    				leftComp = container.getComponentAt(leftx, ly+DEFAULT_AREA_LENGTH+actualVal);
    				ly = leftComp.getY() + leftComp.getHeight();
    				leftComps.add(leftComp);
    			} else {
    				rightComp = container.getComponentAt(rightx, ry+DEFAULT_AREA_LENGTH+actualVal);
    				ry = rightComp.getY() + rightComp.getHeight();
    				rightComps.add(rightComp);
    			}
    		}
    	}
    	dealHorDirection(backupBound.x+backupBound.width+actualVal, difference);
    }
    
    /**
     *  水平方向上拉伸边框的处理 
     */
    private void dealHorDirection(int objx, int difference) {
    	if (difference>0) {
    		difference = Math.min(getMinWidth(rightComps)-minWidth, difference);
    	} else {
    		difference = Math.max(difference, minWidth-getMinWidth(leftComps));
    	}
    	//重新计算左右两侧组件size、point
    	if(CalculateLefttRelatComponent(difference)){
    		CalculateRightRelatComponent(objx+difference, -difference);
    	}
    }
    
    // 上侧边框拉伸，循环找出对齐的两侧控件
    private void dealDirectionAtTop(Rectangle backupBound, int difference, Component creator) {
    	downComps.add(creator);
    	// 取上侧边框上面的组件用的y值
    	int topy = backupBound.y-DEFAULT_AREA_LENGTH - actualVal;
    	// 上侧边框下面的组件y值
    	int bottomy = backupBound.y+DEFAULT_AREA_LENGTH;
    	Component topComp = container.getTopComp(backupBound.x, backupBound.y);
    	upComps.add(topComp);
    	Component bottomComp = null;
    	int min = margin.getLeft();
    	int max = container.getWidth() - margin.getRight();
    	//先找左侧侧对齐时(x相等)的上下两边组件
    	int ux = topComp.getX();
    	int dx = backupBound.x;
    	while(ux>= min && dx>=min) {
    		if (ux == dx) {
    			break;
    		} else {
    			if (ux<dx) {
    				bottomComp = container.getLeftComp(dx, bottomy);
    				dx = bottomComp.getX();
    				downComps.add(bottomComp);
    			} else {
    				topComp = container.getLeftComp(ux, topy);
    				ux = topComp.getX();
    				upComps.add(topComp);
    			}
    		}
    	}
    	// 右侧对齐时（x+w相等）两边组件
    	ux = upComps.get(0).getX()+upComps.get(0).getWidth();
    	dx = backupBound.x + backupBound.width;
    	while (ux<= max && dx<= max) {
    		if (ux == dx) {
    			break;
    		} else {
    			if (ux<dx) {
    				topComp = container.getComponentAt(ux+DEFAULT_AREA_LENGTH+actualVal, topy);
    				ux = topComp.getX()+topComp.getWidth();
    				upComps.add(topComp);
    			} else {
    				bottomComp = container.getComponentAt(dx+DEFAULT_AREA_LENGTH+actualVal, bottomy);
    				dx = bottomComp.getX() +bottomComp.getWidth();
    				downComps.add(bottomComp);
    			}
    		}
    	}
    	
    	dealVertiDirection(backupBound.y , difference);
    }
    
    // 下侧边框拉伸，循环找出对齐的两侧控件
    private void dealDirectionABottom(Rectangle backupBound, int difference, Component creator) {
    	upComps.add(creator);
    	Component topComp = null;
    	Component bottomComp = container.getBottomComp(backupBound.x, backupBound.y, backupBound.height);
    	// 下侧边框下面的组件y坐标
    	int bottomy = backupBound.y+backupBound.height+DEFAULT_AREA_LENGTH + actualVal;
    	// 取下侧边框上面的组件用的y值
    	int topy = backupBound.y+backupBound.height-DEFAULT_AREA_LENGTH;
    	downComps.add(bottomComp);
    	int dx = bottomComp.getX();
    	int ux = backupBound.x;
    	int min = margin.getLeft();
    	int max = container.getWidth() - margin.getRight();
    	while(ux>= min && dx>= min) {
    		if (ux == dx) {
    			break;
    		} else {
    			if (ux<dx) {
    				bottomComp = container.getLeftComp(dx, bottomy);
    				dx = bottomComp.getX();
    				downComps.add(bottomComp);
    			} else {
    				topComp = container.getLeftComp(ux, topy);
    				ux = topComp.getX();
    				upComps.add(topComp);
    			}
    		}
    	}
    	dx = downComps.get(0).getX()+downComps.get(0).getWidth();
    	ux = backupBound.x + backupBound.width;
    	while (ux<= max && dx<= max) {
    		if (ux == dx) {
    			break;
    		} else {
    			if (ux<dx) {
    				topComp = container.getComponentAt(ux+DEFAULT_AREA_LENGTH+actualVal, topy);
    				ux = topComp.getX()+topComp.getWidth();
    				upComps.add(topComp);
    			} else {
    				bottomComp = container.getComponentAt(dx+DEFAULT_AREA_LENGTH + actualVal, bottomy);
    				dx = bottomComp.getX() +bottomComp.getWidth();
    				downComps.add(bottomComp);
    			}
    		}
    	}
    	dealVertiDirection(backupBound.y+backupBound.height + actualVal, difference);
    }
    
    /**
     *  垂直方向上拉伸边框的处理 
     */
    private void dealVertiDirection(int objY, int difference) {
    	if (difference>0) {
    		difference = Math.min(getMinHeight(downComps) - minHeight, difference);
    	} else {
    		difference = Math.max(difference, minHeight - getMinHeight(upComps));
    	}
    	//重新计算上下两侧组件size、point
    	 if(CalculateUpRelatComponent(difference)){
    		CalculateDownRelatComponent(objY+difference, -difference);
    	};
    }
    
    /**
     *  新拖入组件时，计算调整其他关联组件位置大小
     * @param child  新拖入的组件
     * @param x 鼠标所在x坐标
     * @param y 鼠标所在y坐标
     */
    public void fix(XCreator child, int x, int y) {
    	Component parentComp = container.getComponentAt(x, y);
    	if (container.getComponentCount()==0){
    		child.setLocation(0, 0);
        	child.setSize(parentComp.getWidth(), parentComp.getHeight());
    	} else if(isCrossPointArea(parentComp, x, y)){
    		//交叉区域插入组件时，根据具体位置进行上下或者左右或者相邻三个组件的位置大小插入
    		fixCrossPointArea(parentComp, child, x, y);
    		return;
    	} else if (isTrisectionArea(parentComp, x, y)) {
    		// 在边界三等分区域，就不再和组件二等分了
    		fixTrisect(parentComp, child, x, y);
    		return;
    	} else{
    		fixHalve(parentComp, child, x, y);
    	}
    }
    
    /**
     * 平分，正常情况拖入组件时，按照上1/4区域、下1/4区域为上下平分，中左侧1/2区域、中右侧1/2区域为左右平分
     */
    private void fixHalve(Component currentComp, XCreator child, int x, int y) {
    	XCreator creator = (XCreator)currentComp;
    	if(!creator.getTargetChildrenList().isEmpty()){
    		fixHalveOfTab(creator,child,x,y);
    		return;
    	}
    	int maxWidth = currentComp.getWidth();
    	int maxHeight = currentComp.getHeight();
    	int xL = currentComp.getX();
    	int yL = currentComp.getY();
    	Dimension dim = new Dimension();
		boolean isDividUp = y - yL<=maxHeight*TOP_HALF;
		boolean isDividDown = y - yL>=maxHeight*BOTTOM_HALF;
		boolean isDividLeft = x -xL<maxWidth/2;
		int finalX = xL;
		int finalY = yL;
		int finalW = maxWidth;
		int finalH = maxHeight;
		if (isDividUp){
			dim.width = maxWidth;
			dim.height = maxHeight/2 - actualVal/2;
			finalY = yL+dim.height + actualVal;
			finalH = maxHeight - dim.height - actualVal;
		} else if(isDividDown){
			// 若当前区域高度非偶数，那么和isDividUp时计算一致，否则永远都是上半部分小1px
			dim.height = maxHeight/2 - actualVal/2;
			dim.width = maxWidth;
			finalH = maxHeight-dim.height - actualVal;
			yL = yL + finalH + actualVal;
		} else if(isDividLeft){
			dim.width = maxWidth/2 - actualVal/2;
			dim.height = maxHeight;
			finalX = xL+dim.width + actualVal;
			finalW = maxWidth - dim.width - actualVal;
		} else {
			finalW = maxWidth/2 - actualVal/2;
			xL = xL +finalW + actualVal;
			dim.width = maxWidth-finalW - actualVal;
			dim.height = maxHeight;
		}
		if (isCalculateChildPos) {
			childPosition = new int[]{xL, yL, dim.width, dim.height};
		} else {
			currentComp.setLocation(finalX, finalY);
			currentComp.setSize(finalW, finalH);
			child.setLocation(xL, yL);
			child.setSize(dim);
		}
    }
    
    // 边界判断炒得原来的逻辑
    // tab布局的边界拖入新组件，和当前tab布局平分，这时候的actualVal组建间隔是tab里面的组建间隔
    // 不应该在外层自适应布局添加
    private void fixHalveOfTab(XCreator currentCreator, XCreator child, int x, int y){
    	int maxWidth = currentCreator.getWidth();
    	int maxHeight = currentCreator.getHeight();
    	int xL = currentCreator.getX();
    	int yL = currentCreator.getY();
    	Dimension dim = new Dimension();
    	int position = getPositionOfFix(currentCreator, x, y);
		int finalX = xL;
		int finalY = yL;
		int finalW = maxWidth;
		int finalH = maxHeight;
		switch(position){
			case COMP_TOP:
				dim.width = maxWidth;
				dim.height = maxHeight/2;
				finalY = yL+dim.height;
				finalH = maxHeight - dim.height;
				break;
			case COMP_BOTTOM:
				dim.height = maxHeight/2;
				dim.width = maxWidth;
				finalH = maxHeight-dim.height;
				yL = yL + finalH;
				break;
			case COMP_LEFT:
				dim.width = maxWidth/2;
				dim.height = maxHeight;
				finalX = xL+dim.width;
				finalW = maxWidth - dim.width;
				break;
			default:
				finalW = maxWidth/2;
				xL = xL +finalW;
				dim.width = maxWidth-finalW;
				dim.height = maxHeight;
		}
		if (isCalculateChildPos) {
			childPosition = new int[]{xL, yL, dim.width, dim.height};
		} else {
			currentCreator.setLocation(finalX, finalY);
			currentCreator.setSize(finalW, finalH);
			currentCreator.recalculateChildWidth(finalW);
			currentCreator.recalculateChildHeight(finalH);
			child.setLocation(xL, yL);
			child.setSize(dim);
		}
    }
    
    private int getPositionOfFix(XCreator currentCreator,int x,int y){
    	int position = 0;
		XLayoutContainer cardLayout = ((XWCardMainBorderLayout) currentCreator).getCardPart();
		XLayoutContainer container = (XLayoutContainer) cardLayout.getComponent(0);
		Rectangle rect = ComponentUtils.getRelativeBounds(container);
		int tempX = x - rect.x;
		int tempY = y - rect.y + WCardMainBorderLayout.TAB_HEIGHT;
		int containerX = container.getX();
		int containerY = container.getY();
		int containerWidth = container.getWidth();
		int containerHeight = container.getHeight();
		// 当前坐标点
		Rectangle currentXY = new Rectangle(tempX, tempY, 1, 1);
		// 上边缘
		Rectangle upEdge = new Rectangle(containerX, containerY, containerWidth, BORDER_PROPORTION);
		if(upEdge.intersects(currentXY)){
			position = COMP_TOP;
		}
		int bottomY = containerY + containerHeight - BORDER_PROPORTION;
		// 下边缘
		Rectangle bottomEdge = new Rectangle(containerX, bottomY, containerWidth, BORDER_PROPORTION);
		if(bottomEdge.intersects(currentXY)){
			position = COMP_BOTTOM;
		}
		//左右边缘的高度 -10*2 是为了不和上下边缘重合
		int verticalHeight = containerHeight - BORDER_PROPORTION * 2;
		int leftY = containerY + BORDER_PROPORTION;
		// 左边缘 
		Rectangle leftEdge = new Rectangle(containerX, leftY, BORDER_PROPORTION, verticalHeight);
		if(leftEdge.intersects(currentXY)){
			position = COMP_LEFT;
		}
		return position;
    }
    
    /**
     * 组件交叉区域进行插入时，调整受到变动的其他组件,之前是交叉区域插入也按照三等分逻辑，后面测试中发现有bug，改为和bi一样的鼠标所在侧平分
     * 默认左上角、右下角区域是垂直方向插入组件
     * 右上角和左下角是水平方向插入组件，这样避免田字块时重复
     */
    private void fixCrossPointArea(Component currentComp, XCreator child, int x, int y) {
    	//计算前先全部初始化待调整控件所在的list
    	initCompsList();
		switch(crossPointAreaDirect) {
			case COMP_LEFT_TOP :
				dealCrossPointAtLeftTop(currentComp, child);
				break;
			case COMP_RIGHT_BOTTOM :
				dealCrossPointAtRightBottom(currentComp, child);
				break;
			case COMP_LEFT_BOTTOM :
				dealCrossPointAtLeftBottom(currentComp, child);
				break;
			case COMP_RIGHT_TOP : 
				dealCrossPointAtRightTop(currentComp, child);
				break;
			case COMP_TOP : 
				dealCrossPointAtTop(currentComp, child);
				break;
			case COMP_BOTTOM : 
				dealCrossPointAtBottom(currentComp, child);
				break;
			case COMP_LEFT : 
				dealCrossPointAtLeft(currentComp, child);
				break;
			case COMP_RIGHT :
				dealCrossPointAtRight(currentComp, child);
				break;
		}
    	crossPointAreaDirect = 0;
    	clearCompsList();
    }
    
    /**
     * 左上交叉区域插入组件，默认垂直方向插入
     */
    private void dealCrossPointAtLeftTop(Component currentComp, XCreator child) {
    	int minDH = 0, minRW = 0, childw = 0, childh = 0;
    	int cX = currentComp.getX();
    	int cY = currentComp.getY();
    	int cH = currentComp.getHeight();
    	int cW = currentComp.getWidth();
    	Component topComp = container.getTopComp(cX, cY);
		Component leftComp = container.getLeftComp(cX, cY);
		//上方没有组件或者有一个x坐标不相同的组件
    	if (topComp==null || topComp.getX()!=cX) {
    		minDH = cH<leftComp.getHeight() ? cH : leftComp.getHeight();
    		downComps.add(leftComp);
    		downComps.add(currentComp);
			int dLength = minDH/2;
			childw = leftComp.getWidth()+cW+actualVal;
			childh = dLength-actualVal/2;
			if (isCalculateChildPos) {
				childPosition = new int[]{leftComp.getX(), leftComp.getY(), childw, childh};
			} else{
				//先设置child位置，不然leftComp坐标调整后就不对了
				child.setLocation(leftComp.getX(), leftComp.getY());
				child.setSize(childw, childh);
				calculateBottomComps(dLength);
			}
    	} else {
    		rightComps.add(currentComp);
    		rightComps.add(topComp);
    		minRW = cW<topComp.getWidth() ? cW : topComp.getWidth();
			int rightLength = minRW/2;
			childw = rightLength-actualVal/2;
			childh = currentComp.getHeight()+topComp.getHeight() + actualVal;
			if (isCalculateChildPos) {
				childPosition = new int[]{topComp.getX(), topComp.getY(), childw, childh};
			} else {
				child.setLocation(topComp.getX(), topComp.getY());
				child.setSize(childw, childh);
				calculateRightComps(rightLength);
			}
    	}
    }
    
    /**
     * 右下交叉区域插入组件，默认垂直方向插入
     */
    private void dealCrossPointAtRightBottom(Component currentComp, XCreator child) {
    	int minUH = 0;
    	int minLW = 0;
    	int cH = currentComp.getHeight(), cW = currentComp.getWidth(), cX = currentComp.getX(), cY = currentComp.getY();
    	// 鼠标在右下区域时，找右下侧和下右侧的组件
    	Component bottomComp = container.getRightBottomComp(cX, cY, cH, cW);
		Component rightComp = container.getBottomRightComp(cX, cY, cH, cW);
		//右下方没有组件或者有一个右侧不对齐的组件
		if (bottomComp==null || (bottomComp.getX()+bottomComp.getWidth() != cX+cW)) {
			minUH = cH<rightComp.getHeight() ? cH : rightComp.getHeight();
			upComps.add(currentComp);
			upComps.add(rightComp);
			int uLength = minUH/2 ;
    		calculateTopComps(uLength, child, uLength);
		} else {
			leftComps.add(currentComp);
			leftComps.add(bottomComp);
			minLW = cW<bottomComp.getWidth() ? cW : bottomComp.getWidth();
			int leftLength = minLW/2;
    		calculateLeftComps(leftLength, child, leftLength);
		}
    }
    
    /**
     * 左下交叉区域插入组件，默认水平方向插入
     */
    private void dealCrossPointAtLeftBottom(Component currentComp, XCreator child) {
    	int minUH = 0, minRW = 0;
    	int cX = currentComp.getX(), cY = currentComp.getY(), cH = currentComp.getHeight(), cW = currentComp.getWidth();
    	Component bottomComp = container.getBottomComp(cX, cY, cH);
    	Component leftComp = container.getBottomLeftComp(cX, cY, cH);
		//左侧没有或者有一个下厕不对齐的组件
		if (leftComp==null || (leftComp.getY()+leftComp.getHeight() != cY+cH)) {
			rightComps.add(currentComp);
			rightComps.add(bottomComp);
			minRW = cW<bottomComp.getWidth() ? cW : bottomComp.getWidth();
			int rightLength = minRW/2;
			int childw = rightLength - actualVal/2;
			int childh = currentComp.getHeight()+bottomComp.getHeight() + actualVal;
			if (isCalculateChildPos) {
				childPosition = new int[]{currentComp.getX(), currentComp.getY(), childw, childh};
			} else {
				child.setLocation(currentComp.getX(), currentComp.getY());
				child.setSize(childw, childh);
				calculateRightComps(rightLength);
			}
		} else {
			upComps.add(currentComp);
			upComps.add(leftComp);
			minUH = cH<leftComp.getHeight() ? cH : leftComp.getHeight();
			int uLength = minUH/2;
			calculateTopComps(uLength, child, uLength);
		}
    }
    
    /**
     * 右上交叉区域插入组件，默认水平方向插入
     */
    private void dealCrossPointAtRightTop(Component currentComp, XCreator child) {
    	int minDH = 0, minLW = 0;
    	int cX = currentComp.getX(), cY = currentComp.getY(), cH = currentComp.getHeight(), cW = currentComp.getWidth();
    	Component topComp = container.getRightTopComp(cX, cY, cW);
		Component rightComp = container.getRightComp(cX, cY, cW);
		//右方没有组件或者有一个右侧不对齐的组件
		if (rightComp==null || (rightComp.getY()!=cY)) {
			leftComps.add(currentComp);
			leftComps.add(topComp);
			minLW = cW<topComp.getWidth() ? cW : topComp.getWidth();
			int leftLength = minLW/2;
			calculateLeftComps(leftLength, child, leftLength);
		} else {
			minDH = cH<rightComp.getHeight() ? cH : rightComp.getHeight();
			downComps.add(currentComp);
			downComps.add(rightComp);
			int dLength = minDH/2;
			int childw = cW+rightComp.getWidth() + actualVal;
			int childh = dLength - actualVal/2;
			if (isCalculateChildPos) {
				childPosition = new int[]{currentComp.getX(), currentComp.getY(), childw, childh};
			} else {
				child.setLocation(currentComp.getX(), currentComp.getY());
				child.setSize(childw, childh);
				calculateBottomComps(dLength);
			}
		}
    }
    
    private void dealCrossPointAtTop(Component currentComp, XCreator child) {
    	int minDH = 0;
    	int cX = currentComp.getX(), cY = currentComp.getY(), cH = currentComp.getHeight(), cW = currentComp.getWidth();
    	Component leftComp = container.getLeftComp(cX, cY);
		Component rightComp = container.getRightComp(cX, cY, cW);
		minDH = Math.min(rightComp.getHeight(), Math.min(cH, leftComp.getHeight()));
		downComps.add(leftComp);
		downComps.add(currentComp);
		downComps.add(rightComp);
		int dLength = minDH/2;
		int childw = cW+leftComp.getWidth()+rightComp.getWidth() + actualVal*2;
		int childh = dLength - actualVal/2;
		if (isCalculateChildPos){
			childPosition = new int[]{leftComp.getX(), leftComp.getY(), childw, childh};
		} else {
			child.setLocation(leftComp.getX(), leftComp.getY());
			child.setSize(childw, childh);
			calculateBottomComps(dLength);
		}
    }
    
    private void dealCrossPointAtBottom(Component currentComp, XCreator child) {
    	int minUH = 0;
    	int cX = currentComp.getX(), cY = currentComp.getY(), cH = currentComp.getHeight(), cW = currentComp.getWidth();
    	Component leftComp = container.getBottomLeftComp(cX, cY, cH);
		Component rightComp = container.getBottomRightComp(cX, cY, cH, cW);
		minUH = Math.min(rightComp.getHeight(), Math.min(cH, leftComp.getHeight()));
		upComps.add(leftComp);
		upComps.add(currentComp);
		upComps.add(rightComp);
		int uLength = minUH/2;
		calculateTopComps(uLength, child, uLength);
    }
    
    private void dealCrossPointAtRight(Component currentComp, XCreator child) {
    	int minLW = 0;
    	int cX = currentComp.getX(), cY = currentComp.getY(), cH = currentComp.getHeight(), cW = currentComp.getWidth();
    	Component topComp = container.getRightTopComp(cX, cY, cW);
    	Component bottomComp = container.getRightBottomComp(cX, cY, cH, cW);
    	minLW = Math.min(topComp.getWidth(), Math.min(cW, bottomComp.getWidth()));
    	leftComps.add(topComp);
    	leftComps.add(currentComp);
    	leftComps.add(bottomComp);
    	int leftLength = minLW/2;
		calculateLeftComps(leftLength, child, leftLength);
    }
    
    private void dealCrossPointAtLeft(Component currentComp, XCreator child) {
    	int minRW = 0;
    	int cX = currentComp.getX(), cY = currentComp.getY(), cH = currentComp.getHeight(), cW = currentComp.getWidth();
    	Component topComp = container.getTopComp(cX, cY);
    	Component bottomComp = container.getBottomComp(cX, cY, cH);
    	minRW = Math.min(topComp.getWidth(), Math.min(cW, bottomComp.getWidth()));
    	rightComps.add(topComp);
    	rightComps.add(currentComp);
    	rightComps.add(bottomComp);
    	int rightLength = minRW/2;
    	int childw = rightLength - actualVal/2;
    	int childh = topComp.getHeight()+currentComp.getHeight()+bottomComp.getHeight() + actualVal*2;
    	if (isCalculateChildPos){
    		childPosition = new int[]{topComp.getX(), topComp.getY(), childw, childh};
    	} else {
    		child.setLocation(topComp.getX(), topComp.getY());
    		child.setSize(childw, childh);
    		calculateRightComps(rightLength);
    	}
    }
    
    /**
     * 三等分区域时，调整相关联的组件
     * @param currentComp  鼠标所在组件
     * @param child  待插入组件
     */
    private void fixTrisect(Component currentComp, XCreator child, int x, int y) {
    	int minUpH = 0, minDownH = 0, minLeftW = 0, minRightW = 0;
    	int cX = currentComp.getX(), cY = currentComp.getY(), cH = currentComp.getHeight(), cW = currentComp.getWidth();
    	isFindRelatedComps = true;
    	if (ComparatorUtils.equals(trisectAreaDirect, COMP_TOP)) {
    		minUpH = getUpMinHeightComp(cY, x); 
    		minDownH = getDownMinHeightComp(currentComp, y);
    		dealTrisectAtTop(child, minUpH, minDownH);
    	} else if(ComparatorUtils.equals(trisectAreaDirect, COMP_BOTTOM)) {
    		minUpH = getUpMinHeightComp(cY+cH+actualVal, x);
    		if (cY+cH+DEFAULT_AREA_LENGTH<container.getHeight() - margin.getBottom()){
    			Component targetTopComp = container.getBottomComp(x, cY, cH);
    			minDownH = getDownMinHeightComp(targetTopComp, cY+cH+DEFAULT_AREA_LENGTH+actualVal);
    		}
    		dealTrisectAtTop(child, minUpH, minDownH);
    	} else if(ComparatorUtils.equals(trisectAreaDirect, COMP_RIGHT)) {
    		minRightW = getMinRightWidth(cX, cW, y);
    		minLeftW = getMinLeftWidth(currentComp, x);
    		dealTrisectAtRight(child, minLeftW, minRightW);
    	} else if(ComparatorUtils.equals(trisectAreaDirect, COMP_LEFT)) {
    		// 当前组件就在右侧时，cW为0
    		minRightW = getMinRightWidth(cX, 0,  y);
    		if(cX-DEFAULT_AREA_LENGTH > margin.getLeft()) {
    			Component targetRightComp  = container.getLeftComp(cX, y);
    			minLeftW = getMinLeftWidth(targetRightComp, cX-DEFAULT_AREA_LENGTH);
    		}
    		dealTrisectAtRight(child, minLeftW, minRightW);
    	}
    	crossPointAreaDirect = 0;
    	clearCompsList();
    }
    
    /**
	 * 当前组件上边界区域三等分
	 */
    private void dealTrisectAtTop(XCreator child, int minUH, int minDH) {
    	// 三等分有间隔时，实际是两侧都要减去半个间隔大小
    	int averageH = (minUH+minDH - actualVal)/3;
    	int dLength = 0;
    	int uLength = 0;
    	if (minDH==0) {
    		dLength = 0;
    		uLength = minUH/2;
    		calculateTopComps(uLength, child, uLength);
    		return;
    	} else if(minUH==0){
    		dLength = minDH/2;
    		int witdh = container.getWidth() - margin.getLeft() - margin.getRight();
    		if (!isCalculateChildPos) {
    			calculateBottomComps(dLength);
    			child.setLocation(margin.getLeft(), margin.getRight());
    			child.setSize(witdh, dLength - actualVal/2);
    		} else {
    			childPosition = new int[] {margin.getLeft(), margin.getRight(), witdh, dLength - actualVal/2};
    		}
    		return;
    	} else if (minUH >= minDH) {
    		minDH -= actualVal/2;
    		if ((minDH*2/3)<minHeight) {
    			dLength = minDH-minHeight;
    		}else {
    			dLength = minDH/3;
    		}
    		uLength = averageH-dLength;
    	} else{
    		minUH -= actualVal/2;
    		if ((minUH*2/3)<minHeight) {
    			uLength = minUH - minHeight;
    		}else {
    			uLength = minUH/3;
    		}
    		dLength = averageH-uLength;
    	}
    	if (!isCalculateChildPos) {
    		calculateBottomComps(dLength);
    	}
    	// 计算两侧时，都额外去掉半个间隔，3等分和平分交叉点不同，只能特殊处理下
    	averageH += actualVal/2;
    	calculateTopComps(uLength, child, averageH);
    }
    
    /**
	 * 当前组件右边界区域三等分
	 */
    private void dealTrisectAtRight(XCreator child, int minLW, int minRW) {
    	int averageW = (minLW+minRW - actualVal)/3;
    	int leftLength = 0;
    	int rightLength = 0;
    	if (minLW==0) {
    		rightLength = minRW/2;
    		int height = container.getHeight() - margin.getBottom() - margin.getTop();
    		if (!isCalculateChildPos) {
    			calculateRightComps(rightLength);
    			child.setLocation(margin.getLeft(), margin.getRight());
    			child.setSize(rightLength - actualVal/2, height);
    		} else {
    			childPosition = new int[] {margin.getLeft(), margin.getRight(), rightLength - actualVal/2, height};
    		}
    		return;
    	} else if(minRW==0){
    		leftLength = minLW/2;
    		calculateLeftComps(leftLength, child, leftLength);
    		return;
    	} else if (minRW>=minLW) {
    		minLW -= actualVal/2;
    		if(minLW*2/3<minWidth) {
    			leftLength = minLW - minWidth;
    		} else {
    			leftLength = minLW/3;
    		}
    		rightLength = averageW - leftLength;
    	} else {
    		minRW -= actualVal/2;
    		if(minRW*2/3<minWidth) {
    			rightLength = minRW - minWidth;
    		} else {
    			rightLength = minRW/3;
    		}
    		leftLength = averageW - rightLength;
    	}
    	if (!isCalculateChildPos) {
    		calculateRightComps(rightLength);
    	}
    	// averageW 是已经去除间隔后的大小，所以再加上半个间隔，否则处理时会变小
    	averageW += actualVal/2;
    	calculateLeftComps(leftLength, child, averageW);
    }
    
    private void calculateBottomComps(int length) {
    	length += actualVal/2;
    	for (int i=0,num=downComps.size(); i<num; i++){
    		Component comp = downComps.get(i);
    		comp.setLocation(comp.getX(), comp.getY()+length);
    		int offset = comp.getHeight()-length;
    		comp.setSize(comp.getWidth(), offset);
    		XCreator creator = (XCreator)comp;
    		creator.recalculateChildHeight(offset);
    	}
    }
    
    private void calculateTopComps(int length, XCreator child, int averageH) {
    	length += actualVal/2;
    	int childWidth = (upComps.size()-1)*actualVal;
    	int childX = container.getWidth() - margin.getLeft() - margin.getRight();
    	int childY = 0;
    	if(upComps.size() > INDEX_ZERO){
    		childY = upComps.get(INDEX_ZERO).getY()+upComps.get(INDEX_ZERO).getHeight()-length;
    	}
    	for (int i=0,num=upComps.size(); i<num; i++){
    		Component comp = upComps.get(i);
    		childWidth += comp.getWidth();
    		if (comp.getX()<childX){
    			childX = comp.getX();
    		}
    		if (!isCalculateChildPos) {
    			int offset = comp.getHeight()-length;
    			comp.setSize(comp.getWidth(), offset);
    			XCreator creator = (XCreator)comp;
    			creator.recalculateChildHeight(offset);
    		}
    	}
    	childY += actualVal; 
    	averageH -= actualVal/2;
    	if (isCalculateChildPos) {
    		childPosition = new int[]{childX, childY, childWidth, averageH};
    	} else {
    		child.setLocation(childX, childY);
    		child.setSize(childWidth, averageH);
    	}
    }
    
    private void calculateLeftComps(int length, XCreator child, int averageW) {
    	length += actualVal/2;
    	if (leftComps.isEmpty()) {
    		return;
    	}
    	int childH = (leftComps.size()-1)*actualVal;
    	int childX = 0;
    	if(leftComps.size() > INDEX_ZERO){
    		childX = leftComps.get(INDEX_ZERO).getX()+leftComps.get(INDEX_ZERO).getWidth()-length;
    	}
    	int childY = container.getHeight() - margin.getBottom();
    	for (int i=0,num=leftComps.size(); i<num; i++){
    		Component comp = leftComps.get(i);
    		childH += comp.getHeight();
    		if (comp.getY()<childY){
    			childY = comp.getY();
    		}
    		if (!isCalculateChildPos) {
    			int offset = comp.getWidth()-length;
    			comp.setSize(offset, comp.getHeight());
    			XCreator creator = (XCreator)comp;
    			creator.recalculateChildWidth(offset);
    		}
    	}
    	childX += actualVal; 
    	averageW -= actualVal/2;
    	if (isCalculateChildPos) {
    		childPosition = new int[]{childX, childY, averageW, childH};
    	} else {
    		child.setLocation(childX, childY);
    		child.setSize(averageW, childH);
    	}
    }
    
    private void calculateRightComps(int length) {
    	length += actualVal/2;
    	for (int i=0,num=rightComps.size(); i<num; i++){
    		Component comp = rightComps.get(i);
    		comp.setLocation(comp.getX()+length, comp.getY());
    		int offset = comp.getWidth()-length;
    		comp.setSize(offset, comp.getHeight());
    		XCreator creator = (XCreator)comp;
    		creator.recalculateChildWidth(offset);
    	}
    }
    
    /**
	 * 删除组件或者重新拖动时，其它组件重新计算位置大小
	 */
	protected void delete(XCreator creator, int creatorWidth, int creatorHeight) {
		int x = creator.getX();
		int y = creator.getY();
		recalculateChildrenSize(x, y, creatorWidth, creatorHeight);
		updateCreatorBackBound();
    }
    
    /**
     * 重新计算内部组件大小
     * @param x 坐标x
     * @param y 坐标y 
     * @param creatorWidth 删除的组件之前所在布局的宽度
     * @param creatorHeight 删除的组件之前所在布局的高度
     */
    public void recalculateChildrenSize(int x, int y, int creatorWidth, int creatorHeight) {
    	if (container.getComponentCount() == 0){
    		return;
    	} else{
    		initCompsList();
        	int width = creatorWidth;
        	int height = creatorHeight;
        	calculateRelatedComponent(x, y, width, height);
        	if (!rightComps.isEmpty() && getAllHeight(rightComps)==height){
        		CalculateRightRelatComponent(x, width+actualVal);
        	} else if (!leftComps.isEmpty() && getAllHeight(leftComps)==height) {
        		CalculateLefttRelatComponent(width+actualVal);
        	} else if (!downComps.isEmpty() && getAllWidth(downComps)==width) {
        		CalculateDownRelatComponent(y, height+actualVal);
        	} else if(!upComps.isEmpty() && getAllWidth(upComps)==width) {
        		CalculateUpRelatComponent(height+actualVal);
        	} else {
        		// 由于布局三等分的存在，可能会出现删除组件时，找不到关联的组件填充，此时特殊处理
        		calculateNoRelatedComponent(x, y, width, height);
        	}
    	}
    	clearCompsList();
    }
    
    /**
     * 四侧边框都没有对齐的，此时每侧有且只有一个方向不对齐
     * 比如右侧不对齐，那么必然右上或右下没对齐，不会同时不对齐，否则不会出现此情况
     * 正常删除是右左下上优先原则，这边目前只调整右侧以至填充完整，
     * 右侧不对齐时的组件先上下微调，再向左侧填充。
     */
    private void calculateNoRelatedComponent(int x, int y, int width, int height) {
    	// 只有最后一个组件了，直接删除
    	if (container.getComponentCount() <= 1) {
    		return;
    	}
    	// 删除当前组件时，由于没有刚好边框对齐的其他组件，这时候需要调整的组件
    	Component rightComp = container.getRightComp(x, y, width);
    	if(rightComp == null){
    		return;
    	}
    	
    	int ry = rightComp.getY();
    	clearCompsList();
    	initCompsList();
    	Rectangle rec = new Rectangle(x, y, width, height);
    	if (ry != y ) {
    		calculateNoRelatedWhileRightTop(rec, rightComp);
    	} else {
    		calculateNoRelatedWhileRightBott(rec, rightComp);
    	}
    	
    }
    
    private void calculateNoRelatedWhileRightTop(Rectangle bound, Component rcomp) {
    	if(rcomp == null){
    		return;
    	}
    	
    	int ry = rcomp.getY();
    	int rh = rcomp.getHeight();
    	int rw = rcomp.getWidth();
    	int dh = bound.y - ry - actualVal;
    	// 没法缩小高度
    	if (dh < minHeight) {
    		// 没法缩小时则拉伸rcomp的上边框
			dealDirectionAtTop(rcomp.getBounds(), dh + actualVal, rcomp);
			//调整的时候可能有组件达到最小高度，判断下
			if (rcomp.getY() != bound.y) {
				clearCompsList();
		    	initCompsList();
				dealDirectionAtTop(rcomp.getBounds(), bound.y - rcomp.getY()-minHeight - actualVal, rcomp);
				ry =  rcomp.getY();
				int rx = rcomp.getX();
				rcomp.setBounds(rx, ry, rw, minHeight);
				recalculateChildrenSize(rx, bound.y, rw, rh-dh - actualVal);
				recalculateChildrenSize(bound.x, bound.y, bound.width, bound.height);
				return;
			}
    	} else {
    		// 右侧控件底部对齐
    		if (rh+ry == bound.y+bound.height) {
				rcomp.setSize(rw, dh);
				bound.width += rw;
				bound.width += actualVal;
    		} else {
				recalculateChildrenSize(bound.x, ry+rh + actualVal, bound.width, bound.height+bound.y-rh-ry - actualVal);
				recalculateChildrenSize(bound.x, bound.y, bound.width, ry+rh-bound.y);
				return;
    		}
    	}
		recalculateChildrenSize(bound.x, bound.y, bound.width, bound.height);
    }
    
    private void calculateNoRelatedWhileRightBott(Rectangle bound, Component rcomp) {
    	rcomp = container.getBottomRightComp(bound.x, bound.y, bound.height, bound.width);
    	int ry = rcomp.getY();
    	int rh = rcomp.getHeight();
    	int rw = rcomp.getWidth();
    	int dh = ry  + rh - bound.y - bound.height - actualVal;
    	if (dh < minHeight) {
    		dealDirectionABottom(rcomp.getBounds(), -dh - actualVal, rcomp);
    		//调整的时候可能有组件达到最小高度，判断下
    		if (rcomp.getHeight()+ry != bound.y + bound.height) {
    			clearCompsList();
    	    	initCompsList();
    			dh = ry  + rcomp.getHeight() - bound.y - bound.height - actualVal;
    			dealDirectionABottom(rcomp.getBounds(), minHeight-dh, rcomp);
    			rh = rcomp.getHeight();
    			int rx = rcomp.getX();
				rcomp.setBounds(rx, bound.y+bound.height + actualVal, rw, minHeight);
				recalculateChildrenSize(rx, ry, rw, rh-minHeight - actualVal);
				recalculateChildrenSize(bound.x, bound.y, bound.width, bound.height);
				return;
			}
    	} else {
    		if (ry == bound.y) {
    			rcomp.setBounds(rcomp.getX(), bound.y+bound.height + actualVal, rw, dh);
				bound.width += rw;
				bound.width += actualVal;
    		} else {
    			recalculateChildrenSize(bound.x, bound.y, bound.width, ry-bound.y - actualVal);
				recalculateChildrenSize(bound.x, ry, bound.width, bound.height-ry+bound.y);
				return;
    		}
    	}
    	recalculateChildrenSize(bound.x, bound.y, bound.width, bound.height);
    }
    
    private int getMinWidth(List<Component> comps) {
    	if (comps.isEmpty()) {
    		return 0;
    	}
    	int minWidth =container.getWidth() - margin.getLeft() - margin.getRight();
    	for (int i=0, size=comps.size(); i<size; i++) {
    		minWidth = minWidth>comps.get(i).getWidth() ? comps.get(i).getWidth() : minWidth;
    	}
    	return minWidth;
    }
    
    private int getMinHeight(List<Component> comps) {
    	if (comps.isEmpty()) {
    		return 0;
    	}
    	int minH =container.getHeight() - margin.getTop() - margin.getBottom();
    	for (int i=0, size=comps.size(); i<size; i++) {
    		minH = minH>comps.get(i).getHeight() ? comps.get(i).getHeight() : minH;
    	}
    	return minH;
    }
    
    // 删除时计算待删除组件上下侧的组件是否何其对齐
    private int getAllHeight(List<Component> comps) {
    	int allHeight = 0;
    	if (comps.isEmpty()) {
    		return allHeight;
    	}
    	int n=comps.size();
    	for (int i=0; i<n; i++) {
    		allHeight += comps.get(i).getHeight();
    	}
    	allHeight += (n-1)*actualVal;
    	return allHeight;
    }
    
    private int getAllWidth(List<Component> comps) {
    	int allWidth = 0;
    	if (comps.isEmpty()) {
    		return allWidth;
    	}
    	int n=comps.size();
    	for (int i=0;  i<n; i++) {
    		allWidth += comps.get(i).getWidth();
    	}
    	allWidth += (n-1)*actualVal;
    	return allWidth;
    }
    
    /**
     * 获取有哪些相关联的组件
     */
    protected void calculateRelatedComponent(int objX, int objY, int objWidth, int objHeight) {
    	int count = container.getComponentCount();
    	for(int i=0; i<count; i++){
    		Component relatComp = container.getComponent(i);
    		int rx = relatComp.getX();
    		int ry = relatComp.getY();
    		int rwidth = relatComp.getWidth();
    		int rheight = relatComp.getHeight();
    		int verti = ry - objY;
    		int hori = rx - objX;
    		boolean isHori = verti>=0 && objHeight>=(rheight+verti);
    		boolean isVerti = hori>=0 && objWidth>=(rwidth+hori);
    		if (isHori && (objX+objWidth+actualVal)==rx) {
    			rightComps.add(relatComp);
    		} else if(isHori && objX==(rx+rwidth+actualVal)) {
    			leftComps.add(relatComp);
    		} else if(isVerti && (objY+objHeight+actualVal)==ry) {
    			downComps.add(relatComp);
    		} else if(isVerti && objY==(ry+rheight+actualVal)) {
    			upComps.add(relatComp);
    		}
    	}
    }
    
    /**
     * 拖拽组件时遍历某一侧组件得到该侧组件能够缩放的最小宽度，tab布局最小宽度  = 内部组件数 * 单个组件最小宽度
     * @param list 某一侧组件的集合 如：leftComps<Component>
     * @return int 最小宽度
     * 
     */
    private int getCompsMinWidth(List<?> list){
    	return getMaxCompsNum(list, true) * WLayout.MIN_WIDTH;
    }
    
    /**
     * 拖拽组件遍历某一侧得到该侧组件能够缩放的最小高度，tab布局最小高度 = 内部组件数  * 单个组件最小高度  + 标题高度
     * @param list 某一侧组件集合
     * @return int 最小高度
     * 
     */
    private int getCompsMinHeight(List<?> list){
    	for(int i=0;i<list.size();i++){
    		XCreator creator = (XCreator)list.get(i);
    		ArrayList<?> childrenList = creator.getTargetChildrenList();
    		if(!childrenList.isEmpty()){
    			return getMaxCompsNum(list,false) * WLayout.MIN_HEIGHT + WCardMainBorderLayout.TAB_HEIGHT;
    		}
    	}
    	return WLayout.MIN_HEIGHT;
    }
    
    /**
     * 根据子组件的横(纵)坐标获取某一侧组件的最大内部组件数
     * @param list 某一侧组件集合
     * @param isHor 是否以横坐标为准
     * @return int 最大内部组件数
     * 
     */
    private int getMaxCompsNum(List<?> list,boolean isHor){
    	int maxCompNums = 1;
    	for(int i=0,size=list.size();i<size;i++){
    		XCreator creator = (XCreator)list.get(i);
    		ArrayList<?> childrenList = creator.getTargetChildrenList();
    		int count = childrenList.size();
    		if(count > 0){
    			for(int j=0;j<count;j++){
    				XWTabFitLayout tabLayout = (XWTabFitLayout)childrenList.get(j);
    				int[] positions = isHor ? tabLayout.getHors(true) : tabLayout.getVeris(true);
    				int compNums = positions.length - 1;
    				maxCompNums = Math.max(compNums, maxCompNums);
    			}
    		}
    	}
    	return maxCompNums;
    }
    
    /**
     * 根据偏移量缩放内部组件大小,（tab布局用到）
     * @param creator tab布局
     * @param offset 偏移量
     * @param isHor 是否为横向拖拽
     */
    private void adjustCompsSize(XCreator creator,int offset,boolean isHor){
		ArrayList<?> childrenList = creator.getTargetChildrenList();
		int size = childrenList.size();
		if(size > 0){
			for(int j=0;j<size;j++){
				XWTabFitLayout tabLayout = (XWTabFitLayout)childrenList.get(j);
				tabLayout.setBackupBound(tabLayout.getBounds());
				int refSize = isHor ? tabLayout.getWidth() : tabLayout.getHeight();
				double percent = (double)offset/refSize;
				if(percent < 0 && !tabLayout.canReduce(percent)){
					return;
				}
				setAdjustedSize(tabLayout,offset,isHor);
				for(int m=0;m<tabLayout.getComponentCount();m++){
					XCreator childCreator = tabLayout.getXCreator(m);
					BoundsWidget wgt = tabLayout.toData().getBoundsWidget(childCreator.toData());
					wgt.setBounds(tabLayout.getComponent(m).getBounds());
				}
				adjustCreatorsSize(percent,tabLayout,isHor);
			}
		
		}
    }
    
    // 纵向拖拽，先将tab布局的高度设置为拖拽后的实际高度
    private void setAdjustedHeight(XWTabFitLayout tabLayout,int offset){
    	tabLayout.setSize(tabLayout.getWidth(),tabLayout.getHeight() + offset);
    }
    
    // 横向拖拽，先将tab布局的宽度设置为拖拽后的实际宽度
    private void setAdjustedSize(XWTabFitLayout tabLayout,int offset,boolean isHor){
    	if(offset < 0){
    		// 缩放时需要备份原tab布局宽高
    		tabLayout.setReferDim(new Dimension(tabLayout.getWidth(),tabLayout.getHeight()));
    	}
    	if(isHor){
    		tabLayout.setSize(tabLayout.getWidth() + offset, tabLayout.getHeight());
    		return;
    	}
    	setAdjustedHeight(tabLayout,offset);
    }
    
    // 按照拖拽偏移量，对tab布局进行缩放
    private void adjustCreatorsSize(double percent,XWTabFitLayout tabLayout,boolean isHor){
    	if(isHor){
    		tabLayout.adjustCreatorsWidth(percent);
    		return;
    	}
    	tabLayout.adjustCreatorsHeight(percent);
    }
    
    
    /**
     * 删除或拉伸控件右边框 调整右侧组件位置大小
     * @param objX 调整后的坐标x
     * @param objWidth 调整后的宽度
     */
    protected void CalculateRightRelatComponent(int objX, int objWidth){
    	int count = rightComps.size();
    	for(int i=0; i<count; i++){
    		XCreator creator = (XCreator)rightComps.get(i);
    		adjustCompsSize(creator,objWidth,true);
    		int ry = creator.getY();
    		int rwidth = creator.getWidth();
    		int rheight = creator.getHeight();
    		creator.setLocation(objX, ry);
			creator.setSize(rwidth+objWidth, rheight);
    	}
    }
    
    /**
     * 实际拖拽偏移量是否超出了可调整的宽度范围
     * @param offset 实际偏移量
     * @return boolean 是否超出调整范围
     * 
     */
    private boolean isBeyondAdjustWidthScope(int offset){
    	boolean isBeyondScope = false;
    	isBeyondScope = offset < 0 ? isBeyondWidthScope(offset, leftComps) : isBeyondWidthScope(offset, rightComps);
    	return isBeyondScope;
    }
    
    // 实际拖拽偏移量是否超出某一侧的可调整宽度
    private boolean isBeyondWidthScope(int offset,List<?> compsList){
    	int compMinWidth = getCompsMinWidth(compsList);
		for(int i=0;i<compsList.size();i++){
			XCreator creator = (XCreator) compsList.get(i);
			if(Math.abs(offset) > (creator.getWidth() - compMinWidth)){
				return true;
			}
		}
		return false;
    }
    
    /**
     * 删除或拉伸控件左边框时 调整左侧的组件位置大小；
     */
    protected boolean CalculateLefttRelatComponent(int objWidth){
    	if(isBeyondAdjustWidthScope(objWidth)){
    		return false;
    	}
    	int count = leftComps.size();
    	for(int i=0; i<count; i++){
    		XCreator creator = (XCreator)leftComps.get(i);
    		adjustCompsSize(creator,objWidth,true);
    		int rwidth = creator.getWidth();
    		int rheight = creator.getHeight();
    		creator.setSize(rwidth+objWidth, rheight);
    	}
    	return true;
    }
    
    /**
     * 删除或拉伸下边框  调整下方的组件位置大小
     */
    protected void CalculateDownRelatComponent( int objY, int objHeight){
    	int count = downComps.size();
    	for(int i=0; i<count; i++){
    		XCreator creator = (XCreator)downComps.get(i);
    		adjustCompsSize(creator,objHeight,false);
    		int rx = creator.getX();
    		int rwidth = creator.getWidth();
    		int rheight = creator.getHeight();
			creator.setLocation(rx, objY);
			creator.setSize(rwidth, rheight+objHeight);
    	}
    }
    
    /**
     * 实际拖拽偏移量是否超出了可调整的高度范围
     * @param offset 实际偏移量
     * @return boolean 是否超出调整范围
     * 
     */
    private boolean isBeyondAdjustHeightScope(int offset){
    	boolean isBeyondScope = false;
    	isBeyondScope = offset < 0 ? isBeyondHeightScope(offset, upComps) : isBeyondHeightScope(offset, downComps);
    	return isBeyondScope;
    }
    
    // 实际拖拽偏移量是否超出某一侧的可调整高度
    private boolean isBeyondHeightScope(int offset,List<?> compsList){
    	int minHeight = getCompsMinHeight(compsList);
		for(int i=0;i<compsList.size();i++){
			XCreator creator = (XCreator) compsList.get(i);
			if(Math.abs(offset) > (creator.getHeight() - minHeight)){
				return true;
			}
		}
		return false;
    }
    /**
     * 删除或拉伸上边框    调整上方的组件位置大小
     */
    protected boolean CalculateUpRelatComponent(int objHeight){
    	if(isBeyondAdjustHeightScope(objHeight)){
    		return false;
    	}
    	int count = upComps.size();
    	for(int i=0; i<count; i++){
    		XCreator creator =  (XCreator)upComps.get(i);
    		adjustCompsSize(creator, objHeight,false);
    		int rwidth = creator.getWidth();
    		int rheight = creator.getHeight();
    		creator.setSize(rwidth, rheight+objHeight);
    	}
    	return true;
    }

	/**
	 * 不调整，只计算位置
	 * @return child的位置
	 */
	public int[] getChildPosition(Component currentComp, XCreator child, int x, int y ) {
		if (currentComp == container) {
			return new int[]{0, 0, container.getWidth(), container.getHeight()};
		}
		this.isCalculateChildPos = true;
		if (isCrossPointArea(currentComp, x, y)){
			fixCrossPointArea(currentComp, child, x, y);
		} else if (isTrisectionArea(currentComp, x, y)) {
			fixTrisect(currentComp, child, x, y);
		} else {
			fixHalve(currentComp, child, x, y);
		}
		if (childPosition==null) {
			childPosition = new int[]{0, 0, 0, 0};
		}
		this.isCalculateChildPos = false;
		return childPosition;
	}
	
  @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
        return new FRFitLayoutConstraints((XWFitLayout)container, creator);
    }
}