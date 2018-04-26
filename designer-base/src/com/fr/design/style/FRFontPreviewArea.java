package com.fr.design.style;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.fr.base.FRContext;
import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.general.FRFont;
import com.fr.stable.Constants;

/**
 * Preview the FRFont setting.
 * @author Richie
 *
 */
public class FRFontPreviewArea extends UITextArea {
	private String paintText = "Report";
	private FRFont frFont = null;
	
	public FRFontPreviewArea() {
		//初始化paintFont
        frFont = FRContext.getDefaultValues().getFRFont();
	}
	public FRFontPreviewArea(FRFont frFont) {
		this.setFontObject(frFont);
		this.repaint();
	}
	
	 public void paintComponent(Graphics g) {
         Graphics2D g2d = (Graphics2D) g;

         Dimension d = getSize();

         g2d.setColor(getBackground());
         GraphHelper.fillRect(g2d, 0, 0, d.width, d.height);

         if (frFont == null) {
             return;
         }

         FontMetrics fm = getFontMetrics(frFont);

         int resolution = ScreenResolution.getScreenResolution();
         if(this.isEnabled()) {
        	 g2d.setColor(frFont.getForeground());
         } else {
        	 g2d.setColor(new Color(237, 237, 237));
         }
         g2d.setFont(frFont.applyResolutionNP(resolution));

         int startX1 = (d.width - fm.stringWidth(paintText)) / 2;
         int startX2 = (d.width + fm.stringWidth(paintText)) / 2;
         int startY = (d.height - fm.getHeight()) / 2 + fm.getAscent();

         GraphHelper.drawLine(g2d, 4, startY, startX1 - 8, startY, Constants.LINE_THIN);
         GraphHelper.drawString(g2d, paintText, startX1, startY);
         GraphHelper.drawLine(g2d, startX2 + 8, startY, d.width - 4, startY, Constants.LINE_THIN);
     }
	
	 public FRFont getFontObject() {
		 
		return this.frFont;
	}

	public void setFontObject(FRFont font) {
		this.frFont = font;
		this.repaint();
	} 
}