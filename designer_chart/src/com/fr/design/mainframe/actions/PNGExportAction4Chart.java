/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JChart;
import com.fr.design.mainframe.exporter.Exporter4Chart;
import com.fr.design.mainframe.exporter.ImageExporter4Chart;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: 上午10:18
 */
public class PNGExportAction4Chart extends AbstractExportAction4JChart {

    private MenuKeySet menuSet  = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'M';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Chart-Format_Image");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK);
        }
    };

    public PNGExportAction4Chart(JChart chart) {
        super(chart);
        this.setMenuKeySet(menuSet);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/exportimg.png"));
    }

    @Override
    protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"png"}, Inter.getLocText("Image"));
    }

    @Override
    protected String getDefaultExtension() {
        return "png";
    }

    @Override
    protected Exporter4Chart getExporter() {
        return new ImageExporter4Chart();
    }
}