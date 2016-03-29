/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JChart;
import com.fr.design.mainframe.exporter.Exporter4Chart;
import com.fr.design.mainframe.exporter.PdfExporter4Chart;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: 上午10:16
 */
public class PDFExportAction4Chart extends AbstractExportAction4JChart {

    private MenuKeySet pdf = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'P';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Chart-Format_PDF");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    public PDFExportAction4Chart(JChart chart) {
        super(chart);
        this.setMenuKeySet(pdf);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/pdf.png"));
    }

    @Override
	protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"pdf"}, Inter.getLocText("Export-PDF"));
    }

    @Override
	protected String getDefaultExtension() {
        return "pdf";
    }

    @Override
    protected Exporter4Chart getExporter() {
        return new PdfExporter4Chart();
    }
}