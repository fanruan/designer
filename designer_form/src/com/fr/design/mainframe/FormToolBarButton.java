package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetManager;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by harry on 2017-3-2.
 */
public class FormToolBarButton extends JButton implements MouseListener {
    private Widget widget;
    private WidgetOption no;

    public FormToolBarButton(Icon icon, Widget widget) {
        this(null, icon, widget);
    }

    public FormToolBarButton(String text, Icon icon, Widget widget) {
        super(text, icon);
        init();
        this.widget = widget;
        if (widget instanceof com.fr.form.ui.Button) {
            com.fr.form.ui.Button button = (com.fr.form.ui.Button) widget;
            String iconName = button.getIconName();
            if (StringUtils.isNotEmpty(iconName)) {
                Image iimage = WidgetManager.getProviderInstance().getIconManager().getIconImage(iconName);
                if (iimage != null) {
                    setIcon(new ImageIcon(iimage));
                }
            }
        }
        this.addMouseListener(this);
        setMargin(new Insets(0, 0, 0, 0));
    }

    private void init() {
        setBackground(null);
        setRolloverEnabled(true);
    }


    /**
     * 改变按钮的权限细粒度状态
     *
     * @param selectedRole 选择的角色
     * @param isVisible    是否可见
     */
    public void changeAuthorityState(String selectedRole, boolean isVisible) {
        this.widget.changeOnlyVisibleAuthorityState(selectedRole, isVisible);
    }

    /**
     * 是都做过权限细粒度
     *
     * @param role 选择的角色
     * @return 若是对应的该角色做过权限细粒度，则返回true
     */
    public boolean isDoneAuthorityEdited(String role) {
        return this.widget.isDoneVisibleAuthority(role);
    }

    public Widget getWidget() {
        return this.widget;
    }


    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public WidgetOption getNameOption() {
        return this.no;
    }

    public void setNameOption(WidgetOption no) {
        this.no = no;
    }


    protected void paintBorder(Graphics g) {
        paintBorder(g, this);
    }

    protected void paintBorder(Graphics g, FormToolBarButton b) {
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        GUIPaintUtils.drawBorder((Graphics2D) g, 0, 0, b.getWidth(), b.getHeight(), true, Constants.NULL, b.isDoneAuthorityEdited(selectedRoles));
    }

    /**
     * 处理鼠标点击事件
     *
     * @param e 鼠标点击事件
     */
    public void mouseClicked(MouseEvent e) {
        if (BaseUtils.isAuthorityEditing()) {
            auhtorityMouseAction();
            return;
        }
        if (e.getClickCount() >= 2) {
            if (this.getParent() instanceof FormToolBarPane) {
                final FormToolBarPane tb = (FormToolBarPane) this.getParent();
                final FormEditToolBar etb = new FormEditToolBar();
                etb.populate(tb.getFToolBar(), this);
                BasicDialog dialog = etb.showWindow(DesignerContext.getDesignerFrame());
                dialog.addDialogActionListener(new DialogActionAdapter() {
                    public void doOk() {
                        tb.setFToolBar(etb.update());
                    }
                });
                dialog.setVisible(true);
            }
        }
    }


    private void auhtorityMouseAction() {
        if (this.getParent() instanceof FormToolBarPane && this.isEnabled()) {
            this.setSelected(!this.isSelected());

        }

    }

    /**
     * 鼠标进入事件
     *
     * @param e 鼠标进入事件
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * 鼠标退出事件
     *
     * @param e 鼠标退出事件
     */
    public void mouseExited(MouseEvent e) {

    }

    /**
     * 鼠标按下事件
     *
     * @param e 鼠标事件
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * 鼠标释放事件
     *
     * @param e 鼠标事件
     */
    public void mouseReleased(MouseEvent e) {

    }
}
