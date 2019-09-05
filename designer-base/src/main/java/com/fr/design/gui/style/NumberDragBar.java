package com.fr.design.gui.style;

import com.fr.design.constants.UIConstants;
import com.fr.log.FineLoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;

/**
 * 
 * @author zhou
 * @since 2012-5-28下午2:37:37
 */
public class NumberDragBar extends JComponent {

	private ChangeListener changeListener = null;

	private int minValue;
	private int maxValue;

	private int value = 0;

	private GeneralPath path;
	int x = 4;
	private static int H = 15;// 线的位置
    private static final int WIDTH_ADJUST = 8;
    private static final int X_ADJUST = 4;
	private boolean isMoveingOnButton = false;
	private boolean isPressing;

	public NumberDragBar(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		initLister();
	}

	private void initLister() {
		MouseAdapter mouseadaper = new MouseDragEvent();
		this.addMouseListener(mouseadaper);
		this.addMouseMotionListener(mouseadaper);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, 20);
	}

	@Override
	protected void paintComponent(Graphics g) {
		int width = this.getWidth();
		//x值在这里计算，setValue时,有时会因为组件还没画，获取到的是0
		x = (value - minValue) * (width - WIDTH_ADJUST) / (maxValue - minValue) + X_ADJUST;
		Graphics2D g2 = (Graphics2D)g;
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(qualityHints);
		g2.setColor(isEnabled() ? UIConstants.FONT_COLOR : UIManager.getColor("Label.disabledForeground"));
		g2.drawLine(4, H, width - 4, H);

		getPath();
		if (isPressing) {
			g2.setColor(UIConstants.PRESSED_DARK_GRAY);
		} else if (isMoveingOnButton) {
			g2.setColor(UIConstants.LIGHT_BLUE);
		} else {
			g2.setColor(new Color(248, 248, 247));
		}
		g2.fill(path);
		g2.setColor(UIConstants.LINE_COLOR);
		g2.draw(path);

		g2.setColor(isEnabled() ? UIConstants.FONT_COLOR : UIManager.getColor("Label.disabledForeground"));
		g2.drawString(String.valueOf(minValue), 2, 10);
		g2.drawString(String.valueOf(maxValue), width - 10 * String.valueOf(maxValue).length(), 10);
		if(minValue < 0) {
			g2.drawString("0", width / 2 - 2, 10);
		}
	}

    /**
     * 添加响应
     * @param changeListener 响应
     */
	public void addChangeListener(ChangeListener changeListener) {
		this.changeListener = changeListener;
	}

    /**
     * 删除响应
     * @param changeListener 响应
     */
	public void removeChangeListener(ChangeListener changeListener) {
		this.changeListener = null;
	}

	private void fireChanged() {
		if (changeListener != null) {
			changeListener.stateChanged(new ChangeEvent(NumberDragBar.this));
		}
	}

	public int getValue() {
		int width = getWidth();
		value = (x - X_ADJUST) * (maxValue - minValue) / (width - WIDTH_ADJUST) + minValue;
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if(getWidth() <= 0) {
						Thread.sleep(500);
					}
//					int width = getWidth();
//					x = (value - minValue) * (width - WIDTH_ADJUST) / (maxValue - minValue) + X_ADJUST;
					validate();
					repaint();
					revalidate();
				} catch (InterruptedException e) {
                    FineLoggerFactory.getLogger().error(e.toString());
					Thread.currentThread().interrupt();
				}
			}
		});
		thread.start();
	}

	private void getPath() {
		GeneralPath path = new GeneralPath();
		path.moveTo(x, H - 3);
		path.lineTo(x + X_ADJUST, H + X_ADJUST);
		path.lineTo(x - X_ADJUST, H + X_ADJUST);
		path.closePath();
		this.path = path;
	}

    private boolean moveAndPressInPosition(int xx, int yy, int width){
        return (yy <= H + 2) && (yy >= H - 2) && (xx <= width - X_ADJUST) && (xx >= X_ADJUST);
    }

    private boolean clickInPosition(int xx, int yy, int width){
        return (yy <= H + X_ADJUST) && (yy >= H - X_ADJUST) && (xx <= width - X_ADJUST) && (xx >= X_ADJUST);
    }

    private boolean dragInPosition(int xx, int width){
        return isPressing && (xx <= width - X_ADJUST) && (xx >= X_ADJUST);
    }

    private class MouseDragEvent extends MouseAdapter{
        public void mouseMoved(MouseEvent e) {
            if(!isEnabled()) {
                return;
            }
            int xx = e.getX();
            int yy = e.getY();
            int width = getWidth();
            if (moveAndPressInPosition(xx, yy, width)) {
                isMoveingOnButton = true;
                repaint();
            } else {
                isMoveingOnButton = false;
                repaint();
            }
        }

        public void mouseClicked(MouseEvent e) {
            if(!isEnabled()) {
                return;
            }
            int xx = e.getX();
            int yy = e.getY();
            int width = getWidth();
            if (clickInPosition(xx, yy, width)) {
                x = xx;
                repaint();
                fireChanged();
            }
        }

        public void mouseDragged(MouseEvent e) {
            if(!isEnabled()) {
                return;
            }
            int xx = e.getX();
            int width = getWidth();
            if (dragInPosition(xx, width)) {
                x = xx;
                repaint();
                fireChanged();
            }

        }

        public void mousePressed(MouseEvent e) {
            if(!isEnabled()) {
                return;
            }
            int xx = e.getX();
            int yy = e.getY();
            int width = getWidth();
            if (moveAndPressInPosition(xx, yy, width)) {
                isPressing = true;
                repaint();
            } else {
                isPressing = false;
                repaint();
            }
        }

        public void mouseReleased(MouseEvent e) {
            if(!isEnabled()) {
                return;
            }
            isPressing = false;
            repaint();
        }
    }
}