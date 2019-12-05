package com.fr.design.dialog;

import javax.swing.Icon;
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
     * 完全自定义的消息提示弹出框
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
     * 完全自定义的确认弹出框
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


}
