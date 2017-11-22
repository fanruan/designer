package com.fr.plugin.chart.structure.desinger.style;

import com.fr.base.background.ImageBackground;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundQuickPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrNode;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.background.VanChartMarkerBackgroundPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by shine on 2017/2/15.
 */
public class StructureNodeStylePane extends BasicBeanPane<AttrNode> {

    private UIButtonGroup<Integer> nodeRadiusType;
    private UISpinner nodeRadius;
    private UIButtonGroup<Integer> useImage;
    private ImageBackgroundQuickPane imagePane;
    private UISpinner nodeBorderWidth;
    private VanChartMarkerBackgroundPane nodeBorderColor;
    private UINumberDragPane nodeOpacity;
    private JPanel nodeRadiusPane;

    public StructureNodeStylePane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p, p, p, p, p};

        nodeRadiusType = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Automatic"),
                Inter.getLocText("Plugin-ChartF_Custom")});
        nodeRadius = new UISpinner(0,Double.MAX_VALUE,0.5,0);
        nodeBorderWidth = new UISpinner(0,Double.MAX_VALUE,0.5,0);
        nodeBorderColor = new VanChartMarkerBackgroundPane();
        nodeOpacity = new UINumberDragPane(0,100);

        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_Node_Radius"), nodeRadiusType), BorderLayout.NORTH);
        nodeRadiusPane = TableLayout4VanChartHelper.createGapTableLayoutPane("", nodeRadius);
        jPanel.add(nodeRadiusPane, BorderLayout.CENTER);

        Component[][] components1 = new Component[][]{
                new Component[]{null, null},
                new Component[]{jPanel, null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Border_Width")), nodeBorderWidth},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Border_Color")), nodeBorderColor},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Alpha")), nodeOpacity}

        };
        JPanel panel1 = TableLayout4VanChartHelper.createGapTableLayoutPane(components1, rowSize, columnSize);

        useImage = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_YES"),
                Inter.getLocText("Plugin-ChartF_NO")});
        imagePane = new ImageBackgroundQuickPane(false);
        imagePane.setBorder(BorderFactory.createEmptyBorder(0,(int)TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH + TableLayout4VanChartHelper.COMPONENT_INTERVAL,0,0));
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Filled_With_Image")),useImage},
        };
        JPanel useImagePane = TableLayoutHelper.createTableLayoutPane(components, new double[]{p}, columnSize);

        JPanel panel2 = new JPanel(new BorderLayout(0,6));
        panel2.add(useImagePane, BorderLayout.NORTH);
        panel2.add(imagePane, BorderLayout.CENTER);

        useImage.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkImagePane();
            }
        });

        nodeRadiusType.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkRadius();
            }
        });

        this.setLayout(new BorderLayout(0,6));
        this.add(panel1, BorderLayout.NORTH);
        this.add(panel2, BorderLayout.CENTER);
    }

    private void checkRadius() {
        nodeRadiusPane.setVisible(nodeRadiusType.getSelectedIndex() == 1);
    }

    private void checkImagePane() {
        imagePane.setVisible(useImage.getSelectedIndex() == 0);
    }

    @Override
    public void populateBean(AttrNode ob) {
        nodeRadiusType.setSelectedIndex(ob.isAutoRadius() ? 0 : 1);
        nodeRadius.setValue(ob.getRadius());
        nodeBorderWidth.setValue(ob.getBorderWidth());
        nodeBorderColor.populate(ob.getBorderColor());
        nodeOpacity.populateBean(ob.getOpacity());
        useImage.setSelectedIndex(ob.isUseImage() ? 0 : 1);
        if(ob.getImageBackground() != null) {
            imagePane.populateBean(ob.getImageBackground());
        }
        checkRadius();
        checkImagePane();
    }

    @Override
    public AttrNode updateBean() {
        AttrNode attrNode = new AttrNode();
        attrNode.setAutoRadius(nodeRadiusType.getSelectedIndex() == 0);
        attrNode.setRadius(nodeRadius.getValue());
        attrNode.setBorderWidth(nodeBorderWidth.getValue());
        attrNode.setBorderColor(nodeBorderColor.update());
        attrNode.setOpacity(nodeOpacity.updateBean());
        attrNode.setUseImage(useImage.getSelectedIndex() == 0);
        attrNode.setImageBackground((ImageBackground) imagePane.updateBean());
        return attrNode;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
