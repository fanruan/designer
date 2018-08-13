package com.fr.design.gui.controlpane;

import com.fr.design.gui.ilist.ListModelElement;
import com.fr.general.ComparatorUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;

import javax.swing.DefaultListModel;

/**
 * Created by plough on 2018/8/13.
 */
public class ListControlPaneHandlers {
    ListControlPaneProvider listControlPane;

    private ListControlPaneHandlers(ListControlPaneProvider listControlPane) {
        this.listControlPane = listControlPane;
    }

    public ListControlPaneHandlers newInstance(ListControlPaneProvider listControlPane) {
        return new ListControlPaneHandlers(listControlPane);
    }


    private String createUnrepeatedCopyName(String suffix) {
        DefaultListModel model = listControlPane.getModel();
        String[] names = new String[model.getSize()];
        for (int i = 0; i < model.size(); i++) {
            names[i] = ((ListModelElement) model.get(i)).wrapper.getName();
        }
        String lastName = "CopyOf" + suffix;
        while (ArrayUtils.contains(names, lastName)) {
            lastName = "CopyOf" + lastName;
        }
        return lastName;
    }


    /**
     * 生成不重复的名字
     *
     * @param prefix 名字前缀
     * @return 名字
     */
    public String createUnrepeatedName(String prefix) {
        DefaultListModel model = listControlPane.getModel();
        Nameable[] all = new Nameable[model.getSize()];
        for (int i = 0; i < model.size(); i++) {
            all[i] = ((ListModelElement) model.get(i)).wrapper;
        }
        // richer:生成的名字从1开始. kunsnat: 添加属性从0开始.
        int count = all.length + 1;
        while (true) {
            String name_test = prefix + count;
            boolean repeated = false;
            for (int i = 0, len = model.size(); i < len; i++) {
                Nameable nameable = all[i];
                if (ComparatorUtils.equals(nameable.getName(), name_test)) {
                    repeated = true;
                    break;
                }
            }

            if (!repeated) {
                return name_test;
            }

            count++;
        }
    }
}
