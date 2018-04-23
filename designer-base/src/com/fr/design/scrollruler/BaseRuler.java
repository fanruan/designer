package com.fr.design.scrollruler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;

import com.fr.design.constants.UIConstants;
import com.fr.general.FRFont;

public abstract class BaseRuler extends JComponent {

	protected static final int _WIDTH = 17;
	protected static final int _HEIGHT = 17;
    protected static final int SCALE_5 = 5;
    protected static final int SCALE_10 = 10;
    protected static final int SCALE_50 = 50;
    protected static final int SCALE_100 = 100;
    protected static final int NUMBER_99 = 99;
    protected static final int NUMBER_90 = 90;
    protected static final int NUMBER_5 = 5;
    protected static final int NUMBER_100 = 100;
    protected static final int NUMBER_11 = 11;
    protected static final int NUMBER_13 = 13;
    protected static final int NUMBER_14 = 14;
	protected static final Color DEFAULT_BG = UIConstants.DEFAULT_BG_RULER;
	protected static final Color STRAR_BG = new Color(144, 144, 144);
	protected static final Color UNIT_SIGN_COLOR =  UIConstants.RULER_LINE_COLOR;
	protected static final FRFont TEXT_FONT = FRFont.getInstance("Trebuchet MS", Font.PLAIN, (float) 7.6, UIConstants.RULER_SCALE_COLOR);

	private ScrollRulerComponent rc;

	public BaseRuler(ScrollRulerComponent rc) {
		this.rc = rc;
		
		setOpaque(true);
		setBackground(DEFAULT_BG);
		
		setUI(getRulerUI());
	}
//	private void initListener() {
//		this.addMouseListener(new MouseListener() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (e.isPopupTrigger()) {
//					if (menu == null) {
//						menu = new JPopupMenu();
//						ButtonGroup bg = new ButtonGroup();
//						for (int i : new int[] { 0, 1, 2 }) {
//							UIMenuItem item = createMenuItem((short) i);
//							menu.add(item);
//							bg.add(item);
//						}
//					}
//					menu.setSelected(MenuItemsMap.get(rc.getRulerLengthUnit()));
//					menu.show(e.getComponent(), e.getX(), e.getY());
//				}
//			}
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			@Override
//			public void mouseExited(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			@Override
//			public void mousePressed(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
//		
//	}

//	protected UIMenuItem createMenuItem(short i) {
//		UIMenuItem item = MenuItemsMap.get(i);
//		if(item == null) {
//			item = new RulerUnitItem(i);
//			MenuItemsMap.put(i, (RulerUnitItem) item);
//		}
//		return item;
//	}

	protected ScrollRulerComponent getRulerComponent() {
		return rc;
	}
	
	public abstract int getExtra();

	protected abstract RulerUI getRulerUI();
	
	public Dimension getPreferredSize() {
        return new Dimension(_WIDTH, _HEIGHT);
    }

//	class RulerUnitItem extends UICheckBoxMenuItem {
//		private short unit;
//
//		RulerUnitItem(short unit) {
//			this.unit = unit;
//			this.setText("单位：" + unit);
//		}
//
//		short getRulerUnit() {
//			return unit;
//		}
//	}
}