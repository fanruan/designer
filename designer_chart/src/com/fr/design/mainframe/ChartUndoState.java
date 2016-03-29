/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe;

import com.fr.form.ui.ChartBook;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: 下午2:32
 */
public class ChartUndoState extends BaseUndoState<JChart> {
    private ChartBook chartBook;
    private Dimension designerSize;
    private double widthValue;
   	private double heightValue;

    public ChartUndoState(JChart t,ChartArea area) {
        super(t);
        try {
            this.chartBook = (ChartBook) t.getTarget().clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        this.widthValue =area.getCustomWidth();
        this.heightValue = area.getCustomHeight();
    }


    public ChartBook getChartBook(){
        return this.chartBook;
    }

    /**
     * 应用状态
     */
    public void applyState() {
        this.getApplyTarget().applyUndoState(this);

    }
}