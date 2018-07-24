package com.fr.van.chart.designer.component.marker;

import com.fr.base.background.ImageBackground;
import com.fr.base.background.ImageFileBackground;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundQuickPane;

import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.marker.type.MarkerType;
import com.fr.stable.Constants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Created by Mitisky on 16/5/19.
 */
public class VanChartImageMarkerPane extends BasicBeanPane<VanChartAttrMarker> {
    private ImageBackgroundQuickPane imageBackgroundPane;
    private UISpinner width;
    private UISpinner height;

    public VanChartImageMarkerPane() {
        imageBackgroundPane = new ImageBackgroundQuickPane(false);
        imageBackgroundPane.setPreferredSize(getImageBackgroundPreferredSize(imageBackgroundPane.getPreferredSize()));
        width = new UISpinner(0, 100, 0.5, 30);
        height = new UISpinner(0, 100, 0.5, 30);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p, p};
        double[] col = {p, f};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Width")), width},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Height")), height},
        };

        JPanel sizePanel = TableLayoutHelper.createTableLayoutPane(components, row, col);

        JPanel panel = createContentPane(imageBackgroundPane, sizePanel);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 72, 0, 0));

        this.add(panel);
    }

    protected Dimension getImageBackgroundPreferredSize(Dimension dimension) {
        return new Dimension((int) TableLayout4VanChartHelper.EDIT_AREA_WIDTH, (int) dimension.getHeight());
    }

    protected JPanel createContentPane(ImageBackgroundQuickPane imageBackgroundPane, JPanel sizePanel) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(imageBackgroundPane, BorderLayout.CENTER);
        panel.add(sizePanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     *
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Image_Map_Data");
    }

    @Override
    public void populateBean(VanChartAttrMarker marker) {
        if (marker == null) {
            marker = new VanChartAttrMarker();
            marker.setCommon(false);
        }
        if (marker.getImageBackground() != null) {
            imageBackgroundPane.populateBean(marker.getImageBackground());
        }
        width.setValue(marker.getWidth());
        height.setValue(marker.getHeight());
    }

    /**
     * Update.
     */
    @Override
    public VanChartAttrMarker updateBean() {
        VanChartAttrMarker marker = new VanChartAttrMarker();
        updateBean(marker);
        return marker;
    }

    public void updateBean(VanChartAttrMarker marker) {
        marker.setCommon(false);
        marker.setMarkerType(MarkerType.MARKER_NULL);
        ImageFileBackground background = (ImageFileBackground) imageBackgroundPane.updateBean();
        background.setLayout(Constants.IMAGE_EXTEND);
        marker.setImageBackground(background);
        marker.setWidth(width.getValue());
        marker.setHeight(height.getValue());
    }
}
