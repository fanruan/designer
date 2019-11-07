package com.fr.design.gui.xpane;

import com.fr.base.FRContext;
import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.base.background.ColorBackground;
import com.fr.design.designer.creator.cardlayout.XCardSwitchButton;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.general.Background;
import com.fr.general.FRFont;

import com.fr.general.act.TitlePacker;
import com.fr.stable.Constants;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D.Double;
import java.awt.geom.RoundRectangle2D;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-9-18
 * Time: 下午3:31
 */
public class LayoutBorderPreviewPane extends JPanel{
    private titlePreviewPane jp;
    private LayoutBorderStyle borderStyle;
    private int smallGAP = 5;
    private int GAP = 10;
    private boolean isTabLayout;
    private static final String TAB_ZERO = "0";
    private static final String TAB_ONE = "1";

    /**
     * 是否为tab布局
     * @return 是否为tab布局
     */
    public boolean isTagLayout() {
		return isTabLayout;
	}

	public void setTagLayout(boolean isTagLayout) {
		this.isTabLayout = isTagLayout;
	}

	public LayoutBorderPreviewPane(LayoutBorderStyle borderStyle) {
        this.borderStyle = borderStyle;
        repaint();
        jp = new titlePreviewPane();
        add(jp);
    }
	
	public LayoutBorderPreviewPane(LayoutBorderStyle borderStyle, boolean isTabLayout) {
		this(borderStyle);
		this.isTabLayout = true;
    }
	
    /**
     * 重新画
     * @param borderStyle      样式
     */
    public void repaint(LayoutBorderStyle borderStyle){
        this.borderStyle = borderStyle;
        super.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Background background = borderStyle.getBackground();
        if (background != null) {
            Shape shape = new Double(smallGAP, smallGAP, this.getWidth() - GAP, this.getHeight() - GAP);
            background.paint(g, shape);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(smallGAP, smallGAP, this.getWidth() - GAP, this.getHeight() - GAP);
        }

        updateBorders(g);

    }

    private void updateBorders(Graphics g) {
        if(borderStyle != null){
            int height = borderStyle.getTitle().getFrFont().getSize() + GAP;
            jp.setPreferredSize(new Dimension(getWidth() - GAP, height));
            jp.setBounds(smallGAP,smallGAP, getWidth() - GAP, height);
            borderStyle.paint(g, new Double(smallGAP, smallGAP, getWidth() - GAP, getHeight() - GAP));
            jp.setFontObject(borderStyle.getTitle().getFrFont());
            showTitlePreviewPane();
        }
    }

    protected void showTitlePreviewPane(){
        jp.setVisible(borderStyle.getType() == LayoutBorderStyle.TITLE);
    }

    private class titlePreviewPane extends UITextArea {
        private FRFont frFont = null;

        public titlePreviewPane() {
            frFont = FRContext.getDefaultValues().getFRFont();
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
            TitlePacker title = borderStyle.getTitle();
            String paintText = title.getTextObject().toString();
            int startX1 = 0;
            int startY = 0;
            if(!isTabLayout){
                startX1 = (d.width - fm.stringWidth(paintText)) / 2;
                startY = (d.height - fm.getHeight()) / 2 + fm.getAscent();
                if (title.getPosition() == Constants.LEFT){
                    startX1 = smallGAP;
                }  else if (title.getPosition() == Constants.RIGHT){
                    startX1 = d.width - fm.stringWidth(paintText) - smallGAP - fm.getMaxAdvance();
                }
                Background background = title.getBackground();
                if (background != null) {
                    Shape shape = new Double(0, 0, this.getWidth(), this.getHeight());
                    background.paint(g, shape);
                }
                GraphHelper.drawString(g2d, paintText, startX1, startY);
            }else{
                startX1 = (d.width/2 - fm.stringWidth(paintText)) / 2;
                startY = (d.height - fm.getHeight()) / 2 + fm.getAscent();
                drawTabBack(g2d,g,title,fm,startX1,startY);
            }
            g.setColor(borderStyle.getColor());
            int line = GraphHelper.getLineStyleSize(borderStyle.getBorder());
            Double double1 =  new Double(0, getHeight()-1, getWidth(), getHeight());
            double x = double1.getX() + (line == 1 ? 1 : 2) - line - 1;
            double y = double1.getY() + (line == 1 ? 1 : 2) - line ;
            RoundRectangle2D.Double  double2 = new RoundRectangle2D.Double(x, y, double1.getWidth() + line, double1.getHeight() + line,  0, 0);
            GraphHelper.draw(g,double2, borderStyle.getBorder());
        }

        private void drawTabBack(Graphics2D g2d, Graphics g, TitlePacker title, FontMetrics fm, int startX1, int startY){
        	Dimension d = getSize();
        	String paintText = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Title")+TAB_ZERO;

            Background rightBack = ColorBackground.getInstance(XCardSwitchButton.CHOOSED_GRAL);
            Shape right = new Double(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
            rightBack.paint(g, right);
            String rightLabel = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Title")+TAB_ONE;
            GraphHelper.drawString(g2d, rightLabel, (d.width/2 - fm.stringWidth(paintText)) / 2+d.width/2, startY);
            Background background = title.getBackground();
            if (background != null) {
                Shape shape = new Double(0, 0, this.getWidth()/2, this.getHeight());
                background.paint(g, shape);
            }
            GraphHelper.drawString(g2d, paintText, startX1, startY);
        }

        public void setFontObject(FRFont font) {
            this.frFont = font;
            this.repaint();
        }
    }
    
}
