package com.fr.design.gui.icombobox;

import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * august:非常beautiful的ComboBox，不支持编辑状态. 内容过长时，鼠标移动过去会有ToolTips,不会有横向滚动条
 * 假如支持编辑，因为UIComboBox的TextField 的绘制 并不是靠Renderer来控制 ,
 * 它会通过paintCurrentValueBackground()来绘制背景,
 * 然后通过paintCurrentValue(),去绘制UIComboBox里显示的值。所考虑情况比现在复杂多多多多多了，所以暂时不支持
 * 另外，项的内容最好不要有图标
 *
 * @author zhou
 * @since 2012-5-9下午3:18:58
 */
public class UIComboBox extends JComboBox implements UIObserver, GlobalNameObserver {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final int SIZE = 20;

    private static final int SIZE5 = 5;

    protected UIObserverListener uiObserverListener;

    private String comboBoxName = "";

    private GlobalNameListener globalNameListener = null;

    public UIComboBox() {
        super();
        init();
    }

    public UIComboBox(ComboBoxModel model) {
        super(model);
        init();
    }

    public UIComboBox(Object[] items) {
        super(items);
        init();
    }

    public UIComboBox(Vector<?> items) {
        super(items);
        init();
    }

    private void init() {
        setOpaque(false);
        setUI(getUIComboBoxUI());
        setRenderer(new UIComboBoxRenderer());
        setEditor(new UIComboBoxEditor());
        initListener();
    }

    protected void initListener() {
        if (shouldResponseChangeListener()) {
            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    fireSetGlobalName();
                }
            });
            this.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    fireSetGlobalName();
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        uiObserverListener.doChange();
                    }
                }
            });
        }
    }

    protected void fireSetGlobalName() {
        if (globalNameListener != null && shouldResponseNameListener()) {
            globalNameListener.setGlobalName(comboBoxName);
        }
    }


    protected ComboBoxUI getUIComboBoxUI() {
        return new UIComboBoxUI();
    }

    /**
     * 只允许设置为UIComboBoxRenderer，所以要继承UIComboBoxRenderer
     */
    @Override
    public void setRenderer(ListCellRenderer aRenderer) {
        if (aRenderer instanceof UIComboBoxRenderer) {
            super.setRenderer(aRenderer);
        }
    }

    protected ComboPopup createPopup() {
        return null;
    }

    @Override
    public void setGlobalName(String name) {
        comboBoxName = name;
    }

    @Override
    public Dimension getPreferredSize() {
        //加5的原因在于：render里，每一个项前面了空了一格，要多几像素
        return new Dimension(super.getPreferredSize().width + SIZE5, SIZE);
    }

    /**
     * 鼠标进入事件
     */
    public void mouseEnterEvent() {

    }

    /**
     * 鼠标离开事件
     */
    public void mouseExitEvent() {

    }

    /**
     *
     */
    @Override
    public void updateUI() {
        setUI(getUIComboBoxUI());
    }


    /**
     * @param listener 观察者监听事件
     */
    @Override
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    public void removeChangeListener() {
        uiObserverListener = null;
    }

    public UIObserverListener getUiObserverListener() {
        return uiObserverListener;
    }

    /**
     * @return 是否响应变更事件
     */
    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }

    /**
     * @param listener 观察者监听事件
     */
    @Override
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    /**
     * @return 是否响应名称事件
     */
    @Override
    public boolean shouldResponseNameListener() {
        return true;
    }


}