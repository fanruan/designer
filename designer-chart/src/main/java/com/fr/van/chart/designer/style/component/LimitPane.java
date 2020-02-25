package com.fr.van.chart.designer.style.component;

import com.fr.chartx.attr.LimitAttribute;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.i18n.Toolkit;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by shine on 2019/08/28.
 */
public class LimitPane extends BasicBeanPane<LimitAttribute> {

    private UIButtonGroup<Boolean> autoCustomTypeGroup;
    private UISpinner maxProportion;
    private JPanel maxProportionPane;

    public LimitPane() {
        this(true);
    }

    public LimitPane(boolean hasTitle) {
        initComponent(hasTitle);
    }

    private void initComponent(boolean hasTitle) {
        maxProportion = new UISpinner(0, 100, 1, 30);
        autoCustomTypeGroup = new UIButtonGroup<Boolean>(new String[]{Toolkit.i18nText("Fine-Design_Chart_Mode_Auto")
                , Toolkit.i18nText("Fine-Design_Chart_Mode_Custom")}, new Boolean[]{true, false});

        JPanel limitSizePane = TableLayout4VanChartHelper.createGapTableLayoutPane(Toolkit.i18nText("Fine-Design_Chart_Area_Size"), autoCustomTypeGroup);
        maxProportionPane = TableLayout4VanChartHelper.createGapTableLayoutPane(Toolkit.i18nText("Fine-Design_Chart_Max_Proportion"), maxProportion, TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH);
        maxProportionPane.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(limitSizePane, BorderLayout.NORTH);
        panel.add(maxProportionPane, BorderLayout.CENTER);

        autoCustomTypeGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkMaxProPortionUse();
            }
        });

        this.setLayout(new BorderLayout());
        if (hasTitle) {
            JPanel contentPane = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Display_Strategy"), panel);
            this.add(contentPane);
        } else {
            this.add(panel);
        }
    }

    //检查最大显示占比是否可用
    public void checkMaxProPortionUse() {
        maxProportion.setVisible(!autoCustomTypeGroup.getSelectedItem() && autoCustomTypeGroup.isEnabled());
        maxProportionPane.setVisible(!autoCustomTypeGroup.getSelectedItem() && autoCustomTypeGroup.isEnabled());
    }

    @Override
    public void populateBean(LimitAttribute ob) {
        autoCustomTypeGroup.setSelectedItem(ob.isAuto());
        maxProportion.setValue(ob.getMaxSize());
    }

    @Override
    public LimitAttribute updateBean() {
        LimitAttribute attribute = new LimitAttribute();
        attribute.setAuto(autoCustomTypeGroup.getSelectedItem());
        attribute.setMaxSize(maxProportion.getValue());
        return attribute;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
