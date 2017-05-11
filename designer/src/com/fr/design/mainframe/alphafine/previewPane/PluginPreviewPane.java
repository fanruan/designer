package com.fr.design.mainframe.alphafine.previewPane;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;


/**
 * Created by XiaXiang on 2017/5/2.
 */
public class PluginPreviewPane extends JPanel {
    public PluginPreviewPane(String title, Image image, String version, String jartime, CellType type, int price) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(50,0,0,0));
        setBackground(Color.white);
        UILabel imageLabel = new UILabel();
        image = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBackground(Color.black);
        UILabel nameLabel = new UILabel(title);
        nameLabel.setBackground(Color.yellow);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel line = new JPanel();
        line.setPreferredSize(new Dimension(200,1));
        line.setBackground(new Color(0xd2d2d2));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        JPanel bottomPane = new JPanel(new BorderLayout());
        bottomPane.setBackground(Color.white);
        bottomPane.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        if (type == CellType.PLUGIN) {
            UILabel versionLabel = new UILabel("V" + version);
            versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            versionLabel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
            versionLabel.setForeground(new Color(0x666666));
            versionLabel.setFont(new Font("Song_TypeFace",0,12));
            panel.add(versionLabel, BorderLayout.CENTER);
            UILabel jartimeLabel = new UILabel(jartime.substring(0, 10));
            jartimeLabel.setForeground(new Color(0x222222));
            jartimeLabel.setFont(new Font("Song_TypeFace",0,12));
            bottomPane.add(jartimeLabel, BorderLayout.EAST);
        }
        nameLabel.setFont(new Font("Song_TypeFace",0,18));
        nameLabel.setBackground(new Color(0x3394f0));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));
        line.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        String price0 = price == 0 ? Inter.getLocText("FR-Designer-Collect_Information_free") : String.valueOf(price);
        UILabel priceLabel = new UILabel(price0);
        priceLabel.setForeground(new Color(0xf46c4c));
        priceLabel.setFont(new Font("Song_TypeFace",0,10));
        bottomPane.add(priceLabel, BorderLayout.WEST);
        panel.add(nameLabel, BorderLayout.NORTH);
        panel.add(line, BorderLayout.SOUTH);
        nameLabel.setForeground(new Color(0x3394f0));
        add(imageLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(bottomPane, BorderLayout.SOUTH);
    }
}
