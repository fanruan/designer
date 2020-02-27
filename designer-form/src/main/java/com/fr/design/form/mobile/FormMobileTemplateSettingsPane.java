package com.fr.design.form.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.main.mobile.FormMobileAttr;


import javax.swing.*;
import java.awt.*;

/**
 * Created by plough on 2018/1/4.
 */
public class FormMobileTemplateSettingsPane extends BasicBeanPane<FormMobileAttr> {

    private UICheckBox mobileOnlyCheck;  // 设置为手机端专属模版
    private UICheckBox mobileCanvasSizeCheck;  // 设置为手机模版画布大小
    private UICheckBox adaptivePropertyAutoMatchCheck;  // 自适应属性自动匹配
    private JPanel mobileSettingsPane;

    public FormMobileTemplateSettingsPane() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel borderPane = FRGUIPaneFactory.createTitledBorderPane(this.title4PopupWindow());

        JPanel contentPane = new JPanel(FRGUIPaneFactory.createBorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, IntervalConstants.INTERVAL_L5, IntervalConstants.INTERVAL_L2, 0));

        mobileSettingsPane = new JPanel(FRGUIPaneFactory.createBorderLayout());
        mobileSettingsPane.setVisible(false);
        mobileSettingsPane.add(getMobileCanvasSizeCheckPane(), BorderLayout.NORTH);
        mobileSettingsPane.add(getAdaptivePropertyAutoMatchCheckPane(), BorderLayout.CENTER);


        mobileOnlyCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Set_Mobile_Only_Template"));
        mobileOnlyCheck.registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                boolean mobileOnlyCheckSelected = mobileOnlyCheck.isSelected();
                mobileSettingsPane.setVisible(mobileOnlyCheckSelected);
                adaptivePropertyAutoMatchCheck.setSelected(mobileOnlyCheckSelected);
            }
        });

        contentPane.add(mobileOnlyCheck, BorderLayout.NORTH);
        contentPane.add(mobileSettingsPane, BorderLayout.CENTER);

        borderPane.add(contentPane);
        this.add(borderPane);
    }

    private JPanel getMobileCanvasSizeCheckPane() {
        JPanel panel = new JPanel(FRGUIPaneFactory.createBorderLayout());
        mobileCanvasSizeCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set_Mobile_Canvas_Size"));
        // 默认勾选，不可取消
        mobileCanvasSizeCheck.setSelected(true);
        mobileCanvasSizeCheck.setEnabled(false);
        panel.add(mobileCanvasSizeCheck, BorderLayout.NORTH);
        panel.add(getCanvasDescPane(), BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, IntervalConstants.INTERVAL_L6, 0));
        return panel;
    }

    private JPanel getAdaptivePropertyAutoMatchCheckPane() {
        JPanel panel = new JPanel(FRGUIPaneFactory.createBorderLayout());
        adaptivePropertyAutoMatchCheck = new UICheckBox();
        adaptivePropertyAutoMatchCheck.registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                adaptivePropertyAutoMatchCheck.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Adaptive_Property_Auto_Match"));
            }
        });
        adaptivePropertyAutoMatchCheck.setSelected(true);
        panel.add(adaptivePropertyAutoMatchCheck, BorderLayout.NORTH);
        panel.add(getAdaptivePropertyAutoMatchDescPane(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getCanvasDescPane() {

        UILabel desc1 = createDescLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Canvas_Size_Lock_Desc"));
        UILabel desc2 = createDescLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Screen_Match_Desc"));
        UILabel desc3 = createDescLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Screen_Zoom_In_Desc"));
        UILabel desc4 = createDescLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Screen_Zoom_Out_Desc"));

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{desc1, null},
                new Component[]{desc2, null},
                new Component[]{desc3, null},
                new Component[]{desc4, null}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W0, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_W4, IntervalConstants.INTERVAL_L1, 0));
        return panel;
    }

    private JPanel getAdaptivePropertyAutoMatchDescPane() {

        UILabel desc1 = createDescLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Adaptive_Property_Auto_Match_Desc"));

        JPanel panel = new JPanel(FRGUIPaneFactory.createBorderLayout());
        panel.add(desc1, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_W4, IntervalConstants.INTERVAL_L1, 0));
        return panel;
    }

    private UILabel createDescLabel(String desc) {
        UILabel label = new UILabel(desc);
        label.setForeground(Color.gray);
        return label;
    }

    @Override
    public void populateBean(FormMobileAttr ob) {
        if (ob == null) {
            ob = new FormMobileAttr();
        }
//        this.mobileOnlyCheckPane.populateBean(ob.isRefresh());
        mobileOnlyCheck.setSelected(ob.isMobileOnly());
        adaptivePropertyAutoMatchCheck.setSelected(ob.isAdaptivePropertyAutoMatch());
    }

    @Override
    public FormMobileAttr updateBean() {
        return null;
    }

    @Override
    public void updateBean(FormMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            mobileAttr.setMobileOnly(mobileOnlyCheck.isSelected());
            mobileAttr.setAdaptivePropertyAutoMatch(adaptivePropertyAutoMatchCheck.isSelected());
        }
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Template_Settings");
    }

}
