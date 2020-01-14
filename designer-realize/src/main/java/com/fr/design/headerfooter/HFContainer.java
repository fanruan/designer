/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.headerfooter;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.headerfooter.HFElement;
import com.fr.base.headerfooter.NewLineHFElement;
import com.fr.design.dialog.FineJOptionPane;


/**
 * The container of HFComponent.
 */
public class HFContainer extends JPanel implements Scrollable {
    private List hfComponentList = new ArrayList();

    private static final int HOR_GAP = 1;
    private static final int VER_GAP = 2;

    private ChangeListener contentChangeListener = null;

    /**
     * Constructor.
     */
    public HFContainer() {
        this.setEnabled(true);
    }

    @Override
	public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            this.setBackground(UIManager.getColor("TextArea.background"));
        } else {
            this.setBackground(SystemColor.control);
        }
    }

    /**
     * Refresh layout.
     */
    private void refreshLayout() {
        this.removeAll();

        this.setLayout(null);

        int componentX = 2;
        int componentY = VER_GAP;

        for (int i = 0; i < hfComponentList.size(); i++) {
            HFComponent tmpHFComponent = (HFComponent) hfComponentList.get(i);

            this.add(tmpHFComponent);
            tmpHFComponent.setLocation(componentX, componentY);
            tmpHFComponent.setSize(tmpHFComponent.getPreferredSize().width,
                    tmpHFComponent.getPreferredSize().height);

            if (tmpHFComponent.getHFElement().getClass().equals(NewLineHFElement.class)) {
                componentX = 2;
                componentY += VER_GAP + tmpHFComponent.getPreferredSize().height;
            } else {

                componentX += HOR_GAP + tmpHFComponent.getWidth();
            }
        }

        this.doLayout();
        this.revalidate();
        this.repaint();

        this.contentChanged();
    }

    public ChangeListener getContentChangeListener() {
        return contentChangeListener;
    }

    public void setContentChangeListener(ChangeListener contentChangeListener) {
        this.contentChangeListener = contentChangeListener;
    }

    /**
     * DataChanged.
     */
    private void contentChanged() {
        if (this.contentChangeListener != null) {
            ChangeEvent changeEvent = new ChangeEvent(HFContainer.this);
            contentChangeListener.stateChanged(changeEvent);
        }
    }

    /**
     * Populate
     */
    public void populate(List hfElementList) {
        this.hfComponentList.clear();

        for (int i = 0; i < hfElementList.size(); i++) {
            addHFComponent(new HFComponent((HFElement) hfElementList.get(i)));
        }

        //refresh layout.
        this.refreshLayout();
    }

    /**
     * update
     */
    public List update() {
        List hfElementList = new ArrayList();

        for (int i = 0; i < hfComponentList.size(); i++) {
            hfElementList.add(((HFComponent) hfComponentList.get(i)).getHFElement());
        }

        return hfElementList;
    }

    /**
     * Add hfComponent.
     *
     * @param hfComponent the added object of HFComponent
     */
    public void addHFComponent(HFComponent hfComponent) {
        this.addHFComponent(-1, hfComponent);
    }

    /**
     * Add hfComponent at give position.
     *
     * @param index       the given position. (-1 mean that add hfcomponent to the end).
     * @param hfComponent the added object of HFComponent
     */
    public void addHFComponent(int index, HFComponent hfComponent) {
        if (index <= -1 || index > hfComponentList.size()) {
            this.hfComponentList.add(hfComponent);
        } else {
            this.hfComponentList.add(index, hfComponent);
        }

        //set move and delete actionListener
        hfComponent.setMoveLeftActionListener(this.moveLeftActionListener);
        hfComponent.setMoveRightActionListener(this.moveRightActionListener);
        hfComponent.setDeleteActionListener(this.deleteActionListener);

        hfComponent.setContentChangeListener(this.getContentChangeListener());

        //refresh layout.
        this.refreshLayout();
    }

    /**
     * Remove hfComponent.
     */
    public void removeHFComponent(HFComponent hfComponent) {
        if (hfComponentList.contains(hfComponent)) {
            hfComponentList.remove(hfComponent);

            //refresh layout.
            this.refreshLayout();
        }
    }

    /**
     * Move left hfComponent.
     */
    public void moveLeftHFComponent(HFComponent hfComponent) {
        int index = hfComponentList.indexOf(hfComponent);

        if (index > 0) {
            Collections.swap(hfComponentList, index - 1, index);
        }

        //refresh layout.
        this.refreshLayout();
    }

    /**
     * Move right hfComponent.
     */
    public void moveRightHFComponent(HFComponent hfComponent) {
        int index = hfComponentList.indexOf(hfComponent);

        if (index < hfComponentList.size() - 1) {
            Collections.swap(hfComponentList, index, index + 1);
        }

        //refresh layout.
        this.refreshLayout();
    }

    /**
     * Return preferredsize.
     */
    @Override
	public Dimension getPreferredSize() {
        if (hfComponentList.size() <= 0) {
            return super.getPreferredSize();
        }

        int componentWidth = 0;
        int componentHeight = 0;

        int lineNumber = 0;
        int maxComponentCount = 0;

        int tmpCompCount = 0;
        for (int i = 0; i < hfComponentList.size(); i++) {
            HFComponent tmpHFComponent = (HFComponent) hfComponentList.get(i);

            if (i == 0) { //caculate size.
                componentWidth = tmpHFComponent.getPreferredSize().width;
                componentHeight = tmpHFComponent.getPreferredSize().height;
            }

            tmpCompCount++;

            if (tmpHFComponent.getHFElement().getClass().equals(NewLineHFElement.class)) {
                maxComponentCount = Math.max(maxComponentCount, tmpCompCount);
                lineNumber++;

                tmpCompCount = 0;
            }
        }

        maxComponentCount = Math.max(maxComponentCount, tmpCompCount);
        lineNumber++;

        return new Dimension((componentWidth + HOR_GAP) * maxComponentCount + 3,
                (componentHeight + VER_GAP) * lineNumber);
    }

    /**
     * Move left action listener.
     */
    private ActionListener moveLeftActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            Object obj = evt.getSource();

            if (obj instanceof HFComponent) {
                moveLeftHFComponent((HFComponent) obj);
            }
        }
    };

    /**
     * Move right action listener.
     */
    private ActionListener moveRightActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            Object obj = evt.getSource();

            if (obj instanceof HFComponent) {
                moveRightHFComponent((HFComponent) obj);
            }
        }
    };

    /**
     * Delete action listener
     */
    private ActionListener deleteActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            Object obj = evt.getSource();

            if (obj instanceof HFComponent) {
                int returnVal = FineJOptionPane.showConfirmDialog(
                        SwingUtilities.getWindowAncestor(HFContainer.this),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_HF_Are_You_Sure_To_Delete_It") + "?",
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Delete"),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (returnVal == JOptionPane.OK_OPTION) {
                    removeHFComponent((HFComponent) obj);
                }
            }
        }
    };

    // --- Scrollable methods ---------------------------------------------

    /**
     * Returns the preferred size of the viewport for a view component.
     * This is implemented to do the default behavior of returning
     * the preferred size of the component.
     *
     * @return the <code>preferredSize</code> of a <code>JViewport</code>
     *         whose view is this <code>Scrollable</code>
     */
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }


    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one new row
     * or column, depending on the value of orientation.  Ideally,
     * components should handle a partially exposed row or column by
     * returning the distance required to completely expose the item.
     * <p/>
     * The default implementation of this is to simply return 10% of
     * the visible area.  Subclasses are likely to be able to provide
     * a much more reasonable value.
     *
     * @param visibleRect the view area visible within the viewport
     * @param orientation either <code>SwingConstants.VERTICAL</code> or
     *                    <code>SwingConstants.HORIZONTAL</code>
     * @param direction   less than zero to scroll up/left, greater than
     *                    zero for down/right
     * @return the "unit" increment for scrolling in the specified direction
     * @throws java.lang.IllegalArgumentException
     *          for an invalid orientation
     * @see javax.swing.JScrollBar#setUnitIncrement
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        switch (orientation) {
            case SwingConstants.VERTICAL:
                return visibleRect.height / 10;
            case SwingConstants.HORIZONTAL:
                return visibleRect.width / 10;
            default:
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }


    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one block
     * of rows or columns, depending on the value of orientation.
     * <p/>
     * The default implementation of this is to simply return the visible
     * area.  Subclasses will likely be able to provide a much more
     * reasonable value.
     *
     * @param visibleRect the view area visible within the viewport
     * @param orientation either <code>SwingConstants.VERTICAL</code> or
     *                    <code>SwingConstants.HORIZONTAL</code>
     * @param direction   less than zero to scroll up/left, greater than zero
     *                    for down/right
     * @return the "block" increment for scrolling in the specified direction
     * @throws java.lang.IllegalArgumentException
     *          for an invalid orientation
     * @see javax.swing.JScrollBar#setBlockIncrement
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        switch (orientation) {
            case SwingConstants.VERTICAL:
                return visibleRect.height;
            case SwingConstants.HORIZONTAL:
                return visibleRect.width;
            default:
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }


    /**
     * Returns true if a viewport should always force the width of this
     * <code>Scrollable</code> to match the width of the viewport.
     * For example a normal text view that supported line wrapping
     * would return true here, since it would be undesirable for
     * wrapped lines to disappear beyond the right
     * edge of the viewport.  Note that returning true for a
     * <code>Scrollable</code> whose ancestor is a <code>JScrollPane</code>
     * effectively disables horizontal scrolling.
     * <p/>
     * Scrolling containers, like <code>JViewport</code>,
     * will use this method each time they are validated.
     *
     * @return true if a viewport should force the <code>Scrollable</code>s
     *         width to match its own
     */
    public boolean getScrollableTracksViewportWidth() {
        if (getParent() instanceof JViewport) {
            return ((getParent()).getWidth() > getPreferredSize().width);
        }
        return false;
    }

    /**
     * Returns true if a viewport should always force the height of this
     * <code>Scrollable</code> to match the height of the viewport.
     * For example a columnar text view that flowed text in left to
     * right columns could effectively disable vertical scrolling by
     * returning true here.
     * <p/>
     * Scrolling containers, like <code>JViewport</code>,
     * will use this method each time they are validated.
     *
     * @return true if a viewport should force the Scrollables height
     *         to match its own
     */
    public boolean getScrollableTracksViewportHeight() {
        if (getParent() instanceof JViewport) {
            return ((getParent()).getHeight() > getPreferredSize().height);
        }
        return false;
    }
}
