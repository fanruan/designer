package com.fr.design.gui.controlpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * 存放一些公用的方法
 * Created by plough on 2018/8/13.
 */
class ListControlPaneHelper {
    private ListControlPaneProvider listControlPane;

    private ListControlPaneHelper(ListControlPaneProvider listControlPane) {
        this.listControlPane = listControlPane;
    }

    public static ListControlPaneHelper newInstance(ListControlPaneProvider listControlPane) {
        return new ListControlPaneHelper(listControlPane);
    }

    public boolean hasInvalid(boolean isAdd) {

        int idx = getInValidIndex();
        if (isAdd || listControlPane.getSelectedIndex() != idx) {
            try {
                listControlPane.checkValid();
            } catch (Exception exp) {
                FineJOptionPane.showMessageDialog((Component) listControlPane, exp.getMessage());
                listControlPane.setSelectedIndex(idx);
                return true;
            }
        }
        return false;
    }

    private int getInValidIndex() {
        BasicBeanPane[] p = listControlPane.getControlUpdatePane().getUpdatePanes();
        if (p != null) {
            for (int i = 0; i < p.length; i++) {
                if (p[i] != null) {
                    try {
                        p[i].checkValid();
                    } catch (Exception e) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public Nameable[] update() {
        java.util.List<Nameable> res = new java.util.ArrayList<Nameable>();
        listControlPane.getControlUpdatePane().update();
        DefaultListModel listModel = listControlPane.getModel();
        for (int i = 0, len = listModel.getSize(); i < len; i++) {
            res.add(((ListModelElement) listModel.getElementAt(i)).wrapper);
        }

        return res.toArray(new Nameable[res.size()]);
    }

    /**
     * 获取选中的名字
     */
    public String getSelectedName() {
        ListModelElement el = listControlPane.getSelectedValue();
        return el == null ? StringUtils.EMPTY : el.wrapper.getName();
    }

    /**
     * 添加 Nameable
     *
     * @param nameable 添加的Nameable
     * @param index    序号
     */
    public void addNameable(Nameable nameable, int index) {
        JNameEdList nameableList = listControlPane.getNameableList();
        DefaultListModel model = listControlPane.getModel();

        ListModelElement el = new ListModelElement(nameable);
        model.add(index, el);
        nameableList.setSelectedIndex(index);
        nameableList.ensureIndexIsVisible(index);
        nameableList.repaint();
    }

    /**
     * 检查按钮可用状态 Check button enabled.
     */
    public void checkButtonEnabled() {

        int selectedIndex = listControlPane.getSelectedIndex();
        if (selectedIndex == -1) {
            listControlPane.showSelectPane();
        } else {
            listControlPane.showEditPane();
        }
        for (ShortCut4JControlPane sj : listControlPane.getShorts()) {
            sj.checkEnable();
        }
    }


}
