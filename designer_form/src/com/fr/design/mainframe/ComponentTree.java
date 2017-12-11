package com.fr.design.mainframe;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.fr.design.constants.UIConstants;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.treeview.ComponentTreeCellRenderer;
import com.fr.design.designer.treeview.ComponentTreeModel;
import com.fr.design.gui.itree.UITreeUI;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.StringUtils;
import com.fr.web.core.A.M;

public class ComponentTree extends JTree {

    private FormDesigner designer;
    private ComponentTreeModel model;
    private UITreeUI uiTreeUI = new UITreeUI();
    private PopupPreviewPane previewPane;
    private static final int PADDING_LEFT = 10;
    private static final int PADDING_TOP = 8;

    public ComponentTree(FormDesigner designer) {
        this.designer = designer;
        this.setBackground(UIConstants.TREE_BACKGROUND);
        setRootVisible(true);
        setCellRenderer(new ComponentTreeCellRenderer());
        getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        this.setDragEnabled(false);
        this.setDropMode(DropMode.ON_OR_INSERT);
        this.setTransferHandler(new TreeTransferHandler());
        this.refreshTreeRoot();
        addTreeSelectionListener(designer);
        addMouseMotionListener(new ComponetTreeMouseListener(this));
        setEditable(true);
        setUI(uiTreeUI);
        setBorder(BorderFactory.createEmptyBorder(PADDING_TOP, PADDING_LEFT, 0, 0));
    }

    public FormDesigner getDesigner() {
        return designer;
    }

    /**
     * 构造函数
     *
     * @param designer 设计界面组件
     * @param model    构造JTree的model
     */
    public ComponentTree(FormDesigner designer, ComponentTreeModel model) {
        this(designer);
        this.setModel(model);
    }

    public void setSelectionPath(TreePath path) {
        // 不管点击哪一项，都要先退出编辑状态（图表、报表块、绝对布局、tab块）
        designer.stopEditing(path);
        super.setSelectionPath(path);
    }

    @Override
    public void removeAll() {
        if (previewPane != null) {
            previewPane.setVisible(false);
            previewPane.removeAll();
        }
        super.removeAll();
    }


    /**
     * 是否可编辑
     *
     * @param path 树路径
     * @return 是则返回true
     */
    @Override
    public boolean isPathEditable(TreePath path) {
        return false;
    }

    /**
     * 将值转换为文本
     *
     * @param value    值
     * @param selected 是否选中
     * @param expanded 扩展
     * @param leaf     是否叶子
     * @param row      行
     * @param hasFocus 是否焦点
     * @return 返回文本
     */
    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value != null && value instanceof XCreator) {
            return ((XCreator) value).toData().getWidgetName();
        } else {
            return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        }
    }

    public void setAndScrollSelectionPath(TreePath[] treepath) {
        setSelectionPaths(treepath);
        scrollPathToVisible(treepath[0]);
    }


    /**
     * 刷新
     */
    public void refreshUI() {
        updateUI();
        setUI(uiTreeUI);
    }


    public TreePath[] getSelectedTreePath() {
        XCreator[] creators = designer.getSelectionModel().getSelection().getSelectedCreators();
        TreePath[] paths = new TreePath[creators.length];
        for (int i = 0; i < paths.length; i++) {
            paths[i] = buildTreePath(creators[i]);
        }
        return paths;
    }


    /**
     * 搜索指定名称的路径
     *
     * @param text 名称
     * @return 树路径
     */
    public TreePath[] search(String text) {
        if (StringUtils.isNotEmpty(text)) {
            text = text.toLowerCase();
        }
        ArrayList<XCreator> searchList = new ArrayList<XCreator>();
        XLayoutContainer root = designer.getRootComponent();
        searchFormRoot(root, searchList, text);
        TreePath[] paths = new TreePath[searchList.size()];

        for (int i = 0; i < paths.length; i++) {
            paths[i] = buildTreePath(searchList.get(i));
        }
        if (paths.length > 0) {
            setAndScrollSelectionPath(paths);
        } else {
            setSelectionPath();
        }
        return paths;
    }


    private void setSelectionPath() {

        /**
         * 不让传null参数，所以只有自己定义
         */
        super.setSelectionPath(null);
        clearSelection();
    }

    private void searchFormRoot(XLayoutContainer container, ArrayList<XCreator> searchList, String text) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        for (int i = 0, size = container.getXCreatorCount(); i < size; i++) {
            XCreator creator = container.getXCreator(i);
            String xName = creator.toData().getWidgetName();
            if (xName.toLowerCase().contains(text)) {
                searchList.add(creator);
            }
            if (creator instanceof XLayoutContainer) {
                searchFormRoot((XLayoutContainer) creator, searchList, text);
            }
        }
    }

    /**
     * 触发
     */
    public void fireTreeChanged() {
        designer.refreshDesignerUI();
    }

    /**
     * 刷新
     */
    public void refreshTreeRoot() {
        model = new ComponentTreeModel(designer, designer.getTopContainer());
        setModel(model);
        setDragEnabled(false);
        setDropMode(DropMode.ON_OR_INSERT);
        setTransferHandler(new TreeTransferHandler());
        repaint();
    }

    private TreePath buildTreePath(Component comp) {
        ArrayList<Component> path = new ArrayList<Component>();
        Component parent = comp;

        while (parent != null) {
            XCreator creator = (XCreator) parent;
            path.add(0, parent);
            if (creator != comp) {
                creator.notShowInComponentTree(path);
            }
            //绝对布局作为body的时候不显示自适应布局父层
            if (((XCreator) parent).acceptType(XWAbsoluteBodyLayout.class)) {
                if ((parent.getParent() != null)
                        && ((XCreator) parent.getParent()).acceptType(XWFitLayout.class)) {
                    parent = parent.getParent().getParent();
                    continue;
                }
            }

            parent = parent.getParent();
        }
        Object[] components = path.toArray();
        return new TreePath(components);
    }

    private void popupPreviewPane(int popupPosYOnScreen, XCreator comp) {
        if (previewPane == null) {
            previewPane = new PopupPreviewPane();
        }
        if (previewPane.isVisible() && previewPane.getLocationOnScreen().y != popupPosYOnScreen) {
            previewPane.setVisible(false);
        }

        if (!previewPane.isVisible()) {
            previewPane.setComp(comp);
            int popupPosY = popupPosYOnScreen - FormHierarchyTreePane.getInstance().getLocationOnScreen().y;
            GUICoreUtils.showPopupMenu(previewPane, FormHierarchyTreePane.getInstance(), -previewPane.getPreferredSize().width, popupPosY);
        }
    }

    private void hidePreviewPane() {
        if (previewPane != null && previewPane.isVisible()) {
            previewPane.setVisible(false);
        }
    }

    private final class ComponetTreeMouseListener extends MouseAdapter {
        private final JTree tree;

        private ComponetTreeMouseListener(JTree tree) {
            this.tree = tree;
        }

        @Override
        public void mouseMoved(MouseEvent e) {

            Point p = e.getPoint();
            int selRow = tree.getRowForLocation(p.x, p.y);
            TreeCellRenderer r = tree.getCellRenderer();
            if (selRow != -1 && r != null) {
                TreePath path = tree.getPathForRow(selRow);
                Point point = tree.getPathBounds(path).getLocation();
                SwingUtilities.convertPointToScreen(point, tree);
                XCreator comp = (XCreator) path.getLastPathComponent();
                popupPreviewPane(point.y, comp);
            } else {
                hidePreviewPane();
            }
        }
    }

    private class PopupPreviewPane extends JPopupMenu {
        private Container contentPane;
        private BufferedImage compImage;
        private static final int MAX_WIDTH = 360;
        private static final int MAX_HEIGHT = 280;

        PopupPreviewPane() {
            contentPane = new JPanel();
            contentPane.setBackground(Color.white);
            this.setLayout(new BorderLayout());
            this.add(contentPane, BorderLayout.CENTER);
            this.setOpaque(false);
            setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
            setBorder(BorderFactory.createLineBorder(UIConstants.LINE_COLOR));
        }

        public void setComp(XCreator comp) {
            try {
                this.compImage = componentToImage(comp);
                this.updateSize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (compImage != null) {
                g.drawImage(compImage, 0, 0, getWidth(), getHeight(), null);
            }
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
        }

        public void menuSelectionChanged(boolean isIncluded) {
        }

        public Container getContentPane() {
            return contentPane;
        }

        private BufferedImage componentToImage(Component comp) throws IOException
        {
            BufferedImage im = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_ARGB);
            comp.paint(im.getGraphics());
            return im;
        }

        // 根据控件内容，更新弹出框大小
        private void updateSize() {
            int width = compImage.getWidth();
            int height = compImage.getHeight();
            double aspectRatio = (double)width / height;
            if (width > MAX_WIDTH) {
                width = MAX_WIDTH;
                height = (int)(width / aspectRatio);
            }
            if (height > MAX_HEIGHT) {
                height = MAX_HEIGHT;
                width = (int)(height * aspectRatio);
            }
            this.setPreferredSize(new Dimension(width, height));
        }
    }
}