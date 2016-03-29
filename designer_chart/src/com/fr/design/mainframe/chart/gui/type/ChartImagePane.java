package com.fr.design.mainframe.chart.gui.type;

import com.fr.design.chart.series.PlotStyle.ChartSelectDemoPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ChartImagePane extends ChartSelectDemoPane {
	private static final long serialVersionUID = -2785128245790568603L;
	private boolean isDrawRightLine = false;
    public boolean isDoubleClicked = false;
	
	public ChartImagePane(String iconPath, String tipName) {// 建立太复杂? 耗费内存..
		UILabel image = new UILabel(new ImageIcon(getClass().getResource(iconPath)));
		this.setLayout(new BorderLayout());
		this.add(image, BorderLayout.CENTER);
		addMouseListener(this);
		this.setToolTipText(tipName);
		
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, isDrawRightLine ? 1 : 0, UIConstants.LINE_COLOR));
	}

    public ChartImagePane(String fullIconPath, String tipName, boolean isDrawRightLine){
        constructImagePane(fullIconPath, tipName, isDrawRightLine);
    }

    private void constructImagePane(String fullIconPath, String tipName, boolean isDrawRightLine){
        UILabel image = new UILabel(new ImageIcon(getClass().getResource(fullIconPath)));
        this.setLayout(new BorderLayout());
        this.add(image, BorderLayout.CENTER);
        addMouseListener(this);
        this.isDrawRightLine = isDrawRightLine;

        this.setToolTipText(tipName);

        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, isDrawRightLine ? 1 : 0, UIConstants.LINE_COLOR));
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
}