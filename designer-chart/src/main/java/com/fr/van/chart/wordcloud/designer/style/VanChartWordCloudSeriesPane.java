package com.fr.van.chart.wordcloud.designer.style;

import com.fr.base.Utils;
import com.fr.base.background.ImageBackground;
import com.fr.base.background.ImageFileBackground;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundQuickPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.general.IOUtils;
import com.fr.plugin.chart.wordcloud.CloudShapeType;
import com.fr.plugin.chart.wordcloud.VanChartWordCloudPlot;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.VanChartBeautyPane;
import com.fr.van.chart.designer.style.series.VanChartAbstractPlotSeriesPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by Mitisky on 16/11/29.
 */
public class VanChartWordCloudSeriesPane extends VanChartAbstractPlotSeriesPane {
    private static final String AUTO_FONT_SIZE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Auto");
    private static final String CUSTOM_FONT_SIZE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Define_Size");
    private static final double MAX_ROTATION = 90;
    private static final double LABEL_SIZE = 65;
    private UIComboBox fontNameComboBox;
    private UISpinner minRotation;
    private UISpinner maxRotation;
    private UIButtonGroup defineFontSize;
    private JPanel fontPanel;
    private UISpinner minFontSize;
    private UISpinner maxFontSize;

    private UIComboBox cloudShape;
    private ImageBackgroundQuickPane imageBackgroundQuickPane;

    public VanChartWordCloudSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    /**
     * 在每个不同类型Plot, 得到不同类型的属性. 比如: 柱形的风格, 折线的线型曲线.
     */
    @Override
    protected JPanel getContentInPlotType() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createWordCloudStylePane()},
        };

        contentPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        return contentPane;
    }

    private JPanel createWordCloudStylePane(){
        double labelSize = LABEL_SIZE;
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;


        double[] centerC = {labelSize,f,p,f};
        double[] centerR = {p};

        minRotation = new UISpinner(-MAX_ROTATION,MAX_ROTATION,1,0);
        maxRotation = new
                UISpinner(-MAX_ROTATION,MAX_ROTATION,1,0);
        Component[][] centerComps = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Rotation_Angle")), minRotation,
                        new UILabel("-"), maxRotation},
        };
        JPanel centerPanel = TableLayout4VanChartHelper.createGapTableLayoutPane(centerComps,centerR,centerC);

        double[] northC = {f, e};
        double[] northR = {p,p};
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        defineFontSize = new UIButtonGroup(new String[]{AUTO_FONT_SIZE, CUSTOM_FONT_SIZE});
        Component[][] northComps = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Font")), fontNameComboBox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom")), defineFontSize }
        };
        JPanel northPanel = TableLayout4VanChartHelper.createGapTableLayoutPane(northComps,northR,northC);

        minFontSize = new UISpinner(0,Double.MAX_VALUE,1,10);
        maxFontSize = new UISpinner(0,Double.MAX_VALUE,1,100);
        Component[][] fontComps = new Component[][]{
                new Component[]{null, minFontSize,
                        new UILabel("-"), maxFontSize},
        };
        fontPanel = TableLayout4VanChartHelper.createGapTableLayoutPane(fontComps,centerR,centerC);

        double[] columnSize = {f};
        double[] rowSize = {p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{createCloudShapePane()},
                new Component[]{centerPanel},
                new Component[]{northPanel},
                new Component[]{fontPanel},

        };

        defineFontSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkFontPane();
            }
        });

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, rowSize, columnSize);

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Name"), panel);
    }

    private JPanel createCloudShapePane() {
        cloudShape = new UIComboBox(CloudShapeType.getTypes());
        imageBackgroundQuickPane = new ImageBackgroundQuickPane(false){
            @Override
            public Dimension getPreferredSize() {
                if(cloudShape.getSelectedItem() == CloudShapeType.DEFAULT){
                    return new Dimension(0,0);
                } else {
                    return super.getPreferredSize();
                }
            }
        };

        JPanel panel = new JPanel(new BorderLayout(0,4));
        panel.add(cloudShape, BorderLayout.NORTH);
        panel.add(imageBackgroundQuickPane, BorderLayout.CENTER);

        cloudShape.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CloudShapeType type = (CloudShapeType)cloudShape.getSelectedItem();
                String path = type.getImageUrl();
                if(path != null) {
                    ImageFileBackground imageBackground = new ImageFileBackground(IOUtils.readImage(path));
                    imageBackgroundQuickPane.populateBean(imageBackground);
                } else {
                    imageBackgroundQuickPane.populateBean(new ImageFileBackground());
                }
                checkImagePane();
            }
        });

        return TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Shape"), panel);
    }

    private void checkImagePane() {
        GUICoreUtils.setEnabled(imageBackgroundQuickPane, cloudShape.getSelectedItem() == CloudShapeType.CUSTOM);
    }

    private void checkFontPane() {
        fontPanel.setVisible(defineFontSize.getSelectedIndex() == 1);
    }

    /**
     * 更新Plot的属性到系列界面
     *
     * @param plot
     */
    @Override
    public void populateBean(Plot plot) {
        super.populateBean(plot);
        if(plot instanceof VanChartWordCloudPlot){
            VanChartWordCloudPlot wordCloudPlot = (VanChartWordCloudPlot)plot;
            fontNameComboBox.setSelectedItem(wordCloudPlot.getFont().getFamily());
            minRotation.setValue(wordCloudPlot.getMinRotation());
            maxRotation.setValue(wordCloudPlot.getMaxRotation());

            defineFontSize.setSelectedIndex(wordCloudPlot.isAutoFontSize() ? 0 : 1);
            minFontSize.setValue(wordCloudPlot.getMinFontSize());
            maxFontSize.setValue(wordCloudPlot.getMaxFontSize());

            cloudShape.setSelectedItem(wordCloudPlot.getShapeType());
            ImageBackground imageBackground = wordCloudPlot.getShapeImage();
            if(imageBackground != null) {
                imageBackgroundQuickPane.populateBean(imageBackground);
            }
        }
        checkFontPane();
        checkImagePane();
    }

    /**
     * 保存 系列界面的属性到Plot
     *
     * @param plot
     */
    @Override
    public void updateBean(Plot plot) {
        super.updateBean(plot);
        if(plot instanceof VanChartWordCloudPlot){
            VanChartWordCloudPlot wordCloudPlot = (VanChartWordCloudPlot)plot;
            wordCloudPlot.setFont(FRFont.getInstance(fontNameComboBox.getSelectedItem().toString(), Font.PLAIN, 9));
            wordCloudPlot.setMinRotation(minRotation.getValue());
            wordCloudPlot.setMaxRotation(maxRotation.getValue());

            wordCloudPlot.setAutoFontSize(defineFontSize.getSelectedIndex() == 0);
            wordCloudPlot.setMinFontSize(minFontSize.getValue());
            wordCloudPlot.setMaxFontSize(maxFontSize.getValue());

            wordCloudPlot.setShapeType((CloudShapeType) cloudShape.getSelectedItem());
            if(wordCloudPlot.getShapeType() != CloudShapeType.DEFAULT) {
                wordCloudPlot.setShapeImage((ImageBackground) imageBackgroundQuickPane.updateBean());
            } else {
                wordCloudPlot.setShapeImage(null);
            }
        }
    }

    @Override
    protected VanChartBeautyPane createStylePane() {
        return null;
    }
}
