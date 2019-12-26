package com.fr.design.gui.icombocheckbox;

import com.fr.base.BaseUtils;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.stable.StringUtils;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设计器下拉复选框组件
 * 支持全选、半选
 * 可以设置悬停颜色、一页最多显示单选框个数
 * 可以省略显示
 */
public class UIComboCheckBox extends JComponent implements UIObserver, GlobalNameObserver {
    //下拉框的值
    private Object[] values;
    //已经选中的值
    private Object[] selectedValues;

    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    private UICheckListPopup popup;
    private UITextField editor;
    private UIButton arrowButton;
    //选中的值之间显示的分隔符
    private String valueSperator;
    private static final String DEFAULT_VALUE_SPERATOR = ",";
    private static final String OMIT_TEXT = "...";

    private UIObserverListener uiObserverListener;
    private GlobalNameListener globalNameListener = null;
    private String multiComboName = StringUtils.EMPTY;
    private boolean showOmitText = true;

    public UIComboCheckBox(Object[] value) {
        this(value, DEFAULT_VALUE_SPERATOR);
    }

    /**
     * 自定义分隔符的复选框
     *
     * @param value
     * @param valueSperator
     */
    public UIComboCheckBox(Object[] value, String valueSperator) {
        values = value;
        this.valueSperator = valueSperator;
        initComponent();
    }

    /**
     * 设置鼠标悬停的背景色
     *
     * @param color
     */
    public void setCheckboxEnteredColor(Color color) {
        this.popup.setMouseEnteredColor(color);
    }

    /**
     * 设置弹出框最多显示单选的个数，超过显示滚动条
     */
    public void setPopupMaxDisplayNumber(int maxDisplayNumber) {
        this.popup.setMaxDisplayNumber(maxDisplayNumber);
    }

    /**
     * 是否要超过文本框长度后显示省略号
     *
     * @param isShowOmitText
     */
    public void isShowOmitText(boolean isShowOmitText) {
        this.showOmitText = isShowOmitText;
    }

    private void initComponent() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.popup = new UICheckListPopup(values);
        this.popup.addActionListener(new PopupAction());
        this.editor = createEditor();
        this.arrowButton = createArrowButton();
        this.add(editor);
        this.add(arrowButton);
        setText();
    }

    private UIButton createArrowButton() {
        final UIButton arrowBtn = new UIButton();
        arrowBtn.setNormalPainted(false);
        arrowBtn.setPreferredSize(new Dimension(20, 5));
        arrowBtn.setBackground(new Color(218, 218, 218));
        arrowBtn.setOpaque(true);
        arrowBtn.setIcon(getIcon());
        arrowBtn.setExtraPainted(false);
        addPopupListener(arrowBtn);
        arrowBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                arrowBtn.setBackground(new Color(200, 200, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                arrowBtn.setBackground(new Color(218, 218, 218));
            }
        });

        return arrowBtn;
    }

    private UITextField createEditor() {
        UITextField editor = new UITextField() {
            @Override
            protected void initListener() {
                this.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        attributeChange();
                    }
                });
                this.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        attributeChange();
                    }
                });
                UIComboCheckBox.this.popup.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        // do nothing
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        attributeChange();
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                        // do nothing
                    }
                });
            }
        };
        editor.setEditable(false);
        editor.setPreferredSize(new Dimension(110, 20));
        addPopupListener(editor);

        return editor;
    }

    /**
     * 弹出框事件
     *
     * @param component
     */
    private void addPopupListener(Component component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                togglePopup();
            }
        });

    }

    /**
     * 刷新复选框的列表值
     *
     * @param value
     */
    public void refreshCombo(Object[] value) {
        this.values = value;
        this.popup.addCheckboxValues(value);
    }

    /**
     * 获取复选框选中的值
     *
     * @return 复选框选中的值
     */
    public Object[] getSelectedValues() {
        return popup.getSelectedValues();
    }

    private class PopupAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(UICheckListPopup.COMMIT_EVENT)) {
                selectedValues = popup.getSelectedValues();
                setText();
                //把事件继续传递出去
                fireActionPerformed(e);
            }
        }
    }

    private void togglePopup() {
        if (this.arrowButton.isEnabled()) {
            popup.show(this, 0, getHeight());
        }
    }

    /**
     * 清除文本框
     */
    public void clearText() {
        editor.setText(StringUtils.EMPTY);
    }

    /**
     * 获取文本框内容
     */
    public String getText() {
        return editor.getText();
    }

    /**
     * 文本框设置显示值
     */
    private void setText() {
        StringBuilder builder = new StringBuilder();
        if (selectedValues != null) {
            for (Object value : selectedValues) {
                builder.append(value);
                builder.append(valueSperator);
            }
        }
        //去掉末尾多余的逗号
        String text = builder.length() > 0 ? builder.substring(0, builder.length() - 1) : StringUtils.EMPTY;
        //计算加省略号后的文本
        editor.setText(this.showOmitText ? omitEditorText(editor, text) : text);
    }

    /**
     * 根据字体大小计算得到省略后的文字
     *
     * @param textEditor
     * @param text
     * @return 省略后的文字
     */
    private static String omitEditorText(UITextField textEditor, String text) {
        char[] omitChars = OMIT_TEXT.toCharArray();
        //获取字体的大小
        FontMetrics fontMetrics = textEditor.getFontMetrics(textEditor.getFont());
        //计算省略号的长度
        int omitLength = fontMetrics.charsWidth(omitChars, 0, omitChars.length);
        String omitText = StringUtils.EMPTY;
        char[] chars = text.toCharArray();

        for (int i = 1; i <= chars.length; i++) {
            //如果原文本+省略号长度超过文本框
            if (fontMetrics.charsWidth(chars, 0, i) + omitLength > textEditor.getPreferredSize().getWidth()) {
                //从第i-1的位置截断再拼上省略号
                omitText = text.substring(0, i - 2) + OMIT_TEXT;
                break;
            }
        }

        return omitText == StringUtils.EMPTY ? text : omitText;
    }

    /**
     * 给组件登记一个观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    @Override
    public void registerChangeListener(UIObserverListener listener) {
        this.uiObserverListener = listener;
    }


    @Override
    public void setGlobalName(String name) {
        multiComboName = name;
    }

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }

    /**
     * 注册观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    @Override
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    private Icon getIcon() {
        return BaseUtils.readIcon(IconPathConstants.ARROW_ICON_PATH);
    }

    /**
     * 组件是否需要响应观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    @Override
    public boolean shouldResponseNameListener() {
        return true;
    }

    public void addActionListener(ActionListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeActionListener(ActionListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    protected void fireActionPerformed(ActionEvent e) {
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

    public void setSelectedValues(Map<Object, Boolean> map) {
        popup.setSelectedValue(map);
    }

    /**
     * 简单的测试demo
     * @param args
     */
    public static void main(String args[]) {
        UIComboCheckBox comboBox = new UIComboCheckBox(new Object[]
                {"MATA", "HANA", "KAKA", "KUKA", "INFI", "LILY", "RIBO", "CUBE", "MATA", "HANA", "KAKA", "KUKA"});

        comboBox.isShowOmitText(false);
        comboBox.setPopupMaxDisplayNumber(6);

        JPanel jPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jPanel.add(comboBox);

        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setSize(600, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(jPanel, BorderLayout.CENTER);
    }
}
