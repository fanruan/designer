package com.fr.design.mainframe.alphafine.previewPane;

import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/5/2.
 */
public class FilePreviewPane extends JPanel {

    public FilePreviewPane(Image image) {
        UILabel label = new UILabel();
        label.setOpaque(true);
        label.setBackground(Color.white);
        float scale = image.getWidth(null) / 380;
        image = image.getScaledInstance(380, (int) (image.getHeight(null) / scale), Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(image));
        add(label);
    }
}

