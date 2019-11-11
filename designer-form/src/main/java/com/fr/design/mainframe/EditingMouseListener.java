package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.beans.models.StateModel;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.creator.cardlayout.XCardSwitchButton;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.gui.xpane.ToolTipEditor;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.utils.gui.LayoutUtils;

import com.fr.share.ShareConstants;
import com.fr.stable.Constants;

import com.fr.stable.StringUtils;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * 普通模式下的鼠标点击、位置处理器
 */
public class EditingMouseListener extends MouseInputAdapter {

    private FormDesigner designer;

    /**
     * 普通模式下对应的model
     */
    private StateModel stateModel;


    private XLayoutContainer xTopLayoutContainer;
    private XLayoutContainer clickTopLayout;

    /**
     * 获取表单设计器
     *
     * @return 表单设计器
     */
    public FormDesigner getDesigner() {
        return designer;
    }

    /**
     * 选择模型，存储当前选择的组件和剪切板
     */
    private SelectionModel selectionModel;

    /**
     * 获取选择模型
     *
     * @return 选择
     */
    public SelectionModel getSelectionModel() {
        return selectionModel;
    }

    private XCreator lastXCreator;
    private MouseEvent lastPressEvent;
    private DesignerEditor<? extends JComponent> currentEditor;
    private XCreator currentXCreator;

    //备份开始拖动的位置和大小
    private Rectangle dragBackupBounds;

    /**
     * 获取最小移动距离
     *
     * @return 最小移动距离
     */
    public int getMinMoveSize() {
        return minMoveSize;
    }

    private int minDragSize = 5;
    private int minMoveSize = 8;

    private static final int EDIT_BTN_WIDTH = 60;
    private static final int EDIT_BTN_HEIGHT = 24;
    //报表块的编辑按钮不灵敏，范围扩大一点
    private static final int GAP = 10;

    private XElementCase xElementCase;
    private XChartEditor xChartEditor;

    private JWindow promptWindow = new JWindow();

    public EditingMouseListener(FormDesigner designer) {
        this.designer = designer;
        stateModel = designer.getStateModel();
        selectionModel = designer.getSelectionModel();
        UIButton promptButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Forbid_Drag_Into_Adapt_Pane"), BaseUtils.readIcon(IconPathConstants.FORBID_ICON_PATH));
        this.promptWindow.add(promptButton);
    }

    private void promptUser(int x, int y, XLayoutContainer container) {
        if (!selectionModel.getSelection().getSelectedCreator().canEnterIntoAdaptPane() && container.acceptType(XWFitLayout.class)) {
            promptWidgetForbidEnter(x, y, container);
        } else {
            cancelPromptWidgetForbidEnter();
        }
    }

    private void promptWidgetForbidEnter(int x, int y, XLayoutContainer container) {
        container.setBorder(BorderFactory.createLineBorder(Color.RED, Constants.LINE_MEDIUM));
        int screenX = (int) designer.getArea().getLocationOnScreen().getX();
        int screenY = (int) designer.getArea().getLocationOnScreen().getY();
        this.promptWindow.setSize(promptWindow.getPreferredSize());
        this.promptWindow.setPreferredSize(promptWindow.getPreferredSize());
        promptWindow.setLocation(screenX + x + GAP, screenY + y + GAP);
        promptWindow.setVisible(true);
    }

    private void cancelPromptWidgetForbidEnter() {
        designer.getRootComponent().setBorder(BorderFactory.createLineBorder(XCreatorConstants.LAYOUT_SEP_COLOR, Constants.LINE_THIN));
        promptWindow.setVisible(false);
    }


    /**
     * 按下
     *
     * @param e 鼠标事件
     */
    public void mousePressed(MouseEvent e) {
        if (!stopEditing()) {
            return;
        }
        if (!designer.isFocusOwner()) {
            // 获取焦点，以便获取热键
            designer.requestFocus();
        }
        if (e.getButton() == MouseEvent.BUTTON1) {

            Direction dir = selectionModel.getDirectionAt(e);
            if (!DesignerMode.isAuthorityEditing()) {
                stateModel.setDirection(dir);
            }

            if (dir == Location.outer) {
                if (designer.isDrawLineMode()) {
                    designer.updateDrawLineMode(e);
                } else {
                    if (selectionModel.hasSelectionComponent()
                            && selectionModel.getSelection().getRelativeBounds().contains(
                            designer.getArea().getHorizontalValue() + e.getX(),
                            designer.getArea().getVerticalValue() + e.getY())) {
                        lastPressEvent = e;
                        lastXCreator = selectionModel.getSelection().getSelectedCreator();
                    } else {
                        stateModel.startSelecting(e);
                    }
                }
            } else {
                stateModel.startResizing(e);
            }
        }
    }

    /**
     * 释放
     *
     * @param e 鼠标事件
     */
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            if (stateModel.isDragging()) {
                stateModel.draggingCancel();
            }
        } else {
            if (designer.isDrawLineMode()) {
                if (stateModel.prepareForDrawLining()) {
                    designer.getDrawLineHelper().setDrawLine(false);
                    designer.getDrawLineHelper().createDefalutLine();
                }
            } else if (stateModel.isSelecting()) {
                // 如果当前是区域选择状态，则选择该区域所含的组件
                designer.selectComponents(e);
            }
            if (stateModel.isDragging()) {
                mouseDraggingRelease(e);
            }
        }
        lastPressEvent = null;
        lastXCreator = null;
    }

    private void mouseDraggingRelease(MouseEvent e) {
        // 当前鼠标所在的组件
        XCreator hoveredComponent = designer.getComponentAt(e.getX(), e.getY());
        if (designer.isWidgetsIntersect() && dragBackupBounds != null && hoveredComponent != null) {
            XCreator selectionXCreator = designer.getSelectionModel().getSelection().getSelectedCreator();
            if (selectionXCreator != null) {
                selectionXCreator.setBounds(dragBackupBounds.x, dragBackupBounds.y, dragBackupBounds.width, dragBackupBounds.height);
            }
        }
        dragBackupBounds = null;
        // 拉伸时鼠标拖动过快，导致所在组件获取会为空
        if (hoveredComponent == null && e.getY() < 0) {
            // bug63538
            // 不是拖动过快导致的，而是纵坐标为负值导致的，这时参照横坐标为负值时的做法，取边界位置的组件，为鼠标所在点的组件
            // 如果直接return，界面上已经进行了拖拽不能恢复
            hoveredComponent = designer.getComponentAt(0, 0);
        }
        // 获取该组件所在的焦点容器
        XLayoutContainer container = XCreatorUtils.getHotspotContainer(hoveredComponent);

        if (container != null) {
            boolean formSubmit2Adapt = !selectionModel.getSelection().getSelectedCreator().canEnterIntoAdaptPane()
                    && container.acceptType(XWFitLayout.class);
            if (!formSubmit2Adapt) {
                // 如果是处于拖拽状态，则释放组件
                stateModel.releaseDragging(e);
            } else {
                selectionModel.deleteSelection();
                designer.setPainter(null);
            }
            cancelPromptWidgetForbidEnter();
        }
    }

    /**
     * TODO 激活上下文菜单，待完善
     * 6.56暂时不支持右键 bugid 8777
     */
    private void triggerPopup(MouseEvent e) {
        XCreator creator = selectionModel.getSelection().getSelectedCreator();
        if (creator == null) {
            return;
        }
        JPopupMenu popupMenu = null;
        ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, creator);
        popupMenu = adapter.getContextPopupMenu(e);

        if (popupMenu != null) {
            popupMenu.show(designer, e.getX(), e.getY());
        }
        // 通知组件已经被选择了
        designer.getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_SELECTED);
    }

    /**
     * 移动
     *
     * @param e 鼠标事件
     */
    public void mouseMoved(MouseEvent e) {
        XCreator component = designer.getComponentAt(e);

        setCoverPaneNotDisplay(component, e, false);

        if (processTopLayoutMouseMove(component, e)) {
            return;
        }
        if (component instanceof XEditorHolder) {
            XEditorHolder xcreator = (XEditorHolder) component;
            Rectangle rect = xcreator.getBounds();
            int min = rect.x + rect.width / 2 - minMoveSize;
            int max = rect.x + rect.width / 2 + minMoveSize;
            if (e.getX() > min && e.getX() < max) {
                if (designer.getCursor().getType() != Cursor.HAND_CURSOR) {
                    designer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
                return;
            } else {
                if (designer.getCursor().getType() == Cursor.HAND_CURSOR) {
                    designer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }
        Direction dir = selectionModel.getDirectionAt(e);
        if (designer.isDrawLineMode() && stateModel.getDirection() == Location.outer) {
            designer.updateDrawLineMode(e);
        }
        if (!DesignerMode.isAuthorityEditing()) {
            stateModel.setDirection(dir);
        }

        if (component.isReport()) {
            elementCaseMouseMoved(e, component);
            designer.repaint();
            return;
        }

        processChartEditorMouseMove(component, e);

        designer.repaint();
    }

    private void elementCaseMouseMoved(MouseEvent e, XCreator component) {
        xElementCase = (XElementCase) component;
        UIButton button = (UIButton) xElementCase.getCoverPane().getComponent(0);
        if (designer.getCursor().getType() == Cursor.HAND_CURSOR) {
            designer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } // component.getParent() 是报表块所在的XWTitleLayout
        int minX = button.getX() + getParentPositionX(component, 0) - designer.getArea().getHorizontalValue();
        int minY = button.getY() + getParentPositionY(component, 0) - designer.getArea().getVerticalValue();
        if (e.getX() + GAP - xElementCase.getInsets().left > minX && e.getX() - GAP - xElementCase.getInsets().left < minX + button.getWidth()) {
            if (e.getY() + GAP - xElementCase.getInsets().top > minY && e.getY() - GAP - xElementCase.getInsets().top < minY + button.getHeight()) {
                designer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }
        setHelpBtnFocus(e, xElementCase);
    }

    private void setHelpBtnFocus(MouseEvent e, XCreator component) {
        component.setHelpBtnOnFocus(false);
        if (component.getCoverPane() != null) {
            if (component.getCoverPane().getComponentCount() > 1) {
                JComponent button1 = (JComponent) component.getCoverPane().getComponent(1);
                int minX1 = button1.getX() + getParentPositionX(component, 0) - designer.getArea().getHorizontalValue();
                int minY1 = button1.getY() + getParentPositionY(component, 0) - designer.getArea().getVerticalValue();
                if (e.getX() + GAP - component.getInsets().left > minX1 && e.getX() - GAP - component.getInsets().left < minX1 + button1.getWidth()) {
                    if (e.getY() + GAP - component.getInsets().top > minY1 && e.getY() - GAP - component.getInsets().top < minY1 + button1.getHeight()) {
                        designer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        component.setHelpBtnOnFocus(true);
                    }
                }
            }
            component.displayCoverPane(true);
            component.setDirections(Direction.TOP_BOTTOM_LEFT_RIGHT);
        } else {
            //没有帮助信息时，不显示帮助图标
            if (StringUtils.isEmpty(component.toData().getDescription())) {
                return;
            }
            int minX1 = getParentPositionX(component, component.getX()) + component.getWidth() - ShareConstants.SHARE_EL_CONTROL_BUTTON_HW - designer.getArea().getHorizontalValue();
            int minY1 = getParentPositionY(component, component.getY()) - designer.getArea().getVerticalValue();
            if (e.getX() + GAP - component.getInsets().left > minX1 && e.getX() - GAP - component.getInsets().left < minX1 + ShareConstants.SHARE_EL_CONTROL_BUTTON_HW) {
                if (e.getY() + GAP - component.getInsets().top > minY1 && e.getY() - GAP - component.getInsets().top < minY1 + ShareConstants.SHARE_EL_CONTROL_BUTTON_HW) {
                    designer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    component.setHelpBtnOnFocus(true);
                }
            }
        }
    }

    private void setCoverPaneNotDisplay(XCreator component, MouseEvent e, boolean isLinkedHelpDialog) {
        if (xElementCase != null) {
            int x = getParentPositionX(xElementCase, 0) - designer.getArea().getHorizontalValue();
            int y = getParentPositionY(xElementCase, 0) - designer.getArea().getVerticalValue();
            Rectangle rect = new Rectangle(x, y, xElementCase.getWidth(), xElementCase.getHeight());
            if (rect.contains(e.getPoint())) {
                return;
            }

            xElementCase.displayCoverPane(false);
        }
        if (xChartEditor != null) {
            xChartEditor.displayCoverPane(false);
        }
        if (isLinkedHelpDialog) {
            component.destroyHelpDialog();
        }
        component.displayCoverPane(false);
        if (xTopLayoutContainer != null) {
            xTopLayoutContainer.setMouseEnter(false);
        }
        designer.repaint();
    }

    private boolean processTopLayoutMouseMove(XCreator component, MouseEvent e) {
        XLayoutContainer parent = XCreatorUtils.getHotspotContainer(component).getTopLayout();
        if (parent != null) {
            xTopLayoutContainer = parent;
            xTopLayoutContainer.setMouseEnter(true);
            designer.repaint();
            if (!xTopLayoutContainer.isEditable()) {
                if (designer.getCursor().getType() == Cursor.HAND_CURSOR) {
                    designer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                int minX = getParentPositionX(parent, parent.getX()) + parent.getWidth() / 2;
                int minY = getParentPositionY(parent, parent.getY()) + parent.getHeight() / 2;
                int offsetX = EDIT_BTN_WIDTH / 2 + GAP;
                int offsetY = EDIT_BTN_HEIGHT / 2 + GAP;
                if (e.getX() > (minX - offsetX) && e.getX() < (minX + offsetX)) {
                    if (e.getY() > (minY - offsetY) && e.getY() < (minY + offsetY + designer.getParaHeight())) {
                        designer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                }
                setHelpBtnFocus(e, xTopLayoutContainer);
                return true;
            }
        }
        return false;
    }

    private void processChartEditorMouseMove(XCreator component, MouseEvent e) {
        if (component instanceof XChartEditor) {
            xChartEditor = (XChartEditor) component;
            UIButton button = (UIButton) xChartEditor.getCoverPane().getComponent(0);
            if (designer.getCursor().getType() == Cursor.HAND_CURSOR) {
                designer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            int minX = button.getX() + getParentPositionX(component, 0) - designer.getArea().getHorizontalValue();
            int minY = button.getY() + getParentPositionY(component, 0) - designer.getArea().getVerticalValue();
            if (e.getX() + GAP > minX && e.getX() - GAP < minX + button.getWidth()) {
                if (e.getY() + GAP > minY && e.getY() - GAP < minY + button.getHeight()) {
                    designer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
            setHelpBtnFocus(e, xChartEditor);
            designer.repaint();
        }
    }

    private int getParentPositionX(XCreator comp, int x) {
        return comp.getParent() == null ?
                x : getParentPositionX((XCreator) comp.getParent(), comp.getParent().getX() + x);
    }

    private int getParentPositionY(XCreator comp, int y) {
        return comp.getParent() == null ?
                y : getParentPositionY((XCreator) comp.getParent(), comp.getParent().getY() + y);
    }

    /**
     * 拖拽
     *
     * @param e 鼠标事件
     */
    public void mouseDragged(MouseEvent e) {
        if (DesignerMode.isAuthorityEditing()) {
            return;
        }
        if ((e.isShiftDown() || InputEventBaseOnOS.isControlDown(e)) && !stateModel.isSelecting()) {
            stateModel.startSelecting(e);
        }
        // 如果当前是左键拖拽状态，拖拽组件
        if (stateModel.dragable()) {
            if (SwingUtilities.isRightMouseButton(e)) {
                return;
            } else {
                stateModel.dragging(e);
                // 获取e所在的焦点组件
                XCreator hotspot = designer.getComponentAt(e.getX(), e.getY());
                if (dragBackupBounds == null) {
                    XCreator selectingXCreator = designer.getSelectionModel().getSelection().getSelectedCreator();
                    if (selectingXCreator != null) {
                        dragBackupBounds = new Rectangle(selectingXCreator.getX(), selectingXCreator.getY(), selectingXCreator.getWidth(), selectingXCreator.getHeight());
                    }
                }
                // 拉伸时鼠标拖动过快，导致所在组件获取会为空
                if (hotspot == null) {
                    return;
                }
                // 获取焦点组件所在的焦点容器
                XLayoutContainer container = XCreatorUtils.getHotspotContainer(hotspot);
                //提示组件是否可以拖入
                promptUser(e.getX(), e.getY(), container);
            }
        } else if (designer.isDrawLineMode()) {
            if (stateModel.prepareForDrawLining()) {
                stateModel.drawLine(e);
            }
        } else if (stateModel.isSelecting() && (selectionModel.getHotspotBounds() != null)) {
            // 如果是拖拽选择区域状态，则更新选择区域
            stateModel.changeSelection(e);
        } else {
            if ((lastPressEvent == null) || (lastXCreator == null)) {
                return;
            }
            if (e.getPoint().distance(lastPressEvent.getPoint()) > minDragSize) {
                //参数面板和自适应布局不支持拖拽
                if (lastXCreator.isSupportDrag()) {
                    designer.startDraggingComponent(lastXCreator, lastPressEvent, e.getX(), e.getY());
                }
                e.consume();
                lastPressEvent = null;
            }
        }
        designer.repaint();
    }

    //当前编辑的组件是在布局中，鼠标点击布局外部，需要一次性将布局及其父布局都置为不可编辑
    private void setTopLayoutUnEditable(XLayoutContainer clickedTopLayout, XLayoutContainer clickingTopLayout) {
        //双击的前后点击click为相同对象，过滤掉
        if (clickedTopLayout == null || clickedTopLayout == clickingTopLayout) {
            return;
        }
        //位于同一层级的控件，父布局相同，过滤掉
        if (clickingTopLayout != null && clickedTopLayout.getParent() == clickingTopLayout.getParent()) {
            return;
        }
        //前后点击的位于不同层级，要置为不可编辑
        XLayoutContainer xLayoutContainer = (XLayoutContainer) clickedTopLayout.getParent();
        if (xLayoutContainer == clickingTopLayout) {
            return;
        }
        if (xLayoutContainer != null) {
            xLayoutContainer.setEditable(false);
            setTopLayoutUnEditable((XLayoutContainer) clickedTopLayout.getParent(), clickingTopLayout);
        }
    }

    private boolean isCreatorInLayout(XCreator creator, XCreator layout) {
        if (creator.equals(layout)) {
            return true;
        }
        if (layout.getParent() != null) {
            return isCreatorInLayout(creator, (XCreator) layout.getParent());
        }
        return false;
    }

    // 点击控件树，会触发此方法。如果在设计器中选中组件，则直接走 processTopLayoutMouseClick
    public void stopEditTopLayout(XCreator creator) {
        boolean isTabpaneSelected = creator instanceof XWCardLayout && creator.getParent().equals(clickTopLayout);
        if (clickTopLayout != null && (isTabpaneSelected || clickTopLayout.equals(creator))) {
            clickTopLayout.setEditable(false);
        }
        processTopLayoutMouseClick(creator);
    }

    public XCreator processTopLayoutMouseClick(XCreator creator) {
        XLayoutContainer topLayout = XCreatorUtils.getHotspotContainer(creator).getTopLayout();
        if (topLayout != null) {
            if (clickTopLayout != null && !clickTopLayout.equals(topLayout) && !isCreatorInLayout(clickTopLayout,
                    topLayout)) {
                clickTopLayout.setEditable(false);
                setTopLayoutUnEditable(clickTopLayout, topLayout);
            }
            clickTopLayout = topLayout;
            if (!topLayout.isEditable()) {
                creator = topLayout;
            }
        } else {
            if (clickTopLayout != null) {
                clickTopLayout.setEditable(false);
                setTopLayoutUnEditable(clickTopLayout, null);
            }
        }

        return creator;
    }

    /**
     * 点击
     *
     * @param e 鼠标事件
     */
    public void mouseClicked(MouseEvent e) {
        XCreator creator = designer.getComponentAt(e);
        boolean isValidButton = e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3;

        if (!isValidButton && !creator.acceptType(XCardSwitchButton.class)) {
            return;
        }

        creator = processTopLayoutMouseClick(creator);

        if (creator != null) {
            creator.respondClick(this, e);
            if (e.getButton() == MouseEvent.BUTTON3) {
                UIPopupMenu cellPopupMenu = creator.createPopupMenu(designer);
                if (cellPopupMenu != UIPopupMenu.EMPTY) {
                    GUICoreUtils.showPopupMenu(cellPopupMenu, designer, e.getX(), e.getY());
                }
            }
            creator.doLayout();
        }
        LayoutUtils.layoutRootContainer(designer.getRootComponent());
    }


    /**
     * 离开
     *
     * @param e 鼠标事件
     */
    public void mouseExited(MouseEvent e) {
        if (designer.getCursor().getType() != Cursor.DEFAULT_CURSOR && !(e.isShiftDown() || InputEventBaseOnOS.isControlDown(e))) {
            designer.setCursor(Cursor.getDefaultCursor());
        }
        cancelPromptWidgetForbidEnter();
    }

    /**
     * 开始编辑
     *
     * @param creator        容器
     * @param designerEditor 设计器
     * @param adapter        适配器
     */
    public void startEditing(XCreator creator, DesignerEditor<? extends JComponent> designerEditor, ComponentAdapter adapter) {
        if (designerEditor != null) {
            Rectangle rect = ComponentUtils.getRelativeBounds(creator);
            currentEditor = designerEditor;
            currentXCreator = creator;
            Rectangle bounds = new Rectangle(1, 1, creator.getWidth() - 2, creator.getHeight() - 2);
            bounds.x += (rect.x - designer.getArea().getHorizontalValue());
            bounds.y += (rect.y - designer.getArea().getVerticalValue());
            designerEditor.getEditorTarget().setBounds(bounds);
            designer.add(designerEditor.getEditorTarget());
            designer.invalidate();
            designer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            designerEditor.getEditorTarget().requestFocus();
            designer.repaint();
        }
    }

    /**
     * 停止编辑
     *
     * @return 是否编辑成功
     */
    public boolean stopEditing() {
        if (currentEditor != null) {
            designer.remove(currentEditor.getEditorTarget());
            currentEditor.fireEditStoped();

            Container container = currentXCreator.getParent();

            if (container != null) {
                LayoutUtils.layoutRootContainer(container);
            }
            designer.invalidate();
            designer.repaint();
            currentXCreator.stopEditing();
            currentXCreator = null;
            currentEditor = null;
            return true;
        }
        return true;
    }

    /**
     * 重置编辑控件大小
     */
    public void resetEditorComponentBounds() {
        if (currentEditor == null) {
            return;
        }

        if (currentXCreator.getParent() == null) {
            stopEditing();
            return;
        }

        Rectangle rect = ComponentUtils.getRelativeBounds(currentXCreator);
        Rectangle bounds = new Rectangle(1, 1, currentXCreator.getWidth() - 2, currentXCreator.getHeight() - 2);
        bounds.x += (rect.x - designer.getArea().getHorizontalValue());
        bounds.y += (rect.y - designer.getArea().getVerticalValue());
        if (currentXCreator instanceof XEditorHolder) {
            ToolTipEditor.getInstance().resetBounds((XEditorHolder) currentXCreator, bounds, currentEditor.getEditorTarget().getBounds());
        }
        currentEditor.getEditorTarget().setBounds(bounds);
    }
}
