package com.fr.design.mainframe.chart.gui;

/**
 * Created by hufan on 2016/10/20.
 */

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.general.Inter;
import com.fr.third.org.hsqldb.lib.Collection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 图表切换设置面板
 */
public class ChangeConfigPane extends BasicBeanPane<Collection> {
    private JPanel contentPane;
    //配置方式按钮
    private UIButtonGroup<Integer> configStyleButton;
    //配置界面
    private JPanel configPane;
    //按钮切换方式配置界面
    private JPanel buttonConfigPane;
    private ChartTextAttrPane textAttrPane;
    private ColorSelectBoxWithOutTransparent colorSelectBox;
    //轮播切换方式配置接界面
    private JPanel carouselConfigPane;

    public ChangeConfigPane(){
        initButtonGroup();
        contentPane = createContentPane();
        configPane = createConfigPane();
        this.add(contentPane, BorderLayout.CENTER);
    }

    private JPanel createContentPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Change_Style") + ":"),configStyleButton},
                new Component[]{new JSeparator(), null},
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
                    return new Dimension(buttonConfigPane.getWidth(), 0);
                } else{
                    return carouselConfigPane.getPreferredSize();
                }
            }
        };

        panel.add(buttonConfigPane, "button");
        panel.add(carouselConfigPane, "carousel");

        return panel;
    }

    private JPanel createCarouseConfigPane() {
        return new JPanel();
    }

    private JPanel createTitleStylePane(){
        textAttrPane = new ChartTextAttrPane();
        return TableLayoutHelper.createTableLayoutPaneWithTitle(Inter.getLocText("FR-Designer-Widget_Style"), textAttrPane);
    }


    private JPanel createBackgroundColorPane(){
        colorSelectBox = new ColorSelectBoxWithOutTransparent(100);
        return TableLayoutHelper.createTableLayoutPaneWithTitle(Inter.getLocText("FR-Designer-Widget_Style"), textAttrPane);
    }

    private JPanel createButtonConfigPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createTitleStylePane(),null},
                new Component[]{new JSeparator(),null},
                new Component[]{createBackgroundColorPane(),null},
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    private void initButtonGroup() {
        configStyleButton = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Button_Style"),
                Inter.getLocText("Plugin-ChartF_Carousel_Style")});
        configStyleButton.setSelectedIndex(0);
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
    public void populateBean(Collection ob) {

    }

    @Override
    public Collection updateBean() {
        return null;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
