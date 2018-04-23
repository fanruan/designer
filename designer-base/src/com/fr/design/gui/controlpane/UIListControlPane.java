package com.fr.design.gui.controlpane;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.chart.BasePlot;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.tabledata.tabledatapane.GlobalMultiTDTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.GlobalTreeTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.MultiTDTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.TreeTableDataPane;
import com.fr.design.gui.HyperlinkFilterHelper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.ilist.ModNameActionListener;
import com.fr.design.gui.ilist.UINameEdList;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.LineSeparator;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by plough on 2017/7/19.
 */

public abstract class UIListControlPane extends UIControlPane {
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
        return new JControlUpdatePane();
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

    protected void doWhenPopulate(BasicBeanPane beanPane) {

    }

    protected void doBeforePopulate(ListModelElement el, Object obj) {

    }

    @Override
    protected ShortCut4JControlPane addItemShortCut() {
        ShortCut addItemShortCut;
        NameableCreator[] creators = creators();
        if (creators.length == 1) {
            addItemShortCut = new AddItemUpdateAction(creators);
        } else {
            addItemShortCut = getAddItemMenuDef(creators);
        }
        return new AbsoluteEnableShortCut(addItemShortCut);
    }

    protected AddItemMenuDef getAddItemMenuDef (NameableCreator[] creators) {
        return new AddItemMenuDef(creators);
    }

    @Override
    protected ShortCut4JControlPane removeItemShortCut() {
        return new NormalEnableShortCut(new RemoveItemAction());
    }

    @Override
    protected ShortCut4JControlPane copyItemShortCut() {
        return new NormalEnableShortCut(new CopyItemAction());
    }

    @Override
    protected ShortCut4JControlPane moveUpItemShortCut() {
        return new NormalEnableShortCut(new MoveUpItemAction());
    }

    @Override
    protected ShortCut4JControlPane moveDownItemShortCut() {
        return new NormalEnableShortCut(new MoveDownItemAction());
    }

    @Override
    protected ShortCut4JControlPane sortItemShortCut() {
        return new NormalEnableShortCut(new SortItemAction());
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

        for (Nameable aNameableArray : nameableArray) {
            listModel.addElement(new ListModelElement(aNameableArray));
        }

        if (listModel.size() > 0 || this.nameableList.getSelectedIndex() != 0) {
            this.nameableList.setSelectedIndex(0);
        }
        this.checkButtonEnabled();

        isPopulating = false;
    }

    /**
     * 添加名字改变时的listener
     *
     * @param l 名字改变时的监听
     */
    public void addModNameActionListener(ModNameActionListener l) {
        this.nameableList.addModNameActionListener(l);
    }

    /**
     * 添加Editinglistener
     *
     * @param l 监听
     */
    public void addEditingListner(PropertyChangeAdapter l) {
        this.nameableList.addEditingListner(l);
    }

    /*
     * 刷新当前的选中的UpdatePane
     */
    protected void populateSelectedValue() {
        ((JControlUpdatePane) UIListControlPane.this.controlUpdatePane).populate();
    }

    /**
     * 根据name,选中UINameEdList中的item
     */
    public void setSelectedName(String name) {
        DefaultListModel listModel = (DefaultListModel) this.nameableList.getModel();
        for (int i = 0, len = listModel.getSize(); i < len; i++) {
            Nameable item = ((ListModelElement) listModel.getElementAt(i)).wrapper;
            if (ComparatorUtils.equals(name, item.getName())) {
                this.nameableList.setSelectedIndex(i);
                break;
            }
        }
    }

    public String getEditingName() {
        return this.nameableList.getEditingName();
    }

    public Object getEditingType() {
        return this.nameableList.getAllTypes()[editingIndex];
    }

    public void setWarnigText(int index) {
        this.nameableList.setWarnigText(index);
    }

    /**
     * 获取选中的名字
     */
    public String getSelectedName() {
        ListModelElement el = (ListModelElement) this.nameableList.getSelectedValue();

        return el == null ? null : el.wrapper.getName();
    }

    protected boolean isNameRepeted(java.util.List[] list, String name) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].contains(name)) {
                isNameRepeated = true;
                return true;
            }
        }
        isNameRepeated = false;
        return false;
    }

    /**
     * 名字是否重复
     *
     * @return 重复则返回true
     */
    public boolean isNameRepeated() {
        return isNameRepeated;
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

    /**
     * 是否重命名
     *
     * @return 是则true
     */
    public boolean isContainsRename() {
        String rename = Inter.getLocText("FR-Please_Rename") + "!";
        String[] names = this.nameableList.getAllNames();
        for (int i = names.length - 1; i >= 0; i--) {
            if (ComparatorUtils.equals(names[i], rename)) {
                return true;
            }
        }
        return false;
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

    /**
     * 增加项的UpdateAction
     */
    protected class AddItemUpdateAction extends UpdateAction {
        final NameableCreator creator;

        public AddItemUpdateAction(NameableCreator[] creators) {
            this.creator = creators[0];
            this.setName(Inter.getLocText("FR-Action_Add"));
            this.setMnemonic('A');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        }

        /**
         * Gets component on toolbar.
         *
         * @return the created components on toolbar.
         */
        @Override
        public JComponent createToolBarComponent() {
            Object object = this.getValue(UIButton.class.getName());
            if (!(object instanceof AbstractButton)) {
                // 直接使用默认UI
                UIButton button =  new UIButton();
                // 添加一个名字作为自动化测试用
                button.setName(getName());

                //设置属性.
                Integer mnemonicInteger = (Integer) this.getValue(Action.MNEMONIC_KEY);
                if (mnemonicInteger != null) {
                    button.setMnemonic((char) mnemonicInteger.intValue());
                }

                button.setIcon((Icon) this.getValue(Action.SMALL_ICON));
                button.addActionListener(this);

                button.registerKeyboardAction(this, this.getAccelerator(), JComponent.WHEN_IN_FOCUSED_WINDOW);

                this.putValue(UIButton.class.getName(), button);
                button.setText(StringUtils.EMPTY);
                button.setEnabled(this.isEnabled());

                //peter:产生tooltip
                button.setToolTipText(ActionFactory.createButtonToolTipText(this));
                object = button;
            }

            return (JComponent) object;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Nameable nameable = creator.createNameable(UIListControlPane.this);

            UIListControlPane.this.addNameable(nameable, getModel().getSize());
        }
    }

    /*
     * 增加项的MenuDef
     */
    protected class AddItemMenuDef extends MenuDef {
        public AddItemMenuDef(NameableCreator[] creators) {
            super(true);
            this.setName(Inter.getLocText("FR-Action_Add"));
            this.setMnemonic('A');
            this.setIconPath("/com/fr/design/images/control/addPopup.png");
            wrapActionListener(creators);
        }

        /**
         * 生成UIButton
         * @return  菜单按钮
         */
        public UIButton createUIButton() {
            createdButton = super.createUIButton();
            // 此按钮单独抽出，不应使用工具栏外观
            if (!createdButton.isOpaque()) {
                createdButton.setOpaque(true);
                createdButton.setNormalPainted(true);
                createdButton.setBorderPaintedOnlyWhenPressed(false);
            }
            return createdButton;
        }

        private void wrapActionListener(NameableCreator[] creators) {
            for (final NameableCreator creator : creators) {
                if (!whetherAdd(creator.menuName())) {
                    continue;
                }
                boolean isTrue = ComparatorUtils.equals(creator.menuName(), Inter.getLocText("Datasource-Stored_Procedure")) ||
                        ComparatorUtils.equals(creator.menuName(), Inter.getLocText("DS-Relation_TableData")) || ComparatorUtils.equals(creator.menuName(), Inter.getLocText("DS-Multi_Dimensional_Database"));
                if (isTrue) {
                    this.addShortCut(new LineSeparator());
                }
                this.addShortCut(new UpdateAction() {
                    {
                        this.setName(creator.menuName());
                        Icon icon = creator.menuIcon();
                        if (icon != null) {
                            this.setSmallIcon(icon);
                        }
                    }

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (hasInvalid(true)) {
                            return;
                        }

                        Nameable nameable = creator.createNameable(UIListControlPane.this);

                        UIListControlPane.this.addNameable(nameable, getModel().getSize());
                    }
                });
            }
        }

        protected boolean whetherAdd(String itemName){
            return HyperlinkFilterHelper.whetherAddHyperlink4cell(itemName);
        }
    }

    /*
     * 移除item
     */
    private class RemoveItemAction extends UpdateAction {
        public RemoveItemAction() {
            this.setName(Inter.getLocText("FR-Action_Remove"));
            this.setMnemonic('R');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/base/images/cell/control/remove.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                UIListControlPane.this.nameableList.getCellEditor()
                        .stopCellEditing();
            } catch (Exception ignored) {
                // do nothing
            }
            // bug:在选中一个NameObject并删除，会遗留下Name.
            doBeforeRemove();
            if (GUICoreUtils.removeJListSelectedNodes(SwingUtilities
                    .getWindowAncestor(UIListControlPane.this), nameableList)) {
                checkButtonEnabled();
                doAfterRemove();
            }
        }
    }

    /*
     * CopyItem
     */
    private class CopyItemAction extends UpdateAction {
        public CopyItemAction() {
            this.setName(Inter.getLocText("FR-Action_Copy"));
            this.setMnemonic('C');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/design/images/m_edit/copy.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
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
    }

    /*
     * 上移Item
     */
    private class MoveUpItemAction extends UpdateAction {
        public MoveUpItemAction() {
            this.setName(Inter.getLocText("Utils-Move_Up"));
            this.setMnemonic('U');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/design/images/control/up.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
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
    }

    /*
     * 下移Item
     */
    private class MoveDownItemAction extends UpdateAction {
        public MoveDownItemAction() {
            this.setName(Inter.getLocText("Utils-Move_Down"));
            this.setMnemonic('D');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/design/images/control/down.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
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
    }

    private class SortItemAction extends UpdateAction {
        private boolean isAtoZ = false;

        public SortItemAction() {
            this.setName(Inter.getLocText("FR-Action_Sort"));
            this.setMnemonic('S');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/design/images/control/sortAsc.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
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


    public class AbsoluteEnableShortCut extends ShortCut4JControlPane {
        public AbsoluteEnableShortCut(ShortCut shortCut) {
            this.shortCut = shortCut;
        }

        /**
         * 检查是否可用
         */
        @Override
        public void checkEnable() {
            this.shortCut.setEnabled(true);
        }
    }

    public class NormalEnableShortCut extends ShortCut4JControlPane {
        public NormalEnableShortCut(ShortCut shortCut) {
            this.shortCut = shortCut;
        }

        /**
         * 检查是否可用
         */
        @Override
        public void checkEnable() {
            this.shortCut.setEnabled(getModel()
                    .getSize() > 0
                    && UIListControlPane.this.nameableList.getSelectedIndex() != -1);
        }
    }

    public class SortEnableShortCut extends ShortCut4JControlPane {
        public SortEnableShortCut(ShortCut shortCut) {
            this.shortCut = shortCut;
        }

        /**
         * 检查是否可用
         */
        @Override
        public void checkEnable() {
            this.shortCut.setEnabled(getModel().getSize() > 1);
        }

    }

    public class MoveUpEnableShortCut extends ShortCut4JControlPane {
        public MoveUpEnableShortCut(ShortCut shortCut) {
            this.shortCut = shortCut;
        }

        /**
         * 检查是否可用
         */
        @Override
        public void checkEnable() {
            this.shortCut.setEnabled(getModel().getSize() > 1
                    && UIListControlPane.this.nameableList.getSelectedIndex() > 0);
        }

    }

    public class MoveDownEnableShortCut extends ShortCut4JControlPane {
        public MoveDownEnableShortCut(ShortCut shortCut) {
            this.shortCut = shortCut;
        }

        /**
         * 检查是否可用
         */
        @Override
        public void checkEnable() {
            this.shortCut.setEnabled(getModel().getSize() > 1
                    && UIListControlPane.this.nameableList.getSelectedIndex() < UIListControlPane.this.nameableList.getModel().getSize() - 1);
        }

    }


    protected Object getob2Populate (Object ob2Populate) {
        return  ob2Populate;
    }

    private class JControlUpdatePane extends JPanel {
        private CardLayout card;
        private JPanel cardPane;
        private BasicBeanPane[] updatePanes;

        private ListModelElement elEditing;

        public JControlUpdatePane() {
            initUpdatePane();
        }

        private void initUpdatePane() {
            NameableCreator[] creators = creators();
            if (creators == null) {
                return;
            }
            card = new CardLayout();
            cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
            cardPane.setLayout(card);
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.add(cardPane);
            int len = creators.length;
            updatePanes = new BasicBeanPane[len];
        }

        public void populate() {
            ListModelElement el = (ListModelElement) UIListControlPane.this.nameableList.getSelectedValue();
            if (el == null) {
                return;
            }

            elEditing = el;
            NameableCreator[] creators = creators();

            for (int i = 0, len = updatePanes.length; i < len; i++) {
                Object ob2Populate = creators[i].acceptObject2Populate(el.wrapper);
                ob2Populate = getob2Populate(ob2Populate);
                if (ob2Populate != null) {
                    if (updatePanes[i] == null) {
                        if (isMulti(creators[i].getUpdatePane()) || isTree(creators[i].getUpdatePane())) {
                            updatePanes[i] = createPaneByCreators(creators[i], el.wrapper.getName());
                        } else {
                            updatePanes[i] = createPaneByCreators(creators[i]);
                        }
                        cardPane.add(updatePanes[i], String.valueOf(i));
                    }
                    card.show(cardPane, String.valueOf(i));
                    doBeforePopulate(el, ob2Populate);
                    updatePanes[i].populateBean(ob2Populate);
                    doWhenPopulate(updatePanes[i]);
                    break;
                }
            }
        }


        public boolean isMulti(Class _class) {
            return ComparatorUtils.equals(_class, GlobalMultiTDTableDataPane.class) || ComparatorUtils.equals(_class, MultiTDTableDataPane.class);
        }

        public boolean isTree(Class _class) {
            return ComparatorUtils.equals(_class, GlobalTreeTableDataPane.class) || ComparatorUtils.equals(_class, TreeTableDataPane.class);
        }

        public void update() {
            NameableCreator[] creators = creators();
            for (int i = 0; i < updatePanes.length; i++) {
                BasicBeanPane pane = updatePanes[i];

                if (pane != null && pane.isVisible()) {
                    Object bean = pane.updateBean();
                    if (i < creators.length) {
                        creators[i].saveUpdatedBean(elEditing, bean);
                    }
                }
            }
        }

        public void checkValid() throws Exception {
            if (updatePanes != null) {
                for (int i = 0; i < updatePanes.length; i++) {
                    if (updatePanes[i] != null) {
                        updatePanes[i].checkValid();
                    }
                }
            }
        }
    }

    protected BasicBeanPane createPaneByCreators(NameableCreator creator) {
        try {
            return creator.getUpdatePane().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected BasicBeanPane createPaneByCreators(NameableCreator creator, String string) {
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

    // 选项添加个数有限制等情况下 要求能控制快捷按钮的状态
    protected void setToolbarDefEnable(int shortCutIndex, int itemIndex, boolean enabled) {
        ToolBarDef toolbarDef = getToolbarDef();
        if (toolbarDef.getShortCutCount() > shortCutIndex) {
            ShortCut sc = toolbarDef.getShortCut(shortCutIndex);
            if (sc instanceof AddItemMenuDef) {
                AddItemMenuDef am = (AddItemMenuDef) sc;
                if (am.getShortCutCount() > itemIndex) {
                    am.getShortCut(itemIndex).setEnabled(enabled);
                }
            }
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
        BasicBeanPane[] p = ((JControlUpdatePane) controlUpdatePane).updatePanes;
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

}