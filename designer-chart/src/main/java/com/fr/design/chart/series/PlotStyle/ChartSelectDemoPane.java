package com.fr.design.chart.series.PlotStyle;

import com.fr.design.dialog.BasicPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.stable.ArrayUtils;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 *  图表选中demo的类. 用于 选中点击, 悬浮状态, 可以继承, 改变画的内容.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-10-27 下午03:50:28
 */
public class ChartSelectDemoPane extends BasicPane implements UIObserver, MouseListener {
	private static final long serialVersionUID = 7715973616632567352L;

	public boolean isPressing;

	// 所有统一参与的点击状态类. 相当于Group
	protected ChartSelectDemoPane[] demoList = new ChartSelectDemoPane[0];

	protected boolean isRollOver;
	private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

	public void setDemoGroup(ChartSelectDemoPane[] demos) {
		this.demoList = demos;
	}

	@Override
	protected String title4PopupWindow() {
		return "";
	}

    /**
     * 鼠标点击
     * @param e 事件
     */
	public void mouseClicked(MouseEvent e) {
		// list中的所有的都弄成非选中状态.
		if(this.isEnabled()){
            for (int i = 0; i < ArrayUtils.getLength(demoList); i++) {
                demoList[i].isRollOver = false;
                demoList[i].isPressing = false;
            }

            this.isPressing = true;

            fireStateChange();

            for (int i = 0; i < ArrayUtils.getLength(demoList); i++) {
                demoList[i].checkBorder();
                demoList[i].repaint();
            }
        }
	}

    /**
     * 注册监听
     * @param l 监听
     */
	public void addChangeListener(ChangeListener l) {
		changeListeners.add(l);
	}

	private void fireStateChange() {
		for (int i = 0; i < changeListeners.size(); i++) {
			changeListeners.get(i).stateChanged(new ChangeEvent(this));
		}
	}

    /**
     * 鼠标按压
     * @param me 事件
     */
	public void mousePressed(MouseEvent me) {
	}

    /**
     * 鼠标放开
     * @param me 事件
     */
	public void mouseReleased(MouseEvent me) {
	}

    /**
     * 鼠标进入
     * @param me 事件
     */
	public void mouseEntered(MouseEvent me) {
		if(this.isEnabled()){
            for (int i = 0; i < ArrayUtils.getLength(demoList); i++) {
                demoList[i].isRollOver = false;
            }
            isRollOver = true;

            for (int i = 0; i < ArrayUtils.getLength(demoList); i++) {
                demoList[i].checkBorder();
                demoList[i].repaint();
            }
        }
	}

    /**
     * 鼠标移出
     * @param me 事件
     */
	public void mouseExited(MouseEvent me) {
		// 限制在pane范围内!
		if(this.isEnabled()){
            int x = me.getX();
            int y = me.getY();

            Dimension d = this.getPreferredSize();
            if (inDimension(d, x, y)) {
                isRollOver = true;
            } else {
                isRollOver = false;
            }

            for (int i = 0; i < ArrayUtils.getLength(demoList); i++) {
                demoList[i].checkBorder();
                demoList[i].repaint();
            }
        }
	}
	
	private boolean inDimension(Dimension d, int x, int y) {
		return x < d.getWidth() && y < d.getHeight() && x > 0 && y > 0;
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
	public void registerChangeListener(final UIObserverListener listener) {
		changeListeners.add(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				listener.doChange();
			}
		});
	}

	/**
	 * 组件是否需要响应添加的观察者事件
	 *
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

	/**
	 * 修改边框颜色
	 */
	public void checkBorder() {
		this.setBorder(null);

	}
}