package com.fr.design.gui.frpane;

import com.fr.design.dialog.BasicPane;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;


/**
 * 用于属性表中的面板，主要是为了给包含于这个面板内部的众多UI控件加监听事件，在UI控件改变的时候，通知模板做相应的变化
 */
public abstract class AbstractAttrNoScrollPane extends BasicPane {
	private static final int DEFAULT_HEIGHT = 250;
	private static boolean hasChangeListener;

	protected JPanel leftContentPane;
	protected Color original;

	private AttributeChangeListener listener;
	private String globalName = "";

	protected AbstractAttrNoScrollPane() {
		initAll();
	}

	protected void initAll() {
		enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
		original = this.getBackground();
		this.setLayout(new BorderLayout());
		hasChangeListener = false;
		initContentPane();
		initAllListeners();
	}

	/**
	 * 后台初始化所有事件.
	 */
	public void initAllListeners() {
		initListener(AbstractAttrNoScrollPane.this);
	}

	protected void initContentPane() {
		leftContentPane = createContentPane();
		leftContentPane.setBorder(BorderFactory.createMatteBorder(10, 10, 0, 0, original));
		this.add(leftContentPane, BorderLayout.CENTER);
	}

	protected abstract JPanel createContentPane();

	/**
	 * august:不容易啊 还要用笔画图立个方程才计算出来
	 */
	protected void adjustValues() {
		doLayout();
	}

	protected void removeAttributeChangeListener() {
		this.listener = null;
		hasChangeListener = false;
	}


	public void initListener(Container parentComponent) {
		for (int i = 0; i < parentComponent.getComponentCount(); i++) {
			Component tmpComp = parentComponent.getComponent(i);

			if (tmpComp instanceof Container) {
				initListener((Container) tmpComp);
			}
			if (tmpComp instanceof GlobalNameObserver) {
				((GlobalNameObserver) tmpComp).registerNameListener(new GlobalNameListener() {
					public void setGlobalName(String name) {
						globalName = name;
					}

					public String getGlobalName() {
						return globalName;
					}
				});
			}
			if (tmpComp instanceof UIObserver) {
				((UIObserver) tmpComp).registerChangeListener(new UIObserverListener() {
					@Override
					public void doChange() {
						attributeChanged();
					}
				});
			}
		}
	}

	/**
	 * 是否有改变监听
	 * @return  是则返回true
	 */
	public static boolean isHasChangeListener() {
		return hasChangeListener;
	}

	/**
	 * 返回预定义的大小.
	 */
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, DEFAULT_HEIGHT);
	}


	/**
	 * 返回绑定的属性事件.
	 * @param listener  增加监听
	 */
	public void addAttributeChangeListener(AttributeChangeListener listener) {
		this.listener = listener;
		hasChangeListener = true;
	}

	/**
	 * 响应属性事件.
	 */
	public void attributeChanged() {
		synchronized (this) {
			if (listener != null) {
				listener.attributeChange();
			}
		}
	}

	/**
	 * 返回图标的路径
	 */
	public String getIconPath() {
		// 默认为空，子类有需要再重写
		return StringUtils.EMPTY;
	}

	/**
	 * 界面标题
	 * @return 标题
	 */
	public String title4PopupWindow() {
		// 默认为空，子类有需要再重写
		return StringUtils.EMPTY;
	}

	/**
	 * 设置选中的ID, 用于双击展示界面.
	 */
	public void setSelectedByIds(int level, String... id) {

	}

	public String getGlobalName(){
		return globalName;
	}

	/**
	 * 主要用于图表设计器
	 * @return 是
	 */
	public boolean isNeedPresentPaneWhenFilterData(){
		return true;
	}

}