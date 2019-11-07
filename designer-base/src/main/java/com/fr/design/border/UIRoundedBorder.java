package com.fr.design.border;

import com.fr.base.GraphHelper;

import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

public class UIRoundedBorder extends LineBorder {

	private static final long serialVersionUID = 1L;

	private int roundedCorner;
	private int lineStyle;

	public UIRoundedBorder(Color color) {
		super(color);
	}

	public UIRoundedBorder(Color color, int thickness){
		super(color, thickness);
	}

	public UIRoundedBorder(Color color, int thickness, int roundedCorners){
		super(color, thickness, true);
		this.roundedCorner = roundedCorners;
	}

	public UIRoundedBorder(int lineStyle, Color color, int roundedCorners){
		super(color, GraphHelper.getLineStyleSize(lineStyle), true);
		this.lineStyle = lineStyle;
		this.roundedCorner = roundedCorners;
	}

	public int getRoundedCorner() {
		return roundedCorner;
	}

	public int getLineStyle() {
		return lineStyle;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
		Color oldColor = g.getColor();

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(lineColor);
		GraphHelper.draw(g2d, new RoundRectangle2D.Double(x, y, width - 1.0D, height - 1.0D, roundedCorner, roundedCorner), lineStyle);
        g2d.setColor(oldColor);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}
