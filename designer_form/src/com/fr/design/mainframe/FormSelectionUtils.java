package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.AbstractLayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRTabFitLayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.XWScaleLayout;
import com.fr.design.designer.creator.XWTitleLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 表单选中工具类
 *
 * @author yaoh.wu
 * @version 2017年11月15日13点51分
 * @since 8.0
 */
public class FormSelectionUtils {

    /**
     * 组件复制时坐标偏移
     */
    private static final int DELAY_X_Y = 20;

    /**
     * 组件重命名后缀
     */
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
                resetTabSub2RealSize(copiedCreator);
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
     * REPORT-6096 复制得到的是显示的大小，如果因屏幕分辨率问题存在缩放的话，显示大小和实际大小会有区别，粘贴后tab内部调整大小时会再次缩放导致问题。
     * 因此在粘贴之前将tab内部组件调整成实际的大小。
     *
     * @param copiedCreator 复制的组件
     */
    private static void resetTabSub2RealSize(XCreator copiedCreator) {
        ArrayList<?> childrenList = copiedCreator.getTargetChildrenList();
        if (!childrenList.isEmpty()) {
            for (Object aChildrenList : childrenList) {
                XWTabFitLayout tabLayout = (XWTabFitLayout) aChildrenList;
                double percent = tabLayout.getContainerPercent();
                Component[] components = tabLayout.getComponents();
                for (Component component : components) {
                    Rectangle show = component.getBounds();
                    component.setBounds(new Rectangle((int) (show.x * percent), (int) (show.y * percent), (int) (show.width * percent), (int) (show.height * percent)));
                }
            }
        }
    }

    /**
     * 相对布局粘贴
     */
    private static void relativePaste(FormDesigner designer, FormSelection clipboard, LayoutAdapter adapter, int x, int y) {

        //@see FRTabFitLayoutAdapter
        Rectangle tabContainerRect = ComponentUtils.getRelativeBounds(designer.getSelectionModel().getSelection()
                .getSelectedCreator().getParent());

        designer.getSelectionModel().getSelection().reset();
        for (XCreator creator : clipboard.getSelectedCreators()) {
            if (creator instanceof XWScaleLayout) {
                //XWScaleLayout封装了在自适应布局中需要保持默认高度的控件，由于自适应粘贴时会再次包装，因此复制时要进行解包
                Component[] innerComponents = creator.getComponents();
                for (Component innerComponent : innerComponents) {
                    XCreator innerXCreator = (XCreator) innerComponent;
                    relativePasteXCreator(designer, innerXCreator, adapter, tabContainerRect, x, y);
                }
            } else {
                relativePasteXCreator(designer, creator, adapter, tabContainerRect, x, y);
            }
        }
        rebuildSelection(designer);
        designer.getEditListenerTable().fireCreatorModified(
                designer.getSelectionModel().getSelection().getSelectedCreator(), DesignerEvent.CREATOR_PASTED);
    }


    private static void relativePasteXCreator(FormDesigner designer, XCreator creator, LayoutAdapter adapter, Rectangle tabContainerRect, int x, int y) {
        try {
            Widget copied = copyWidget(designer, creator);
            XCreator copiedXCreator = XCreatorUtils.createXCreator(copied, creator.getSize());
            if (adapter.getClass().equals(FRTabFitLayoutAdapter.class)) {
                if (!adapter.accept(copiedXCreator, x - tabContainerRect.x, y - tabContainerRect.y)) {
                    designer.showMessageDialog(Inter.getLocText("FR-Designer_Too_Small_To_Paste"));
                    return;
                }
            } else {
                if (!adapter.accept(copiedXCreator, x, y)) {
                    designer.showMessageDialog(Inter.getLocText("FR-Designer_Too_Small_To_Paste"));
                    return;
                }
            }
            boolean addSuccess = adapter.addBean(copiedXCreator, x, y);
            if (addSuccess) {
                designer.getSelectionModel().getSelection().addSelectedCreator(copiedXCreator);
            }
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * 组件复用绝对布局获取粘贴组件位置
     */
    private static Point getPasteLocation(AbstractLayoutAdapter layoutAdapter, XCreator copiedCreator, int x, int y) {
        //当宽度为奇数时 设置偏移
        int xoffset = copiedCreator.getWidth() & 1;
        //当高度为奇数时 设置偏移
        int yoffset = copiedCreator.getHeight() & 1;

        if (!layoutAdapter.accept(copiedCreator, x, y)) {
            XLayoutContainer container = layoutAdapter.getContainer();
            boolean xOut = x < 0 || x + copiedCreator.getWidth() / 2 + xoffset > container.getWidth();
            boolean yOut = y < 0 || y + copiedCreator.getHeight() / 2 + yoffset > container.getHeight();

            boolean isEdge = (x - DELAY_X_Y == container.getWidth() - copiedCreator.getWidth() / 2 - xoffset)
                    && (y - DELAY_X_Y == container.getHeight() - copiedCreator.getHeight() / 2 - yoffset);

            y = yOut ? container.getHeight() - copiedCreator.getHeight() / 2 - yoffset : y;
            if (xOut) {
                if (isEdge) {
                    //向左偏移
                    x = container.getWidth() - copiedCreator.getWidth() / 2 - DELAY_X_Y - xoffset;
                }
                //紧贴下边界
                else {
                    x = container.getWidth() - copiedCreator.getWidth() / 2 - xoffset;
                }
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