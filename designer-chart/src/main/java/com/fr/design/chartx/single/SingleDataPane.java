package com.fr.design.chartx.single;

import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shine on 2019/5/21.
 */
public class SingleDataPane extends BasicBeanPane<AbstractDataDefinition> {

    private UIComboBoxPane<AbstractDataDefinition> comboBoxPane;

    private DataSetPane dataSetPane;

    private CellDataPane cellDataPane;

    public SingleDataPane(AbstractDataSetFieldsPane dataSetFieldsPane, AbstractCellDataFieldsPane cellDataFieldsPane) {
        initComps(dataSetFieldsPane, cellDataFieldsPane);
    }

    private void initComps(AbstractDataSetFieldsPane dataSetFieldsPane, AbstractCellDataFieldsPane cellDataFieldsPane) {

        cellDataPane = new CellDataPane(cellDataFieldsPane);
        dataSetPane = new DataSetPane(dataSetFieldsPane);

        comboBoxPane = new UIComboBoxPane<AbstractDataDefinition>() {
            @Override
            protected List<FurtherBasicBeanPane<? extends AbstractDataDefinition>> initPaneList() {
                List<FurtherBasicBeanPane<? extends AbstractDataDefinition>> list = new ArrayList<FurtherBasicBeanPane<? extends AbstractDataDefinition>>();
                list.add(dataSetPane);
                list.add(cellDataPane);
                return list;
            }

            protected void initLayout() {
                this.setLayout(new BorderLayout(LayoutConstants.HGAP_LARGE, 6));
                JPanel northPane = new JPanel(new BorderLayout(LayoutConstants.HGAP_LARGE, 0));
                northPane.add(jcb, BorderLayout.CENTER);
                UILabel label1 = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Source"));
                label1.setPreferredSize(new Dimension(ChartDataPane.LABEL_WIDTH, ChartDataPane.LABEL_HEIGHT));
                northPane.add(GUICoreUtils.createBorderLayoutPane(new Component[]{jcb, null, null, label1, null}));
                northPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 8));
                this.add(northPane, BorderLayout.NORTH);
                this.add(cardPane, BorderLayout.CENTER);

            }

            @Override
            protected String title4PopupWindow() {
                return null;
            }
        };


        this.setLayout(new BorderLayout());
        this.add(comboBoxPane, BorderLayout.CENTER);

    }

    @Override
    public void populateBean(AbstractDataDefinition ob) {
        comboBoxPane.populateBean(ob);
    }

    @Override
    public AbstractDataDefinition updateBean() {
        return comboBoxPane.updateBean();
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
