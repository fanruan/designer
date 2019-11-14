package com.fr.design.dialog;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.iscrollbar.UIScrollBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public abstract class BasicScrollPane<T> extends BasicBeanPane<T>{
	private static final long serialVersionUID = -4293765343535336275L;
	private static final int MAXVALUE = 100;
    private static final int DET_WIDTH_OVER_HEIGHT = 4;
    private static final int DET_HEIGHT = 5;
    private static final int DET_WIDTH = 12;
    private static final int MOUSE_WHELL_SPEED = 5;
	private int maxheight = 280;
	private int beginY = 0;
	protected Color original;

	private UIScrollBar scrollBar;
	protected JPanel leftcontentPane;


	protected abstract JPanel createContentPane();

	protected BasicScrollPane() {
		enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
		original = this.getBackground();
		this.setLayout(new BarLayout());
		scrollBar = new UIScrollBar(JScrollBar.VERTICAL) {
			private static final long serialVersionUID = 155777947121777223L;

			@Override
			public int getVisibleAmount() {
				int preferheight = leftcontentPane.getPreferredSize().height;
				if(preferheight <= 0) {
					return 0;
				}
				int e = MAXVALUE * (getHeight() - 1) / preferheight;
				setVisibleAmount(e);
				return e;
			}

			@Override
			public int getMaximum() {
				return MAXVALUE;
			}

		};
		this.add(scrollBar);
		scrollBar.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				ajustValues();
			}
		});
		// august:鼠标滚轮滑动事件
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int value = scrollBar.getValue();
				value += MOUSE_WHELL_SPEED * e.getWheelRotation();
				scrollBar.setValue(value);
				ajustValues();
			}
		});

		layoutContentPane();
	}
	
	//上层pane已经有了scroll，需要把事件屏蔽掉
	protected BasicScrollPane(boolean noScroll) {
		original = this.getBackground();
		this.setLayout(new BarLayout());
		scrollBar = new UIScrollBar(JScrollBar.VERTICAL) {
			private static final long serialVersionUID = 155777947121777223L;

			@Override
			public int getVisibleAmount() {
				int preferheight = leftcontentPane.getPreferredSize().height;
				if(preferheight <= 0) {
					return 0;
				}
				int e = MAXVALUE * (getHeight() - 1) / preferheight;
				setVisibleAmount(e);
				return e;
			}

			@Override
			public int getMaximum() {
				return MAXVALUE;
			}

		};
		this.add(scrollBar);
		layoutContentPane();
	}

	protected void layoutContentPane() {
		leftcontentPane = createContentPane();
		leftcontentPane.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 5, original));
		this.add(leftcontentPane);
	}

	/**
	 * august:不容易啊 还要用笔画图立个方程才计算出来
	 * 
	 * @param
	 */
	protected void ajustValues() {
		doLayout();
	}

	protected class BarLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
			//do nothing
		}

		@Override
		public void removeLayoutComponent(Component comp) {
			//do nothing
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return leftcontentPane.getPreferredSize();
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return leftcontentPane.getMinimumSize();
		}

		@Override
		public void layoutContainer(Container parent) {
			if(getHeight() >= leftcontentPane.getPreferredSize().height) {
				scrollBar.setEnabled(false);
				scrollBar.setVisible(false);
			} else {
                boolean show = isShowScrollBar();
                scrollBar.setEnabled(show);
                scrollBar.setVisible(show);
			}
			maxheight = getHeight() - DET_HEIGHT;
			if ((MAXVALUE - scrollBar.getVisibleAmount()) == 0) {
				beginY = 0;
			} else {
				int preferheight = leftcontentPane.getPreferredSize().height;
				int value = scrollBar.getValue();

				int baseValue = MAXVALUE - scrollBar.getVisibleAmount();
				beginY = baseValue == 0 ? 0 : value * (preferheight - maxheight) / baseValue;
				if(MAXVALUE - scrollBar.getVisibleAmount() != 0) {
					beginY = value * (preferheight - maxheight) / (MAXVALUE - scrollBar.getVisibleAmount());
				}
			}
            setLeftContentPaneBounds(parent, scrollBar, beginY, maxheight);
			leftcontentPane.validate();
		}

	}

	protected void setLeftContentPaneBounds(Container parent, UIScrollBar scrollBar, int beginY, int maxheight) {
        int width = parent.getWidth();
        int height = parent.getHeight();
        if (leftcontentPane.getPreferredSize().height > maxheight && scrollBar.isVisible()) {
            leftcontentPane.setBounds(0, -beginY, width - scrollBar.getWidth() + getOverWidth() - DET_WIDTH_OVER_HEIGHT, height + beginY);
            scrollBar.setBounds(width - scrollBar.getWidth() - 1, 0, scrollBar.getWidth(), height);
        } else {
			int hideBarWidth = hideBarWidth() ? scrollBar.getWidth() : 0;
			leftcontentPane.setBounds(0, 0, width - DET_WIDTH + hideBarWidth, height);
        }
    }

    protected int getOverWidth(){
		return 0;
	}

	protected boolean hideBarWidth(){
    	return false;
	}

    protected boolean isShowScrollBar() {
        return true;
    }

	@Override
	public T updateBean() {
		return null;
	}

	/**
	 * 用于在调用removeAll以后恢复原来pane的结构，放在这边是因为BarLayout是内部类
	 * @param pane
	 */
	public void reloaPane(JPanel pane){
		this.setLayout(new BarLayout());
		this.add(scrollBar);
		leftcontentPane = pane;
		leftcontentPane.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 5, original));
		this.add(leftcontentPane);
	}
}