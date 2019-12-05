package com.fr.design.dialog;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.HeadlessException;

/**
 * @author Joe
 * @version 10.0
 * Created by Joe on 12/5/2019
 */
public class FineJOptionPane extends JOptionPane {

    private final static String[] OPTION_DEFAULT = { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_OK") };
    private final static String[] OPTION_YES_NO = { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Yes"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No") };
    private final static String[] OPTION_YES_NO_CANCEL = { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Yes"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_Cancel") };
    private final static String[] OPTION_OK_CANCEL = { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_OK"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_Cancel") };

    //这里用一个二维数组，根据optionType为具体的按钮建立索引
    private final static String[][] OPTIONS_ARRAY = {OPTION_DEFAULT, OPTION_YES_NO, OPTION_YES_NO_CANCEL, OPTION_OK_CANCEL};

    //默认标题
    private final static String MESSAGE_DIALOG_TITLE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Message");
    private final static String CONFIRM_DIALOG_TITLE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Confirm");

    //重写所有showMessageDialog方法
    public static void showMessageDialog(Component parentComponent, Object message)
            throws HeadlessException {
        showMessageDialog(parentComponent, message, MESSAGE_DIALOG_TITLE,
                INFORMATION_MESSAGE);
    }

    public static void showMessageDialog(Component parentComponent, Object message,
                                         String title, int messageType)
            throws HeadlessException {
        showMessageDialog(parentComponent, message, title, messageType, null);
    }

    public static void showMessageDialog(Component parentComponent, Object message,
                                         String title, int messageType, Icon icon)
            throws HeadlessException {
        showMessageDialog(parentComponent, message, title, DEFAULT_OPTION,
                messageType, icon, OPTIONS_ARRAY[0], null);
    }

    public static void showMessageDialog(Component parentComponent, Object message,
                                         String title, int optionType, int messageType,
                                         Icon icon, Object[] options, Object initialValue)
            throws HeadlessException {
        showOptionDialog(parentComponent, message, title, optionType,
                messageType, icon, options, initialValue);
    }


    //重写所有showConfirmDialog方法
    public static int showConfirmDialog(Component parentComponent, Object message)
            throws HeadlessException {
        return showConfirmDialog(parentComponent, message,
                CONFIRM_DIALOG_TITLE,
                YES_NO_CANCEL_OPTION);
    }

    public static int showConfirmDialog(Component parentComponent, Object message,
                                        String title, int optionType) throws HeadlessException {
        return showConfirmDialog(parentComponent, message, title, optionType,
                QUESTION_MESSAGE);
    }

    public static int showConfirmDialog(Component parentComponent, Object message,
                                        String title, int optionType, int messageType)
            throws HeadlessException {
        return showConfirmDialog(parentComponent, message, title, optionType,
                messageType, null);
    }

    //第一个方法，可以根据提供的optionType找到对应的国际化字符串；第二个方法，给使用者提供灵活性
    public static int showConfirmDialog(Component parentComponent, Object message,
                                        String title, int optionType, int messageType, Icon icon)
            throws HeadlessException {
        return showConfirmDialog(parentComponent, message, title, optionType,
                messageType, icon, OPTIONS_ARRAY[optionType + 1], null);
    }

    public static int showConfirmDialog(Component parentComponent, Object message,
                                        String title, int optionType, int messageType, Icon icon,
                                        Object[] objects, Object initialValue)
            throws HeadlessException {
        return showOptionDialog(parentComponent, message, title, optionType,
                messageType, icon, objects, initialValue);
    }


}
