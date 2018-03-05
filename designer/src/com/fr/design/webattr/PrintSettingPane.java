package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.base.PaperSize;
import com.fr.base.Utils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.report.UnitFieldPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.print.NoClientPrintAttr;
import com.fr.print.PrintAttr;
import com.fr.print.nativeprint.core.NativePrintConfigManager;
import com.fr.report.stable.ReportConstants;
import com.fr.stable.Constants;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JList;
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
    private UIRadioButton noClientPrintRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_No_Client_Print"));
    private UIRadioButton nativePrintRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Native_Print"));
    private UIRadioButton topBottomRadioButton;
    private UIRadioButton leftRightRadioButton;
    private UICheckBox setMarginWhenPrintCheck;
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

        north.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

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

        setMarginWhenPrintCheck = new UICheckBox(Inter.getLocText("FR-Designer_Set_Margin_When_Printing"));
        setMarginWhenPrintCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        UILabel tipLabel = getTipLabel(Inter.getLocText("FR-Designer_Tip_Use_Default_Print_Margin"));
        JPanel northPane = GUICoreUtils.createFlowPane(new Component[] {
                setMarginWhenPrintCheck, tipLabel}, FlowLayout.LEFT);
        northPane.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 0));

        printPane.add(northPane, BorderLayout.NORTH);

        JPanel centerPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Default_Settings"));

        UICheckBox inheritPageMarginSettingCheck = getNoBorderCheckBox(Inter.getLocText("FR-Designer_Inherit_Page_Margin_Setting"));
        JPanel pageMarginSettingPane = getPageMarginSettingPane();
        pageMarginSettingPane.setBorder(BorderFactory.createEmptyBorder(10, -10, 0, 0));
        JPanel pageMarginCheckPane = getCheckboxAndDynamicPane(inheritPageMarginSettingCheck, pageMarginSettingPane);
        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {60, p};
        Component[][] components = {
                {getTopAlignLabelPane(Inter.getLocText("FR-Designer_Margin") + ":"), pageMarginCheckPane},
                {getTopAlignLabelPane(Inter.getLocText("FR-Designer_Order") + ":"), getPageOrderPane()}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 15);

        centerPane.add(panel);

        printPane.add(centerPane, BorderLayout.CENTER);

        return printPane;
    }

    private JPanel getNativePrintPane() {
        JPanel printPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel northPane = FRGUIPaneFactory.createNColumnGridInnerContainer_Pane(1, 0, 15);
        UILabel tipDownload = getTipLabel(Inter.getLocText("FR-Designer_Tip_Native_Print_Need_Client"));
        northPane.add(tipDownload);
        UICheckBox marginSettingCheck = new UICheckBox(Inter.getLocText("FR-Designer_Show_Print_Setting_Window_When_Printing"));
        marginSettingCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        UILabel tipCheck = getTipLabel(Inter.getLocText("FR-Designer_Tip_Use_Default_Settings"));
        JPanel checkPane =  GUICoreUtils.createFlowPane(new Component[] {
                marginSettingCheck, tipCheck}, FlowLayout.LEFT);
        northPane.add(checkPane);
        northPane.setBorder(BorderFactory.createEmptyBorder(3, 10, 10, 0));

        printPane.add(northPane, BorderLayout.NORTH);

        JPanel centerPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Default_Settings"));


//        JPanel centerContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        UIScrollPane scrollPane = new UIScrollPane(getNativePrintMainSettingPane());
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(600, 340));
//        centerContentPane.add(scrollPane, BorderLayout.CENTER);

//        centerContentPane.add(getNativePrintMainSettingPane(), BorderLayout.CENTER);
//        centerPane.add(centerContentPane);
        centerPane.add(scrollPane);

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

        UICheckBox inheritPagePaperSettingCheck = getNoBorderCheckBox(Inter.getLocText("FR-Designer_Inherit_Page_Paper_Setting"));
        JPanel paperSettingPane = getPaperSettingPane();
        JPanel paperSettingCheckPane = getCheckboxAndDynamicPane(inheritPagePaperSettingCheck, paperSettingPane);

        UICheckBox inheritPageLayoutSettingCheck = getNoBorderCheckBox(Inter.getLocText("FR-Designer_Inherit_Page_Layout_Setting"));
        JPanel layoutSettingPane = getLayoutSettingPane();
        JPanel layoutSettingCheckPane = getCheckboxAndDynamicPane(inheritPageLayoutSettingCheck, layoutSettingPane);

        UICheckBox zoomCheck = getNoBorderCheckBox(Inter.getLocText("FR-Designer_Print_To_Fit_Paper_Size"));

        // 页码标签
        UILabel printAreaLabel = new UILabel(Inter.getLocText("FR-Engine-Page_Number") + ":");
        JPanel printAreaLabelPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        printAreaLabelPane.add(printAreaLabel, BorderLayout.NORTH);
        printAreaLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // 边距
        UICheckBox inheritPageMarginSettingCheck = getNoBorderCheckBox(Inter.getLocText("FR-Designer_Inherit_Page_Margin_Setting"));
        JPanel pageMarginSettingPane = getPageMarginSettingPane();
        pageMarginSettingPane.setBorder(BorderFactory.createEmptyBorder(10, -10, 0, 0));
        JPanel pageMarginCheckPane = getCheckboxAndDynamicPane(inheritPageMarginSettingCheck, pageMarginSettingPane);

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p, p, p, p};
        double[] columnSize = {60, p};
        Component[][] components = {
                {new UILabel(Inter.getLocText("FR-Designer_Printer") + ":"), printerPane},
                {new UILabel(Inter.getLocText("FR-Designer_Copy_Number") + ":"), copyPane},
                {printAreaLabelPane, getPrintAreaPane()},
                {getTopAlignLabelPane(Inter.getLocText("FR-Designer_Paper") + ":"), paperSettingCheckPane},
                {getTopAlignLabelPane(Inter.getLocText("FR-Designer_Layout") + ":"), layoutSettingCheckPane},
                {getTopAlignLabelPane(Inter.getLocText("FR-Designer_Margin") + ":"), pageMarginCheckPane},
                {new UILabel(Inter.getLocText("FR-Designer_Scale_EnlargeOrReduce") + ":"), zoomCheck},
                {getTopAlignLabelPane(Inter.getLocText("FR-Designer_Order") + ":"), getPageOrderPane()}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 15);
        return panel;
    }

    private JPanel getLayoutSettingPane() {
        JPanel layoutSettingPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        layoutSettingPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        UIRadioButton portraitRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Portrait"));
        portraitRadioButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        UIRadioButton landscapeRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Landscape"));
        layoutSettingPane.add(portraitRadioButton);
        layoutSettingPane.add(landscapeRadioButton);

        ButtonGroup layoutButtonGroup = new ButtonGroup();
        layoutButtonGroup.add(portraitRadioButton);
        layoutButtonGroup.add(landscapeRadioButton);

        portraitRadioButton.setSelected(true);
        return layoutSettingPane;
    }

    private JPanel getPaperSettingPane() {
        UIComboBox predefinedComboBox = new UIComboBox();
        for (int i = 0; i < ReportConstants.PaperSizeNameSizeArray.length; i++) {
            Object[] tmpPaperSizeNameArray = ReportConstants.PaperSizeNameSizeArray[i];
            predefinedComboBox.addItem(tmpPaperSizeNameArray[1]);
        }
        predefinedComboBox.setRenderer(new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof PaperSize) {
                    PaperSize paperSize = (PaperSize) value;
                    for (int i = 0; i < ReportConstants.PaperSizeNameSizeArray.length; i++) {
                        Object[] tmpPaperSizeNameArray = ReportConstants.PaperSizeNameSizeArray[i];

                        if (ComparatorUtils.equals(paperSize, tmpPaperSizeNameArray[1])) {
                            String sbuf = tmpPaperSizeNameArray[0].toString() + " [" +
                                    Utils.convertNumberStringToString(paperSize.getWidth().toMMValue4Scale2()) +
                                    'x' +
                                    Utils.convertNumberStringToString(paperSize.getHeight().toMMValue4Scale2()) +
                                    ' ' +
                                    Inter.getLocText("PageSetup-mm") +
                                    ']';
                            this.setText(sbuf);
                            break;
                        }
                    }
                }

                return this;
            }
        });

        JPanel panel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        panel.add(predefinedComboBox);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        return panel;
    }

    // 页码范围
    private JPanel getPrintAreaPane() {
        UIRadioButton allPageRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_All_Pages"));
        UIRadioButton currentPageRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Current_Page"));
        UIRadioButton customPageRadioButton = new UIRadioButton(Inter.getLocText("FR-Engine_HJS-Specified_Pages"));
        ButtonGroup group = new ButtonGroup();
        group.add(allPageRadioButton);
        group.add(currentPageRadioButton);
        group.add(customPageRadioButton);
        allPageRadioButton.setSelected(true);

        UITextField areaField = new UITextField(20);
        UILabel areaFieldTip = getTipLabel(Inter.getLocText("FR-Designer_Print_Area_Tip"));

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

    private JPanel getCheckboxAndDynamicPane(UICheckBox checkBox, JPanel dynamicPane) {
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                dynamicPane.setVisible(e.getStateChange() == ItemEvent.DESELECTED);
            }
        });
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.add(checkBox, BorderLayout.NORTH);
        JPanel dynamicPaneWrapper = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        dynamicPaneWrapper.add(dynamicPane);
        panel.add(dynamicPaneWrapper, BorderLayout.CENTER);
        return panel;
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
        return Inter.getLocText("FR-Designer_Print_Setting");
    }

    public void populate(PrintAttr printAttr) {
        if (printAttr == null) {
            return;
        }

        if (printAttr.getPrintType() == PrintAttr.NO_CLIENT_PRINT) {
            noClientPrintRadioButton.setSelected(true);
        } else {
            nativePrintRadioButton.setSelected(true);
        }


        NoClientPrintAttr noClientPrintAttr = printAttr.getNoClientPrintAttr();
        setMarginWhenPrintCheck.setSelected(noClientPrintAttr.isSetMarginOnPrint());
    }

    public PrintAttr updateBean() {
        PrintAttr printAttr = new PrintAttr();

        printAttr.setPrintType(noClientPrintRadioButton.isSelected() ?
                PrintAttr.NO_CLIENT_PRINT : PrintAttr.NATIVE_PRINT);


        NoClientPrintAttr noClientPrintAttr = new NoClientPrintAttr();
//        NoClientPrintAttr noClientPrintAttr = reportWebAttr.getNoClientPrintAttr();
        noClientPrintAttr.setSetMarginOnPrint(setMarginWhenPrintCheck.isSelected());
        printAttr.setNoClientPrintAttr(noClientPrintAttr);


        return printAttr;
    }
}
