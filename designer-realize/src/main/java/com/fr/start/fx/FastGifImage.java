package com.fr.start.fx;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;

import javax.imageio.stream.FileImageInputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * 边加载边播放的gif加载器
 *
 * @author daniel
 */
public class FastGifImage extends WritableImage {
    private String url;
    private int gifCount;

    public FastGifImage(String url, int w, int h) {
        super(w, h);
        this.url = validateUrl(url);
        seekCount();
        initialize();
    }

    /**
     * 给出gif帧数，加快加载速度
     *
     * @param url      gif url
     * @param gifCount gif帧数
     * @param w        宽
     * @param h        高
     */
    public FastGifImage(String url, int gifCount, int w, int h) {
        super(w, h);
        this.url = validateUrl(url);
        this.gifCount = gifCount;
        initialize();
    }

    private void seekCount() {
        try {
            GIFImageReaderSpi spi = new GIFImageReaderSpi();
            GIFImageReader gifReader = (GIFImageReader) spi.createReaderInstance();
            gifReader.setInput(new FileImageInputStream(new File(new URI(url))));
            gifCount = gifReader.getNumImages(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Pattern URL_QUICKMATCH = Pattern.compile("^\\p{Alpha}[\\p{Alnum}+.-]*:.*$");

    private static String validateUrl(final String url) {
        if (url == null) {
            throw new NullPointerException("URL must not be null");
        }

        if (url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL must not be empty");
        }

        try {
            if (!URL_QUICKMATCH.matcher(url).matches()) {
                final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                URL resource;
                if (url.charAt(0) == '/') {
                    resource = contextClassLoader.getResource(url.substring(1));
                } else {
                    resource = contextClassLoader.getResource(url);
                }
                if (resource == null) {
                    throw new IllegalArgumentException("Invalid URL or resource not found");
                }
                return resource.toString();
            }
            // Use URL constructor for validation
            return new URL(url).toString();
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid URL" + e.getMessage());
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL" + e.getMessage());
        }
    }

    private void finishImage(ImageLoader loader) {
        initializeAnimatedImage(loader);
    }

    // Generates the animation Timeline for multiframe images.
    private void initializeAnimatedImage(ImageLoader loader) {

        animation = new Animation(this, loader);
        animation.start();
    }

    // Support for animated images.
    private Animation animation;

    private static final class Animation {
        final WeakReference<FastGifImage> imageRef;
        final Timeline timeline;
        private ImageLoader loader;

        public Animation(final FastGifImage image, final ImageLoader loader) {
            this.loader = loader;
            imageRef = new WeakReference<FastGifImage>(image);
            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);

            final int frameCount = loader.getFrameCount();
            int duration = 0;

            for (int i = 0; i < frameCount; ++i) {
                addKeyFrame(i, duration);
                duration = duration + loader.getFrameDelay(i);
            }

            // Note: we need one extra frame in the timeline to define how long
            // the last frame is shown, the wrap around is "instantaneous"
            addKeyFrame(0, duration);
        }

        public void start() {
            timeline.play();
        }

        public void stop() {
            timeline.stop();
            loader = null;
        }

        private void updateImage(final int frameIndex) {
            final FastGifImage image = imageRef.get();
            if (image != null) {
                image.setPlatformImagePropertyImpl(
                        loader.getFrame(frameIndex));
            } else {
                timeline.stop();
            }
        }

        private void addKeyFrame(final int index, final double duration) {
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(duration),
                            new EventHandler() {
                                @Override
                                public void handle(Event event) {
                                    updateImage(index);
                                }
                            }
                    ));
        }
    }

    private static Method method;

    static {
        try {
            method = FastGifImage.class.getSuperclass().getSuperclass().getDeclaredMethod("platformImagePropertyImpl");
            method.setAccessible(true);
        } catch (Exception e) {

        }
    }

    private void setPlatformImagePropertyImpl(PlatformImage image) {
        try {
            Object o = method.invoke(this);
            Method method = o.getClass().getDeclaredMethod("set", Object.class);
            method.setAccessible(true);
            method.invoke(o, image);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    private void initialize() {
        finishImage(new PrismImageLoader2(url, gifCount, (int) getRequestedWidth(), (int) getRequestedHeight(), isPreserveRatio(), isSmooth()));
    }

    /**
     * 销毁gif动画
     */
    public void destroy() {
        animation.stop();
    }

}
