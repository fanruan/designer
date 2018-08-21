package com.fr.design.mainframe.alphafine.preview;

import com.bulenkov.iconloader.IconLoader;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

/**
 * @Author alex.sung
 * created by 2018.08.15
 */
public class ContainsCirclePane extends JPanel {

    public ContainsCirclePane(int pngIndex) {
        UILabel iconLabel = new UILabel(IconLoader.getIcon(AlphaFineConstants.IMAGE_URL + AlphaFineConstants.ALPHA_HOT_IMAGE_NAME + pngIndex + ".png"));
        iconLabel.setPreferredSize(AlphaFineConstants.HOT_ICON_LABEL_SIZE);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(Color.WHITE);
        add(iconLabel);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int height = AlphaFineConstants.HOT_ICON_LABEL_HEIGHT;
        setBackground(Color.white);
        int x0 = getSize().width / 2;
        int y0 = height / 2 + 23;
        int r = height / 2 + 9;
        g.setColor(AlphaFineConstants.LIGHT_GRAY);
        g.drawOval(x0 - r, y0 - r, r * 2, r * 2);
    }
}
