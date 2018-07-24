package com.fr.design.gui.itree.checkboxtree;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.stable.Constants;
import sun.swing.SwingUtilities2;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalCheckBoxUI;
import javax.swing.text.View;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Maintenance tip - There were some tricks to getting this code
 * working:
 * <p/>
 * 1. You have to overwrite addMouseListener() to do nothing
 * 2. You have to add a mouse event on mousePressed by calling
 * super.addMouseListener()
 * 3. You have to replace the UIActionMap for the keyboard event
 * "pressed" with your own one.
 * 4. You have to remove the UIActionMap for the keyboard event
 * "released".
 * 5. You have to grab focus when the next state is entered,
 * otherwise clicking on the component won't get the focus.
 * 6. You have to make a TristateDecorator as a button model that
 * wraps the original button model and does state management.
 *
 * @author Dr. Heinz M. Kabutz
 */
public class TristateCheckBox extends UICheckBox {
    /**
     * This is a type-safe enumerated type
     */
    public static class State {
        private String type;

        private State(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static final State NOT_SELECTED = new State("NOT_SELECTED");
    public static final State SELECTED = new State("SELECTED");
    public static final State DO_NOT_CARE = new State("DO_NOT_CARE");

    private final TristateDecorator model;

    public TristateCheckBox(String text, Icon icon, State initial) {
        super(text, icon);
        setUI(new TristateCheckBoxUI());
        // Add a listener for when the mouse is pressed
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                grabFocus();
                model.setState(getNextState(model.getState()));
            }
        });
        // Reset the keyboard action map
        ActionMap map = new ActionMapUIResource();
        map.put("pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grabFocus();
                model.setState(getNextState(model.getState()));
            }
        });
        map.put("released", null);
        SwingUtilities.replaceUIActionMap(this, map);
        // set the model to the adapted model
        model = new TristateDecorator(getModel());
        setModel(model);
        setState(initial);
    }

    public TristateCheckBox(String text, State initial) {
        this(text, null, initial);
        setUI(new TristateCheckBoxUI());
    }

    public TristateCheckBox(String text) {
        this(text, DO_NOT_CARE);
        setUI(new TristateCheckBoxUI());
    }

    public TristateCheckBox() {
        this(null);
        setUI(new TristateCheckBoxUI());
    }

    /**
     * No one may add mouse listeners, not even Swing!
     */
    @Override
    public void addMouseListener(MouseListener l) {
    }

    /**
     * Set the new state to either SELECTED, NOT_SELECTED or
     * DO_NOT_CARE.  If state == null, it is treated as DO_NOT_CARE.
     */
    public void setState(State state) {
        model.setState(state);
    }

    /**
     * Return the current state, which is determined by the
     * selection status of the model.
     */
    public State getState() {
        return model.getState();
    }


    /**
     * Exactly which Design Pattern is this?  Is it an Adapter,
     * a Proxy or a Decorator?  In this case, my vote lies with the
     * Decorator, because we are extending functionality and
     * "decorating" the original model with a more powerful model.
     */
    private class TristateDecorator implements ButtonModel {
        private final ButtonModel other;

        private TristateDecorator(ButtonModel other) {
            this.other = other;
        }

        private void setState(State state) {
            if (state == NOT_SELECTED) {
                other.setArmed(false);
                setPressed(false);
                setSelected(false);
            } else if (state == SELECTED) {
                other.setArmed(false);
                setPressed(false);
                setSelected(true);
            }
            // either "null" or DO_NOT_CARE
            else {
                other.setArmed(true);
                setPressed(true);
                setSelected(true);
            }
        }

        /**
         * The current state is embedded in the selection / armed
         * state of the model.
         * <p/>
         * We return the SELECTED state when the checkbox is selected
         * but not armed, DO_NOT_CARE state when the checkbox is
         * selected and armed (grey) and NOT_SELECTED when the
         * checkbox is deselected.
         */
        private State getState() {
            if (isSelected() && !isArmed()) {
                // normal black tick
                return SELECTED;
            } else if (isSelected() && isArmed()) {
                // don't care grey tick
                return DO_NOT_CARE;
            } else {
                // normal deselected
                return NOT_SELECTED;
            }
        }

        /**
         * Filter: No one may change the armed status except us.
         */
        @Override
        public void setArmed(boolean b) {
        }

        /**
         * We disable focusing on the component when it is not
         * enabled.
         */
        @Override
        public void setEnabled(boolean b) {
            setFocusable(b);
            other.setEnabled(b);
        }

        /**
         * All these methods simply delegate to the "other" model
         * that is being decorated.
         */
        @Override
        public boolean isArmed() {
            return other.isArmed();
        }

        @Override
        public boolean isSelected() {
            return other.isSelected();
        }

        @Override
        public boolean isEnabled() {
            return other.isEnabled();
        }

        @Override
        public boolean isPressed() {
            return other.isPressed();
        }

        @Override
        public boolean isRollover() {
            return other.isRollover();
        }

        @Override
        public void setSelected(boolean b) {
            other.setSelected(b);
        }

        @Override
        public void setPressed(boolean b) {
            other.setPressed(b);
        }

        @Override
        public void setRollover(boolean b) {
            other.setRollover(b);
        }

        @Override
        public void setMnemonic(int key) {
            other.setMnemonic(key);
        }

        @Override
        public int getMnemonic() {
            return other.getMnemonic();
        }

        @Override
        public void setActionCommand(String s) {
            other.setActionCommand(s);
        }

        @Override
        public String getActionCommand() {
            return other.getActionCommand();
        }

        @Override
        public void setGroup(ButtonGroup group) {
            other.setGroup(group);
        }

        @Override
        public void addActionListener(ActionListener l) {
            other.addActionListener(l);
        }

        @Override
        public void removeActionListener(ActionListener l) {
            other.removeActionListener(l);
        }

        @Override
        public void addItemListener(ItemListener l) {
            other.addItemListener(l);
        }

        @Override
        public void removeItemListener(ItemListener l) {
            other.removeItemListener(l);
        }

        @Override
        public void addChangeListener(ChangeListener l) {
            other.addChangeListener(l);
        }

        @Override
        public void removeChangeListener(ChangeListener l) {
            other.removeChangeListener(l);
        }

        @Override
        public Object[] getSelectedObjects() {
            return other.getSelectedObjects();
        }
    }

    /**
     * We rotate between NOT_SELECTED, SELECTED and DO_NOT_CARE. Subclass can
     * override this method to tell the check box what next state is. Here is
     * the default implementation.
     * <code><pre>
     *   if (current == NOT_SELECTED) {
     *       return SELECTED;
     *   }
     *   else if (current == SELECTED) {
     *       return DO_NOT_CARE;
     *   }
     *   else {
     *       return NOT_SELECTED;
     *   }
     * </code></pre>
     */
    protected State getNextState(State current) {
        if (current == NOT_SELECTED) {
            return SELECTED;
        } else if (current == SELECTED) {
            return DO_NOT_CARE;
        } else {
            return NOT_SELECTED;
        }
    }

    private class TristateCheckBoxUI extends MetalCheckBoxUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            synchronized (this) {
                AbstractButton b = (AbstractButton) c;
                ButtonModel model = b.getModel();
                Dimension size = c.getSize();
                Font f = c.getFont();
                g.setFont(f);
                FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, f);

                Rectangle viewRect = new Rectangle(size);
                Rectangle iconRect = new Rectangle();
                Rectangle textRect = new Rectangle();

                Insets i = c.getInsets();
                viewRect.x += i.left;
                viewRect.y += i.top;
                viewRect.width -= (i.right + viewRect.x);
                viewRect.height -= (i.bottom + viewRect.y);

                Icon altIcon = b.getIcon();

                String text = SwingUtilities.layoutCompoundLabel(
                        c, fm, b.getText(), altIcon != null ? altIcon : getDefaultIcon(),
                        b.getVerticalAlignment(), b.getHorizontalAlignment(),
                        b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
                        viewRect, iconRect, textRect, b.getIconTextGap());

                // fill background
                if (c.isOpaque()) {
                    g.setColor(b.getBackground());
                    g.fillRect(0, 0, size.width, size.height);
                }

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (SELECTED.equals(getState())) {
                    GUIPaintUtils.fillPaint(g2d, iconRect.x, iconRect.y, iconRect.width, iconRect.height, false, Constants.NULL,
                            model.isEnabled() ? UIConstants.CHECKBOX_HOVER_SELECTED : UIConstants.DISABLED_ICON_COLOR, 0);
                } else if (model.isRollover() && !SELECTED.equals(getState())) {
                    g.setColor(UIConstants.CHECKBOX_HOVER_SELECTED);
                    g2d.drawRoundRect(iconRect.x, iconRect.y, iconRect.width - 1, iconRect.height - 1, UIConstants.ARC, UIConstants.ARC);
                } else {
                    g.setColor(UIConstants.LINE_COLOR);
                    g2d.drawRoundRect(iconRect.x, iconRect.y, iconRect.width - 1, iconRect.height - 1, UIConstants.ARC, UIConstants.ARC);
                }

                if (SELECTED.equals(getState())) {
                    UIConstants.YES_ICON.paintIcon(c, g, iconRect.x + 2, iconRect.y + 2);
                } else if (DO_NOT_CARE.equals(getState())) {
                    g.setColor(UIConstants.CHECKBOX_HOVER_SELECTED);
                    g2d.fillRoundRect(iconRect.x + 2, iconRect.y + 2, iconRect.width - 4, iconRect.height - 4, UIConstants.ARC, UIConstants.ARC);
                }
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                // Draw the Text
                drawLine(text, g, b, c, textRect, fm);
            }
        }

        private void drawLine(String text, Graphics g, AbstractButton b, JComponent c, Rectangle textRect, FontMetrics fm) {
            if (text != null) {
                View v = (View) c.getClientProperty(BasicHTML.propertyKey);
                if (v != null) {
                    v.paint(g, textRect);
                } else {
                    int mnemIndex = b.getDisplayedMnemonicIndex();
                    if (model.isEnabled()) {
                        g.setColor(b.getForeground());
                    } else {
                        g.setColor(getDisabledTextColor());
                    }
                    SwingUtilities2.drawStringUnderlineCharAt(c, g, text,
                            mnemIndex, textRect.x, textRect.y + fm.getAscent());
                }
            }
        }

    }
}