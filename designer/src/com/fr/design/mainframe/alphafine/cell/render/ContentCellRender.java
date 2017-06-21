package com.fr.design.mainframe.alphafine.cell.render;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.general.IOUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class ContentCellRender implements ListCellRenderer<Object> {


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
        if (isSelected && !model.hasNoResult()) {
            panel.setBackground(AlphaFineConstants.BLUE);
        }
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        titleLabel.setText("  " + model.getName());
        String iconUrl = "/com/fr/design/mainframe/alphafine/images/alphafine" + model.getType().getTypeValue() + ".png";
        if (model.hasNoResult()) {
            titleLabel.setIcon(null);
            titleLabel.setForeground(AlphaFineConstants.MEDIUM_GRAY);
        } else {
            titleLabel.setIcon(new ImageIcon(IOUtils.readImage(iconUrl)));
            titleLabel.setForeground(AlphaFineConstants.BLACK);
        }
        titleLabel.setFont(AlphaFineConstants.MEDIUM_FONT);
        String description = model.getDescription();
        if (StringUtils.isNotBlank(description)) {
            detailLabel.setText("-" + description);
            detailLabel.setForeground(AlphaFineConstants.LIGHT_GRAY);
            panel.add(detailLabel, BorderLayout.CENTER);
            int width = (int) (titleLabel.getPreferredSize().getWidth() + detailLabel.getPreferredSize().getWidth());
            if ( width > AlphaFineConstants.LEFT_WIDTH - 30) {
                int nameWidth = (int) (AlphaFineConstants.LEFT_WIDTH - detailLabel.getPreferredSize().getWidth() - 45);
                titleLabel.setPreferredSize(new Dimension(nameWidth, AlphaFineConstants.CELL_HEIGHT));
            }
        }

        panel.add(titleLabel, BorderLayout.WEST);
        panel.setPreferredSize(new Dimension(list.getFixedCellWidth(), AlphaFineConstants.CELL_HEIGHT));
        return panel;
    }
}
