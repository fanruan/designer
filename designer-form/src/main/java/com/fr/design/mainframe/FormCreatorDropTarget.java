package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.DesignModelAdapter;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.Painter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.models.AddingModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.share.SharableEditorProvider;
import com.fr.form.share.ShareLoader;
import com.fr.form.ui.ChartEditor;
import com.fr.form.ui.SharableWidgetBindInfo;
import com.fr.form.ui.Widget;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.stable.Constants;

import javax.swing.BorderFactory;
import javax.swing.JWindow;
import java.util.Map;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

/**
 * 添加模式下鼠标事件处理器。
 */
public class FormCreatorDropTarget extends DropTarget {

    private FormDesigner designer;
    /**
     * 当前鼠标的设计组件
     */
    private Component current;
    /**
     * 当前添加模式对应的model
     */
    private AddingModel addingModel;
    private static final int GAP = 30;

    private JWindow promptWindow = new JWindow();
    private UIButton promptButton = new UIButton("", BaseUtils.readIcon(IconPathConstants.FORBID_ICON_PATH));

    public FormCreatorDropTarget(FormDesigner designer) {
        this.designer = designer;
        this.addingModel = designer.getAddingModel();
        this.promptWindow.add(promptButton);
    }

    private void adding(int x, int y) {
        // 当前鼠标所在的组件
        XCreator hoveredComponent = designer.getComponentAt(x, y);
        // 获取该组件所在的焦点容器
        XLayoutContainer container = XCreatorUtils.getHotspotContainer(hoveredComponent);
        boolean success = false;
        if (container != null) {
            //XWCardTagLayout 切换添加状态到普通状态
            container.stopAddingState(designer);

            // 如果是容器，则调用其acceptComponent接受组件
            AddingModel model = designer.getAddingModel();

            boolean chartEnter2Para = !addingModel.getXCreator().canEnterIntoParaPane() && container.acceptType(XWParameterLayout.class);
            boolean formSubmit2Adapt = !addingModel.getXCreator().canEnterIntoAdaptPane() && container.acceptType(XWFitLayout.class);

            if (model != null && !chartEnter2Para && !formSubmit2Adapt) {
                success = model.add2Container(designer, container, x, y);
            }
            cancelPromptWidgetForbidEnter();
        }
        if (success) {
            // 如果添加成功，则触发相应事件
            XCreator xCreator = container.acceptType(XWParameterLayout.class) ? designer.getParaComponent() : designer.getRootComponent();
            //SetSelection时要确保选中的是最顶层的布局
            //tab布局添加的时候是初始化了XWCardLayout，实际上最顶层的布局是XWCardMainBorderLayout
            XCreator addingXCreator = addingModel.getXCreator();
            Widget widget = (addingXCreator.getTopLayout() != null) ? (addingXCreator.getTopLayout().toData()) : addingXCreator.toData();
            //图表埋点
            dealChartBuryingPoint(widget);
            if (addingXCreator.isShared()) {
                String shareId = addingXCreator.getShareId();
                SharableEditorProvider sharableEditor = ShareLoader.getLoader().getSharedElCaseEditorById(shareId);
                SharableWidgetBindInfo bindInfo = ShareLoader.getLoader().getElCaseBindInfoById(shareId);
                if (sharableEditor != null && bindInfo != null) {
                    Map<String, String> tdNameMap = TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()).addTableData(bindInfo.getName(), sharableEditor.getTableDataSource());
                    //合并数据集之后,可能会有数据集名称变化，做一下联动
                    //共享的组件拿的时候都是克隆的,这边改拖拽中克隆的对象而非新克隆对象,上面这个新克隆的对象只是为了拿数据集
                    for (Map.Entry<String, String> entry : tdNameMap.entrySet()) {
                        designer.getTarget().renameTableData(widget, entry.getKey(), entry.getValue());
                    }
                }
            }
            designer.getSelectionModel().setSelectedCreators(
                    FormSelectionUtils.rebuildSelection(xCreator, new Widget[]{widget}));
            designer.getEditListenerTable().fireCreatorModified(addingModel.getXCreator(), DesignerEvent.CREATOR_ADDED);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
        // 取消提示
        designer.setPainter(null);
        // 切换添加状态到普通状态
        designer.stopAddingState();
    }

    private void entering(int x, int y) {
        // 将要添加的组件图标移动到鼠标下的位置
        addingModel.moveTo(x, y);
        designer.repaint();
    }

    private void exiting() {
        cancelPromptWidgetForbidEnter();
        // 隐藏组件图标
        addingModel.reset();
        designer.setPainter(null);
        designer.repaint();
    }

    private void hovering(int x, int y) {
        // 当前位置移植鼠标e所在的位置
        addingModel.moveTo(x, y);
        // 获取e所在的焦点组件
        XCreator hotspot = designer.getComponentAt(x, y);
        // 获取焦点组件所在的焦点容器
        XLayoutContainer container = XCreatorUtils.getHotspotContainer(hotspot);
        //提示组件是否可以拖入
        promptUser(x, y, container);
        if (container != null) {
            dealWithContainer(x, y, container);
        } else {
            // 如果鼠标不在任何组件上，则取消提示器
            designer.setPainter(null);
            current = null;
        }
        designer.repaint();
    }

    private void dealWithContainer(int x, int y, XLayoutContainer container) {
        HoverPainter painter = null;

        if (container != current || designer.getPainter() == null) {
            // 如果焦点容器不是当前容器
            if (current != null) {
                // 取消前一个焦点容器的提示渲染器
                designer.setPainter(null);
            }
            if (container == null) {
                throw new IllegalArgumentException("container can not be null!");
            }
            //获取painter的时候要考虑布局之间嵌套的问题
            XLayoutContainer xLayoutContainer = container.getTopLayout();
            if (xLayoutContainer != null && xLayoutContainer.getParent() != null
                    && ((XLayoutContainer) xLayoutContainer.getParent()).acceptType(XWAbsoluteLayout.class)) {
                if (!xLayoutContainer.isEditable()) {
                    xLayoutContainer = (XLayoutContainer) xLayoutContainer.getParent();
                }
            }
            painter = AdapterBus.getContainerPainter(designer,
                    xLayoutContainer != null && xLayoutContainer.acceptType(XWAbsoluteLayout.class) ? xLayoutContainer : container);

            // 为界面设计器设置提示渲染提示器
            designer.setPainter(painter);

            // 将当前容器更新为新的容器
            current = container;
        } else {
            // 获取当前设计界面的提示渲染器
            Painter p = designer.getPainter();
            if (p instanceof HoverPainter) {
                painter = (HoverPainter) p;
            }
        }
        if (painter != null) {
            // 为提示渲染器设置焦点位置、区域等渲染参数
            Rectangle rect = ComponentUtils.getRelativeBounds(container);
            rect.x -= designer.getArea().getHorizontalValue();
            rect.y -= designer.getArea().getVerticalValue();
            painter.setRenderingBounds(rect);
            painter.setHotspot(new Point(x, y));
            painter.setCreator(addingModel.getXCreator());
        }
    }

    private void promptUser(int x, int y, XLayoutContainer container) {
        if (!addingModel.getXCreator().canEnterIntoParaPane() && container.acceptType(XWParameterLayout.class)) {
            promptButton.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Forbid_Drag_Into_Para_Pane"));
            promptWidgetForbidEnter(x, y, container);
        } else if (!addingModel.getXCreator().canEnterIntoAdaptPane() && container.acceptType(XWFitLayout.class)) {
            promptButton.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Forbid_Drag_Into_Adapt_Pane"));
            promptWidgetForbidEnter(x, y, container);
        } else {
            cancelPromptWidgetForbidEnter();
        }
    }

    private void promptWidgetForbidEnter(int x, int y, XLayoutContainer container) {
        container.setBorder(BorderFactory.createLineBorder(Color.RED, Constants.LINE_MEDIUM));
        int screenX = designer.getArea().getLocationOnScreen().x;
        int screenY = designer.getArea().getLocationOnScreen().y;
        promptWindow.setLocation(screenX + x + GAP, screenY + y + GAP);
        promptWindow.setVisible(true);
    }

    private void cancelPromptWidgetForbidEnter() {
        if (designer.getParaComponent() != null) {
            designer.getParaComponent().setBorder(BorderFactory.createLineBorder(XCreatorConstants.LAYOUT_SEP_COLOR, Constants.LINE_THIN));
        }
        designer.getRootComponent().setBorder(BorderFactory.createLineBorder(XCreatorConstants.LAYOUT_SEP_COLOR, Constants.LINE_THIN));
        promptWindow.setVisible(false);
    }

    /**
     * 拖拽进入
     *
     * @param dtde 事件
     */
    @Override
    public synchronized void dragEnter(DropTargetDragEvent dtde) {
        Point loc = dtde.getLocation();
        this.entering(loc.x, loc.y);
    }

    /**
     * 拖拽移动经过
     *
     * @param dtde 事件
     */
    @Override
    public synchronized void dragOver(DropTargetDragEvent dtde) {
        Point loc = dtde.getLocation();
        hovering(loc.x, loc.y);
    }

    /**
     * 拖拽事件
     *
     * @param dtde 事件
     */
    @Override
    public synchronized void dropActionChanged(DropTargetDragEvent dtde) {
    }

    /**
     * 拖拽离开
     *
     * @param dte 事件
     */
    @Override
    public synchronized void dragExit(DropTargetEvent dte) {
        this.exiting();
    }

    /**
     * 拖拽释放
     *
     * @param dtde 事件
     */
    @Override
    public synchronized void drop(DropTargetDropEvent dtde) {
        Point loc = dtde.getLocation();
        this.adding(loc.x, loc.y);
        //针对在表单中拖入一个控件直接ctrl+s无反应
        designer.requestFocus();
    }

    private void dealChartBuryingPoint(Widget widget) {
        if (widget instanceof ChartEditor) {
            BaseChartCollection chartCollection = ((ChartEditor) widget).getChartCollection();
            VanChart vanChart = ((ChartCollection) chartCollection).getSelectedChartProvider(VanChart.class);
            if (vanChart != null) {
                ChartInfoCollector.getInstance().collection(vanChart.getUuid(), vanChart.getID(), null);
            }
        }
    }
}
