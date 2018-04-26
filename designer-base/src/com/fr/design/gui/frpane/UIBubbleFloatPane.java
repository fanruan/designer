package com.fr.design.gui.frpane;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.fr.base.FRContext;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.UIDialog;
import com.fr.stable.Constants;
import com.sun.awt.AWTUtilities;

/**
 * @author Jerry
 *         非模态悬浮对话框，气泡形状
 */
public abstract class UIBubbleFloatPane<T> extends BasicBeanPane<T> {
	private static final long serialVersionUID = -6386018511442190959L;
	private static int OFF_LEFT = 10;
    private static final int ARROR_PARALLEL = 30;
    private static final int ARROR_VERTICAL = 20;
    private static final int TITLE_HEIGHT = 60;
    private static final double TIME_DEFAULT = 0.5;
    private static final double TIME_GAP = 0.1;

	private BasicBeanPane<T> contentPane;
	private Rectangle bounds;
	private int arrowPosition;
	private double time = 0.5;

	private UIDialog showDialog;

	/**
	 * @param arrowPosition 箭头的位置，上下左右，暂时只处理了左边，后面用到了再说 TODO
	 * @param arrowPoint    箭头的坐标点
	 * @param contentPane   对话框中的Panel
	 */
	public UIBubbleFloatPane(int arrowPosition, Point arrowPoint, BasicBeanPane<T> contentPane) {
		this(arrowPosition, arrowPoint, contentPane, contentPane.getPreferredSize().width, contentPane.getPreferredSize().height);
	}

	/**
	 * 这个方法主要用于那些宽度和高度有变化的面板，因为显然外面的气泡要固定大小，不然忽大忽小，体验太差了
	 *
	 * @param arrowPosition 箭头的位置，上下左右，暂时只处理了左边，后面用到了再说 TODO
	 * @param arrowPoint    箭头的坐标点
	 * @param contentPane   对话框中的Panel
	 * @param width         对话框中的Panel的宽度，写死了
	 * @param height        对话框中的Panel的高度，写死了
	 */
	public UIBubbleFloatPane(int arrowPosition, Point arrowPoint, BasicBeanPane<T> contentPane, int width, int height) {
		this.contentPane = contentPane;
		this.arrowPosition = arrowPosition;
		this.time = initBoundsTime(arrowPosition, arrowPoint, width, height);
		if (arrowPosition == Constants.LEFT) {
			this.bounds = new Rectangle(arrowPoint.x - width, (int) (arrowPoint.y - height * time), width, height);
		} else if (arrowPosition == Constants.TOP) {
			this.bounds = new Rectangle((int) (arrowPoint.x - width * time), arrowPoint.y , width, height);
		} else if (arrowPosition == Constants.RIGHT) {
			this.bounds = new Rectangle(arrowPoint.x - OFF_LEFT * 2 - width, (int) (arrowPoint.y - height * time), width, height);
		}

		initComponents();
	}

    /**
     * show对话框
     * @param fatherPane 上一层界面 parentPane
     * @param ob  传入的内容，在show之前populate
     */
	public void show(JComponent fatherPane, T ob) {
		populateBean(ob);
		UIDialog dialog = showUnsizedWindow(SwingUtilities.getWindowAncestor(fatherPane));
		showDialog = dialog;
		Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
		dialog.setVisible(true);
	}

	/**
	 * 数据展现
	 */
	public void populateBean(T ob) {
		contentPane.populateBean(ob);
	}

	/**
	 * 停止编辑, 更新至最新的面板到属性保存
	 */
	public T updateBean() {
		updateContentPane();
		return contentPane.updateBean();
	}

	/**
	 * 需要实现更新内容，将updateBean传值给对象即可，在窗口消失的时候会被调用
	 */
	protected abstract void updateContentPane();

	/**
	 * 以对话框的形式弹出
	 *
	 * @param window 窗口
	 * @return 对话框
	 */
	public UIDialog showUnsizedWindow(Window window) {
		CustomShapedDialog dg = null;
		if (window instanceof Frame) {
			dg = new CustomShapedDialog((Frame) window);
		} else {
			dg = new CustomShapedDialog((Dialog) window);
		}
		if (arrowPosition == Constants.LEFT || arrowPosition == Constants.RIGHT) {
			dg.setSize(bounds.width + ARROR_PARALLEL, bounds.height + ARROR_VERTICAL);
		} else {
			dg.setSize(bounds.width + ARROR_VERTICAL, bounds.height + ARROR_PARALLEL);
		}

		dg.setLocation(bounds.x, bounds.y);
		dg.setBackground(Color.blue);
		dg.setResizable(false);
		return dg;
	}

	@Override
	protected String title4PopupWindow() {
		return null;
	}

	private double initBoundsTime(int arrowPosition, Point arrowPoint, int width, int height) {
		int x = arrowPoint.x;
		int y = arrowPoint.y;
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height - TITLE_HEIGHT;
		double time = TIME_DEFAULT;

		if (arrowPosition == Constants.LEFT || arrowPosition == Constants.RIGHT) {
			while (y + time * height > screenHeight && time > 0) {
				time -= TIME_GAP;
			}

			while (y - (1 - time) * height < 0 && time < 1) {
				time += TIME_GAP;
			}
		} else if (arrowPosition == Constants.TOP) {
			while (x + time * width > screenWidth && time > 0) {
				time -= TIME_GAP;
			}

			while (x - (1 - time) * width < 0 && time < 1) {
				time += TIME_GAP;
			}
		}
		return 1 - time;
	}

	private AWTEventListener awt = new AWTEventListener() {
		public void eventDispatched(AWTEvent event) {
			doSomeInAll(event);
		}
	};

	private void doSomeInAll(AWTEvent event) {
		if (event instanceof MouseEvent) {
			MouseEvent mv = (MouseEvent) event;
			if (mv.getClickCount() > 0) {
				Point point = new Point((int) (mv.getLocationOnScreen().getX()) - 2 * OFF_LEFT, (int) mv.getLocationOnScreen().getY());
				// 判断鼠标点击是否在边界内
				if (!containsPoint(point) && showDialog != null) {
					updateContentPane();
					showDialog.setVisible(false);
					Toolkit.getDefaultToolkit().removeAWTEventListener(awt);
				}
			}
		}
	}

    private boolean containsPoint(Point point) {
        if(arrowPosition == Constants.TOP){
            //箭头和按钮也算在pane内
            Rectangle judgedBounds = new Rectangle(bounds.x, bounds.y - OFF_LEFT * 2, bounds.width, bounds.height + OFF_LEFT * 2 + OFF_LEFT);
           return judgedBounds.contains(point);
        }
        return bounds.contains(point);
    }

	private void initComponents() {
		if(arrowPosition == Constants.LEFT || arrowPosition == Constants.RIGHT) {
			this.setBounds(20, 10, bounds.width, bounds.height);
		} else {
			this.setBounds(10, 10, bounds.width, bounds.height);
		}
		
		this.setLayout(new BorderLayout());
		this.add(contentPane, BorderLayout.CENTER);
	}

	private class CustomShapedDialog extends UIDialog {

        private static final int GAP_SMALL = 10;
        private static final int GAP = 20;
        private static final int GAP_BIG = 30;

		public CustomShapedDialog(Frame parent) {
			super(parent);
			this.initComponents();
		}

		public CustomShapedDialog(Dialog parent) {
			super(parent);
			this.initComponents();
		}


		private Shape getCustomShap() {
			Polygon polygon = new Polygon();
			if (arrowPosition == Constants.LEFT) {
				polygon.addPoint(GAP_SMALL, 0);
				polygon.addPoint(bounds.width + GAP_BIG, 0);
				polygon.addPoint(bounds.width + GAP_BIG, bounds.height + GAP);
				polygon.addPoint(GAP_SMALL, bounds.height + GAP);
				polygon.addPoint(GAP_SMALL, (int) ((bounds.height + GAP) * time) - GAP_SMALL);
				polygon.addPoint(0, (int) ((bounds.height + GAP) * time - GAP));
				polygon.addPoint(GAP_SMALL, (int) ((bounds.height + GAP) * time - GAP_BIG));
				polygon.addPoint(GAP_SMALL, 0);
			} else if (arrowPosition == Constants.TOP) {
				polygon.addPoint(0, GAP_SMALL);
				polygon.addPoint((int) (bounds.width * time + GAP_SMALL), GAP_SMALL);
				polygon.addPoint((int) (bounds.width * time + GAP), 0);
				polygon.addPoint((int) (bounds.getWidth() * time + GAP_BIG), GAP_SMALL);
				polygon.addPoint(bounds.width + GAP, GAP_SMALL);
				polygon.addPoint(bounds.width + GAP, bounds.height + GAP_BIG);
				polygon.addPoint(0, bounds.height + GAP_BIG);
				polygon.addPoint(0, GAP_SMALL);
			} else if (arrowPosition == Constants.RIGHT) {
				polygon.addPoint(0, 0);
				polygon.addPoint(bounds.width + GAP, 0);
				polygon.addPoint(bounds.width + GAP, (int) ((bounds.height + GAP) * time) - GAP_BIG);
				polygon.addPoint(bounds.width + GAP_BIG, (int) ((bounds.height + GAP) * time) - GAP);
				polygon.addPoint(bounds.width + GAP, (int) ((bounds.height + GAP) * time) - GAP_SMALL);
				polygon.addPoint(bounds.width + GAP, bounds.height + GAP);
				polygon.addPoint(0, bounds.height + GAP);
				polygon.addPoint(0, 0);
			}

			return polygon;
		}

		private Shape getShape4Board() {
			Polygon polygon = new Polygon();
			if (arrowPosition == Constants.LEFT) {
				polygon.addPoint(GAP_SMALL, 1);
				polygon.addPoint(bounds.width + GAP_BIG - 1, 1);
				polygon.addPoint(bounds.width + GAP_BIG - 1, bounds.height + GAP - 1);
				polygon.addPoint(GAP_SMALL + 1, bounds.height + GAP - 1);
				polygon.addPoint(GAP_SMALL + 1, (int) ((bounds.height + GAP) * time) - GAP_SMALL);
				polygon.addPoint(1, (int) ((bounds.height + GAP) * time - GAP));
				polygon.addPoint(GAP_SMALL + 1, (int) ((bounds.height + GAP) * time - GAP_BIG));
				polygon.addPoint(GAP_SMALL + 1, 1);
			} else if (arrowPosition == Constants.TOP) {
				polygon.addPoint(1, GAP_SMALL + 1);
				polygon.addPoint((int) (bounds.width * time + GAP_SMALL), GAP_SMALL + 1);
				polygon.addPoint((int) (bounds.width * time + GAP), 1);
				polygon.addPoint((int) (bounds.getWidth() * time + GAP_BIG), GAP_SMALL + 1);
				polygon.addPoint(bounds.width + GAP - 1, GAP_SMALL + 1);
				polygon.addPoint(bounds.width + GAP - 1, bounds.height + GAP_BIG - 1);
				polygon.addPoint(1, bounds.height + GAP_BIG - 1);
				polygon.addPoint(1, GAP_SMALL + 1);
			} else if(arrowPosition == Constants.RIGHT) {
				polygon.addPoint(1, 1);
				polygon.addPoint(bounds.width + GAP - 1, 1);
				polygon.addPoint(bounds.width + GAP - 1, (int) ((bounds.height + GAP) * time) - GAP_BIG);
				polygon.addPoint(bounds.width + GAP_BIG - 1, (int) ((bounds.height + GAP) * time) - GAP);
				polygon.addPoint(bounds.width + GAP - 1, (int) ((bounds.height + GAP) * time) - GAP_SMALL);
				polygon.addPoint(bounds.width + GAP - 1, bounds.height + GAP - 1);
				polygon.addPoint(1, bounds.height + GAP - 1);
				polygon.addPoint(1, 0);
			}
			return polygon;
		}

		/**
		 * 画出界面的样式, 边框等.
		 */
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			Stroke oldStroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
			g2d.setColor(new Color(51, 51, 51));
			g2d.drawPolygon((Polygon) getShape4Board());
			g2d.setStroke(oldStroke);
		}

		protected void initComponents() {
			setUndecorated(true);
			try {
				AWTUtilities.setWindowShape(CustomShapedDialog.this, this.getCustomShap());
			} catch (UnsupportedOperationException e) {
				FRContext.getLogger().info("Not support");
			}
			final JPanel contentPane = (JPanel) this.getContentPane();
			this.setLayout(null);
			contentPane.add(UIBubbleFloatPane.this);
			setVisible(true);
		}

		/**
		 * 检查
		 */
		public void checkValid() throws Exception {

		}
	}
}