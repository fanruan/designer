package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetInfoConfig;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ToolBarButton extends UIButton implements MouseListener {
    private Widget widget;
    private WidgetOption no;

    public ToolBarButton(Icon icon, Widget widget) {
        this(null, icon, widget);
    }

    public ToolBarButton(String text, Icon icon, Widget widget) {
        super(text, icon);
        this.widget = widget;
        if (widget instanceof com.fr.form.ui.Button) {
            com.fr.form.ui.Button button = (com.fr.form.ui.Button) widget;
            String iconName = button.getIconName();
            if (StringUtils.isNotEmpty(iconName)) {
                Image iimage = WidgetInfoConfig.getInstance().getIconManager().getIconImage(iconName);
                if (iimage != null) {
                    setIcon(new ImageIcon(iimage));
                }
            }
        }
        this.addMouseListener(this);
        setMargin(new Insets(0, 0, 0, 0));
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
        this.setBorderType(UIButton.NORMAL_BORDER);
        super.paintBorder(g);
    }

    /**
     * 处理鼠标点击事件
     *
     * @param e 鼠标点击事件
     */
    public void mouseClicked(MouseEvent e) {
        if (DesignerMode.isAuthorityEditing()) {
            auhtorityMouseAction();
            return;
        }
        if (e.getClickCount() >= 2) {
            if (this.getParent() instanceof ToolBarPane) {
                final ToolBarPane tb = (ToolBarPane) this.getParent();
                final EditToolBar etb = new EditToolBar();
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
        if (this.getParent() instanceof ToolBarPane && this.isEnabled()) {
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