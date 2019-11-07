package com.fr.design.gui.ibutton;

import com.fr.base.BaseUtils;
import com.fr.base.CellBorderStyle;
import com.fr.base.GraphHelper;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.core.UITextComponent;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.plaf.ButtonUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class UIButton extends JButton implements UIObserver, UITextComponent {

	private static final int TOOLTIP_INIT_DELAY = 300;  // 延迟 0.3s 显示提示文字
	public static final int OTHER_BORDER = 1;
	public static final int NORMAL_BORDER = 2;
	private static final int HEIGHT = 20;

	private boolean isExtraPainted = true;
	private boolean isRoundBorder = true;
	private int rectDirection = Constants.NULL;
	private Stroke borderStroke = UIConstants.BS;
	private Color borderColor = UIConstants.LINE_COLOR;

	private boolean isPressedPainted = true;
	private boolean isNormalPainted = true;
	protected boolean isBorderPaintedOnlyWhenPressed = false;

	private int borderType = NORMAL_BORDER;
	private CellBorderStyle border = null;

	protected UIObserverListener uiObserverListener;

	public UIButton() {
		this(StringUtils.EMPTY);
	}

	public UIButton(String string) {
		super(string);
		init();
	}


	public UIButton(Icon icon) {
		super(icon);
		init();
	}

	public UIButton(Action action) {
		super(action);
		init();
	}

	public UIButton(String text, Icon icon) {
		super(text, icon);
		init();
	}

    /**
     * 是否进行过权限编辑
     * @param role 角色
     * @return 否
     */
	public boolean isDoneAuthorityEdited(String role) {
		return false;
	}

	public UIButton(Icon normal, Icon rollOver, Icon pressed) {
		super(normal);
		setBorderPainted(false);
		setRolloverIcon(rollOver);
		setPressedIcon(pressed);
		setExtraPainted(false);
		setBackground(null);
		setForeground(UIConstants.FONT_COLOR);
		setOpaque(false);
		initListener();
	}

	protected void initListener() {
		if (shouldResponseChangeListener()) {
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (uiObserverListener == null) {
						return;
					}
					uiObserverListener.doChange();
				}
			});
		}
	}


	//确定是正常的边框类型，还是其他的Border类型
	//若是其他的border类型，则要setOtherType，即设置线型颜色等。若是其他类型，但是没有设置，则默认的是虚线型边框
	public void setBorderType(int borderType) {
		this.borderType = borderType;
	}


	public void setBorderStyle(CellBorderStyle border) {
		this.border = border;
	}

	public void set4ToolbarButton() {
		setNormalPainted(false);
		Dimension dim = getPreferredSize();
		dim.height = HEIGHT;
		setBackground(null);
		setOpaque(false);
		setSize(dim);
		setBorderPaintedOnlyWhenPressed(true);
	}

	public void set4LargeToolbarButton() {
		setNormalPainted(false);
		setBackground(null);
		setOpaque(false);
		setSize(new Dimension(40, 40));
		setBorderPaintedOnlyWhenPressed(true);
	}

    public void set4ChartLargeToolButton(){
        setNormalPainted(false);
      	setBackground(null);
      	setOpaque(false);
      	setSize(new Dimension(34, 44));
      	setBorderPaintedOnlyWhenPressed(true);
    }


	private void init() {
		setOpaque(false);
		setBackground(null);
		setRolloverEnabled(true);
		initListener();
		ToolTipManager.sharedInstance().setInitialDelay(TOOLTIP_INIT_DELAY);
	}

	@Override
	public ButtonUI getUI() {
		return new UIButtonUI();
	}

    /**
     * 更新界面
     */
	public void updateUI() {
		setUI(getUI());
	}

	public CellBorderStyle getBorderStyle() {
		return this.border;
	}

	@Override
	public Insets getInsets() {
		if (getIcon() != null) {
			return new Insets(0, 3, 0, 3);
		}
		return new Insets(0, 10, 0, 10);
	}

	//@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		if (isFixedHeight() || dim.height < HEIGHT) {
			dim.height = HEIGHT;
		}
		return dim;
	}


	public int getBorderType() {
		return borderType;
	}

	public void setOtherBorder(Stroke s, Color c) {
		borderStroke = s;
		borderColor = c;
	}


	@Override
	protected void paintBorder(Graphics g) {

		if (!isBorderPainted()) {
			return;
		}
		if (borderType == OTHER_BORDER) {
			paintOtherBorder(g);
		} else {
			boolean isPress = (isBorderPaintedOnlyWhenPressed && getModel().isPressed());
			if (isPress || !isBorderPaintedOnlyWhenPressed) {
				if (ui instanceof UIButtonUI) {
					((UIButtonUI) ui).paintBorder(g, this);

				} else {
					super.paintBorder(g);

				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension size = this.getSize();
		Graphics2D g2d = (Graphics2D) g;
		Stroke oldStroke = g2d.getStroke();
		if (border != null) {
			g2d.setColor(border.getTopColor());
			GraphHelper.drawLine(g2d, 3, 4, size.getWidth() - 4, 4, border.getTopStyle());
			g2d.setColor(border.getLeftColor());
			GraphHelper.drawLine(g2d, 3, 4, 3, size.getHeight() - 4, border.getLeftStyle());
			g2d.setColor(border.getBottomColor());
			GraphHelper.drawLine(g2d, 3, size.getHeight() - 4, size.getWidth() - 4, size.getHeight() - 4, border.getBottomStyle());
			g2d.setColor(border.getRightColor());
			GraphHelper.drawLine(g2d, size.getWidth() - 4, 4, size.getWidth() - 4, size.getHeight() - 4, border.getRightStyle());
		} else {
			GraphHelper.drawLine(g2d, 2, 4, size.getWidth() - 4, 4, Constants.LINE_NONE);
			GraphHelper.drawLine(g2d, 2, 4, 2, size.getHeight() - 4, Constants.LINE_NONE);
			GraphHelper.drawLine(g2d, 2, size.getHeight() - 4, size.getWidth() - 4, size.getHeight() - 4, Constants.LINE_NONE);
			GraphHelper.drawLine(g2d, size.getWidth() - 4, 4, size.getWidth() - 4, size.getHeight() - 4, Constants.LINE_NONE);
		}
		g2d.setStroke(oldStroke);
	}


	protected void paintOtherBorder(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(borderStroke);
		Shape shape = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, UIConstants.ARC, UIConstants.ARC);
		g2d.setColor(borderColor);
		g2d.draw(shape);
	}

	public void setExtraPainted(boolean extra) {
		this.isExtraPainted = extra;
	}

    /**
     * 是否额外画
     * @return 是则返回TRUE
     */
	public boolean isExtraPainted() {
		return this.isExtraPainted;
	}

	/**
	 * @return
	 */
	public int getRectDirection() {
		return rectDirection;
	}

    /**
     * 是否圆边框
     * @return 是则返回true
     */
	public boolean isRoundBorder() {
		return isRoundBorder;
	}

	/**
	 * @param isRoundBorder
	 */
	public void setRoundBorder(boolean isRoundBorder) {
		setRoundBorder(isRoundBorder, Constants.NULL);
	}

	/**
	 * @param isRound
	 * @param rectDirection
	 */
	public void setRoundBorder(boolean isRound, int rectDirection) {
		this.isRoundBorder = isRound;
		this.rectDirection = rectDirection;
	}

    /**
     * 是否按压画
     * @return 是则返回TRUE
     */
	public boolean isPressedPainted() {
		return isPressedPainted;
	}

	/**
	 * @param isPressedPainted
	 */
	public void setPressedPainted(boolean isPressedPainted) {
		this.isPressedPainted = isPressedPainted;
	}

    /**
     * 是否正常画
     * @return 是则返回TRUE
     */
	public boolean isNormalPainted() {
		return isNormalPainted;
	}

	/**
	 * @param isNormalPressed
	 */
	public void setNormalPainted(boolean isNormalPressed) {
		this.isNormalPainted = isNormalPressed;
		if (!isNormalPressed) {
			setBackground(null);
			setOpaque(false);
		}
	}

	/**
	 * @param value
	 */
	public void setBorderPaintedOnlyWhenPressed(boolean value) {
		this.isBorderPaintedOnlyWhenPressed = value;
	}

	private boolean isFixedHeight() {
		String text = this.getText();
		if (StringUtils.isEmpty(text)) {
			return true;
		}
		// 如果允许换行，需要放开按钮高度的限制
		return !text.startsWith("<html>");
	}

	/**
     * 主函数
     * @param args 入口参数
     */
	public static void main(String... args) {
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) jf.getContentPane();
		content.setLayout(null);

		UIButton bb = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
		bb.setEnabled(false);
		bb.setBorderType(OTHER_BORDER);
		//  bb.setBounds(20, 20,content.getSize().width, bb.getPreferredSize().height);
		bb.setPreferredSize(new Dimension(100, 30));
		bb.setBounds(0, 0, bb.getPreferredSize().width, bb.getPreferredSize().height);
		content.add(bb);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
	public void registerChangeListener(UIObserverListener listener) {
		this.uiObserverListener = listener;
	}

	/**
	 * 组件是否需要响应添加的观察者事件
	 *
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}
}