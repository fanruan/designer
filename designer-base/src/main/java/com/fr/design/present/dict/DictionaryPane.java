package com.fr.design.present.dict;

import com.fr.data.Dictionary;
import com.fr.data.impl.DynamicSQLDict;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.data.tabledata.Prepare4DataSourceChange;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhou
 * @since 2012-5-31下午12:20:41
 */
public class DictionaryPane extends UIComboBoxPane<Dictionary> implements DataCreatorUI, Prepare4DataSourceChange {
    private TableDataDictPane tableDataDictPane;

    @Override
    protected void initLayout() {
        this.setLayout(new BorderLayout(0, 4));
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p};
        int[][] rowCount = {{1, 1}, {1, 1}};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Type_Set"), UILabel.LEFT), jcb},
                new Component[]{null, null}
        };
        JPanel northPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_HUGER, LayoutConstants.VGAP_MEDIUM);
        this.add(northPane, BorderLayout.NORTH);
        this.add(cardPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_DS_Dictionary");
    }

    @Override
    public JComponent toSwingComponent() {
        return this;
    }

    @Override
    public void populateBean(Dictionary ob) {
        for (int i = 0; i < this.cards.size(); i++) {
            FurtherBasicBeanPane pane = cards.get(i);
            if (pane.accept(ob)) {
                pane.populateBean(ob);
                jcb.setSelectedIndex(i);
            } else {
                pane.reset();
            }
        }
        if (ob instanceof DynamicSQLDict) {
            jcb.setSelectedIndex(1);
            tableDataDictPane.populateBean((DynamicSQLDict) ob);
        }
    }

    @Override
    protected List<FurtherBasicBeanPane<? extends Dictionary>> initPaneList() {
        List<FurtherBasicBeanPane<? extends Dictionary>> paneList = new ArrayList<FurtherBasicBeanPane<? extends Dictionary>>();
        paneList.add(new DatabaseDictPane());
        paneList.add(tableDataDictPane = new TableDataDictPane());
        paneList.add(new CustomDictPane());
        paneList.add(new FormulaDictPane());
        return paneList;
    }

    @Override
    public void registerDSChangeListener() {
        tableDataDictPane.registerDSChangeListener();
    }
}
