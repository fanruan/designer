package com.fr.design.gui.ibutton;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.constants.UIConstants;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.stable.Constants;
import sun.swing.SwingUtilities2;

import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUIPaintUtils;

import static com.fr.design.utils.gui.GUIPaintUtils.fillPaint;

public class UISliderButtonUI extends BasicButtonUI {

    private Rectangle viewRec = new Rectangle();
    private Rectangle textRec = new Rectangle();
    private Rectangle iconRec = new Rectangle();

    @Override
    public void paint(Graphics g, JComponent c) {
        UISliderButton b = (UISliderButton) c;
        Graphics2D g2d = (Graphics2D) g;
        int w = b.getWidth();
        int h = b.getHeight();

        String text = initRecAndGetText(b, SwingUtilities2.getFontMetrics(b, g), b.getWidth(), b.getHeight());
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        clearTextShiftOffset();

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (b.isExtraPainted()) {
            if (isPressed(b) && b.isPressedPainted()) {
                fillPressed(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles));
            } else if (isRollOver(b)) {
                fillRollOver(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted());
            } else if (b.isNormalPainted()) {
                fillNormal(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted());
            }
        }
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        paintContent(g, b, text);
    }

    protected boolean isRollOver(AbstractButton b) {
        ButtonModel model = b.getModel();
        return model.isRollover() && !b.isSelected();
    }

    protected boolean isPressed(AbstractButton b) {
        ButtonModel model = b.getModel();
        return (model.isArmed() && model.isPressed()) || b.isSelected();
    }

    private void paintContent(Graphics g, AbstractButton b, String text) {
        if (b.getIcon() != null) {
            paintIcon(g, b);
        }
        if (!StringUtils.isEmpty(text)) {
            paintText(g, b, text);
        }
    }

    private void paintText(Graphics g, AbstractButton b, String text) {
        View v = (View) b.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            v.paint(g, textRec);
            return;
        }
        FontMetrics fm = SwingUtilities2.getFontMetrics(b, g);
        int mnemonicIndex = b.getDisplayedMnemonicIndex();
        if (b.isEnabled()) {
            g.setColor(UIConstants.FONT_COLOR);
        } else {
            g.setColor(UIConstants.LINE_COLOR);
        }

        SwingUtilities2.drawStringUnderlineCharAt(b, g, text, mnemonicIndex, textRec.x + getTextShiftOffset(), textRec.y + fm.getAscent() + getTextShiftOffset());
    }

    private String initRecAndGetText(AbstractButton b, FontMetrics fm, int width, int height) {
        Insets i = b.getInsets();
        viewRec.x = i.left;
        viewRec.y = i.top;
        viewRec.width = width - (i.right + viewRec.x);
        viewRec.height = height - (i.bottom + viewRec.y);
        textRec.x = textRec.y = textRec.width = textRec.height = 0;
        iconRec.x = iconRec.y = iconRec.width = iconRec.height = 0;
        // layout the text and icon
        return SwingUtilities.layoutCompoundLabel(
                b, fm, b.getText(), b.getIcon(),
                b.getVerticalAlignment(), b.getHorizontalAlignment(),
                b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
                viewRec, iconRec, textRec,
                b.getText() == null ? 0 : b.getIconTextGap());
    }

    protected void paintBorder(Graphics g, UISliderButton b) {
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        GUIPaintUtils.drawBorder((Graphics2D) g, 0, 0, b.getWidth(), b.getHeight(), b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles));

    }

    protected void paintIcon(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        Icon icon = b.getIcon();
        Icon tmpIcon = null;
        if (icon == null) {
            return;
        }
        Icon selectedIcon = null;
		/* the fallback icon should be based on the selected state */
        if (model.isSelected()) {
            selectedIcon = (Icon) b.getSelectedIcon();
            if (selectedIcon != null) {
                icon = selectedIcon;
            }
        }
        if (!model.isEnabled()) {
            if (model.isSelected()) {
                tmpIcon = (Icon) b.getDisabledSelectedIcon();
                if (tmpIcon == null) {
                    tmpIcon = selectedIcon;
                }
            }
            if (tmpIcon == null) {
                tmpIcon = (Icon) b.getDisabledIcon();
            }
        } else if (model.isPressed() && model.isArmed()) {
            tmpIcon = (Icon) b.getPressedIcon();
            if (tmpIcon != null) {
                // revert back to 0 offset
                clearTextShiftOffset();
            }
        } else if (b.isRolloverEnabled() && model.isRollover()) {
            if (model.isSelected()) {
                tmpIcon = (Icon) b.getRolloverSelectedIcon();
                if (tmpIcon == null) {
                    tmpIcon = selectedIcon;
                }
            }
            if (tmpIcon == null) {
                tmpIcon = (Icon) b.getRolloverIcon();
            }
        }
        if (tmpIcon != null) {
            icon = tmpIcon;
        }
        paintModelIcon(model, icon, g, c);
    }

    private void paintModelIcon(ButtonModel model, Icon icon, Graphics g, JComponent c) {
        if (model.isPressed() && model.isArmed()) {
            icon.paintIcon(c, g, iconRec.x + getTextShiftOffset(),
                    iconRec.y + getTextShiftOffset());
        } else {
            icon.paintIcon(c, g, iconRec.x, iconRec.y);
        }
    }

    private void fillNormal(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, boolean isAuthorityEdited, boolean isPressedPainted) {
        GradientPaint gp;
        if (DesignerMode.isAuthorityEditing() && isAuthorityEdited) {
            gp = new GradientPaint(1, 1, UIConstants.AUTHORITY_BLUE, 1, height - 1f, UIConstants.AUTHORITY_DARK_BLUE);
        } else if (isPressedPainted) {
            gp = new GradientPaint(1, 1, UIConstants.SELECT_TAB, 1, height - 1, UIConstants.SELECT_TAB);
        }else{
            gp = new GradientPaint(1, 1, UIConstants.POP_DIALOG_BORDER, 1, height - 1, UIConstants.POP_DIALOG_BORDER);
        }

        fillPaint(g2d, x, y, width, height, isRound, rectDirection, gp, UIConstants.ARC);
    }

    private void fillRollOver(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, boolean isAuthorityEdited, boolean isPressedPainted) {
        GradientPaint gp;
        if (DesignerMode.isAuthorityEditing() && isAuthorityEdited) {
            gp = new GradientPaint(1, 1, UIConstants.AUTHORITY_BLUE, 1, height - 1, UIConstants.HOVER_BLUE);
        } else if (isPressedPainted) {
            gp = new GradientPaint(1, 1, UIConstants.POP_DIALOG_BORDER, 1, height - 1, UIConstants.POP_DIALOG_BORDER);
        }else {
            gp = new GradientPaint(1, 1, UIConstants.POP_DIALOG_BORDER, 1, height - 1, UIConstants.POP_DIALOG_BORDER);
        }
        fillPaint(g2d, x, y, width, height, isRound, rectDirection, gp, UIConstants.ARC);
    }

    private void fillPressed(Graphics2D g2d, int x, int y, int width, int height, boolean isRound, int rectDirection, boolean isAuthorityEdited) {
        Color oldColor = g2d.getColor();
        if (DesignerMode.isAuthorityEditing() && isAuthorityEdited) {
            g2d.setColor(UIConstants.AUTHORITY_PRESS_BLUE);
        } else {
            g2d.setColor(UIConstants.POP_DIALOG_BORDER);
        }
        Shape oldClip = g2d.getClip();
        if (isRound) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.clip(new RoundRectangle2D.Double(x, y, width, height, UIConstants.ARC, UIConstants.ARC));
            g2d.fillRoundRect(x, y, width, height, UIConstants.ARC, UIConstants.ARC);
            g2d.setClip(oldClip);
            if (rectDirection == Constants.RIGHT) {
                g2d.fillRect(width - 2, y, x + 2, height);
            } else if (rectDirection == Constants.LEFT) {
                g2d.fillRect(x, y, x + 2, height);
            }
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        } else {
            g2d.clip(new Rectangle(x, y, width, height));
            g2d.fillRect(0, 0, width, height);
            g2d.setClip(oldClip);
        }

        g2d.setColor(oldColor);
    }
}