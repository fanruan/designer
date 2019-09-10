package com.fr.design.report;

import com.fr.base.iofile.attr.WatermarkAttr;
import com.fr.design.dialog.BasicPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.ispinner.UnsignedIntUISpinner;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.NewColorSelectPane;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by plough on 2018/5/15.
 */
public class WatermarkPane extends BasicPane {
    private static final int MAX_WIDTH = 160;

    // 水印预览面板
    private WatermarkPreviewPane watermarkPreviewPane;
    // 文字
    private TinyFormulaPane formulaPane;
    // 字号
    private UIComboBox fontSizeComboBox;
    //横向间距
    private UISpinner horizontalGapSpinner;
    //横向间距
    private UISpinner verticalGapSpinner;
    // 文字颜色
    private NewColorSelectPane colorPane;

    private static final Dimension SPINNER_DIMENSION = new Dimension(75, 20);

    public WatermarkPane() {
        initComponents();
    }

    private void initComponents() {
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, -5, 4));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(contentPane, BorderLayout.CENTER);

        // 预览
        JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        contentPane.add(leftPane, BorderLayout.CENTER);
        leftPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Style_Preview"), null));
        JPanel previewPaneWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        leftPane.add(previewPaneWrapper, BorderLayout.CENTER);
        previewPaneWrapper.setBorder(BorderFactory.createEmptyBorder(2, 8, 4, 8));
        watermarkPreviewPane = new WatermarkPreviewPane();
        previewPaneWrapper.add(watermarkPreviewPane, BorderLayout.CENTER);

        // 设置
        JPanel rightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        contentPane.add(rightPane, BorderLayout.EAST);
        rightPane.add(initRightPane(), BorderLayout.NORTH);
    }

    public void populate(WatermarkAttr watermark) {
        if (watermark == null) {
            watermark = new WatermarkAttr();
        }
        populateFourmula(watermark.getText());
        populateFontSize(watermark.getFontSize());
        populateWatermarkGap(watermark);
        populateColor(watermark.getColor());
        paintPreviewPane();
    }

    public WatermarkAttr update() {
        WatermarkAttr watermark = new WatermarkAttr();
        watermark.setText(formulaPane.getUITextField().getText());
        watermark.setFontSize((int)fontSizeComboBox.getSelectedItem());
        watermark.setHorizontalGap((int) horizontalGapSpinner.getValue());
        watermark.setVerticalGap((int) verticalGapSpinner.getValue());
        watermark.setColor(colorPane.getColor());
        colorPane.updateUsedColor();
        return watermark;
    }

    public TinyFormulaPane getFormulaPane() {
        return formulaPane;
    }

    public void setFormulaPane(TinyFormulaPane formulaPane) {
        this.formulaPane = formulaPane;
    }

    protected UIScrollPane initRightPane(){
        formulaPane = new TinyFormulaPane();
        fontSizeComboBox = new UIComboBox(FRFontPane.FONT_SIZES);
        fontSizeComboBox.setEditable(true);
        horizontalGapSpinner = new UnsignedIntUISpinner(100, Integer.MAX_VALUE, 1, 200);
        verticalGapSpinner = new UnsignedIntUISpinner(50, Integer.MAX_VALUE, 1, 100);
        horizontalGapSpinner.setPreferredSize(SPINNER_DIMENSION);
        verticalGapSpinner.setPreferredSize(SPINNER_DIMENSION);
        JPanel fontSizeTypePane = new JPanel(new BorderLayout(10,0));
        fontSizeTypePane.add(fontSizeComboBox, BorderLayout.CENTER);

        //水印间距面板
        JPanel watermarkGapPane = new JPanel(new BorderLayout(10, 0));
        JPanel jp = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(2, 10, 0);
        jp.add(horizontalGapSpinner);
        jp.add(verticalGapSpinner);
        watermarkGapPane.add(jp, BorderLayout.CENTER);

        JPanel watermarkGapTipsPane = new JPanel(new BorderLayout());
        JPanel tipsJp = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(2, 10, 0);
        tipsJp.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Direction_Horizontal"), SwingConstants.CENTER));
        tipsJp.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Direction_Vertical"), SwingConstants.CENTER));
        watermarkGapTipsPane.add(tipsJp, BorderLayout.CENTER);

        colorPane = new NewColorSelectPane();
        JPanel colorLabelPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        colorLabelPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Text_Color")), BorderLayout.NORTH);

        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, MAX_WIDTH};

        JPanel rightContentPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Watermark_Text")), formulaPane},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Font_Size")), fontSizeTypePane},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Watermark_Gap")),watermarkGapPane },
                {null,watermarkGapTipsPane },
                {colorLabelPane, colorPane},
        }, rowSize, columnSize, 10);
        rightContentPane.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 12));

        UIScrollPane configPane = new UIScrollPane(rightContentPane);
        configPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Config"),null));
        return configPane;
    }

    protected void populateFontSize(int fontSize){
        this.fontSizeComboBox.setSelectedItem(fontSize);
        this.fontSizeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                paintPreviewPane();
            }
        });
    }

    protected void populateWatermarkGap(WatermarkAttr watermark){
        this.horizontalGapSpinner.setValue(watermark.getHorizontalGap());
        this.horizontalGapSpinner.addUISpinnerFocusListenner(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                paintPreviewPane();
            }

            @Override
            public void focusLost(FocusEvent e) {
                paintPreviewPane();
            }
        });

        this.verticalGapSpinner.setValue(watermark.getVerticalGap());
        this.verticalGapSpinner.addUISpinnerFocusListenner(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                paintPreviewPane();
            }

            @Override
            public void focusLost(FocusEvent e) {
                paintPreviewPane();
            }
        });
    }

    protected void paintPreviewPane(){
        watermarkPreviewPane.repaint(update());
    }

    private void populateFourmula(String formula) {
        this.formulaPane.populateBean(formula);
        this.formulaPane.getUITextField().getDocument()
                .addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        paintPreviewPane();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        paintPreviewPane();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        paintPreviewPane();
                    }
                });
    }

    private void populateColor(Color color) {
        // 颜色面板的色值只有 rgb，去掉 alpha 通道
        colorPane.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue()));
        colorPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                paintPreviewPane();
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WaterMark");
    }
}
