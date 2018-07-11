package com.fr.design.mainframe.alphafine.cell.render;

import com.bulenkov.iconloader.IconLoader;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class ContentCellRender implements ListCellRenderer<Object> {
    private static final int OFFSET = 45;
    private static final String SELECTED_PATH = AlphaFineConstants.IMAGE_URL + "selected";
    private static final String CELL_PATH = AlphaFineConstants.IMAGE_URL + "alphafine";
    private static final String SUFFIX = ".png";

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        UILabel titleLabel = new UILabel();
        UILabel detailLabel = new UILabel();
        if (value instanceof MoreModel) {
            return new TitleCellRender().getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
        AlphaCellModel model = (AlphaCellModel) value;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(null);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        titleLabel.setText("  " + model.getName());
        if (model.hasAction()) {
            if (isSelected) {
                String iconUrl = SELECTED_PATH + model.getType().getTypeValue() + SUFFIX;
                panel.setBackground(AlphaFineConstants.BLUE);
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setIcon(IconLoader.getIcon(iconUrl));
            } else {
                String iconUrl = CELL_PATH + model.getType().getTypeValue() + SUFFIX;
                titleLabel.setIcon(IconLoader.getIcon(iconUrl));
                titleLabel.setForeground(AlphaFineConstants.BLACK);
            }

        } else {
            titleLabel.setIcon(null);
            titleLabel.setForeground(AlphaFineConstants.MEDIUM_GRAY);
        }
        titleLabel.setFont(AlphaFineConstants.MEDIUM_FONT);
        String description = model.getDescription();
        if (StringUtils.isNotBlank(description)) {
            detailLabel.setText("-" + description);
            detailLabel.setForeground(AlphaFineConstants.LIGHT_GRAY);
            panel.add(detailLabel, BorderLayout.CENTER);
            int width = (int) (titleLabel.getPreferredSize().getWidth() + detailLabel.getPreferredSize().getWidth());
            if (width > AlphaFineConstants.LEFT_WIDTH - OFFSET) {
                int nameWidth = (int) (AlphaFineConstants.LEFT_WIDTH - detailLabel.getPreferredSize().getWidth() - OFFSET);
                titleLabel.setPreferredSize(new Dimension(nameWidth, AlphaFineConstants.CELL_HEIGHT));
            }
        } else {
            titleLabel.setPreferredSize(new Dimension(AlphaFineConstants.LEFT_WIDTH - OFFSET, AlphaFineConstants.CELL_HEIGHT));
        }

        panel.add(titleLabel, BorderLayout.WEST);
        panel.setPreferredSize(new Dimension(list.getFixedCellWidth(), AlphaFineConstants.CELL_HEIGHT));
        return panel;
    }
}
