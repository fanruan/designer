package com.fr.design.mainframe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.adapters.layout.AbstractLayoutAdapter;
import com.fr.form.ui.container.WLayout;
import com.fr.general.ComparatorUtils;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.form.ui.Widget;

public class FormSelectionUtils {

    //组件复制时坐标偏移
    private static final int DELAY_X = 20;
    private static final int DELAY_Y = 20;

    //组件复制时是否已经向左上偏移
    private static boolean BACK_OFFSET = false;

    //组件重命名后缀
    private static final String postfix = "_c";

    /**
     * @param designer  编辑器
     * @param parent    粘贴依据的组件
     * @param clipBoard 剪贴板内容
     * @param x         x
     * @param y         y
     */
    public static void paste2Container(FormDesigner designer, XLayoutContainer parent, FormSelection clipBoard, int x,
                                       int y) {
        LayoutAdapter adapter = parent.getLayoutAdapter();
        if (parent instanceof XWAbsoluteLayout) {
            designer.getSelectionModel().getSelection().reset();
            Rectangle rec = clipBoard.getSelctionBounds();
            for (XCreator creator : clipBoard.getSelectedCreators()) {
                try {
                    Widget copied = copyWidget(designer, creator.toData());
                    XCreator copiedCreator = XCreatorUtils.createXCreator(copied, creator.getSize());
                    // 获取位置
                    Point point = getPasteLocation((AbstractLayoutAdapter) adapter,
                            copiedCreator,
                            x + creator.getX() - rec.x + copiedCreator.getWidth() / 2,
                            y + creator.getY() - rec.y + copiedCreator.getHeight() / 2);
                    boolean addSuccess = adapter.addBean(copiedCreator, point.x, point.y);

                    if (addSuccess) {
                        designer.getSelectionModel().getSelection().addSelectedCreator(copiedCreator);
                    }

                } catch (CloneNotSupportedException e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            }
            rebuildSelection(designer);
            designer.getEditListenerTable().fireCreatorModified(
                    designer.getSelectionModel().getSelection().getSelectedCreator(), DesignerEvent.CREATOR_PASTED);
            return;
        }
        Toolkit.getDefaultToolkit().beep();
    }

    /**
     * 组件复用绝对布局获取粘贴组件位置
     *
     * @param layoutAdapter 绝对布局容器AbstractLayoutAdapter
     * @param copiedCreator 复制的组件
     * @param x             x=组件x + clonedCreator.getWidth() / 2
     * @param y             y=组件y + clonedCreator.getHeight() / 2
     *                      除2的步骤会导致当宽度或者高度为奇数是，中心点向左上各偏移一个像素
     *                      由于中心点向左上各偏移一个像素，依赖中心点计算的右下点就会相应的想做上偏移一个像素，导致结果不准确
     * @return 新位置坐标
     */
    private static Point getPasteLocation(AbstractLayoutAdapter layoutAdapter, XCreator copiedCreator, int x, int y) {
        //当宽度为奇数时 设置偏移
        int x_offset = (copiedCreator.getWidth() & 1) == 1 ? 1 : 0;
        //当高度为奇数时 设置偏移
        int y_offset = (copiedCreator.getHeight() & 1) == 1 ? 1 : 0;

        if (!layoutAdapter.accept(copiedCreator, x, y)) {
            XLayoutContainer container = layoutAdapter.getContainer();
            boolean xOut = x < 0 || x + copiedCreator.getWidth() / 2 + x_offset > container.getWidth();
            boolean yOut = y < 0 || y + copiedCreator.getHeight() / 2 + y_offset > container.getHeight();
            /*
             * 组件原始位置位于布局的右下角，
             * 和布局右下边界线紧挨，
             * 粘贴时组件在原始位置向左错开20像素。
             * x,y同时越界
             */
            if (xOut && yOut) {
                x = BACK_OFFSET ? container.getWidth() - copiedCreator.getWidth() / 2 - x_offset
                        : container.getWidth() - copiedCreator.getWidth() / 2 - DELAY_X - x_offset;
                y = BACK_OFFSET ?
                        container.getHeight() - copiedCreator.getHeight() / 2 - y_offset
                        : container.getHeight() - copiedCreator.getHeight() / 2 - DELAY_Y - y_offset;
                BACK_OFFSET = !BACK_OFFSET;
                return new Point(x, y);
            }
            /*
            * 组件原始位置与布局边界距离小于20像素（下边界&右边界同时小于或者任意一个边界小于），
            * 则粘贴时距离小于20像素一侧直接贴近布局边界，
            * 距离大于20像素的一侧正常错开。
            * x,y中只有一个越界
            */
            else if ((xOut || yOut)) {
                x = xOut ? container.getWidth() - copiedCreator.getWidth() / 2 - x_offset : x;
                y = yOut ? container.getHeight() - copiedCreator.getHeight() / 2 - y_offset : y;
                return new Point(x, y);
            }
        }
        return new Point(x, y);
    }


    private static Widget copyWidget(FormDesigner formDesigner, Widget widget) throws
            CloneNotSupportedException {
        ArrayList<String> clonedNameList = new ArrayList<String>();
        Widget copied = (Widget) widget.clone();
        //重命名拷贝的组件
        setCopiedName(formDesigner, copied, clonedNameList);
        if (copied instanceof WLayout) {
            for (int i = 0; i < ((WLayout) copied).getWidgetCount(); i++) {
                setCopiedName(formDesigner, ((WLayout) copied).getWidget(i), clonedNameList);
            }
        }
        return copied;
    }

    /**
     * 组件拷贝命名规则
     *
     * @param formDesigner
     * @param copied
     * @param clonedNameList
     */
    private static void setCopiedName(FormDesigner formDesigner, Widget copied, ArrayList<String> clonedNameList) {
        String name = copied.getWidgetName();
        do {
            name += postfix;
        } while (formDesigner.getTarget().isNameExist(name) || clonedNameList.contains(name));
        copied.setWidgetName(name);
        clonedNameList.add(name);
    }

    public static void rebuildSelection(FormDesigner designer) {
        ArrayList<XCreator> newSelection = new ArrayList<XCreator>();
        List<Widget> widgetList = new ArrayList<Widget>();
        for (XCreator comp : designer.getSelectionModel().getSelection().getSelectedCreators()) {
            widgetList.add(comp.toData());
        }
        designer.getSelectionModel().setSelectedCreators(
                rebuildSelection(designer.getRootComponent(), widgetList, newSelection));
    }

    public static ArrayList<XCreator> rebuildSelection(XCreator rootComponent, Widget[] selectWidgets) {
        List<Widget> selectionWidget = new ArrayList<Widget>();
        if (selectWidgets != null) {
            selectionWidget.addAll(Arrays.asList(selectWidgets));
        }
        return FormSelectionUtils.rebuildSelection(rootComponent, selectionWidget, new ArrayList<XCreator>());
    }

    private static ArrayList<XCreator> rebuildSelection(XCreator rootComponent, List<Widget> selectionWidget,
                                                        ArrayList<XCreator> newSelection) {
        FormSelectionUtils._rebuild(rootComponent, selectionWidget, newSelection);
        if (newSelection.isEmpty()) {
            newSelection.add(rootComponent);
        }
        return newSelection;
    }

    private static void _rebuild(XCreator root, List<Widget> selectionWidget, List<XCreator> newSelection) {
        if (selectionWidget.isEmpty()) {
            return;
        }
        for (Widget x : selectionWidget) {
            if (ComparatorUtils.equals(x, root.toData())) {
                if (!newSelection.contains(root)) {
                    newSelection.add(root);
                    selectionWidget.remove(x);
                }
                break;
            }
        }

        int count = root.getComponentCount();
        for (int i = 0; i < count && !selectionWidget.isEmpty(); i++) {
            Component c = root.getComponent(i);
            if (c instanceof XCreator) {
                XCreator creator = (XCreator) c;
                for (Widget x : selectionWidget) {
                    if (ComparatorUtils.equals(x, creator.toData())) {
                        newSelection.add(creator);
                        selectionWidget.remove(x);
                        break;
                    }
                }
                if (c instanceof XLayoutContainer) {
                    _rebuild((XLayoutContainer) c, selectionWidget, newSelection);
                }
            }
        }
    }
}