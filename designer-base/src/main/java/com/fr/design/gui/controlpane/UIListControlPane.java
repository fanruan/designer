package com.fr.design.gui.controlpane;

import com.fr.base.chart.BasePlot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.ilist.UINameEdList;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;

import javax.swing.DefaultListModel;
import javax.swing.JList;
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

/**
 * Created by plough on 2017/7/19.
 */

public abstract class UIListControlPane extends UIControlPane implements ListControlPaneProvider {
    private static final String LIST_NAME = "UIControl_List";
    private static final int EDIT_RANGE = 25;  // 编辑按钮的x坐标范围

    protected UINameEdList nameableList;
    private int editingIndex;
    protected String selectedName;
    protected boolean isPopulating = false;
    private CommonShortCutHandlers commonHandlers;
    private ListControlPaneHelper helper;


    public UIListControlPane() {
        super();

    }

    public UIListControlPane(BasePlot plot) {
        super(plot);
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
        nameableList.addMouseListener(getListMouseListener());
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

    private UINameEdList createJNameList() {
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

    private void updateControlUpdatePane() {
        ((JControlUpdatePane) controlUpdatePane).update();
    }

    protected void setNameListEditable(boolean editable) {
        this.nameableList.setEditable(editable);
    }

    @Override
    public Nameable[] update() {
        return getHelper().update();
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
       return getHelper().getSelectedName();
    }

    /**
     * 添加 Nameable
     *
     * @param nameable 添加的Nameable
     * @param index    序号
     */
    public void addNameable(Nameable nameable, int index) {
        getHelper().addNameable(nameable, index);
        popupEditDialog();
    }

    public DefaultListModel getModel() {
        return (DefaultListModel) UIListControlPane.this.nameableList.getModel();
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
     * UINameEdList的鼠标事件
     */
    private MouseListener getListMouseListener() {
        return new MouseAdapter() {
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
    }

    /**
     * 检查按钮可用状态 Check button enabled.
     */
    @Override
    public void checkButtonEnabled() {
        getHelper().checkButtonEnabled();
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

    @Override
    public boolean hasInvalid(boolean isAdd) {
        return getHelper().hasInvalid(isAdd);
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
    public int getSelectedIndex() {
        return nameableList.getSelectedIndex();
    }

    @Override
    public ListModelElement getSelectedValue() {
        return (ListModelElement) this.nameableList.getSelectedValue();
    }

    @Override
    public JControlUpdatePane getControlUpdatePane() {
        return (JControlUpdatePane) controlUpdatePane;
    }

    @Override
    public JNameEdList getNameableList() {
        return nameableList;
    }
}
