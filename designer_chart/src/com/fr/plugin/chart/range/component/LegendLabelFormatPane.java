package com.fr.plugin.chart.range.component;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.style.FormatPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartHtmlLabelPaneWithOutWidthAndHeight;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.chart.base.LegendLabelFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LegendLabelFormatPane extends JPanel{
    private static final long serialVersionUID = 1614283200308877354L;

    private UIButtonGroup labelFormatStyle;
    private FormatPane labelFormat;
    private VanChartHtmlLabelPaneWithOutWidthAndHeight htmlLabelPane;
    private JPanel centerPane;

    public void setParentPane(VanChartStylePane parent) {
        htmlLabelPane.setParent(parent);
    }

    public LegendLabelFormatPane(){
        labelFormatStyle = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Common"),
                Inter.getLocText("Plugin-ChartF_Custom")});
        labelFormat = new FormatPane();
        htmlLabelPane = new VanChartHtmlLabelPaneWithOutWidthAndHeight();

        centerPane = new JPanel(new CardLayout()){
            @Override
            public Dimension getPreferredSize() {
                if(labelFormatStyle.getSelectedIndex() == 0){
                    return labelFormat.getPreferredSize();
                }else{
                    return htmlLabelPane.getPreferredSize();
                }
            }
        };
        centerPane.add(labelFormat,Inter.getLocText("Plugin-ChartF_Common"));
        centerPane.add(htmlLabelPane, Inter.getLocText("Plugin-ChartF_Custom"));

        JPanel contentPane = new JPanel(new BorderLayout(0, 4));
        contentPane.add(labelFormatStyle, BorderLayout.NORTH);
        contentPane.add(centerPane, BorderLayout.CENTER);

        labelFormatStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCardPane(false);
            }
        });
        labelFormatStyle.setSelectedIndex(0);

        JPanel panel =  TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Label_Format"), contentPane);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
    }

    private void checkCardPane(boolean populate) {
        if(centerPane != null && labelFormatStyle != null){
            CardLayout cardLayout = (CardLayout) centerPane.getLayout();
            if (labelFormatStyle.getSelectedIndex() == 1) {
                if(!populate) {
                    checkCustomLabelText();
                }
                cardLayout.show(centerPane,Inter.getLocText("Plugin-ChartF_Custom"));
            } else {
                cardLayout.show(centerPane, Inter.getLocText("Plugin-ChartF_Common"));
            }
        }
    }

    protected void checkCustomLabelText() {
    }

    protected void setCustomFormatterText(String text) {
        htmlLabelPane.setCustomFormatterText(text);
    }

    /*标签部分的更新和存储数据部分要重写*/
    public void populate(LegendLabelFormat legendLabelFormat){
        if (labelFormatStyle != null) {
            labelFormatStyle.setSelectedIndex(legendLabelFormat.isCommonValueFormat() ? 0 : 1);
        }
        if (labelFormat != null) {
            labelFormat.populateBean(legendLabelFormat.getFormat());
        }
        if (htmlLabelPane != null){
            htmlLabelPane.populate(legendLabelFormat.getHtmlLabel());
        }

        checkCardPane(true);
    }

    public void update(LegendLabelFormat legendLabelFormat){
        if (labelFormatStyle != null) {
            legendLabelFormat.setCommonValueFormat(labelFormatStyle.getSelectedIndex() == 0);
        }
        if (labelFormat != null) {
            legendLabelFormat.setFormat(labelFormat.update());
        }
        if (htmlLabelPane != null) {
            htmlLabelPane.update(legendLabelFormat.getHtmlLabel());
        }
    }
    /*标签部分的更新和存储数据部分要重写*/

}