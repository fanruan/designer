package com.fr.design.designer.beans.models;

import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.design.mainframe.FormSelectionUtils;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.stable.ArrayUtils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * 该model保存当前选择的组件和剪切版信息
 */
public class SelectionModel {
    //被粘贴组件在所选组件位置处往下、往右各错开20像素。执行多次粘贴时，在上一次粘贴的位置处错开20像素。
    private static final int DELTA_X_Y = 20; //粘贴时候的偏移距离
    private static final double OFFSET_RELATIVE = 0.80;
    private static FormSelection clipboard = new FormSelection();
    private FormDesigner designer;
    private FormSelection selection;
    private Rectangle hotspotBounds;

    public SelectionModel(FormDesigner designer) {
        this.designer = designer;
        selection = new FormSelection();
    }

    /**
     * 重置。清空formSelction以及选择区域
     */
    public void reset() {
        selection.reset();
        hotspotBounds = null;
    }

    /**
     * formSelction是否为空
     *
     * @return 是否为空
     */
    public static boolean isEmpty() {
        return clipboard.isEmpty();
    }

    /**
     * 鼠标点击一下，所选中的单个组件。按下Ctrl或者shift键时鼠标可以进行多选
     *
     * @param e 鼠标事件
     */
    public void selectACreatorAtMouseEvent(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3 || (!InputEventBaseOnOS.isControlDown(e) && !e.isShiftDown())) {
            // 如果Ctrl或者Shift键盘没有按下，则清除已经选择的组件
            selection.reset();
        } else {
            //按下Ctrl或者shift键时鼠标可以进行多选,两次点击同一控件就取消选中
            XCreator comp = designer.getComponentAt(e);
            XLayoutContainer topLayout = XCreatorUtils.getHotspotContainer(comp).getTopLayout();
            if (topLayout != null && !topLayout.isEditable()) {
                comp = topLayout;
            }
            XLayoutContainer container = XCreatorUtils.getParentXLayoutContainer(comp);
            for (XCreator selected : selection.getSelectedCreators()) {
                if (selected == comp || XCreatorUtils.getParentXLayoutContainer(selected) != container) {
                    selection.removeCreator(selected);
                }
            }
        }
        // 获取e所在的组件
        XCreator comp = designer.getComponentAt(e);
        selectACreator(comp);
    }

    public void selectACreator(XCreator comp) {
        //布局组件的顶层布局如不可编辑，要获取其顶层布局
        XLayoutContainer topLayout = XCreatorUtils.getHotspotContainer(comp).getTopLayout();
        if (topLayout != null && !topLayout.isEditable()) {
            comp = topLayout;
        }

        // 如果父层是scale和title两个专属容器，返回其父层，组件本身是不让被选中的
        if (comp != designer.getRootComponent() && comp != designer.getParaComponent()) {
            XCreator parentContainer = (XCreator) comp.getParent();
            if (parentContainer != null) {
                comp = parentContainer.isDedicateContainer() ? parentContainer : comp;
            }
        }
        if (selection.removeSelectedCreator(comp) || selection.addSelectedCreator(comp)) {
            designer.getEditListenerTable().fireCreatorModified(comp, DesignerEvent.CREATOR_SELECTED);
            designer.repaint();
        }
    }

    /**
     * 将所选组件剪切到剪切板上
     */
    public void cutSelectedCreator2ClipBoard() {
        if (hasSelectionComponent()) {
            selection.cut2ClipBoard(clipboard);
            designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_CUTED);
            designer.repaint();
        }
    }

    /**
     * 复制当前选中的组件到剪切板
     */
    public void copySelectedCreator2ClipBoard() {
        if (!selection.isEmpty()) {
            selection.copy2ClipBoard(clipboard);
        }
    }

    /**
     * 从剪切板粘帖组件
     *
     * @return 否
     */
    public boolean pasteFromClipBoard() {
        if (!clipboard.isEmpty()) {
            if (!hasSelectedPasteSource()) {
                //未选
                unselectedPaste();
            } else {
                //已选
                selectedPaste();
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
        return false;
    }

    public FormSelection getSelection() {
        return selection;
    }

    /**
     * 粘贴时未选择组件
     */
    private void unselectedPaste() {
        if (designer.getClass().equals(FormDesigner.class)) {
            if (selection.getSelectedCreator() instanceof XWFitLayout) {
                pasteXWFitLayout();
            } else {
                //绝对布局
                //编辑器外面还有两层容器，使用designer.getRootComponent()获取到的是编辑器中层的容器，不是编辑器表层
                //当前选择的就是编辑器表层
                FormSelectionUtils.paste2Container(designer, (XLayoutContainer) selection.getSelectedCreator(),
                        clipboard,
                        DELTA_X_Y,
                        DELTA_X_Y);
            }
        } else {
            //cpt本地组件复用，编辑器就一层，是最底层，使用designer.getRootComponent()就可以获取到
            //使用selection.getSelectedCreator()也应该是可以获取到的。
            FormSelectionUtils.paste2Container(designer, designer.getRootComponent(),
                    clipboard,
                    DELTA_X_Y,
                    DELTA_X_Y);
        }
    }

    private void pasteXWFitLayout() {
        if (selection.getSelectedCreator().getClass().equals(XWTabFitLayout.class)) {
            XLayoutContainer container = (XLayoutContainer) selection.getSelectedCreator();
            //tab布局编辑器内部左上角第一个坐标点
            int leftUpX = container.toData().getMargin().getLeft() + 1;
            int leftUpY = container.toData().getMargin().getTop() + 1;
            //选中第一个坐标点坐在的组件
            selection.setSelectedCreator((XCreator) container.getComponentAt(leftUpX, leftUpY));
            Rectangle rectangle = selection.getRelativeBounds();
            if (hasSelectedPasteSource()) {
                selectedPaste();
            } else {
                FormSelectionUtils.paste2Container(designer, container, clipboard,
                        rectangle.x + rectangle.width / 2,
                        rectangle.y + DELTA_X_Y);
            }
        } else {
            //自适应布局编辑器内部左上角第一个坐标点
            int leftUpX = designer.getRootComponent().toData().getMargin().getLeft() + 1;
            int leftUpY = designer.getRootComponent().toData().getMargin().getTop() + 1;
            //选中第一个坐标点坐在的组件
            selection.setSelectedCreator((XCreator) designer.getRootComponent().getComponentAt(leftUpX, leftUpY));
            Rectangle rectangle = selection.getRelativeBounds();
            if (hasSelectedPasteSource()) {
                selectedPaste();
            } else {
                FormSelectionUtils.paste2Container(designer, designer.getRootComponent(),
                        clipboard,
                        rectangle.x + rectangle.width / 2,
                        rectangle.y + DELTA_X_Y);
            }
        }
    }

    /**
     * 粘贴时选择组件
     */
    private void selectedPaste() {
        XLayoutContainer container = null;
        //获取到编辑器的表层容器（已选的组件的父容器就是表层容器）
        container = XCreatorUtils.getParentXLayoutContainer(selection.getSelectedCreator());
        if (container != null && selection.getSelectedCreator().getParent() instanceof XWFitLayout) {
            //自适应布局

            Rectangle selectionRec = selection.getRelativeBounds();
            //获取父容器位置，补充因参数面板高度导致的位置坐标计算偏移
            Rectangle containerRec = ComponentUtils.getRelativeBounds(container);
            int positionX, positionY;

            if (container.getClass().equals(XWTabFitLayout.class)) {
                //tab内部粘贴不补充高度偏移
                //且不计算参数面板造成的影响，因为在
                //@see com.fr.design.designer.beans.adapters.layout.FRTabFitLayoutAdapter#addBean中做了
                positionX = selectionRec.x + selectionRec.width / 2;
                positionY = (int) (selectionRec.y + selectionRec.height * OFFSET_RELATIVE);
            } else {
                //计算自适应布局位置
                positionX = selectionRec.x - containerRec.x + selectionRec.width / 2;
                positionY = (int) (selectionRec.y - containerRec.y + selectionRec.height * OFFSET_RELATIVE);
            }
            FormSelectionUtils.paste2Container(designer, container, clipboard, positionX, positionY);
        } else if (container != null && selection.getSelectedCreator().getParent() instanceof XWAbsoluteLayout) {
            //绝对布局
            Rectangle rec = selection.getSelctionBounds();
            FormSelectionUtils.paste2Container(designer, container, clipboard, rec.x + DELTA_X_Y, rec.y + DELTA_X_Y);
        }
    }

    /**
     * 删除当前所有选择的组件
     */
    public void deleteSelection() {
        XCreator[] roots = selection.getSelectedCreators();
        if (roots.length > 0) {
            boolean isInPara = true;  // 在参数面板内删除控件
            for (XCreator creator : roots) {
                if (isInPara && !(creator.getParent() instanceof XWParameterLayout)) {
                    isInPara = false;
                }
                if (creator.acceptType(XWParameterLayout.class)) {
                    designer.removeParaComponent();
                }
                removeCreatorFromContainer(creator, creator.getWidth(), creator.getHeight());
                creator.deleteRelatedComponent(creator, designer);
                creator.removeAll();
                // 清除被选中的组件
                selection.reset();
            }

            setSelectedCreator(isInPara ? designer.getParaComponent() : designer.getRootComponent());
            // 触发事件
            designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_DELETED);
            designer.repaint();
        }
    }

    /**
     * 从选择组件中删除某组件
     *
     * @param creator       组件
     * @param creatorWidth  组件之前宽度
     * @param creatorHeight 组件之前高度
     */
    public void removeCreator(XCreator creator, int creatorWidth, int creatorHeight) {
        selection.removeCreator(creator);
        removeCreatorFromContainer(creator, creatorWidth, creatorHeight);
        designer.repaint();
    }

    /**
     * 设置选择区域
     */
    public void setHotspotBounds(Rectangle rect) {
        hotspotBounds = rect;
    }

    /**
     * 获得当前选择区域
     */
    public Rectangle getHotspotBounds() {
        return hotspotBounds;
    }

    private void removeCreatorFromContainer(XCreator creator, int creatorWidth, int creatorHeight) {
        XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(creator);
        if (parent == null) {
            return;
        }
        boolean changeCreator = creator.shouldScaleCreator() || creator.hasTitleStyle();
        if (parent.acceptType(XWFitLayout.class) && changeCreator) {
            creator = (XCreator) creator.getParent();
        }
        parent.getLayoutAdapter().removeBean(creator, creatorWidth, creatorHeight);
        // 删除其根组件，同时就删除了同时被选择的叶子组件
        parent.remove(creator);
        LayoutManager layout = parent.getLayout();

        if (layout != null) {
            // 刷新组件容器的布局
            LayoutUtils.layoutContainer(parent);
        }
    }

    /**
     * 是否有组件被选择。如果所选组件是最底层容器，也视为无选择
     *
     * @return 是则返回true
     * yaoh.wu 不应该通过判断是否是最底层容器来判断是否选择了组件
     * 而是应该判断选择的容器是否是编辑器的最表层容器,也就是点击空白地方选择的容器
     * 但是直接判断选择的容器是否是编辑器最表层类型又会引发拖动时选不上的情况，
     * 因此通过判断父容器来实现
     * <p>
     * 举例：frm组件复用 绝对布局情况下，不选择时有三层容器：
     * 底层@see {@link com.fr.design.designer.creator.XWBorderLayout}
     * 中层@see {@link XWFitLayout}
     * 表层@see {@link com.fr.design.designer.creator.XWAbsoluteBodyLayout}
     * <p>
     * 但是编辑窗口的最外层其实是表层@see {@link com.fr.design.designer.creator.XWAbsoluteBodyLayout},
     * 其他两层不是靠添加组件就可以编辑的。
     */
    public boolean hasSelectedPasteSource() {
        XCreator selectionXCreator = selection.getSelectedCreator();
        if (designer.getClass().equals(FormDesigner.class)) {
            //frm本地组件复用
            if (selectionXCreator != null) {
                //选中的是否是tab布局编辑器本身
                boolean tabEditor = selectionXCreator.getClass().equals(XWCardMainBorderLayout.class)
                        || selectionXCreator.getClass().equals(XWCardLayout.class)
                        || selectionXCreator.getClass().equals(XWTabFitLayout.class);
                //选中的是否是frm绝对布局编辑器本身
                boolean absoluteEditor = selectionXCreator.getClass().equals(XWAbsoluteBodyLayout.class);
                //选中是否是frm绝对画布块编辑器本身
                boolean absoluteCanvas = selectionXCreator.getClass().equals(XWAbsoluteLayout.class);
                //选中的是否是相对布局编辑器本身
                boolean relativeEditor = selectionXCreator.getClass().equals(XWFitLayout.class);

                return !(tabEditor || absoluteEditor || absoluteCanvas || relativeEditor);
            } else {
                return false;
            }
        } else {
            //cpt本地组件复用,selection.getSelectedCreator().getParent()=@XWParameterLayout instanceof @XWAbsoluteLayout
            return selectionXCreator != null && selectionXCreator.getParent() != null;
        }
    }

    /**
     * 是否有组件被选择。如果所选组件是最底层容器，也视为无选择
     *
     * @return 是则返回true
     */
    public boolean hasSelectionComponent() {
        return !selection.isEmpty() && selection.getSelectedCreator().getParent() != null;
    }

    /**
     * 移动组件至指定位置
     *
     * @param x 坐标x
     * @param y 坐标y
     */
    public void move(int x, int y) {
        for (XCreator creator : selection.getSelectedCreators()) {
            creator.setLocation(creator.getX() + x, creator.getY() + y);
            LayoutAdapter layoutAdapter = AdapterBus.searchLayoutAdapter(designer, creator);
            if (layoutAdapter != null) {
                layoutAdapter.fix(creator);
            }
        }
        designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(),
                DesignerEvent.CREATOR_SELECTED);
    }

    /**
     * 释放捕获
     */
    public void releaseDragging() {
        designer.setPainter(null);
        selection.fixCreator(designer);
        designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(),
                DesignerEvent.CREATOR_RESIZED);
    }

    public Direction getDirectionAt(MouseEvent e) {
        Direction dir;
        if (InputEventBaseOnOS.isControlDown(e) || e.isShiftDown()) {
            XCreator creator = designer.getComponentAt(e.getX(), e.getY(), selection.getSelectedCreators());
            if (creator != designer.getRootComponent() && selection.addedable(creator)) {
                return Location.add;
            }
        }
        if (hasSelectionComponent()) {
            int x = e.getX() + designer.getArea().getHorizontalValue();
            int y = e.getY() + designer.getArea().getVerticalValue();
            dir = getDirection(selection.getRelativeBounds(), x, y);
            if (selection.size() == 1 && !ArrayUtils.contains(selection.getSelectedCreator().getDirections(), dir
                    .getActual())) {
                dir = Location.outer;
            }
        } else {
            dir = Location.outer;
        }

        if (designer.getDesignerMode().isFormParameterEditor() && dir == Location.outer) {
            dir = designer.getLoc2Root(e);
        }
        return dir;
    }

    private Direction getDirection(Rectangle bounds, int x, int y) {
        if (x < (bounds.x - XCreatorConstants.RESIZE_BOX_SIZ)) {
            return Location.outer;
        } else if ((x >= (bounds.x - XCreatorConstants.RESIZE_BOX_SIZ)) && (x <= bounds.x)) {
            return getDirectionLeft(bounds, y);
        } else if ((x > bounds.x) && (x < (bounds.x + bounds.width))) {
            return getDirectionCenter(bounds, y);
        } else if ((x >= (bounds.x + bounds.width))
                && (x <= (bounds.x + bounds.width + XCreatorConstants.RESIZE_BOX_SIZ))) {
            return getDirectionRight(bounds, y);
        } else {
            return Location.outer;
        }
    }

    private Direction getDirectionRight(Rectangle bounds, int y) {
        if (y < (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) {
            return Location.outer;
        } else if ((y >= (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) && (y <= bounds.y)) {
            return Location.right_top;
        } else if ((y > bounds.y) && (y < (bounds.y + bounds.height))) {
            return Location.right;
        } else if ((y >= (bounds.y + bounds.height))
                && (y <= (bounds.y + bounds.height + XCreatorConstants.RESIZE_BOX_SIZ))) {
            return Location.right_bottom;
        } else {
            return Location.outer;
        }
    }

    private Direction getDirectionCenter(Rectangle bounds, int y) {
        if (y < (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) {
            return Location.outer;
        } else if ((y >= (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) && (y <= bounds.y)) {
            return Location.top;
        } else if ((y > bounds.y) && (y < (bounds.y + bounds.height))) {
            return Location.inner;
        } else if ((y >= (bounds.y + bounds.height))
                && (y <= (bounds.y + bounds.height + XCreatorConstants.RESIZE_BOX_SIZ))) {
            return Location.bottom;
        } else {
            return Location.outer;
        }
    }

    private Direction getDirectionLeft(Rectangle bounds, int y) {
        if (y < (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) {
            return Location.outer;
        } else if ((y >= (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) && (y <= bounds.y)) {
            return Location.left_top;
        } else if ((y > bounds.y) && (y < (bounds.y + bounds.height))) {
            return Location.left;
        } else if ((y >= (bounds.y + bounds.height))
                && (y <= (bounds.y + bounds.height + XCreatorConstants.RESIZE_BOX_SIZ))) {
            return Location.left_bottom;
        } else {
            return Location.outer;
        }
    }

    private void fireCreatorSelected() {
        designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(),
                DesignerEvent.CREATOR_SELECTED);
    }

    public void setSelectedCreator(XCreator rootComponent) {
        selection.setSelectedCreator(rootComponent);
        fireCreatorSelected();
    }

    public void setSelectedCreators(ArrayList<XCreator> rebuildSelection) {
        selection.setSelectedCreators(rebuildSelection);
        fireCreatorSelected();
    }
}
