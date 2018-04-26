package com.fr.design.dialog;

import com.fr.base.FRContext;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: 小灰灰
 * Date: 13-7-16
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class UIDialog extends JDialog {
    public static final String OK_BUTTON = "basic_ok";
    public static final String CANCEL_BUTTON = "basic_cancel";
    private UIButton okButton, cancelButton;
    private BasicPane pane;
    private java.util.List<DialogActionListener> listeners = new ArrayList<DialogActionListener>();
    private boolean isDoOKSucceed;


    public UIDialog(Frame parent) {
        super(parent);
    }

    public UIDialog(Dialog parent) {
        super(parent);
    }


    public UIDialog(Frame parent, BasicPane pane) {
        this(parent, pane, true);
    }

    public UIDialog(Dialog parent, BasicPane pane) {
        this(parent, pane, true);
    }


    public UIDialog(Frame parent, BasicPane pane, boolean isNeedButtonPane) {
        super(parent);
        this.pane = pane;
        initComponents(isNeedButtonPane);
    }

    public UIDialog(Dialog parent, BasicPane pane, boolean isNeedButtonPane) {
        super(parent);
        this.pane = pane;
        initComponents(isNeedButtonPane);
    }

    public void setDoOKSucceed(boolean isOk) {
        isDoOKSucceed = isOk;
    }

    private void initComponents(boolean isNeedButtonPane) {
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
        contentPane.setLayout(new BorderLayout(0, 4));
        this.applyClosingAction();
        this.applyEscapeAction();
        contentPane.add(pane, BorderLayout.CENTER);
        if (isNeedButtonPane) {
            contentPane.add(this.createControlButtonPane(), BorderLayout.SOUTH);
        }
        this.setName(pane.title4PopupWindow());
        this.setModal(true);
        this.pack();
        GUICoreUtils.centerWindow(this);
    }

    private JPanel createControlButtonPane() {
        JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controlPane.add(buttonsPane, BorderLayout.EAST);

        //增加一个自定义按钮, 可以用于eg: 设为全局配置
        addCustomButton(buttonsPane);
        //确定
        addOkButton(buttonsPane);
        //取消
        addCancelButton(buttonsPane);

        this.getRootPane().setDefaultButton(okButton);

        return controlPane;
    }

    protected void addCustomButton(JPanel buttonsPane){

    }

    private void addCancelButton(JPanel buttonsPane) {
        cancelButton = new UIButton(Inter.getLocText("Cancel"));
        cancelButton.setName(CANCEL_BUTTON);
        cancelButton.setMnemonic('C');
        buttonsPane.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                doCancel();
            }
        });
    }

    private void addOkButton(JPanel buttonsPane) {
        okButton = new UIButton(Inter.getLocText("OK"));
        okButton.setName(OK_BUTTON);
        okButton.setMnemonic('O');
        buttonsPane.add(okButton);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                doOK();
            }
        });
    }


    /**
     * 添加监听器
     *
     * @param l 监听器
     *
     */
    public void addDialogActionListener(DialogActionListener l) {
        listeners.add(l);
    }

    /**
     * 清除所有监听器
     *
     */
    public void clearDialogActionListeners() {
        listeners.clear();
    }

    /**
     * 确定操作
     *
     */
    public void doOK() {
        try {
            checkValid();
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(this, exp.getMessage());
            return;
        }

        synchronized (this) {
            // dialogExit();august:怎么能这儿exit呢,dispose了就是释放了对话框的线程锁了,
            // 那么下面的doOk和原来的派遣线程就会竞争执行,万一派遣线程依赖doOk的结果、而doOK又是后执行的，那么就会出错了
            // 这Bug实在是太隐蔽了！
            isDoOKSucceed = true;
            for (DialogActionListener l : listeners) {
                try {
                    l.doOk();
                } catch (RuntimeException e) {
                    isDoOKSucceed = false;
                    FRContext.getLogger().error(e.getMessage());
                }
            }
            if (isDoOKSucceed) {
                dialogExit();
            }
        }
    }

    /**
     * 取消操作
     *
     */
    public void doCancel() {

        for (DialogActionListener l : listeners) {
            l.doCancel();
        }
        dialogExit();
    }

    /**
     * Dialog exit.
     */
    protected void dialogExit() {
        this.setVisible(false);
        this.dispose();
    }

    protected void applyClosingAction() {
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                doCancel();
            }
        });
    }

    protected void applyEscapeAction() {
        // default pane.
        JPanel defaultPane = (JPanel) this.getContentPane();

        // esp.
        InputMap inputMapAncestor = defaultPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = defaultPane.getActionMap();

        // transfer focus to CurrentEditor
        inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dialogExit");
        actionMap.put("dialogExit", new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                doCancel();
            }
        });
    }

    /**
     * 检测结果是否合法
     *
     */
    public abstract void checkValid() throws Exception;

    public void setButtonEnabled(boolean b) {
        this.okButton.setEnabled(b);
    }
}