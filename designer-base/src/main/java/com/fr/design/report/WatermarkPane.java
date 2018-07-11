package com.fr.design.report;

import com.fr.base.iofile.attr.WatermarkAttr;
import com.fr.design.dialog.BasicPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.NewColorSelectPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
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
    // 文字颜色
    private NewColorSelectPane colorPane;

    public WatermarkPane() {
        initComponents();
    }

    private void initComponents() {
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(contentPane, BorderLayout.CENTER);

        // 预览
        JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        contentPane.add(leftPane, BorderLayout.CENTER);
        leftPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer-Widget-Style_Preview"), null));
        JPanel previewPaneWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        leftPane.add(previewPaneWrapper, BorderLayout.CENTER);
        previewPaneWrapper.setBorder(BorderFactory.createEmptyBorder(2, 8, 4, 8));
        watermarkPreviewPane = new WatermarkPreviewPane();
        previewPaneWrapper.add(watermarkPreviewPane, BorderLayout.CENTER);

        // 设置
        JPanel rightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        contentPane.add(rightPane, BorderLayout.EAST);
        rightPane.add(initRightPane(), BorderLayout.CENTER);
    }

    public void populate(WatermarkAttr watermark) {
        if (watermark == null) {
            watermark = new WatermarkAttr();
        }
        populateFourmula(watermark.getText());
        populateFontSize(watermark.getFontSize());
        populateColor(watermark.getColor());
        paintPreviewPane();
    }

    public WatermarkAttr update() {
        WatermarkAttr watermark = new WatermarkAttr();
        watermark.setText(formulaPane.getUITextField().getText());
        watermark.setFontSize((int)fontSizeComboBox.getSelectedItem());
        watermark.setColor(colorPane.getColor());
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
        JPanel fontSizeTypePane = new JPanel(new BorderLayout(10,0));
        fontSizeTypePane.add(fontSizeComboBox, BorderLayout.CENTER);

        colorPane = new NewColorSelectPane();
        JPanel colorLabelPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        colorLabelPane.add(new UILabel(Inter.getLocText("FR-Designer_Text_Color")), BorderLayout.NORTH);

        double p = TableLayout.PREFERRED;
        double[] rowSize = {p,p,p};
        double[] columnSize = { p, MAX_WIDTH};

        JPanel rightContentPane = TableLayoutHelper.createCommonTableLayoutPane( new JComponent[][]{
                {new UILabel(Inter.getLocText("FR-Designer_Watermark_Text")), formulaPane},
                {new UILabel(Inter.getLocText("FR-Designer_Font_Size")), fontSizeTypePane},
                {colorLabelPane, colorPane},
        }, rowSize, columnSize, 10);
        rightContentPane.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 12));

        UIScrollPane configPane = new UIScrollPane(rightContentPane);
        configPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Config"),null));
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
        colorPane.setColor(color);
        colorPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                paintPreviewPane();
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_WaterMark");
    }
}