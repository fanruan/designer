package com.fr.van.chart.designer.component;

import com.fr.base.FRContext;
import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.i18n.Toolkit;
import com.fr.general.FRFont;
import com.fr.plugin.chart.type.LineType;
import com.fr.stable.Constants;

import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Created by shine on 2019/08/30.
 */
public class LineTypeComboBox extends UIComboBox {

    public LineTypeComboBox(LineType[] lineType) {
        super(lineType);

        this.setRenderer(new CellRenderer());
    }

    private static class CellRenderer extends UIComboBoxRenderer {

        private LineType lineType;

        public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            JLabel comp = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            this.lineType = (LineType) value;
            comp.setText(null);
            return comp;
        }

        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;

            Dimension d = getSize();
            g2d.setColor(getForeground());

            switch (this.lineType) {
                case NONE:
                    FRFont font = FRContext.getDefaultValues().getFRFont();
                    int resolution = ScreenResolution.getScreenResolution();
                    Font rfont = font.applyResolutionNP(resolution);
                    g2d.setFont(rfont);
                    FontMetrics fm = GraphHelper.getFontMetrics(rfont);
                    GraphHelper.drawString(g2d, Toolkit.i18nText("Fine-Design_Report_None"), 4, (d.height - fm.getHeight()) / 2 + fm.getAscent());
                    break;
                case NORMAL:
                    GraphHelper.drawLine(g2d, 4, d.height / 2, d.width - 8, d.height / 2);
                    break;
                case DASH:
                    GraphHelper.drawLine(g2d, 4, d.height / 2, d.width - 8, d.height / 2, Constants.LINE_DASH);
                    break;
                default:
                    break;

            }
        }

        public Dimension getPreferredSize() {
            return new Dimension(60, 16);
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

    }


}
