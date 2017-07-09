package com.fr.design.mainframe.alphafine.cell.render;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.general.IOUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class TitleCellRender implements ListCellRenderer<Object> {

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
        showMoreLabel.setFont(AlphaFineConstants.SMALL_FONT);
        showMoreLabel.setText(moreModel.getContent());
        titleLabel.setForeground(AlphaFineConstants.DARK_GRAY);
        showMoreLabel.setForeground(AlphaFineConstants.DARK_GRAY);
        panel.add(titleLabel, BorderLayout.WEST);
        if (moreModel.isNeedMore()) {
            showMoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            panel.add(showMoreLabel, BorderLayout.EAST);
        }
        if (moreModel.isLoading()) {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/com/fr/design/mainframe/alphafine/images/loading.gif"));
            //设置cell的加载动画
            imageIcon.setImageObserver(list);
            UILabel loadingLabel = new UILabel(imageIcon);
            panel.add(loadingLabel, BorderLayout.SOUTH);
        }
        panel.setPreferredSize(new Dimension(list.getFixedCellWidth(), AlphaFineConstants.CELL_TITLE_HEIGHT));
        return panel;
    }
}
