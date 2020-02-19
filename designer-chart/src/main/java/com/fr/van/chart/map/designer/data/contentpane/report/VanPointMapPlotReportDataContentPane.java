package com.fr.van.chart.map.designer.data.contentpane.report;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.plugin.chart.map.data.VanMapReportDefinition;
import com.fr.van.chart.map.designer.data.component.report.AbstractLongLatAreaPane;
import com.fr.van.chart.map.designer.data.component.report.PointMapAreaPane;
import com.fr.van.chart.map.designer.data.component.report.PointMapLongLatAreaPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Created by Mitisky on 16/5/17.
 */
public class VanPointMapPlotReportDataContentPane extends VanAreaMapPlotReportDataContentPane {
    private LongLatReportFormulaPane longLatReportFormulaPane;

    public VanPointMapPlotReportDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    @Override
    protected JPanel getContent() {
        longLatReportFormulaPane = new LongLatReportFormulaPane();
        JPanel content = new JPanel(new BorderLayout(0, 4));
        content.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
        content.add(longLatReportFormulaPane, BorderLayout.CENTER);
        return content;
    }

    @Override
    protected void populateDefinition(VanMapReportDefinition mapReportDefinition) {
        super.populateDefinition(mapReportDefinition);
        longLatReportFormulaPane.populateBean(mapReportDefinition);
    }

    @Override
    protected void updateDefinition(VanMapReportDefinition mapReportDefinition) {
        super.updateDefinition(mapReportDefinition);
        longLatReportFormulaPane.updateBean(mapReportDefinition);
    }

    //================================public class===================================

    public class LongLatReportFormulaPane extends BasicBeanPane<VanMapReportDefinition> {

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        private UIButtonGroup<Integer> locationType;

        private JPanel centerPane;
        private AbstractLongLatAreaPane areaPane;
        private AbstractLongLatAreaPane longLatAreaPane;

        public LongLatReportFormulaPane() {
            this.setLayout(new BorderLayout(0, 5));
            centerPane = new JPanel(new CardLayout()) {
                @Override
                public Dimension getPreferredSize() {
                    if (locationType.getSelectedIndex() == 0) {
                        return areaPane.getPreferredSize();
                    } else {
                        return longLatAreaPane.getPreferredSize();
                    }
                }
            };

            locationType = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Location_With_Area_Name"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Location_With_LongAndLat")});
            locationType.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    checkCenterPane();
                }
            });

            longLatAreaPane = getLongLatAreaPane();
            areaPane = getAreaPane();

            centerPane.add(areaPane, "area");
            centerPane.add(longLatAreaPane, "longLat");

            locationType.setSelectedIndex(0);

            double[] columnSize = {p, f};
            double[] rowSize = {p};

            Component[][] components = new Component[][]{
                    new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Geographic")), locationType},
            };

            JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 12, 6);


            this.add(panel, BorderLayout.NORTH);
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

        public void populateBean(VanMapReportDefinition mapReportDefinition) {
            locationType.setSelectedIndex(mapReportDefinition.isUseAreaName() ? 0 : 1);
            if (locationType.getSelectedIndex() == 0) {
                areaPane.populate(mapReportDefinition);

            } else {
                longLatAreaPane.populate(mapReportDefinition);
            }

            checkCenterPane();

        }

        public void updateBean(VanMapReportDefinition mapReportDefinition) {
            boolean useAreaName = locationType.getSelectedIndex() == 0;
            mapReportDefinition.setUseAreaName(useAreaName);
            if (useAreaName) {
                areaPane.update(mapReportDefinition);
            } else {
                longLatAreaPane.update(mapReportDefinition);
            }

            checkCenterPane();
        }

        /**
         * Update.
         */
        @Override
        public VanMapReportDefinition updateBean() {
            return null;
        }

        @Override
        protected String title4PopupWindow() {
            return null;
        }


    }

    protected AbstractLongLatAreaPane getAreaPane() {
        return new PointMapAreaPane();
    }

    protected AbstractLongLatAreaPane getLongLatAreaPane() {
        return new PointMapLongLatAreaPane();
    }

}
