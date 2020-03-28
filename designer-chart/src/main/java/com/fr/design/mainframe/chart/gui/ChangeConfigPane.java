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
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.i18n.Toolkit;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 图表切换设置面板
 */
public class ChangeConfigPane extends BasicBeanPane<ChartCollection> {
    private static final int WIDTH = 100;
    private static final int EDIT_AREA_WIDTH = 180;
    private static final int LABEL_WIDTH = 20;
    private static final int MIN_TIME = 0;
    private static final int MAX_TIME = Integer.MAX_VALUE;
    private static final int CONSTANT_TEN = 10;
    private static final int CONSTANT_THIRTY = 30;
    private static final int CONSTANT_ZERO = 0;
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
    private ColorSelectBoxWithOutTransparent colorSelectBox4carousel;
    private UIButtonGroup switchStyleGroup;


    public ChangeConfigPane(){
        initButtonGroup();
        configPane = createConfigPane();
        contentPane = createContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(CONSTANT_TEN, CONSTANT_THIRTY, CONSTANT_TEN, CONSTANT_THIRTY));
        this.add(contentPane, BorderLayout.CENTER);
    }

    private JPanel createContentPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Change_Style")),configStyleButton},
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
                if (configStyleButton.getSelectedIndex() == 0){
                    return buttonConfigPane.getPreferredSize();
                } else{
                    return carouselConfigPane.getPreferredSize();
                }
            }
        };

        panel.add(buttonConfigPane, "button");
        panel.add(carouselConfigPane, "carousel");

        panel.setBorder(BorderFactory.createEmptyBorder(CONSTANT_ZERO, CONSTANT_TEN, CONSTANT_ZERO, CONSTANT_ZERO));

        return panel;
    }

    private JPanel createCarouseConfigPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f, p};
        double[] rowSize = {p, p, p};
        timeInterval = new UISpinner(MIN_TIME, MAX_TIME, 1, 0);
        colorSelectBox4carousel = new ColorSelectBoxWithOutTransparent(WIDTH);
        switchStyleGroup = new UIButtonGroup(new String[]{Toolkit.i18nText("Fine-Design_Chart_Show"), Toolkit.i18nText("Fine-Design_Report_Hide")});

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Arrow_Style")), switchStyleGroup, null},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Time_Interval")), timeInterval, new UILabel(Toolkit.i18nText("Fine-Design_Chart_Time_Second"))},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Basic_Background")),colorSelectBox4carousel, null}
        };

        return TableLayout4VanChartHelper.createGapTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel createTitleStylePane() {
        final UILabel text = new UILabel(Toolkit.i18nText("Fine-Design_Chart_Character"), SwingConstants.LEFT);
        styleAttrPane = new ChartTextAttrPane() {
            protected JPanel getContentPane(JPanel buttonPane) {
                double p = TableLayout.PREFERRED;
                double f = TableLayout.FILL;
                double[] columnSize = {f, EDIT_AREA_WIDTH};
                double[] rowSize = {p, p};

                return TableLayout4VanChartHelper.createGapTableLayoutPane(getComponents(buttonPane), rowSize, columnSize);
            }

            protected Component[][] getComponents(JPanel buttonPane) {
                return new Component[][]{
                        new Component[]{text, getFontNameComboBox()},
                        new Component[]{null, buttonPane}
                };
            }
        };
        return styleAttrPane;
    }


    private JPanel createButtonBackgroundColorPane(){
        colorSelectBox4button = new ColorSelectBoxWithOutTransparent(WIDTH);
        return TableLayout4VanChartHelper.createGapTableLayoutPane(Toolkit.i18nText("Fine-Design_Basic_Background"), colorSelectBox4button, EDIT_AREA_WIDTH);
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

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private void initButtonGroup() {
        configStyleButton = new UIButtonGroup<Integer>(new String[]{Toolkit.i18nText("Fine-Design_Chart_Button_Style"),
                Toolkit.i18nText("Fine-Design_Chart_Carousel_Style")});
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
        switchStyleGroup.setSelectedIndex(changeConfigAttr.isShowArrow() ? 0 : 1);

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
        changeConfigAttr.setShowArrow(switchStyleGroup.getSelectedIndex() == 0);
    }

    @Override
    protected String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Change_Config_Attributes");
    }
}
