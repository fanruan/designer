package com.fr.design.gui.controlpane;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 存放一些通用的事件处理方法
 * Created by plough on 2018/8/13.
 */
public class CommonShortCutHandlers {
    ListControlPaneProvider listControlPane;
    JNameEdList nameableList;

    private CommonShortCutHandlers(ListControlPaneProvider listControlPane) {
        this.listControlPane = listControlPane;
        this.nameableList = listControlPane.getNameableList();
    }

    public static CommonShortCutHandlers newInstance(ListControlPaneProvider listControlPane) {
        return new CommonShortCutHandlers(listControlPane);
    }

    public void onAddItem(NameableCreator creator) {
        if (listControlPane.hasInvalid(true)) {
            return;
        }

        Nameable nameable = creator.createNameable(listControlPane);
        listControlPane.addNameable(nameable, listControlPane.getModel().getSize());
    }

    public void onRemoveItem() {
        try {
            nameableList.getCellEditor()
                    .stopCellEditing();
        } catch (Exception ignored) {
        }
        if (GUICoreUtils.removeJListSelectedNodes(SwingUtilities
                .getWindowAncestor((Component) listControlPane), nameableList)) {
            listControlPane.checkButtonEnabled();
        }
    }

    public void onCopyItem() {
        // p:选中的值.
        ListModelElement selectedValue = (ListModelElement) nameableList.getSelectedValue();
        if (selectedValue == null) {
            return;
        }

        listControlPane.getControlUpdatePane().update();

        Nameable selectedNameable = selectedValue.wrapper;

        // p: 用反射机制实现
        try {
            Nameable newNameable = (Nameable) BaseUtils.cloneObject(selectedNameable);
            newNameable.setName(createUnrepeatedCopyName(selectedNameable.getName()));

            listControlPane.addNameable(newNameable, listControlPane.getSelectedIndex() + 1);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public void onMoveUpItem() {
        int selectedIndex = nameableList.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }

        // 上移
        if (selectedIndex > 0) {
            DefaultListModel listModel = (DefaultListModel) nameableList.getModel();
            Object selecteObj1 = listModel.get(selectedIndex - 1);
            listModel.set(selectedIndex - 1, listModel.get(selectedIndex));
            listModel.set(selectedIndex, selecteObj1);

            nameableList.setSelectedIndex(selectedIndex - 1);
            nameableList.ensureIndexIsVisible(selectedIndex - 1);
        }
    }

    public void onMoveDownItem() {
        int selectedIndex = nameableList.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }

        if (selectedIndex < nameableList.getModel().getSize() - 1) {
            DefaultListModel listModel = (DefaultListModel) nameableList
                    .getModel();

            Object selecteObj1 = listModel.get(selectedIndex + 1);
            listModel.set(selectedIndex + 1, listModel.get(selectedIndex));
            listModel.set(selectedIndex, selecteObj1);

            nameableList.setSelectedIndex(selectedIndex + 1);
            nameableList.ensureIndexIsVisible(selectedIndex + 1);
        }
    }

    public void onSortItem(boolean isAtoZ) {
        // p:选中的值.
        Object selectedValue = nameableList.getSelectedValue();

        DefaultListModel listModel = (DefaultListModel) nameableList
                .getModel();
        Nameable[] nameableArray = new Nameable[listModel.getSize()];
        if (nameableArray.length <= 0) {
            return;
        }

        for (int i = 0; i < listModel.getSize(); i++) {
            nameableArray[i] = ((ListModelElement) listModel.getElementAt(i)).wrapper;
        }

        // p:排序.
        if (isAtoZ) {
            Comparator<Nameable> nameableComparator = new Comparator<Nameable>() {
                @Override
                public int compare(Nameable o1, Nameable o2) {
                    return -ComparatorUtils.compare(o1.getName(), o2
                            .getName());
                }
            };
            isAtoZ = !isAtoZ;
            Arrays.sort(nameableArray, nameableComparator);
        } else {
            Comparator<Nameable> nameableComparator = new Comparator<Nameable>() {
                @Override
                public int compare(Nameable o1, Nameable o2) {
                    return ComparatorUtils.compare(o1.getName(), o2
                            .getName());
                }
            };
            isAtoZ = !isAtoZ;
            Arrays.sort(nameableArray, nameableComparator);
        }

        for (int i = 0; i < nameableArray.length; i++) {
            listModel.set(i, new ListModelElement(nameableArray[i]));
        }

        // p:需要选中以前的那个值.
        if (selectedValue != null) {
            nameableList.setSelectedValue(selectedValue, true);
        }

        listControlPane.checkButtonEnabled();
        // p:需要repaint.
        nameableList.repaint();
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
