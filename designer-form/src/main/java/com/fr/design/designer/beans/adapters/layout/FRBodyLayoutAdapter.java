package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.general.ComparatorUtils;

import java.awt.*;
import java.util.*;

/**
 * 这个类用作fit和absolute的父类，存放公共的方法
 * 因为body目前是fit自适应的，内部布局如absolute周围要添加控件的话还是要遵循fit的逻辑，
 * 所以把公共的方法挪动这边父类里来
 * Created by zhouping on 2016/8/18.
 */
public class FRBodyLayoutAdapter extends AbstractLayoutAdapter {
    protected static final int COMP_TOP = 1;
    protected static final int COMP_BOTTOM = 2;
    protected static final int COMP_LEFT = 3;
    protected static final int COMP_RIGHT = 4;
    private static final int COMP_LEFT_TOP = 5;
    private static final int COMP_LEFT_BOTTOM = 6;
    private static final int COMP_RIGHT_TOP = 7;
    private static final int COMP_RIGHT_BOTTOM = 8;
    private static final int INDEX_ZERO = 0;

    protected static final double TOP_HALF = 0.25;
    protected static final double BOTTOM_HALF = 0.75;
    protected static final int DEFAULT_AREA_LENGTH = 5; //判断交叉区域范围的默认长度
    protected static final int BORDER_PROPORTION = 10; //边界三等分或交叉区域大小取组件1/10和默认大小

    protected int trisectAreaDirect = 0;
    private int crossPointAreaDirect = 0;
    // 增加删除拉伸控件用的临时list
    protected java.util.List<Component> rightComps;
    protected java.util.List<Component> leftComps;
    protected java.util.List<Component> downComps;
    protected java.util.List<Component> upComps;
    // 三等分时计算对应侧的组件
    protected boolean isFindRelatedComps = false;
    // 渲染时只计算对应的bounds而不调整
    private boolean isCalculateChildPos = false;
    private int[] childPosition = null; //painter用的位置
    protected int minWidth = 0; // 最小尺寸，由于屏幕百分比里不同，显示的最小大小也不同
    protected int minHeight = 0;
    protected int actualVal = 0;  // 存在间隔时，add move drag 判断对齐等都要考虑
    protected PaddingMargin margin; // 布局容器边距

    /**
     * 在添加组件状态时，当鼠标移动到某个容器上方时，如果该容器有布局管理器，则会调用该布局
     * 管理适配器的accept来决定当前位置是否可以放置，并提供特殊的标识，比如红色区域标识。比
     * 如在BorderLayout中，如果某个方位已经放置了组件，则此时应该返回false标识该区域不可以
     * 放置。
     *
     * @param creator 组件
     * @param x       添加的位置x，该位置是相对于container的
     * @param y       添加的位置y，该位置是相对于container的
     * @return 是否可以放置
     */
    @Override
    public boolean accept(XCreator creator, int x, int y) {
        return false;
    }

    @Override
    protected void addComp(XCreator creator, int x, int y) {

    }

    public FRBodyLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    /**
     * 交叉点区域时，能否对应位置放入组件
     *
     * @param comp 待放置组件
     * @param x    x
     * @param y    y
     * @return 能否放入
     */
    protected boolean canAcceptWhileCrossPoint(Component comp, int x, int y) {
        int cX = comp.getX(), cY = comp.getY(), cH = comp.getHeight(), cW = comp.getWidth();
        Component topComp = container.getTopComp(cX, cY);
        Component bottomComp = container.getBottomComp(cX, cY, cH);
        Component rightComp = container.getRightComp(cX, cY, cW);
        Component leftComp = container.getLeftComp(cX, cY);
        int minLength = 0, min = minHeight * 2;
        boolean isNotDefaultArea = false;
        if (ComparatorUtils.equals(crossPointAreaDirect, COMP_LEFT_TOP)) {
            isNotDefaultArea = (topComp == null) || (topComp.getX() != cX);
            minLength = isNotDefaultArea ? Math.min(cH, leftComp.getHeight()) : Math.min(cW, topComp.getWidth());
            min = isNotDefaultArea ? min : minWidth * 2;
        } else if (ComparatorUtils.equals(crossPointAreaDirect, COMP_RIGHT_BOTTOM)) {
            bottomComp = container.getRightBottomComp(cX, cY, cH, cW);
            isNotDefaultArea = (bottomComp == null) || (bottomComp.getX() + bottomComp.getWidth() != cX + cW);
            rightComp = container.getBottomRightComp(cX, cY, cH, cW);
            minLength = isNotDefaultArea ? Math.min(cH, rightComp.getHeight()) : Math.min(cW, bottomComp.getWidth());
            min = isNotDefaultArea ? min : minWidth * 2;
        } else if (ComparatorUtils.equals(crossPointAreaDirect, COMP_LEFT_BOTTOM)) {
            leftComp = container.getBottomLeftComp(cX, cY, cH);
            isNotDefaultArea = (leftComp == null) || (leftComp.getY() + leftComp.getHeight() != cY + cH);
            minLength = isNotDefaultArea ? Math.min(cW, bottomComp.getWidth()) : Math.min(cH, leftComp.getHeight());
            min = isNotDefaultArea ? minWidth * 2 : min;
        } else if (ComparatorUtils.equals(crossPointAreaDirect, COMP_RIGHT_TOP)) {
            isNotDefaultArea = (rightComp == null) || (rightComp.getY() != cY);
            topComp = container.getRightTopComp(cX, cY, cW);
            minLength = isNotDefaultArea ? Math.min(cW, topComp.getWidth()) : Math.min(cH, rightComp.getWidth());
            min = isNotDefaultArea ? minWidth * 2 : min;
        } else if (ComparatorUtils.equals(crossPointAreaDirect, COMP_TOP)) {
            minLength = Math.min(rightComp.getHeight(), Math.min(cH, leftComp.getHeight()));
        } else if (ComparatorUtils.equals(crossPointAreaDirect, COMP_BOTTOM)) {
            leftComp = container.getBottomLeftComp(cX, cY, cH);
            rightComp = container.getBottomRightComp(cX, cY, cH, cW);
            minLength = Math.min(rightComp.getHeight(), Math.min(cH, leftComp.getHeight()));
        } else {
            if (ComparatorUtils.equals(crossPointAreaDirect, COMP_RIGHT)) {
                topComp = container.getRightTopComp(cX, cY, cW);
                bottomComp = container.getRightBottomComp(cX, cY, cH, cW);
            }
            minLength = Math.min(topComp.getWidth(), Math.min(cW, bottomComp.getWidth()));
            min = minWidth * 2;
        }
        // 有间隔的话，要考虑容纳间隔
        return minLength >= min + actualVal;
    }

    /**
     * 三等分区域时，能否对应位置放入组件
     *
     * @param comp 待放置组件
     * @param x    x
     * @param y    y
     * @return 能否放入
     */
    protected boolean canAcceptWhileTrisection(Component comp, int x, int y) {
        //符合三等分，实际区域不满足三等分的大小
        int cX = comp.getX(), cY = comp.getY(), cH = comp.getHeight(), cW = comp.getWidth();
        int upMinHeight = 0, downMinHeight = 0, leftMinWidth = 0, rightMinWidth = 0;
        if (ComparatorUtils.equals(trisectAreaDirect, COMP_TOP)) {
            upMinHeight = getUpMinHeightComp(cY, x);
            downMinHeight = getDownMinHeightComp(comp, y);
            return upMinHeight == 0 ? downMinHeight >= minHeight * 2 + actualVal : (upMinHeight + downMinHeight) >= minHeight * 3 + actualVal;
        } else if (ComparatorUtils.equals(trisectAreaDirect, COMP_BOTTOM)) {
            upMinHeight = getUpMinHeightComp(cY + cH + actualVal, x);
            if (cY + cH + DEFAULT_AREA_LENGTH > container.getHeight() - margin.getBottom()) {
                downMinHeight = 0;
            } else {
                Component targetComp = container.getBottomComp(x, cY, cH);
                downMinHeight = getDownMinHeightComp(targetComp, cY + cH + DEFAULT_AREA_LENGTH + actualVal);
            }
            return downMinHeight == 0 ? (upMinHeight >= minHeight * 2 + actualVal) : ((upMinHeight + downMinHeight) >= minHeight * 3 + actualVal);
        } else if (ComparatorUtils.equals(trisectAreaDirect, COMP_LEFT)) {
            rightMinWidth = getMinRightWidth(cX, 0, y);
            if (cX - DEFAULT_AREA_LENGTH < margin.getLeft()) {
                leftMinWidth = 0;
            } else {
                Component targetRightComp = container.getLeftComp(cX, y);
                leftMinWidth = getMinLeftWidth(targetRightComp, cX - DEFAULT_AREA_LENGTH - actualVal);
            }
            return leftMinWidth == 0 ? (rightMinWidth >= minWidth * 2 + actualVal) : ((leftMinWidth + rightMinWidth) >= minWidth * 3 + actualVal);
        } else if (ComparatorUtils.equals(trisectAreaDirect, COMP_RIGHT)) {
            leftMinWidth = getMinLeftWidth(comp, x);
            rightMinWidth = getMinRightWidth(cX, cW, y);
            return rightMinWidth == 0 ? (leftMinWidth >= minWidth * 2 + actualVal) : ((leftMinWidth + rightMinWidth) >= minWidth * 3 + actualVal);
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
        int max = container.getWidth() - margin.getRight();
        int mouseX = x;
        int minHeight = cY;
        int bott = 0;
        if (isFindRelatedComps) {
            upComps = new ArrayList<Component>();
        }
        for (; mouseX < max; ) {
            Component comp = container.getTopComp(mouseX, cY);
            if (comp == null) {
                break;
            }
            bott = comp.getHeight() + comp.getY() + actualVal;
            if (bott == cY) {
                if (comp.getHeight() < minHeight) {
                    minHeight = comp.getHeight();
                }
                mouseX = comp.getX() + comp.getWidth() + DEFAULT_AREA_LENGTH + actualVal;
                if (isFindRelatedComps) {
                    upComps.add(comp);
                }
            } else {
                break;
            }
        }
        if (container.getTopComp(x, cY) == null) {
            return 0;
        }
        mouseX = container.getTopComp(x, cY).getX() - DEFAULT_AREA_LENGTH - actualVal;
        while (mouseX > margin.getLeft()) {
            Component comp = container.getTopComp(mouseX, cY);
            bott = comp.getHeight() + comp.getY() + actualVal;
            if (bott == cY) {
                if (comp.getHeight() < minHeight) {
                    minHeight = comp.getHeight();
                }
                mouseX = comp.getX() - DEFAULT_AREA_LENGTH - actualVal;
                if (isFindRelatedComps) {
                    upComps.add(comp);
                }
            } else {
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
        int max = container.getWidth() - margin.getRight();
        if (isFindRelatedComps) {
            downComps = new ArrayList<Component>();
        }
        int mouseX = cX + DEFAULT_AREA_LENGTH;
        while (mouseX < max) {
            Component comp = container.getComponentAt(mouseX, y);
            if (comp.getY() == cY) {
                if (comp.getHeight() < minHeight) {
                    minHeight = comp.getHeight();
                }
                mouseX = comp.getX() + comp.getWidth() + DEFAULT_AREA_LENGTH + actualVal;
                if (isFindRelatedComps) {
                    downComps.add(comp);
                }
            } else {
                break;
            }
        }
        mouseX = cX - DEFAULT_AREA_LENGTH - actualVal;
        while (mouseX > margin.getLeft()) {
            Component comp = container.getComponentAt(mouseX, y);
            if (comp.getY() == cY) {
                if (comp.getHeight() < minHeight) {
                    minHeight = comp.getHeight();
                }
                mouseX = comp.getX() - DEFAULT_AREA_LENGTH - actualVal;
                if (isFindRelatedComps) {
                    downComps.add(comp);
                }
            } else {
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
        int xL = cX + DEFAULT_AREA_LENGTH;
        xL = cW == 0 ? xL : (xL + cW + actualVal);
        if (xL > container.getWidth() - margin.getRight()) {
            return 0;
        }
        // 以当前组件紧挨着右侧的组件为基准，在y轴方向查找符合条件的组件
        Component targetComp = container.getComponentAt(xL, y);
        int minWidth = targetComp.getWidth();
        int max = container.getHeight() - margin.getBottom();
        if (isFindRelatedComps) {
            rightComps = new ArrayList<Component>();
        }
        int mouseY = targetComp.getY() + DEFAULT_AREA_LENGTH;
        while (mouseY < max) {
            Component comp = container.getComponentAt(xL, mouseY);
            if (comp.getX() == targetComp.getX()) {
                if (comp.getWidth() < minWidth) {
                    minWidth = comp.getWidth();
                }
                mouseY = comp.getY() + comp.getHeight() + DEFAULT_AREA_LENGTH + actualVal;
                if (isFindRelatedComps) {
                    rightComps.add(comp);
                }
            } else {
                break;
            }
        }
        mouseY = targetComp.getY() - DEFAULT_AREA_LENGTH - actualVal;
        while (mouseY > margin.getTop()) {
            Component comp = container.getComponentAt(xL, mouseY);
            if (comp.getX() == targetComp.getX()) {
                if (comp.getWidth() < minWidth) {
                    minWidth = comp.getWidth();
                }
                mouseY = comp.getY() - DEFAULT_AREA_LENGTH - actualVal;
                if (isFindRelatedComps) {
                    rightComps.add(comp);
                }
            } else {
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
        int compRightLength = currentComp.getX() + currentComp.getWidth();
        int max = container.getHeight() - margin.getBottom();
        if (isFindRelatedComps) {
            leftComps = new ArrayList<Component>();
        }
        int rightx = 0;
        int mouseY = currentComp.getY() + DEFAULT_AREA_LENGTH;
        while (mouseY < max) {
            Component comp = container.getComponentAt(x, mouseY);
            rightx = comp.getX() + comp.getWidth();
            if (rightx == compRightLength) {
                if (comp.getWidth() < minWidth) {
                    minWidth = comp.getWidth();
                }
                mouseY = comp.getY() + comp.getHeight() + DEFAULT_AREA_LENGTH + actualVal;
                if (isFindRelatedComps) {
                    leftComps.add(comp);
                }
            } else {
                break;
            }
        }
        mouseY = currentComp.getY() - DEFAULT_AREA_LENGTH - actualVal;
        while (mouseY > margin.getTop()) {
            Component comp = container.getComponentAt(x, mouseY);
            rightx = comp.getX() + comp.getWidth();
            if (rightx == compRightLength) {
                if (comp.getWidth() < minWidth) {
                    minWidth = comp.getWidth();
                }
                mouseY = comp.getY() - DEFAULT_AREA_LENGTH - actualVal;
                if (isFindRelatedComps) {
                    leftComps.add(comp);
                }
            } else {
                break;
            }
        }
        return minWidth;
    }

    /**
     * 判断是否鼠标在组件的三等分区域，如果组件在布局管理器中间，上下左右都可能会三等分
     *
     * @param parentComp 鼠标所在区域的组件
     * @param x          坐标x
     * @param y          坐标y
     * @return 是则返回true
     */
    public boolean isTrisectionArea(Component parentComp, int x, int y) {
        XCreator creator = (XCreator) parentComp;
        if (container.getComponentCount() <= 1) {
            return false;
        }
        int maxWidth = parentComp.getWidth();
        int maxHeight = parentComp.getHeight();
        int xL = parentComp.getX();
        int yL = parentComp.getY();
        // 组件宽高的十分之一和默认值取大
        int minRangeWidth = Math.max(maxWidth / BORDER_PROPORTION, DEFAULT_AREA_LENGTH);
        int minRangeHeight = Math.max(maxHeight / BORDER_PROPORTION, DEFAULT_AREA_LENGTH);
        if (y < yL + minRangeHeight) {
            // 在组件上侧三等分
            trisectAreaDirect = COMP_TOP;
        } else if (y > yL + maxHeight - minRangeHeight) {
            // 在组件下侧三等分
            trisectAreaDirect = COMP_BOTTOM;
        } else if (x < xL + minRangeWidth) {
            // 在组件左侧三等分
            trisectAreaDirect = COMP_LEFT;
        } else if (x > xL + maxWidth - minRangeWidth) {
            // 在组件右侧三等分
            trisectAreaDirect = COMP_RIGHT;
        }
        // tab布局的边界特殊处理，不进行三等分
        if (!creator.getTargetChildrenList().isEmpty()) {
            return false;
        }

        return !ComparatorUtils.equals(trisectAreaDirect, 0);
    }

    /**
     * 是否为组件交叉点区域 或者是相邻三组建中间点
     *
     * @param currentComp 当前组件
     * @param x           坐标x
     * @param y           坐标y
     * @return 是则返回true
     */
    public boolean isCrossPointArea(Component currentComp, int x, int y) {
        // 3个及以上都会出现交叉点区域（包括边界处的）
        if (currentComp == null || container.getComponentCount() <= 2) {
            return false;
        }
        int cX = currentComp.getX();
        int cY = currentComp.getY();
        int cW = currentComp.getWidth();
        int cH = currentComp.getHeight();
        int areaWidth = Math.max(cW / BORDER_PROPORTION, DEFAULT_AREA_LENGTH);
        int areaHeight = Math.max(cH / BORDER_PROPORTION, DEFAULT_AREA_LENGTH);
        int rx = cX + cW;
        int by = cY + cH;
        int objX = cX + areaWidth;
        int objY = cY + areaHeight;
        int containerW = container.getWidth() - margin.getRight();
        int containerH = container.getHeight() - margin.getBottom();
        if (x < objX && y < objY) {
            //左上角区域
            crossPointAreaDirect = cY > margin.getTop() || cX > margin.getLeft() ? COMP_LEFT_TOP : 0;
        } else if (y < objY && x > rx - areaWidth) {
            //右上角
            crossPointAreaDirect = cY > margin.getTop() || rx < containerW ? COMP_RIGHT_TOP : 0;
        } else if (x < objX && y > by - areaHeight) {
            //左下角
            crossPointAreaDirect = cX > margin.getLeft() || by < containerH ? COMP_LEFT_BOTTOM : 0;
        } else if (x > rx - areaWidth && y > by - areaHeight) {
            //右下角
            crossPointAreaDirect = by < containerH || rx < containerW ? COMP_RIGHT_BOTTOM : 0;
        } else {
            isMiddlePosition(currentComp, x, y, areaWidth, areaHeight);
        }
        // tab布局的边界特殊处理
        XCreator creator = (XCreator) currentComp;
        if (!creator.getTargetChildrenList().isEmpty()) {
            return false;
        }
        return crossPointAreaDirect != 0;
    }

    private void isMiddlePosition(Component comp, int x, int y, int areaWidth, int areaHeight) {
        int cX = comp.getX();
        int cY = comp.getY();
        int cW = comp.getWidth();
        int cH = comp.getHeight();
        boolean isCrosspoint = false;
        if (x > (cX + cW / 2 - areaWidth) && x < (cX + cW / 2 + areaWidth)) {
            // 上下边框线中间位置
            Component leftComp = container.getLeftComp(cX, cY);
            Component rightComp = container.getRightComp(cX, cY, cW);
            if (y < cY + areaHeight) {
                isCrosspoint = leftComp != null && rightComp != null && leftComp.getY() == cY && rightComp.getY() == cY;
                crossPointAreaDirect = isCrosspoint ? COMP_TOP : 0;
            } else if (y > cY + cH - areaHeight) {
                leftComp = container.getBottomLeftComp(cX, cY, cH);
                rightComp = container.getBottomRightComp(cX, cY, cH, cW);
                if (leftComp != null && rightComp != null) {
                    isCrosspoint = leftComp.getY() + leftComp.getHeight() == cY + cH && rightComp.getY() + rightComp.getHeight() == cY + cH;
                }
                crossPointAreaDirect = isCrosspoint ? COMP_BOTTOM : 0;
            }
        } else if (y > (cY + cH / 2 - areaHeight) && y < (cY + cH / 2 + areaHeight)) {
            // 左右边框线中间位置
            Component topComp = container.getTopComp(cX, cY);
            Component bottomComp = container.getBottomComp(cX, cY, cH);
            if (x < (cX + areaWidth)) {
                isCrosspoint = topComp != null && bottomComp != null && topComp.getX() == cX && bottomComp.getX() == cX;
                crossPointAreaDirect = isCrosspoint ? COMP_LEFT : 0;
            } else if (x > (cX + cW - areaWidth)) {
                topComp = container.getRightTopComp(cX, cY, cW);
                bottomComp = container.getRightBottomComp(cX, cY, cH, cW);
                if (topComp != null && bottomComp != null) {
                    isCrosspoint = topComp.getX() + topComp.getWidth() == cX + cW && bottomComp.getX() + bottomComp.getWidth() == cX + cW;
                }
                crossPointAreaDirect = isCrosspoint ? COMP_RIGHT : 0;
            }
        }
    }

    /**
     * 初始化周边组件列表
     */
    protected void initCompsList() {
        rightComps = new ArrayList<Component>();
        leftComps = new ArrayList<Component>();
        upComps = new ArrayList<Component>();
        downComps = new ArrayList<Component>();
    }

    /**
     * 清除周边组件列表
     */
    protected void clearCompsList() {
        rightComps = null;
        leftComps = null;
        upComps = null;
        downComps = null;
    }

    /**
     * 平分，正常情况拖入组件时，按照上1/4区域、下1/4区域为上下平分，中左侧1/2区域、中右侧1/2区域为左右平分
     *
     * @param currentComp 当前位置组件
     * @param child       待放置组件
     * @param x           x
     * @param y           y
     */
    protected void fixHalve(Component currentComp, XCreator child, int x, int y) {
        XCreator creator = (XCreator) currentComp;
        if (!creator.getTargetChildrenList().isEmpty()) {
            fixHalveOfTab(creator, child, x, y);
            return;
        }
        int maxWidth = currentComp.getWidth();
        int maxHeight = currentComp.getHeight();
        int xL = currentComp.getX();
        int yL = currentComp.getY();
        Dimension dim = new Dimension();
        boolean isDividUp = y - yL <= maxHeight * TOP_HALF;
        boolean isDividDown = y - yL >= maxHeight * BOTTOM_HALF;
        boolean isDividLeft = x - xL < maxWidth / 2;
        int finalX = xL;
        int finalY = yL;
        int finalW = maxWidth;
        int finalH = maxHeight;
        if (isDividUp) {
            dim.width = maxWidth;
            dim.height = maxHeight / 2 - actualVal / 2;
            finalY = yL + dim.height + actualVal;
            finalH = maxHeight - dim.height - actualVal;
        } else if (isDividDown) {
            // 若当前区域高度非偶数，那么和isDividUp时计算一致，否则永远都是上半部分小1px
            dim.height = maxHeight / 2 - actualVal / 2;
            dim.width = maxWidth;
            finalH = maxHeight - dim.height - actualVal;
            yL = yL + finalH + actualVal;
        } else if (isDividLeft) {
            dim.width = maxWidth / 2 - actualVal / 2;
            dim.height = maxHeight;
            finalX = xL + dim.width + actualVal;
            finalW = maxWidth - dim.width - actualVal;
        } else {
            finalW = maxWidth / 2 - actualVal / 2;
            xL = xL + finalW + actualVal;
            dim.width = maxWidth - finalW - actualVal;
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

    // 边界判断抄得原来的逻辑
    // tab布局的边界拖入新组件，和当前tab布局平分，这时候的actualVal组建间隔是tab里面的组建间隔
    // 不应该在外层自适应布局添加
    private void fixHalveOfTab(XCreator currentCreator, XCreator child, int x, int y) {
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
        switch (position) {
            case COMP_TOP:
                dim.width = maxWidth;
                dim.height = maxHeight / 2  - actualVal / 2;
                finalY = yL + dim.height +  actualVal;
                finalH = maxHeight - dim.height - actualVal;
                break;
            case COMP_BOTTOM:
                dim.height = maxHeight / 2 -  actualVal / 2;
                dim.width = maxWidth;
                finalH = maxHeight - dim.height - actualVal;
                yL = yL + finalH + actualVal;
                break;
            case COMP_LEFT:
                dim.width = maxWidth / 2 -  actualVal / 2;
                dim.height = maxHeight;
                finalX = xL + dim.width + actualVal;
                finalW = maxWidth - dim.width - actualVal;
                break;
            default:
                finalW = maxWidth / 2 -  actualVal / 2;
                xL = xL + finalW + actualVal;
                dim.width = maxWidth - finalW - actualVal;
                dim.height = maxHeight;
        }
        if (isCalculateChildPos) {
            childPosition = new int[]{xL, yL, dim.width, dim.height};
        } else {
            currentCreator.setLocation(finalX, finalY);
            currentCreator.setSize(finalW, finalH);
            currentCreator.recalculateChildWidth(finalW, false);
            currentCreator.recalculateChildHeight(finalH, false);
            child.setLocation(xL, yL);
            child.setSize(dim);
        }
    }

    private int getPositionOfFix(XCreator currentCreator, int x, int y) {
        int position = 0;
        XLayoutContainer cardLayout = ((XWCardMainBorderLayout) currentCreator).getCardPart();
        XLayoutContainer container = (XLayoutContainer) cardLayout.getComponent(0);
        Rectangle rect = ComponentUtils.getRelativeBounds(container);
        /*
        * 为了获取到鼠标drop位置的控件，
        * 我们之前已经将y值变为相对坐标值；
        * 由于在x轴上没有偏移，所以x值一直等于相对坐标值，最多差一个边界值1。
        * 在进行新添加的控件位置计算时，
        * 又通过ComponentUtils.getRelativeBounds()方法获取到了绝对坐标，
        * 再次计算相对坐标，所以将y值重新变成绝对坐标。
        * */
        y = y + WCardMainBorderLayout.TAB_HEIGHT + this.getParaEditorYOffset();
        int tempX = x - rect.x;
        int tempY = y - rect.y;
        int containerX = container.getX();
        int containerY = container.getY();
        int containerWidth = container.getWidth();
        int containerHeight = container.getHeight();
        // 当前坐标点
        Rectangle currentXY = new Rectangle(tempX, tempY, 1, 1);
        // 上边缘
        Rectangle upEdge = new Rectangle(containerX, containerY, containerWidth, BORDER_PROPORTION);
        if (upEdge.intersects(currentXY)) {
            position = COMP_TOP;
        }
        int bottomY = containerY + containerHeight - BORDER_PROPORTION;
        // 下边缘
        Rectangle bottomEdge = new Rectangle(containerX, bottomY, containerWidth, BORDER_PROPORTION);
        if (bottomEdge.intersects(currentXY)) {
            position = COMP_BOTTOM;
        }
        //左右边缘的高度 -10*2 是为了不和上下边缘重合
        int verticalHeight = containerHeight - BORDER_PROPORTION * 2;
        int leftY = containerY + BORDER_PROPORTION;
        // 左边缘
        Rectangle leftEdge = new Rectangle(containerX, leftY, BORDER_PROPORTION, verticalHeight);
        if (leftEdge.intersects(currentXY)) {
            position = COMP_LEFT;
        }
        return position;
    }

    /**
     * 获取因为参数面板导致的Y坐标偏移
     *
     * @return 参数面板导致的Y坐标偏移
     */
    protected int getParaEditorYOffset() {
        int offset = 0;
        if (container.getParent() != null) {
            Component components[] = container.getParent().getComponents();
            for (Component component : components) {
                if (component instanceof XWParameterLayout) {
                    offset = component.getY() + component.getHeight();
                    break;
                }
            }
        }
        return offset;
    }


    /**
     * 组件交叉区域进行插入时，调整受到变动的其他组件,之前是交叉区域插入也按照三等分逻辑，后面测试中发现有bug，改为和bi一样的鼠标所在侧平分
     * 默认左上角、右下角区域是垂直方向插入组件
     * 右上角和左下角是水平方向插入组件，这样避免田字块时重复
     *
     * @param currentComp 当前位置组件
     * @param child       待放置组件
     * @param x           x
     * @param y           y
     */
    protected void fixCrossPointArea(Component currentComp, XCreator child, int x, int y) {
        //计算前先全部初始化待调整控件所在的list
        initCompsList();
        switch (crossPointAreaDirect) {
            case COMP_LEFT_TOP:
                dealCrossPointAtLeftTop(currentComp, child);
                break;
            case COMP_RIGHT_BOTTOM:
                dealCrossPointAtRightBottom(currentComp, child);
                break;
            case COMP_LEFT_BOTTOM:
                dealCrossPointAtLeftBottom(currentComp, child);
                break;
            case COMP_RIGHT_TOP:
                dealCrossPointAtRightTop(currentComp, child);
                break;
            case COMP_TOP:
                dealCrossPointAtTop(currentComp, child);
                break;
            case COMP_BOTTOM:
                dealCrossPointAtBottom(currentComp, child);
                break;
            case COMP_LEFT:
                dealCrossPointAtLeft(currentComp, child);
                break;
            case COMP_RIGHT:
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
        if (topComp == null || topComp.getX() != cX) {
            minDH = cH < leftComp.getHeight() ? cH : leftComp.getHeight();
            downComps.add(leftComp);
            downComps.add(currentComp);
            int dLength = minDH / 2;
            childw = leftComp.getWidth() + cW + actualVal;
            childh = dLength - actualVal / 2;
            if (isCalculateChildPos) {
                childPosition = new int[]{leftComp.getX(), leftComp.getY(), childw, childh};
            } else {
                //先设置child位置，不然leftComp坐标调整后就不对了
                child.setLocation(leftComp.getX(), leftComp.getY());
                child.setSize(childw, childh);
                calculateBottomComps(dLength);
            }
        } else {
            rightComps.add(currentComp);
            rightComps.add(topComp);
            minRW = cW < topComp.getWidth() ? cW : topComp.getWidth();
            int rightLength = minRW / 2;
            childw = rightLength - actualVal / 2;
            childh = currentComp.getHeight() + topComp.getHeight() + actualVal;
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
        if (bottomComp == null || (bottomComp.getX() + bottomComp.getWidth() != cX + cW)) {
            minUH = cH < rightComp.getHeight() ? cH : rightComp.getHeight();
            upComps.add(currentComp);
            upComps.add(rightComp);
            int uLength = minUH / 2;
            calculateTopComps(uLength, child, uLength);
        } else {
            leftComps.add(currentComp);
            leftComps.add(bottomComp);
            minLW = cW < bottomComp.getWidth() ? cW : bottomComp.getWidth();
            int leftLength = minLW / 2;
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
        if (leftComp == null || (leftComp.getY() + leftComp.getHeight() != cY + cH)) {
            rightComps.add(currentComp);
            rightComps.add(bottomComp);
            minRW = cW < bottomComp.getWidth() ? cW : bottomComp.getWidth();
            int rightLength = minRW / 2;
            int childw = rightLength - actualVal / 2;
            int childh = currentComp.getHeight() + bottomComp.getHeight() + actualVal;
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
            minUH = cH < leftComp.getHeight() ? cH : leftComp.getHeight();
            int uLength = minUH / 2;
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
        if (rightComp == null || (rightComp.getY() != cY)) {
            leftComps.add(currentComp);
            leftComps.add(topComp);
            minLW = cW < topComp.getWidth() ? cW : topComp.getWidth();
            int leftLength = minLW / 2;
            calculateLeftComps(leftLength, child, leftLength);
        } else {
            minDH = cH < rightComp.getHeight() ? cH : rightComp.getHeight();
            downComps.add(currentComp);
            downComps.add(rightComp);
            int dLength = minDH / 2;
            int childw = cW + rightComp.getWidth() + actualVal;
            int childh = dLength - actualVal / 2;
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
        int dLength = minDH / 2;
        int childw = cW + leftComp.getWidth() + rightComp.getWidth() + actualVal * 2;
        int childh = dLength - actualVal / 2;
        if (isCalculateChildPos) {
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
        int uLength = minUH / 2;
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
        int leftLength = minLW / 2;
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
        int rightLength = minRW / 2;
        int childw = rightLength - actualVal / 2;
        int childh = topComp.getHeight() + currentComp.getHeight() + bottomComp.getHeight() + actualVal * 2;
        if (isCalculateChildPos) {
            childPosition = new int[]{topComp.getX(), topComp.getY(), childw, childh};
        } else {
            child.setLocation(topComp.getX(), topComp.getY());
            child.setSize(childw, childh);
            calculateRightComps(rightLength);
        }
    }

    /**
     * 三等分区域时，调整相关联的组件
     *
     * @param currentComp 鼠标所在组件
     * @param child       待插入组件
     */
    protected void fixTrisect(Component currentComp, XCreator child, int x, int y) {
        int minUpH = 0, minDownH = 0, minLeftW = 0, minRightW = 0;
        int cX = currentComp.getX(), cY = currentComp.getY(), cH = currentComp.getHeight(), cW = currentComp.getWidth();
        isFindRelatedComps = true;
        if (ComparatorUtils.equals(trisectAreaDirect, COMP_TOP)) {
            minUpH = getUpMinHeightComp(cY, x);
            minDownH = getDownMinHeightComp(currentComp, y);
            dealTrisectAtTop(child, minUpH, minDownH);
        } else if (ComparatorUtils.equals(trisectAreaDirect, COMP_BOTTOM)) {
            minUpH = getUpMinHeightComp(cY + cH + actualVal, x);
            if (cY + cH + DEFAULT_AREA_LENGTH < container.getHeight() - margin.getBottom()) {
                Component targetTopComp = container.getBottomComp(x, cY, cH);
                minDownH = getDownMinHeightComp(targetTopComp, cY + cH + DEFAULT_AREA_LENGTH + actualVal);
            }
            dealTrisectAtTop(child, minUpH, minDownH);
        } else if (ComparatorUtils.equals(trisectAreaDirect, COMP_RIGHT)) {
            minRightW = getMinRightWidth(cX, cW, y);
            minLeftW = getMinLeftWidth(currentComp, x);
            dealTrisectAtRight(child, minLeftW, minRightW);
        } else if (ComparatorUtils.equals(trisectAreaDirect, COMP_LEFT)) {
            // 当前组件就在右侧时，cW为0
            minRightW = getMinRightWidth(cX, 0, y);
            if (cX - DEFAULT_AREA_LENGTH > margin.getLeft()) {
                Component targetRightComp = container.getLeftComp(cX, y);
                minLeftW = getMinLeftWidth(targetRightComp, cX - DEFAULT_AREA_LENGTH - actualVal);//bug104400没算上间隔
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
        int averageH = (minUH + minDH - actualVal) / 3;
        int dLength = 0;
        int uLength = 0;
        if (minDH == 0) {
            dLength = 0;
            uLength = minUH / 2;
            calculateTopComps(uLength, child, uLength);
            return;
        } else if (minUH == 0) {
            dLength = minDH / 2;
            int witdh = container.getWidth() - margin.getLeft() - margin.getRight();
            if (!isCalculateChildPos) {
                calculateBottomComps(dLength);
                child.setLocation(margin.getLeft(), margin.getRight());
                child.setSize(witdh, dLength - actualVal / 2);
            } else {
                childPosition = new int[]{margin.getLeft(), margin.getRight(), witdh, dLength - actualVal / 2};
            }
            return;
        } else if (minUH >= minDH) {
            minDH -= actualVal / 2;
            if ((minDH * 2 / 3) < minHeight) {
                dLength = minDH - minHeight;
            } else {
                dLength = minDH / 3;
            }
            uLength = averageH - dLength;
        } else {
            minUH -= actualVal / 2;
            if ((minUH * 2 / 3) < minHeight) {
                uLength = minUH - minHeight;
            } else {
                uLength = minUH / 3;
            }
            dLength = averageH - uLength;
        }
        if (!isCalculateChildPos) {
            calculateBottomComps(dLength);
        }
        // 计算两侧时，都额外去掉半个间隔，3等分和平分交叉点不同，只能特殊处理下
        averageH += actualVal / 2;
        calculateTopComps(uLength, child, averageH);
    }

    /**
     * 当前组件右边界区域三等分
     */
    private void dealTrisectAtRight(XCreator child, int minLW, int minRW) {
        int averageW = (minLW + minRW - actualVal) / 3;
        int leftLength = 0;
        int rightLength = 0;
        if (minLW == 0) {
            rightLength = minRW / 2;
            int height = container.getHeight() - margin.getBottom() - margin.getTop();
            if (!isCalculateChildPos) {
                calculateRightComps(rightLength);
                child.setLocation(margin.getLeft(), margin.getRight());
                child.setSize(rightLength - actualVal / 2, height);
            } else {
                childPosition = new int[]{margin.getLeft(), margin.getRight(), rightLength - actualVal / 2, height};
            }
            return;
        } else if (minRW == 0) {
            leftLength = minLW / 2;
            calculateLeftComps(leftLength, child, leftLength);
            return;
        } else if (minRW >= minLW) {
            minLW -= actualVal / 2;
            if (minLW * 2 / 3 < minWidth) {
                leftLength = minLW - minWidth;
            } else {
                leftLength = minLW / 3;
            }
            rightLength = averageW - leftLength;
        } else {
            minRW -= actualVal / 2;
            if (minRW * 2 / 3 < minWidth) {
                rightLength = minRW - minWidth;
            } else {
                rightLength = minRW / 3;
            }
            leftLength = averageW - rightLength;
        }
        if (!isCalculateChildPos) {
            calculateRightComps(rightLength);
        }
        // averageW 是已经去除间隔后的大小，所以再加上半个间隔，否则处理时会变小
        averageW += actualVal / 2;
        calculateLeftComps(leftLength, child, averageW);
    }

    private void calculateBottomComps(int length) {
        length += actualVal / 2;
        for (int i = 0, num = downComps.size(); i < num; i++) {
            Component comp = downComps.get(i);
            comp.setLocation(comp.getX(), comp.getY() + length);
            int offset = comp.getHeight() - length;
            comp.setSize(comp.getWidth(), offset);
            XCreator creator = (XCreator) comp;
            creator.recalculateChildHeight(offset, false);
        }
    }

    private void calculateTopComps(int length, XCreator child, int averageH) {
        length += actualVal / 2;
        int childWidth = (upComps.size() - 1) * actualVal;
        int childX = container.getWidth() - margin.getLeft() - margin.getRight();
        int childY = 0;
        if (upComps.size() > INDEX_ZERO) {
            childY = upComps.get(INDEX_ZERO).getY() + upComps.get(INDEX_ZERO).getHeight() - length;
        }
        for (int i = 0, num = upComps.size(); i < num; i++) {
            Component comp = upComps.get(i);
            childWidth += comp.getWidth();
            if (comp.getX() < childX) {
                childX = comp.getX();
            }
            if (!isCalculateChildPos) {
                int offset = comp.getHeight() - length;
                comp.setSize(comp.getWidth(), offset);
                XCreator creator = (XCreator) comp;
                creator.recalculateChildHeight(offset, false);
            }
        }
        childY += actualVal;
        averageH -= actualVal / 2;
        if (isCalculateChildPos) {
            childPosition = new int[]{childX, childY, childWidth, averageH};
        } else {
            child.setLocation(childX, childY);
            child.setSize(childWidth, averageH);
        }
    }

    private void calculateLeftComps(int length, XCreator child, int averageW) {
        length += actualVal / 2;
        if (leftComps.isEmpty()) {
            return;
        }
        int childH = (leftComps.size() - 1) * actualVal;
        int childX = 0;
        if (leftComps.size() > INDEX_ZERO) {
            childX = leftComps.get(INDEX_ZERO).getX() + leftComps.get(INDEX_ZERO).getWidth() - length;
        }
        int childY = container.getHeight() - margin.getBottom();
        for (int i = 0, num = leftComps.size(); i < num; i++) {
            Component comp = leftComps.get(i);
            childH += comp.getHeight();
            if (comp.getY() < childY) {
                childY = comp.getY();
            }
            if (!isCalculateChildPos) {
                int offset = comp.getWidth() - length;
                comp.setSize(offset, comp.getHeight());
                XCreator creator = (XCreator) comp;
                creator.recalculateChildWidth(offset, false);
            }
        }
        childX += actualVal;
        averageW -= actualVal / 2;
        if (isCalculateChildPos) {
            childPosition = new int[]{childX, childY, averageW, childH};
        } else {
            child.setLocation(childX, childY);
            child.setSize(averageW, childH);
        }
    }

    private void calculateRightComps(int length) {
        length += actualVal / 2;
        for (int i = 0, num = rightComps.size(); i < num; i++) {
            Component comp = rightComps.get(i);
            comp.setLocation(comp.getX() + length, comp.getY());
            int offset = comp.getWidth() - length;
            comp.setSize(offset, comp.getHeight());
            XCreator creator = (XCreator) comp;
            creator.recalculateChildWidth(offset, false);
        }
    }

    /**
     * 不调整，只计算位置
     *
     * @return child的位置
     */
    public int[] getChildPosition(Component currentComp, XCreator child, int x, int y) {
        if (currentComp == container) {
            return new int[]{0, 0, container.getWidth(), container.getHeight()};
        }
        this.isCalculateChildPos = true;
        if (isCrossPointArea(currentComp, x, y)) {
            fixCrossPointArea(currentComp, child, x, y);
        } else if (isTrisectionArea(currentComp, x, y)) {
            fixTrisect(currentComp, child, x, y);
        } else {
            fixHalve(currentComp, child, x, y);
        }
        if (childPosition == null) {
            childPosition = new int[]{0, 0, 0, 0};
        }
        this.isCalculateChildPos = false;
        return childPosition;
    }

}
