package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.Utils;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.design.gui.columnrow.ColumnRowVerticalPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.hyperlink.AbstractHyperLinkPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.ColumnRow;
import com.fr.stable.ParameterProvider;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-12-28 下午02:51:10
 *          类说明: 图表超链 -- 关联单元格图表.
 */
public class ChartHyperRelateCellLinkPane extends AbstractHyperLinkPane<ChartHyperRelateCellLink> {
    private static final long serialVersionUID = 7874948047886548990L;

    private UITextField itemNameTextField;
    private ColumnRowVerticalPane colRowPane;
    private ReportletParameterViewPane parameterViewPane;

    public ChartHyperRelateCellLinkPane() {
        this.initComponent();
    }

    public ChartHyperRelateCellLinkPane(HashMap hyperLinkEditorMap, boolean needRenamePane) {
        super(hyperLinkEditorMap, needRenamePane);
        this.initComponent();
    }

    private void initComponent() {
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        if (needRenamePane()) {
            itemNameTextField = new UITextField();
            this.add(GUICoreUtils.createNamedPane(itemNameTextField, Inter.getLocText("FR-Designer_Name") + ":"), BorderLayout.NORTH);
        }

        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(centerPane, BorderLayout.CENTER);

        centerPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[]{"Related", "FR-Designer_Cell"})));

        colRowPane = new ColumnRowVerticalPane();
        centerPane.add(colRowPane, BorderLayout.NORTH);

        parameterViewPane = new ReportletParameterViewPane(getChartParaType(), getValueEditorPane(), getValueEditorPane());
        parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Parameter")));
        parameterViewPane.setPreferredSize(new Dimension(500, 200));
        this.add(parameterViewPane, BorderLayout.SOUTH);
    }

    @Override
    public void populateBean(ChartHyperRelateCellLink ob) {
        if (ob == null) {
            return;
        }
        if (itemNameTextField != null) {
            itemNameTextField.setText(ob.getItemName());
        }
        if (ob.getRelateCCName() != null) {
            ColumnRow colRow = ColumnRow.valueOf(ob.getRelateCCName());
            colRowPane.populate(colRow);
        } else {
            colRowPane.populate(ColumnRow.valueOf("A1"));
        }

        List parameterList = this.parameterViewPane.update();
        parameterList.clear();

        ParameterProvider[] parameters = ob.getParameters();
        parameterViewPane.populate(parameters);
    }

    @Override
    public ChartHyperRelateCellLink updateBean() {
        ChartHyperRelateCellLink chartLink = new ChartHyperRelateCellLink();
        updateBean(chartLink);
        if (itemNameTextField != null) {
            chartLink.setItemName(this.itemNameTextField.getText());
        }
        return chartLink;
    }

    /**
     * 属性表 对应update
     */
    public void updateBean(ChartHyperRelateCellLink chartLink) {
        chartLink.setRelateCCName(Utils.objectToString(colRowPane.update()));

        List parameterList = this.parameterViewPane.update();
        if (parameterList != null && !parameterList.isEmpty()) {
            ParameterProvider[] parameters = new ParameterProvider[parameterList.size()];
            parameterList.toArray(parameters);

            chartLink.setParameters(parameters);
        } else {
            chartLink.setParameters(null);
        }
        if (itemNameTextField != null) {
            chartLink.setItemName(this.itemNameTextField.getText());
        }
    }

    @Override
    public String title4PopupWindow() {
        return Inter.getLocText(new String[]{"Related", "Cell"});
    }

    public static class ChartNoRename extends ChartHyperRelateCellLinkPane {
        protected boolean needRenamePane() {
            return false;
        }
    }

}