package com.fr.design.mainframe;

import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.base.Utils;
import com.fr.base.iofile.attr.WatermarkAttr;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.models.AddingModel;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.utils.ComponentUtils;

import com.fr.form.main.parameter.FormParameterUI;
import com.fr.page.WatermarkPainter;
import com.fr.report.core.ReportUtils;
import com.fr.stable.ArrayUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * FormDesigner的UI类，是一个有状态的UI类，它根据FormDesigner的当前状态画出
 * 具有所见即所得的设计界面，以及当前设计界面的一些辅助状态，比如选择标识、拖动区域 以及当前正在添加的组件
 */
public class FormDesignerUI extends ComponentUI {

    // 当前的设计器
    private FormDesigner designer;
    private SelectionModel selectionModel;
    private Rectangle2D.Double back_or_selection_rect = new Rectangle2D.Double(0, 0, 0, 0);
    private float time;

    public FormDesignerUI() {
    }

    /**
     *  初始化界面
     * @param c      组件
     */
    public void installUI(JComponent c) {
        designer = (FormDesigner) c;
        selectionModel = designer.getSelectionModel();
    }

    /**
     * 渲染当前的设计界面以及设计辅助状态
     * @param g 画图类
     * @param c 组件
     */
    @Override
    public void paint(final Graphics g, JComponent c) {
        XCreator rootComponent = designer.getRootComponent();
        this.time = (float)designer.getResolution()/ScreenResolution.getScreenResolution();
        if (rootComponent != null) {
            // 设计自适应界面
            repaintFit(g, rootComponent, c);
        }
        XCreator paraComponent = designer.getParaComponent();
        if (paraComponent != null) {
            // 设计参数面板
            repaintPara(g, paraComponent, c);
        }

        if (designer.isDrawLineMode() && designer.getDrawLineHelper().drawLining()) {
            designer.getDrawLineHelper().drawAuxiliaryLine(g);
            return;
        }

        paintSelection(g);

        if (DesignerMode.isAuthorityEditing()) {
            paintAuthorityDetails(g, designer.getRootComponent());
        }

        Rectangle hotspot_bounds = selectionModel.getHotspotBounds();

        if (hotspot_bounds != null) {
            // 当前区域选择框
            g.setColor(XCreatorConstants.SELECTION_COLOR);
            g.drawRect(hotspot_bounds.x - designer.getArea().getHorizontalValue(), hotspot_bounds.y
                    - designer.getArea().getVerticalValue(), hotspot_bounds.width, hotspot_bounds.height);
        }

        if (designer.getPainter() != null) {
            // ComponentAdapter和LayoutAdapter提供的额外的Painter，该Painter一般用于提示作用，
            // 相当于一个浮动层, 要考虑参数面板的高度
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    designer.getPainter().paint(g, designer.getArea().getHorizontalValue(),
                                                designer.getArea().getVerticalValue() + designer.getParaHeight());
                    return null;
                }
            }.execute();
        }
        AddingModel addingModel = designer.getAddingModel();

        if ((addingModel != null) && (addingModel.getXCreator() != null)) {
            // 当前正在添加的组件
            paintAddingBean(g, addingModel);
        }
    }

    // 绘制水印
    private void paintWatermark(Graphics2D g) {
        if (designer.getTarget() instanceof FormParameterUI) { // cpt 的参数面板
            return;
        }
        WatermarkAttr watermark = ReportUtils.getWatermarkAttrFromTemplateAndGlobal(designer.getTarget());
        WatermarkPainter painter = WatermarkPainter.createPainter(watermark, designer.getResolution());
        painter.paint(g, 0, 0, designer.getArea().getBounds());
    }

    private int[] getActualLine(int i) {
        int j[];
        switch (i) {
            case Direction.TOP:
                j = new int[]{Direction.TOP};
                break;
            case Direction.BOTTOM:
                j = new int[]{Direction.BOTTOM};
                break;
            case Direction.LEFT:
                j = new int[]{Direction.LEFT};
                break;
            case Direction.RIGHT:
                j = new int[]{Direction.RIGHT};
                break;
            case Direction.LEFT_TOP:
                j = new int[]{Direction.TOP, Direction.LEFT};
                break;
            case Direction.LEFT_BOTTOM:
                j = new int[]{Direction.BOTTOM, Direction.LEFT};
                break;
            case Direction.RIGHT_TOP:
                j = new int[]{Direction.TOP, Direction.RIGHT};
                break;
            case Direction.RIGHT_BOTTOM:
                j = new int[]{Direction.BOTTOM, Direction.RIGHT};
                break;
            default:
                j = new int[]{Direction.TOP, Direction.LEFT};
                break;
        }
        return j;
    }

    private void paintPositionLine(Graphics g, Rectangle bounds, int l[]) {
        Graphics2D g2d = (Graphics2D) g.create();
        int x1, y1, x2, y2;
        String text;
        for (int k : l) {
            if (k == 1 || k == 2) {
                x1 = 0;
                x2 = 6;
                y2 = y1 = bounds.y - designer.getArea().getVerticalValue() + (k == 1 ? 0 : bounds.height);
                text = Utils.objectToString(y1 + designer.getArea().getVerticalValue());
            } else {
                y1 = 0;
                y2 = 6;
                x1 = x2 = bounds.x - designer.getArea().getHorizontalValue() + (k == 3 ? 0 : bounds.width);
                text = Utils.objectToString(x1 + designer.getArea().getHorizontalValue());
            }
            text += com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Indent_Pixel");
            g2d.setColor(XCreatorConstants.RESIZE_BOX_BORDER_COLOR);
            GraphHelper.drawString(g2d, text, x1 + 3, y1 + 10);
            GraphHelper.drawLine(g2d, x1, y1, x2, y2);
        }
        g2d.dispose();
    }

    /**
     * 渲染当前正在添加的组件，采用Renderer原理
     */
    private void paintAddingBean(Graphics g, final AddingModel addingModel) {
        XCreator bean = addingModel.getXCreator();
        int x = addingModel.getCurrentX();
        int y = addingModel.getCurrentY();
        int width = bean.getWidth();
        int height = bean.getHeight();
        Graphics clipg = g.create(x, y, width, height);
        ArrayList<JComponent> dbcomponents = new ArrayList<JComponent>();
        // 禁止双缓冲行为
        ComponentUtils.disableBuffer(bean, dbcomponents);

        ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, bean);
        // 调用ComponentAdapter的paintComponentMascot方法渲染该组件添加提示
        adapter.paintComponentMascot(clipg);
        clipg.dispose();
        // 恢复双缓冲
        ComponentUtils.resetBuffer(dbcomponents);
    }


    private void paintAuthorityCreator(Graphics2D g2d, Rectangle creatorBounds) {
        back_or_selection_rect.setRect(creatorBounds.getX(), creatorBounds.getY(),
                creatorBounds.getWidth(), creatorBounds.getHeight());
        Area borderLineArea = new Area(back_or_selection_rect);
        GraphHelper.fill(g2d, borderLineArea);
    }

    /**
     *  画权限编辑的
     * @param g             画图类
     * @param xCreator  组件
     */
    public void paintAuthorityDetails(Graphics g, XCreator xCreator) {
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if (selectedRoles == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setPaint(UIConstants.AUTHORITY_COLOR);
        int count = xCreator.getComponentCount();
        for (int i = 0; i < count; i++) {
            XCreator subCreator = (XCreator) xCreator.getComponent(i);
            if (subCreator instanceof XLayoutContainer) {
                paintAuthorityDetails(g, subCreator);
            } else {
                if (subCreator.toData().isDirtyWidget(selectedRoles)) {
                    Rectangle creatorBounds = ComponentUtils.getRelativeBounds(subCreator);
                    creatorBounds.x -= designer.getArea().getHorizontalValue();
                    creatorBounds.y -= designer.getArea().getVerticalValue();
                    paintAuthorityCreator(g2d, creatorBounds);
                }
            }
        }
        g2d.setPaintMode();
    }

    /**
     * 画选中范围
     * @param g 画图
     */
    public void paintSelection(Graphics g) {
        if (!selectionModel.hasSelectionComponent()) {
            return;
        }
        Rectangle bounds = designer.getTopContainer().getBounds();
        bounds.x = -designer.getArea().getHorizontalValue();
        bounds.y = -designer.getArea().getVerticalValue();
        Graphics clipg = g.create();
        clipg.clipRect(bounds.x, bounds.y, bounds.width + 1, bounds.height + 1);
        paintResizing(clipg);
        clipg.dispose();
    }

    /**
     * 画出当前选择、拖拽状态框
     *
     * @param g  图形
     */
    private void paintResizing(Graphics g) {
        Rectangle bounds = selectionModel.getSelection().getRelativeBounds();
        if (designer.hasWAbsoluteLayout() && designer.getStateModel().getDirection() != null) {
            int[] actualline = getActualLine(designer.getStateModel().getDirection().getActual());
            paintPositionLine(g, bounds, actualline);
        }
        if (designer.getStateModel().isDragging()) {
            designer.getStateModel().paintAbsorptionline(g);
        }

        bounds.x -= designer.getArea().getHorizontalValue();
        bounds.y -= designer.getArea().getVerticalValue();

        drawResizingThumbs(g, selectionModel.getSelection().getDirections(), bounds.x, bounds.y, bounds.width, bounds.height);
        //选中时边框颜色
        g.setColor(XCreatorConstants.FORM_BORDER_COLOR);

        for (XCreator creator : selectionModel.getSelection().getSelectedCreators()) {
            Rectangle creatorBounds = ComponentUtils.getRelativeBounds(creator);
            creatorBounds.x -= designer.getArea().getHorizontalValue();
            creatorBounds.y -= designer.getArea().getVerticalValue();
            if (creator.acceptType(XWFitLayout.class)) {
                resetFitlayoutBounds(creatorBounds);
            } else if (designer.getRootComponent().acceptType(XWFitLayout.class)) {
                resetCreatorBounds(creatorBounds);
            }
            creator.paintBorder(g, creatorBounds);
        }
    }

    /**
     * 初始为自适应时，处理选中的范围
     * @param bound
     */
    private void resetFitlayoutBounds( Rectangle bound) {
        bound.x ++;
        bound.width -= 2;
        bound.y ++;
        bound.height -= 2;
    }

    private void resetCreatorBounds( Rectangle bound) {
        Rectangle rec = bound;
        if (rec.x == 0) {
            bound.x ++;
            bound.width --;
        }
        if (rec.y == 0) {
            bound.y ++;
            bound.height --;
        }
        if (rec.x+rec.width == designer.getWidth()) {
            bound.width --;
        }
        if (rec.y+rec.height == designer.getHeight()) {
            bound.height --;
        }
    }


    /**
     * 画出八个拖拽框
     */
    private void drawResizingThumbs(Graphics g, int[] directions, int x, int y, int w, int h) {
        int bx = x - XCreatorConstants.RESIZE_BOX_SIZ;
        int by = y - XCreatorConstants.RESIZE_BOX_SIZ;

        if (ArrayUtils.contains(directions, Direction.LEFT_TOP)) {
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.TOP)) {
            bx = x + ((w - XCreatorConstants.RESIZE_BOX_SIZ) / 2);
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.RIGHT_TOP)) {
            bx = x + w;
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.LEFT)) {
            bx = x - XCreatorConstants.RESIZE_BOX_SIZ;
            by = y + ((h - XCreatorConstants.RESIZE_BOX_SIZ) / 2);
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.LEFT_BOTTOM)) {
            bx = x - XCreatorConstants.RESIZE_BOX_SIZ;
            by = y + h;
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.BOTTOM)) {
            bx = x + ((w - XCreatorConstants.RESIZE_BOX_SIZ) / 2);
            by = y + h;
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.RIGHT_BOTTOM)) {
            bx = x + w;
            by = y + h;
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.RIGHT)) {
            bx = x + w;
            by = y + ((h - XCreatorConstants.RESIZE_BOX_SIZ) / 2);
            drawBox(g, bx, by);
        }
    }

    /**
     * 画每一个小拖拽框
     */
    private void drawBox(Graphics g, int x, int y) {
        g.setColor(XCreatorConstants.RESIZE_BOX_INNER_COLOR);
        g.fillRect(x, y, XCreatorConstants.RESIZE_BOX_SIZ, XCreatorConstants.RESIZE_BOX_SIZ);
        g.setColor(XCreatorConstants.RESIZE_BOX_BORDER_COLOR);
        g.drawRect(x, y, XCreatorConstants.RESIZE_BOX_SIZ, XCreatorConstants.RESIZE_BOX_SIZ);
    }

    /**
     * 画自适应布局
     */
    private void repaintFit(Graphics g, Component component, Component parent) {
        try {
            SwingUtilities.updateComponentTreeUI(component);
        } catch (Exception ex) {
        }
        ArrayList<JComponent> dbcomponents = new ArrayList<JComponent>();
        // 禁止双缓冲
        ComponentUtils.disableBuffer(component, dbcomponents);
        Graphics clipg;
        clipg = g.create(
                -designer.getArea().getHorizontalValue(),
                -designer.getArea().getVerticalValue() + designer.getParaHeight(),
                parent.getSize().width + designer.getArea().getHorizontalValue(),
                parent.getSize().height + designer.getArea().getVerticalValue());

//        BufferedImage img = CoreGraphHelper.createBufferedImage(parent.getSize().width + designer.getArea().getHorizontalValue(), parent.getSize().height + designer.getArea().getVerticalValue(), BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2d = img.createGraphics();
//        component.printAll(g2d);
//        g2d.dispose();
//        g.drawImage(img,-designer.getArea().getHorizontalValue(),-designer.getArea().getVerticalValue() + designer.getParaHeight(), (int) (parent.getSize().width*time + designer.getArea().getHorizontalValue()), (int) (parent.getSize().height*time + designer.getArea().getVerticalValue()),null);

        designer.paintContent(clipg);
        paintWatermark((Graphics2D) clipg);
        clipg.dispose();

        // 恢复双缓冲
        ComponentUtils.resetBuffer(dbcomponents);
        designer.resetEditorComponentBounds();
    }

    /**
     * 画参数面板
     */
    private void repaintPara(Graphics g, Component component, Component parent) {
        try {
            SwingUtilities.updateComponentTreeUI(component);
        } catch (Exception ex) {
        }
        ArrayList<JComponent> dbcomponents = new ArrayList<JComponent>();
        // 禁止双缓冲
        ComponentUtils.disableBuffer(component, dbcomponents);
        Graphics clipg1;
        clipg1 = g.create(-designer.getArea().getHorizontalValue(),
                -designer.getArea().getVerticalValue() ,
                parent.getSize().width + designer.getArea().getHorizontalValue(),
                designer.getParaHeight() + designer.getArea().getVerticalValue());

        designer.paintPara(clipg1);
        clipg1.dispose();

        // 恢复双缓冲
        ComponentUtils.resetBuffer(dbcomponents);
    }

}
