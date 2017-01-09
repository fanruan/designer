package com.fr.design.dialog.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.border.UITitledBorder;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 由于MobileUserHtmlGroupPane 现在在report和form中均会用到，会出现重复代码，故放入base中
 * Created by fanglei on 2016/12/28.
 */
public abstract class MobileUseHtmlGroupBeanPane<T> extends BasicBeanPane<T> {

    private List<UIRadioButton> radioButtons = new ArrayList<UIRadioButton>();

    public MobileUseHtmlGroupBeanPane() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(UITitledBorder.createBorderWithTitle(this.title4PopupWindow()));

        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {p, p, p};

        UIRadioButton useApp = new UIRadioButton(Inter.getLocText("FR-mobile_native_analysis"));
        useApp.setSelected(true);
        UIRadioButton useHTML5 = new UIRadioButton(Inter.getLocText("FR-mobile_html_analysis"));

        addToButtonGroup(useApp, useHTML5);

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-mobile_analysis_style")), useApp, useHTML5},
                new Component[]{new UILabel(Inter.getLocText("FR-mobile_analysis_annotation")), null, null}
        };
        JPanel usePane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        usePane.setBorder(BorderFactory.createEmptyBorder(10, 13, 10, 10));

        this.add(usePane);
    }

    private void addToButtonGroup(UIRadioButton... radios) {
        ButtonGroup buttonGroup = new ButtonGroup();
        for (UIRadioButton radio : radios) {
            radioButtons.add(radio);
            buttonGroup.add(radio);
        }
    }

    /**
     * 设置按钮状态
     */
    public void setEnabled(boolean enabled) {
        for (UIRadioButton radioButton : radioButtons) {
            radioButton.setEnabled(enabled);
        }
    }

    /**
     * 获取当前选中的按钮index
     *
     * @return 按钮index
     */
    public int getSelectRadioIndex() {
        for (int i = 0, len = radioButtons.size(); i < len; i++) {
            if (radioButtons.get(i).isSelected()) {
                return i;
            }
        }

        return 0;
    }

    /**
     * 选中指定index的按钮
     */
    public void selectIndexButton(int index) {
        if (index < 0 || index > radioButtons.size() - 1) {
            return;
        }

        UIRadioButton button = radioButtons.get(index);
        button.setSelected(true);
    }

    /**
     * 给所有的按钮加上监听
     */
    public void addActionListener(ActionListener actionListener) {
        for (UIRadioButton radioButton : radioButtons) {
            radioButton.addActionListener(actionListener);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-mobile_report_analysis");
    }
}

