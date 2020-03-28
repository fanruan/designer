/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.imenu;

import com.fr.design.utils.ColorRoutines;
import com.fr.design.utils.ThemeUtils;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-4
 * Time: 下午3:11
 */
public class UIBasicMenuItemUI extends MenuItemUI {
    /* diagnostic aids -- should be false for production builds. */
    private static final boolean TRACE = false; // trace creates and disposes
    private static final boolean VERBOSE = false; // show reuse hits/misses
    private static final boolean DEBUG = true; // show bad params, misc.
    private static final int DEFAULT_TEXT_ICON_GAP = 8; // Should be from table
    private static final int DEFAULT_ICON_GAP = 4; // Should be from table

    protected JMenuItem menuItem = null;
    protected Color selectionBackground;
    protected Color selectionForeground;
    protected Color disabledForeground;
    protected Color acceleratorForeground;
    protected Color acceleratorSelectionForeground;
    private String acceleratorDelimiter;

    protected static int defaultTextIconGap, defaultIconGap;
    protected Font acceleratorFont;

    protected MouseInputListener mouseInputListener;
    protected MenuDragMouseListener menuDragMouseListener;
    protected MenuKeyListener menuKeyListener;
    private PropertyChangeListener propertyChangeListener;

    protected Icon arrowIcon = null;
    protected Icon checkIcon = null;

    protected boolean oldBorderPainted;

    /**
     * Used for accelerator binding, lazily created.
     */
    InputMap windowInputMap;

    /* Client Property keys for text and accelerator text widths */
    static final String MAX_TEXT_WIDTH = "maxTextWidth";
    static final String MAX_ACC_WIDTH = "maxAccWidth";
    static final String MAX_ICON_WIDTH = "maxIconWidth";

    /**
     * 加载UI
     *
     * @param c 组件
     */
    public void installUI(JComponent c) {
        menuItem = (JMenuItem) c;
        installDefaults();
        installComponents(menuItem);
        installListeners();
        installKeyboardActions();
    }

    /**
     * @since 1.3
     */
    protected void installComponents(JMenuItem menuItem) {
        BasicHTML.updateRenderer(menuItem, menuItem.getText());
    }

    protected String getPropertyPrefix() {
        return "MenuItem";
    }

    protected void installListeners() {
        if ((mouseInputListener = createMouseInputListener(menuItem)) != null) {
            menuItem.addMouseListener(mouseInputListener);
            menuItem.addMouseMotionListener(mouseInputListener);
        }

        if ((menuDragMouseListener = createMenuDragMouseListener(menuItem)) != null) {
            menuItem.addMenuDragMouseListener(menuDragMouseListener);
        }

        // removed in 1.3.06 because installing additional listeners
        // is unnecessary and caused malfunctions with sub menus
//		if((menuKeyListener = createMenuKeyListener(menuItem)) != null) {
//			menuItem.addMenuKeyListener(menuKeyListener);
//		}

        if ((propertyChangeListener = createPropertyChangeListener(menuItem)) != null) {
            menuItem.addPropertyChangeListener(propertyChangeListener);
        }
    }

    protected void installKeyboardActions() {
        ActionMap actionMap = getActionMap();

        SwingUtilities.replaceUIActionMap(menuItem, actionMap);
        updateAcceleratorBinding();
    }

    /**
     * 去除UI
     *
     * @param c 组件
     */
    public void uninstallUI(JComponent c) {
        menuItem = (JMenuItem) c;
        uninstallDefaults();
        uninstallComponents(menuItem);
        uninstallListeners();
        uninstallKeyboardActions();

        //Remove the textWidth and accWidth values from the parent's Client Properties.
        Container parent = menuItem.getParent();
        if ((parent != null && parent instanceof JComponent) && !(menuItem instanceof JMenu && ((JMenu) menuItem).isTopLevelMenu())) {
            JComponent p = (JComponent) parent;
            p.putClientProperty(MAX_ACC_WIDTH, null);
            p.putClientProperty(MAX_TEXT_WIDTH, null);
        }

        menuItem = null;
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(menuItem);
        menuItem.setBorderPainted(oldBorderPainted);
        if (menuItem.getMargin() instanceof UIResource) {
            menuItem.setMargin(null);
        }
        if (arrowIcon instanceof UIResource) {
            arrowIcon = null;
        }
        if (checkIcon instanceof UIResource) {
            checkIcon = null;
        }
    }

    /**
     * @since 1.3
     */
    protected void uninstallComponents(JMenuItem menuItem) {
        BasicHTML.updateRenderer(menuItem, "");
    }

    protected void uninstallListeners() {
        if (mouseInputListener != null) {
            menuItem.removeMouseListener(mouseInputListener);
            menuItem.removeMouseMotionListener(mouseInputListener);
        }
        if (menuDragMouseListener != null) {
            menuItem.removeMenuDragMouseListener(menuDragMouseListener);
        }
//		if(menuKeyListener != null) {
//			menuItem.removeMenuKeyListener(menuKeyListener);
//		}
        if (propertyChangeListener != null) {
            menuItem.removePropertyChangeListener(propertyChangeListener);
        }

        mouseInputListener = null;
        menuDragMouseListener = null;
        menuKeyListener = null;
        propertyChangeListener = null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(menuItem, null);
        if (windowInputMap != null) {
            SwingUtilities.replaceUIInputMap(menuItem, JComponent.WHEN_IN_FOCUSED_WINDOW, null);
            windowInputMap = null;
        }
    }

    protected MouseInputListener createMouseInputListener(JComponent c) {
        return new MouseInputHandler();
    }

    protected MenuDragMouseListener createMenuDragMouseListener(JComponent c) {
        return new MenuDragMouseHandler();
    }

    private PropertyChangeListener createPropertyChangeListener(JComponent c) {
        return new PropertyChangeHandler();
    }

    ActionMap getActionMap() {
        String propertyPrefix = getPropertyPrefix();
        String uiKey = propertyPrefix + ".actionMap";
        ActionMap am = (ActionMap) UIManager.get(uiKey);

        if (am == null) {
            am = createActionMap();
            UIManager.getLookAndFeelDefaults().put(uiKey, am);
        }

        return am;
    }

    ActionMap createActionMap() {
        ActionMap map = new ActionMapUIResource();
        map.put("doClick", new ClickAction());

        return map;
    }

    InputMap createInputMap(int condition) {
        if (condition == JComponent.WHEN_IN_FOCUSED_WINDOW) {
            return new ComponentInputMapUIResource(menuItem);
        }
        return null;
    }

    void updateAcceleratorBinding() {
        KeyStroke accelerator = menuItem.getAccelerator();

        if (windowInputMap != null) {
            windowInputMap.clear();
        }

        if (accelerator != null) {
            if (windowInputMap == null) {
                windowInputMap = createInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
                SwingUtilities.replaceUIInputMap(menuItem,
                        JComponent.WHEN_IN_FOCUSED_WINDOW, windowInputMap);
            }
            windowInputMap.put(accelerator, "doClick");
        }
    }

    public Dimension getMinimumSize(JComponent c) {
        Dimension d = null;
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            d = getPreferredSize(c);
            d.width -= (double) v.getPreferredSpan(View.X_AXIS) - (double) v.getMinimumSpan(View.X_AXIS);
        }
        return d;
    }

    public Dimension getPreferredSize(JComponent c) {
        return getPreferredMenuItemSize(c, checkIcon, arrowIcon, DEFAULT_TEXT_ICON_GAP);
    }

    /**
     * Renders the text of the current menu item.
     * <p/>
     *
     * @param g        graphics context
     * @param menuItem menu item to render
     * @param textRect bounding rectangle for rendering the text
     * @param text     string to render
     * @since 1.4
     */
    protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
        ButtonModel model = menuItem.getModel();
        FontMetrics fm = g.getFontMetrics();
        int mnemIndex = menuItem.getDisplayedMnemonicIndex();

        if (!model.isEnabled()) {
            // *** paint the text disabled
            g.setColor(ThemeUtils.MENU_DISABLED_FG_COLOR);
            BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent());
        } else {
            boolean isSelected = menuItem instanceof JMenu && model.isSelected();
            // *** paint the text normally
            if (isTopLevelMenu()) {
                if (menuItem.getClientProperty("rollover") == Boolean.TRUE &&
                        ThemeUtils.MENU_ROLLOVER && !model.isSelected()) {
                    g.setColor(ThemeUtils.MENU_ROLLOVER_FG_COLOR);
                } else {
                    if (!(menuItem.getForeground() instanceof ColorUIResource)) {
                        g.setColor(menuItem.getForeground());
                    } else {
                        g.setColor(ThemeUtils.MENU_FONT_COLOR);
                    }
                }
            } else if (model.isArmed() || isSelected) {
                g.setColor(ThemeUtils.MENU_SELECTED_TEXT_COLOR);
            } else {
                // normal
                g.setColor(menuItem.getForeground());
            }

            BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent());
        }
    }

    public Dimension getMaximumSize(JComponent c) {
        Dimension d = null;
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            d = getPreferredSize(c);
            d.width += (double) v.getMaximumSpan(View.X_AXIS) - (double) v.getPreferredSpan(View.X_AXIS);
        }
        return d;
    }

    // these rects are used for painting and preferredsize calculations.
    // they used to be regenerated constantly.  Now they are reused.
    static Rectangle zeroRect = new Rectangle(0, 0, 0, 0);
    static Rectangle iconRect = new Rectangle();
    static Rectangle textRect = new Rectangle();
    static Rectangle acceleratorRect = new Rectangle();
    static Rectangle checkIconRect = new Rectangle();
    static Rectangle arrowIconRect = new Rectangle();
    static Rectangle viewRect = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
    static Rectangle r = new Rectangle();

    private void resetRects() {
        iconRect.setBounds(zeroRect);
        textRect.setBounds(zeroRect);
        acceleratorRect.setBounds(zeroRect);
        checkIconRect.setBounds(zeroRect);
        arrowIconRect.setBounds(zeroRect);
        viewRect.setBounds(0, 0, Short.MAX_VALUE, Short.MAX_VALUE);
        r.setBounds(zeroRect);
    }

    /**
     * We draw the background in paintMenuItem()
     * so override update (which fills the background of opaque
     * components by default) to just call paint().
     */
    public void update(Graphics g, JComponent c) {
        paint(g, c);
    }

    public void paint(Graphics g, JComponent c) {
        paintMenuItem(g, c, checkIcon, arrowIcon,
                selectionBackground, selectionForeground, DEFAULT_TEXT_ICON_GAP);
    }

    /*
     * Returns true if the component is a JMenu and it is a top
     * level menu (on the menubar).
     */
    private boolean isTopLevelMenu() {
        if ((menuItem instanceof JMenu) && (((JMenu) menuItem).isTopLevelMenu())) {
            return true;
        }

        return false;
    }

    public MenuElement[] getPath() {
        MenuSelectionManager m = MenuSelectionManager.defaultManager();
        MenuElement[] oldPath = m.getSelectedPath();
        MenuElement[] newPath;
        int i = oldPath.length;
        if (i == 0) {
            return new MenuElement[0];
        }
        Component parent = menuItem.getParent();
        if (oldPath[i - 1].getComponent() == parent) {
            // The parent popup menu is the last so far
            newPath = new MenuElement[i + 1];
            System.arraycopy(oldPath, 0, newPath, 0, i);
            newPath[i] = menuItem;
        } else {
            // A sibling menuitem is the current selection
            //
            //  This probably needs to handle 'exit submenu into
            // a menu item.  Search backwards along the current
            // selection until you find the parent popup menu,
            // then copy up to that and add yourself...
            int j;
            for (j = oldPath.length - 1; j >= 0; j--) {
                if (oldPath[j].getComponent() == parent) {
                    break;
                }
            }
            newPath = new MenuElement[j + 2];
            System.arraycopy(oldPath, 0, newPath, 0, j + 1);
            newPath[j + 1] = menuItem;

        }
        return newPath;
    }

    protected class MouseInputHandler implements MouseInputListener {
        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
            MenuSelectionManager manager = MenuSelectionManager.defaultManager();
            Point p = e.getPoint();
            boolean isxContains = p.x >= 0 && p.x < menuItem.getWidth();
            boolean isyContains = p.y >= 0 && p.y < menuItem.getHeight();
            if (isxContains && isyContains) {
                doClick(manager);
            } else {
                manager.processMouseEvent(e);
            }
        }

        public void mouseEntered(MouseEvent e) {
            MenuSelectionManager manager = MenuSelectionManager.defaultManager();
            int modifiers = e.getModifiers();
            // 4188027: drag enter/exit added in JDK 1.1.7A, JDK1.2
            if ((modifiers & (InputEvent.BUTTON1_MASK | InputEvent.BUTTON2_MASK | InputEvent.BUTTON3_MASK)) != 0) {
                MenuSelectionManager.defaultManager().processMouseEvent(e);
            } else {
                manager.setSelectedPath(getPath());
            }
        }

        public void mouseExited(MouseEvent e) {
            MenuSelectionManager manager = MenuSelectionManager.defaultManager();

            int modifiers = e.getModifiers();
            // 4188027: drag enter/exit added in JDK 1.1.7A, JDK1.2
            if ((modifiers & (InputEvent.BUTTON1_MASK | InputEvent.BUTTON2_MASK | InputEvent.BUTTON3_MASK)) != 0) {
                MenuSelectionManager.defaultManager().processMouseEvent(e);
            } else {

                MenuElement[] path = manager.getSelectedPath();
                if (path.length > 1) {
                    MenuElement[] newPath = new MenuElement[path.length - 1];
                    int i, c;
                    for (i = 0, c = path.length - 1; i < c; i++) {
                        newPath[i] = path[i];
                    }
                    manager.setSelectedPath(newPath);
                }
            }
        }

        public void mouseDragged(MouseEvent e) {
            MenuSelectionManager.defaultManager().processMouseEvent(e);
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    private class MenuDragMouseHandler implements MenuDragMouseListener {
        public void menuDragMouseEntered(MenuDragMouseEvent e) {
        }

        public void menuDragMouseDragged(MenuDragMouseEvent e) {
            MenuSelectionManager manager = e.getMenuSelectionManager();
            MenuElement[] path = e.getPath();
            manager.setSelectedPath(path);
        }

        public void menuDragMouseExited(MenuDragMouseEvent e) {
        }

        public void menuDragMouseReleased(MenuDragMouseEvent e) {
            MenuSelectionManager manager = e.getMenuSelectionManager();
            Point p = e.getPoint();
            boolean isxContains = p.x >= 0 && p.x < menuItem.getWidth();
            boolean isyContains = p.y >= 0 && p.y < menuItem.getHeight();
            if (isxContains && isyContains) {
                doClick(manager);
            } else {
                manager.clearSelectedPath();
            }
        }
    }

    private class PropertyChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            String name = e.getPropertyName();
            boolean isquick = ComparatorUtils.equals(name, "displayedMnemonic") || ComparatorUtils.equals(name, "accelerator");
            boolean istextRelate = ComparatorUtils.equals(name, "text") || ComparatorUtils.equals(name, "font");
            if (ComparatorUtils.equals(name, "labelFor") || isquick) {
                updateAcceleratorBinding();
            } else if (istextRelate || ComparatorUtils.equals(name, "foreground")) {
                // remove the old html view client property if one
                // existed, and install a new one if the text installed
                // into the JLabel is html source.
                JMenuItem lbl = ((JMenuItem) e.getSource());
                String text = lbl.getText();
                BasicHTML.updateRenderer(lbl, text);
            }
        }
    }

    private static class ClickAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            JMenuItem mi = (JMenuItem) e.getSource();
            MenuSelectionManager.defaultManager().clearSelectedPath();
            mi.doClick();
        }
    }

    /**
     * Call this method when a menu item is to be activated.
     * This method handles some of the details of menu item activation
     * such as clearing the selected path and messaging the
     * JMenuItem's doClick() method.
     *
     * @param msm A MenuSelectionManager. The visual feedback and
     *            internal bookkeeping tasks are delegated to
     *            this MenuSelectionManager. If <code>null</code> is
     *            passed as this argument, the
     *            <code>MenuSelectionManager.defaultManager</code> is
     *            used.
     * @see MenuSelectionManager
     * @see JMenuItem#doClick(int)
     * @since 1.4
     */
    protected void doClick(MenuSelectionManager msm) {
        // Visual feedback
        if (msm == null) {
            msm = MenuSelectionManager.defaultManager();
        }
        msm.clearSelectedPath();
        menuItem.doClick(0);
    }

    /**
     * This is to see if the menu item in question is part of the
     * system menu on an internal frame.
     * The Strings that are being checked can be found in
     * MetalInternalFrameTitlePaneUI.java,
     * WindowsInternalFrameTitlePaneUI.java, and
     * MotifInternalFrameTitlePaneUI.java.
     *
     * @since 1.4
     */
    private boolean isInternalFrameSystemMenu() {
        String actionCommand = menuItem.getActionCommand();
        return (StringUtils.equals(actionCommand,"Close") || StringUtils.equals(actionCommand,"Minimize")
                || StringUtils.equals(actionCommand,"Restore") || StringUtils.equals(actionCommand,"Maximize"));
    }

    //////////////////////////////////////////////////////////

    public static class EmptyIcon implements Icon {
        int width;
        int height;

        public EmptyIcon(int width, int height) {
            this.height = height;
            this.width = width;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            return;
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }
    }

    /**
     * 创建UI
     *
     * @param c 组件
     * @return 组件UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new UIBasicMenuItemUI();
    }

    protected void installDefaults() {
        final String prefix = getPropertyPrefix();

        acceleratorFont = UIManager.getFont("MenuItem.acceleratorFont");

        menuItem.setOpaque(true);
        if (menuItem.getMargin() == null || (menuItem.getMargin() instanceof UIResource)) {
            menuItem.setMargin(UIManager.getInsets(prefix + ".margin"));
        }

        LookAndFeel.installBorder(menuItem, prefix + ".border");
        oldBorderPainted = menuItem.isBorderPainted();
        menuItem.setBorderPainted(((Boolean) (UIManager.get(prefix + ".borderPainted"))).booleanValue());
        installFont(prefix, menuItem);

        // MenuItem specific defaults
        if (selectionBackground == null || selectionBackground instanceof UIResource) {
            selectionBackground = UIManager.getColor(prefix + ".selectionBackground");
        }
        if (selectionForeground == null || selectionForeground instanceof UIResource) {
            selectionForeground = UIManager.getColor(prefix + ".selectionForeground");
        }
        if (disabledForeground == null || disabledForeground instanceof UIResource) {
            disabledForeground = UIManager.getColor(prefix + ".disabledForeground");
        }
        if (acceleratorForeground == null || acceleratorForeground instanceof UIResource) {
            acceleratorForeground = UIManager.getColor(prefix + ".acceleratorForeground");
        }
        if (acceleratorSelectionForeground == null || acceleratorSelectionForeground instanceof UIResource) {
            acceleratorSelectionForeground = UIManager.getColor(prefix + ".acceleratorSelectionForeground");
        }

        // Get accelerator delimiter
        acceleratorDelimiter = UIManager.getString("MenuItem.acceleratorDelimiter");
        if (acceleratorDelimiter == null) {
            acceleratorDelimiter = "+";
        }

        // Icons
        if (arrowIcon == null || arrowIcon instanceof UIResource) {
            arrowIcon = UIManager.getIcon(prefix + ".arrowIcon");
        }
        if (checkIcon == null || checkIcon instanceof UIResource) {
            checkIcon = UIManager.getIcon(prefix + ".checkIcon");
        }

        defaultTextIconGap = DEFAULT_TEXT_ICON_GAP; // Should be from table
        defaultIconGap = DEFAULT_ICON_GAP; // Should be from table
    }

    private void installFont(final String prefix, final JComponent menuItem) {
        if (menuItem == null) {
            return;
        }
        LookAndFeel.installColorsAndFont(menuItem, prefix + ".background", prefix + ".foreground", prefix + ".font");
    }

    protected Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon,
                                                 Icon arrowIcon, int defaultTextIconGap) {
        JMenuItem b = (JMenuItem) c;
        Icon icon = b.getIcon();
        String text = b.getText();
        KeyStroke accelerator = b.getAccelerator();
        String acceleratorText = "";
        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                //acceleratorText += "-";
                acceleratorText += acceleratorDelimiter;
            }
            int keyCode = accelerator.getKeyCode();
            if (keyCode != 0) {
                acceleratorText += KeyEvent.getKeyText(keyCode);
            } else {
                acceleratorText += accelerator.getKeyChar();
            }
        }
        caulateFont(b, icon, text, acceleratorText);
        Container parent = menuItem.getParent();
        doWithTopMenu(parent, icon);
        if (!isTopLevelMenu()) {
            // Add in the checkIcon
            r.width += checkIconRect.width;
            r.width += defaultTextIconGap;
            // Add in the arrowIcon
            r.width += defaultTextIconGap;
            r.width += arrowIconRect.width;
        }
        r.width += 2 * defaultTextIconGap;
        Insets insets = b.getInsets();
        if (insets != null) {
            r.width += insets.left + insets.right;
            r.height += insets.top + insets.bottom;
        }
        // if the width is even, bump it up one. This is critical
        // for the focus dash line to draw properly
        if (r.width % 2 == 0) {
            r.width++;
        }
        if (r.height % 2 == 0) {
            r.height++;
        }
        return r.getSize();
    }

    private void doWithTopMenu(Container parent, Icon icon) {
        if (parent != null && parent instanceof JComponent &&
                !(menuItem instanceof JMenu && ((JMenu) menuItem).isTopLevelMenu())) {
            JComponent p = (JComponent) parent;

            //Get widest text so far from parent, if no one exists null is returned.
            Integer maxTextWidth = (Integer) p.getClientProperty(MAX_TEXT_WIDTH);
            Integer maxAccWidth = (Integer) p.getClientProperty(MAX_ACC_WIDTH);
            Integer maxIconWidth = (Integer) p.getClientProperty(MAX_ICON_WIDTH); // WAS ME

            int maxTextValue = maxTextWidth != null ? maxTextWidth.intValue() : 0;
            int maxAccValue = maxAccWidth != null ? maxAccWidth.intValue() : 0;
            int maxIconValue = maxIconWidth != null ? maxIconWidth.intValue() : 0;

            //Compare the text widths, and adjust the r.width to the widest.
            if (r.width < maxTextValue) {
                r.width = maxTextValue;
            } else {
                p.putClientProperty(UIBasicMenuItemUI.MAX_TEXT_WIDTH, new Integer(r.width));
            }

            //Compare the accelarator widths.
            if (acceleratorRect.width > maxAccValue) {
                maxAccValue = acceleratorRect.width;
                p.putClientProperty(UIBasicMenuItemUI.MAX_ACC_WIDTH, new Integer(acceleratorRect.width));
            }

            //Compare the accelarator widths.
            if (icon != null && icon.getIconWidth() > maxIconValue) {
                maxIconValue = icon.getIconWidth();
                p.putClientProperty(UIBasicMenuItemUI.MAX_ICON_WIDTH, new Integer(maxIconValue));
            }

            //Add on the widest accelerator
            // If-clause added in 1.3.03
            if (maxAccValue > 0) {
                r.width += maxAccValue;
                r.width += defaultTextIconGap;
            }
        }
    }

    private void caulateFont(JMenuItem b, Icon icon, String text, String acceleratorText) {
        Font font = b.getFont();
        FontMetrics fm = b.getFontMetrics(font);
        FontMetrics fmAccel = b.getFontMetrics(acceleratorFont);

        resetRects();

        layoutMenuItem(fm, text, fmAccel, acceleratorText, icon, checkIcon, arrowIcon,
                b.getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(),
                b.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect,
                checkIconRect, arrowIconRect, text == null ? 0 : defaultTextIconGap, DEFAULT_ICON_GAP);
        // find the union of the icon and text rects
        r.setBounds(textRect);
        r = SwingUtilities.computeUnion(iconRect.x, iconRect.y, iconRect.width, iconRect.height, r);

        // Added in 1.3.03 for compensation
        if (icon != null || checkIcon != null) {
            r.width -= 3 * UIBasicMenuItemUI.DEFAULT_TEXT_ICON_GAP;
        }
    }

    protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon,
                                 Color background, Color foreground, int defaultTextIconGap) {
        JMenuItem b = (JMenuItem) c;
        ButtonModel model = b.getModel();
        JComponent p = (JComponent) b.getParent();
        Integer maxValueInt = (Integer) p.getClientProperty(UIBasicMenuItemUI.MAX_ICON_WIDTH);
        int maxValue = maxValueInt == null ? 16 : maxValueInt.intValue();
        int menuWidth = b.getWidth();
        int menuHeight = b.getHeight();
        Insets i = c.getInsets();
        resetRects();
        viewRect.setBounds(0, 0, menuWidth, menuHeight);
        viewRect.x += 0;
        viewRect.y += i.top;
        viewRect.width -= (i.right + viewRect.x);
        viewRect.height -= (i.bottom + viewRect.y);
        Font holdf = g.getFont();
        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics(f);
        FontMetrics fmAccel = g.getFontMetrics(acceleratorFont);
        int offset = 0;
        // layout the text and icon
        Icon ic = b.getIcon();
        Icon iCheck = checkIcon;
        Icon paintIcon = ic;
        // get Accelerator text
        KeyStroke accelerator = b.getAccelerator();
        String acceleratorText = "";
        String text = layoutMenuItem(fm, b.getText(), fmAccel, acceleratorText, ic, null,
                arrowIcon, b.getVerticalAlignment(), b.getHorizontalAlignment(),
                b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect,
                iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect,
                b.getText() == null ? 0 : defaultTextIconGap, DEFAULT_ICON_GAP);
        // Paint background
        paintBackground(g, b, background);
        Color holdc = g.getColor();
        paintCheck(g, c, model, offset, foreground, holdc);
        paintIcon(b, g, c, paintIcon, model, offset);
        paintText(text, b, g, c);
        drawAccText(acceleratorText, g, p, model, fmAccel, c);
        paintArrow(g, model, c, foreground);
        g.setColor(holdc);
        g.setFont(holdf);
    }

    private void drawAccText(String acceleratorText, Graphics g, JComponent p, ButtonModel model, FontMetrics fmAccel, JComponent c) {
        if (acceleratorText != null && !ComparatorUtils.equals(acceleratorText, "")) {
            //Get the maxAccWidth from the parent to calculate the offset.
            int accOffset = 0;
            Container parent = menuItem.getParent();
            if (parent != null && parent instanceof JComponent) {
                Integer amaxValueInt = (Integer) p.getClientProperty(UIBasicMenuItemUI.MAX_ACC_WIDTH);
                int amaxValue = amaxValueInt != null ? amaxValueInt.intValue() : acceleratorRect.width;

                //Calculate the offset, with which the accelerator texts will be drawn with.
                accOffset = amaxValue - acceleratorRect.width;
            }

            g.setFont(acceleratorFont);
            if (!model.isEnabled()) {
                g.setColor(ThemeUtils.MENU_ICON_SHADOW_COLOR);
                BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset, acceleratorRect.y + fmAccel.getAscent());
                g.setColor(ThemeUtils.MENU_ICON_DISABLED_COLOR);
                BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset - 1, acceleratorRect.y + fmAccel.getAscent() - 1);
                //}
            } else {
                boolean isMenuSelected = c instanceof JMenu && model.isSelected();
                if (model.isArmed() || isMenuSelected) {
                    //g.setColor(acceleratorSelectionForeground);
                    g.setColor(ThemeUtils.MENU_SELECTED_TEXT_COLOR);
                } else {
                    g.setColor(ThemeUtils.MENU_ITEM_FONT_COLOR);
                }

                BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset, acceleratorRect.y + fmAccel.getAscent());
            }
        }
    }

    private void paintArrow(Graphics g, ButtonModel model, JComponent c, Color foreground) {
        if (arrowIcon != null) {
            boolean isMenuSelected = c instanceof JMenu && model.isSelected();
            if (model.isArmed() || isMenuSelected) {
                g.setColor(foreground);
            }
            if (!isTopLevelMenu()) {
                arrowIcon.paintIcon(c, g, arrowIconRect.x, arrowIconRect.y);
            }
        }
    }

    private void paintText(String text, JMenuItem b, Graphics g, JComponent c) {
        if (text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            g.setColor(ThemeUtils.MENU_ITEM_FONT_COLOR);

            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, b, textRect, text);
            }
        }
    }

    private void paintIcon(JMenuItem b, Graphics g, JComponent c, Icon paintIcon, ButtonModel model, int offset) {
        if (paintIcon != null) {
            Icon icon;
            if (!model.isEnabled()) {
                icon = (Icon) b.getDisabledIcon();
                if (icon != null) {
                    icon.paintIcon(c, g, iconRect.x + offset, iconRect.y);
                }
            } else if (model.isPressed() && model.isArmed()) {
                icon = (Icon) b.getPressedIcon();
                if (icon == null) {
                    // Use default icon
                    icon = (Icon) b.getIcon();
                }
                if (icon != null) {
                    icon.paintIcon(c, g, iconRect.x + offset, iconRect.y);
                }
            } else if (model.isArmed() || model.isSelected()) {
                icon = (Icon) b.getIcon();
                if (icon != null) {
                    icon.paintIcon(c, g, iconRect.x + offset, iconRect.y);
                }
            } else {
                icon = (Icon) b.getIcon();
                if (icon != null) {
                    icon.paintIcon(c, g, iconRect.x + offset, iconRect.y);
                }
            }

        }
    }

    private void paintCheck(Graphics g, JComponent c, ButtonModel model, int offset, Color foreground, Color holdc) {
        if (checkIcon != null) {
            boolean isMenuSelected = c instanceof JMenu && model.isSelected();
            if (model.isArmed() || isMenuSelected) {
                g.setColor(foreground);
            } else {
                g.setColor(holdc);
            }
            if (!isTopLevelMenu()) {
                checkIcon.paintIcon(c, g, iconRect.x + offset, iconRect.y);
            }
            g.setColor(holdc);
        }

    }

    /**
     * Draws the background of one menu item.
     *
     * @param g        the paint graphics
     * @param menuItem menu item to be painted
     * @param bgColor  unused
     * @since 1.4
     */
    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
        if (!menuItem.isOpaque()) {
            return;
        }
        ButtonModel model = menuItem.getModel();
        Color oldColor = g.getColor();
        int menuWidth = menuItem.getWidth();
        int menuHeight = menuItem.getHeight();
        boolean armed =
                (model.isArmed() || (menuItem instanceof JMenu && model.isSelected()));

        if ((menuItem instanceof JMenu) && ((JMenu) menuItem).isTopLevelMenu()) {
            // TopLevelMenu
            if (model.isSelected()) {
                g.setColor(ThemeUtils.MENU_BAR_COLOR);
                g.fillRect(0, 0, menuWidth, menuHeight);
                drawXpTopMenuBorder(g, 0, 0, menuWidth, menuHeight, true);
            } else if (menuItem.getClientProperty("rollover") == Boolean.TRUE && ThemeUtils.MENU_ROLLOVER) {
                g.setColor(ThemeUtils.MENU_ROLLOVER_BG_COLOR);
                g.fillRect(0, 0, menuWidth - 8, menuHeight);
                g.setColor(ThemeUtils.MENU_BAR_COLOR);
                g.fillRect(menuWidth - 8, 0, 8, menuHeight);
                drawXpTopMenuBorder(g, 0, 0, menuWidth, menuHeight, false);
            } else {
                if (menuItem.getBackground() instanceof ColorUIResource) {
                    g.setColor(ThemeUtils.MENU_BAR_COLOR);
                } else {
                    g.setColor(menuItem.getBackground());
                }

                g.fillRect(0, 0, menuWidth, menuHeight);
            }
        } else if (armed) {
            // Item rollover
            g.setColor(ThemeUtils.MENU_ITEM_ROLLOVER_COLOR);
            g.fillRect(0, 0, menuWidth, menuHeight);
        } else {
            // Item normal
            if (menuItem.getBackground() instanceof ColorUIResource) {
                g.setColor(ThemeUtils.MENU_POPUP_COLOR);
            } else {
                g.setColor(menuItem.getBackground());
            }

            g.fillRect(0, 0, menuWidth, menuHeight);
        }

        g.setColor(oldColor);
    }


    private void drawXpTopMenuBorder(
            Graphics g, int x, int y, int w, int h, boolean selected) {
        g.setColor(ThemeUtils.MENU_BORDER_COLOR);
        if (selected) {
            g.drawLine(x, y, x + w - 8, y);
            g.drawLine(x, y, x, y + h - 1);
            g.drawLine(x + w - 8, y, x + w - 8, y + h - 1);

            // shadow
            drawXpShadow(g,
                    ColorRoutines.darken(ThemeUtils.MENU_BORDER_COLOR, 15),
                    ThemeUtils.MENU_BAR_COLOR,
                    x + w - 7, y + 6, 6, h - 6);
        } else {
            g.drawRect(x, y, w - 8, h - 1);
        }
    }


    private void drawXpShadow(Graphics g, Color c1, Color c2,
                              int x1, int y1, int w, int h) {
        Color c;
        for (int x = 0; x < w; x++) {
            c = ColorRoutines.getGradient(c1, c2, w, x);
            g.setColor(c);
            g.drawLine(x1 + x, y1, x1 + x, y1 + h);

            int index = 0;
            for (int y = y1 - 1; y >= y1 - 6; y--) {
                g.setColor(ColorRoutines.getGradient(c, c2, 5, index++));
                g.drawLine(x1 + x, y, x1 + x, y);
            }
        }
    }

    /**
     * Compute and return the location of the icons origin, the
     * location of origin of the text baseline, and a possibly clipped
     * version of the compound labels string.  Locations are computed
     * relative to the viewRect rectangle.
     */

    private String layoutMenuItem(FontMetrics fm, String text, FontMetrics fmAccel,
                                  String acceleratorText, Icon icon, Icon checkIcon, Icon arrowIcon,
                                  int verticalAlignment, int horizontalAlignment, int verticalTextPosition,
                                  int horizontalTextPosition, Rectangle viewRect, Rectangle iconRect,
                                  Rectangle textRect, Rectangle acceleratorRect, Rectangle checkIconRect,
                                  Rectangle arrowIconRect, int textIconGap, int menuItemGap) {
        SwingUtilities.layoutCompoundLabel(menuItem, fm, text, icon, verticalAlignment,
                horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewRect,
                iconRect, textRect, textIconGap);
        if ((acceleratorText == null) ||ComparatorUtils.equals(acceleratorText,"")) {
            acceleratorRect.width = acceleratorRect.height = 0;
            acceleratorText = "";
        } else {
            acceleratorRect.width = SwingUtilities.computeStringWidth(fmAccel, acceleratorText);
            acceleratorRect.height = fmAccel.getHeight();
        }
        if (!isTopLevelMenu()) {
            if (checkIcon != null) {
                checkIconRect.height = checkIcon.getIconHeight();
                checkIconRect.width = checkIcon.getIconWidth();
            } else {
                checkIconRect.width = checkIconRect.height = 0;
            }
            if (arrowIcon != null) {
                arrowIconRect.width = arrowIcon.getIconWidth();
                arrowIconRect.height = arrowIcon.getIconHeight();
            } else {
                arrowIconRect.width = arrowIconRect.height = 0;
            }
        } else {
            checkIconRect.width = checkIconRect.height = 0;
            arrowIconRect.width = arrowIconRect.height = 0;
        }
        Rectangle labelRect = iconRect.union(textRect);
        if (checkIcon != null) {
            checkIconRect.x += menuItemGap;
        } else {
            textRect.x += menuItemGap;
            iconRect.x += menuItemGap;
        }
        acceleratorRect.x = viewRect.x + viewRect.width - arrowIconRect.width - menuItemGap - acceleratorRect.width;
        acceleratorRect.y = labelRect.y + (labelRect.height / 2) - (acceleratorRect.height / 2);
        if (!isTopLevelMenu()) {
            arrowIconRect.y = labelRect.y + (labelRect.height / 2) - (arrowIconRect.height / 2);
            checkIconRect.y = labelRect.y + (labelRect.height / 2) - (checkIconRect.height / 2);
            arrowIconRect.x = viewRect.x + viewRect.width - menuItemGap
                    - arrowIconRect.width;
        }
        return text;
    }

}