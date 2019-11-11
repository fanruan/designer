package com.fr.design.hyperlink;

import com.fr.base.BaseFormula;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itree.filetree.ReportletPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.js.ReportletHyperlink;
import com.fr.js.ReportletHyperlinkDialogAttr;
import com.fr.stable.CommonUtils;
import com.fr.stable.FormulaProvider;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 热点链接部分 上方 定义特征 样式 报表 等属性的界面.
 *
 * @author kunsnat
 */
public class ReportletHyperNorthPane extends AbstractHyperNorthPane<ReportletHyperlink> {
    /**
     * item name text filed
     */
    private UITextField itemNameTextField;
    /**
     * 是否展示item name
     */
    private boolean needRenamePane = false;
    /**
     * 参数路径输入框
     */
    private UITextField reportPathTextField;
    /**
     * 是否展示参数面板勾选框
     */
    private UICheckBox showParameterInterface;
    /**
     * 选择文件按钮
     */
    private UIButton browserButton;

    /**
     * 参数传递方式下拉选择框
     */
    private UIComboBox postComboBox;
    /**
     * 对话框标题输入框
     */
    private TinyFormulaPane titleFiled;


    /**
     * 对话框居中按钮
     */
    private UIRadioButton center;
    /**
     * 对话框位置自定义按钮
     */
    private UIRadioButton custom;
    /**
     * 距左
     */
    private UINumberField leftLocation;
    /**
     * 距上
     */
    private UINumberField topLocation;


    public ReportletHyperNorthPane(boolean needRenamePane) {
        this.needRenamePane = needRenamePane;
    }

    public ReportletHyperNorthPane() {
    }

    /**
     * 初始化面板
     */
    @Override
    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final List<Component[]> dialogComponents = new ArrayList<>();
        final List<Component[]> othersComponents = new ArrayList<>();

        initRenamePanel(dialogComponents, othersComponents);
        initHeaderPanel(dialogComponents, othersComponents);
        initTargetComboBoxPanel(dialogComponents, othersComponents);

        initTitlePanel(dialogComponents);
        initDialogSizePanel(dialogComponents);
        initDialogLocationPanel(dialogComponents);

        initFooterPanel(dialogComponents, othersComponents);

        initPlaceHolder(othersComponents);

        bindListener(dialogComponents, othersComponents);

        // 创建内容面板
        JPanel content = TableLayoutHelper.createTableLayoutPane(dialogComponents.toArray(new Component[dialogComponents.size()][]), TableLayoutHelper.FILL_LASTCOL_AND_ROW);
        this.add(content, BorderLayout.CENTER);
    }


    /**
     * 生成最上方的配置面板
     *
     * @return JPanel
     */
    @Override
    protected JPanel setHeaderPanel() {

        JPanel reportletNamePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // 路径输入框
        reportPathTextField = new UITextField(20);
        reportletNamePane.add(reportPathTextField, BorderLayout.CENTER);

        // 选择路径按钮
        browserButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Select"));
        browserButton.setPreferredSize(new Dimension(browserButton.getPreferredSize().width, 20));
        reportletNamePane.add(browserButton, BorderLayout.EAST);
        browserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                final ReportletPane reportletPane = new ReportletPane();
                reportletPane.setSelectedReportletPath(reportPathTextField.getText());
                BasicDialog reportletDialog = reportletPane.showWindow(SwingUtilities.getWindowAncestor(ReportletHyperNorthPane.this));

                reportletDialog.addDialogActionListener(new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        reportPathTextField.setText(reportletPane.getSelectedReportletPath());
                    }
                });
                reportletDialog.setVisible(true);
            }
        });
        return reportletNamePane;
    }

    @Override
    protected String title4PopupWindow() {
        return "reportlet";
    }

    @Override
    protected void populateSubHyperlinkBean(ReportletHyperlink link) {
        if (itemNameTextField != null) {
            this.itemNameTextField.setText(link.getItemName());
        }
        this.reportPathTextField.setText(link.getReportletPath());
        this.showParameterInterface.setSelected(link.isShowParameterInterface());
        this.postComboBox.setSelectedIndex(link.isByPost() ? 1 : 0);

        ReportletHyperlinkDialogAttr attr = link.getAttr();
        titleFiled.populateBean(StringUtils.EMPTY);
        leftLocation.setText(StringUtils.EMPTY);
        topLocation.setText(StringUtils.EMPTY);
        center.setSelected(true);
        if (attr != null) {
            Object title = attr.getTitle();
            String titleContent;
            if (title instanceof FormulaProvider) {
                titleContent = ((FormulaProvider) title).getContent();
            } else {
                titleContent = title == null ? StringUtils.EMPTY : title.toString();
            }
            titleFiled.populateBean(titleContent);
            boolean isCenter = attr.isCenter();
            if (!isCenter) {
                int left = attr.getLeft(), top = attr.getTop();
                leftLocation.setText(Integer.toString(left));
                topLocation.setText(Integer.toString(top));
            }
            center.setSelected(isCenter);
            custom.setSelected(!isCenter);
        }
    }

    @Override
    protected ReportletHyperlink updateSubHyperlinkBean() {
        ReportletHyperlink reportletHyperlink = new ReportletHyperlink();
        updateSubHyperlinkBean(reportletHyperlink);
        return reportletHyperlink;
    }

    @Override
    protected void updateSubHyperlinkBean(ReportletHyperlink reportletHyperlink) {
        if (itemNameTextField != null) {
            reportletHyperlink.setItemName(this.itemNameTextField.getText());
        }
        reportletHyperlink.setReportletPath(this.reportPathTextField.getText());
        reportletHyperlink.setShowParameterInterface(this.showParameterInterface.isSelected());
        reportletHyperlink.setByPost(postComboBox.getSelectedIndex() == 1);

        ReportletHyperlinkDialogAttr attr = new ReportletHyperlinkDialogAttr();
        String title = titleFiled.updateBean();
        if (CommonUtils.maybeFormula(title)) {
            attr.setTitle(BaseFormula.createFormulaBuilder().build(titleFiled.updateBean()));
        } else {
            attr.setTitle(title);
        }
        attr.setCenter(center.isSelected());
        if (!attr.isCenter()) {
            attr.setLeft((int) leftLocation.getValue());
            attr.setTop((int) topLocation.getValue());
        }
        reportletHyperlink.setAttr(attr);

    }

    public String getReportletName() {
        String text = this.reportPathTextField.getText();
        return StringUtils.isBlank(text) ? StringUtils.EMPTY : text.substring(1);
    }

    /**
     * 获取按钮焦点
     */
    public void requestButtonFocus() {
        this.browserButton.requestFocus();
        JPopupMenu popup = new JPopupMenu();
        FakeTipAction tip = new FakeTipAction();
        tip.setEnabled(false);
        popup.add(tip);
        GUICoreUtils.showPopupCloseMenu(popup, this.browserButton);
    }

    private class FakeTipAction extends UpdateAction {
        public FakeTipAction() {
            this.setName(Toolkit.i18nText("Fine-Design_Basic_Template_Select"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // do nothing
        }
    }

    /**
     * 底部面板，参数传递方式
     *
     * @return JPanel
     */
    @Override
    protected JPanel setFootPanel() {
        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        // 参数传递方式下拉框
        postComboBox = new UIComboBox(new String[]{"GET", "POST"});
        postComboBox.setPreferredSize(new Dimension(60, 20));
        postComboBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        content.add(postComboBox);


        showParameterInterface = new UICheckBox(Toolkit.i18nText("Fine-Design_Basic_Parameter_UI_Display"));

        showParameterInterface.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        content.add(showParameterInterface);
        return content;
    }

    private void bindListener(final List<Component[]> dialogComponents, final List<Component[]> othersComponents) {
        final UIComboBox targetFrameComboBox = this.getTargetFrameComboBox();
        targetFrameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean show = DIALOG == targetFrameComboBox.getSelectedIndex();
                JPanel content;
                List<Component[]> cs = show ? dialogComponents : othersComponents;
                ReportletHyperNorthPane.this.removeAll();
                content = TableLayoutHelper.createTableLayoutPane(cs.toArray(new Component[cs.size()][]), TableLayoutHelper.FILL_LASTCOL_AND_ROW);
                ReportletHyperNorthPane.this.add(content, BorderLayout.CENTER);
                ReportletHyperNorthPane.this.revalidate();
                ReportletHyperNorthPane.this.repaint();
            }
        });
    }

    private void initPlaceHolder(List<Component[]> othersComponents) {
        JPanel empty1 = new JPanel();
        empty1.setPreferredSize(new Dimension(20, 20));
        JPanel empty2 = new JPanel();
        empty2.setPreferredSize(new Dimension(20, 20));
        JPanel empty3 = new JPanel();
        empty3.setPreferredSize(new Dimension(20, 23));

        othersComponents.add(new Component[]{empty1, new JPanel()});
        othersComponents.add(new Component[]{empty2, new JPanel()});
        othersComponents.add(new Component[]{empty3, new JPanel()});
    }

    private void initRenamePanel(List<Component[]> dialogComponents, List<Component[]> othersComponents) {
        // 是否有重命名属性
        if (this.needRenamePane) {
            itemNameTextField = new UITextField();
            Component[] renameComponents = new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Name") + ":"), itemNameTextField};
            dialogComponents.add(renameComponents);
            othersComponents.add(renameComponents);
        }
    }

    private void initHeaderPanel(List<Component[]> dialogComponents, List<Component[]> othersComponents) {
        //最上方位置的面板
        JPanel headerPane = this.setHeaderPanel();
        Component[] headerComponents = new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Reportlet") + ":"), headerPane};
        dialogComponents.add(headerComponents);
        othersComponents.add(headerComponents);
    }

    private void initTargetComboBoxPanel(List<Component[]> dialogComponents, List<Component[]> othersComponents) {
        // 链接打开于
        UIComboBox targetFrameComboBox = new UIComboBox(getTargetFrames());
        this.setTargetFrameComboBox(targetFrameComboBox);
        targetFrameComboBox.setEditable(true);
        targetFrameComboBox.setPreferredSize(new Dimension(100, 20));
        JPanel targetFramePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        targetFramePanel.add(targetFrameComboBox);
        Component[] targetComponents = new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Link_Opened_In") + ":"), targetFramePanel};
        dialogComponents.add(targetComponents);
        othersComponents.add(targetComponents);
    }

    private void initTitlePanel(List<Component[]> dialogComponents) {
        // 对话框标题
        titleFiled = new TinyFormulaPane();
        titleFiled.getUITextField().setColumns(15);
        final JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.add(titleFiled);
        Component[] titleComponents = new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Dialog_Title") + ":"), titlePanel};
        dialogComponents.add(titleComponents);
    }

    private void initDialogSizePanel(List<Component[]> dialogComponents) {// 对话框大小
        final JPanel sizeJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        UILabel heightLabel = new UILabel(Toolkit.i18nText("Fine-Design_Chart_Height") + ":");
        heightLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        sizeJPanel.add(heightLabel);
        UINumberField heightTextFiled = new UINumberField();
        heightTextFiled.setMinValue(0);
        heightTextFiled.canFillNegativeNumber(false);
        heightTextFiled.setText(String.valueOf(DEFAULT_H_VALUE));
        heightTextFiled.setPreferredSize(new Dimension(40, 20));
        sizeJPanel.add(heightTextFiled);
        this.setHeightTextFiled(heightTextFiled);
        UILabel widthLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Designer_Width") + ":");
        widthLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        sizeJPanel.add(widthLabel);
        UINumberField widthTextFiled = new UINumberField();
        widthTextFiled.setMinValue(0);
        widthTextFiled.canFillNegativeNumber(false);
        widthTextFiled.setText(String.valueOf(DEFAULT_V_VALUE));
        widthTextFiled.setPreferredSize(new Dimension(40, 20));
        sizeJPanel.add(widthTextFiled);
        this.setWidthTextFiled(widthTextFiled);
        sizeJPanel.setVisible(true);
        dialogComponents.add(new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Dialog_Size") + ":"), sizeJPanel});
    }

    private void initDialogLocationPanel(List<Component[]> dialogComponents) {
        // 显示位置
        final JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        // 居中
        center = new UIRadioButton(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Dialog_Position_Center"));
        // 自定义
        custom = new UIRadioButton(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Dialog_Position_Custom"));

        custom.setBorder(BorderFactory.createEmptyBorder(4, 20, 4, 5));
        ButtonGroup group = new ButtonGroup();
        group.setSelected(center.getModel(), true);
        group.add(center);
        group.add(custom);
        locationPanel.add(center);
        locationPanel.add(custom);

        // 位置 距左
        final UILabel leftLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Dialog_Position_Left"));
        leftLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
        leftLocation = new UINumberField();
        leftLocation.setMinValue(0);
        leftLocation.setPreferredSize(new Dimension(40, 20));
        // 位置 距上
        final UILabel topLabel = new UILabel(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Dialog_Position_Top"));
        topLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        topLocation = new UINumberField();
        topLocation.setMinValue(0);
        topLocation.setPreferredSize(new Dimension(40, 20));

        locationPanel.add(leftLabel);
        locationPanel.add(leftLocation);
        locationPanel.add(topLabel);
        locationPanel.add(topLocation);
        leftLabel.setVisible(false);
        leftLocation.setVisible(false);
        topLabel.setVisible(false);
        topLocation.setVisible(false);
        ChangeListener actionListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                boolean visible = custom.isSelected();
                leftLabel.setVisible(visible);
                leftLocation.setVisible(visible);
                topLabel.setVisible(visible);
                topLocation.setVisible(visible);
            }
        };
        // 默认居中
        center.setSelected(true);
        center.addChangeListener(actionListener);
        custom.addChangeListener(actionListener);

        dialogComponents.add(new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Dialog_Show_Position") + ":"), locationPanel});
    }

    private void initFooterPanel(List<Component[]> dialogComponents, List<Component[]> othersComponents) {
        // 最下方的配置面板
        // 参数传递方式
        Component[] footerComponents = new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Basic_Reportlet_Parameter_Type") + ":"), this.setFootPanel()};
        dialogComponents.add(footerComponents);
        othersComponents.add(footerComponents);
    }
}
