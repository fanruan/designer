package com.fr.design.gui.style;

import com.fr.base.CoreDecimalFormat;
import com.fr.base.GraphHelper;
import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.data.core.FormatField;
import com.fr.data.core.FormatField.FormatContents;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
public class FormatPane extends AbstractBasicStylePane {
    private static final long serialVersionUID = 724330854437726751L;

    private static final int LABLE_X = 4;
    private static final int LABLE_Y = 18;
    private static final int LABLE_DELTA_WIDTH = 8;
    private static final int LABLE_HEIGHT = 15; //标签背景的范围
    private static final int CURRENCY_FLAG_POINT = 6;

    private static final Integer[] TYPES = new Integer[]{FormatContents.NULL, FormatContents.NUMBER, FormatContents.CURRENCY, FormatContents.PERCENT, FormatContents.SCIENTIFIC,
            FormatContents.DATE, FormatContents.TIME, FormatContents.TEXT};

    private static final Integer[] DATETYPES = new Integer[]{FormatContents.NULL, FormatContents.DATE, FormatContents.TIME,};

    private Format format;

    private UIComboBox typeComboBox;
    private UILabel sampleLabel;

    private FormatePaneNumField patternTextField = null;
    private JList patternList = null;
    private JPanel centerPane;
    private boolean isRightFormate;
    private boolean isDate = false;
    /**
     * Constructor.
     */
    public FormatPane() {
        this.initComponents(TYPES);
    }

    protected void initComponents(Integer[] types) {
        this.setLayout(new BorderLayout(0, 4));

        iniSampleLable();

        JPanel contentPane = new JPanel(new BorderLayout(0, 4)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 185);
            }

        };
        typeComboBox = new UIComboBox(types);
        UIComboBoxRenderer render = createComBoxRender();
        typeComboBox.setRenderer(render);
        typeComboBox.addItemListener(itemListener);
        contentPane.add(sampleLabel, BorderLayout.NORTH);
        this.add(typeComboBox, BorderLayout.NORTH);
        centerPane = new JPanel(new CardLayout());
        centerPane.add(new JPanel(), "hide");
        centerPane.add(contentPane, "show");
        this.add(centerPane, BorderLayout.CENTER);
        // content pane.
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        contentPane.add(centerPane, BorderLayout.CENTER);
        patternTextField = new FormatePaneNumField();
        centerPane.add(patternTextField, BorderLayout.NORTH);
        patternTextField.getDocument().addDocumentListener(patternTextDocumentListener);
        patternList = new JList(new DefaultListModel());
        centerPane.add(new UIScrollPane(patternList), BorderLayout.CENTER);
        patternList.addListSelectionListener(patternListSelectionListener);
        patternList.setSelectionBackground(UIConstants.LIGHT_BLUE);
        patternList.setSelectionForeground(Color.black);
        // init values.

    }

    protected UIComboBoxRenderer createComBoxRender(){
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

    private void iniSampleLable() {
        Border innterborder = new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 4);
        Font tmpFont = null;
        Border border = BorderFactory.createTitledBorder(innterborder, Inter.getLocText("FR-Base_StyleFormat_Sample"), TitledBorder.LEFT, 0, tmpFont, UIConstants.LINE_COLOR);
        sampleLabel = new UILabel(FormatField.getInstance().getFormatValue()) {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                int width = getWidth();
                Color orignal = g.getColor();
                g.setColor(getBackground());
                g.fillRect(LABLE_X, LABLE_Y, width - LABLE_DELTA_WIDTH, LABLE_HEIGHT);
                g.setColor(UIConstants.LINE_COLOR);
                FontMetrics cellFM = g.getFontMetrics();
                int textWidth = cellFM.stringWidth(getText());
                GraphHelper.drawString(g, getText(), (width - textWidth) / 2, 26);
                g.setColor(orignal);
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
     * @return 标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("FR-Base_Format");
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
                boolean isCurrency1 = (pattern.length() > 0 && pattern.charAt(0) == '¤');
                boolean isCurrency2 = (pattern.length() > 0 && pattern.charAt(0) == '$');
                boolean isCurrency = isCurrency1 || isCurrency2;
                boolean isCurrency4 = (pattern.length() > CURRENCY_FLAG_POINT && ComparatorUtils.equals(pattern.substring(0, CURRENCY_FLAG_POINT), "#,##0;"));
                if (isCurrency || isCurrency4) {
                    setPatternComboBoxAndList(FormatContents.CURRENCY, pattern);
                } else if (pattern.endsWith("%")) {
                    setPatternComboBoxAndList(FormatContents.PERCENT, pattern);
                } else if (pattern.indexOf("E") > 0) {
                    setPatternComboBoxAndList(FormatContents.SCIENTIFIC, pattern);
                } else {
                    setPatternComboBoxAndList(FormatContents.NUMBER, pattern);
                }
                patternTextField.setText(pattern);
            } else if (format instanceof SimpleDateFormat) { // date and time
                String pattern = ((SimpleDateFormat) format).toPattern();
                if (!isTimeType(pattern)) {
                    setPatternComboBoxAndList(FormatContents.DATE, pattern);
                } else {
                    setPatternComboBoxAndList(FormatContents.TIME, pattern);
                }
                patternTextField.setText(pattern);
            } else if (format instanceof TextFormat) { // Text
                this.typeComboBox.setSelectedItem(FormatContents.TEXT);
            }
        }
    }


    /**
     * 判断是否是数组有模式
     *
     * @param stringArray 字符串数组
     * @param pattern   格式
     * @return  是否是数组有模式
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
        int i = isArrayContainPattern(FormatField.getInstance().getFormatArray(formatStyle, false), pattern);
        if (i == -1) {
            this.patternList.setSelectedIndices(ArrayUtils.EMPTY_INT_ARRAY);
        } else {
            this.patternList.setSelectedIndex(i);
        }
    }

    private boolean isTimeType(String pattern) {
        return pattern.matches(".*[Hhmsa].*");
    }

    /**
     * update
     */
    public Format update() {
        String patternString = patternTextField.getText();
        if (getFormatContents() == FormatContents.TEXT) {
            return FormatField.getInstance().getFormat(getFormatContents(), patternString);
        }
        if (isRightFormate) {
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
            isRightFormate = true;
            if (StringUtils.isEmpty(patternTextField.getText())) {
                return;
            }
            this.sampleLabel.setText(FormatField.getInstance().getFormatValue(getFormatContents(), patternTextField.getText()));
        } catch (Exception e) {
            this.sampleLabel.setForeground(Color.red);
            this.sampleLabel.setText(e.getMessage());
            isRightFormate = false;
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
            int contents = getFormatContents();
            CardLayout cardLayout = (CardLayout) centerPane.getLayout();
            if (isTextOrNull()) {
                cardLayout.show(centerPane, "hide");
                patternTextField.setText("");
            } else {
                cardLayout.show(centerPane, "show");
            }

            String[] patternArray = FormatField.getInstance().getFormatArray(contents, false);
            //
            DefaultListModel patternModel = (DefaultListModel) patternList.getModel();
            patternModel.removeAllElements();

            for (int i = 0; i < patternArray.length; i++) {
                patternModel.addElement(patternArray[i]);
            }

            if (patternModel.size() > 0) {
                patternList.setSelectedIndex(0);
            }
        }

    };
    /**
     * text pattern document listener.
     */
    DocumentListener patternTextDocumentListener = new DocumentListener() {

        public void insertUpdate(DocumentEvent evt) {
            refreshPreviewLabel();
        }

        public void removeUpdate(DocumentEvent evt) {
            refreshPreviewLabel();
        }

        public void changedUpdate(DocumentEvent evt) {
            refreshPreviewLabel();
        }
    };
    /**
     * Pattern list selection listener.
     */
    ListSelectionListener patternListSelectionListener = new ListSelectionListener() {

        public void valueChanged(ListSelectionEvent evt) {
            if (!evt.getValueIsAdjusting()) {
                patternTextField.setText((String) patternList.getSelectedValue());
            }
        }
    };

    @Override
    /**
     * populate
     */
    public void populateBean(Style style) {
        this.populateBean(style.getFormat());
    }

    @Override
    /**
     * update
     */
    public Style update(Style style) {
        return style.deriveFormat(this.update());
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
        this.add(typeComboBox, BorderLayout.NORTH);
    }

    public void setComboBoxModel(boolean isDate) {
        if (this.isDate != isDate) {
            this.isDate = isDate;
            this.typeComboBox.setSelectedIndex(0);
            if (isDate) {
                for (int i = 0; i < DATETYPES.length; i++) {
                    this.typeComboBox.addItem(DATETYPES[i]);
                }
                for (int i = 0; i < TYPES.length; i++) {
                    this.typeComboBox.removeItemAt(1);
                }
            } else {
                for (int i = 0; i < TYPES.length; i++) {
                    this.typeComboBox.addItem(TYPES[i]);
                }
                for (int i = 0; i < DATETYPES.length; i++) {
                    this.typeComboBox.removeItemAt(1);
                }
            }
        }
    }
}