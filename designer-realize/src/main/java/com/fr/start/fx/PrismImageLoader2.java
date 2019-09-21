package com.fr.start.fx;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.log.FineLoggerFactory;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageLoadListener;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorageException;
import com.sun.javafx.iio.common.ImageTools;
import com.sun.javafx.iio.gif.GIFImageLoaderFactory;
import com.sun.javafx.tk.PlatformImage;
import com.sun.prism.Image;
import com.sun.prism.impl.PrismSettings;


import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 边加载边播放的gif加载器
 *
 * @author daniel
 */
class PrismImageLoader2 implements com.sun.javafx.tk.ImageLoader {

    private Image[] images;
    private int[] delayTimes;
    private int width;
    private int height;
    private int gifCount = 1;
    private Exception exception;

    public PrismImageLoader2(final String url, int gifCount, final int width, final int height,
                             final boolean preserveRatio, final boolean smooth) {
        this.gifCount = gifCount;
        images = new Image[gifCount];
        delayTimes = new int[gifCount];
        this.width = width;
        this.height = height;
        ExecutorService es = Executors.newSingleThreadExecutor(new NamedThreadFactory("PrismImageLoader2"));
        es.execute(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    inputStream = ImageTools.createInputStream(url);
                    loadAll(inputStream, width, height, preserveRatio, smooth);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        es.shutdown();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getFrameCount() {
        return gifCount;
    }

    @Override
    @SuppressWarnings("squid:S2142")
    public PlatformImage getFrame(int index) {
        while (images[index] == null) {
            synchronized (this) {
                if (images[index] == null) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        FineLoggerFactory.getLogger().error(e.getMessage(), e);
                    }
                }
            }
        }
        return images[index];
    }


    @Override
    public int getFrameDelay(int index) {
//        while (images[0] == null) {
//            synchronized (this) {
//                if(images[0] == null) {
//                    try {
//                        this.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return 0;
//        }
//        return delayTimes[0];
        // 直接使用第一帧的时间
        return 40;
    }

    @Override
    public int getLoopCount() {
        return 0;
    }

    @Override
    public Exception getException() {
        return exception;
    }


    @SuppressWarnings("squid:S244")
    private void loadAll(InputStream stream, int w, int h,
                         boolean preserveRatio, boolean smooth) {
        ImageLoadListener listener = new PrismLoadListener();

        try {
            ImageLoader loader = null;
            loader = GIFImageLoaderFactory.getInstance().createImageLoader(stream);
            loader.addListener(listener);

            for (int i = 0; i < gifCount; i++) {
                ImageFrame imageFrame = loader.load(i, w, h, preserveRatio, smooth);
                images[i] = convert(imageFrame);
                synchronized (this) {
                    this.notify();
                }
            }
        } catch (ImageStorageException e) {
            handleException(e);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleException(final ImageStorageException isException) {
        // unwrap ImageStorageException if possible
        final Throwable exceptionCause = isException.getCause();
        if (exceptionCause instanceof Exception) {
            handleException((Exception) exceptionCause);
        } else {
            handleException((Exception) isException);
        }
    }

    private void handleException(final Exception exception) {
        if (PrismSettings.verbose) {
            exception.printStackTrace(System.err);
        }
        this.exception = exception;
    }

    private Image convert(ImageFrame imgFrames) {
        ImageFrame frame = imgFrames;
        Image image = Image.convertImageFrame(frame);
        ImageMetadata metadata = frame.getMetadata();
        if (metadata != null) {
            Integer delay = metadata.delayTime;
            if (delay != null) {
                delayTimes[0] = delay.intValue();
            }
        }
        return image;
    }


    private class PrismLoadListener implements ImageLoadListener {
        @Override
        public void imageLoadWarning(ImageLoader loader, String message) {

        }

        @Override
        public void imageLoadProgress(ImageLoader loader,
                                      float percentageComplete) {
            // progress only matters when backgroundLoading=true, but
            // currently we are relying on AbstractRemoteResource for tracking
            // progress of the InputStream, so there's no need to implement
            // this for now; eventually though we might want to consider
            // moving away from AbstractRemoteResource and instead use
            // the built-in support for progress in the javafx-iio library...
        }

        @Override
        public void imageLoadMetaData(ImageLoader loader, ImageMetadata metadata) {
            // We currently have no need to listen for ImageMetadata ready.
        }
    }

}
