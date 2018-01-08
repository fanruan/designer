package com.fr.design.report.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.report.mobile.ElementCaseMobileAttr;

import javax.swing.*;
import java.awt.*;

/**
 * Created by plough on 2018/1/8.
 */
public class ReportMobileTemplateSettingsPane extends BasicBeanPane<ElementCaseMobileAttr> {

    private UICheckBox mobileCanvasSizeCheck;  // 设置为手机模版画布大小

    public ReportMobileTemplateSettingsPane() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel borderPane = FRGUIPaneFactory.createTitledBorderPane(this.title4PopupWindow());

        JPanel contentPane = new JPanel(FRGUIPaneFactory.createBorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L2, 0, 0));

        contentPane.add(getMobileCanvasSizeCheckPane(), BorderLayout.CENTER);

        borderPane.add(contentPane);
        this.add(borderPane);
    }

    private JPanel getMobileCanvasSizeCheckPane() {
        JPanel panel = new JPanel(FRGUIPaneFactory.createBorderLayout());
        mobileCanvasSizeCheck = new UICheckBox(Inter.getLocText("FR-Designer_Set_Mobile_Canvas_Size"));
        panel.add(mobileCanvasSizeCheck, BorderLayout.NORTH);
        panel.add(getCanvasDescPane(), BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, IntervalConstants.INTERVAL_L6, 0));
        return panel;
    }

    private JPanel getCanvasDescPane() {

        UILabel desc1 = createDescLabel(Inter.getLocText("FR-Designer_Mobile_Screen_Match_Desc"));
        UILabel desc2 = createDescLabel(Inter.getLocText("FR-Designer_Mobile_Screen_Zoom_In_Desc"));
        UILabel desc3 = createDescLabel(Inter.getLocText("FR-Designer_Mobile_Screen_Zoom_Out_Desc"));

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{desc1, null},
                new Component[]{desc2, null},
                new Component[]{desc3, null}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W0, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L7, IntervalConstants.INTERVAL_W4, IntervalConstants.INTERVAL_L1, 0));
        return panel;
    }

    private UILabel createDescLabel(String desc) {
        UILabel label = new UILabel(desc);
        label.setForeground(Color.gray);
        return label;
    }

    @Override
    public void populateBean(ElementCaseMobileAttr ob) {
        if (ob == null) {
            ob = new ElementCaseMobileAttr();
        }
        mobileCanvasSizeCheck.setSelected(ob.isMobileCanvasSize());
    }

    @Override
    public ElementCaseMobileAttr updateBean() {
        return null;
    }

    @Override
    public void updateBean(ElementCaseMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            mobileAttr.setMobileCanvasSize(mobileCanvasSizeCheck.isSelected());
        }
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Template_Settings");
    }
}