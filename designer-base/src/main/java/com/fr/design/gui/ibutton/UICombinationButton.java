package com.fr.design.gui.ibutton;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.fr.design.constants.UIConstants;
import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUICoreUtils;

public class UICombinationButton extends JPanel{
	protected UIButton leftButton;
	protected UIButton rightButton;
	
	protected void leftButtonClickEvent() {
		// 左边按钮点击事件
	}
	
	protected void rightButtonClickEvent() {
		// 右边按钮点击事件
	}
	
	public UICombinationButton() {
		this(new UIButton(), new UIButton());
	}
	
	public UICombinationButton(UIButton left, UIButton right) {
		leftButton = left;
		rightButton = right;
		
		leftButton.setRoundBorder(true, Constants.RIGHT);
		rightButton.setRoundBorder(true, Constants.LEFT);
		
		leftButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				rightButton.getModel().setPressed(true);
				rightButton.getModel().setSelected(true);
				rightButton.repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				rightButton.getModel().setPressed(false);
				rightButton.getModel().setSelected(false);
				rightButton.repaint();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				leftButtonClickEvent();
			}
		});
		rightButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				leftButton.getModel().setPressed(true);
				leftButton.getModel().setSelected(true);
				leftButton.repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				leftButton.getModel().setPressed(false);
				leftButton.getModel().setSelected(false);
				leftButton.repaint();
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				rightButtonClickEvent();
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(leftButton, BorderLayout.CENTER);
		this.add(rightButton, BorderLayout.EAST);
	}

	public UICombinationButton(String left, Icon right) {
		this();
		leftButton.setText(left);
		rightButton.setIcon(right);
	}
	
	public UICombinationButton(String left, String right) {
		this();
		leftButton.setText(left);
		rightButton.setText(right);
	}
	
	public UICombinationButton(Icon left, Icon right) {
		this();
		leftButton.setIcon(left);
		rightButton.setIcon(right);
	}
	
	public UIButton getLeftButton() {
		return leftButton;
	}
	
	public void setExtraPainted(boolean isExtraPainted) {
		if(!isExtraPainted) {
			leftButton.setBackground(null);
			rightButton.setBackground(null);
			leftButton.setOpaque(false);
			rightButton.setOpaque(false);
		}
	}
	
	public UIButton getRightButton() {
		return rightButton;
	}
	
	public void set4Toolbar() {
		leftButton.setNormalPainted(false);
		rightButton.setNormalPainted(false);
		leftButton.setBorderPaintedOnlyWhenPressed(true);
		rightButton.setBorderPaintedOnlyWhenPressed(true);
	}
	
	protected void showPopWindow(JPopupMenu menu) {
		GUICoreUtils.showPopupMenu(menu, this, 0, getY() + getHeight() - 3);
	}

	public static void main(String... args) {
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel)jf.getContentPane();
		content.setLayout(null);
		
		UICombinationButton bb = new UICombinationButton("123455", UIConstants.ARROW_DOWN_ICON);
		bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
		content.add(bb);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}
}