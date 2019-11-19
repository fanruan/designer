package com.fr.design.widget.ui.designer.layout;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.properties.items.FRAbsoluteConstraintsItems;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.form.ui.container.WAbsoluteLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by ibm on 2017/8/2.
 */
public class FRAbsoluteLayoutDefinePane extends AbstractFRLayoutDefinePane<WAbsoluteLayout> {
    protected UIComboBox comboBox;

    public FRAbsoluteLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }


    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        initUIComboBox();
        JPanel thirdPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jPanel = createThirdPane();
        jPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        thirdPane.add(jPanel, BorderLayout.CENTER);
        UIExpandablePane layoutExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Area_Scaling"), 280, 20, thirdPane);
        this.add(layoutExpandablePane, BorderLayout.CENTER);
    }

    public JPanel createThirdPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Scaling_Mode")), comboBox},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        return panel;
    }


    public void initUIComboBox() {
        Item[] items = FRAbsoluteConstraintsItems.ITEMS;
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Item item : items) {
            model.addElement(item);
        }
        comboBox = new UIComboBox(model);
    }

    @Override
    public String title4PopupWindow() {
        return "absoluteLayout";
    }

    @Override
    public void populateBean(WAbsoluteLayout ob) {
        populateSubPane(ob);
        comboBox.setSelectedIndex(ob.getCompState());
    }


    @Override
    public WAbsoluteLayout updateBean() {
        WAbsoluteLayout wAbsoluteLayout = updateSubPane();
        wAbsoluteLayout.setCompState(comboBox.getSelectedIndex());
        return wAbsoluteLayout;

    }

    public WAbsoluteLayout updateSubPane() {
        return (WAbsoluteLayout)creator.toData();
    }

    public void populateSubPane(WAbsoluteLayout ob) {

    }

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }

}

