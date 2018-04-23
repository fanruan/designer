package com.fr.design.actions.cell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.fr.base.BaseUtils;
import com.fr.base.CellBorderStyle;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UICombinationButton;
import com.fr.design.gui.ipoppane.PopupHider;
import com.fr.design.icon.BorderIcon;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.BorderUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.stable.Constants;
import com.fr.design.style.BorderPane;
import com.fr.design.style.color.TransparentPane;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * 这个Pane用来显示常用边框和设置自定义边框
 * 
 * @author richer
 * 
 */
public class UIToolbarBorderButton extends UICombinationButton implements PopupHider {
	private EventListenerList styleChangeListenerList = new EventListenerList();
	private boolean isCanBeNull = false;
	private ElementCasePane reportPane;
	private JPopupMenu popupWin;

	public UIToolbarBorderButton(Icon icon, ElementCasePane reportPane) {
		super(new UIButton(icon), new UIButton(BaseUtils.readIcon("/com/fr/design/images/gui/popup.gif")));
		this.reportPane = reportPane;
	}

	public CellBorderStyle getCellBorderStyle() {
		return this.cellBorderStyle;
	}

	public void setCellBorderStyle(CellBorderStyle cellBorderStyle) {
		this.cellBorderStyle = cellBorderStyle;
		this.leftButton.setIcon(new BorderIcon(cellBorderStyle));
		fireStyleStateChanged();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (!enabled) {
			cellBorderStyle = null;
		}

		leftButton.setEnabled(enabled);
		rightButton.setEnabled(enabled);
	}

	@Override
	public void setToolTipText(String tooltipText) {
		super.setToolTipText(tooltipText);

		leftButton.setToolTipText(tooltipText);
		rightButton.setToolTipText(tooltipText);
	}

	
	private void showPopupMenu() {
        if (popupWin != null && popupWin.isVisible()) {
            hidePopupMenu();
            return;
        }

        if (!this.isEnabled()) {
            return;
        }

        popupWin = this.getActionPopupMenu();
        GUICoreUtils.showPopupMenu(popupWin, this, 0, this.getSize().height);
    }

	protected JPopupMenu getActionPopupMenu() {
		if (this.popupWin == null) {
			this.popupWin = new BorderStyleControlWindow(this.isCanBeNull());
		}

		return popupWin;
	}
	
	protected void leftButtonClickEvent() {
		UIToolbarBorderButton.this.cellBorderStyle = ((BorderIcon)getLeftButton().getIcon()).cellBorderStyle;
		UIToolbarBorderButton.this.fireStyleStateChanged();
	}
	
	@Override
	protected void rightButtonClickEvent() {
		showPopupMenu();
	}

	class BorderStyleControlWindow extends JPopupMenu {

		/**
		 * Constructor
		 */
		public BorderStyleControlWindow(boolean isSupportTransparent) {
			this.initComponents(isSupportTransparent);
		}

		public void initComponents(boolean isSupportTransparent) {
			setLightWeightPopupEnabled(JPopupMenu.getDefaultLightWeightPopupEnabled());

			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			setBorderPainted(true);
			setBorder(UIManager.getBorder("PopupMenu.border"));
			setOpaque(false);
			setDoubleBuffered(true);
			setFocusable(false);

			this.add(new NormalBorderPane(isSupportTransparent, UIToolbarBorderButton.this), BorderLayout.CENTER);
			this.pack();
		}
	}

	// richer:常用边框线类型展现面板
	class NormalBorderPane extends TransparentPane {
		PopupHider popupHider;

		public NormalBorderPane(boolean isSupportTransparent, PopupHider popupHider) {
			super(isSupportTransparent);
			this.popupHider = popupHider;
		}

		@Override
		public void initCenterPaneChildren(JPanel centerPane) {
			JPanel menuColorPane = new /**/JPanel();
			centerPane.add(menuColorPane);

			menuColorPane.setLayout(new /**/GridLayout(3, 4, 2, 2));
			for (int i = 0; i < borderStyleArray.length; i++) {
				final UIButton borderStyleCell = new UIButton(new BorderIcon(borderStyleArray[i]));
				borderStyleCell.set4ToolbarButton();
				borderStyleCell.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						UIButton toolBarButton = (UIButton) e.getSource();
						if (toolBarButton.getIcon() instanceof BorderIcon) {
							BorderIcon borderIcon = (BorderIcon) toolBarButton.getIcon();
							UIToolbarBorderButton.this.setCellBorderStyle(borderIcon.cellBorderStyle);
							hidePopupMenu();
						} else {
							UIToolbarBorderButton.this.setCellBorderStyle(borderStyleArray[0]);
						}
					}
				});

				borderStyleCell.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseEntered(MouseEvent e) {
						borderStyleCell.setBorder(BorderFactory.createEtchedBorder());
					}

					@Override
					public void mouseExited(MouseEvent e) {
						borderStyleCell.setBorder(null);
					}
				});
				borderStyleCell.setToolTipText(BorderStyleTooltips[i]);
				menuColorPane.add(borderStyleCell);
			}

			centerPane.add(Box.createVerticalStrut(5));
			centerPane.add(new JSeparator());
			centerPane.add(Box.createVerticalStrut(5));
		}

		@Override
		public void doTransparent() {
			UIToolbarBorderButton.this.setCellBorderStyle(null);
			popupHider.hidePopupMenu();
		}

		@Override
		public void customButtonPressed() {
			popupHider.hidePopupMenu();
			final BorderPane borderPane = new BorderPane();
			BasicDialog borderDialog = borderPane.showWindow(SwingUtilities.getWindowAncestor(reportPane));
			Object[] fourObjectArray = BorderUtils.createCellBorderObject(reportPane);
			if (fourObjectArray != null && fourObjectArray.length == 4) {
				borderPane.populate((CellBorderStyle) fourObjectArray[0], ((Boolean) fourObjectArray[1]).booleanValue(),
						((Integer) fourObjectArray[2]).intValue(), (Color) fourObjectArray[3]);
			}
			borderDialog.addDialogActionListener(new DialogActionAdapter() {

				@Override
				public void doOk() {
					CellBorderStyle cellBorderStyle = borderPane.update();
					UIToolbarBorderButton.this.setCellBorderStyle(cellBorderStyle);
				}
			});
			borderDialog.setVisible(true);

		}

		@Override
		protected String title4PopupWindow() {
			return Inter.getLocText("Border");
		}
	}
	
	/**
     * Adds a new StyleChangeListener
     */
    public void addStyleChangeListener(ChangeListener changeListener) {
    	styleChangeListenerList.add(ChangeListener.class, changeListener);
    }

    /**
     * Removes an old StyleChangeListener.
     */
    public void removeColorChangeListener(ChangeListener changeListener) {
    	styleChangeListenerList.remove(ChangeListener.class, changeListener);
    }
    
    /**
     * 
     */
    public void fireStyleStateChanged() {
        Object[] listeners = styleChangeListenerList.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }
    
    public boolean isCanBeNull() {
		return this.isCanBeNull;
	}

	public void setCanBeNull(boolean isCanBeNull) {
		this.isCanBeNull = isCanBeNull;
	}

	private CellBorderStyle cellBorderStyle = new CellBorderStyle();

	private static final CellBorderStyle[] borderStyleArray = {
			new CellBorderStyle(),
			new CellBorderStyle(Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_THIN, Color.black,
					Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE),
			new CellBorderStyle(Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_THIN, Color.black, Constants.LINE_NONE, Color.black,
					Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE),
			new CellBorderStyle(Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black,
					Constants.LINE_THIN, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE),
			new CellBorderStyle(Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_DOUBLE, Color.black,
					Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE),
			new CellBorderStyle(Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_THICK, Color.black,
					Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE),
			new CellBorderStyle(Color.black, Constants.LINE_THIN, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_THIN, Color.black,
					Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE),
			new CellBorderStyle(Color.black, Constants.LINE_THIN, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_DOUBLE, Color.black,
					Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE),
			new CellBorderStyle(Color.black, Constants.LINE_THIN, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_THICK, Color.black,
					Constants.LINE_NONE, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE),
			new CellBorderStyle(Color.black, Constants.LINE_THIN, Color.black, Constants.LINE_THIN, Color.black, Constants.LINE_THIN, Color.black,
					Constants.LINE_THIN, Color.black, Constants.LINE_THIN, Color.black, Constants.LINE_THIN),
			new CellBorderStyle(Color.black, Constants.LINE_THIN, Color.black, Constants.LINE_THIN, Color.black, Constants.LINE_THIN, Color.black,
					Constants.LINE_THIN, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE),
			new CellBorderStyle(Color.black, Constants.LINE_THICK, Color.black, Constants.LINE_THICK, Color.black, Constants.LINE_THICK, Color.black,
					Constants.LINE_THICK, Color.black, Constants.LINE_NONE, Color.black, Constants.LINE_NONE) };
	private static final String[] BorderStyleTooltips = { Inter.getLocText("NO_Border_Line"), Inter.getLocText("Bottom_Border_Line"),
			Inter.getLocText("Left_Border_Line"), Inter.getLocText("Right_Border_Line"), Inter.getLocText("Double_Bottom_BorderLine"),
			Inter.getLocText("Thick_Bottom_Border_Line"), Inter.getLocText("Top_Bottom_Border_Line"),
			Inter.getLocText("Top_And_Double_Bottom_Border_Line"), Inter.getLocText("Top_And_Thick_Bottom_Border_Line"),
			Inter.getLocText("All_Border_Line"), Inter.getLocText("Out_Border_Line"), Inter.getLocText("Out_Thick_Border_Line") };

	@Override
	public void hidePopupMenu() {
		if (popupWin != null) {
            popupWin.setVisible(false);
        }

        popupWin = null;
	}
}