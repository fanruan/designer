package com.fr.design.file;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.DesignerContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Author : daisy
 * Date: 13-8-27
 * Time: 下午6:07
 */
public abstract class NewTemplatePane extends JComponent implements MouseListener, MouseMotionListener {

	private static final Icon GRAY_NEW_CPT = BaseUtils.readIcon("/com/fr/design/images/buttonicon/additicon_grey.png");
	private static final int ICON_START_X = 5;
	private static final int HEIGHT = 26;
	private Graphics2D g2d;
	private Icon newWorkBookIconMode = null;


	public NewTemplatePane() {
        newWorkBookIconMode = getNew();
		this.setLayout(new BorderLayout(0, 0));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setBorder(null);
		this.setForeground(new Color(99, 99, 99));
	}

	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
        dim.width = HEIGHT;
		return dim;
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D) g;
		g2d.setColor(UIConstants.TEMPLATE_TAB_PANE_BACKGROUND);
		g2d.fill(new Rectangle2D.Double(0, 0, getWidth(),getHeight()));
		int sheetIconY = (getHeight() - newWorkBookIconMode.getIconHeight()) / 2;
		newWorkBookIconMode.paintIcon(this, g2d, ICON_START_X, sheetIconY);
//		paintUnderLine(g2d);
	}


	private void paintUnderLine(Graphics2D g2d) {
		//画下面的那条线
		g2d.setPaint(UIConstants.LINE_COLOR);
		g2d.draw(new Line2D.Double((float) 0, (float) (getHeight()-1), getWidth(), (float) (getHeight()-1)));
	}

    /**
     *鼠标点击
     * @param e 事件
     */
	public void mouseClicked(MouseEvent e) {
		if (needGrayNewCpt()) {
			newWorkBookIconMode = GRAY_NEW_CPT;
		}
	}

    /**
     *鼠标按下
     * @param e 事件
     */
	public void mousePressed(MouseEvent e) {
		int evtX = e.getX();
		if (needGrayNewCpt()) {
			newWorkBookIconMode = GRAY_NEW_CPT;
		}
		if (isOverNewIcon(evtX) && newWorkBookIconMode != GRAY_NEW_CPT) {
			newWorkBookIconMode = getMousePressNew();
			DesignerContext.getDesignerFrame().addAndActivateJTemplate();
		}
		this.repaint();
	}

    /**
     *鼠标松开
     * @param e 事件
     */
	public void mouseReleased(MouseEvent e) {
		if (needGrayNewCpt()) {
			newWorkBookIconMode = GRAY_NEW_CPT;
		}
	}

    /**
     *鼠标进入
     * @param e 事件
     */
	public void mouseEntered(MouseEvent e) {
		if (needGrayNewCpt()) {
			newWorkBookIconMode = GRAY_NEW_CPT;
		}
	}

    /**
     *鼠标离开
     * @param e 事件
     */
	public void mouseExited(MouseEvent e) {
		if (needGrayNewCpt()) {
			newWorkBookIconMode = GRAY_NEW_CPT;
		} else {
			newWorkBookIconMode = getNew();
		}

		this.repaint();
	}

    /**
     *鼠标拖拽
     * @param e 事件
     */
	public void mouseDragged(MouseEvent e) {
	}

    /**
     *鼠标移动
     * @param e 事件
     */
	public void mouseMoved(MouseEvent e) {
		int evtX = e.getX();
		if (needGrayNewCpt()) {
			newWorkBookIconMode = GRAY_NEW_CPT;
		} else if (isOverNewIcon(evtX)) {
			newWorkBookIconMode = getMouseOverNew();
		}

		this.repaint();

	}

	private boolean needGrayNewCpt() {
		return DesignerMode.isAuthorityEditing() || DesignerMode.isVcsMode();
	}


	private boolean isOverNewIcon(int evtX) {
		return (evtX >= ICON_START_X && evtX <= ICON_START_X + newWorkBookIconMode.getIconWidth());
	}

	public void setButtonGray(boolean isGray) {
		newWorkBookIconMode = isGray ? GRAY_NEW_CPT : getNew();
	}

    public abstract Icon getNew();

    public abstract Icon getMouseOverNew();

    public abstract Icon getMousePressNew();

}
