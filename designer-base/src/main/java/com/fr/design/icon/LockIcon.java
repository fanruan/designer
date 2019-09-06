package com.fr.design.icon;

import com.fr.base.BaseUtils;
import com.fr.log.FineLoggerFactory;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.ImageObserver;

public class LockIcon extends ImageIcon {
    private final static Image lockImage = BaseUtils.readImage("/com/fr/design/images/gui/locked.gif");

    private Image mainImage = null;

    transient int loadStatus = 0;
    ImageObserver imageObserver;

    protected final static Component component = new Component() {
    };
    protected final static MediaTracker tracker = new MediaTracker(component);

    int width = -1;
    int height = -1;

    public LockIcon(Image mainImage) {
        this.mainImage = mainImage;

        if (this.mainImage != null) {
            loadImage(this.mainImage);
        }
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        if (mainImage != null) {
            g.drawImage(mainImage, x, y, c);
        }
        if (lockImage != null) {
            g.drawImage(lockImage, x, y, c);
        }
    }

    /**
     * Loads the image, returning only when the image is loaded.
     *
     * @param image the image
     */
    @Override
    protected void loadImage(Image image) {
        synchronized (tracker) {
            tracker.addImage(image, 0);
            try {
                tracker.waitForID(0, 0);
            } catch (InterruptedException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }

            loadStatus = tracker.statusID(0, false);
            tracker.removeImage(image, 0);

            width = image.getWidth(imageObserver);
            height = image.getHeight(imageObserver);
        }
    }

    /**
     * Returns this icon's <code>Image</code>.
     *
     * @return the <code>Image</code> object for this <code>ImageIcon</code>
     */
    @Override
    public Image getImage() {
        return mainImage;
    }

    public LockIcon getDisabledLockIcon() {
        Image mainDisabledImage = GrayFilter.createDisabledImage(mainImage);

        return new LockIcon(mainDisabledImage);
    }

    /**
     * Gets the width of the icon.
     *
     * @return the width in pixels of this icon
     */
    @Override
    public int getIconWidth() {
        return width;
    }

    /**
     * Gets the height of the icon.
     *
     * @return the height in pixels of this icon
     */
    @Override
    public int getIconHeight() {
        return height;
    }

    {
        loadImage(lockImage);
    }
}