package com.fr.design.mainframe.alphafine.preview;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/8/14.
 */
public class NoResultPane extends JPanel {
    private static final Font MEDIUM_FONT = new Font("Song_TypeFace", 0, 14);
    public NoResultPane(String title, Icon icon) {
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setPreferredSize(new Dimension(AlphaFineConstants.LEFT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT));
        UILabel image = new UILabel();
        image.setPreferredSize(new Dimension(150, 111));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setIcon(icon);
        image.setBorder(BorderFactory.createEmptyBorder(100,0,0,0));
        UILabel description = new UILabel(title);
        description.setForeground(AlphaFineConstants.MEDIUM_GRAY);
        description.setFont(MEDIUM_FONT);
        description.setBorder(BorderFactory.createEmptyBorder(0, 0, 135, 0));
        description.setHorizontalAlignment(SwingConstants.CENTER);
        add(image, BorderLayout.CENTER);
        add(description, BorderLayout.SOUTH);
    }
}
