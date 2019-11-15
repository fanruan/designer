package com.fr.design.chartx.data.map;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/13
 */
public abstract class AbstractAreaLngLatPane extends JPanel {
    private JPanel centerPane;
    private UIButtonGroup<Integer> locationType;

    private JPanel areaPane;
    private JPanel lngLatAreaPane;

    protected abstract JPanel createAreaPane();

    protected abstract JPanel createAreaLngLatPane();

    public AbstractAreaLngLatPane() {
        centerPane = new JPanel(new CardLayout()) {
            @Override
            public Dimension getPreferredSize() {
                if (locationType.getSelectedIndex() == 0) {
                    return areaPane.getPreferredSize();
                } else {
                    return lngLatAreaPane.getPreferredSize();
                }
            }
        };

        locationType = new UIButtonGroup<Integer>(new String[]{Toolkit.i18nText("Fine-Design_Chart_Location_With_Area_Name"),
                Toolkit.i18nText("Fine-Design_Chart_Location_With_LongAndLat")});
        locationType.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkCenterPane();
            }
        });

        lngLatAreaPane = createAreaLngLatPane();
        areaPane = createAreaPane();

        centerPane.add(areaPane, "area");
        centerPane.add(lngLatAreaPane, "longLat");

        locationType.setSelectedIndex(0);

        this.setLayout(new BorderLayout(0, 6));
        this.add(locationType, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
    }

    private void checkCenterPane() {
        CardLayout cardLayout = (CardLayout) centerPane.getLayout();
        if (locationType.getSelectedIndex() == 0) {
            cardLayout.show(centerPane, "area");
        } else {
            cardLayout.show(centerPane, "longLat");
        }
    }

    public void populate(boolean useAreaName) {
        locationType.setSelectedIndex(useAreaName ? 0 : 1);

        checkCenterPane();
    }

    public boolean update() {
        return locationType.getSelectedIndex() == 0;
    }

    protected JPanel createPane(String[] labels, JComponent... fieldComponents) {

        int len = Math.min(labels.length, fieldComponents.length);

        if (len == 0) {
            return new JPanel();
        }

        Component[][] components = new Component[len][2];
        for (int i = 0; i < len; i++) {
            components[i] = new Component[]{new UILabel(labels[i], SwingConstants.LEFT), fieldComponents[i]};
        }
        double p = TableLayout.PREFERRED;
        double[] columnSize = {ChartDataPane.LABEL_WIDTH, 122};
        double[] rowSize = new double[len];
        Arrays.fill(rowSize, p);

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 6);
    }

}

