package com.fr.design.gui.controlpane;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.chart.BasePlot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.ilist.UINameEdList;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by plough on 2017/7/19.
 */

public abstract class UIListControlPane extends UIControlPane implements ListControlPaneProvider {
    public static final String LIST_NAME = "UIControl_List";
    private static final int EDIT_RANGE = 25;  // 编辑按钮的x坐标范围

    protected UINameEdList nameableList;
    protected int editingIndex;
    protected String selectedName;
    private boolean isNameRepeated = false;
    protected boolean isPopulating = false;


    public UIListControlPane() {
        this.initComponentPane();
    }

    public UIListControlPane(BasePlot plot) {
        super(plot);
    }

    @Override
    protected JPanel createControlUpdatePane() {
        return JControlUpdatePane.newInstance(this);
    }

    /**
     * 生成添加按钮的NameableCreator
     *
     * @return 按钮的NameableCreator
     */
    @Override
    public abstract NameableCreator[] createNameableCreators();


    @Override
    protected void initLeftPane(JPanel leftPane) {
        nameableList = createJNameList();
        nameableList.setName(LIST_NAME);
        nameableList.setSelectionBackground(UIConstants.ATTRIBUTE_PRESS);
        leftPane.add(new UIScrollPane(nameableList), BorderLayout.CENTER);


        nameableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        nameableList.addMouseListener(listMouseListener);
        nameableList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                // richie:避免多次update和populate大大降低效率
                if (!evt.getValueIsAdjusting()) {
                    // shoc 切换的时候加检验
                    if (hasInvalid(false)) {
                        return;
                    }
                    ((JControlUpdatePane) UIListControlPane.this.controlUpdatePane).update();
                    ((JControlUpdatePane) UIListControlPane.this.controlUpdatePane).populate();
                    UIListControlPane.this.checkButtonEnabled();
                }
            }
        });
        if (isNewStyle()) {
            nameableList.getModel().addListDataListener(new ListDataListener() {
                @Override
                public void intervalAdded(ListDataEvent e) {
                    saveSettings();
                }

                @Override
                public void intervalRemoved(ListDataEvent e) {
                    saveSettings();
                }

                @Override
                public void contentsChanged(ListDataEvent e) {
                    saveSettings();
                }
            });
        }
    }

    public UINameEdList createJNameList() {
        UINameEdList nameEdList = new UINameEdList(new DefaultListModel()) {
            @Override
            protected void doAfterLostFocus() {
                UIListControlPane.this.updateControlUpdatePane();
            }
            @Override
            protected void doAfterStopEditing() {
                saveSettings();
            }
        };
        nameEdList.setCellRenderer(new UINameableListCellRenderer(this));
        return nameEdList;
    }

    public void updateControlUpdatePane() {
        ((JControlUpdatePane) controlUpdatePane).update();
    }

    public void setNameListEditable(boolean editable) {
        this.nameableList.setEditable(editable);
    }

    @Override
    public Nameable[] update() {
        java.util.List<Nameable> res = new java.util.ArrayList<Nameable>();
        ((JControlUpdatePane) this.controlUpdatePane).update();
        DefaultListModel listModel = (DefaultListModel) this.nameableList.getModel();
        for (int i = 0, len = listModel.getSize(); i < len; i++) {
            res.add(((ListModelElement) listModel.getElementAt(i)).wrapper);
        }

        return res.toArray(new Nameable[res.size()]);
    }

    @Override
    public void populate(Nameable[] nameableArray) {
        isPopulating = true;  // 加一个标识位，避免切换单元格时，触发 saveSettings
        nameableList.getCellEditor().stopCellEditing();
        DefaultListModel listModel = (DefaultListModel) this.nameableList.getModel();
        listModel.removeAllElements();
        if (ArrayUtils.isEmpty(nameableArray)) {
            isPopulating = false;
            return;
        }

        listModel.setSize(nameableArray.length);
        for (int i = 0; i < nameableArray.length; i++) {
            listModel.set(i, new ListModelElement(nameableArray[i]));
        }

        if (listModel.size() > 0 || this.nameableList.getSelectedIndex() != 0) {
            this.nameableList.setSelectedIndex(0);
        }
        this.checkButtonEnabled();

        isPopulating = false;
    }


    /**
     * 获取选中的名字
     */
    public String getSelectedName() {
        ListModelElement el = (ListModelElement) this.nameableList.getSelectedValue();

        return el == null ? null : el.wrapper.getName();
    }

    /**
     * 添加 Nameable
     *
     * @param nameable 添加的Nameable
     * @param index    序号
     */
    public void addNameable(Nameable nameable, int index) {
        UINameEdList nameEdList = UIListControlPane.this.nameableList;
        DefaultListModel model = (DefaultListModel) nameEdList.getModel();

        ListModelElement el = new ListModelElement(nameable);
        model.add(index, el);
        nameableList.setSelectedIndex(index);
        nameableList.ensureIndexIsVisible(index);

        nameEdList.repaint();
        popupEditDialog();
    }

    protected DefaultListModel getModel() {
        return (DefaultListModel) UIListControlPane.this.nameableList.getModel();
    }

    private String createUnrepeatedCopyName(String suffix) {
        DefaultListModel model = this.getModel();
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
    @Override
    public String createUnrepeatedName(String prefix) {
        DefaultListModel model = this.getModel();
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

    private void popupEditDialog() {
        popupEditDialog(null);
    }

    protected void popupEditDialog(Point mousePos) {
        if (isNewStyle()) {
            Rectangle currentCellBounds = nameableList.getCellBounds(editingIndex, editingIndex);
            if (editingIndex < 0 || (mousePos != null && !currentCellBounds.contains(mousePos))) {
                return;
            }
            popupEditDialog.setLocation(getPopupDialogLocation());
            if (popupEditDialog instanceof PopupEditDialog) {
                ((PopupEditDialog)popupEditDialog).setTitle(getSelectedName());
            }
            popupEditDialog.setVisible(true);
        }
    }

    private Point getPopupDialogLocation() {
        Point resultPos = new Point(0, 0);
        Point listPos = nameableList.getLocationOnScreen();
        resultPos.x = listPos.x - popupEditDialog.getWidth();
        resultPos.y = listPos.y + (nameableList.getSelectedIndex() - 1) * EDIT_RANGE;

        // 当对象在屏幕上的位置比较靠下时，往下移动弹窗至与属性面板平齐
        Window frame = DesignerContext.getDesignerFrame();
        // 不能太低
        int maxY = frame.getLocationOnScreen().y + frame.getHeight() - popupEditDialog.getHeight();
        if (resultPos.y > maxY) {
            resultPos.y = maxY;
        }
        // 也不能太高
        int minY = frame.getLocationOnScreen().y + EDIT_RANGE;
        if (resultPos.y < minY) {
            resultPos.y = minY;
        }

        // 当在左侧显示不下时，在右侧弹出弹窗
        if (resultPos.x < 0) {
            resultPos.x = listPos.x + nameableList.getParent().getWidth();
        }
        // 如果右侧显示不下，可以向左移动
        int maxX = Toolkit.getDefaultToolkit().getScreenSize().width - popupEditDialog.getWidth() - EDIT_RANGE;
        if (resultPos.x > maxX) {
            resultPos.x = maxX;
        }

        return resultPos;
    }

    @Override
    public void onAddItem(NameableCreator creator) {
        Nameable nameable = creator.createNameable(this);
        this.addNameable(nameable, getModel().getSize());
    }

    @Override
    public void onRemoveItem() {
        try {
            this.nameableList.getCellEditor().stopCellEditing();
        } catch (Exception ignored) {
            // do nothing
        }
        // bug:在选中一个NameObject并删除，会遗留下Name.
        doBeforeRemove();
        if (GUICoreUtils.removeJListSelectedNodes(SwingUtilities
                .getWindowAncestor(this), nameableList)) {
            checkButtonEnabled();
            doAfterRemove();
        }
    }

    @Override
    public void onCopyItem() {
        // p:选中的值.
        ListModelElement selectedValue = (ListModelElement) nameableList.getSelectedValue();
        if (selectedValue == null) {
            return;
        }

        ((JControlUpdatePane) controlUpdatePane).update();

        Nameable selectedNameable = selectedValue.wrapper;

        // p: 用反射机制实现
        try {
            Nameable newNameable = (Nameable) BaseUtils.cloneObject(selectedNameable);
            newNameable.setName(createUnrepeatedCopyName(selectedNameable.getName()));

            UIListControlPane.this.addNameable(newNameable, nameableList.getSelectedIndex() + 1);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }

    @Override
    public void onMoveUpItem() {
        int selectedIndex = nameableList.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }

        // 上移
        if (selectedIndex > 0) {
            DefaultListModel listModel = (DefaultListModel) nameableList
                    .getModel();

            Object selecteObj1 = listModel.get(selectedIndex - 1);
            listModel.set(selectedIndex - 1, listModel.get(selectedIndex));
            listModel.set(selectedIndex, selecteObj1);

            nameableList.setSelectedIndex(selectedIndex - 1);
            nameableList.ensureIndexIsVisible(selectedIndex - 1);
        }
    }

    @Override
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

    @Override
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

        checkButtonEnabled();
        // p:需要repaint.
        nameableList.repaint();
    }

    @Override
    public boolean isItemSelected() {
        return getModel().getSize() > 0 && nameableList.getSelectedIndex() != -1;
    }

    /*
     * UINameEdList的鼠标事件
     */
    private MouseListener listMouseListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent evt) {
            nameableList.stopEditing();
            if (evt.getClickCount() >= 2
                    && SwingUtilities.isLeftMouseButton(evt) && evt.getX() > EDIT_RANGE) {
                editingIndex = nameableList.getSelectedIndex();
                selectedName = nameableList.getNameAt(editingIndex);
                nameableList.editItemAt(nameableList.getSelectedIndex());
            } else if (SwingUtilities.isLeftMouseButton(evt) && evt.getX() <= EDIT_RANGE) {
                editingIndex = nameableList.getSelectedIndex();
                selectedName = nameableList.getNameAt(editingIndex);
                popupEditDialog(evt.getPoint());
            }

            // peter:处理右键的弹出菜单
            if (!SwingUtilities.isRightMouseButton(evt)) {
                return;
            }

            // peter: 注意,在checkButtonEnabled()方法里面,设置了所有的Action的Enabled.
            checkButtonEnabled();

            // p:右键菜单.
            JPopupMenu popupMenu = new JPopupMenu();

            for (ShortCut4JControlPane sj : getShorts()) {
                sj.getShortCut().intoJPopupMenu(popupMenu);
            }

            // peter: 只有弹出菜单有子菜单的时候,才需要弹出来.
            GUICoreUtils.showPopupMenu(popupMenu, nameableList, evt.getX() - 1,
                    evt.getY() - 1);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            JList list = (JList) e.getSource();
            if (list.locationToIndex(e.getPoint()) == -1 && !e.isShiftDown()
                    && !isMenuShortcutKeyDown(e)) {
                list.clearSelection();
            }
        }

        private boolean isMenuShortcutKeyDown(InputEvent event) {
            return (event.getModifiers() & Toolkit.getDefaultToolkit()
                    .getMenuShortcutKeyMask()) != 0;
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    };

    /**
     * 检查按钮可用状态 Check button enabled.
     */
    @Override
    public void checkButtonEnabled() {

        int selectedIndex = nameableList.getSelectedIndex();
        if (selectedIndex == -1) {
            this.cardLayout.show(cardPane, "SELECT");
        } else {
            this.cardLayout.show(cardPane, "EDIT");
        }
        for (ShortCut4JControlPane sj : getShorts()) {
            sj.checkEnable();
        }
    }

    public BasicBeanPane createPaneByCreators(NameableCreator creator) {
        try {
            return creator.getUpdatePane().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public BasicBeanPane createPaneByCreators(NameableCreator creator, String string) {
        Constructor constructor = null;
        try {
            constructor = creator.getUpdatePane().getDeclaredConstructor(new Class[]{String.class});
            constructor.setAccessible(true);
            return (BasicBeanPane) constructor.newInstance(string);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查是否符合规范
     *
     * @throws Exception
     */
    @Override
    public void checkValid() throws Exception {
        ((JControlUpdatePane) this.controlUpdatePane).checkValid();
    }

    private int getInValidIndex() {
        BasicBeanPane[] p = ((JControlUpdatePane) controlUpdatePane).getUpdatePanes();
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

    @Override
    protected boolean hasInvalid(boolean isAdd) {
        int idx = UIListControlPane.this.getInValidIndex();
        if (isAdd || nameableList.getSelectedIndex() != idx) {
            try {
                checkValid();
            } catch (Exception exp) {
                JOptionPane.showMessageDialog(UIListControlPane.this, exp.getMessage());
                nameableList.setSelectedIndex(idx);
                return true;
            }
        }
        return false;
    }
    /**
     * 设置选中项
     *
     * @param index 选中项的序列号
     */
    public void setSelectedIndex(int index) {
        nameableList.setSelectedIndex(index);
    }

    @Override
    public ListModelElement getSelectedElement() {
        return (ListModelElement) this.nameableList.getSelectedValue();
    }
}
