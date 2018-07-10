package com.fr.design.style.background.gradient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;


public class SelectColorPointBtn implements Comparable<SelectColorPointBtn> {
	private double x;
	private double y;
	private double a = 4;
	private double b = 4;
	private GeneralPath ipath;
	private GeneralPath jpath;
	private Color colorInner;

    /*提供一个可设置拖拉按钮边框颜色*/
    private Color borderColor;
	public SelectColorPointBtn(double m, double n, Color colorInner){
		this(m, n, colorInner, Color.BLACK);
	}

    public SelectColorPointBtn(double m, double n, Color colorInner, Color borderColor){
        this.x = m;
        this.y = n;
        this.colorInner = colorInner;
        this.borderColor = borderColor;
    }
	
	public void setColorInner(Color color){
		this.colorInner = color; 
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	private void getPath() {
		GeneralPath path = new GeneralPath();
		path.moveTo(x, y);
		path.lineTo(x+a, y+b);
		path.lineTo(x+a, y+b+2*a);
		path.lineTo(x-a, y+b+2*a);
		path.lineTo(x-a, y+b);
		path.closePath();
		this.ipath = path;
		
		GeneralPath pathInner = new GeneralPath();
		pathInner.moveTo(x+1-a, y+b);
		pathInner.lineTo(x-1+a, y+b);
		pathInner.lineTo(x-1+a, y-1+b+2*a);
		pathInner.lineTo(x+1-a, y-1+b+2*a);
		pathInner.closePath();
		this.jpath = pathInner;
	}
	
	public Color getColorInner(){
		return this.colorInner;
	}
	public void paint(Graphics g){
		getPath();
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(237, 237, 237));
		g2.fill(ipath);
		g2.setColor(borderColor);
		g2.draw(ipath);
		
		g2.setColor(this.colorInner);
		g2.fill(jpath);

		g2.setColor(new Color(228, 228, 228));
		g2.draw(jpath);
	}
	
	public boolean contains(double x, double y){
		return ipath.contains(x, y);
	}

	@Override
	public int compareTo(SelectColorPointBtn o) {
		return Double.compare(x, o.x);
	}
}