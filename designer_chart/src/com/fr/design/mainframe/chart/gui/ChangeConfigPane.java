package com.fr.design.mainframe.chart.gui;

/**
 * Created by hufan on 2016/10/20.
 */

import com.fr.chart.base.AttrChangeConfig;
import com.fr.chart.base.AttrChangeType;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 图表切换设置面板
 */
public class ChangeConfigPane extends BasicBeanPane<ChartCollection> {
    private static final int WIDTH = 100;
    private static final int MIN_TIME = 0;
    private static final int MAX_TIME = Integer.MAX_VALUE;
    private JPanel contentPane;
    //配置方式按钮
    private UIButtonGroup<Integer> configStyleButton;
    //配置界面
    private JPanel configPane;
    //按钮切换方式配置界面
    private JPanel buttonConfigPane;
    private ChartTextAttrPane styleAttrPane;
    private ColorSelectBoxWithOutTransparent colorSelectBox4button;

    //轮播切换方式配置接界面
    private JPanel carouselConfigPane;
    protected UISpinner timeInterval;
    protected UICheckBox arrowCheckbox;
    private ColorSelectBoxWithOutTransparent colorSelectBox4carousel;

    public ChangeConfigPane(){
        initButtonGroup();
        configPane = createConfigPane();
        contentPane = createContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        this.add(contentPane, BorderLayout.CENTER);
    }

    private JPanel createContentPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Change_Style")),configStyleButton},
                new Component[]{configPane, null},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel createConfigPane() {

        buttonConfigPane = createButtonConfigPane();
        carouselConfigPane = createCarouseConfigPane();

        JPanel panel = new JPanel(new CardLayout()){
            @Override
            public Dimension getPreferredSize() {
                if(configStyleButton.getSelectedIndex() == 0){
                    return buttonConfigPane.getPreferredSize();
                } else{
                    return carouselConfigPane.getPreferredSize();
                }
            }
        };

        panel.add(buttonConfigPane, "button");
        panel.add(carouselConfigPane, "carousel");

        panel.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));

        return panel;
    }

    private JPanel createCarouseConfigPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p};
        timeInterval = new UISpinner(MIN_TIME, MAX_TIME, 1, 0);
        colorSelectBox4carousel = new ColorSelectBoxWithOutTransparent(WIDTH);
        arrowCheckbox = new UICheckBox(Inter.getLocText("FR-Base_TurnOn"));
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Time_Interval")), timeInterval},
                new Component[]{new UILabel(Inter.getLocText("Background")),colorSelectBox4carousel},
                new Component[] {new UILabel(Inter.getLocText("Plugin-ChartF_Arrow_Style")), arrowCheckbox}
        };

        return TableLayout4VanChartHelper.createGapTableLayoutPane(components,rowSize,columnSize);
    }

    private JPanel createTitleStylePane(){
        styleAttrPane = new ChartTextAttrPane(){
            protected Component[][] getComponents(JPanel buttonPane) {
                return new Component[][]{
                        new Component[]{fontNameComboBox, null},
                        new Component[]{buttonPane, null}
                };
            }
        };
        styleAttrPane.setPreferredSize(new Dimension(WIDTH, (int) styleAttrPane.getPreferredSize().getHeight()));
        return TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-Chart_Character"), styleAttrPane);
    }


    private JPanel createButtonBackgroundColorPane(){
        colorSelectBox4button = new ColorSelectBoxWithOutTransparent(WIDTH);
        return TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Background"), colorSelectBox4button);
    }

    private JPanel createButtonConfigPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{createTitleStylePane(),null},
                new Component[]{createButtonBackgroundColorPane(),null},
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    private void initButtonGroup() {
        configStyleButton = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Button_Style"),
                Inter.getLocText("Plugin-ChartF_Carousel_Style")});
        configStyleButton.setPreferredSize(new Dimension(WIDTH * 2, (int) configStyleButton.getPreferredSize().getHeight()));
        configStyleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCardPane();
            }
        });
    }

    private void checkCardPane() {
        CardLayout cardLayout = (CardLayout) configPane.getLayout();
        if (configStyleButton.getSelectedIndex() == 0) {
            cardLayout.show(configPane, "button");
        } else {
            cardLayout.show(configPane, "carousel");
        }
    }

    @Override
    public void populateBean(ChartCollection ob) {
        if (ob == null){
            return;
        }
        AttrChangeConfig changeConfigAttr = ob.getChangeConfigAttr();
        if (changeConfigAttr == null){
            return;
        }
        configStyleButton.setSelectedIndex(changeConfigAttr.getChangeType() == AttrChangeType.BUTTON ? 0 : 1);

        //按钮切换界面
        styleAttrPane.populate(changeConfigAttr.getStyleAttr());
        colorSelectBox4button.setSelectObject(changeConfigAttr.getButtonColor());

        //轮播切换界面
        timeInterval.setValue(changeConfigAttr.getTimeInterval());
        colorSelectBox4carousel.setSelectObject(changeConfigAttr.getCarouselColor());
        arrowCheckbox.setSelected(changeConfigAttr.getShowArrow());

        checkCardPane();

    }

    @Override
    public ChartCollection updateBean() {
        return null;
    }

    public void updateBean(ChartCollection ob) {
        if (ob == null){
            return;
        }
        AttrChangeConfig changeConfigAttr = ob.getChangeConfigAttr();
        if (changeConfigAttr == null){
            return;
        }
        changeConfigAttr.setChangeType(configStyleButton.getSelectedIndex() == 0 ? AttrChangeType.BUTTON : AttrChangeType.CAROUSEL);
        changeConfigAttr.setStyleAttr(styleAttrPane.update());
        changeConfigAttr.setButtonColor(colorSelectBox4button.getSelectObject());
        changeConfigAttr.setTimeInterval((int) timeInterval.getValue());
        changeConfigAttr.setCarouselColor(colorSelectBox4carousel.getSelectObject());
        changeConfigAttr.setShowArrow(arrowCheckbox.isSelected());
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Chart-Change_Config_Attributes");
    }
}
