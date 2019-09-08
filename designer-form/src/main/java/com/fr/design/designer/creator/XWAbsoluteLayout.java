/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.base.GraphHelper;
import com.fr.base.iofile.attr.SharableAttrMark;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRAbsoluteLayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.form.layout.FRAbsoluteLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormArea;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetHelpDialog;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.ui.Connector;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.WLayout;
import com.fr.general.FRScreen;
import com.fr.general.IOUtils;

import com.fr.share.ShareConstants;
import com.fr.stable.AssistUtils;
import com.fr.stable.Constants;

import javax.swing.Icon;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author richer
 * @since 6.5.3
 */
public class XWAbsoluteLayout extends XLayoutContainer {

    private static final int EDIT_BTN_WIDTH = 75;
    private static final int EDIT_BTN_HEIGHT = 20;
    private int minWidth = WLayout.MIN_WIDTH;
    private int minHeight = WLayout.MIN_HEIGHT;
    private static final Color OUTER_BORDER_COLOR = new Color(65, 155, 249, 30);
    private static final Color INNER_BORDER_COLOR = new Color(65, 155, 249);
    private static final int BORDER_WIDTH = 1;
    private Icon controlMode = IOUtils.readIcon(IconPathConstants.TD_EL_SHARE_HELP_ICON_PATH);

    //由于屏幕分辨率不同，界面上的容器大小可能不是默认的100%，此时拖入组件时，保存的大小按照100%时的计算
    protected double containerPercent = 1.0;

    private boolean isHovering = false;

    private HashMap<Connector, XConnector> xConnectorMap;

    public XWAbsoluteLayout() {
        this(new WAbsoluteLayout(), new Dimension());
    }

    public XWAbsoluteLayout(WAbsoluteLayout widget) {
        this(widget, new Dimension());
    }

    public XWAbsoluteLayout(WAbsoluteLayout widget, Dimension initSize) {
        super(widget, initSize);
        this.xConnectorMap = new HashMap<Connector, XConnector>();
        Connector connector;
        for (int i = 0; i < widget.connectorCount(); i++) {
            connector = widget.getConnectorIndex(i);
            xConnectorMap.put(connector, new XConnector(connector, this));
        }

        initPercent(widget);
    }

    /**
     * 初始化时默认的组件大小
     *
     * @return 默认Dimension
     */
    @Override
    public Dimension initEditorSize() {
        return new Dimension(500, 300);
    }

    //根据屏幕大小来确定显示的百分比, 1440*900默认100%, 1366*768缩放90%
    private void initPercent(WAbsoluteLayout widget) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        double screenValue = FRScreen.getByDimension(scrnsize).getValue();
        if (!AssistUtils.equals(screenValue, FormArea.DEFAULT_SLIDER)) {
            this.setContainerPercent(screenValue / FormArea.DEFAULT_SLIDER);
        }
    }

    /**
     * 返回容器大小的百分比
     *
     * @return the containerPercent
     */
    public double getContainerPercent() {
        return containerPercent;
    }

    /**
     * 设置容器大小的百分比
     *
     * @param containerPercent the containerPercent to set
     */
    public void setContainerPercent(double containerPercent) {
        this.containerPercent = containerPercent;
        minWidth = (int) (XWAbsoluteLayout.MIN_WIDTH * containerPercent);
        minHeight = (int) (XWAbsoluteLayout.MIN_HEIGHT * containerPercent);
    }

    /**
     * 返回界面处根据百分比调整后的最小宽度
     *
     * @return 最小宽度
     */
    public int getActualMinWidth() {
        return this.minWidth;
    }

    /**
     * 返回界面处根据百分比调整后的最小高度
     *
     * @return 最小高度
     */
    public int getActualMinHeight() {
        return this.minHeight;
    }

    /**
     * 返回界面处根据百分比调整后的间隔大小（且为偶数）
     *
     * @return 间隔
     */
    public int getAcualInterval() {
        // adapter那边交叉三等分、删除都要判断是否对齐，所以间隔转为偶数
        int interval = (int) (toData().getCompInterval() * containerPercent);
        int val = interval / 2;
        return val * 2;
    }

    /**
     * 界面容器大小不是默认的时，处理控件的BoundsWidget，且避免出现空隙
     */
    private Rectangle dealWidgetBound(Rectangle rec) {
        if (AssistUtils.equals(1.0, containerPercent)) {
            return rec;
        }
        rec.x = (int) (rec.x / containerPercent);
        rec.y = (int) (rec.y / containerPercent);
        rec.width = (int) (rec.width / containerPercent);
        rec.height = (int) (rec.height / containerPercent);
        return rec;
    }

    /**
     * 新增删除拉伸后单个组件的BoundsWidget
     */
    public void updateBoundsWidget(XCreator xCreator) {
        WAbsoluteLayout layout = this.toData();
        if (xCreator.hasTitleStyle()) {
            xCreator = (XLayoutContainer) xCreator.getParent();
        }
        if (xCreator.acceptType(XWAbsoluteLayout.class)) {
            ((XWAbsoluteLayout) xCreator).updateBoundsWidget();
        }
        // 如果子组件时tab布局，则tab布局内部的组件的wiget也要更新，否则保存后重新打开大小不对
        ArrayList<?> childrenList = xCreator.getTargetChildrenList();
        if (!childrenList.isEmpty()) {
            for (int i = 0; i < childrenList.size(); i++) {
                XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList.get(i);
                tabLayout.updateBoundsWidget();
            }
        }
    }

    private Rectangle calculateBound(Rectangle rec, double pw, double ph) {
        Rectangle calRec = new Rectangle(0, 0, 0, 0);
        calRec.x = (int) Math.round(rec.x / pw);
        calRec.y = (int) Math.round(rec.y / ph);
        calRec.width = (int) Math.round(rec.width / pw);
        calRec.height = (int) Math.round(rec.height / ph);
        return calRec;
    }

    /**
     * 新增删除拉伸后每个组件的BoundsWidget
     */
    public void updateBoundsWidget() {
        WAbsoluteLayout layout = this.toData();
        Rectangle backupBound = this.getBackupBound();
        Rectangle currentBound = this.getBounds();
        if (backupBound != null && layout.getCompState() == WAbsoluteLayout.STATE_FIT) {
            double percentW = ((double) backupBound.width / (double) currentBound.width);
            double percentH = ((double) backupBound.height / (double) currentBound.height);
            for (int index = 0, n = this.getComponentCount(); index < n; index++) {
                XCreator creator = (XCreator) this.getComponent(index);
                BoundsWidget wgt = (BoundsWidget) layout.getBoundsWidget(creator.toData());
                // 用当前的显示大小计算后调正具体位置
                Rectangle wgtBound = creator.getBounds();
                Rectangle rec = calculateBound(wgtBound, percentW, percentH);
                wgt.setBounds(rec);
                creator.setBounds(rec);
                //绝对布局嵌套，要更新内部的绝对布局
                if (creator.acceptType(XWAbsoluteLayout.class)) {
                    creator.setBackupBound(wgtBound);
                    ((XWAbsoluteLayout) creator).updateBoundsWidget();
                }
            }
        }
    }

    /**
     * 更新子组件的Bound
     * 这边主要用于绝对布局子组件在适应区域选项时
     * 涉及到的不同分辨率下缩放
     *
     * @param minHeight 最小高度
     */
    @Override
    public void updateChildBound(int minHeight) {
        double prevContainerPercent = FRScreen.getByDimension(toData().getDesigningResolution()).getValue() / FormArea.DEFAULT_SLIDER;
        if (toData().getCompState() == 0 && !AssistUtils.equals(containerPercent, prevContainerPercent)) {
            for (int i = 0; i < this.getComponentCount(); i++) {
                XCreator creator = getXCreator(i);
                Rectangle rec = new Rectangle(creator.getBounds());
                rec.x = (int)Math.round (rec.x / prevContainerPercent * containerPercent);
                rec.y = (int)Math.round (rec.y / prevContainerPercent * containerPercent);
                rec.height = (int)Math.round (rec.height / prevContainerPercent * containerPercent);
                rec.width = (int)Math.round (rec.width / prevContainerPercent * containerPercent);
                BoundsWidget wgt = (BoundsWidget) toData().getBoundsWidget(creator.toData());
                wgt.setBounds(rec);
                creator.setBounds(rec);
                creator.updateChildBound(minHeight);
            }
        }
        toData().setDesigningResolution(Toolkit.getDefaultToolkit().getScreenSize());
    }

    /**
     * 增加对齐线
     *
     * @param connector 对齐线
     */
    public void addConnector(Connector connector) {
        xConnectorMap.put(connector, new XConnector(connector, this));
        ((WAbsoluteLayout) data).addConnector(connector);
    }

    public XConnector getXConnector(Connector connector) {
        return xConnectorMap.get(connector);
    }

    /**
     * 去除对齐线
     *
     * @param connector 对齐线
     */
    public void removeConnector(Connector connector) {
        ((WAbsoluteLayout) data).removeConnector(connector);
        xConnectorMap.remove(connector);
    }

    /**
     * 返回对应的widget容器
     *
     * @return 返回WAbsoluteLayout
     */
    @Override
    public WAbsoluteLayout toData() {
        return (WAbsoluteLayout) data;
    }

    @Override
    protected String getIconName() {
        return "layout_absolute_new.png";
    }

    /**
     * 返回默认的容器name
     *
     * @return 返回绝对布局容器名
     */
    @Override
    public String createDefaultName() {
        return "absolute";
    }

    @Override
    protected void initLayoutManager() {
        this.setLayout(new FRAbsoluteLayout());
    }

    @Override
    protected void initStyle() {
        // do nothing
    }

    /**
     * 是否支持标题样式
     *
     * @return 默认false
     */
    @Override
    public boolean hasTitleStyle() {
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        WAbsoluteLayout layout = (WAbsoluteLayout) data;
        Connector[] connector = layout.getConnector();
        for (int i = 0, size = connector.length; i < size; i++) {
            connector[i].draw(g);
        }
    }

    /**
     * 转换保存组件信息的wlayout为对应的container
     */
    @Override
    public void convert() {
        isRefreshing = true;
        WAbsoluteLayout abs = toData();
        this.removeAll();
        for (int i = 0, count = abs.getWidgetCount(); i < count; i++) {
            BoundsWidget bw = (BoundsWidget) abs.getWidget(i);
            if (bw != null) {
                Rectangle bounds = bw.getBounds();
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(bw.getWidget());
                if (!comp.acceptType(XWParameterLayout.class)) {
                    comp.setDirections(Direction.ALL);
                }
                add(comp);
                comp.setBounds(bounds);
            }
        }
        isRefreshing = false;
    }

    /**
     * 当前组件zorder位置替换新的控件
     *
     * @param widget     控件
     * @param oldcreator 旧组件
     * @return 组件
     */
    @Override
    public XCreator replace(Widget widget, XCreator oldcreator) {
        int i = this.getComponentZOrder(oldcreator);
        if (i != -1) {
            this.toData().replace(new BoundsWidget(widget, oldcreator.getBounds()),
                    new BoundsWidget(oldcreator.toData(), oldcreator.getBounds()));
            this.convert();
            return (XCreator) this.getComponent(i);
        }
        return null;
    }

    /**
     * 组件增加
     *
     * @param e 容器事件
     */
    @Override
    public void componentAdded(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        XWidgetCreator creator = (XWidgetCreator) e.getChild();
        WAbsoluteLayout wabs = this.toData();
        if (!creator.acceptType(XWFitLayout.class)) {
            creator.setDirections(Direction.ALL);
        }
        wabs.addWidget(new BoundsWidget(creator.toData(), creator.getBounds()));
    }

    /**
     * 在设计界面中有组件移除的时候，需要通知WLayout容器重新paint
     *
     * @param e 容器事件
     */
    @Override
    public void componentRemoved(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        WAbsoluteLayout wlayout = this.toData();
        XWidgetCreator xwc = ((XWidgetCreator) e.getChild());
        Widget wgt = xwc.toData();
        BoundsWidget bw = new BoundsWidget(wgt, xwc.getBounds());
        wlayout.removeWidget(bw);
    }

    @Override
    public Dimension getMinimumSize() {
        return toData().getMinDesignSize();
    }

    @Override
    public LayoutAdapter getLayoutAdapter() {
        return new FRAbsoluteLayoutAdapter(this);
    }

    @Override
    public XLayoutContainer getTopLayout() {
        XLayoutContainer xTopLayout = XCreatorUtils.getParentXLayoutContainer(this).getTopLayout();
        if (xTopLayout != null && !xTopLayout.isEditable()) {
            return xTopLayout;
        } else {
            return this;
        }
    }

    /**
     * 得到属性名
     *
     * @return 属性名
     * @throws java.beans.IntrospectionException
     */
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form-Widget_Name"))
        };
    }

    public void paint(Graphics g) {
        super.paint(g);
        //如果鼠标移动到布局内且布局不可编辑，画出编辑蒙层
        if (isMouseEnter && !this.editable) {
            int x = 0;
            int y = 0;
            int w = getWidth();
            int h = getHeight();

            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();
            //画白色的编辑层
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 50 / 100.0F));
            g2d.setColor(XCreatorConstants.COVER_COLOR);
            g2d.fillRect(x, y, w, h);
            //画编辑按钮所在框
            FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, formDesigner.getCursor().getType() != Cursor.DEFAULT_CURSOR ? 0.9f : 0.7f);
            g2d.setColor(XCreatorConstants.EDIT_COLOR);
            g2d.setComposite(alphaComposite);
            g2d.fillRoundRect((x + w / 2 - EDIT_BTN_WIDTH / 2), (y + h / 2 - EDIT_BTN_HEIGHT / 2), EDIT_BTN_WIDTH, EDIT_BTN_HEIGHT, 4, 4);
            g2d.setComposite(oldComposite);
            //画编辑按钮图标
            BufferedImage image = IOUtils.readImage(IconPathConstants.EDIT_ICON_PATH);
            g2d.drawImage(
                    image,
                    (x + w / 2 - 23),
                    (y + h / 2 - image.getHeight() / 2),
                    image.getWidth(),
                    image.getHeight(),
                    null,
                    this
            );
            g2d.setColor(Color.WHITE);
            //画编辑文字
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawString(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Edit"), x + w / 2 - 2, y + h / 2 + 5);
            g.setColor(XCreatorConstants.FORM_BORDER_COLOR);
            GraphHelper.draw(g, new Rectangle(BORDER_WIDTH, BORDER_WIDTH, getWidth() - BORDER_WIDTH * 2, getHeight() - BORDER_WIDTH * 2), Constants.LINE_MEDIUM);
            paintExtro(g);
        }
    }

    public void paintExtro(Graphics g) {
        if (this.toData().getWidgetAttrMark(SharableAttrMark.XML_TAG) != null) {
            int width = getWidth() - ShareConstants.SHARE_EL_CONTROL_BUTTON_HW;
            g.setColor(UIConstants.NORMAL_BACKGROUND);
            g.fillArc(width, 0, ShareConstants.SHARE_EL_CONTROL_BUTTON_HW, ShareConstants.SHARE_EL_CONTROL_BUTTON_HW,
                    0, 360);
            controlMode.paintIcon(this, g, width, 0);
        }
    }

    @Override
    public void paintBorder(Graphics g, Rectangle bounds){
        if(editable){
            g.setColor(OUTER_BORDER_COLOR);
            GraphHelper.draw(g, new Rectangle(bounds.x - 3, bounds.y - 3, bounds.width + 5, bounds.height + 5), Constants.LINE_LARGE);
            g.setColor(INNER_BORDER_COLOR);
            GraphHelper.draw(g, new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height), Constants.LINE_MEDIUM);
        }else if(!isMouseEnter){
            super.paintBorder(g, bounds);
        }
    }
    /**
     * 响应点击事件
     *
     * @param editingMouseListener 鼠标点击，位置处理器
     * @param e                    鼠标点击事件
     */
    public void respondClick(EditingMouseListener editingMouseListener, MouseEvent e) {
        //帮助弹窗
        if (this.isHelpBtnOnFocus()) {
            new WidgetHelpDialog(DesignerContext.getDesignerFrame(), this.toData().getDescription()).showWindow(e);
            return;
        }
        FormDesigner designer = editingMouseListener.getDesigner();
        SelectionModel selectionModel = editingMouseListener.getSelectionModel();
        boolean isEditing = isEditable() ||
                e.getButton() == MouseEvent.BUTTON1 && (designer.getCursor().getType() == Cursor.HAND_CURSOR || e.getClickCount() == 2);
        setEditable(isEditing);

        selectionModel.selectACreatorAtMouseEvent(e);
        designer.repaint();

        if (editingMouseListener.stopEditing()) {
            if (this != designer.getRootComponent()) {
                ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
                editingMouseListener.startEditing(this, isEditing ? adapter.getDesignerEditor() : null, adapter);
            }
        }
    }

    /**
     * body大小手动调整的时候
     * 按照比例调整组件的宽度
     *
     * @param percent 比例
     */
    @Override
    public void adjustCompWidth(double percent) {
        for (int i = 0; i < getComponentCount(); i++) {
            XCreator xCreator = (XCreator) getComponent(i);
            Rectangle rectangle = xCreator.getBounds();
            xCreator.setBounds((int) (rectangle.x * percent), rectangle.y, (int) (rectangle.width * percent), rectangle.height);
            BoundsWidget widget = (BoundsWidget) toData().getBoundsWidget(xCreator.toData());
            widget.setBounds(xCreator.getBounds());
        }
    }

    /**
     * body大小手动调整的时候
     * 按照比例调整组件的高度
     *
     * @param percent 比例
     */
    @Override
    public void adjustCompHeight(double percent) {
        for (int i = 0; i < getComponentCount(); i++) {
            XCreator xCreator = (XCreator) getComponent(i);
            Rectangle rectangle = xCreator.getBounds();
            xCreator.setBounds(rectangle.x, (int) (rectangle.y * percent), rectangle.width, (int) (rectangle.height * percent));
            BoundsWidget widget = (BoundsWidget) toData().getBoundsWidget(xCreator.toData());
            widget.setBounds(xCreator.getBounds());
        }
    }

    @Override
    public boolean supportInnerOrderChangeActions() {
        return true;
    }

    /**
     * 是否支持共享-现只支持报表块、图表、tab块、绝对布局
     * @return
     */
    public boolean isSupportShared() {
        return true;
    }
}
