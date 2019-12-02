package com.fr.design.chartx.single;

import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.utils.gui.UIComponentUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;

/**
 * Created by shine on 2019/5/21.
 */
public class SingleDataPane extends BasicBeanPane<AbstractDataDefinition> {

    private static final int  TABLE_DATA_LABEL_LINE_WIDTH = 81;

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

                UILabel label = new BoldFontTextLabel(Toolkit.i18nText("Fine-Design_Chart_Data_Source"));
                UIComponentUtils.setPreferedWidth(label, TABLE_DATA_LABEL_LINE_WIDTH);
                northPane.add(label,BorderLayout.WEST);
                northPane.add(jcb, BorderLayout.CENTER);

                northPane.setBorder(BorderFactory.createEmptyBorder(5,24,0,15));
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

    public void setSelectedIndex(int index) {
        comboBoxPane.setSelectedIndex(index);
    }
}
