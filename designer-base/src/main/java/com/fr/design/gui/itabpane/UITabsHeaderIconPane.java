package com.fr.design.gui.itabpane;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.core.UITabComponent;
import com.fr.design.gui.ibutton.UITabButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * 本来想弄个延迟加载的，发现在单元格属性表那边没有意义，就算了.这个面板是纯粹的，没有与模板的任何交互操作(比如说populate() update())
 * 
 * @author zhou
 * @since 2012-5-11下午3:30:18
 */
public class UITabsHeaderIconPane extends JPanel implements UITabComponent {
	private static final long serialVersionUID = 1L;
	private static final int DIS = 30;

	private UILabel nameLabel;

	private UITabPaneCreator[] creators;
	private ArrayList<UITabButton> labels;

	private JPanel centerPane;

	private int selectedIndex = -1;

	public UITabsHeaderIconPane(final String name, UITabPaneCreator[] tabcreators) {
		this.creators = tabcreators;
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, UIConstants.LINE_COLOR));

		JPanel northTabsPane = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 42);
			}
		};
		this.add(northTabsPane, BorderLayout.NORTH);

		northTabsPane.setBorder(null);
		nameLabel = new UILabel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 18);
			}
		};
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		northTabsPane.add(nameLabel, BorderLayout.NORTH);
		nameLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.LINE_COLOR));
		JPanel tabsPane = new JPanel();
		tabsPane.setLayout(new GridLayout(1, creators.length));
		northTabsPane.add(tabsPane, BorderLayout.CENTER);
		labels = new ArrayList<UITabButton>(creators.length);
		for (int i = 0; i < creators.length; i++) {
			String iconpath = creators[i].getIconPath();// august:如果图标路径为空，就说明要用文本了
			final UITabButton label = StringUtils.isEmpty(iconpath) ? new UITabButton(creators[i].getTabName()) : new IconTabLabel(BaseUtils.readIcon(creators[i]
					.getIconPath()));
			tabsPane.add(label);
			labels.add(label);
			label.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					int newselectedIndex = labels.indexOf(label);
					if (selectedIndex != newselectedIndex) {
						setSelectedIndex(newselectedIndex);
					}
				}
			});
		}

		centerPane = new JPanel(new BorderLayout());
		centerPane.setBorder(null);
		this.add(centerPane, BorderLayout.CENTER);

		this.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				nameLabel.setText(name + '-' + creators[selectedIndex].getTabName());
				show(creators[selectedIndex].getPane());
			}
		});

		setSelectedIndex(0);

		initKeyListener();
	}

	private void initKeyListener() {
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		InputMap inputMapAncestor = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = this.getActionMap();
		inputMapAncestor.clear();
		actionMap.clear();

		inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, DEFAULT_MODIFIER), "switch");
		actionMap.put("switch", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				setSelectedIndex(selectedIndex + 1 == creators.length ? 0 : selectedIndex + 1);
			}
		});
	}

	// august: 滑动效果方法
	private void show(final JPanel panel) {
		int count = centerPane.getComponentCount();// 获取centerPanel中控件数
		List<Component> list = new ArrayList<Component>();//
		list.addAll(Arrays.asList(centerPane.getComponents()));
		if (count > 0) {// 如果centerPanel中控件数大于0就执行效果
			for (int i = 0; i < count; i++) {
				Component comp = centerPane.getComponent(i);// 获得该位置的控件

				if (comp instanceof JPanel) {
					final JPanel currentPanel = (JPanel)comp;// 获得当前panel

					// augsut:必须用多线程，因为swing是单线程的，不用就实现不了动态效果
					new Thread() {
						public void run() {
							int height = centerPane.getHeight();
							int width = centerPane.getWidth();
							int y = -height;
							for (int i = 0; i <= height; i += DIS) {
								// 设置面板位置
								currentPanel.setBounds(0, i, width, height);
								panel.setBounds(0, y, width, height);
								y += DIS;
								try {
									Thread.sleep(3);
								} catch (InterruptedException e) {
									Thread.currentThread().interrupt();
								}
							}
							if (currentPanel != panel) {
								centerPane.remove(currentPanel);// 移除当前面板
							}
							panel.setBounds(0, 0, width, height);
						}
					}.start();
					break;
				}
			}
		} else {
			panel.setBounds(0, 0, centerPane.getWidth(), centerPane.getHeight());// 设置滑动初始位置
		}

		if (!list.contains(panel)) {
			centerPane.add(panel);// 添加要切换的面板
		}

		centerPane.validate();// 重构内容面板
		centerPane.repaint();// 重绘内容面板
	}

	/**
	 * Adds a <code>ChangeListener</code> to the listener list.
	 */
	public void addChangeListener(ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

	/**
	 * removes a <code>ChangeListener</code> from the listener list.
	 */
	public void removeChangeListener(ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

	// august: Process the listeners last to first
	private void fireSelectedChanged() {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener)listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}
	}

	@Override
	public synchronized int getSelectedIndex() {
		return selectedIndex;
	}

	@Override
	public synchronized void setSelectedIndex(int newselectedIndex) {
		selectedIndex = newselectedIndex;
		fireSelectedChanged();
		for (int i = 0; i < labels.size(); i++) {
			labels.get(i).setSelectedDoNotFireListener(i == selectedIndex);
		}
	}

	private class IconTabLabel extends UITabButton {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public IconTabLabel(Icon readIcon) {
			super(readIcon);
			this.setLayout(new BorderLayout());
			this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, UIConstants.LINE_COLOR));
		}

		@Override
		protected void paintRolloverAndSelected(Graphics2D g2d, int w, int h) {
			if (!isSelected()) {
				GradientPaint gp = new GradientPaint(1, 1, TOP, 1, h - 1, DOWN);
				g2d.setPaint(gp);
				g2d.fillRect(0, h / 2, w, h / 2);
			} else {
				Color blue = UIConstants.LIGHT_BLUE;
				g2d.setColor(blue);
				g2d.fillRect(0, 0, w, h / 2);
				GradientPaint gp = new GradientPaint(1, 1, blue, 1, h - 1, blue);
				g2d.setPaint(gp);
				g2d.fillRect(0, h / 2, w, h / 2);
			}
		}

	}

}