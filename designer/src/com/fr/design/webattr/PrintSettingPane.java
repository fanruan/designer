package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.report.UnitFieldPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.print.nativeprint.core.NativePrintConfigManager;
import com.fr.stable.Constants;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by plough on 2018/3/1.
 */
public class PrintSettingPane extends BasicPane {
    private UIRadioButton noClientPrintRadioButton = new UIRadioButton("零客户端打印");
    private UIRadioButton nativePrintRadioButton = new UIRadioButton("本地软件打印");
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
        buttonGroup.add(nativePrintRadioButton);
        noClientPrintRadioButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
        JPanel radioGroupPane = GUICoreUtils.createFlowPane(new Component[] {
                noClientPrintRadioButton, nativePrintRadioButton}, FlowLayout.LEFT, 0, 0);
        north.add(radioGroupPane);

        printCard = new CardLayout();
        printPane = new JPanel();
        printPane.setLayout(printCard);
        printPane.add(noClientPrintRadioButton.getText(), getNoClientPrintPane());
        printPane.add(nativePrintRadioButton.getText(), getNativePrintPane());

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
        nativePrintRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    printCard.show(printPane, nativePrintRadioButton.getText());
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

    private JPanel getNativePrintPane() {
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


        JPanel centerContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerContentPane.add(getNativePrintMainSettingPane(), BorderLayout.CENTER);
        JPanel basicSettingPane = getBasicSettingPane();
        basicSettingPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        centerContentPane.add(basicSettingPane, BorderLayout.SOUTH);
        centerPane.add(centerContentPane);

        printPane.add(centerPane, BorderLayout.CENTER);
        return printPane;
    }

    private JPanel getNativePrintMainSettingPane() {

        // 打印机
        String[] printerArray = NativePrintConfigManager.getInstance().getAllPrinterNames();
        UIComboBox printerComboBox = new UIComboBox(printerArray);
        printerComboBox.setPreferredSize(new Dimension(200, printerComboBox.getPreferredSize().height));
        JPanel printerPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        printerPane.add(printerComboBox);

        // 份数
        UIBasicSpinner copySpinner = new UIBasicSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
        GUICoreUtils.setColumnForSpinner(copySpinner, 5);
        JPanel copyPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        copyPane.add(copySpinner);

        UICheckBox inheritPagePaperSettingCheck = getNoBorderCheckBox("继承页面纸张设置");
        UICheckBox inheritPageLayoutSettingCheck = getNoBorderCheckBox("继承页面布局设置");
        UICheckBox zoomCheck = getNoBorderCheckBox("根据纸张大小缩放打印");

        // 页码标签
        UILabel printAreaLabel = new UILabel("页码：");
        JPanel printAreaLabelPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        printAreaLabelPane.add(printAreaLabel, BorderLayout.NORTH);
        printAreaLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p, p};
        double[] columnSize = {60, p};
        Component[][] components = {
                {new UILabel("打印机："), printerPane},
                {new UILabel("份数："), copyPane},
                {printAreaLabelPane, getPrintAreaPane()},
                {new UILabel("纸张："), inheritPagePaperSettingCheck},
                {new UILabel("布局："), inheritPageLayoutSettingCheck},
                {new UILabel("缩放："), zoomCheck}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 15);
        return panel;
    }

    // 页码范围
    private JPanel getPrintAreaPane() {
        UIRadioButton allPageRadioButton = new UIRadioButton("所有页");
        UIRadioButton currentPageRadioButton = new UIRadioButton("当前页");
        UIRadioButton customPageRadioButton = new UIRadioButton("指定页");
        ButtonGroup group = new ButtonGroup();
        group.add(allPageRadioButton);
        group.add(currentPageRadioButton);
        group.add(customPageRadioButton);
        allPageRadioButton.setSelected(true);

        UITextField areaField = new UITextField(20);
        UILabel areaFieldTip = getTipLabel("格式(1, 4-9, 6, 8)");

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, p, p};
        Component[][] components = {
                {allPageRadioButton, null, null},
                {currentPageRadioButton, null, null},
                {customPageRadioButton, areaField, areaFieldTip}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 0);
        return panel;
    }

    private UILabel getTipLabel(String tipText) {
        UILabel tipLabel = new UILabel(tipText);
        tipLabel.setForeground(Color.gray);
        return tipLabel;
    }

    // 边距 + 顺序
    private JPanel getBasicSettingPane() {
        UICheckBox inheritPageMarginSettingCheck = getNoBorderCheckBox("继承页面边距设置");
        JPanel pageMarginCheckPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel pageMarginSettingPane = getPageMarginSettingPane();
        pageMarginSettingPane.setBorder(BorderFactory.createEmptyBorder(10, -10, 0, 0));
        pageMarginCheckPane.add(inheritPageMarginSettingCheck, BorderLayout.NORTH);
        pageMarginCheckPane.add(pageMarginSettingPane, BorderLayout.CENTER);
        initListenerForCheckboxAndDynamicPane(inheritPageMarginSettingCheck, pageMarginSettingPane);

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {60, p};
        Component[][] components = {
                {getTopAlignLabelPane("边距："), pageMarginCheckPane},
                {getTopAlignLabelPane("顺序："), getPageOrderPane()}
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 15);
    }

    private void initListenerForCheckboxAndDynamicPane(UICheckBox checkBox, JPanel dynamicPane) {
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                dynamicPane.setVisible(e.getStateChange() == ItemEvent.DESELECTED);
            }
        });
    }

    // 返回包含一个标签的 panel，标签始终位于 panel 顶部
    private JPanel getTopAlignLabelPane(String labelText) {
        JPanel labelPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        labelPane.add(new UILabel(labelText), BorderLayout.NORTH);
        return labelPane;
    }

    // 页边距设置面板
    private JPanel getPageMarginSettingPane() {
        JPanel marginPane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_M_Pane();
        // left
        JPanel marginLeftPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        marginPane.add(marginLeftPane);

        JPanel marginLeftTextPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        marginLeftPane.add(marginLeftTextPane);
        marginLeftTextPane.add(new UILabel(Inter.getLocText("Top") + ":"));
        UnitFieldPane marginTopUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
        marginLeftTextPane.add(marginTopUnitFieldPane);
        JPanel marginLeftUnitPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        marginLeftPane.add(marginLeftUnitPane);
        marginLeftUnitPane.add(new UILabel(Inter.getLocText("Bottom") + ":"));
        UnitFieldPane marginBottomUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
        marginLeftUnitPane.add(marginBottomUnitFieldPane);

        // right
        JPanel marginRightPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        marginPane.add(marginRightPane);

        // peter:这个一个垂直的上下的字符panel.
        JPanel marginRightTextPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        marginRightPane.add(marginRightTextPane);
        marginRightTextPane.add(new UILabel(Inter.getLocText("Left") + ":"));
        UnitFieldPane marginLeftUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
        marginRightTextPane.add(marginLeftUnitFieldPane);

        JPanel marginRightUnitPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        marginRightPane.add(marginRightUnitPane);
        marginRightUnitPane.add(new UILabel(Inter.getLocText("Right") + ":"));
        UnitFieldPane marginRightUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
        marginRightUnitPane.add(marginRightUnitFieldPane);

        return marginPane;
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
