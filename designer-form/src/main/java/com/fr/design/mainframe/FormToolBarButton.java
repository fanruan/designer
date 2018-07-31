package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetInfoConfig;
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
    private WidgetOption widgetOption;

    public FormToolBarButton(Icon icon, Widget widget) {
        this(null, icon, widget);
    }

    public FormToolBarButton(String text, Icon icon, Widget widget) {
        super(text, icon);
        setBackground(null);
        setRolloverEnabled(true);
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
        return this.widgetOption;
    }

    public void setNameOption(WidgetOption widgetOption) {
        this.widgetOption = widgetOption;
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
        //该button只在报表块工具栏中使用，
        //parent只有FormToolBarPane一种，故可以直接强转
        final FormToolBarPane toolBarPane = (FormToolBarPane) this.getParent();
        if (DesignerMode.isAuthorityEditing()) {
            auhtorityMouseAction();
            return;
        }
        if (e.getClickCount() >= 2) {
            final FormEditToolBar editToolBar = new FormEditToolBar();
            editToolBar.populate(toolBarPane.getFToolBar(), this);
            BasicDialog dialog = editToolBar.showWindow(DesignerContext.getDesignerFrame());
            dialog.addDialogActionListener(new DialogActionAdapter() {
                public void doOk() {
                        toolBarPane.setFToolBar(editToolBar.update());
                    }
            });
            dialog.setVisible(true);
        }
    }


    private void auhtorityMouseAction() {
        if (this.isEnabled()) {
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
