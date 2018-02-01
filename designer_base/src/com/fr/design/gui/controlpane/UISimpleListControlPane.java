package com.fr.design.gui.controlpane;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.ilist.UIList;
import com.fr.design.gui.itoolbar.UIToolBarUI;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 简单列表面板
 * Created by plough on 2018/2/1.
 */
public class UISimpleListControlPane extends BasicPane {
    public static final String LIST_NAME = "UISimpleControl_List";

    protected UIList nameList;
    protected String selectedName;
    private ShortCut4JControlPane[] shorts;
    private ToolBarDef toolbarDef;
    private UIToolbar toolBar;

    public UISimpleListControlPane() {
        initComponentPane();
    }

    public ShortCut4JControlPane[] getShorts() {
        return shorts;
    }

    protected void initComponentPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(getContentPane(), BorderLayout.CENTER);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
        this.checkButtonEnabled();
    }

    protected JPanel getContentPane() {
        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel listPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        initListPane(listPane);
        contentPane.add(listPane, BorderLayout.CENTER);

        shorts = this.createShortcuts();
        if (ArrayUtils.isEmpty(shorts)) {
            return contentPane;
        }

        toolbarDef = new ToolBarDef();
        for (ShortCut4JControlPane sj : shorts) {
            toolbarDef.addShortCut(sj.getShortCut());
        }
        toolBar = ToolBarDef.createJToolBar();
        toolBar.setUI(new UIToolBarUI(){
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });
        toolbarDef.updateToolBar(toolBar);
        // 封装一层，加边框
        JPanel toolBarPane = new JPanel(new BorderLayout());
        toolBarPane.add(toolBar, BorderLayout.CENTER);
        toolBarPane.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, UIConstants.RULER_LINE_COLOR));

        listPane.add(toolBarPane, BorderLayout.NORTH);

        return contentPane;
    }

    protected ShortCut4JControlPane[] createShortcuts() {
        return new ShortCut4JControlPane[]{
                moveUpItemShortCut(),
                moveDownItemShortCut(),
                sortItemShortCut(),
        };
    }

    protected void initListPane(JPanel listPane) {
        nameList = createJNameList();
        nameList.setName(LIST_NAME);
        nameList.setSelectionBackground(UIConstants.ATTRIBUTE_PRESS);
        listPane.add(new UIScrollPane(nameList), BorderLayout.CENTER);


        nameList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        nameList.addMouseListener(listMouseListener);
        nameList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                // richie:避免多次update和populate大大降低效率
                if (!evt.getValueIsAdjusting()) {
                    UISimpleListControlPane.this.checkButtonEnabled();
                }
            }
        });
    }

    public UIList createJNameList() {
        UIList nameList = new UIList(new DefaultListModel()) {
            @Override
            public int locationToIndex(Point location) {
                int index = super.locationToIndex(location);
                if (index != -1 && !getCellBounds(index, index).contains(location)) {
                    return -1;
                }
                else {
                    return index;
                }
            }
        };
        nameList.setCellRenderer(new NameableListCellRenderer(this));
        return nameList;
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

    public Nameable[] update() {
        java.util.List<Nameable> res = new java.util.ArrayList<Nameable>();
        DefaultListModel listModel = (DefaultListModel) this.nameList.getModel();
        for (int i = 0, len = listModel.getSize(); i < len; i++) {
            res.add(((ListModelElement) listModel.getElementAt(i)).wrapper);
        }

        return res.toArray(new Nameable[res.size()]);
    }

    public void populate(Nameable[] nameableArray) {
        DefaultListModel listModel = (DefaultListModel) this.nameList.getModel();
        listModel.removeAllElements();
        if (ArrayUtils.isEmpty(nameableArray)) {
            return;
        }

        listModel.setSize(nameableArray.length);
        for (int i = 0; i < nameableArray.length; i++) {
            listModel.set(i, new ListModelElement(nameableArray[i]));
        }
        if (listModel.size() > 0 || this.nameList.getSelectedIndex() != 0) {
            this.nameList.setSelectedIndex(0);
        }
        this.checkButtonEnabled();
    }

    /**
     * 根据name,选中UINameEdList中的item
     */
    public void setSelectedName(String name) {
        DefaultListModel listModel = (DefaultListModel) this.nameList.getModel();
        for (int i = 0, len = listModel.getSize(); i < len; i++) {
            Nameable item = ((ListModelElement) listModel.getElementAt(i)).wrapper;
            if (ComparatorUtils.equals(name, item.getName())) {
                this.nameList.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * 获取选中的名字
     */
    public String getSelectedName() {
        ListModelElement el = (ListModelElement) this.nameList.getSelectedValue();

        return el == null ? null : el.wrapper.getName();
    }

    protected DefaultListModel getModel() {
        return (DefaultListModel) this.nameList.getModel();
    }

    @Override
    protected String title4PopupWindow() {
        return null;
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
            int selectedIndex = nameList.getSelectedIndex();
            if (selectedIndex == -1) {
                return;
            }

            // 上移
            if (selectedIndex > 0) {
                DefaultListModel listModel = (DefaultListModel) nameList.getModel();

                Object prevObj = listModel.get(selectedIndex - 1);
                Object currentObj = listModel.get(selectedIndex);
                listModel.set(selectedIndex - 1, currentObj);
                listModel.set(selectedIndex, prevObj);

                nameList.setSelectedIndex(selectedIndex - 1);
                nameList.ensureIndexIsVisible(selectedIndex - 1);
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
            int selectedIndex = nameList.getSelectedIndex();
            if (selectedIndex == -1) {
                return;
            }

            if (selectedIndex < nameList.getModel().getSize() - 1) {
                DefaultListModel listModel = (DefaultListModel) nameList.getModel();

                Object nextObj = listModel.get(selectedIndex + 1);
                Object currentObj = listModel.get(selectedIndex);
                listModel.set(selectedIndex + 1, currentObj);
                listModel.set(selectedIndex, nextObj);

                nameList.setSelectedIndex(selectedIndex + 1);
                nameList.ensureIndexIsVisible(selectedIndex + 1);
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
            Object selectedValue = nameList.getSelectedValue();

            DefaultListModel listModel = (DefaultListModel) nameList.getModel();
            if (listModel.getSize() <= 0) {
                return;
            }
            Nameable[] nameableArray = new Nameable[listModel.getSize()];

            for (int i = 0; i < listModel.getSize(); i++) {
                nameableArray[i] = ((ListModelElement) listModel.getElementAt(i)).wrapper;
            }

            // p:排序.
            if (isAtoZ) {  // 升序
                Comparator<Nameable> nameableComparator = new Comparator<Nameable>() {
                    @Override
                    public int compare(Nameable o1, Nameable o2) {
                        return ComparatorUtils.compare(o2.getName(), o1.getName());
                    }
                };
                isAtoZ = !isAtoZ;
                Arrays.sort(nameableArray, nameableComparator);
            } else {  // 降序
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
                nameList.setSelectedValue(selectedValue, true);
            }

            checkButtonEnabled();
            // p:需要repaint.
            nameList.repaint();
        }
    }

    /*
     * UIList的鼠标事件
     */
    private MouseListener listMouseListener = new MouseAdapter() {
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
    };

    /**
     * 检查按钮可用状态 Check button enabled.
     */
    public void checkButtonEnabled() {
        for (ShortCut4JControlPane sj : getShorts()) {
            sj.checkEnable();
        }
    }

    /**
     * 设置选中项
     *
     * @param index 选中项的序列号
     */
    public void setSelectedIndex(int index) {
        nameList.setSelectedIndex(index);
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
                    && UISimpleListControlPane.this.nameList.getSelectedIndex() != -1);
        }
    }


    private class NameableListCellRenderer extends
            JPanel implements ListCellRenderer {

        private UILabel label;
        private UISimpleListControlPane listControlPane;
        private Color initialLabelForeground;

        public NameableListCellRenderer(UISimpleListControlPane listControlPane) {
            super();
            this.listControlPane = listControlPane;
            initComponents();
            setOpaque(true);
            setBorder(getNoFocusBorder());
            setName("List.cellRenderer");
        }

        private void initComponents() {
            label = new UILabel();
            label.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 0));
            initialLabelForeground = label.getForeground();
            this.setLayout(new BorderLayout());
            this.add(label, BorderLayout.CENTER);
        }

        private Border getNoFocusBorder() {
            return BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.LIST_ITEM_SPLIT_LINE);
        }

        private void setText(String t) {
            label.setText(t);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            setComponentOrientation(list.getComponentOrientation());

            Color bg = null;
            Color fg = null;

            JList.DropLocation dropLocation = list.getDropLocation();
            if (dropLocation != null
                    && !dropLocation.isInsert()
                    && dropLocation.getIndex() == index) {

                bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
                fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");

                isSelected = true;
            }

            if (isSelected) {
                setBackground(bg == null ? list.getSelectionBackground() : bg);
                setForeground(fg == null ? list.getSelectionForeground() : fg);
                label.setForeground(Color.WHITE);
            }
            else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
                label.setForeground(initialLabelForeground);
            }

            setText((value == null) ? StringUtils.EMPTY : value.toString());

            setEnabled(list.isEnabled());
            setFont(list.getFont());

            if (value instanceof ListModelElement) {
                Nameable wrappee = ((ListModelElement) value).wrapper;
                this.setText(wrappee.getName());
            }

            return this;
        }
    }
}
