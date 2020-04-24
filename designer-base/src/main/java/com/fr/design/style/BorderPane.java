/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.style;

import com.fr.base.BaseUtils;
import com.fr.base.CellBorderStyle;
import com.fr.base.GraphHelper;
import com.fr.base.Style;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.VerticalFlowLayout;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.style.color.NewColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * CellBorder Pane.
 */
public class BorderPane extends BasicPane {
	// Constants used to set active borders
	private static final int NO_BORDERS = 0;
	private static final int EXTERNAL_BORDERS = 1;
	private static final int INSIDE_BORDERS = 2;

	private static final int TOP_BORDER = 3;
	private static final int LEFT_BORDER = 4;
	private static final int BOTTOM_BORDER = 5;
	private static final int RIGHT_BORDER = 6;
	private static final int VERTICAL_BORDER = 7;
	private static final int HORIZONTAL_BORDER = 8;

	private boolean insideMode = false;

	private CellBorderStyle cellBorderStyle = new CellBorderStyle();

	// private BorderButton insideBorderButton;

	private BorderComponent borderComponent;
	private JToggleButton topToggleButton;
	private JToggleButton horizontalToggleButton;
	private JToggleButton bottomToggleButton;
	private JToggleButton leftToggleButton;
	private JToggleButton verticalToggleButton;
	private JToggleButton rightToggleButton;

	private LineComboBox currentLineCombo;
	private NewColorSelectBox currentLineColorPane;

	
	private UIButton insidebutton;

	public BorderPane() {
		this.initComponents();
	}

	protected void initComponents() {
		borderComponent = new BorderComponent();
		topToggleButton = new ToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/top.png"), BorderPane.TOP_BORDER);
		horizontalToggleButton = new ToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/horizontal.png"), BorderPane.HORIZONTAL_BORDER);
		bottomToggleButton = new ToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/bottom.png"), BorderPane.BOTTOM_BORDER);

		leftToggleButton = new ToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/left.png"), BorderPane.LEFT_BORDER);
		verticalToggleButton = new ToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/vertical.png"), BorderPane.VERTICAL_BORDER);
		rightToggleButton = new ToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/right.png"), BorderPane.RIGHT_BORDER);
		this.currentLineCombo = new LineComboBox(CoreConstants.UNDERLINE_STYLE_ARRAY);
		this.currentLineColorPane = new NewColorSelectBox(100);

		this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

		centerPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview"), null));
		JPanel borderAllControlPane = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
		centerPane.add(borderAllControlPane, BorderLayout.NORTH);
		borderAllControlPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		borderAllControlPane.add(new UILabel(" "));
		// Button reseting borders
		borderAllControlPane.add(createVerButtonPane(NO_BORDERS, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No")));
		// Button setting all borders to active with
		// current color and current style excepting inside borders
		borderAllControlPane.add(createVerButtonPane(EXTERNAL_BORDERS, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_External")));

		borderAllControlPane.add(createVerButtonPane(INSIDE_BORDERS, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Inner")));

		// Control
		JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		centerPane.add(borderPane, BorderLayout.CENTER);
		borderPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));

		borderComponent.addBorderChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				fireStateChanged();
			}
		});

		borderPane.add(borderComponent, BorderLayout.CENTER);

		JPanel borderLeftPane = new JPanel();
		JPanel borderCornerPane = new JPanel();
		JPanel borderBottomPane = new JPanel();

		borderPane.add(borderLeftPane, BorderLayout.WEST);
		JPanel tmpBorderBottomPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		borderPane.add(tmpBorderBottomPane, BorderLayout.SOUTH);
		tmpBorderBottomPane.add(borderBottomPane, BorderLayout.CENTER);
		tmpBorderBottomPane.add(borderCornerPane, BorderLayout.WEST);

		borderLeftPane.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 4, 16));

		borderLeftPane.add(topToggleButton);
		borderLeftPane.add(horizontalToggleButton);
		borderLeftPane.add(bottomToggleButton);

		borderBottomPane.setLayout(new /**/FlowLayout(FlowLayout.CENTER, 42, 5));
		borderBottomPane.add(leftToggleButton);
		borderBottomPane.add(verticalToggleButton);
		borderBottomPane.add(rightToggleButton);

		borderCornerPane.setPreferredSize(new Dimension(borderLeftPane.getPreferredSize().width, borderBottomPane.getPreferredSize().height));

		JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

		northPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Line"), null));
		JPanel rightTopPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		northPane.add(rightTopPane, BorderLayout.NORTH);
		JPanel first = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		first.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style") + ":"));
		first.add(this.currentLineCombo);
		rightTopPane.add(first, BorderLayout.NORTH);

		JPanel second = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		second.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Color") + ":"));

		second.add(this.currentLineColorPane);
		rightTopPane.add(second, BorderLayout.CENTER);
		this.currentLineColorPane.setSelectObject(Color.BLACK);
		this.add(northPane, BorderLayout.NORTH);
		this.add(centerPane, BorderLayout.CENTER);
	}

	public void addChangeListener(ChangeListener changeListener) {
		listenerList.add(ChangeListener.class, changeListener);
	}

	/**
     */
	public void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		ChangeEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (e == null) {
					e = new ChangeEvent(this);
				}
				((ChangeListener)listeners[i + 1]).stateChanged(e);
			}
		}
	}

	private JPanel createVerButtonPane(int display, String text) {
		JPanel verPane = new JPanel();
		verPane.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 2, 2));

		BorderButton button = new BorderButton(display);
		verPane.add(button);
		verPane.add(new UILabel(text));
		insidebutton = button;
		return verPane;
	}

	private void refreshAllToggleButtons() {
		topToggleButton.setSelected(this.cellBorderStyle.getTopStyle() != Constants.LINE_NONE);
		horizontalToggleButton.setSelected(this.cellBorderStyle.getHorizontalStyle() != Constants.LINE_NONE);
		bottomToggleButton.setSelected(this.cellBorderStyle.getBottomStyle() != Constants.LINE_NONE);
		leftToggleButton.setSelected(this.cellBorderStyle.getLeftStyle() != Constants.LINE_NONE);
		verticalToggleButton.setSelected(this.cellBorderStyle.getVerticalStyle() != Constants.LINE_NONE);
		rightToggleButton.setSelected(this.cellBorderStyle.getRightStyle() != Constants.LINE_NONE);
	}

	public CellBorderStyle getCellBorderStyle() {
		return this.cellBorderStyle;
	}

	public void setCellBorderStyle(CellBorderStyle cellBorderStyle) {
		this.cellBorderStyle = cellBorderStyle;
	}

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Border");
	}

	// p:populate Style
	// 在编辑全局style的时候调用这个方法.
	public void populate(Style style) {
		if (style == null) {
			style = Style.DEFAULT_STYLE;
		}
		CellBorderStyle cellBorderStyle = new CellBorderStyle();

		cellBorderStyle.setTopStyle(style.getBorderTop());
		cellBorderStyle.setTopColor(style.getBorderTopColor());
		cellBorderStyle.setLeftStyle(style.getBorderLeft());
		cellBorderStyle.setLeftColor(style.getBorderLeftColor());
		cellBorderStyle.setBottomStyle(style.getBorderBottom());
		cellBorderStyle.setBottomColor(style.getBorderBottomColor());
		cellBorderStyle.setRightStyle(style.getBorderRight());
		cellBorderStyle.setRightColor(style.getBorderRightColor());

		this.populate(cellBorderStyle, false, style.getBorderTop(), style.getBorderTopColor());
	}

	public void populate(CellBorderStyle cellBorderStyle, boolean insideMode, int currentStyle, Color currentColor) {
		try {
			if (cellBorderStyle != null) {
				this.cellBorderStyle = (CellBorderStyle)cellBorderStyle.clone();
			}
		} catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
		}
		this.insideMode = insideMode;
		// ben 这里也有问题，CellBorderStyle的 linestyle和color很可能不止一种
		this.currentLineCombo.setSelectedLineStyle(currentStyle == Constants.LINE_NONE ? Constants.LINE_THIN : currentStyle);
		this.currentLineColorPane.setSelectObject(currentColor);

		 this.insidebutton.setEnabled(this.insideMode);
		this.horizontalToggleButton.setEnabled(this.insideMode);
		this.verticalToggleButton.setEnabled(this.insideMode);

		this.borderComponent.repaint();
	}

	// p:update Style
	// 在编辑全局style的时候调用这个方法,
	// 通过CellStyle设置Style
	public Style update(Style style) {
		if (style == null) {
			style = Style.DEFAULT_STYLE;
		}

		CellBorderStyle cellBorderStyle = this.update();

		style = style.deriveBorder(cellBorderStyle.getTopStyle(), cellBorderStyle.getTopColor(), cellBorderStyle.getBottomStyle(), cellBorderStyle.getBottomColor(),
				cellBorderStyle.getLeftStyle(), cellBorderStyle.getLeftColor(), cellBorderStyle.getRightStyle(), cellBorderStyle.getRightColor());

		return style;
	}

	public CellBorderStyle update() {
		return this.cellBorderStyle;
	}

	private class ToggleButton extends JToggleButton implements ActionListener {
		private int borderType = 0;

		public ToggleButton(Icon icon, int borderType) {
			super(icon);
			this.borderType = borderType;

			this.setPreferredSize(new Dimension(32, 32));
			this.addActionListener(this);
		}

		public void actionPerformed(ActionEvent evt) {
			if (this.isSelected()) {
				borderComponent.setActiveBorders(borderType);
			} else {
				borderComponent.setActiveBorders(BorderPane.NO_BORDERS);
			}

			switch (borderType) {
			case BorderPane.TOP_BORDER: {
				cellBorderStyle.setTopColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setTopStyle(currentLineCombo.getSelectedLineStyle());
				break;
			}
			case BorderPane.HORIZONTAL_BORDER: {
				cellBorderStyle.setHorizontalColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setHorizontalStyle(currentLineCombo.getSelectedLineStyle());
				break;
			}
			case BorderPane.BOTTOM_BORDER: {
				cellBorderStyle.setBottomColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setBottomStyle(currentLineCombo.getSelectedLineStyle());
				break;
			}
			case BorderPane.LEFT_BORDER: {
				cellBorderStyle.setLeftColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setLeftStyle(currentLineCombo.getSelectedLineStyle());
				break;
			}
			case BorderPane.VERTICAL_BORDER: {
				cellBorderStyle.setVerticalColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setVerticalStyle(currentLineCombo.getSelectedLineStyle());
				break;
			}
			case BorderPane.RIGHT_BORDER: {
				cellBorderStyle.setRightColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setRightStyle(currentLineCombo.getSelectedLineStyle());
				break;
			}
			}

			borderComponent.repaint();
		}
	}

	// Shortcut setting button
	private class BorderButton extends UIButton implements ActionListener {
		private int border;

		private BorderButton(int border) {
			this.border = border;
			if (border == BorderPane.EXTERNAL_BORDERS) {
				this.setIcon(BaseUtils.readIcon("com/fr/design/images/m_format/out.png"));
			} else if(border == BorderPane.INSIDE_BORDERS) {
				this.setIcon(BaseUtils.readIcon("com/fr/design/images/m_format/in.png"));
			}
			this.addActionListener(this);
			setPreferredSize(new Dimension(32, 32));
			setFocusPainted(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			borderComponent.setActiveBorders(border);

			if (border == NO_BORDERS) {
				cellBorderStyle.setTopColor(Color.BLACK);
				cellBorderStyle.setTopStyle(Constants.LINE_NONE);
				cellBorderStyle.setHorizontalColor(Color.BLACK);
				cellBorderStyle.setHorizontalStyle(Constants.LINE_NONE);
				cellBorderStyle.setBottomColor(Color.BLACK);
				cellBorderStyle.setBottomStyle(Constants.LINE_NONE);
				cellBorderStyle.setLeftColor(Color.BLACK);
				cellBorderStyle.setLeftStyle(Constants.LINE_NONE);
				cellBorderStyle.setVerticalColor(Color.BLACK);
				cellBorderStyle.setVerticalStyle(Constants.LINE_NONE);
				cellBorderStyle.setRightColor(Color.BLACK);
				cellBorderStyle.setRightStyle(Constants.LINE_NONE);
			} else if (border == EXTERNAL_BORDERS) {
				cellBorderStyle.setTopColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setTopStyle(currentLineCombo.getSelectedLineStyle());
				cellBorderStyle.setBottomColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setBottomStyle(currentLineCombo.getSelectedLineStyle());
				cellBorderStyle.setLeftColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setLeftStyle(currentLineCombo.getSelectedLineStyle());
				cellBorderStyle.setRightColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setRightStyle(currentLineCombo.getSelectedLineStyle());
			} else if (border == INSIDE_BORDERS) {
				cellBorderStyle.setHorizontalColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setHorizontalStyle(currentLineCombo.getSelectedLineStyle());
				cellBorderStyle.setVerticalColor(currentLineColorPane.getSelectObject());
				cellBorderStyle.setVerticalStyle(currentLineCombo.getSelectedLineStyle());
			}

			refreshAllToggleButtons();
			borderComponent.repaint();
		}
	}


	private class BorderComponent extends JComponent {
		// Border is active?
		private boolean topActive = false;
		private boolean leftActive = false;
		private boolean bottomActive = false;
		private boolean rightActive = false;
		private boolean verticalActive = false;
		private boolean horizontalActive = false;

		private EventListenerList borderChangeListenerList = new EventListenerList();

		private BorderComponent() {
			setOpaque(true);

			// Listener allowing to set borders by clicking on component
			// If the border is already active needs to deactivate and unset it
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if (me.getX() <= 24) {// Left 优先
						if (cellBorderStyle.getLeftColor() == currentLineColorPane.getSelectObject() && cellBorderStyle.getLeftStyle() == currentLineCombo.getSelectedLineStyle()) {
							cellBorderStyle.setLeftColor(Color.BLACK);
							cellBorderStyle.setLeftStyle(Constants.LINE_NONE);
							setActiveBorders(BorderPane.NO_BORDERS);
						} else {
							setActiveBorders(BorderPane.LEFT_BORDER);
							cellBorderStyle.setLeftColor(currentLineColorPane.getSelectObject());
							cellBorderStyle.setLeftStyle(currentLineCombo.getSelectedLineStyle());
						}
					} else if (me.getY() <= 24) {// Top 优先。
						if (cellBorderStyle.getTopColor() == currentLineColorPane.getSelectObject() && cellBorderStyle.getTopStyle() == currentLineCombo.getSelectedLineStyle()) {
							cellBorderStyle.setTopColor(Color.BLACK);
							cellBorderStyle.setTopStyle(Constants.LINE_NONE);
							setActiveBorders(BorderPane.NO_BORDERS);
						} else {
							setActiveBorders(BorderPane.TOP_BORDER);
							cellBorderStyle.setTopColor(currentLineColorPane.getSelectObject());
							cellBorderStyle.setTopStyle(currentLineCombo.getSelectedLineStyle());
						}
					} else if (me.getY() > (getHeight() - 24)) {// Bottom 优先
						if (cellBorderStyle.getBottomColor() == currentLineColorPane.getSelectObject()
								&& cellBorderStyle.getBottomStyle() == currentLineCombo.getSelectedLineStyle()) {
							cellBorderStyle.setBottomColor(Color.BLACK);
							cellBorderStyle.setBottomStyle(Constants.LINE_NONE);
							setActiveBorders(BorderPane.NO_BORDERS);
						} else {
							setActiveBorders(BorderPane.BOTTOM_BORDER);
							cellBorderStyle.setBottomColor(currentLineColorPane.getSelectObject());
							cellBorderStyle.setBottomStyle(currentLineCombo.getSelectedLineStyle());
						}
					} else if (me.getX() > (getWidth() - 24)) {// Right 最后
						if (cellBorderStyle.getRightColor() == currentLineColorPane.getSelectObject() && cellBorderStyle.getRightStyle() == currentLineCombo.getSelectedLineStyle()) {
							cellBorderStyle.setRightColor(Color.BLACK);
							cellBorderStyle.setRightStyle(Constants.LINE_NONE);
							setActiveBorders(BorderPane.NO_BORDERS);
						} else {
							setActiveBorders(BorderPane.RIGHT_BORDER);
							cellBorderStyle.setRightColor(currentLineColorPane.getSelectObject());
							cellBorderStyle.setRightStyle(currentLineCombo.getSelectedLineStyle());
						}
					} else {
						if (insideMode) {
							if (me.getX() > (getWidth() / 2 - 8) && me.getX() < (getWidth() / 2 + 8)) { // 竖线
								if (cellBorderStyle.getVerticalColor() == currentLineColorPane.getSelectObject()
										&& cellBorderStyle.getVerticalStyle() == currentLineCombo.getSelectedLineStyle()) {
									cellBorderStyle.setVerticalColor(Color.BLACK);
									cellBorderStyle.setVerticalStyle(Constants.LINE_NONE);
									setActiveBorders(BorderPane.NO_BORDERS);
								} else {
									setActiveBorders(BorderPane.VERTICAL_BORDER);
									cellBorderStyle.setVerticalColor(currentLineColorPane.getSelectObject());
									cellBorderStyle.setVerticalStyle(currentLineCombo.getSelectedLineStyle());
								}
							} else if (me.getY() > (getHeight() / 2 - 8) && me.getY() < (getHeight() / 2 + 8)) {// 横线
								if (cellBorderStyle.getHorizontalColor() == currentLineColorPane.getSelectObject()
										&& cellBorderStyle.getHorizontalStyle() == currentLineCombo.getSelectedLineStyle()) {
									cellBorderStyle.setHorizontalColor(Color.BLACK);
									cellBorderStyle.setHorizontalStyle(Constants.LINE_NONE);
									setActiveBorders(BorderPane.NO_BORDERS);
								} else {
									setActiveBorders(BorderPane.HORIZONTAL_BORDER);
									cellBorderStyle.setHorizontalColor(currentLineColorPane.getSelectObject());
									cellBorderStyle.setHorizontalStyle(currentLineCombo.getSelectedLineStyle());
								}
							}
						}
					}

					refreshAllToggleButtons();
					repaint();
				}
			});
		}

		// Paint the background component which depends on cell set case
		public void paint(Graphics g) {
			// background
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

			// borders
			g.setColor(Color.black);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

			// up left corner
			if (topActive) {
				int[] x = { 3, 3, 10 };
				int[] y = { 7, 13, 10 };
				g.fillPolygon(x, y, 3);
			} else {
				g.drawLine(3, 10, 10, 10);
			}

			if (leftActive) {
				int[] x = { 7, 13, 10 };
				int[] y = { 3, 3, 10 };
				g.fillPolygon(x, y, 3);
			} else {
				g.drawLine(10, 3, 10, 10);
			}

			// up middle
			if (insideMode) {
				g.drawLine(getWidth() / 2 - 3, 10, getWidth() / 2 + 3, 10);
				if (verticalActive) {
					int[] x = { getWidth() / 2 - 3, getWidth() / 2 + 3, getWidth() / 2 };
					int[] y = { 3, 3, 10 };
					g.fillPolygon(x, y, 3);
				} else {
					g.drawLine(getWidth() / 2, 3, getWidth() / 2, 10);
				}
			}

			// up right corner
			if (topActive) {
				int[] x = { getWidth() - 3, getWidth() - 11, getWidth() - 3 };
				int[] y = { 7, 10, 13 };
				g.fillPolygon(x, y, 3);
			} else {
				g.drawLine(getWidth() - 11, 10, getWidth() - 4, 10);
			}

			if (rightActive) {
				int[] x = { getWidth() - 14, getWidth() - 8, getWidth() - 11 };
				int[] y = { 3, 3, 10 };
				g.fillPolygon(x, y, 3);
			} else {
				g.drawLine(getWidth() - 11, 3, getWidth() - 11, 10);
			}

			// right middle
			if (insideMode) {
				g.drawLine(getWidth() - 11, getHeight() / 2 - 3, getWidth() - 11, getHeight() / 2 + 3);
				if (horizontalActive) {
					int[] x = { getWidth() - 3, getWidth() - 11, getWidth() - 3 };
					int[] y = { getHeight() / 2 - 3, getHeight() / 2, getHeight() / 2 + 3 };
					g.fillPolygon(x, y, 3);
				} else {
					g.drawLine(getWidth() - 11, getHeight() / 2, getWidth() - 4, getHeight() / 2);
				}
			}

			// down left corner
			if (bottomActive) {
				int[] x = { 3, 3, 10 };
				int[] y = { getHeight() - 14, getHeight() - 8, getHeight() - 11 };
				g.fillPolygon(x, y, 3);
			} else {
				g.drawLine(3, getHeight() - 11, 10, getHeight() - 11);
			}

			if (leftActive) {
				int[] x = { 7, 13, 10 };
				int[] y = { getHeight() - 3, getHeight() - 3, getHeight() - 11 };
				g.fillPolygon(x, y, 3);
			} else {
				g.drawLine(10, getHeight() - 11, 10, getHeight() - 4);
			}

			// left middle
			if (insideMode) {
				g.drawLine(10, getHeight() / 2 - 3, 10, getHeight() / 2 + 3);
				if (horizontalActive) {
					int[] x = { 3, 3, 10 };
					int[] y = { getHeight() / 2 - 3, getHeight() / 2 + 3, getHeight() / 2 };
					g.fillPolygon(x, y, 3);
				} else {
					g.drawLine(3, getHeight() / 2, 10, getHeight() / 2);
				}
			}

			// down right corner
			if (rightActive) {
				int[] x = { getWidth() - 14, getWidth() - 8, getWidth() - 11 };
				int[] y = { getHeight() - 3, getHeight() - 3, getHeight() - 11 };
				g.fillPolygon(x, y, 3);
			} else {
				g.drawLine(getWidth() - 11, getHeight() - 11, getWidth() - 11, getHeight() - 4);
			}

			if (bottomActive) {
				int[] x = { getWidth() - 3, getWidth() - 11, getWidth() - 3 };
				int[] y = { getHeight() - 14, getHeight() - 11, getHeight() - 8 };
				g.fillPolygon(x, y, 3);
			} else {
				g.drawLine(getWidth() - 11, getHeight() - 11, getWidth() - 4, getHeight() - 11);
			}

			// bottom middle
			if (insideMode) {
				g.drawLine(getWidth() / 2 - 3, getHeight() - 11, getWidth() / 2 + 3, getHeight() - 11);
				if (verticalActive) {
					int[] x = { getWidth() / 2 - 3, getWidth() / 2 + 3, getWidth() / 2 };
					int[] y = { getHeight() - 3, getHeight() - 3, getHeight() - 11 };
					g.fillPolygon(x, y, 3);
				} else {
					g.drawLine(getWidth() / 2, getHeight() - 11, getWidth() / 2, getHeight() - 4);
				}
			}

			// inside square(s)
			if (insideMode) {
				int pInset = 4;
				int pWidth = this.getWidth() / 2 - 20 - pInset;
				int pHeight = this.getHeight() / 2 - 20 - pInset;
				g.setColor(Color.lightGray);
				g.fillRect(20, 20, pWidth, pHeight);
				g.setColor(Color.lightGray);
				g.fillRect(this.getWidth() / 2 + pInset + 1, 20, pWidth, pHeight);
				g.setColor(Color.lightGray);
				g.fillRect(20, this.getHeight() / 2 + pInset + 1, pWidth, pHeight);
				g.setColor(Color.lightGray);
				g.fillRect(this.getWidth() / 2 + pInset + 1, this.getHeight() / 2 + pInset + 1, pWidth, pHeight);
			} else {
				g.setColor(Color.lightGray);
				g.fillRect(20, 20, this.getWidth() - 40, this.getHeight() - 40);
			}
			updateBorders(g);

		}

		public void addBorderChangeListener(ChangeListener changeListener) {
			borderChangeListenerList.add(ChangeListener.class, changeListener);
		}

		/**
         */
		public void fireBorderStateChanged() {
			Object[] listeners = borderChangeListenerList.getListenerList();
			ChangeEvent e = null;

			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == ChangeListener.class) {
					if (e == null) {
						e = new ChangeEvent(this);
					}
					((ChangeListener)listeners[i + 1]).stateChanged(e);
				}
			}
		}

		// Draw the borders which specified color and style on component
		private void updateBorders(Graphics g) {
			Graphics2D g2D = (Graphics2D)g;
			currentLineColorPane.getSelectObject();

			if (cellBorderStyle.getTopColor() != null && cellBorderStyle.getTopStyle() != Constants.LINE_NONE) {
				g2D.setColor(cellBorderStyle.getTopColor());
				GraphHelper.drawLine(g2D, 15, 15, getWidth() - 15, 15, cellBorderStyle.getTopStyle());
			}

			if (cellBorderStyle.getLeftColor() != null && cellBorderStyle.getLeftStyle() != Constants.LINE_NONE) {
				g2D.setColor(cellBorderStyle.getLeftColor());
				GraphHelper.drawLine(g2D, 15, 15, 15, getHeight() - 15, cellBorderStyle.getLeftStyle());
			}

			if (cellBorderStyle.getBottomColor() != null && cellBorderStyle.getBottomStyle() != Constants.LINE_NONE) {
				g2D.setColor(cellBorderStyle.getBottomColor());
				GraphHelper.drawLine(g2D, 15, getHeight() - 15, getWidth() - 15, getHeight() - 15, cellBorderStyle.getBottomStyle());
			}

			if (cellBorderStyle.getRightColor() != null && cellBorderStyle.getRightStyle() != Constants.LINE_NONE) {
				g2D.setColor(cellBorderStyle.getRightColor());
				GraphHelper.drawLine(g2D, getWidth() - 15, 15, getWidth() - 15, getHeight() - 15, cellBorderStyle.getRightStyle());
			}

			if (cellBorderStyle.getVerticalColor() != null && cellBorderStyle.getVerticalStyle() != Constants.LINE_NONE) {
				g2D.setColor(cellBorderStyle.getVerticalColor());
				GraphHelper.drawLine(g2D, getWidth() / 2, 16, getWidth() / 2, getHeight() - 16, cellBorderStyle.getVerticalStyle());
			}

			if (cellBorderStyle.getHorizontalColor() != null && cellBorderStyle.getHorizontalStyle() != Constants.LINE_NONE) {
				g2D.setColor(cellBorderStyle.getHorizontalColor());
				GraphHelper.drawLine(g2D, 16, getHeight() / 2, getWidth() - 16, getHeight() / 2, cellBorderStyle.getHorizontalStyle());
			}

			fireBorderStateChanged();
		}

		// Set active the specified border and deactive others
		private void setActiveBorders(int activeBorders) {
			switch (activeBorders) {
			case BorderPane.NO_BORDERS:
				topActive = leftActive = bottomActive = rightActive = verticalActive = horizontalActive = false;
				break;
			case BorderPane.EXTERNAL_BORDERS:
				topActive = leftActive = bottomActive = rightActive = true;
				verticalActive = horizontalActive = false;
				break;
			case BorderPane.INSIDE_BORDERS:
				topActive = leftActive = bottomActive = rightActive = false;
				verticalActive = horizontalActive = true;
				break;
			case BorderPane.TOP_BORDER:
				topActive = true;
				leftActive = bottomActive = rightActive = verticalActive = horizontalActive = false;
				break;
			case BorderPane.LEFT_BORDER:
				leftActive = true;
				topActive = bottomActive = rightActive = verticalActive = horizontalActive = false;
				break;
			case BorderPane.BOTTOM_BORDER:
				bottomActive = true;
				topActive = leftActive = rightActive = verticalActive = horizontalActive = false;
				break;
			case BorderPane.RIGHT_BORDER:
				rightActive = true;
				topActive = leftActive = bottomActive = verticalActive = horizontalActive = false;
				break;
			case BorderPane.VERTICAL_BORDER:
				verticalActive = true;
				topActive = leftActive = bottomActive = rightActive = horizontalActive = false;
				break;
			case BorderPane.HORIZONTAL_BORDER:
				horizontalActive = true;
				topActive = leftActive = bottomActive = rightActive = verticalActive = false;
				break;
			}
		}
	}
}
