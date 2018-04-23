package com.fr.design.mainframe.chart.gui.style.title;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Title;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ChartBackgroundNoImagePane;
import com.fr.design.mainframe.chart.gui.style.ChartBorderPane;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by eason on 14-10-22.
 */
public class ChartTitlePaneNoFormula extends BasicScrollPane<Chart> {
    private static final long serialVersionUID = 5748881235830708722L;
    private UICheckBox isTitleVisable;

    private UITextField titleContent;
    private ChartTextAttrPane textAttrPane;
    private UIButtonGroup<Integer> alignmentPane;

    private JPanel chartDefaultAttrPane;
    private ChartBorderPane borderPane;
    private ChartBackgroundNoImagePane backgroundPane;

    private JPanel titlePane;

    private class ContentPane extends JPanel {
        private static final long serialVersionUID = -6455600016731592455L;

        public ContentPane() {
            initComponents();
        }

        private void initComponents() {
            isTitleVisable = new UICheckBox(Inter.getLocText("Chart_Title_Is_Visible"));
            titlePane = createTitlePane();

            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] columnSize = {f};
            double[] rowSize = {p, p};
            Component[][] components = new Component[][]{
                    new Component[]{isTitleVisable},
                    new Component[]{titlePane}
            } ;

            JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
            this.setLayout(new BorderLayout());
            this.add(panel,BorderLayout.CENTER);

            isTitleVisable.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkTitlePaneUse();
                }
            });
        }
    }

    private JPanel createTitlePane(){
        borderPane = new ChartBorderPane();
        backgroundPane = new ChartBackgroundNoImagePane();
        chartDefaultAttrPane =  createDefaultAttrPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{chartDefaultAttrPane,null},
                new Component[]{new JSeparator(),null},
                new Component[]{borderPane,null},
                new Component[]{backgroundPane,null}
        } ;

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    private JPanel createDefaultAttrPane(){
        titleContent = new UITextField();
        textAttrPane = new ChartTextAttrPane();
        Icon[] alignmentIconArray = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")};
        Integer[] alignment = new Integer[]{Constants.LEFT, Constants.CENTER, Constants.RIGHT};
        alignmentPane = new UIButtonGroup<Integer>(alignmentIconArray, alignment);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {LayoutConstants.CHART_ATTR_TOMARGIN, p, f};
        double[] rowSize = {p, p,p};
        Component[][] components = new Component[][]{
                new Component[]{null,titleContent,null},
                new Component[]{null,textAttrPane,null},
                new Component[]{null,new BoldFontTextLabel(Inter.getLocText("Alignment-Style") + ":"),alignmentPane} ,
        } ;

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    // 检查标题界面是否可用.
    private void checkTitlePaneUse() {
        isTitleVisable.setVisible(true);
        isTitleVisable.setEnabled(true);
        titlePane.setVisible(isTitleVisable.isSelected());
        this.repaint();
    }

    /**
     * 弹出框的界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_TITLE_TITLE;
    }

    @Override
    protected JPanel createContentPane() {
        return new ContentPane();
    }

    @Override
    public void populateBean(Chart chart) {
        Title title = chart.getTitle();
        if (title == null) {
            return;
        }
        isTitleVisable.setSelected(title.isTitleVisible());

        titleContent.setText(Utils.objectToString(title.getTextObject()));

        alignmentPane.setSelectedItem(title.getPosition());
        TextAttr textAttr = title.getTextAttr();
        if (textAttr == null) {
            textAttr = new TextAttr();
        }
        textAttrPane.populate(textAttr);
        borderPane.populate(title);
        backgroundPane.populate(title);

        checkTitlePaneUse();
    }

    @Override
    public void updateBean(Chart chart) {
        if (chart == null) {
            chart = new Chart();
        }
        Title title = chart.getTitle();
        if (title == null) {
            title = new Title(StringUtils.EMPTY);
        }
        title.setTitleVisible(isTitleVisable.isSelected());
        String titleString = titleContent.getText();
        title.setTextObject(titleString);
        TextAttr textAttr = title.getTextAttr();
        if (textAttr == null) {
            textAttr = new TextAttr();
        }
        title.setPosition(alignmentPane.getSelectedItem());
        textAttrPane.update(textAttr);
        borderPane.update(title);
        backgroundPane.update(title);
    }
}