package com.fr.design.designer.beans.models;

import com.fr.design.beans.location.Absorptionline;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelectionUtils;
import com.fr.design.utils.ComponentUtils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * 普通模式下的状态model
 */
public class StateModel {
    // 对应的selection model

    private SelectionModel selectionModel;
    // 当前鼠标进入拖拽区域的位置类型
    private Direction driection;

    // 当前拖拽的起始位置
    private int currentX;
    private int currentY;

    //拖拽组件原始位置大小备份
    private Rectangle selectedPositionBackup;

    private Point startPoint = new Point();
    private Point currentPoint = new Point();

    private Absorptionline lineInX;
    private Absorptionline lineInY;
    //等距线
    private Absorptionline lineEquidistant;

    // 当前是否处于拖拽选择状态
    private boolean selecting;
    private boolean dragging;

    private boolean addable;

    private FormDesigner designer;

    public StateModel(FormDesigner designer) {
        this.designer = designer;
        selectionModel = designer.getSelectionModel();
    }

    /**
     * 返回direction
     *
     * @return direction方向
     */
    public Direction getDirection() {
        return driection;
    }

    /**
     * 是否有组件正被选中
     *
     * @return true 如果至少一个组件被选中
     */
    public boolean isSelecting() {
        return selecting;
    }

    /**
     * 是否能拖拽
     *
     * @return 非outer且选中为空
     */
    public boolean dragable() {
        return ((driection != Location.outer) && !selecting);
    }

    /**
     * 拖拽中是否可以转换为添加模式：
     * 如果拖拽组件只有一个，鼠标当前所在位置的最底层表单容器与这个组件的容器不同；
     * 如果拖拽组件为多个，鼠标当前所在位置的最底层表单容器除了要求要跟这些组件的容器不同外，还必须是绝对定位布局
     */
    private void checkAddable(MouseEvent e) {
        addable = false;
        designer.setPainter(null);

        if (driection != Location.inner) {
            return;
        }

        XCreator comp = designer.getComponentAt(e.getX(), e.getY(), selectionModel.getSelection().getSelectedCreators());
        XLayoutContainer container = XCreatorUtils.getHotspotContainer(comp);
        XCreator creator = selectionModel.getSelection().getSelectedCreator();
        Component creatorContainer = XCreatorUtils.getParentXLayoutContainer(creator);
        if (creatorContainer != null && creatorContainer != container
                && (selectionModel.getSelection().size() == 1 || container instanceof XWAbsoluteLayout)) {
            HoverPainter painter = AdapterBus.getContainerPainter(designer, container);
            designer.setPainter(painter);
            if (painter != null) {
                Rectangle rect = ComponentUtils.getRelativeBounds(container);
                rect.x -= designer.getArea().getHorizontalValue();
                rect.y -= designer.getArea().getVerticalValue();
                painter.setRenderingBounds(rect);
                painter.setHotspot(new Point(e.getX(), e.getY()));
                painter.setCreator(creator);
            }
            addable = true;
        }
    }

    /**
     * @param container 容器
     * @param mouseX    鼠标释放位置X
     * @param mouseY    鼠标释放位置Y
     * @return 是否成功
     */
    private boolean addBean(XLayoutContainer container, int mouseX, int mouseY) {
        LayoutAdapter adapter = container.getLayoutAdapter();
        Rectangle rectangleContainer = ComponentUtils.getRelativeBounds(container);
        if (selectionModel.getSelection().size() == 1) {
            return adapter.addBean(selectionModel.getSelection().getSelectedCreator(),
                    mouseX + designer.getArea().getHorizontalValue() - rectangleContainer.x,
                    mouseY + designer.getArea().getVerticalValue() - rectangleContainer.y);
        }
        for (XCreator creator : selectionModel.getSelection().getSelectedCreators()) {
            adapter.addBean(creator,
                    mouseX + designer.getArea().getHorizontalValue() - rectangleContainer.x,
                    mouseY + designer.getArea().getVerticalValue() - rectangleContainer.y);
        }
        return true;
    }

    /**
     * @param mouseReleasedX 鼠标释放位置X
     * @param mouseReleasedY 鼠标释放位置Y
     */
    private void adding(int mouseReleasedX, int mouseReleasedY) {
        // 当前鼠标所在的组件
        XCreator hoveredComponent = designer.getComponentAt(mouseReleasedX, mouseReleasedY, selectionModel.getSelection().getSelectedCreators());

        // 获取该组件所在的焦点容器
        XLayoutContainer container = XCreatorUtils.getHotspotContainer(hoveredComponent);

        boolean success = false;

        if (container != null) {
            // 如果是容器，则调用其acceptComponent接受组件
            success = addBean(container, mouseReleasedX, mouseReleasedY);
        }

        if (success) {
            FormSelectionUtils.rebuildSelection(designer);
            designer.getEditListenerTable().fireCreatorModified(
                    selectionModel.getSelection().getSelectedCreator(), DesignerEvent.CREATOR_ADDED);
        } else {
            selectionModel.getSelection().setSelectionBounds(selectedPositionBackup, designer);
            Toolkit.getDefaultToolkit().beep();
        }
        // 取消提示
        designer.setPainter(null);
    }

    /**
     * 是否拖拽
     *
     * @return dragging状态
     */
    public boolean isDragging() {
        return dragging;
    }

    /**
     * 是否可以开始画线
     *
     * @return startPoint不为空返回true
     */
    public boolean prepareForDrawLining() {
        return startPoint != null;
    }

    /**
     * 设置开始位置
     *
     * @param p point位置
     */
    public void setStartPoint(Point p) {
        this.startPoint = p;
    }

    /**
     * 返回开始位置
     *
     * @return 点位置
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * 返回当前点位置
     *
     * @return 点位置
     */
    public Point getEndPoint() {
        return currentPoint;
    }

    /**
     * 当前选中组件
     *
     * @param e 鼠标事件
     */
    public void startSelecting(MouseEvent e) {
        selecting = true;
        selectionModel.setHotspotBounds(new Rectangle());
        currentX = getMouseXY(e).x;
        currentY = getMouseXY(e).y;
    }

    /**
     * 当前鼠标的xy
     *
     * @param e 鼠标事件
     */
    public void startResizing(MouseEvent e) {
        if (!selectionModel.getSelection().isEmpty()) {
            driection.backupBounds(designer);
        }
        currentX = getMouseXY(e).x;
        currentY = getMouseXY(e).y;
    }

    /**
     * 起始点开始DrawLine
     *
     * @param p 点位置
     */
    public void startDrawLine(Point p) {
        this.startPoint = p;
        if (p != null) {
            try {
                designer.setCursor(XConnector.connectorCursor);
            } catch (Exception e) {
            }
        } else {
            designer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * 鼠标释放时所在的区域及圈中的组件
     *
     * @param e 鼠标事件
     */
    public void selectCreators(MouseEvent e) {
        int x = getMouseXY(e).x;
        int y = getMouseXY(e).y;

        Rectangle bounds = createCurrentBounds(x, y);

        if ((x != currentX) || (y != currentY)) {
            ArrayList<XCreator> creators = getHotspotCreators(bounds, designer.getRootComponent());
            if (creators.isEmpty() && designer.getRootComponent().getXCreatorCount() > 0) {
                //当初始化布局是绝对布局的时候特殊处理，画框的时候若没有控件，默认焦点设置在绝对布局上
                XCreator layout = designer.getRootComponent().getXCreator(0);
                if (layout.acceptType(XWAbsoluteBodyLayout.class)) {
                    creators.add(layout);
                }
            }
            selectionModel.setSelectedCreators(creators);
        }
        selectionModel.setHotspotBounds(null);
    }

    /**
     * 画所在区域线
     *
     * @param e 鼠标事件
     */
    public void drawLine(MouseEvent e) {
        designer.getDrawLineHelper().setDrawLine(true);
        Point p = designer.getDrawLineHelper().getNearWidgetPoint(e);
        if (p != null) {
            currentPoint = p;
        } else {
            currentPoint.x = e.getX() + designer.getArea().getHorizontalValue();
            currentPoint.y = e.getY() + designer.getArea().getVerticalValue();
        }
    }

    private Rectangle createCurrentBounds(int x, int y) {
        Rectangle bounds = new Rectangle();

        bounds.x = Math.min(x, currentX);
        bounds.y = Math.min(y, currentY);
        bounds.width = Math.max(x, currentX) - bounds.x;
        bounds.height = Math.max(y, currentY) - bounds.y;

        return bounds;
    }

    private ArrayList<XCreator> getHotspotCreators(Rectangle selection, XCreator root) {
        ArrayList<XCreator> creators = new ArrayList<>();

        if (!root.isVisible() && !designer.isRoot(root)) {
            return creators;
        }

        if (root instanceof XLayoutContainer) {
            XLayoutContainer container = (XLayoutContainer) root;
            int count = container.getXCreatorCount();
            Rectangle clipped = new Rectangle(selection);

            for (int i = count - 1; i >= 0; i--) {
                XCreator child = container.getXCreator(i);

                if (selection.contains(child.getBounds())) {
                    creators.add(child);
                } else {
                    clipped.x = selection.x - child.getX();
                    clipped.y = selection.y - child.getY();
                    creators.addAll(getHotspotCreators(clipped, child));
                }
            }
        }

        return creators;
    }


    /**
     * 重置model
     */
    public void resetModel() {
        dragging = false;
        selecting = false;
    }

    /**
     * 重置
     */
    public void reset() {
        driection = Location.outer;
        dragging = false;
        selecting = false;
    }

    /**
     * 取消拖拽
     */
    public void draggingCancel() {
        designer.repaint();
        reset();
    }

    /**
     * 设置可拉伸方向
     *
     * @param dir 拉伸方向
     */
    public void setDirection(Direction dir) {
        if (driection != dir) {
            this.driection = dir;
            driection.updateCursor(designer);
        }
    }

    /**
     * x吸附线赋值
     *
     * @param line 线
     */
    public void setXAbsorptionline(Absorptionline line) {
        this.lineInX = line;
    }

    /**
     * y吸附线赋值
     *
     * @param line 线
     */
    public void setYAbsorptionline(Absorptionline line) {
        this.lineInY = line;
    }

    /**
     * 等距线赋值
     *
     * @param line 线
     */
    public void setEquidistantLine(Absorptionline line) {
        this.lineEquidistant = line;
    }

    /**
     * 画吸附线
     *
     * @param g Graphics类
     */
    public void paintAbsorptionline(Graphics g) {
        if (lineInX != null) {
            lineInX.paint(g, designer.getArea());
        }
        if (lineInY != null) {
            lineInY.paint(g, designer.getArea());
        }
        if (lineEquidistant != null) {
            lineEquidistant.paint(g, designer.getArea());
        }
    }

    /**
     * 拖拽
     *
     * @param e 鼠标事件
     */
    public void dragging(MouseEvent e) {
        //进入dragging状态时备份组件大小和位置
        if (!dragging) {
            selectedPositionBackup = selectionModel.getSelection().getRelativeBounds();
        }
        checkAddable(e);
        setDependLinePainter(e);
        driection.drag(getMouseXY(e).x - currentX, getMouseXY(e).y - currentY, designer);
        this.dragging = true;
    }

    // 拖拽时画依附线用到的painter
    private void setDependLinePainter(MouseEvent e) {
        XCreator comp = designer.getComponentAt(e.getX(), e.getY(), selectionModel.getSelection().getSelectedCreators());
        if(comp ==null){
            return;
        }
        XLayoutContainer container = XCreatorUtils.getHotspotContainer(comp);
        XCreator creator = selectionModel.getSelection().getSelectedCreator();
        HoverPainter painter = AdapterBus.getContainerPainter(designer, container);
        designer.setPainter(painter);
        if (painter != null) {
            painter.setHotspot(new Point(e.getX(), e.getY()));
            painter.setCreator(creator);
        }
    }

    /**
     * 释放捕获
     *
     * @param e 鼠标事件
     */
    public void releaseDragging(MouseEvent e) {
        this.dragging = false;
        if (addable) {
            adding(e.getX(), e.getY());
        } else if (!selectionModel.getSelection().isEmpty()) {
            selectionModel.releaseDragging();
        }
        designer.repaint();
    }

    /**
     * 改变选择区域
     *
     * @param e 鼠标事件
     */
    public void changeSelection(MouseEvent e) {
        Rectangle bounds = createCurrentBounds(getMouseXY(e).x, getMouseXY(e).y);
        selectionModel.setHotspotBounds(bounds);
    }

    /**
     * 返回鼠标所在的x、y  考虑滚动条的值
     *
     * @param e 鼠标事件
     * @return xy值
     */
    public Point getMouseXY(MouseEvent e) {
        Point p1 = new Point(e.getX() + designer.getArea().getHorizontalValue(), e.getY()
                + designer.getArea().getVerticalValue());
        return p1;
    }

}