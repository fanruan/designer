package com.fr.van.chart.map.designer.data.contentpane.table;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.plugin.chart.map.data.VanMapTableDefinitionProvider;
import com.fr.van.chart.map.designer.data.component.table.AbstractLongLatAreaPane;
import com.fr.van.chart.map.designer.data.component.table.AreaPane;
import com.fr.van.chart.map.designer.data.component.table.PointMapAreaPane;
import com.fr.van.chart.map.designer.data.component.table.PointMapLongLatAreaPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Created by Mitisky on 16/5/17.
 */
public class VanPointMapPlotTableDataContentPane extends VanAreaMapPlotTableDataContentPane {
    private static final int LEFT_GAP = 19;
    private static final int V_GAP = 15;
    //改控件相当于面积图的区域名控件
    private LongLatAreaTableComboPane longLatTableComboPane;

    public VanPointMapPlotTableDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    protected void initAreaNameCom() {
        longLatTableComboPane = new LongLatAreaTableComboPane();
    }

    protected JPanel createAreaNamePane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, LEFT_GAP, V_GAP, 0));
        panel.add(longLatTableComboPane, BorderLayout.CENTER);
        return panel;
    }

    @Override
    protected void refreshAreaName(List list) {
        longLatTableComboPane.refreshBoxListWithSelectTableData(list);
    }

    @Override
    protected void checkAreaName(boolean hasUse) {
        longLatTableComboPane.checkBoxUse(hasUse);
    }

    protected boolean isAreaSelectedItem() {
        return longLatTableComboPane.isSelectedItem();
    }

    @Override
    protected void clearAreaName() {
        longLatTableComboPane.clearAllBoxList();
    }

    @Override
    protected void updateDefinition(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
        longLatTableComboPane.updateBean(mapTableDefinitionProvider);
    }

    @Override
    protected void populateDefinition(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
        longLatTableComboPane.populateBean(mapTableDefinitionProvider);
    }


    public class LongLatAreaTableComboPane extends BasicBeanPane<VanMapTableDefinitionProvider> {
        private UIButtonGroup<Integer> locationType;

        private JPanel centerPane;

        private AbstractLongLatAreaPane longLatAreaPane;

        private AbstractLongLatAreaPane areaNamePane;


        public LongLatAreaTableComboPane() {

            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;

            this.setLayout(new BorderLayout(0, 5));
            centerPane = new JPanel(new CardLayout()) {
                @Override
                public Dimension getPreferredSize() {
                    if (locationType.getSelectedIndex() == 0) {
                        return new Dimension(180, (int) areaNamePane.getPreferredSize().getHeight());
                    } else {
                        return new Dimension(180, (int) longLatAreaPane.getPreferredSize().getHeight());
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

            longLatAreaPane = createLongLatAreaPane(this);
            areaNamePane = createAreaPane(this);

            centerPane.add(areaNamePane, "area");
            centerPane.add(longLatAreaPane, "longLat");

            locationType.setSelectedIndex(0);

            double[] columnSize = {p, f};
            double[] rowSize = {p};
            Component[][] components = new Component[][]{
                    new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Geographic")), locationType},
            };

            JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 30, 6);

            this.add(panel, BorderLayout.NORTH);
            this.add(centerPane, BorderLayout.CENTER);

        }

        public void fireCheckSeriesUse(boolean hasUse) {
            checkSeriseUse(hasUse);
        }

        private void checkCenterPane() {
            CardLayout cardLayout = (CardLayout) centerPane.getLayout();
            if (locationType.getSelectedIndex() == 0) {
                cardLayout.show(centerPane, "area");
            } else {
                cardLayout.show(centerPane, "longLat");
            }
            fireCheckSeriesUse(true);
        }

        protected void refreshBoxListWithSelectTableData(List list) {
            areaNamePane.refreshBoxListWithSelectTableData(list);
            longLatAreaPane.refreshBoxListWithSelectTableData(list);
        }

        /**
         * 检查 某些Box是否可用
         *
         * @param hasUse 是否使用.
         */
        public void checkBoxUse(boolean hasUse) {
            areaNamePane.checkBoxUse(hasUse);
            longLatAreaPane.checkBoxUse(hasUse);
        }

        /**
         * 清空所有的box设置
         */
        public void clearAllBoxList() {
            areaNamePane.clearAllBoxList();
            longLatAreaPane.clearAllBoxList();
        }

        @Override
        public void populateBean(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
            locationType.setSelectedIndex(mapTableDefinitionProvider.isUseAreaName() ? 0 : 1);

            if (locationType.getSelectedIndex() == 0) {
                areaNamePane.populate(mapTableDefinitionProvider);
            } else {
                longLatAreaPane.populate(mapTableDefinitionProvider);
            }
            checkCenterPane();
        }

        public void updateBean(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
            boolean useAreaName = locationType.getSelectedIndex() == 0;
            mapTableDefinitionProvider.setUseAreaName(useAreaName);
            if (useAreaName) {
                areaNamePane.update(mapTableDefinitionProvider);
            } else {
                longLatAreaPane.update(mapTableDefinitionProvider);
            }

            checkCenterPane();
        }

        @Override
        public VanMapTableDefinitionProvider updateBean() {
            return null;
        }

        @Override
        protected String title4PopupWindow() {
            return "longAndLat";
        }

        public boolean isSelectedItem() {
            if (locationType.getSelectedIndex() == 0) {
                return areaNamePane.isSelectedItem();
            } else {
                return longLatAreaPane.isSelectedItem();
            }
        }
    }

    protected AbstractLongLatAreaPane createAreaPane(LongLatAreaTableComboPane longLatAreaTableComboPane) {
        return new PointMapAreaPane(longLatAreaTableComboPane) {
            protected void initAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
                areaPane = new AreaPane(parentPane) {
                    protected Component[][] getComponent () {
                        return new Component[][]{
                                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name")), VanPointMapPlotTableDataContentPane.this.createAreaPanel(areaNameCom)}
                        };
                    }
                };
            }
        };
    }

    protected AbstractLongLatAreaPane createLongLatAreaPane(LongLatAreaTableComboPane longLatAreaTableComboPane) {
        return new PointMapLongLatAreaPane(longLatAreaTableComboPane);
    }

}
