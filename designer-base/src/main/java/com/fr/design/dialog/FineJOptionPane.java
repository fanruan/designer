package com.fr.design.dialog;

import com.fr.invoke.Reflect;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.HeadlessException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joe
 * @version 10.0
 * Created by Joe on 12/5/2019
 */
public class FineJOptionPane extends JOptionPane {

    public final static String[] OPTION_DEFAULT = { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_OK") };
    public final static String[] OPTION_YES_NO = { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Yes"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No") };
    public final static String[] OPTION_YES_NO_CANCEL = { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Yes"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_Cancel") };
    public final static String[] OPTION_OK_CANCEL = { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_OK"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_Cancel") };

    //选项类型optionType 和 选项字符串数组 一一对应
    private final static Map<Integer, String[]> OPTION_MAP = new HashMap<>();

    static {
        OPTION_MAP.put(DEFAULT_OPTION, OPTION_DEFAULT);
        OPTION_MAP.put(YES_NO_OPTION, OPTION_YES_NO);
        OPTION_MAP.put(YES_NO_CANCEL_OPTION, OPTION_YES_NO_CANCEL);
        OPTION_MAP.put(OK_CANCEL_OPTION, OPTION_OK_CANCEL);
    }

    private final static String MESSAGE_DIALOG_TITLE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Message");
    private final static String CONFIRM_DIALOG_TITLE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Confirm");
    private final static String INPUT_DIALOG_TITLE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tool_Tips");

    /**
     * 使用默认 标题 和 消息类型 的消息提示弹出框
     * @param parentComponent 父容器
     * @param message 具体的提示消息
     * @throws HeadlessException
     */
    public static void showMessageDialog(Component parentComponent, Object message)
            throws HeadlessException {
        showMessageDialog(parentComponent, message, MESSAGE_DIALOG_TITLE,
                INFORMATION_MESSAGE);
    }

    /**
     * 使用默认 Icon 的消息提示弹出框
     * @param parentComponent 父容器
     * @param message 具体的提示消息
     * @param title 标题
     * @param messageType 消息类型
     * @throws HeadlessException
     */
    public static void showMessageDialog(Component parentComponent, Object message,
                                         String title, int messageType)
            throws HeadlessException {
        showMessageDialog(parentComponent, message, title, messageType, null);
    }

    /**
     * 使用默认 选项类型、选项 和 初始选项 的消息提示弹出框
     * @param parentComponent 父容器
     * @param message 具体的提示消息
     * @param title 标题
     * @param messageType 消息类型
     * @param icon 图标
     * @throws HeadlessException
     */
    public static void showMessageDialog(Component parentComponent, Object message,
                                         String title, int messageType, Icon icon)
            throws HeadlessException {
        showMessageDialog(parentComponent, message, title, DEFAULT_OPTION,
                messageType, icon, OPTION_DEFAULT, null);
    }

    /**
     * 自定义的消息提示弹出框
     * @param parentComponent 父容器
     * @param message 具体的提示消息
     * @param title 标题
     * @param optionType 选项类型
     * @param messageType 消息类型
     * @param icon 图标
     * @param options 选项
     * @param initialValue 初始选项
     * @throws HeadlessException
     */
    public static void showMessageDialog(Component parentComponent, Object message,
                                         String title, int optionType, int messageType,
                                         Icon icon, Object[] options, Object initialValue)
            throws HeadlessException {
        showOptionDialog(parentComponent, message, title, optionType,
                messageType, icon, options, initialValue);
    }

    /**
     * 使用默认 标题 和 选项类型 的确认弹出框
     * @param parentComponent 父容器
     * @param message 具体的提示消息
     * @throws HeadlessException
     */
    public static int showConfirmDialog(Component parentComponent, Object message)
            throws HeadlessException {
        return showConfirmDialog(parentComponent, message,
                CONFIRM_DIALOG_TITLE,
                YES_NO_CANCEL_OPTION);
    }

    /**
     * 使用默认 消息类型 的确认弹出框
     * @param parentComponent 父容器
     * @param message 具体的提示消息
     * @param title 标题
     * @param optionType 选项类型
     * @throws HeadlessException
     */
    public static int showConfirmDialog(Component parentComponent, Object message,
                                        String title, int optionType) throws HeadlessException {
        return showConfirmDialog(parentComponent, message, title, optionType,
                QUESTION_MESSAGE);
    }

    /**
     * 使用默认 Icon 的确认弹出框
     * @param parentComponent 父容器
     * @param message 具体的提示消息
     * @param title 标题
     * @param optionType 选项类型
     * @param messageType 消息类型
     * @throws HeadlessException
     */
    public static int showConfirmDialog(Component parentComponent, Object message,
                                        String title, int optionType, int messageType)
            throws HeadlessException {
        return showConfirmDialog(parentComponent, message, title, optionType,
                messageType, null);
    }

    /**
     * 根据 选项类型 获取对应 选项 ，且使用默认 初始选项 的确认弹出框
     * @param parentComponent 父容器
     * @param message 具体的提示消息
     * @param title 标题
     * @param optionType 选项类型
     * @param messageType 消息类型
     * @param icon 图标
     * @throws HeadlessException
     */
    public static int showConfirmDialog(Component parentComponent, Object message,
                                        String title, int optionType, int messageType, Icon icon)
            throws HeadlessException {
        return showConfirmDialog(parentComponent, message, title, optionType,
                messageType, icon, OPTION_MAP.get(optionType), null);
    }

    /**
     * 自定义的确认弹出框
     * @param parentComponent 父容器
     * @param message 具体的提示消息
     * @param title 标题
     * @param optionType 选项类型
     * @param messageType 消息类型
     * @param icon 图标
     * @param options 选项
     * @param initialValue 初始选项
     * @throws HeadlessException
     */
    public static int showConfirmDialog(Component parentComponent, Object message,
                                        String title, int optionType, int messageType, Icon icon,
                                        Object[] options, Object initialValue)
            throws HeadlessException {
        return showOptionDialog(parentComponent, message, title, optionType,
                messageType, icon, options, initialValue);
    }

    /**
     * 指定消息内容的输入弹出框
     * @param message 消息内容
     * @return
     * @throws HeadlessException
     */
    public static String showInputDialog(Object message)
            throws HeadlessException {
        return showInputDialog(null, message);
    }

    /**
     * 使用默认 标题 和 消息类型 的输入弹出框
     * @param parentComponent 父容器
     * @param message 消息内容
     * @return
     * @throws HeadlessException
     */
    public static String showInputDialog(Component parentComponent,
                                         Object message) throws HeadlessException {
        return showInputDialog(parentComponent, message, INPUT_DIALOG_TITLE, QUESTION_MESSAGE);
    }

    /**
     * 使用默认 标题、消息类型、Icon 和 选项 的输入弹出框
     * @param parentComponent 父容器
     * @param message 消息类型
     * @param initialSelectionValue 初始选项
     * @return
     */
    public static String showInputDialog(Component parentComponent, Object message,
                                         Object initialSelectionValue) {
        return (String)showInputDialog(parentComponent, message,
                INPUT_DIALOG_TITLE, QUESTION_MESSAGE, null, null,
                initialSelectionValue);
    }

    /**
     * 使用默认 父容器、消息内容 和 初始选项 的输入弹出框
     * @param message 消息内容
     * @param initialSelectionValue 初始选项
     * @return
     */
    public static String showInputDialog(Object message, Object initialSelectionValue) {
        return showInputDialog(null, message, initialSelectionValue);
    }

    /**
     * 使用默认 Icon、选项 和 初始选项 的输入弹出框
     * @param parentComponent 父容器
     * @param message 消息内容
     * @param title 标题
     * @param messageType 消息类型
     * @return
     * @throws HeadlessException
     */
    public static String showInputDialog(Component parentComponent,
                                         Object message, String title, int messageType)
            throws HeadlessException {
        return (String)showInputDialog(parentComponent, message, title,
                messageType, null, null, null);
    }

    /**
     * 自定义的输入弹出框
     * @param parentComponent 父容器
     * @param message 消息内容
     * @param title 标题
     * @param messageType 消息类型
     * @param icon 图标
     * @param selectionValues 选项
     * @param initialSelectionValue 初始选项
     * @return
     * @throws HeadlessException
     */
    public static Object showInputDialog(Component parentComponent,
                                         Object message, String title, int messageType, Icon icon,
                                         Object[] selectionValues, Object initialSelectionValue)
            throws HeadlessException {
        JOptionPane    pane = new JOptionPane(message, messageType,
                OK_CANCEL_OPTION, icon,
                OPTION_OK_CANCEL, null);

        pane.setWantsInput(true);
        pane.setSelectionValues(selectionValues);
        pane.setInitialSelectionValue(initialSelectionValue);
        pane.setComponentOrientation(((parentComponent == null) ?
                getRootFrame() : parentComponent).getComponentOrientation());

        int style = Reflect.on(JOptionPane.class).call("styleFromMessageType", messageType).get();
        JDialog dialog = Reflect.on(pane).call("createDialog", parentComponent, title, style).get();

        pane.selectInitialValue();
        dialog.show();
        dialog.dispose();

        Object value = pane.getInputValue();

        if (value == UNINITIALIZED_VALUE) {
            return null;
        }
        return value;
    }

}
