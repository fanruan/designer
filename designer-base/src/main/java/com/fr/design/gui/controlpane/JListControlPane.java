package com.fr.design.gui.controlpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.ilist.ModNameActionListener;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.invoke.Reflect;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class JListControlPane extends JControlPane implements ListControlPaneProvider {
    private static final String LIST_NAME = "JControl_List";

    protected JNameEdList nameableList;
    protected int editingIndex;
    protected String selectedName;
    private boolean isNameRepeated = false;
    private CommonShortCutHandlers commonHandlers;
    private ListControlPaneHelper helper;

    public JListControlPane() {
        this.initComponentPane();
    }

    @Override
    protected JPanel createControlUpdatePane() {
        return JControlUpdatePane.newInstance(this);
    }
    
    private ListControlPaneHelper getHelper() {
        if (helper == null) {
            helper = ListControlPaneHelper.newInstance(this);
        }
        return helper;
    }
    
    private CommonShortCutHandlers getCommonHandlers() {
        if (commonHandlers == null) {
            commonHandlers = CommonShortCutHandlers.newInstance(this);
        }
        return commonHandlers;
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

    protected JNameEdList createJNameList() {
        JNameEdList nameEdList = new JNameEdList(new DefaultListModel()) {
            @Override
            protected void doAfterLostFocus() {
                JListControlPane.this.updateControlUpdatePane();
            }
        };
        nameEdList.setCellRenderer(new NameableListCellRenderer());
        return nameEdList;
    }

    private void updateControlUpdatePane() {
        ((JControlUpdatePane) controlUpdatePane).update();
    }

    @Override
    public Nameable[] update() {
        return getHelper().update();
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
    protected void addModNameActionListener(ModNameActionListener l) {
        this.nameableList.addModNameActionListener(l);
    }

    /**
     * 添加Editinglistener
     *
     * @param l 监听
     */
    protected void addEditingListener(PropertyChangeAdapter l) {
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
        return getHelper().getSelectedName();
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
        getHelper().addNameable(nameable, index);
    }

    /**
     * 是否重命名
     *
     * @return 是则true
     */
    public boolean isContainsRename() {
        String rename = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Please_Rename") + "!";
        String[] names = this.nameableList.getAllNames();
        for (int i = names.length - 1; i >= 0; i--) {
            if (ComparatorUtils.equals(names[i], rename)) {
                return true;
            }
        }
        return false;
    }

    public DefaultListModel getModel() {
        return (DefaultListModel) JListControlPane.this.nameableList.getModel();
    }

    /**
    * 生成不重复的名字
    *
    * @param prefix 名字前缀
    * @return 名字
    */
    @Override
    public String createUnrepeatedName(String prefix) {
        return getCommonHandlers().createUnrepeatedName(prefix);
    }

    @Override
    public void onAddItem(NameableCreator creator) {
        getCommonHandlers().onAddItem(creator);
    }

    @Override
    public void onRemoveItem() {
        getCommonHandlers().onRemoveItem();
    }

    @Override
    public void onCopyItem() {
        getCommonHandlers().onCopyItem();
    }

    @Override
    public void onMoveUpItem() {
        getCommonHandlers().onMoveUpItem();
    }

    @Override
    public void onMoveDownItem() {
        getCommonHandlers().onMoveDownItem();
    }

    @Override
    public void onSortItem(boolean isAtoZ) {
        getCommonHandlers().onSortItem(isAtoZ);
    }

    @Override
    public boolean isItemSelected() {
        return getModel().getSize() > 0 && nameableList.getSelectedIndex() != -1;
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
        getHelper().checkButtonEnabled();
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

    @Override
    public BasicBeanPane createPaneByCreators(NameableCreator creator) {
        return Reflect.on(creator.getUpdatePane()).create().get();
    }

    @Override
    public BasicBeanPane createPaneByCreators(NameableCreator creator, String string) {
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

    @Override
    public boolean hasInvalid(boolean isAdd) {
        return getHelper().hasInvalid(isAdd);
    }

    /**
     * 设置选中项
     *
     * @param index 选中项的序列号
     */
    @Override
    public void setSelectedIndex(int index) {
        nameableList.setSelectedIndex(index);
    }

    @Override
    public int getSelectedIndex() {
        return nameableList.getSelectedIndex();
    }

    @Override
    public JNameEdList getNameableList() {
        return nameableList;
    }

    @Override
    public ListModelElement getSelectedValue() {
        return (ListModelElement) this.nameableList.getSelectedValue();
    }

    @Override
    public JControlUpdatePane getControlUpdatePane() {
        return (JControlUpdatePane) controlUpdatePane;
    }
}
