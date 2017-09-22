package com.fr.design.mainframe.alphafine.preview;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by XiaXiang on 2017/5/2.
 */
public class FilePreviewPane extends JPanel {

    public FilePreviewPane(BufferedImage image) {
        setLayout(new BorderLayout());
        UILabel label = new UILabel();
        float widthScale = (AlphaFineConstants.RIGHT_WIDTH) / (float) image.getWidth();
        float heightScale = (AlphaFineConstants.CONTENT_HEIGHT) / (float) image.getHeight();
        Image showImage;
        showImage = widthScale > heightScale ? image.getScaledInstance((int) (image.getWidth() * heightScale), AlphaFineConstants.CONTENT_HEIGHT, Image.SCALE_SMOOTH) : image.getScaledInstance(AlphaFineConstants.RIGHT_WIDTH, (int) (image.getWidth() * widthScale), Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(showImage));
        add(label);
    }
}

