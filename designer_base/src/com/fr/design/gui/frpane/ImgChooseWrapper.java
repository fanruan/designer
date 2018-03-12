package com.fr.design.gui.frpane;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewer;
import com.fr.design.utils.ImageUtils;
import com.fr.stable.CoreGraphHelper;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import java.awt.Image;
import java.io.File;

/**
 * 图片选择框包装类
 * Created by zack on 2018/3/9.
 */
public class ImgChooseWrapper {
    private ImagePreviewer previewPane = null;

    private ImageFileChooser imageFileChooser = null;

    private Style imageStyle = null;

    private SwingWorker<Void, Void> imageWorker;

    public static ImgChooseWrapper getInstance(ImagePreviewer previewPane, ImageFileChooser imageFileChooser, Style imageStyle) {
        return new ImgChooseWrapper(previewPane, imageFileChooser, imageStyle);
    }

    private ImgChooseWrapper(ImagePreviewer previewPane, ImageFileChooser imageFileChooser, Style imageStyle) {
        this.previewPane = previewPane;
        this.imageFileChooser = imageFileChooser;
        this.imageStyle = imageStyle;
    }


    public void setPreviewPane(ImagePreviewer previewPane) {
        this.previewPane = previewPane;
    }

    public ImageFileChooser getImageFileChooser() {
        return imageFileChooser;
    }

    public void setImageFileChooser(ImageFileChooser imageFileChooser) {
        this.imageFileChooser = imageFileChooser;
    }

    public Style getImageStyle() {
        return imageStyle;
    }

    public void setImageStyle(Style imageStyle) {
        this.imageStyle = imageStyle;
    }

    public SwingWorker<Void, Void> getImageWorker() {
        return imageWorker;
    }

    public void setImageWorker(SwingWorker<Void, Void> imageWorker) {
        this.imageWorker = imageWorker;
    }

    public void dealWithImageFile(int returnVal) {
        if (returnVal != JFileChooser.CANCEL_OPTION) {
            File selectedFile = imageFileChooser.getSelectedFile();

            if (selectedFile != null && selectedFile.isFile()) {
                if (previewPane != null) {
                    previewPane.showLoading();
                }
                if (imageWorker != null && !imageWorker.isDone()) {
                    imageWorker = null;
                }
                imageWorker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Image image = imageFileChooser.isCompressSelected() ? ImageUtils.defaultImageCompress(selectedFile) : BaseUtils.readImage(selectedFile.getPath());
                        CoreGraphHelper.waitForImage(image);

                        if (previewPane != null) {
                            previewPane.setImageStyle(imageStyle);
                            previewPane.setImage(image);
                            previewPane.repaint();
                        }
                        return null;
                    }
                };
                imageWorker.execute();
            } else {
                if (previewPane != null) {
                    previewPane.setImage(null);
                }

            }
            if (previewPane != null) {
                previewPane.repaint();
            }
        }
    }
}
