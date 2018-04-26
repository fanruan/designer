package com.fr.design.style;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.background.ColorBackground;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.BackgroundCustomJComponent;
import com.fr.design.style.color.ColorSelectPane;


/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-24 下午03:02:37
 * 类说明: 自定义形状的 弹出box
 */
public class CustomSelectBox extends AbstractPopBox {

	private Color color;
	
	public CustomSelectBox(int direction) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setSize(20, 20);
		
		displayComponent = new BackgroundCustomJComponent(direction);
	    this.add(displayComponent, BorderLayout.CENTER);
	    
	    displayComponent.setOpaque(false);
        displayComponent.setPreferredSize(new Dimension(20, 15));
	    
	    this.setSelectObject(Color.BLACK);
		
		this.addMouseListener(mouseListener);
	}
	
	@Override
	 public JPanel initWindowPane(double preferredWidth) {
    	ColorSelectPane colorPane = new ColorSelectPane();
    	
    	colorPane.addChangeListener(new ChangeListener() {
    		public void stateChanged(ChangeEvent e) {
    			ColorSelectPane source = (ColorSelectPane)e.getSource();
    			setSelectObject(source.getColor());
    			hidePopupMenu();
    		}
    	});
    	return colorPane;
    }
    
    public Color getSelectObject() {
        return this.color;
    }

    public void setSelectObject(Color color) {
    	this.color = color;
    	
    	fireDisplayComponent(ColorBackground.getInstance(color));
    }
}