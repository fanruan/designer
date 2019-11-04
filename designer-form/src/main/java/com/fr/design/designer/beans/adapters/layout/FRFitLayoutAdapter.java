/**
 *
 */
package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRFitLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWCardTitleLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.designer.properties.FRFitLayoutConstraints;
import com.fr.design.designer.properties.FRFitLayoutPropertiesGroupModel;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WTabDisplayPosition;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.general.ComparatorUtils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * 自适应布局的容器适配器
 *
 * @author jim
 * @date 2014-6-24
 */
public class FRFitLayoutAdapter extends FRBodyLayoutAdapter {

    public static final String WIDGETPANEICONPATH = "/com/fr/web/images/form/resources/layout_absolute.png";
    private static final int DEPENDING_SCOPE = 3;
    private HoverPainter painter;
    //区分拖拽和编辑宽高
    private boolean isEdit;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    /**
     * 构造函数
     *
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
     * @param x     坐标x
     * @param y     坐标y
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

    public void updateCreatorBackBound() {
        for (int i = 0, size = container.getComponentCount(); i < size; i++) {
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
     *
     * @param creator 组件
     * @param x       添加的位置x，该位置是相对于container的
     * @param y       添加的位置y，该位置是相对于container的
     * @return 是否可以放置
     */
    @Override
    public boolean accept(XCreator creator, int x, int y) {
        // 计算是否能拖入鼠标区域时，会用到fix 的方法
        isFindRelatedComps = false;
        //拖入组件判断时，先判断是否为交叉点区域，其次三等分区域，再次平分区域
        Component comp = container.getComponentAt(x, y);
        if (comp == null || checkInterval(comp)) {
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
        XLayoutContainer topLayout = XCreatorUtils.getHotspotContainer((XCreator) comp).getTopLayout();
        if (topLayout != null && !isMatchEdge && !topLayout.isEditable() && !topLayout.acceptType(XWAbsoluteLayout.class)) {
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
    private boolean checkInterval(Component comp) {
        return container.getComponentCount() > 0 && comp == container;
    }

    /**
     * 是否在组件边缘
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return 是否在组件边缘
     */
    public boolean matchEdge(int x, int y) {
        if (intersectsEdge(x, y, container)) {
            //寻找最近的fit, 在边缘地段添加的控件, 将其送给该fit
            XLayoutContainer parent = container.findNearestFit();
            container = parent != null ? parent : container;
            return true;
        }
        return false;
    }

    /**
     * 是否在组件边缘
     *
     * @param x         横坐标
     * @param y         纵坐标
     * @param container 参照组件
     * @return 是否在组件边缘
     */
    //是否包含于边缘地段, 按顺序上, 下, 左, 右检测
    public boolean intersectsEdge(int x, int y, XLayoutContainer container) {
        int containerX = container.getX();
        int containerY = container.getY();
        int containerWidth = container.getWidth();
        int containerHeight = container.getHeight();

        // 当前坐标点
        Rectangle currentXY = new Rectangle(x, y, 1, 1);
        // 上边缘
        Rectangle upEdge = new Rectangle(containerX, containerY, containerWidth, BORDER_PROPORTION);
        if (upEdge.intersects(currentXY)) {
            return true;
        }

        int bottomY = containerY + containerHeight - BORDER_PROPORTION;
        // 下边缘
        Rectangle bottomEdge = new Rectangle(containerX, bottomY, containerWidth, BORDER_PROPORTION);
        if (bottomEdge.intersects(currentXY)) {
            return true;
        }

        //左右边缘的高度 -10*2 是为了不和上下边缘重合
        int verticalHeight = containerHeight - BORDER_PROPORTION * 2;
        int leftY = containerY + BORDER_PROPORTION;
        // 左边缘
        Rectangle leftEdge = new Rectangle(containerX, leftY, BORDER_PROPORTION, verticalHeight);
        if (leftEdge.intersects(currentXY)) {
            return true;
        }

        int rightY = containerY + BORDER_PROPORTION;
        int rightX = containerX + containerWidth - BORDER_PROPORTION;
        // 右边缘
        Rectangle rightEdge = new Rectangle(rightX, rightY, BORDER_PROPORTION, verticalHeight);
        return rightEdge.intersects(currentXY);
    }

    /**
     * 交叉点区域时，能否对应位置放入组件
     */
    protected boolean canAcceptWhileCrossPoint(Component comp, int x, int y) {
        return super.canAcceptWhileCrossPoint(comp, x, y);
    }

    protected boolean canAcceptWhileTrisection(Component comp, int x, int y) {
        return super.canAcceptWhileTrisection(comp, x, y);
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
        return super.isTrisectionArea(parentComp, x, y);
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
        return super.isCrossPointArea(currentComp, x, y);
    }


    protected Rectangle getLayoutBound(XWCardMainBorderLayout mainLayout) {
        return mainLayout.getBounds();
    }

    private Rectangle adjustBackupBound(Rectangle backupBound, XWCardMainBorderLayout mainLayout) {
        // zhouping: REPORT-2334 表单tab布局中图表放大缩小会明显
        // 这边不需要单独处理参数面板高度了，下面的方法中获取的是XWCardMainBorderLayout相对坐标
        Rectangle rec = getLayoutBound(mainLayout);
        // XWTabLayout里面的横纵坐标收到外层XWCardMainBorderLayout的横纵坐标影响
        // 减掉之后可以按照它原来的逻辑执行
        backupBound.x -= rec.x;
        backupBound.y -= rec.y;
        XWCardLayout cardLayout = mainLayout.getCardPart();
        LayoutBorderStyle style = cardLayout.toData().getBorderStyle();
        XWCardTitleLayout xwCardTitleLayout = mainLayout.getTitlePart();
        Dimension titleDimension = xwCardTitleLayout.getSize();

        // 当tab布局为标题样式时，才需要处理标题栏高度产生的影响
        if (ComparatorUtils.equals(style.getType(), LayoutBorderStyle.TITLE)) {
            WTabDisplayPosition wTabDisplayPosition = xwCardTitleLayout.getDisplayPosition();
            switch (wTabDisplayPosition){
                case TOP_POSITION:
                    backupBound.y -= titleDimension.height;
                    break;
                case LEFT_POSITION:
                    backupBound.x -= titleDimension.width;
                    break;
                default:
                    return backupBound;
            }
        }
        return backupBound;
    }

    /**
     * 拖拽控件边框后，根据控件的大小尺寸，进行相关组件的调整
     *
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
        if (!ComparatorUtils.equals(outerLayout, container.getBackupParent())) {
            XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) outerLayout;
            backupBound = adjustBackupBound(backupBound, mainLayout);
        }

        //拖拽组件拖拽以后的大小
        int w = creator.getWidth();
        int h = creator.getHeight();
        initCompsList();
        creator.setBounds(backupBound);
        int difference = 0;
        if (x != backupBound.x) {
            dealLeft(backupBound, x, posXs, difference, creator);
        } else if (w != backupBound.width) {
            dealRight(backupBound, x, w, posXs, difference, creator);
        } else if (y != backupBound.y) {
            dealTop(backupBound, y, posYs, difference, creator);
        } else if (h != backupBound.height) {
            dealButtom(backupBound, y, h, posYs, difference, creator);
        }
        clearCompsList();
        XWFitLayout layout = (XWFitLayout) container;
        layout.updateBoundsWidget(); // 更新对应的BoundsWidget
        updateCreatorBackBound();
    }

    /**
     * 拖拽控件边框后，根据控件的大小尺寸，进行相关组件的调整
     *
     * @param backupBound 边界备份
     * @param bounds      组件边界
     * @param xCreator    组件
     * @param row         选中的行
     * @param difference  偏移量
     */
    public void calculateBounds(Rectangle backupBound, Rectangle bounds, XCreator xCreator, int row, int difference) {
        Rectangle rc = new Rectangle(0, 0, 0, 0);
        XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(xCreator);
        if (parent != null) {
            Rectangle rec = ComponentUtils.getRelativeBounds(parent);
            rc.x = rec.x;
            rc.y = rec.y;
        }
        int x = backupBound.x - rc.x, y = backupBound.y - rc.y;
        //处理左右延伸
        switch (row) {
            case 0:
                if (backupBound.width + backupBound.x == container.getWidth() - margin.getRight() + rc.x) {
                    x += difference;
                }
                break;
            case 1:
                if (backupBound.y + backupBound.height == container.getHeight() - margin.getBottom() + rc.y) {
                    y += difference;
                }
                break;
        }
        bounds.setLocation(x, y);
        xCreator.setBackupBound(backupBound);
        xCreator.setBounds(bounds);
        this.fix(xCreator);
    }

    private void dealLeft(Rectangle backupBound, int x, int[] posXs, int difference, XCreator creator) {
        if (backupBound.x == margin.getLeft()) {
            return;
        }
        x = adjustCoordinateByDependingLine(x, posXs);
        difference = x - backupBound.x;
        dealDirectionAtLeft(backupBound, difference, creator);
    }

    private void dealRight(Rectangle backupBound, int x, int w, int[] posXs, int difference, XCreator creator) {
        if (backupBound.width + backupBound.x == container.getWidth() - margin.getRight()) {
            return;
        }
        w = adjustDiffByDependingLine(x, posXs, w);
        difference = w - backupBound.width; //拖拽长度
        dealDirectionAtRight(backupBound, difference, creator);
    }

    private void dealTop(Rectangle backupBound, int y, int[] posYs, int difference, XCreator creator) {
        if (backupBound.y == margin.getTop()) {
            return;
        }
        y = adjustCoordinateByDependingLine(y, posYs);
        difference = y - backupBound.y;
        dealDirectionAtTop(backupBound, difference, creator);
    }

    private void dealButtom(Rectangle backupBound, int y, int h, int[] posYs, int difference, XCreator creator) {
        if (backupBound.y + backupBound.height == container.getHeight() - margin.getBottom()) {
            return;
        }
        h = adjustDiffByDependingLine(y, posYs, h);
        difference = h - backupBound.height;
        dealDirectionABottom(backupBound, difference, creator);
    }

    // 根据需要依附的位置调整拖拽的坐标值
    private int adjustCoordinateByDependingLine(int coordinate, int[] coordinates) {
        if (!isEdit) {
            for (int i = 0; i < coordinates.length; i++) {
                if (coordinate == coordinates[i]) {
                    continue;
                }
                if (coordinate > coordinates[i] - DEPENDING_SCOPE && coordinate < coordinates[i] + DEPENDING_SCOPE) {
                    coordinate = coordinates[i];
                    break;
                }
            }
        }
        return coordinate;
    }

    // 根据需要依附的位置调整拖拽的距离
    private int adjustDiffByDependingLine(int coordinate, int[] coordinates, int diff) {
        if (!isEdit) {
            for (int i = 0; i < coordinates.length; i++) {
                if (coordinate + diff > coordinates[i] - DEPENDING_SCOPE && coordinate + diff < coordinates[i] + DEPENDING_SCOPE) {
                    diff = coordinates[i] - coordinate;
                    break;
                }
            }
        }
        return diff;
    }

    // 左侧边框拉伸，循环找出对齐的两侧控件
    private void dealDirectionAtLeft(Rectangle backupBound, int difference, Component creator) {
        rightComps.add(creator);
        Component rightComp = null;
        int leftx = backupBound.x - DEFAULT_AREA_LENGTH - actualVal;
        // 取左侧边框右面的组件x值
        int rightx = backupBound.x + DEFAULT_AREA_LENGTH;
        Component leftComp = container.getLeftComp(backupBound.x, backupBound.y);
        leftComps.add(leftComp);
        //先找上侧对齐时(y相等)的左右两边组件
        int ry = backupBound.y;
        int ly = leftComp.getY();
        int min = margin.getTop();
        int max = container.getHeight() - margin.getBottom();
        while (ry >= min && ly >= min) {
            if (ry == ly) {
                break;
            } else {
                if (ry > ly) {
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
        ry = backupBound.y + backupBound.height;
        ly = leftComps.get(0).getY() + leftComps.get(0).getHeight();
        while (ry <= max && ly <= max) {
            if (ry == ly) {
                break;
            } else {
                if (ry > ly) {
                    leftComp = container.getComponentAt(leftx, ly + DEFAULT_AREA_LENGTH + actualVal);
                    ly = leftComp.getY() + leftComp.getHeight();
                    leftComps.add(leftComp);
                } else {
                    rightComp = container.getComponentAt(rightx, ry + DEFAULT_AREA_LENGTH + actualVal);
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
        int leftx = backupBound.x + backupBound.width - DEFAULT_AREA_LENGTH;
        // 取右侧边框右面的组件x值
        int rightx = backupBound.x + backupBound.width + DEFAULT_AREA_LENGTH + actualVal;
        Component rightComp = container.getRightComp(backupBound.x, backupBound.y, backupBound.width);
        rightComps.add(rightComp);
        int ly = backupBound.y, ry = rightComp.getY();
        int min = margin.getTop();
        int max = container.getHeight() - margin.getBottom();
        while (ry >= min && ly >= min) {
            if (ry == ly) {
                break;
            } else {
                if (ry > ly) {
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
        while (ry <= max && ly <= max) {
            if (ry == ly) {
                break;
            } else {
                if (ry > ly) {
                    leftComp = container.getComponentAt(leftx, ly + DEFAULT_AREA_LENGTH + actualVal);
                    ly = leftComp.getY() + leftComp.getHeight();
                    leftComps.add(leftComp);
                } else {
                    rightComp = container.getComponentAt(rightx, ry + DEFAULT_AREA_LENGTH + actualVal);
                    ry = rightComp.getY() + rightComp.getHeight();
                    rightComps.add(rightComp);
                }
            }
        }
        dealHorDirection(backupBound.x + backupBound.width + actualVal, difference);
    }

    /**
     * 水平方向上拉伸边框的处理
     */
    private void dealHorDirection(int objx, int difference) {
        if (difference > 0) {
            difference = Math.min(getMinWidth(rightComps) - minWidth, difference);
        } else {
            difference = Math.max(difference, minWidth - getMinWidth(leftComps));
        }
        //重新计算左右两侧组件size、point
        if (calculateLefttRelatComponent(difference)) {
            calculateRightRelatComponent(objx + difference, -difference);
        }
    }

    // 上侧边框拉伸，循环找出对齐的两侧控件
    private void dealDirectionAtTop(Rectangle backupBound, int difference, Component creator) {
        downComps.add(creator);
        // 取上侧边框上面的组件用的y值
        int topy = backupBound.y - DEFAULT_AREA_LENGTH - actualVal;
        // 上侧边框下面的组件y值
        int bottomy = backupBound.y + DEFAULT_AREA_LENGTH;
        Component topComp = container.getTopComp(backupBound.x, backupBound.y);
        upComps.add(topComp);
        Component bottomComp = null;
        int min = margin.getLeft();
        int max = container.getWidth() - margin.getRight();
        //先找左侧侧对齐时(x相等)的上下两边组件
        int ux = topComp.getX();
        int dx = backupBound.x;
        while (ux >= min && dx >= min) {
            if (ux == dx) {
                break;
            } else {
                if (ux < dx) {
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
        ux = upComps.get(0).getX() + upComps.get(0).getWidth();
        dx = backupBound.x + backupBound.width;
        while (ux <= max && dx <= max) {
            if (ux == dx) {
                break;
            } else {
                if (ux < dx) {
                    topComp = container.getComponentAt(ux + DEFAULT_AREA_LENGTH + actualVal, topy);
                    ux = topComp.getX() + topComp.getWidth();
                    upComps.add(topComp);
                } else {
                    bottomComp = container.getComponentAt(dx + DEFAULT_AREA_LENGTH + actualVal, bottomy);
                    dx = bottomComp.getX() + bottomComp.getWidth();
                    downComps.add(bottomComp);
                }
            }
        }

        dealVertiDirection(backupBound.y, difference);
    }

    // 下侧边框拉伸，循环找出对齐的两侧控件
    private void dealDirectionABottom(Rectangle backupBound, int difference, Component creator) {
        upComps.add(creator);
        Component topComp = null;
        Component bottomComp = container.getBottomComp(backupBound.x, backupBound.y, backupBound.height);
        // 下侧边框下面的组件y坐标
        int bottomy = backupBound.y + backupBound.height + DEFAULT_AREA_LENGTH + actualVal;
        // 取下侧边框上面的组件用的y值
        int topy = backupBound.y + backupBound.height - DEFAULT_AREA_LENGTH;
        downComps.add(bottomComp);
        int dx = bottomComp.getX();
        int ux = backupBound.x;
        int min = margin.getLeft();
        int max = container.getWidth() - margin.getRight();
        while (ux >= min && dx >= min) {
            if (ux == dx) {
                break;
            } else {
                if (ux < dx) {
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
        dx = downComps.get(0).getX() + downComps.get(0).getWidth();
        ux = backupBound.x + backupBound.width;
        while (ux <= max && dx <= max) {
            if (ux == dx) {
                break;
            } else {
                if (ux < dx) {
                    topComp = container.getComponentAt(ux + DEFAULT_AREA_LENGTH + actualVal, topy);
                    ux = topComp.getX() + topComp.getWidth();
                    upComps.add(topComp);
                } else {
                    bottomComp = container.getComponentAt(dx + DEFAULT_AREA_LENGTH + actualVal, bottomy);
                    dx = bottomComp.getX() + bottomComp.getWidth();
                    downComps.add(bottomComp);
                }
            }
        }
        dealVertiDirection(backupBound.y + backupBound.height + actualVal, difference);
    }

    /**
     * 垂直方向上拉伸边框的处理
     */
    private void dealVertiDirection(int objY, int difference) {
        if (difference > 0) {
            difference = Math.min(getMinHeight(downComps) - minHeight, difference);
        } else {
            difference = Math.max(difference, minHeight - getMinHeight(upComps));
        }
        //重新计算上下两侧组件size、point
        if (calculateUpRelatComponent(difference)) {
            calculateDownRelatComponent(objY + difference, -difference);
        }
    }

    /**
     * 新拖入组件时，计算调整其他关联组件位置大小
     *
     * @param child 新拖入的组件
     * @param x     鼠标所在x坐标
     * @param y     鼠标所在y坐标
     */
    public void fix(XCreator child, int x, int y) {
        Component parentComp = container.getComponentAt(x, y);
        if (container.getComponentCount() == 0) {
            child.setLocation(0, 0);
            child.setSize(parentComp.getWidth(), parentComp.getHeight());
        } else if (isCrossPointArea(parentComp, x, y)) {
            //交叉区域插入组件时，根据具体位置进行上下或者左右或者相邻三个组件的位置大小插入
            fixCrossPointArea(parentComp, child, x, y);
            return;
        } else if (isTrisectionArea(parentComp, x, y)) {
            // 在边界三等分区域，就不再和组件二等分了
            fixTrisect(parentComp, child, x, y);
            return;
        } else {
            fixHalve(parentComp, child, x, y);
        }
    }

    /**
     * 平分，正常情况拖入组件时，按照上1/4区域、下1/4区域为上下平分，中左侧1/2区域、中右侧1/2区域为左右平分
     */
    protected void fixHalve(Component currentComp, XCreator child, int x, int y) {
        super.fixHalve(currentComp, child, x, y);
    }

    /**
     * 组件交叉区域进行插入时，调整受到变动的其他组件,之前是交叉区域插入也按照三等分逻辑，后面测试中发现有bug，改为和bi一样的鼠标所在侧平分
     * 默认左上角、右下角区域是垂直方向插入组件
     * 右上角和左下角是水平方向插入组件，这样避免田字块时重复
     */
    protected void fixCrossPointArea(Component currentComp, XCreator child, int x, int y) {
        super.fixCrossPointArea(currentComp, child, x, y);
    }

    /**
     * 三等分区域时，调整相关联的组件
     *
     * @param currentComp 鼠标所在组件
     * @param child       待插入组件
     */
    protected void fixTrisect(Component currentComp, XCreator child, int x, int y) {
        super.fixTrisect(currentComp, child, x, y);
    }

    /**
     * 删除组件或者重新拖动时，其它组件重新计算位置大小
     */
    protected void delete(XCreator creator, int creatorWidth, int creatorHeight) {
        int x = creator.getX();
        int y = creator.getY();
        recalculateChildrenSize(x, y, creatorWidth, creatorHeight, true);
    }

    /**
     * 重新计算内部组件大小
     *
     * @param x             坐标x
     * @param y             坐标y
     * @param creatorWidth  删除的组件之前所在布局的宽度
     * @param creatorHeight 删除的组件之前所在布局的高度
     */
    public void recalculateChildrenSize(int x, int y, int creatorWidth, int creatorHeight) {
        recalculateChildrenSize(x, y, creatorWidth, creatorHeight, false);
    }

    /**
     * 重新计算内部组件大小
     *
     * @param x             坐标x
     * @param y             坐标y
     * @param creatorWidth  删除的组件之前所在布局的宽度
     * @param creatorHeight 删除的组件之前所在布局的高度
     * @param isDel 删除操作
     */
    public void recalculateChildrenSize(int x, int y, int creatorWidth, int creatorHeight, boolean isDel) {
        if (container.getComponentCount() == 0) {
            return;
        } else {
            initCompsList();
            int width = creatorWidth;
            int height = creatorHeight;
            calculateRelatedComponent(x, y, width, height);
            if (!rightComps.isEmpty() && getAllHeight(rightComps) == height) {
                calculateRightRelatComponent(x, width + actualVal);
            } else if (!leftComps.isEmpty() && getAllHeight(leftComps) == height) {
                calculateLefttRelatComponent(width + actualVal, isDel);
            } else if (!downComps.isEmpty() && getAllWidth(downComps) == width) {
                calculateDownRelatComponent(y, height + actualVal);
            } else if (!upComps.isEmpty() && getAllWidth(upComps) == width) {
                calculateUpRelatComponent(height + actualVal, isDel);
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
        if (rightComp == null) {
            return;
        }

        int ry = rightComp.getY();
        clearCompsList();
        initCompsList();
        Rectangle rec = new Rectangle(x, y, width, height);
        if (ry != y) {
            calculateNoRelatedWhileRightTop(rec, rightComp);
        } else {
            calculateNoRelatedWhileRightBott(rec);
        }

    }

    private void calculateNoRelatedWhileRightTop(Rectangle bound, Component rcomp) {
        if (rcomp == null) {
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
                dealDirectionAtTop(rcomp.getBounds(), bound.y - rcomp.getY() - minHeight - actualVal, rcomp);
                ry = rcomp.getY();
                int rx = rcomp.getX();
                rcomp.setBounds(rx, ry, rw, minHeight);
                recalculateChildrenSize(rx, bound.y, rw, rh - dh - actualVal);
                recalculateChildrenSize(bound.x, bound.y, bound.width, bound.height);
                return;
            }
        } else {
            // 右侧控件底部对齐
            if (rh + ry == bound.y + bound.height) {
                rcomp.setSize(rw, dh);
                bound.width += rw;
                bound.width += actualVal;
            } else {
                recalculateChildrenSize(bound.x, ry + rh + actualVal, bound.width, bound.height + bound.y - rh - ry - actualVal);
                recalculateChildrenSize(bound.x, bound.y, bound.width, ry + rh - bound.y);
                return;
            }
        }
        recalculateChildrenSize(bound.x, bound.y, bound.width, bound.height, true);
    }

    private void calculateNoRelatedWhileRightBott(Rectangle bound) {
        Component rcomp = container.getBottomRightComp(bound.x, bound.y, bound.height, bound.width);
        if(rcomp == null){
            return;
        }
        int ry = rcomp.getY();
        int rh = rcomp.getHeight();
        int rw = rcomp.getWidth();
        int dh = ry + rh - bound.y - bound.height - actualVal;
        if (dh < minHeight) {
            dealDirectionABottom(rcomp.getBounds(), -dh - actualVal, rcomp);
            //调整的时候可能有组件达到最小高度，判断下
            if (rcomp.getHeight() + ry != bound.y + bound.height) {
                clearCompsList();
                initCompsList();
                dh = ry + rcomp.getHeight() - bound.y - bound.height - actualVal;
                dealDirectionABottom(rcomp.getBounds(), minHeight - dh, rcomp);
                rh = rcomp.getHeight();
                int rx = rcomp.getX();
                rcomp.setBounds(rx, bound.y + bound.height + actualVal, rw, minHeight);
                recalculateChildrenSize(rx, ry, rw, rh - minHeight - actualVal);
                recalculateChildrenSize(bound.x, bound.y, bound.width, bound.height);
                return;
            }
        } else {
            if (ry == bound.y) {
                rcomp.setBounds(rcomp.getX(), bound.y + bound.height + actualVal, rw, dh);
                bound.width += rw;
                bound.width += actualVal;
            } else {
                recalculateChildrenSize(bound.x, bound.y, bound.width, ry - bound.y - actualVal);
                recalculateChildrenSize(bound.x, ry, bound.width, bound.height - ry + bound.y);
                return;
            }
        }
        recalculateChildrenSize(bound.x, bound.y, bound.width, bound.height, true);
    }

    private int getMinWidth(List<Component> comps) {
        if (comps.isEmpty()) {
            return 0;
        }
        int minWidth = container.getWidth() - margin.getLeft() - margin.getRight();
        for (int i = 0, size = comps.size(); i < size; i++) {
            minWidth = minWidth > comps.get(i).getWidth() ? comps.get(i).getWidth() : minWidth;
        }
        return minWidth;
    }

    private int getMinHeight(List<Component> comps) {
        if (comps.isEmpty()) {
            return 0;
        }
        int minH = container.getHeight() - margin.getTop() - margin.getBottom();
        for (int i = 0, size = comps.size(); i < size; i++) {
            minH = minH > comps.get(i).getHeight() ? comps.get(i).getHeight() : minH;
        }
        return minH;
    }

    // 删除时计算待删除组件上下侧的组件是否何其对齐
    private int getAllHeight(List<Component> comps) {
        int allHeight = 0;
        if (comps.isEmpty()) {
            return allHeight;
        }
        int n = comps.size();
        for (int i = 0; i < n; i++) {
            allHeight += comps.get(i).getHeight();
        }
        allHeight += (n - 1) * actualVal;
        return allHeight;
    }

    private int getAllWidth(List<Component> comps) {
        int allWidth = 0;
        if (comps.isEmpty()) {
            return allWidth;
        }
        int n = comps.size();
        for (int i = 0; i < n; i++) {
            allWidth += comps.get(i).getWidth();
        }
        allWidth += (n - 1) * actualVal;
        return allWidth;
    }

    /**
     * 获取有哪些相关联的组件
     */
    protected void calculateRelatedComponent(int objX, int objY, int objWidth, int objHeight) {
        int count = container.getComponentCount();
        for (int i = 0; i < count; i++) {
            Component relatComp = container.getComponent(i);
            int rx = relatComp.getX();
            int ry = relatComp.getY();
            int rwidth = relatComp.getWidth();
            int rheight = relatComp.getHeight();
            int verti = ry - objY;
            int hori = rx - objX;
            boolean isHori = verti >= 0 && objHeight >= (rheight + verti);
            boolean isVerti = hori >= 0 && objWidth >= (rwidth + hori);
            if (isHori && (objX + objWidth + actualVal) == rx) {
                rightComps.add(relatComp);
            } else if (isHori && objX == (rx + rwidth + actualVal)) {
                leftComps.add(relatComp);
            } else if (isVerti && (objY + objHeight + actualVal) == ry) {
                downComps.add(relatComp);
            } else if (isVerti && objY == (ry + rheight + actualVal)) {
                upComps.add(relatComp);
            }
        }
    }

    /**
     * 拖拽组件时遍历某一侧组件得到该侧组件能够缩放的最小宽度，tab布局最小宽度  = 内部组件数 * 单个组件最小宽度
     *
     * @param list 某一侧组件的集合 如：leftComps<Component>
     * @return int 最小宽度
     */
    private int getCompsMinWidth(List<?> list) {
        return getMaxCompsNum(list, true) * WLayout.MIN_WIDTH;
    }

    /**
     * 拖拽组件遍历某一侧得到该侧组件能够缩放的最小高度，tab布局最小高度 = 内部组件数  * 单个组件最小高度  + 标题高度
     *
     * @param list 某一侧组件集合
     * @return int 最小高度
     */
    private int getCompsMinHeight(List<?> list) {
        for (int i = 0; i < list.size(); i++) {
            XCreator creator = (XCreator) list.get(i);
            ArrayList<?> childrenList = creator.getTargetChildrenList();
            if (!childrenList.isEmpty()) {
                return getMaxCompsNum(list, false) * WLayout.MIN_HEIGHT + WCardMainBorderLayout.TAB_HEIGHT;
            }
        }
        return WLayout.MIN_HEIGHT;
    }

    /**
     * 根据子组件的横(纵)坐标获取某一侧组件的最大内部组件数
     *
     * @param list  某一侧组件集合
     * @param isHor 是否以横坐标为准
     * @return int 最大内部组件数
     */
    private int getMaxCompsNum(List<?> list, boolean isHor) {
        int maxCompNums = 1;
        for (int i = 0, size = list.size(); i < size; i++) {
            XCreator creator = (XCreator) list.get(i);
            ArrayList<?> childrenList = creator.getTargetChildrenList();
            int count = childrenList.size();
            if (count > 0) {
                for (int j = 0; j < count; j++) {
                    XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList.get(j);
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
     *
     * @param creator tab布局
     * @param offset  偏移量
     * @param isHor   是否为横向拖拽
     */
    private void adjustCompsSize(XCreator creator, int offset, boolean isHor) {
        ArrayList<?> childrenList = creator.getTargetChildrenList();
        int size = childrenList.size();
        if (size > 0) {
            for (int j = 0; j < size; j++) {
                XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList.get(j);
                tabLayout.setBackupBound(tabLayout.getBounds());
                int refSize = isHor ? tabLayout.getWidth() : tabLayout.getHeight();
                double percent = (double) offset / refSize;
                if (percent < 0 && !tabLayout.canReduce(percent)) {
                    return;
                }
                setAdjustedSize(tabLayout, offset, isHor);
                for (int m = 0; m < tabLayout.getComponentCount(); m++) {
                    XCreator childCreator = tabLayout.getXCreator(m);
                    WAbsoluteLayout.BoundsWidget wgt = (WAbsoluteLayout.BoundsWidget) tabLayout.toData().getBoundsWidget(childCreator.toData());
                    wgt.setBounds(tabLayout.getComponent(m).getBounds());
                }
                adjustCreatorsSize(percent, tabLayout, isHor);
            }

        }
    }

    // 纵向拖拽，先将tab布局的高度设置为拖拽后的实际高度
    private void setAdjustedHeight(XWTabFitLayout tabLayout, int offset) {
        tabLayout.setSize(tabLayout.getWidth(), tabLayout.getHeight() + offset);
    }

    // 横向拖拽，先将tab布局的宽度设置为拖拽后的实际宽度
    private void setAdjustedSize(XWTabFitLayout tabLayout, int offset, boolean isHor) {
        if (offset < 0) {
            // 缩放时需要备份原tab布局宽高
            tabLayout.setReferDim(new Dimension(tabLayout.getWidth(), tabLayout.getHeight()));
        }
        if (isHor) {
            tabLayout.setSize(tabLayout.getWidth() + offset, tabLayout.getHeight());
            return;
        }
        setAdjustedHeight(tabLayout, offset);
    }

    // 按照拖拽偏移量，对tab布局进行缩放
    private void adjustCreatorsSize(double percent, XWTabFitLayout tabLayout, boolean isHor) {
        if (isHor) {
            tabLayout.adjustCreatorsWidth(percent);
            return;
        }
        tabLayout.adjustCreatorsHeight(percent);
    }


    /**
     * 删除或拉伸控件右边框 调整右侧组件位置大小
     *
     * @param objX     调整后的坐标x
     * @param objWidth 调整后的宽度
     */
    protected void calculateRightRelatComponent(int objX, int objWidth) {
        int count = rightComps.size();
        for (int i = 0; i < count; i++) {
            XCreator creator = (XCreator) rightComps.get(i);
            adjustCompsSize(creator, objWidth, true);
            int ry = creator.getY();
            int rwidth = creator.getWidth();
            int rheight = creator.getHeight();
            creator.setLocation(objX, ry);
            creator.setSize(rwidth + objWidth, rheight);
        }
    }

    /**
     * 实际拖拽偏移量是否超出了可调整的宽度范围
     *
     * @param offset 实际偏移量
     * @return boolean 是否超出调整范围
     */
    private boolean isBeyondAdjustWidthScope(int offset) {
        boolean isBeyondScope = false;
        isBeyondScope = offset < 0 ? isBeyondWidthScope(offset, leftComps) : isBeyondWidthScope(offset, rightComps);
        return isBeyondScope;
    }

    // 实际拖拽偏移量是否超出某一侧的可调整宽度
    private boolean isBeyondWidthScope(int offset, List<?> compsList) {
        int compMinWidth = getCompsMinWidth(compsList);
        for (int i = 0; i < compsList.size(); i++) {
            XCreator creator = (XCreator) compsList.get(i);
            if (Math.abs(offset) > (creator.getWidth() - compMinWidth)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除或拉伸控件左边框时 调整左侧的组件位置大小；
     */
    protected boolean calculateLefttRelatComponent(int objWidth) {
        return calculateLefttRelatComponent(objWidth, false);
    }

    protected boolean calculateLefttRelatComponent(int objWidth, boolean isDel) {
        if (!isDel && isBeyondAdjustWidthScope(objWidth)) {
            return false;
        }
        int count = leftComps.size();
        for (int i = 0; i < count; i++) {
            XCreator creator = (XCreator) leftComps.get(i);
            adjustCompsSize(creator, objWidth, true);
            int rwidth = creator.getWidth();
            int rheight = creator.getHeight();
            creator.setSize(rwidth + objWidth, rheight);
        }
        return true;
    }

    /**
     * 删除或拉伸下边框  调整下方的组件位置大小
     */
    protected void calculateDownRelatComponent(int objY, int objHeight) {
        int count = downComps.size();
        for (int i = 0; i < count; i++) {
            XCreator creator = (XCreator) downComps.get(i);
            adjustCompsSize(creator, objHeight, false);
            int rx = creator.getX();
            int rwidth = creator.getWidth();
            int rheight = creator.getHeight();
            creator.setLocation(rx, objY);
            creator.setSize(rwidth, rheight + objHeight);
        }
    }

    /**
     * 实际拖拽偏移量是否超出了可调整的高度范围
     *
     * @param offset 实际偏移量
     * @return boolean 是否超出调整范围
     */
    private boolean isBeyondAdjustHeightScope(int offset) {
        boolean isBeyondScope = false;
        isBeyondScope = offset < 0 ? isBeyondHeightScope(offset, upComps) : isBeyondHeightScope(offset, downComps);
        return isBeyondScope;
    }

    // 实际拖拽偏移量是否超出某一侧的可调整高度
    private boolean isBeyondHeightScope(int offset, List<?> compsList) {
        int minHeight = getCompsMinHeight(compsList);
        for (int i = 0; i < compsList.size(); i++) {
            XCreator creator = (XCreator) compsList.get(i);
            if (Math.abs(offset) > (creator.getHeight() - minHeight)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除或拉伸上边框    调整上方的组件位置大小
     */
    protected boolean calculateUpRelatComponent(int objHeight) {
        return calculateUpRelatComponent(objHeight, false);
    }

    protected boolean calculateUpRelatComponent(int objHeight, boolean isDel) {
        if (!isDel && isBeyondAdjustHeightScope(objHeight)) {
            return false;
        }
        int count = upComps.size();
        for (int i = 0; i < count; i++) {
            XCreator creator = (XCreator) upComps.get(i);
            adjustCompsSize(creator, objHeight, false);
            int rwidth = creator.getWidth();
            int rheight = creator.getHeight();
            creator.setSize(rwidth, rheight + objHeight);
        }
        return true;
    }

    /**
     * 不调整，只计算位置
     *
     * @return child的位置
     */
    public int[] getChildPosition(Component currentComp, XCreator child, int x, int y) {
        return super.getChildPosition(currentComp, child, x, y);
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
        return new FRFitLayoutConstraints((XWFitLayout) container, creator);
    }
}
