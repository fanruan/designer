package com.fr.design.mainframe.alphafine.cell.render;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.cell.model.BottomModel;

import javax.swing.*;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Created by alex.sung on 2018/8/3.
 */
public class BottomCellRender implements ListCellRenderer<Object> {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        UILabel goToWebLabel = new UILabel();
        BottomModel bottomModel = (BottomModel) value;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(null);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel line = new JPanel();
        line.setPreferredSize(new Dimension(200, 1));
        line.setBackground(AlphaFineConstants.GRAY);
        panel.add(line, BorderLayout.NORTH);

        goToWebLabel.setFont(AlphaFineConstants.MEDIUM_FONT);
        goToWebLabel.setText(bottomModel.getGoToWeb());
        goToWebLabel.setForeground(AlphaFineConstants.BLUE);
        goToWebLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        panel.add(goToWebLabel, BorderLayout.EAST);

        panel.setPreferredSize(new Dimension(list.getFixedCellWidth(), AlphaFineConstants.CELL_TITLE_HEIGHT));
        return panel;
    }
}
