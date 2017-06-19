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
    private UILabel name;
    private UILabel more;

    public TitleCellRender() {
        this.name = new UILabel();
        this.more = new UILabel();
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        MoreModel moreModel = (MoreModel) value;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AlphaFineConstants.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        name.setText(moreModel.getName());
        name.setFont(AlphaFineConstants.SMALL_FONT);
        more.setFont(AlphaFineConstants.SMALL_FONT);
        more.setText(moreModel.getContent());
        name.setForeground(AlphaFineConstants.DARK_GRAY);
        more.setForeground(AlphaFineConstants.DARK_GRAY);
        panel.add(name, BorderLayout.WEST);
        if (moreModel.isNeedMore()) {
            this.more.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            panel.add(this.more, BorderLayout.EAST);
        }
        if (moreModel.isLoading()) {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/com/fr/design/mainframe/alphafine/images/loading.gif"));

            //设置cell的加载动画
            imageIcon.setImageObserver(list);
            UILabel loadingLabel = new UILabel(imageIcon);
            panel.add(loadingLabel, BorderLayout.SOUTH);
        }
        panel.setPreferredSize(new Dimension((int) panel.getPreferredSize().getWidth(), AlphaFineConstants.CELL_TITLE_HEIGHT));
        return panel;
    }
}
