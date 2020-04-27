/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.data.datapane;

import com.fr.data.TableDataSource;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.layout.FRGUIPaneFactory;


import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @author richer
 * @since 6.5.5
 * 创建于2011-6-14
 */
public class ReportTableDataPane extends LoadingBasicPane {
    private TableDataPaneController tdPane;

    @Override
    protected void initComponents(JPanel container) {
        container.setLayout(FRGUIPaneFactory.createBorderLayout());
        tdPane = new TableDataPaneListPane() {
            @Override
            public NameableCreator[] createNameableCreators() {

                return TableDataCreatorProducer.getInstance().createReportTableDataCreator();
            }
        };
        container.add(tdPane.getPanel(), BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Report_TableData");
    }

    public void populate(TableDataSource tds) {
        tdPane.populate(tds);
    }

    public void update(TableDataSource tds) {
        tdPane.update(tds);
    }

    /**
     * 检查tdListPane有效性check Valid
     *
     * @throws Exception 异常
     */
    public void checkValid() throws Exception {
        this.tdPane.checkValid();
    }

    public Map<String, String> getDsNameChangedMap() {
        return tdPane.getDsNameChangedMap();
    }

    public boolean isNamePermitted() {
        return tdPane.isNamePermitted();
    }
}