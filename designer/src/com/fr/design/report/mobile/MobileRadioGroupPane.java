package com.fr.design.report.mobile;

import com.fr.base.mobile.MobileFitAttrState;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.data.index.Index;
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

    private List<IndexRadioButton> radioButtons = new ArrayList<IndexRadioButton>();

    public MobileRadioGroupPane(String title) {
        initComponents(title);
    }

    private void initComponents(String title) {
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, p, p, p, p};

        IndexRadioButton horizonRadio = new IndexRadioButton(MobileFitAttrState.HORIZONTAL.description(), MobileFitAttrState.HORIZONTAL);
        horizonRadio.setSelected(true);
        IndexRadioButton verticalRadio = new IndexRadioButton(MobileFitAttrState.VERTICAL.description(), MobileFitAttrState.VERTICAL);
        IndexRadioButton bidirectionalRadio = new IndexRadioButton(MobileFitAttrState.BIDIRECTIONAL.description(), MobileFitAttrState.BIDIRECTIONAL);
        IndexRadioButton notFitRadio = new IndexRadioButton(MobileFitAttrState.NONE.description(), MobileFitAttrState.NONE);

        addToButtonGroup(horizonRadio, verticalRadio, notFitRadio, bidirectionalRadio);

        Component[][] components = new Component[][]{
                new Component[] {
                        new UILabel(title),
                        horizonRadio,
                        verticalRadio,
                        bidirectionalRadio,
                        notFitRadio
                }
        };
        JPanel fitOpsPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        fitOpsPane.setBorder(BorderFactory.createEmptyBorder(10, 13, 10, 10));
        this.add(fitOpsPane);
    }

    private void addToButtonGroup(IndexRadioButton... radios) {
        ButtonGroup buttonGroup = new ButtonGroup();
        for (IndexRadioButton radio : radios) {
            radioButtons.add(radio);
            buttonGroup.add(radio);
        }
    }

    /**
     * 设置按钮状态
     */
    public void setEnabled(boolean enabled) {
        for (IndexRadioButton radioButton : radioButtons) {
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
                return radioButtons.get(i).getRadioButtonIndex();
            }
        }

        return 0;
    }

    /**
     * 选中指定index的按钮
     */
    public void selectIndexButton(int index) {
        //这里删了默认按钮，所以这边判断的时候不能-1了
//        if (index < 0 || index > radioButtons.size() - 1) { fanglei: 这个注释不要删！不然以后可能忘记这个问题
        if (index < 0 || index > radioButtons.size()) {
            return;
        }

        for (IndexRadioButton radioButton : this.radioButtons) {
            if (radioButton.getRadioButtonIndex() == index) {
                radioButton.setSelected(true);
            }
        }
    }

    /**
     * 给所有的按钮加上监听
     */
    public void addActionListener(ActionListener actionListener) {
        for (IndexRadioButton radioButton : radioButtons) {
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

/**
 * created by fanglei on 2017/1/16
 * 不再用radioButtonGroup的数组下标作为index，而是给每个按钮传入MobileFitAttrState的枚举值
 */
class IndexRadioButton extends UIRadioButton {
    private int index;

    IndexRadioButton(String text, MobileFitAttrState mobileFitAttrState) {
        super(text);
        this.index = mobileFitAttrState.getState();
    }

    public int getRadioButtonIndex() {
        return this.index;
    }

    public void setRadioButtonIndex(int index) {
        this.index = index;
    }
}
