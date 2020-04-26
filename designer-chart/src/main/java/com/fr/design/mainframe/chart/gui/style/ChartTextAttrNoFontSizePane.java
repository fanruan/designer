package com.fr.design.mainframe.chart.gui.style;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

/**
 * 字体格式设置, 无字体大小设置.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-21 下午03:35:47
 */
public class ChartTextAttrNoFontSizePane extends ChartTextAttrPane {

    private static final long serialVersionUID = 4890526255627852602L;

    public ChartTextAttrNoFontSizePane() {
        super();
    }

    protected void initComponents() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        Component[] components1 = new Component[]{
                getFontColor(), getItalic(), getBold()
        };
        JPanel buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(GUICoreUtils.createFlowPane(components1, FlowLayout.LEFT, LayoutConstants.HGAP_LARGE), BorderLayout.CENTER);

        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{getFontNameComboBox()},
                new Component[]{buttonPane}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);

        populate(FRFont.getInstance());
    }
}