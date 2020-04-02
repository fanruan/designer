package com.fr.van.chart.map.designer;

import com.fr.design.gui.ibutton.UIButtonGroup;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

/**
 * Created by hufan on 2016/12/19.
 */
public abstract class VanChartGroupPane extends JPanel{

    public VanChartGroupPane(final String[] nameList, final JPanel[] paneList) {
        final UIButtonGroup<Integer> buttonGroup = new UIButtonGroup<Integer>(nameList);
        buttonGroup.setSelectedIndex(0);

        final CardLayout cardLayout = new CardLayout();
        final JPanel centerPane = new JPanel(cardLayout){
            @Override
            public Dimension getPreferredSize() {
                return paneList[buttonGroup.getSelectedIndex()].getPreferredSize();
            }
        };

        for (int i = 0; i < paneList.length && i < nameList.length; i++){
            centerPane.add(paneList[i], nameList[i]);
        }

        buttonGroup.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cardLayout.show(centerPane, nameList[buttonGroup.getSelectedIndex()]);
                tabChanged(buttonGroup.getSelectedIndex());
            }
        });
        buttonGroup.setBorder(getButtonGroupBorder());
        this.setLayout(new BorderLayout(0, 4));
        this.add(buttonGroup, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
    }

    protected void tabChanged(int index) {
    }

    protected Border getButtonGroupBorder () {
        return null;
    }
}
