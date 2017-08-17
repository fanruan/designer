package com.fr.plugin.chart.designer.style.series;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by shine on 2016/12/13.
 */
public class VanChartEffectPane extends BasicBeanPane<AttrEffect> {
    protected UIButtonGroup enabledButton;
    private JPanel content;
    protected UISpinner period;

    public VanChartEffectPane() {
        this(true);
    }

    public VanChartEffectPane(boolean hasEnabledChoose){
        enabledButton = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});

        enabledButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkoutPaneVisible(enabledButton.getSelectedIndex() == 0);
            }
        });

        period = new UISpinner(0, Double.MAX_VALUE, 0.1, 0);
        content = createContentPane();

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_Flash_Animation"),enabledButton);
        this.setLayout(new BorderLayout(0, 5));
        if(hasEnabledChoose) {
            this.add(panel, BorderLayout.NORTH);
            content.setBorder(BorderFactory.createEmptyBorder(10,25,0,15));
        }
        this.add(content, BorderLayout.CENTER);
    }

    protected JPanel createContentPane() {
        JPanel panel = createPeriodPane();
        return panel;
    }

    protected JPanel createPeriodPane(){
        JPanel periodPane = new JPanel();
        periodPane.setLayout(new BorderLayout(5, 0));
        periodPane.add(new UILabel(Inter.getLocText("Plugin-ChartF_Flash_Period")), BorderLayout.WEST);
        periodPane.add(period, BorderLayout.CENTER);
        periodPane.add(new UILabel("s"), BorderLayout.EAST);
        return periodPane;
    }

    @Override
    public void populateBean(AttrEffect ob) {
        enabledButton.setSelectedIndex(ob.isEnabled() ? 0 : 1);
        period.setValue(ob.getPeriod());
        checkoutPaneVisible(enabledButton.getSelectedIndex() == 0);
    }

    @Override
    public AttrEffect updateBean() {
        AttrEffect attrEffect = new AttrEffect();
        attrEffect.setEnabled(enabledButton.getSelectedIndex() == 0);
        attrEffect.setPeriod(period.getValue());
        return attrEffect;
    }

    protected void checkoutPaneVisible(boolean isVisible) {
        content.setVisible(isVisible);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_Flash_Animation");
    }
}
