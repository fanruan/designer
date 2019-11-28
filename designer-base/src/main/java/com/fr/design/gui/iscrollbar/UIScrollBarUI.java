package com.fr.design.gui.iscrollbar;

import com.fr.design.constants.UIConstants;
import com.fr.stable.StringUtils;
import sun.swing.DefaultLookup;

import javax.swing.BoundedRangeModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.UIResource;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Like BasicScrollBarUI,but without two buttons
 *
 * @author zhou
 * @since 2012-5-18下午12:51:55
 */
public class UIScrollBarUI extends ScrollBarUI implements LayoutManager, SwingConstants {
    protected Dimension minimumThumbSize;
    protected Dimension maximumThumbSize;

    protected Color thumbHighlightColor;
    protected Color thumbLightShadowColor;
    protected Color thumbDarkShadowColor;
    protected Color thumbColor;
    protected Color trackColor;
    protected Color trackHighlightColor;

    protected JScrollBar scrollbar;
    // protected UIButton incrButton;
    // protected UIButton decrButton;
    protected boolean isDragging;
    protected boolean isPressing;
    protected TrackListener trackListener;
    protected ModelListener modelListener;

    protected Rectangle thumbRect;
    protected Rectangle trackRect;

    protected int trackHighlight;

    protected static final int NO_HIGHLIGHT = 0;
    protected static final int DECREASE_HIGHLIGHT = 1;
    protected static final int INCREASE_HIGHLIGHT = 2;

    protected ScrollListener scrollListener;
    protected PropertyChangeListener propertyChangeListener;
    protected Timer scrollTimer;

    private final static int SCROLL_SPEED_THROTTLE = 60; // delay in milli seconds

    /**
     * True indicates a middle click will absolutely position the scrollbar.
     */
    private boolean supportsAbsolutePositioning;

    /**
     * Hint as to what width (when vertical) or height (when horizontal) should
     * be.
     */
    private int scrollBarWidth;

    private Handler handler;

    private boolean thumbActive;

    /**
     * Determine whether scrollbar layout should use cached value or adjusted
     * value returned by scrollbar's <code>getValue</code>.
     */
    private boolean useCachedValue = false;
    /**
     * The scrollbar value is cached to save real value if the view is adjusted.
     */
    private int scrollBarValue;

    /**
     * Distance between the increment button and the track. This may be a
     * negative number. If negative, then an overlap between the button and
     * track will occur, which is useful for shaped buttons.
     * <p/>
     * TODO This should be made protected in a feature release
     */
    private int incrGap;

    /**
     * Distance between the decrement button and the track. This may be a
     * negative number. If negative, then an overlap between the button and
     * track will occur, which is useful for shaped buttons.
     * <p/>
     * TODO This should be made protected in a feature release
     */
    private int decrGap;

    /**
     * @param c
     * @return
     */
    public static ComponentUI createUI(JComponent c) {
        return new UIScrollBarUI();
    }

    protected void configureScrollBarColors() {
        LookAndFeel.installColors(scrollbar, "ScrollBar.background", "ScrollBar.foreground");
        thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
        thumbLightShadowColor = UIManager.getColor("ScrollBar.thumbShadow");
        thumbDarkShadowColor = UIManager.getColor("ScrollBar.thumbDarkShadow");
        thumbColor = UIManager.getColor("ScrollBar.thumb");
        trackColor = UIManager.getColor("ScrollBar.track");
        trackHighlightColor = UIManager.getColor("ScrollBar.trackHighlight");
    }

    /**
     * 设置UI
     *
     * @param c
     */
    @Override
    public void installUI(JComponent c) {
        scrollbar = (JScrollBar) c;
        thumbRect = new Rectangle(0, 0, 0, 0);
        trackRect = new Rectangle(0, 0, 0, 0);
        installDefaults();
        installComponents();
        installListeners();
        installKeyboardActions();
    }

    /**
     * 重新设置UI
     *
     * @param c
     */
    @Override
    public void uninstallUI(JComponent c) {
        scrollbar = (JScrollBar) c;
        uninstallListeners();
        uninstallDefaults();
        uninstallComponents();
        uninstallKeyboardActions();
        thumbRect = null;
        scrollbar = null;
    }

    protected void installDefaults() {
        scrollBarWidth = UIManager.getInt("ScrollBar.width");
        if (scrollBarWidth <= 0) {
            scrollBarWidth = 16;
        }
//		minimumThumbSize = (Dimension)UIManager.get("ScrollBar.minimumThumbSize");
        //滚动条的滑块的高度最小为30像素
        minimumThumbSize = new Dimension(10, 30);
        maximumThumbSize = (Dimension) UIManager.get("ScrollBar.maximumThumbSize");

        Boolean absB = (Boolean) UIManager.get("ScrollBar.allowsAbsolutePositioning");
        supportsAbsolutePositioning = (absB != null) ? absB.booleanValue() : false;

        trackHighlight = NO_HIGHLIGHT;
        if (scrollbar.getLayout() == null || (scrollbar.getLayout() instanceof UIResource)) {
            scrollbar.setLayout(this);
        }
        configureScrollBarColors();
        LookAndFeel.installBorder(scrollbar, "ScrollBar.border");
        LookAndFeel.installProperty(scrollbar, "opaque", Boolean.TRUE);

        scrollBarValue = scrollbar.getValue();

        incrGap = UIManager.getInt("ScrollBar.incrementButtonGap");
        decrGap = UIManager.getInt("ScrollBar.decrementButtonGap");

        // TODO this can be removed when incrGap/decrGap become protected
        // handle scaling for sizeVarients for special case components. The
        // key "JComponent.sizeVariant" scales for large/small/mini
        // components are based on Apples LAF
        String scaleKey = (String) scrollbar.getClientProperty("JComponent.sizeVariant");
        if (scaleKey != null) {
            if ("large".equals(scaleKey)) {
                scrollBarWidth *= 1.15;
                incrGap *= 1.15;
                decrGap *= 1.15;
            } else if ("small".equals(scaleKey)) {
                scrollBarWidth *= 0.857;
                incrGap *= 0.857;
                decrGap *= 0.714;
            } else if ("mini".equals(scaleKey)) {
                scrollBarWidth *= 0.714;
                incrGap *= 0.714;
                decrGap *= 0.714;
            }
        }
    }

    protected void installComponents() {
        scrollbar.setEnabled(scrollbar.isEnabled());
    }

    protected void uninstallComponents() {
    }

    protected void installListeners() {
        trackListener = createTrackListener();
        modelListener = createModelListener();
        propertyChangeListener = createPropertyChangeListener();

        scrollbar.addMouseListener(trackListener);
        scrollbar.addMouseMotionListener(trackListener);
        scrollbar.getModel().addChangeListener(modelListener);
        scrollbar.addPropertyChangeListener(propertyChangeListener);
        scrollbar.addFocusListener(getHandler());

        scrollListener = createScrollListener();
        scrollTimer = new Timer(SCROLL_SPEED_THROTTLE, scrollListener);
        scrollTimer.setInitialDelay(300); // default InitialDelay?
    }

    protected void installKeyboardActions() {

        InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
        SwingUtilities.replaceUIInputMap(scrollbar, JComponent.WHEN_FOCUSED, inputMap);
        inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        SwingUtilities.replaceUIInputMap(scrollbar, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIInputMap(scrollbar, JComponent.WHEN_FOCUSED, null);
        SwingUtilities.replaceUIActionMap(scrollbar, null);
    }

    private InputMap getInputMap(int condition) {
        if (condition == JComponent.WHEN_FOCUSED) {
            InputMap keyMap = (InputMap) DefaultLookup.get(scrollbar, this, "ScrollBar.focusInputMap");
            InputMap rtlKeyMap;

            if (scrollbar.getComponentOrientation().isLeftToRight() || ((rtlKeyMap = (InputMap) DefaultLookup.get(scrollbar, this, "ScrollBar.focusInputMap.RightToLeft")) == null)) {
                return keyMap;
            } else {
                rtlKeyMap.setParent(keyMap);
                return rtlKeyMap;
            }
        } else if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
            InputMap keyMap = (InputMap) DefaultLookup.get(scrollbar, this, "ScrollBar.ancestorInputMap");
            InputMap rtlKeyMap;

            if (scrollbar.getComponentOrientation().isLeftToRight()
                    || ((rtlKeyMap = (InputMap) DefaultLookup.get(scrollbar, this, "ScrollBar.ancestorInputMap.RightToLeft")) == null)) {
                return keyMap;
            } else {
                rtlKeyMap.setParent(keyMap);
                return rtlKeyMap;
            }
        }
        return null;
    }

    protected void uninstallListeners() {
        scrollTimer.stop();
        scrollTimer = null;

        scrollbar.getModel().removeChangeListener(modelListener);
        scrollbar.removeMouseListener(trackListener);
        scrollbar.removeMouseMotionListener(trackListener);
        scrollbar.removePropertyChangeListener(propertyChangeListener);
        scrollbar.removeFocusListener(getHandler());
        handler = null;
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(scrollbar);
        if (scrollbar.getLayout() == this) {
            scrollbar.setLayout(null);
        }
    }

    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    protected TrackListener createTrackListener() {
        return new TrackListener();
    }

    protected ModelListener createModelListener() {
        return new ModelListener();
    }

    protected ScrollListener createScrollListener() {
        return new ScrollListener();
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    private void updateThumbState(int x, int y) {
        Rectangle rect = getThumbBounds();

        setThumbRollover(rect.contains(x, y));
    }

    /**
     * Sets whether or not the mouse is currently over the thumb.
     *
     * @param active True indicates the thumb is currently active.
     * @since 1.5
     */
    protected void setThumbRollover(boolean active) {
        if (thumbActive != active) {
            thumbActive = active;
            scrollbar.repaint(getThumbBounds());
        }
    }

    /**
     * Returns true if the mouse is currently over the thumb.
     *
     * @return true if the thumb is currently active
     * @since 1.5
     */
    public boolean isThumbRollover() {
        return thumbActive;
    }

    /**
     * 只画Thumb
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        Rectangle thumbBounds = getThumbBounds();
        if (thumbBounds.intersects(g.getClipBounds())) {
            paintThumb(g, c, thumbBounds);
        }
    }

    /**
     * A vertical scrollbar's preferred width is the maximum of preferred widths
     * of the (non <code>null</code>) increment/decrement buttons, and the
     * minimum width of the thumb. The preferred height is the sum of the
     * preferred heights of the same parts. The basis for the preferred size of
     * a horizontal scrollbar is similar.
     * <p/>
     * The <code>preferredSize</code> is only computed once, subsequent calls to
     * this method just return a cached size.
     *
     * @param c the <code>JScrollBar</code> that's delegating this method to
     *          us
     * @return the preferred size of a Basic JScrollBar
     * @see #getMaximumSize
     * @see #getMinimumSize
     */
    @Override
    public Dimension getPreferredSize(JComponent c) {
        return (scrollbar.getOrientation() == JScrollBar.VERTICAL) ? new Dimension(scrollBarWidth, 48) : new Dimension(48, scrollBarWidth);
    }

    /**
     * @param c The JScrollBar that's delegating this method to us.
     * @return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
     * @see #getMinimumSize
     * @see #getPreferredSize
     */
    @Override
    public Dimension getMaximumSize(JComponent c) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    protected void paintDecreaseHighlight(Graphics g) {
        Insets insets = scrollbar.getInsets();
        Rectangle thumbR = getThumbBounds();
        g.setColor(trackHighlightColor);

        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            // paint the distance between the start of the track and top of the
            // thumb
            int x = insets.left;
            int y = trackRect.y;
            int w = scrollbar.getWidth() - (insets.left + insets.right);
            int h = thumbR.y - y;
            g.fillRect(x, y, w, h);
        } else {
            // if left-to-right, fill the area between the start of the track
            // and
            // the left edge of the thumb. If right-to-left, fill the area
            // between
            // the end of the thumb and end of the track.
            int x, w;
            if (scrollbar.getComponentOrientation().isLeftToRight()) {
                x = trackRect.x;
                w = thumbR.x - x;
            } else {
                x = thumbR.x + thumbR.width;
                w = trackRect.x + trackRect.width - x;
            }
            int y = insets.top;
            int h = scrollbar.getHeight() - (insets.top + insets.bottom);
            g.fillRect(x, y, w, h);
        }
    }

    protected void paintIncreaseHighlight(Graphics g) {
        Insets insets = scrollbar.getInsets();
        Rectangle thumbR = getThumbBounds();
        g.setColor(trackHighlightColor);

        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            // fill the area between the bottom of the thumb and the end of the
            // track.
            int x = insets.left;
            int y = thumbR.y + thumbR.height;
            int w = scrollbar.getWidth() - (insets.left + insets.right);
            int h = trackRect.y + trackRect.height - y;
            g.fillRect(x, y, w, h);
        } else {
            // if left-to-right, fill the area between the right of the thumb
            // and the
            // end of the track. If right-to-left, then fill the area to the
            // left of
            // the thumb and the start of the track.
            int x, w;
            if (scrollbar.getComponentOrientation().isLeftToRight()) {
                x = thumbR.x + thumbR.width;
                w = trackRect.x + trackRect.width - x;
            } else {
                x = trackRect.x;
                w = thumbR.x - x;
            }
            int y = insets.top;
            int h = scrollbar.getHeight() - (insets.top + insets.bottom);
            g.fillRect(x, y, w, h);
        }
    }

    protected void paintThumb(Graphics g, JComponent cc, Rectangle thumbBounds) {
        int width = thumbBounds.width;
        int height = thumbBounds.height;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.translate(thumbBounds.x, thumbBounds.y);
        Color color = isPressing ? UIConstants.LINE_COLOR : UIConstants.BARNOMAL;
        g2.setColor(color);
        g2.fillRoundRect(1, 1, width - 2, height - 2, UIConstants.LARGEARC, UIConstants.LARGEARC);

    }


    /**
     * Return the smallest acceptable size for the thumb. If the scrollbar
     * becomes so small that this size isn't available, the thumb will be
     * hidden.
     * <p/>
     * <b>Warning </b>: the value returned by this method should not be be
     * modified, it's a shared static constant.
     *
     * @return The smallest acceptable size for the thumb.
     * @see #getMaximumThumbSize
     */
    protected Dimension getMinimumThumbSize() {
        return minimumThumbSize;
    }

    /**
     * Return the largest acceptable size for the thumb. To create a fixed size
     * thumb one make this method and <code>getMinimumThumbSize</code> return
     * the same value.
     * <p/>
     * <b>Warning </b>: the value returned by this method should not be be
     * modified, it's a shared static constant.
     *
     * @return The largest acceptable size for the thumb.
     * @see #getMinimumThumbSize
     */
    protected Dimension getMaximumThumbSize() {
        return maximumThumbSize;
    }

	/*
     * LayoutManager Implementation
	 */


    /**
     * @param name
     * @param child
     */
    public void addLayoutComponent(String name, Component child) {
    }

    /**
     * @param child
     */
    public void removeLayoutComponent(Component child) {
    }

    /**
     * 得到最好的布局大小
     *
     * @param scrollbarContainer
     * @return
     */
    public Dimension preferredLayoutSize(Container scrollbarContainer) {
        return getPreferredSize((JComponent) scrollbarContainer);
    }

    /**
     * 最小的布局大小
     *
     * @param scrollbarContainer
     * @return
     */
    public Dimension minimumLayoutSize(Container scrollbarContainer) {
        return getMinimumSize((JComponent) scrollbarContainer);
    }

    private int getValue(JScrollBar sb) {
        return (useCachedValue) ? scrollBarValue : sb.getValue();
    }





		/*
         * Width and left edge of the buttons and thumb.
		 */

      /*
               * Nominal locations of the buttons, assuming their preferred size will
      		 * fit.
      		 */

    /*
             * The thumb must fit within the height left over after we subtract the
    		 * preferredSize of the buttons and the insets and the gaps
    		 */

      /*
               * Compute the height and origin of the thumb. The case where the thumb
      		 * is at the bottom edge is handled specially to avoid numerical
      		 * problems in computing thumbY. Enforce the thumbs min/max dimensions.
      		 * If the thumb doesn't fit in the track (trackH) we'll hide it later.
      		 */

    /*
             * If the buttons don't fit, allocate half of the available space to
    		 * each and move the lower one (incrButton) down.
    		 */

    /*
             * Update the trackRect field.
    		 */

      /*
      		 * If the thumb isn't going to fit, zero it's bounds. Otherwise make
      		 * sure it fits between the buttons. Note that setting the thumbs bounds
      		 * will cause a repaint.
      		 */

    @SuppressWarnings("squid:S2164")
    protected void layoutVScrollbar(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;
        boolean squareButtons = DefaultLookup.getBoolean(scrollbar, this, "ScrollBar.squareButtons", false);
        int decrButtonH = squareButtons ? itemW : 0;
        int decrButtonY = sbInsets.top;
        int incrButtonH = squareButtons ? itemW : 0;
        int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButtonH + incrButtonH;
        int gaps = decrGap + incrGap;
        float trackH = sbSize.height - (sbInsetsH + sbButtonsH) - gaps;
        float min = sb.getMinimum();
        float extent = sb.getVisibleAmount();
        float range = sb.getMaximum() - min;
        float value = getValue(sb);
        int thumbH = (range <= 0) ? getMaximumThumbSize().height : (int) (trackH * (extent / range));
        thumbH = Math.max(thumbH, getMinimumThumbSize().height);
        thumbH = Math.min(thumbH, getMaximumThumbSize().height);
        int thumbY = incrButtonY - incrGap - thumbH;
        if (value < (sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - thumbH;
            thumbY = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
            thumbY += decrButtonY + decrButtonH + decrGap;
        }
        int sbAvailButtonH = (sbSize.height - sbInsetsH);
        if (sbAvailButtonH < sbButtonsH) {
            incrButtonH = decrButtonH = sbAvailButtonH / 2;
            incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        }
        int itrackY = decrButtonY + decrButtonH + decrGap;
        int itrackH = incrButtonY - incrGap - itrackY;
        trackRect.setBounds(itemX, itrackY, itemW, itrackH);
        if (thumbH >= (int) trackH) {
            setThumbBounds(0, 0, 0, 0);
        } else {
            if ((thumbY + thumbH) > incrButtonY - incrGap) {
                thumbY = incrButtonY - incrGap - thumbH;
            }
            if (thumbY < (decrButtonY + decrButtonH + decrGap)) {
                thumbY = decrButtonY + decrButtonH + decrGap + 1;
            }
            setThumbBounds(itemX, thumbY, itemW, thumbH);
        }
    }



    /*
    		 * Height and top edge of the buttons and thumb.
    		 */

    /*
    		 * Nominal locations of the buttons, assuming their preferred size will
    		 * fit.
    		 */

    /*
    		 * The thumb must fit within the width left over after we subtract the
    		 * preferredSize of the buttons and the insets and the gaps
    		 */

    /*
    		 * Compute the width and origin of the thumb. Enforce the thumbs min/max
    		 * dimensions. The case where the thumb is at the right edge is handled
    		 * specially to avoid numerical problems in computing thumbX. If the
    		 * thumb doesn't fit in the track (trackH) we'll hide it later.
    		 */
           /*
           		 * If the buttons don't fit, allocate half of the available space to
           		 * each and move the right one over.
           		 */

    /*
    		 * Update the trackRect field.
    		 */
    /*
    		 * Make sure the thumb fits between the buttons. Note that setting the
    		 * thumbs bounds causes a repaint.
    		 */
    @SuppressWarnings("squid:S2164")
    protected void layoutHScrollbar(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;
        boolean ltr = sb.getComponentOrientation().isLeftToRight();
        boolean squareButtons = DefaultLookup.getBoolean(scrollbar, this, "ScrollBar.squareButtons", false);
        int leftButtonW = squareButtons ? itemH : 0;
        int rightButtonW = squareButtons ? itemH : 0;
        if (!ltr) {
            int temp = leftButtonW;
            leftButtonW = rightButtonW;
            rightButtonW = temp;
        }
        int leftButtonX = sbInsets.left;
        int rightButtonX = sbSize.width - (sbInsets.right + rightButtonW);
        int leftGap = ltr ? decrGap : incrGap;
        int rightGap = ltr ? incrGap : decrGap;
        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = leftButtonW + rightButtonW;
        float trackW = sbSize.width - (sbInsetsW + sbButtonsW) - (leftGap + rightGap);
        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = getValue(sb);
        int thumbW = (range <= 0) ? getMaximumThumbSize().width : (int) (trackW * (extent / range));
        thumbW = Math.max(thumbW, getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, getMaximumThumbSize().width);
        int thumbX = ltr ? rightButtonX - rightGap - thumbW : leftButtonX + leftButtonW + leftGap;
        if (value < (max - sb.getVisibleAmount())) {
            float thumbRange = trackW - thumbW;
            if (ltr) {
                thumbX = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
            } else {
                thumbX = (int) (0.5f + (thumbRange * ((max - extent - value) / (range - extent))));
            }
            thumbX += leftButtonX + leftButtonW + leftGap;
        }
        int sbAvailButtonW = (sbSize.width - sbInsetsW);
        if (sbAvailButtonW < sbButtonsW) {
            rightButtonW = leftButtonW = sbAvailButtonW / 2;
            rightButtonX = sbSize.width - (sbInsets.right + rightButtonW + rightGap);
        }
        int itrackX = leftButtonX + leftButtonW + leftGap;
        int itrackW = rightButtonX - rightGap - itrackX;
        trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        set(thumbW, thumbX, trackW, rightButtonX, rightGap, leftButtonX, leftButtonW, leftGap, itemY, itemH);
    }

    private void set(int thumbW, int thumbX, float trackW, int rightButtonX, int rightGap, int leftButtonX, int leftButtonW, int leftGap, int itemY, int itemH) {
        if (thumbW >= (int) trackW) {
            setThumbBounds(0, 0, 0, 0);
        } else {
            if (thumbX + thumbW > rightButtonX - rightGap) {
                thumbX = rightButtonX - rightGap - thumbW;
            }
            if (thumbX < leftButtonX + leftButtonW + leftGap) {
                thumbX = leftButtonX + leftButtonW + leftGap + 1;
            }
            setThumbBounds(thumbX, itemY, thumbW, itemH);
        }
    }

    /**
     * @param scrollbarContainer
     */
    public void layoutContainer(Container scrollbarContainer) {
		/*
		 * If the user is dragging the value, we'll assume that the scrollbars
		 * layout is OK modulo the thumb which is being handled by the dragging
		 * code.
		 */
        if (isDragging) {
            return;
        }

        JScrollBar scrollbar = (JScrollBar) scrollbarContainer;
        switch (scrollbar.getOrientation()) {
            case JScrollBar.VERTICAL:
                layoutVScrollbar(scrollbar);
                break;

            case JScrollBar.HORIZONTAL:
                layoutHScrollbar(scrollbar);
                break;
        }
    }

    /**
     * Set the bounds of the thumb and force a repaint that includes the old
     * thumbBounds and the new one.
     *
     * @see #getThumbBounds
     */
    protected void setThumbBounds(int x, int y, int width, int height) {
		/*
		 * If the thumbs bounds haven't changed, we're done.
		 */
        boolean isX = thumbRect.x == x;
        boolean isWidth = thumbRect.width == width;
        boolean isY = thumbRect.y == y;
        boolean isHeight = thumbRect.height == height;
        boolean istrue1 = isX && isWidth;
        boolean istrue2 = isY && isHeight;
        if (istrue1 && istrue2) {
            return;
        }

		/*
		 * Update thumbRect, and repaint the union of x,y,w,h and the old
		 * thumbRect.
		 */
        int minX = Math.min(x, thumbRect.x);
        int minY = Math.min(y, thumbRect.y);
        int maxX = Math.max(x + width, thumbRect.x + thumbRect.width);
        int maxY = Math.max(y + height, thumbRect.y + thumbRect.height);

        thumbRect.setBounds(x, y, width, height);
        scrollbar.repaint(minX, minY, maxX - minX, maxY - minY);

        // Once there is API to determine the mouse location this will need
        // to be changed.
        setThumbRollover(false);
    }

    /**
     * Return the current size/location of the thumb.
     * <p/>
     * <b>Warning </b>: the value returned by this method should not be be
     * modified, it's a reference to the actual rectangle, not a copy.
     *
     * @return The current size/location of the thumb.
     * @see #setThumbBounds
     */
    protected Rectangle getThumbBounds() {
        return thumbRect;
    }

    /**
     * Returns the current bounds of the track, i.e. the space in between the
     * increment and decrement buttons, less the insets. The value returned by
     * this method is updated each time the scrollbar is laid out (validated).
     * <p/>
     * <b>Warning </b>: the value returned by this method should not be be
     * modified, it's a reference to the actual rectangle, not a copy.
     *
     * @return the current bounds of the scrollbar track
     * @see #layoutContainer
     */
    protected Rectangle getTrackBounds() {
        return trackRect;
    }

    /*
     * Method for scrolling by a block increment. Added for mouse wheel
     * scrolling support, RFE 4202656.
     */
    static void scrollByBlock(JScrollBar scrollbar, int direction) {
        // This method is called from BasicScrollPaneUI to implement wheel
        // scrolling, and also from scrollByBlock().
        int oldValue = scrollbar.getValue();
        int blockIncrement = scrollbar.getBlockIncrement(direction);
        int delta = blockIncrement * ((direction > 0) ? +1 : -1);
        int newValue = oldValue + delta;

        // Check for overflow.
        if (delta > 0 && newValue < oldValue) {
            newValue = scrollbar.getMaximum();
        } else if (delta < 0 && newValue > oldValue) {
            newValue = scrollbar.getMinimum();
        }

        scrollbar.setValue(newValue);
    }

    protected void scrollByBlock(int direction) {
        scrollByBlock(scrollbar, direction);
        trackHighlight = direction > 0 ? INCREASE_HIGHLIGHT : DECREASE_HIGHLIGHT;
        Rectangle dirtyRect = getTrackBounds();
        scrollbar.repaint(dirtyRect.x, dirtyRect.y, dirtyRect.width, dirtyRect.height);
    }

    /*
     * Method for scrolling by a unit increment. Added for mouse wheel scrolling
     * support, RFE 4202656.
     *
     * If limitByBlock is set to true, the scrollbar will scroll at least 1 unit
     * increment, but will not scroll farther than the block increment. See
     * BasicScrollPaneUI.Handler.mouseWheelMoved().
     */
    static void scrollByUnits(JScrollBar scrollbar, int direction, int units, boolean limitToBlock) {
        // This method is called from BasicScrollPaneUI to implement wheel
        // scrolling, as well as from scrollByUnit().
        int delta;
        int limit = -1;
        if (limitToBlock) {
            if (direction < 0) {
                limit = scrollbar.getValue() - scrollbar.getBlockIncrement(direction);
            } else {
                limit = scrollbar.getValue() + scrollbar.getBlockIncrement(direction);
            }
        }
        for (int i = 0; i < units; i++) {
            if (direction > 0) {
                delta = scrollbar.getUnitIncrement(direction);
            } else {
                delta = -scrollbar.getUnitIncrement(direction);
            }
            int oldValue = scrollbar.getValue();
            int newValue = oldValue + delta;
            // Check for overflow.
            if (delta > 0 && newValue < oldValue) {
                newValue = scrollbar.getMaximum();
            } else if (delta < 0 && newValue > oldValue) {
                newValue = scrollbar.getMinimum();
            }
            if (oldValue == newValue) {
                break;
            }
            if (limitToBlock && i > 0) {
                assert limit != -1;
                boolean islow = direction < 0 && newValue < limit;
                boolean isHigh = direction > 0 && newValue > limit;
                if (islow && isHigh) {
                    break;
                }
            }
            scrollbar.setValue(newValue);
        }
    }

    protected void scrollByUnit(int direction) {
        scrollByUnits(scrollbar, direction, 1, false);
    }

    /**
     * Indicates whether the user can absolutely position the thumb with a mouse
     * gesture (usually the middle mouse button).
     *
     * @return true if a mouse gesture can absolutely position the thumb
     * @since 1.5
     */
    public boolean isSupportsAbsolutePositioning() {
        return supportsAbsolutePositioning;
    }

    /**
     * A listener to listen for model changes.
     */
    protected class ModelListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            if (!useCachedValue) {
                scrollBarValue = scrollbar.getValue();
            }
            layoutContainer(scrollbar);
            useCachedValue = false;
        }
    }

    /**
     * Track mouse drags.
     */
    protected class TrackListener extends MouseAdapter implements MouseMotionListener {
        protected transient int offset;
        protected transient int currentMouseX, currentMouseY;
        private transient int direction = +1;


        @Override
        public void mouseReleased(MouseEvent e) {
            isPressing = false;
            if (isDragging) {
                updateThumbState(e.getX(), e.getY());
            }
            boolean isMiddle = !isSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e);
            if (SwingUtilities.isRightMouseButton(e) || isMiddle) {
                return;
            }
            if (!scrollbar.isEnabled()) {
                return;
            }
            Rectangle r = getTrackBounds();
            scrollbar.repaint(r.x, r.y, r.width, r.height);

            trackHighlight = NO_HIGHLIGHT;
            isDragging = false;
            offset = 0;
            scrollTimer.stop();
            useCachedValue = true;
            scrollbar.setValueIsAdjusting(false);
            scrollbar.repaint();

        }

        /**
         * If the mouse is pressed above the "thumb" component then reduce the
         * scrollbars value by one page ("page up"), otherwise increase it by
         * one page. If there is no thumb then page up if the mouse is in the
         * upper half of the track.
         */
        @Override
        public void mousePressed(MouseEvent e) {
            boolean isMiddle = !isSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e);
            if (SwingUtilities.isRightMouseButton(e) || isMiddle) {
                return;
            }

            if (!scrollbar.isEnabled()) {
                return;
            }

            isPressing = true;
            if (!scrollbar.hasFocus() && scrollbar.isRequestFocusEnabled()) {
                scrollbar.requestFocus();
            }
            useCachedValue = true;
            scrollbar.setValueIsAdjusting(true);
            currentMouseX = e.getX();
            currentMouseY = e.getY();
            // Clicked in the Thumb area?
            if (getThumbBounds().contains(currentMouseX, currentMouseY)) {
                switch (scrollbar.getOrientation()) {
                    case JScrollBar.VERTICAL:
                        offset = currentMouseY - getThumbBounds().y;
                        break;
                    case JScrollBar.HORIZONTAL:
                        offset = currentMouseX - getThumbBounds().x;
                        break;
                }
                isDragging = true;
                scrollbar.repaint();
                return;
            } else if (isSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e)) {
                switch (scrollbar.getOrientation()) {
                    case JScrollBar.VERTICAL:
                        offset = getThumbBounds().height / 2;
                        break;
                    case JScrollBar.HORIZONTAL:
                        offset = getThumbBounds().width / 2;
                        break;
                }
                isDragging = true;
                setValueFrom(e);
                scrollbar.repaint();
                return;
            }
            isDragging = false;
            sCrollBar();
        }

        private void sCrollBar() {
            Dimension sbSize = scrollbar.getSize();
            direction = +1;
            switch (scrollbar.getOrientation()) {
                case JScrollBar.VERTICAL:
                    if (getThumbBounds().isEmpty()) {
                        int scrollbarCenter = sbSize.height / 2;
                        direction = (currentMouseY < scrollbarCenter) ? -1 : +1;
                    } else {
                        int thumbY = getThumbBounds().y;
                        direction = (currentMouseY < thumbY) ? -1 : +1;
                    }
                    break;
                case JScrollBar.HORIZONTAL:
                    if (getThumbBounds().isEmpty()) {
                        int scrollbarCenter = sbSize.width / 2;
                        direction = (currentMouseX < scrollbarCenter) ? -1 : +1;
                    } else {
                        int thumbX = getThumbBounds().x;
                        direction = (currentMouseX < thumbX) ? -1 : +1;
                    }
                    if (!scrollbar.getComponentOrientation().isLeftToRight()) {
                        direction = -direction;
                    }
                    break;
            }
            scrollByBlock(direction);
            scrollTimer.stop();
            scrollListener.setDirection(direction);
            scrollListener.setScrollByBlock(true);
            startScrollTimerIfNecessary();
            scrollbar.repaint();
        }


        public void mouseDragged(MouseEvent e) {
            if (getThumbBounds().contains(e.getPoint())) {
                isDragging = true;
            }
            boolean isMiddle = !isSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e);
            if (SwingUtilities.isRightMouseButton(e) || isMiddle) {
                return;
            }
            if (!scrollbar.isEnabled() || getThumbBounds().isEmpty()) {
                return;
            }
            if (isDragging) {
                setValueFrom(e);
            } else {
                currentMouseX = e.getX();
                currentMouseY = e.getY();
                updateThumbState(currentMouseX, currentMouseY);
                startScrollTimerIfNecessary();
            }

            scrollbar.repaint();
        }
        @SuppressWarnings("squid:S2164")
        private void setValueFrom(MouseEvent e) {
            boolean active = isThumbRollover();
            BoundedRangeModel model = scrollbar.getModel();
            Rectangle thumbR = getThumbBounds();
            int thumbMin, thumbMax, thumbPos;

            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                thumbMin = trackRect.y;
                thumbMax = trackRect.y + trackRect.height - thumbR.height;
                thumbPos = Math.min(thumbMax, Math.max(thumbMin, (e.getY() - offset)));
                setThumbBounds(thumbR.x, thumbPos, thumbR.width, thumbR.height);
            } else {
                thumbMin = trackRect.x;
                thumbMax = trackRect.x + trackRect.width - thumbR.width;
                thumbPos = Math.min(thumbMax, Math.max(thumbMin, (e.getX() - offset)));
                setThumbBounds(thumbPos, thumbR.y, thumbR.width, thumbR.height);
            }

			/*
			 * Set the scrollbars value. If the thumb has reached the end of the
			 * scrollbar, then just set the value to its maximum. Otherwise
			 * compute the value as accurately as possible.
			 */
            if (thumbPos == thumbMax) {
                if (scrollbar.getOrientation() == JScrollBar.VERTICAL || scrollbar.getComponentOrientation().isLeftToRight()) {
                    scrollbar.setValue(model.getMaximum() - model.getExtent());
                } else {
                    scrollbar.setValue(model.getMinimum());
                }
            } else {
                float valueMax = model.getMaximum() - model.getExtent();
                float valueRange = valueMax - model.getMinimum();
                float thumbValue = thumbPos - thumbMin;
                float thumbRange = thumbMax - thumbMin;
                int value;
                if (scrollbar.getOrientation() == JScrollBar.VERTICAL || scrollbar.getComponentOrientation().isLeftToRight()) {
                    value = (int) (0.5 + ((thumbValue / thumbRange) * valueRange));
                } else {
                    value = (int) (0.5 + (((thumbMax - thumbPos) / thumbRange) * valueRange));
                }

                useCachedValue = true;
                scrollBarValue = value + model.getMinimum();
                scrollbar.setValue(adjustValueIfNecessary(scrollBarValue));
            }
            setThumbRollover(active);
        }

        private int adjustValueIfNecessary(int value) {
            if (scrollbar.getParent() instanceof JScrollPane) {
                JScrollPane scrollpane = (JScrollPane) scrollbar.getParent();
                JViewport viewport = scrollpane.getViewport();
                Component view = viewport.getView();
                if (view instanceof JList) {
                    JList list = (JList) view;
                    if (DefaultLookup.getBoolean(list, list.getUI(), "List.lockToPositionOnScroll", false)) {
                        int adjustedValue = value;
                        int mode = list.getLayoutOrientation();
                        int orientation = scrollbar.getOrientation();
                        if (orientation == JScrollBar.VERTICAL && mode == JList.VERTICAL) {
                            int index = list.locationToIndex(new Point(0, value));
                            Rectangle rect = list.getCellBounds(index, index);
                            if (rect != null) {
                                adjustedValue = rect.y;
                            }
                        }
                        if (orientation == JScrollBar.HORIZONTAL && (mode == JList.VERTICAL_WRAP || mode == JList.HORIZONTAL_WRAP)) {
                            if (scrollpane.getComponentOrientation().isLeftToRight()) {
                                int index = list.locationToIndex(new Point(value, 0));
                                Rectangle rect = list.getCellBounds(index, index);
                                if (rect != null) {
                                    adjustedValue = rect.x;
                                }
                            } else {
                                Point loc = new Point(value, 0);
                                int extent = viewport.getExtentSize().width;
                                loc.x += extent - 1;
                                int index = list.locationToIndex(loc);
                                Rectangle rect = list.getCellBounds(index, index);
                                if (rect != null) {
                                    adjustedValue = rect.x + rect.width - extent;
                                }
                            }
                        }
                        value = adjustedValue;

                    }
                }
            }
            return value;
        }

        private void startScrollTimerIfNecessary() {
            if (scrollTimer.isRunning()) {
                return;
            }

            Rectangle tb = getThumbBounds();

            switch (scrollbar.getOrientation()) {
                case JScrollBar.VERTICAL:
                    if (direction > 0) {
                        if (tb.y + tb.height < trackListener.currentMouseY) {
                            scrollTimer.start();
                        }
                    } else if (tb.y > trackListener.currentMouseY) {
                        scrollTimer.start();
                    }
                    break;
                case JScrollBar.HORIZONTAL:
                    boolean isAfterThumb = direction > 0 && isMouseAfterThumb();
                    boolean isBeforeThumb = direction < 0 && isMouseBeforeThumb();
                    if (isAfterThumb || isBeforeThumb) {
                        scrollTimer.start();
                    }
                    break;
            }
        }

    }

    /**
     * Listener for scrolling events initiated in the <code>ScrollPane</code>.
     */
    protected class ScrollListener implements ActionListener {
        int direction = +1;
        boolean useBlockIncrement;

        public ScrollListener() {
            direction = +1;
            useBlockIncrement = false;
        }

        public ScrollListener(int dir, boolean block) {
            direction = dir;
            useBlockIncrement = block;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public void setScrollByBlock(boolean block) {
            this.useBlockIncrement = block;
        }

        public void actionPerformed(ActionEvent e) {
            if (useBlockIncrement) {
                scrollByBlock(direction);
                // Stop scrolling if the thumb catches up with the mouse
                if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                    if (direction > 0) {
                        if (getThumbBounds().y + getThumbBounds().height >= trackListener.currentMouseY) {
                            ((Timer) e.getSource()).stop();
                        }
                    } else if (getThumbBounds().y <= trackListener.currentMouseY) {
                        ((Timer) e.getSource()).stop();
                    }
                } else {
                    boolean isAfterThumb = direction > 0 && !isMouseAfterThumb();
                    boolean isBeforeThumb = direction < 0 && !isMouseBeforeThumb();
                    if (isAfterThumb || isBeforeThumb) {
                        ((Timer) e.getSource()).stop();
                    }
                }
            } else {
                scrollByUnit(direction);
            }
            if (direction > 0 && scrollbar.getValue() + scrollbar.getVisibleAmount() >= scrollbar.getMaximum()) {
                ((Timer) e.getSource()).stop();
            } else if (direction < 0 && scrollbar.getValue() <= scrollbar.getMinimum()) {
                ((Timer) e.getSource()).stop();
            }
        }
    }

    private boolean isMouseLeftOfThumb() {
        return trackListener.currentMouseX < getThumbBounds().x;
    }

    private boolean isMouseRightOfThumb() {
        Rectangle tb = getThumbBounds();
        return trackListener.currentMouseX > tb.x + tb.width;
    }

    private boolean isMouseBeforeThumb() {
        return scrollbar.getComponentOrientation().isLeftToRight() ? isMouseLeftOfThumb() : isMouseRightOfThumb();
    }

    private boolean isMouseAfterThumb() {
        return scrollbar.getComponentOrientation().isLeftToRight() ? isMouseRightOfThumb() : isMouseLeftOfThumb();
    }

    private void updateButtonDirections() {
    }

    /**
     * PropertyChangeHandler
     */
    public class PropertyChangeHandler implements PropertyChangeListener {
        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.

        /**
         * @param e
         */
        public void propertyChange(PropertyChangeEvent e) {
            getHandler().propertyChange(e);
        }
    }

    //
    // EventHandler
    //
    private class Handler implements FocusListener, PropertyChangeListener {
        //
        // FocusListener
        //
        public void focusGained(FocusEvent e) {
            scrollbar.repaint();
        }

        public void focusLost(FocusEvent e) {
            scrollbar.repaint();
        }

        //
        // PropertyChangeAdapter
        //

        /**
         * @param e
         */
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();

            if (StringUtils.equals("model", propertyName)) {
                BoundedRangeModel oldModel = (BoundedRangeModel) e.getOldValue();
                BoundedRangeModel newModel = (BoundedRangeModel) e.getNewValue();
                oldModel.removeChangeListener(modelListener);
                newModel.addChangeListener(modelListener);
                scrollbar.repaint();
                scrollbar.revalidate();
            } else if (StringUtils.equals("orientation", propertyName)) {
                updateButtonDirections();
            } else if (StringUtils.equals("componentOrientation", propertyName)) {
                updateButtonDirections();
                InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
                SwingUtilities.replaceUIInputMap(scrollbar, JComponent.WHEN_FOCUSED, inputMap);
            }
        }
    }
}
