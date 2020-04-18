package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.mobile.MobileFitAttrState;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.form.util.FormDesignerUtils;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.mobile.ui.MobileCollapsedStyleExpandPane;
import com.fr.design.mainframe.mobile.ui.MobileComboBoxDialogEditor;
import com.fr.form.ui.ElementCaseEditor;

import com.fr.form.ui.mobile.MobileCollapsedStyle;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.*;

/**
 * 报表块-移动端属性面板
 * <p>
 * Created by fanglei on 2017/8/8.
 */
public class ElementCaseDefinePane extends MobileWidgetDefinePane {
    private static final double MAX_HEIGHT_LIMIT = 0.8;
    private static final Item[] ITEMS = {
            new Item(MobileFitAttrState.HORIZONTAL.description(), MobileFitAttrState.HORIZONTAL),
            new Item(MobileFitAttrState.VERTICAL.description(), MobileFitAttrState.VERTICAL),
            new Item(MobileFitAttrState.BIDIRECTIONAL.description(), MobileFitAttrState.BIDIRECTIONAL),
            new Item(MobileFitAttrState.NONE.description(), MobileFitAttrState.NONE)
    };

    private XCreator xCreator; // 当前选中控件的xCreator
    private FormDesigner designer; // 当前设计器
    private UIComboBox hComboBox; // 横屏下拉框
    private UIComboBox vComboBox;// 竖屏下拉框
    private UICheckBox heightRestrictCheckBox; // 手机显示限制高度复选框
    private UILabel maxHeightLabel;
    private UISpinner maxHeightSpinner; // 最大高度Spinner
    private AttributeChangeListener changeListener;
    private UICheckBox allowFullCheckBox;
    private UICheckBox functionalWhenUnactivatedCheckBox;
    private MobileComboBoxDialogEditor mobileCollapsedStyleEditor;

    public ElementCaseDefinePane(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        this.hComboBox = new UIComboBox(ITEMS);
        this.vComboBox = new UIComboBox(ITEMS);
        this.heightRestrictCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Mobile_Height_Limit"));
        this.maxHeightLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Mobile_Height_Percent"), SwingConstants.LEFT);
        this.maxHeightSpinner = new UISpinner(0, MAX_HEIGHT_LIMIT, 0.01, 0.75) {
            public void setValue(double value) {
                String warningText = StringUtils.EMPTY;
                if (value > MAX_HEIGHT_LIMIT) {
                    warningText = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Mobile_Warning");
                } else if (value < 0) {
                    // 弹窗提示
                    warningText = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Max_Height_Cannot_Be_Negative");
                }
                if (StringUtils.isNotEmpty(warningText)) {
                    // 弹窗提示
                    JOptionPane.showMessageDialog(null,
                            warningText,
                            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tool_Tips"),
                            JOptionPane.PLAIN_MESSAGE);
                }
                super.setValue(value);
            }
        };
        maxHeightSpinner.setVisible(false);
        maxHeightLabel.setVisible(false);

        allowFullCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Allow_Full_Screen"));

        functionalWhenUnactivatedCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Functional_When_Unactivated"), true);

        mobileCollapsedStyleEditor = new MobileComboBoxDialogEditor(new MobileCollapsedStyleExpandPane()) {
            @Override
            protected void firePropertyChanged() {
                ElementCaseDefinePane.this.update();
            }
        };

        Component[][] components = new Component[][]{
                new Component[] {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Horizontal"), SwingConstants.LEFT), hComboBox},
                new Component[] {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Vertical"), SwingConstants.LEFT), vComboBox},
                createComponents(),
                new Component[] {allowFullCheckBox, null},
                new Component[] {functionalWhenUnactivatedCheckBox, null},
                new Component[] {heightRestrictCheckBox, null},
                new Component[] {maxHeightLabel, maxHeightSpinner}
        };
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        final JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 30, LayoutConstants.VGAP_LARGE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);
        UIExpandablePane folderPane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Fit"), 280, 20, panelWrapper);
        this.add(folderPane, BorderLayout.NORTH);
        this.bingListeners2Widgets();
        this.setGlobalNames();
        this.repaint();
    }

    private Component[] createComponents() {
        return FormDesignerUtils.isAppRelayout(designer) && !isInAbsoluteLayout() ?
                new Component[] {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Collapse_Expand")), mobileCollapsedStyleEditor} :
                new Component[0];
    }

    private boolean isInAbsoluteLayout() {
        Container parent = xCreator.getParent();
        while (parent != null) {
            if (parent instanceof XWAbsoluteLayout && !(parent instanceof XWAbsoluteBodyLayout)) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    private void bingListeners2Widgets() {
        reInitAllListeners();
        this.changeListener = new AttributeChangeListener() {
            @Override
            public void attributeChange() {
                update();
            }
        };
    }

    /**
     * 后台初始化所有事件.
     */
    private void reInitAllListeners() {
        initListener(this);
    }

    @Override
    public void populate(FormDesigner designer) {
        this.designer = designer;
        this.addAttributeChangeListener(changeListener);
        ElementCaseEditor elementCaseEditor = (ElementCaseEditor) xCreator.toData();
        this.hComboBox.setSelectedItem(new Item(elementCaseEditor.getHorziontalAttr().description(), elementCaseEditor.getHorziontalAttr()));
        this.vComboBox.setSelectedItem(new Item(elementCaseEditor.getVerticalAttr().description(), elementCaseEditor.getVerticalAttr()));
        this.heightRestrictCheckBox.setSelected(elementCaseEditor.isHeightRestrict());
        this.maxHeightLabel.setVisible(elementCaseEditor.isHeightRestrict());
        this.maxHeightSpinner.setVisible(elementCaseEditor.isHeightRestrict());
        this.maxHeightSpinner.setValue(elementCaseEditor.getHeightPercent());
        this.allowFullCheckBox.setSelected(elementCaseEditor.isAllowFullScreen());
        this.functionalWhenUnactivatedCheckBox.setSelected(!elementCaseEditor.isFunctionalWhenUnactivated());
        this.mobileCollapsedStyleEditor.setStyle(elementCaseEditor.getMobileCollapsedStyle());
        this.mobileCollapsedStyleEditor.setSelected(elementCaseEditor.getMobileCollapsedStyle().isCollapsedWork());
    }

    @Override
    public void update() {
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified(); // 触发设计器保存按钮亮起来
        String globalName = this.getGlobalName();
        switch (globalName) {
            case "hComboBox":
                ((ElementCaseEditor) xCreator.toData()).setHorziontalAttr(((MobileFitAttrState) ((Item) hComboBox.getSelectedItem()).getValue()));
                break;
            case "vComboBox":
                ((ElementCaseEditor) xCreator.toData()).setVerticalAttr(((MobileFitAttrState) ((Item) vComboBox.getSelectedItem()).getValue()));
                break;
            case "heightRestrictCheckBox":
                boolean isHeightRestrict = heightRestrictCheckBox.isSelected();
                ((ElementCaseEditor) xCreator.toData()).setHeightRestrict(isHeightRestrict);
                maxHeightSpinner.setVisible(isHeightRestrict);
                maxHeightLabel.setVisible(isHeightRestrict);
                break;
            case "maxHeightSpinner":
                ((ElementCaseEditor) xCreator.toData()).setHeightPercent(maxHeightSpinner.getValue());
                break;
            case "allowFullCheckBox":
                ((ElementCaseEditor) xCreator.toData()).setAllowFullScreen(allowFullCheckBox.isSelected());
                break;
            case "functionalWhenUnactivatedCheckBox":
                ((ElementCaseEditor) xCreator.toData()).setFunctionalWhenUnactivated(!functionalWhenUnactivatedCheckBox.isSelected());
        }
        MobileCollapsedStyle style =  this.mobileCollapsedStyleEditor.getStyle();
        if (style != null) {
            style.setCollapsedWork(this.mobileCollapsedStyleEditor.isSelectedCustom());
            ((ElementCaseEditor) xCreator.toData()).setMobileCollapsedStyle(style);
        }
    }

    private void setGlobalNames() {
        this.hComboBox.setGlobalName("hComboBox");
        this.vComboBox.setGlobalName("vComboBox");
        this.heightRestrictCheckBox.setGlobalName("heightRestrictCheckBox");
        this.maxHeightSpinner.setGlobalName("maxHeightSpinner");
        this.allowFullCheckBox.setGlobalName("allowFullCheckBox");
        this.functionalWhenUnactivatedCheckBox.setGlobalName("functionalWhenUnactivatedCheckBox");
    }

}
