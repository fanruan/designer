package com.fr.design.gui.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.fr.base.FRContext;
import com.fr.design.gui.icombobox.filter.Filter;
import com.fr.design.gui.icombobox.ComboCheckBox;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.ExtendedComboBox;
import com.fr.design.gui.icombobox.FRTreeComboBox;
import com.fr.design.gui.icombobox.FilterComboBox;
import com.fr.design.gui.icombobox.LazyComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Richer
 * Date: 11-6-30
 * Time: 下午8:21
 */
public class ComboBoxDemo extends JPanel {
    public ComboBoxDemo() {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        Component[][] coms = new Component[][]{
                {new UILabel(Inter.getLocText("Form-ComboCheckBox")+":"), createComboCheckBox()},
                {new UILabel(Inter.getLocText(new String[]{"DS-Dictionary", "Form-ComboBox"})+":"), createDictComboBox()},
                {new UILabel(Inter.getLocText("long_data_can_not_show_fully")+":"), createExtendedComboBox()},
                {new UILabel(Inter.getLocText(new String[]{"Filter", "Form-ComboBox"})+":"), createFilterComboBox()},
                {new UILabel(Inter.getLocText("Form-ComboBox")+":"), createTreeComboBox()},
                {new UILabel(Inter.getLocText(new String[]{"Delay", "Load", "Form-ComboBox"})+":"), createLazyComboBox()}
        };
        double[] rowSize = new double[coms.length];
        double[] columnSize = {p, f};
        for (int i = 0; i < rowSize.length; i++) {
            rowSize[i] = p;
        }
        JPanel centerPane = TableLayoutHelper.createTableLayoutPane(coms, rowSize, columnSize);
        add(centerPane, BorderLayout.CENTER);
    }

    private ComboCheckBox createComboCheckBox() {
        ComboCheckBox ccb = new ComboCheckBox(new Object[]{"张三", "李四", "王五", "赵六"});
        ccb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    if (value instanceof Object[]) {
                        Object[] objs = (Object[]) value;
                        String[] res = new String[objs.length];
                        for (int i = 0, len = objs.length; i < len; i++) {
                            res[i] = objs[i].toString();
                        }
                        setText(StringUtils.join(",", res));
                    }
                }
                return this;
            }
        });
        return ccb;
    }

    private DictionaryComboBox createDictComboBox() {
        DictionaryComboBox dcb = new DictionaryComboBox(
                new Integer[]{1, 2, 3, 4},
                new String[]{"一", "二", "三", "四"}
        );
        return dcb;
    }

    private ExtendedComboBox createExtendedComboBox() {
        ExtendedComboBox ecb = new ExtendedComboBox(new String[]{
                "Hello   world,   alksdfjlaskdjflaskjdflaksdf",
                "Hello   world,   alksdfjlaskdjflaskjdflaksdfasdfklajsdflkasjdflkasdf",
                "Hello   world,   alksdfjlaskdjflaskjdflaksdfasdfklajsdflkasjdflkasdfaaaaaaaaaaaaaaaaaadfsdf",
                "Hello   world,   太长了，都看不全了jlaskdjflaskjdflaksdfasdfklajsdflkasjdflkasdfsdfgklsdjfgklsdfjgklsjdfgkljsdflkgjsdlfk"});
        return ecb;
    }

    private FilterComboBox createFilterComboBox() {
        ArrayList items = new ArrayList();
        items.add("abc");
        items.add("abcd");
        items.add("dfee");
        items.add("asdfg");
        items.add("cdefg");
        Filter filter = new Filter() {
            @Override
            public boolean accept(String prefix, Object object) {
                return prefix == null || object != null &&
                        object.toString().toLowerCase().startsWith(prefix.toLowerCase());
            }
        };

        FilterComboBox fcb = new FilterComboBox(filter, items);
        return fcb;
    }

    private FRTreeComboBox createTreeComboBox() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("根节点");
        DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("子节点1");
        root.add(child1);
        DefaultMutableTreeNode child11 = new DefaultMutableTreeNode("子节点1-1");
        child1.add(child11);
        DefaultMutableTreeNode child12 = new DefaultMutableTreeNode("子节点1-2");
        child1.add(child12);
        DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("子节点2");
        root.add(child2);
        DefaultMutableTreeNode child21 = new DefaultMutableTreeNode("子节点2-1");
        child2.add(child21);
        JTree tree = new JTree(root);
        TreeCellRenderer treeCellRender = new DefaultTreeCellRenderer() {
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (value instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    Object userObj = node.getUserObject();
                    setText(userObj + "-我是渲染器额外加的");
                }
                return this;
            }
        };
        FRTreeComboBox tcb = new FRTreeComboBox(tree, treeCellRender, true, false);
        ListCellRenderer listCellRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TreePath) {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) ((TreePath) value).getLastPathComponent();
                    setText(treeNode.getUserObject().toString());
                }
                return this;
            }
        };
        tcb.setRenderer(listCellRenderer);
        tcb.setOnlyLeafSelectable(false);
        return tcb;
    }

    private LazyComboBox createLazyComboBox() {
        LazyComboBox lcb = new LazyComboBox() {
            @Override
            public Object[] load() {
                // 睡5秒
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
                return new Object[]{"11", "22", "33", "44", "55", "66"};
            }
        };
        return lcb;
    }
}