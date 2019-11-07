package com.fr.design.mainframe.widget;

/**
 * Created by xiaxiang on 2016/9/30.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import javax.swing.tree.*;

import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.*;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxUI;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.mainframe.ComponentTree;
import com.sun.java.swing.plaf.motif.*;
import com.sun.java.swing.plaf.windows.*;

/**
 * 控件树下拉列表框
 */
public class UITreeComboBox extends UIComboBox {
    /**
     * 显示用的树
     */
    private ComponentTree tree;

    public UITreeComboBox(ComponentTree componentTree){
        this.setTree(componentTree);
        tree.getDesigner().addDesignerEditListener(new TreeComboBoxDesignerEditAdapter());
//        for(int i=0; i<tree.getRowCount(); i++)
//        {
//            tree.expandRow(i);
//        }
        setPreferredSize(new Dimension(200, 20));
    }

    /**
     * 设置树
     * @param tree ComponentTree
     */
    public void setTree(ComponentTree tree){
        this.tree = tree;
        if(tree != null){
            this.setSelectedItem(tree.getSelectionPath());
            this.setRenderer(new UITreeComboBoxRenderer());
        }
        this.updateUI();
    }

    @Override
    public void setRenderer(ListCellRenderer aRenderer) {
        ListCellRenderer oldRenderer = renderer;
        renderer = aRenderer;
        firePropertyChange( "renderer", oldRenderer, renderer );
        invalidate();
    }

    /**
     * 取得树
     * @return JTree
     */
    public ComponentTree getTree(){
        return tree;
    }

    /**
     * 设置当前选择的树路径
     * @param o Object
     */
    public void setSelectedItem(Object o){
        getModel().setSelectedItem(o);
    }

    public void updateUI(){
        ComboBoxUI cui = (ComboBoxUI)UIManager.getUI(this);
        if(cui instanceof MetalComboBoxUI){
            cui = new MetalJTreeComboBoxUI();
        } else if(cui instanceof MotifComboBoxUI){
            cui = new MotifJTreeComboBoxUI();
        } else {
            cui = new UIJTreeComboBoxUI();
        }
        setUI(cui);
    }

    private void refreshShortCuts() {
        TreePath path = this.getTree().getSelectionPath();
        if (path == null) {
            return;
        }
        Component component = (Component) path.getLastPathComponent();
        if (!(component instanceof XCreator)) {
            return;
        }
        com.fr.design.designer.beans.ComponentAdapter adapter = AdapterBus.getComponentAdapter(this.getTree().getDesigner(), (XCreator) component);
        adapter.getContextPopupMenu(null);
    }

    // UI Inner classes -- one for each supported Look and Feel
    class MetalJTreeComboBoxUI extends MetalComboBoxUI{
        protected ComboPopup createPopup() {
            return new TreePopup(comboBox);
        }
    }

    class WindowsJTreeComboBoxUI extends WindowsComboBoxUI{
        protected ComboPopup createPopup() {
            return new TreePopup(comboBox);
        }
    }

    class UIJTreeComboBoxUI extends UIComboBoxUI {
        protected ComboPopup createPopup() {
            return new TreePopup(comboBox);
        }

        @Override
        public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
            paintCurrentValue(g, bounds, hasFocus, 3);
        }
    }

    class MotifJTreeComboBoxUI extends MotifComboBoxUI{
        protected ComboPopup createPopup() {
            return new TreePopup(comboBox);
        }
    }


    /**
     * <p>Title: UITreeComboBoxRenderer</p>
     * <p>Description: 树形结构而来的DefaultListCellRenderer </p>
     */
    class UITreeComboBoxRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus){
            if(tree != null && tree.getSelectedTreePath().length > 0){
                TreePath path = tree.getSelectedTreePath()[0];
                tree.setAndScrollSelectionPath(tree.getSelectedTreePath());
                Object node = path.getLastPathComponent();
                value = node;
                TreeCellRenderer r = tree.getCellRenderer();
                JLabel lb = (JLabel)r.getTreeCellRendererComponent(
                        tree, value, isSelected, false, false, index,
                        cellHasFocus);
                return lb;
            }
            return super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);
        }
    }


    private class TreeComboBoxDesignerEditAdapter implements DesignerEditListener {

        @Override
        public void fireCreatorModified(DesignerEvent evt) {
            if (evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED) {
                TreePath[] paths = tree.getSelectedTreePath();
                if (tree != null && paths.length > 0) {
                    tree.setAndScrollSelectionPath(paths);
                    setSelectedItem(paths[0]);
                    MenuSelectionManager.defaultManager().clearSelectedPath();
                }

            } else if (evt.getCreatorEventID() == DesignerEvent.CREATOR_PASTED) {
                tree.refreshUI();
                TreePath[] paths = tree.getSelectedTreePath();
                if (tree != null && paths.length > 0) {
                    tree.setAndScrollSelectionPath(paths);
                    setSelectedItem(paths[0]);
                    MenuSelectionManager.defaultManager().clearSelectedPath();
                }
            } else if (evt.getCreatorEventID() == DesignerEvent.CREATOR_CUTED) {
                tree.refreshUI();
                setSelectedItem(null);
                MenuSelectionManager.defaultManager().clearSelectedPath();
            } else {
                tree.refreshUI();
                repaint();
            }
            refreshShortCuts();
        }

        @Override
        public boolean equals(Object o) {
            if (o != null) {
                return o.getClass() == this.getClass();
            } else {
                return false;
            }
        }
    }

    /**
     * 测试
     */
//    public static void main(String args[]){
//        JFrame frame = new JFrame("UITreeComboBox");
//        final UITreeComboBox box = new UITreeComboBox(new ComponentTree(new FormDesigner()));
//        box.setPreferredSize(new Dimension(300, 28));
//        frame.getContentPane().add(box);
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }
}

/**
 * <p>Title: UITreeComboBox</p>
 * <p>Description: TreePopup</p>
 */
class TreePopup extends JPopupMenu implements ComboPopup{
    protected UITreeComboBox comboBox;
    protected JScrollPane scrollPane;

    protected MouseMotionListener mouseMotionListener;
    protected MouseListener mouseListener;


    public void popupMenu(MouseEvent e) {
        TreePath path = comboBox.getTree().getSelectionPath();
        if (path == null) {
            return;
        }
        Component component = (Component) path.getLastPathComponent();
        if (!(component instanceof XCreator)) {
            return;
        }
        com.fr.design.designer.beans.ComponentAdapter adapter = AdapterBus.getComponentAdapter(comboBox.getTree().getDesigner(), (XCreator) component);
        JPopupMenu menu = adapter.getContextPopupMenu(e);
        menu.show(comboBox, e.getX(), e.getY());
    }

    public TreePopup(JComboBox comboBox){
        this.comboBox = (UITreeComboBox)comboBox;
        setLayout(new BorderLayout());
        setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());
        JTree tree = this.comboBox.getTree();
        if(tree != null){
            scrollPane = new UIScrollPane(tree);
            scrollPane.setBorder(null);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    public void show() {
        updatePopup();
        show(comboBox, 0, comboBox.getHeight());
        comboBox.getTree().requestFocus();
        comboBox.getTree().setBackground(UIConstants.TREE_BACKGROUND);
        comboBox.getTree().setOpaque(true);
    }

    public void hide(){
        setVisible(false);
        comboBox.firePropertyChange("popupVisible", true, false);
    }

    protected JList list = new JList();
    public JList getList(){
        return list;
    }

    public MouseMotionListener getMouseMotionListener(){
        if(mouseMotionListener == null){
            mouseMotionListener = new MouseMotionAdapter(){};
        }
        return mouseMotionListener;
    }

    public KeyListener getKeyListener(){
        return null;
    }

    public void uninstallingUI(){}

    /**
     * Implementation of ComboPopup.getMouseListener().
     *
     * @return a <code>MouseListener</code> or null
     * @see ComboPopup#getMouseListener
     */
    public MouseListener getMouseListener(){
        if(mouseListener == null){
            mouseListener = new InvocationMouseHandler();
        }
        return mouseListener;
    }

    protected void togglePopup(){
        if(isVisible()){
            hide();
        } else{
            show();
        }
    }
    protected void updatePopup(){
        setPreferredSize(new Dimension(comboBox.getSize().width, 200));
        Object selectedObj = comboBox.getSelectedItem();
        if(selectedObj != null){
            TreePath tp = (TreePath)selectedObj;
            ((UITreeComboBox)comboBox).getTree().setSelectionPath(tp);
        }
    }

    protected class InvocationMouseHandler extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            if(!SwingUtilities.isLeftMouseButton(e) || !comboBox.isEnabled()){
                return;
            }
            if(comboBox.isEditable()){
                Component comp = comboBox.getEditor().getEditorComponent();
                if((!(comp instanceof JComponent)) ||
                        ((JComponent)comp).isRequestFocusEnabled()){
                    comp.requestFocus();
                }
            } else if(comboBox.isRequestFocusEnabled()){
                comboBox.requestFocus();
            }
            togglePopup();
        }

        public void mouseClicked (MouseEvent e){
            if (e.isMetaDown()) {
                popupMenu(e);
            } else {
                return;
            }
        }
    }



}
