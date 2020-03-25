package com.fr.design.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

/**
 * created by Harrison on 2020/03/22
 **/
public class SnapChatUtil {
    
    public static void paintSnapChat(Graphics g, Rectangle textRect) {
        
        Color oldColor = g.getColor();
        
        double x = textRect.getWidth();
        x += textRect.getX();
        x += 2;
    
        double y = textRect.getY();
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        Ellipse2D.Double shape =
                new Ellipse2D.Double(x, y, 4, 4);
        g2d.fill(shape);
        g2d.draw(shape);
        
        g2d.setColor(oldColor);
    }
}
