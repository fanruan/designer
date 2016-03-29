package com.fr.design.gui.controlpane;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.data.tabledata.tabledatapane.GlobalMultiTDTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.GlobalTreeTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.MultiTDTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.TreeTableDataPane;
import com.fr.design.actions.UpdateAction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.ilist.ModNameActionListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.*;
import com.fr.design.dialog.BasicPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

public abstract class JControlPane extends BasicPane implements UnrepeatedNameHelper {
    public static final String LIST_NAME = "JControl_List";
    private static final int SHORT_WIDTH = 30; //每加一个short Divider位置加30

    private ShortCut4JControlPane[] shorts;
    private NameableCreator[] creators;
    protected JNameEdList nameableList;
    protected JControlUpdatePane controlUpdatePane;

    private ToolBarDef toolbarDef;
    private UIToolbar toolBar;

    // peter:这是整体的一个cardLayout Pane
    private CardLayout cardLayout;
    private JPanel cardPane;
    protected int editingIndex;
    protected String selectedName;
    private boolean isNameRepeated = false;

    public JControlPane() {
        this.initComponentPane();
    }

    /**
     * 生成添加按钮的NameableCreator
     * @return 按钮的NameableCreator
     */
    public abstract NameableCreator[] createNameableCreators();

    protected void initComponentPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.creators = this.createNameableCreators();
        this.controlUpdatePane = new JControlUpdatePane();

        // p: edit card layout
        this.cardLayout = new CardLayout();
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardPane.setLayout(this.cardLayout);
        // p:选择的Label
        UILabel selectLabel = new UILabel();
        cardPane.add(selectLabel, "SELECT");
        cardPane.add(controlUpdatePane, "EDIT");
        // SplitPane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, getLeftPane(), cardPane);
        mainSplitPane.setBorder(BorderFactory.createLineBorder(GUICoreUtils.getTitleLineBorderColor()));
        mainSplitPane.setOneTouchExpandable(true);

        this.add(mainSplitPane, BorderLayout.CENTER);
        mainSplitPane.setDividerLocation(getLeftPreferredSize());
        this.checkButtonEnabled();
    }


    protected JPanel getLeftPane(){
               // LeftPane
        JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        initNameList(leftPane);

        shorts = this.createShortcuts();
        if (ArrayUtils.isEmpty(shorts)) {
            return leftPane;
        }

        toolbarDef = new ToolBarDef();
        for (ShortCut4JControlPane sj : shorts) {
            toolbarDef.addShortCut(sj.getShortCut());
        }
        toolBar = ToolBarDef.createJToolBar();
        toolbarDef.updateToolBar(toolBar);
        leftPane.add(toolBar, BorderLayout.NORTH);
        return leftPane;
    }


    private void initNameList(JPanel leftPane) {
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
                    JControlPane.this.controlUpdatePane.update();
                    JControlPane.this.controlUpdatePane.populate();
                    JControlPane.this.checkButtonEnabled();
                }
            }
        });
    }

    public JNameEdList createJNameList() {
        JNameEdList nameEdList = new JNameEdList(new DefaultListModel()) {
            protected void doAfterLostFocus() {
                JControlPane.this.updateControlUpdatePane();
            }
        };
        nameEdList.setCellRenderer(new NameableListCellRenderer());
        return nameEdList;
    }

    public void updateControlUpdatePane() {
        controlUpdatePane.update();
    }

    protected void doWhenPopulate(BasicBeanPane beanPane){

    }

    protected int getLeftPreferredSize() {
        return shorts.length * SHORT_WIDTH;
    }

    protected ShortCut4JControlPane[] createShortcuts() {
        return new ShortCut4JControlPane[]{
                addItemShortCut(),
                removeItemShortCut(),
                copyItemShortCut(),
                moveUpItemShortCut(),
                moveDownItemShortCut(),
                sortItemShortCut()
        };
    }

    protected ShortCut4JControlPane addItemShortCut() {
        ShortCut addItemShortCut;
        if (creators.length == 1) {
            addItemShortCut = new AddItemUpdateAction(creators);
        } else {
            addItemShortCut = new AddItemMenuDef(creators);
        }
        return new AbsoluteEnableShortCut(addItemShortCut);
    }

    protected ShortCut4JControlPane removeItemShortCut() {
        return new NormalEnableShortCut(new RemoveItemAction());
    }

    protected ShortCut4JControlPane copyItemShortCut() {
        return new NormalEnableShortCut(new CopyItemAction());
    }

    protected ShortCut4JControlPane moveUpItemShortCut() {
        return new NormalEnableShortCut(new MoveUpItemAction());
    }

    protected ShortCut4JControlPane moveDownItemShortCut() {
        return new NormalEnableShortCut(new MoveDownItemAction());
    }

    protected ShortCut4JControlPane sortItemShortCut() {
        return new NormalEnableShortCut(new SortItemAction());
    }

    public void setNameListEditable(boolean editable) {
        this.nameableList.setEditable(editable);
    }

    public Nameable[] update() {
        java.util.List<Nameable> res = new java.util.ArrayList<Nameable>();
        this.controlUpdatePane.update();
        DefaultListModel listModel = (DefaultListModel) this.nameableList.getModel();
        for (int i = 0, len = listModel.getSize(); i < len; i++) {
            res.add(((ListModelElement) listModel.getElementAt(i)).wrapper);
        }

        return res.toArray(new Nameable[res.size()]);
    }

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
    public void addEditingListner(PropertyChangeAdapter l) {
        this.nameableList.addEditingListner(l);
    }

    /*
     * 刷新当前的选中的UpdatePane
     */
    protected void populateSelectedValue() {
        JControlPane.this.controlUpdatePane.populate();
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

    /**
     * 刷新 NameableCreator
     *
     * @param creators 生成器
     */
    public void refreshNameableCreator(NameableCreator[] creators) {
        this.creators = creators;
        shorts = this.createShortcuts();
        toolbarDef.clearShortCuts();
        for (ShortCut4JControlPane sj : shorts) {
            toolbarDef.addShortCut(sj.getShortCut());
        }

        toolbarDef.updateToolBar(toolBar);
        toolBar.validate();
        toolBar.repaint();
        this.repaint();
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
     * @return    重复则返回true
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
        JNameEdList nameEdList = JControlPane.this.nameableList;
        DefaultListModel model = (DefaultListModel) nameEdList.getModel();

        ListModelElement el = new ListModelElement(nameable);
        model.add(index, el);
        nameableList.setSelectedIndex(index);
        nameableList.ensureIndexIsVisible(index);

        nameEdList.repaint();
    }

    /**
     * 是否重命名
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
        return (DefaultListModel) JControlPane.this.nameableList.getModel();
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
            this.setName(Inter.getLocText("FR-Action_Add"));
            this.setMnemonic('A');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
        }

        public void actionPerformed(ActionEvent e) {
            Nameable nameable = creator.createNameable(JControlPane.this);

            JControlPane.this.addNameable(nameable, getModel().getSize());
        }
    }

    /*
     * 增加项的MenuDef
     */
    protected class AddItemMenuDef extends MenuDef {
        public AddItemMenuDef(NameableCreator[] creators) {
            this.setName(Inter.getLocText("FR-Action_Add"));
            this.setMnemonic('A');
            this.setIconPath("/com/fr/design/images/control/addPopup.png");
            wrapActionListener(creators);
        }

        private void wrapActionListener(NameableCreator[] creators) {
            for (final NameableCreator creator : creators) {
                if (!whetherAdd(creator.menuName())){
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

                    public void actionPerformed(ActionEvent e) {
                        if (hasInvalid(true)) {
                            return;
                        }

                        Nameable nameable = creator.createNameable(JControlPane.this);

                        JControlPane.this.addNameable(nameable, getModel().getSize());
                    }
                });
            }
        }

        private boolean whetherAdd(String itemName){
            JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            if (jTemplate == null) {
                return  false;
            }
            //先屏蔽掉这个，之后还有别的
            String[] names = {Inter.getLocText("FR-Hyperlink_Chart_Float")};
            for (String name : names){
                if(!jTemplate.isJWorkBook() && ComparatorUtils.equals(itemName, name)){
                    return false;
                }
            }
            String formName = Inter.getLocText("Hyperlink-Form_link");
            return !(jTemplate.isJWorkBook() && ComparatorUtils.equals(itemName, formName));
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

        public void actionPerformed(ActionEvent evt) {
            try {
                JControlPane.this.nameableList.getCellEditor()
                        .stopCellEditing();
            } catch (Exception ignored) {
            }
            // bug:在选中一个NameObject并删除，会遗留下Name.
            doBeforeRemove();
            if (GUICoreUtils.removeJListSelectedNodes(SwingUtilities
                    .getWindowAncestor(JControlPane.this), nameableList)) {
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
                    .readIcon("/com/fr/base/images/cell/control/copy.png"));
        }

        public void actionPerformed(ActionEvent evt) {
            // p:选中的值.
            ListModelElement selectedValue = (ListModelElement) nameableList.getSelectedValue();
            if (selectedValue == null) {
                return;
            }

            controlUpdatePane.update();

            Nameable selectedNameable = selectedValue.wrapper;

            // p: 用反射机制实现
            try {
                Nameable newNameable = (Nameable) BaseUtils.cloneObject(selectedNameable);
                newNameable.setName(createUnrepeatedCopyName(selectedNameable.getName()));

                JControlPane.this.addNameable(newNameable, nameableList.getSelectedIndex() + 1);
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
                    public int compare(Nameable o1, Nameable o2) {
                        return -ComparatorUtils.compare(o1.getName(), o2
                                .getName());
                    }
                };
                isAtoZ = !isAtoZ;
                Arrays.sort(nameableArray, nameableComparator);
            } else {
                Comparator<Nameable> nameableComparator = new Comparator<Nameable>() {
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

            for (ShortCut4JControlPane sj : shorts) {
                sj.getShortCut().intoJPopupMenu(popupMenu);
            }

            // peter: 只有弹出菜单有子菜单的时候,才需要弹出来.
            GUICoreUtils.showPopupMenu(popupMenu, nameableList, evt.getX() - 1,
                    evt.getY() - 1);
        }

        public void mouseMoved(MouseEvent e) {

        }
    };

    /**
     * 检查按钮可用状态 Check button enabled.
     */
    public void checkButtonEnabled() {
        int selectedIndex = nameableList.getSelectedIndex();
        if (selectedIndex == -1) {
            this.cardLayout.show(cardPane, "SELECT");
        } else {
            this.cardLayout.show(cardPane, "EDIT");
        }
        for (ShortCut4JControlPane sj : this.shorts) {
            sj.checkEnable();
        }
    }

    protected void doBeforeRemove(){

    }

    protected void doAfterRemove(){

    }

    public NameableCreator[] creators() {
        return creators == null ? new NameableCreator[0] : creators;
    }

    /*
     * Nameable的ListCellRenerer
     */
    private class NameableListCellRenderer extends
            DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);

            if (value instanceof ListModelElement) {
                Nameable wrappee = ((ListModelElement) value).wrapper;
                this.setText(((ListModelElement) value).wrapper.getName());

                boolean iconSet = false;
                for (NameableCreator creator : JControlPane.this.creators) {
                    if (creator.menuIcon() != null && creator.acceptObject2Populate(wrappee) != null) {
                        this.setIcon(creator.menuIcon());
                        this.setToolTipText(creator.createTooltip());
                        iconSet = true;
                        break;
                    }
                }
                if (!iconSet) {
                    this.setIcon(BaseUtils.readIcon("/com/fr/base/images/oem/cpt.png"));
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
        public void checkEnable() {
            this.shortCut.setEnabled(getModel()
                    .getSize() > 0
                    && JControlPane.this.nameableList.getSelectedIndex() != -1);
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
                    && JControlPane.this.nameableList.getSelectedIndex() > 0);
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
                    && JControlPane.this.nameableList.getSelectedIndex() < JControlPane.this.nameableList.getModel().getSize() - 1);
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
            ListModelElement el = (ListModelElement) JControlPane.this.nameableList.getSelectedValue();
            if (el == null) {
                return;
            }

            elEditing = el;

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
                    updatePanes[i].populateBean(ob2Populate);
                    doWhenPopulate(updatePanes[i]);
                    break;
                }
            }
        }

        public boolean isMulti(Class _class){
            return ComparatorUtils.equals(_class, GlobalMultiTDTableDataPane.class) || ComparatorUtils.equals(_class, MultiTDTableDataPane.class);
        }

        public boolean isTree(Class _class){
            return ComparatorUtils.equals(_class, GlobalTreeTableDataPane.class) || ComparatorUtils.equals(_class, TreeTableDataPane.class);
        }

        public void update() {
            for (int i = 0; i < updatePanes.length; i++) {
                BasicBeanPane pane = updatePanes[i];

                if (pane != null && pane.isVisible()) {
                    Object bean = pane.updateBean();
                    if (i < JControlPane.this.creators.length) {
                        JControlPane.this.creators[i].saveUpdatedBean(elEditing, bean);
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
        if (this.toolbarDef.getShortCutCount() > shortCutIndex) {
            ShortCut sc = this.toolbarDef.getShortCut(shortCutIndex);
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
    public void checkValid() throws Exception {
        this.controlUpdatePane.checkValid();
    }

    private int getInValidIndex() {
        BasicBeanPane[] p = controlUpdatePane.updatePanes;
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

    private boolean hasInvalid(boolean isAdd) {
        int idx = JControlPane.this.getInValidIndex();
        if (isAdd || nameableList.getSelectedIndex() != idx) {
            try {
                checkValid();
            } catch (Exception exp) {
                JOptionPane.showMessageDialog(JControlPane.this, exp.getMessage());
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