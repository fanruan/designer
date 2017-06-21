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
    private UILabel name;
    private UILabel content;

    public ContentCellRender() {

    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        this.name = new UILabel();
        this.content = new UILabel();
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
        name.setText("  " + model.getName());
        String iconUrl = "/com/fr/design/mainframe/alphafine/images/alphafine" + model.getType().getTypeValue() + ".png";
        if (model.hasNoResult()) {
            name.setIcon(null);
            name.setForeground(AlphaFineConstants.MEDIUM_GRAY);
        } else {
            name.setIcon(new ImageIcon(IOUtils.readImage(iconUrl)));
            name.setForeground(AlphaFineConstants.BLACK);
        }
        name.setFont(AlphaFineConstants.MEDIUM_FONT);
        String description = model.getDescription();
        if (StringUtils.isNotBlank(description)) {
            content.setText("-" + description);
            content.setForeground(AlphaFineConstants.LIGHT_GRAY);
            panel.add(content, BorderLayout.CENTER);
            int width = (int) (name.getPreferredSize().getWidth() + content.getPreferredSize().getWidth());
            if ( width > AlphaFineConstants.LEFT_WIDTH - 30) {
                int nameWidth = (int) (AlphaFineConstants.LEFT_WIDTH - content.getPreferredSize().getWidth() - 45);
                name.setPreferredSize(new Dimension(nameWidth, AlphaFineConstants.CELL_HEIGHT));
            }
        }

        panel.add(name, BorderLayout.WEST);
        panel.setPreferredSize(new Dimension(list.getFixedCellWidth(), AlphaFineConstants.CELL_HEIGHT));
        return panel;
    }
}
