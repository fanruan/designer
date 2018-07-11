package com.fr.design.scrollruler;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.fr.base.ScreenResolution;
import com.fr.stable.Constants;
import com.fr.stable.unit.CM;
import com.fr.stable.unit.INCH;
import com.fr.stable.unit.MM;
import com.fr.stable.unit.PT;

public abstract class RulerUI extends ComponentUI {

	protected ScrollRulerComponent rulerComponent;
	
	public RulerUI(ScrollRulerComponent rulerComponent) {
		this.rulerComponent = rulerComponent;
	}
	
	protected double toPX(int i) {
		int resolution = ScreenResolution.getScreenResolution();
		if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_MM) {
			return new MM(i).toPixD(resolution);
		} else if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_CM) {
			return new CM(i).toPixD(resolution);
		} else if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_INCH) {
			return new INCH(i).toPixD(resolution);
		} else if(rulerComponent.getRulerLengthUnit() == Constants.UNIT_PT){
			return new PT(i).toPixD(resolution);
		} else {
			return i;
		}
	}
	
	protected int pxToLength(double d) {
		int resolution = ScreenResolution.getScreenResolution();
		if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_MM) {
			return (int) (d / (new MM(1).toPixD(resolution)));
		} else if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_CM) {
			return (int) (d / new CM(1).toPixD(resolution));
		} else if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_INCH) {
			return (int) (d / new INCH(1).toPixD(resolution));
		} else if(rulerComponent.getRulerLengthUnit() == Constants.UNIT_PT){
			return (int) (d / new PT(1).toPixD(resolution));
		} else {
			return (int) d;
		}
	}
	
	protected int getunit() {
		if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_MM) {
			return 1;
		} else if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_INCH) {
			return 1;
		} else {
			return BaseRuler.SCALE_10;
		}
	}

	public void paint(Graphics g, JComponent c) {
		if(c.isOpaque()) {
			g.setColor(c.getBackground());
		}
		if (!(c instanceof BaseRuler)) {
			throw new IllegalArgumentException("The component c to paint must be a Ruler!");
		}
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_MM) {
			paintRuler(g,1,((BaseRuler) c).getExtra(),c.getSize(),1);
		} else if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_CM) {
			paintRuler(g,10,((BaseRuler) c).getExtra(),c.getSize(),BaseRuler.SCALE_10);
		} else if (rulerComponent.getRulerLengthUnit() == Constants.UNIT_INCH) {
			paintRuler(g,10,((BaseRuler) c).getExtra(),c.getSize(),BaseRuler.SCALE_10);
		} else if(rulerComponent.getRulerLengthUnit() == Constants.UNIT_PT){
			paintPTRuler(g,((BaseRuler) c).getExtra(),c.getSize(),BaseRuler.SCALE_5);
		} else {
			paintPTRuler(g,((BaseRuler) c).getExtra(),c.getSize(),BaseRuler.SCALE_10);
		}
	}
	protected abstract void paintPTRuler(Graphics g, int extra, Dimension size, int j);

	protected abstract void paintRuler(Graphics g, int showText, int extra, Dimension size, int ratio);
	
}