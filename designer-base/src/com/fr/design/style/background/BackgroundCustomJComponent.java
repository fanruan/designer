package com.fr.design.style.background;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import com.fr.stable.Constants;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-24 下午03:15:05
 * 类说明: 自定义形状和背景的Component
 */
public class BackgroundCustomJComponent extends BackgroundJComponent {
	private static final long serialVersionUID = 8038447675721697619L;
	
	private int direction = Constants.LEFT;
	
	public BackgroundCustomJComponent(int direction) {
		this.direction = direction;
	}
	
    public void paint(Graphics g) {
    	Graphics2D g2d = (Graphics2D)g;
    	
    	if(background != null && this.getSize().getWidth() > 0 && this.getSize().getHeight() > 0) {
    		if(direction == Constants.LEFT) {
    			background.paint(g, new Rectangle2D.Double(2, 3, 9, 9));
    			
    			g2d.setColor(Color.gray);
    			g2d.draw(new Rectangle2D.Double(1, 2, 12, 10));
    			
    			GeneralPath path = new GeneralPath();
    			
    			path.moveTo(13, 2);
    			path.lineTo(13, 12);
    			path.lineTo(19, 7);
    			path.closePath();
    			g2d.setColor(Color.WHITE);
    			g2d.fill(path);
    			g2d.setColor(Color.gray);
    			g2d.draw(path);
    		} else if(direction == Constants.RIGHT) {// 20 15
    			background.paint(g, new Rectangle2D.Double(9, 3, 9, 9));
    			
    			g2d.setColor(Color.gray);
    			g2d.draw(new Rectangle2D.Double(7, 2, 12, 10));
    			
    			GeneralPath path = new GeneralPath();
    			
    			path.moveTo(7, 2);
    			path.lineTo(7, 12);
    			path.lineTo(1, 7);
    			path.closePath();
    			g2d.setColor(Color.WHITE);
    			g2d.fill(path);
    			g2d.setColor(Color.gray);
    			g2d.draw(path);
    		}
    	} else {
    		this.setBackground(null);
    	}
    }
}