package com.fr.design.mainframe.alphafine.preview;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;


import javax.swing.*;
import java.awt.*;


/**
 * Created by XiaXiang on 2017/5/2.
 */
public class PluginPreviewPane extends JPanel {
    public PluginPreviewPane(String title, Image image, String version, String jartime, CellType type, int price) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        setBackground(Color.WHITE);
        UILabel imageLabel = new UILabel();
        image = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBackground(Color.black);
        UILabel nameLabel = new UILabel(title);
        nameLabel.setBackground(Color.yellow);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel line = new JPanel();
        line.setPreferredSize(new Dimension(200, 1));
        line.setBackground(AlphaFineConstants.GRAY);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JPanel bottomPane = new JPanel(new BorderLayout());
        bottomPane.setBackground(Color.WHITE);
        bottomPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        if (type == CellType.PLUGIN) {
            UILabel versionLabel = new UILabel("V" + version);
            versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            versionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            versionLabel.setForeground(AlphaFineConstants.DARK_GRAY);
            versionLabel.setFont(AlphaFineConstants.MEDIUM_FONT);
            panel.add(versionLabel, BorderLayout.CENTER);
            UILabel jartimeLabel = new UILabel(jartime.substring(0, 10));
            jartimeLabel.setForeground(AlphaFineConstants.BLACK);
            jartimeLabel.setFont(AlphaFineConstants.MEDIUM_FONT);
            bottomPane.add(jartimeLabel, BorderLayout.EAST);
        }
        nameLabel.setFont(AlphaFineConstants.LARGE_FONT);
        nameLabel.setBackground(AlphaFineConstants.BLUE);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        line.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        String price0 = price == 0 ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Collect_Information_free") : "￥" + String.valueOf(price);
        UILabel priceLabel = new UILabel(price0);
        priceLabel.setForeground(AlphaFineConstants.RED);
        priceLabel.setFont(AlphaFineConstants.MEDIUM_FONT);
        bottomPane.add(priceLabel, BorderLayout.WEST);
        panel.add(nameLabel, BorderLayout.NORTH);
        panel.add(line, BorderLayout.SOUTH);
        nameLabel.setForeground(AlphaFineConstants.BLUE);
        add(imageLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(bottomPane, BorderLayout.SOUTH);
    }
}
