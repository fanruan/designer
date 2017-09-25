package com.fr.design.mainframe.alphafine.cell.render;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class TitleCellRender implements ListCellRenderer<Object> {
    private static final int LOAD_OFFSET = 28;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        UILabel titleLabel = new UILabel();
        UILabel showMoreLabel = new UILabel();
        MoreModel moreModel = (MoreModel) value;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AlphaFineConstants.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        titleLabel.setText(moreModel.getName());
        titleLabel.setFont(AlphaFineConstants.SMALL_FONT);
        titleLabel.setForeground(AlphaFineConstants.DARK_GRAY);
        showMoreLabel.setFont(AlphaFineConstants.SMALL_FONT);
        showMoreLabel.setText(moreModel.getContent());
        showMoreLabel.setForeground(AlphaFineConstants.DARK_GRAY);
        panel.add(titleLabel, BorderLayout.WEST);
        if (moreModel.isNeedMore()) {
            showMoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            panel.add(showMoreLabel, BorderLayout.EAST);
        }
        panel.setPreferredSize(new Dimension(list.getFixedCellWidth(), AlphaFineConstants.CELL_TITLE_HEIGHT));
        return panel;
    }
}
