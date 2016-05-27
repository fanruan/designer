package com.fr.design.report.mobile;

import com.fr.base.mobile.MobileFitAttrState;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/16/0016.
 */
public class MobileRadioGroupPane extends BasicBeanPane<MobileFitAttrState> {

    private List<UIRadioButton> radioButtons = new ArrayList<UIRadioButton>();

    public MobileRadioGroupPane(String title) {
        initComponents(title);
    }

    private void initComponents(String title) {
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, p, p, p, p};

        UIRadioButton defaultRadio = new UIRadioButton(MobileFitAttrState.DEFAULT.description());
        defaultRadio.setSelected(true);
        UIRadioButton horizonRadio = new UIRadioButton(MobileFitAttrState.HORIZONTAL.description());
        UIRadioButton verticalRadio = new UIRadioButton(MobileFitAttrState.VERTICAL.description());
        UIRadioButton notFitRadio = new UIRadioButton(MobileFitAttrState.NONE.description());

        addToButtonGroup(defaultRadio, horizonRadio, verticalRadio, notFitRadio);

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(title), defaultRadio, horizonRadio, verticalRadio, notFitRadio}
        };
        JPanel fitOpsPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        fitOpsPane.setBorder(BorderFactory.createEmptyBorder(10, 13, 10, 10));

        this.add(fitOpsPane);
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
    public void populateBean(MobileFitAttrState ob) {
        selectIndexButton(ob.getState());
    }

    @Override
    public MobileFitAttrState updateBean() {
        int index = getSelectRadioIndex();
        return MobileFitAttrState.parse(index);
    }

    @Override
    protected String title4PopupWindow() {
        return StringUtils.EMPTY;
    }
}
