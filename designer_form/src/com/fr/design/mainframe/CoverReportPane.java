package com.fr.design.mainframe;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.icon.IconPathConstants;
import com.fr.share.ShareConstants;
import com.fr.general.FRScreen;
import com.fr.general.IOUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 报表块的封面（如果后面所有的组件都有帮助信息的话就抽接口吧）
 * Coder: zack
 * Date: 2016/11/2
 * Time: 11:32
 */
public class CoverReportPane extends CoverPane implements HelpDialogHandler{
    private static final int BORDER_WIDTH = 2;
    private Icon controlMode = IOUtils.readIcon(IconPathConstants.TD_EL_SHARE_HELP_ICON_PATH);
    private JComponent controlButton = new JComponent() {
        protected void paintComponent(Graphics g) {
            g.setColor(UIConstants.NORMAL_BACKGROUND);
            g.fillArc(0, 0, ShareConstants.SHARE_EL_CONTROL_BUTTON_HW, ShareConstants.SHARE_EL_CONTROL_BUTTON_HW,
                    0, 360);
            controlMode.paintIcon(this, g, 0, 0);
        }
    };


    private String helpMsg;//帮助信息(后续帮助信息可能会变成标配,就直接放这边了)

    private ElementCaseHelpDialog helpDialog = null;

    public CoverReportPane() {
        this(StringUtils.EMPTY);
    }

    public CoverReportPane(String helpMsg) {
        super();
        this.helpMsg = helpMsg;
    }

    public String getHelpMsg() {
        return helpMsg;
    }

    public void setHelpMsg(String helpMsg) {
        this.helpMsg = helpMsg;
        //帮助信息为空就不显示帮助按钮
        if (StringUtils.isNotEmpty(helpMsg)) {
            add(controlButton);
        }
    }

    public void setMsgDisplay(MouseEvent e) {
        if (helpDialog == null) {
//            controlMode = IOUtils.readIcon(IconPathConstants.TD_EL_SHARE_CLOSE_ICON_PATH);
            controlButton.setVisible(false);
            helpDialog = new ElementCaseHelpDialog(DesignerContext.getDesignerFrame(), helpMsg);
            double screenValue = FRScreen.getByDimension(Toolkit.getDefaultToolkit().getScreenSize()).getValue();
            int offsetX = 0;
            if (screenValue < FormArea.DEFAULT_SLIDER) {
                offsetX = (int) ((1 - screenValue / FormArea.DEFAULT_SLIDER)
                        * WidgetPropertyPane.getInstance().getEditingFormDesigner().getRootComponent().getWidth() / 2);
            }
            int rX = WestRegionContainerPane.getInstance().getWidth() + e.getX() + offsetX - 227;//弹出框宽度190加上图标的宽度27加上10的偏移
            int rY = 165 + e.getY();//165是设计器最上面几个面板的高度
            helpDialog.setLocationRelativeTo(DesignerContext.getDesignerFrame(), rX, rY);
            helpDialog.showWindow();
            helpDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    helpDialog = null;
                    controlButton.setVisible(true);
                }
            });
            HelpDialogManager.getInstance().setPane(this);
        }
    }

    protected LayoutManager getCoverLayout() {
        return new LayoutManager() {

            @Override
            public void removeLayoutComponent(Component comp) {
            }

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                return parent.getPreferredSize();
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return null;
            }

            @Override
            public void layoutContainer(Container parent) {
                UIButton editButton = getEditButton();
                int width = parent.getParent().getWidth();
                int height = parent.getParent().getHeight();
                int preferWidth = editButton.getPreferredSize().width;
                int preferHeight = editButton.getPreferredSize().height;
                editButton.setBounds((width - preferWidth) / 2, (height - preferHeight) / 2, preferWidth, preferHeight);
                controlButton.setBounds((width - 28), 0, 27, 27);
            }

            @Override
            public void addLayoutComponent(String name, Component comp) {
            }
        };
    }

    public void destroyHelpDialog() {
        if (helpDialog != null) {
            controlMode = IOUtils.readIcon(IconPathConstants.TD_EL_SHARE_HELP_ICON_PATH);
            controlButton.repaint();
            helpDialog.dispose();
            helpDialog = null;
        }
    }

    @Override
    protected Rectangle getPaintBorderBounds(){
        return new Rectangle(BORDER_WIDTH - 1, BORDER_WIDTH- 1, getWidth() - BORDER_WIDTH * 2 , getHeight() - BORDER_WIDTH * 2);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
//        if (aFlag) {
//            HelpDialogManager.getInstance().setPane(this);
//        }
    }
}
