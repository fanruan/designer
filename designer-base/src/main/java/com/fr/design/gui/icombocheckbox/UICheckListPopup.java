package com.fr.design.gui.icombocheckbox;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.IOUtils;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UICheckListPopup extends UIPopupMenu {
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    private List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();

    private Object[] values;
    private JPanel checkboxPane;
    private UIScrollPane jScrollPane;
    private Color mouseEnteredColor = UIConstants.CHECKBOX_HOVER_SELECTED;
    private int maxDisplayNumber = 8;
    private boolean supportSelectAll = true;

    public static final String COMMIT_EVENT = "commit";
    private static final String SELECT_ALL = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Choose_All");
    private static final int CHECKBOX_HEIGHT = 25;

    public UICheckListPopup(Object[] values) {
        this(values, true);
    }

    public UICheckListPopup(Object[] value, boolean supportSelectAll) {
        super();
        values = value;
        this.supportSelectAll = supportSelectAll;
        initComponent();
    }

    public void setMouseEnteredColor(Color color) {
        this.mouseEnteredColor = color;
    }

    public void setMaxDisplayNumber(int maxDisplayNumber) {
        this.maxDisplayNumber = maxDisplayNumber;
        addCheckboxValues(values);
    }

    private void initComponent() {
        checkboxPane = new JPanel();
        checkboxPane.setLayout(new GridLayout(checkBoxList.size(), 1, 0, 0));
        checkboxPane.setBackground(Color.WHITE);
        jScrollPane = new UIScrollPane(checkboxPane);

        this.setLayout(new BorderLayout());
        this.add(jScrollPane, BorderLayout.CENTER);

        addCheckboxValues(values);
    }

    public void addCheckboxValues(Object[] value) {
        checkboxPane.removeAll();
        checkBoxList.clear();

        //全选加在第一个位置
        if (supportSelectAll) {
            addOneCheckValue(SELECT_ALL);
        }
        for (Object checkValue : value) {
            addOneCheckValue(checkValue);
        }
        addSelectListener();

        jScrollPane.setPreferredSize(new Dimension(130, checkBoxList.size() * CHECKBOX_HEIGHT + 10));
        //超过1页的数量时显示滚动条
        if (checkBoxList.size() > maxDisplayNumber) {
            jScrollPane.setPreferredSize(new Dimension(130, maxDisplayNumber * CHECKBOX_HEIGHT));
        }
        checkboxPane.repaint();
        jScrollPane.repaint();
    }

    private void addOneCheckValue(Object checkValue) {
        JPanel checkPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        checkPane.setPreferredSize(new Dimension(120, CHECKBOX_HEIGHT));
        final JCheckBox temp = createCheckbox();
        final UILabel label = new UILabel(checkValue.toString());
        label.setBackground(Color.WHITE);
        label.setPreferredSize(new Dimension(80, 20));
        checkPane.setBackground(Color.WHITE);
        checkPane.add(temp);
        checkPane.add(label);
        addMouseListener(temp, label);

        checkBoxList.add(temp);
        checkboxPane.add(checkPane);
    }

    private JCheckBox createCheckbox() {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setPreferredSize(new Dimension(20, 20));
        checkBox.setBackground(Color.WHITE);
        checkBox.setIcon(IOUtils.readIcon(IconPathConstants.CHECKBOX_NORMAL));
        checkBox.setSelectedIcon(IOUtils.readIcon(IconPathConstants.CHECKBOX_SELECTED));

        return checkBox;
    }

    /**
     * 设置鼠标事件，鼠标进入时背景色变换
     *
     * @param checkBox
     * @param label
     */
    private void addMouseListener(final JCheckBox checkBox, final UILabel label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(Color.WHITE);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setOpaque(true);
                label.setBackground(mouseEnteredColor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                checkBox.doClick();
            }
        });
    }

    /**
     * 单选框选中事件
     */
    private void addSelectListener() {
        for (int i = 0; i < checkBoxList.size(); i++) {
            JCheckBox checkBox = checkBoxList.get(i);
            if (supportSelectAll && i == 0) {
                checkBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        //全选checkbox事件
                        doSelectAll(checkBoxList.get(0).isSelected());
                    }
                });
            } else {
                checkBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        //do半选判断放在commit事件里
                        commit();
                    }
                });
            }
        }
    }

    /**
     * 全选
     *
     * @param isSelected 选中是true，未选是false
     */
    private void doSelectAll(boolean isSelected) {
        for (int i = 1; i < checkBoxList.size(); i++) {
            //全选和反全选都不考虑全选按钮本身
            if (!SELECT_ALL.equals(checkBoxList.get(i).getText())) {
                checkBoxList.get(i).setSelected(isSelected);
            }
        }
    }

    public void setSelectedValue(Map<Object, Boolean> selectedValues) {
        List<Object> allValue = Arrays.asList(values);
        for (Object value : selectedValues.keySet()) {
            int index = allValue.indexOf(value);
            checkBoxList.get(index + 1).setSelected(selectedValues.get(value));
        }
    }

    /**
     * 获取所有选中的值
     *
     * @return
     */
    public Object[] getSelectedValues() {
        List<Object> selectedValues = new ArrayList<Object>();
        int selectCount = 0;

        for (int i = 1; i < checkBoxList.size(); i++) {
            if (checkBoxList.get(i).isSelected()) {
                selectedValues.add(values[i - 1]);
                selectCount++;
            }
        }
        //全选半选切换
        switchSelectIcon(selectCount);

        return selectedValues.toArray(new Object[selectedValues.size()]);
    }


    /**
     * 切换全选半选图片
     */
    private void switchSelectIcon(int selectCount) {
        JCheckBox checkBox = checkBoxList.get(0);
        if (selectCount == 0) {
            checkBox.setIcon(BaseUtils.readIcon(IconPathConstants.CHECKBOX_NORMAL));
        } else if (selectCount < checkBoxList.size() - 1) {
            //虽然有选中，但是要判断此时全选状态去换图标
            if (checkBoxList.get(0).isSelected()) {
                checkBox.setSelectedIcon(BaseUtils.readIcon(IconPathConstants.CHECKBOX_HATFSELECT));
            } else {
                checkBox.setIcon(BaseUtils.readIcon(IconPathConstants.CHECKBOX_HATFSELECT));
            }
        } else {
            //全选了，图标要换回来
            checkBox.setSelectedIcon(BaseUtils.readIcon(IconPathConstants.CHECKBOX_SELECTED));
        }
    }

    public void commit() {
        fireActionPerformed(new ActionEvent(this, 0, COMMIT_EVENT));
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    public void addActionListener(ActionListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeActionListener(ActionListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    protected void fireActionPerformed(ActionEvent e) {
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

}
