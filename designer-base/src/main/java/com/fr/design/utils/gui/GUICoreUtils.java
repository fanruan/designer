/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.utils.gui;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.data.util.function.AverageFunction;
import com.fr.data.util.function.CountFunction;
import com.fr.data.util.function.DataFunction;
import com.fr.data.util.function.MaxFunction;
import com.fr.data.util.function.MinFunction;
import com.fr.data.util.function.NoneFunction;
import com.fr.data.util.function.SumFunction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.border.UITitledBorder;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.EditTextField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorCell;
import com.fr.design.style.color.ColorFactory;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.style.color.ColorSelectable;
import com.fr.general.FRFont;
import com.fr.stable.Constants;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StringUtils;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Noninstantiable utility class
public final class GUICoreUtils {

	private static final int WINDOW_GAP = 20;
	private static final int HEIGHT_GAP = 28;
	private static final int WIN_LOCATION_Y=23;
	private static final int CASE_FOUR = 4;

	// 覆盖缺省构造器，不可实例化
	private GUICoreUtils() {
		throw new AssertionError();
	}

	/**
	 * August:一般的cursor的 png图片都是32*32的，下面的方法是用来生成16*16的图片所对应的cursor图标
	 *
	 * @param cursor 光标
	 * @param hotSpot 热点
	 * @param name 名称
	 * @param ob 观察者
	 * @return 光标
	 */
	public static Cursor createCustomCursor(Image cursor, Point hotSpot, String name, ImageObserver ob) {

		Dimension bestCursorSize = Toolkit.getDefaultToolkit().getBestCursorSize(cursor.getWidth(ob), cursor.getHeight(ob));

		BufferedImage bufferedImage = new BufferedImage(bestCursorSize.width, bestCursorSize.height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < bestCursorSize.width; x++) {
			for (int y = 0; y < bestCursorSize.height; y++) {
				bufferedImage.setRGB(x, y, 0);
			}
		}
		bufferedImage.getGraphics().drawImage(cursor, 0, 0, ob);

		return Toolkit.getDefaultToolkit().createCustomCursor(bufferedImage, hotSpot, name);

	}

	/**
	 * 初始化中央面板
	 * @param centerPane 中央面板
	 * @param colorSelectable 颜色选择
	 */
	public static void initCenterPaneChildren(JPanel centerPane, ColorSelectable colorSelectable) {
		JPanel menuColorPane1 = new JPanel();
		centerPane.add(menuColorPane1);

		menuColorPane1.setLayout(new GridLayout(5, 8, 5, 5));
		for (int i = 0; i < ColorFactory.MenuColors.length; i++) {
			menuColorPane1.add(new ColorCell(ColorFactory.MenuColors[i], colorSelectable));
		}

		centerPane.add(Box.createVerticalStrut(5));
		centerPane.add(new JSeparator());
	}

	/**
	 * 调整样式
	 * @param style 样式
	 * @param textField 文本框
	 * @param resolution 调整量
	 * @param value 对齐方式
	 */
	public static void adjustStyle(Style style, EditTextField textField, int resolution, Object value) {
		if (style == null) {
			// peter:获取默认的Style.
			style = Style.DEFAULT_STYLE;
		}

		// alignment.
		int horizontalAlignment = BaseUtils.getAlignment4Horizontal(style, value);
		if (horizontalAlignment == Constants.LEFT) {
			textField.setHorizontalAlignment(SwingConstants.LEFT);
		} else if (horizontalAlignment == Constants.CENTER) {
			textField.setHorizontalAlignment(SwingConstants.CENTER);
		} else if (horizontalAlignment == Constants.RIGHT) {
			textField.setHorizontalAlignment(SwingConstants.RIGHT);
		} else {
			textField.setHorizontalAlignment(SwingConstants.LEFT);
		}

		FRFont frFont = style.getFRFont();
		textField.setFont(new Font(frFont.getFontName(), frFont.getStyle(), frFont.getShowSize(resolution)));
		textField.setForeground(style.getFRFont().getForeground());

		if (style.getBackground() instanceof ColorBackground) {
			textField.setBackground(((ColorBackground) style.getBackground()).getColor());
		} else {
			textField.setBackground(Color.WHITE);
		}
	}

	/**
	 * 生成一个边界布局
	 *
	 * @param args 布局内部的元素，位置等
	 * @return 具有边界布局的面板
	 */
	public static JPanel createBorderLayoutPane(Object... args) {
		if (args.length % 2 != 0) {
			throw new IllegalArgumentException("Illegal Arguments");
		}
		BorderLayout layout = new BorderLayout();
		Object maybeHgap = args[args.length - 2];
		Object maybeVgap = args[args.length - 1];
		boolean hasGap = false;
		if (maybeHgap instanceof Integer && maybeVgap instanceof Integer) {
			layout.setHgap((Integer) maybeHgap);
			layout.setVgap((Integer) maybeVgap);
			hasGap = true;
		}
		JPanel pane = new JPanel(layout);
		pane.setOpaque(false);
		for (int i = 0; i < (hasGap ? args.length - 2 : args.length) / 2; i++) {
			pane.add((Component) args[2 * i], args[2 * i + 1]);
		}
		return pane;
	}

	/**
	 * set color title border
	 * 设置带颜色的边框
	 * @param s 标题
	 * @param c 颜色
	 * @return 同上
	 */
	public static TitledBorder createTitledBorder(String s, Color c) {
		UITitledBorder tb = UITitledBorder.createBorderWithTitle(s);
		if (c == null) {
			c = Color.black;
		}
		tb.setTitleColor(c);
		return tb;
	}

	/**
	 * 设置带颜色的边框
	 * set color title border
	 * @param s 标题
	 * @return 同上
	 */
	public static TitledBorder createTitledBorder(String s) {
		return createTitledBorder(s, Color.black);
	}

	/**
	 * 创建工具栏组件
	 * @param updateAction 更新动作
	 * @return UIToggleButton 按钮
	 * 
	 */
	public static UIToggleButton createToolBarComponent(UpdateAction updateAction) {
		UIToggleButton button = new UIToggleButton();
		button.set4ToolbarButton();
		Object object = updateAction.getValue(UIToggleButton.class.getName());
		if (!(object instanceof AbstractButton)) {

			Integer mnemonicInteger = (Integer) updateAction.getValue(Action.MNEMONIC_KEY);
			if (mnemonicInteger != null) {
				button.setMnemonic((char) mnemonicInteger.intValue());
			}

			button.setIcon((Icon) updateAction.getValue(Action.SMALL_ICON));
			button.addActionListener(updateAction);

			button.registerKeyboardAction(updateAction, updateAction.getAccelerator(), JComponent.WHEN_IN_FOCUSED_WINDOW);

			updateAction.putValue(UIToggleButton.class.getName(), button);
			button.setText(StringUtils.EMPTY);
			button.setEnabled(updateAction.isEnabled());

			button.setToolTipText(ActionFactory.createButtonToolTipText(updateAction));
			object = button;
		}

		return (UIToggleButton) object;
	}

	/**
	 * 创建工具栏组件-反白icon
	 * @param updateAction 更新动作
	 * @return UIToggleButton 按钮
	 *
	 */
	public static UIToggleButton createToolBarComponentWhiteIcon(UpdateAction updateAction) {
		UIToggleButton button = new UIToggleButton((Icon[]) updateAction.getValue(Action.SMALL_ICON), true);
		button.set4ToolbarButton();
		Integer mnemonicInteger = (Integer) updateAction.getValue(Action.MNEMONIC_KEY);
		if (mnemonicInteger != null) {
			button.setMnemonic((char) mnemonicInteger.intValue());
		}

		button.addActionListener(updateAction);

		button.registerKeyboardAction(updateAction, updateAction.getAccelerator(), JComponent.WHEN_IN_FOCUSED_WINDOW);

		updateAction.putValue(UIToggleButton.class.getName(), button);
		button.setText(StringUtils.EMPTY);
		button.setEnabled(updateAction.isEnabled());

		button.setToolTipText(ActionFactory.createButtonToolTipText(updateAction));

		return button;
	}

	/**
	 * 设置一个窗口
	 *
	 * @param win the current window august:现在要考虑左边日志模板的影响
	 */
	public static void centerWindow(Window win) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		Dimension winSize = win.getSize();

		if (winSize.height > screenSize.height) {
			winSize.height = screenSize.height;
		}
		if (winSize.width > screenSize.width) {
			winSize.width = screenSize.width;
		}
		win.setLocation((screenSize.width - winSize.width) / 2, (screenSize.height - winSize.height) / 2 - WINDOW_GAP);
	}

	/**
	 * Gets window/frame to screen center.
	 * @param owerWin 父窗口
	 * @param win 窗口
	 */
	public static void setWindowCenter(Window owerWin, Window win) {
		Point owerPoint = owerWin.getLocation();
		Dimension owerSize = owerWin.getSize();
		Dimension winSize = win.getSize();

		win.setLocation((owerSize.width - winSize.width) / 2 + owerPoint.x, (owerSize.height - winSize.height) / 2 + owerPoint.y);
	}

	/**
	 * Gets window/frame to screen center.
	 * @param 窗口
	 */
	public static void setWindowFullScreen(Window win) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (OperatingSystem.isWindows()) {
			win.setLocation(0, 0);
			win.setSize(screenSize.width, screenSize.height - HEIGHT_GAP);
		} else {
			win.setLocation(5, WIN_LOCATION_Y);
			win.setSize(screenSize.width, screenSize.height - HEIGHT_GAP *2);
		}
	}

	/**
	 * Shows down component.
	 * 显示弹出关闭菜单
	 * @param popup 弹出菜单
	 * @param 父组件
	 */
	public static void showPopupCloseMenu(JPopupMenu popup, Component parentComponent) {
		if (popup == null) {// check null.
			return;
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Window frame = SwingUtilities.getWindowAncestor(parentComponent);

		int leftX = 0;

		int rightX = parentComponent.getLocation().x + frame.getLocation().x + popup.getPreferredSize().width;
		if (rightX > screenSize.width) {
			leftX = screenSize.width - rightX;
		}

		popup.show(parentComponent, leftX, parentComponent.getSize().height);
	}

	/**
	 * 显示弹出菜单
	 * @param popup 弹出菜单
	 * @param parentComponent 父组件
	 * @param x x坐标
	 * @param y y坐标
	 */
	public static void showPopMenuWithParentWidth(JPopupMenu popup, Component parentComponent, int x, int y) {
		if (popup == null) {// check null.
			return;
		}
		Dimension size = popup.getPreferredSize();
		size.width = Math.max(size.width, parentComponent.getWidth());
		popup.setPreferredSize(size);
		showPopupCloseMenu(popup, parentComponent);
	}

	/**
	 * 显示弹出菜单
	 * @param popup 弹出菜单
	 * @param parentComponent 父组件
	 * @param x x坐标
	 * @param y y坐标
	 */
	public static void showPopupMenu(JPopupMenu popup, Component parentComponent, int x, int y) {
		if (popup == null) {// check null.
			return;
		}

		Point point = new Point(x, y);
		SwingUtilities.convertPointToScreen(point, parentComponent);

		Dimension size = popup.getPreferredSize();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		screen.setSize(screen.getSize().width, screen.height - HEIGHT_GAP);

		// peter:调整X的高度.
		if (point.x + size.width > screen.width && size.width < screen.width) {
			x += (screen.width - point.x - size.width);
		}

		// peter:调整y高度.
		if (point.y + size.height > screen.height && size.height < screen.height) {
			y -= size.height;
		}

		popup.show(parentComponent, x, y);
	}

	/**
	 * Set enabled.<br>
	 * With the enabled of all children component.
	 * @param parentComponent 父组件
	 * @param enabled 是否可用
	 */
	public static void setEnabled(JComponent parentComponent, boolean enabled) {
		// check the border of comp.
		Border border = parentComponent.getBorder();
		if (border != null && border instanceof TitledBorder) {
			TitledBorder titledBorder = (TitledBorder) border;

			if (enabled) {
				titledBorder.setTitleColor(UIManager.getColor("Label.foreground"));
			} else {
				titledBorder.setTitleColor(UIManager.getColor("Label.disabledForeground"));
			}
		}

		for (int i = 0; i < parentComponent.getComponentCount(); i++) {
			Component tmpComp = parentComponent.getComponent(i);

			if (tmpComp instanceof JComponent) {
				GUICoreUtils.setEnabled((JComponent) tmpComp, enabled);
			} else {
				tmpComp.setEnabled(enabled);
			}
		}

		parentComponent.setEnabled(enabled);
	}

	/**
	 * 增加监听
	 * @param parentComponent 父组件
	 * @param changeListener 监听
	 * @author kunsnat E-mail kunsnat@gmail.com
	 */
	public static void addChangeListener(JComponent parentComponent, ChangeListener changeListener) {
		for (int i = 0; i < parentComponent.getComponentCount(); i++) {
			Component tmpComp = parentComponent.getComponent(i);

			// addColorChangeListener ColorSelectBox
			if (tmpComp instanceof AbstractButton) {
				((AbstractButton) tmpComp).addChangeListener(changeListener);
			} else if (tmpComp instanceof ColorSelectBox) {
				((ColorSelectBox) tmpComp).addSelectChangeListener(changeListener);
			} else if (tmpComp instanceof JSlider) {
				((JSlider) tmpComp).addChangeListener(changeListener);
			} else if (tmpComp instanceof JComponent) {
				GUICoreUtils.addChangeListener((JComponent) tmpComp, changeListener);
			}
		}
	}

	/**
	 * 增加监听
	 * @param parentComponent 父组件
	 * @param actionListener 监听
	 */
	public static void addActionListener(JComponent parentComponent, ActionListener actionListener) {
		for (int i = 0; i < parentComponent.getComponentCount(); i++) {
			Component tmpComp = parentComponent.getComponent(i);

			if (tmpComp instanceof UIComboBox) {
				((UIComboBox) tmpComp).addActionListener(actionListener);
			} else if (tmpComp instanceof JComponent) {
				GUICoreUtils.addActionListener((JComponent) tmpComp, actionListener);
			}
		}
	}

	/**
	 * 生成一个左边是名字,右边是comp的一个JPanel
	 * @param comp 组件
	 * @param name 名称
	 * @return 面板
	 */
	public static JPanel createNamedPane(JComponent comp, String name) {
		JPanel mainPane = new JPanel();
		mainPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());

		mainPane.add(new UILabel(name), BorderLayout.WEST);
		mainPane.add(comp, BorderLayout.CENTER);

		return mainPane;
	}

	 /**
	  * 生成一个上边是名字,下边是comp的一个JPanel
	  * @param comp 组件
	  * @param name 名称
	  * @return 面板
	  */
	public static JPanel createVerticalNamedPane(JComponent comp, String name) {
		JPanel mainPane = new JPanel();
		mainPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());

		mainPane.add(new UILabel(name), BorderLayout.NORTH);
		mainPane.add(comp, BorderLayout.CENTER);

		return mainPane;
	}

	/**
	 * 产生一个Flow Pane, flowAligment是FlowLayout.LEFT, CENTER, RIGHT.
	 * @param comp 组件
	 * @param flowAlignment 对齐方式
	 * @return 面板
	 */
	public static JPanel createFlowPane(Component comp, int flowAlignment) {// by
		return GUICoreUtils.createFlowPane(new Component[]{comp}, flowAlignment);
	}

	/**
	 * 产生一个Flow Pane, flowAligment是FlowLayout.LEFT, CENTER, RIGHT.
	 * @param comps 组件
	 * @param flowAlignment 对齐方式
	 * @return 面板
	 */
	public static JPanel createFlowPane(Component[] comps, int flowAlignment) {// by
		return GUICoreUtils.createFlowPane(comps, flowAlignment, 0);
	}

	/**
	 * 产生一个Flow Pane, flowAligment是FlowLayout.LEFT, CENTER, RIGHT.
	 * @param comps 组件
	 * @param flowAlignement 对齐方式
	 * @param hSpace 水平间隔
	 * @return 面板
	 */
	public static JPanel createFlowPane(Component[] comps, int flowAlignment, int hSpace) {// by
		// peter
		return GUICoreUtils.createFlowPane(comps, flowAlignment, hSpace, 0);
	}

	/**
	 * 产生一个Flow Pane, flowAligment是FlowLayout.LEFT, CENTER, RIGHT,
	 * @param comps 组件
	 * @param flowAlignment 对齐方式
	 * @param hSpace 垂直间隔
	 * @param vSpace 水平间隔
	 * @return  面板
	 */
	public static JPanel createFlowPane(Component[] comps, int flowAlignment, int hSpace, int vSpace) {
		JPanel leftPane = new /**/JPanel();
		leftPane.setLayout(new /**/FlowLayout(flowAlignment, hSpace, vSpace));
		for (int i = 0; i < comps.length; i++) {
			leftPane.add(comps[i]);
		}

		return leftPane;
	}


	/**
	 * 创建一个靠左流式布局，流式内嵌
	 *
	 * @return JPanel对象
	 */
	public static JPanel createBoxFlowInnerContainerPane(int hgap, int vgap) {
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
		return jp;
	}

	/**
	 * 生成一个以流式布局为布局的面板
	 * @param comps 面板中的组件以及布局的参数，后3位参数（可选）分别表示对齐方式，水平间隙，垂直间隙
	 * @return  面板
	 */
	public static JPanel createFlowPane(Object... comps) {
		int len = comps.length;
		int last = len;
		FlowLayout layout = new FlowLayout();
		JPanel panel = new JPanel(layout);
		if (len > 3 && comps[len - 3] instanceof Integer && comps[len - 2] instanceof Integer && comps[len - 1] instanceof Integer) {
			layout.setAlignment((Integer) comps[len - 3]);
			layout.setHgap((Integer) comps[len - 2]);
			layout.setVgap((Integer) comps[len - 1]);
			last = len - 3;
		} else if (len > 2 && comps[len - 1] instanceof Integer && comps[len - 2] instanceof Component) {
			layout.setAlignment((Integer) comps[len - 1]);
			last = len - 1;
		}
		for (int i = 0; i < last; i++) {
			if (comps[i] instanceof Component) {
				panel.add((Component)comps[i]);
			}
		}
		return panel;
	}

	/**
	 * 产生一个BorderPane, boderPosition=BoderLayout.CENTER, NORTH, SOUNTH, RIGHT.
	 * @param comp 组件
	 * @param boderPosition 位置
	 * @return 面板
	 */
	public static JPanel createBorderPane(JComponent comp, String boderPosition) {// by
		// peter
		JPanel newPane = new /**/JPanel();
		newPane.setLayout(FRGUIPaneFactory.createBorderLayout());
		newPane.add(comp, boderPosition);

		return newPane;
	}

    /**
     * 生成一个边界布局的面板
     * @param components  面板中的组件，第一个组件位置在中间，第二个组件位置再东边，
     *        第三个组件位置在南边，第四个组件位置在西边，第五个组件位置在北边
     * @return  具有边界布局的容器
     */
    public static JPanel createBorderLayoutPane(Component[] components) {
		JPanel pane = new JPanel(new BorderLayout());
		for (int i = 0, len = components.length; i < len; i++) {
			switch (i) {
				case 0:
					pane.add(components[0], BorderLayout.CENTER);
					break;
				case 1:
					if (components[1] != null) {
						pane.add(components[1], BorderLayout.EAST);
					}
					break;
				case 2:
					if (components[2] != null) {
						pane.add(components[2], BorderLayout.SOUTH);
					}
					break;
				case 3:
					if (components[3] != null) {
						pane.add(components[3], BorderLayout.WEST);
					}
					break;
				case CASE_FOUR:
					if (components[CASE_FOUR] != null) {
						pane.add(components[CASE_FOUR], BorderLayout.NORTH);
					}
					break;
				default:
					pane.add(components[0], BorderLayout.CENTER);
			}
		}
		return pane;
	}

	/**
	 * it's a very good method, user can get treePath from treeNode.
	 * @param 节点
	 * @return 路径
	 */
	public static TreePath getTreePath(TreeNode treeNode) {
		List<TreeNode> objectList = new ArrayList<TreeNode>();

		// peter:需要判断treenode不为空.
		if (treeNode != null) {
			objectList.add(treeNode);
			while ((treeNode = treeNode.getParent()) != null) {
				objectList.add(0, treeNode);
			}
		}

		Object[] objects = new Object[objectList.size()];
		objectList.toArray(objects);

		// peter:为了不抛出Exception,直接返回null.
		if (objects.length <= 0) {
			return null;
		}

		return new TreePath(objects);
	}

	/**
	 * peter:获得最上面的那个选中的TreePath
	 * @param tree 树
	 * @param treePaths 路径
	 * @return 路径
	 */
	public static TreePath getTopTreePath(JTree tree, TreePath[] treePaths) {
		if (tree == null || treePaths == null || treePaths.length == 0) {
			return null;
		}

		TreePath topTreePath = null;

		// peter:开始比较行.
		int row = Integer.MAX_VALUE;
		for (int i = 0; i < treePaths.length; i++) {
			int tmpRow = tree.getRowForPath(treePaths[i]);
			if (tmpRow < row) {
				row = tmpRow;
				topTreePath = treePaths[i];
			}
		}

		return topTreePath;
	}

	/**
	 * 获得UI的TitledBorder,默认的LineBorder的颜色.
	 * @return 颜色
	 */
	public static Color getTitleLineBorderColor() {
		Border b = UIManager.getBorder("TitledBorder.border");
		if (b instanceof LineBorder) {
			return ((LineBorder) b).getLineColor();
		}

		return Color.GRAY;
	}

	/**
	 * peter: 删除选中的所有节点
	 * @param ancestorWindow 父窗口
	 * @param nodeList 节点列表
	 * @return 布尔值
	 */
	public static boolean removeJListSelectedNodes(Window ancestorWindow, JList nodeList) {
		int selectedIndex = nodeList.getSelectedIndex();
		if (selectedIndex == -1) {
			return false;
		}

		int returnVal = JOptionPane.showConfirmDialog(ancestorWindow, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Are_You_Sure_To_Remove_The_Selected_Item") + "?", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (returnVal == JOptionPane.OK_OPTION) {
			int minSelectedIndex = nodeList.getMinSelectionIndex();
			int[] selectedIndices = nodeList.getSelectedIndices();
			// peter:先排序，然后从后往前删除，这样不会发生错乱.
			Arrays.sort(selectedIndices);
			for (int i = selectedIndices.length - 1; i >= 0; i--) {
				((DefaultListModel) nodeList.getModel()).remove(selectedIndices[i]);
			}

			if (nodeList.getModel().getSize() > 0) {
				if (minSelectedIndex < nodeList.getModel().getSize()) {
					// nodeList.setSelectedIndex(minSelectedIndex);
					nodeList.setSelectedValue(nodeList.getModel().getElementAt(minSelectedIndex), true);
				} else {
					nodeList.setSelectedValue(nodeList.getModel().getElementAt(nodeList.getModel().getSize() - 1), true);
					// nodeList.setSelectedIndex(nodeList.getModel().getSize() -
					// 1);
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * 得到Spinner的编辑器
	 * @param spinner spinner
	 * @return 文本域
	 */
	public static JFormattedTextField getSpinnerTextField(JSpinner spinner) {
		JComponent editor = spinner.getEditor();
		if (editor instanceof JSpinner.DefaultEditor) {
			return ((JSpinner.DefaultEditor) editor).getTextField();
		} else {
			System.err.println("Unexpected editor type: " + spinner.getEditor().getClass() + " isn't a descendant of DefaultEditor");
			return null;
		}
	}

	/**
	 * 为到Spinner的编辑器设置宽度
	 * @param spinner spinner
	 * @columns 列数
	 */
	public static void setColumnForSpinner(JSpinner spinner, int columns) {
		JFormattedTextField cftf = getSpinnerTextField(spinner);
		if (cftf != null) {
			cftf.setColumns(columns); // specify more width than we need
			cftf.setHorizontalAlignment(UITextField.LEFT);
		}
	}

	/**
	 * ************************************************************************
	 * peter:重绘.
	 *
	 * @param component 组件
	 */
	public static void repaint(final Component component) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				component.invalidate();
				component.validate();
				component.repaint();
			}
		});
	}

	
	/**
	 * harry：创建自定义按钮(指不受皮肤控制的按钮)
	 * @param icon 图标
	 * @param roverIcon 悬浮图标
	 * @param pressedIcon 点击图标
	 * @return 按钮
	 */
	public static UIButton createTransparentButton(Icon icon, Icon roverIcon, Icon pressedIcon) {
		UIButton button = new UIButton();
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setBorder(null);
		button.setMargin(null);
		button.setOpaque(false);
		button.setIcon(icon);
		button.setRolloverEnabled(true);
		button.setRolloverIcon(roverIcon);
		button.setPressedIcon(pressedIcon);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setRequestFocusEnabled(false);

		return button;
	}

	public static DataFunction[] FunctionArray = null;

	/**
	 * 获取函数
	 * @return DataFunction[] 函数
	 * 
	 */
	public static DataFunction[] getFunctionArray() {
		if (FunctionArray == null) {
			FunctionArray = new DataFunction[]{new SumFunction(), new AverageFunction(), new MaxFunction(), new MinFunction(), new CountFunction(), new NoneFunction(),};
		}

		return FunctionArray;
	}

	/**
	 * 让UIComboBox在不触发ItemListener的情况下选中某项
	 *
	 * @param jcb 复选框
	 * @param item 选项
	 */
	public static void setSelectedItemQuietly(UIComboBox jcb, Object item) {
		ItemListener[] listeners = jcb.getItemListeners();
		for (ItemListener aListener : listeners) {
			jcb.removeItemListener(aListener);
		}

		jcb.setSelectedItem(item);

		for (ItemListener aListener : listeners) {
			jcb.addItemListener(aListener);
		}
	}

	/**
	 * 让UIComboBox在不触发ItemListener的情况下选中某项
	 *
	 * @param jcb 复选框
	 * @param index 选项序号
	 */
	public static void setSelectedItemQuietly(UIComboBox jcb, int index) {
		ItemListener[] listeners = jcb.getItemListeners();
		for (ItemListener aListener : listeners) {
			jcb.removeItemListener(aListener);
		}

		jcb.setSelectedIndex(index);

		for (ItemListener aListener : listeners) {
			jcb.addItemListener(aListener);
		}
	}

	/**
	 * 是否在同一区域
	 * @param oneRect 矩形框
	 * @param otherRect 其他矩形框
	 * @return 同上
	 */
	public static boolean isTheSameRect(Rectangle oneRect, Rectangle otherRect) {
		return oneRect.getX() == otherRect.getX()
				&& oneRect.getY() == otherRect.getY()
				&& oneRect.getWidth() == otherRect.getWidth()
				&& oneRect.getHeight() == otherRect.getHeight();
	}

	/**
	 * 生成提示标签
	 * @param tipText 提示文字
	 * @return UILabel 标签对象
	 */
	public static UILabel createTipLabel(String tipText) {
		UILabel tipLabel = new UILabel("<html>" + tipText + "</html>");
		tipLabel.setForeground(Color.gray);
		return tipLabel;
	}

	/**
	 * 生成没有边框的 UICheckBox
	 * @param text 说明文字
	 * @return UICheckBox
	 */
	public static UICheckBox createNoBorderCheckBox(String text) {
		UICheckBox checkBox = new UICheckBox(text);
		checkBox.setBorder(BorderFactory.createEmptyBorder());
		return checkBox;
	}

	/**
	 * 创建包含选择框和一个动态面板的联动面板。根据选择框的状态，动态面板会动态地显示或隐藏
	 * @param checkBox 选择框
	 * @param dynamicPane 包含任意内容的动态面板
	 * @param hideOnSelected 选中时隐藏动态面板（若为false，则在"去掉勾选"时隐藏动态面板）
	 * @return 联动面板
	 */
	public static JPanel createCheckboxAndDynamicPane(UICheckBox checkBox, final JPanel dynamicPane, final boolean hideOnSelected) {
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int visibleState = hideOnSelected ? ItemEvent.DESELECTED : ItemEvent.SELECTED;
				dynamicPane.setVisible(e.getStateChange() == visibleState);
			}
		});
		JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		panel.add(checkBox, BorderLayout.NORTH);
		JPanel dynamicPaneWrapper = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		dynamicPaneWrapper.add(dynamicPane);
		panel.add(dynamicPaneWrapper, BorderLayout.CENTER);
		return panel;
	}

	/**
	 * 创建一个单列垂直布局的 TableLayout 面板
	 * @param comps 组件数组
	 * @return 布局完成后的面板
	 */
	public static JPanel createHeaderLayoutPane(Component... comps) {
		// TableLayout
		double p = TableLayout.PREFERRED;
		double[] columnSize = {p};

		double[] rowSize = new double[comps.length];
		for (int i = 0; i < rowSize.length; i++) {
			rowSize[i] = p;
		}

		Component[][] components = new Component[rowSize.length][columnSize.length];
		for (int i = 0; i < rowSize.length; i++) {
			components[i][0] = comps[i];
		}
		return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 10);
	}

	/**
	 * 获取当前所有显示器设备的总长总宽
	 * @return
	 */
	public static Rectangle getRectScreen() {
		Rectangle rectangle = new Rectangle(0, 0, 0, 0);
		for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			rectangle = rectangle.union(gd.getDefaultConfiguration().getBounds());
		}
		return rectangle;
	}
}
