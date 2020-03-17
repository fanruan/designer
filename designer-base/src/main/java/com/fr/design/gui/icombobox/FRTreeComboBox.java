package com.fr.design.gui.icombobox;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.general.NameObject;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

public class FRTreeComboBox extends UIComboBox {

    private static final int PAGE_DIFF = 5;
    private static final int DEFAULT_HEIGHT = 120;

    // richer:下拉展示用的tree
    protected JTree tree;
    private boolean onlyLeafSelectable = true;
    private Object selectedObject = null;

    public FRTreeComboBox() {
        this(new JTree());
    }

    public FRTreeComboBox(JTree tree) {
        this(tree, null);
    }

    public FRTreeComboBox(JTree tree, TreeCellRenderer renderer) {
        this(tree, renderer, false);
    }

    public FRTreeComboBox(JTree tree, TreeCellRenderer renderer, boolean editable) {
        this(tree, renderer, false, editable);
    }

    public FRTreeComboBox(JTree tree, TreeCellRenderer renderer,
                          boolean showRoot, boolean editable) {
        this.setTree(tree);
        this.tree.setCellRenderer(renderer);
        this.setEditor(createEditor());
        if (!showRoot) {
            tree.expandPath(new TreePath(tree.getModel().getRoot()));
            tree.setRootVisible(false);
        }
        this.setEditable(editable);
        setUI(new FRTreeComboBoxUI());
    }

    public JTree getTree() {
        return this.tree;
    }

    /**
     * 自定义那些那些node可以选中，默认情况下所有node节点都可以选中
     * @param node
     * @return
     */
    protected boolean customSelectable(DefaultMutableTreeNode node){
        return true;
    }

    protected UIComboBoxEditor createEditor() {
        return new FrTreeSearchComboBoxEditor(this);
    }

    public void setTree(JTree tree) {
        this.tree = tree;
        if (tree != null) {
            this.setSelectedItem(tree.getSelectionPath());
            this.setRenderer(new TreeComboBoxRenderer());
        }
    }

    public void setOnlyLeafSelectable(boolean onlyLeafSelectable) {
        this.onlyLeafSelectable = onlyLeafSelectable;
    }

    /**
     * 获得FRTreeComboBox的返回值
     *
     * @return Object
     */
    public Object getSelectedObject() {
        if (!(this.getSelectedItem() instanceof TreePath)) {
            return this.getSelectedItem();
        }
        TreePath treePath = (TreePath) this.getSelectedItem();
        if (treePath == null) {
            return null;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        return node.getUserObject();
    }

    public String getProcedureSelectName() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) ((TreePath) this.getSelectedItem()).getLastPathComponent();
        NameObject no = (NameObject) node.getUserObject();
        String ds2 = no.getName();
        node = (DefaultMutableTreeNode) node.getParent();
        no = (NameObject) node.getUserObject();
        String ds1 = no.getName();
        return ds1 + "." + ds2;
    }

    protected void dealSamePath(TreePath parent,TreeNode node,UITextField textField){
        boolean isBreak = false;
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            TreeNode n = (TreeNode) e.nextElement();
            TreePath path = parent.pathByAddingChild(n);
            TreeNode pathnode = (TreeNode) path.getLastPathComponent();
            for (Enumeration e2 = pathnode.children(); e2.hasMoreElements(); ) {
                TreeNode n2 = (TreeNode) e2.nextElement();
                TreePath path2 = path.pathByAddingChild(n2);
                if (pathToString(path2).toUpperCase().contains(textField.getText().toUpperCase())) {
                    tree.scrollPathToVisible(path2);
                    tree.setSelectionPath(path2);
                    isBreak = true;
                    break;
                }
            }
            if (isBreak) {
                break;
            }
        }
    }

    public void setSelectedItem(Object o) {
        selectedObject = o;
        if (o instanceof String) {
            this.setSelectedItemString((String) o);
            return;
        }
        this.tree.setSelectionPath((TreePath) o);
        if (this.isEditable && o != null) {
            this.setSelectedItemString(pathToString((TreePath) o));
        } else {
            this.getModel().setSelectedItem(o);
        }
    }

    public Object getSelectedItemObject() {
        return selectedObject;
    }

    private boolean validTreePath(String treePath){
        return StringUtils.isNotEmpty(treePath) && treePath.charAt(0) == '[' && treePath.endsWith("]");
    }

    protected String pathToString(TreePath path) {
        String temp = path.toString();
        if (validTreePath(temp)) {
            temp = temp.substring(2, temp.length() - 1);
            String[] selectedtable = temp.split(",");
            return selectedtable[selectedtable.length - 1].trim();
        }
        return "";
    }

    /*
      * richer:根据NameObject的名字来选取
      */
    public void setSelectedItemString(String _name) {
        if (StringUtils.isBlank(_name)) {
            this.setSelectedIndex(-1);
            return;
        }
        DefaultTreeModel model = (DefaultTreeModel) this.tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode leaf = root.getFirstLeaf();
        do {
            Object userObj = leaf.getUserObject();
            if (userObj instanceof NameObject) {
                if (ComparatorUtils.equals(_name, ((NameObject) userObj).getName())) {
                    TreePath path = new TreePath(model.getPathToRoot(leaf));
                    this.tree.setSelectionPath(path);
                    this.setSelectedItem(path);
                    break;
                }
            }
        } while ((leaf = leaf.getNextLeaf()) != null);
        if (leaf == null) {
            this.getModel().setSelectedItem(_name);
        }
    }

    public void setSelectedFirst() {
        DefaultTreeModel model = (DefaultTreeModel) this.tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        if (root == null) {
            return;
        }
        DefaultMutableTreeNode leaf = root.getFirstLeaf();
        if (leaf == null) {
            return;
        }
        if (leaf.getUserObject() instanceof NameObject) {
            TreePath path = new TreePath(model.getPathToRoot(leaf));
            this.tree.setSelectionPath(path);
            this.setSelectedItem(path);
        }
    }

    public void setPopSize(int width, int height) {
        treePopup.setPopSize(width, height);
    }


    private static TreePopup treePopup;

    private static class FRTreeComboBoxUI extends BasicComboBoxUI implements MouseListener{
        private boolean isRollover = false;

        public FRTreeComboBoxUI() {
            super();
        }
        protected ComboPopup createPopup() {
            treePopup = new TreePopup(comboBox);
            return treePopup;
        }
        @Override
        protected UIButton createArrowButton() {
            arrowButton = new UIButton(UIConstants.ARROW_DOWN_ICON){
                /**
                 * 组件是否需要响应添加的观察者事件
                 *
                 * @return 如果需要响应观察者事件则返回true，否则返回false
                 */
                @Override
                public boolean shouldResponseChangeListener() {
                    return false;
                }
            };
            ((UIButton) arrowButton).setRoundBorder(true, Constants.LEFT);
            arrowButton.addMouseListener(this);
            comboBox.addMouseListener(this);
            return (UIButton) arrowButton;
        }

        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color linecolor = null;
            if (comboBox.isPopupVisible()) {
                linecolor = UIConstants.LINE_COLOR;
                arrowButton.setSelected(true);
            } else if (isRollover) {
                linecolor = UIConstants.LIGHT_BLUE;
            } else {
                linecolor = UIConstants.LINE_COLOR;
                arrowButton.setSelected(false);
            }
            g2d.setColor(linecolor);
            if (!comboBox.isPopupVisible()) {
                g2d.drawRoundRect(0, 0, c.getWidth() - arrowButton.getWidth() + 3, c.getHeight() - 1, UIConstants.LARGEARC, UIConstants.LARGEARC);
            } else {
                g2d.drawRoundRect(0, 0, c.getWidth() , c.getHeight() + 3, UIConstants.LARGEARC, UIConstants.LARGEARC	);
                g2d.drawLine(0, c.getHeight()-1, c.getWidth(), c.getHeight()-1);
            }
        }



        private void setRollover(boolean isRollover) {
            if (this.isRollover != isRollover) {
                this.isRollover = isRollover;
                comboBox.repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setRollover(true);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setRollover(false);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }
    }

    /**
     * 添加弹出菜单监听
     *
     * @param l 监听事件
     *
     * @date 2015-1-22-下午5:04:00
     *
     */
    public void addPopupMenuListener(PopupMenuListener l) {
        treePopup.addPopupMenuListener(l);
    }

    private class TreeComboBoxRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            if (value != null) {
                TreePath path = (TreePath) value;
                TreeNode node = (TreeNode) path.getLastPathComponent();
                value = node;
                TreeCellRenderer treeCellRenderer = tree.getCellRenderer();
                UILabel lb = (UILabel) treeCellRenderer.getTreeCellRendererComponent(tree, value, isSelected,
                        false, node.isLeaf(), index, cellHasFocus);
                return lb;
            }
            return super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);
        }
    }

    /*
      * richer:弹出部分
      */
    private static class TreePopup extends JPopupMenu implements ComboPopup {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private int defaultWidth = 0;
        private int defaultHeight = 0;
        protected FRTreeComboBox comboBox;
        protected JScrollPane scrollPane;
        protected JTree tree;

        protected MouseMotionListener mouseMotionListener;
        protected MouseListener mouseListener;
        private MouseListener treeSelectListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JTree tree = (JTree) e.getSource();
                TreePath treePath = tree.getPathForLocation(e.getPoint().x, e
                        .getPoint().y);
                if (treePath == null) {
                    return;
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath
                        .getLastPathComponent();
                selectTreeItem(node, treePath);
            }
        };

        //samuel:相应首字母导航的键盘确定
        private KeyListener treeKeyListener = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    TreePath treePath = tree.getSelectionPath();
                    if (treePath == null) {
                        return;
                    }
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath
                            .getLastPathComponent();
                    selectTreeItem(node, treePath);
                }
            }
        };

        private void selectTreeItem(DefaultMutableTreeNode node, TreePath treePath) {
            if (node.isRoot()) {
                return;
            }
            if (comboBox.onlyLeafSelectable && !node.isLeaf()) {
                return;
            }

            //自定义node是否可选择
            if (!comboBox.customSelectable(node)){
                return;
            }

            comboBox.setSelectedItem(treePath);
            togglePopup();
            MenuSelectionManager.defaultManager().clearSelectedPath();
        }

        public TreePopup(JComboBox comboBox) {
            this.comboBox = (FRTreeComboBox) comboBox;
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.setLightWeightPopupEnabled(comboBox
                    .isLightWeightPopupEnabled());
            tree = this.comboBox.getTree();
            if (tree != null) {
                this.scrollPane = new JScrollPane(tree);
                this.scrollPane.setBorder(null);
                this.add(this.scrollPane, BorderLayout.CENTER);
                tree.addMouseListener(this.treeSelectListener);
                tree.addKeyListener(this.treeKeyListener);
            }
        }

        public void show() {
            this.updatePopup();
            try {
                /*
                     * 这里有个问题，可能是哪里的listenerr有冲突将这个JPopMenu show的X设置为70时
                     * 正好可以脱离DBManipulationPane，此时可以正确相应鼠标点击事件，否则由于焦点不在JPopmenu
                     * 上点击，树时将导致窗口销毁，无法正确相应点击事件
                     */
                this.show(comboBox, 0, comboBox.getHeight());
            } catch (IllegalComponentStateException e) {
                // richer:这里有可能会抛出一个异常，可以不用处理
            }
        }

        public void hide() {
            this.setVisible(false);
        }

        public JList getList() {
            return new JList();
        }

        public MouseMotionListener getMouseMotionListener() {
            if (mouseMotionListener == null) {
                mouseMotionListener = new MouseMotionAdapter() {
                };
            }
            return mouseMotionListener;
        }

        public KeyListener getKeyListener() {
            return null;
        }

        public void uninstallingUI() {
        }

        public MouseListener getMouseListener() {
            if (mouseListener == null) {
                mouseListener = new InvocationMouseHandler();
            }
            return mouseListener;
        }

        protected void togglePopup() {
            if (this.isVisible()) {
                this.hide();
            } else {
                this.show();
            }
        }

        protected void updatePopup() {
            int width = defaultWidth == 0 ? this.comboBox.getSize().width : defaultWidth;
            int height = defaultHeight == 0 ? DEFAULT_HEIGHT : defaultHeight;
            this.setPreferredSize(new Dimension(width, height));
            Object selectedObj = this.comboBox.getSelectedItem();
            if (selectedObj instanceof TreePath) {
                TreePath tp = (TreePath) selectedObj;
                (this.comboBox).getTree().setSelectionPath(tp);
            }
        }

        public void setPopSize(int width, int height) {
            this.defaultWidth = width;
            this.defaultHeight = height;
        }

        protected class InvocationMouseHandler extends MouseAdapter {
            public void mousePressed(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)
                        || !comboBox.isEnabled()) {
                    return;
                }
                if (comboBox.isEditable()) {
                    Component comp = comboBox.getEditor().getEditorComponent();
                    if ((!(comp instanceof JComponent))
                            || ((JComponent) comp).isRequestFocusEnabled()) {
                        comp.requestFocus();
                    }
                } else if (comboBox.isRequestFocusEnabled()) {
                    comboBox.requestFocus();
                }
                togglePopup();
            }
        }
    }

    public class FrTreeSearchComboBoxEditor extends UIComboBoxEditor implements DocumentListener {
        private volatile boolean setting = false;
        private FRTreeComboBox comboBox;
        private Object item;

        public FrTreeSearchComboBoxEditor(FRTreeComboBox comboBox) {
            super();
            this.comboBox = comboBox;
            textField.getDocument().addDocumentListener(this);
            textField.addKeyListener(this.treeKeyListener);
        }

        private KeyListener treeKeyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_DOWN) {
                    int row = tree.getRowForPath(tree.getSelectionPath()) + 1;
                    tree.setSelectionRow(row);
                    tree.scrollRowToVisible(row);
                } else if (key == KeyEvent.VK_UP) {
                    int row = tree.getRowForPath(tree.getSelectionPath()) - 1;
                    tree.setSelectionRow(row);
                    tree.scrollRowToVisible(row);
                } else if (key == KeyEvent.VK_PAGE_DOWN) {
                    int row = tree.getRowForPath(tree.getSelectionPath()) + PAGE_DIFF;
                    tree.setSelectionRow(row);
                    tree.scrollRowToVisible(row);
                } else if (key == KeyEvent.VK_PAGE_UP) {
                    int row = tree.getRowForPath(tree.getSelectionPath()) - PAGE_DIFF;
                    tree.setSelectionRow(row);
                    tree.scrollRowToVisible(row);
                }
            }

            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    TreePath treePath = tree.getSelectionPath();
                    if (treePath == null) {
                        return;
                    }
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath
                            .getLastPathComponent();
                    if (node.isLeaf()) {
                        comboBox.setSelectedItem(treePath);
                        textField.setText(pathToString(treePath));
                        MenuSelectionManager.defaultManager().clearSelectedPath();
                    }
                }

            }
        };


        public void setItem(Object item) {
            this.item = item;
            this.setting = true;
            textField.setText((item == null) ? "" : item.toString());
            this.setting = false;
        }

        public Object getItem() {
            return this.item;
        }

        public void insertUpdate(DocumentEvent e) {
            changeHandler();
        }

        public void removeUpdate(DocumentEvent e) {
            changeHandler();
        }

        public void changedUpdate(DocumentEvent e) {
            changeHandler();
        }

        protected void changeHandler() {
            if (setting) {
                return;
            }
            setPopupVisible(true);
            this.item = textField.getText();
            TreeNode root = (TreeNode) tree.getModel().getRoot();
            TreePath parent = new TreePath(root);
            TreeNode node = (TreeNode) parent.getLastPathComponent();
            dealSamePath(parent,node,textField);
            this.getEditorComponent().requestFocus();
        }

    }
}