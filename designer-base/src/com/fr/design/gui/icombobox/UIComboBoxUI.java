package com.fr.design.gui.icombobox;

import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonUI;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * @author zhou                                                                                                                       F
 * @since 2012-5-9下午4:33:07
 */
public class UIComboBoxUI extends BasicComboBoxUI implements MouseListener {

    protected boolean isRollover = false;

    public UIComboBoxUI() {
        super();
    }


    @Override
    protected UIButton createArrowButton() {
        arrowButton = new UIButton(UIConstants.ARROW_DOWN_ICON) {
            public boolean shouldResponseChangeListener() {
                return false;
            }

            @Override
            public ButtonUI getUI() {
                return new UIButtonUI() {
                    @Override
                    protected boolean isPressed(AbstractButton b) {
                        return model.isArmed() && model.isPressed();
                    }

                    @Override
                    protected void doExtraPainting(UIButton b, Graphics2D g2d, int w, int h, String selectedRoles) {
                        if (isPressed(b) && b.isPressedPainted()) {
                            GUIPaintUtils.fillPressed(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), UIConstants.COMBOBOX_BTN_PRESS);
                        } else if (isRollOver(b)) {
                            GUIPaintUtils.fillRollOver(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted(), UIConstants.COMBOBOX_BTN_ROLLOVER);
                        } else if (b.isNormalPainted()) {
                            GUIPaintUtils.fillNormal(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted(), UIConstants.COMBOBOX_BTN_NORMAL);
                        }
                    }
                };
            }
        };
        ((UIButton) arrowButton).setRoundBorder(true, Constants.LEFT);
        arrowButton.addMouseListener(this);
        comboBox.addMouseListener(this);
        return (UIButton) arrowButton;
    }

    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color linecolor = UIConstants.POP_DIALOG_BORDER;

        if (comboBox.isPopupVisible()) {
            arrowButton.setSelected(true);
        } else {
            linecolor = UIConstants.POP_DIALOG_BORDER;
            arrowButton.setSelected(false);
        }
        g2d.setColor(linecolor);
        if (!comboBox.isPopupVisible()) {
            g2d.drawRoundRect(0, 0, c.getWidth() - arrowButton.getWidth() + 3, c.getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
        } else {
            g2d.drawRoundRect(0, 0, c.getWidth(), c.getHeight() + 3, UIConstants.ARC, UIConstants.ARC);
            g2d.drawLine(0, c.getHeight() - 1, c.getWidth(), c.getHeight() - 1);
        }
    }

    /**
     * 覆盖之
     */
    @Override
    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        paintCurrentValue(g, bounds, hasFocus, 0);
    }

    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus, int paddingLeft) {
        ListCellRenderer renderer = comboBox.getRenderer();
        Component c;

        if (hasFocus && !isPopupVisible(comboBox)) {
            c = renderer.getListCellRendererComponent(listBox,
                    comboBox.getSelectedItem(),
                    -1,
                    true,
                    false);
        } else {
            c = renderer.getListCellRendererComponent(listBox,
                    comboBox.getSelectedItem(),
                    -1,
                    false,
                    false);
            c.setBackground(UIManager.getColor("ComboBox.background"));
        }
        c.setFont(comboBox.getFont());
        if (hasFocus && !isPopupVisible(comboBox)) {
            if (comboBox.isEnabled()) {
                c.setForeground(Color.black);
                c.setBackground(Color.WHITE);
            } else {
                c.setForeground(DefaultLookup.getColor(
                        comboBox, this, "ComboBox.disabledForeground", comboBox.getForeground()));
                c.setBackground(DefaultLookup.getColor(
                        comboBox, this, "ComboBox.disabledBackground", comboBox.getBackground()));
            }
        } else {
            if (comboBox.isEnabled()) {
                c.setForeground(comboBox.getForeground());
                c.setBackground(comboBox.getBackground());
            } else {
                c.setForeground(DefaultLookup.getColor(
                        comboBox, this, "ComboBox.disabledForeground", comboBox.getForeground()));
                c.setBackground(DefaultLookup.getColor(
                        comboBox, this, "ComboBox.disabledBackground", comboBox.getBackground()));

            }
        }

        boolean shouldValidate = false;
        if (c instanceof JPanel) {
            shouldValidate = true;
        }

        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;

        currentValuePane.paintComponent(g, c, comboBox, x + paddingLeft, y, w, h, shouldValidate);
    }

    @Override
    protected ComboPopup createPopup() {
        if (comboBox instanceof UIComboBox) {
            if (((UIComboBox) comboBox).createPopup() != null) {
                return ((UIComboBox) comboBox).createPopup();
            }
        }
        return new UIComboPopup(comboBox);
    }

    private void setRollover(boolean isRollover) {
        if (this.isRollover != isRollover) {
            this.isRollover = isRollover;
            comboBox.repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (comboBox != null) {
            ((UIComboBox) comboBox).mouseEnterEvent();
            arrowButton.getModel().setRollover(true);
            setRollover(true);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (comboBox != null) {
            ((UIComboBox) comboBox).mouseExitEvent();
            arrowButton.getModel().setRollover(false);
            setRollover(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    private class UIComboPopup extends BasicComboPopup {
        public UIComboPopup(JComboBox comboBox) {
            super(comboBox);
        }

        @Override
        protected JScrollPane createScroller() {
            return new UIScrollPane(list);
        }

        // august:重载paintBorder方法 来画出我们想要的边框..
        @Override
        public void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(UIConstants.TITLED_BORDER_COLOR);
            g2.drawRoundRect(0, -arrowButton.getHeight(), getWidth() - 1, getHeight() + arrowButton.getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
        }

        @Override
        public void setVisible(boolean b) {
            super.setVisible(b);
            if (!b) {
                comboBox.repaint();
            }
        }

        @Override
        protected JList createList() {
            return new JList(comboBox.getModel()) {

                @Override
                public void processMouseEvent(MouseEvent e) {
                    if (InputEventBaseOnOS.isControlDown(e)) {
                        e = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers() ^ DEFAULT_MODIFIER, e.getX(), e.getY(), e.getClickCount(),
                                e.isPopupTrigger());
                    }
                    super.processMouseEvent(e);
                }

                @Override
                public String getToolTipText(MouseEvent event) {
                    int index = locationToIndex(event.getPoint());
                    if (index != -1) {
                        Object value = getModel().getElementAt(index);
                        ListCellRenderer renderer = getCellRenderer();
                        Component rendererComp = renderer.getListCellRendererComponent(this, value, index, true, false);
                        if (rendererComp.getPreferredSize().width > getVisibleRect().width) {
                            String tips = (rendererComp instanceof JComponent) ? ((JComponent) rendererComp).getToolTipText() : null;
                            if (tips == null && value instanceof String) {
                                tips = (String) value;
                            }
                            return tips;
                        } else {
                            return null;
                        }
                    }
                    return null;
                }

                @Override
                public Point getToolTipLocation(MouseEvent event) {
                    int index = locationToIndex(event.getPoint());
                    if (index != -1 && StringUtils.isNotEmpty(getToolTipText(event))) {
                        Rectangle cellBounds = getCellBounds(index, index);
                        return new Point(event.getX(), cellBounds.y + cellBounds.height);
                    }
                    return null;
                }
            };
        }

    }
}