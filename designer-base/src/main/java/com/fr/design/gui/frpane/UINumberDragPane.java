package com.fr.design.gui.frpane;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.style.NumberDragBar;
import com.fr.general.Inter;

public class UINumberDragPane extends BasicBeanPane<Double> implements GlobalNameObserver {
	private static final long serialVersionUID = -8681716725163358249L;
	private NumberDragBar dragBar;
	private UISpinner spinner;
	private boolean isEditing = false;
	private String numberDargPaneName = "";
	private GlobalNameListener globalNameListener = null;

    /**
     * 带说明
     * @param value 带说明
     */
	public void userEvent(double value) {

	}

	public UINumberDragPane(double minValue, double maxValue) {
		this(minValue, maxValue, 1);
	}

	public UINumberDragPane(double minValue, double maxValue, double dierta) {
		dragBar = new NumberDragBar((int) minValue, (int) maxValue);
		spinner = new UISpinner(minValue, maxValue, dierta, minValue);
		spinner.setGlobalName(Inter.getLocText("StyleAlignment-Text_Rotation"));
		this.setLayout(new BorderLayout(4, 0));
		this.add(spinner, BorderLayout.EAST);
		this.add(dragBar, BorderLayout.CENTER);
		dragBar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (globalNameListener != null && shouldResponseNameListener()) {
					globalNameListener.setGlobalName(numberDargPaneName);
				}
				spinner.setValue(dragBar.getValue());
				if (isEditing) {
					userEvent(updateBean());
				}
			}
		});
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (globalNameListener != null && shouldResponseNameListener()) {
					globalNameListener.setGlobalName(numberDargPaneName);
				}
				dragBar.setValue((int) spinner.getValue());
				if (isEditing) {
					userEvent(updateBean());
				}
			}
		});
	}

	/**
     * 添加事件
	 * @param l 事件
	 */
	public void addChangeListener(ChangeListener l) {
		spinner.addChangeListener(l);

	}

	/**
     * 移除事件
	 * @param l 事件
	 */

	public void removeChangeListener(ChangeListener l) {
		spinner.removeChangeListener(l);
	}

	/**
	 * @param name
	 */
	public void setGlobalName(String name) {
		numberDargPaneName = name;
	}

	@Override
	public void populateBean(Double ob) {
		isEditing = false;
		dragBar.setValue(ob.intValue());
		spinner.setValue(ob);
		isEditing = true;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = new Dimension();
		dim.width = super.getPreferredSize().width;
		dim.height = super.getPreferredSize().height + 2;
		return dim;
	}

    public void setEnabled(boolean enabled) {
        dragBar.setEnabled(enabled);
        spinner.setEnabled(enabled);
    }

	@Override
	public Double updateBean() {
		return spinner.getValue();
	}

	@Override
	protected String title4PopupWindow() {
		return null;
	}

	/**
	 * 注册
	 * @param listener 观察者监听事件
	 */
	public void registerNameListener(GlobalNameListener listener) {
		globalNameListener = listener;
	}

	/**
	 * 是否应该响应
	 * @return 是否应该响应
	 */
	public boolean shouldResponseNameListener() {
		return true;
	}
}