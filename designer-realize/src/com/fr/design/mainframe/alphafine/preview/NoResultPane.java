package com.fr.design.mainframe.alphafine.preview;

import com.bulenkov.iconloader.IconLoader;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.general.FRFont;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/8/14.
 */
public class NoResultPane extends JPanel {
    public NoResultPane(String title, String iconUrl) {
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setPreferredSize(new Dimension(AlphaFineConstants.LEFT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT));
        UILabel image = new UILabel();
        image.setPreferredSize(new Dimension(150, 111));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setIcon(IconLoader.getIcon(iconUrl));
        image.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        UILabel description = new UILabel(title);
        description.setForeground(AlphaFineConstants.MEDIUM_GRAY);
        description.setFont(FRFont.getInstance("SimSun", Font.PLAIN, 14));
        description.setBorder(BorderFactory.createEmptyBorder(0, 0, 135, 0));
        description.setHorizontalAlignment(SwingConstants.CENTER);
        add(image, BorderLayout.CENTER);
        add(description, BorderLayout.SOUTH);
    }
}
