package com.fr.design.webattr.printsettings;

import com.fr.base.PaperSize;
import com.fr.base.Utils;
import com.fr.base.print.NativePrintAttr;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.report.UnitFieldPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.report.stable.ReportConstants;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by plough on 2018/3/5.
 */
public abstract class AbstractNativePrintSettingPane extends JPanel {
    private static final int ODD_INDEX = 0;
    private static final int EVEN_INDEX = 1;
    private static final String CUSTOM_PAPERSIZE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Custom");
    private static final PaperSize DEFAULT_PAPERSIZE = PaperSize.PAPERSIZE_A4;

    // 公共组件
    private UICheckBox showDialogCheck;
    private UICheckBox needSelectSheetCheck;  // 打印需要指定 sheet
    private UIComboBox printerComboBox;
    private UIBasicSpinner copySpinner;  // 份数
    private UIRadioButton allPageRadioButton;
    private UIRadioButton currentPageRadioButton;
    private UIRadioButton customPageRadioButton;
    private UIRadioButton doublePrintRadioButton;
    private UIComboBox doublePrintComboBox;
    private UITextField specifiedAreaField;
    private UIComboBox predefinedPaperSizeComboBox;
    private UICheckBox inheritPagePaperSettingCheck;
    private UICheckBox inheritPageLayoutSettingCheck;
    private UICheckBox inheritPageMarginSettingCheck;
    private UICheckBox fitPaperSizeCheck;  // 缩放
    private UINumberField scalePercentField;  // 缩放百分比
    private UIRadioButton portraitRadioButton;
    private UIRadioButton landscapeRadioButton;
    private PageMarginSettingPane pageMarginSettingPane;
    private JPanel centerPane;
    private JPanel customPaperSizePane;
    private UnitFieldPane customWidthFieldPane;
    private UnitFieldPane customHeightFieldPane;

    AbstractNativePrintSettingPane() {
        initComponents();
        initListeners();
    }

    private void initComponents() {
        JPanel printPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel northPane = getHeaderPane();
        printPane.add(northPane, BorderLayout.NORTH);

        centerPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default_Settings"));
        centerPane.add(getNativePrintMainSettingPane());

        printPane.add(centerPane, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(printPane, BorderLayout.CENTER);
    }

    private JPanel getHeaderPane() {
        UILabel tipDownload = GUICoreUtils.createTipLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Tip_Native_Print_Need_Client"));

        // 打印时需要打印设置窗口
        showDialogCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Show_Print_Setting_Window_When_Printing"));
        showDialogCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        UILabel showDialogCheckTip = GUICoreUtils.createTipLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Tip_Use_Default_Settings"));
        JPanel showDialogCheckPane = GUICoreUtils.createFlowPane(new Component[]{
                showDialogCheck, showDialogCheckTip}, FlowLayout.LEFT);

        // 打印需要指定 sheet
        needSelectSheetCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Need_Select_Sheet_When_Printing"));
        needSelectSheetCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        UILabel needSelectSheetCheckTip = GUICoreUtils.createTipLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Tip_Invalid_In_Page_View"));
        JPanel needSelectSheetCheckPane = GUICoreUtils.createFlowPane(new Component[]{
                needSelectSheetCheck, needSelectSheetCheckTip}, FlowLayout.LEFT);

        return createHeaderPane(tipDownload, showDialogCheckPane, needSelectSheetCheckPane);
    }

    abstract JPanel createHeaderPane(Component... comps);

    private void initListeners() {
        allPageRadioButton.addItemListener(getPageRaidoListener());
        currentPageRadioButton.addItemListener(getPageRaidoListener());
        customPageRadioButton.addItemListener(getPageRaidoListener());
        doublePrintRadioButton.addItemListener(getPageRaidoListener());
        showDialogCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                checkEnabled();
            }
        });
        specifiedAreaField.addFocusListener(new FocusAdapter() {
            String lastValidText = StringUtils.EMPTY;

            @Override
            public void focusGained(FocusEvent e) {
                lastValidText = specifiedAreaField.getText();
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = specifiedAreaField.getText();
                Pattern r = Pattern.compile("^(\\d+-)?\\d+$");
                Matcher m = r.matcher(text);
                if (!m.matches()) {
                    specifiedAreaField.setText(lastValidText);
                }
                super.focusLost(e);
            }
        });
        predefinedPaperSizeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateCustomPaperSizeArea();
            }
        });
    }

    private ItemListener getPageRaidoListener() {
        return new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                specifiedAreaField.setEnabled(customPageRadioButton.isSelected());
                doublePrintComboBox.setEnabled(doublePrintRadioButton.isSelected());
            }
        };
    }

    private JPanel getNativePrintMainSettingPane() {
        // 打印机
        String[] printerArray = getAllPrinterNames();
        printerComboBox = new UIComboBox(printerArray);
        printerComboBox.setPreferredSize(new Dimension(200, printerComboBox.getPreferredSize().height));
        JPanel printerPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        printerPane.add(printerComboBox);

        // 份数
        copySpinner = new UIBasicSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        GUICoreUtils.setColumnForSpinner(copySpinner, 5);
        JPanel copyPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        copyPane.add(copySpinner);

        // 继承页面纸张设置
        inheritPagePaperSettingCheck = GUICoreUtils.createNoBorderCheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Inherit_Page_Paper_Setting"));
        JPanel paperSettingPane = getPaperSettingPane();
        JPanel paperSettingCheckPane = GUICoreUtils.createCheckboxAndDynamicPane(inheritPagePaperSettingCheck, paperSettingPane, true);

        // 继承页面布局设置
        inheritPageLayoutSettingCheck = GUICoreUtils.createNoBorderCheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Inherit_Page_Layout_Setting"));
        JPanel layoutSettingPane = getLayoutSettingPane();
        JPanel layoutSettingCheckPane = GUICoreUtils.createCheckboxAndDynamicPane(inheritPageLayoutSettingCheck, layoutSettingPane, true);

        // 页码标签
        UILabel printAreaLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Page_Number") + ":");
        JPanel printAreaLabelPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        printAreaLabelPane.add(printAreaLabel, BorderLayout.NORTH);
        printAreaLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // 边距
        inheritPageMarginSettingCheck = GUICoreUtils.createNoBorderCheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Inherit_Page_Margin_Setting"));
        pageMarginSettingPane = new PageMarginSettingPane();
        pageMarginSettingPane.setBorder(BorderFactory.createEmptyBorder(10, -10, 0, 0));
        JPanel pageMarginCheckPane = GUICoreUtils.createCheckboxAndDynamicPane(inheritPageMarginSettingCheck, pageMarginSettingPane, true);

        // 缩放
        fitPaperSizeCheck = GUICoreUtils.createNoBorderCheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Print_To_Fit_Paper_Size"));
        JPanel scalePane = getScalePane();
        scalePane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        JPanel scaleCheckPane = GUICoreUtils.createCheckboxAndDynamicPane(fitPaperSizeCheck, scalePane, true);

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p, p, p};
        double[] columnSize = {60, p};
        Component[][] components = {
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Printer") + ":"), printerPane},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Copy_Number") + ":"), copyPane},
                {printAreaLabelPane, getPrintAreaPane()},
                {getTopAlignLabelPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Paper") + ":"), paperSettingCheckPane},
                {getTopAlignLabelPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout") + ":"), layoutSettingCheckPane},
                {getTopAlignLabelPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Margin") + ":"), pageMarginCheckPane},
                {getTopAlignLabelPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Scale_EnlargeOrReduce") + ":"), scaleCheckPane},
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 15);
    }

    private String[] getAllPrinterNames() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(
                DocFlavor.INPUT_STREAM.AUTOSENSE, null);
        Set<String> allPrinterName = new HashSet<String>();

        allPrinterName.add(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_None"));

        for (int i = 0, len = printServices.length; i < len; i++) {
            allPrinterName.add(printServices[i].getName());
        }

        return allPrinterName.toArray(new String[allPrinterName.size()]);
    }

    private JPanel getPaperSettingPane() {
        predefinedPaperSizeComboBox = new UIComboBox();
        for (int i = 0; i < ReportConstants.PaperSizeNameSizeArray.length; i++) {
            Object[] tmpPaperSizeNameArray = ReportConstants.PaperSizeNameSizeArray[i];
            predefinedPaperSizeComboBox.addItem(tmpPaperSizeNameArray[1]);
        }
        predefinedPaperSizeComboBox.addItem(CUSTOM_PAPERSIZE);
        predefinedPaperSizeComboBox.setRenderer(new UIComboBoxRenderer() {
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
                                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_PageSetup_mm") +
                                    ']';
                            this.setText(" " + sbuf);
                            break;
                        }
                    }
                }

                return this;
            }
        });

        // 下拉框
        JPanel comboPanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        comboPanel.add(predefinedPaperSizeComboBox);
        comboPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        customPaperSizePane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_M_Pane();
        // 宽度设置
        JPanel customWidthPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        customWidthPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Designer_Width") + ":"));
        customWidthFieldPane = new UnitFieldPane(Constants.UNIT_MM);
        customWidthFieldPane.setUnitValue(DEFAULT_PAPERSIZE.getWidth());
        customWidthPane.add(customWidthFieldPane);
        // 高度设置
        JPanel customHeightPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        customHeightPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Height") + ":"));
        customHeightFieldPane = new UnitFieldPane(Constants.UNIT_MM);
        customHeightFieldPane.setUnitValue(DEFAULT_PAPERSIZE.getHeight());
        customHeightPane.add(customHeightFieldPane);

        customPaperSizePane.add(customWidthPane);
        customPaperSizePane.add(customHeightPane);
        customPaperSizePane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.add(comboPanel, BorderLayout.NORTH);
        panel.add(customPaperSizePane, BorderLayout.CENTER);

        return panel;
    }

    private void updateCustomPaperSizeArea() {
        boolean isCustom = ComparatorUtils.equals(predefinedPaperSizeComboBox.getSelectedItem(), CUSTOM_PAPERSIZE);
        customPaperSizePane.setVisible(isCustom);
    }

    private JPanel getLayoutSettingPane() {
        JPanel layoutSettingPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        layoutSettingPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        portraitRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_PageSetup_Portrait"));
        portraitRadioButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        landscapeRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_PageSetup_Landscape"));
        layoutSettingPane.add(portraitRadioButton);
        layoutSettingPane.add(landscapeRadioButton);

        ButtonGroup layoutButtonGroup = new ButtonGroup();
        layoutButtonGroup.add(portraitRadioButton);
        layoutButtonGroup.add(landscapeRadioButton);

        portraitRadioButton.setSelected(true);
        return layoutSettingPane;
    }

    // 页码范围
    private JPanel getPrintAreaPane() {
        allPageRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_All_Pages"));
        currentPageRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Current_Page"));
        customPageRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_HJS-Specified_Pages"));
        doublePrintRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Double_Side_Print"));
        ButtonGroup group = new ButtonGroup();
        group.add(allPageRadioButton);
        group.add(currentPageRadioButton);
        group.add(customPageRadioButton);
        group.add(doublePrintRadioButton);
        allPageRadioButton.setSelected(true);

        specifiedAreaField = new UITextField(20) {
            @Override
            public void setEnabled(boolean enabled) {
                // 如果未选中"指定页"，此输入框始终不可用
                if (enabled && !customPageRadioButton.isSelected()) {
                    return;
                }
                super.setEnabled(enabled);
            }
        };
        UILabel areaFieldTip = GUICoreUtils.createTipLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Print_Area_Tip"));

        doublePrintComboBox = new UIComboBox() {
            @Override
            public void setEnabled(boolean enabled) {
                // 如果未选中"双面打印"，此下拉框始终不可用
                if (enabled && !doublePrintRadioButton.isSelected()) {
                    return;
                }
                super.setEnabled(enabled);
            }
        };
        doublePrintComboBox.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_HF_Odd_Page"));
        doublePrintComboBox.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_HF_Even_Page"));

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p, p, p};
        Component[][] components = {
                {allPageRadioButton, null, null},
                {currentPageRadioButton, null, null},
                {customPageRadioButton, specifiedAreaField, areaFieldTip},
                {doublePrintRadioButton, doublePrintComboBox, new JPanel()}
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 8);
    }

    private JPanel getScalePane() {
        scalePercentField = new UINumberField(5);
        scalePercentField.setMaxIntegerLength(3);
        scalePercentField.setMaxDecimalLength(0);
        scalePercentField.setMaxValue(200);

        UILabel percent = new UILabel("%");

        // TableLayout
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, p};
        Component[][] components = {
                {scalePercentField, percent}
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 0);
    }

    // 返回包含一个标签的 panel，标签始终位于 panel 顶部
    JPanel getTopAlignLabelPane(String labelText) {
        JPanel labelPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        labelPane.add(new UILabel(labelText), BorderLayout.NORTH);
        labelPane.add(new JPanel(), BorderLayout.CENTER);
        return labelPane;
    }

    public void populate(NativePrintAttr nativePrintAttr) {
        extraPopulate(nativePrintAttr);
        showDialogCheck.setSelected(nativePrintAttr.isShowDialog());
        needSelectSheetCheck.setSelected(nativePrintAttr.isNeedSelectSheet());
        printerComboBox.setSelectedItem(nativePrintAttr.getPrinterName());
        copySpinner.setValue(nativePrintAttr.getCopy());

        if (nativePrintAttr.getPageType().equals(NativePrintAttr.PageType.ALL_PAGES)) {
            allPageRadioButton.setSelected(true);
        } else if (nativePrintAttr.getPageType().equals(NativePrintAttr.PageType.CURRENT_PAGE)) {
            currentPageRadioButton.setSelected(true);
        } else if (nativePrintAttr.getPageType().equals(NativePrintAttr.PageType.SPECIFIED_PAGES)) {
            customPageRadioButton.setSelected(true);
            specifiedAreaField.setText(nativePrintAttr.getArea());
        } else {
            doublePrintRadioButton.setSelected(true);
            if (nativePrintAttr.getPageType().equals(NativePrintAttr.PageType.ODD_PAGES)) {
                doublePrintComboBox.setSelectedIndex(ODD_INDEX);
            } else {
                doublePrintComboBox.setSelectedIndex(EVEN_INDEX);
            }
        }
        specifiedAreaField.setEnabled(customPageRadioButton.isSelected());
        doublePrintComboBox.setEnabled(doublePrintRadioButton.isSelected());

        inheritPagePaperSettingCheck.setSelected(nativePrintAttr.isInheritPagePaperSetting());

        PaperSize paperSize = nativePrintAttr.getPaperSize();
        predefinedPaperSizeComboBox.setSelectedItem(paperSize);
        if (!ComparatorUtils.equals(predefinedPaperSizeComboBox.getSelectedItem(), paperSize)) {
            // 自定义尺寸
            predefinedPaperSizeComboBox.setSelectedItem(CUSTOM_PAPERSIZE);
            customWidthFieldPane.setUnitValue(paperSize.getWidth());
            customHeightFieldPane.setUnitValue(paperSize.getHeight());
        }
        updateCustomPaperSizeArea();

        inheritPageLayoutSettingCheck.setSelected(nativePrintAttr.isInheritPageLayoutSetting());
        if (nativePrintAttr.getOrientation() == ReportConstants.PORTRAIT) {
            portraitRadioButton.setSelected(true);
        } else {
            landscapeRadioButton.setSelected(true);
        }
        inheritPageMarginSettingCheck.setSelected(nativePrintAttr.isInheritPageMarginSetting());
        pageMarginSettingPane.populate(nativePrintAttr.getMargin());
        fitPaperSizeCheck.setSelected(nativePrintAttr.isFitPaperSize());
        scalePercentField.setValue(nativePrintAttr.getScalePercent());

        checkEnabled();
    }

    protected void extraPopulate(NativePrintAttr nativePrintAttr) {
        // do nothing
    }

    public void update(NativePrintAttr nativePrintAttr) {
        extraUpdate(nativePrintAttr);
        nativePrintAttr.setShowDialog(showDialogCheck.isSelected());
        nativePrintAttr.setNeedSelectSheet(needSelectSheetCheck.isSelected());
        nativePrintAttr.setPrinterName((String) printerComboBox.getSelectedItem());
        nativePrintAttr.setCopy((int) copySpinner.getValue());

        // 页码
        if (allPageRadioButton.isSelected()) {
            nativePrintAttr.setPageType(NativePrintAttr.PageType.ALL_PAGES);
        } else if (currentPageRadioButton.isSelected()) {
            nativePrintAttr.setPageType(NativePrintAttr.PageType.CURRENT_PAGE);
        } else if (customPageRadioButton.isSelected()) {
            nativePrintAttr.setPageType(NativePrintAttr.PageType.SPECIFIED_PAGES);
            nativePrintAttr.setArea(specifiedAreaField.getText());
        } else if (doublePrintComboBox.getSelectedIndex() == ODD_INDEX) {
            nativePrintAttr.setPageType(NativePrintAttr.PageType.ODD_PAGES);
        } else {
            nativePrintAttr.setPageType(NativePrintAttr.PageType.EVEN_PAGES);
        }

        nativePrintAttr.setInheritPagePaperSetting(inheritPagePaperSettingCheck.isSelected());

        PaperSize newPaperSize;
        if (ComparatorUtils.equals(predefinedPaperSizeComboBox.getSelectedItem(), CUSTOM_PAPERSIZE)) {
            newPaperSize = new PaperSize(customWidthFieldPane.getUnitValue(), customHeightFieldPane.getUnitValue());
        } else {
            newPaperSize = (PaperSize) predefinedPaperSizeComboBox.getSelectedItem();
        }
        nativePrintAttr.setPaperSize(newPaperSize);

        nativePrintAttr.setInheritPageLayoutSetting(inheritPageLayoutSettingCheck.isSelected());
        nativePrintAttr.setOrientation(portraitRadioButton.isSelected() ?
                ReportConstants.PORTRAIT : ReportConstants.LANDSCAPE);
        nativePrintAttr.setInheritPageMarginSetting(inheritPageMarginSettingCheck.isSelected());
        nativePrintAttr.setMargin(pageMarginSettingPane.updateBean());
        nativePrintAttr.setFitPaperSize(fitPaperSizeCheck.isSelected());
        nativePrintAttr.setScalePercent((int) scalePercentField.getValue());
    }

    protected void extraUpdate(NativePrintAttr nativePrintAttr) {
        // do nothing
    }

    // 刷新面板可用状态
    public void checkEnabled() {
        GUICoreUtils.setEnabled(centerPane, !showDialogCheck.isSelected());
    }
}
