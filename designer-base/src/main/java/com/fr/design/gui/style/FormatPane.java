package com.fr.design.gui.style;

import com.fr.base.CoreDecimalFormat;
import com.fr.base.GraphHelper;
import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.data.core.FormatField;
import com.fr.data.core.FormatField.FormatContents;
import com.fr.design.i18n.Toolkit;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.gui.icombobox.TextFontComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;

import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Format pane to edit java.text.Format.
 *
 * @author zhou
 * @since 2012-5-24上午10:57:00
 */
public class FormatPane extends AbstractBasicStylePane implements GlobalNameObserver {
    private static final long serialVersionUID = 724330854437726751L;

    private static final int LABEL_X = 4;
    private static final int LABEL_Y = 18;
    private static final int LABEL_DELTA_WIDTH = 8;
    private static final int LABEL_HEIGHT = 15; //标签背景的范围
    private static final int CURRENCY_FLAG_POINT = 6;
    private static final Border LEFT_BORDER = BorderFactory.createEmptyBorder(0, 30, 0, 0);

    private static final Integer[] TYPES = new Integer[]{
            FormatContents.NULL, FormatContents.NUMBER,
            FormatContents.CURRENCY, FormatContents.PERCENT,
            FormatContents.SCIENTIFIC, FormatContents.DATE,
            FormatContents.TIME, FormatContents.TEXT};

    private static final Integer[] DATE_TYPES = new Integer[]{FormatContents.NULL, FormatContents.DATE, FormatContents.TIME};

    private Format format;

    private UIComboBox typeComboBox;
    private TextFontComboBox textField;
    private UILabel sampleLabel;
    private JPanel contentPane;
    private JPanel txtCenterPane;
    private JPanel centerPane;
    private JPanel formatFontPane;
    private FRFontPane frFontPane;
    private boolean isRightFormat;
    private boolean isDate = false;
    private GlobalNameListener globalNameListener = null;

    /**
     * Constructor.
     */
    public FormatPane() {
        this.initComponents(TYPES);
    }

    protected UIComboBox getTypeComboBox() {
        return typeComboBox;
    }

    protected void initComponents(Integer[] types) {
        this.setLayout(new BorderLayout(0, 4));
        initSampleLabel();
        contentPane = new JPanel(new BorderLayout(0, 4)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 65);
            }
        };
        typeComboBox = new UIComboBox(types);
        UIComboBoxRenderer render = createComBoxRender();
        typeComboBox.setRenderer(render);
        typeComboBox.addItemListener(itemListener);
        typeComboBox.setGlobalName("typeComboBox");
        contentPane.add(sampleLabel, BorderLayout.NORTH);

        txtCenterPane = new JPanel(new BorderLayout());
        textField = new TextFontComboBox();
        textField.addItemListener(textFieldItemListener);
        textField.setEditable(true);
        textField.setGlobalName("textField");
        txtCenterPane.add(textField, BorderLayout.NORTH);

        contentPane.add(txtCenterPane, BorderLayout.CENTER);

        centerPane = new JPanel(new CardLayout());
        centerPane.add(new JPanel(), "hide");
        centerPane.setPreferredSize(new Dimension(0, 0));
        centerPane.add(contentPane, "show");


        frFontPane = new FRFontPane();
        UILabel font = new UILabel(Toolkit.i18nText("Fine-Design_Form_FR_Font"), SwingConstants.LEFT);
        JPanel fontPane = new JPanel(new BorderLayout());
        fontPane.add(font, BorderLayout.NORTH);

        typeComboBox.setPreferredSize(new Dimension(155,20));
        JPanel typePane = new JPanel(new BorderLayout());
        typePane.add(typeComboBox, BorderLayout.CENTER);
        typePane.setBorder(LEFT_BORDER);
//        centerPane.setBorder(LEFT_BORDER);
        frFontPane.setBorder(LEFT_BORDER);

        Component[][] components = getComponent(fontPane, centerPane, typePane);
        this.add(createContentPane(components), BorderLayout.CENTER);
    }

    protected JPanel createContentPane (Component[][] components) {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_MEDIUM);
    }


    protected Component[][] getComponent (JPanel fontPane, JPanel centerPane, JPanel typePane) {
        return new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Report_Base_Format"), SwingConstants.LEFT), typePane},
                new Component[]{centerPane, null},
                new Component[]{fontPane, frFontPane},
        };
    }

    protected UIComboBoxRenderer createComBoxRender() {
        return new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Integer) {
                    label.setText(" " + FormatField.getInstance().getName((Integer) value));
                }
                return label;
            }
        };
    }

    private void initSampleLabel() {
        Border interBorder = new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 4);
        String title = Toolkit.i18nText("Fine-Design_Report_Base_StyleFormat_Sample");
        Border border = BorderFactory.createTitledBorder(interBorder, title, TitledBorder.LEFT, 0, null, UIConstants.LINE_COLOR);
        sampleLabel = new UILabel(FormatField.getInstance().getFormatValue()) {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                int width = getWidth();
                Color original = g.getColor();
                g.setColor(getBackground());
                g.fillRect(LABEL_X, LABEL_Y, width - LABEL_DELTA_WIDTH, LABEL_HEIGHT);
                g.setColor(UIConstants.LINE_COLOR);
                FontMetrics cellFM = g.getFontMetrics();
                int textWidth = cellFM.stringWidth(getText());
                GraphHelper.drawString(g, getText(), (width - textWidth) / 2, 26);
                g.setColor(original);
            }
        };
        sampleLabel.setHorizontalAlignment(UILabel.CENTER);
        sampleLabel.setBorder(border);
    }


    @Override
    /**
     * 得到合适的大小
     */
    public Dimension getPreferredSize() {
        if (this.typeComboBox.getSelectedIndex() == FormatContents.NULL) {
            return typeComboBox.getPreferredSize();
        }
        return super.getPreferredSize();
    }

    /**
     * 弹出框标题
     *
     * @return 标题
     */
    public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Report_Text");
    }

    /**
     * Populate
     */
    public void populateBean(Format format) {
        this.format = format;

        if (format == null) {
            this.typeComboBox.setSelectedIndex(FormatContents.NULL);
        } else {
            if (format instanceof CoreDecimalFormat) {
                // check all value
                String pattern = ((CoreDecimalFormat) format).toPattern();
                if (isCurrencyFormatStyle(pattern)) {
                    setPatternComboBoxAndList(FormatContents.CURRENCY, pattern);
                } else if (pattern.indexOf("%") > 0) {
                    setPatternComboBoxAndList(FormatContents.PERCENT, pattern);
                } else if (pattern.indexOf("E") > 0) {
                    setPatternComboBoxAndList(FormatContents.SCIENTIFIC, pattern);
                } else {
                    setPatternComboBoxAndList(FormatContents.NUMBER, pattern);
                }
            } else if (format instanceof SimpleDateFormat) { // date and time
                String pattern = ((SimpleDateFormat) format).toPattern();
                if (!isTimeType(pattern)) {
                    setPatternComboBoxAndList(FormatContents.DATE, pattern);
                } else {
                    setPatternComboBoxAndList(FormatContents.TIME, pattern);
                }
            } else if (format instanceof TextFormat) { // Text
                this.typeComboBox.setSelectedItem(FormatContents.TEXT);
            }
        }
    }

    private boolean isCurrencyFormatStyle(String pattern) {
        if (pattern.length() == 0) {
            return false;
        }

        if (pattern.charAt(0) == '¤' || pattern.charAt(0) == '$') {
            return true;
        }

        return pattern.length() > CURRENCY_FLAG_POINT && pattern.startsWith("#,##0;");
    }

    /**
     * 判断是否是数组有模式
     *
     * @param stringArray 字符串数组
     * @param pattern     格式
     * @return 是否是数组有模式
     */
    public static int isArrayContainPattern(String[] stringArray, String pattern) {
        for (int i = 0; i < stringArray.length; i++) {
            if (ComparatorUtils.equals(stringArray[i], pattern)) {
                return i;
            }
        }

        return -1;
    }

    private void setPatternComboBoxAndList(int formatStyle, String pattern) {
        this.typeComboBox.setSelectedItem(formatStyle);
        this.textField.setSelectedItem(pattern);
    }

    private boolean isTimeType(String pattern) {
        return pattern.matches(".*[Hhmsa].*");
    }

    /**
     * update
     */
    public Format update() {
        String patternString = String.valueOf(textField.getSelectedItem());
        if (getFormatContents() == FormatContents.TEXT) {
            return FormatField.getInstance().getFormat(getFormatContents(), patternString);
        }
        if (isRightFormat) {
            if (StringUtils.isNotEmpty(patternString)) {
                return FormatField.getInstance().getFormat(getFormatContents(), patternString);
            }
        }
        return null;
    }

    private int getFormatContents() {
        return (Integer) typeComboBox.getSelectedItem();
    }

    /**
     * Refresh preview label.
     */
    private void refreshPreviewLabel() {
        this.sampleLabel.setText(FormatField.getInstance().getFormatValue());
        this.sampleLabel.setForeground(UIManager.getColor("Label.foreground"));
        try {
            isRightFormat = true;
            if (StringUtils.isEmpty(String.valueOf(textField.getSelectedItem()))) {
                return;
            }
            this.sampleLabel.setText(FormatField.getInstance().getFormatValue(getFormatContents(), String.valueOf(textField.getSelectedItem())));
        } catch (Exception e) {
            this.sampleLabel.setForeground(Color.red);
            this.sampleLabel.setText(e.getMessage());
            isRightFormat = false;
        }
    }

    private boolean isTextOrNull() {
        int contents = getFormatContents();
        return contents == FormatContents.TEXT || contents == FormatContents.NULL;
    }

    /**
     * Radio selection listener.
     */
    ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int contents = getFormatContents();
                String[] items = FormatField.getInstance().getFormatArray(contents, false);
                CardLayout cardLayout = (CardLayout) centerPane.getLayout();

                if (isTextOrNull()) {
                    centerPane.setPreferredSize(new Dimension(0, 0));
                    cardLayout.show(centerPane, "hide");
                } else {
                    textField.removeAllItems();
                    textField.setItemArray(items);
                    textField.setSelectedIndex(0);
                    centerPane.setPreferredSize(new Dimension(270, 65));
                    cardLayout.show(centerPane, "show");
                }
            }

        }
    };

    ItemListener textFieldItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                refreshPreviewLabel();
            }
        }
    };

    @Override
    /**
     * populate
     */
    public void populateBean(Style style) {
        this.populateBean(style.getFormat());
        this.frFontPane.populateBean(style.getFRFont());
    }

    @Override
    /**
     * update
     */
    public Style update(Style style) {
        if (ComparatorUtils.equals(globalNameListener.getGlobalName(), "textField") || ComparatorUtils.equals(globalNameListener.getGlobalName(), "typeComboBox")) {
            return style.deriveFormat(this.update());
        } else {
            return style.deriveFRFont(this.frFontPane.update(style.getFRFont()));
        }
    }

    /**
     * 默认只显示百分比的编辑下拉.
     */
    public void justUsePercentFormat() {
        typeComboBox.setEnabled(false);
        this.typeComboBox.setSelectedItem(FormatContents.PERCENT);
    }

    public void setForDataSheet() {
        Integer[] otherTypes = new Integer[]{FormatContents.NULL, FormatContents.NUMBER, FormatContents.CURRENCY, FormatContents.PERCENT, FormatContents.SCIENTIFIC,};
        this.typeComboBox = new UIComboBox(otherTypes);
        UIComboBoxRenderer render = new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Integer) {
                    label.setText(" " + FormatField.getInstance().getName((Integer) value));
                }
                return label;
            }
        };
        typeComboBox.setRenderer(render);
        typeComboBox.addItemListener(itemListener);
        setTypeComboBoxPane(typeComboBox);
    }

    protected void setTypeComboBoxPane (UIComboBox typeComboBox) {
        this.add(typeComboBox, BorderLayout.NORTH);
    }

    public void setComboBoxModel(boolean isDate) {
        if (this.isDate != isDate) {
            this.isDate = isDate;
            this.typeComboBox.setSelectedIndex(0);
            if (isDate) {
                for (int i = 0; i < DATE_TYPES.length; i++) {
                    this.typeComboBox.addItem(DATE_TYPES[i]);
                }
                for (int i = 0; i < TYPES.length; i++) {
                    this.typeComboBox.removeItemAt(1);
                }
            } else {
                for (int i = 0; i < TYPES.length; i++) {
                    this.typeComboBox.addItem(TYPES[i]);
                }
                for (int i = 0; i < DATE_TYPES.length; i++) {
                    this.typeComboBox.removeItemAt(1);
                }
            }
        }
    }

    @Override
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    @Override
    public boolean shouldResponseNameListener() {
        return false;
    }

    @Override
    public void setGlobalName(String name) {

    }
}
