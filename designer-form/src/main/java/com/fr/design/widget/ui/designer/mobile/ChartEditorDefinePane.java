package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.mobile.ChartMobileAttrProvider;
import com.fr.base.mobile.ChartMobileFitAttrState;
import com.fr.base.mobile.ChartMobileFitAttrStateProvider;
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
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.mobile.ui.MobileCollapsedStylePane;
import com.fr.design.mainframe.mobile.ui.MobileComboBoxDialogEditor;
import com.fr.form.ui.BaseChartEditor;
import com.fr.form.ui.ChartEditor;
import com.fr.form.ui.mobile.MobileCollapsedStyle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by plough on 2018/1/18.
 */
public class ChartEditorDefinePane extends MobileWidgetDefinePane {
    private static final Item[] ITEMS = {
            new Item(ChartMobileFitAttrState.AUTO.description(), ChartMobileFitAttrState.AUTO),
            new Item(ChartMobileFitAttrState.AREA.description(), ChartMobileFitAttrState.AREA),
            new Item(ChartMobileFitAttrState.PROPORTION.description(), ChartMobileFitAttrState.PROPORTION)
    };

    private XCreator xCreator; // 当前选中控件的xCreator
    private FormDesigner designer; // 当前设计器
    private UIComboBox zoomOutComboBox;// 缩小逻辑下拉框
    private AttributeChangeListener changeListener;
    private UILabel tipLabel;
    private UICheckBox allowFullCheckBox;//允许全屏
    private UICheckBox functionalWhenUnactivatedCheckBox;//组件未激活时可使用组件内功能
    private MobileComboBoxDialogEditor mobileCollapsedStyleEditor;

    public ChartEditorDefinePane(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        JPanel mobileSettingsPane;
        if (isInAbsoluteLayout()) {
            mobileSettingsPane = getUnavailableTipPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Tip_Chart_Adaptivity_Unavailable_In_Absolute_Layout"));
        } else if (!FormDesignerUtils.isAppRelayout(designer)) {
            mobileSettingsPane = getUnavailableTipPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Tip_Chart_Adaptivity_Unavailable"));
        } else {
            mobileSettingsPane = getMobileSettingsPane();
        }
        this.add(mobileSettingsPane, BorderLayout.NORTH);
        this.repaint();
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

    private JPanel getUnavailableTipPane(String tipText) {
        JPanel panel = new JPanel(new BorderLayout());
        UILabel unavailableTipLabel = new UILabel();
        unavailableTipLabel.setText("<html>" + tipText + "<html>");
        unavailableTipLabel.setForeground(Color.gray);
        panel.add(unavailableTipLabel, BorderLayout.NORTH);
        allowFullCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Allow_Full_Screen"));
        panel.add(allowFullCheckBox,BorderLayout.CENTER);
        functionalWhenUnactivatedCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Functional_When_Unactivated"), true);
        panel.add(functionalWhenUnactivatedCheckBox, BorderLayout.SOUTH);
        return panel;
    }

    private UIExpandablePane getMobileSettingsPane() {
        initZoomOutComboBox();

        tipLabel = new UILabel();
        tipLabel.setForeground(Color.gray);
        updateTipLabel();
        allowFullCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Allow_Full_Screen"));
        functionalWhenUnactivatedCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Functional_When_Unactivated"), true);
        mobileCollapsedStyleEditor = new MobileComboBoxDialogEditor(new MobileCollapsedStylePane()) {
            @Override
            protected void firePropertyChanged() {
                ChartEditorDefinePane.this.update();
            }
        };

        Component[][] components = new Component[][]{
                new Component[] {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Zoom_In_Logic"), SwingConstants.LEFT), new UILabel(ChartMobileFitAttrState.PROPORTION.description())},
                new Component[] {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Zoom_Out_Logic"), SwingConstants.LEFT), zoomOutComboBox},
                new Component[] {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Collapse_Expand")), mobileCollapsedStyleEditor},
                new Component[] {tipLabel, null},
                new Component[] {allowFullCheckBox, null},
                new Component[] {functionalWhenUnactivatedCheckBox, null}
        };

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        final JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 30, LayoutConstants.VGAP_LARGE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);

        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Chart_Adaptivity"), 280, 20, panelWrapper);
    }

    private void initZoomOutComboBox() {
        this.zoomOutComboBox = new UIComboBox(ITEMS);
    }


    private void updateTipLabel() {
        ChartMobileFitAttrState fitAttrState = (ChartMobileFitAttrState) ((Item)zoomOutComboBox.getSelectedItem()).getValue();
        // 使用 html，可以自动换行
        tipLabel.setText("<html>" + fitAttrState.tip() + "</html>");
    }

    private void bindListeners2Widgets() {
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

        BaseChartEditor chartEditor = (BaseChartEditor)xCreator.toData();
        boolean allowFullScreen = chartEditor.getMobileAttr().isAllowFullScreen();
        this.allowFullCheckBox.setSelected(allowFullScreen);
        boolean isFunctionalWhenUnactivated = chartEditor.getMobileAttr().isFunctionalWhenUnactivated();
        this.functionalWhenUnactivatedCheckBox.setSelected(!isFunctionalWhenUnactivated);
        this.bindListeners2Widgets();
        this.addAttributeChangeListener(changeListener);

        if (!FormDesignerUtils.isAppRelayout(designer) || isInAbsoluteLayout()) {
            return;
        }

        ChartMobileFitAttrStateProvider zoomOutAttr = chartEditor.getMobileAttr().getZoomOutAttr();
        this.zoomOutComboBox.setSelectedItem(new Item(zoomOutAttr.description(), zoomOutAttr));
        updateTipLabel();

        this.zoomOutComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 只响应选中事件
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    return;
                }
                updateTipLabel();
                ChartMobileFitAttrState selectedAttr = (ChartMobileFitAttrState) ((Item) e.getItem()).getValue();
                if (selectedAttr.getState() != ChartMobileFitAttrState.AUTO.getState()) {

                }
            }
        });

        MobileCollapsedStyle style = ((ChartEditor) xCreator.toData()).getMobileCollapsedStyle();
        this.mobileCollapsedStyleEditor.setStyle(style);
        this.mobileCollapsedStyleEditor.setSelected(style.isCollapsedWork());
    }

    @Override
    public void update() {
        ChartMobileAttrProvider mobileAttr = ((BaseChartEditor)xCreator.toData()).getMobileAttr();
        if(zoomOutComboBox != null) {
            mobileAttr.setZoomInAttr(ChartMobileFitAttrState.PROPORTION);
            mobileAttr.setZoomOutAttr((ChartMobileFitAttrState) ((Item) zoomOutComboBox.getSelectedItem()).getValue());
            mobileAttr.setAllowFullScreen(allowFullCheckBox.isSelected());
            mobileAttr.setFunctionalWhenUnactivated(!functionalWhenUnactivatedCheckBox.isSelected());
        }else {
            mobileAttr.setAllowFullScreen(allowFullCheckBox.isSelected());
            mobileAttr.setFunctionalWhenUnactivated(!functionalWhenUnactivatedCheckBox.isSelected());
        }
        MobileCollapsedStyle style = this.mobileCollapsedStyleEditor.getStyle();
        if (style != null) {
            ((ChartEditor) xCreator.toData()).setMobileCollapsedStyle(style);
            style.setCollapsedWork(this.mobileCollapsedStyleEditor.isSelectedCustom());
        }
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified(); // 触发设计器保存按钮亮起来
    }
}
