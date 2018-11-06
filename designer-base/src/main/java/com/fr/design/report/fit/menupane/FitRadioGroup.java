package com.fr.design.report.fit.menupane;

import com.fr.design.gui.ibutton.UIRadioButton;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 自适应四个按钮选项的group
 * <p>
 * Created by Administrator on 2016/5/5/0005.
 */
public class FitRadioGroup extends ButtonGroup {

    private List<UIRadioButton> radioButtons = new ArrayList<UIRadioButton>();

    @Override
    public void add(AbstractButton button) {
        super.add(button);

        UIRadioButton radioButton = (UIRadioButton) button;
        radioButtons.add(radioButton);
    }

    /**
     * 设置按钮状态
     */
    public boolean isEnabled() {
        return radioButtons.get(0).isEnabled();
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

}
