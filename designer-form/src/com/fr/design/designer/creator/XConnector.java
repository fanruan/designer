package com.fr.design.designer.creator;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JComponent;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.designer.beans.location.Direction;
import com.fr.form.ui.Connector;
import com.fr.design.form.util.XCreatorConstants;

//这个类仅仅是对Connector包装下，使得表单设计页面逻辑更清晰
public class XConnector implements XComponent {

	private XWAbsoluteLayout parent;
	private Connector connector;
	public static Cursor connectorCursor;
	public static Cursor moveCursor;

	static {
		connectorCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				BaseUtils.readImage("/com/fr/design/images/form/designer/cursor/connectorcursor.png"), new Point(0, 0),
				"connector");
		moveCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				BaseUtils.readImage("/com/fr/design/images/form/designer/cursor/move.png"), new Point(16, 16),
				"move");
	}

	public XConnector(Connector connector, XWAbsoluteLayout xWAbsoluteLayout) {
		this.connector = connector;
		this.parent = xWAbsoluteLayout;
	}

	private boolean near(int x1, int x2) {
		return x1 - x2 >= -XCreatorConstants.RESIZE_BOX_SIZ && x1 - x2 <= XCreatorConstants.RESIZE_BOX_SIZ;
	}

	private Rectangle createRectangle(Point A, Point B) {
		return new Rectangle(Math.min(A.x, B.x), Math.min(A.y, B.y), Math.abs(A.x - B.x), Math.abs(A.y - B.y));
	}

	public XLayoutContainer getParentXLayoutContainer() {
		return parent;
	}

	public Connector getConnector() {
		return connector;
	}

	@Override
	public Rectangle getBounds() {
		return createRectangle(connector.getStartPoint(), connector.getEndPoint());
	}

	@Override
	public void setBounds(Rectangle oldbounds) {

	}

	public JComponent createToolPane(BaseJForm jform, FormDesigner formEditor) {
		return jform.getEditingPane();
	}

	public ConnectorDirection getDirection(int x, int y) {

		Point pS = connector.getStartPoint();
		if (near(x, pS.x) && near(y, pS.y)) {
			return new ConnectorDirection(pS, null);
		}
		Point pE = connector.getEndPoint();
		if (near(x, pE.x) && near(y, pE.y)) {
			return new ConnectorDirection(null, pE);
		}

		Point p1 = pS;
		Point p2;
		Point p;
		int size = connector.getPointCount();
		for (int i = 0; i < size - 1; i++) {
			p2 = connector.getPointIndex(i + 1);
			p = connector.getMidPoint(p1, p2);
			if (near(p.x, x) && near(p.y, y)) {
				return new ConnectorDirection(p1, p2);
			}
			p1 = p2;
		}
		return new ConnectorDirection();
	}

	public class ConnectorDirection implements Direction {

		private Point A;
		private Point B;
		private Rectangle oldbounds;

		private ConnectorDirection() {

		}

		private ConnectorDirection(Point A, Point B) {
			this.A = A;
			this.B = B;
		}

		private Cursor getCursor() {
			if (A == null || B == null) {
				return A == B ? Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR) : moveCursor;
			}
			if (A.x == B.x) {
				return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
			} else if (A.y == B.y) {
				return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
			} else {
				return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
			}
		}

		private void setXY(Point p1, Point p2, int dx, int dy, Rectangle oldbounds) {
			if (p1.x == p2.x) {
				p1.x = p2.x = oldbounds.x + dx;
			} else {
				p1.y = p2.y = oldbounds.y + dy;
			}
		}

		@Override
		public void drag(int dx, int dy, FormDesigner designer) {
			if (A == null || B == null) {
				if (A != null) {
					A.x = oldbounds.x + dx;
					A.y = oldbounds.y + dy;
				} else if (B != null) {
					B.x = oldbounds.x + dx;
					B.y = oldbounds.y + dy;
				} else {
					setBounds(new Rectangle(oldbounds.x + dx, oldbounds.y + dy, oldbounds.width, oldbounds.height));
				}
				designer.getDrawLineHelper().resetConnector(connector);
				return;
			}
			if (A == connector.getStartPoint()) {
				this.A = new Point(A.x, A.y);
				connector.addPoint(1, A);
			}
			if (connector.getEndPoint() == B) {
				this.B = new Point(B.x, B.y);
				connector.addPoint(connector.getPointCount() - 1, B);
			}
			setXY(A, B, dx, dy, oldbounds);

		}

		public Rectangle getBounds() {
			if (A == null || B == null) {
				if (A != null) {
					return new Rectangle(A.x, A.y, 0, 0);
				} else if (B != null) {
					return new Rectangle(B.x, B.y, 0, 0);
				} else {
					return XConnector.this.getBounds();
				}
			}
			return createRectangle(A, B);
		}

		@Override
		public int getActual() {
			return 0;
		}

		@Override
		public void updateCursor(FormDesigner formEditor) {
			formEditor.setCursor(getCursor());
		}

		@Override
		public void backupBounds(FormDesigner formEditor) {
			oldbounds = getBounds();
		}
	}
}