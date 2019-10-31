package com.fr.design.mainframe.chart.gui.type;

import com.fr.design.chart.series.PlotStyle.ChartSelectDemoPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.IOUtils;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ChartImagePane extends ChartSelectDemoPane {
	private static final long serialVersionUID = -2785128245790568603L;
	private static final int IMAGE_WIDTH = 58;
	private static final int IMAGE_HIGTH = 50;
	private static final Color ENTER_COLOR = new Color(216, 242, 253);
	private boolean isDrawRightLine = false;
    public boolean isDoubleClicked = false;
	
	public ChartImagePane(String iconPath, String tipName) {// 建立太复杂? 耗费内存..
        UILabel image = new UILabel(IOUtils.readIcon(iconPath));
        this.setLayout(new BorderLayout());
        this.add(image, BorderLayout.CENTER);
		addMouseListener(this);
		this.setToolTipText(tipName);
		
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, isDrawRightLine ? 1 : 0, UIConstants.SELECT_TAB));
		this.setBackground(UIConstants.TOOLBARUI_BACKGROUND);
        this.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HIGTH));
    }

    public ChartImagePane(String fullIconPath, String tipName, boolean isDrawRightLine){
        constructImagePane(fullIconPath, tipName, isDrawRightLine);
    }

    private void constructImagePane(final String fullIconPath, String tipName, boolean isDrawRightLine) {

        UILabel image = new UILabel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                BufferedImage image1 = IOUtils.readImageWithCache(fullIconPath);
                g.drawImage(image1, 0, 0, IMAGE_WIDTH, IMAGE_HIGTH, null);
            }
        };

        this.setLayout(new BorderLayout());
        this.add(image, BorderLayout.CENTER);
        addMouseListener(this);
        this.isDrawRightLine = isDrawRightLine;

        this.setToolTipText(tipName);

        this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, isDrawRightLine ? 1 : 0, UIConstants.SELECT_TAB));
        this.setBackground(UIConstants.TOOLBARUI_BACKGROUND);
        this.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HIGTH));

    }

    /**
     * 鼠标点击
     * @param e 事件
     */
    public void mouseClicked(MouseEvent e) {
        if(this.isPressing){
            this.isDoubleClicked = true;
        }else{
            this.isDoubleClicked = false;
        }
        super.mouseClicked(e);
    }

    /**
     * 修改边框颜色
     */
    public void checkBorder() {
        if (!isRollOver && !isPressing) {
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, isDrawRightLine ? 1 : 0, UIConstants.SELECT_TAB));
        } else if (isRollOver && !isPressing) {
            this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ENTER_COLOR));
        } else {
            this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, UIConstants.FLESH_BLUE));
        }
    }

}