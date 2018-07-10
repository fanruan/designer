/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.headerfooter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;

import com.fr.base.headerfooter.DateHFElement;
import com.fr.base.headerfooter.FormulaHFElement;
import com.fr.base.headerfooter.HFElement;
import com.fr.base.headerfooter.ImageHFElement;
import com.fr.base.headerfooter.NewLineHFElement;
import com.fr.base.headerfooter.NumberOfPageHFElement;
import com.fr.base.headerfooter.PageNumberHFElement;
import com.fr.base.headerfooter.TextHFElement;
import com.fr.base.headerfooter.TimeHFElement;
import com.fr.page.ReportSettingsProvider;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.base.PaperSize;
import com.fr.base.ScreenResolution;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.report.core.ReportHF;
import com.fr.stable.Constants;
import com.fr.stable.unit.CM;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.INCH;
import com.fr.stable.unit.MM;
import com.fr.stable.unit.UNIT;
import com.fr.design.style.background.BackgroundPane;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * Edit header or footer(the object ReportHF).
 */
public class HeaderFooterEditPane extends JPanel {
	private HFPreviewPane hfPreviewPane;
	private JScrollPane scrollPreviewPane;

	private HFContainer leftHFContainer;
	private HFContainer centerHFContainer;
	private HFContainer rightHFContainer;
	private HFContainer currentHFContainer;
	private AdjustHeightPane headerUnitFieldPane;
	private AdjustHeightPane footerUnitFieldPane;
	private AdjustHeightPane headFootUnitFieldPane;

	private UICheckBox printBackgroundCheckBox;
	private Background background;

	private double hfWidth;
	/**
	 * Constructor
	 */
	public HeaderFooterEditPane() {
		this.initComponents();
	}

	/**
	 * init components.
	 */
	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel centerPane =FRGUIPaneFactory.createBorderLayout_L_Pane();
		this.add(centerPane, BorderLayout.CENTER);

		JPanel hfPreviewContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		centerPane.add(hfPreviewContentPane, BorderLayout.CENTER);
		hfPreviewContentPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Preview"),null));

		hfPreviewPane = new HFPreviewPane();
		scrollPreviewPane = new JScrollPane(hfPreviewPane);
		hfPreviewContentPane.add(scrollPreviewPane, BorderLayout.CENTER);

		JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		centerPane.add(controlPane, BorderLayout.NORTH);
		controlPane.add(createToolbar(), BorderLayout.NORTH);
		//center panel
		JPanel controlContentPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(3);
		controlPane.add(controlContentPane, BorderLayout.CENTER);
		controlContentPane.setPreferredSize(new Dimension(centerPane.getPreferredSize().width, 120));

		ChangeListener contentChangeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				refreshPreivewPane();
			}
		};

		leftHFContainer = new HFContainer();
		centerHFContainer = new HFContainer();
		rightHFContainer = new HFContainer();

		leftHFContainer.addMouseListener(this.focusMouseListener);
		centerHFContainer.addMouseListener(this.focusMouseListener);
		rightHFContainer.addMouseListener(this.focusMouseListener);

		leftHFContainer.setContentChangeListener(contentChangeListener);
		centerHFContainer.setContentChangeListener(contentChangeListener);
		rightHFContainer.setContentChangeListener(contentChangeListener);

		controlContentPane.add(createContainerSection(Inter.getLocText("HF-Left_Section") + ":", leftHFContainer));
		controlContentPane.add(createContainerSection(Inter.getLocText("HF-Center_Section") + ":", centerHFContainer));
		controlContentPane.add(createContainerSection(Inter.getLocText("HF-Right_Section") + ":", rightHFContainer));

		//set current HFContainer
		setCurrentHFContainer(leftHFContainer);

	}

	private JToolBar createToolbar() {
		//toolbar.
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		Dimension separatorDimension = new Dimension(6, 18);//Separator分隔线

		toolbar.add(this.createHFInsertButton(new TextHFElement()));
		toolbar.add(this.createHFInsertButton(new FormulaHFElement()));
		toolbar.addSeparator(separatorDimension);
		toolbar.add(this.createHFInsertButton(new PageNumberHFElement()));
		toolbar.add(this.createHFInsertButton(new NumberOfPageHFElement()));
		toolbar.addSeparator(separatorDimension);
		toolbar.add(this.createHFInsertButton(new DateHFElement()));
		toolbar.add(this.createHFInsertButton(new TimeHFElement()));
		toolbar.addSeparator(separatorDimension);
		toolbar.add(this.createHFInsertButton(new ImageHFElement()));
		toolbar.add(this.createHFInsertButton(new NewLineHFElement()));
		toolbar.addSeparator(separatorDimension);        
		UIButton customBackgroundButton = new UIButton();
        customBackgroundButton.setToolTipText(Inter.getLocText("Background"));
		customBackgroundButton.set4ToolbarButton();
		customBackgroundButton.setIcon(BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/background.png"));
		toolbar.add(customBackgroundButton);
		customBackgroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				final BackgroundPane backgroundPane = new BackgroundPane();
				BasicDialog backgroundDialog = backgroundPane.showWindow(
						SwingUtilities.getWindowAncestor(HeaderFooterEditPane.this));
				backgroundPane.populate(background);               
				backgroundDialog.addDialogActionListener(new DialogActionAdapter() {
					@Override
					public void doOk() {
						background = backgroundPane.update();
						refreshPreivewPane();
					}                
				});
				backgroundDialog.setVisible(true);
			}
		});
		printBackgroundCheckBox = new UICheckBox(Inter.getLocText("ReportGUI-Print_Background"));
		toolbar.add(printBackgroundCheckBox);

		toolbar.addSeparator(separatorDimension);
		
		JPanel headerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		headerUnitFieldPane = new AdjustHeightPane();
		headerPane.add(new UILabel(Inter.getLocText("PageSetup-Header") + ":"));
		headerPane.add(headerUnitFieldPane);

		JPanel footerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		footerUnitFieldPane = new AdjustHeightPane();
		footerPane.add(new UILabel(Inter.getLocText("PageSetup-Footer") + ":"));
		footerPane.add(footerUnitFieldPane);

		JPanel headerFooterPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		headFootUnitFieldPane = new AdjustHeightPane();
		headerFooterPane.add(new UILabel(Inter.getLocText("Height") + ":"));
		headerFooterPane.add(headFootUnitFieldPane);  
		
		toolbar.add(headerFooterPane);
		return toolbar;
	}


	/**
	 * Create HFInsertButton.
	 */
	public HFInsertButton createHFInsertButton(HFElement hfElement) {
		HFInsertButton hfInsertButton = new HFInsertButton(hfElement);
		hfInsertButton.addActionListener(insertActionListener);

		return hfInsertButton;
	}

	/**
	 * Create container section.
	 */
	public JPanel createContainerSection(String containerTitle, HFContainer hfContainer) {
		JPanel sectionPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		//        sectionPane.setLayout(FRGUIPaneFactory.createBorderLayout());

		UILabel titleLabel = new UILabel(containerTitle);
		sectionPane.add(titleLabel, BorderLayout.NORTH);

		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

		sectionPane.add(new JScrollPane(hfContainer), BorderLayout.CENTER);

		return sectionPane;
	}

	/**
	 * Popupate pane according to the input reportHF.
	 */
	public void populate(ReportHF reportHF, double hfWidth, double hfHeight) {
		if (reportHF == null) {
			reportHF = new ReportHF();
		}

		this.hfWidth = hfWidth;

		this.leftHFContainer.populate(reportHF.getLeftList());
		this.centerHFContainer.populate(reportHF.getCenterList());
		this.rightHFContainer.populate(reportHF.getRightList());

		this.background = reportHF.getBackground();
		this.printBackgroundCheckBox.setSelected(reportHF.isPrintBackground());

		//need to refrsh.
		refreshPreivewPane();
	}

	/**
	 * Update reportHF according to current pane.
	 */
	public ReportHF update() {
		ReportHF reportHF = new ReportHF();

		reportHF.setLeftList(this.leftHFContainer.update());
		reportHF.setCenterList(this.centerHFContainer.update());
		reportHF.setRightList(this.rightHFContainer.update());

		reportHF.setBackground(this.background);
		reportHF.setPrintBackground(this.printBackgroundCheckBox.isSelected());

		return reportHF;
	}

	/**
	 * populate pageSetting
	 */
	public void populateReportSettings(ReportSettingsProvider reportSettings, boolean isHeader) {
		headerUnitFieldPane.setUnitValue(reportSettings.getHeaderHeight());
		footerUnitFieldPane.setUnitValue(reportSettings.getFooterHeight());
		if (isHeader) {
			headFootUnitFieldPane.setUnitValue(reportSettings.getHeaderHeight());
		} else {
			headFootUnitFieldPane.setUnitValue(reportSettings.getFooterHeight());
		}
		refreshPreivewPane();

	}

	/**
	 * update pageSetting
	 */
	public UNIT updateReportSettings() {
		return this.headFootUnitFieldPane.getUnitValue();
	}

	private void refreshPreivewPane() {
		ReportHF reportHF = this.update();
		this.hfPreviewPane.refreshReportHFPaintable(reportHF, (int)hfWidth, (int)(this.headFootUnitFieldPane.getUnitValue().toPixD(ScreenResolution.getScreenResolution())), 1, 100, 1);

	}

	/**
	 * Insert action listner
	 */
	private ActionListener insertActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			HFInsertButton hfInsertButton = (HFInsertButton) evt.getSource();

			HFElement insertHFElement;
			try {
				insertHFElement = (HFElement) hfInsertButton.getHFElement().clone();
			} catch (CloneNotSupportedException cloneNotSupportedException) {
				insertHFElement = hfInsertButton.getHFElement();
			}

			final HFComponent newHFComponent = new HFComponent(insertHFElement);

			//NewLineHFElement cannot be editable.
			if (!insertHFElement.getClass().equals(NewLineHFElement.class)) {
				//popup edit dialog.
				final HFAttributesEditDialog hfAttributesEditDialog = new HFAttributesEditDialog();
				hfAttributesEditDialog.populate(insertHFElement, true);

				hfAttributesEditDialog.showWindow(
						SwingUtilities.getWindowAncestor(HeaderFooterEditPane.this),
						new DialogActionAdapter(){
							@Override
							public void doOk() {
								hfAttributesEditDialog.update();
								currentHFContainer.addHFComponent(newHFComponent);

								//need to refrsh.
								refreshPreivewPane();
							}

						}).setVisible(true);
			}
			else
			{
				//显示换行符
				currentHFContainer.addHFComponent(newHFComponent);
			}

			//need to refrsh.
			refreshPreivewPane();
		}
	};

	/**
	 * Change focus listener
	 */
	private MouseListener focusMouseListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent evt) {
			Object obj = evt.getSource();

			if (obj instanceof HFContainer) {
				if (!((HFContainer) obj).isEnabled()) {//需要判断Enable.
					return;
				}

				((HFContainer) obj).requestFocus();

				//unfocus all.
				leftHFContainer.setBorder(null);
				centerHFContainer.setBorder(null);
				rightHFContainer.setBorder(null);

				setCurrentHFContainer((HFContainer) obj);
			}
		}
	};

	/**
	 * Set current select HFContainer.
	 */
	private void setCurrentHFContainer(HFContainer hfContainer) {
		currentHFContainer = hfContainer;
		currentHFContainer.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
	}

	class HFPreviewPane extends JPanel implements Scrollable {
		private int borderWidth = 8;
		private int borderHeight = 20;

		private ReportHF reportHF = null;
		private int hfWidth;
		private int hfHeight;
		private int pageNumber;
		private int totalPageNumber;
		private int firstPageNumber;

		public HFPreviewPane() {
			//用默认数值.
			this(new ReportHF(), 
					(int)(FU.getInstance(PaperSize.PAPERSIZE_A4.getWidth().toFU() - new INCH(0.75f).toFU() - new INCH(0.75f).toFU()).toPixD(ScreenResolution.getScreenResolution())), 
					(int)0.53 * ScreenResolution.getScreenResolution(), 1, 100, 1);
		}

		public HFPreviewPane(ReportHF reportHF, int hfWidth, int hfHeight, int pageNumber, int totalPageNumber, int firstPageNumber) {
			this.refreshReportHFPaintable(reportHF, hfWidth, hfHeight, pageNumber, totalPageNumber, firstPageNumber);
		}

		public void refreshReportHFPaintable(ReportHF reportHF, int hfWidth, int hfHeight, int pageNumber, int totalPageNumber, int firstPageNumber) {
			this.reportHF = reportHF;
			this.hfWidth = hfWidth;
			this.hfHeight = hfHeight;
			this.pageNumber = pageNumber;
			this.totalPageNumber = totalPageNumber;
			this.firstPageNumber = firstPageNumber;

			if(scrollPreviewPane != null) {
				scrollPreviewPane.validate();
				scrollPreviewPane.repaint();
				scrollPreviewPane.revalidate();
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (reportHF == null) {
				return;
			}

			Graphics2D g2d = (Graphics2D) g;

			//size
			Dimension size = this.getSize();
			if (!this.isEnabled()) {
				g2d.setPaint(SystemColor.control);
			} else {
				g2d.setPaint(Color.WHITE);
			}
			g2d.fill(new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight()));
			if (!this.isEnabled()) {//不能编辑,就不需要画了.
				return;
			}

			Rectangle2D rectangle2D = new Rectangle2D.Double(
					Math.max(0, (size.getWidth() - this.hfWidth) / 2), borderHeight,
					this.hfWidth, this.hfHeight);

			g2d.setPaint(Color.BLACK);
			GraphHelper.draw(g2d, new Rectangle2D.Double(rectangle2D.getX() - 1, rectangle2D.getY() - 1,
					rectangle2D.getWidth() + 1, rectangle2D.getHeight() + 1), Constants.LINE_DASH);

			this.reportHF.paint(g2d, rectangle2D, hfWidth, pageNumber, totalPageNumber, firstPageNumber, false, ScreenResolution.getScreenResolution());
		}

		/**
		 * Return preferredsize.
		 */
		@Override
		public Dimension getPreferredSize() {
			if (reportHF == null) {
				return super.getPreferredSize();
			}

			return new Dimension((borderWidth * 2 + this.hfWidth),
					(borderHeight * 2 + this.hfHeight));
		}

		// --- Scrollable methods ---------------------------------------------

		/**
		 * Returns the preferred size of the viewport for a view component.
		 * This is implemented to do the default behavior of returning
		 * the preferred size of the component.
		 *
		 * @return the <code>preferredSize</code> of a <code>JViewport</code>
		 *         whose view is this <code>Scrollable</code>
		 */
		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}


		/**
		 * Components that display logical rows or columns should compute
		 * the scroll increment that will completely expose one new row
		 * or column, depending on the value of orientation.  Ideally,
		 * components should handle a partially exposed row or column by
		 * returning the distance required to completely expose the item.
		 * <p/>
		 * The default implementation of this is to simply return 10% of
		 * the visible area.  Subclasses are likely to be able to provide
		 * a much more reasonable value.
		 *
		 * @param visibleRect the view area visible within the viewport
		 * @param orientation either <code>SwingConstants.VERTICAL</code> or
		 *                    <code>SwingConstants.HORIZONTAL</code>
		 * @param direction   less than zero to scroll up/left, greater than
		 *                    zero for down/right
		 * @return the "unit" increment for scrolling in the specified direction
		 * @throws IllegalArgumentException for an invalid orientation
		 * @see javax.swing.JScrollBar#setUnitIncrement
		 */
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			switch (orientation) {
			case SwingConstants.VERTICAL:
				return visibleRect.height / 10;
			case SwingConstants.HORIZONTAL:
				return visibleRect.width / 10;
			default:
				throw new IllegalArgumentException("Invalid orientation: " + orientation);
			}
		}

		/**
		 * Components that display logical rows or columns should compute
		 * the scroll increment that will completely expose one block
		 * of rows or columns, depending on the value of orientation.
		 * <p/>
		 * The default implementation of this is to simply return the visible
		 * area.  Subclasses will likely be able to provide a much more
		 * reasonable value.
		 *
		 * @param visibleRect the view area visible within the viewport
		 * @param orientation either <code>SwingConstants.VERTICAL</code> or
		 *                    <code>SwingConstants.HORIZONTAL</code>
		 * @param direction   less than zero to scroll up/left, greater than zero
		 *                    for down/right
		 * @return the "block" increment for scrolling in the specified direction
		 * @throws IllegalArgumentException for an invalid orientation
		 * @see javax.swing.JScrollBar#setBlockIncrement
		 */
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			switch (orientation) {
			case SwingConstants.VERTICAL:
				return visibleRect.height;
			case SwingConstants.HORIZONTAL:
				return visibleRect.width;
			default:
				throw new IllegalArgumentException("Invalid orientation: " + orientation);
			}
		}


		/**
		 * Returns true if a viewport should always force the width of this
		 * <code>Scrollable</code> to match the width of the viewport.
		 * For example a normal text view that supported line wrapping
		 * would return true here, since it would be undesirable for
		 * wrapped lines to disappear beyond the right
		 * edge of the viewport.  Note that returning true for a
		 * <code>Scrollable</code> whose ancestor is a <code>JScrollPane</code>
		 * effectively disables horizontal scrolling.
		 * <p/>
		 * Scrolling containers, like <code>JViewport</code>,
		 * will use this method each time they are validated.
		 *
		 * @return true if a viewport should force the <code>Scrollable</code>s
		 *         width to match its own
		 */
		public boolean getScrollableTracksViewportWidth() {
			if (getParent() instanceof JViewport) {
				return ((getParent()).getWidth() > getPreferredSize().width);
			}
			return false;
		}

		/**
		 * Returns true if a viewport should always force the height of this
		 * <code>Scrollable</code> to match the height of the viewport.
		 * For example a columnar text view that flowed text in left to
		 * right columns could effectively disable vertical scrolling by
		 * returning true here.
		 * <p/>
		 * Scrolling containers, like <code>JViewport</code>,
		 * will use this method each time they are validated.
		 *
		 * @return true if a viewport should force the Scrollables height
		 *         to match its own
		 */
		public boolean getScrollableTracksViewportHeight() {
			if (getParent() instanceof JViewport) {
				return ((getParent()).getHeight() > getPreferredSize().height);
			}
			return false;
		}
	}

	/**
	 * HFInser button.
	 */
	class HFInsertButton extends UIButton {
		private HFElement hfElement;

		public HFInsertButton(HFElement hfElement) {
			this.setHFElement(hfElement);
			this.set4ToolbarButton();
		}

		private void initAttributes() {
			this.setIcon(HFComponent.getHFElementIcon(hfElement));
			this.setToolTipText(HFComponent.getHFELementText(hfElement));
		}

		public HFElement getHFElement() {
			return this.hfElement;
		}

		public void setHFElement(HFElement hfElement) {
			this.hfElement = hfElement;

			//init attributes.
			this.initAttributes();
		}
	}

	/**
	 * AdjustHeightPane
	 */
	public class AdjustHeightPane extends JPanel {
		private UISpinner valueSpinner;
		private UILabel unitLabel;

		public AdjustHeightPane() {
			this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());

			valueSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
			this.add(valueSpinner);
			valueSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					refreshPreivewPane();
				}

			}); 

			// Setting compobox contents...
			unitLabel = new UILabel();

			setUnitType(Constants.UNIT_MM);
		}

		private void setUnitType(int unitType) {
			if (unitType == Constants.UNIT_CM) {
				unitLabel.setText(Inter.getLocText("Unit_CM"));
			} else if (unitType == Constants.UNIT_INCH) {
				unitLabel.setText(Inter.getLocText("PageSetup-inches"));
			} else {
				unitLabel.setText(Inter.getLocText("PageSetup-mm"));
			}

			//ajust the heigt of unitLabel.
			Dimension unitDimension = new Dimension(unitLabel.getPreferredSize().width,
					valueSpinner.getPreferredSize().height);
			unitLabel.setMinimumSize(unitDimension);
			unitLabel.setMinimumSize(unitDimension);
			unitLabel.setSize(unitDimension);
			unitLabel.setPreferredSize(unitDimension);

			this.add(unitLabel);
		}

		public UNIT getUnitValue() {
			int unitType = DesignerEnvManager.getEnvManager().getPageLengthUnit();
			if (unitType == Constants.UNIT_CM) {
				return new CM(((Number) valueSpinner.getValue()).floatValue());
			} else if (unitType == Constants.UNIT_INCH) {
				return new INCH(((Number) valueSpinner.getValue()).floatValue());
			} else {
				return new MM(((Number) valueSpinner.getValue()).floatValue());
			}
		}

		public void setUnitValue(UNIT value) {
			int unitType = DesignerEnvManager.getEnvManager().getPageLengthUnit();
			if (unitType == Constants.UNIT_CM) {
				valueSpinner.setValue((int) value.toCMValue4Scale2());
			} else if (unitType == Constants.UNIT_INCH) {
				valueSpinner.setValue((int) value.toINCHValue4Scale3());
			} else {
				valueSpinner.setValue((int) value.toMMValue4Scale2());
			}

			setUnitType(unitType);
		}
	}  
}