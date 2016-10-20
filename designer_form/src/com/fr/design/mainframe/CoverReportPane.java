package com.fr.design.mainframe;

import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.icon.IconPathConstants;
import com.fr.form.share.ShareConstants;
import com.fr.general.FRScreen;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-24
 * Time: 上午9:09
 */
public class CoverReportPane extends JPanel {

    private UIButton editButton;
    private Icon controlMode = IOUtils.readIcon(IconPathConstants.TD_EL_SHARE_CLOSE_ICON_PATH);
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

    private AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 60 / 100.0F);

    public CoverReportPane() {
        this(StringUtils.EMPTY);
    }

    public CoverReportPane(String helpMsg) {
        this.helpMsg = helpMsg;
        setLayout(getCoverLayout());
        setBackground(null);
        setOpaque(false);

        editButton = new UIButton(Inter.getLocText("Edit"), IOUtils.readIcon(IconPathConstants.TD_EDIT_ICON_PATH)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(60, 24);
            }
        };
        editButton.setBorderPainted(false);
        editButton.setExtraPainted(false);
        editButton.setBackground(new Color(176, 196, 222));
        add(editButton);
        add(controlButton);
        WidgetPropertyPane.getInstance().getEditingFormDesigner().addDesignerEditListener(new DesignerEditListener() {
            @Override
            public void fireCreatorModified(DesignerEvent evt) {
                if (evt.getCreatorEventID() == (DesignerEvent.CREATOR_DELETED)
                        || evt.getCreatorEventID() == (DesignerEvent.CREATOR_RESIZED)) {
                    destroyHelpDialog();
                }
            }
        });
    }

    public String getHelpMsg() {
        return helpMsg;
    }

    public void setHelpMsg(String helpMsg) {
        this.helpMsg = helpMsg;
    }

    public void setMsgDisplay(MouseEvent e) {
        if (helpDialog == null) {
            controlMode = IOUtils.readIcon(IconPathConstants.TD_EL_SHARE_CLOSE_ICON_PATH);
            controlButton.repaint();
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
        } else {
            controlMode = IOUtils.readIcon(IconPathConstants.TD_EL_SHARE_HELP_ICON_PATH);
            controlButton.repaint();
            helpDialog.dispose();
            helpDialog = null;
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


    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(composite);
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setComposite(oldComposite);
        super.paint(g);
    }

    public void destroyHelpDialog() {
        if (helpDialog != null) {
            controlMode = IOUtils.readIcon(IconPathConstants.TD_EL_SHARE_HELP_ICON_PATH);
            controlButton.repaint();
            helpDialog.dispose();
            helpDialog = null;
        }
    }

}