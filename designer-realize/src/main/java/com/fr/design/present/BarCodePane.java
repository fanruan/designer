package com.fr.design.present;

import com.fr.code.bar.core.BarcodeAttr;
import com.fr.code.BarcodeImpl;
import com.fr.code.bar.core.BarCodeUtils;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.report.cell.cellattr.BarcodePresent;
import com.fr.stable.pinyin.ChineseHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhou
 * @since 2012-6-4下午6:49:59
 */
public class BarCodePane extends FurtherBasicBeanPane<BarcodePresent> {
    private final int NUM16 = 16;
    private BarCodePreviewPane barCodePreviewPane;
    private UIComboBox typeComboBox;
    private UISpinner barWidthSpinner;
    private UISpinner barHeightSpinner;
    private UISpinner RCodesizespinner;
    private UICheckBox drawingTextCheckBox;
    private UIComboBox RCodeVersionComboBox;
    private UIComboBox RCodeErrorCorrectComboBox;
    private UILabel typeSetLabel;

    private String testText = "12345";

    public BarCodePane() {
        this.initComponents();
        addlistener();
    }

    private void initComponents() {
        barCodePreviewPane = new BarCodePreviewPane();
        this.barWidthSpinner = new UISpinner(1,100.0,1.0,10.0);
        this.barHeightSpinner = new UISpinner(1,100.0,1.0,30);
        this.barWidthSpinner.setPreferredSize(new Dimension(60, 20));
        this.barHeightSpinner.setPreferredSize(new Dimension(60, 20));
        JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        TitledBorder titledBorder = new TitledBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 5), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_StyleFormat_Sample"), 4, 2, this.getFont(), UIConstants.LINE_COLOR);
        borderPane.setBorder(titledBorder);
        borderPane.add(barCodePreviewPane, BorderLayout.CENTER);
        setTypeComboBox();
        setSome();
        RCodesizespinner = new UISpinner(1,6,1,2);
        RCodeVersionComboBox = new UIComboBox();
        RCodeErrorCorrectComboBox = new UIComboBox();
        typeSetLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Type_Set"), UILabel.LEFT);
        initVersionComboBox();
        initErrorCorrectComboBox();

        drawingTextCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("BarCodeD-Drawing_Text"));
        drawingTextCheckBox.setSelected(true);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p, p, p, p, p, p};
        int[][] rowCount = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        barCodePreviewPane.setPreferredSize(new Dimension(0, 125));
        typeComboBox.setPreferredSize(new Dimension(155,20));
        final JPanel centerPane = new JPanel(new CardLayout());

        Component[][] components = new Component[][]{
                new Component[]{typeSetLabel, typeComboBox},
                new Component[]{borderPane, null},
                new Component[]{centerPane, null}
        };
        JPanel barCode = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_HUGER, LayoutConstants.VGAP_LARGE);
        centerPane.add(getNormalPane(), "normal");
        centerPane.add(getSpecialPane(), "special");
        typeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                CardLayout cardLayout = (CardLayout) centerPane.getLayout();
                cardLayout.show(centerPane, typeComboBox.getSelectedIndex() == NUM16 ? "special" : "normal");
                setTestText(BarCodeUtils.getTestTextByBarCode(typeComboBox.getSelectedIndex()));
                repaintPreviewBarCode();
            }
        });
        this.setLayout(new BorderLayout());
        this.add(barCode, BorderLayout.CENTER);
    }

    private void setTypeComboBox() {
        typeComboBox = new UIComboBox(BarCodeUtils.getAllSupportedBarCodeTypeArray());
        typeComboBox.setRenderer(new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Integer) {
                    this.setText(" " + BarCodeUtils.getBarCodeTypeName(((Integer) value).intValue()));
                }
                return this;
            }
        });
    }

    private void setSome() {
        UINumberField heightTextField = barHeightSpinner.getTextField();
        heightTextField.setColumns(2);

        UINumberField widthTextField = barWidthSpinner.getTextField();
        widthTextField.setColumns(2);
    }

    private JPanel getNormalPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p, p, p, p};
        double[] columnSize = {p, f, f};
        int[][] rowCount = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        JPanel barWidthContainer = new JPanel(new BorderLayout());
        barWidthContainer.add(barWidthSpinner);
        JPanel barHeightContainer = new JPanel(new BorderLayout());
        barHeightContainer.add(barHeightSpinner);
        UILabel uiLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Tree_Width"), UILabel.RIGHT);
        uiLabel.setPreferredSize(typeSetLabel.getPreferredSize());
        JPanel drawingTextCheckBoxPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        drawingTextCheckBox.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        drawingTextCheckBoxPane.add(drawingTextCheckBox);
        Component[][] components_normal = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Barcode_Size"), UILabel.LEFT), barWidthContainer, barHeightContainer},
                new Component[]{null, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Tree_Width"), UILabel.CENTER), new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Tree_Height"), UILabel.CENTER)},
                new Component[]{drawingTextCheckBoxPane, null, null}
        };


        JPanel normalPane = TableLayoutHelper.createGapTableLayoutPane(components_normal, rowSize, columnSize, rowCount, LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_LARGE);
        return normalPane;
    }


    private JPanel getSpecialPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        UILabel uiLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("RCodeVersion"), UILabel.LEFT);
        uiLabel.setPreferredSize(typeSetLabel.getPreferredSize());
        RCodeVersionComboBox.setPreferredSize(new Dimension(155,20));
        RCodeErrorCorrectComboBox.setPreferredSize(new Dimension(155,20));
        RCodesizespinner.setPreferredSize(new Dimension(155,20));
        Component[][] components_special = new Component[][]{
                new Component[]{uiLabel, RCodeVersionComboBox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("RCodeErrorCorrect"), UILabel.LEFT), RCodeErrorCorrectComboBox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RCodeDrawPix"), UILabel.LEFT), RCodesizespinner}
        };

        JPanel specialPane = TableLayoutHelper.createGapTableLayoutPane(components_special, rowSize, columnSize, rowCount, LayoutConstants.VGAP_HUGER, LayoutConstants.VGAP_LARGE);
        return specialPane;
    }

    private void addlistener() {
        RCodesizespinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                repaintPreviewBarCode();
            }
        });
        RCodeVersionComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                repaintPreviewBarCode();
            }
        });
        RCodeErrorCorrectComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                repaintPreviewBarCode();
            }
        });
        this.barWidthSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                repaintPreviewBarCode();
            }
        });
        this.barHeightSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                repaintPreviewBarCode();
            }
        });
        drawingTextCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                repaintPreviewBarCode();
            }
        });
        repaintPreviewBarCode();
    }

    @Override
    /**
     *
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Highlight_Barcode");
    }

    private void initVersionComboBox() {
        String[] array = {com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Auto", "Choose"}), "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
        initcombobox(this.RCodeVersionComboBox, array, 0);
    }

    private void initErrorCorrectComboBox() {
        String[] array = {"L" + com.fr.design.i18n.Toolkit.i18nText("Level") + "7%", "M" + com.fr.design.i18n.Toolkit.i18nText("Level") + "15%", "Q" + com.fr.design.i18n.Toolkit.i18nText("Level") + "25%", "H" + com.fr.design.i18n.Toolkit.i18nText("Level") + "30%"};
        initcombobox(this.RCodeErrorCorrectComboBox, array, 1);
    }

    private void initcombobox(UIComboBox combobox, String[] array, int index) {
        combobox.removeAllItems();
        for (int i = 0; i < array.length; i++) {
            combobox.addItem(array[i]);
        }
        combobox.setSelectedIndex(index);
    }

    private void repaintPreviewBarCode() {
        try {
            // carl:不支持中文转条形码
            if (ChineseHelper.containChinese(getTestText()) && this.typeComboBox.getSelectedIndex() != NUM16) {
                throw new Exception("Illegal Character.");
            }
            this.barCodePreviewPane.setObject(BarCodeUtils.getBarcodeImpl(this.updateBean().getBarcode(), getTestText()));
        } catch (Exception exp) {
            this.barCodePreviewPane.setObject(com.fr.design.i18n.Toolkit.i18nText("Error") + ": " + exp.getMessage());
        }
    }

    /**
     *
     */
    public void reset() {
        populateBean(new BarcodePresent());
    }

    @Override
    public void populateBean(BarcodePresent ob) {
        BarcodeAttr barcodeAttr = ob.getBarcode();
        if (barcodeAttr == null) {
            barcodeAttr = new BarcodeAttr();
        }
        this.setTestText(BarCodeUtils.getTestTextByBarCode(barcodeAttr.getType()));
        this.typeComboBox.setSelectedIndex(barcodeAttr.getType());
        this.barWidthSpinner.setValue(new Double(barcodeAttr.getBarWidth()) * 10);
        this.barHeightSpinner.setValue(new Integer(barcodeAttr.getBarHeight()));
        this.drawingTextCheckBox.setSelected(barcodeAttr.isDrawingText());
        this.RCodesizespinner.setValue(new Integer(barcodeAttr.getRcodeDrawPix()));
        this.repaintPreviewBarCode();
    }

    @Override
    public BarcodePresent updateBean() {
        BarcodeAttr barcodeAttr = new BarcodeAttr();
        if ((typeComboBox.getSelectedIndex() == NUM16)) {
            barcodeAttr.setRCodeVersion(this.RCodeVersionComboBox.getSelectedIndex());
            barcodeAttr.setRCodeErrorCorrect(this.RCodeErrorCorrectComboBox.getSelectedIndex());
            barcodeAttr.setRcodeDrawPix((int) this.RCodesizespinner.getValue());
        }
        barcodeAttr.setType(this.typeComboBox.getSelectedIndex());
        barcodeAttr.setBarWidth(((Double) this.barWidthSpinner.getValue()).doubleValue() / 10);
        barcodeAttr.setBarHeight((int) this.barHeightSpinner.getValue());
        barcodeAttr.setDrawingText(this.drawingTextCheckBox.isSelected());
        return new BarcodePresent(barcodeAttr);
    }

    public void setTestText(String testText) {
        this.testText = testText;
    }

    public String getTestText() {
        return testText;
    }

    private static class BarCodePreviewPane extends JPanel {
        private Object obj;

        public BarCodePreviewPane() {
//			setBackground(Color.WHITE);
        }

        /**
         * BarcodeImpl or Error String.
         */
        public void setObject(Object obj) {
            this.obj = obj;
            GUICoreUtils.repaint(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (obj == null) {
                return;
            }
            if (obj instanceof BarcodeImpl) {
                BarcodeImpl barcodeImpl = (BarcodeImpl) obj;
                Dimension size = this.getSize();
                barcodeImpl.draw((Graphics2D) g, (int) (size.getWidth() - barcodeImpl.getWidth()) / 2, (int) (size.getHeight() - barcodeImpl.getHeight()) / 2);
            } else {
                // 在中央画出字符.
                Graphics2D graphics2D = (Graphics2D) g;
                graphics2D.setPaint(Color.RED);
                Map map = new HashMap();
                map.put(TextAttribute.SIZE, new Float(14.0));
                AttributedString vanGogh = new AttributedString(obj.toString(), map);
                AttributedCharacterIterator paragraph = vanGogh.getIterator();
                int paragraphStart = paragraph.getBeginIndex();
                int paragraphEnd = paragraph.getEndIndex();
                // Create a new LineBreakMeasurer from the paragraph.
                AffineTransform tx = null;
                LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, new FontRenderContext(tx, false, false));
                // Set formatting width to width of Component.
                Dimension size = getSize();
                float formatWidth = size.width;
                float drawPosY = 0;
                lineMeasurer.setPosition(paragraphStart);
                // Get lines from lineMeasurer until the entire
                // paragraph has been displayed.
                while (lineMeasurer.getPosition() < paragraphEnd) {
                    // Retrieve next layout.
                    TextLayout layout = lineMeasurer.nextLayout(formatWidth);
                    // Move y-coordinate by the ascent of the layout.
                    drawPosY += layout.getAscent();
                    // Compute pen x position. If the paragraph is
                    // right-to-left, we want to align the TextLayouts
                    // to the right edge of the panel.
                    float drawPosX;
                    if (layout.isLeftToRight()) {
                        drawPosX = 0;
                    } else {
                        drawPosX = formatWidth - layout.getAdvance();
                    }
                    // Draw the TextLayout at (drawPosX, drawPosY).
                    layout.draw(graphics2D, drawPosX, drawPosY);
                    // Move y-coordinate in preparation for next layout.
                    drawPosY += layout.getDescent() + layout.getLeading();
                }
            }
        }
    }

    @Override
    /**
     *
     */
    public boolean accept(Object ob) {
        return ob instanceof BarcodePresent;
    }
}
