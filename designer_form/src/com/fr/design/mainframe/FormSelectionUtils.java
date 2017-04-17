package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.AbstractLayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.*;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormSelectionUtils {

    //组件复制时坐标偏移
    private static final int DELAY_X_Y = 20;

    //组件重命名后缀
    private static final String POSTFIX = "_c";

    private FormSelectionUtils() {

    }

    /**
     * 粘贴到容器
     */
    public static void paste2Container(FormDesigner designer, XLayoutContainer parent,
                                       FormSelection clipboard, int x, int y) {
        LayoutAdapter adapter = parent.getLayoutAdapter();
        if (parent instanceof XWAbsoluteLayout) {
            //绝对布局
            absolutePaste(designer, clipboard, adapter, x, y);
            return;
        } else if (parent instanceof XWFitLayout) {
            //相对布局
            relativePaste(designer, clipboard, adapter, x, y);
            return;
        }
        Toolkit.getDefaultToolkit().beep();
    }

    /**
     * 绝对布局粘贴
     */
    private static void absolutePaste(FormDesigner designer, FormSelection clipboard, LayoutAdapter adapter, int x, int y) {

        designer.getSelectionModel().getSelection().reset();
        Rectangle rec = clipboard.getSelctionBounds();
        for (XCreator creator : clipboard.getSelectedCreators()) {
            try {
                Widget copied = copyWidget(designer, creator);
                XCreator copiedCreator = XCreatorUtils.createXCreator(copied, creator.getSize());
                // 获取位置
                Point point = getPasteLocation((AbstractLayoutAdapter) adapter,
                        copiedCreator,
                        x + creator.getX() - rec.x + copiedCreator.getWidth() / 2,
                        y + creator.getY() - rec.y + copiedCreator.getHeight() / 2);
                if (!adapter.accept(copiedCreator, point.x, point.y)) {
                    designer.showMessageDialog(Inter.getLocText("FR-Designer_Too_Large_To_Paste"));
                    return;
                }
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

    }

    /**
     * 相对布局粘贴
     */
    private static void relativePaste(FormDesigner designer, FormSelection clipboard, LayoutAdapter adapter, int x, int y) {
        designer.getSelectionModel().getSelection().reset();
        for (XCreator creator : clipboard.getSelectedCreators()) {
            try {
                Widget copied = copyWidget(designer, creator);
                XCreator copiedCreator = XCreatorUtils.createXCreator(copied, creator.getSize());
                if (!adapter.accept(copiedCreator, x, y)) {
                    designer.showMessageDialog(Inter.getLocText("FR-Designer_Too_Small_To_Paste"));
                    return;
                }
                boolean addSuccess = adapter.addBean(copiedCreator, x, y);
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
    }

    /**
     * 组件复用绝对布局获取粘贴组件位置
     */
    private static Point getPasteLocation(AbstractLayoutAdapter layoutAdapter, XCreator copiedCreator, int x, int y) {
        //当宽度为奇数时 设置偏移
        int xoffset = (copiedCreator.getWidth() & 1) == 1 ? 1 : 0;
        //当高度为奇数时 设置偏移
        int yoffset = (copiedCreator.getHeight() & 1) == 1 ? 1 : 0;

        if (!layoutAdapter.accept(copiedCreator, x, y)) {
            XLayoutContainer container = layoutAdapter.getContainer();
            boolean xOut = x < 0 || x + copiedCreator.getWidth() / 2 + xoffset > container.getWidth();
            boolean yOut = y < 0 || y + copiedCreator.getHeight() / 2 + yoffset > container.getHeight();
            /*
             * 组件原始位置位于布局的右下角，
             * 和布局右下边界线紧挨，
             * 粘贴时组件在原始位置向左错开20像素。
             * x,y同时越界
             */
            if (xOut && yOut) {
                //向左偏移
                x = container.getWidth() - copiedCreator.getWidth() / 2 - DELAY_X_Y - xoffset;
                //紧贴下边界
                y = container.getHeight() - copiedCreator.getHeight() / 2 - yoffset;
                return new Point(x, y);
            }
            /*
            * 组件原始位置与布局边界距离小于20像素（下边界&右边界同时小于或者任意一个边界小于），
            * 则粘贴时距离小于20像素一侧直接贴近布局边界，
            * 距离大于20像素的一侧正常错开。
            * x,y中只有一个越界
            */
            if ((xOut || yOut)) {
                x = xOut ? container.getWidth() - copiedCreator.getWidth() / 2 - xoffset : x;
                y = yOut ? container.getHeight() - copiedCreator.getHeight() / 2 - yoffset : y;
                return new Point(x, y);
            }
        }
        return new Point(x, y);
    }


    /**
     * 拷贝组件
     */
    private static Widget copyWidget(FormDesigner formDesigner, XCreator xCreator) throws
            CloneNotSupportedException {
        ArrayList<String> nameSpace = new ArrayList<>();
        Widget copied = (Widget) xCreator.toData().clone();
        //重命名拷贝的组件
        String name = getCopiedName(formDesigner, copied, nameSpace);
        if (copied instanceof WTitleLayout) {
            XWTitleLayout xwTitleLayout = new XWTitleLayout((WTitleLayout) copied, xCreator.getSize());
            xwTitleLayout.resetCreatorName(name);
        } else {
            copied.setWidgetName(name);
        }
        return copied;
    }

    /**
     * 组件拷贝命名规则
     */
    private static String getCopiedName(FormDesigner formDesigner, Widget copied, ArrayList<String> nameSpace) {
        StringBuilder name = new StringBuilder(copied.getWidgetName());
        do {
            name.append(POSTFIX);
        } while (formDesigner.getTarget().isNameExist(name.toString()) || nameSpace.contains(name.toString()));
        nameSpace.add(name.toString());
        return name.toString();
    }

    public static void rebuildSelection(FormDesigner designer) {
        ArrayList<XCreator> newSelection = new ArrayList<>();
        List<Widget> widgetList = new ArrayList<>();
        for (XCreator comp : designer.getSelectionModel().getSelection().getSelectedCreators()) {
            widgetList.add(comp.toData());
        }
        designer.getSelectionModel().setSelectedCreators(
                rebuildSelection(designer.getRootComponent(), widgetList, newSelection));
    }

    public static ArrayList<XCreator> rebuildSelection(XCreator rootComponent, Widget[] selectWidgets) {
        List<Widget> selectionWidget = new ArrayList<>();
        if (selectWidgets != null) {
            selectionWidget.addAll(Arrays.asList(selectWidgets));
        }
        return FormSelectionUtils.rebuildSelection(rootComponent, selectionWidget, new ArrayList<XCreator>());
    }

    private static ArrayList<XCreator> rebuildSelection(XCreator rootComponent, List<Widget> selectionWidget,
                                                        ArrayList<XCreator> newSelection) {
        FormSelectionUtils.rebuild(rootComponent, selectionWidget, newSelection);
        if (newSelection.isEmpty()) {
            newSelection.add(rootComponent);
        }
        return newSelection;
    }

    private static void rebuild(XCreator root, List<Widget> selectionWidget, List<XCreator> newSelection) {
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
                    rebuild((XLayoutContainer) c, selectionWidget, newSelection);
                }
            }
        }
    }
}