package com.fr.design.present;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.BarcodeAttr;
import com.fr.report.cell.cellattr.BarcodePresent;
import com.fr.report.cell.painter.barcode.BarcodeImpl;
import com.fr.report.cell.painter.barcode.core.BarCodeUtils;
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
    private UIBasicSpinner barWidthSpinner;
    private UIBasicSpinner barHeightSpinner;
    private UIBasicSpinner RCodesizespinner;
    private UICheckBox drawingTextCheckBox;
    private UIComboBox RCodeVersionComboBox;
    private UIComboBox RCodeErrorCorrectComboBox;
    private UILabel typeSetLabel;

    private String testText = "12345";

    public BarCodePane() {
        this.initComponents();
        addlistener();
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(new BarCodePane(), BorderLayout.CENTER);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(270, 400);
        jf.setVisible(true);
    }

    private void initComponents() {
        barCodePreviewPane = new BarCodePreviewPane();
        this.barWidthSpinner = new UIBasicSpinner(new SpinnerNumberModel(10.0, 1, 100, 1.0));
        this.barHeightSpinner = new UIBasicSpinner(new SpinnerNumberModel(30, 1, 100, 1));
        this.barWidthSpinner.setPreferredSize(new Dimension(60, 20));
        this.barHeightSpinner.setPreferredSize(new Dimension(60, 20));
        JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        TitledBorder titledBorder = new TitledBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 5), Inter.getLocText("StyleFormat-Sample"), 4, 2, this.getFont(), UIConstants.LINE_COLOR);
        borderPane.setBorder(titledBorder);
        borderPane.add(barCodePreviewPane, BorderLayout.CENTER);
        setTypeComboBox();
        setSome();
        RCodesizespinner = new UIBasicSpinner(new SpinnerNumberModel(2, 1, 6, 1));
        RCodeVersionComboBox = new UIComboBox();
        RCodeErrorCorrectComboBox = new UIComboBox();
        typeSetLabel = new UILabel(Inter.getLocText("Type_Set"), UILabel.RIGHT);
        initVersionComboBox();
        initErrorCorrectComboBox();

        drawingTextCheckBox = new UICheckBox(Inter.getLocText("BarCodeD-Drawing_Text"));
        drawingTextCheckBox.setSelected(true);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p, p, p, p, p, p};
        barCodePreviewPane.setPreferredSize(new Dimension(0, 125));
        final JPanel centerPane = new JPanel(new CardLayout());

        Component[][] components = new Component[][]{
                new Component[]{typeSetLabel, typeComboBox},
                new Component[]{borderPane, null},
                new Component[]{centerPane, null}
        };
        JPanel barCode = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
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
        JFormattedTextField heightTextField = ((JSpinner.DefaultEditor) barHeightSpinner.getEditor()).getTextField();
        heightTextField.setColumns(2);

        JFormattedTextField widthTextField = ((JSpinner.DefaultEditor) barWidthSpinner.getEditor()).getTextField();
        widthTextField.setColumns(2);
    }

    private JPanel getNormalPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p, p, p, p};
        double[] columnSize1 = {p, f, f};
        JPanel barWidthContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        barWidthContainer.add(barWidthSpinner);
        JPanel barHeightContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        barHeightContainer.add(barHeightSpinner);
        UILabel uiLabel = new UILabel(Inter.getLocText("Tree-Width") + ":", UILabel.RIGHT);
        uiLabel.setPreferredSize(typeSetLabel.getPreferredSize());
        Component[][] components_normal = new Component[][]{
                new Component[]{new UILabel("条形码大小", UILabel.LEFT), barWidthContainer, barHeightContainer},
                new Component[]{null, new UILabel(Inter.getLocText("Tree-Width"), UILabel.CENTER), new UILabel(Inter.getLocText("Height"), UILabel.CENTER)},
                new Component[]{drawingTextCheckBox, null, null}
        };


        JPanel normalPane = TableLayoutHelper.createTableLayoutPane(components_normal, rowSize, columnSize1);
        return normalPane;
    }


    private JPanel getSpecialPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize1 = {p, p};
        double[] rowSize = {p, p, p, p, p, p, p, p};
        UILabel uiLabel = new UILabel(Inter.getLocText("RCodeVersion") + ":", UILabel.RIGHT);
        uiLabel.setPreferredSize(typeSetLabel.getPreferredSize());
        Component[][] components_special = new Component[][]{
                new Component[]{uiLabel, RCodeVersionComboBox},
                new Component[]{new UILabel(Inter.getLocText("RCodeErrorCorrect") + ":", UILabel.RIGHT), RCodeErrorCorrectComboBox},
                new Component[]{new UILabel(Inter.getLocText("RCodeDrawPix") + ":", UILabel.RIGHT), RCodesizespinner}
        };

        JPanel specialPane = TableLayoutHelper.createTableLayoutPane(components_special, rowSize, columnSize1);
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
        return Inter.getLocText("Highlight-Barcode");
    }

    private void initVersionComboBox() {
        String[] array = {Inter.getLocText(new String[]{"Auto", "Choose"}), "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
        initcombobox(this.RCodeVersionComboBox, array, 0);
    }

    private void initErrorCorrectComboBox() {
        String[] array = {"L" + Inter.getLocText("Level") + "7%", "M" + Inter.getLocText("Level") + "15%", "Q" + Inter.getLocText("Level") + "25%", "H" + Inter.getLocText("Level") + "30%"};
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
            this.barCodePreviewPane.setObject(Inter.getLocText("Error") + ": " + exp.getMessage());
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
        this.barWidthSpinner.setValue(new Double(barcodeAttr.getBarWidth()));
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
            barcodeAttr.setRcodeDrawPix(((Integer) this.RCodesizespinner.getValue()).intValue());
        }
        barcodeAttr.setType(this.typeComboBox.getSelectedIndex());
        barcodeAttr.setBarWidth(((Double) this.barWidthSpinner.getValue()).doubleValue() / 10);
        barcodeAttr.setBarHeight(((Integer) this.barHeightSpinner.getValue()).intValue());
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