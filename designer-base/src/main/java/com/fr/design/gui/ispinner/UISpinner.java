package com.fr.design.gui.ispinner;

import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonUI;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.stable.CommonUtils;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import com.fr.stable.collections.utils.MathUtils;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.*;

public class UISpinner extends JPanel implements UIObserver, GlobalNameObserver {

    protected double value;
    private static final int SIZE = 20;
    private static final int LEN = 13;
    private static final int WIDTH = 13;
    private static final int HEIGHT = 10;
    private UINumberField textField;
    private UIButton preButton;
    private UIButton nextButton;
    private double minValue;
    private double maxValue;
    private double dierta;
    private String spinnerName = StringUtils.EMPTY;
    private UIObserverListener uiObserverListener;
    private GlobalNameListener globalNameListener = null;


    public UISpinner(double minValue, double maxValue, double dierta) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.dierta = dierta;
        initComponents();
        iniListener();
    }

    public UISpinner(double minValue, double maxValue, double dierta, double defaultValue) {
        this(minValue, maxValue, dierta);
        textField.setValue(defaultValue);
    }

    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

    /**
     * 给组件分别加上FocusListener
     *
     * @param focusListener 监听事件
     */
    public void addUISpinnerFocusListenner(FocusListener focusListener) {
        this.addFocusListener(focusListener);
        this.textField.addFocusListener(focusListener);
        this.preButton.addFocusListener(focusListener);
        this.nextButton.addFocusListener(focusListener);

    }

    public double getValue() {
        return value;
    }

    public void setGlobalName(String name) {
        spinnerName = name;
    }

    public UINumberField getTextField() {
        return textField;
    }

    public void setValue(double value) {
        setValue(value, true);
    }

    /**
     * 赋值但不触发保存,只是展现,一般是populate的时候用
     * @param value
     */
    public void setValueWithoutEvent(double value) {
        setValue(value, false);
    }

    public void setValue(double value, boolean fireStateChange) {
        if (globalNameListener != null && shouldResponseNameListener()) {
            globalNameListener.setGlobalName(spinnerName);
        }
        value = value < minValue ? minValue : value;
        value = value > maxValue ? maxValue : value;
        if (CommonUtils.equals(value, this.value)) {
            return;
        }
        this.value = value;
        setTextField(value);
        if (fireStateChange) {
            fireStateChanged();
        }
    }

    protected void setTextField(double value){
        textField.getDocument().removeDocumentListener(docListener);
        textField.setValue(value);
        textField.getDocument().addDocumentListener(docListener);
    }

    public void setTextFieldValue(double value) {
        if (globalNameListener != null && shouldResponseNameListener()) {
            globalNameListener.setGlobalName(spinnerName);
        }
        value = value < minValue ? minValue : value;
        value = value > maxValue ? maxValue : value;

        if (CommonUtils.equals(value, this.value)) {
            return;
        }
        this.value = value;
        fireStateChanged();
    }


    public void setEnabled(boolean flag) {
        super.setEnabled(flag);
        this.textField.setEnabled(flag);
        this.preButton.setEnabled(flag);
        this.nextButton.setEnabled(flag);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.height = SIZE;
        return dim;
    }

    /**
     * 增加 a <code>ChangeListener</code> to the listener list.
     *
     * @param l 监听事件
     */
    public void addChangeListener(ChangeListener l) {
        this.listenerList.add(ChangeListener.class, l);
    }

    /**
     * 移除 a <code>ChangeListener</code> from the listener list.
     *
     * @param l 监听事件
     */
    public void removeChangeListener(ChangeListener l) {
        this.listenerList.remove(ChangeListener.class, l);
    }

    // august: Process the listeners last to first
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(new ChangeEvent(this));
            }
        }
    }

    private class ButtionUI extends UIButtonUI {
        private boolean isNormalPaint = true;

        @Override
        protected void doExtraPainting(UIButton b, Graphics2D g2d, int w, int h, String selectedRoles) {
            if (isPressed(b) && b.isPressedPainted()) {
                isNormalPaint = false;
                Color pressColor = UIConstants.COMBOBOX_BTN_PRESS;
                GUIPaintUtils.fillPressed(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), pressColor);
            } else if (isRollOver(b)) {
                isNormalPaint = false;
                Color hoverColor = UIConstants.COMBOBOX_BTN_ROLLOVER;
                GUIPaintUtils.fillRollOver(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted(), hoverColor);
            } else if (b.isNormalPainted()) {
                isNormalPaint = true;
                GUIPaintUtils.fillNormal(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted());
            }
        }

        @Override
        protected void paintModelIcon(ButtonModel model, Icon icon, Graphics g, JComponent c) {
            if (isNormalPaint) {
                g.setColor(UIConstants.COMBOBOX_BTN_NORMAL);
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
            super.paintModelIcon(model, icon, g, c);
        }
    }

    private void initComponents() {
        textField = initNumberField();
        textField.setMaxValue(maxValue);
        textField.setMinValue(minValue);
        setValue(value);
        preButton = new UIButton(UIConstants.ARROW_UP_ICON) {
            public boolean shouldResponseChangeListener() {
                return false;
            }

            @Override
            public ButtonUI getUI() {
                return new ButtionUI();
            }
        };
        preButton.setRoundBorder(true, Constants.LEFT);
        nextButton = new UIButton(UIConstants.ARROW_DOWN_ICON) {
            public boolean shouldResponseChangeListener() {
                return false;
            }

            @Override
            public ButtonUI getUI() {
                return new ButtionUI();
            }
        };
        nextButton.setRoundBorder(true, Constants.LEFT);
        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
        JPanel arrowPane = new JPanel();
        arrowPane.setPreferredSize(new Dimension(LEN, SIZE));
        arrowPane.setLayout(new GridLayout(2, 1));
        preButton.setBounds(0, 1, WIDTH, HEIGHT);
        nextButton.setBounds(0, HEIGHT, WIDTH, HEIGHT);
        arrowPane.add(preButton);
        arrowPane.add(nextButton);
        add(arrowPane, BorderLayout.EAST);
        componentInitListeners();
    }

    private void componentInitListeners() {
        preButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(value + dierta);
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(value - dierta);
            }
        });
        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (isEnabled() && e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    setValue(value - e.getWheelRotation());
                }
            }
        });
        initTextFiledListeners();
    }

    protected void initTextFiledListeners(){
        textField.getDocument().removeDocumentListener(docListener);
        textField.getDocument().addDocumentListener(docListener);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                textField.getDocument().removeDocumentListener(docListener);
                textField.setValue(value);
                textField.getDocument().addDocumentListener(docListener);
            }
        });
    }

    protected UINumberField initNumberField() {
        return new UINumberField(2) {
            public boolean shouldResponseChangeListener() {
                return false;
            }
        };
    }

    private DocumentListener docListener = new DocumentListener() {
        @Override
        public void removeUpdate(DocumentEvent e) {
            setTextFieldValue(textField.getValue());
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            setTextFieldValue(textField.getValue());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setTextFieldValue(textField.getValue());
        }
    };

    /**
     * 给组件登记一个观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    /**
     * 给组件登记一个全局名字观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    public boolean shouldResponseNameListener() {
        return true;
    }

    /**
     * 程序入口  测试
     *
     * @param args 参数
     */
    public static void main(String... args) {
//        LayoutManager layoutManager = null;
//        JFrame jf = new JFrame("test");
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JPanel content = (JPanel) jf.getContentPane();
//        content.setLayout(layoutManager);
//
//        UISpinner bb = new UISpinner(0, 9, 1);
//        bb.setValue(4);
//        bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
//        content.add(bb);
//        GUICoreUtils.centerWindow(jf);
//        jf.setSize(400, 400);
//        jf.setVisible(true);
    }
}
