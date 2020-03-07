package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.chart.base.TextAttr;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

public class ChartTextAttrPane extends BasicPane {
    private static final long serialVersionUID = 6731679928019436869L;
    public static final int FONT_START = 6;
    public static final int FONT_END = 72;
    private UIComboBox fontNameComboBox;
    private UIComboBox fontSizeComboBox;
    private UIToggleButton bold;
    private UIToggleButton italic;
    private UIColorButton fontColor;
    public static Integer[] Font_Sizes = new Integer[FONT_END - FONT_START + 1];

    static {
        for (int i = FONT_START; i <= FONT_END; i++) {
            Font_Sizes[i - FONT_START] = i;
        }
    }

    public ChartTextAttrPane() {
        initState();
        initComponents();
    }

    public UIComboBox getFontNameComboBox() {
        return fontNameComboBox;
    }

    public UIComboBox getFontSizeComboBox() {
        return fontSizeComboBox;
    }

    public UIToggleButton getBold() {
        return bold;
    }

    public UIToggleButton getItalic() {
        return italic;
    }

    public UIColorButton getFontColor() {
        return fontColor;
    }

    public void setFontColor(UIColorButton fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * 标题
     *
     * @return 标题
     */
    public String title4PopupWindow() {
        // TODO Auto-generated method stub
        return null;
    }

    public void populate(TextAttr textAttr) {
        if (textAttr == null) {
            return;
        }
        FRFont frFont = textAttr.getFRFont();
        populate(frFont);
    }

    public void update(TextAttr textAttr) {
        if (textAttr == null) {
            textAttr = new TextAttr();
        }
        FRFont frFont = textAttr.getFRFont();
        frFont = updateFRFont();
        textAttr.setFRFont(frFont);
    }

    public TextAttr update() {
        TextAttr textAttr = new TextAttr();
        FRFont frFont = textAttr.getFRFont();
        frFont = updateFRFont();
        textAttr.setFRFont(frFont);
        return textAttr;
    }

    public void populate(FRFont frFont) {
        UIObserverListener listener = fontNameComboBox == null ? null : fontNameComboBox.getUiObserverListener();
        removeAllComboBoxListener();

        if (frFont != null) {
            fontNameComboBox.setSelectedItem(frFont.getFamily());
            bold.setSelected(frFont.isBold());
            italic.setSelected(frFont.isItalic());
            setFontSize(frFont);
            if (fontColor != null) {
                fontColor.setColor(frFont.getForeground());
            }
        }

        //更新结束后，注册监听器
        registerAllComboBoxListener(listener);
    }

    protected void setFontSize(FRFont frFont) {
        if (fontSizeComboBox != null) {
            fontSizeComboBox.setSelectedItem(frFont.getSize());
        }
    }

    private void removeAllComboBoxListener() {
        fontNameComboBox.removeChangeListener();
        fontSizeComboBox.removeChangeListener();
    }

    private void registerAllComboBoxListener(UIObserverListener listener) {
        fontNameComboBox.registerChangeListener(listener);
        fontSizeComboBox.registerChangeListener(listener);
    }

    /**
     * 更新字
     *
     * @return 更新字
     */
    public FRFont updateFRFont() {
        String name = Utils.objectToString(fontNameComboBox.getSelectedItem());

        return FRFont.getInstance(name, getFontStyle(), getFontSize(), fontColor.getColor());
    }

    protected int getFontStyle() {
        int style = Font.PLAIN;
        if (bold.isSelected() && !italic.isSelected()) {
            style = Font.BOLD;
        } else if (!bold.isSelected() && italic.isSelected()) {
            style = Font.ITALIC;
        } else if (bold.isSelected() && italic.isSelected()) {
            style = 3;
        }

        return style;
    }

    protected float getFontSize() {
        return Float.parseFloat(Utils.objectToString(fontSizeComboBox.getSelectedItem()));
    }

    public void setEnabled(boolean enabled) {
        this.fontNameComboBox.setEnabled(enabled);
        this.fontSizeComboBox.setEnabled(enabled);
        this.fontColor.setEnabled(enabled);
        this.bold.setEnabled(enabled);
        this.italic.setEnabled(enabled);
    }

    protected Object[] getFontSizeComboBoxModel() {
        return Font_Sizes;
    }

    protected void initState() {
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontSizeComboBox = new UIComboBox(getFontSizeComboBoxModel());
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
        initFontColorState();
    }

    protected void initFontColorState() {
        setFontColor(new UIColorButton());
    }

    protected void initComponents() {
        Component[] components1 = new Component[]{
                fontColor, italic, bold
        };
        JPanel buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(fontSizeComboBox, BorderLayout.CENTER);
        buttonPane.add(GUICoreUtils.createFlowPane(components1, FlowLayout.LEFT, LayoutConstants.HGAP_LARGE), BorderLayout.EAST);

        this.setLayout(new BorderLayout());
        this.add(getContentPane(buttonPane), BorderLayout.CENTER);

        populate(FRFont.getInstance());
    }

    protected JPanel getContentPane (JPanel buttonPane) {
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f,e};

        return TableLayout4VanChartHelper.createGapTableLayoutPane(getComponents(buttonPane), getRowSize(), columnSize);
    }

    protected double[] getRowSize () {
        double p = TableLayout.PREFERRED;
        return new double[]{p, p, p};
    }

    protected Component[][] getComponents(JPanel buttonPane) {
        UILabel text = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Character"), SwingConstants.LEFT);
        return new Component[][]{
                new Component[]{null, null},
                new Component[]{text, fontNameComboBox},
                new Component[]{null, buttonPane}
        };
    }
}