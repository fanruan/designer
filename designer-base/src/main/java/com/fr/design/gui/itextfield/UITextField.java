package com.fr.design.gui.itextfield;

import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;

/**
 * @author Jerry
 */
public class UITextField extends JTextField implements UIObserver, GlobalNameObserver {
    private boolean isBorderPainted = true;
    private boolean isRoundBorder = true;
    private int rectDirection = Constants.NULL;
    private UIObserverListener uiObserverListener;
    private String textFieldName = StringUtils.EMPTY;
    private GlobalNameListener globalNameListener = null;
    private Dimension preferredSize = null;
    private String placeholder = StringUtils.EMPTY;

    //有些情况下setText的时候不希望触发attributeChange，添加一个属性标识
    private boolean isSetting = false;

    public UITextField() {
        super();
        InputEventBaseOnOS.addBasicEditInputMap(this);
        initListener();
    }

    public UITextField(int columns) {
        super(columns);
        InputEventBaseOnOS.addBasicEditInputMap(this);
        initListener();
    }

    public UITextField(String text, int columns) {
        super(text, columns);
        InputEventBaseOnOS.addBasicEditInputMap(this);
        initListener();
    }

    public UITextField(String text) {
        super(text);
        InputEventBaseOnOS.addBasicEditInputMap(this);

        initListener();
    }

    public UITextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        InputEventBaseOnOS.addBasicEditInputMap(this);
        initListener();
    }

    protected void initListener() {
        if (shouldResponseChangeListener()) {
            getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    attributeChange();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    attributeChange();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    attributeChange();
                }
            });
        }
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }


    public boolean isSetting() {
        return isSetting;
    }

    public void setSetting(boolean setting) {
        isSetting = setting;
    }

    public void setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
    }

    public void setGlobalName(String name) {
        textFieldName = name;
    }

    protected void attributeChange() {
        if(isSetting){
            return;
        }
        if (globalNameListener != null && shouldResponseNameListener()) {
            globalNameListener.setGlobalName(textFieldName);
        }
        if (uiObserverListener != null) {
            uiObserverListener.doChange();
        }
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 4, 0, 4);
    }

    @Override
    public Dimension getPreferredSize() {
        if (preferredSize == null) {
            return new Dimension(super.getPreferredSize().width, 20);
        }
        return preferredSize;
    }

    @Override
    public UITextFieldUI getUI() {
        return (UITextFieldUI) ui;
    }

    /**
     * 设置变化的背景颜色
     */
    public void setBackgroundUIColor(Color color) {
        ((UITextFieldUI) this.ui).setBackgroundColor4NoGiveNumber(color);
    }

    /**
     * 更新UI
     */
    public void updateUI() {
        this.setUI(new UITextFieldUI(this));
    }

    @Override
    protected void paintComponent(final Graphics pG) {
        super.paintComponent(pG);
        if (placeholder.length() == 0 || getText().length() > 0) {
            return;
        }
        final Graphics2D g = (Graphics2D) pG;
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getDisabledTextColor());
        g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
                .getMaxAscent() + getInsets().top + 1);
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (!isBorderPainted) {
            return;
        }
        getUI().paintBorder((Graphics2D) g, getWidth(), getHeight(), isRoundBorder, rectDirection);
    }

    /**
     * @param isRoundBorder
     */
    public void setRoundBorder(boolean isRoundBorder) {
        this.isRoundBorder = isRoundBorder;
    }


    /**
     * @param rectDirection
     */
    public void setRectDirection(int rectDirection) {
        this.rectDirection = rectDirection;
    }

    /**
     * 注册监听
     *
     * @param listener 观察者监听事件
     */
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    /**
     * 是否需要回应监听器响应
     *
     * @return 是则返回true
     */
    public boolean shouldResponseNameListener() {
        return true;
    }


    /**
     * 注册监听器
     *
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
     * 是否需要回应监听器响应
     *
     * @return 是则返回true
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    /**
     * 主函数
     *
     * @param args 参数
     */
    public static void main(String... args) {
        LayoutManager layoutManager = null;
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(layoutManager);

        UITextField bb = new UITextField(5);
        bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
        content.add(bb);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 400);
        jf.setVisible(true);
    }

    public void setBorderPainted(boolean isBorderPainted) {
        this.isBorderPainted = isBorderPainted;
    }

}