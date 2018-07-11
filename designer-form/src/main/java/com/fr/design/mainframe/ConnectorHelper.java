package com.fr.design.mainframe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.stable.Constants;
import com.fr.base.GraphHelper;
import com.fr.design.designer.beans.ConnectorCreator;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.form.ui.Connector;

public class ConnectorHelper {
    //这个类是用来画连接线的，暂时用不到

    public static final int NEAR = 5;
    private static double ratio = 0.5;
    private final static int ADSORPTION = 15; // 吸附距离
    private ArrayList<Point> drawingPoint;
    private FormDesigner designer;
    private boolean drawing;

    public ConnectorHelper(FormDesigner formEditor) {
        this.designer = formEditor;
    }

    public void resetConnector(Connector connector) {
        ConnectorCreator cc = new ConnectorCreator(designer.getTarget().getContainer(), connector.getStartPoint(), connector.getEndPoint());
        connector.addAll(cc.createPointList());
    }

    public boolean drawLining() {
        return this.drawing;
    }

    public void setDrawLine(boolean d) {
        this.drawing = d;
    }

    private boolean near(Point p1, Point p2) {
        return p1.x - p2.x < NEAR && p2.x - p1.x < NEAR && p1.y - p2.y < NEAR && p2.y - p1.y < NEAR;
    }

    private Point getNearPoint(MouseEvent e, Rectangle r) {
        Point p1 = new Point((int) (r.x + r.getWidth() * ratio), r.y);
        Point p2 = new Point((int) (r.x + r.getWidth()), (int) (r.y + r.getHeight() * ratio));
        Point p3 = new Point((int) (r.x + r.getWidth() * (1 - ratio)), (int) (r.y + r.getHeight()));
        Point p4 = new Point(r.x, (int) (r.y + r.getHeight() * (1 - ratio)));
        Point p = new Point(e.getX() + designer.getArea().getHorizontalValue(), e.getY() + designer.getArea().getVerticalValue());
        if (near(p, p1)) {
            return p1;
        } else if (near(p, p2)) {
            return p2;
        } else if (near(p, p3)) {
            return p3;
        } else if (near(p, p4)) {
            return p4;
        }
        return null;
    }

    private ArrayList<Point> createDefalutNode(Point startPoint, Point endPoint) {
        long s = System.currentTimeMillis();
        ConnectorCreator cc = new ConnectorCreator(designer.getTarget().getContainer(), new Point(startPoint), new Point(endPoint));
        ArrayList<Point> p = cc.createPointList();
        long e = System.currentTimeMillis();
        return p;
    }

    public void drawAuxiliaryLine(Graphics g) {
        Point startPoint = designer.getStateModel().getStartPoint();
        Point endPoint = designer.getStateModel().getEndPoint();
        drawingPoint = createDefalutNode(startPoint, endPoint);
        Point[] p = drawingPoint.toArray(new Point[drawingPoint.size()]);
        g.setColor(Color.green);
        for (int i = 0; i < p.length - 1; i++) {
            GraphHelper.drawLine(g, p[i].x - designer.getArea().getHorizontalValue(), p[i].y
                            - designer.getArea().getVerticalValue(), p[i + 1].x - designer.getArea().getHorizontalValue(),
                    p[i + 1].y - designer.getArea().getVerticalValue(), Constants.LINE_HAIR);
        }
    }

    public void createDefalutLine() {
        if (drawingPoint != null
                && drawingPoint.size() > 1
                && ConnectorCreator.getMinimumDistance(drawingPoint.get(0), drawingPoint.get(drawingPoint.size() - 1)) > ADSORPTION) {
            ((XWAbsoluteLayout) designer.getRootComponent()).addConnector(new Connector().addAll(drawingPoint));
        }
        drawingPoint = null;
    }

    public Point getNearWidgetPoint(MouseEvent e) {
        BoundsWidget widget;
        Point p = null;
        for (int i = 0, size = designer.getTarget().getContainer().getWidgetCount(); i < size; i++) {
            widget = ((BoundsWidget) designer.getTarget().getContainer().getWidget(i));
            if (widget.isVisible()) {
                if ((p = getNearPoint(e, widget.getBounds())) != null) {
                    break;
                }
            }
        }
        return p;
    }

}