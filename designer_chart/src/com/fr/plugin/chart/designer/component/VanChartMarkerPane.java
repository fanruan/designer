package com.fr.plugin.chart.designer.component;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.marker.VanChartCommonMarkerPane;
import com.fr.plugin.chart.designer.component.marker.VanChartImageMarkerPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * 标记点设置界面
 */
public class VanChartMarkerPane extends BasicPane {
    private static final long serialVersionUID = 7206339620703021514L;
    private UIButtonGroup<String> commonORCustom;
    private JPanel centerPane;
    private CardLayout cardLayout;

    private BasicBeanPane commonMarkerPane;
    
    private BasicBeanPane imageMarkerPane;

    public VanChartMarkerPane() {
        this.setLayout(new BorderLayout(0, 4));

        String[] array = new String[]{Inter.getLocText("Plugin-ChartF_Rule"), Inter.getLocText("Plugin-ChartF_Custom")};
        commonORCustom = new UIButtonGroup<String>(array, array);

        commonORCustom.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkCenterPane();
            }
        });

        commonMarkerPane = createCommonMarkerPane();
        imageMarkerPane = createImageMarkerPane();

        cardLayout = new CardLayout();
        centerPane = new JPanel(cardLayout) {

            @Override
            public Dimension getPreferredSize() {
                if(commonORCustom.getSelectedIndex() == 0){
                    return commonMarkerPane.getPreferredSize();
                } else {
                    return imageMarkerPane.getPreferredSize();
                }
            }
        };
        centerPane.add(commonMarkerPane, Inter.getLocText("Plugin-ChartF_Rule"));
        centerPane.add(imageMarkerPane, Inter.getLocText("Plugin-ChartF_Custom"));

        layoutComponents();
    }

    public void checkLargePlot(boolean large){
        if(large){
            commonORCustom.setSelectedIndex(0);
            checkCenterPane();
        }
        commonORCustom.setEnabled(!large);
    }

    protected BasicBeanPane<VanChartAttrMarker> createImageMarkerPane() {
        return new VanChartImageMarkerPane();
    }

    protected BasicBeanPane<VanChartAttrMarker> createCommonMarkerPane() {
        return new VanChartCommonMarkerPane(){
            protected double[] getcolumnSize () {
                double s = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
                double d = TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH;
                return new double[] {d, s};
            }
        };
    }

    protected void layoutComponents() {
        this.add(TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_Point_Style"), commonORCustom), BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
    }

    protected void layoutComponentsWithOutNorth() {
        this.add(centerPane, BorderLayout.CENTER);
    }

    private void checkCenterPane() {
        cardLayout.show(centerPane, commonORCustom.getSelectedItem());
    }

    protected String title4PopupWindow(){
        return Inter.getLocText("Plugin-ChartF_Marker");
    }

    public void populate(VanChartAttrMarker marker) {
        if(marker == null){
            marker = new VanChartAttrMarker();
        }
        commonORCustom.setSelectedIndex(marker.isCommon() ? 0 : 1);
        if(marker.isCommon()){
            commonMarkerPane.populateBean(marker);
        } else {
            imageMarkerPane.populateBean(marker);
        }

        checkCenterPane();
    }

    public VanChartAttrMarker update() {
        VanChartAttrMarker marker = new VanChartAttrMarker();
        if(commonORCustom.getSelectedIndex() == 0){
            commonMarkerPane.updateBean(marker);
        } else {
            imageMarkerPane.updateBean(marker);
        }
        return marker;
    }

}