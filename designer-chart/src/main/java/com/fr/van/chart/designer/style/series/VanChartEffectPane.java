package com.fr.van.chart.designer.style.series;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Dimension;

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
            setContentPaneBorder();
        }
        this.add(content, BorderLayout.CENTER);
    }

    protected void setContentPaneBorder() {
        content.setBorder(BorderFactory.createEmptyBorder(10,25,0,15));
    }

    protected JPanel createContentPane() {
        JPanel panel = createPeriodPane();
        return panel;
    }

    protected JPanel createPeriodPane(){
        JPanel periodPane = new JPanel();
        periodPane.setLayout(new BorderLayout(5, 0));
        UILabel label1= new UILabel(Inter.getLocText("Plugin-ChartF_Flash_Period"));
        label1.setPreferredSize(new Dimension((int)TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH, 20));
        periodPane.add(label1, BorderLayout.WEST);
        periodPane.add(period, BorderLayout.CENTER);
        periodPane.add(new UILabel(Inter.getLocText("FR-Base-Time_Second")), BorderLayout.EAST);
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
