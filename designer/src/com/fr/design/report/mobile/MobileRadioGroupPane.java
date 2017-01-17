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
public class MobileRadioGroupPane extends BasicBeanPane<MobileFitAttrState>{

    private List<RadioButton> radioButtons = new ArrayList<RadioButton>();

    public MobileRadioGroupPane(String title) {
        initComponents(title);
    }

    private void initComponents(String title) {
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, p, p, p, p, p};

        RadioButton defaultRadio = new RadioButton(new UIRadioButton(MobileFitAttrState.DEFAULT.description()), MobileFitAttrState.DEFAULT.getState());
        defaultRadio.getRadioButton().setSelected(true);
        RadioButton horizonRadio = new RadioButton(new UIRadioButton(MobileFitAttrState.HORIZONTAL.description()), MobileFitAttrState.HORIZONTAL.getState());
        RadioButton verticalRadio = new RadioButton(new UIRadioButton(MobileFitAttrState.VERTICAL.description()), MobileFitAttrState.VERTICAL.getState());
        RadioButton bidirectionalRadio = new RadioButton(new UIRadioButton(MobileFitAttrState.BIDIRECTIONAL.description()), MobileFitAttrState.BIDIRECTIONAL.getState());
        RadioButton notFitRadio = new RadioButton(new UIRadioButton(MobileFitAttrState.NONE.description()), MobileFitAttrState.NONE.getState());

        addToButtonGroup(defaultRadio, horizonRadio, verticalRadio, notFitRadio, bidirectionalRadio);

        Component[][] components = new Component[][]{
                new Component[] {
                        new UILabel(title),
                        defaultRadio.getRadioButton(),
                        horizonRadio.getRadioButton(),
                        verticalRadio.getRadioButton(),
                        bidirectionalRadio.getRadioButton(),
                        notFitRadio.getRadioButton()
                }
        };
        JPanel fitOpsPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        fitOpsPane.setBorder(BorderFactory.createEmptyBorder(10, 13, 10, 10));
        this.add(fitOpsPane);
    }

    private void addToButtonGroup(RadioButton... radios) {
        ButtonGroup buttonGroup = new ButtonGroup();
        for (RadioButton radio : radios) {
            radioButtons.add(radio);
            buttonGroup.add(radio.getRadioButton());
        }
    }

    /**
     * 设置按钮状态
     */
    public void setEnabled(boolean enabled) {
        for (RadioButton radioButton : radioButtons) {
            radioButton.getRadioButton().setEnabled(enabled);
        }
    }

    /**
     * 获取当前选中的按钮index
     *
     * @return 按钮index
     */
    public int getSelectRadioIndex() {
        for (int i = 0, len = radioButtons.size(); i < len; i++) {
            if (radioButtons.get(i).getRadioButton().isSelected()) {
                return radioButtons.get(i).getRadioButtonIndex();
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

        for (RadioButton radioButton : this.radioButtons) {
            if (radioButton.getRadioButtonIndex() == index) {
                radioButton.getRadioButton().setSelected(true);
            }
        }
    }

    /**
     * 给所有的按钮加上监听
     */
    public void addActionListener(ActionListener actionListener) {
        for (RadioButton radioButton : radioButtons) {
            radioButton.getRadioButton().addActionListener(actionListener);
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

/**
 * created by fanglei on 2017/1/16
 * 不再用radioButtonGroup的数组下标作为index，而是给每个按钮传入MobileFitAttrState的枚举值
 */
class RadioButton {
    private UIRadioButton radioButton;
    private int index;

    RadioButton(UIRadioButton radioButton, int index) {
        this.radioButton = radioButton;
        this.index = index;
    }

    public UIRadioButton getRadioButton() {
        return this.radioButton;
    }

    public void setUIReadioButton(UIRadioButton radioButton) {
        this.radioButton = radioButton;
    }

    public int getRadioButtonIndex() {
        return this.index;
    }

    public void setRadioButtonIndex(int index) {
        this.index = index;
    }
}
