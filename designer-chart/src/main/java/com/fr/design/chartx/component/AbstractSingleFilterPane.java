package com.fr.design.chartx.component;

import com.fr.base.Utils;
import com.fr.chartx.data.field.DataFilterProperties;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.mainframe.chart.gui.data.PresentComboBox;
import com.fr.design.mainframe.chart.gui.style.AbstractChartTabPane;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Created by shine on 2019/07/18.
 */
public abstract class AbstractSingleFilterPane extends AbstractChartTabPane<DataFilterProperties> implements UIObserver {

    private static final int FIL_HEIGHT = 150;

    private UICheckBox useTopCheckBox;

    private UITextField topNumTextField;
    private UICheckBox hideNullCheckBox;
    private UICheckBox mergeOtherCheckBox;

    private PresentComboBox present;

    private JPanel topPane;

    private UIObserverListener listener;

    public AbstractSingleFilterPane() {
        super(true);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.height = FIL_HEIGHT;
        return dim;
    }


    protected void layoutContentPane() {
        super.layoutContentPane();
        leftcontentPane.setBorder(BorderFactory.createEmptyBorder());
    }

    public void reloaPane(JPanel pane) {
        super.reloaPane(pane);
        leftcontentPane.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    protected JPanel createContentPane() {
        this.setLayout(new BorderLayout());
        JPanel pane = initPane();
        this.add(pane, BorderLayout.NORTH);
        return pane;
    }


    private JPanel initPane() {
        useTopCheckBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Chart_Only_Use_Before_Records"));
        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new BorderLayout());
        panel1.add(useTopCheckBox, BorderLayout.NORTH);
        topNumTextField = new UITextField("0");
        UILabel label = new UILabel(Toolkit.i18nText("Fine-Design_Chart_Records_Num"));
        mergeOtherCheckBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Chart_Data_CombineOther"));
        mergeOtherCheckBox.setSelected(true);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{label, topNumTextField},
                new Component[]{mergeOtherCheckBox, null}
        };

        topPane = TableLayout4VanChartHelper.createGapTableLayoutPane(components, rowSize, columnSize);
        topPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 0));
        //默认不显示
        topPane.setVisible(false);
        panel1.add(topPane, BorderLayout.CENTER);
        hideNullCheckBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Chart_Null_Hide", title4PopupWindow(), title4PopupWindow()));
        panel2.add(hideNullCheckBox, BorderLayout.NORTH);

        useTopCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                checkBoxUse();
            }
        });

        present = new PresentComboBox() {
            protected void fireChange() {
                fire();
            }
        };
        JPanel presentPane = new JPanel(new BorderLayout(30, 0));
        presentPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Present")), BorderLayout.WEST);
        presentPane.add(present,BorderLayout.CENTER);
        presentPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panel2.add(presentPane, BorderLayout.SOUTH);

        double[] column = {f};
        double[] row = {p, p};
        Component[][] coms = new Component[][]{
                new Component[]{panel1},
                new Component[]{panel2}
        };
        return TableLayout4VanChartHelper.createGapTableLayoutPane(coms, row, column);
    }

    @Override
    public void registerChangeListener(UIObserverListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }

    private void fire() {
        if (this.listener != null) {
            listener.doChange();
        }
    }

    /**
     * 检查Box是否可用
     */
    public void checkBoxUse() {
        topPane.setVisible(useTopCheckBox.isSelected());
    }

    @Override
    public void populateBean(DataFilterProperties ob) {
        useTopCheckBox.setSelected(ob.isUseTop());

        topNumTextField.setText(String.valueOf(ob.getTop()));

        hideNullCheckBox.setSelected(ob.isHideNull());

        mergeOtherCheckBox.setSelected(ob.isMerge());

        present.populate(ob.getPresent());

        checkBoxUse();
    }

    @Override
    public DataFilterProperties updateBean() {
        DataFilterProperties dataFilterProperties = new DataFilterProperties();

        dataFilterProperties.setUseTop(useTopCheckBox.isSelected());

        Number number = Utils.objectToNumber(topNumTextField.getText(), true);
        if (number != null) {
            dataFilterProperties.setTop(number.intValue());
        }
        dataFilterProperties.setHideNull(hideNullCheckBox.isSelected());

        dataFilterProperties.setMerge(mergeOtherCheckBox.isSelected());

        dataFilterProperties.setPresent(present.update());

        return dataFilterProperties;
    }

}
