package com.fr.design.chartx.component.combobox;

import com.fr.base.ChartColorMatching;
import com.fr.base.ChartPreStyleConfig;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.i18n.Toolkit;
import com.fr.general.GeneralUtils;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Rectangle2D;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2020-03-05
 * 一个带颜色展示的配色选择下拉框
 */
public class ColorSchemeComboBox extends UIComboBox {

    private Map<String, ColorInfo> colorSchemes;

    public ColorSchemeComboBox() {
        this(null);
    }

    public ColorSchemeComboBox(Map<String, ColorInfo> colorSchemes) {
        //通过配色方案的集合初始化下拉控件，如果参数为null，从配置中读取配色方案。
        if (colorSchemes == null) {
            colorSchemes = getColorSchemesFromConfig();
        }
        this.colorSchemes = colorSchemes;

        this.setModel(new DefaultComboBoxModel(colorSchemes.keySet().toArray()));

        this.setRenderer(new ColorSchemeCellRenderer());
    }

    private Map<String, ColorInfo> getColorSchemesFromConfig() {
        Map<String, ColorInfo> colorSchemes = new LinkedHashMap<>();
        ChartPreStyleConfig config = ChartPreStyleConfig.getInstance();

        //所有的样式名称
        Iterator names = config.names();

        //添加默认的方案和第一个方案
        String defaultName = config.getCurrentStyle();
        ChartColorMatching defaultStyle = (ChartColorMatching) config.getPreStyle(defaultName);
        Object firstName = names.next();
        ChartColorMatching firstStyle = (ChartColorMatching) config.getPreStyle(firstName);
        if (defaultStyle == null) {
            defaultStyle = firstStyle;
        }
        colorSchemes.put(Toolkit.i18nText("Fine-Design_Report_Default"), colorMatchingToColorInfo(defaultStyle));
        colorSchemes.put(firstStyle.getId(), colorMatchingToColorInfo(firstStyle));

        //添加其他的配色方案
        while (names.hasNext()) {
            Object key = names.next();
            ChartColorMatching colorMatching = (ChartColorMatching) config.getPreStyle(key);
            colorSchemes.put(colorMatching.getId(), colorMatchingToColorInfo(colorMatching));
        }

        //添加自定义组合色和自定义渐变色
        colorSchemes.put(Toolkit.i18nText("Fine-Design_Chart_Custom_Combination_Color"), null);
        colorSchemes.put(Toolkit.i18nText("Fine-Design_Chart_Custom_Gradient"), null);

        return colorSchemes;
    }

    public ColorInfo getSelectColorInfo() {
        String selectedItem = (String) getSelectedItem();
        return colorSchemes.get(selectedItem);
    }

    private ColorInfo colorMatchingToColorInfo(ChartColorMatching colorMatching) {
        ColorInfo colorInfo = new ColorInfo();
        colorInfo.setGradient(colorMatching.getGradient());
        colorInfo.setColors(colorMatching.getColorList());
        return colorInfo;
    }

    public SelectType getSelectType() {
        int selectedIndex = this.getSelectedIndex();
        int itemCount = this.getItemCount();
        if (selectedIndex == itemCount - 1) {
            return SelectType.GRADATION_COLOR;
        }
        if (selectedIndex == itemCount - 2) {
            return SelectType.COMBINATION_COLOR;
        }
        if (selectedIndex == 0) {
            return SelectType.DEFAULT;
        }
        return SelectType.NORMAL;
    }

    public void setSelectType(SelectType selectType) {
        int itemCount = this.getItemCount();
        switch (selectType) {
            case DEFAULT:
                setSelectedIndex(0);
                break;
            case GRADATION_COLOR:
                setSelectedIndex(itemCount - 1);
                break;
            case COMBINATION_COLOR:
                setSelectedIndex(itemCount - 2);
                break;
        }
    }

    public enum SelectType {
        DEFAULT,
        COMBINATION_COLOR,
        GRADATION_COLOR,
        NORMAL
    }

    public Set<String> getItems() {
        return colorSchemes.keySet();
    }

    public class ColorInfo {

        private List<Color> colors;

        private boolean gradient;

        public List<Color> getColors() {
            return colors;
        }

        public void setColors(List<Color> colors) {
            this.colors = colors;
        }

        public boolean isGradient() {
            return gradient;
        }

        public void setGradient(boolean gradient) {
            this.gradient = gradient;
        }
    }

    /**
     * CellRenderer.
     */
    class ColorSchemeCellRenderer extends UIComboBoxRenderer {

        private String schemeName = Toolkit.i18nText("Fine-Design_Report_Default");

        //左边距
        private static final double X = 4d;

        //上边距
        private static final double Y = 4d;

        private static final String BLANK_SPACE = " ";

        private static final int HEIGHT = 20;

        private static final int MAX_COUNT = 5;

        private static final int BLANK = 1;

        @Override
        public Dimension getPreferredSize() {
            Dimension preferredSize = super.getPreferredSize();
            preferredSize.setSize(super.getPreferredSize().getWidth(), HEIGHT);
            return preferredSize;
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel comp = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            this.schemeName = GeneralUtils.objectToString(value);
            ColorInfo colorInfo = colorSchemes.get(schemeName);
            if (colorInfo == null) {
                comp.setText(BLANK_SPACE + schemeName);
            } else {
                FontMetrics fontMetrics = comp.getFontMetrics(comp.getFont());
                //宽度是5倍的高加上4倍的留白
                double width = (HEIGHT - 2 * Y) * MAX_COUNT + BLANK * (MAX_COUNT - 1);
                String fill = BLANK_SPACE;
                //图形和文字之间留的宽度大于3倍的X
                while (fontMetrics.stringWidth(fill) < width + 3 * X) {
                    fill += BLANK_SPACE;
                }
                comp.setText(fill + schemeName);
            }
            comp.setToolTipText(schemeName);
            return comp;
        }

        public void paint(Graphics g) {
            super.paint(g);

            Graphics2D g2d = (Graphics2D) g;

            ColorInfo colorInfo = colorSchemes.get(schemeName);
            if (colorInfo != null) {
                if (colorInfo.isGradient()) {
                    drawGradient(g2d, colorInfo.getColors());
                } else {
                    drawCombineColor(g2d, colorInfo.getColors());
                }
            }
        }

        private void drawGradient(Graphics2D g2d, List<Color> colors) {
            //上下留4px，宽度等于5倍高
            double height = HEIGHT - 2 * Y;
            double width = height * MAX_COUNT + BLANK * (MAX_COUNT - 1);
            LinearGradientPaint linearGradientPaint = new LinearGradientPaint((float) X, (float) Y, (float) (X + width), (float) Y, new float[]{0f, 1f}, colors.toArray(new Color[colors.size()]));
            g2d.setPaint(linearGradientPaint);
            Rectangle2D rec = new Rectangle2D.Double(X, Y, width, height);
            g2d.fill(rec);
        }

        private void drawCombineColor(Graphics2D g2d, List<Color> colors) {
            int size = Math.min(colors.size(), MAX_COUNT);
            double height = HEIGHT - 2 * Y;
            //加上1px留白
            double width = ((height + BLANK) * MAX_COUNT - size) / size;
            for (int i = 0; i < size; i++) {
                g2d.setPaint(colors.get(i));
                Rectangle2D rec = new Rectangle2D.Double(X + (width + BLANK) * i, Y, width, height);
                g2d.fill(rec);
            }
        }
    }
}
