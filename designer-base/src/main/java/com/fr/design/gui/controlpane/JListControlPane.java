package com.fr.design.gui.controlpane;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.tabledata.tabledatapane.GlobalMultiTDTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.GlobalTreeTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.MultiTDTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.TreeTableDataPane;
import com.fr.design.gui.HyperlinkFilterHelper;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.ilist.ModNameActionListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.LineSeparator;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.invoke.Reflect;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Comparator;

public abstract class JListControlPane extends JControlPane {
    public static final String LIST_NAME = "JControl_List";

    protected JNameEdList nameableList;
    protected int editingIndex;
    protected String selectedName;
    private boolean isNameRepeated = false;

    public JListControlPane() {
        this.initComponentPane();
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
                    ((JControlUpdatePane) JListControlPane.this.controlUpdatePane).update();
                    ((JControlUpdatePane) JListControlPane.this.controlUpdatePane).populate();
                    JListControlPane.this.checkButtonEnabled();
                }
            }
        });
    }

    public JNameEdList createJNameList() {
        JNameEdList nameEdList = new JNameEdList(new DefaultListModel()) {
            @Override
            protected void doAfterLostFocus() {
                JListControlPane.this.updateControlUpdatePane();
            }
        };
        nameEdList.setCellRenderer(new NameableListCellRenderer());
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
            addItemShortCut = new AddItemMenuDef(creators);
        }
        return new AbsoluteEnableShortCut(addItemShortCut);
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
        DefaultListModel listModel = (DefaultListModel) this.nameableList.getModel();
        listModel.removeAllElements();
        if (ArrayUtils.isEmpty(nameableArray)) {
            return;
        }

        for (Nameable aNameableArray : nameableArray) {
            listModel.addElement(new ListModelElement(aNameableArray));
        }

        if (listModel.size() > 0) {
            this.nameableList.setSelectedIndex(0);
        }
        this.checkButtonEnabled();
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
    public void addEditingListener(PropertyChangeAdapter l) {
        this.nameableList.addEditingListner(l);
    }

    /*
     * 刷新当前的选中的UpdatePane
     */
    protected void populateSelectedValue() {
        ((JControlUpdatePane) JListControlPane.this.controlUpdatePane).populate();
    }

    /**
     * 根据name,选中JNameEdList中的item
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

    public void setIllegalIndex(int index) {
        this.nameableList.setIllegalIndex(index);
    }

    /**
     * 获取选中的名字
     */
    public String getSelectedName() {
        ListModelElement el = (ListModelElement) this.nameableList.getSelectedValue();

        return el == null ? null : el.wrapper.getName();
    }

    protected boolean isNameRepeated(java.util.List[] list, String name) {
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
        JNameEdList nameEdList = JListControlPane.this.nameableList;
        DefaultListModel model = (DefaultListModel) nameEdList.getModel();

        ListModelElement el = new ListModelElement(nameable);
        model.add(index, el);
        nameableList.setSelectedIndex(index);
        nameableList.ensureIndexIsVisible(index);

        nameEdList.repaint();
    }

    /**
     * 是否重命名
     *
     * @return 是则true
     */
    public boolean isContainsRename() {
        String rename = com.fr.design.i18n.Toolkit.i18nText("FR-Please_Rename") + "!";
        String[] names = this.nameableList.getAllNames();
        for (int i = names.length - 1; i >= 0; i--) {
            if (ComparatorUtils.equals(names[i], rename)) {
                return true;
            }
        }
        return false;
    }

    protected DefaultListModel getModel() {
        return (DefaultListModel) JListControlPane.this.nameableList.getModel();
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

    /**
     * 增加项的UpdateAction
     */
    protected class AddItemUpdateAction extends UpdateAction {
        final NameableCreator creator;

        public AddItemUpdateAction(NameableCreator[] creators) {
            this.creator = creators[0];
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Action_Add"));
            this.setMnemonic('A');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Nameable nameable = creator.createNameable(JListControlPane.this);

            JListControlPane.this.addNameable(nameable, getModel().getSize());
        }
    }

    /*
     * 增加项的MenuDef
     */
    protected class AddItemMenuDef extends MenuDef {
        public AddItemMenuDef(NameableCreator[] creators) {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Action_Add"));
            this.setMnemonic('A');
            this.setIconPath("/com/fr/design/images/control/addPopup.png");
            wrapActionListener(creators);
        }

        private void wrapActionListener(NameableCreator[] creators) {
            for (final NameableCreator creator : creators) {
                if (filterNameableCreator(creator)) {
                    continue;
                }
                boolean isTrue = ComparatorUtils.equals(creator.menuName(), com.fr.design.i18n.Toolkit.i18nText("Datasource-Stored_Procedure")) ||
                        ComparatorUtils.equals(creator.menuName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Relation_TableData")) || ComparatorUtils.equals(creator.menuName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Multi_Dimensional_Database"));
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

                        Nameable nameable = creator.createNameable(JListControlPane.this);

                        JListControlPane.this.addNameable(nameable, getModel().getSize());
                    }
                });
            }
        }
    }

    /*
     * 移除item
     */
    private class RemoveItemAction extends UpdateAction {
        public RemoveItemAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Action_Remove"));
            this.setMnemonic('R');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/base/images/cell/control/remove.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                JListControlPane.this.nameableList.getCellEditor()
                        .stopCellEditing();
            } catch (Exception ignored) {
            }
            // bug:在选中一个NameObject并删除，会遗留下Name.
            doBeforeRemove();
            if (GUICoreUtils.removeJListSelectedNodes(SwingUtilities
                    .getWindowAncestor(JListControlPane.this), nameableList)) {
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
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Action_Copy"));
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

                JListControlPane.this.addNameable(newNameable, nameableList.getSelectedIndex() + 1);
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
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Utils-Move_Up"));
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
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Utils-Move_Down"));
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
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Action_Sort"));
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
     * JNameEdList的鼠标事件
     */
    private MouseListener listMouseListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent evt) {
            nameableList.stopEditing();
            if (evt.getClickCount() >= 2
                    && SwingUtilities.isLeftMouseButton(evt)) {
                editingIndex = nameableList.getSelectedIndex();
                selectedName = nameableList.getNameAt(editingIndex);
                nameableList.editItemAt(nameableList.getSelectedIndex());
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

    private class NameableListCellRenderer extends
            DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);

            if (value instanceof ListModelElement) {
                ListModelElement element = ((ListModelElement) value);
                Nameable nameable = element.wrapper;
                this.setText(nameable.getName());
                boolean iconSet = false;
                for (NameableCreator creator : JListControlPane.this.creators()) {
                    if (creator.menuIcon() != null && creator.acceptObject2Populate(nameable) != null) {
                        this.setIcon(creator.menuIcon());
                        this.setToolTipText(creator.createTooltip());
                        iconSet = true;
                        break;
                    }
                }
                if (!iconSet) {
                    this.setIcon(IOUtils.readIcon("/com/fr/base/images/oem/cpt.png"));
                }
            }
            return this;
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
                    && JListControlPane.this.nameableList.getSelectedIndex() != -1);
        }
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
            ListModelElement el = (ListModelElement) JListControlPane.this.nameableList.getSelectedValue();
            if (el == null) {
                return;
            }

            elEditing = el;
            NameableCreator[] creators = creators();

            for (int i = 0, len = updatePanes.length; i < len; i++) {
                Object ob2Populate = creators[i].acceptObject2Populate(el.wrapper);
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
        return Reflect.on(creator.getUpdatePane()).create().get();
    }

    private BasicBeanPane createPaneByCreators(NameableCreator creator, String string) {
        return Reflect.on(creator.getUpdatePane()).create(string).get();
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
        int idx = JListControlPane.this.getInValidIndex();
        if (isAdd || nameableList.getSelectedIndex() != idx) {
            try {
                checkValid();
            } catch (Exception exp) {
                JOptionPane.showMessageDialog(JListControlPane.this, exp.getMessage());
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


    /**
     * 用于在list面板中，过滤某些下拉选项
     * @return true：过滤掉这个creator
     */
    protected boolean filterNameableCreator(NameableCreator creator) {
        return !HyperlinkFilterHelper.whetherAddHyperlink4cell(creator.menuName());
    }
}
