/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.base.ExcelUtils;
import com.fr.design.mainframe.JChart;
import com.fr.design.mainframe.exporter.ExcelExporter4Chart;
import com.fr.design.mainframe.exporter.Exporter4Chart;
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
 * Time: 上午9:52
 */
public class ExcelExportAction4Chart extends AbstractExportAction4JChart {

    private MenuKeySet excel= new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'E';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Chart-Format_Excel");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK);
        }
    };

    public ExcelExportAction4Chart(JChart chart) {
        super(chart);
        this.setMenuKeySet(excel);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/excel.png"));
    }


    @Override
	protected ChooseFileFilter getChooseFileFilter() {
    	return new ChooseFileFilter(new String[]{"xls", "xlsx"}, Inter.getLocText("Export-Excel"));
    }

    @Override
	protected String getDefaultExtension() {
    	return ExcelUtils.checkPOIJarExist() ? "xlsx" : "xls";
    }

    @Override
    protected Exporter4Chart getExporter() {
        return new ExcelExporter4Chart();
    }
}