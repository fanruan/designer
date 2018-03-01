package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by plough on 2018/3/1.
 */
public class PrintSettingPane extends BasicPane {
    private UIRadioButton noClientPrintRadioButton = new UIRadioButton("零客户端打印");
    private UIRadioButton localSoftwarePrintRadioButton = new UIRadioButton("本地软件打印");
    private UIRadioButton topBottomRadioButton;
    private UIRadioButton leftRightRadioButton;
    private CardLayout printCard;
    private JPanel printPane;

    public PrintSettingPane() {
        initComponents();
        initListener();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel allPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(allPanel, BorderLayout.CENTER);
        JPanel north = FRGUIPaneFactory.createVerticalFlowLayout_S_Pane(true);
        allPanel.add(north, BorderLayout.NORTH);
        ButtonGroup buttonGroup = new ButtonGroup();
        noClientPrintRadioButton.setSelected(true);
        buttonGroup.add(noClientPrintRadioButton);
        buttonGroup.add(localSoftwarePrintRadioButton);
        noClientPrintRadioButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
        JPanel radioGroupPane = GUICoreUtils.createFlowPane(new Component[] {
                noClientPrintRadioButton, localSoftwarePrintRadioButton}, FlowLayout.LEFT, 0, 0);
        north.add(radioGroupPane);

        printCard = new CardLayout();
        printPane = new JPanel();
        printPane.setLayout(printCard);
        printPane.add(noClientPrintRadioButton.getText(), getNoClientPrintPane());
        printPane.add(localSoftwarePrintRadioButton.getText(), getLocalSoftwarePrintPane());

//        north.add(printPane);
        north.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

//        JPanel center = FRGUIPaneFactory.createTitledBorderPane("默认配置");
        allPanel.add(printPane, BorderLayout.CENTER);
    }

    private void initListener() {
        noClientPrintRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    printCard.show(printPane, noClientPrintRadioButton.getText());
                }
            }
        });
        localSoftwarePrintRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    printCard.show(printPane, localSoftwarePrintRadioButton.getText());
                }
            }
        });
    }

    private JPanel getNoClientPrintPane() {
        JPanel printPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        UICheckBox marginSettingCheck = new UICheckBox("打印时可设置打印边距");
        marginSettingCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        UILabel tipLabel = getTipLabel("提示：若不勾选，则使用如下默认设置中的打印边距。");
        JPanel northPane = GUICoreUtils.createFlowPane(new Component[] {
                marginSettingCheck, tipLabel}, FlowLayout.LEFT);
        northPane.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 0));

        printPane.add(northPane, BorderLayout.NORTH);

        JPanel centerPane = FRGUIPaneFactory.createTitledBorderPane("默认配置");
        centerPane.add(getBasicSettingPane());

        printPane.add(centerPane, BorderLayout.CENTER);

        return printPane;
    }

    private JPanel getLocalSoftwarePrintPane() {
        JPanel printPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel northPane = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(1, 0, 15);
        UILabel tipDownload = getTipLabel("提示：本地软件打印需要终端下载客户端，但能支持更多、更强大的功能。");
        northPane.add(tipDownload);
        UICheckBox marginSettingCheck = new UICheckBox("打印时需要打印设置窗口");
        marginSettingCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        UILabel tipCheck = getTipLabel("提示：若不勾选，则使用如下默认设置。");
        JPanel checkPane =  GUICoreUtils.createFlowPane(new Component[] {
                marginSettingCheck, tipCheck}, FlowLayout.LEFT);
        northPane.add(checkPane);
        northPane.setBorder(BorderFactory.createEmptyBorder(3, 10, 10, 0));

        printPane.add(northPane, BorderLayout.NORTH);

        JPanel centerPane = FRGUIPaneFactory.createTitledBorderPane("默认配置");
        centerPane.add(getBasicSettingPane());

        printPane.add(centerPane, BorderLayout.CENTER);
        return printPane;
    }

    private UILabel getTipLabel(String tipText) {
        UILabel tipLabel = new UILabel(tipText);
        tipLabel.setForeground(Color.gray);
        return tipLabel;
    }

    // 边距 + 顺序
    private JPanel getBasicSettingPane() {
        UICheckBox inheritPageMarginSettingCheck = getNoBorderCheckBox("继承页面边距设置");
        JPanel orderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        orderPane.add(new UILabel("顺序："), BorderLayout.NORTH);

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {60, p};
        Component[][] components = {
                {new UILabel("边距："), inheritPageMarginSettingCheck},
                {orderPane, getPageOrderPane()}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 15);
        return panel;
    }

    // 生成没有边框的 UICheckBox
    private UICheckBox getNoBorderCheckBox(String text) {
        UICheckBox checkBox = new UICheckBox(text);
        checkBox.setBorder(BorderFactory.createEmptyBorder());
        return checkBox;
    }

    // 打印顺序
    private JPanel getPageOrderPane() {
        // page order
        JPanel pageOrderPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);

        Icon topBottomIcon = BaseUtils.readIcon("/com/fr/base/images/dialog/pagesetup/down.png");
        topBottomRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Top_to_bottom"));
        pageOrderPane.add(FRGUIPaneFactory.createIconRadio_S_Pane(topBottomIcon, topBottomRadioButton));

        Icon leftRightIcon = BaseUtils.readIcon("/com/fr/base/images/dialog/pagesetup/over.png");
        leftRightRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Left_to_right"));
        pageOrderPane.add(FRGUIPaneFactory.createIconRadio_S_Pane(leftRightIcon, leftRightRadioButton));

        ButtonGroup pageOrderButtonGroup = new ButtonGroup();
        pageOrderButtonGroup.add(topBottomRadioButton);
        pageOrderButtonGroup.add(leftRightRadioButton);

        topBottomRadioButton.setSelected(true);
        return pageOrderPane;
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("ReportServerP-Import_JavaScript");
    }

    public void populate(ReportWebAttr reportWebAttr) {

    }

    public void update(ReportWebAttr reportWebAttr) {

    }
}
